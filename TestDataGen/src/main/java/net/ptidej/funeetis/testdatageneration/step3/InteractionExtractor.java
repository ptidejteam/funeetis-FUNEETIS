package net.ptidej.funeetis.testdatageneration.step3;

import org.json.JSONObject;

public class InteractionExtractor {
	public JSONObject extractDetails(String description) {
		// String description = step.getString("1"); // Assume step is a JSONObject with
		// a single key-pair
		JSONObject details = new JSONObject();
		// Extract operation
		details.put("operation", extractOperation(description));

		// Extract target and protocol
		if (description.contains("OVER")) {
			String[] parts = description.split("OVER");
			details.put("protocol", parts[1].trim());
			details.put("target", extractTarget(parts[0]));
		}

		// Extract inputs
		details.put("inputs", extractInputs(description));

		return details;
	}

	private String extractOperation(String description) {
		String[] words = description.split(" ");
		for (String word : words) {
			if (word.equals("SENDS") || word.equals("RECEIVES") || word.equals("SETS") || word.equals("MOVES")
					|| word.equals("VALIDATES")) {
				return word;
			}
		}
		return "N/A"; // Default operation if none matched
	}

	private JSONObject extractTarget(String part) {
		JSONObject target = new JSONObject();
		if (part.contains("TO")) {
			String[] elements = part.split("TO");
			target.put("name", elements[1].trim().split(" ")[0]); // Assumes target name is the first word after "TO"
		} else if (part.contains("FROM")) {
			String[] elements = part.split("FROM");
			target.put("name", elements[1].trim().split(" ")[0]); // Assumes target name is the first word after "FROM"
		}
		return target;
	}

	private JSONObject extractInputs(String description) {
		JSONObject inputs = new JSONObject();
		if (description.contains("with")) {
			String inputPart = description.split("with")[1].trim();
			String[] params = inputPart.split("(AND|,)"); // Splits by "AND" or ","
			for (String param : params) {
				String[] keyValue = param.trim().split(" ");
				inputs.put(keyValue[0], keyValue[1]); // Assumes format "key value"
			}
		}
		return inputs;
	}

}
