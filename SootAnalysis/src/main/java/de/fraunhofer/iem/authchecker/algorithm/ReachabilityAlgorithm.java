package de.fraunhofer.iem.authchecker.algorithm;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

import de.fraunhofer.iem.authchecker.entity.CallGraphNodeEntity;
import de.fraunhofer.iem.authchecker.model.CallGraphModel;

public interface ReachabilityAlgorithm {

  List<CallGraphNodeEntity> execute(CallGraphModel callGraphModel, CallGraphNodeEntity source);
}