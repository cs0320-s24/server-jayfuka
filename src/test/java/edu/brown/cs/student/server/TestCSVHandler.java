package edu.brown.cs.student.server;

import com.squareup.moshi.Moshi;
import okio.Buffer;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCSVHandler {

    private static HttpURLConnection tryRequest(String apiCall) throws IOException {
        // Configure the connection (but don't actually send the request yet)
        URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

        // The default method is "GET", which is what we're using here.
        // If we were using "POST", we'd need to say so.
        clientConnection.setRequestMethod("GET");

        clientConnection.connect();
        return clientConnection;
    }

    @Test
    // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type
    // checker
    public void testAPI() throws IOException {
        HttpURLConnection clientConnection = tryRequest("/loadcsv");
        assertEquals(200, clientConnection.getResponseCode());

        Moshi moshi = new Moshi.Builder().build();

        System.out.println(clientConnection.getInputStream());

        clientConnection.disconnect();
    }

}
