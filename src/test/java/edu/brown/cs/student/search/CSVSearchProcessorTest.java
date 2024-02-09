package edu.brown.cs.student.search;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

public class CSVSearchProcessorTest {

  /** Tests on income_by_race.csv */
  @Test
  void simpleQueryCharacteristics() {
    String csv_file_path = "data/census/income_by_race.csv";
    CSVSearchConfig config =
        new CSVSearchConfig(csv_file_path, "providence-county-ri", true, null, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 79);
    assertEquals(results.get(1).get(1), "White");
    assertEquals(results.get(2).get(5), "3384.0000000000000");
    assertEquals(results.get(4).get(0), "4");
  }

  @Test
  void specificColumnWithQuery() {
    String csv_file_path = "data/census/income_by_race.csv";
    CSVSearchConfig config =
        new CSVSearchConfig(csv_file_path, "providence-county-ri", true, 8, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 79);
    assertEquals(results.get(1).get(1), "White");
    assertEquals(results.get(2).get(5), "3384.0000000000000");
    assertEquals(results.get(4).get(0), "4");
  }

  @Test
  void specificColumnWithoutQuery() {
    String csv_file_path = "data/census/income_by_race.csv";
    CSVSearchConfig config =
        new CSVSearchConfig(csv_file_path, "providence-county-ri", true, 7, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 0);
  }

  @Test
  void columnNameSearch() {
    String csv_file_path = "data/census/income_by_race.csv";
    CSVSearchConfig config =
        new CSVSearchConfig(csv_file_path, "providence-county-ri", true, -1, false, "Year");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 0);
  }

  /** Tests on postsecondary_education.csv */
  @Test
  void queryLengthLong() {
    String csv_file_path = "data/census/postsecondary_education.csv";
    CSVSearchConfig config = new CSVSearchConfig(csv_file_path, "Women", true, null, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 8);
  }

  @Test
  void queryLengthShort() {
    String csv_file_path = "data/census/postsecondary_education.csv";
    CSVSearchConfig config = new CSVSearchConfig(csv_file_path, "men", true, null, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 16);
  }

  @Test
  void searchInsideNumbers() {
    String csv_file_path = "data/census/postsecondary_education.csv";
    CSVSearchConfig config = new CSVSearchConfig(csv_file_path, "35", true, null, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 3);
  }

  @Test
  void fuzzySearchCapitalization() {
    String csv_file_path = "data/census/postsecondary_education.csv";
    CSVSearchConfig config = new CSVSearchConfig(csv_file_path, "aSiAn", true, null, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 2);
    assertEquals(results.get(1).get(0), "Asian"); // Search should NOT modify the file
  }

  @Test
  void doNotSearchHeader() {
    String csv_file_path = "data/census/postsecondary_education.csv";
    CSVSearchConfig config = new CSVSearchConfig(csv_file_path, "share", true, null, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 0);
  }

  /** Tests on malformed_signs.csv */
  @Test
  void recognizeMalformedColumn() {
    String csv_file_path = "data/malformed/malformed_signs.csv";
    CSVSearchConfig config = new CSVSearchConfig(csv_file_path, "Nick", true, null, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 1);
    assertEquals(results.get(0).get(1), "Roberto");
  }

  @Test
  void specifyMalformedColumn() {
    String csv_file_path = "data/malformed/malformed_signs.csv";
    CSVSearchConfig config = new CSVSearchConfig(csv_file_path, "", true, 2, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    FuzzySearchCriteria searchCriteriaNow = new FuzzySearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 10);
    assertEquals(results.get(3).get(1), "Gabi");
  }

  /** Tests on stardata.csv */
  @Test
  void exactMatchContainsIsInsufficient() {
    String csv_file_path = "data/stars/stardata.csv";
    CSVSearchConfig config = new CSVSearchConfig(csv_file_path, "gustav_", true, null, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    ExactSearchCriteria searchCriteriaNow = new ExactSearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 0);
  }

  @Test
  void exactMatchCapitalization() {
    String csv_file_path = "data/stars/stardata.csv";
    CSVSearchConfig config = new CSVSearchConfig(csv_file_path, "gustav", true, null, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    ExactSearchCriteria searchCriteriaNow = new ExactSearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 0);
  }

  @Test
  void exactMatchPositiveTest() {
    String csv_file_path = "data/stars/stardata.csv";
    CSVSearchConfig config = new CSVSearchConfig(csv_file_path, "Gustav_13", true, null, true, "");
    CSVSearchProcessor processor = new CSVSearchProcessor(config);
    ExactSearchCriteria searchCriteriaNow = new ExactSearchCriteria();
    List<List<String>> results = processor.processCSV(searchCriteriaNow);

    assertEquals(results.size(), 1);
    assertEquals(results.get(0).get(2), "4.53049");
  }

  @Test
  void equalsPositiveTest() {
    // Includes testing of handling null!
    String csv_file_path1 = "data/stars/stardata.csv";
    CSVSearchConfig config1 =
        new CSVSearchConfig(csv_file_path1, "Gustav_13", true, null, true, "");
    CSVSearchProcessor processor1 = new CSVSearchProcessor(config1);

    String csv_file_path2 = "data/stars/stardata.csv";
    CSVSearchConfig config2 =
        new CSVSearchConfig(csv_file_path2, "Gustav_13", true, null, true, "");
    CSVSearchProcessor processor2 = new CSVSearchProcessor(config2);

    assertTrue(processor1.equals(processor2));
    assertEquals(processor1, processor2);
  }

  @Test
  void equalsNegativeTest() {
    // Includes testing of handling null!
    String csv_file_path1 = "data/stars/stardata.csv";
    CSVSearchConfig config1 =
        new CSVSearchConfig(csv_file_path1, "Gustav_13", true, null, true, "");
    CSVSearchProcessor processor1 = new CSVSearchProcessor(config1);

    String csv_file_path2 = "data/stars/stardata.csv";
    CSVSearchConfig config2 = new CSVSearchConfig(csv_file_path2, "Gustav_1", true, null, true, "");
    CSVSearchProcessor processor2 = new CSVSearchProcessor(config2);

    assertFalse(processor1.equals(processor2));
    assertNotEquals(processor1, processor2);
  }

  @Test
  void hashCodePositiveTest() {
    // Includes testing of handling null!
    String csv_file_path1 = "data/stars/stardata.csv";
    CSVSearchConfig config1 =
        new CSVSearchConfig(csv_file_path1, "Gustav_13", true, null, true, "");
    CSVSearchProcessor processor1 = new CSVSearchProcessor(config1);

    String csv_file_path2 = "data/stars/stardata.csv";
    CSVSearchConfig config2 =
        new CSVSearchConfig(csv_file_path2, "Gustav_13", true, null, true, "");
    CSVSearchProcessor processor2 = new CSVSearchProcessor(config2);

    assertEquals(processor1.hashCode(), processor2.hashCode());
  }

  @Test
  void hashCodeNegativeTest() {
    // Includes testing of handling null!
    String csv_file_path1 = "data/stars/stardata.csv";
    CSVSearchConfig config1 =
        new CSVSearchConfig(csv_file_path1, "Gustav_13", true, null, true, "");
    CSVSearchProcessor processor1 = new CSVSearchProcessor(config1);

    String csv_file_path2 = "data/stars/stardata.csv";
    CSVSearchConfig config2 = new CSVSearchConfig(csv_file_path2, "Gustav_13", true, 1, true, "");
    CSVSearchProcessor processor2 = new CSVSearchProcessor(config2);

    assertNotEquals(processor1.hashCode(), processor2.hashCode());
  }

  @Test
  void toStringTest() {
    String[] args1 = {"data/stars/stardata.csv", "searchValue", "true", "2", "true"};
    CSVSearchConfig config1 = CSVSearchConfig.parseArguments(args1);
    CSVSearchProcessor processor1 = new CSVSearchProcessor(config1);

    String expecting = "CSVSearchProcessor{Config = \n" + config1.toString() + "}";

    assertEquals(expecting, processor1.toString());
  }
}
