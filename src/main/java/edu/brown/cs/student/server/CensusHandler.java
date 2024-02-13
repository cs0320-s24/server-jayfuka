package edu.brown.cs.student.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.soup.Soup;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;


public class CensusHandler implements Route {

    public CensusHandler(List<Soup> menu){

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        // TODO: What are we querying?
        String soupname = request.queryParams("soupName");


        Map<String, Object> responseMap = new HashMap<>();
        // TODO: Use CSV Parser
        // TODO: What are we returning?

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
