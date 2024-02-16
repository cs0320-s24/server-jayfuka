package edu.brown.cs.student.server;

import com.squareup.moshi.Moshi;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCSVHandler {

    final List answer = new ArrayList();

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

    @BeforeAll
    public static void setup_before_everything() {
        // Set the Spark port number. This can only be done once, and has to
        // happen before any route maps are added. Hence using @BeforeClass.
        // Setting port 0 will cause Spark to use an arbitrary available port.
        Spark.port(0);
        // Don't try to remember it. Spark won't actually give Spark.port() back
        // until route mapping has started. Just get the port number later. We're using
        // a random _free_ port to remove the chances that something is already using a
        // specific port on the system used for testing.

        // Remove the logging spam during tests
        //   This is surprisingly difficult. (Notes to self omitted to avoid complicating things.)

        // SLF4J doesn't let us change the logging level directly (which makes sense,
        //   given that different logging frameworks have different level labels etc.)
        // Changing the JDK *ROOT* logger's level (not global) will block messages
        //   (assuming using JDK, not Log4J)
        Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    }

    @BeforeEach
    public void setup() {
        // Re-initialize state, etc. for _every_ test method run
        this.answer.clear();

        // Restarting might be not a good idea?
        Spark.get("searchcsv", new CSVHandler());
        Spark.get("loadcsv", new CSVHandler());
        Spark.get("viewcsv", new CSVHandler());
        Spark.get("broadband", new BroadbandHandler());
        Spark.init();
        Spark.awaitInitialization(); // don't continue until the server is listening
    }

    @AfterEach
    public void teardown() {
        // Gracefully stop Spark listening on both endpoints after each test
        Spark.unmap("searchcsv");
        Spark.unmap("loadcsv");
        Spark.unmap("viewcsv");
        Spark.unmap("broadband");
        Spark.awaitStop(); // don't proceed until the server is stopped
    }

    @Test
    public void testLoad() throws IOException {
        HttpURLConnection clientConnection = tryRequest("loadcsv");
        assertEquals(200, clientConnection.getResponseCode());
        Moshi moshi = new Moshi.Builder().build();
        CSVHandler.CSVSuccessResponse response = moshi.adapter(CSVHandler.CSVSuccessResponse.class)
                .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
        String expected = "CSVSuccessResponse[response_type=success, responseMap={loading=success}]";
        assertEquals(expected, response.toString());
        clientConnection.disconnect();
    }


}
