package edu.brown.cs.categorizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class ParseTwitterData {

  public static void parseDoc(String path) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(path));
    FileWriter fw = new FileWriter("../readient/twitter.txt");
    BufferedWriter bw = new BufferedWriter(fw);
    String newLine = br.readLine();
    while(newLine != null) {
      String[] line = br.readLine().split(",");
      String classify = line[0].replace("\"", "");
      String document = line[line.length -1].replace("\"", "");
      String whitespace = document.replace(" ", "");
      if(!whitespace.isEmpty() && !classify.isEmpty()) {
        String output = String.format("%s    %s",classify,document);
        bw.write(output);
        bw.newLine();
      }


      newLine = br.readLine();


    }
    br.close();
    bw.close();

  }

  public static void main(String[] args) throws Exception {
    parseDoc("../readient/trainer.csv");
  }



}
