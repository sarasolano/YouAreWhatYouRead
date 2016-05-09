package edu.brown.cs.categorizer;

import java.util.Comparator;

/**
 * Emotion comparator for emotion categorizer.
 *
 * @author Baab
 *
 */
public class EmotionComparator implements Comparator<Emotion> {

  /**
   * Constructor for emotion comparator.
   */
  public EmotionComparator() {

  }

  @Override
  public int compare(Emotion o1, Emotion o2) {
    return Integer.compare(o2.getPresent(), o1.getPresent());
  }

}
