package net.ptidej.funeetis.testdatageneration.step3;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Payload {
	private String testCaseId;
    private String testCaseName;
    private List<JsonObject> steps;

    public Payload(String testCaseId, String testCaseName, List<JsonObject> steps) {
        this.testCaseId = testCaseId;
        this.testCaseName = testCaseName;
        this.steps = steps;
    }

    public JsonObject generatePayload() {
        JsonObject testCase = new JsonObject();
        testCase.addProperty("TC_ID", this.testCaseId);
        testCase.addProperty("name", this.testCaseName);

        JsonArray stepsArray = new JsonArray();
        for (JsonObject step : this.steps) {
            stepsArray.add(step);
        }
        testCase.add("steps", stepsArray);
        return testCase;
    }

    public static JsonObject createStep(String entity,String operation,  JsonObject inputs, JsonObject target, JsonObject expectations) {
        JsonObject stepJson = new JsonObject();
        stepJson.addProperty("entity", entity);
        stepJson.addProperty("operation", operation);
        stepJson.add("inputs", inputs != null ? inputs : new JsonObject());  // Ensure null inputs are handled as empty JsonObjects
        stepJson.add("target", target != null ? target : new JsonObject());  // Ensure null target is handled as an empty JsonObject
        stepJson.add("expectations", expectations != null ? expectations : new JsonObject());  // Ensure null expectations are handled as empty JsonObjects
        return stepJson;
    }
}
