package edu.brown.cs.student.server;

import edu.brown.cs.student.csv.CSVParser;
import edu.brown.cs.student.csv.CreatorFromRow;
import edu.brown.cs.student.exceptions.FactoryFailureException;
import edu.brown.cs.student.search.CSVSearchProcessor;
import edu.brown.cs.student.search.FuzzySearchCriteria;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

public class CSVHandler implements Route {

    public CSVHandler(FileReader csvData) {
        // TODO: implement csv search
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }
}
