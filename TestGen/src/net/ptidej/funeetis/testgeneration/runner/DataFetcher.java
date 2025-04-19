package net.ptidej.funeetis.testgeneration.runner;

import java.io.IOException;

import com.google.gson.JsonArray;

import net.ptidej.funeetis.testgeneration.step5.TestGenerator;
import net.ptidej.funeetis.wimp.instrumentation.ChatGPTTemp;
import net.ptidej.funeetis.wimp.instrumentation.GeneratePromptForChatGPT;
import net.ptidej.funeetis.wimp.instrumentation.SUT;
import net.ptidej.funeetis.wimpdata.fetcher.BuddyDataFetcher;
import net.ptidej.funeetis.wimpdata.fetcher.CloudDataFetcher;
import net.ptidej.funeetis.wimpdata.fetcher.FitbitDataFetcher;
import net.ptidej.funeetis.wimpdata.fetcher.GatewayDataFetcher;

/*
public class Test1 extends TestCase {
	public void test() {
		SystemDesription sd = ...;
		Assert.assertEquals(components[0].getSentValue(), 91);
		...
	}
}*/
public class DataFetcher{
	public static void main(String[] args) throws IOException {
		//To test parameters in ChatGPT API: uncomment the next line.
		//ChatGPTTemp.testParameters();
		FitbitDataFetcher fdf=new FitbitDataFetcher("https://fitbit-data-3941d-default-rtdb.firebaseio.com/fitbit.json");
		GatewayDataFetcher gdf=new GatewayDataFetcher("https://fitbitgateway-default-rtdb.firebaseio.com/heartRate.json");
		CloudDataFetcher cdf=new CloudDataFetcher("https://cloud-wimp-default-rtdb.firebaseio.com/fitbit.json");
		BuddyDataFetcher bdf=new BuddyDataFetcher("https://buddy4iot-default-rtdb.firebaseio.com/buddy.json");
		
        String deviceName = "Gateway-Phone";
        String timestamp = "2025-02-05";
        Integer fitbitHeartRate = fdf.getHeartRate(deviceName, timestamp);
        Integer gatewayHeartRate=gdf.getHeartRate(deviceName, timestamp);
        Integer cloudHeartRate=cdf.getHeartRate(timestamp);
        Boolean hasMoved=bdf.hasMoveCompleted(deviceName, timestamp);
        Double distanceMoved=bdf.getForwardedDistance(deviceName, timestamp);
        if (fitbitHeartRate != null) {
            System.out.println("Heart Rate: " + fitbitHeartRate);
        } else {
            System.out.println("No heart rate data found for the given device and timestamp.");
        }
         SUT.processSUT();
         TestGenerator.getTests();
               
	}
	
	// Method to fetch and return heart rate based on device name and timestamp
   
}
