package edu.brown.cs.categorizer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    File serializedModel = new File("../readient/sentiment_categorizer.ser");
    if (serializedModel.exists()) {
      try {
        InputStream modelIn = new BufferedInputStream(
            new FileInputStream(serializedModel));
        model = new DoccatModel(modelIn);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    } else {
      try {
        dataIn = new FileInputStream("../readient/twitter.txt");
        ObjectStream lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
        ObjectStream sampleStream = new DocumentSampleStream(lineStream);
        // Specifies the minimum number of times a feature must be seen
        model = DocumentCategorizerME.train("en", sampleStream);
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
    OutputStream modelOut = null;
    try {
      modelOut = new BufferedOutputStream(new FileOutputStream(
          new File("../readient/sentiment_categorizer.ser")));
      model.serialize(modelOut);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (modelOut != null)
        try {
          modelOut.close();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
    }
  }

  public int classifyNewTweet(String tweet) {
    DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
    double[] outcomes = myCategorizer.categorize(tweet);
    String category = myCategorizer.getBestCategory(outcomes);

    if (category.equalsIgnoreCase("4")) {
      System.out.println("positive");
      return 1;
    } else {
      System.out.println("negative");
      return 0;
    }
  }
}
