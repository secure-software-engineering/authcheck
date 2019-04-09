package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

public class SuccessfulResultPathEntity extends ResultPathEntity {

  public SuccessfulResultPathEntity(String cweIdentifier, List<CallGraphNodeEntity> path) {
    super(false, cweIdentifier, path);
  }
}
