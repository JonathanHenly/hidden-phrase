import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.OutputStream;
import java.io.Writer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class JsonUtils {
	
	public static JsonObject phraseToJson(Phrase phrase) {
		JsonObject jobj = new JsonObject();
		JsonArray jary = new JsonArray();
		
		for(String word : phrase.getWords()) {
			jary.add(word);
		}
		
		jobj.add("phrase", jary);
		
		return jobj;
	}
}
