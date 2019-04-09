package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
public class InputModelEntity {

  private String methodSignature;

  private String authorizationExpression;

  public InputModelEntity(String methodSignature, String authorizationExpression) {
    this.methodSignature = methodSignature;
    this.authorizationExpression = authorizationExpression;
  }

  public String getMethodSignature() {
    return methodSignature;
  }

  public String getAuthorizationExpression() {
    return authorizationExpression;
  }

  @Override
  public String toString() {
    return this.methodSignature;
  }
}
