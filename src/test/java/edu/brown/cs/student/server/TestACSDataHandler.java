package edu.brown.cs.student.server;

import org.junit.jupiter.api.Test;
import org.testng.Assert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.testng.Assert.assertThrows;

public class TestACSDataHandler {

    @Test
    void testBroadbandNoCache() throws URISyntaxException, IOException, InterruptedException {
        ACSDataSource dataFetcher = new ACSDataSource();
        String stateName = "Rhode Island";
        String countyName = "Providence County";
        Object result = dataFetcher.broadbandNoCache(stateName, countyName);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Map<String, Object> expected = new HashMap<>();
        List<List<Object>> expectedList = List.of(List.of("Providence County", "Rhode Island", "85.4", "44", "007"));
        expected.put("broadbandData", expectedList);
        expected.put("dateTime", now);
        Assert.assertEquals(expected, result);
    }

    @Test
    void testBroadbandWithCache() throws URISyntaxException, IOException, InterruptedException {
        ACSDataSource dataFetcher = new ACSDataSource();
        String stateName = "Rhode Island";
        String countyName = "Providence County";
        Object result = dataFetcher.broadbandWithCache(stateName, countyName);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Map<String, Object> expected = new HashMap<>();
        List<List<Object>> expectedList = List.of(List.of("Providence County", "Rhode Island", "85.4", "44", "007"));
        expected.put("broadbandData", expectedList);
        expected.put("dateTime", now);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testNoCacheInvalidState() throws URISyntaxException, IOException, InterruptedException {
        ACSDataSource dataFetcher = new ACSDataSource();
        String stateName = "Rhote Island"; // Correct to "Rhode Island"
        String countyName = "Providence County";

        try {
            Object result = dataFetcher.broadbandNoCache(stateName, countyName);
        } catch (IllegalStateException e) {
            assertEquals("State not found: " + stateName, e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception type thrown: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testCacheInvalidState() throws URISyntaxException, IOException, InterruptedException {
        ACSDataSource dataFetcher = new ACSDataSource();
        String stateName = "Rhote Island"; // Correct to "Rhode Island"
        String countyName = "Providence County";

        try {
            Object result = dataFetcher.broadbandWithCache(stateName, countyName);
        } catch (IllegalStateException e) {
            assertEquals("State not found: " + stateName, e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception type thrown: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testNoCacheInvalidCounty() throws URISyntaxException, IOException, InterruptedException {
        ACSDataSource dataFetcher = new ACSDataSource();
        String stateName = "Rhode Island"; // Correct to "Rhode Island"
        String countyName = "Nowhere";

        try {
            Object result = dataFetcher.broadbandNoCache(stateName, countyName);
        } catch (IllegalStateException e) {
            assertEquals("County not found: " + countyName, e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception type thrown: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void testCacheInvalidCounty() throws URISyntaxException, IOException, InterruptedException {
        ACSDataSource dataFetcher = new ACSDataSource();
        String stateName = "Rhode Island"; // Correct to "Rhode Island"
        String countyName = "Someplace";

        try {
            Object result = dataFetcher.broadbandWithCache(stateName, countyName);
        } catch (IllegalStateException e) {
            assertEquals("County not found: " + countyName, e.getMessage());
        } catch (Exception e) {
            fail("Unexpected exception type thrown: " + e.getClass().getSimpleName());
        }
    }


}
