package de.fraunhofer.iem.authchecker.util;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

  public static String formatDate(Date date) {
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.GERMANY);
    return dateFormat.format(date);
  }

  public static long timeDifference(Date startDate, Date endDate) {
    return (endDate.getTime() - startDate.getTime()) / 1000;
  }

}
