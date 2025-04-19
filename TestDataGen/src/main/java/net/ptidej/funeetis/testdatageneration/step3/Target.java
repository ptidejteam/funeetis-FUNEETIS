package net.ptidej.funeetis.testdatageneration.step3;

import com.google.gson.JsonObject;

public class Target {
	JsonObject target;

	public Target(JsonObject target) {
		this.target=target;
	}

	 public JsonObject target2Json() {	        
	        return target;
	    }

}
