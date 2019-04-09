package de.fraunhofer.iem.authchecker.algorithm;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import de.fraunhofer.iem.authchecker.entity.CallGraphEdgeEntity;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeEntity;
import de.fraunhofer.iem.authchecker.model.CallGraphModel;

public class AllPathsAlgorithm {

  public List<List<CallGraphNodeEntity>> execute(CallGraphModel callGraphModel,
      CallGraphNodeEntity source, CallGraphNodeEntity destination) {
    List<List<CallGraphNodeEntity>> paths = new ArrayList<List<CallGraphNodeEntity>>();
    dfs(callGraphModel, source, destination, paths, new LinkedHashSet<CallGraphNodeEntity>());
    return paths;
  }

  private void dfs(CallGraphModel cg, CallGraphNodeEntity current, CallGraphNodeEntity destination,
      List<List<CallGraphNodeEntity>> paths, LinkedHashSet<CallGraphNodeEntity> path) {
    path.add(current);

    if (current.equals(destination)) {
      paths.add(new ArrayList<CallGraphNodeEntity>(path));
      path.remove(current);
      return;
    }

    final ArrayList<CallGraphEdgeEntity> callGraphEdgeEntities = cg.getEdgesFrom(current);

    for (CallGraphEdgeEntity callGraphEdgeEntity : callGraphEdgeEntities) {
      if (!path.contains(callGraphEdgeEntity.getEnd())) {
        dfs(cg, callGraphEdgeEntity.getEnd(), destination, paths, path);
      }
    }

    path.remove(current);
  }
}
