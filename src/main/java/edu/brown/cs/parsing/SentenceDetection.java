package edu.brown.cs.parsing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceDetection {
  private SentenceModel model;

  public static void main(String[] args) {
    SentenceDetection sd;
    sd = new SentenceDetection();

    String[] out = sd.getSentences(
        "Dr. Baab is a very famous doctor in Atlanta. I'm pleased that she is my doctor.");
    System.out.println(out[0]);
  }

  public SentenceDetection() {

    InputStream dataIn = null;
    File serializedModel = new File("docmodel.ser");
    if (serializedModel.exists()) {
      try {
        InputStream modelIn = new BufferedInputStream(
            new FileInputStream(serializedModel));
        model = new SentenceModel(modelIn);
        modelIn.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    } else {
      InputStream modelIn;
      try {
        modelIn = new FileInputStream("da-sent.bin");
      } catch (FileNotFoundException e1) {
        throw new RuntimeException(e1);
      }
      try {
        model = new SentenceModel(modelIn);

        OutputStream modelOut = null;
        try {
          modelOut = new BufferedOutputStream(
              new FileOutputStream(new File("docmodel.ser")));
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
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (modelIn != null) {
          try {
            modelIn.close();
          } catch (IOException e) {
          }
        }
      }
    }

  }

  public String[] getSentences(String doc) {
    SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
    String[] sentences = sentenceDetector.sentDetect(doc);
    return sentences;

  }

}
