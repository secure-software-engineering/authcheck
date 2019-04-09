package de.fraunhofer.iem.authchecker.statemachine;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
public class StateMachineEvent {

  private StateMachineState start;

  private StateMachineState end;

  private StateMachineMethodType stateMachineMethodType;

  public StateMachineEvent(StateMachineState start, StateMachineState end,
      StateMachineMethodType stateMachineMethodType) {
    this.start = start;
    this.end = end;
    this.stateMachineMethodType = stateMachineMethodType;
  }

  public StateMachineState getStart() {
    return start;
  }

  public StateMachineState getEnd() {
    return end;
  }

  public StateMachineMethodType getStateMachineMethodType() {
    return stateMachineMethodType;
  }
}
