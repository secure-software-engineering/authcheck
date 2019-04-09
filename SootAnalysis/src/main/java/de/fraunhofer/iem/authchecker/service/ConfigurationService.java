package de.fraunhofer.iem.authchecker.service;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.fraunhofer.iem.authchecker.analysis.CweConfiguration;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;
import de.fraunhofer.iem.authchecker.util.SootAlgorithm;

public class ConfigurationService {

  private static final Logger LOGGER = LoggerUtil.getLogger();

  public CweConfiguration parseFromFile(String filePath) {
    LOGGER.info("Starting configuration file parsing");
    try {
      File file = new File(filePath);
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(new FileReader(file));

      String inputModelPath = (((JSONObject) obj).get("inputModelPath").toString());
      String programDirectory = (((JSONObject) obj).get("programDirectory").toString());
      String applicationPackage = (((JSONObject) obj).get("applicationPackage").toString());
      String controllerPackage = (((JSONObject) obj).get("controllerPackage").toString());
      String jceJarPath = (((JSONObject) obj).get("jceJarPath").toString());
      String rtJarPath = (((JSONObject) obj).get("rtJarPath").toString());
      String mainClass = (((JSONObject) obj).get("mainClass").toString());
      String configurationClass = (((JSONObject) obj).get("configurationClass").toString());
      String configurationMethod = (((JSONObject) obj).get("configurationMethod").toString());
      String sootAlgorithm = (((JSONObject) obj).get("sootAlgorithm").toString());
      JSONArray controllerClasses = (JSONArray) ((JSONObject) obj).get("controllerClasses");

      List<String> controllerClassesList = new ArrayList<String>();
      // Add all controller classes
      for (Object controllerClass : controllerClasses) {
        controllerClassesList.add(controllerClass.toString());
      }
      LOGGER.info("Finished parsing configuration file");
      return new CweConfiguration(
          programDirectory,
          inputModelPath,
          applicationPackage,
          controllerPackage,
          jceJarPath,
          rtJarPath,
          mainClass,
          configurationClass,
          configurationMethod,
          controllerClassesList,
          SootAlgorithm.valueOf(sootAlgorithm)
      );
    } catch (Exception e) {
      LOGGER.error("Failed at parsing configuration file");
      System.exit(-1);
      return null;
    }
  }
}
