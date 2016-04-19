package edu.brown.cs.db;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVReader;

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
   * The users in the database.
   */
  private static HashMap<Integer, String> users = new HashMap<>();
  /**
   * The articles in the database.
   */
  private static HashMap<String, Integer> articles = new HashMap<>();

  /**
   * Creates the user table.
   *
   * @param csv
   *          the csv file where the user data is saved
   * @throws SQLException
   *           if there is an error while executing SQL query
   * @throws IOException
   *           if the program is unable to read the csv file
   */
  private static void createUserTable(String csv)
      throws SQLException, IOException {
    String query =
        "CREATE TABLE user("
            + "id INT NOT NULL,"
            + "first_name VARCHAR(40) NULL,"
            + "last_name VARCHAR(40) NULL,"
            + "user_name VARCHAR(40) NOT NULL,"
            + "passwordHash TEXT NOT NULL,"
            + "avg_read_level REAL NOT NULL,"
            + "PRIMARY KEY(id)"
            + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
    query = "INSERT OR IGNORE INTO user VALUES(?, ?, ?, ?, ?, ?)";
    statement = CONN.prepareStatement(query);
    CSVReader reader = new CSVReader(new FileReader(csv));
    String[] line = reader.readNext();
    while (line != null) {
      int id = Integer.parseInt(line[0]);
      String fName = line[1];
      String lName = line[2];
      String userName = line[3];
      String password = line[4];
      double readLevel = Double.parseDouble(line[5]);
      users.put(id, userName);

      statement.setInt(1, id);
      statement.setString(2, fName);
      statement.setString(3, lName);
      statement.setString(4, userName);
      statement.setString(5, password);
      statement.setDouble(6, readLevel);
      statement.addBatch();
      line = reader.readNext();
    }
    reader.close();
  }

  /**
   * Creates the article table.
   *
   * @param csv
   *          the csv file where the article data is saved
   * @throws SQLException
   *           if there is an error while executing SQL query
   * @throws IOException
   *           if the program is unable to read the csv file
   */
  private static void createArticleTable(String csv)
      throws SQLException, IOException {
    String query = "CREATE TABLE article("
        + "id TEXT NOT NULL,"
        + "name TEXT NOT NULL,"
        + "user INT NOT NULL,"
        + "PRIMARY KEY(id, user, topic),"
        + "FOREIGN KEY(user) REFERENCES user(id),"
        + "FOREIGN KEY(topic) REFERENCES topic(id)"
        + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
    query = "INSERT OR IGNORE INTO article VALUES(?, ?, ?)";
    statement = CONN.prepareStatement(query);
    CSVReader reader = new CSVReader(new FileReader(csv));
    String[] line = reader.readNext();
    while (line != null) {
      String id = line[0];
      String name = line[1];
      int user = Integer.parseInt(line[2]);
      if (!users.containsKey(user)) {
        continue;
      }
      articles.put(id, user);

      statement.setString(1, id);
      statement.setString(2, name);
      statement.setInt(3, user);
      statement.addBatch();
      line = reader.readNext();
    }
    reader.close();
  }

  /**
   * Creates the topic table.
   *
   * @param csv
   *          the csv file where the topic data is saved
   * @throws SQLException
   *           if there is an error while executing SQL query
   * @throws IOException
   *           if the program is unable to read the csv file
   */
  private static void createTopicTable(String csv)
      throws SQLException, IOException {
    String query = "CREATE TABLE topic("
        + "article TEXT NOT NULL,"
        + "topic TEXT NOT NULL,"
        + "PRIMARY KEY(id)"
        + ")";
    PreparedStatement statement = CONN.prepareStatement(query);
    statement.execute();
    statement.close();
    query = "INSERT OR IGNORE INTO topic VALUES(?, ?)";
    statement = CONN.prepareStatement(query);
    CSVReader reader = new CSVReader(new FileReader(csv));
    String[] line = reader.readNext();
    while (line != null) {
      String article = line[0];
      String topic = line[1];
      if (!articles.containsKey(article)) {
        continue;
      }
      statement.setString(1, article);
      statement.setString(4, topic);
      statement.addBatch();
      line = reader.readNext();
    }
    reader.close();
  }

  /**
   * Creates the mood table.
   *
   * @param csv
   *          the csv file where the mood data is saved
   * @throws SQLException
   *           if there is an error while executing SQL query
   * @throws IOException
   *           if the program is unable to read the csv file
   */
  private static void createMoodTable(String csv)
      throws SQLException, IOException {
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
    statement.close();
    query = "INSERT OR IGNORE INTO mood VALUES(?, ?, ?)";
    statement = CONN.prepareStatement(query);
    CSVReader reader = new CSVReader(new FileReader(csv));
    String[] line = reader.readNext();
    while (line != null) {
      int mood = Integer.parseInt(line[0]);
      String article = line[1];
      double prob = Double.parseDouble(line[2]);
      if (!articles.containsKey(article)) {
        continue;
      }
      statement.setInt(1, mood);
      statement.setString(2, article);
      statement.setDouble(3, prob);
      statement.addBatch();
      line = reader.readNext();
    }
    reader.close();
  }

  /**
   * Creates the artible_sent table.
   *
   * @param csv
   *          the csv file where the article-sentiment data is saved
   * @throws SQLException
   *           if there is an error while executing SQL query
   * @throws IOException
   *           if the program is unable to read the csv file
   */
  private static void createSentimentTable(String csv)
      throws SQLException, IOException {
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
    query = "INSERT OR IGNORE INTO mood VALUES(?, ?, ?)";
    statement = CONN.prepareStatement(query);
    CSVReader reader = new CSVReader(new FileReader(csv));
    String[] line = reader.readNext();
    while (line != null) {
      String article = line[0];
      String sentiment = line[1];
      double prob = Double.parseDouble(line[2]);
      if (!articles.containsKey(article)) {
        continue;
      }
      statement.setString(1, article);
      statement.setString(2, sentiment);
      statement.setDouble(3, prob);
      statement.addBatch();
      line = reader.readNext();
    }
    reader.close();
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
          "usage: ./import <db_file> <users_csv> <artible_csv> "
              + "<topic_csv> <sentiment_csv> <mood_csv> <article_sentiment_csv>");
      return;
    }
    String db = args[0];
    String user = args[1];
    String article = args[2];
    String topic = args[3];
    String sentiment = args[4];
    String mood = args[5];
    try {
      Class.forName("org.sqlite.JDBC");
      CONN = DriverManager.getConnection("jdbc:sqlite:" + db);
      Statement statement = CONN.createStatement();
      statement.executeUpdate("DROP TABLE IF EXISTS user;");
      statement.executeUpdate("DROP TABLE IF EXISTS article;");
      statement.executeUpdate("DROP TABLE IF EXISTS topic;");
      statement.executeUpdate("DROP TABLE IF EXISTS sentiment;");
      statement.executeUpdate("DROP TABLE IF EXISTS mood;");
      statement.executeUpdate("DROP TABLE IF EXISTS article_sent;");
      statement.executeUpdate("PRAGMA synchronous = OFF;");
      statement.executeUpdate("PRAGMA journal_mode = MEMORY;");
      statement.close();

      createUserTable(user);
      createArticleTable(article);
      createTopicTable(topic);
      createSentimentTable(sentiment);
      createMoodTable(mood);

      CONN.close();
    } catch (SQLException | ClassNotFoundException | IOException e) {
      e.printStackTrace();
      System.out.println("ERROR: " + e.getLocalizedMessage());
    }
  }
}
