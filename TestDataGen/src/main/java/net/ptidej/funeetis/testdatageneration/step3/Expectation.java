package net.ptidej.funeetis.testdatageneration.step3;

import com.google.gson.JsonObject;

public class Expectation {
	private JsonObject expectations;

	public Expectation(JsonObject expectations) {
		this.expectations = expectations;
	}

	public JsonObject toJson() {
		return expectations;
	}
}
