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
  private double ranking;
  private double readLevel;
  private List<String> topics;
  private Map<Integer, Double> sentiments;
  private Map<String, Double> moods;

  public Article(String artID, String name, String username, Integer rank,
      double readLevel) {
    this.id = artID;
    this.title = name;
    this.user = username;
    this.ranking = rank;
    this.readLevel = readLevel;
    this.moods = new HashMap<>();
    this.topics = new ArrayList<>();
    this.sentiments = new HashMap<>();
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
  public double getRanking() {
    return ranking;
  }

  public double getReadLevel() {
    return readLevel;
  }

  public List<String> getTopics() {
    return topics;
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
    return Collections.unmodifiableMap(sentiments);
  }

  public void setSentiments(Map<Integer, Double> s) {
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
