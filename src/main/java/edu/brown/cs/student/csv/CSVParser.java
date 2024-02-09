package edu.brown.cs.student.csv;


import edu.brown.cs.student.exceptions.FactoryFailureException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Parser class to read and process CSV files. Crux of the package, powering functionality needed by
 * end-users and developers.
 */
public class CSVParser<T> {

  /** Generic Reader object Used to read CSV data Relatively flexible, which is key to developers */
  private final Reader reader;

  /**
   * Interface that converts each row of CSV into a specific object Relatively flexible, which is
   * key to satisfying developers
   */
  private final CreatorFromRow<T> creator;

  /**
   * Regular expression to parse a single line of a CSV. Credit: Sprint #1 Handout, Tim Nelson, and
   * CS 32 Course Staff.
   */
  private static final Pattern REGEX_SPLIT_CSV_ROW =
      Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");

  /**
   * Constructs a CSVParser object with the given Reader and CreatorFromRow (input params)
   *
   * @param reader The Reader (any type of reader!) object to read CSV data
   * @param creator A CreatorFromRow instance to convert rows into objects (of any type!)
   */
  public CSVParser(Reader reader, CreatorFromRow<T> creator) {
    this.reader = reader;
    this.creator = creator;
  }

  /**
   * Splits a CSV line into a list of fields using a regular expression Utilizes the regex pattern
   * REGEX_SPLIT_CSV_ROW
   *
   * @param line The CSV line from our full CSV to be split
   * @return A list of fields extracted from the line
   */
  private List<String> splitCSVLine(String line) {
    List<String> fields = new ArrayList<>();

    /**
     * Splits via REGEX_SPLIT_CSV_ROW Specify limit of -1 to avoid discarding empty fields Credit:
     * https://www.geeksforgeeks.org/split-string-java-examples/
     */
    String[] splitData = REGEX_SPLIT_CSV_ROW.split(line, -1); // list of strings using regex

    for (String field : splitData) {
      // Extra processing for quotations!
      if (field.startsWith("\"") && field.endsWith("\"")) {

        // Remove the leading and trailing quotes
        String fieldNoQuote = field.substring(1, field.length() - 1);

        // Two consecutive quotes inside quotation marks indicate a literal quote in a field.
        String fieldSpecialQuotes = fieldNoQuote.replace("\"\"", "\"");

        fields.add(fieldSpecialQuotes);
      } else {
        fields.add(field);
      }
    }
    return fields;
  }

  /**
   * Parses the CSV data Converts each row into an object of type T Utilizes the reader and regex
   * for splitting (see splitCSVLine above)
   *
   * @return A List of objects created from each row in the CSV.
   * @throws IOException if an error occurs during reading the CSV data via reader
   * @throws FactoryFailureException if an error occurs during object creation via CreatorFromRow
   */
  public List<T> parse() throws IOException, FactoryFailureException {
    List<T> results = new ArrayList<>();

    /**
     * We'll use a StringBuilder instead of a String so that we can append efficiently Credit:
     * https://stackoverflow.com/questions/5234147/why-stringbuilder-when-there-is-string
     */
    StringBuilder stringBuilder = new StringBuilder();
    int c;

    // Read the CSV character by character using the provided reader
    while ((c = reader.read()) != -1) {
      char character = (char) c;

      // For each new line, move what's in the stringBuilder to results
      if (character == '\n') {
        if (!stringBuilder.isEmpty()) {
          List<String> row = splitCSVLine(stringBuilder.toString()); // Call our splitCSVLine method
          stringBuilder.setLength(0); // Clear the StringBuilder
          T object = creator.create(row);
          results.add(object);
        }
      } else {
        // If it's not a new line, just add the character to the stringBuilder
        stringBuilder.append(character);
      }
    }

    // Process the last line if not empty
    if (!stringBuilder.isEmpty()) {
      List<String> row = splitCSVLine(stringBuilder.toString());
      T object = creator.create(row);
      results.add(object);
    }

    return results;
  }

  /**
   * toString method that heavily relies on reader and creator
   *
   * @return a string that clearly summarizes the given CSVParser
   */
  @Override
  public String toString() {
    return "CSVParser{"
        + "\n"
        + "reader="
        + reader.getClass().getName()
        + ", \n"
        + "creator="
        + creator.getClass().getName()
        + '}';
  }

  /**
   * Equals method to check for structural (not referential) equality Heavily relies on reader and
   * creator
   *
   * @param o a candidate object we are comparing to this CSVParser
   * @return boolean - true if and only if structural equality is met
   */
  @Override
  public boolean equals(Object o) {
    // If references same place in memory, return true
    if (this == o) return true;

    // If the other object isn't even CSVParser, return false
    if (o == null || getClass() != o.getClass()) return false;

    // Typecast o to CSVParser. This is tricky since we don't know the object type!
    CSVParser<?> csvParser = (CSVParser<?>) o;

    // Rely on reader and creator to check for equality
    return reader.equals(csvParser.reader) && creator.equals(csvParser.creator);
  }

  /**
   * hashCode using reader and creator
   *
   * @return integer hash
   */
  @Override
  public int hashCode() {
    int result = 1;
    final int prime = 31;
    result = prime * result + (reader == null ? 0 : reader.hashCode());
    result = prime * result + (creator == null ? 0 : creator.hashCode());
    return result;
  }
}
