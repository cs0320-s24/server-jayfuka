package edu.brown.cs.student.csv;


import edu.brown.cs.student.exceptions.FactoryFailureException;
import java.util.List;

/**
 * This custom CreatorFromRow will simply take the first element of each row! Rows with 0 elements
 * will result in null as the output.
 */
public class TakeFirstFromRow implements CreatorFromRow<String> {

  /**
   * A create method that simply takes the first element
   *
   * @param row a list of Strings
   * @return One String (the first element row) or simply null if row is empty
   * @throws FactoryFailureException In this case, there's no reason to throw this exception, but
   *     the exception remains in the definition since we're extending CreatorFromRow
   */
  @Override
  public String create(List<String> row) throws FactoryFailureException {
    if (!row.isEmpty()) {
      return row.get(0);
    } else {
      return null;
    }
  }
}
