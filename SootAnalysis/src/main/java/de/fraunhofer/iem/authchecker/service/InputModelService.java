package de.fraunhofer.iem.authchecker.service;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.io.File;
import java.io.FileReader;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.fraunhofer.iem.authchecker.entity.InputModelEntity;
import de.fraunhofer.iem.authchecker.exception.ConfigurationException;
import de.fraunhofer.iem.authchecker.model.InputModel;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;

public class InputModelService {

  private static final Logger LOGGER = LoggerUtil.getLogger();
  private static final String ERROR_MSG_AUTH_GROUPS = "The 'authorizationGroups' are expected in the configuration file";
  
  public InputModel parseFromFile(String filePath) {
    LOGGER.info("Starting input model parsing");
    InputModel inputModel = new InputModel();
    try {

      File file = new File(filePath);
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(new FileReader(file));

      if(((JSONObject) obj).get("authorizationGroups") != null) {
    	  throw new ConfigurationException(ERROR_MSG_AUTH_GROUPS);
      }
      
      JSONArray criticalMethods = (JSONArray) ((JSONObject) obj).get("criticalMethods");

      // Add all groups
      //for (Object group : groups) {
      //  inputModel.addGroup(group.toString());
      //}

      // Add all critical Methods
      for (Object criticalMethod : criticalMethods) {
        JSONObject criticalMethodJSON = (JSONObject) criticalMethod;
        String methodSignature = criticalMethodJSON.get("methodSignature").toString();

        String authorizationExpression = null;

        if (criticalMethodJSON.get("authorizationExpression") != null) {
          authorizationExpression = criticalMethodJSON.get("authorizationExpression")
              .toString();
        }

        InputModelEntity method = new InputModelEntity(
            methodSignature,
            authorizationExpression
        );

        inputModel.addInputModelEntity(method);
        LOGGER.info("Added method to input model " + method);
      }
      return inputModel;
    } catch (ConfigurationException ce) {
    	LOGGER.error("Failed at parsing input model, error: " + ce.getMessage());
        System.exit(-1);
        return null;
    } catch (Exception e) {
      LOGGER.error("Failed at parsing input model");
      System.exit(-1);
      return null;
    }
  }
}
