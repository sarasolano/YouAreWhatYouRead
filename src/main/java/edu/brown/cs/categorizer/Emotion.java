package edu.brown.cs.categorizer;

/**
 * Emotion class for storing emotions.
 *
 * @author Baab
 *
 */
public class Emotion {
  private String emotion;
  private int present;

  /**
   * Emotion to weight.
   *
   * @param emotion
   *          the emotion
   * @param present
   *          the weight
   */
  public Emotion(String emotion, int present) {
    this.emotion = emotion;
    this.present = present;
  }

  /**
   * Get the emotion.
   *
   * @return the emotion string
   */
  public String getEmotion() {
    return emotion;

  }

  /**
   * Get the weight.
   *
   * @return the weight
   */
  public int getPresent() {
    return present;
  }

  /**
   * Add emotions together.
   *
   * @param e
   *          the emotion to add
   */
  protected void addEmotion(Emotion e) {
    if (e.getEmotion().equals(emotion)) {
      present = present + e.getPresent();
    }
  }

}
