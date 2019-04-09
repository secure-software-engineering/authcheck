package de.fraunhofer.iem.authchecker.statemachine;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
public enum StateMachineMethodType {
  INIT, AUTHENTICATE, AUTHORIZE, CRITICAL_AUTHENTICATION, CRITICAL_AUTHORIZATION_NOT_GROUP, CRITICAL_AUTHORIZATION_GROUP, UNKNOWN
}