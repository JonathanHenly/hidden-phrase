import java.io.*;
import java.util.*;

import javax.swing.event.ListSelectionEvent;

public class Unscrambler {
	// DEBUGGING PURPOSES
	private static final String CLASS_NAME = "Unscrambler";
	
	// don't limit the number of word combinations
	private static final int ALL_POSSIBLE_PHRASES = -1;
	
	private static Dictionary dictionary;

	private HashSet<String> words;
	private ArrayList<Phrase> possiblePhrases;
	private HashMap<Character, Integer> letterMap;

	/**
	 * Constructs a new {@code Unscrambler}.
	 */
	public Unscrambler() {
		dictionary = new Dictionary();
	}
	
	public ArrayList<Phrase> unscramblePhrase(String phrase) {
		return unscramblePhrase(phrase, Unscrambler.ALL_POSSIBLE_PHRASES);
	}
	
	/**
	 * 
	 * @param phrase
	 * @return
	 */
	public ArrayList<Phrase> unscramblePhrase(String phrase, int numWords) {
		// DEBUGGING
		log("Received phrase: " + phrase);

		words = new HashSet<String>();
		possiblePhrases = new ArrayList<Phrase>();

		phrase = phrase.replace(" ", "");
		phrase = phrase.toUpperCase();

		// letterMap is used to make sure phrases don't have words with
		// duplicate letters
		letterMap = new HashMap<Character, Integer>(phrase.length());

		// populate the letter map
		for (Character c : phrase.toCharArray()) {
			Integer val = letterMap.get(c);

			val = (val == null) ? 1 : val + 1;
			letterMap.put(c, val);
		}
		
		unscramble(phrase);
		
		// DEBUGGING
		log("Number of possible words: " + words.size());
		
		buildPossiblePhrases(new LinkedList<String>(words), phrase.length());
		
		// DEBUGGING
		log("Number of possible phrases: " + possiblePhrases.size());
		
		return possiblePhrases;
	}

	/**
	 * 
	 * @param wordList
	 * @param maxLength
	 * @return
	 */
	private void buildPossiblePhrases(LinkedList<String> wordList, int maxLength) {
		int count = 0;

		// DEBUGGING
		/*
		 * String log = ""; for(String s : wordList) { log += s + "\n"; }
		 * log(log);
		 */

		while (count < wordList.size()) {
			// pull from the beginning, append to the end later
			String word = wordList.removeFirst();

			// decrease each letter in word that is in letter map by 1, used to
			// prevent words with duplicate letters
			for (Character c : word.toCharArray()) {
				letterMap.put(c, letterMap.get(c) - 1);
			}

			buildPossiblePhrasesHelper(new LinkedList<String>(wordList), word,
					0, maxLength);

			// reset each count in the letter map
			for (Character c : word.toCharArray()) {
				letterMap.put(c, letterMap.get(c) + 1);
			}

			// append to the end
			wordList.addLast(word);

			count += 1;
		}

	}

	/**
	 * 
	 * @param wordList
	 * @param phrase
	 * @param spaces
	 * @param maxLength
	 */
	private void buildPossiblePhrasesHelper(LinkedList<String> wordList,
			String phrase, int spaces, int maxLength) {
		String newWord = "";

		// DEBUGGING
		/*
		 * String log = "  [phrase]: " + phrase + "  [spaces]: " + spaces +
		 * "\n  [wordList size]: " + wordList.size() + " [wordList]:\n";
		 * 
		 * for(String s : wordList) { log += s + "  "; } log(log);
		 */

		if (phrase.length() - spaces == maxLength) {
			Phrase p = new Phrase(phrase);
			possiblePhrases.add(p);
		} else if (phrase.length() - spaces < maxLength) {
			int count = 0;
			String prevPhrase = "";

			while (count < wordList.size()) {
				// take a word from the beginning of the list
				newWord = wordList.removeFirst();

				// store a copy of phrase
				prevPhrase = phrase;

				// DEBUGGING
				/*
				 * String debug =
				 * String.format("  [phrase newWord]: %s  [length]: %d", phrase
				 * + " " + newWord, phrase.length() + newWord.length()); debug
				 * += String.format("  [length check]: %b", phrase.length() +
				 * newWord.length() - spaces <= maxLength); log(debug);
				 */

				// check if appending newWord to phrase will result in a phrase
				// less equal to maxLength
				if (phrase.length() + newWord.length() - spaces <= maxLength) {
					int index = 0;
					boolean noDuplicates = true;

					// check for duplicate letters in the phrase, do not change
					// to a for-each loop (the value of index is used in the
					// for-loop after the next block).
					for (index = 0; index < newWord.length(); ++index) {
						Character letter = newWord.charAt(index);
						Integer value = letterMap.get(letter);
						if (value == 0) {
							noDuplicates = false;
							break;
						} else {
							letterMap.put(letter, value - 1);
						}
					}

					if (noDuplicates) {
						phrase += " " + newWord;
						spaces += 1;

						// recursive call
						buildPossiblePhrasesHelper(new LinkedList<String>(
								wordList), phrase, spaces, maxLength);

						// adjust phrase and space count after recursive call
						phrase = prevPhrase;
						spaces -= 1;
					}

					// reset letter map values to what they were, do not change
					// to to a for-each loop.
					for (index = index - 1; index >= 0; --index) {
						Character letter = newWord.charAt(index);
						Integer value = letterMap.get(letter);
						letterMap.put(letter, value + 1);
					}
				}

				// add newWord to the end of the list
				wordList.addLast(newWord);

				count += 1;
			}
		}

	}

	/**
	 * 
	 * @param phrase
	 *            the phrase to unscramble.
	 */
	private void unscramble(String phrase) {
		// DEBUGGING PURPOSES
		log(phrase);

		for (int i = 0; i < phrase.length(); ++i) {
			getPossibleWords(phrase.substring(0, i) + phrase.substring(i + 1),
					phrase.charAt(i) + "");
		}

	}

	/**
	 * Helper function for unscramble.
	 * 
	 * Goes through every combination of the characters in {@code phrase} and
	 * checks them against the backing dictionary. This method runs in
	 * {@code n=0 i=phrase.length() sum(n!/(n-k)!)} time.
	 * 
	 * @param phrase
	 *            get every combination of its characters.
	 * @param word
	 *            used for recursion.
	 */
	private void getPossibleWords(String phrase, String word) {
		// log(String.format("word: %10s || letters left: %10s", word, phrase));
		if (dictionary.isAWord(word)) {
			words.add(word);
		}

		for (int i = 0; i < phrase.length(); ++i) {
			getPossibleWords(phrase.substring(0, i) + phrase.substring(i + 1),
					word + phrase.charAt(i));
		}
	}

	/**
	 * DEBUGGING PURPOSES
	 */
	private static void log(String msg) {
		Driver.log(CLASS_NAME, msg);
	}

}
