package de.fraunhofer.iem.authchecker.presenter;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import org.apache.logging.log4j.Logger;

import de.fraunhofer.iem.authchecker.adapter.TemplateAdapter;
import de.fraunhofer.iem.authchecker.analysis.CweResult;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;
import de.fraunhofer.iem.authchecker.util.ReportWebView;

public class HtmlResultPresenter implements AnalysisResultPresenter<CweResult> {

  protected static final Logger LOGGER = LoggerUtil.getLogger();

  private boolean pipelineMode;

  public HtmlResultPresenter(boolean pipelineMode) {
    this.pipelineMode = pipelineMode;
  }

  public void present(CweResult result) {
    LOGGER.info("Sorting results for report.");
    result.sortResultsByFailed();

    LOGGER.info("Building html report.");
    TemplateAdapter templateAdapter = new TemplateAdapter();
    try {
      templateAdapter.buildReport(result);
    } catch (Exception e) {
      LOGGER.error("Something went wrong at building the html report.");
    }
    LOGGER.info("Successfully created html analysis report.");

    if (!pipelineMode) {
      LOGGER.info("Will show html report view.");
      ReportWebView.showReport();
      LOGGER.info("Successfully showed html report view.");
    }
  }
}