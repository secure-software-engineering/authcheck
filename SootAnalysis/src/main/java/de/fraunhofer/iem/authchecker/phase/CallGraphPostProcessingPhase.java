package de.fraunhofer.iem.authchecker.phase;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

import de.fraunhofer.iem.authchecker.algorithm.PatternMatchingAlgorithm;
import de.fraunhofer.iem.authchecker.analysis.CweConfiguration;
import de.fraunhofer.iem.authchecker.artifact.Artifact;
import de.fraunhofer.iem.authchecker.artifact.CallGraphArtifact;
import de.fraunhofer.iem.authchecker.artifact.InputModelArtifact;
import de.fraunhofer.iem.authchecker.entity.AnnotationEntity;
import de.fraunhofer.iem.authchecker.entity.CallGraphEdgeEntity;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeEntity;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeTypeEntity;
import de.fraunhofer.iem.authchecker.entity.ConfigurationEntity;
import de.fraunhofer.iem.authchecker.model.CallGraphModel;
import de.fraunhofer.iem.authchecker.model.InputModel;

public class CallGraphPostProcessingPhase extends Phase<CweConfiguration> {

  private static final String SPRING_RUN_METHOD = "Spring.run";

  private static final String AUTHENTICATE_METHOD = "authenticate";

  private static final String AUTHORIZE_METHOD = "authorize";

  private PatternMatchingAlgorithm patternMatchingAlgorithm;

  public CallGraphPostProcessingPhase(CweConfiguration config,
      PatternMatchingAlgorithm algorithm, Artifact... artifacts) {
    super(config, "callGraphPostProcessing", artifacts);
    this.patternMatchingAlgorithm = algorithm;
  }

  public void run() {
    this.addStaticNodesAndEdges();
    this.addAuthenticationAuthorizationEdges();
  }

  private void addStaticNodesAndEdges() {
    CallGraphNodeEntity springCallGraphNodeEntity = new CallGraphNodeEntity(
        SPRING_RUN_METHOD,
        CallGraphNodeTypeEntity.INIT,
        SPRING_RUN_METHOD
    );

    CallGraphNodeEntity authenticateCallGraphNodeEntity = new CallGraphNodeEntity(
        AUTHENTICATE_METHOD,
        CallGraphNodeTypeEntity.AUTHENTICATE,
        AUTHENTICATE_METHOD);
    CallGraphNodeEntity authorizeCallGraphNodeEntity = new CallGraphNodeEntity(
        AUTHORIZE_METHOD,
        CallGraphNodeTypeEntity.AUTHORIZE,
        AUTHORIZE_METHOD
    );

    this.getCallGraph().addEdge(
        new CallGraphEdgeEntity(
            springCallGraphNodeEntity,
            authenticateCallGraphNodeEntity,
            null
        )
    );

    this.getCallGraph().addEdge(
        new CallGraphEdgeEntity(
            authenticateCallGraphNodeEntity,
            authorizeCallGraphNodeEntity,
            null
        )
    );
  }

  //TODO: maybe we can optimize this with an strategy pattern for the whole construction algorithm
  private void addAuthenticationAuthorizationEdges() {
    //for each critical Method that is annotated create an edge if path matches pattern
    for (AnnotationEntity annotation : this.getAnnotationEntities()) {
      String authExpression = "";
      boolean foundPattern = false;
      for (ConfigurationEntity patternAuthorization : this.getPatternAuthorizationEntities()) {
        for (String pattern : patternAuthorization.getPatterns()) {
          if (this.patternMatchingAlgorithm.matchPattern(pattern, annotation.getPattern())
              && ( (annotation.getHttpMethod() == null && patternAuthorization.getHttpMethod() == null) 
            		  || (annotation.getHttpMethod().equals(patternAuthorization.getHttpMethod())))) {
            foundPattern = true;
            if (authExpression == null || authExpression.equals("")) {
              authExpression = patternAuthorization.getAuthorizationExpression();
            } else {
              authExpression += " and " + patternAuthorization.getAuthorizationExpression();
            }
          }
        }
      }

      CallGraphNodeEntity criticalMethod = new CallGraphNodeEntity(annotation.getMethodSignature(),
          this.getInputModel().getNodeTypeBasedOnMethod(annotation.getMethodSignature()),
          annotation.getMethodName());
      if (!authExpression.equals("")) {
        //if we found groups we add them to the call graph edge.
        this.getCallGraph().addEdge(new CallGraphEdgeEntity(
            new CallGraphNodeEntity(AUTHORIZE_METHOD, CallGraphNodeTypeEntity.AUTHORIZE,
                AUTHORIZE_METHOD),
            criticalMethod, authExpression));
      } else if (authExpression.equals("") && foundPattern) {
        //TODO: maybe we can check if at least one hasRole() or hasAnyRole() is used
        //at least we found one pattern that matches, but no authorization expression, so it must be only authentication.
        this.getCallGraph().addEdge(new CallGraphEdgeEntity(
            new CallGraphNodeEntity(AUTHENTICATE_METHOD, CallGraphNodeTypeEntity.AUTHENTICATE,
                AUTHENTICATE_METHOD), criticalMethod, null));
      } else if (!foundPattern) {
        //if no pattern was found, it means that the method can be reached from the spring.run method.
        this.getCallGraph().addEdge(new CallGraphEdgeEntity(
            new CallGraphNodeEntity(SPRING_RUN_METHOD, CallGraphNodeTypeEntity.INIT,
                SPRING_RUN_METHOD),
            criticalMethod, null));
      }
    }
  }

  private CallGraphModel getCallGraph() {
    CallGraphArtifact artifact = (CallGraphArtifact) this.getArtifact("callGraph");
    return artifact.getCallGraphModel();
  }

  private InputModel getInputModel() {
    InputModelArtifact artifact = (InputModelArtifact) this.getArtifact("inputModel");
    return artifact.getInputModel();
  }

  private List<ConfigurationEntity> getPatternAuthorizationEntities() {
    CallGraphArtifact artifact = (CallGraphArtifact) this.getArtifact("callGraph");
    return artifact.getPatternAuthorizationEntities();
  }

  private List<AnnotationEntity> getAnnotationEntities() {
    CallGraphArtifact artifact = (CallGraphArtifact) this.getArtifact("callGraph");
    return artifact.getAnnotationEntities();
  }
}
