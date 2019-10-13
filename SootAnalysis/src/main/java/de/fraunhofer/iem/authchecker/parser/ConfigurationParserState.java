package de.fraunhofer.iem.authchecker.parser;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
public enum ConfigurationParserState {
  START, FOUND_AUTH_REQ, HTTP_METHOD, PATTERN, ANT_MATCHERS, ANY_REQUEST
}
