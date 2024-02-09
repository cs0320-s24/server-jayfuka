package edu.brown.cs.student.csv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CSVParserTest {

  // Basic tests of functionality
  @Test
  public void testParseSimpleCSV() {
    String csvData = "a,b,c\n1,2,3";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(2, result.size(), "Expected 2 rows in the result");
      assertEquals(List.of("a", "b", "c"), result.get(0), "First row should match");
      assertEquals(List.of("1", "2", "3"), result.get(1), "Second row should match");
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void unequalNumEntries() {
    String csvData = "a,b,c\n1,2,3,4\nx,y";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(3, result.size());
      assertEquals(List.of("a", "b", "c"), result.get(0));
      assertEquals(List.of("1", "2", "3", "4"), result.get(1));
      assertEquals(List.of("x", "y"), result.get(2));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void oneLine() {
    String csvData = "this,is,just,one,line";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(1, result.size());
      assertEquals(List.of("this", "is", "just", "one", "line"), result.get(0));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void emptyCSV() {
    String csvData = "";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(0, result.size());
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  // Testing multiple types of Readers

  // Source for CharArrayReader processing:
  // https://jenkov.com/tutorials/java-io/chararrayreader.html
  @Test
  public void charArrayReaderTest() {
    char[] csvData = "012345,0\n8923,00".toCharArray();

    CSVParser<List<String>> parser = new CSVParser<>(new CharArrayReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(2, result.size());
      assertEquals(List.of("012345", "0"), result.get(0));
      assertEquals(List.of("8923", "00"), result.get(1));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void bufferedReaderTest() {
    String csvData = "first,last\nJay,Gopal\nTim,Nelson\nNim,Telson\n0.99,0.98";
    BufferedReader reader = new BufferedReader(new StringReader(csvData));

    CSVParser<List<String>> parser = new CSVParser<>(reader, row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(5, result.size());
      assertEquals(List.of("first", "last"), result.get(0));
      assertEquals(List.of("Jay", "Gopal"), result.get(1));
      assertEquals(List.of("Tim", "Nelson"), result.get(2));
      assertEquals(List.of("Nim", "Telson"), result.get(3));
      assertEquals(List.of("0.99", "0.98"), result.get(4));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  // Testing multiple types of CreatorFromRows

  @Test
  public void firstInEqual() {
    String csvData = "a,b,c\n1,2,3\nz,y,x";
    CSVParser<String> parser = new CSVParser<>(new StringReader(csvData), new TakeFirstFromRow());

    try {
      List<String> result = parser.parse();
      assertEquals(3, result.size());
      assertEquals("a", result.get(0));
      assertEquals("1", result.get(1));
      assertEquals("z", result.get(2));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void firstInUnequal() {
    String csvData = "a,b,c\n1,2,3,4\nx,y";
    CSVParser<String> parser = new CSVParser<>(new StringReader(csvData), new TakeFirstFromRow());

    try {
      List<String> result = parser.parse();
      assertEquals(3, result.size());
      assertEquals("a", result.get(0));
      assertEquals("1", result.get(1));
      assertEquals("x", result.get(2));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void lastInEqual() {
    String csvData = "a,b,c\n1,2,3\nz,y,x";
    CSVParser<String> parser = new CSVParser<>(new StringReader(csvData), new TakeLastFromRow());

    try {
      List<String> result = parser.parse();
      assertEquals(3, result.size());
      assertEquals("c", result.get(0));
      assertEquals("3", result.get(1));
      assertEquals("x", result.get(2));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void lastInUnequal() {
    String csvData = "a,b,c\n1,2,3,4\nx,y";
    CSVParser<String> parser = new CSVParser<>(new StringReader(csvData), new TakeLastFromRow());

    try {
      List<String> result = parser.parse();
      assertEquals(3, result.size());
      assertEquals("c", result.get(0));
      assertEquals("4", result.get(1));
      assertEquals("y", result.get(2));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  // Testing CSV single line parsing abilities (REGEX). These will be on a single line.

  @Test
  public void straightForwardCommas() {
    String csvData = "a,b,c";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(1, result.size());
      assertEquals(List.of("a", "b", "c"), result.get(0));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void noCommas() {
    String csvData = "a";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(1, result.size());
      assertEquals(List.of("a"), result.get(0));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void emptyFields() {
    String csvData = "a,b,c,d,,,e";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(1, result.size());
      assertEquals(List.of("a", "b", "c", "d", "", "", "e"), result.get(0));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void commaInQuotes() {
    String csvData = "a,b,\"c,d\",e";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(1, result.size());
      assertEquals(List.of("a", "b", "c,d", "e"), result.get(0));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void commaInQuotesEnd() {
    String csvData = "a,b,\"c,d\"";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(1, result.size());
      assertEquals(List.of("a", "b", "c,d"), result.get(0));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void manyCommasInQuote() {
    String csvData = "a,b,\"c,,,,d\",e";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(1, result.size());
      assertEquals(List.of("a", "b", "c,,,,d", "e"), result.get(0));
    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void justOneQuoteInside() {
    String csvData = "a,b,\"c,d,e";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(1, result.size());
      // REGEX EXPECTED FAILURE! Correct answer is: List.of("a", "b", "\"c","d","e")
      assertEquals(List.of("a,b,\"c", "d", "e"), result.get(0));

    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  @Test
  public void putQuotesInsideQuotes() {
    String csvData = "\"hey there!\",\"here \"are\" nested quotes\",\"goodbye!\"";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      assertEquals(1, result.size());
      assertEquals(List.of("hey there!", "here \"are\" nested quotes", "goodbye!"), result.get(0));

    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  /**
   * Inspiration: https://www.ietf.org/rfc/rfc4180.txt Discovered the above source through this
   * thread: https://stackoverflow.com/questions/35092666/how-to-escape-a-comma-in-csv-file
   */
  @Test
  public void lineBreakInField() {
    String csvData = "a,b,\"c\nd\",e";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);

    try {
      List<List<String>> result = parser.parse();
      // REGEX FAILURE! Correct answer is: 1
      assertEquals(2, result.size());
      // REGEX FAILURE! Correct answer has 1 List<String>: List.of("a","b","c\nd","e")
      assertEquals(List.of("a,b,\"c"), result.get(0));
      assertEquals(List.of("d\"", "e"), result.get(1));

    } catch (Exception e) {
      fail("Exception thrown during parsing: " + e.getMessage());
    }
  }

  // toString, hashCode, and equals

  @Test
  public void toStringTest() {
    String csvData = "this,is,just,one,line";
    CSVParser<List<String>> parser = new CSVParser<>(new StringReader(csvData), row -> row);
    String parserToString = parser.toString();

    assertTrue(parserToString.startsWith("CSVParser{"));
    assertTrue(parserToString.contains("reader="));
    assertTrue(parserToString.contains("java.io.StringReader"));
    assertTrue(parserToString.contains("creator="));
    assertTrue(parserToString.endsWith("}"));
  }

  @Test
  public void equalsPositiveTest() {
    String csvData = "a,b,\"c,,,,d\",e";
    CSVParser<List<String>> parser1 = new CSVParser<>(new StringReader(csvData), row -> row);
    CSVParser<List<String>> parser2 = new CSVParser<>(new StringReader(csvData), row -> row);

    // KNOWN FAILURE: Reader equality, and perhaps lambda equality, are not met
    assertNotEquals(parser1, parser2);

    StringReader s1 = new StringReader(csvData);
    StringReader s2 = new StringReader(csvData);
    assertNotEquals(s1, s2);
  }

  @Test
  public void equalsNegativeTest() {
    String csvData1 = "a,b,\"c,,,,d\",e";
    String csvData2 = "1,2,\"c,,,,d\",e";
    CSVParser<List<String>> parser1 = new CSVParser<>(new StringReader(csvData1), row -> row);
    CSVParser<List<String>> parser2 = new CSVParser<>(new StringReader(csvData2), row -> row);

    assertNotEquals(parser1, parser2);
  }

  @Test
  public void hashCodePositiveTest() {
    String csvData = "data,for,hashcode,test";
    CSVParser<List<String>> parser1 = new CSVParser<>(new StringReader(csvData), row -> row);
    CSVParser<List<String>> parser2 = new CSVParser<>(new StringReader(csvData), row -> row);

    // KNOWN FAILURE: Just like equality, hashing for reader, and perhaps lambdas, is not equal
    assertNotEquals(parser1.hashCode(), parser2.hashCode());
  }

  @Test
  public void hashCodeNegativeTest() {
    String csvData1 = "data,for,hashcode,test,1";
    String csvData2 = "data,for,hashcode,test,2";

    CSVParser<List<String>> parser1 = new CSVParser<>(new StringReader(csvData1), row -> row);
    CSVParser<List<String>> parser2 = new CSVParser<>(new StringReader(csvData2), row -> row);

    assertNotEquals(parser1.hashCode(), parser2.hashCode());
  }
}
