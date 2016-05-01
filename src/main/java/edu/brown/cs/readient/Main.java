package edu.brown.cs.readient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.brown.cs.db.QueryManager;
import edu.brown.cs.parsing.ArticleParser;
import edu.brown.cs.stats.Readability;
import edu.brown.cs.stats.StatsGenerator;
import edu.brown.cs.stats.StatsGenerator.Stats;
import edu.stanford.nlp.util.Pair;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public final class Main {
  public static void main(String[] args) {
    (new Main(args)).run();
  }

  private static final int LOGIN_ARGS = 2;
  private static final int SIGNUP_ARGS = 4;
  private static final Gson GSON =
      new GsonBuilder().setPrettyPrinting().create();
  private static final String DB = "data.db";
  private QueryManager manager;
  private StatsGenerator sg;

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
      if (options.has("gui")) {
        System.out.println("Sorry... this is invalid :( ");
      } else if (options.has("login")) {
        if (arguments.size() != LOGIN_ARGS) {
          throw new IllegalArgumentException(
              "usage: ./readient <username> <passoword>");
        }
        sg = new StatsGenerator();
        manager = new QueryManager(DB);
        Profile profile = getProfile(arguments.get(0), arguments.get(1));
        cmdLine(profile);
      } else if (options.has("signup")) {
        if (arguments.size() != SIGNUP_ARGS) {
          throw new IllegalArgumentException("usage: ./readient <username> "
              + "<passoword> <first_name> <last_name>");
        }
        sg = new StatsGenerator();
        manager = new QueryManager(DB);
        manager.addUser(arguments.get(0), arguments.get(1), arguments.get(2),
            arguments.get(3));
        Profile profile = getProfile(arguments.get(0), arguments.get(1));
        cmdLine(profile);
      } else {
        printRunHelp();
      }
    } catch (Exception e) {
      // catches every the Exception given by the query manager
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  private void cmdLine(Profile p) {
    Profile prof = p;
    Scanner s = new Scanner(System.in);
    System.out.print("readient> ");
    while (s.hasNext()) {
      String in = s.nextLine();
      String[] line = in.trim().split(" ");
      if (line[0].equals("logout")) {
        break;
      } else if (in.isEmpty()) {
        continue;
      } else if (line[0].equals("profile")) {
        System.out.println(GSON.toJson(profileJson(prof)));
      } else if (line[0].equals("info")) {
        System.out.println(GSON.toJson(userJson(prof.getUser())));
      } else if (line[0].equals("add")) {
        if (line.length < 2) {
          printHelp();
        } else if (line.length == 2) {
          try {
            Pair<Profile, Article> res = addArticle(prof, line[1], null);
            prof = res.first();
            System.out.println(GSON.toJson(res.second()));
          } catch (SQLException e) {
            System.out.println("Article could not be added to the databse :( "
                + e.getMessage());
          }
        } else if (line.length == 2) {
          try {
            int rank = Integer.parseInt(line[2]);
            if (rank != 0 || rank != 1) {
              printHelp();
            }
            Pair<Profile, Article> res = addArticle(prof, line[1], rank);
            prof = res.first();
            System.out.println(GSON.toJson(res.second()));
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
          for (Article art : prof.getArticles()) {
            if (art.getId().equals(line[1])) {
              System.out.println(GSON.toJsonTree(art));
              break;
            }
          }
        } else {
          printHelp();
        }
      } else if (line[0].equals("remove")) {

      } else if (line[0].equals("help")) {
        printHelp();
      } else {
        System.err.println(GSON.toJson("Invalid Commnad"));
      }
      System.out.print("readient> ");
    }
    s.close();
  }

  private Pair<Profile, Article> addArticle(Profile prof, String url,
      Integer rank)
      throws SQLException {
    ArticleParser p = new ArticleParser(url);
    Stats stats = StatsGenerator.analyze(p.iterator());
    String id = manager.addArticle(p.title(), prof.getUser().getUsername(),
        rank, stats.words());
    Map<String, Double> emotions = sg.moods(p, stats);
    manager.addMoods(id, emotions);
    Map<Integer, Double> sent = sg.sentiment(p, stats);
    manager.addSentiment(id, sent.get(1), sent.get(0));
    String topic = sg.topic(p);
    manager.addTopic(id, topic);
    double readLevel = (new Readability(stats)).avg();
    manager.addReadLevel(id, readLevel);
    byte[] pass = prof.getUser().getPasswordHash();
    byte[] salt = prof.getUser().getSalt();
    Article art = new Article(id, p.title(), prof.getUser().getUsername(), rank,
        readLevel);
    art.setMood(emotions);
    List<String> topics = new ArrayList<>();
    topics.add(topic);
    art.setTopics(topics);
    art.setSentiments(sent);
    prof.addArticle(art);
    return new Pair<>(getProfile(prof.getUser().getUsername(), pass, salt,
        prof.getArticles()),
        art);
  }

  private Profile getProfile(String username, String password)
      throws SQLException {
    User user = manager.getUser(username, password);
    if (user == null) {
      throw new IllegalArgumentException("invalid username");
    }
    Profile profile =
        new Profile(user, manager.avgReadLevel(user.getUsername()),
            manager.wordsRead(user.getUsername()),
            manager.getArticles(user.getUsername()));
    return profile;
  }

  private Profile getProfile(String username, byte[] pass, byte[] salt,
      List<Article> arts)
      throws SQLException {
    User user = manager.getUser(username, pass, salt);
    if (user == null) {
      throw new IllegalArgumentException("invalid username");
    }
    Profile profile =
        new Profile(user, manager.avgReadLevel(user.getUsername()),
            manager.wordsRead(user.getUsername()), arts);
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

  private static void printRunHelp() {
    System.out.println(
        "--help: prints this message\n"
            + "--login: logs in a user into Readient\n"
            + "--signup: signs a user into Readient and logs them in\n");
  }

  private static void printHelp() {
    System.out.println(
        "help: prints this message\n"
            + "profile: prints the while user profile\n"
            + "info: prints the user info\n"
            + "add <url>: adds an article to the user's profile\n"
            + "add <url> <rank>: adds an article to the user's profile with "
            + "a rank of 1 for like and 0 for unlike\n"
            + "remove <art_id>: removes an id form the user's profile\n"
            + "get <art_id>: gets the info for the given article\n"
            + "logout: logs out of Readient :(");
  }
}
