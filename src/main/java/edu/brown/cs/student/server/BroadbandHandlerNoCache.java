package edu.brown.cs.student.server;


import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandlerNoCache implements Route {

  private ACSDataSource source; // Declare ACSDataSource as an instance variable

  public BroadbandHandlerNoCache() {
    this.source = new ACSDataSource(); // Initialize ACSDataSource in the constructor
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String stateName = request.queryParams("state");
    String countyName = request.queryParams("county");
    Map<String, Object> responseMap = new HashMap<>();
    try {
      Object lookup_result = source.broadbandNoCache(stateName, countyName);
      if (lookup_result == null) {
        throw new IllegalStateException("Null result from lookup!");
      }
      responseMap.put("broadband_data", lookup_result);
      return new BroadbandSuccessResponse(responseMap).serialize();
    } catch (IllegalStateException e) {
      return new BroadbandFailureResponse("error", e.getMessage()).serialize();
    } catch (IOException | URISyntaxException | InterruptedException e) {
      return new BroadbandFailureResponse(
              "error", "An error occurred while processing the request.")
          .serialize();
    }
  }

  public record BroadbandSuccessResponse(String response_type, Map<String, Object> responseMap) {

    public BroadbandSuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }
    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<BroadbandSuccessResponse> adapter =
            moshi.adapter(BroadbandSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  public record BroadbandFailureResponse(String response_type) {
    public BroadbandFailureResponse(String error, String message) {
      this("error");
    }

    /**
     * @return this response, serialized as Json
     */
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(BroadbandFailureResponse.class).toJson(this);
    }
  }
}
