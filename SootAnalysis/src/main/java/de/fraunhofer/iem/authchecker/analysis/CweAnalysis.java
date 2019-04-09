package de.fraunhofer.iem.authchecker.analysis;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

import de.fraunhofer.iem.authchecker.algorithm.AllEdgesPathsAlgorithm;
import de.fraunhofer.iem.authchecker.algorithm.AllNodesAlgorithm;
import de.fraunhofer.iem.authchecker.algorithm.SpringPatternMatchingAlgorithm;
import de.fraunhofer.iem.authchecker.artifact.CallGraphArtifact;
import de.fraunhofer.iem.authchecker.artifact.CweArtifact;
import de.fraunhofer.iem.authchecker.artifact.InputModelArtifact;
import de.fraunhofer.iem.authchecker.entity.CweEntity;
import de.fraunhofer.iem.authchecker.model.InputModel;
import de.fraunhofer.iem.authchecker.phase.CallGraphCleanUpPhase;
import de.fraunhofer.iem.authchecker.phase.CallGraphConstructionPhase;
import de.fraunhofer.iem.authchecker.phase.CallGraphPostProcessingPhase;
import de.fraunhofer.iem.authchecker.phase.CallGraphSnapShotPhase;
import de.fraunhofer.iem.authchecker.phase.CweCheckPhase;
import de.fraunhofer.iem.authchecker.phase.Phase;
import de.fraunhofer.iem.authchecker.presenter.AnalysisResultPresenter;

public class CweAnalysis extends Analysis<CweConfiguration, CweResult> {

  private CallGraphArtifact callGraphArtifact;

  private InputModelArtifact inputModelArtifact;

  private CweArtifact cweArtifact;

  public CweAnalysis(CweConfiguration config, InputModel inputModel,
      List<CweEntity> cweEntities, AnalysisResultPresenter<CweResult> resultPresenter) {
    super("CWE Analysis", config, new CweResult(), resultPresenter);
    this.callGraphArtifact = new CallGraphArtifact();
    this.inputModelArtifact = new InputModelArtifact(inputModel);
    this.cweArtifact = new CweArtifact(cweEntities);
  }

  public void setupPhases(CweConfiguration config, List<Phase> phases) {
    CallGraphConstructionPhase cgConstruction = new CallGraphConstructionPhase(
        config,
        callGraphArtifact,
        inputModelArtifact
    );

    CallGraphPostProcessingPhase cgPostProcessing = new CallGraphPostProcessingPhase(
        config,
        new SpringPatternMatchingAlgorithm(),
        callGraphArtifact,
        inputModelArtifact
    );

    CallGraphCleanUpPhase cgCleanUp = new CallGraphCleanUpPhase(
        config,
        new AllNodesAlgorithm(),
        callGraphArtifact
    );

    CallGraphSnapShotPhase cgSnapshot = new CallGraphSnapShotPhase(
        config,
        "./report/",
        true,
        callGraphArtifact,
        inputModelArtifact
    );

    CweCheckPhase ccPhase = new CweCheckPhase(
        config,
        this.getResult(),
        new AllEdgesPathsAlgorithm(),
        callGraphArtifact,
        inputModelArtifact,
        cweArtifact
    );

    phases.add(cgConstruction);
    phases.add(cgPostProcessing);
    phases.add(cgCleanUp);
    phases.add(cgSnapshot);
    phases.add(ccPhase);
  }
}