package de.fraunhofer.iem.authchecker.interpreter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AuthorizationEvaluationContext {

  private Set<String> allPermissions;
  private Map<String, List<String>> groupsPermissions;
  
  public AuthorizationEvaluationContext(Map<String, List<String>> groupsPermissions) {
	this.groupsPermissions = groupsPermissions;
	this.allPermissions = new HashSet<String>();
	for (List<String> permissions : this.groupsPermissions.values()) {
		for(String permission : permissions) {
			if (!this.allPermissions.contains(permission)) {
				this.allPermissions.add(permission);
			}
		}
	}
  }

  public boolean hasRole(String role) {
	  
	  if (role == null) {
		  return false;	  
	  }
	  
	  // Starting from Spring 4, the hasRole() method prepends the 
	  // "ROLE_" prefix with role/authority specified.
	  
	  if (role.indexOf("ROLE_") != 0) {
		  role = "ROLE_" + role;
	  }
	  
	  return hasAuthority(role);
  }

  public boolean hasAnyRole(String... roles) {
	if (roles == null) {
	  return false;
	}
	
	for (String role : roles) {
	  if (hasRole(role)) {
	    return true;
	  }
	}
	return false;
  }
  
  public boolean hasAuthority(String authority) {
	  if (authority == null) {
		  return false;	  
	  }
	  
	  return this.groupsPermissions.containsKey(authority) || this.allPermissions.contains(authority);
  }
  
  public boolean hasAnyAuthority(String... roles) {
	if (roles == null) {
		return false;
	}
	
	for (String role : roles) {
		if (hasRole(role)) {
			return true;
		}
	}
	
	return false;
  }

  public boolean permitAll() {
    return true;
  }

  public boolean denyAll() {
    return false;
  }
}