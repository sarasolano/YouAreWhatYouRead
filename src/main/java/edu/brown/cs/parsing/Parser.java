package edu.brown.cs.parsing;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class Parser implements Iterable<String> {
	String url;
	String text;
	DocumentPreprocessor dp;
	List<String> sentences;

	public Parser(String url) {
		Document doc;
		this.url = url;
		this.sentences = new ArrayList<>();
		try {
			doc = Jsoup.connect(url).get();
			String title = doc.select("h1").text();
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
			}
			sentences.add(sentence.toString());
		}
	}

//	// example of how to use it
//	public static void main(String[] args) {
//		Parser p = new Parser("https://en.wikipedia.org/wiki/Amazon_Reef");
//		for (String sentence : p) {
//			// you can use HasWords by doing .word() -> string
//			// will fix this to use strings at some point
//			System.out.println(sentence);
//		}
//	}

	@Override
	public Iterator<String> iterator() {
		return sentences.iterator();
	}
}
