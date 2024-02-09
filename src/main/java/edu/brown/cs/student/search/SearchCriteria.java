package edu.brown.cs.student.search;


import java.util.List;

/**
 * Abstract class for search criteria Essentially, we want to know if a given row of a CSV matches
 * criteria
 */
public abstract class SearchCriteria {

  /**
   * @param row Processed row of CSV. List of strings.
   * @param searchValue The query we are looking for
   * @param columnIndex The column we are looking in. Null means look at all columns.
   * @return A boolean: does the row meet search criteria?
   */
  public abstract boolean matches(List<String> row, String searchValue, Integer columnIndex);
}
