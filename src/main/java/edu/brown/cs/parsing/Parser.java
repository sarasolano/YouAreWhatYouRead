package edu.brown.cs.parsing;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class Parser implements Iterable<List<HasWord>> {
	String url;
	String text;
	DocumentPreprocessor dp;

	public Parser(String url) {
		Document doc;
		this.url = url;
		try {
			doc = Jsoup.connect(url).get();
			String title = doc.select("h1").text();
			String text = doc.select("p").text();
			String allText = title + ". " + text;
			this.text = allText;
			Reader r = new StringReader(allText);
			this.dp = new DocumentPreprocessor(r);
		} catch (IOException e) {
			System.out.println("ERROR: Problem extracting the text.");
		}
	}

	// example of how to use it
//	public static void main(String[] args) {
//		Parser p = new Parser("https://en.wikipedia.org/wiki/Amazon_Reef");
//		for (List<HasWord> sentence : p) {
//			// you can use HasWords by doing .word() -> string
//			// will fix this to use strings at some point
//			System.out.println(sentence);
//		}
//	}

	@Override
	public Iterator<List<HasWord>> iterator() {
		return (Iterator<List<HasWord>>) dp.iterator();
	}
}
