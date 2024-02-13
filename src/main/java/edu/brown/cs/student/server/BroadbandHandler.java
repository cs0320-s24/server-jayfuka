package edu.brown.cs.student.server;

// Add query param that isd a boolean - caching true or false
// State and county
// Uses ACSDataSource to take in a state and county, and output percent, date time, state, county, etc.

import edu.brown.cs.student.activity.Census;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;

public class BroadbandHandler {


//  @Override
//  public Object handle(Request request, Response response) throws Exception {
//    Set<String> params = request.queryParams();
//    String stateName = request.queryParams("state");
//    String countyName = request.queryParams("county");
//
//    ACSDataSource dataNow = newACSDataSource();
//
//    } catch(Exception e) {
//      e.printStackTrace();
//      // This is a relatively unhelpful exception message. An important part of this sprint will be
//      // in learning to debug correctly by creating your own informative error messages where Spark
//      // falls short.
//      responseMap.put("result", "Exception");
//    }
//    return responseMap;
//
//  }
}
