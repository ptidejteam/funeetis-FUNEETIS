package net.ptidej.funeetis.testdatageneration.step3;

import com.google.gson.JsonObject;

public class Inputs {
	private JsonObject inputs;

	public Inputs(JsonObject inputs) {
		this.inputs = inputs;
	}

	public JsonObject toJson() {
		return inputs; 
	}
}
