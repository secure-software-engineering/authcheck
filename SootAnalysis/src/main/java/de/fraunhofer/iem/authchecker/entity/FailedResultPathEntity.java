package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

public class FailedResultPathEntity extends ResultPathEntity {

  private String methodIdentifier;

  private String stateIdentifier;

  private String errorMessage;

  private String fixMessage;

  private int pathFailedIndex;

  public FailedResultPathEntity(String cweIdentifier, int pathFailedIndex,
      List<CallGraphNodeEntity> path, String methodIdentifier, String stateIdentifier,
      String errorMessage, String fixMessage) {
    super(true, cweIdentifier, path);
    this.methodIdentifier = methodIdentifier;
    this.stateIdentifier = stateIdentifier;
    this.errorMessage = errorMessage;
    this.fixMessage = fixMessage;
    this.pathFailedIndex = pathFailedIndex;
  }

  public int getPathFailedIndex() {
    return pathFailedIndex;
  }

  public String getMethodIdentifier() {
    return methodIdentifier;
  }

  public String getStateIdentifier() {
    return stateIdentifier;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public String getFixMessage() {
    return fixMessage;
  }
}
