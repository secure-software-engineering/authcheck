package de.fraunhofer.iem.authchecker.phase;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.Logger;

import de.fraunhofer.iem.authchecker.analysis.Configuration;
import de.fraunhofer.iem.authchecker.artifact.Artifact;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;

public abstract class Phase<AC extends Configuration> implements PhaseInterface {

  protected static final Logger LOGGER = LoggerUtil.getLogger();

  protected String identifier;
  protected AC config;
  private List<Artifact> artifacts;

  public Phase(AC config, String identifier, Artifact... artifacts) {
    this.config = config;
    this.identifier = identifier;
    this.artifacts = Arrays.asList(artifacts);
  }

  public String getIdentifier() {
    return identifier;
  }

  public Artifact getArtifact(String identifier) {
    for (Artifact artifact : artifacts) {
      if (artifact.getIdentifier().equals(identifier)) {
        return artifact;
      }
    }
    LOGGER.error("Artifact " + identifier + " inside phase " + this.identifier
        + " can not be found. Please check the wiring of phases and artifacts.");
    System.exit(-1);
    return null;
  }
}