package de.fraunhofer.iem.authchecker.exception;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import de.fraunhofer.iem.authchecker.entity.CweEntity;

public class CweViolationException extends Exception {

  private String state;

  public CweViolationException(String state) {
    super("Violation");
    this.state = state;
  }

  public String getState() {
    return state;
  }
}
