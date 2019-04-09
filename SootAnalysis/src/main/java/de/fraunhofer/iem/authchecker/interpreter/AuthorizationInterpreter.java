package de.fraunhofer.iem.authchecker.interpreter;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class AuthorizationInterpreter implements BooleanInterpreter {

  private AuthorizationEvaluationContext authContext;

  public AuthorizationInterpreter(AuthorizationEvaluationContext authContext) {
    this.authContext = authContext;
  }

  public boolean interpret(String authorizationExpression) {
    //TODO: check if this is right
    if (authorizationExpression == null || authorizationExpression.equals("")) {
      return false;
    }

    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression(authorizationExpression);
    EvaluationContext context = new StandardEvaluationContext(this.authContext);
    return (boolean) exp.getValue(context);
  }
}
