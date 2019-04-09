package de.fraunhofer.iem.authchecker.service;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

import de.fraunhofer.iem.authchecker.entity.CallGraphEdgeEntity;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeTypeEntity;

public class PathService {

  private List<CallGraphEdgeEntity> path;

  public PathService(List<CallGraphEdgeEntity> path) {
    this.path = path;
  }

  public String calculateAuthorizationExpression() {
    for (CallGraphEdgeEntity edge : path) {
      if (edge.getStart().getType().equals(CallGraphNodeTypeEntity.AUTHORIZE)) {
        if (edge.getAuthExpression() != null) {
          return edge.getAuthExpression();
        }
      }
    }
    return null;
  }
}