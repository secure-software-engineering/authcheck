package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
public class CweMessagesEntity {

  private String errorMessage;

  private String fixMessage;

  public CweMessagesEntity(String errorMessage, String fixMessage) {
    this.errorMessage = errorMessage;
    this.fixMessage = fixMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getFixMessage() {
    return fixMessage;
  }

  public void setFixMessage(String fixMessage) {
    this.fixMessage = fixMessage;
  }
}
