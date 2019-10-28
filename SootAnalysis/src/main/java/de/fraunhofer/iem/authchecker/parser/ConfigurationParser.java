package de.fraunhofer.iem.authchecker.parser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

import org.apache.logging.log4j.Logger;

import de.fraunhofer.iem.authchecker.entity.AnnotationEntity;
import de.fraunhofer.iem.authchecker.entity.ConfigurationEntity;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;
import de.fraunhofer.iem.authchecker.util.MethodUtil;
import soot.ArrayType;
import soot.Local;
import soot.RefType;
import soot.SootMethod;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;

public class ConfigurationParser extends AbstractStmtSwitch {

  private static final Logger LOGGER = LoggerUtil.getLogger();

  private static final String AUTHORIZE_REQUESTS_CLASS = "org.springframework.security.config.annotation.web.builders.HttpSecurity";
  private static final String AUTHORIZE_REQUESTS_METHOD = "authorizeRequests";

  private static final String HTTP_METHOD_CLASS = "org.springframework.http.HttpMethod";

  private static final String ANT_MATCHERS_CLASS = "org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer$ExpressionInterceptUrlRegistry";
  private static final String ANT_MATCHERS_METHOD = "antMatchers";
  private static final String ANY_REQUEST_CLASS = "org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer$ExpressionInterceptUrlRegistry";
  private static final String ANY_REQUEST_METHOD = "anyRequest";
  private static final String ANT_AUTHORIZATION_CLASS = "org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer$AuthorizedUrl";
  private static final String ANT_HAS_ROLE_METHOD = "hasRole";
  private static final String ANT_HAS_ANY_ROLE_METHOD = "hasAnyRole";
  private static final String ANT_HAS_AUTHORITY_METHOD = "hasAuthority";
  private static final String ANT_HAS_ANY_AUTHORITY_METHOD = "hasAnyAuthority";
  private static final String ANT_AUTHENTICATED_METHOD = "authenticated";
  private static final String ANT_PERMIT_ALL_METHOD = "permitAll";
  private static final String ANT_ACCESS_METHOD = "access";

  private static final String AND_CLASS = "org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer$ExpressionInterceptUrlRegistry";
  private static final String AND_METHOD = "and";

  private static final String STRING_CLASS = "java.lang.String";

  private boolean isAllPatternAuth;
  
  private ConfigurationParserState actState;
  private ConfigurationEntity lastPatternEnt;
  private EntityStore parsedEntities;
  
  private HashMap<String, List<String>> localVariables;
  
  public ConfigurationParser(EntityStore parsedEntities) {
	this.parsedEntities = parsedEntities;
    this.localVariables = new HashMap<String, List<String>>();
	this.isAllPatternAuth = false;
	this.actState = ConfigurationParserState.START;
  }

  @Override
  public void caseAssignStmt(AssignStmt stmt) {
    switch (this.actState) {
      case START:
        caseStart(stmt);
        break;
      case FOUND_AUTH_REQ:
        caseFoundAuthReq(stmt);
        break;
      case HTTP_METHOD:
        caseHttpMethod(stmt);
        break;
      case PATTERN:
        casePattern(stmt);
        break;
      case ANT_MATCHERS:
        caseAntMatchers(stmt);
        break;
      case ANY_REQUEST:
      	caseAnyRequest(stmt);
      	break;
    }
  }

  @Override
  public void caseInvokeStmt(InvokeStmt stmt) {
    switch (this.actState) {
      case START:
        caseStart(stmt);
        break;
      case ANT_MATCHERS:
        caseAntMatchers(stmt);
        break;
      case ANY_REQUEST:
    	caseAnyRequest(stmt);
    	break;
    }
  }

  private void finishUpParsing() {
	  
	  // No need for thread safety in here.
	  if (isAllPatternAuth) {
		 
		  List<ConfigurationEntity> allPatterns = new ArrayList<ConfigurationEntity>();
		  
		  for (AnnotationEntity annotEntity : parsedEntities.getAnnotations()) {
			ConfigurationEntity newEntity = new ConfigurationEntity(annotEntity.getHttpMethod());
			newEntity.addPattern(annotEntity.getPattern());
			allPatterns.add(newEntity);
			
			LOGGER.info("Added pattern entity " + newEntity);
		  }
		  
		  for (ConfigurationEntity patternEntity : parsedEntities.getPatterns()) {
	    	 ConfigurationEntity newEntity = new ConfigurationEntity(patternEntity.getHttpMethod());
	    	 newEntity.addPatterns(patternEntity.getPatterns());
	    	 allPatterns.add(newEntity);
	    	 
	    	 LOGGER.info("Added pattern entity " + newEntity);
		 }
		
		 parsedEntities.getPatterns().addAll(0, allPatterns);
	  }
	  
	  isAllPatternAuth = false;
  }
  
  private void caseStart(Stmt stmt) {
    if (stmt.containsInvokeExpr()) {
      SootMethod method = stmt.getInvokeExpr().getMethod();
      if (MethodUtil
          .matchClassAndMethod(method, AUTHORIZE_REQUESTS_CLASS, AUTHORIZE_REQUESTS_METHOD)) {
        this.actState = ConfigurationParserState.FOUND_AUTH_REQ;
      }
    }
  }

  private void caseFoundAuthReq(AssignStmt stmt) {
    if (stmt.getRightOp() instanceof StaticFieldRef) {
      StaticFieldRef fieldRef = ((StaticFieldRef) stmt.getRightOp());
      if (MethodUtil.matchClass(fieldRef, HTTP_METHOD_CLASS)) {
        String httpMethod = fieldRef.getField().getName();
        this.lastPatternEnt = new ConfigurationEntity(httpMethod);
        actState = ConfigurationParserState.HTTP_METHOD;
      }
    } else if (stmt.getRightOp().getType() instanceof ArrayType) {
    	this.lastPatternEnt = new ConfigurationEntity(null);
    	this.caseHttpMethod(stmt);
    } else if (stmt.containsInvokeExpr() && MethodUtil
        .matchClassAndMethod(stmt.getInvokeExpr().getMethod(), AND_CLASS, AND_METHOD)) {
      this.actState = ConfigurationParserState.START;
    } else if (stmt.containsInvokeExpr() && MethodUtil
    	.matchClassAndMethod(stmt.getInvokeExpr().getMethod(),
    			ANY_REQUEST_CLASS, ANY_REQUEST_METHOD)) {
    	this.actState = ConfigurationParserState.ANY_REQUEST;
    }
  }

  private void caseHttpMethod(AssignStmt stmt) {
    if (stmt.getRightOp().getType() instanceof ArrayType) {
      ArrayType arrayType = (ArrayType) stmt.getRightOp().getType();
      if (arrayType.getArrayElementType() instanceof RefType) {
        RefType refType = (RefType) arrayType.getArrayElementType();
        if (refType.getClassName().equals(STRING_CLASS)) {
          this.actState = ConfigurationParserState.PATTERN;
        }
      }
    }
  }

  private void casePattern(AssignStmt stmt) {
    if (stmt.containsInvokeExpr()) {
      SootMethod method = stmt.getInvokeExpr().getMethod();
      if (MethodUtil.matchClassAndMethod(method, ANT_MATCHERS_CLASS, ANT_MATCHERS_METHOD)) {
        this.actState = ConfigurationParserState.ANT_MATCHERS;
      } 
    } else if (stmt.getRightOp() instanceof StringConstant) {
      StringConstant stringConstant = (StringConstant) stmt.getRightOp();
      this.lastPatternEnt.addPattern(stringConstant.value);
    }
  }

  private void caseAntMatchers(Stmt stmt) {
    if (stmt.containsInvokeExpr()) {
      SootMethod method = stmt.getInvokeExpr().getMethod();
      if (MethodUtil
          .matchClassAndMethod(method, ANT_AUTHORIZATION_CLASS, ANT_HAS_ROLE_METHOD)
          && stmt.getInvokeExpr().getArgs().size() == 1) {
    	  caseAntMachersX(stmt, ANT_HAS_ROLE_METHOD, true);
      } else if (MethodUtil
              .matchClassAndMethod(method, ANT_AUTHORIZATION_CLASS, ANT_HAS_ANY_ROLE_METHOD)) {
            caseAntMachersX(stmt, ANT_HAS_ANY_ROLE_METHOD, false);
      } else if (MethodUtil
              .matchClassAndMethod(method, ANT_AUTHORIZATION_CLASS, ANT_HAS_AUTHORITY_METHOD)
              && stmt.getInvokeExpr().getArgs().size() == 1) {
            caseAntMachersX(stmt, ANT_HAS_AUTHORITY_METHOD, true);
      } else if (MethodUtil
              .matchClassAndMethod(method, ANT_AUTHORIZATION_CLASS, ANT_HAS_ANY_AUTHORITY_METHOD)) {
            caseAntMachersX(stmt, ANT_HAS_ANY_AUTHORITY_METHOD, false);
      } else if (MethodUtil
          .matchClassAndMethod(method, ANT_AUTHORIZATION_CLASS, ANT_ACCESS_METHOD)
          && stmt.getInvokeExpr().getArgs().size() == 1) {
        caseAntMatchersAccess(stmt);
      } else if (MethodUtil
          .matchClassAndMethod(method, ANT_AUTHORIZATION_CLASS, ANT_AUTHENTICATED_METHOD)) {
        caseAntMatchersAuthenticate();
      } else if (MethodUtil
          .matchClassAndMethod(method, ANT_AUTHORIZATION_CLASS, ANT_PERMIT_ALL_METHOD)) {
        caseAntMatchersPermitAll();
      }
    } else if (stmt instanceof AssignStmt &&
    		((AssignStmt)stmt).getRightOp().getType() instanceof ArrayType) {
    	AssignStmt assignStmt = (AssignStmt)stmt;
    	ArrayType arrayType = (ArrayType) assignStmt.getRightOp().getType();    	
    	if (arrayType.getArrayElementType() instanceof RefType) {
            RefType refType = (RefType) arrayType.getArrayElementType();
            if (refType.getClassName().equals(STRING_CLASS) &&
            		assignStmt.getLeftOp() instanceof Local) {
            	Local localVar = (Local) assignStmt.getLeftOp();
            	this.localVariables.put(localVar.getName(), new LinkedList<String>());
            }
    	}
    } else if (stmt instanceof AssignStmt &&
    		((AssignStmt)stmt).getLeftOp() instanceof ArrayRef &&
    		((AssignStmt)stmt).getRightOp() instanceof StringConstant) {
    	AssignStmt assignStmt = (AssignStmt)stmt;
    	ArrayRef arrayRef = (ArrayRef) assignStmt.getLeftOp();
    	StringConstant stringConstant = (StringConstant) assignStmt.getRightOp();
    	String arrayVar = arrayRef.getBaseBox().getValue() instanceof Local ? 
    			((Local) arrayRef.getBaseBox().getValue()).getName() : "";
    	if (this.localVariables.containsKey(arrayVar)) {
    		this.localVariables.get(arrayVar).add(stringConstant.value);
    	}
    }
  }
  
  private void caseAnyRequest(Stmt stmt) {
	  if (stmt.containsInvokeExpr()) {
	      SootMethod method = stmt.getInvokeExpr().getMethod();
	      
	      if (MethodUtil
	          .matchClassAndMethod(method, ANT_AUTHORIZATION_CLASS, ANT_AUTHENTICATED_METHOD)) {
	    	
	    	  this.actState = ConfigurationParserState.FOUND_AUTH_REQ;
	    	isAllPatternAuth = true;
	    	
	      } else if (MethodUtil
	          .matchClassAndMethod(method, ANT_AUTHORIZATION_CLASS, ANT_PERMIT_ALL_METHOD)) {
	        caseAntMatchersPermitAll();
	      }
	  }
  }
  
  private void caseAntMachersX(Stmt stmt, String method, boolean singleArg) {
	String authExpression;
	if (singleArg) {
		StringConstant stringConstant = (StringConstant) stmt.getInvokeExpr().getArg(0);
	    authExpression = method + "('" + stringConstant.value + "')";
	} else {
		Local argVar = (Local) stmt.getInvokeExpr().getArg(0);
		List<String> authPerms = this.localVariables.get(argVar.getName());	
		authExpression = method + "(";
		for (int i = 0; i < authPerms.size(); i++) {
			authExpression += "'" +(String) authPerms.get(i) + "'";
			if (i != authPerms.size() - 1) {
				authExpression += ", ";
			} 
		}		
		authExpression += ")";
	}	
	this.lastPatternEnt.appendAuthorizationExpression(authExpression);
    this.parsedEntities.addPattern(lastPatternEnt);
    this.actState = ConfigurationParserState.FOUND_AUTH_REQ;
    LOGGER.info("Added pattern entity " + lastPatternEnt);
  }

  private void caseAntMatchersAccess(Stmt stmt) {
    StringConstant authExpression = (StringConstant) stmt.getInvokeExpr().getArg(0);
    this.lastPatternEnt.appendAuthorizationExpression(authExpression.value);
    this.parsedEntities.addPattern(lastPatternEnt);
    this.actState = ConfigurationParserState.FOUND_AUTH_REQ;
    LOGGER.info("Added pattern entity " + lastPatternEnt);
  }

  private void caseAntMatchersAuthenticate() {
	this.parsedEntities.addPattern(lastPatternEnt);
    this.actState = ConfigurationParserState.FOUND_AUTH_REQ;
    LOGGER.info("Added pattern entity " + lastPatternEnt);
  }

  private void caseAntMatchersPermitAll() {
    this.lastPatternEnt = null;
    this.actState = ConfigurationParserState.FOUND_AUTH_REQ;
  }

  public List<ConfigurationEntity> getPatternEntities() {
    finishUpParsing();
	return this.parsedEntities.getPatterns();
  }
}
