package net.ptidej.funeetis.testdatageneration.step3;

import com.google.gson.JsonObject;

public class TestData {
	private String id;
	private String description;
	private JsonObject testSteps;

	public TestData(String id, String description, JsonObject testSteps) {
		this.id = id;
		this.description = description;
		this.testSteps = testSteps; // Assuming this is a complete JSON object of steps
	}

	public JsonObject toJson() {
		JsonObject finalJson = new JsonObject();
		finalJson.addProperty("TC_ID", this.id);
		finalJson.addProperty("name", this.description);
		finalJson.add("steps", this.testSteps); // Ensuring that the JSON structure is correct
		return finalJson;
	}
}
