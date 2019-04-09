package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
public class CallGraphEdgeEntity {

  private CallGraphNodeEntity start;

  private CallGraphNodeEntity end;

  private String authExpression;

  public CallGraphEdgeEntity(CallGraphNodeEntity start, CallGraphNodeEntity end,
      String authExpression) {
    this.start = start;
    this.end = end;
    this.authExpression = authExpression;
  }

  public CallGraphNodeEntity getStart() {
    return start;
  }

  public void setStart(CallGraphNodeEntity start) {
    this.start = start;
  }

  public CallGraphNodeEntity getEnd() {
    return end;
  }

  public void setEnd(CallGraphNodeEntity end) {
    this.end = end;
  }

  public String getAuthExpression() {
    return authExpression;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CallGraphEdgeEntity that = (CallGraphEdgeEntity) o;

    if (!start.equals(that.start)) {
      return false;
    }
    if (end != null ? !end.equals(that.end) : that.end != null) {
      return false;
    }
    return authExpression.equals(that.authExpression);
  }

  @Override
  public int hashCode() {
    int result = start.hashCode();
    result = 31 * result + (end != null ? end.hashCode() : 0);
    result = 31 * result + (authExpression != null ? authExpression.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Start: " + this.getStart().getMethodName() + " End: " + this.getEnd().getMethodName();
  }
}