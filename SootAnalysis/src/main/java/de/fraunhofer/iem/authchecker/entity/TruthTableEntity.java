package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.List;

public class TruthTableEntity {

  private boolean[][] matrix;

  private List<String> groups;

  public TruthTableEntity(List<String> groups) {
    this.matrix = new boolean[(int) Math.pow(2,groups.size())][groups.size()];
    // semi deep copy
    this.groups = new ArrayList<String>();
    this.groups.addAll(groups);
    this.init();
  }

  private void init() {
    int rows = (int) Math.pow(2, groups.size());
    for (int i=0; i<rows; i++) {
      for (int j=groups.size()-1; j>=0; j--) {
        boolean value = (i/(int) Math.pow(2, groups.size()-1-j))%2 == 1;
        matrix[i][j] = value;
      }
    }
  }

  public boolean[][] getMatrix() {
    return this.matrix;
  }

  public int getRowCount() {
    return (int) Math.pow(2, groups.size());
  }

  @Override
  public String toString() {
    String string = this.groups.toString() + "\n";
    int rows = (int) Math.pow(2, groups.size());
    for (int i=0; i<rows; i++) {
      for (int j=0; j<groups.size(); j++) {
        string += (matrix[i][j] == true ? "1" : "0") + " ";
      }
      string += "\n";
    }
    return string;
  }

  public List<String> getGroupsForRow(int rowNumber) {
    List<String> rowGroups = new ArrayList<String>();
    boolean[] row = this.matrix[rowNumber];
    for (int i=0; i < this.groups.size(); i++) {
      if (row[i]) {
        rowGroups.add(this.groups.get(i));
      }
    }
    return rowGroups;
  }
}