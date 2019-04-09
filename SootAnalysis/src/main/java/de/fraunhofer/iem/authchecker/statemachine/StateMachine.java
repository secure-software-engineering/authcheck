package de.fraunhofer.iem.authchecker.statemachine;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;

public class StateMachine {

  private ArrayList<StateMachineState> stateMachineStates;

  private ArrayList<StateMachineEvent> stateMachineEvents;

  public StateMachine() {
    stateMachineStates = new ArrayList<StateMachineState>();
    stateMachineEvents = new ArrayList<StateMachineEvent>();
  }

  public void addState(String name, StateType type) {
    this.stateMachineStates.add(new StateMachineState(name, type));
  }

  public void addEdge(String startName, String endName,
      StateMachineMethodType stateMachineMethodType) {
    StateMachineState start = this.getStateByName(startName);
    StateMachineState end = this.getStateByName(endName);
    if (start != null && end != null) {
      this.stateMachineEvents.add(new StateMachineEvent(start, end, stateMachineMethodType));
    }
  }

  public ArrayList<StateMachineEvent> getOutOfEdges(StateMachineState stateMachineState) {
    ArrayList<StateMachineEvent> result = new ArrayList<StateMachineEvent>();
    for (StateMachineEvent stateMachineEvent : this.stateMachineEvents) {
      if (stateMachineEvent.getStart().getName().equals(stateMachineState.getName())) {
        result.add(stateMachineEvent);
      }
    }
    return result;
  }

  public ArrayList<StateMachineEvent> getInToEdges(StateMachineState stateMachineState) {
    ArrayList<StateMachineEvent> result = new ArrayList<StateMachineEvent>();
    for (StateMachineEvent stateMachineEvent : this.stateMachineEvents) {
      if (stateMachineEvent.getEnd().getName().equals(stateMachineState.getName())) {
        result.add(stateMachineEvent);
      }
    }
    return result;
  }

  public StateMachineState getInitState() {
    for (StateMachineState stateMachineState : this.stateMachineStates) {
      if (stateMachineState.getType().equals(StateType.INIT) || stateMachineState.getType()
          .equals(StateType.INIT_ACCEPT)) {
        return stateMachineState;
      }
    }
    return null;
  }

  private StateMachineState getStateByName(String name) {
    for (StateMachineState stateMachineState : this.stateMachineStates) {
      if (stateMachineState.getName().equals(name)) {
        return stateMachineState;
      }
    }
    return null;
  }
}
