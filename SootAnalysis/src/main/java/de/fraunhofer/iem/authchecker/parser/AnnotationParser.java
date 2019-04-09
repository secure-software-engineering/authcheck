package de.fraunhofer.iem.authchecker.parser;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;

import de.fraunhofer.iem.authchecker.entity.AnnotationEntity;
import de.fraunhofer.iem.authchecker.util.LoggerUtil;
import de.fraunhofer.iem.authchecker.util.MethodUtil;
import soot.SootMethod;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;

public class AnnotationParser {

  protected static final Logger LOGGER = LoggerUtil.getLogger();

  private static final String ANNOTATION_TYPE = "Lorg/springframework/web/bind/annotation/RequestMapping;";

  private ArrayList<AnnotationEntity> annotations = new ArrayList<AnnotationEntity>();

  public void parseAnnotation(SootMethod method, List<Tag> classAnnotations) {
    AnnotationEntity annotationEntity = new AnnotationEntity();
    annotationEntity.setMethodSignature(method.getSignature());
    String methodName = MethodUtil.generateMethodName(method);
    annotationEntity.setMethodName(methodName);
    // check every visibility annotation tag
    for (Tag tag : method.getTags()) {
      if (tag instanceof VisibilityAnnotationTag) {
        VisibilityAnnotationTag vat = (VisibilityAnnotationTag) tag;
        for (AnnotationTag annotation : vat.getAnnotations()) {
          // check if we found the right annotation
          if (annotation.getType().equals(ANNOTATION_TYPE)) {
            extractAnnotation(annotationEntity, annotation, classAnnotations);
            // we only add the annotation if the parsing was successful
            LOGGER.info("Added annotation " + annotationEntity);
            this.annotations.add(annotationEntity);
          }
        }
      }
    }
  }

  private void extractAnnotation(AnnotationEntity annotationEntity,
      AnnotationTag annotation, List<Tag> classAnnotations) {
    for (AnnotationElem annotationElement : annotation.getElems()) {
      if (annotationElement instanceof AnnotationArrayElem) {
        switch (annotationElement.getName()) {
          case "value":
            annotationEntity
                .setPattern(this.extractClassPrefix(classAnnotations) +
                    this.extractPattern(annotationElement));
            break;
          case "method":
            annotationEntity
                .setHttpMethod(this.extractHttpMethod(annotationElement));
            break;
        }
      }
    }
  }

  private String extractClassPrefix(List<Tag> classAnnotations) {
    for (Tag tag : classAnnotations) {
      if (tag instanceof VisibilityAnnotationTag) {
        VisibilityAnnotationTag vat = (VisibilityAnnotationTag) tag;
        for (AnnotationTag annotation : vat.getAnnotations()) {
          if (annotation.getType().equals(ANNOTATION_TYPE)) {
            for (AnnotationElem annotationElement : annotation.getElems()) {
              if (annotationElement instanceof AnnotationArrayElem) {
                switch (annotationElement.getName()) {
                  case "value":
                    return this.extractPattern(annotationElement);
                }
              }
            }
          }
        }
      }
    }
    return "";
  }

  private String extractPattern(AnnotationElem annotationElem) {
    ArrayList<AnnotationElem> values = ((AnnotationArrayElem) annotationElem).getValues();
    for (AnnotationElem element : values) {
      if (element instanceof AnnotationStringElem) {
        return ((AnnotationStringElem) element).getValue();
      }
    }
    return null;
  }

  private String extractHttpMethod(AnnotationElem annotationElem) {
    ArrayList<AnnotationElem> values = ((AnnotationArrayElem) annotationElem).getValues();
    for (AnnotationElem element : values) {
      if (element instanceof AnnotationEnumElem) {
        return ((AnnotationEnumElem) element).getConstantName();
      }
    }
    return null;
  }

  public ArrayList<AnnotationEntity> getAnnotations() {
    return annotations;
  }
}