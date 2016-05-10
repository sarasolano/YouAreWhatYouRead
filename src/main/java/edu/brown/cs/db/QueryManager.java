package edu.brown.cs.db;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.UUID;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;

import edu.brown.cs.parsing.ArticleParser;
import edu.brown.cs.readient.Article;
import edu.brown.cs.readient.User;
import edu.brown.cs.stats.Readability;
import edu.brown.cs.stats.StatsGenerator;
import edu.brown.cs.stats.StatsGenerator.Stats;
import edu.brown.cs.stats.Utils;

/**
 * A QueryManager for a database with the following schema.
 *
 * @author sarasolano
 */
public class QueryManager implements AutoCloseable {

  public static void main(String[] args) {
    try {
      int[] ranks = {-1, 0, 1};
      QueryManager m = new QueryManager("data.db");
      StatsGenerator sg = new StatsGenerator();
      Random r = new Random();
      long max = (new Date()).getTime();
      long min = Utils.minusYears(new Date(max), 1).getTime();
      long diff = max - min + 1;
      Scanner s = new Scanner(new FileReader("dummy.txt"));
      while (s.hasNext()) {
        String url = s.next();
        addArticleByUsername(m, sg, "", url, ranks[r.nextInt(2)],
            min + (r.nextLong() * diff));
      }
      s.close();
    } catch (IOException | ClassNotFoundException | ParseException
        | SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public static final DateFormat DATE_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd HH:mm");
  /**
   * The secure random number generator for salts.
   */
  private static final Random RANDOM = new SecureRandom();
  /**
   * The size of the salt.
   */
  private static final int SALT_SIZE = 64;
  /**
   * The amount of iterations for the hashing.
   */
  private static final int ITERATIONS = 10000;
  /**
   * The length of the hash key.
   */
  private static final int KEY_LENGTH = 256;
  /**
   * The database connection.
   */
  private Connection conn;

  /**
   * Constructs a query manager.
   *
   * @param db
   *          the path to the database
   * @throws SQLException
   *           if there is a database access error
   * @throws ClassNotFoundException
   *           if the class is not found
   */
  public QueryManager(String db) throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    conn = DriverManager.getConnection("jdbc:sqlite:" + db);
  }

  private static Article addArticleByUsername(QueryManager manager,
      StatsGenerator sg, String username, String url, Integer rank, long date)
      throws SQLException, ParseException, IOException {
    ArticleParser p = new ArticleParser(url);
    Stats stats = StatsGenerator.analyze(p.iterator());
    String id = manager.addArticle(p.title(), p.url(), username, rank,
        stats.words(), date);
    Map<String, Double> emotions = sg.moods(p, stats);
    manager.addMoods(id, emotions);
    List<Integer> sent = sg.sentiment(p, stats);
    manager.addSentiments(id, sent);
    String topic = sg.topic(p);
    manager.addTopic(id, topic);
    Readability read = new Readability(stats);
    manager.addReadLevel(id, read.avgRead(), read.avgGrade());
    Article art = manager.getArticle(id);
    return art;
  }

  /**
   * Gets all the usernames in the database.
   *
   * @return the usernames
   * @throws SQLException
   */
  public HashSet<String> getUserNames() throws SQLException {
    String query = "SELECT user_name FROM user;";
    PreparedStatement stat = conn.prepareStatement(query);
    ResultSet rs = stat.executeQuery();
    HashSet<String> toReturn = new HashSet<>();
    while (rs.next()) {
      toReturn.add(rs.getString(1));
    }
    stat.close();
    return toReturn;
  }

  /**
   * Gets a user given the username.
   *
   * @param username
   *          the username of the user
   * @param password
   *          the user password
   * @return the user if exists, null otherwise
   * @throws SQLException
   *           if there is an error while querying
   */
  public User getUser(String username, String password) throws SQLException {
    String query = "SELECT user_name, first_name, last_name, "
        + "password_hash, salt FROM user WHERE user_name = ?;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, username);
    ResultSet rs = stat.executeQuery();
    User toReturn = null;
    while (rs.next()) {
      byte[] expectedHash = stringToByte(rs.getString(4));
      byte[] salt = stringToByte(rs.getString(5));
      if (isExpectedPassword(password, salt, expectedHash)) {
        toReturn = new User(rs.getString(1), rs.getString(2), rs.getString(3),
            rs.getString(4), rs.getString(5));
      } else {
        throw new IllegalArgumentException("Error: wrong password");
      }
    }
    rs.close();
    stat.close();
    return toReturn;
  }

  /**
   * Gets a user given the username.
   *
   * @param username
   *          the username of the user
   * @param hash
   *          the hashed password
   * @param salt
   *          the salt for the hashed password
   * @return the user if exists, null otherwise
   * @throws SQLException
   *           if there is an error while querying
   */
  public User getUser(String username, byte[] expectedHash, byte[] salt)
      throws SQLException {
    String query = "SELECT user_name, first_name, last_name, "
        + "password_hash, salt FROM user WHERE user_name = ? "
        + "AND password_hash = ? AND salt = ?;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, username);
    stat.setString(2, bytetoString(expectedHash));
    stat.setString(3, bytetoString(salt));
    ResultSet rs = stat.executeQuery();
    User toReturn = null;
    while (rs.next()) {
      toReturn = new User(rs.getString(1), rs.getString(2), rs.getString(3),
          rs.getString(4), rs.getString(5));
    }
    rs.close();
    stat.close();
    return toReturn;
  }

  public User getUserByUsername(String username) throws SQLException {
    String query = "SELECT user_name, first_name, last_name, "
        + "password_hash, salt FROM user WHERE user_name = ?;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, username);
    ResultSet rs = stat.executeQuery();
    User toReturn = null;
    while (rs.next()) {
      toReturn = new User(rs.getString(1), rs.getString(2), rs.getString(3),
          rs.getString(4), rs.getString(5));
    }
    rs.close();
    stat.close();
    return toReturn;
  }

  /**
   * Gets all the articles submitted by a given user.
   *
   * @param username
   *          the username of the user
   * @return a list of articles submitted by the user
   * @throws SQLException
   * @throws ParseException
   */
  public List<Article> getArticles(String username)
      throws SQLException, ParseException {
    String query =
        "SELECT id, name, url, user, added, rank, read_level, grade_level, "
            + "words FROM article, read_level "
            + "WHERE article.id == read_level.article AND article.user == ? "
            + "ORDER BY strftime('%s', added) DESC;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, username);
    ResultSet rs = stat.executeQuery();
    List<Article> toReturn = new ArrayList<>();
    while (rs.next()) {
      String id = rs.getString(1);
      Article art = new Article(id, rs.getString(2), rs.getString(3),
          rs.getString(4), DATE_FORMAT.parse(rs.getString(5)), rs.getInt(6),
          rs.getDouble(7), rs.getDouble(8), rs.getInt(9));
      art.setMood(getMoods(id));
      art.setSentiments(getSentiments(id));
      art.setTopics(getTopics(id));
      toReturn.add(art);
    }
    rs.close();
    stat.close();
    return toReturn;
  }

  public List<Article> getArticlesBetweenDates(long start, long end,
      String username) throws SQLException, ParseException {
    String query =
        "SELECT id, name, url, user, added, rank, read_level, grade_level, "
            + "words FROM article, read_level WHERE article.id == read_level.article "
            + "AND article.user == ? AND added BETWEEN ? AND ? "
            + "ORDER BY added ASC;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, username);
    stat.setString(2, DATE_FORMAT.format(start));
    stat.setString(3, DATE_FORMAT.format(end));
    ResultSet rs = stat.executeQuery();
    List<Article> toReturn = new ArrayList<>();
    while (rs.next()) {
      String id = rs.getString(1);
      Article art = new Article(id, rs.getString(2), rs.getString(3),
          rs.getString(4), DATE_FORMAT.parse(rs.getString(5)), rs.getInt(6),
          rs.getDouble(7), rs.getDouble(8), rs.getInt(9));
      art.setMood(getMoods(id));
      art.setSentiments(getSentiments(id));
      art.setTopics(getTopics(id));
      toReturn.add(art);
    }
    rs.close();
    stat.close();
    return toReturn;
  }

  public Map<String, Integer> countArticlesByDates(String username)
      throws SQLException, ParseException {
    String query =
        "SELECT COUNT(id), added FROM article WHERE article.user == ? "
            + "GROUP BY added;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, username);
    ResultSet rs = stat.executeQuery();
    Map<String, Integer> toReturn = new HashMap<>();
    while (rs.next()) {
      int count = rs.getInt(1);
      String date = DATE_FORMAT.parse(rs.getString(2)).getTime() / 1000 + " ";
      toReturn.put(date, count);
    }
    rs.close();
    stat.close();
    return toReturn;
  }

  public Article getArticle(String artID) throws SQLException, ParseException {
    String query =
        "SELECT id, name, url, user, added, rank, read_level, grade_level, words FROM article, read_level "
            + "WHERE article.id == read_level.article AND article.id == ?;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, artID);
    ResultSet rs = stat.executeQuery();
    Article toReturn = null;
    while (rs.next()) {
      String id = rs.getString(1);
      Article art = new Article(id, rs.getString(2), rs.getString(3),
          rs.getString(4), DATE_FORMAT.parse(rs.getString(5)), rs.getInt(6),
          rs.getDouble(7), rs.getDouble(8),
          rs.getInt(9));
      art.setMood(getMoods(id));
      art.setSentiments(getSentiments(id));
      art.setTopics(getTopics(id));
      toReturn = art;
    }
    rs.close();
    stat.close();
    return toReturn;
  }

  public void removeArticle(String artID) throws SQLException {
    String query = "DELETE FROM article WHERE id = ?;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, artID);
    stat.execute();
    query = "DELETE FROM mood WHERE article = ?;";
    stat.setString(1, artID);
    stat.execute();
    query = "DELETE FROM sentiment WHERE article = ?;";
    stat.setString(1, artID);
    stat.execute();
    query = "DELETE FROM topic WHERE article = ?;";
    stat.setString(1, artID);
    stat.execute();
  }

  /**
   * Gets all the emotions in an article.
   *
   * @param artID
   *          the id of the article
   * @return a map from emotion to probability
   * @throws SQLException
   */
  public Map<String, Double> getMoods(String artID) throws SQLException {
    String query = "SELECT mood, probability FROM mood WHERE article == ?;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, artID);
    ResultSet rs = stat.executeQuery();
    Map<String, Double> toReturn = new HashMap<>();
    while (rs.next()) {
      toReturn.put(rs.getString(1), rs.getDouble(2));
    }
    rs.close();
    stat.close();
    return toReturn;
  }

  /**
   * Gets all the positive and negative sentiment probabilities in a given
   * article.
   *
   * @param artID
   *          the id of the article
   * @return a map from sentiment to probability for each sentence
   * @throws SQLException
   */
  public List<Integer> getSentiments(String artID) throws SQLException {
    String query = "SELECT sentence, sentiment FROM sentiment"
        + " WHERE article = ?;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, artID);
    ResultSet rs = stat.executeQuery();
    List<Integer> toReturn = new ArrayList<>();
    while (rs.next()) {
      toReturn.add(rs.getInt(1), rs.getInt(2));
    }
    rs.close();
    stat.close();
    return toReturn;
  }

  /**
   * Gets all the topics for a given article.
   *
   * @param artID
   *          the id of the article
   * @return a list of topics in the article
   * @throws SQLException
   */
  public List<String> getTopics(String artID) throws SQLException {
    String query = "SELECT topic FROM topic WHERE article == ?;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, artID);
    ResultSet rs = stat.executeQuery();
    List<String> toReturn = new ArrayList<>();
    while (rs.next()) {
      toReturn.add(rs.getString(1));
    }
    rs.close();
    stat.close();
    return toReturn;
  }

  /**
   * Gets the amount of words read by a user.
   *
   * @param username
   *          the username of the user
   * @return the amount of words read by the user, which is the sum of the count
   *         of words in every article submitted by the user
   * @throws SQLException
   *           if there is an error while querying
   */
  public int wordsRead(String username) throws SQLException {
    String query = "SELECT SUM(words) FROM article WHERE user == ?;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, username);
    ResultSet results = stat.executeQuery();
    // only add if results isn't empty
    int toReturn = 0;
    if (results.next()) {
      toReturn = results.getInt(1);
    }
    stat.close();
    results.close();
    return toReturn;
  }

  /**
   * Gets the avg reading level for a user's profile.
   *
   * @param username
   *          the user name of the user
   * @return the avg reading level of the user
   * @throws SQLException
   *           if there is an error while querying
   */
  public double avgReadLevel(String username) throws SQLException {
    String query = "SELECT SUM(read_level)/COUNT(*) FROM "
        + "read_level, article WHERE article.id == read_level.article "
        + "AND article.user == ?;";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, username);
    ResultSet results = stat.executeQuery();
    // only add if results isn't empty
    double toReturn = 0;
    if (results.next()) {
      BigDecimal bd = new BigDecimal(results.getDouble(1));
      bd = bd.round(new MathContext(3));
      toReturn = bd.doubleValue();
    }
    stat.close();
    results.close();
    return toReturn;
  }

  public Map<String, Double> avgMoods(String username) throws SQLException {
    String query = "SELECT mood, SUM(probability) FROM mood, article WHERE "
        + "article.id = mood.article AND article.user = ? "
        + "GROUP BY mood";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, username);
    ResultSet rs = stat.executeQuery();
    HashMap<String, Double> toReturn = new HashMap<>();
    while (rs.next()) {
      toReturn.put(rs.getString(1), rs.getDouble(2));
    }
    stat.close();
    rs.close();
    return toReturn;
  }

  /**
   * Adds a user to the database.
   *
   * @param username
   *          the user name
   * @param password
   *          the password of the profile
   * @param fName
   *          the first name
   * @param lName
   *          the last name
   * @throws SQLException
   *           if there is an error while executing
   */
  public void addUser(String username, String password, String fName,
      String lName) throws SQLException {
    String query = "INSERT INTO user VALUES(?, ?, ?, ?, ?);";
    PreparedStatement stat = conn.prepareStatement(query);
    byte[] salt = getSalt();
    stat.setString(1, fName);
    stat.setString(2, lName);
    stat.setString(3, username);
    stat.setString(4, bytetoString(hash(password, salt)));
    stat.setString(5, bytetoString(salt));
    stat.execute();
    stat.close();
  }

  public void changePassword(String username, String oldPass, String newPass)
      throws SQLException {
    User user = getUser(username, oldPass);
    if (user == null) {
      new IllegalArgumentException("Invalid username or password");
    }
    String query =
        "UPDATE user SET password_hash = ?, salt = ? WHERE user_name = ?";
    PreparedStatement stat = conn.prepareStatement(query);
    byte[] salt = getSalt();
    stat.setString(1, bytetoString(hash(newPass, salt)));
    stat.setString(2, bytetoString(salt));
    stat.setString(3, username);
    stat.execute();
    stat.close();
  }

  public String addArticle(String name, String url, String username,
      Integer rank, int words, long time) throws SQLException {
    String query = "INSERT INTO article VALUES(?, ?, ?, ?, ?, "
        + (rank == null ? "NULL," : "?,") + " ?);";
    PreparedStatement stat = conn.prepareStatement(query);
    String id = "a/" + UUID.randomUUID();
    stat.setString(1, id);
    stat.setString(2, name);
    stat.setString(3, url);
    stat.setString(4, username);
    // Calendar cal =
    // Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
    // cal.setTime(new Date());
    // stat.setString(5, DATE_FORMAT.format(cal.getTime()));
    stat.setString(5, DATE_FORMAT.format(time));
    if (rank != null) {
      stat.setInt(6, rank);
      stat.setInt(7, words);
    } else {
      stat.setInt(6, words);
    }
    stat.execute();
    stat.close();
    return id;
  }

  /**
   * Adds an article to the database.
   *
   * @param name
   *          the name of the article
   * @param username
   *          the user that submitted the article
   * @param rank
   *          the ranking given by the user
   * @param words
   *          the number of words in the article
   * @return the id of the newly added article
   * @throws SQLException
   *           if there is an error while executing
   */
  public String addArticle(String name, String url, String username,
      Integer rank, int words) throws SQLException {
    String query = "INSERT INTO article VALUES(?, ?, ?, ?, ?, "
        + (rank == null ? "NULL," : "?,") + " ?);";
    PreparedStatement stat = conn.prepareStatement(query);
    String id = "a/" + UUID.randomUUID();
    stat.setString(1, id);
    stat.setString(2, name);
    stat.setString(3, url);
    stat.setString(4, username);
    Calendar cal =
        Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
    cal.setTime(new Date());
    stat.setString(5, DATE_FORMAT.format(cal.getTime()));
    if (rank != null) {
      stat.setInt(6, rank);
      stat.setInt(7, words);
    } else {
      stat.setInt(6, words);
    }
    stat.execute();
    stat.close();
    return id;
  }

  /**
   * Add article's reading level to the database.
   *
   * @param artID
   *          the id of the article
   * @param readLevel
   *          the reading level of the article
   * @param gradeLevel
   *          the grade level of the article
   * @throws SQLException
   *           if there is an error while executing
   */
  public void addReadLevel(String artID, double readLevel, double gradeLevel)
      throws SQLException {
    String query = "INSERT INTO read_level VALUES(?, ?, ?);";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, artID);
    stat.setDouble(2, readLevel);
    stat.setDouble(3, gradeLevel);
    stat.execute();
    stat.close();
  }

  /**
   * Adds article sentiment probabilities to the database.
   *
   * @param artID
   *          the id of the article
   * @param sent
   *          the sentiment for each sentence in the article
   * @throws SQLException
   *           if there is a problem while executing
   */
  public void addSentiments(String artID, List<Integer> sent)
      throws SQLException {
    String query = "INSERT INTO sentiment VALUES(?, ?, ?);";
    PreparedStatement stat = conn.prepareStatement(query);
    for (int i = 0; i < sent.size(); i++) {
      stat.setDouble(1, sent.get(i));
      stat.setString(2, artID);
      stat.setInt(3, i);
      stat.addBatch();
    }
    stat.executeBatch();
    stat.close();
  }

  /**
   * Adds a topic to the database.
   *
   * @param artID
   *          the id of the article.
   * @param topic
   *          the topic of the article
   * @throws SQLException
   *           if there is an error while executing
   */
  public void addTopic(String artID, String topic) throws SQLException {
    String query = "INSERT INTO topic VALUES(?, ?);";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, artID);
    stat.setString(2, topic);
    stat.execute();
    stat.close();
  }

  /**
   * Adds a topic to the database.
   *
   * @param artID
   *          the id of the article.
   * @param topics
   *          the list of topic of the article
   * @throws SQLException
   *           if there is an error while executing
   */
  public void addTopics(String artID, List<String> topics) throws SQLException {
    String query = "INSERT INTO topic VALUES(?, ?);";
    PreparedStatement stat = conn.prepareStatement(query);
    for (String topic : topics) {
      stat.setString(1, artID);
      stat.setString(2, topic);
      stat.addBatch();
    }
    stat.executeBatch();
    stat.close();
  }

  /**
   * Adds a mood to the database.
   *
   * @param artID
   *          the id of the article
   * @param mood
   *          the mood
   * @param prob
   *          the mood probability
   * @throws SQLException
   *           if there is an error while executing
   */
  public void addMood(String artID, String mood, double prob)
      throws SQLException {
    String query = "INSERT INTO mood VALUES(?, ?, ?);";
    PreparedStatement stat = conn.prepareStatement(query);
    stat.setString(1, artID);
    stat.setString(2, mood);
    stat.setDouble(3, prob);
    stat.execute();
    stat.close();
  }

  /**
   * Adds a mood to the database.
   *
   * @param artID
   *          the id of the article
   * @param moods
   *          a map from mood to probability
   * @throws SQLException
   *           if there is an error while executing
   */
  public void addMoods(String artID, Map<String, Double> moods)
      throws SQLException {
    String query = "INSERT INTO mood VALUES(?, ?, ?);";
    PreparedStatement stat = conn.prepareStatement(query);
    for (Entry<String, Double> mood : moods.entrySet()) {
      stat.setString(1, artID);
      stat.setString(2, mood.getKey());
      stat.setDouble(3, mood.getValue());
      stat.addBatch();
    }
    stat.executeBatch();
    stat.close();
  }

  /**
   * Returns a salted and hashed password using the provided hash.
   *
   * @param password
   *          the password to be hashed
   * @param salt
   *          a 16 bytes salt, ideally obtained with the getNextSalt method
   * @return the hashed password with a pinch of salt
   */
  public static byte[] hash(String password, byte[] salt) {
    char[] pwd = password.toCharArray();
    PBEKeySpec spec = new PBEKeySpec(pwd, salt, ITERATIONS, KEY_LENGTH);
    Arrays.fill(pwd, Character.MIN_VALUE);
    try {
      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      return skf.generateSecret(spec).getEncoded();
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new AssertionError(
          "Error while hashing a password: " + e.getMessage(), e);
    } finally {
      spec.clearPassword();
    }
  }

  /**
   * Returns true if the given password and salt match the hashed value, false
   * otherwise.
   *
   * @param password
   *          the password to check
   * @param salt
   *          the salt used to hash the password
   * @param expectedHash
   *          the expected hashed value of the password
   * @return true if the given password and salt match the hashed value, false
   *         otherwise
   */
  public static boolean isExpectedPassword(String password, byte[] salt,
      byte[] expectedHash) {
    char[] pwd = password.toCharArray();
    byte[] pwdHash = hash(password, salt);
    Arrays.fill(pwd, Character.MIN_VALUE);
    if (pwdHash.length != expectedHash.length) {
      return false;
    }
    for (int i = 0; i < pwdHash.length; i++) {
      if (pwdHash[i] != expectedHash[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns a random salt to be used to hash a password.
   *
   * @return a 64 bytes random salt
   */
  public static byte[] getSalt() {
    byte[] salt = new byte[SALT_SIZE];
    RANDOM.nextBytes(salt);
    return salt;
  }

  /**
   * Converts a base 64 byte array into a String.
   *
   * @param input
   *          the input to convert
   * @return the byte array converted into a string
   */
  public static String bytetoString(byte[] input) {
    return Base64.encodeBase64String(input);
  }

  /**
   * Converts the given String into a byte array of base 64.
   *
   * @param input
   *          the input to convert
   * @return the string converted into a byte array
   */
  public static byte[] stringToByte(String input) {
    if (Base64.isBase64(input)) {
      return Base64.decodeBase64(input);

    } else {
      return Base64.encodeBase64(input.getBytes());
    }
  }

  @Override
  public void close() throws Exception {
    conn.close();
  }
}
