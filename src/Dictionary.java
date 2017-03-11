import java.io.*;
import java.util.*;

public class Dictionary {
	// DEBUGGING PURPOSES
	private static final String CLASS_NAME = "Dictionary";

	private static final String DIC_PATH = "zin/dictionary.txt";
	private static final int INIT_CAPACITY = 200000;
	private HashSet<String> dictionary;

	/**
	 * Constructs and fills a new Dictionary from the dictionary path
	 * {@value #DIC_PATH} with an initial capacity of {@value #INIT_CAPACITY}.
	 */
	public Dictionary() {
		dictionary = new HashSet<String>(INIT_CAPACITY);

		this.fillDictionary();
	}

	/**
	 * Fill the dictionary with words from {@value #DIC_PATH}.
	 */
	private void fillDictionary() {
		Scanner in = new Scanner(FileUtils.openFileInputStream(DIC_PATH));

		while (in.hasNextLine()) {
			dictionary.add(in.nextLine().trim().toUpperCase());
		}

		in.close();
	}

	/**
	 * 
	 * @param word
	 *            word to look up in the dictionary.
	 * @return true if the word is in the dictionary, false otherwise.
	 */
	public boolean isAWord(String word) {
		return dictionary.contains(word.toUpperCase());
	}

	/**
	 * DEBUGGING PURPOSES
	 */
	private static void log(String msg) {
		Driver.log(CLASS_NAME, msg);
	}

}
