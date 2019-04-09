package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
public class CallGraphNodeEntity {

  private String identifier;

  private String methodName;

  private CallGraphNodeTypeEntity type;

  public CallGraphNodeEntity(String identifier, CallGraphNodeTypeEntity type, String methodName) {
    this.identifier = identifier;
    this.methodName = methodName;
    this.type = type;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getMethodName() {
    return this.methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public CallGraphNodeTypeEntity getType() {
    return type;
  }

  public void setType(CallGraphNodeTypeEntity type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CallGraphNodeEntity callGraphNodeEntity = (CallGraphNodeEntity) o;

    return identifier.equals(callGraphNodeEntity.identifier);
  }

  @Override
  public int hashCode() {
    int result = identifier.hashCode();
    result = 31 * result + type.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return this.identifier + " with type " + this.type;
  }
}
