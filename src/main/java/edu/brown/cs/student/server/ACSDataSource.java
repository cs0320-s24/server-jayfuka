package edu.brown.cs.student.server;


import edu.brown.cs.student.activity.Census;
import edu.brown.cs.student.soup.Soup;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ACSDataSource {

  // Specify yes or no caching
  // Make static methods
  // 1 - retrieve broadband data with caching
  // 2 - retrieve w/o caching

  private final static String API_KEY = "47f9ee8e9ab596f0aec07dae474192f8a895fd54";

  private Object broadbandNoCache(String stateName, String countyName) throws URISyntaxException, IOException, InterruptedException {
    String stateURL = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";
    sendRequest(stateURL);
    //    List<List<String>> stateList = sendRequest(stateURL);
    // This doesn't work because sendRequest returns a string, not a list of strings
    //    for (entries inside statelist) {
    //      if (entry = stateName)){
                int stateCode = 0; // change to the stateCode gotten
    //      }
    //    }
    //
    String countyURL = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode;
    sendRequest(countyURL);
    //    List<List<String>> countyList = sendRequest(countyURL);
    // Iterate through the soups in the menu and return the first one
    //    for (entries inside countylist) {
    //      if (entry = countyName)){
              int countyCode = 0; // change to the stateCode gotten
    //      }
    //    }
    //
    String finalURL = "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
            + countyCode + "*&in=state:06";
    sendRequest(finalURL);

    return null;
  }

  private static Object broadbandWithCache(){

    return null;
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
