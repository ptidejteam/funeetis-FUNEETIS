package net.ptidej.funeetis.wimpdata.fetcher;

import org.json.JSONObject;

import net.ptidej.funeetis.testgeneration.runner.FirebaseConnector;

public class BuddyDataFetcher {
	String firebaseUrl;
	public BuddyDataFetcher(String firebaseUrl) {
		this.firebaseUrl=firebaseUrl;
	}
	public Double getForwardedDistance(String targetDeviceName,String targetTimestamp) {
		double distanceForward=0;
    	FirebaseConnector fbDataFetcher=new FirebaseConnector();
        try {
            String content = fbDataFetcher.fetchData(firebaseUrl);          
            JSONObject jsonObject = new JSONObject(content);           
            // Iterate over each key in the JSONObject
            for (String key : jsonObject.keySet()) {
                JSONObject entry = jsonObject.getJSONObject(key);

                int taskID = entry.getInt("taskID");
                int heartRate = entry.getInt("heartRate");
                JSONObject inputs = entry.getJSONObject("inputs");
                JSONObject actualResults = entry.getJSONObject("actualResults");
                String message = actualResults.getString("msg");
                double distance = inputs.getDouble("distance");
                System.out.println("Task ID: " + taskID);
                System.out.println("Heart Rate: " + heartRate);
                System.out.println("Actual Results Message: " + message);

                if (taskID == 2) {
                    distanceForward=distance;
                    System.out.println("Heart Rate for taskID 2: " + heartRate);
                    
                    break; 
            }
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distanceForward; // Return null if no matching heart rate is found
    }
	public Boolean hasMoveCompleted(String targetDeviceName,String targetTimestamp) {
		FirebaseConnector fbDataFetcher=new FirebaseConnector();
        try {
            String content = fbDataFetcher.fetchData(firebaseUrl);           
            JSONObject jsonObject = new JSONObject(content);          
            // Iterate over each key in the JSONObject
            for (String key : jsonObject.keySet()) {
                JSONObject entry = jsonObject.getJSONObject(key);

                int taskID = entry.getInt("taskID");
                int heartRate = entry.getInt("heartRate");
                JSONObject actualResults = entry.getJSONObject("actualResults");
                JSONObject expectedResults = entry.getJSONObject("expectations");
                String message = actualResults.getString("msg");
                String expectedMessage=expectedResults.getString("msg");
                System.out.println("Task ID: " + taskID);         
                System.out.println("Actual Results Message: " + message);
              

                if (taskID == 2) {
                    System.out.println("Heart Rate for taskID 2: " + heartRate);
                    return message.equals(expectedMessage);
                }
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Return null if no matching heart rate is found
    }
}
