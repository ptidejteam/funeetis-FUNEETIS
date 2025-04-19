package net.ptidej.funeetis.testgeneration;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GeneratedTestCases {

    private FitbitDataFetcher fitbitDataFetcher;
    private GatewayDataFetcher gatewayDataFetcher;
    private CloudDataFetcher cloudDataFetcher;
    private BuddyDataFetcher buddyDataFetcher;

    @Before
    public void setUp() {
        fitbitFetcher = new fitbitDataFetcher("TBP");
        gatewayFetcher = new gatewayDataFetcher("TBP");
        cloudFetcher = new cloudDataFetcher("TBP");
        buddyFetcher = new buddyDataFetcher("TBP");
    }

    @Test
    public void testCase1() {
        assertEquals("WHEEL_MOVE_FINISHED", fitbitFetcher.sendHeartData("80 bpm"));
        assertEquals("WHEEL_MOVE_FINISHED", gatewayFetcher.sendHeartData("80 bpm to cloudapp"));
        assertEquals("WHEEL_MOVE_FINISHED", cloudFetcher.sendMoveCommand("1", "1"));
        assertEquals("WHEEL_MOVE_FINISHED", buddyFetcher.sendMoveStatus("wheel_move_finished"));
    }

    @Test
    public void testCase2() {
        assertEquals("WHEEL_MOVE_FINISHED", cloudFetcher.sendRotateCommand("50", "30"));
        assertEquals("WHEEL_MOVE_FINISHED", buddyFetcher.sendRotateStatus("wheel_move_finished"));
    }

    @Test
    public void testCase3() {
        assertEquals("NOK", buddyFetcher.sendEnableWheelStatus("nok"));
        assertEquals("NOK", buddyFetcher.sendEnableWheelStatus("nok"));
    }

    @Test
    public void testCase4() {
        assertEquals("NOK", buddyFetcher.sendEnableWheelStatus("nok"));
        assertEquals("NOK", buddyFetcher.sendEnableWheelStatus("nok"));
    }

}
