package de.fraunhofer.iem.authchecker.statemachine;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fraunhofer.iem.authchecker.analysis.CweConfiguration;
import de.fraunhofer.iem.authchecker.comparator.SpringExpressionComparator;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeEntity;
import de.fraunhofer.iem.authchecker.entity.InputModelEntity;
import de.fraunhofer.iem.authchecker.exception.CweViolationException;
import de.fraunhofer.iem.authchecker.model.InputModel;

public class StateMachineRunner {

  private CweConfiguration config;
	
  private InputModel inputModel;

  private StateMachine stateMachine;

  private StateMachineState currentState;

  private String currentAuthorizationExpression = "";

  public StateMachineRunner(StateMachine stateMachine, CweConfiguration config, InputModel inputModel) {
	this.config = config;
	this.inputModel = inputModel;
	this.currentState = null;
    this.stateMachine = stateMachine;
  }

  public void flow(CallGraphNodeEntity callGraphNodeEntity) throws RuntimeException, CweViolationException {
    // calculate the real method group for the node
    StateMachineMethodType stateMachineMethodType = this.convertNodeType(callGraphNodeEntity);
    switch (stateMachineMethodType) {
      case INIT:
        // initialize
        this.currentState = stateMachine.getInitState();
        return;
      case UNKNOWN:
        // just do nothing and return
        return;
      default:
        // otherwise just flow to the next state
        StateMachineState nextState = this.calculateNextState(stateMachineMethodType);
        if (nextState == null) {
          throw new RuntimeException("Unexpected null state.");
        }
        this.currentState = nextState;
    }
    // after flow, check if we are in an error state
    checkErrorState();
  }

  public void setCurrentAuthorizationExpression(String currentAuthorizationExpression) {
    this.currentAuthorizationExpression = currentAuthorizationExpression;
  }

  private StateMachineState calculateNextState(StateMachineMethodType methodType) {
    ArrayList<StateMachineEvent> stateMachineEvents = stateMachine.getOutOfEdges(currentState);
    for (StateMachineEvent stateMachineEvent : stateMachineEvents) {
      if (stateMachineEvent.getStateMachineMethodType().equals(methodType)) {
        return stateMachineEvent.getEnd();
      }
    }
    return null;
  }

  public StateMachineState getCurrentState() {
    return this.currentState;
  }

  public boolean isAcceptingState() {
    return currentState.getType().equals(StateType.ACCEPT)
        || currentState.getType().equals(StateType.INIT_ACCEPT);
  }

  private void checkErrorState() throws CweViolationException {
    if (this.currentState.getType().equals(StateType.ERROR)) {
      throw new CweViolationException(currentState.getName());
    }
  }

  //TODO: this should be refactored
  private StateMachineMethodType convertNodeType(CallGraphNodeEntity callGraphNodeEntity) {
    switch (callGraphNodeEntity.getType()) {
      case INIT:
        return StateMachineMethodType.INIT;
      case AUTHENTICATE:
        return StateMachineMethodType.AUTHENTICATE;
      case AUTHORIZE:
        return StateMachineMethodType.AUTHORIZE;
      case CRITICAL_AUTHENTICATION:
        return StateMachineMethodType.CRITICAL_AUTHENTICATION;
      case CRITICAL_AUTHORIZATION:
        InputModelEntity method = this.inputModel.getMethod(callGraphNodeEntity.getIdentifier());
        String exprExpected = method.getAuthorizationExpression();
        String exprCurrent = currentAuthorizationExpression;

        SpringExpressionComparator springExpressionComparator = new SpringExpressionComparator(
            this.calculateRelevantGroups(exprExpected, exprCurrent)
        );

        if (springExpressionComparator.compare(
            exprExpected,
            exprCurrent)) {
          return StateMachineMethodType.CRITICAL_AUTHORIZATION_GROUP;
        }
        return StateMachineMethodType.CRITICAL_AUTHORIZATION_NOT_GROUP;
      default:
        return StateMachineMethodType.UNKNOWN;
    }
  }

  //TODO: The old way of computing relevant groups can be buggy in a few 
  // instances, as it checks for containment in a string. Change this to
  // parse the expressions and extract the exact permissions/roles from them
  // and perform a check against them.
  
  private Map<String, List<String>> calculateRelevantGroups(String exp1, String exp2) {  
	  Map<String, List<String>> relevantGroups = new HashMap<String, List<String>> ();
	  if (exp1 != null && exp2 != null) {
		  for (String groupKey : this.config.getGroupPermissions().keySet()) {	
			  if (exp1.contains(groupKey) || exp2.contains(groupKey)) {
				  relevantGroups.put(groupKey, this.config.getGroupPermissions().get(groupKey));
			  } else {
				  for (String permission : this.config.getGroupPermissions().get(groupKey)) {	
					  if (exp1.contains(permission) || exp2.contains(permission)) {
						  relevantGroups.put(groupKey, this.config.getGroupPermissions().get(groupKey));
						  break;
					  }
				  }
			  }
		  }
	  }  
	  return relevantGroups;
  }
}
