package edu.brown.cs.student.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.activity.Activity;
import edu.brown.cs.student.activity.ActivityAPIUtilities;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;


public class CensusFindStateHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Set<String> params = request.queryParams();
        String stateName = request.queryParams("state");

        // Creates a hashmap to store the results of the request
        Map<String, Object> responseMap = new HashMap<>();
        try {
            // Sends a request to the API and receives JSON back
            String activityJson = this.sendRequest();
            // Deserializes JSON into an Activity
            Activity activity = ActivityAPIUtilities.deserializeActivity(activityJson);
            // Adds results to the responseMap
            responseMap.put("result", "success");
            responseMap.put("activity", activity);
            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
            // This is a relatively unhelpful exception message. An important part of this sprint will be
            // in learning to debug correctly by creating your own informative error messages where Spark
            // falls short.
            responseMap.put("result", "Exception");
        }
        return responseMap;

    }

    private String sendRequest() throws URISyntaxException, IOException, InterruptedException {
        // Build a request to ACS API
        HttpRequest buildCensusApiRequest =
            HttpRequest.newBuilder()
                .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"))
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
