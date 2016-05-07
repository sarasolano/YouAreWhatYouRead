package edu.brown.cs.readient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Article {

  private String id;
  private String title;
  private String user;
  private String url;
  private Date addedDate;
  private Integer ranking;
  private double readLevel;
  private double gradeLevel;
  private List<String> topics;
  private List<Integer> sentiments;
  private Map<String, Double> moods;
  private int words;

  public Article(String artID, String name, String url, String username,
      Date added, Integer rank, double readLevel, double gradeLevel,
      int words) {
    this.id = artID;
    this.title = name;
    this.url = url;
    this.addedDate = added;
    this.user = username;
    this.ranking = rank;
    this.readLevel = readLevel;
    this.gradeLevel = gradeLevel;
    this.moods = new ConcurrentHashMap<>();
    this.topics = Collections.synchronizedList(new ArrayList<>());
    this.sentiments = Collections.synchronizedList(new ArrayList<>());
    this.words = words;
  }

  public String getId() {
    return id;
  }

  public int getWords() {
    return words;
  }

  public String getTitle() {
    return title;
  }

  public String getUser() {
    return user;
  }

  public String url() {
    return url;
  }

  public Date getAddedDate() {
    return addedDate;
  }

  /**
   * Gets the article ranking.
   *
   * @return the ranking
   */
  public Integer getRanking() {
    return ranking;
  }

  public double getReadLevel() {
    return readLevel;
  }

  /**
   * @return the gradeLevel
   */
  public double getGradeLevel() {
    return gradeLevel;
  }

  public synchronized List<String> getTopics() {
    return Collections.unmodifiableList(topics);
  }

  public synchronized void addTopic(String t) {
    topics.add(t);
  }

  public synchronized void setTopics(List<String> t) {
    this.topics = t;
  }

  public synchronized Map<String, Double> getMoods() {
    return Collections.unmodifiableMap(moods);
  }

  public synchronized void setMood(Map<String, Double> m) {
    this.moods = m;
  }

  public List<Integer> getListSentiment() {
    return sentiments;
  }

  public synchronized Map<Integer, Double> getSentiments() {
    HashMap<Integer, Double> sent = new HashMap<>();
    double pos = 0;
    double neg = 0;
    for (int i : sentiments) {
      if (i == -1) {
        neg++;
      } else {
        pos++;
      }
    }
    sent.put(0, neg);
    sent.put(1, pos);
    return Collections.unmodifiableMap(sent);
  }

  public synchronized void setSentiments(List<Integer> s) {
    this.sentiments = s;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Article other = (Article) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
