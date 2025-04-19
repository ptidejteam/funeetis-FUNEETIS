package net.ptidej.funeetis.testgeneration.step5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

import net.ptidej.funeetis.wimp.instrumentation.SUT;

public class TestGenerator {
	public static void getTests() throws IOException {
		String jsonData = new String(Files.readAllBytes(Paths.get("testdata.json")));
		JSONObject testCases = new JSONObject(jsonData);
		Set<String> entities = new HashSet<>();
		StringBuilder sb = new StringBuilder();
		sb.append("package net.ptidej.funeetis.testgeneration;\n\n");
		sb.append("import static org.junit.Assert.*;\n");
		sb.append("import org.junit.Before;\n");
		sb.append("import org.junit.Test;\n\n");
		sb.append("public class GeneratedTestCases {\n\n");

		// Setup method for initializing data fetchers
		sb.append("    private FitbitDataFetcher fitbitDataFetcher;\n");
		sb.append("    private GatewayDataFetcher gatewayDataFetcher;\n");
		sb.append("    private CloudDataFetcher cloudDataFetcher;\n");
		sb.append("    private BuddyDataFetcher buddyDataFetcher;\n\n");

		sb.append("    @Before\n");
		sb.append("    public void setUp() {\n");
		for (String key : testCases.keySet()) {
			JSONObject testCase = testCases.getJSONObject(key);
			JSONArray steps = testCase.getJSONArray("steps");
			for (int i = 0; i < steps.length(); i++) {
				JSONObject step = steps.getJSONObject(i);
				String entity = step.getString("entity");
				if (entities.add(entity)) {
					String className = entity + "DataFetcher";
					String instanceName = entity.toLowerCase() + "Fetcher";
					sb.append("        ").append(instanceName).append(" = new ").append(className)
							.append("(\"TBP\");\n");
				}
			}
		}
		sb.append("    }\n\n");

		// Generate test methods
		for (String key : testCases.keySet()) {
			JSONObject testCase = testCases.getJSONObject(key);
			JSONArray steps = testCase.getJSONArray("steps");

			sb.append("    @Test\n");
			sb.append("    public void testCase").append(key).append("() {\n");

			for (int i = 0; i < steps.length(); i++) {
				JSONObject step = steps.getJSONObject(i);
				String entity = step.getString("entity");
				String operation = step.getString("operation");
				for (String methodName : SUT.getMethodNames()) {
					// Skip steps that are not "getMethodNames"
					if (!methodName.toLowerCase().contains(OperationNormalizer.extractIntent(operation).toLowerCase())) {
						continue;
					}
					JSONObject inputs = step.getJSONObject("inputs");
					JSONObject expectations = step.getJSONObject("expectations");

					String instanceName = entity.toLowerCase() + "Fetcher";
					sb.append("        assertEquals(\"").append(expectations.getString("Execution_status"))
							.append("\", ").append(instanceName).append(".").append(operation).append("(");

					String inputParams = inputs.keySet().stream().map(k -> inputs.getString(k))
							.map(s -> "\"" + s + "\"").reduce((a, b) -> a + ", " + b).orElse("");

					sb.append(inputParams);
					sb.append("));\n");

				}
			}
			sb.append("    }\n\n");
		}

		sb.append("}\n");

		// Write to a Java file
		Files.write(Paths.get("GeneratedTests.java"), sb.toString().getBytes());
		System.out.println("Test cases generated successfully.");
	}

}
