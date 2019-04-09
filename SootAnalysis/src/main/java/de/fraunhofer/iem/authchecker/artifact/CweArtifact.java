package de.fraunhofer.iem.authchecker.artifact;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

import de.fraunhofer.iem.authchecker.entity.CweEntity;

public class CweArtifact extends Artifact {

  private List<CweEntity> cweEntities;

  public CweArtifact(List<CweEntity> cweEntities) {
    super("cwe");
    this.cweEntities = cweEntities;
  }

  public List<CweEntity> getCWEs() {
    return this.cweEntities;
  }
}
