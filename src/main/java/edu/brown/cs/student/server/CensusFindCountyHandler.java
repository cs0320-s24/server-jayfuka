package edu.brown.cs.student.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.activity.Activity;
import edu.brown.cs.student.activity.ActivityAPIUtilities;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;


public class CensusFindCountyHandler implements Route {

    private String countyName;
    private String stateName;

    public CensusFindCountyHandler(String countyName, String stateName){
        this.countyName = countyName;
        this.stateName = stateName;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Set<String> params = request.queryParams();
        String participants = request.queryParams("participants");
        //     System.out.println(participants);

        // Creates a hashmap to store the results of the request
        Map<String, Object> responseMap = new HashMap<>();
        try {
            // Sends a request to the API and receives JSON back
            String activityJson = this.sendRequest(Integer.parseInt(participants));
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

    public record SoupSuccessResponse(String response_type, Map<String, Object> responseMap) {
        public SoupSuccessResponse(Map<String, Object> responseMap) {
            this("success", responseMap);
        }

        String serialize() {
            try {
                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<SoupSuccessResponse> adapter = moshi.adapter(SoupSuccessResponse.class);
                return adapter.toJson(this);
            } catch (Exception e) {

                e.printStackTrace();
                throw e;
            }
        }
    }

    /** Response object to send if someone requested soup from an empty Menu */
    public record SoupNoRecipesFailureResponse(String response_type) {
        public SoupNoRecipesFailureResponse() {
            this("error");
        }

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(SoupNoRecipesFailureResponse.class).toJson(this);
        }
    }
}
