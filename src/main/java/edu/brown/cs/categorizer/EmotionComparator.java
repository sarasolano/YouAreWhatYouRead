package edu.brown.cs.categorizer;

import java.util.Comparator;

public class EmotionComparator implements Comparator<Emotion> {

  public EmotionComparator() {

  }

  @Override
  public int compare(Emotion o1, Emotion o2) {
    return Integer.compare(o2.getPresent(),o1.getPresent());
  }

}
