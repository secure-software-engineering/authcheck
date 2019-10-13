package de.fraunhofer.iem.authchecker.comparator;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;
import java.util.Map;

import de.fraunhofer.iem.authchecker.entity.TruthTableEntity;
import de.fraunhofer.iem.authchecker.interpreter.AuthorizationEvaluationContext;
import de.fraunhofer.iem.authchecker.interpreter.AuthorizationInterpreter;
import de.fraunhofer.iem.authchecker.interpreter.BooleanInterpreter;

public class SpringExpressionComparator implements ExpressionComparator {

  private TruthTableEntity truthTable;

  public SpringExpressionComparator(Map<String, List<String>> relevantGroups) {
    this.truthTable = new TruthTableEntity(relevantGroups);
  }

  public boolean compare(String exprExpected, String exprCurrent) {
    for (int i = 0; i < this.truthTable.getRowCount(); i++) {
      //build context and interpreter
      AuthorizationEvaluationContext context = new AuthorizationEvaluationContext(
          truthTable.getGroupsForRow(i)
      );
      BooleanInterpreter interpreter = new AuthorizationInterpreter(context);

      //interpret both expressions
      boolean exprExpEvaluation = interpreter.interpret(exprExpected);
      boolean exprCurrEvaluation = interpreter.interpret(exprCurrent);

      //if we find one value that isn't the same, we return false
      if (exprExpEvaluation != exprCurrEvaluation) {
        return false;
      }
    }
    return true;
  }
}
