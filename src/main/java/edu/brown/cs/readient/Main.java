package edu.brown.cs.readient;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.brown.cs.db.QueryManager;
import edu.brown.cs.parsing.ArticleParser;
import edu.brown.cs.stats.Readability;
import edu.brown.cs.stats.StatsGenerator;
import edu.brown.cs.stats.StatsGenerator.Stats;
import edu.stanford.nlp.util.Pair;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

public final class Main {
  public static void main(String[] args) {
    (new Main(args)).run();
  }

  private static final int LOGIN_ARGS = 2;
  private static final int SIGNUP_ARGS = 4;
  private static final int GUI_ARGS = 0;
  private int port = 8080;
  private static final Gson CMND_GSON = new GsonBuilder().setPrettyPrinting()
      .create();
  private static final Gson GUI_GSON = new GsonBuilder()
      .excludeFieldsWithoutExposeAnnotation().create();
  private static final String DB = "data.db";
  private QueryManager manager;
  private StatsGenerator sg;
  private HashSet<String> usernames;
  private Profile profile = null; // profile for gui
  private String[] args;

  public Main(String[] a) {
    args = a;
  }

  public void run() {
    OptionParser parser = new OptionParser();
    parser.accepts("signup");
    parser.accepts("login");
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class);

    OptionSet options = parser.parse(args);
    @SuppressWarnings("unchecked")
    List<String> arguments = (List<String>) options.nonOptionArguments();
    try {
      manager = new QueryManager(DB);
      sg = new StatsGenerator();
      usernames = manager.getUserNames();
      if (options.has("gui")) {
        if (options.has("port")) { // check if the --port tack was called
          if ((Integer) options.valueOf("port") == null) {
            // if there is no integer after the port tag, then it is a super
            // problem
            System.out.println("ERROR: must specify port");
            return;
          }
          // set the port
          port = (Integer) options.valueOf("port");
        }

        // figure out if the inputs are within the range of the --gui input
        // arguments
        if (arguments.size() < GUI_ARGS
            || (arguments.size() > GUI_ARGS && !options.has("port"))
            || (arguments.size() > GUI_ARGS + 1 && options.has("port"))) {
          System.out
              .println("ERROR: usage: ./readient --gui " + "--port <number>");
          return;
        }

        runSparkServer();
      } else if (options.has("login")) {
        if (arguments.size() != LOGIN_ARGS) {
          throw new IllegalArgumentException(
              "usage: ./readient --login <username> <passoword>");
        }
        sg = new StatsGenerator();
        Profile profile = getProfile(arguments.get(0), arguments.get(1));
        cmdLine(profile);
      } else if (options.has("signup")) {
        if (arguments.size() != SIGNUP_ARGS) {
          throw new IllegalArgumentException(
              "usage: ./readient --signup <username> "
                  + "<passoword> <first_name> <last_name>");
        }
        sg = new StatsGenerator();
        manager.addUser(arguments.get(0), arguments.get(1), arguments.get(2),
            arguments.get(3));
        Profile profile = getProfile(arguments.get(0), arguments.get(1));
        cmdLine(profile);
      } else {
        printRunHelp();
      }
    } catch (Exception e) {
      // catches every the Exception given
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  private void runSparkServer() {
    Spark.setPort(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());
    FreeMarkerEngine marker = createEngine();

    Spark.get("/signin", (req, res) -> {
      String s = req.session().attribute("username");
      if (s != null) {
        res.redirect("/home");
      }
      Map<String, Object> variables = ImmutableMap.of("title",
          "Home | Readient");
      res.cookie("username", "Harry", 1000000);
      return new ModelAndView(variables, "signin.ftl");
    }, marker);

    Spark.post("/logout", (req, res) -> {
      JsonObject obj = new JsonObject();
      obj.addProperty("logout", false);
      Session s = req.session();
      s.removeAttribute("username");
      return GUI_GSON.toJson(obj);
    });

    Spark.get("/home", (req, res) -> {

      String s = req.session().attribute("username");
      if (s == null) {
        res.redirect("/signin");
      }
      Map<String, Object> variables = ImmutableMap.of("title",
          "Home | Readient", "username", s);
      System.out.println("/home");
      return new ModelAndView(variables, "home.ftl");
    }, marker);

    Spark.get("/confirmation", (req, res) -> {

      String s = req.session().attribute("username");
      if (s != null) {
        res.redirect("/home");
      }
      System.out.println("djadfds");
      Map<String, Object> variables = ImmutableMap.of("title",
          "Confirmation");
      return new ModelAndView(variables, "confirmation.ftl");
    }, marker);

    Spark.get("/", (req, res) -> {

      res.redirect("/home");
      Map<String, Object> variables = ImmutableMap.of("title",
          "Home | Readient");
      return new ModelAndView(variables, "home.ftl");
    }, marker);

    Spark.get("/signup", (req, res) -> {
      String s = req.session().attribute("username");
      if (s != null) {
        res.redirect("/home");
      }
      Map<String, Object> variables = ImmutableMap.of("title",
          "Home | Readient");
      return new ModelAndView(variables, "signup.ftl");
    }, marker);

    Spark.post("/exists", (req, res) -> {
      JsonObject obj = new JsonObject();
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      if (usernames.contains(username)) {
        obj.addProperty("isUserName", true);
        return GUI_GSON.toJson(obj);
      } else {
        obj.addProperty("isUserName", false);
        return GUI_GSON.toJson(obj);
      }
    });

    Spark.post("/login", (req, res) -> {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      String password = qm.value("password");
      System.out.println(password);
      try {
        profile = getProfile(username, password);
        if (profile == null) {
          JsonObject obj = new JsonObject();
          System.out.println("mmee");
          obj.addProperty("error", "profile doesn't exist");
          return GUI_GSON.toJson(obj);

        } else {

          final Profile p = profile;
          Session s = req.session();
          s.attribute("username", username);
          System.out.println(req.session().id());
          return GUI_GSON.toJson(GUI_GSON.toJson(profileJson(p)));
        }
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      return GUI_GSON.toJson(new JsonObject());

    });

    Spark.post("/create", (req, res) -> {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      System.out.println(username);
      String password = qm.value("password");
      String[] name = qm.value("name").split(" ");
      try {
        manager.addUser(username, password, name[0],
            name.length == 1 ? "" : name[1]);
        profile = getProfile(username, password);
        final Profile p = profile;
        System.out.println(username);
        return GUI_GSON.toJson(profileJson(p));
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      return GUI_GSON.toJson(new JsonObject());
    });

    Spark.post("/profile", (req, res) -> {
      final Profile p = profile;
      return GUI_GSON.toJson(profileJson(p));
    });

    Spark.post("/article/a/:aid", (req, res) -> {
      String artID = req.params(":aid").replace('.', '/');
      final Profile p = profile;
      if (p.containsArticle(artID)) {
        return GUI_GSON.toJson(p.getArticle(artID));
      } else {
        return GUI_GSON.toJson(new JsonObject());
      }
    });

    Spark.post("/add", (req, res) -> {
      QueryParamsMap qm = req.queryMap();
      String url = qm.value("url");
      Integer rank = qm.value("rank") == null ? null
          : Integer.parseInt(qm.value("rank"));
      try {
        Pair<Profile, Article> result = addArticle(profile, url, rank);
        profile = result.first();
        return GUI_GSON.toJson(result.second());
      } catch (SQLException e) {
        System.out.println(
            "Article could not be added to the databse :( " + e.getMessage());
      }
      return GUI_GSON.toJson(new JsonObject());
    });

    Spark.post("/remove", (req, res) -> {
      QueryParamsMap qm = req.queryMap();
      String in = qm.value("articles");
      JsonArray arts = GUI_GSON.fromJson(in, JsonArray.class);
      for (JsonElement obj : arts) {
        if (profile.containsArticle(obj.getAsString())) {
          removeArticle(obj.getAsString(), profile);
        }
      }
      return GUI_GSON.toJson(profileJson(profile));
    });
  }

  private void cmdLine(Profile p) {
    Profile prof = p;
    Scanner s = new Scanner(System.in);
    System.out.println("Welcome to Readient!");
    System.out.println("Available commands: ");
    printHelp();
    System.out.println();
    System.out.print("readient> ");
    while (s.hasNext()) {
      String in = s.nextLine();
      String[] line = in.trim().split(" ");
      if (line[0].equals("logout")) {
        break;
      } else if (in.isEmpty()) {
        continue;
      } else if (line[0].equals("profile")) {
        System.out.println(CMND_GSON.toJson(profileJson(prof)));
      } else if (line[0].equals("info")) {
        System.out.println(CMND_GSON.toJson(userJson(prof.getUser())));
      } else if (line[0].equals("add")) {
        if (line.length < 2) {
          printHelp();
        } else if (line.length == 2) {
          try {
            Pair<Profile, Article> res = addArticle(prof, line[1], null);
            prof = res.first();
            System.out.println(CMND_GSON.toJson(res.second()));
          } catch (SQLException e) {
            System.out.println("Article could not be added to the databse :( "
                + e.getMessage());
          }
        } else if (line.length == 3) {
          try {
            int rank = Integer.parseInt(line[2]);
            if (rank != 0 || rank != 1) {
              System.out
                  .println("Rank should be 1 for like " + "and 0 for not like");
            } else {
              Pair<Profile, Article> res = addArticle(prof, line[1], rank);
              prof = res.first();
              System.out.println(CMND_GSON.toJson(res.second()));
            }
          } catch (SQLException e) {
            System.out.println("Article could not be added to the databse :( "
                + e.getMessage());
          } catch (NumberFormatException e) {
            System.out.println("That wasn't a number, was it...");
          }
        } else {
          printHelp();
        }
      } else if (line[0].equals("get")) {
        if (line.length == 2) {
          if (prof.containsArticle(line[1])) {
            System.out.println(CMND_GSON.toJson(prof.getArticle(line[1])));
          } else {
            System.out.println("Article does not exist");
          }
        } else {
          printHelp();
        }
      } else if (line[0].equals("remove")) {
        if (line.length == 2) {
          if (prof.containsArticle(line[1])) {
            removeArticle(line[1], prof);
            System.out.println("Article has been removed!");
          } else {
            System.out.println("Article does not exist");
          }
        } else {
          printHelp();
        }
      } else if (line[0].equals("help")) {
        printHelp();
      } else {
        System.out.println("command not found :(");
      }
      System.out.print("readient> ");
    }
    s.close();
  }

  private synchronized Pair<Profile, Article> addArticle(Profile prof,
      String url, Integer rank) throws SQLException {
    ArticleParser p = new ArticleParser(url);
    Stats stats = StatsGenerator.analyze(p.iterator());
    String id = manager.addArticle(p.title(), p.url(),
        prof.getUser().getUsername(), rank, stats.words());
    Map<String, Double> emotions = sg.moods(p, stats);
    manager.addMoods(id, emotions);
    List<Integer> sent = sg.sentiment(p, stats);
    manager.addSentiments(id, sent);
    String topic = sg.topic(p);
    manager.addTopic(id, topic);
    Readability read = new Readability(stats);
    manager.addReadLevel(id, read.avgRead(), read.avgGrade());
    Article art = new Article(id, p.title(), p.url(),
        prof.getUser().getUsername(), rank, read.avgRead(), read.avgGrade());
    art.setMood(emotions);
    List<String> topics = new ArrayList<>();
    topics.add(topic);
    art.setTopics(topics);
    art.setSentiments(sent);
    prof.addArticle(art);
    prof.setAvgReadLevel(manager.avgReadLevel(prof.getUser().getUsername()));
    prof.setWordsRead(manager.wordsRead(prof.getUser().getUsername()));
    getAvgs(prof);
    return new Pair<>(prof, art);
  }

  private synchronized Profile removeArticle(String artID, Profile prof) {
    try {
      manager.removeArticle(artID);
    } catch (SQLException e) {
      System.out.println("Article could not be removed from the database");
    }
    prof.removeArticle(artID);
    getAvgs(prof);
    return prof;
  }

  private synchronized void getAvgs(Profile prof) {
    try {
      prof.setAvgReadLevel(manager.avgReadLevel(prof.getUser().getUsername()));
      prof.setWordsRead(manager.wordsRead(prof.getUser().getUsername()));
    } catch (SQLException e) {
      System.out.println("Unable to connect to the database");
    }
  }

  private synchronized Profile getProfile(String username, String password) {
    System.out.println("hi" + username);
    System.out.println("hi" + password);
    User user;
    try {
      user = manager.getUser(username, password);
    } catch (SQLException e) {
      System.out.println("llllll");
      return null;
    }
    System.out.println("bbbbb");
    Profile profile;
    try {
      profile = new Profile(user,
          manager.getArticles(user.getUsername()));
    } catch (SQLException e) {
      return null;
    }
    return profile;
  }

  private static JsonObject profileJson(Profile p) {
    JsonObject json = userJson(p.getUser());
    JsonArray art = new JsonArray();
    for (Article a : p.getArticles()) {
      JsonObject obj = new JsonObject();
      obj.addProperty("id", a.getId());
      obj.addProperty("title", a.getTitle());
      art.add(obj);
    }
    json.add("articles", art);
    return json;
  }

  private static JsonObject userJson(User user) {
    JsonObject json = new JsonObject();
    json.addProperty("username", user.getUsername());
    json.addProperty("name", user.getName());
    return json;
  }

  /**
   * Creates ftl files.
   *
   * @return a freemarker engine
   */
  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File(
        "../readient/src/main/resources/template/freemarker");
    Spark.exception(Exception.class, new ExceptionPrinter());
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * The printer class for the Exceptions in the bacon GUI.
   *
   * @author sarasolano
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  private static void printRunHelp() {
    System.out.println("--help: prints this message\n"
        + "--login: logs in a user into Readient\n"
        + "--signup: signs a user into Readient and logs them in\n");
  }

  private static void printHelp() {
    System.out.println("\thelp: prints this message\n"
        + "\tprofile: prints the while user profile\n"
        + "\tinfo: prints the user info\n"
        + "\tadd <url>: adds an article to the user's profile\n"
        + "\tadd <url> <rank>: adds an article to the user's profile with "
        + "a rank of 1 for like and 0 for unlike\n"
        + "\tremove <art_id>: removes an id form the user's profile\n"
        + "\tget <art_id>: gets the info for the given article\n"
        + "\tlogout: logs out of Readient :(");
  }
}
