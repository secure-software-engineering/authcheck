package de.fraunhofer.iem.authchecker.util;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

  /**
   * Extracts all matches between two delimiters inside a string.
   */
  public static ArrayList<String> extract(String str, String startDel, String endDel) {
    ArrayList<String> matches = new ArrayList<String>();
    Pattern pattern = Pattern.compile(startDel + "(.*?)" + endDel);
    Matcher matcher = pattern.matcher(str);
    while (matcher.find()) {
      matches.add(matcher.group(1));
    }
    return matches;
  }

  /**
   * Extracts only the first match between two delimiters inside a string.
   */
  public static String extractOne(String str, String startDel, String endDel) {
    return StringUtil.extract(str, startDel, endDel).get(0);
  }
}
