package edu.brown.cs.categorizer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EmotionCategorizer {
  EmotionDictionary ed;

  public EmotionCategorizer(EmotionDictionary dict) {
    ed = dict;
  }

  private Map<String, Emotion> categorize(String doc) {
    Map<String, Emotion> emotions = new HashMap<String, Emotion>();
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
    return emotions;
  }

  public List<Emotion> getTopEmotions(String doc) {
    List<Emotion> topEmotions = new LinkedList<Emotion>();
    topEmotions.addAll(categorize(doc).values());
    topEmotions.sort(new EmotionComparator());
    if (!topEmotions.isEmpty()) {
      return topEmotions;
    } else {
      return new LinkedList<>();
    }
  }

}
