package edu.brown.cs.parsing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.Gson;

public class WordCounter {
	
	HashMap<String, Integer> wc;
	Collection<Word> words;
	
	public WordCounter() {
		wc = new HashMap<>();
		words = new ArrayList<>();
	}
	
	public void increment(String word) {
		if (wc.containsKey(word)) {
			int value = wc.get(word);
			wc.put(word, value + 1);
		} else {
			wc.put(word, 1);
		}
	}
	
	public String getJSON() {
		for (Entry<String, Integer> entry : wc.entrySet()) {
			Word word = new Word(entry.getKey(), entry.getValue());
			words.add(word);
		}
		return new Gson().toJson(words);
	}
	
	private class Word {
		String text;
		int weight; 
		
		private Word(String text, int weight) {
			this.text = text;
			this.weight = weight;
		}
	}

}
