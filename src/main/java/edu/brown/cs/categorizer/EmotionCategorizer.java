package edu.brown.cs.categorizer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The emotion categorizer. Uses NRC-emotion-lexicon dictionary.
 *
 * @author Baab
 *
 */
public class EmotionCategorizer {
  EmotionDictionary ed;

  /**
   * Constructor for categorizer.
   *
   * @param dict
   *          the dictionary
   */
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
                emotions.put(e.getEmotion(),
                    new Emotion(e.getEmotion(), e.getPresent()));
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

  /**
   * Gets the top emotions in the doc.
   *
   * @param doc
   *          the string of the document
   * @return the top emotions in order
   */
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
