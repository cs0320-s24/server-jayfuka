package edu.brown.cs.student.search;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CSVSearchConfigTest {

  @Test
  void validArgs() {
    String[] args = {"data/JayIsAwesomeFile.csv", "searchValue", "true", "2", "true"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    assertNotNull(config);
    assertEquals("data/JayIsAwesomeFile.csv", config.filename);
    assertEquals("searchValue", config.searchValue);
    assertTrue(config.hasHeader);
    assertEquals(2, config.columnIndex);
    assertTrue(config.colIdentifierIsIndex);
    assertEquals("", config.columnName);
  }

  @Test
  void outsideProtectedDirectory() {
    String[] args = {"another_folder/JayIsAwesomeFile.csv", "searchValue", "true"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    // Important: The program should NOT throw an error. It should return null for the config.
    assertNull(config);
  }

  @Test
  void insideDirectoryButNavigatesOutside() {
    String[] args = {"data/../JayIsAwesomeFile.csv", "searchValue", "true"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    // Important: The program should NOT throw an error. It should return null for the config.
    assertNull(config);
  }

  @Test
  void notCSVFile() {
    String[] args = {"data/JayIsAwesomeFile.java", "searchValue", "true"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    // Important: The program should NOT throw an error. It should return null for the config.
    assertNull(config);
  }

  @Test
  void tooFewArgs() {
    String[] args = {"data/JayIsAwesomeFile.csv", "searchValue"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    assertNull(config);
  }

  @Test
  void tooManyArgs() {
    String[] args = {"data/JayIsAwesomeFile.csv", "searchValue", "a", "b", "c", "d", "e"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    assertNull(config);
  }

  @Test
  void invalidColumnSpec() {
    String[] args = {"data/JayIsAwesomeFile.csv", "searchValue", "true", "2"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    assertNull(config);
  }

  @Test
  void hasHeaderBoolean() {
    String[] args = {"data/JayIsAwesomeFile.csv", "searchValue", "notBooleanHere"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    assertNull(config);
  }

  @Test
  void colIdentifierIsIndexBoolean() {
    String[] args = {"data/JayIsAwesomeFile.csv", "searchValue", "true", "2", "notBooleanHere"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    assertNull(config);
  }

  @Test
  void columnNameButNoHeader() {
    String[] args = {"data/JayIsAwesomeFile.csv", "searchValue", "false", "someColName", "false"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    assertNull(config);
  }

  @Test
  void columnIndexNotInt() {
    String[] args = {"data/JayIsAwesomeFile.csv", "searchValue", "false", "someColName", "true"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    assertNull(config);
  }

  @Test
  void validColumnName() {
    String[] args = {"data/JayIsAwesomeFile.csv", "searchValue", "true", "someColName", "false"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    assertNotNull(config);
    assertEquals("data/JayIsAwesomeFile.csv", config.filename);
    assertEquals("searchValue", config.searchValue);
    assertTrue(config.hasHeader);
    assertEquals(-1, config.columnIndex);
    assertFalse(config.colIdentifierIsIndex);
    assertEquals("someColName", config.columnName);
  }

  @Test
  void negativeColIndex() {
    String[] args = {"data/JayIsAwesomeFile.csv", "searchValue", "true", "-100", "true"};
    CSVSearchConfig config = CSVSearchConfig.parseArguments(args);

    assertNull(config);
  }

  @Test
  void equalsPositiveExample() {
    String[] args1 = {"data/JayIsAwesomeFile.csv", "searchValue", "true", "2", "true"};
    CSVSearchConfig config1 = CSVSearchConfig.parseArguments(args1);

    String[] args2 = {"data/JayIsAwesomeFile.csv", "searchValue", "true", "2", "true"};
    CSVSearchConfig config2 = CSVSearchConfig.parseArguments(args2);

    assertTrue(config1.equals(config2));
    assertEquals(config1, config2);
  }

  @Test
  void equalsNegativeExample() {
    String[] args1 = {"data/JayIsAwesomeFile.csv", "searchValue", "true", "2", "true"};
    CSVSearchConfig config1 = CSVSearchConfig.parseArguments(args1);

    String[] args2 = {"data/JayIsAwesomeFile.csv", "searchValue", "false", "2", "true"};
    CSVSearchConfig config2 = CSVSearchConfig.parseArguments(args2);

    assertFalse(config1.equals(config2));
    assertNotEquals(config1, config2);
  }

  @Test
  void equalsHandlingNullPositiveExample() {
    String[] args1 = {"data/JayIsAwesomeFile.csv", "searchValue", "true"};
    CSVSearchConfig config1 = CSVSearchConfig.parseArguments(args1);

    String[] args2 = {"data/JayIsAwesomeFile.csv", "searchValue", "true"};
    CSVSearchConfig config2 = CSVSearchConfig.parseArguments(args2);

    assertTrue(config1.equals(config2));
    assertEquals(config1, config2);
  }

  @Test
  void equalsHandlingNullNegativeExample() {
    String[] args1 = {"data/JayIsAwesomeFile.csv", "searchValue", "true"};
    CSVSearchConfig config1 = CSVSearchConfig.parseArguments(args1);

    String[] args2 = {"data/JayIsAwesomeFile.csv", "anotherSearchVal", "true"};
    CSVSearchConfig config2 = CSVSearchConfig.parseArguments(args2);

    assertFalse(config1.equals(config2));
    assertNotEquals(config1, config2);
  }

  @Test
  void toStringTest() {
    String[] args1 = {"data/JayIsAwesomeFile.csv", "searchValue", "true", "2", "true"};
    CSVSearchConfig config1 = CSVSearchConfig.parseArguments(args1);

    String expecting =
        "CSVSearchConfig{"
            + '\n'
            + "filename='"
            + "data/JayIsAwesomeFile.csv"
            + '\''
            + '\n'
            + ", searchValue='"
            + "searchValue"
            + '\''
            + '\n'
            + ", hasHeader="
            + true
            + '\n'
            + ", columnIndex="
            + "2"
            + '\n'
            + ", colIdentifierIsIndex="
            + true
            + '\n'
            + ", columnName='"
            + ""
            + '\''
            + '}';

    assertEquals(expecting, config1.toString());
  }

  @Test
  void hashCodePositiveTest() {
    String[] args1 = {"data/JayIsAwesomeFile.csv", "searchValue", "true", "2", "true"};
    CSVSearchConfig config1 = CSVSearchConfig.parseArguments(args1);
    int c1Hash = config1.hashCode();

    String[] args2 = {"data/JayIsAwesomeFile.csv", "searchValue", "true", "2", "true"};
    CSVSearchConfig config2 = CSVSearchConfig.parseArguments(args2);
    int c2Hash = config2.hashCode();

    assertEquals(c1Hash, c2Hash);
  }

  @Test
  void hashCodeNegativeTest() {
    String[] args1 = {"data/JayIsAwesomeFile.csv", "searchValue", "true", "2", "true"};
    CSVSearchConfig config1 = CSVSearchConfig.parseArguments(args1);
    int c1Hash = config1.hashCode();

    String[] args2 = {"data/JayIsAwesomeFile.csv", "otherSearchVal", "true", "2", "true"};
    CSVSearchConfig config2 = CSVSearchConfig.parseArguments(args2);
    int c2Hash = config2.hashCode();

    assertNotEquals(c1Hash, c2Hash);
  }
}
