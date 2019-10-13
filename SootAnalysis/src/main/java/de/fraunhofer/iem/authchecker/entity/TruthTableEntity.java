package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class TruthTableEntity {

  private boolean[][] matrix;

  private Map<Integer, String> groupIndex;
  private Map<String, List<String>> groups;
  
  public TruthTableEntity(Map<String, List<String>> groups) {
    this.matrix = new boolean[(int) Math.pow(2,groups.size())][groups.size()];
    
    // Semi deep copy
    this.groupIndex = new HashMap<Integer, String>();
    this.groups = new HashMap<String, List<String>>();
    
    int index = 0;
    for (String key : groups.keySet()) {
    	this.groupIndex.put(index++, key);
    	this.groups.put(key, groups.get(key));
	}
    
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

  public Map<String, List<String>> getGroupsForRow(int rowNumber) {
	Map<String, List<String>> rowGroups = new HashMap<String, List<String>>();
    boolean[] row = this.matrix[rowNumber];
    for (int i=0; i < this.groups.size(); i++) {
      if (row[i]) {
        rowGroups.put(this.groupIndex.get(i), this.groups.get(this.groupIndex.get(i)));
      }
    }
    return rowGroups;
  }
}