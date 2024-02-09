package edu.brown.cs.student.search;


import edu.brown.cs.student.csv.CSVParser;
import edu.brown.cs.student.exceptions.FactoryFailureException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** CSV search core logic once a config is given */
public class CSVSearchProcessor {
  private CSVSearchConfig config;

  /**
   * Class to process a CSV file. Does search logic.
   *
   * @param config A CSVSearchConfig instance with details such as filename and search value
   */
  public CSVSearchProcessor(CSVSearchConfig config) {
    this.config = config;
  }

  /**
   * Core function to process a CSV file and search based on the config
   *
   * @return A list of lines in the CSV that satisfy the criteria, with each line as List<String>
   */
  public List<List<String>> processCSV(SearchCriteria criteria) {
    try (FileReader fileReader = new FileReader(config.filename)) {
      // Update the user to communicate CSV file was found
      System.out.println("[UPDATE] Searching CSV file: " + config.filename);
      CSVParser<List<String>> parser = new CSVParser<>(fileReader, row -> row);
      List<List<String>> rows = parser.parse(); // use CSVParser to convert CSV to usable format
      List<List<String>> finalReturn =
          new ArrayList<>(); // Stores the rows that satisfy the criteria

      Integer columnIndex = config.columnIndex;
      if (!config.colIdentifierIsIndex) {
        columnIndex = rows.get(0).indexOf(config.columnName);
        if (columnIndex == -1) {
          System.err.println(
              "[ERROR] Column name '" + config.columnName + "' not found in CSV header.");
          return null;
        }
      }
      if (config.hasHeader) {
        rows.remove(0);
      }
      // Update the user on CSV processing
      System.out.println("[UPDATE] CSV Processing Complete");

      boolean atLeastOne = false;
      for (List<String> row : rows) {
        if (criteria.matches(row, config.searchValue, columnIndex)) {
          String rowString = String.join(", ", row);
          System.out.println(rowString);
          finalReturn.add(row);
          atLeastOne = true;
        }
      }
      if (!atLeastOne) {
        // Communicate to the user that the search was done, and no matches were found
        System.out.println("No matches found in the search!");
      }

      return finalReturn;

    } catch (IOException | FactoryFailureException e) {
      // Print error message
      System.err.println("[ERROR] " + e.getMessage());
      return null;
    }
  }

  /** hashCode, equals, and toString are all heavily based on the config! */
  @Override
  public int hashCode() {
    return config.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    // If references same place in memory, return true
    if (this == o) return true;

    // If the other object isn't even CSVSearchProcessor, return false
    if (o == null || getClass() != o.getClass()) return false;

    // Typecast o to CSVSearchProcessor
    CSVSearchProcessor that = (CSVSearchProcessor) o;

    // If we have two CSVSearchProcessors, just compare configs!
    return config.equals(that.config);
  }

  @Override
  public String toString() {
    return "CSVSearchProcessor{Config = \n" + config.toString() + "}";
  }
}
