package edu.brown.cs.stats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

import edu.brown.cs.categorizer.Emotion;
import edu.brown.cs.categorizer.EmotionCategorizer;
import edu.brown.cs.categorizer.EmotionDictionary;
import edu.brown.cs.categorizer.SentimentCategorizer;
import edu.brown.cs.categorizer.TopicCategorizer;
import edu.brown.cs.parsing.ArticleParser;

public class StatsGenerator {
  private static final String EMOTION_DICTIONARY = "emotionDictionary.txt";
  private static final Pattern WORD = Pattern.compile("\\b([a-z][-'a-z]*)\\b");
  private static final Pattern VOWEL = Pattern.compile("[aeiouy]");
  private static final Pattern HYPHENATION =
      Pattern.compile("[a-z]{2,}-[a-z]{2,}");
  private static final Pattern SYLLABLE =
      Pattern.compile("[bcdfghjklmnpqrstvwxz]"
          + "*[aeiouy]+[bcdfghjklmnpqrstvwxz]*");

  private EmotionCategorizer emotion;
  private SentimentCategorizer sentiment;
  private TopicCategorizer topic;

  public StatsGenerator() throws IOException {
    emotion = new EmotionCategorizer(new EmotionDictionary(EMOTION_DICTIONARY));
    sentiment = new SentimentCategorizer();
    topic = new TopicCategorizer();
  }

  public Map<String, Double> moods(ArticleParser p, Stats s) {
    Map<String, Double> moods = new HashMap<>();
    for (Emotion e : emotion.getTopEmotions(p.text())) {
      double score = (double) e.getPresent() / (double) s.words();
      moods.put(e.getEmotion(), score);
    }
    return moods;
  }

  public Map<Integer, Double> sentiment(ArticleParser p, Stats s) {
    Map<Integer, Double> sent = new HashMap<>();
    double pos = 0;
    double neg = 0;
    for (String sentence : p.sentences()) {
      if (sentiment.classify(sentence) == 0) {
        neg++;
      } else {
        pos++;
      }
    }
    sent.put(0, neg / s.sentences);
    sent.put(1, pos / s.sentences);
    return sent;
  }

  public String topic(ArticleParser p) {
    return topic.classifyNewDoc(p.text());
  }

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

    stats.sentences++;
  }

  private static int syllables(String w) {
    Matcher m = SYLLABLE.matcher(w);
    int count = 0;
    while (m.find()) {
      count++;
    }
    return w.charAt(w.length() - 1) != 'e' ? count - 1 : count;
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
