package de.fraunhofer.iem.authchecker.transformer;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;

import de.fraunhofer.iem.authchecker.parser.AnnotationParser;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;
import soot.Body;
import soot.BodyTransformer;
import soot.tagkit.Tag;

public class AnnotationParserTransformer extends BodyTransformer {

  private static final Logger LOGGER = LoggerUtil.getLogger();

  private AnnotationParser annotationParser;

  private String controllerPackage;

  public AnnotationParserTransformer(AnnotationParser annotationParser, String controllerPackage) {
    this.annotationParser = annotationParser;
    this.controllerPackage = controllerPackage;
  }

  protected void internalTransform(Body body, String phase, Map options) {
    if (filter(body)) {
      LOGGER.info("Found annotation parsing candidate " + body.getMethod());
      List<Tag> classAnnotations = body.getMethod().getDeclaringClass().getTags();
      this.annotationParser.parseAnnotation(body.getMethod(), classAnnotations);
    }
  }

  private boolean filter(Body body) {
    return body.getMethod().getSignature().contains(this.controllerPackage)
        && !body.getMethod().isConstructor()
        && body.getMethod().getTags().size() > 0;
  }
}
