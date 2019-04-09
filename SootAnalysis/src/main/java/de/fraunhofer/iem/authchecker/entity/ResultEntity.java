package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResultEntity {

  private Map<CweEntity, ResultPathEntity> paths;

  public ResultEntity() {
    this.paths = new LinkedHashMap<CweEntity, ResultPathEntity>();
  }

  public void addPath(CweEntity cwe, ResultPathEntity path) {
    this.paths.put(cwe, path);
  }

  public List<ResultPathEntity> getPaths() {
    List<ResultPathEntity> paths = new ArrayList<ResultPathEntity>();
    for (Map.Entry<CweEntity, ResultPathEntity> entry : this.paths.entrySet()) {
      paths.add(entry.getValue());
    }
    return paths;
  }

  public boolean isFailed() {
    for (Map.Entry<CweEntity, ResultPathEntity> entry : this.paths.entrySet()) {
      if (entry.getValue().isFailed()) {
        return true;
      }
    }
    return false;
  }

  public String getPathStart() {
    for (Map.Entry<CweEntity, ResultPathEntity> entry : this.paths.entrySet()) {
      return entry.getValue().getCallGraphPath().get(0).getMethodName();
    }
    return null;
  }

  public String getPathEnd() {
    for (Map.Entry<CweEntity, ResultPathEntity> entry : this.paths.entrySet()) {
      int size = entry.getValue().getCallGraphPath().size();
      return entry.getValue().getCallGraphPath().get(size - 1).getMethodName();
    }
    return null;
  }
}
