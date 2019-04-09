package de.fraunhofer.iem.authchecker.algorithm;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import org.springframework.util.AntPathMatcher;

public class SpringPatternMatchingAlgorithm implements PatternMatchingAlgorithm {

  public boolean matchPattern(String pattern, String path) {
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    return antPathMatcher.match(pattern, path);
  }
}
