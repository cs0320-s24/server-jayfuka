package edu.brown.cs.student.search;

/** A config that stores the data for a single CSV Search query */
public class CSVSearchConfig {

  // Main variables that will be stored in the config
  public String filename;
  public String searchValue;
  public boolean hasHeader;
  public Integer columnIndex;
  public boolean colIdentifierIsIndex;

  public String columnName;

  /**
   * Class that stores key data for CSV searching
   *
   * @param filename path to the CSV file to search
   * @param searchValue string to search for in the CSV file
   * @param hasHeader boolean to indicate if the CSV file has a header
   * @param columnIndex Column index to use for search. Can be null to search all columns. If this
   *     is -1, then you need to look at the columnName for column search.
   * @param colIdentifierIsIndex Boolean to indicate if the user put in a column index
   * @param columnName String column name for search. Only used if columnIndex is -1.
   */
  public CSVSearchConfig(
      String filename,
      String searchValue,
      boolean hasHeader,
      Integer columnIndex,
      boolean colIdentifierIsIndex,
      String columnName) {
    this.filename = filename;
    this.searchValue = searchValue;
    this.hasHeader = hasHeader;
    this.columnIndex = columnIndex;
    this.colIdentifierIsIndex = colIdentifierIsIndex;
    this.columnName = columnName;
  }

  /**
   * @param args A list of strings to use for CSV Search. filename, searchValue, hasHeader,
   *     columnIndex OR columnName, colIdentifierIsIndex
   * @return CSVSearchConfig instance with variables initialized from input list
   */
  public static CSVSearchConfig parseArguments(String[] args) {
    if (!((args.length == 3) || (args.length == 5))) {
      System.err.println(
          "Usage: CSVSearchUtility <filename> <search value> <boolean hasHeader> [<column identifier>] [<boolean colIdentifierIsIndex>]");
      System.err.println(
          "If a column identifier is given, colIdentifierIsIndex also needs to be specified.");
      return null;
    }

    String filename = args[0];

    // Check if the filename is valid (meaning it doesn't go outside the "data" folder)
    boolean isValidFilename = filename.startsWith("data/") && !filename.contains("../");
    if (!isValidFilename) {
      System.err.println(
          "[ERROR] Invalid filename. Filename must start with 'data/' and must not contain '../' to navigate outside the protected directory.");
      return null;
    }
    boolean isCSVFilename = filename.endsWith(".csv");
    if (!isCSVFilename) {
      System.err.println(
          "[ERROR] Invalid filename. File must be a CSV, so filename must end with '.csv'");
      return null;
    }

    String searchValue = args[1];
    boolean hasHeader = false;
    try {
      hasHeader = Boolean.parseBoolean(args[2]);
      if (!(args[2].equals("true") || args[2].equals("false"))) {
        System.err.println("[ERROR] hasHeader must be a boolean. Enter 'true' or 'false'");
        return null;
      }
    } catch (Exception ignored) {
      System.err.println("[ERROR] hasHeader must be a boolean. Enter 'true' or 'false'");
      return null;
    }

    // Defaults before parsing logic below
    Integer columnIndex = null;
    boolean colIdentifierIsIndex = true;
    String columnName = "";

    // If column is specified, we'd see more than 3 arguments
    if (args.length > 3) {
      try {
        colIdentifierIsIndex = Boolean.parseBoolean(args[4]);
        if (!(args[4].equals("true") || args[4].equals("false"))) {
          System.err.println(
              "[ERROR] colIdentifierIsIndex must be a boolean. Enter 'true' or 'false'");
          return null;
        }
      } catch (Exception ignored) {
        System.err.println(
            "[ERROR] colIdentifierIsIndex must be a boolean. Enter 'true' or 'false'");
        return null;
      }
      if (!colIdentifierIsIndex) {
        // Can't use the column index. We'll use columnName!
        columnName = args[3];
        columnIndex = -1; // Tells the search processor to identify column index from name
      } else {
        // Use column index
        try {
          columnIndex = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
          System.err.println("[ERROR] Column identifier must be an integer if it's an index!");
          return null;
        }

        // The given index cannot be negative!
        if (columnIndex < 0) {
          System.err.println("[ERROR] Column index cannot be negative!");
          return null;
        }
      }
      if (!hasHeader && !colIdentifierIsIndex) {
        System.err.println(
            "[ERROR] CSV must have a header if the column identifier is not an index");
        return null;
      }
    }

    return new CSVSearchConfig(
        filename, searchValue, hasHeader, columnIndex, colIdentifierIsIndex, columnName);
  }

  /**
   * Standard toString() method
   *
   * @return A string that clearly details all parts of the config
   */
  @Override
  public String toString() {
    return "CSVSearchConfig{"
        + '\n'
        + "filename='"
        + filename
        + '\''
        + '\n'
        + ", searchValue='"
        + searchValue
        + '\''
        + '\n'
        + ", hasHeader="
        + hasHeader
        + '\n'
        + ", columnIndex="
        + columnIndex
        + '\n'
        + ", colIdentifierIsIndex="
        + colIdentifierIsIndex
        + '\n'
        + ", columnName='"
        + columnName
        + '\''
        + '}';
  }

  /**
   * Structural equality Inspired by:
   * https://www.geeksforgeeks.org/overriding-equals-method-in-java/
   *
   * @param o - a candidate for structural equality
   * @return Boolean - details whether this CSVSearchConfig matches o structurally
   */
  @Override
  public boolean equals(Object o) {
    // If references same place in memory, return true
    if (this == o) return true;

    // If the other object isn't even CSVSearchConfig, return false
    if (o == null || getClass() != o.getClass()) return false;

    // Typecast o to CSVSearchConfig
    CSVSearchConfig that = (CSVSearchConfig) o;

    // Deal with null in column index!
    boolean colIndEqual = false;
    if (columnIndex == null && that.columnIndex == null) {
      colIndEqual = true;
    } else {
      if (columnIndex == null || that.columnIndex == null) {
        colIndEqual = false;
      } else {
        colIndEqual = columnIndex.equals(that.columnIndex);
      }
    }

    // If we have two CSVSearchConfigs, compare each variable
    return hasHeader == that.hasHeader
        && colIdentifierIsIndex == that.colIdentifierIsIndex
        && filename.equals(that.filename)
        && searchValue.equals(that.searchValue)
        && colIndEqual
        && columnName.equals(that.columnName);
  }

  /**
   * A hash code function to make sure we can create hash maps with this config Inspired by:
   * https://www.baeldung.com/java-hashcode
   *
   * @return integer (hash)
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (hasHeader ? 1 : 0);
    ;
    result = prime * result + (colIdentifierIsIndex ? 1 : 0);
    result = prime * result + filename.hashCode();
    result = prime * result + searchValue.hashCode();
    // Need to handle null value for columnIndex!
    result = prime * result + (columnIndex == null ? 0 : columnIndex.hashCode());
    result = prime * result + columnName.hashCode();
    return result;
  }
}
