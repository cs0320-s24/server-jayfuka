package edu.brown.cs.student.server;


import edu.brown.cs.student.activity.Census;
import edu.brown.cs.student.soup.Soup;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

  private final static String API_KEY = "47f9ee8e9ab596f0aec07dae474192f8a895fd54";

  public Object broadbandNoCache(String stateName, String countyName) throws URISyntaxException, IOException, InterruptedException {
    String stateURL = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";
    List<List<String>> stateEntries = parseResponse(sendRequest(stateURL));
    String stateCode = "0";
    for (List<String> entry : stateEntries) {
      if (entry.get(0).equals(stateName)) {
        stateCode = entry.get(1);
        break;
      }
    }
    String countyURL = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode;
    List<List<String>> countyEntries = parseResponse(sendRequest(countyURL));
    String countyCode = "0";
    for (List<String> entry : countyEntries) {
      if (entry.get(0).equals(countyName)) {
        countyCode = entry.get(3);
        break;
      }
    }
    String finalURL = "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
            + countyCode + "&in=state:" + stateCode;
    List<List<String>> finalType = parseResponse(sendRequest(finalURL));
    return finalType;
  }

  private static Object broadbandWithCache(){

    return null;
  }

  public List<List<String>> parseResponse(String response) {
    String[] lines = response.split("\n");

    return Arrays.stream(lines)
            .skip(1)  // Skip the header row
            .map(this::parseLine)
            .filter(entry -> !entry.isEmpty())
            .toList();
  }

  private List<String> parseLine(String line) {
    return Arrays.stream(line.split(","))
            .map(item -> item.replaceAll("[\\[\\]\"]", "").trim())
            .collect(Collectors.toList());
  }



  public static String sendRequest(
          String urlNOW
  ) throws URISyntaxException, IOException, InterruptedException {
    // Build a request to ACS API
    HttpRequest buildCensusApiRequest =
        HttpRequest.newBuilder()
            .uri(new URI(urlNOW))
            .GET()
            .build();

    // Send that API request then store the response in this variable. Note the generic type.
    HttpResponse<String> sentCensusApiResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildCensusApiRequest, HttpResponse.BodyHandlers.ofString());

    return sentCensusApiResponse.body();
  }

}
