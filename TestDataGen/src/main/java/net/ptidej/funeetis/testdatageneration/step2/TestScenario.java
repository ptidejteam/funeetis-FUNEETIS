package net.ptidej.funeetis.testdatageneration.step2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestScenario {

	private JSONObject scenarios = new JSONObject();
	private AtomicInteger scenarioCount = new AtomicInteger(1);

	public TestScenario(String filePath) {
		String jsonData = readJsonFile(filePath);
		if (jsonData != null) {
			parseJsonData(new JSONObject(jsonData));
		}
	}

	private String readJsonFile(String filePath) {
		try {
			return new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			System.err.println("Error reading JSON file: " + e.getMessage());
			return null;
		}
	}

	private void parseJsonData(JSONObject data) {
		for (String key : new String[] { "basic_Flow", "bounded_flows", "specific_flows" }) {
			if (data.has(key)) {
				extractScenarios(data.get(key));
			}
		}
	}

	private void extractScenarios(Object flowData) {
		if (flowData instanceof JSONObject) {
			addScenario((JSONObject) flowData);
		} else if (flowData instanceof JSONArray) {
			for (int i = 0; i < ((JSONArray) flowData).length(); i++) {
				addScenario(((JSONArray) flowData).getJSONObject(i));
			}
		}
	}

	private void addScenario(JSONObject flow) {
		JSONObject scenario = new JSONObject();
		scenario.put("postconditions", flow.get("postconditions"));
		scenario.put("steps", flow.get("steps"));
		scenarios.put("" + scenarioCount.getAndIncrement(), scenario);
	}

	public void saveScenariosToFile(String filename) {
		try {
			Files.write(Paths.get(filename), scenarios.toString(4).getBytes());
			System.out.println("Scenarios saved to " + Paths.get(filename));
		} catch (IOException e) {
			System.err.println("Error writing JSON to file: " + e.getMessage());
		}
	}

	public JSONObject getScenarios() {
		return scenarios;
	}

}
