package de.fraunhofer.iem.authchecker.analysis;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;
import java.util.Map;

import de.fraunhofer.iem.authchecker.util.SootAlgorithm;

public class CweConfiguration extends Configuration {

  private String inputModelPath;

  private String applicationPackage;

  private String controllerPackage;

  private List<String> controllerClasses;

  private String jceJarPath;

  private String rtJarPath;

  private String mainClass;

  private String configurationClass;

  private String configurationMethod;

  private SootAlgorithm sootAlgorithm;
  
  private Map<String, List<String>> groupPermissions;

  public CweConfiguration(String programDirectory, String inputModelPath, String applicationPackage,
      String controllerPackage, String jceJarPath, String rtJarPath, String mainClass,
      String configurationClass, String configurationMethod, List<String> controllerClasses,
      Map<String, List<String>> groupPermissions, SootAlgorithm sootAlgorithm) {
    super(programDirectory);
    this.inputModelPath = inputModelPath;
    this.applicationPackage = applicationPackage;
    this.controllerPackage = controllerPackage;
    this.jceJarPath = jceJarPath;
    this.rtJarPath = rtJarPath;
    this.mainClass = mainClass;
    this.configurationClass = configurationClass;
    this.configurationMethod = configurationMethod;
    this.controllerClasses = controllerClasses;
    this.groupPermissions = groupPermissions;
    this.sootAlgorithm = sootAlgorithm;
  }

  public String getInputModelPath() {
    return inputModelPath;
  }

  public void setInputModelPath(String inputModelPath) {
    this.inputModelPath = inputModelPath;
  }

  public String getApplicationPackage() {
    return applicationPackage;
  }

  public void setApplicationPackage(String applicationPackage) {
    this.applicationPackage = applicationPackage;
  }

  public String getControllerPackage() {
    return controllerPackage;
  }

  public void setControllerPackage(String controllerPackage) {
    this.controllerPackage = controllerPackage;
  }

  public String getJceJarPath() {
    return jceJarPath;
  }

  public void setJceJarPath(String jceJarPath) {
    this.jceJarPath = jceJarPath;
  }

  public String getRtJarPath() {
    return rtJarPath;
  }

  public void setRtJarPath(String rtJarPath) {
    this.rtJarPath = rtJarPath;
  }

  public String getMainClass() {
    return mainClass;
  }

  public void setMainClass(String mainClass) {
    this.mainClass = mainClass;
  }

  public String getConfigurationClass() {
    return configurationClass;
  }

  public void setConfigurationClass(String configurationClass) {
    this.configurationClass = configurationClass;
  }

  public String getConfigurationMethod() {
    return configurationMethod;
  }

  public void setConfigurationMethod(String configurationMethod) {
    this.configurationMethod = configurationMethod;
  }

  public List<String> getControllerClasses() {
    return controllerClasses;
  }

  public void setControllerClasses(List<String> controllerClasses) {
    this.controllerClasses = controllerClasses;
  }

  public SootAlgorithm getSootAlgorithm() {
    return sootAlgorithm;
  }

  public void setSootAlgorithm(SootAlgorithm sootCallGraphAlgorithm) {
    this.sootAlgorithm = sootCallGraphAlgorithm;
  }
  
  public void setGroupPermissions(Map<String, List<String>> groupPermissions) {
	this.groupPermissions = groupPermissions;
  }
  
  public Map<String, List<String>> getGroupPermissions(){
	  return this.groupPermissions;
  }
}
