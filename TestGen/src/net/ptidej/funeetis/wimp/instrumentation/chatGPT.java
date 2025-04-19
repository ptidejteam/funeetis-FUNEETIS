package net.ptidej.funeetis.wimp.instrumentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.JSONArray;
public class chatGPT {
	public static String getResponse(String message) {
		String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "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";
        String model = "gpt-4o-mini";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");
            String body = "{\"model\":\"" + model + "\", \"messages\":[{\"role\":\"user\", \"content\":\"" + message + "\"}]}";
            con.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Check the response code
            int responseCode = con.getResponseCode();
            System.out.println("Error Code for API:::"+responseCode);
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("HTTP error code: " + responseCode);
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String errorLine;
                StringBuffer errorResponse = new StringBuffer();
                while ((errorLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorLine);
                }
                errorReader.close();
                return "HTTP error: " + responseCode + " - " + errorResponse.toString();
            }

            // Get the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            System.out.println("RESPONSE:::"+response);
            in.close();
         // Parse the JSON response using org.json
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray choices = jsonResponse.getJSONArray("choices");
            String content = choices.getJSONObject(0).getJSONObject("message").getString("content");

            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
			
}