package net.ptidej.funeetis.testdatageneration.runner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.ptidej.funeetis.testdatageneration.step1.FileToJsonConverter;
import net.ptidej.funeetis.testdatageneration.step2.TestScenario;
import net.ptidej.funeetis.testdatageneration.step3.ArtifactExtractor;
import net.ptidej.funeetis.testdatageneration.step3.Preconditions;

public class TestDataGenerator {
	static String UCName;
	static JSONObject preconditions;
	static JSONObject basicFlow;
	static JSONArray boundedFlows;
	static JSONArray specificFlows;

	public static void main(String[] args) {
		String jsonFileName = "";
		JSONObject ucsSections = null;
		if (args.length > 0) {
			String filePath = args[0];
			FileToJsonConverter converter = new FileToJsonConverter(filePath);
			jsonFileName = createJsonFileNameFromFilePath(filePath);
			ucsSections = converter.parseFileToJson();
			processJsonObjects(ucsSections);
			converter.createJsonFile(jsonFileName, UCName);
			String strPreconditions = preconditions.toString();
			converter.createJsonFile(jsonFileName, strPreconditions);

			String strBasicFlow = basicFlow.toString();
			converter.createJsonFile(jsonFileName, strBasicFlow);

			String strBoundedFlows = boundedFlows.toString();
			converter.createJsonFile(jsonFileName, strBoundedFlows);

			String strSpecificFlows = specificFlows.toString();
			converter.createJsonFile(jsonFileName, strSpecificFlows);

		} else {
			System.out.println("No Excel file is provided.");
		}

		String inputFilePath = "ucs.json"; // Path to the input JSON file
		String outputFilePath = "scenarios.json"; // Path to the output JSON file
		TestScenario testScenario = new TestScenario(inputFilePath);
		testScenario.saveScenariosToFile(outputFilePath);
		// If you want to use the scenarios within the application, you can access them
		// like this:
		JSONObject scenarios = testScenario.getScenarios();
		System.out.println(scenarios.toString(4)); // Print formatted JSON
		Preconditions preConditions = new Preconditions(inputFilePath);
		JSONArray preconditions = preConditions.getPreconditions();
		JSONObject prconditions=new JSONObject();	
		for (int i = 0; i < preconditions.length(); i++) {
			JSONObject pc = preconditions.getJSONObject(i);
			for (String myKey : pc.keySet()) {
				prconditions.put(myKey, pc.getString(myKey));
            }
		}
		JsonObject prConditions=new JsonObject();
		prConditions=JsonParser.parseString(prconditions.toString()).getAsJsonObject();
		
		
		if (prConditions.size() == 0) {
			System.out.println("No preconditions found.");
		} else {
			preConditions.writePreconditionsToFileJsonObject(prConditions);
			System.out.println(preConditions);
		}
		
		ArtifactExtractor ae=new ArtifactExtractor();
		ae.artifactExtractor(scenarios);		      
    }
    public static JSONObject gsonToJson(String gsonInput) {
    	return new JSONObject(gsonInput);
    }
    

	private static String createJsonFileNameFromFilePath(String filePath) {
		// Extract the file name without extension
		String jsonName = filePath.contains(".") ? filePath.substring(0, filePath.lastIndexOf('.')) : "default";
		// Format the new JSON file name
		return jsonName + ".json";
	}

	public static void processJsonObjects(JSONObject ucsSections) {
		try {
			UCName = ucsSections.optString("uc_name"); // Using optString to avoid potential exceptions
			preconditions = ucsSections.optJSONObject("preconditions"); // Opt methods return null if not found
			basicFlow = ucsSections.optJSONObject("basic_flow");
			boundedFlows = ucsSections.optJSONArray("bounded_flow");
			specificFlows = ucsSections.optJSONArray("specific_flow");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}