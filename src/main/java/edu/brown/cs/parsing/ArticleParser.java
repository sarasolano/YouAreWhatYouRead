package edu.brown.cs.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class ArticleParser implements Iterable<String> {
  private String url;
  private String text;
  private String title;
  private double clickbait;
  private DocumentPreprocessor dp;
  private List<String> sentences;
  private List<String> noStopwords;
  private WordCounter wc;
  private static Set<String> stopwords;

  public ArticleParser(String path) throws IOException {
    Document doc;
    this.url = path;
    this.sentences = new ArrayList<>();
    this.noStopwords = new ArrayList<>();
    this.wc = new WordCounter();
    if (stopwords == null || stopwords.isEmpty()) {
      try {
        fillStopwords(new File("stops.txt"));
      } catch (IOException e) {
        System.out.println("ERROR: Error filling stopwords.");
      }
    }
      Response response = Jsoup.connect(path).ignoreContentType(true)
          .userAgent(
              "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
          .referrer("http://www.google.com").timeout(12000)
          .followRedirects(true).execute();
      doc = response.parse();
      title = doc.select("h1").text();
      String text = doc.select("p").text();
      int numLinks = doc.select("a").size();
      int numImages = doc.select("img").size();
//      System.out.println(numLinks);
//      System.out.println(numImages);
//      System.out.println(text.split(" ").length);
      if (text.split(" ").length < 150) {
        text = doc.body().text();
      }
      // cheap hack for buzzfeed
      if (text.split(" ").length < 500) {
          text = text + ". " + doc.select("h2").text();
      }
      int wc = text.split(" ").length;
      clickbait = ((double) numImages * numLinks) / (double) (wc);
      //System.out.println(text.split(" ").length);
      String allText = title + ". " + text;
      this.text = allText;
      Reader r = new StringReader(allText);
      this.dp = new DocumentPreprocessor(r);
      makeSentences();
  }

  private void makeSentences() {

    for (List<HasWord> sent : dp) {
      StringBuilder sentence = new StringBuilder();
      StringBuilder stopSentence = new StringBuilder();
      for (HasWord word : sent) {
        sentence.append(word + " ");
        if (!stopwords.contains(word.word().toLowerCase())) {
          wc.increment(word.word().toLowerCase());
          stopSentence.append(word + " ");
        }
      }
      sentences.add(sentence.toString());
      noStopwords.add(stopSentence.toString());
    }
  }

  public String title() {
    return title;
  }

  public String url() {
    return url;
  }

  public String text() {
    return text;
  }

  public List<String> sentences() {
    return Collections.unmodifiableList(sentences);
  }

  public List<String> stopSentences() {
    return Collections.unmodifiableList(noStopwords);
  }

  public String jsonCounts() {
    return wc.getJSON();
  }

  public static void fillStopwords(File stopwords) throws IOException {
    ArticleParser.stopwords = new HashSet<>();
    BufferedReader br = new BufferedReader(new FileReader(stopwords));
    String line = null;
    while ((line = br.readLine()) != null) {
      String word = line.trim();
      ArticleParser.stopwords.add(word);
    }
    br.close();
  }

  public Set<String> topThree() {
	  return wc.topThree();
  }

  // example of how to use it
  public static void main(String[] args) throws IOException {
	  // ArticleParser p = new ArticleParser("http://www.refinery29.com/2016/05/110220/get-on-top-safe-sex-pledge-campaign");
	  // ArticleParser p = new ArticleParser("https://en.wikipedia.org/wiki/Enda_Kenny");
	  // ArticleParser p = new ArticleParser("http://www.refinery29.com/cheap-salad-recipes#slide");
	  // ArticleParser p = new ArticleParser("https://www.washingtonpost.com/news/post-nation/wp/2016/05/09/north-carolina-justice-dept-face-monday-deadline-for-bathroom-bill/");
	  // ArticleParser p = new ArticleParser("https://spoonuniversity.com/lifestyle/20-high-protein-foods-to-eat-after-you-work-out/");
	 //ArticleParser p = new ArticleParser("https://en.wikipedia.org/wiki/Andrew_Novell");
	  // ArticleParser p = new ArticleParser("http://www.huffingtonpost.com/entry/facebook-trending-news-curators_us_5730c2c8e4b096e9f092103c");
	  //    ArticleParser p = new
 //   ArticleParser("http://www.economist.com/blogs/democracyinamerica/2016/05/pivotal-primary");
//    ArticleParser p = new
//     ArticleParser("http://blogs.scientificamerican.com/cross-check/psychedelic-therapy-and-bad-trips/");
//    ArticleParser p = new
//     ArticleParser("http://www.nytimes.com/2016/05/04/us/politics/indiana-republican-democratic.html?hp&action=click&pgtype=Homepage&clickSource=story-heading&module=span-ab-top-region&region=top-news&WT.nav=top-news");
    // ArticleParser p = new
    // ArticleParser("http://hotair.com/archives/2016/05/05/wow-im-not-ready-to-endorse-trump-says-paul-ryan/");
//     ArticleParser p = new
//     ArticleParser("http://www.helpguide.org/articles/emotional-health/anger-management.htm");
//    ArticleParser p = new ArticleParser(
//        "http://www.cnn.com/2016/05/07/americas/el-chapo-prison-transfer/");
    // ArticleParser p = new
   // ArticleParser("http://www.bustle.com/articles/158767-10-ways-to-pull-off-a-style-youre-intimidated-by");
	  //	  ArticleParser p = new ArticleParser("https://www.buzzfeed.com/carolinekee/what-the-heck-is-a-peenus?utm_term=.oxNWqX3AWG#.alx643vX6q");
	  // ArticleParser p = new ArticleParser("http://www.osbornmusic.com/jokes.html");
	  // System.out.println(p.getClickbait());
//	  for (String sentence : p) {
//      // you can use HasWords by doing .word() -> string
//      // will fix this to use strings at some point
//      System.out.println(sentence);
 //   }
//    System.out.println(p.jsonCounts());
//    System.out.println(p.topThree());
  }

  @Override
  public Iterator<String> iterator() {
    return sentences.iterator();
  }
  
  public String getClickbait() {
	  if (clickbait > 150) {
		  return "Lol is this literally Buzzfeed?";
	  } else if (clickbait > 30) {
		  return "Clickbait.";
	  } else if (clickbait > 7) {
		  return "Classy Clickbait";
	  } else if (clickbait > 5) {
		  return "Great Level!";
	  } else if (clickbait > 0.7) {
		  return "Serious Article";
	  } else {
		  return "Are you sure this isn't a book?";
	  }
  }
}
