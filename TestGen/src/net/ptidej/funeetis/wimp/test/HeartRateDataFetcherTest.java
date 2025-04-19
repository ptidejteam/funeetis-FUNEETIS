package net.ptidej.funeetis.wimp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import net.ptidej.funeetis.wimpdata.fetcher.BuddyDataFetcher;
import net.ptidej.funeetis.wimpdata.fetcher.CloudDataFetcher;
import net.ptidej.funeetis.wimpdata.fetcher.FitbitDataFetcher;
import net.ptidej.funeetis.wimpdata.fetcher.GatewayDataFetcher;

public class HeartRateDataFetcherTest {
	private FitbitDataFetcher fitbitDataFetcher;
	private GatewayDataFetcher gatewayDataFetcher;
	private CloudDataFetcher cloudDataFetcher;
	private BuddyDataFetcher buddyDataFetcher;
	@Before
	public void setUp() {
		fitbitDataFetcher=new FitbitDataFetcher("https://fitbit-data-3941d-default-rtdb.firebaseio.com/fitbit.json");
		gatewayDataFetcher=new GatewayDataFetcher("https://fitbitgateway-default-rtdb.firebaseio.com/heartRate.json");
		cloudDataFetcher=new CloudDataFetcher("https://cloud-wimp-default-rtdb.firebaseio.com/fitbit.json");
		buddyDataFetcher=new BuddyDataFetcher("https://buddy4iot-default-rtdb.firebaseio.com/buddy.json");
	}

	@Test
	public void testFitfitData() {
		assertNotNull("Heart rate should not be null", fitbitDataFetcher.getHeartRate("Fitbit-1", "2025-02-05"));
		assertNotEquals("Heart rate should match expected value", (Integer)72, fitbitDataFetcher.getHeartRate("Fitbit-1", "2025-02-05"));
	}

	@Test
	public void testFitfitData2() {
		assertEquals("", (Integer) 94, (Integer) fitbitDataFetcher.getHeartRate("Fitbit-1", "2025-02-13"));
		assertEquals("", (Integer) 94, (Integer) gatewayDataFetcher.getHeartRate("Gateway-Phone", "2025-02-13"));
		assertEquals("", (Integer) 94, (Integer) cloudDataFetcher.getHeartRate("2025-02-13"));
		assertEquals("", (Double) 2.0, (Double) buddyDataFetcher.getForwardedDistance("Buddy1", "2025-02-13"));
		assertEquals("", true, (Boolean) buddyDataFetcher.hasMoveCompleted("Buddy1", "2025-02-13"));

	}
}
