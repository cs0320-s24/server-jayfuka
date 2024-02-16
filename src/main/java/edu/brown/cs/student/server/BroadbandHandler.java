package edu.brown.cs.student.server;

import edu.brown.cs.student.activity.Census;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

    private ACSDataSource source;  // Declare ACSDataSource as an instance variable

    public BroadbandHandler() {
        this.source = new ACSDataSource();  // Initialize ACSDataSource in the constructor
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String stateName = request.queryParams("state");
        String countyName = request.queryParams("county");
        try {
            return createSuccessResponse(source.broadbandWithCache(stateName, countyName));
        } catch (Exception e) {
            return createErrorResponse("CSV search failed: " + e.getMessage());
        }
    }

    private Map<String, Object> createSuccessResponse(Object body) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("body", body);
        responseMap.put("status", "success");
        return responseMap;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", message);
        responseMap.put("status", "error");
        return responseMap;
    }


}
