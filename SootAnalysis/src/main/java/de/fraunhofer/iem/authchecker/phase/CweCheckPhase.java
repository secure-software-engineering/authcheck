package de.fraunhofer.iem.authchecker.phase;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fraunhofer.iem.authchecker.adapter.MessageAdapter;
import de.fraunhofer.iem.authchecker.algorithm.PathAlgorithm;
import de.fraunhofer.iem.authchecker.analysis.CweConfiguration;
import de.fraunhofer.iem.authchecker.analysis.CweResult;
import de.fraunhofer.iem.authchecker.artifact.Artifact;
import de.fraunhofer.iem.authchecker.artifact.CallGraphArtifact;
import de.fraunhofer.iem.authchecker.artifact.CweArtifact;
import de.fraunhofer.iem.authchecker.artifact.InputModelArtifact;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeEntity;
import de.fraunhofer.iem.authchecker.entity.CweEntity;
import de.fraunhofer.iem.authchecker.entity.FailedResultPathEntity;
import de.fraunhofer.iem.authchecker.entity.ResultEntity;
import de.fraunhofer.iem.authchecker.entity.SuccessfulResultPathEntity;
import de.fraunhofer.iem.authchecker.exception.CweViolationException;
import de.fraunhofer.iem.authchecker.model.CallGraphModel;
import de.fraunhofer.iem.authchecker.model.InputModel;
import de.fraunhofer.iem.authchecker.model.PathModel;
import de.fraunhofer.iem.authchecker.statemachine.StateMachineRunner;
import de.fraunhofer.iem.authchecker.statemachine.StateMachineState;

public class CweCheckPhase extends Phase<CweConfiguration> {

  private PathAlgorithm pathAlgorithm;

  private CweResult analysisResult;

  public CweCheckPhase(CweConfiguration config, CweResult analysisResult,
      PathAlgorithm pathAlgorithm, Artifact... artifacts) {
    super(config, "cweCheck", artifacts);
    this.analysisResult = analysisResult;
    this.pathAlgorithm = pathAlgorithm;
  }

  public void run() {
    this.analysisResult.addCheckedCwes(this.getCWEArtifact().getCWEs());

    List<PathModel> paths = this.calculateAllPaths(this.getCallGraph());
    for (PathModel path : paths) {
      checkPath(path);
    }
  }

  private void checkPath(PathModel path) {
    LOGGER.info("Checking path " + path);

    ResultEntity resultEntity = new ResultEntity();

    for (CweEntity cweEntity : this.getCWEArtifact().getCWEs()) {
      LOGGER.info("Checking " + cweEntity.getIdentifier() + " on the path");
      StateMachineRunner runner = new StateMachineRunner(cweEntity.getStateMachine(),
          this.config, this.getInputModel());
      runner.setCurrentAuthorizationExpression(path.getAuthorizationExpression());

      int pathIndex = 0;
      CallGraphNodeEntity lastNode = null;

      try {
        for (CallGraphNodeEntity node : path.getPathNodes()) {
          lastNode = node;

          runner.flow(node);

          if (pathIndex == path.getPathNodes().size() - 1) {
            if (!runner.isAcceptingState()) {
              throw new CweViolationException(runner.getCurrentState().getName());
            }
          }
          pathIndex++;
        }
        resultEntity.addPath(cweEntity,
            new SuccessfulResultPathEntity(cweEntity.getIdentifier(), path.getPathNodes()));
        LOGGER.info(cweEntity.getIdentifier() + " is successful on this path");
      } catch (CweViolationException e) {
        this.analysisResult.addViolatedCwe(cweEntity);

        Map<String, String> additionalInformation = new HashMap<String, String>();
        additionalInformation.put("methodName", lastNode.getMethodName());

        StateMachineState state = runner.getCurrentState();
        try {
          MessageAdapter adapter = new MessageAdapter();
          String errorMessage = adapter
              .renderMessage(cweEntity.getErrorMessage(state), additionalInformation);
          String fixMessage = adapter
              .renderMessage(cweEntity.getFixMessage(state), additionalInformation);

          FailedResultPathEntity resultPath = new FailedResultPathEntity(
              cweEntity.getIdentifier(),
              pathIndex,
              path.getPathNodes(),
              lastNode.getIdentifier(),
              state.getName(),
              errorMessage,
              fixMessage
          );

          resultEntity.addPath(cweEntity, resultPath);
          LOGGER.info(cweEntity.getIdentifier() + " is violated on this path");
        } catch (IOException ioException) {
          LOGGER.error("Can not get error and fix messages");
        }
      }
    }
    this.analysisResult.addResult(resultEntity);
  }

  private List<PathModel> calculateAllPaths(CallGraphModel callGraph) {
    List<PathModel> paths = new ArrayList<PathModel>();
    for (CallGraphNodeEntity destination : this.getCallGraph().getAllEndNodes()) {
      paths.addAll(this.pathAlgorithm.execute(callGraph, callGraph.getEntryPoint(), destination));
    }
    return paths;
  }

  private CallGraphModel getCallGraph() {
    CallGraphArtifact artifact = (CallGraphArtifact) this.getArtifact("callGraph");
    return artifact.getCallGraphModel();
  }

  private InputModel getInputModel() {
    InputModelArtifact artifact = (InputModelArtifact) this.getArtifact("inputModel");
    return artifact.getInputModel();
  }

  private CweArtifact getCWEArtifact() {
    return (CweArtifact) this.getArtifact("cwe");
  }
}
