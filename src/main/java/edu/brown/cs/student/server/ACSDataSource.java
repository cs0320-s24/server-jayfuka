package edu.brown.cs.student.server;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ACSDataSource {

  // Specify yes or no caching
  // Make static methods
  // 1 - retrieve broadband data with caching
  // 2 - retrieve w/o caching

  private final static String API_KEY = "47f9ee8e9ab596f0aec07dae474192f8a895fd54";

  private static Object broadbandNoCache(){
    // 3 API calls to ACS API
    return null;
  }

  private static Object broadbandWithCache(){

    return null;
  }

  private String sendRequest(
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
