package de.fraunhofer.iem.authchecker.util;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import soot.SootMethod;
import soot.jimple.StaticFieldRef;

public class MethodUtil {

  public static String generateMethodName(SootMethod method) {
    return method.getDeclaringClass().getName() + "." + method.getName();
  }

  public static boolean matchClassAndMethod(SootMethod method, String className,
      String methodName) {
    String actMethodName = method.getName();
    String actClassName = method.getDeclaringClass().getName();
    return actMethodName.equals(methodName) && actClassName.equals(className);
  }

  public static boolean matchClass(SootMethod method, String className) {
    String actClassName = method.getDeclaringClass().getName();
    return actClassName.equals(className);
  }

  public static boolean matchClass(StaticFieldRef ref, String className) {
    String actClassName = ref.getField().getDeclaringClass().getName();
    return actClassName.equals(className);
  }

}
