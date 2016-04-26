package edu.brown.cs.categorizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EmotionCategorizer {
  Map<String, Emotion> emotions = new HashMap<String, Emotion>();

  public static void main(String[] args) {
    try {
      EmotionDictionary dict = new EmotionDictionary(
          "../readient/emotionDictionary.txt");
      EmotionCategorizer ec = new EmotionCategorizer(
          "I want to frollic around on the ground forever. Please never let go!"
              + " I want to love you forever!",
          dict);
      EmotionCategorizer ec2 = new EmotionCategorizer(
          "I hate you! leave me alone", dict);
      EmotionCategorizer ec3 = new EmotionCategorizer(
          "I love you. Hold me in your beautiful arms.", dict);
      System.out.println(ec.getTopEmotion().getEmotion());
      System.out.println(ec2.getTopEmotion().getEmotion());
      System.out.println(ec3.getTopEmotion().getEmotion());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public EmotionCategorizer(String doc, EmotionDictionary ed) {
    String line = doc;
    if (!line.isEmpty()) {
      String replaced = line.replaceAll("[^a-zA-Z]", " ");
      String whitespace = replaced.replaceAll("\\s+", " ");
      String lower = whitespace.toLowerCase();
      String[] fin = lower.split(" ");
      for (int i = 0; i < fin.length; i++) {
        if (!fin[i].equals("")) {
          String word = fin[i];
          List<Emotion> wordEmotions = ed.lookup(word);
          if (wordEmotions != null) {
            for (Emotion e : wordEmotions) {
              Emotion storedEmotion = emotions.get(e.getEmotion());
              if (storedEmotion == null) {
                emotions.put(e.getEmotion(), e);

              } else {
                storedEmotion.addEmotion(e);
              }
            }

          }

        }

      }
    }
  }

  public Emotion getTopEmotion() {
    List<Emotion> topEmotions = new LinkedList<Emotion>();
    topEmotions.addAll(emotions.values());
    topEmotions.sort(new EmotionComparator());
    if (!topEmotions.isEmpty()) {
      System.out.println(topEmotions.get(1).getEmotion());
      return topEmotions.get(0);
    } else {
      return null;
    }

  }

}
