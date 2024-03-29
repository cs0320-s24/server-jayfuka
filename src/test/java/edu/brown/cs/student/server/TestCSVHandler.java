package edu.brown.cs.student.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
  public static synchronized void setup_before_everything() {
    // Set the Spark port number. This can only be done once, and has to
    // happen before any route maps are added. Hence using @BeforeClass.
    // Setting port 0 will cause Spark to use an arbitrary available port.
    try {
      Spark.port(0); // Attempt to set Spark to use an arbitrary available port
    } catch (Exception e) {
      // Don't think we need anything here
      // But there was a bug if we didn't have this try-catch
      // Probably because it is trying to initialize multiple times
    }
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

    CSVHandler handler = new CSVHandler();
    BroadbandHandler handler2 = new BroadbandHandler();

    Spark.get("searchcsv", handler);
    Spark.get("loadcsv", handler);
    Spark.get("viewcsv", handler);
    Spark.get("broadband", handler2);
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
    CSVHandler.CSVSuccessResponse response =
        moshi
            .adapter(CSVHandler.CSVSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    String expected = "CSVSuccessResponse[response_type=success, responseMap={loading=success}]";
    assertEquals(expected, response.toString());
    clientConnection.disconnect();
  }

  @Test
  public void testView() throws IOException {
    HttpURLConnection firstConnection = tryRequest("loadcsv");
    HttpURLConnection clientConnection = tryRequest("viewcsv");
    Moshi moshi = new Moshi.Builder().build();
    moshi
        .adapter(CSVHandler.CSVSuccessResponse.class)
        .fromJson(new Buffer().readFrom(firstConnection.getInputStream()));
    CSVHandler.CSVSuccessResponse response =
        moshi
            .adapter(CSVHandler.CSVSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    String expected =
        "CSVSuccessResponse[response_type=success, responseMap={data=[[City/Town, Median Household Income , Median Family Income, Per Capita Income\n"
            + "], [Rhode Island, 74,489.00, 95,198.00, \"39,603.00\"\n"
            + "], [Barrington, 130,455.00, 154,441.00, \"69,917.00\"\n"
            + "], [Bristol, 80,727.00, 115,740.00, \"42,658.00\"\n"
            + "], [Burrillville, 96,824.00, 109,340.00, \"39,470.00\"\n"
            + "], [Central Falls, 40,235.00, 42,633.00, \"17,962.00\"\n"
            + "], [Charlestown, 86,023.00, 102,325.00, \"50,086.00\"\n"
            + "], [Coventry, 88,779.00, 104,685.00, \"41,409.00\"\n"
            + "], [Cranston, 77,145.00, 95,763.00, \"38,269.00\"\n"
            + "], [Cumberland, 104,613.00, 116,321.00, \"46,179.00\"\n"
            + "], [East Greenwich, 133,373.00, 173,775.00, \"71,096.00\"\n"
            + "], [East Providence, 65,016.00, 93,935.00, \"38,714.00\"\n"
            + "], [Exeter, 95,053.00, 116,894.00, \"41,058.00\"\n"
            + "], [Foster, 99,892.00, 118,000.00, \"37,382.00\"\n"
            + "], [Glocester, 97,753.00, 108,125.00, \"39,743.00\"\n"
            + "], [Hopkinton, 87,712.00, 103,393.00, \"42,672.00\"\n"
            + "], [Jamestown, 120,129.00, 156,465.00, \"74,159.00\"\n"
            + "], [Johnston, 75,579.00, 93,174.00, \"36,251.00\"\n"
            + "], [Lincoln, 94,571.00, 115,975.00, \"44,135.00\"\n"
            + "], [Little Compton, 96,111.00, 126,823.00, \"81,912.00\"\n"
            + "], [Middletown, 88,211.00, 104,953.00, \"47,714.00\"\n"
            + "], [Narragansett, 82,532.00, 124,830.00, \"44,414.00\"\n"
            + "], [New Shoreham, 72,279.00, 75,096.00, \"37,067.00\"\n"
            + "], [Newport, 77,092.00, 115,140.00, \"48,803.00\"\n"
            + "], [North Kingstown, 104,026.00, 126,663.00, \"52,035.00\"\n"
            + "], [North Providence, 68,821.00, 82,117.00, \"35,843.00\"\n"
            + "], [North Smithfield, 87,121.00, 108,906.00, \"43,850.00\"\n"
            + "], [Pawtucket, 56,427.00, 71,649.00, \"30,246.00\"\n"
            + "], [Portsmouth, 104,073.00, 134,442.00, \"54,981.00\"\n"
            + "], [Providence, 55,787.00, 65,461.00, \"31,757.00\"\n"
            + "], [Richmond, 100,493.00, 112,121.00, \"44,904.00\"\n"
            + "], [Scituate, 104,388.00, 117,740.00, \"50,027.00\"\n"
            + "], [Smithfield, 87,819.00, 111,767.00, \"40,495.00\"\n"
            + "], [South Kingstown, 102,242.00, 114,202.00, \"42,080.00\"\n"
            + "], [Tiverton, 85,522.00, 108,484.00, \"44,202.00\"\n"
            + "], [Warren, 75,755.00, 105,304.00, \"42,683.00\"\n"
            + "], [Warwick, 77,110.00, 97,033.00, \"41,476.00\"\n"
            + "], [West Greenwich, 126,402.00, 122,674.00, \"44,457.00\"\n"
            + "], [West Warwick, 62,649.00, 80,699.00, \"36,148.00\"\n"
            + "], [Westerly, 81,051.00, 107,013.00, \"46,913.00\"\n"
            + "], [Woonsocket, 48,822.00, 58,896.00, 26,561.00]]}]";

    // Normalize line endings by removing all newline characters
    String normalizedExpected = expected.replaceAll("\\r\\n|\\r|\\n", "");
    String normalizedActual = response.toString().replaceAll("\\r\\n|\\r|\\n", "");

    assertEquals(normalizedExpected, normalizedActual);
    clientConnection.disconnect();
    firstConnection.disconnect();
  }

  @Test
  public void testSearch() throws IOException {
    HttpURLConnection firstConnection = tryRequest("loadcsv");
    HttpURLConnection clientConnection =
        tryRequest(
            "searchcsv?searchValue=providence&hasHeader=true&columnIndex=0&colIdentifierIsIndex=true");

    Moshi moshi = new Moshi.Builder().build();
    moshi
        .adapter(CSVHandler.CSVSuccessResponse.class)
        .fromJson(new Buffer().readFrom(firstConnection.getInputStream()));
    CSVHandler.CSVSuccessResponse response =
        moshi
            .adapter(CSVHandler.CSVSuccessResponse.class)
            .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    String expected =
        "CSVSuccessResponse[response_type=success, responseMap={results=[[East Providence, 65,016.00, 93,935.00, \"38,714.00\"\n"
            + "], [North Providence, 68,821.00, 82,117.00, \"35,843.00\"\n"
            + "], [Providence, 55,787.00, 65,461.00, \"31,757.00\"\n"
            + "]]}]";
    // Normalize line endings by removing all newline characters
    String normalizedExpected = expected.replaceAll("\\r\\n|\\r|\\n", "");
    String normalizedActual = response.toString().replaceAll("\\r\\n|\\r|\\n", "");

    assertEquals(normalizedExpected, normalizedActual);
    clientConnection.disconnect();
    firstConnection.disconnect();
  }
}
