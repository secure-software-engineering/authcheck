package de.fraunhofer.iem.authchecker.artifact;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
public abstract class Artifact {

  private String identifier;

  public Artifact(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() {
    return identifier;
  }

}
