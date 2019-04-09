package de.fraunhofer.iem.authchecker.entity;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.Collection;

public class ConfigurationEntity {

  private ArrayList<String> patterns;

  private String httpMethod;

  private String authorizationExpression;

  public ConfigurationEntity(String httpMethod) {
    this.httpMethod = httpMethod;
    this.authorizationExpression = "";
    this.patterns = new ArrayList<String>();
  }

  public ArrayList<String> getPatterns() {
    return patterns;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public String getAuthorizationExpression() {
    return authorizationExpression;
  }

  public void addPattern(String pattern) {
    this.patterns.add(pattern);
  }

  public void addPatterns(Collection<String> patterns) {
    this.patterns.addAll(patterns);
  }

  public void setAuthorizationExpression(String groupExpression) {
    this.authorizationExpression = groupExpression;
  }

  public void appendAuthorizationExpression(String appendExpression) {
    if(authorizationExpression.equals("")) {
      this.authorizationExpression += appendExpression;
    } else {
      this.authorizationExpression += " and " + appendExpression;
    }
  }

  @Override
  public String toString() {
    return "Patterns: " + this.patterns + " with Method: " + this.httpMethod
        + " authorization expression: " + this.authorizationExpression;
  }
}
