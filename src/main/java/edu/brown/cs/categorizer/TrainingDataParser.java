package edu.brown.cs.categorizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TrainingDataParser {
  public static void main(String[] args) {
    try {
      TrainingDataParser.removeTopic("Metro","trainingnew.txt","nyt_topics.txt");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public static void removeTopic(String topic, String newFile,
      String originalFile) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(originalFile));
    FileWriter fw = new FileWriter(newFile);
    BufferedWriter bw = new BufferedWriter(fw);
    String line = br.readLine();
    while(line != null) {
      String[] lineEle = line.split(" ");
      if (!lineEle[0].equals(topic) && lineEle.length >50) {
        bw.write(line);
        bw.newLine();
      }
      line = br.readLine();
    }
    bw.close();
    br.close();

  }

}
