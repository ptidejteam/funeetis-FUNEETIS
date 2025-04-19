package net.ptidej.funeetis.testdatageneration.step1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class FileToJsonConverter {
	private final String filePath;
	String jsonFileName;
	JSONObject orderedJson;
	JSONObject jsonObject = new JSONObject();
	JSONObject preConditionsEntry = new JSONObject();
	JSONObject basicFlowEntry = new JSONObject();
	JSONObject sFlowEntry = new JSONObject();
	JSONObject bFlowEntry = new JSONObject();
	Map<String, Object> map = new LinkedHashMap<>();

	public FileToJsonConverter(String filePath) {
		this.filePath = filePath;
	}

	public JSONObject parseFileToJson() {
		// Final JSON object

		JSONObject finalJson = new JSONObject();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

			JSONObject name = new JSONObject();
			JSONObject preconditions = new JSONObject();
			JSONObject basicFlow = new JSONObject();
			JSONArray boundedFlows = new JSONArray();
			JSONArray specificFlows = new JSONArray();
			String currentSection = "";
			StringBuilder sectionContent = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				String lowerCaseLine = line.toLowerCase().trim();
				// Detect section starts
				if (lowerCaseLine.contains("use case")) {
					name.put("u_name", lowerCaseLine);
					finalJson.put("uc_name", lowerCaseLine);
					continue;
				}
				if (lowerCaseLine.contains("precondition")) {
					saveSection(finalJson, currentSection, sectionContent, preconditions, basicFlow, boundedFlows,
							specificFlows);
					currentSection = "precondition";
					sectionContent = new StringBuilder();
					continue;
				}
				if (lowerCaseLine.contains("basic flow")) {
					saveSection(finalJson, currentSection, sectionContent, preconditions, basicFlow, boundedFlows,
							specificFlows);
					currentSection = "basic_flow";
					sectionContent = new StringBuilder();
					continue;
				}
				if (lowerCaseLine.contains("bounded alternative")) {
					saveSection(finalJson, currentSection, sectionContent, preconditions, basicFlow, boundedFlows,
							specificFlows);
					currentSection = "bounded_flow";
					sectionContent = new StringBuilder();
					continue;
				}
				if (lowerCaseLine.contains("specific alternative")) {
					saveSection(finalJson, currentSection, sectionContent, preconditions, basicFlow, boundedFlows,
							specificFlows);
					currentSection = "specific_flow";
					sectionContent = new StringBuilder();
					continue;
				}
				// Append content to current section
				sectionContent.append(line).append("\n");

			}

			saveSection(finalJson, currentSection, sectionContent, preconditions, basicFlow, boundedFlows,
					specificFlows);
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalJson;

	}

	private void saveSection(JSONObject finalJson, String currentSection, StringBuilder sectionContent,
			JSONObject preconditions, JSONObject basicFlow, JSONArray boundedFlows, JSONArray specificFlows) {
		if (sectionContent.length() == 0 || currentSection.isEmpty())
			return; // Skip empty sections

		String content = sectionContent.toString().trim();
		switch (currentSection) {
		case "precondition":
			preconditions.put("preconditions", content);
			finalJson.put("preconditions", preconditions);
			break;
		case "basic_flow":
			basicFlow.put("basic_flow", content);
			finalJson.put("basic_flow", basicFlow);
			break;
		case "bounded_flow":
			JSONObject newBoundedFlow = new JSONObject();
			newBoundedFlow.put("bounded_flow", content);
			boundedFlows.put(newBoundedFlow);
			finalJson.put("bounded_flow", boundedFlows);
			break;
		case "specific_flow":
			JSONObject newSpecificFlow = new JSONObject();
			newSpecificFlow.put("specific_flow", content);
			specificFlows.put(newSpecificFlow);
			finalJson.put("specific_flow", specificFlows);
			break;
		default:
			System.out.println("Warning: Unrecognized Section - " + currentSection);
		}
	}

	public void createJsonFile(String fileName, String content) {

		try (FileWriter file = new FileWriter(fileName)) {
			if (content.toLowerCase().contains("use case")) {
				String[] parts = content.split(":", 2);
				String ucName = parts.length > 1 ? parts[1].trim()
						: "Use Case Name Not Found or not does not follow the right format (use case : xx)";
				jsonObject.put("name", ucName);
				map.put("name", ucName);
			}
			if (content.toLowerCase().contains("precondition")) {
				JSONArray preconditionsArray = new JSONArray();
				JSONObject jsonObjectPr = new JSONObject(content);
				String preconditionValue = jsonObjectPr.getString("preconditions");
				// Split the input string by newlines
				String[] preconditionsLines = preconditionValue.split("\n");

				for (int i = 0; i < preconditionsLines.length; i++) {
					// Regular expression to match different delimiters
					String regex = "\\s*(is not|not equal|is|=)\\s*";
					String[] parts = preconditionsLines[i].split(regex, 2);
					if (parts.length == 2 && !parts[0].isEmpty() && !parts[1].isEmpty()) {
						JSONObject preCondition = new JSONObject();
						preCondition.put(parts[0].trim(), parts[1].trim());
						preconditionsArray.put(preCondition);
					} else {
						throw new RuntimeException("Precondition not propery defined");
					}
				}
				jsonObject.put("preconditions", preconditionsArray);
				map.put("preconditions", preconditionsArray);
			}
			if (content.toLowerCase().contains("basic_flow")) {
				JSONObject basicFlow = new JSONObject();
				JSONObject jsonObjectBasicFlow = new JSONObject(content);
				JSONArray basicFlowArray = new JSONArray();
				String basicFlowValue = jsonObjectBasicFlow.getString("basic_flow");
				boolean postconditionStarted = false;
				String[] postconditions = null;
				JSONArray pstConditions = new JSONArray();
				// Split the input string by newlines
				String[] basicFlowLines = basicFlowValue.split("\n");

				for (int j = 0; j < basicFlowLines.length; j++) {
					if (basicFlowLines[j].contains("RFS")) {
						continue;
					}
					if (basicFlowLines[j].toLowerCase().contains("postcondition")) {
						postconditions = Arrays.copyOfRange(basicFlowLines, j + 1, basicFlowLines.length);

						postconditionStarted = true; // Start processing postconditions from this line onward
						break;

					} else {
						JSONObject step = new JSONObject();
						if (basicFlowLines[j].contains(".")) {
							String[] parts = basicFlowLines[j].split("\\.");
							step.put(parts[0], parts[1]);
						} else {
							step.put("step", basicFlowLines[j]);
						}
						basicFlowArray.put(step);

					}

				}
				if (postconditionStarted) {

					pstConditions = getPostconditions(postconditions);
				}

				basicFlow.put("steps", basicFlowArray);
				basicFlow.put("postconditions", pstConditions);

				jsonObject.put("basic_Flow", basicFlow);

			}

			if (content.toLowerCase().contains("bounded_flow")) {
				JSONObject bflow = new JSONObject();
				JSONArray individualBF = new JSONArray();
				JSONArray jsonArrayBF = new JSONArray(content);

				for (int i = 0; i < jsonArrayBF.length(); i++) {
					boolean postconditionStarted = false;
					String[] postconditions = null;
					JSONObject bf = new JSONObject();
					JSONArray bFlowArray = new JSONArray();
					JSONArray pstConditions = new JSONArray();
					JSONObject jsonObjectBF = jsonArrayBF.getJSONObject(i);

					String BFValue = jsonObjectBF.getString("bounded_flow");
					// Split the input string by newlines
					String[] BFLines = BFValue.split("\n");

					for (int j = 0; j < BFLines.length; j++) {
						if (BFLines[j].contains("RFS")) {
							continue;
						}
						if (BFLines[j].toLowerCase().contains("postcondition")) {
							postconditions = Arrays.copyOfRange(BFLines, j + 1, BFLines.length);

							postconditionStarted = true; // Start processing postconditions from this line onward
							break;

						} else {
							JSONObject step = new JSONObject();
							if (BFLines[j].contains(".")) {
								String[] parts = BFLines[j].split("\\.");
								step.put(parts[0], parts[1]);
							} else {
								step.put("step", BFLines[j]);
							}
							bFlowArray.put(step);

						}

					}
					if (postconditionStarted) {

						pstConditions = getPostconditions(postconditions);
					}

					bf.put("steps", bFlowArray);
					bf.put("postconditions", pstConditions);
					individualBF.put(bf);
					bflow.put("bounded_flow", bf);
				}

				jsonObject.put("bounded_flows", individualBF);

			}
			if (content.toLowerCase().contains("specific_flow")) {

				JSONObject sflow = new JSONObject();
				JSONArray individualSF = new JSONArray();
				JSONArray jsonArraySF = new JSONArray(content);

				for (int i = 0; i < jsonArraySF.length(); i++) {
					boolean postconditionStarted = false;
					String[] postconditions = null;
					JSONObject sf = new JSONObject();
					JSONArray sFlowArray = new JSONArray();
					JSONArray pstConditions = new JSONArray();
					JSONObject jsonObjectSF = jsonArraySF.getJSONObject(i);

					String SFValue = jsonObjectSF.getString("specific_flow");
					// Split the input string by newlines
					String[] SFLines = SFValue.split("\n");

					for (int j = 0; j < SFLines.length; j++) {
						if (SFLines[j].contains("RFS")) {
							continue;
						}
						if (SFLines[j].toLowerCase().contains("postcondition")) {
							postconditions = Arrays.copyOfRange(SFLines, j + 1, SFLines.length);

							postconditionStarted = true; // Start processing postconditions from this line onward
							break;

						} else {
							JSONObject step = new JSONObject();
							if (SFLines[j].contains(".")) {
								String[] parts = SFLines[j].split("\\.");
								step.put(parts[0], parts[1]);
							} else {
								step.put("step", SFLines[j]);
							}
							sFlowArray.put(step);

						}

					}
					if (postconditionStarted) {

						pstConditions = getPostconditions(postconditions);
					}

					sf.put("steps", sFlowArray);
					sf.put("postconditions", pstConditions);
					individualSF.put(sf);
					sflow.put("specific_flow", sf);

				}

				jsonObject.put("specific_flows", individualSF);
			}

			file.write(jsonObject.toString(4)); // Write JSON with indentation
		} catch (IOException e) {
			System.err.println("Error writing to file: " + e.getMessage());
		}
		// System.out.println("New::::"+orderedJson.toString());
		// System.out.println("Currently::::"+jsonObject.toString());
	}

	public JSONObject getFile() {
		return orderedJson;

	}

	public JSONArray getPostconditions(String[] postconditions) {
		JSONArray postconditionsArray = new JSONArray();
		for (int k = 0; k < postconditions.length; k++) {
			String regex = "\\s*(is not|not equal|is |=)\\s*";
			String[] parts = postconditions[k].split(regex, 2);
			if (parts.length == 2 && !parts[0].isEmpty() && !parts[1].isEmpty()) {
				JSONObject postCondition = new JSONObject();
				postCondition.put(parts[0].trim(), parts[1].trim());
				postconditionsArray.put(postCondition);
			} else {
				throw new RuntimeException("Postcondition not propery defined");
			}
		}
		return postconditionsArray;
	}

}