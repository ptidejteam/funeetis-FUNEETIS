package net.ptidej.funeetis.wimp.instrumentation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GeneratePromptForChatGPT {
	public static void generatePrompts(JsonArray jsonArray) {
		for (JsonElement element : jsonArray) {
            JsonObject sut = element.getAsJsonObject();
            String programmingLanguage = sut.get("programmingLanguage").getAsString();
            JsonArray methods = sut.getAsJsonArray("methods");

            for (JsonElement methodElement : methods) {
                JsonObject method = methodElement.getAsJsonObject();
                String prompt = generatePrompt(method, programmingLanguage);
                System.out.println(prompt);
            }
        }
	}
	private static String generatePrompt(JsonObject method, String programmingLanguage) {
        String methodName = method.get("name").getAsString();
        String prompt= "Write code (method or function " + methodName + ") using " + programmingLanguage + " as programming language to send the captured data to firebaseURL<TBD> as json object";
        return prompt;
    }


}
