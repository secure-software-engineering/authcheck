package de.fraunhofer.iem.authchecker.factory;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.HashMap;
import java.util.Map;

import de.fraunhofer.iem.authchecker.entity.CweEntity;
import de.fraunhofer.iem.authchecker.entity.CweMessagesEntity;
import de.fraunhofer.iem.authchecker.statemachine.StateMachine;
import de.fraunhofer.iem.authchecker.statemachine.StateMachineMethodType;
import de.fraunhofer.iem.authchecker.statemachine.StateType;

public class CweFactory {

  public static CweEntity buildCwe306() {
    StateMachine stateMachine = new StateMachine();
    stateMachine.addState("NA", StateType.INIT_ACCEPT);
    stateMachine.addState("A", StateType.ACCEPT);
    stateMachine.addState("CWE306", StateType.ERROR);
    stateMachine.addEdge("NA", "A", StateMachineMethodType.AUTHENTICATE);
    stateMachine.addEdge("NA", "CWE306", StateMachineMethodType.CRITICAL_AUTHENTICATION);
    stateMachine.addEdge("NA", "CWE306", StateMachineMethodType.CRITICAL_AUTHORIZATION_GROUP);
    stateMachine.addEdge("NA", "CWE306", StateMachineMethodType.CRITICAL_AUTHORIZATION_NOT_GROUP);
    stateMachine.addEdge("A", "A", StateMachineMethodType.CRITICAL_AUTHENTICATION);
    stateMachine.addEdge("A", "A", StateMachineMethodType.CRITICAL_AUTHORIZATION_GROUP);
    stateMachine.addEdge("A", "A", StateMachineMethodType.CRITICAL_AUTHORIZATION_NOT_GROUP);
    stateMachine.addEdge("A", "A", StateMachineMethodType.AUTHORIZE);

    Map<String, CweMessagesEntity> messages = new HashMap<String, CweMessagesEntity>();
    messages.put("CWE306", new CweMessagesEntity(
        "In this path there is no authentication method called but the method $additionalInformation.get(\"methodName\") needs a valid authentication.",
        "Please add authentication to the method $additionalInformation.get(\"methodName\")"
    ));

    return new CweEntity(306, "Missing authentication", stateMachine, messages);
  }

  public static CweEntity buildCwe862() {
    StateMachine stateMachine = new StateMachine();
    stateMachine.addState("NA", StateType.INIT_ACCEPT);
    stateMachine.addState("A", StateType.ACCEPT);
    stateMachine.addState("CWE862", StateType.ERROR);
    stateMachine.addEdge("NA", "A", StateMachineMethodType.AUTHORIZE);
    stateMachine.addEdge("NA", "NA", StateMachineMethodType.AUTHENTICATE);
    stateMachine.addEdge("NA", "NA", StateMachineMethodType.CRITICAL_AUTHENTICATION);
    stateMachine.addEdge("NA", "CWE862", StateMachineMethodType.CRITICAL_AUTHORIZATION_GROUP);
    stateMachine.addEdge("NA", "CWE862", StateMachineMethodType.CRITICAL_AUTHORIZATION_NOT_GROUP);
    stateMachine.addEdge("A", "A", StateMachineMethodType.CRITICAL_AUTHENTICATION);
    stateMachine.addEdge("A", "A", StateMachineMethodType.CRITICAL_AUTHORIZATION_GROUP);
    stateMachine.addEdge("A", "A", StateMachineMethodType.CRITICAL_AUTHORIZATION_NOT_GROUP);

    Map<String, CweMessagesEntity> messages = new HashMap<String, CweMessagesEntity>();
    messages.put("CWE862", new CweMessagesEntity(
        "In this path there is no authorization method called but the method $additionalInformation.get(\"methodName\") needs a valid authorization.",
        "Please add authorization to the method $additionalInformation.get(\"methodName\")"
    ));

    return new CweEntity(862, "Missing authorization", stateMachine, messages);
  }

  public static CweEntity buildCwe863() {
    StateMachine stateMachine = new StateMachine();
    stateMachine.addState("NA", StateType.INIT_ACCEPT);
    stateMachine.addState("A", StateType.ACCEPT);
    stateMachine.addState("CWE863", StateType.ERROR);
    stateMachine.addEdge("NA", "A", StateMachineMethodType.AUTHORIZE);
    stateMachine.addEdge("NA", "NA", StateMachineMethodType.AUTHENTICATE);
    stateMachine.addEdge("NA", "NA", StateMachineMethodType.CRITICAL_AUTHENTICATION);
    stateMachine.addEdge("NA", "CWE863", StateMachineMethodType.CRITICAL_AUTHORIZATION_GROUP);
    stateMachine.addEdge("NA", "CWE863", StateMachineMethodType.CRITICAL_AUTHORIZATION_NOT_GROUP);
    stateMachine.addEdge("A", "A", StateMachineMethodType.CRITICAL_AUTHENTICATION);
    stateMachine.addEdge("A", "A", StateMachineMethodType.CRITICAL_AUTHORIZATION_GROUP);
    stateMachine.addEdge("A", "CWE863", StateMachineMethodType.CRITICAL_AUTHORIZATION_NOT_GROUP);

    Map<String, CweMessagesEntity> messages = new HashMap<String, CweMessagesEntity>();
    messages.put("CWE863", new CweMessagesEntity(
        "In this path the given authorization is incorrect, because $additionalInformation.get(\"methodName\") needs more privileges.",
        "Please add authorization to the method $additionalInformation.get(\"methodName\")."
    ));

    return new CweEntity(863, "Incorrect authorization", stateMachine, messages);
  }

  public static CweEntity buildCwe306862863() {
    StateMachine stateMachine = new StateMachine();

    stateMachine.addState("NA", StateType.INIT_ACCEPT);
    stateMachine.addState("AUTHENTICATED", StateType.ACCEPT);
    stateMachine.addState("AUTHORIZED", StateType.ACCEPT);
    stateMachine.addState("CWE306", StateType.ERROR);
    stateMachine.addState("CWE862", StateType.ERROR);
    stateMachine.addState("CWE863", StateType.ERROR);

    stateMachine.addEdge("NA", "CWE306", StateMachineMethodType.CRITICAL_AUTHENTICATION);
    stateMachine.addEdge("NA", "CWE862", StateMachineMethodType.CRITICAL_AUTHORIZATION_GROUP);
    stateMachine.addEdge("NA", "CWE862", StateMachineMethodType.CRITICAL_AUTHORIZATION_NOT_GROUP);
    stateMachine.addEdge("NA", "CWE306", StateMachineMethodType.AUTHORIZE);
    stateMachine.addEdge("NA", "AUTHENTICATED", StateMachineMethodType.AUTHENTICATE);

    stateMachine
        .addEdge("AUTHENTICATED", "AUTHENTICATED", StateMachineMethodType.CRITICAL_AUTHENTICATION);
    stateMachine
        .addEdge("AUTHENTICATED", "CWE862", StateMachineMethodType.CRITICAL_AUTHORIZATION_GROUP);
    stateMachine.addEdge("AUTHENTICATED", "CWE862",
        StateMachineMethodType.CRITICAL_AUTHORIZATION_NOT_GROUP);
    stateMachine.addEdge("AUTHENTICATED", "AUTHORIZED", StateMachineMethodType.AUTHORIZE);

    stateMachine
        .addEdge("AUTHORIZED", "AUTHORIZED", StateMachineMethodType.CRITICAL_AUTHORIZATION_GROUP);
    stateMachine
        .addEdge("AUTHORIZED", "AUTHORIZED", StateMachineMethodType.CRITICAL_AUTHENTICATION);
    stateMachine
        .addEdge("AUTHORIZED", "CWE863", StateMachineMethodType.CRITICAL_AUTHORIZATION_NOT_GROUP);

    Map<String, CweMessagesEntity> messages = new HashMap<String, CweMessagesEntity>();
    messages.put("CWE306", new CweMessagesEntity(
        "In this path there is no authentication method called but the method $additionalInformation.get(\"methodName\") needs a valid authentication.",
        "Please add authentication to the method $additionalInformation.get(\"methodName\")"
    ));

    messages.put("CWE862", new CweMessagesEntity(
        "In this path there is no authentication method called but the method $additionalInformation.get(\"methodName\") needs a valid authentication.",
        "Please add authentication to the method $additionalInformation.get(\"methodName\")"
    ));

    messages.put("CWE863", new CweMessagesEntity(
        "In this path there is no authentication method called but the method $additionalInformation.get(\"methodName\") needs a valid authentication.",
        "Please add authentication to the method $additionalInformation.get(\"methodName\")"
    ));

    return new CweEntity(306862863, "Missing Authentication, Missing/Incorrect Authorization",
        stateMachine, messages);
  }
}
