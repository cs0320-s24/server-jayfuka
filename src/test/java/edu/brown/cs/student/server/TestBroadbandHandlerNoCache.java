package edu.brown.cs.student.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestBroadbandHandlerNoCache {
  final List answer = new ArrayList();

  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  @BeforeEach
  public void setup() {
    // Re-initialize state, etc. for _every_ test method run
    this.answer.clear();

    // In fact, restart the entire Spark server for every test!
    Spark.get("searchcsv", new CSVHandler());
    Spark.get("loadcsv", new CSVHandler());
    Spark.get("viewcsv", new CSVHandler());
    Spark.get("broadband", new BroadbandHandler());
    Spark.get("broadbandNoCache", new BroadbandHandlerNoCache());
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
    Spark.unmap("broadbandNoCache");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send the request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.setRequestMethod("GET");
    clientConnection.connect();
    return clientConnection;
  }

  @Test
  // Recall that the "throws IOException" doesn't signify anything but acknowledgement to the type
  // checker
  public void testAPINoCache() throws IOException {
    HttpURLConnection clientConnection = tryRequest("broadbandNoCache");

    // Going to 'broadbandNoCache' alone shouldn't work, and the code should be 500
    assertEquals(500, clientConnection.getResponseCode());

    clientConnection.disconnect();
  }

  @Test
  public void testAPIValidStateNoCache() throws IOException {

    HttpURLConnection clientConnection =
        tryRequest("broadbandNoCache?state=rhode%20island&county=providence%20county");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    BroadbandHandlerNoCache.BroadbandSuccessResponse response =
        moshi
            .adapter(BroadbandHandlerNoCache.BroadbandSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    String time = now.toString();
    String expected =
        "{broadband_data={dateTime="
            + time
            + ", broadbandData=[[Providence County, Rhode Island, 85.4, 44, 007]]}}";
    assertEquals(expected, response.responseMap().toString());
    clientConnection.disconnect();
  }

  @Test
  public void testAPIInvalidStateNoCache() throws IOException {

    HttpURLConnection clientConnection =
        tryRequest("broadbandNoCache?state=invalid&county=providence%20county");
    assertEquals(200, clientConnection.getResponseCode());

    // You will see in console:
    // State Name 'invalid' not found
    clientConnection.disconnect();
  }
}
