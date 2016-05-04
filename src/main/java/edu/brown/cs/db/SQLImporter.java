package edu.brown.cs.db;

import java.io.IOException;
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
   * @throws SQLException
   *           if there is an error while executing SQL query
   * @throws IOException
   *           if the program is unable to read the csv file
   */
  private static void createUserTable()
      throws SQLException, IOException {
    String query =
        "CREATE TABLE user("
            + "first_name VARCHAR(40) NULL,"
            + "last_name VARCHAR(40) NULL,"
            + "user_name VARCHAR(40) NOT NULL,"
            + "password_hash VARCHAR(100) NOT NULL,"
            + "salt VARCHAR(100) NOT NULL,"
            + "PRIMARY KEY(user_name)"
            + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
  }

  /**
   * Creates the article table.
   *
   * @throws SQLException
   *           if there is an error while executing SQL query
   * @throws IOException
   *           if the program is unable to read the csv file
   */
  private static void createArticleTable()
      throws SQLException, IOException {
    String query = "CREATE TABLE article("
        + "id TEXT NOT NULL,"
        + "name TEXT NOT NULL,"
        + "url TEXT NOT NULL,"
        + "user TEXT NOT NULL,"
        + "rank INT,"
        + "words INT NOT NULL,"
        + "PRIMARY KEY(id, user),"
        + "FOREIGN KEY(user) REFERENCES user(user_name)"
        + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
  }

  /**
   * Creates the read_level table.
   *
   * @throws SQLException
   *           if there is an error while executing SQL query
   */
  private static void createReadLevel() throws SQLException {
    String query = "CREATE TABLE read_level("
        + "article TEXT NOT NULL,"
        + "read_level REAL NOT NULL,"
        + "grade_level REAL NOT NULL,"
        + "PRIMARY KEY(article)"
        + "FOREIGN KEY(article) REFERENCES article(id)"
        + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
  }

  /**
   * Creates the topic table.
   *
   * @throws SQLException
   *           if there is an error while executing SQL query
   */
  private static void createTopicTable()
      throws SQLException, IOException {
    String query = "CREATE TABLE topic("
        + "article TEXT NOT NULL,"
        + "topic TEXT NOT NULL,"
        + "PRIMARY KEY(article, topic)"
        + "FOREIGN KEY(article) REFERENCES article(id)"
        + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
  }

  /**
   * Creates the mood table.
   *
   * @throws SQLException
   *           if there is an error while executing SQL query
   */
  private static void createSentimentTable()
      throws SQLException, IOException {
    String query = "CREATE TABLE sentiment("
        + "sentiment INT NOT NULL," // sentiment is 0 for negative and 1 for
                                    // positive
        + "article TEXT NOT NULL,"
        + "sentence INT NOT NULL,"
        + "PRIMARY KEY(sentiment, article, sentence),"
        + "CONSTRAINT sentiment CHECK(sentiment == 0 || sentiment == 1)"
        + "FOREIGN KEY(article) REFERENCES article(id)"
        + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
  }

  /**
   * Creates the mood table.
   *
   * @throws SQLException
   *           if there is an error while executing SQL query
   */
  private static void createMoodTable()
      throws SQLException, IOException {
    String query = "CREATE TABLE mood("
        + "article TEXT NOT NULL,"
        + "mood TEXT NOT NULL,"
        + "probability REAL NOT NULL,"
        + "PRIMARY KEY(article, mood),"
        + "FOREIGN KEY(article) REFERENCES article(id)"
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
    if (args.length < 1 || args.length > 1) {
      System.err.println(
          "usage: ./import <db_file>");
      return;
    }
    String db = args[0];
    try {
      Class.forName("org.sqlite.JDBC");
      CONN = DriverManager.getConnection("jdbc:sqlite:" + db);
      Statement statement = CONN.createStatement();
      statement.executeUpdate("DROP TABLE IF EXISTS user;");
      statement.executeUpdate("DROP TABLE IF EXISTS article;");
      statement.executeUpdate("DROP TABLE IF EXISTS topic;");
      statement.executeUpdate("DROP TABLE IF EXISTS read_level;");
      statement.executeUpdate("DROP TABLE IF EXISTS sentiment;");
      statement.executeUpdate("DROP TABLE IF EXISTS mood;");
      statement.executeUpdate("PRAGMA synchronous = OFF;");
      statement.executeUpdate("PRAGMA journal_mode = MEMORY;");
      statement.close();

      createUserTable();
      createArticleTable();
      createTopicTable();
      createReadLevel();
      createSentimentTable();
      createMoodTable();

      CONN.close();
    } catch (SQLException | ClassNotFoundException | IOException e) {
      e.printStackTrace();
      System.out.println("ERROR: " + e.getLocalizedMessage());
    }
  }
}
