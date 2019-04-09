package de.fraunhofer.iem.authchecker.phase;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import de.fraunhofer.iem.authchecker.adapter.DotAdapter;
import de.fraunhofer.iem.authchecker.analysis.CweConfiguration;
import de.fraunhofer.iem.authchecker.artifact.Artifact;
import de.fraunhofer.iem.authchecker.artifact.CallGraphArtifact;
import de.fraunhofer.iem.authchecker.artifact.InputModelArtifact;
import de.fraunhofer.iem.authchecker.model.CallGraphModel;
import de.fraunhofer.iem.authchecker.model.InputModel;

public class CallGraphSnapShotPhase extends Phase<CweConfiguration> {

  private boolean shouldBuildPng;

  private String directory;

  public CallGraphSnapShotPhase(CweConfiguration config, String directory,
      boolean shouldBuildPng, Artifact... artifacts) {
    super(config, "callGraphSnapshot", artifacts);
    this.directory = directory;
    this.shouldBuildPng = shouldBuildPng;
  }

  public void run() {
    DotAdapter dotAdapter = new DotAdapter(this.getCallGraphModel());
    String file = directory + "callGraph.dot";
    dotAdapter.buildDotFile(file, this.getInputModel());
    LOGGER.info("Build dot file successfully to " + file);
    if (shouldBuildPng) {
      dotAdapter.buildPngFromDot(file);
      LOGGER.info("Build png file successfully to " + file + ".png");
    }
  }

  private CallGraphModel getCallGraphModel() {
    CallGraphArtifact cgArtifact = (CallGraphArtifact) this.getArtifact("callGraph");
    return cgArtifact.getCallGraphModel();
  }

  private InputModel getInputModel() {
    InputModelArtifact imArtifact = (InputModelArtifact) this.getArtifact("inputModel");
    return imArtifact.getInputModel();
  }

}
