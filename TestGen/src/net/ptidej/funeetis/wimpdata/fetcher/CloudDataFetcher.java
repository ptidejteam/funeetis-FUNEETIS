package net.ptidej.funeetis.wimpdata.fetcher;

import org.json.JSONObject;

import net.ptidej.funeetis.testgeneration.runner.FirebaseConnector;

public class CloudDataFetcher {
	String firebaseUrl;
	public CloudDataFetcher(String firebaseUrl) {
		this.firebaseUrl=firebaseUrl;
	}
	public Integer getHeartRate(String targetTimestamp) {  
		FirebaseConnector fbDataFetcher=new FirebaseConnector();
		int hr=0;
        try {
            String content = fbDataFetcher.fetchData(firebaseUrl);
            
            JSONObject jsonObject = new JSONObject(content);          
            // Iterate over each key in the JSONObject
            for (String key : jsonObject.keySet()) {
                JSONObject entry = jsonObject.getJSONObject(key);

                int taskID = entry.getInt("taskID");
                int heartRate = entry.getInt("heartRate");
               
                System.out.println("Task ID: " + taskID);
                System.out.println("Heart Rate: " + heartRate);
              

                if (taskID == 2) {
                    hr=heartRate;
                    System.out.println("Heart Rate for taskID 2: " + heartRate);
                    
                    break; 
            }
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hr; // Return null if no matching heart rate is found
}
}