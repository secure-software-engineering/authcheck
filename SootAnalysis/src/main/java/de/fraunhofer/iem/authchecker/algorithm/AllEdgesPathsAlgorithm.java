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
import de.fraunhofer.iem.authchecker.model.PathModel;
import de.fraunhofer.iem.authchecker.service.PathService;

public class AllEdgesPathsAlgorithm implements PathAlgorithm {

  public List<PathModel> execute(CallGraphModel callGraphModel, CallGraphNodeEntity source,
      CallGraphNodeEntity destination) {
    List<PathModel> paths = new ArrayList<PathModel>();
    dfs(callGraphModel, new CallGraphEdgeEntity(null, source, null), destination, paths,
        new LinkedHashSet<CallGraphEdgeEntity>());
    return paths;
  }

  private void dfs(CallGraphModel cg, CallGraphEdgeEntity current, CallGraphNodeEntity destination,
      List<PathModel> paths, LinkedHashSet<CallGraphEdgeEntity> path) {
    if (current.getStart() != null) {
      path.add(current);
    }

    if (current.getEnd().equals(destination)) {

      List<CallGraphEdgeEntity> pathList = new ArrayList<CallGraphEdgeEntity>(path);
      PathService service = new PathService(pathList);

      paths.add(new PathModel(service.calculateAuthorizationExpression(), pathList));
      path.remove(current);
      return;
    }

    final ArrayList<CallGraphEdgeEntity> callGraphEdgeEntities = cg.getEdgesFrom(current.getEnd());

    for (CallGraphEdgeEntity callGraphEdgeEntity : callGraphEdgeEntities) {
      if (!path.contains(callGraphEdgeEntity)) {
        dfs(cg, callGraphEdgeEntity, destination, paths, path);
      }
    }

    if (current.getStart() != null) {
      path.remove(current);
    }
  }
}
