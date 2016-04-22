package edu.brown.cs.categorizer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EmotionCategorizer {
  Map<String, Emotion> emotions = new HashMap<String, Emotion>();

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


  public Emotion getTopEmotion() {
    List<Emotion> topEmotions = new LinkedList<Emotion>();
    topEmotions.addAll(emotions.values());
    topEmotions.sort(new EmotionComparator());
    if (!topEmotions.isEmpty()) {
      return topEmotions.get(0);
    } else {
      return null;
    }

  }

}
