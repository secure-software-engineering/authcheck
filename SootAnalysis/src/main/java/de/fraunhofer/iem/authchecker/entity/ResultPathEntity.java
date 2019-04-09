package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

public class ResultPathEntity {

  private boolean failed;

  private String cweIdentifier;

  private List<CallGraphNodeEntity> callGraphPath;

  public ResultPathEntity(boolean failed, String cweIdentifier,
      List<CallGraphNodeEntity> callGraphPath) {
    this.failed = failed;
    this.cweIdentifier = cweIdentifier;
    this.callGraphPath = callGraphPath;
  }

  public boolean isFailed() {
    return failed;
  }

  public String getCweIdentifier() {
    return cweIdentifier;
  }

  public List<CallGraphNodeEntity> getCallGraphPath() {
    return callGraphPath;
  }
}
