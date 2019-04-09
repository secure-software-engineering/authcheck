package de.fraunhofer.iem.authchecker.transformer;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.Map;

import de.fraunhofer.iem.authchecker.entity.CallGraphEdgeEntity;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeEntity;
import de.fraunhofer.iem.authchecker.model.CallGraphModel;
import de.fraunhofer.iem.authchecker.model.InputModel;
import de.fraunhofer.iem.authchecker.util.MethodUtil;
import soot.Scene;
import soot.SceneTransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

public class CallGraphConstructionTransformer extends SceneTransformer {

  private CallGraphModel callGraphModel = new CallGraphModel();

  private InputModel inputModel;

  private String packageName;

  public CallGraphConstructionTransformer(InputModel inputModel, String packageName) {
    this.inputModel = inputModel;
    this.packageName = packageName;
  }

  @Override
  protected void internalTransform(String phaseName, Map options) {
    this.buildInternalCallGraph(Scene.v().getCallGraph());
  }

  public void buildInternalCallGraph(CallGraph callGraph) {
    for (Edge edge : callGraph) {
      if (this.filter(edge)) {
        String methodNameSrc = MethodUtil.generateMethodName(edge.getSrc().method());
        String methodNameTgt = MethodUtil.generateMethodName(edge.getTgt().method());

        String sourceSignature = edge.getSrc().method().getSignature();
        String targetSignature = edge.getTgt().method().getSignature();

        CallGraphNodeEntity src = new CallGraphNodeEntity(
            sourceSignature,
            inputModel.getNodeTypeBasedOnMethod(sourceSignature),
            methodNameSrc
        );

        CallGraphNodeEntity tgt = new CallGraphNodeEntity(
            targetSignature,
            inputModel.getNodeTypeBasedOnMethod(targetSignature),
            methodNameTgt
        );

        this.callGraphModel.addEdge(new CallGraphEdgeEntity(src, tgt, null));
      }
    }
  }

  private boolean filter(Edge edge) {
    return
        edge.getSrc().method().getDeclaringClass().getJavaPackageName().startsWith(this.packageName)
            && edge.getTgt().method().getDeclaringClass().getJavaPackageName()
            .startsWith(this.packageName)
            && !edge.getSrc().method().isStaticInitializer()
            && !edge.getTgt().method().isStaticInitializer();
  }

  public CallGraphModel getCallGraphModel() {
    return this.callGraphModel;
  }
}
