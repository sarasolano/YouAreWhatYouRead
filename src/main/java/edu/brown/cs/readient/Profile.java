package edu.brown.cs.readient;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Profile {

  private User user;
  private int wordsRead;
  private double avgReadLevel;
  private Map<String, Article> articles;

  public Profile(User u, double readLevel, int wordsRead,
      Collection<Article> arts) {
    this.user = u;
    this.avgReadLevel = readLevel;
    this.wordsRead = wordsRead;
    articles = new ConcurrentHashMap<>();
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

  public synchronized void setWordsRead(int wordsRead) {
    this.wordsRead = wordsRead;
  }

  public synchronized void setAvgReadLevel(double avgReadLevel) {
    this.avgReadLevel = avgReadLevel;
  }

  public synchronized Collection<Article> getArticles() {
    return Collections.unmodifiableCollection(articles.values());
  }

  public synchronized boolean containsArticle(String artID) {
    return articles.containsKey(artID);
  }

  public synchronized Article getArticle(String artID) {
    return articles.get(artID);
  }

  public synchronized double getAvgReadLevel() {
    return avgReadLevel;
  }

  public synchronized int wordsRead() {
    return wordsRead;
  }

  public synchronized void addArticle(Article art) {
    articles.put(art.getId(), art);
  }

  public synchronized void removeArticle(String artID) {
    articles.remove(artID);
  }

  public synchronized int numArticles() {
    return articles.size();
  }
}
