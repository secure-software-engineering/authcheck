package de.fraunhofer.iem.authchecker.interpreter;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

public class AuthorizationEvaluationContext {

  private List<String> roles;

  public AuthorizationEvaluationContext(List<String> roles) {
    this.roles = roles;
  }

  public boolean hasRole(String role) {
    return roles != null && this.roles.contains(role);
  }

  public boolean hasAnyRole(String... roles) {
    if (roles == null) {
      return false;
    }

    for (String role : roles) {
      if (this.roles.contains(role)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean hasAuthority(String role) {
	  return hasRole(role);
  }
  
  public boolean hasAnyAuthority(String... roles) {
	  return hasAnyRole(roles);
  }

  public boolean permitAll() {
    return true;
  }

  public boolean denyAll() {
    return false;
  }
}