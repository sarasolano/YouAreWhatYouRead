package edu.brown.cs.parsing;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class ArticleParser implements Iterable<String> {
	private String url;
	private String text;
	private String title;
	private DocumentPreprocessor dp;
	private List<String> sentences;
	private WordCounter wc;

	public ArticleParser(String path) {
		Document doc;
		this.url = path;
		this.sentences = new ArrayList<>();
		this.wc = new WordCounter();
		try {
			Response response = Jsoup.connect(path)
			           .ignoreContentType(true)
			           .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")  
			           .referrer("http://www.google.com")   
			           .timeout(12000) 
			           .followRedirects(true)
			           .execute();
			doc = response.parse();
			title = doc.select("h1").text();
			String text = doc.select("p").text();
			String allText = title + ". " + text;
			this.text = allText;
			Reader r = new StringReader(allText);
			this.dp = new DocumentPreprocessor(r);
			makeSentences();
		} catch (IOException e) {
			System.out.println("ERROR: Problem extracting the text.");
		}
	}

  private void makeSentences() {

    for (List<HasWord> sent : dp) {
      StringBuilder sentence = new StringBuilder();
      for (HasWord word : sent) {
        sentence.append(word + " ");
        wc.increment(word.word());
      }
      sentences.add(sentence.toString());
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
	
	public String jsonCounts() {
		return wc.getJSON();
	}

	// example of how to use it
	public static void main(String[] args) {
		ArticleParser p = new ArticleParser("http://www.economist.com/blogs/democracyinamerica/2016/05/pivotal-primary");
		// ArticleParser p = new ArticleParser("http://blogs.scientificamerican.com/cross-check/psychedelic-therapy-and-bad-trips/");
		// ArticleParser p = new ArticleParser("http://www.nytimes.com/2016/05/04/us/politics/indiana-republican-democratic.html?hp&action=click&pgtype=Homepage&clickSource=story-heading&module=span-ab-top-region&region=top-news&WT.nav=top-news");
		// ArticleParser p = new ArticleParser("http://hotair.com/archives/2016/05/05/wow-im-not-ready-to-endorse-trump-says-paul-ryan/");
		// ArticleParser p = new ArticleParser("http://www.helpguide.org/articles/emotional-health/anger-management.htm");
		// ArticleParser p = new ArticleParser("http://www.bustle.com/articles/158767-10-ways-to-pull-off-a-style-youre-intimidated-by");
		for (String sentence : p) {
			// you can use HasWords by doing .word() -> string
			// will fix this to use strings at some point
			System.out.println(sentence);
		}
		System.out.println(p.jsonCounts());
	}

	@Override
	public Iterator<String> iterator() {
		return sentences.iterator();
	}
}
