package net.ptidej.funeetis.testdatageneration.step3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class Preconditions {
	private String filePath;

	public Preconditions(String filePath) {
		this.filePath = filePath;
	}

	public JSONArray getPreconditions() {
		String jsonData = readJsonFile(filePath);
		if (jsonData != null) {
			return extractPreconditions(new JSONObject(jsonData));
		}
		return new JSONArray();
	}

	private String readJsonFile(String filePath) {
		try {
			return new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			System.err.println("Error reading JSON file: " + e.getMessage());
			return null;
		}
	}

	private JSONArray extractPreconditions(JSONObject data) {
		if (data.has("preconditions")) {
			return data.getJSONArray("preconditions");
		}
		return new JSONArray();
	}

	public void writePreconditionsToFile(JSONArray preconditions) {
		try {
			Path path = Paths.get("preconditions_.json");
			Files.write(path, preconditions.toString(4).getBytes());
			System.out.println("Preconditions saved to " + path.toAbsolutePath());
		} catch (IOException e) {
			System.err.println("Error writing preconditions to file: " + e.getMessage());
		}
	}
	public void writePreconditionsToFileJsonObject(JsonObject preconditions) {
		try {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        String json = gson.toJson(preconditions);

	        Path path = Paths.get("preconditions.json");
	        Files.write(path, json.getBytes());
	        System.out.println("Preconditions saved to " + path.toAbsolutePath());
	    } catch (IOException e) {
	        System.err.println("Error writing preconditions to file: " + e.getMessage());
	    }
	}
}
