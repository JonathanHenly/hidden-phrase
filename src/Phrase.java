import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Phrase {
	// DEBUGGING PURPOSES
	private static final String CLASS_NAME = "Phrase";
	private static HashSet<Phrase> dups = new HashSet<Phrase>();
	
	private String[] words;
	private int wordCount;
	private int hash;

	public Phrase(String phrase) {
		words = phrase.split(" ");
		wordCount = words.length;
		
		if(dups.contains(this)) {
			int hashCode = 0;
			Phrase other = this;
			
			Iterator<Phrase> it = dups.iterator();
			while(it != null) {
				other = it.next();
				if(other.hashCode() == this.hashCode()) {
					hashCode = other.hashCode();
					
					break;
				}
			}
			
			log(String.format("%s this: %s  that: %s  hashCode: %d", "_DUPLICATE_", this.toString(), other.toString(), hashCode));
		}
	}
	
	/**
	 * Returns a copy of the underlying array of words.
	 * 
	 * @return a copy of the underlying <code>String</code> array of words.
	 */
	public String[] getWords() {
		return Arrays.copyOf(words, words.length);
	}

	public int getWordCount() {
		return wordCount;
	}

	public int getNumSpaces() {
		return getWordCount() - 1;
	}
	
	@Override
	public int hashCode() {
		if (this.hash == 0) {
	        int h = 0;
	        int multiplier = 1;
	        for (int i = 0; i < this.words.length; ++i) {
	            h += this.words[i].hashCode() * multiplier;
	            int shifted = multiplier << 5;
	            multiplier = shifted - multiplier;
	        }
	        this.hash = h;
	    }
		
		return this.hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj instanceof Phrase) {
			Phrase that = (Phrase) obj;
			if (this.wordCount == that.wordCount) {
				for(int i = 0; i < this.wordCount; ++i) {
					if(!this.words[i].equals(that.words[i])) {
						return false;
					}
				}
				
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String toString() {
		String s = "Phrase: ";
		
		for(String word : words) {
			s += word + " ";
		}
		
		return s;
	}
	
	/**
	 * DEBUGGING PURPOSES
	 */
	private static void log(String msg) {
		Driver.log(CLASS_NAME, msg);
	}
	
}
