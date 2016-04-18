package edu.brown.cs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SQLImporter is a runnable class that creates tables to the inputed database
 * and imports previously save data into the database.
 *
 * @author sarasolano
 */
public class SQLImporter {
  /**
   * The current connection to the inputed database.
   */
  private static Connection CONN;

  /**
   * Creates the user table.
   *
   * @param csv
   *          the csv file where the user data is saved
   * @throws SQLException
   *           if there is an error while executing SQL query
   */
  private static void createUserTable(String csv) throws SQLException {
    String query =
        "CREATE TABLE user("
            + "id INT NOT NULL,"
            + "first_name VARCHAR(40) NULL,"
            + "last_name VARCHAR(40) NULL,"
            + "user_name VARCHAR(40) NOT NULL,"
            + "passwordHash TEXT NOT NULL,"
            + "PRIMARY KEY(id)"
            + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
    // TODO(ssolano): add csv data into the database.
  }

  /**
   * Creates the profile table.
   *
   * @param csv
   *          the csv file where the profile data is saved
   * @throws SQLException
   *           if there is an error while executing SQL query
   */
  private static void createProfileTable(String csv) throws SQLException {
    String query =
        "CREATE TABLE profile("
            + "user INT NOT NULL,"
            + "avg_read_level REAL NOT NULL,"
            + "num_articles INT NOT NULL,"
            + "PRIMARY KEY(user),"
            + "FOREIGN KEY(user) REFERENCES user(id)"
            + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
  }

  /**
   * Creates the article table.
   *
   * @param csv
   *          the csv file where the article data is saved
   * @throws SQLException
   *           if there is an error while executing SQL query
   */
  private static void createArticleTable(String csv) throws SQLException {
    String query = "CREATE TABLE article("
        + "id TEXT NOT NULL,"
        + "name TEXT NOT NULL,"
        + "user INT NOT NULL,"
        + "topic INT NOT NULL,"
        + "PRIMARY KEY(id, user, topic),"
        + "FOREIGN KEY(user) REFERENCES user(id),"
        + "FOREIGN KEY(topic) REFERENCES topic(id)"
        + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
  }

  /**
   * Creates the topic table.
   *
   * @param csv
   *          the csv file where the topic data is saved
   * @throws SQLException
   *           if there is an error while executing SQL query
   */
  private static void createTopicTable(String csv) throws SQLException {
    String query = "CREATE TABLE topic("
        + "id TEXT NOT NULL,"
        + "name TEXT NOT NULL,"
        + "PRIMARY KEY(id)"
        + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
  }

  /**
   * Creates the sentiment table.
   *
   * @param csv
   *          the csv file where the sentiment data is saved
   * @throws SQLException
   *           if there is an error while executing SQL query
   */
  private static void createSentimentTable(String csv) throws SQLException {
    String query = "CREATE TABLE sentiment("
        + "id TEXT NOT NULL,"
        + "label TEXT NOT NULL,"
        + "PRIMARY KEY(id)"
        + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
  }

  /**
   * Creates the mood table.
   *
   * @param csv
   *          the csv file where the mood data is saved
   * @throws SQLException
   *           if there is an error while executing SQL query
   */
  private static void createMoodTable(String csv) throws SQLException {
    String query = "CREATE TABLE mood("
        + "mood INT NOT NULL," // mood is 0 for negative and 1 for positive
        + "article TEXT NOT NULL,"
        + "probability REAL NOT NULL,"
        + "PRIMARY KEY(mood, article),"
        + "CONSTRAINT mood CHECK(mood == 0 || mood == 1)"
        + "FOREIGN KEY(article) REFERENCES article(id)"
        + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
  }

  /**
   * Creates the artible_sent table.
   *
   * @param csv
   *          the csv file where the article-sentiment data is saved
   * @throws SQLException
   *           if there is an error while executing SQL query
   */
  private static void createArticleSetiment(String csv) throws SQLException {
    String query = "CREATE TABLE article_sent("
        + "article TEXT NOT NULL,"
        + "sentiment TEXT NOT NULL,"
        + "probability REAL NOT NULL,"
        + "PRIMARY KEY(article, sentiment),"
        + "FOREIGN KEY(article) REFERENCES article(id),"
        + "FOREIGN KEY(sentiment) REFERENCES sentiment(id)"
        + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
  }

  /**
   * The main method.
   *
   * @param args
   *          the input from the user
   */
  public static void main(String[] args) {
    if (args.length < 8 || args.length > 8) {
      System.err.println(
          "usage: ./import <db_file> <users_csv> <profiles_csv> <artible_csv> "
              + "<topic_csv> <sentiment_csv> <mood_csv> <article_sentiment_csv>");
      return;
    }
    String db = args[0];
    String user = args[1];
    String profile = args[2];
    String article = args[3];
    String topic = args[4];
    String sentiment = args[5];
    String mood = args[6];
    String articleSent = args[7];
    try {
      Class.forName("org.sqlite.JDBC");
      CONN = DriverManager.getConnection("jdbc:sqlite:" + db);
      Statement statement = CONN.createStatement();
      statement.executeUpdate("DROP TABLE IF EXISTS user;");
      statement.executeUpdate("DROP TABLE IF EXISTS profile;");
      statement.executeUpdate("DROP TABLE IF EXISTS article;");
      statement.executeUpdate("DROP TABLE IF EXISTS topic;");
      statement.executeUpdate("DROP TABLE IF EXISTS sentiment;");
      statement.executeUpdate("DROP TABLE IF EXISTS mood;");
      statement.executeUpdate("DROP TABLE IF EXISTS article_sent;");
      statement.executeUpdate("PRAGMA synchronous = OFF;");
      statement.executeUpdate("PRAGMA journal_mode = MEMORY;");
      statement.close();

      createUserTable(user);
      createProfileTable(profile);
      createArticleTable(article);
      createTopicTable(topic);
      createSentimentTable(sentiment);
      createMoodTable(mood);
      createArticleSetiment(articleSent);

      CONN.close();
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
      System.out.println("ERROR: " + e.getLocalizedMessage());
    }
  }
}
