package de.fraunhofer.iem.authchecker.model;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fraunhofer.iem.authchecker.entity.CallGraphNodeTypeEntity;
import de.fraunhofer.iem.authchecker.entity.InputModelEntity;

public class InputModel {

  private HashMap<String,InputModelEntity> entities;

  private List<String> groups;

  public InputModel() {
    this.entities = new HashMap<String, InputModelEntity>();
    this.groups = new ArrayList<String>();
  }

  public void addInputModelEntity(InputModelEntity entity) {
    this.entities.put(entity.getMethodSignature(), entity);
  }

  public void addGroup(String group) {
    this.groups.add(group);
  }

  public List<String> getGroups() {
    return this.groups;
  }

  public InputModelEntity getMethod(String methodSignature) {
    return entities.get(methodSignature);
  }

  private boolean isMethodAuthenticateCritical(String methodSignature) {
    InputModelEntity method = this.getMethod(methodSignature);
    return method != null
        && (method.getAuthorizationExpression() == null
        || method.getAuthorizationExpression().equals(""));
  }

  private boolean isMethodAuthorizeCritical(String methodSignature) {
    InputModelEntity method = this.getMethod(methodSignature);
    return method != null && method.getAuthorizationExpression() != null;
  }

  public CallGraphNodeTypeEntity getNodeTypeBasedOnMethod(String methodSignature) {
    if (this.isMethodAuthenticateCritical(methodSignature)) {
      return CallGraphNodeTypeEntity.CRITICAL_AUTHENTICATION;
    }

    if (this.isMethodAuthorizeCritical(methodSignature)) {
      return CallGraphNodeTypeEntity.CRITICAL_AUTHORIZATION;
    }

    return CallGraphNodeTypeEntity.UNKNOWN;
  }
}
