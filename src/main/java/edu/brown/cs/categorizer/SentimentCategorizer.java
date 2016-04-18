package edu.brown.cs.categorizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class SentimentCategorizer {
  DoccatModel model;

  public static void main(String[] args) {
    SentimentCategorizer twitterCategorizer = new SentimentCategorizer();
    twitterCategorizer.trainModel();
    twitterCategorizer.classifyNewTweet("Have a nice day!");
    twitterCategorizer.classifyNewTweet("I hate life");
    twitterCategorizer.classifyNewTweet("Trump is the worst person ever");
    twitterCategorizer.classifyNewTweet("Lets go to the beach");
    twitterCategorizer.classifyNewTweet("Why is it raining today?");
    twitterCategorizer.classifyNewTweet("I'm sick with a cold");
    twitterCategorizer.classifyNewTweet(
        "Middleton grew up in Chapel Row, a village near Newbury, Berkshire, England.");
    twitterCategorizer.classifyNewTweet("The man has ebola.");
    
  }

  public void trainModel() {
    InputStream dataIn = null;
    try {
      dataIn = new FileInputStream("../readient/twitter.txt");
      ObjectStream lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
      ObjectStream sampleStream = new DocumentSampleStream(lineStream);
      // Specifies the minimum number of times a feature must be seen
      int cutoff = 2;
      int trainingIterations = 30;
      model = DocumentCategorizerME.train("en", sampleStream, cutoff,
          trainingIterations);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (dataIn != null) {
        try {
          dataIn.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void classifyNewTweet(String tweet) {
    DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
    double[] outcomes = myCategorizer.categorize(tweet);
    String category = myCategorizer.getBestCategory(outcomes);

    if (category.equalsIgnoreCase("4")) {
      System.out.println("The tweet is positive :) ");
    } else {
      System.out.println("The tweet is negative :( ");
    }
  }
}
