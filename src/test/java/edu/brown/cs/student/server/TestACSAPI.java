package edu.brown.cs.student.server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestACSAPI {

    @Test
    public void testAPI() throws IOException, URISyntaxException, InterruptedException {
        ACSDataSource source = new ACSDataSource();
        String stateURL = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";
        System.out.println(source.sendRequest(stateURL));
//        HttpURLConnection clientConnection = tryRequest("order");
//        // Get an OK response (the *connection* worked, the *API* provides an error response)
//        assertEquals(200, clientConnection.getResponseCode());
//
//        // Now we need to see whether we've got the expected Json response.
//        // SoupAPIUtilities handles ingredient lists, but that's not what we've got here.
//        Moshi moshi = new Moshi.Builder().build();
//        // We'll use okio's Buffer class here
//        OrderHandler.SoupNoRecipesFailureResponse response =
//                moshi
//                        .adapter(OrderHandler.SoupNoRecipesFailureResponse.class)
//                        .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
//
//        System.out.println(response);
//        // ^ If that succeeds, we got the expected response. Notice that this is *NOT* an exception, but
//        // a real Json reply.
//
//        clientConnection.disconnect();
    }

}
