package de.fraunhofer.iem.authchecker.analysis;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.List;

import de.fraunhofer.iem.authchecker.comparator.SortFailedFirstComparator;
import de.fraunhofer.iem.authchecker.entity.CweEntity;
import de.fraunhofer.iem.authchecker.entity.ResultEntity;
import de.fraunhofer.iem.authchecker.entity.ResultPathEntity;

public class CweResult extends Result {

  private int pathCount;

  private int vulnerablePathCount;

  private List<CweEntity> checkedCwes;

  private List<CweEntity> violatedCwes;

  private List<ResultEntity> results;

  public CweResult() {
    this.pathCount = 0;
    this.vulnerablePathCount = 0;
    this.checkedCwes = new ArrayList<CweEntity>();
    this.violatedCwes = new ArrayList<CweEntity>();
    this.results = new ArrayList<ResultEntity>();
  }

  public int getPathCount() {
    return pathCount;
  }

  public int getVulnerablePathCount() {
    return vulnerablePathCount;
  }

  public List<CweEntity> getCheckedCwes() {
    return checkedCwes;
  }

  public void addCheckedCwe(CweEntity cwe) {
    this.checkedCwes.add(cwe);
  }

  public void addCheckedCwes(List<CweEntity> cwes) {
    this.checkedCwes.addAll(cwes);
  }

  public List<CweEntity> getViolatedCwes() {
    return violatedCwes;
  }

  public void addViolatedCwe(CweEntity cwe) {
    if (!this.violatedCwes.contains(cwe)) {
      this.violatedCwes.add(cwe);
    }
  }

  public List<ResultEntity> getResults() {
    return results;
  }

  public void sortResultsByFailed() {
    results.sort(new SortFailedFirstComparator());
  }

  public boolean isCweViolated(CweEntity cweEntity) {
    for (CweEntity cwe : this.violatedCwes) {
      if (cwe.equals(cweEntity)) {
        return true;
      }
    }
    return false;
  }

  public void addResult(ResultEntity result) {
    this.results.add(result);
    for (ResultPathEntity path : result.getPaths()) {
      this.pathCount++;
      if (path.isFailed()) {
        this.vulnerablePathCount++;
      }
    }
  }
}
