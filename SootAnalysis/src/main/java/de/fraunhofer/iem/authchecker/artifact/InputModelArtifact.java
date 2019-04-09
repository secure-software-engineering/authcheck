package de.fraunhofer.iem.authchecker.artifact;

import de.fraunhofer.iem.authchecker.model.InputModel;

public class InputModelArtifact extends Artifact {

  private InputModel inputModel;

  public InputModelArtifact(InputModel inputModel) {
    super("inputModel");
    this.inputModel = inputModel;
  }

  public InputModel getInputModel() {
    return this.inputModel;
  }
}
