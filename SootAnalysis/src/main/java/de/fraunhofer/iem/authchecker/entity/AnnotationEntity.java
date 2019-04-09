package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/

public class AnnotationEntity {

  private String methodSignature;

  private String methodName;

  private String pattern;

  private String httpMethod;

  public AnnotationEntity() {

  }

  public String getMethodSignature() {
    return methodSignature;
  }

  public void setMethodSignature(String methodSignature) {
    this.methodSignature = methodSignature;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  @Override
  public String toString() {
    return "Method: " + this.methodSignature + " is annotated with pattern: " + this.pattern
        + " and http method: " + this.getHttpMethod();
  }
}
