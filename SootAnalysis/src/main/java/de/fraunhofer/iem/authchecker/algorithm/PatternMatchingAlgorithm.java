package de.fraunhofer.iem.authchecker.algorithm;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
public interface PatternMatchingAlgorithm {

  boolean matchPattern(String pattern, String path);
}
