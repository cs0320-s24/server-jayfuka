package edu.brown.cs.student.server;

import static spark.Spark.after;

import java.io.FileNotFoundException;
import spark.Spark;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the various
 * handlers (2).
 *
 * <p>Notice that the OrderHandler takes in a state (menu) that can be shared if we extended the
 * restaurant They need to share state (a menu). This would be a great opportunity to use dependency
 * injection. If we needed more endpoints, more functionality classes, etc. we could make sure they
 * all had the same shared state.
 */
public class Server {

  private static final String LOADCSV_PATH = "/loadcsv";
  private static final String VIEWCSV_PATH = "/viewcsv";
  private static final String SEARCHCSV_PATH = "/searchcsv";

  private static final String ACS_PATH = "/broadband";

  private static final String ACS_NO_CACHE_PATH = "/broadbandNoCache";

  private static CSVHandler csvHandler;
  private static BroadbandHandler broadbandHandler;

  private static BroadbandHandlerNoCache broadbandHandlerNoCache;

  public static void main(String[] args) throws FileNotFoundException {
    int port = 3232;
    Spark.port(port);
    /*
       Setting CORS headers to allow cross-origin requests from the client; this is necessary for the client to
       be able to make requests to the server.

       By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
       This is not a good idea in real-world applications, since it opens up your server to cross-origin requests
       from any website. Instead, you should set this header to the origin of your client, or a list of origins
       that you trust.

       By setting the Access-Control-Allow-Methods header to "*", we allow requests with any HTTP method.
       Again, it's generally better to be more specific here and only allow the methods you need, but for
       this demo we'll allow all methods.

       We recommend you learn more about CORS with these resources:
           - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
           - https://portswigger.net/web-security/cors
    */
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    csvHandler = new CSVHandler();

    // Register endpoints with the same CSVHandler
    Spark.get(LOADCSV_PATH, csvHandler);
    Spark.get(VIEWCSV_PATH, csvHandler);
    Spark.get(SEARCHCSV_PATH, csvHandler);

    broadbandHandler = new BroadbandHandler();

    Spark.get(ACS_PATH, broadbandHandler);

    broadbandHandlerNoCache = new BroadbandHandlerNoCache();

    Spark.get(ACS_NO_CACHE_PATH, broadbandHandlerNoCache);

    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }
}
