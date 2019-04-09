package de.fraunhofer.iem.authchecker.presenter;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import org.apache.logging.log4j.Logger;

import de.fraunhofer.iem.authchecker.analysis.CweResult;
import de.fraunhofer.iem.authchecker.entity.CweEntity;
import de.fraunhofer.iem.authchecker.entity.FailedResultPathEntity;
import de.fraunhofer.iem.authchecker.entity.ResultEntity;
import de.fraunhofer.iem.authchecker.entity.ResultPathEntity;
import de.fraunhofer.iem.authchecker.entity.SuccessfulResultPathEntity;
import de.fraunhofer.iem.authchecker.util.DateUtil;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;

public class LogConsoleResultPresenter implements AnalysisResultPresenter<CweResult> {

  protected static final Logger LOGGER = LoggerUtil.getLogger();

  private boolean log;

  private boolean console;

  public LogConsoleResultPresenter(boolean log, boolean console) {
    this.log = log;
    this.console = console;
  }

  public void present(CweResult result) {
    long seconds = DateUtil.timeDifference(result.getStartTime(), result.getFinishTime());
    LOGGER.info("Sorting results for presentation.");
    result.sortResultsByFailed();

    write("--------------------ANALYSIS-RESULT--------------------");
    write("CWE Analysis");
    write("The analysis took " + seconds + " seconds.");
    write("It was started on " + DateUtil.formatDate(result.getStartTime()) + " and finished on "
        + DateUtil.formatDate(result.getFinishTime()) + ".");

    for (CweEntity cwe : result.getCheckedCwes()) {
      String status = "OK";
      if (result.getViolatedCwes().contains(cwe)) {
        status = "FAILED";
      }
      write(cwe.getIdentifier() + ": " + status);
    }

    write("Paths: " + (result.getPathCount() - result.getVulnerablePathCount()) + " SUCCESS / "
        + result.getVulnerablePathCount() + " FAILED / " + result.getPathCount() + " TOTAL");
    write("-------------------------------------------------------");

    for (ResultEntity resultEntity : result.getResults()) {
      write("-----------------------PATH-START----------------------");
      write("Path from " + resultEntity.getPathStart() + " to " + resultEntity.getPathEnd());
      for (ResultPathEntity resultPath : resultEntity.getPaths()) {
        writePath(resultPath);
      }
      write("------------------------PATH-END-----------------------");
      write("");
    }

    write("--------------------------END--------------------------");
  }

  private void writePath(ResultPathEntity resultPath) {
    write(resultPath.getCweIdentifier() + " checked:");
    if (resultPath instanceof SuccessfulResultPathEntity) {
      SuccessfulResultPathEntity rpSuccess = (SuccessfulResultPathEntity) resultPath;
      write("Everything is fine");
    } else if (resultPath instanceof FailedResultPathEntity) {
      FailedResultPathEntity rpFailed = (FailedResultPathEntity) resultPath;
      write("Failed at path position " + rpFailed.getPathFailedIndex());
      write("Method: " + rpFailed.getMethodIdentifier());
      write("State machine state: " + rpFailed.getStateIdentifier());
      write("Error message: " + rpFailed.getErrorMessage());
      write("Fix message: " + rpFailed.getFixMessage());
    }
  }

  private void write(String message) {
    if (this.log) {
      writeToLog(message);
    }
    if (this.console) {
      writeToConsole(message);
    }
  }

  private void writeToLog(String message) {
    LOGGER.info(message);
  }

  private void writeToConsole(String message) {
    System.out.println(message);
  }
}