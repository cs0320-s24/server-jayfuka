package edu.brown.cs.student.cli;


import edu.brown.cs.student.search.CSVSearchConfig;
import edu.brown.cs.student.search.CSVSearchProcessor;
import edu.brown.cs.student.search.FuzzySearchCriteria;

/** Command line interface to do CSV searches */
public class CSVSearchUtility {
  public static void main(String[] args) {
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);
    /**
     * If there's an issue with the arg parsing, return null. The config will handle error message
     * printing (it will have specifics)
     */
    if (config == null) {
      return;
    }

    FuzzySearchCriteria defaultCriteria = new FuzzySearchCriteria();

    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    processor.processCSV(defaultCriteria);
  }
}
