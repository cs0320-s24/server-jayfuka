package edu.brown.cs.student.server;

import edu.brown.cs.student.csv.CSVParser;
import edu.brown.cs.student.exceptions.FactoryFailureException;
import edu.brown.cs.student.search.CSVSearchConfig;
import edu.brown.cs.student.search.CSVSearchProcessor;
import edu.brown.cs.student.search.FuzzySearchCriteria;
import java.io.Reader;
import java.lang.reflect.Type;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileReader;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;


public class CSVHandler implements Route {

    private CSVParser<List<String>> parserNow;
    private Boolean csvLoaded = false;

    private List<List<String>> csvData;

    private String csv_file_path;

    public CSVHandler() {
        csv_file_path = "data/census/ACS_Five_Year.csv";
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String path = request.pathInfo();

        if (path.equals("/loadcsv")) {
            return handleLoadCSV(request, response);
        } else if (path.equals("/viewcsv")) {
            return handleViewCSV(request, response);
        } else if (path.equals("/searchcsv")) {
            return handleSearchCSV(request, response);
        } else {
            return new CSVFailureResponse("Invalid endpoint. Available endpoints: /loadcsv, /viewcsv, /searchcsv");
        }
    }

    private Object handleLoadCSV(Request request, Response response) throws Exception {
        String filePath = csv_file_path; // Get file path from query parameter

        if (filePath == null) {
            return new CSVFailureResponse("Missing required query parameter 'filePath'");
        }

        Reader reader = new FileReader(filePath);
        parserNow = new CSVParser<>(reader, row -> row);

        try {
            csvData = parserNow.parse();
            csvLoaded = true;
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("loading", "success");
            return new CSVHandler.CSVSuccessResponse(responseMap).serialize();
        } catch (FactoryFailureException e) {
            return new CSVFailureResponse("Error creating objects via CreatorFromRow: " + e.getMessage());
        } catch (IOException e) {
            return new CSVFailureResponse("Error reading CSV file: " + e.getMessage());
        }
    }

    private Object handleViewCSV(Request request, Response response) throws Exception {
        if (!csvLoaded) {
            return new CSVFailureResponse("No CSV loaded yet. Please use /loadcsv first.");
        }
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("data", csvData);
        return new CSVHandler.CSVSuccessResponse(responseMap).serialize();
    }

    private Object handleSearchCSV(Request request, Response response) throws Exception {
        if (!csvLoaded) {
            return new CSVFailureResponse("No CSV loaded yet. Please use /loadcsv first.");
        }
        String searchValue = request.queryParams("searchValue");
        if (searchValue == null) {
            return new CSVFailureResponse("Missing required query parameter 'searchValue'");
        }
        String hasHeaderStr = request.queryParams("hasHeader");
        if (hasHeaderStr == null) {
            return new CSVFailureResponse("Missing required query parameter 'hasHeader'");
        }
        String columnIndexStr = request.queryParams("columnIndex");
        String colIdentifierIsIndexStr = request.queryParams("colIdentifierIsIndex");
        String columnName = request.queryParams("columnName");

        try {
            // Capture the System.err output temporarily
            ByteArrayOutputStream errContent = new ByteArrayOutputStream();
            PrintStream originalErr = System.err;
            System.setErr(new PrintStream(errContent));

            String[] args = {csv_file_path, searchValue, hasHeaderStr, columnIndexStr, colIdentifierIsIndexStr, columnName};

            System.out.println(args);

            // Now, all of the error messages will go to the errContent stream!
            CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

            // Restore original System.err stream
            System.setErr(originalErr);

            if (config == null) {
                return new CSVFailureResponse("Invalid configuration parameters: " + errContent.toString());
            }

            // We will use FuzzySearchProcessor for search - but this can easily be changed to an exact search!
            CSVSearchProcessor processor = new CSVSearchProcessor(config);
            FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
            List<List<String>> results = processor.processCSV(searchCriteriaNow);
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("results", results);
            return new CSVHandler.CSVSuccessResponse(responseMap).serialize();
        } catch (Exception e) {
            return new CSVFailureResponse("CSV search failed: " + e.getMessage());
        }
    }


    public record CSVSuccessResponse(String response_type, Map<String, Object> responseMap) {

        public CSVSuccessResponse(Map<String, Object> responseMap) {
            this("success", responseMap);
        }

        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            try {
                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<CSVHandler.CSVSuccessResponse> adapter = moshi.adapter(CSVHandler.CSVSuccessResponse.class);
                return adapter.toJson(this);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

        public record CSVFailureResponse(String response_type) {
            public CSVFailureResponse(String error, String message) {
                this("error");
            }

            /**
             * @return this response, serialized as Json
             */
            String serialize() {
                Moshi moshi = new Moshi.Builder().build();
                return moshi.adapter(CSVHandler.CSVFailureResponse.class).toJson(this);
            }
        }


}
