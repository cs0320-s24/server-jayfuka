//package edu.brown.cs.student.server;
//
//import com.squareup.moshi.JsonAdapter;
//import com.squareup.moshi.Moshi;
//import edu.brown.cs.student.activity.Activity;
//import edu.brown.cs.student.activity.ActivityAPIUtilities;
//import edu.brown.cs.student.activity.Census;
//import edu.brown.cs.student.activity.CensusAPIUtilities;
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import spark.Request;
//import spark.Response;
//import spark.Route;
//
//
//
//// Make this into ACSDataSource with a generic send request function taking in URL
//
//public class CensusFindStateHandler implements Route {
//
//    private final static String API_KEY = "47f9ee8e9ab596f0aec07dae474192f8a895fd54";
////    @Override
////    public Object handle(Request request, Response response) throws Exception {
////        Set<String> params = request.queryParams();
////        String stateName = request.queryParams("state");
////
////        // Creates a hashmap to store the results of the request
////        Map<String, Object> responseMap = new HashMap<>();
////        try {
////            // Sends a request to the API and receives JSON back
////            String activityJson = this.sendRequest();
////            // Deserializes JSON into an Activity
////            Census lookup = CensusAPIUtilities.deserializeActivity(activityJson);
////            lookup.getData();
////
////            //Iterate through the list
////            //TODO: Actually iterature through lookup
////            String stateCode = "001";
////
////
////
////            // Adds results to the responseMap
////            responseMap.put("result", "success");
////            responseMap.put("activity", activity);
////            return responseMap;
////        } catch (Exception e) {
////            e.printStackTrace();
////            // This is a relatively unhelpful exception message. An important part of this sprint will be
////            // in learning to debug correctly by creating your own informative error messages where Spark
////            // falls short.
////            responseMap.put("result", "Exception");
////        }
////        return responseMap;
////
////    }
////
////    private String sendRequest(
////        //Can make this more useful by asking for a URL arg
////    ) throws URISyntaxException, IOException, InterruptedException {
////        // Build a request to ACS API
////        HttpRequest buildCensusApiRequest =
////            HttpRequest.newBuilder()
////                .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*&key=" + API_KEY))
////                .GET()
////                .build();
////
////        // Send that API request then store the response in this variable. Note the generic type.
////        HttpResponse<String> sentCensusApiResponse =
////            HttpClient.newBuilder()
////                .build()
////                .send(buildCensusApiRequest, HttpResponse.BodyHandlers.ofString());
////
////
////        return sentCensusApiResponse.body();
////    }
//
//}
