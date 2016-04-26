package edu.brown.cs.readient;

import java.util.ArrayList;
import java.util.List;

public class Article {
  /**
   * Sentiment POJO.
   *
   * @author sarasolano
   */
  class Sentiment {
    private int sentiment;
    private double probability;

    Sentiment(int sent, double prob) {
      this.sentiment = sent;
      this.probability = prob;
    }

    public int getSentiment() {
      return sentiment;
    }

    public double getProbability() {
      return probability;
    }
  }

  /**
   * Mood POJO.
   *
   * @author sarasolano
   */
  class Mood {
    private String tag;
    private double probability;

    Mood(String mood, double prob) {
      this.tag = mood;
      this.probability = prob;
    }

    public String getTag() {
      return tag;
    }

    public double getProbability() {
      return probability;
    }
  }

  /**
   * The number of possible sentiments.
   */
  private static final int NUM_SENT = 2;
  private String id;
  private String title;
  private String user;
  private double ranking;
  private double readLevel;
  private List<String> topics;
  private Sentiment[] sentiments;
  private List<Mood> moods;

  public Article(String artID, String name, String username, double rank,
      double readLevel) {
    this.id = artID;
    this.title = name;
    this.user = username;
    this.ranking = rank;
    this.readLevel = readLevel;
    this.topics = new ArrayList<>();
    this.moods = new ArrayList<>();
    this.sentiments = new Sentiment[NUM_SENT];
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

  public void addTopic(String topic) {
    topics.add(topic);
  }

  public List<Mood> getMoods() {
    return moods;
  }

  public void setMoods(List<Mood> moods) {
    this.moods = moods;
  }

  public Sentiment[] getSentiments() {
    return sentiments;
  }

  public void setPosSentiment(double prob) {
    this.sentiments[1] = new Sentiment(1, prob);
  }

  public void setNegSentiment(double prob) {
    this.sentiments[0] = new Sentiment(0, prob);
  }
}
