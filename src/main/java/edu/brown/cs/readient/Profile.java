package edu.brown.cs.readient;

import java.util.ArrayList;
import java.util.List;

public class Profile {
  private User user;
  private int wordsRead;
  private List<Article> articles;

  public Profile(User u, int wordsRead) {
    this.user = u;
    this.wordsRead = 0;
    this.articles = new ArrayList<>();
  }

  public String getUserName() {
    return user.getUsername();
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
