package edu.brown.cs.readient;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile {
  public void setWordsRead(int wordsRead) {
    this.wordsRead = wordsRead;
  }

  public void setAvgReadLevel(double avgReadLevel) {
    this.avgReadLevel = avgReadLevel;
  }

  private User user;
  private int wordsRead;
  private double avgReadLevel;
  private Map<String, Article> articles;

  public Profile(User u, double readLevel, int wordsRead,
      Collection<Article> arts) {
    this.user = u;
    this.avgReadLevel = readLevel;
    this.wordsRead = wordsRead;
    articles = new HashMap<>();
    for (Article art : arts) {
      articles.put(art.getId(), art);
    }
  }

  public Profile(User u, List<Article> arts) {
    this(u, 0, 0, arts);
  }

  public User getUser() {
    return user;
  }

  public Collection<Article> getArticles() {
    return Collections.unmodifiableCollection(articles.values());
  }

  public boolean containsArticle(String artID) {
    return articles.containsKey(artID);
  }

  public Article getArticle(String artID) {
    return articles.get(artID);
  }

  public double getAvgReadLevel() {
    return avgReadLevel;
  }

  public int wordsRead() {
    return wordsRead;
  }

  public void addArticle(Article art) {
    articles.put(art.getId(), art);
  }

  public void removeArticle(String artID) {
    articles.remove(artID);
  }

  public int numArticles() {
    return articles.size();
  }
}
