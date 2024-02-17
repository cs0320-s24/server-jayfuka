package edu.brown.cs.student.server;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ACSDataSource {

  // Specify yes or no caching
  // Make static methods
  // 1 - retrieve broadband data with caching
  // 2 - retrieve w/o caching

  private static final String API_KEY = "47f9ee8e9ab596f0aec07dae474192f8a895fd54";
  private static final Map<String, String> stateCodeCache = new HashMap<>();
  private static final Map<String, String> countyCodeCache = new HashMap<>();

  public Object broadbandNoCache(String stateName, String countyName)
      throws URISyntaxException, IOException, InterruptedException {
    String stateURL =
        "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*" + "&key=" + API_KEY;
    List<List<String>> stateEntries = parseResponse(sendRequest(stateURL));
    String stateCode = "";
    boolean stateFound = false;

    for (List<String> entry : stateEntries) {
      if (entry.get(0).trim().equalsIgnoreCase(stateName)) {
        stateCode = entry.get(1);
        stateFound = true;
        break;
      }
    }

    if (!stateFound) {
      System.err.println("State '" + stateName + "' Not Found");
    }

    String countyURL =
        "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
            + stateCode
            + "&key="
            + API_KEY;
    List<List<String>> countyEntries = parseResponse(sendRequest(countyURL));
    String countyCode = "";
    boolean countyFound = false;

    for (List<String> entry : countyEntries) {
      if (entry.get(0).trim().equalsIgnoreCase(countyName)) {
        countyCode = entry.get(3);
        countyFound = true;
        break;
      }
    }

    if (!countyFound) {
      System.err.println("County '" + countyName + "' Not Found");
    }

    String finalURL =
        "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
            + countyCode
            + "&in=state:"
            + stateCode
            + "&key="
            + API_KEY;

    List<List<String>> broadbandData = parseResponse(sendRequest(finalURL));

    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    String time = now.toString();
    Map<String, Object> result = new HashMap<>();
    result.put("broadbandData", broadbandData);
    result.put("dateTime", time);
    return result;
  }

  public Object broadbandWithCache(String stateName, String countyName)
      throws URISyntaxException, IOException, InterruptedException {
    String stateCode = stateCodeCache.getOrDefault(stateName.toLowerCase(), null);
    boolean stateFound = false;
    if (stateCode == null) {
      String stateURL =
          "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*" + "&key=" + API_KEY;
      List<List<String>> stateEntries = parseResponse(sendRequest(stateURL));
      for (List<String> entry : stateEntries) {
        if (entry.get(0).trim().equalsIgnoreCase(stateName)) {
          stateCode = entry.get(1);
          stateCodeCache.put(stateName.toLowerCase(), stateCode);
          stateFound = true;
          break;
        }
      }

      if (!stateFound) {
        System.err.println("State Name '" + stateName + "' not found");
      }
    }

    String countyKey = stateCode + ":" + countyName;
    String countyCode = countyCodeCache.getOrDefault(countyKey.toLowerCase(), null);
    boolean countyFound = false;
    if (countyCode == null) {
      String countyURL =
          "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode;
      List<List<String>> countyEntries = parseResponse(sendRequest(countyURL));
      for (List<String> entry : countyEntries) {
        if (entry.get(0).trim().equalsIgnoreCase(countyName)) {
          countyCode = entry.get(3);
          countyCodeCache.put(countyKey.toLowerCase(), countyCode);
          countyFound = true;
          break;
        }
      }
      if (!countyFound) {
        System.err.println("County Name '" + countyName + "' not found in state: " + stateName);
      }
    }

    String finalURL =
        "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
            + countyCode
            + "&in=state:"
            + stateCode;

    List<List<String>> broadbandData = parseResponse(sendRequest(finalURL));

    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    String time = now.toString();
    Map<String, Object> result = new HashMap<>();
    result.put("broadbandData", broadbandData);
    result.put("dateTime", time);
    return result;
  }

  public List<List<String>> parseResponse(String response) {
    String[] lines = response.split("\n");
    return Arrays.stream(lines)
        .skip(1) // Skip the header row
        .map(this::parseLine)
        .filter(entry -> !entry.isEmpty())
        .toList();
  }

  private List<String> parseLine(String line) {
    return Arrays.stream(line.split(","))
        .map(item -> item.replaceAll("[\\[\\]\"]", "").trim())
        .collect(Collectors.toList());
  }

  public static String sendRequest(String urlNOW)
      throws URISyntaxException, IOException, InterruptedException {
    // Build a request to ACS API
    HttpRequest buildCensusApiRequest = HttpRequest.newBuilder().uri(new URI(urlNOW)).GET().build();

    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> sentCensusApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildCensusApiRequest, HttpResponse.BodyHandlers.ofString());

    return sentCensusApiResponse.body();
  }
}
