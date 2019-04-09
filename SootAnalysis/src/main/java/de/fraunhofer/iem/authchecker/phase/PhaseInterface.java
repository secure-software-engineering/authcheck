package de.fraunhofer.iem.authchecker.phase;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import de.fraunhofer.iem.authchecker.artifact.Artifact;

public interface PhaseInterface {

  void run();

  String getIdentifier();

  Artifact getArtifact(String identifier);
}
