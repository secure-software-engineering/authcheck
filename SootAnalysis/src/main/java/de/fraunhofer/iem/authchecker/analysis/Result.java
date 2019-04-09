package de.fraunhofer.iem.authchecker.analysis;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.Date;

public abstract class Result {

  private Date startTime;

  private Date finishTime;

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date start) {
    this.startTime = start;
  }

  public Date getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(Date finish) {
    this.finishTime = finish;
  }
}