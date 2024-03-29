package edu.brown.cs.student.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class TestACSDataSource {

  @Test
  void basicTestBroadbandNoCache() throws URISyntaxException, IOException, InterruptedException {
    ACSDataSource dataFetcher = new ACSDataSource();
    String stateName = "Rhode Island";
    String countyName = "Providence County";
    Map<String, Object> result =
        (Map<String, Object>) dataFetcher.broadbandNoCache(stateName, countyName);

    // Convert dateTime in result from String to LocalDateTime
    LocalDateTime resultDateTime = LocalDateTime.parse((String) result.get("dateTime"));
    result.put("dateTime", resultDateTime);

    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    Map<String, Object> expected = new HashMap<>();
    List<List<String>> expectedList =
        List.of(List.of("Providence County", "Rhode Island", "85.4", "44", "007"));
    expected.put("dateTime", now);
    expected.put("broadbandData", expectedList);

    Assert.assertEquals(expected, result);
  }

  @Test
  void broadbandNoCacheSequentialRequests()
      throws URISyntaxException, IOException, InterruptedException {
    ACSDataSource dataFetcher = new ACSDataSource();
    // Combo 1
    String stateName = "New Jersey";
    String countyName = "Ocean County";
    Map<String, Object> result =
        (Map<String, Object>) dataFetcher.broadbandNoCache(stateName, countyName);
    // Convert dateTime in result from String to LocalDateTime
    LocalDateTime resultDateTime = LocalDateTime.parse((String) result.get("dateTime"));
    result.put("dateTime", resultDateTime);

    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    Map<String, Object> expected = new HashMap<>();
    List<List<Object>> expectedList =
        List.of(List.of("Ocean County", "New Jersey", "83.0", "34", "029"));
    expected.put("broadbandData", expectedList);
    expected.put("dateTime", now);
    Assert.assertEquals(expected, result);

    // Combo 2
    stateName = "California";
    countyName = "Stanislaus County";
    result = (Map<String, Object>) dataFetcher.broadbandNoCache(stateName, countyName);
    // Convert dateTime in result from String to LocalDateTime
    resultDateTime = LocalDateTime.parse((String) result.get("dateTime"));
    result.put("dateTime", resultDateTime);

    now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    expected = new HashMap<>();
    expectedList = List.of(List.of("Stanislaus County", "California", "90.2", "06", "099"));
    expected.put("broadbandData", expectedList);
    expected.put("dateTime", now);
    Assert.assertEquals(expected, result);

    // Combo 3
    stateName = "North Carolina";
    countyName = "Cleveland County";
    result = (Map<String, Object>) dataFetcher.broadbandNoCache(stateName, countyName);
    // Convert dateTime in result from String to LocalDateTime
    resultDateTime = LocalDateTime.parse((String) result.get("dateTime"));
    result.put("dateTime", resultDateTime);

    now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    expected = new HashMap<>();
    expectedList = List.of(List.of("Cleveland County", "North Carolina", "72.0", "37", "045"));
    expected.put("broadbandData", expectedList);
    expected.put("dateTime", now);
    Assert.assertEquals(expected, result);
  }

  @Test
  void basicTestBroadbandWithCache() throws URISyntaxException, IOException, InterruptedException {
    ACSDataSource dataFetcher = new ACSDataSource();
    String stateName = "Rhode Island";
    String countyName = "Providence County";
    Map<String, Object> result =
        (Map<String, Object>) dataFetcher.broadbandWithCache(stateName, countyName);
    // Convert dateTime in result from String to LocalDateTime
    LocalDateTime resultDateTime = LocalDateTime.parse((String) result.get("dateTime"));
    result.put("dateTime", resultDateTime);

    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    Map<String, Object> expected = new HashMap<>();
    List<List<Object>> expectedList =
        List.of(List.of("Providence County", "Rhode Island", "85.4", "44", "007"));
    expected.put("broadbandData", expectedList);
    expected.put("dateTime", now);
    Assert.assertEquals(expected, result);
  }

  @Test
  void broadbandWithCacheSequentialRequests()
      throws URISyntaxException, IOException, InterruptedException {
    ACSDataSource dataFetcher = new ACSDataSource();
    // Combo 1
    String stateName = "New Jersey";
    String countyName = "Ocean County";
    Map<String, Object> result =
        (Map<String, Object>) dataFetcher.broadbandWithCache(stateName, countyName);
    // Convert dateTime in result from String to LocalDateTime
    LocalDateTime resultDateTime = LocalDateTime.parse((String) result.get("dateTime"));
    result.put("dateTime", resultDateTime);

    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    Map<String, Object> expected = new HashMap<>();
    List<List<Object>> expectedList =
        List.of(List.of("Ocean County", "New Jersey", "83.0", "34", "029"));
    expected.put("broadbandData", expectedList);
    expected.put("dateTime", now);
    Assert.assertEquals(expected, result);

    // Combo 2
    stateName = "California";
    countyName = "Stanislaus County";
    result = (Map<String, Object>) dataFetcher.broadbandWithCache(stateName, countyName);
    // Convert dateTime in result from String to LocalDateTime
    resultDateTime = LocalDateTime.parse((String) result.get("dateTime"));
    result.put("dateTime", resultDateTime);

    now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    expected = new HashMap<>();
    expectedList = List.of(List.of("Stanislaus County", "California", "90.2", "06", "099"));
    expected.put("broadbandData", expectedList);
    expected.put("dateTime", now);
    Assert.assertEquals(expected, result);

    // Combo 3
    stateName = "North Carolina";
    countyName = "Cleveland County";
    result = (Map<String, Object>) dataFetcher.broadbandWithCache(stateName, countyName);
    // Convert dateTime in result from String to LocalDateTime
    resultDateTime = LocalDateTime.parse((String) result.get("dateTime"));
    result.put("dateTime", resultDateTime);

    now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    expected = new HashMap<>();
    expectedList = List.of(List.of("Cleveland County", "North Carolina", "72.0", "37", "045"));
    expected.put("broadbandData", expectedList);
    expected.put("dateTime", now);
    Assert.assertEquals(expected, result);
  }

  @Test
  public void testNoCacheInvalidState()
      throws URISyntaxException, IOException, InterruptedException {
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
  public void testNoCacheInvalidCounty()
      throws URISyntaxException, IOException, InterruptedException {
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
  public void testCacheInvalidCounty()
      throws URISyntaxException, IOException, InterruptedException {
    ACSDataSource dataFetcher = new ACSDataSource();
    String stateName = "Rhode Island";
    String countyName = "Someplace";

    try {
      Map<String, Object> result =
          (Map<String, Object>) dataFetcher.broadbandWithCache(stateName, countyName);
    } catch (IllegalStateException e) {
      assertEquals("County not found: " + countyName, e.getMessage());
    } catch (Exception e) {
      fail("Unexpected exception type thrown: " + e.getClass().getSimpleName());
    }
  }
}
