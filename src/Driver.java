import java.util.*;
import java.io.*;

public class Driver {
	// DEBUGGING PURPOSES
	private static final String CLASS_NAME = "Driver";

	private static final String SCRAM_PHRASE_FILE_PATH = "zin/zin.txt";
	private static final String LOG_PATH = "zout/zout.txt";
	private static BufferedWriter out = FileUtils.openBufferedWriter(LOG_PATH);

	public Driver() {
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> scrambledPhrases = Driver.getScrambledPhrases();
		ArrayList<ArrayList<Phrase>> results = new ArrayList<ArrayList<Phrase>>(
				scrambledPhrases.size());
		Unscrambler uscram = new Unscrambler();
		
		/*
		 * Iterate over scrambled phrases, unscrambling them and adding the
		 * results to an array.
		 */
		for (String scram : scrambledPhrases) {
			StringBuilder sb = new StringBuilder();
			ArrayList<Phrase> latestResult = uscram.unscramblePhrase(scram);

			for (Phrase phrase : latestResult) {
				sb.append(JsonUtils.phraseToJson(phrase) + "\n");
			}
			
			// DEBUGGING PURPOSES
			log(sb.toString());

			results.add(latestResult);
		}

		// DEBUGGING PURPOSES
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	private static ArrayList<String> getScrambledPhrases() {
		ArrayList<String> phrases = new ArrayList<String>();
		Scanner in = new Scanner(
				FileUtils.openFileInputStream(SCRAM_PHRASE_FILE_PATH));

		// DEBUGGING
		log("Reading phrases from " + SCRAM_PHRASE_FILE_PATH);
		
		while (in.hasNextLine()) {
			String line = in.nextLine();
			
			// DEBUGGING
			log("Read in phrase \"" + line + "\"");
			
			phrases.add(line);
			// phrases.add(in.nextLine().trim());
		}

		in.close();

		return phrases;
	}

	/**
	 * DEBUGGING PURPOSES
	 */
	public static void log(String msg) {
		log(CLASS_NAME, msg);
	}

	/**
	 * DEBUGGING PURPOSES
	 */
	public static void log(String obj, String msg) {
		try {
			out.write("[" + obj + "]\n" + msg + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * DEBUGGING PURPOSES
	 */
	public static void flushLog() {
		try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
