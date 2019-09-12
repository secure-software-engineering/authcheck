package de.fraunhofer.iem.authchecker.parser;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;

import org.apache.logging.log4j.Logger;

import de.fraunhofer.iem.authchecker.entity.ConfigurationEntity;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;
import de.fraunhofer.iem.authchecker.util.MethodUtil;
import soot.ArrayType;
import soot.RefType;
import soot.SootMethod;
import soot.jimple.AbstractStmtSwitch;
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
  private static final String ANT_AUTHORIZATION_CLASS = "org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer$AuthorizedUrl";
  private static final String ANT_HAS_ROLE_METHOD = "hasRole";
  private static final String ANT_AUTHENTICATED_METHOD = "authenticated";
  private static final String ANT_PERMIT_ALL_METHOD = "permitAll";
  private static final String ANT_ACCESS_METHOD = "access";

  private static final String AND_CLASS = "org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer$ExpressionInterceptUrlRegistry";
  private static final String AND_METHOD = "and";

  private static final String STRING_CLASS = "java.lang.String";

  private List<ConfigurationEntity> patternEntities;

  private ConfigurationParserState actState;

  private ConfigurationEntity lastPatternEnt;

  public ConfigurationParser(List<ConfigurationEntity> patternEntities) {
    this.patternEntities = patternEntities;
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
    }
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
    } else if (stmt.containsInvokeExpr() && MethodUtil
        .matchClassAndMethod(stmt.getInvokeExpr().getMethod(), AND_CLASS, AND_METHOD)) {
      this.actState = ConfigurationParserState.START;
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
        caseAntMachersHasRole(stmt);
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
    }
  }

  private void caseAntMachersHasRole(Stmt stmt) {
    StringConstant roleStringConstant = (StringConstant) stmt.getInvokeExpr().getArg(0);
    String authExpression = "hasRole('" + roleStringConstant.value + "')";
    this.lastPatternEnt.appendAuthorizationExpression(authExpression);
    this.patternEntities.add(lastPatternEnt);
    this.actState = ConfigurationParserState.FOUND_AUTH_REQ;
    LOGGER.info("Added pattern entity " + lastPatternEnt);
  }

  private void caseAntMatchersAccess(Stmt stmt) {
    StringConstant authExpression = (StringConstant) stmt.getInvokeExpr().getArg(0);
    this.lastPatternEnt.appendAuthorizationExpression(authExpression.value);
    this.patternEntities.add(lastPatternEnt);
    this.actState = ConfigurationParserState.FOUND_AUTH_REQ;
    LOGGER.info("Added pattern entity " + lastPatternEnt);
  }

  private void caseAntMatchersAuthenticate() {
    this.patternEntities.add(lastPatternEnt);
    this.actState = ConfigurationParserState.FOUND_AUTH_REQ;
    LOGGER.info("Added pattern entity " + lastPatternEnt);
  }

  private void caseAntMatchersPermitAll() {
    this.lastPatternEnt = null;
    this.actState = ConfigurationParserState.FOUND_AUTH_REQ;
  }

  public List<ConfigurationEntity> getPatternEntities() {
    return patternEntities;
  }
}
