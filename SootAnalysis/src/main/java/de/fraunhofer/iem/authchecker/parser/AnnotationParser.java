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

  private static final String HTTP_METHOD_GET = "GET"; 
  private static final String HTTP_METHOD_PUT = "PUT"; 
  private static final String HTTP_METHOD_POST = "POST"; 
  private static final String HTTP_METHOD_DELETE = "DELETE"; 
    
  private static final String ANNOTATION_TYPE_REQUESTMAPPING = "Lorg/springframework/web/bind/annotation/RequestMapping;";
  private static final String ANNOTATION_TYPE_GETMAPPING  = "Lorg/springframework/web/bind/annotation/GetMapping;";
  private static final String ANNOTATION_TYPE_POSTMAPPING  = "Lorg/springframework/web/bind/annotation/PostMapping;";
  private static final String ANNOTATION_TYPE_PUTMAPPING  = "Lorg/springframework/web/bind/annotation/PutMapping;";
  private static final String ANNOTATION_TYPE_DELETEMAPPING  = "Lorg/springframework/web/bind/annotation/DeleteMapping;";
   
  
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
        for (AnnotationTag annoTag : vat.getAnnotations()) {
          if (parseAnnotationTag(annotationEntity, annoTag, classAnnotations)) {
            // we only add the annotation if the parsing was successful
            LOGGER.info("Added annotation " + annotationEntity);
            this.annotations.add(annotationEntity);
          }          
        }
      }
    }
  }

  private boolean parseAnnotationTag(AnnotationEntity annoEntity,
		  AnnotationTag annoTag, List<Tag> classAnnots) {
	  switch(annoTag.getType()) {
	  	case ANNOTATION_TYPE_GETMAPPING: 
	  		annoEntity.setHttpMethod(HTTP_METHOD_GET);
	  		break;
	  	case ANNOTATION_TYPE_POSTMAPPING:
	  		annoEntity.setHttpMethod(HTTP_METHOD_POST);
	  		break;
	  	case ANNOTATION_TYPE_PUTMAPPING: 
	  		annoEntity.setHttpMethod(HTTP_METHOD_PUT);
	  		break;	  		
	  	case ANNOTATION_TYPE_DELETEMAPPING: 
	  		annoEntity.setHttpMethod(HTTP_METHOD_DELETE);
	  		break;
	  	case ANNOTATION_TYPE_REQUESTMAPPING: 
	  		break;
	  	default: return false;
	  }
	  extractAnnotation(annoEntity, annoTag, classAnnots);
	  return true;
  }
  
  private void extractAnnotation(AnnotationEntity annoEntity,
		  AnnotationTag annoTag, List<Tag> classAnnots) {
	  for (AnnotationElem annotationElement : annoTag.getElems()) {
	    if (annotationElement instanceof AnnotationArrayElem) {
	      switch (annotationElement.getName()) {
	        case "value":
	        	annoEntity
	              .setPattern(this.extractClassPrefix(classAnnots) +
	                  this.extractPattern(annotationElement));
	          break;
	        case "method":
	        	annoEntity
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