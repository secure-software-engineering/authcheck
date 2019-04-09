package de.fraunhofer.iem.authchecker;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

import de.fraunhofer.iem.authchecker.analysis.CweAnalysis;
import de.fraunhofer.iem.authchecker.analysis.CweConfiguration;
import de.fraunhofer.iem.authchecker.entity.CweEntity;
import de.fraunhofer.iem.authchecker.factory.CweFactory;
import de.fraunhofer.iem.authchecker.model.InputModel;
import de.fraunhofer.iem.authchecker.presenter.HtmlResultPresenter;
import de.fraunhofer.iem.authchecker.service.ConfigurationService;
import de.fraunhofer.iem.authchecker.service.InputModelService;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;

public class Checker {

  private static final Logger LOGGER = LoggerUtil.getLogger();

  public static void main(String[] args) {
    if (args.length == 0) {
      LOGGER.error("No arguments given. Please add path to configuration file as first argument.");
      System.exit(-1);
    }

    ConfigurationService configurationService = new ConfigurationService();
    CweConfiguration config = configurationService.parseFromFile(args[0]);

    InputModelService inputModelService = new InputModelService();
    InputModel inputModel = inputModelService.parseFromFile(config.getInputModelPath());

    ArrayList<CweEntity> cweEntities = new ArrayList<CweEntity>();
    cweEntities.add(CweFactory.buildCwe306());
    cweEntities.add(CweFactory.buildCwe862());
    cweEntities.add(CweFactory.buildCwe863());

    CweAnalysis analysis = new CweAnalysis(
        config,
        inputModel,
        cweEntities,
        new HtmlResultPresenter(false)
    );
    
    analysis.execute();
  }
}