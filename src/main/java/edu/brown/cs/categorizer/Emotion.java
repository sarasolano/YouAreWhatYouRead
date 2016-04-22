package edu.brown.cs.categorizer;

public class Emotion {
  private String emotion;
  private int present;

  public Emotion(String emotion, int present) {
    this.emotion = emotion;
    this.present = present;

  }

  public String getEmotion() {
    return emotion;

  }

  public int getPresent() {
    return present;
  }

  public void addEmotion(Emotion e) {
    if (e.getEmotion().equals(emotion)) {
      present += e.getPresent();
    }
  }

}
