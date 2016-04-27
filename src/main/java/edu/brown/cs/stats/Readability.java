package edu.brown.cs.stats;

import edu.brown.cs.stats.StatsGenerator.Stats;

/**
 * A readability score generator based off of https://readability-score.com/.
 *
 * @author sarasolano
 */
public class Readability {
  private static final int SCORES = 7;
  private Stats stat;

  public Readability(Stats s) {
    this.stat = s;
  }

  // http://en.wikipedia.org/wiki/SMOG_Index
  public double smogIndex() {
    double score =
        1.043 * Math.sqrt(stat.complex() * (30 / stat.sentences())) + 3.1291;
    return Utils.round(score, Utils.DECIMAL_PLACE);
  }

  // // http://en.wikipedia.org/wiki/SMOG
  // public double smog() {
  // double score = Math.sqrt(stat.complex() * (30 / stat.sentences())) + 3;
  // return Utils.round(score, Utils.DECIMAL_PLACE);
  // }

  // http://en.wikipedia.org/wiki/Flesch-Kincaid_Readability_Test
  public double fleschReadingEase() {
    double score = 206.835 - 1.015 * stat.words() / stat.sentences()
        - 84.6 * stat.syllables() / stat.words();
    return Utils.round(score, Utils.DECIMAL_PLACE);
  }

  // // http://en.wikipedia.org/wiki/Flesch-Kincaid_Readability_Test
  // public double fleschGradeLevel() {
  // double score = 0.39 * stat.words() / stat.sentences()
  // + 11.8 * stat.syllables() / stat.words() - 15.59;
  // return Utils.round(score, Utils.DECIMAL_PLACE);
  // }

  // http://en.wikipedia.org/wiki/Automated_Readability_Index
  public double ari() {
    double score = 4.71 * stat.characters() / stat.words()
        + 0.5 * stat.words() / stat.sentences() - 21.43;
    return Utils.round(score, Utils.DECIMAL_PLACE);
  }

  // http://en.wikipedia.org/wiki/Gunning-Fog_Index
  public double gunningFog() {
    double score = 0.4 * (stat.words() / stat.sentences()
        + 100 * stat.complex() / stat.words());
    return Utils.round(score, Utils.DECIMAL_PLACE);
  }

  // http://en.wikipedia.org/wiki/Coleman-Liau_Index
  public double colemanLiau() {
    double score = (5.89 * stat.characters() / stat.words())
        - (30 * stat.sentences() / stat.words()) - 15.8;
    return Utils.round(score, Utils.DECIMAL_PLACE);
  }

  /**
   * Gets the average readability score.
   *
   * @return the avg score
   */
  public double avg() {
    double sum = smogIndex() + fleschReadingEase() // + fleschGradeLevel() +
                                                   // smog()
        + ari() + gunningFog() + colemanLiau();
    double avg = sum / 5;
    return Utils.round(avg, Utils.DECIMAL_PLACE);
  }
}
