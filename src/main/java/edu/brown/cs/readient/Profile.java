package edu.brown.cs.readient;

import java.util.ArrayList;
import java.util.List;

public class Profile {
  private User user;
  private int wordsRead;
  private double avgReadLevel;
  private List<Article> articles;

  public Profile(User u, int readLevel, int wordsRead, List<Article> arts) {
    this.user = u;
    this.avgReadLevel = readLevel;
    this.wordsRead = wordsRead;
    this.articles = arts;
  }

  public Profile(User u, int readLevel, int wordsRead) {
    this(u, readLevel, wordsRead, new ArrayList<>());
  }

  public String getUserName() {
    return user.getUsername();
  }

  public double getAvgReadLevel() {
    return avgReadLevel;
  }

  public int wordsRead() {
    return wordsRead;
  }

  public void addArticle(Article art) {
    articles.add(art);
  }

  public int numArticles() {
    return articles.size();
  }
}
