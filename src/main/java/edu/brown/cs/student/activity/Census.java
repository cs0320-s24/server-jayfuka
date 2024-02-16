package edu.brown.cs.student.activity;


import java.util.List;

public class Census {
  private List<List<String>> rawStateToCode;

  public Census() {}

  @Override
  public String toString() {
    return this.rawStateToCode.toString();
  }
}
