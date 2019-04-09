package de.fraunhofer.iem.authchecker.model;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.List;

import de.fraunhofer.iem.authchecker.entity.CallGraphEdgeEntity;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeEntity;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeTypeEntity;

public class CallGraphModel {

  private ArrayList<CallGraphNodeEntity> callGraphNodeEntities;

  private ArrayList<CallGraphEdgeEntity> callGraphEdgeEntities;

  public CallGraphModel() {
    this.callGraphNodeEntities = new ArrayList<CallGraphNodeEntity>();
    this.callGraphEdgeEntities = new ArrayList<CallGraphEdgeEntity>();
  }

  public List<CallGraphEdgeEntity> getEdges() {
    return this.callGraphEdgeEntities;
  }

  public CallGraphModel addEdge(CallGraphEdgeEntity callGraphEdgeEntity) {
    if (!doesNodeExist(callGraphEdgeEntity.getStart())) {
      addNode(callGraphEdgeEntity.getStart());
    }

    if (!doesNodeExist(callGraphEdgeEntity.getEnd())) {
      addNode(callGraphEdgeEntity.getEnd());
    }

    this.callGraphEdgeEntities.add(callGraphEdgeEntity);
    return this;
  }

  private void addNode(CallGraphNodeEntity callGraphNodeEntity) {
    this.callGraphNodeEntities.add(callGraphNodeEntity);
  }

  public CallGraphNodeEntity getNode(String methodIdentifier) {
    for (CallGraphNodeEntity callGraphNodeEntity : this.callGraphNodeEntities) {
      if (callGraphNodeEntity.getIdentifier().equals(methodIdentifier)) {
        return callGraphNodeEntity;
      }
    }
    return null;
  }

  public ArrayList<CallGraphNodeEntity> getCallGraphNodeEntities() {
    return callGraphNodeEntities;
  }

  public ArrayList<CallGraphNodeEntity> getAllEndNodes() {
    ArrayList<CallGraphNodeEntity> foundCallGraphNodeEntities = new ArrayList<CallGraphNodeEntity>();
    for (CallGraphNodeEntity callGraphNodeEntity : this.callGraphNodeEntities) {
      if (this.isEndNode(callGraphNodeEntity)) {
        foundCallGraphNodeEntities.add(callGraphNodeEntity);
      }
    }
    return foundCallGraphNodeEntities;
  }

  public ArrayList<CallGraphEdgeEntity> getEdgesFrom(CallGraphNodeEntity start) {
    ArrayList<CallGraphEdgeEntity> foundCallGraphEdgeEntities = new ArrayList<CallGraphEdgeEntity>();
    for (CallGraphEdgeEntity callGraphEdgeEntity : this.callGraphEdgeEntities) {
      if (callGraphEdgeEntity.getStart().equals(start)) {
        foundCallGraphEdgeEntities.add(callGraphEdgeEntity);
      }
    }
    return foundCallGraphEdgeEntities;
  }

  public ArrayList<CallGraphEdgeEntity> getEdgesTo(CallGraphNodeEntity end) {
    ArrayList<CallGraphEdgeEntity> foundCallGraphEdgeEntities = new ArrayList<CallGraphEdgeEntity>();
    for (CallGraphEdgeEntity callGraphEdgeEntity : this.callGraphEdgeEntities) {
      if (callGraphEdgeEntity.getEnd().equals(end)) {
        foundCallGraphEdgeEntities.add(callGraphEdgeEntity);
      }
    }
    return foundCallGraphEdgeEntities;
  }

  public CallGraphEdgeEntity getEdge(CallGraphNodeEntity source, CallGraphNodeEntity target) {
    for (CallGraphEdgeEntity edge : this.callGraphEdgeEntities) {
      if (edge.getStart().equals(source) && edge.getEnd().equals(target)) {
        return edge;
      }
    }
    return null;
  }

  public void removeAllEdgesOfNode(CallGraphNodeEntity callGraphNodeEntity) {
    ArrayList<CallGraphEdgeEntity> edgesShouldBeRemoved = new ArrayList<CallGraphEdgeEntity>();
    for (CallGraphEdgeEntity callGraphEdgeEntity : this.callGraphEdgeEntities) {
      if (callGraphEdgeEntity.getEnd().equals(callGraphNodeEntity)
          || callGraphEdgeEntity.getStart().equals(callGraphNodeEntity)) {
        edgesShouldBeRemoved.add(callGraphEdgeEntity);
      }
    }
    this.callGraphEdgeEntities.removeAll(edgesShouldBeRemoved);
  }

  public void removeNode(CallGraphNodeEntity callGraphNodeEntity) {
    this.callGraphNodeEntities.remove(callGraphNodeEntity);
    this.removeAllEdgesOfNode(callGraphNodeEntity);
  }

  public void removeEdgeWithNodes(CallGraphEdgeEntity callGraphEdgeEntity) {
    this.removeEdge(callGraphEdgeEntity);
    this.removeNode(callGraphEdgeEntity.getStart());
    this.removeNode(callGraphEdgeEntity.getEnd());
  }

  public void removeEdge(CallGraphEdgeEntity callGraphEdgeEntity) {
    this.callGraphEdgeEntities.remove(callGraphEdgeEntity);
  }

  public boolean doesNodeExist(CallGraphNodeEntity cmpr) {
    for (CallGraphNodeEntity callGraphNodeEntity : callGraphNodeEntities) {
      if (callGraphNodeEntity.equals(cmpr)) {
        return true;
      }
    }
    return false;
  }

  public CallGraphNodeEntity getEntryPoint() {
    for (CallGraphNodeEntity callGraphNodeEntity : callGraphNodeEntities) {
      if (callGraphNodeEntity.getType().equals(CallGraphNodeTypeEntity.INIT)) {
        return callGraphNodeEntity;
      }
    }
    return null;
  }

  private boolean isEndNode(CallGraphNodeEntity callGraphNodeEntity) {
    return this.getEdgesFrom(callGraphNodeEntity).size() == 0;
  }
}
