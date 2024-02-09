package edu.brown.cs.student.search;


import java.util.List;

/**
 * Function to check if a given row matches search criteria Does EXACT match (very strict). Does NOT
 * convert to lower case or accept subsets
 */
public class ExactSearchCriteria extends SearchCriteria {

  /**
   * EXACT match core function
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
        if (value.equals(searchValue)) {
          return true;
        }
      }
    } else if (columnIndex >= 0 && columnIndex < row.size()) {
      String columnValue = row.get(columnIndex);
      return columnValue.equals(searchValue);
    }
    return false;
  }
}
