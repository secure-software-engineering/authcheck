package de.fraunhofer.iem.authchecker.util;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {

  private static final String LOGGER_NAME = "Analysis";

  public static Logger getLogger() {
    return LogManager.getLogger(LOGGER_NAME);
  }

  public static Logger getLogger(String loggerName) {
    return LogManager.getLogger(loggerName);
  }
}