package net.ptidej.funeetis.wimp.instrumentation;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ModuleOrClass {
	private String moduleName;
	private JsonArray methods;
	private static List<String> allMethodNames = new ArrayList<>();

	public ModuleOrClass(String moduleName, JsonArray methods) {
		this.moduleName = moduleName;
		this.methods = methods;
		for (int i = 0; i < methods.size(); i++) {
			JsonObject method = methods.get(i).getAsJsonObject();
			allMethodNames.add(method.get("name").getAsString());
		}
	}

	public String getModuleName() {
		return moduleName;
	}

	public JsonArray getMethods() {
		return methods;
	}
	public static List<String> getAllMethodNames() {
		return allMethodNames;
	}
	@Override
	public String toString() {
		return processMethods(methods);
	}

	private String processMethods(JsonArray methods) {
		StringBuilder prompt = new StringBuilder();
		for (int i = 0; i < methods.size(); i++) {
			JsonObject method = methods.get(i).getAsJsonObject();
			prompt.append("Method Name: ").append(method.get("name").getAsString()).append("\n");
			prompt.append("Description: ").append(method.get("description").getAsString()).append("\n");
			prompt.append("Inputs:\n");
			JsonArray inputs = method.getAsJsonArray("inputs");
			for (int j = 0; j < inputs.size(); j++) {
				JsonObject input = inputs.get(j).getAsJsonObject();
				prompt.append("- ").append(input.get("paramName").getAsString()).append(" (")
						.append(input.get("paramType").getAsString()).append("): ")
						.append(input.get("description").getAsString()).append("\n  Test Data: ")
						.append(input.get("testData").getAsString()).append("\n");
			}
			JsonObject returns = method.getAsJsonObject("returns");
			prompt.append("Returns: ").append(returns.get("type").getAsString()).append(" - ")
					.append(returns.get("description").getAsString()).append("\n");

			JsonObject errorHandling = method.getAsJsonObject("errorHandling");
			JsonArray exceptions = errorHandling.getAsJsonArray("exceptions");
			prompt.append("Exceptions:\n");
			for (int k = 0; k < exceptions.size(); k++) {
				JsonObject exception = exceptions.get(k).getAsJsonObject();
				prompt.append("- ").append(exception.get("type").getAsString()).append(": ")
						.append(exception.get("message").getAsString()).append("\n");
			}
			prompt.append("\n"); // Add a newline for separation between methods
		}

		// System.out.println(prompt.toString());
		return prompt.toString();
	}

}
