package net.ptidej.funeetis.testdatageneration.step3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ArtifactExtractor {
	public void artifactExtractor(JSONObject scenarios) {
		JsonObject testData=new JsonObject();
		JsonObject postConditions=new JsonObject();
		// Using an Iterator to go through each key in the JSONObject
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		Iterator<String> keys = scenarios.keys();
		int scenarioCount=0;
		while (keys.hasNext()) {
			scenarioCount++;
			List<JsonObject> payloadSteps=new ArrayList<>();
			JsonObject pstConditions=new JsonObject();
			String key = keys.next();
			try {
				// Retrieve the JSONObject associated with each key
				JSONObject scenarioDetail = scenarios.getJSONObject(key);
				JSONObject pstconditions=new JSONObject();
				if (scenarioDetail.has("steps")) {
					JSONArray steps = scenarioDetail.getJSONArray("steps");
					JSONArray pcs = scenarioDetail.getJSONArray("postconditions");
					
					
					for (int i = 0; i < pcs.length(); i++) {
						JSONObject pc = pcs.getJSONObject(i);
						for (String myKey : pc.keySet()) {
							pstconditions.put(myKey, pc.getString(myKey));
			            }
					}
					//JsonObject pstConditions=new JsonObject();
					pstConditions=JsonParser.parseString(pstconditions.toString()).getAsJsonObject();
					
					for (int i = 0; i < steps.length(); i++) {
						String operation ="TBD"+(i+1);						
						JSONObject step = steps.getJSONObject(i);
						Iterator<String> scenarioSteps = step.keys();						
						while (scenarioSteps.hasNext()) {
							String scStepKey = scenarioSteps.next();                        
							String scenarioStep = step.getString(scStepKey).toLowerCase();
							 if (scenarioStep.contains(" sends ")) {	
								JsonObject ts=createTestStep(scenarioStep,pstConditions,operation);
								payloadSteps.add(ts);
								
							} 							
							
						}
							
					}
					
				} else {
					System.out.println("Scenario Key: " + key + " has no steps.");
				}
			} catch (JSONException e) {
				System.err.println("Error processing JSON data for key '" + key + "': " + e.getMessage());
			}
			
			Payload payload = new Payload("TBD", "TBD", payloadSteps);
		    JsonObject jsonPayload = payload.generatePayload();
		    String prettyJson = gson.toJson(jsonPayload);
		    System.out.println(prettyJson);
			System.out.println("::::::::::::::END OF SCENARIO:::::::::::::::::::::::::: "+scenarioCount);
			testData.add(scenarioCount+"", jsonPayload);
			postConditions.add(scenarioCount+"", pstConditions);
			
		}
		writeTestDataToFileJsonObject(testData);
		writePostConditionsToFileJsonObject(postConditions);
	}
	private JsonObject createTestStep(String scenarioStep,JsonObject expectations,String operation) {
		String entity="";
		JsonObject inputs = new JsonObject();
	    JsonObject target = new JsonObject(); 
	    if (scenarioStep.contains(" sends ")) {
	        operation = getOperation(scenarioStep);
	        String[] words = scenarioStep.split("\\s+");  
	        entity=words[0];
	        if (scenarioStep.contains(" with ")) {
	            inputs = getInputs(scenarioStep);
	        }
	        if (scenarioStep.contains(" over ")) {
	            String protocol = getTargetProtocol(scenarioStep);
	            if (protocol != null) {
	                target.addProperty("protocol", protocol);
	            }
	        }
	        if (scenarioStep.contains(" to ")) {
	            String targetName = getTargetName(scenarioStep);
	            if (targetName != null) {
	                target.addProperty("name", targetName);
	            }
	        }
	    }
        Inputs inputObject=new Inputs(inputs);
        Target targetObject=new Target(target);
		Expectation expectationObject = new Expectation(expectations);
		return Payload.createStep(entity,operation,inputObject.toJson(), targetObject.target2Json(), expectationObject.toJson());
	   
	}
	public static boolean containsCondition(String sentence) {
        // Normalize the sentence to lower case to make checks case-insensitive
        sentence = sentence.toLowerCase();

        // List of condition keywords or phrases
        String[] conditionKeywords = {
            "is between", " greater than", " less than ", " not equal ", " is not ",
            "exceeds", " equals ", " is "
        };

        // Check if any of the keywords or phrases is contained in the sentence
        for (String keyword : conditionKeywords) {
            if (sentence.contains(keyword)) {
                return true;
            }
        }

        return false;
    }
	public JSONObject getCondition(String step) {
		JSONObject conditions = new JSONObject();
		// Define regex patterns for various types of conditions, capturing variable names
        String regex = "(\\w+)\\s+(is between\\s+(\\d+)\\s+and\\s+(\\d+))|" +  // Captures range conditions
                "(\\w+)\\s+(greater than|less than|equals|exceeds|is not|not equal)\\s+(\\d+)";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(step);

        // Find and collect all matches
        while (matcher.find()) {
            // Iterate through the capturing groups to find matched conditions
            for (int i = 1; i <= matcher.groupCount(); i += 3) {
                if (matcher.group(i) != null && matcher.group(i + 1) != null) {
                    String key = matcher.group(i).trim();
                    String value = matcher.group(i + 1).trim();
                    conditions.put(key, value);
                }
            }
        }
        if(conditions.isEmpty()) {
        	return null;
        }else {
       return conditions;
        }
	}
	public String getScenarioDescription(String step) {
		step = step.trim(); // Trim any leading or trailing whitespace

		if (step.startsWith("if ")) {
			step = step.substring(3); // Remove the first 3 characters ('if' and a space)
		}

		if (step.endsWith(" then")) {
			step = step.substring(0, step.length() - 5); // Remove the last 5 characters (' then')
		}

		String description = step.trim(); // Trim again to clean up any extra spaces
		return description;

	}
    public String getTargetName(String step) {
    	// Normalize the input by trimming spaces and handling case sensitivity
        String normalizedInput = step.trim().toLowerCase();

        // Find the index of "to"
        int toIndex = normalizedInput.indexOf(" to ");

        if (toIndex == -1) {
            return null; // Return null if "to" is not found
        }

        // Calculate the start index of the node name after "to"
        int startNodeIndex = toIndex + 4; // "to " has 3 characters, adding 1 for the space after "to"

        // Find the next space after the start index to determine the end of the node name
        int endNodeIndex = normalizedInput.indexOf(" ", startNodeIndex);
        if (endNodeIndex == -1) { // If there's no space, take the rest of the string
            endNodeIndex = normalizedInput.length();
        }

        // Extract the node name immediately following "to"
        String targetNode = normalizedInput.substring(startNodeIndex, endNodeIndex);

        // Return the extracted node name
        return targetNode;
    }
    
	public void getInteraction(String step) {
		// Normalize the input by trimming spaces and handling case sensitivity
		String normalizedInput = step.trim();
		// Initialize variables for nodes
		String node1, node2;

		// Check for the presence of "to" or "from"
		int toIndex = normalizedInput.toLowerCase().indexOf(" to ");
		int fromIndex = normalizedInput.toLowerCase().indexOf(" from ");

		if (toIndex == -1 && fromIndex == -1) {
			System.out.println("Invalid input: No interaction found");
			return; // Exit if no valid interaction keyword is found
		}

		// Determine which keyword is used and its position
		boolean usingTo = (toIndex != -1 && (fromIndex == -1 || toIndex < fromIndex));

		// Set indexes based on the keyword found
		int keywordIndex = usingTo ? toIndex : fromIndex;
		String keyword = usingTo ? " to " : " from ";

		// Extract Node1 (before the keyword)
		node1 = normalizedInput.substring(0, keywordIndex);
		// Get only the first word of Node1
		node1 = node1.split("\\s+")[0];

		// Extract Node2 (after the keyword)
		int startNode2 = keywordIndex + keyword.length();
		int endNode2 = normalizedInput.indexOf(" ", startNode2);
		if (endNode2 == -1) { // If there's no space, take the rest of the string
			node2 = normalizedInput.substring(startNode2);
		} else {
			node2 = normalizedInput.substring(startNode2, endNode2);
		}
		// Get only the first word of Node2
		node2 = node2.split("\\s+")[0];

		// Reverse the nodes if the keyword is "from"
		if (!usingTo) {
			String temp = node1;
			node1 = node2;
			node2 = temp;
		}
	}

	public String getTargetProtocol(String step) {
		String protocol = null;
		// Normalize the input and split into words
		String[] words = step.split("\\s+");
		// Iterate over the words to find "over"
		for (int i = 0; i < words.length - 1; i++) { // -1 to prevent out-of-bounds access
			if (words[i].equalsIgnoreCase("over")) {
				// Return the next word as the protocol
				protocol = words[i + 1];

			}
		}
		return protocol;
	}

	public JsonObject getInputs(String step) {
	    JsonObject inputs = new JsonObject();

	    // Find the index of "with"
	    int withIndex = step.toLowerCase().indexOf(" with ") + 6; // +6 to move past " with "

	    // Attempt to find the index of "over"
	    int overIndex = step.toLowerCase().indexOf(" over ");

	    // Calculate the end index for the substring extraction
	    int endIndex = (overIndex != -1) ? overIndex : step.length();

	    // Check if "with" substring is found and properly positioned before "over" if "over" exists
	    if (withIndex > 6 && (overIndex == -1 || withIndex < overIndex)) {
	        // Extract the substring from "with" to the calculated end index
	        String extracted = step.substring(withIndex, endIndex).trim();

	        // Split the string based on "," or "and"
	        String[] parts = extracted.split(",|\\band\\b");
	        for (String part : parts) {
	            String trimmedPart = part.trim();
	            if (!trimmedPart.isEmpty()) {
	                // Split each part into key and value on "="
	                String[] keyValue = trimmedPart.split("=");
	                if (keyValue.length == 2) {
	                    String key = keyValue[0].trim();
	                    String value = keyValue[1].trim();
	                    inputs.addProperty(key, value);
	                }
	            }
	        }
	    }
	    return inputs;
	}
	public String getOperation(String step) {
		  String actionName = "";
	        String targetWord = "SENDS";

	        // Split the sentence into words based on spaces
	        String[] words = step.split("\\s+");

	        // Find the index of the word "SENDS"
	        int sendIndex = -1;
	        for (int i = 0; i < words.length; i++) {
	            if (words[i].equalsIgnoreCase(targetWord)) {
	                sendIndex = i;
	                break;
	            }
	        }

	        // Check if "SENDS" is found and has at least two more words after it
	        if (sendIndex != -1 && sendIndex + 2 < words.length) {
	            // Combine "send" with the next two words
	            actionName = "send" + capitalizeFirstLetter(words[sendIndex + 1]) + capitalizeFirstLetter(words[sendIndex + 2]);
	            return actionName;
	        } else {
	            // If "SENDS" is not found or there are not enough words, return "TBD"
	            return "TBD";
	        }
	    }
	
	
	private static String capitalizeFirstLetter(String word) {
        if (word == null || word.isEmpty()) return word;
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }
	public void writeTestDataToFileJsonObject(JsonObject testdata) {
		try {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        String json = gson.toJson(testdata);

	        Path path = Paths.get("testdata.json");
	        Files.write(path, json.getBytes());
	        System.out.println("Test data (payloads) saved to " + path.toAbsolutePath());
	    } catch (IOException e) {
	        System.err.println("Error writing test data to file: " + e.getMessage());
	    }
	}
	public void writePostConditionsToFileJsonObject(JsonObject postconditions) {
		try {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        String json = gson.toJson(postconditions);

	        Path path = Paths.get("postconditions.json");
	        Files.write(path, json.getBytes());
	        System.out.println("Postconditions saved to " + path.toAbsolutePath());
	    } catch (IOException e) {
	        System.err.println("Error writing postconditions to file: " + e.getMessage());
	    }
	}
}
