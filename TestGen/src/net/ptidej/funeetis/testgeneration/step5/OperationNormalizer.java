package net.ptidej.funeetis.testgeneration.step5;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OperationNormalizer {
	public static String extractIntent(String operation) {
	    // Normalize
	    String normalizedOp = operation.toLowerCase();

	    // Define patterns to extract the keyword
	    List<Pattern> patterns = List.of(
	        Pattern.compile("send(.*?)data"),
	        Pattern.compile("send(.*?)command"),
	        Pattern.compile("send(.*?)status")
	    );

	    for (Pattern pattern : patterns) {
	        Matcher matcher = pattern.matcher(normalizedOp);
	        if (matcher.find()) {
	            String extracted = matcher.group(1);
	            // Capitalize first letter, lowercase the rest
	            return capitalize(extracted);
	        }
	    }

	    // Fallback: use the whole operation (capitalized)
	    return capitalize(operation);
	}

	private static String capitalize(String s) {
	    if (s == null || s.isEmpty()) return s;
	    return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
}
