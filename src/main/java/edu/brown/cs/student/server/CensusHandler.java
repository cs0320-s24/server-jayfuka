package edu.brown.cs.student.server;


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


public class CensusHandler implements Route {

    @Override
    public Object handle(Request request, Response response) {

        Set<String> params = request.queryParams();
        String participants = request.queryParams("participants");

        // TODO: Find the params we are looking for

        Map<String, Object> responseMap = new HashMap<>();
        try {
            String activityJson = this.sendRequest(Integer.parseInt(participants));
            Activity activity = ActivityAPIUtilities.deserializeActivity(activityJson);
            responseMap.put("result", "success");
            responseMap.put("census", activity);
            return responseMap;
        } catch (Exception e) {
            e.printStackTrace();
            responseMap.put("result", "Exception");
        }
        return responseMap;
    }

    private String sendRequest(int participantNum) throws URISyntaxException, IOException, InterruptedException {

        // TODO: Change URL
        HttpRequest buildBoredApiRequest =
                HttpRequest.newBuilder()
                        .uri(new URI("http://www.boredapi.com/api/activity?participants=" + participantNum))
                        .GET()
                        .build();

        HttpResponse<String> sentBoredApiResponse =
                HttpClient.newBuilder()
                        .build()
                        .send(buildBoredApiRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(sentBoredApiResponse);
        System.out.println(sentBoredApiResponse.body());

        return sentBoredApiResponse.body();
    }
}
