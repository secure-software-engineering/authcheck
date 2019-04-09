package de.fraunhofer.iem.authchecker.adapter;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import org.apache.logging.log4j.Logger;

import de.fraunhofer.iem.authchecker.entity.CallGraphEdgeEntity;
import de.fraunhofer.iem.authchecker.entity.CallGraphNodeTypeEntity;
import de.fraunhofer.iem.authchecker.entity.InputModelEntity;
import de.fraunhofer.iem.authchecker.model.CallGraphModel;
import de.fraunhofer.iem.authchecker.model.InputModel;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;
import soot.util.dot.DotGraph;
import soot.util.dot.DotGraphEdge;
import soot.util.dot.DotGraphNode;

public class DotAdapter {

  private static final Logger LOGGER = LoggerUtil.getLogger();

  private CallGraphModel callGraphModel;

  public DotAdapter(CallGraphModel callGraphModel) {
    this.callGraphModel = callGraphModel;
  }

  public void buildDotFile(String filePath, InputModel inputModel) {
    DotGraph dg = new DotGraph("call-graph");
    for (CallGraphEdgeEntity callGraphEdgeEntity : this.callGraphModel.getEdges()) {

      String groupsStartInputModel = "";
      if (callGraphEdgeEntity.getStart().getType()
          .equals(CallGraphNodeTypeEntity.CRITICAL_AUTHORIZATION)) {
        InputModelEntity method = inputModel
            .getMethod(callGraphEdgeEntity.getStart().getIdentifier());
        if (method != null && method.getAuthorizationExpression() != null) {
          groupsStartInputModel = "\n" + method.getAuthorizationExpression();
        }
      }

      String groupsEndInputModel = "";
      if (callGraphEdgeEntity.getEnd().getType()
          .equals(CallGraphNodeTypeEntity.CRITICAL_AUTHORIZATION)) {
        InputModelEntity method = inputModel
            .getMethod(callGraphEdgeEntity.getEnd().getIdentifier());
        if (method != null && method.getAuthorizationExpression() != null) {
          groupsEndInputModel = "\n" + method.getAuthorizationExpression();
        }
      }

      String srcNode =
          callGraphEdgeEntity.getStart().getIdentifier() + "\n" + callGraphEdgeEntity.getStart()
              .getType() + groupsStartInputModel;
      String tgtNode =
          callGraphEdgeEntity.getEnd().getIdentifier() + "\n" + callGraphEdgeEntity.getEnd()
              .getType() + groupsEndInputModel;
      DotGraphNode srcNodeDot = dg.drawNode(srcNode);
      DotGraphNode tgtNodeDot = dg.drawNode(tgtNode);
      this.addStyleToNode(srcNodeDot, callGraphEdgeEntity.getStart().getType());
      this.addStyleToNode(tgtNodeDot, callGraphEdgeEntity.getEnd().getType());
      DotGraphEdge edgeDot = dg.drawEdge(srcNode, tgtNode);
      if (callGraphEdgeEntity.getAuthExpression() != null) {
        edgeDot.setLabel(callGraphEdgeEntity.getAuthExpression());
      }
    }

    dg.plot(filePath);
  }

  private void addStyleToNode(DotGraphNode node, CallGraphNodeTypeEntity callGraphNodeTypeEntity) {
    String fillColor;
    switch (callGraphNodeTypeEntity) {
      case INIT:
        fillColor = "blue";
        break;
      case AUTHORIZE:
        fillColor = "green";
        break;
      case AUTHENTICATE:
        fillColor = "green";
        break;
      case CRITICAL_AUTHENTICATION:
        fillColor = "orange";
        break;
      case CRITICAL_AUTHORIZATION:
        fillColor = "red";
        break;
      case UNKNOWN:
        fillColor = "white";
        break;
      default:
        fillColor = "white";
    }
    node.setAttribute("fillcolor", fillColor);
    node.setStyle("filled");
  }

  public void buildPngFromDot(String file) {
    try {
      LOGGER.info("Start building png from file " + file);
      Process p = Runtime.getRuntime().exec(String.format("dot -T png -O %s", file));
      p.waitFor();
    } catch (Exception e) {
      LOGGER.error("Somehow failed to build png file from graph.");
    }
  }

}
