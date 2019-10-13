package de.fraunhofer.iem.authchecker.service;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.fraunhofer.iem.authchecker.analysis.CweConfiguration;
import de.fraunhofer.iem.authchecker.exception.ConfigurationException;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;
import de.fraunhofer.iem.authchecker.util.SootAlgorithm;

public class ConfigurationService {

  private static final Logger LOGGER = LoggerUtil.getLogger();
  private static final String ERROR_MSG_NO_AUTH_GROUPS = "Please specify the 'authorizationGroups' in configuration file";
  
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
      
      JSONArray authorizationGroups = (JSONArray) ((JSONObject) obj).get("authorizationGroups");
      
      if(authorizationGroups == null) {
    	  throw new ConfigurationException(ERROR_MSG_NO_AUTH_GROUPS);
      }
      
      Map<String, List<String>> groupPermissions = new HashMap<String, List<String>>();
      
      for(Object groupInfo : authorizationGroups) {
    	  
    	  JSONObject groupDoc = (JSONObject) groupInfo;
    	  List<String> parsedPerms = new ArrayList<String>();
    	  
    	  try {
    		  JSONArray permissions = (JSONArray) groupDoc.get("permissions");
        	  for (Object permission : permissions) {
        		  parsedPerms.add(permission.toString());
    	      }  
    	  } catch (RuntimeException re) {
    		  // We allow the permissions to be empty, but then it will flat 
    		  // auth model.
    	  }  
    	  
    	  groupPermissions.put(groupDoc.get("groupName").toString(), parsedPerms);
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
          groupPermissions,
          SootAlgorithm.valueOf(sootAlgorithm)
      );
      
    } catch (ConfigurationException ce) {
    	LOGGER.error("Failed at parsing configuration file, error: " + ce.getMessage());
        System.exit(-1);
        return null;
    }  catch (Exception e) {
        LOGGER.error("Failed at parsing configuration file");
        System.exit(-1);
        return null;
    }
  }
}
