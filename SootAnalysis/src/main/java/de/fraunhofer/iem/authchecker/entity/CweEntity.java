package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.Map;

import de.fraunhofer.iem.authchecker.statemachine.StateMachine;
import de.fraunhofer.iem.authchecker.statemachine.StateMachineState;

public class CweEntity {

  private int id;

  private String identifier;

  private String description;

  private StateMachine stateMachine;

  private Map<String, CweMessagesEntity> messages;

  public CweEntity(int id, String description, StateMachine stateMachine,
      Map<String, CweMessagesEntity> messages) {
    this.id = id;
    this.identifier = "CWE " + id;
    this.description = description;
    this.stateMachine = stateMachine;
    this.messages = messages;
  }

  public int getId() {
    return id;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public String getDescription() {
    return description;
  }

  public StateMachine getStateMachine() {
    return stateMachine;
  }

  public String getErrorMessage(StateMachineState state) {
    CweMessagesEntity messages = this.messages.get(state.getName());
    if (messages != null) {
      return messages.getErrorMessage();
    }
    return null;
  }

  public String getFixMessage(StateMachineState state) {
    CweMessagesEntity messages = this.messages.get(state.getName());
    if (messages != null) {
      return messages.getFixMessage();
    }
    return null;
  }
}