package de.fraunhofer.iem.authchecker.analysis;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
public abstract class Configuration {

  private String programDirectory;

  public Configuration(String programDirectory) {
    this.programDirectory = programDirectory;
  }

  public String getProgramDirectory() {
    return programDirectory;
  }

  public void setProgramDirectory(String programDirectory) {
    this.programDirectory = programDirectory;
  }
}