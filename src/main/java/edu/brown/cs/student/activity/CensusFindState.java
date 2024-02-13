package edu.brown.cs.student.activity;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CensusFindState {
  private List<List<String>> rawStateToCode;


  public CensusFindState() {

  }

  @Override
  public String toString() {
    return this.rawStateToCode.toString();
  }
}
