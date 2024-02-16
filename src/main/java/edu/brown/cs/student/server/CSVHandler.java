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
            return createErrorResponse("Invalid endpoint. Available endpoints: /loadcsv, /viewcsv, /searchcsv");
        }
    }

    private Object handleLoadCSV(Request request, Response response) throws Exception {
        String filePath = csv_file_path; // Get file path from query parameter

        if (filePath == null) {
            return createErrorResponse("Missing required query parameter 'filePath'");
        }

        Reader reader = new FileReader(filePath);
        parserNow = new CSVParser<>(reader, row -> row);

        try {
            csvData = parserNow.parse();
            csvLoaded = true;
            return createSuccessResponse("CSV loaded successfully!");
        } catch (FactoryFailureException e) {
            return createErrorResponse("Error creating objects via CreatorFromRow: " + e.getMessage());
        } catch (IOException e) {
            return createErrorResponse("Error reading CSV file: " + e.getMessage());
        }
    }

    private Object handleViewCSV(Request request, Response response) throws Exception {
        if (!csvLoaded) {
            return createErrorResponse("No CSV loaded yet. Please use /loadcsv first.");
        }

        // Serialize the data using Moshi for a cleaner JSON format
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<List<List<String>>> adapter = moshi.adapter((Type) List.class);
        //String serializedData = adapter.toJson(parserNow.parse()); // Re-parse for fresh data
        String serializedData = adapter.toJson(csvData);

        return createSuccessResponse(serializedData);
    }

    private Object handleSearchCSV(Request request, Response response) throws Exception {
        System.out.println("Test logging statement");
        if (!csvLoaded) {
            return createErrorResponse("No CSV loaded yet. Please use /loadcsv first.");
        }
        String searchValue = request.queryParams("searchValue");
        if (searchValue == null) {
            return createErrorResponse("Missing required query parameter 'searchValue'");
        }
        String hasHeaderStr = request.queryParams("hasHeader");
        if (hasHeaderStr == null) {
            return createErrorResponse("Missing required query parameter 'hasHeader'");
        }
        String columnIndexStr = request.queryParams("columnIndex");
        String colIdentifierIsIndexStr = request.queryParams("colIdentifierIsIndex");
        String columnName = request.queryParams("columnName");
        System.out.println("Made it to line 108");
        createErrorResponse("Made it to line 108");
        try {
            createErrorResponse("Made it to line 109");
            System.out.println("Made it to line 109");
            // Capture the System.err output temporarily
            ByteArrayOutputStream errContent = new ByteArrayOutputStream();
            PrintStream originalErr = System.err;
            System.setErr(new PrintStream(errContent));

            System.out.println("Made it to line 114");
            System.out.println("File: " + csv_file_path );
            System.out.println("Search: " + searchValue );
            System.out.println("Header: " + hasHeaderStr );
            System.out.println("Column: " + columnIndexStr );
            System.out.println("colIdentifierIsIndexStr: " + colIdentifierIsIndexStr );
            System.out.println("Column Name: " +   columnName );

            String[] args = {csv_file_path, searchValue, hasHeaderStr, columnIndexStr, colIdentifierIsIndexStr, columnName};

            System.out.println(args);

            // Now, all of the error messages will go to the errContent stream!
            CSVSearchConfig config = CSVSearchConfig.parseArguments(args);
            System.out.println("Made it to line 120");
            // Restore original System.err stream
            System.setErr(originalErr);

            if (config == null) {
                return createErrorResponse("Invalid configuration parameters: " + errContent.toString());
            }

            // We will use FuzzySearchProcessor for search - but this can easily be changed to an exact search!
            CSVSearchProcessor processor = new CSVSearchProcessor(config);
            FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
            List<List<String>> results = processor.processCSV(searchCriteriaNow);
            createErrorResponse("Made it to line 130");
            System.out.println("Made it to line 130");

            // Serialize the search results using Moshi for a cleaner JSON format
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<List<List<String>>> adapter = moshi.adapter((Type) List.class);
            String serializedResults = adapter.toJson(results);

            return createSuccessResponse(serializedResults);
        } catch (Exception e) {
            return createErrorResponse("CSV search failed: " + e.getMessage());
        }
    }

    private Map<String, Object> createSuccessResponse(String body) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("body", body);
        responseMap.put("status", "success");
        return responseMap;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", message);
        responseMap.put("status", "error");
        return responseMap;
    }

}
