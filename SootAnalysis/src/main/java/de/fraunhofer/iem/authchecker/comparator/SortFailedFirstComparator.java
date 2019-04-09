package de.fraunhofer.iem.authchecker.comparator;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.Comparator;

import de.fraunhofer.iem.authchecker.entity.ResultEntity;

public class SortFailedFirstComparator implements Comparator<ResultEntity> {

  @Override
  public int compare(ResultEntity o1, ResultEntity o2) {
    if (o1.isFailed() && !o2.isFailed()) {
      return -1;
    } else if (!o1.isFailed() && o2.isFailed()) {
      return 1;
    }
    return 0;
  }
}