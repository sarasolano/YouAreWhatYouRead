package edu.brown.cs.readient;

import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

import edu.brown.cs.db.QueryManager;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public final class Main {
  public static void main(String[] args) {
    (new Main(args)).run();
  }

  private static final int LOGIN_ARGS = 3;
  private static final int SIGNUP_ARGS = 3;
  private static final Gson GSON = new Gson();
  private static final String DB = "data.db";
  private QueryManager manager;

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
      } else if (options.has("login") || options.has("signup")) {

        if (arguments.size() != LOGIN_ARGS) {
          throw new IllegalArgumentException(
              "usage: ./readient <username> <passoword>");
        } else if (arguments.size() != SIGNUP_ARGS) {
          throw new IllegalArgumentException("usage: ./readient <username> "
              + "<passoword> <first_name> <last_name>");
        }
        manager = new QueryManager(DB);
        if (options.has("signup")) {
          manager.addUser(arguments.get(0), arguments.get(1), arguments.get(2),
              arguments.get(3));
        }
        User user = manager.getUser(arguments.get(0), arguments.get(1));
        if (user == null) {
          throw new IllegalArgumentException("Error: invalid username");
        }
        Profile profile =
            new Profile(user, manager.avgReadLevel(user.getUsername()),
                manager.wordsRead(user.getUsername()),
                manager.getArticles(user.getUsername()));
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
    Scanner s = new Scanner(System.in);
    while (s.hasNext()) {
      String[] line = s.nextLine().trim().split(" ");
      if (line[0].equals("logout")) {
        break;
      } else if (line.length == 0) {
        continue;
      } else if (line[0].equals("profile")) {

      } else if (line[0].equals("info")) {

      } else if (line[0].equals("add")) {

      } else if (line[0].equals("get")) {

      } else if (line[0].equals("remove")) {

      } else if (line[0].equals("help")) {

      } else {
        System.err.println(GSON.toJson("Invalid Commnad"));
      }
    }
    s.close();
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
            + "remove <art_id>: removes an id form the user's profile\n"
            + "get <art_id>: gets the info for the given article\n"
            + "logout: logs out of Readient :(");
  }
}
