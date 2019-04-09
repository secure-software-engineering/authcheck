package de.fraunhofer.iem.authchecker.transformer;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.Map;
import org.apache.logging.log4j.Logger;

import de.fraunhofer.iem.authchecker.parser.ConfigurationParser;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;
import de.fraunhofer.iem.authchecker.util.MethodUtil;
import soot.Body;
import soot.BodyTransformer;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.ExceptionalUnitGraph;

public class ConfigurationParserTransformer extends BodyTransformer {

  private static final Logger LOGGER = LoggerUtil.getLogger();

  private static final String CONFIG_METHOD = "configure";

  private static final String CONFIG_CLASS = "com.example.demo.security.WebSecurityConfig";

  private ConfigurationParser configurationParser;

  private String configurationMethod;

  private String configurationClass;

  public ConfigurationParserTransformer(ConfigurationParser configurationParser, String configurationMethod, String configurationClass) {
    this.configurationParser = configurationParser;
    this.configurationMethod = configurationMethod;
    this.configurationClass = configurationClass;
  }

  protected void internalTransform(Body body, String phase, Map options) {
    if (this.filter(body.getMethod())) {
      LOGGER.info("Starting configuration parsing");
      ExceptionalUnitGraph eug = new ExceptionalUnitGraph(body);
      for (Unit unit : eug.getBody().getUnits()) {
        unit.apply(configurationParser);
      }
      LOGGER.info("Finished configuration parsing");
    }
  }

  private boolean filter(SootMethod method) {
    return MethodUtil.matchClassAndMethod(
        method,
        this.configurationClass,
        this.configurationMethod
    );
  }
}