package edu.brown.cs.readient;

import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.brown.cs.db.QueryManager;
import spark.Spark;

public class SparkServer {

  private Gson gson;
  private QueryManager db;

  public SparkServer(int port, String urlDB) {
    gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    Spark.setPort(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    try {
      this.db = new QueryManager(urlDB);
    } catch (ClassNotFoundException | SQLException e) {
      System.err.println("ERROR: " + e.getMessage());
    }
  }

  /**
   * runs the spark server.
   */
  // public void run() {
  // Spark.get("/home", new HomeHandler(), new FreeMarkerEngine());
  // Spark.post("/article", new ArticleHandler());
  // Spark.post("/profile", new ProfileHandler());
  // }

}
