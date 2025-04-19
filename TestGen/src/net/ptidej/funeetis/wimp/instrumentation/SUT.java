package net.ptidej.funeetis.wimp.instrumentation;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SUT {
	public static void processSUT() {
		String filePath = "sut.json";
		// Check if file exists
		if (!Files.exists(Paths.get(filePath))) {
			System.out.println("File does not exist: " + filePath);
			return;
		}

		// Try-with-resources to handle file reading
		try (FileReader reader = new FileReader(filePath)) {
			JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
			JsonArray nodes = jsonObject.getAsJsonArray("nodes");

			for (JsonElement element : nodes) {
				String prompt = processNode(element.getAsJsonObject());
				System.out.println(chatGPT
						.getResponse(prompt.replace("\r\n", " ").replace("\n", " ").replace("\r", " ").toString()));
			}
		} catch (Exception e) {
			System.out.println("Error reading or parsing the JSON file: " + e.getMessage());
		}
	}

	private static String processNode(JsonObject node) {
		StringBuilder prompt = new StringBuilder();
		try {
			String name = node.get("name").getAsString();
			String os = node.get("os").getAsString();
			String app = node.get("app").getAsString();
			String pl = node.get("pl").getAsString();
			JsonObject network = node.getAsJsonObject("network");
			String ip = network.get("ip").getAsString();
			int port = network.get("port").getAsInt();
			ModuleOrClass moduleOrClass = processCodeConstruct(node.getAsJsonObject("codeConstruct"));
			System.out.println("method:" + moduleOrClass.toString());
			System.out.println("Node Name: " + name);
			System.out.println("Operating System: " + os);
			System.out.println("Application: " + app);
			System.out.println("Programming Language: " + pl);
			System.out.println("IP Address: " + ip);
			System.out.println("Port: " + port);
			
			prompt.append("Generate the code for " + name + " with " + os
					+ " as operating system. The name of application is " + app + " and programming language is " + pl
					+ " the app consists of classes/modules and each has the following details for each function/method\n "
					+ moduleOrClass.toString() + " Ignore any configuration file");
			return prompt.toString();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error processing node: " + e.getMessage());
			return "";
		}
	}

	private static ModuleOrClass processCodeConstruct(JsonObject codeConstruct) {
		if (codeConstruct == null)
			return null;
		JsonObject moduleOrClassJson = codeConstruct.getAsJsonObject("moduleOrClass");
		if (moduleOrClassJson != null) {
			String moduleName = moduleOrClassJson.get("name").getAsString();
			JsonArray methods = moduleOrClassJson.getAsJsonArray("methodOrFunction");
			return new ModuleOrClass(moduleName, methods);
		}
		return null;
	}
	public static List<String> getMethodNames() {
		Set<String> uniqueSet = new LinkedHashSet<>(ModuleOrClass.getAllMethodNames());
		List<String> uniqueList = new ArrayList<>(uniqueSet);
		return uniqueList;
	}
}
