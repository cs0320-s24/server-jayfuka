package edu.brown.cs.student.search;


import java.util.List;

/**
 * Does a "fuzzy match" in that all text is converted to lower case (both query and row) The row
 * value can contain the query (e.g. "something" contains "some")
 */
public class FuzzySearchCriteria extends SearchCriteria {

  /**
   * Fuzzy match core function
   *
   * @param row Processed row of CSV. List of strings.
   * @param searchValue The query we are looking for
   * @param columnIndex The column we are looking in. Null means look at all columns.
   * @return A boolean: does the row meet search criteria?
   */
  @Override
  public boolean matches(List<String> row, String searchValue, Integer columnIndex) {
    if (columnIndex == null) {
      for (String value : row) {
        if (value.toLowerCase().contains(searchValue.toLowerCase())) {
          return true;
        }
      }
    } else if (columnIndex >= 0 && columnIndex < row.size()) {
      String columnValue = row.get(columnIndex).toLowerCase();
      return columnValue.contains(searchValue.toLowerCase());
    }
    return false;
  }
}
