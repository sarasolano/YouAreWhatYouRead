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
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

/**
 * Sentiment categorizer class.
 *
 * @author Baab
 *
 */
public class SentimentCategorizer {
  DoccatModel model;

  public static void main(String[] args) {
    SentimentCategorizer twitterCategorizer = new SentimentCategorizer();
    // twitterCategorizer.trainModel();
    twitterCategorizer.classify("Have a nice day!");
    twitterCategorizer.classify("I hate life");
    twitterCategorizer.classify("Trump is the worst person ever");
    twitterCategorizer.classify("Lets go to the beach");
    twitterCategorizer.classify("Why is it raining today?");
    twitterCategorizer.classify("I'm sick with a cold");
    twitterCategorizer.classify(
        "Middleton grew up in Chapel Row, a village near Newbury, Berkshire, England.");
    twitterCategorizer.classify("The man has ebola.");

  }

  /**
   * Sentiment categorizer constructor
   */
  public SentimentCategorizer() {
    trainModel();
  }

  /**
   * Loads in serialized model or trains model if serialized model does not
   * exist.
   */
  private void trainModel() {
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
        ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn,
            "UTF-8");
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(
            lineStream);
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

  /**
   * Classifies a string with a sentiment.
   *
   * @param s
   *          the string
   * @return an int indicating whether positive or negative
   */
  public int classify(String s) {
    DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
    double[] outcomes = myCategorizer.categorize(s);
    String category = myCategorizer.getBestCategory(outcomes);
    if (category.equalsIgnoreCase("4")) {
      return 1;
    } else {
      return -1;
    }
  }
}
