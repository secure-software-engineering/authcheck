package de.fraunhofer.iem.authchecker.statemachine;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
public class StateMachineState {

  private String name;

  private StateType type;

  public StateMachineState(String name, StateType type) {
    this.name = name;
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public StateType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "StateMachineState " + this.name + " with type " + this.type;
  }
}

