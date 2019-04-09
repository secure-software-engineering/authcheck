package de.fraunhofer.iem.authchecker.analysis;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

import de.fraunhofer.iem.authchecker.phase.Phase;

public interface AnalysisInterface<AC, AR> {

  void execute();

  void setupPhases(AC config, List<Phase> phases);

  AR getResult();

  String getIdentifier();
}