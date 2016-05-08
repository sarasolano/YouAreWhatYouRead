package edu.brown.cs.readient;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class Profile {

  private User user;
  private int wordsRead;
  private double avgReadLevel;
  private Map<String, Double> avgMoods;
  private Map<String, Article> articleMap;
  private Set<Article> articles;

  public Profile(User u, double readLevel, int wordsRead,
      Collection<Article> arts) {
    this.user = u;
    this.avgReadLevel = readLevel;
    this.wordsRead = wordsRead;
    articles = Collections.synchronizedSet(new TreeSet<Article>(
        (Article a1, Article a2) -> {
          if (a1.getAddedDate().after(a2.getAddedDate())) {
            return 1;
          } else if (a1.getAddedDate().before(a2.getAddedDate())) {
            return -1;
          } else {
            return 0;
          }
        }));
    articleMap = new ConcurrentHashMap<>();
    avgMoods = new ConcurrentHashMap<>();
    for (Article art : arts) {
      articles.add(art);
      articleMap.put(art.getId(), art);
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

  public synchronized void setAvgMoods(Map<String, Double> avgMoods) {
    this.avgMoods = avgMoods;
  }

  public synchronized Collection<Article> getArticles() {
    return Collections.unmodifiableCollection(articles);
  }

  public synchronized boolean containsArticle(String artID) {
    return articleMap.containsKey(artID);
  }

  public synchronized Article getArticle(String artID) {
    return articleMap.get(artID);
  }

  public synchronized double getAvgReadLevel() {
    return avgReadLevel;
  }

  public synchronized Map<String, Double> getAvgMoods() {
    return avgMoods;
  }

  public synchronized int wordsRead() {
    return wordsRead;
  }

  public synchronized void addArticle(Article art) {
    articleMap.put(art.getId(), art);
  }

  public synchronized void removeArticle(String artID) {
    Article a = articleMap.get(artID);
    articles.remove(a);
    articleMap.remove(artID);
  }

  public synchronized int numArticles() {
    return articles.size();
  }
}
