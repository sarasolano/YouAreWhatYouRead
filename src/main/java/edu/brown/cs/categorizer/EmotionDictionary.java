package edu.brown.cs.categorizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EmotionDictionary {
  private Map<String, List<Emotion>> emotionLexicons = new HashMap<String, List<Emotion>>();

  public EmotionDictionary(String dict) throws IOException {
    generateDict(dict);

  }

  private void generateDict(String dict) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(dict));
    String line = br.readLine();
    while (line != null) {
      String[] splitLine = line.split("\\s+");
      String word = splitLine[0];
      List<Emotion> emotions = emotionLexicons.get(word);
      if (emotions == null) {
        if (splitLine.length >= 3) {
          if (!splitLine[1].equals("positive")
              && !splitLine[1].equals("negative")) {
            emotions = new LinkedList<Emotion>();
            emotions
                .add(new Emotion(splitLine[1], Integer.parseInt(splitLine[2])));
            emotionLexicons.put(word, emotions);
          }

        }

      } else {
        if (splitLine.length >= 3) {
          if (!splitLine[1].equals("positive")
              && !splitLine[1].equals("negative")) {
            emotions
                .add(new Emotion(splitLine[1], Integer.parseInt(splitLine[2])));
          }

        }

      }
      line = br.readLine();
    }
    br.close();
  }

  public List<Emotion> lookup(String word) {
    return emotionLexicons.get(word);
  }

}
