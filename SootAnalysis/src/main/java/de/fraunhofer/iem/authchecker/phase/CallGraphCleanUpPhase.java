package de.fraunhofer.iem.authchecker.phase;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.List;

import de.fraunhofer.iem.authchecker.algorithm.ReachabilityAlgorithm;
import de.fraunhofer.iem.authchecker.analysis.CweConfiguration;
import de.fraunhofer.iem.authchecker.artifact.Artifact;
import de.fraunhofer.iem.authchecker.artifact.CallGraphArtifact;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeEntity;
import de.fraunhofer.iem.authchecker.model.CallGraphModel;

public class CallGraphCleanUpPhase extends Phase<CweConfiguration> {

  private ReachabilityAlgorithm reachabilityAlgorithm;

  public CallGraphCleanUpPhase(CweConfiguration config,
      ReachabilityAlgorithm reachabilityAlgorithm, Artifact... artifacts) {
    super(config, "callGraphCleanUp", artifacts);
    this.reachabilityAlgorithm = reachabilityAlgorithm;
  }

  public void run() {
    // get all reachable nodes from init point
    List<CallGraphNodeEntity> reachableCallGraphNodeEntities = reachabilityAlgorithm
        .execute(this.getCallGraph(), this.getCallGraph().getEntryPoint());

    // remove all reachable nodes from all nodes
    ArrayList<CallGraphNodeEntity> allNodesCopy = new ArrayList<CallGraphNodeEntity>(
        this.getCallGraph().getCallGraphNodeEntities());
    allNodesCopy.removeAll(reachableCallGraphNodeEntities);

    // and finally delete them from call graph
    for (CallGraphNodeEntity callGraphNodeEntity : allNodesCopy) {
      LOGGER.info("Removing node " + callGraphNodeEntity.getIdentifier());
      this.getCallGraph().removeNode(callGraphNodeEntity);
    }
  }

  private CallGraphModel getCallGraph() {
    CallGraphArtifact artifact = (CallGraphArtifact) this.getArtifact("callGraph");
    return artifact.getCallGraphModel();
  }
}