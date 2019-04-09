package de.fraunhofer.iem.authchecker.algorithm;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.List;

import de.fraunhofer.iem.authchecker.entity.CallGraphEdgeEntity;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeEntity;
import de.fraunhofer.iem.authchecker.model.CallGraphModel;

public class AllNodesAlgorithm implements ReachabilityAlgorithm {

  public List<CallGraphNodeEntity> execute(CallGraphModel callGraphModel,
      CallGraphNodeEntity source) {
    ArrayList<CallGraphNodeEntity> reachableCallGraphNodeEntities = new ArrayList<CallGraphNodeEntity>();
    dfs(callGraphModel, source, source, reachableCallGraphNodeEntities);
    return reachableCallGraphNodeEntities;
  }

  private void dfs(CallGraphModel cg, CallGraphNodeEntity current, CallGraphNodeEntity start,
      List<CallGraphNodeEntity> reachable) {
    if (current.equals(start) && !reachable.contains(start)) {
      reachable.add(start);
    }

    final ArrayList<CallGraphEdgeEntity> callGraphEdgeEntities = cg.getEdgesFrom(current);

    for (CallGraphEdgeEntity callGraphEdgeEntity : callGraphEdgeEntities) {
      if (callGraphEdgeEntity.getEnd() != null && !reachable
          .contains(callGraphEdgeEntity.getEnd())) {
        reachable.add(callGraphEdgeEntity.getEnd());
        dfs(cg, callGraphEdgeEntity.getEnd(), start, reachable);
      }
    }
  }
}