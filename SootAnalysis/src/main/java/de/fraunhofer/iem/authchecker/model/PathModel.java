package de.fraunhofer.iem.authchecker.model;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.List;

import de.fraunhofer.iem.authchecker.entity.CallGraphEdgeEntity;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeEntity;

public class PathModel {

  private List<CallGraphNodeEntity> pathNodes;

  private List<CallGraphEdgeEntity> pathEdges;

  private String authorizationExpression;

  public PathModel(String authorizationExpression, List<CallGraphEdgeEntity> path) {
    this.authorizationExpression = authorizationExpression;
    this.pathEdges = path;
    this.pathNodes = new ArrayList<CallGraphNodeEntity>();
    //TODO: this can be refactored with different iterators
    if (path.size() > 0) {
      pathNodes.add(path.get(0).getStart());
      for (CallGraphEdgeEntity edge : path) {
        this.pathNodes.add(edge.getEnd());
      }
    }
  }

  public List<CallGraphNodeEntity> getPathNodes() {
    return pathNodes;
  }

  public void setPathNodes(List<CallGraphNodeEntity> pathNodes) {
    this.pathNodes = pathNodes;
  }

  public List<CallGraphEdgeEntity> getPathEdges() {
    return pathEdges;
  }

  public void setPathEdges(List<CallGraphEdgeEntity> pathEdges) {
    this.pathEdges = pathEdges;
  }

  public String getAuthorizationExpression() {
    return authorizationExpression;
  }

  public void setAuthorizationExpression(String authorizationExpression) {
    this.authorizationExpression = authorizationExpression;
  }

  @Override
  public String toString() {
    return this.pathNodes.toString();
  }
}
