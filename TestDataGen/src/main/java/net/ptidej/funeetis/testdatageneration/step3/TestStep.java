package net.ptidej.funeetis.testdatageneration.step3;

import com.google.gson.JsonObject;

public class TestStep {
	String operation;
	String entity;
    JsonObject inputs;
    JsonObject target;
    JsonObject expectations;

    public TestStep(String operation, String entity, JsonObject inputs, JsonObject target, JsonObject expectations) {
        this.operation = operation;
        this.entity=entity;
        this.inputs = inputs != null ? inputs : new JsonObject();  // Handle null inputs
        this.target = target != null ? target : new JsonObject();  // Handle null target
        this.expectations = expectations != null ? expectations : new JsonObject();  // Handle null expectations
    }

    // Convert this TestStep to a JsonObject
    public JsonObject toJson() {
        JsonObject stepJson = new JsonObject();
        stepJson.addProperty("operation", operation);
        stepJson.addProperty("entity", entity);
        stepJson.add("inputs", inputs);
        stepJson.add("target", target);
        stepJson.add("expectations", expectations);
        return stepJson;
    }

}
