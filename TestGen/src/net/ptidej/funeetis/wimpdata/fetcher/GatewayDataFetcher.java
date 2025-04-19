package net.ptidej.funeetis.wimpdata.fetcher;

import org.json.JSONObject;

import net.ptidej.funeetis.testgeneration.runner.FirebaseConnector;

public class GatewayDataFetcher {
	String firebaseUrl;
	public GatewayDataFetcher(String firebaseUrl) {
		this.firebaseUrl=firebaseUrl;
		
	}
	public Integer getHeartRate(String targetDeviceName,String targetTimestamp) {
    	FirebaseConnector fbDataFetcher=new FirebaseConnector();
        try {
            String content = fbDataFetcher.fetchData(firebaseUrl);
            JSONObject jsonObject = new JSONObject(content);
            // Iterate through each key in the JSON object
            while (jsonObject.keys().hasNext()) {
                String key = jsonObject.keys().next();
                String value = jsonObject.getString(key);
                JSONObject innerObject = new JSONObject(value);
                // Check both device name and timestamp
                if (innerObject.getString("deviceName").equals(targetDeviceName) && 
                    innerObject.getString("timestamp").startsWith(targetTimestamp)) {
                    return innerObject.getInt("heartRate");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Return null if no matching heart rate is found
    }

}
