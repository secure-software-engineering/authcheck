package de.fraunhofer.iem.authchecker.artifact;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

import de.fraunhofer.iem.authchecker.entity.AnnotationEntity;
import de.fraunhofer.iem.authchecker.entity.ConfigurationEntity;
import de.fraunhofer.iem.authchecker.model.CallGraphModel;

public class CallGraphArtifact extends Artifact {

  private CallGraphModel callGraphModel;

  private List<AnnotationEntity> annotationEntities;

  private List<ConfigurationEntity> patternAuthorizationEntities;

  public CallGraphArtifact() {
    super("callGraph");
  }

  public CallGraphModel getCallGraphModel() {
    return callGraphModel;
  }

  public void setCallGraphModel(CallGraphModel callGraphModel) {
    this.callGraphModel = callGraphModel;
  }

  public List<AnnotationEntity> getAnnotationEntities() {
    return annotationEntities;
  }

  public void setAnnotationEntities(List<AnnotationEntity> annotationEntities) {
    this.annotationEntities = annotationEntities;
  }

  public List<ConfigurationEntity> getPatternAuthorizationEntities() {
    return patternAuthorizationEntities;
  }

  public void setPatternAuthorizationEntities(
      List<ConfigurationEntity> patternAuthorizationEntities) {
    this.patternAuthorizationEntities = patternAuthorizationEntities;
  }
}
