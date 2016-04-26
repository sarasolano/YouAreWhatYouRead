package edu.brown.cs.stats;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatsGenerator {
  private static final Pattern WORD = Pattern.compile("\\b([a-z][-'a-z]*)\\b");
  private static final Pattern VOWEL = Pattern.compile("[aeiouy]");
  private static final Pattern HYPHENATION =
      Pattern.compile("[a-z]{2,}-[a-z]{2,}");
  private static final Pattern SYLLABLE =
      Pattern.compile("[bcdfghjklmnpqrstvwxz]*[aeiouy]"
          + "+[bcdfghjklmnpqrstvwxz]*");
          // private static final Pattern END_SENTENCE =
          // Pattern.compile("\\b\\s*[.!?]\\s*\\b");
          // private static final Pattern END_LINE_SENTENCE =
          // Pattern.compile("\\b\\s*[.!?]\\s*$");
          // private static final String[] ABBREVIATIONS = new String[]{
          // // personal titles
          // "Mr", "Mrs", "M", "Dr", "Prof", "Det", "Insp",
          // // Commercial abbreviations
          // "Pty", "PLC", "Ltd", "Inc",
          // // Other abbreviations
          // "etc", "vs",};

  // public static Stats analyze(Parser p) {
  // TODO(ssolano): make it input a concurrent parser
  // }

  private static Stats analize(Stats st, String sentence) {
    Stats stats = st == null ? new Stats() : st;
    String s = sentence.toLowerCase().trim();
    Matcher m = WORD.matcher(s);
    while (m.find()) {
      String word = m.group(1);

      // words with a vowel sound are omitted (acronyms or abbreviations)
      if (!VOWEL.matcher(word).find()) {
        continue;
      }

      // check if it is a valid hyphenated word
      if (word.contains("-") && !HYPHENATION.matcher(word).matches()) {
        continue;
      }

      stats.addWord(word);

      int syl = syllables(word);

      stats.syllables += syl;

      if (syl > 2 && word.contains("-")) {
        stats.complex++;
      }
    }

    stats.sentences++;

    // // replace abbreviations to not confuse it with the end of the sentences
    // s = replaceAbbreviations(s);
    //
    // // clean up quotation marks
    // s.replaceAll("[\"']", "");
    //
    // m = END_SENTENCE.matcher(s);
    //
    // while (m.find()) {
    // stats.sentences++;
    // }
    //
    // m = END_LINE_SENTENCE.matcher(s);
    //
    // if (m.find()) {
    // stats.sentences++;
    // }

    return stats;
  }

  private static int syllables(String w) {
    Matcher m = SYLLABLE.matcher(w);
    int count = 0;
    while (m.find()) {
      count++;
    }
    return w.charAt(w.length() - 1) != 'e' ? count - 1 : count;
  }

  // private static String replaceAbbreviations(String w) {
  // String toReturn = w;
  // for (String ab : ABBREVIATIONS) {
  // toReturn = toReturn.replaceAll("\\s" + ab + "\\.\\s", ab);
  // }
  // return toReturn;
  // }

  public static final class Stats {
    private int sentences;
    private int complex;
    private int words;
    private int syllables;
    private int characters;
    private Map<String, AtomicInteger> uniqueWords =
        new HashMap<>();

    protected void addWord(String w) {
      if (!uniqueWords.containsKey(w)) {
        uniqueWords.put(w, new AtomicInteger(0));
      }
      uniqueWords.get(w).incrementAndGet();
      words++;
    }

    public int characters() {
      return characters;
    }

    public int complex() {
      return complex;
    }

    public int sentences() {
      return sentences;
    }

    public int syllables() {
      return syllables;
    }

    public int words() {
      return words;
    }

    public Map<String, AtomicInteger> getUniqueWords() {
      return Collections.unmodifiableMap(this.uniqueWords);
    }
  }
}
