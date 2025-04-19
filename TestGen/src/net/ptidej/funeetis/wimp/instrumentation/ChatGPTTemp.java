package net.ptidej.funeetis.wimp.instrumentation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ChatGPTTemp {
	public static void testParameters() {
        String apiKey = "";
        String endpoint = "https://api.openai.com/v1/chat/completions";

        String jsonInputString = "{\n" +
        	    "  \"model\": \"gpt-4\",\n" +
        	    "  \"messages\": [\n" +
        	    "    {\n" +
        	    "      \"role\": \"user\",\n" +
        	    "      \"content\": \"Generate tests to test WIMP software.\"\n" +
        	    "    }\n" +
        	    "  ],\n" +
        	    "  \"temperature\": 0.7,\n" +
        	    "  \"top_p\": 0.9,\n" +
        	    "  \"max_tokens\": 100\n" +
        	    "}";

        try {
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

           // int status = conn.getResponseCode();
            
            BufferedReader br;
            if(conn.getResponseCode()>=200 && conn.getResponseCode()<300) {
            	br=new BufferedReader(new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8));
            	
            }else {
            	br=new BufferedReader(new InputStreamReader(conn.getErrorStream(),StandardCharsets.UTF_8));
            }
           StringBuilder response=new StringBuilder();
           String responseLine;
           while((responseLine=br.readLine())!=null) {
        	   response.append(responseLine.trim());
           }
          System.out.println("Response:");
          System.out.println(response.toString());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
