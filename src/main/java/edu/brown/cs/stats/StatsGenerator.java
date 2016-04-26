package edu.brown.cs.stats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StatsGenerator {
  private static final Pattern WORD = Pattern.compile("\\b([a-z][-'a-z]*)\\b");
  private static final Pattern VOWEL = Pattern.compile("[aeiouy]");
  private static final Pattern HYPHENATION =
      Pattern.compile("[a-z]{2,}-[a-z]{2,}");
  private static final Pattern END_SENTENCE =
      Pattern.compile("\\b\\s*[.!?]\\s*\\b");
  private static final Pattern END_LINE_SENTENCE =
      Pattern.compile("\\b\\s*[.!?]\\s*$");
  private static final Pattern SYLLABLE =
      Pattern.compile("[bcdfghjklmnpqrstvwxz]*[aeiouy]"
          + "+[bcdfghjklmnpqrstvwxz]*");
  private static final String[] ABBREVIATIONS = new String[]{
      // personal titles
      "Mr", "Mrs", "M", "Dr", "Prof", "Det", "Insp",
      // Commercial abbreviations
      "Pty", "PLC", "Ltd", "Inc",
      // Other abbreviations
      "etc", "vs",};

  public static Stats analyze(InputStream stream) {
    Stats stat = new Stats();
    Scanner s = new Scanner(new InputStreamReader(stream));
    while (s.hasNext()) {
      analize(stat, s.nextLine());
    }
    s.close();
    return stat;
  }

  public static Stats analyze(File file) {
    try {
      Stats stat = new Stats();
      Scanner s = new Scanner(new FileReader(file));
      while (s.hasNext()) {
        analize(stat, s.nextLine());
      }
      s.close();
    } catch (FileNotFoundException e) {
      System.out.println("Error: " + e.getMessage());
    }
    return null;
  }

  public static Stats analyze(Iterator<String> iter) {
    Stats stat = new Stats();
    while (iter.hasNext()) {
      analize(stat, iter.next());
    }
    return stat;
  }

  private static void analize(Stats st, String line) {
    Stats stats = st == null ? new Stats() : st;
    String s = line.toLowerCase().trim();
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

    // replace abbreviations to not confuse it with the end of the sentences
    s = replaceAbbreviations(s);

    // clean up quotation marks
    s.replaceAll("[\"']", "");

    m = END_SENTENCE.matcher(s);

    while (m.find()) {
      stats.sentences++;
    }

    m = END_LINE_SENTENCE.matcher(s);

    if (m.find()) {
      stats.sentences++;
    }
  }

  private static int syllables(String w) {
    Matcher m = SYLLABLE.matcher(w);
    int count = 0;
    while (m.find()) {
      count++;
    }
    return w.charAt(w.length() - 1) != 'e' ? count - 1 : count;
  }

  private static String replaceAbbreviations(String w) {
    String toReturn = w;
    for (String ab : ABBREVIATIONS) {
      toReturn = toReturn.replaceAll("\\s" + ab + "\\.\\s", ab);
    }
    return toReturn;
  }

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