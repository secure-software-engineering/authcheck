package de.fraunhofer.iem.authchecker.analysis;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;

import de.fraunhofer.iem.authchecker.phase.Phase;
import de.fraunhofer.iem.authchecker.presenter.AnalysisResultPresenter;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;

public abstract class Analysis<AC extends Configuration, AR extends Result> implements
    AnalysisInterface<AC, AR> {

  protected static final Logger LOGGER = LoggerUtil.getLogger();
  protected AR result;
  protected AC config;
  private String identifier;
  private List<Phase> phases;
  private AnalysisResultPresenter<AR> presenter;

  public Analysis(String identifier, AC config, AR result, AnalysisResultPresenter<AR> presenter) {
    this.identifier = identifier;
    this.config = config;
    this.phases = new ArrayList<Phase>();
    this.result = result;
    this.presenter = presenter;
  }

  public void execute() {
    LOGGER.info("Will setup phases");
    this.setupPhases(this.config, this.phases);
    LOGGER.info("Starting analysis " + this.identifier);
    this.result.setStartTime(new Date());
    for (Phase phase : this.phases) {
      LOGGER.info("Starting phase " + phase.getIdentifier());
      phase.run();
      LOGGER.info("Finished phase " + phase.getIdentifier());
    }
    this.result.setFinishTime(new Date());
    LOGGER.info("Finished analysis");
    LOGGER.info("Starting presentation of results");
    this.presenter.present(result);
    LOGGER.info("Finished presenting results");
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public AR getResult() {
    return this.result;
  }
}
