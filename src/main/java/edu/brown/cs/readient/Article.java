package edu.brown.cs.readient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Article {

  private String id;
  private String title;
  private String user;
  private Integer ranking;
  private double readLevel;
  private double gradeLevel;
  private List<String> topics;
  private List<Integer> sentiments;
  private Map<String, Double> moods;

  public Article(String artID, String name, String username, Integer rank,
      double readLevel, double gradeLevel) {
    this.id = artID;
    this.title = name;
    this.user = username;
    this.ranking = rank;
    this.readLevel = readLevel;
    this.gradeLevel = gradeLevel;
    this.moods = new HashMap<>();
    this.topics = new ArrayList<>();
    this.sentiments = new ArrayList<>();
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getUser() {
    return user;
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

  public List<String> getTopics() {
    return topics;
  }

  public void addTopic(String t) {
    topics.add(t);
  }

  public void setTopics(List<String> t) {
    this.topics = t;
  }

  public Map<String, Double> getMoods() {
    return Collections.unmodifiableMap(moods);
  }

  public void setMood(Map<String, Double> m) {
    this.moods = m;
  }

  public Map<Integer, Double> getSentiments() {
    HashMap<Integer, Double> sent = new HashMap<>();
    double pos = 0;
    double neg = 0;
    for (int i : sentiments) {
      if (i == 0) {
        neg++;
      } else {
        pos++;
      }
    }
    sent.put(0, neg);
    sent.put(1, pos);
    return Collections.unmodifiableMap(sent);
  }

  public void setSentiments(List<Integer> s) {
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
