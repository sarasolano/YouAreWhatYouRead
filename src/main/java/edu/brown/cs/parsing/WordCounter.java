package edu.brown.cs.parsing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import com.google.gson.Gson;

public class WordCounter {

	private HashMap<String, Integer> wc;
	private PriorityQueue<Word> words;
	private Collection<Word> top20;
	private Set<String> top3;

	public WordCounter() {
		wc = new HashMap<>();
		words = new PriorityQueue<>();
		top20 = new ArrayList<>();
		top3 = new HashSet<>();
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
		for (int i = 1; i <= 3; i++) {
			Word w = words.poll();
			if (w != null) {
				top20.add(w);
				top3.add(w.text);
			}
		}
		for (int i = 4; i <= 30; i++) {
			Word w = words.poll();
			if (w != null) {
				top20.add(w);
			}
		}
		return new Gson().toJson(top20);
	}

	public Set<String> topThree() {
		return top3;
	}

	private class Word implements Comparable<Word> {
		String text;
		int weight;

		private Word(String text, int weight) {
			this.text = text;
			this.weight = weight;
		}

		@Override
		public int compareTo(Word o) {
			if (this.weight < o.weight) {
				return 1;
			} else if (this.weight == o.weight) {
				return 0;
			} else {
				return -1;
			}
		}
	}

}
