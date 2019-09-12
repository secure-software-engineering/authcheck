package de.fraunhofer.iem.authchecker.parser;
/*******************************************************************************
 * Copyright (c) 2019 Fraunhofer IEM, Paderborn, Germany.
 * 
 ******************************************************************************/
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.logging.log4j.Logger;

import de.fraunhofer.iem.authchecker.entity.AnnotationEntity;
import de.fraunhofer.iem.authchecker.entity.ConfigurationEntity;
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
  
  private List<AnnotationEntity> annotations;
  private List<ConfigurationEntity> patternEntities;
  
  public AnnotationParser(List<ConfigurationEntity> patternEntities) {
	  this.annotations = new ArrayList<AnnotationEntity>();
	  this.patternEntities = patternEntities;
  }
  
  public void parseAnnotation(SootMethod method, List<Tag> classAnnotations) {
    // Check for every visible annotation tag
    for (Tag tag : method.getTags()) {
      if (tag instanceof VisibilityAnnotationTag) {
    	  parseAnnotations(method, (VisibilityAnnotationTag) tag, classAnnotations);
      }
    }
  }
	  
  private void parseAnnotations(SootMethod method,
		  VisibilityAnnotationTag vat, List<Tag> classAnnotations) {
	  
		MethodAnnotationInfo methodInfo = extractMethodAnnotationInfo(
				vat, classAnnotations);
		
		if (methodInfo.isMappingAnnotated()) {
			AnnotationEntity annotationEntity = new AnnotationEntity();
			annotationEntity.setMethodSignature(method.getSignature());
			annotationEntity.setMethodName(MethodUtil.generateMethodName(method));
			annotationEntity.setHttpMethod(methodInfo.getHttpMethod());
			annotationEntity.setPattern(methodInfo.getPattern());
			this.annotations.add(annotationEntity);
			LOGGER.info("Added annotation Method:" + annotationEntity);
		}
		
		//Method-level security annotations, if supplied, will always override
		//annotations specified at the class level.		 
		if(methodInfo.isAuthAnnotated() || methodInfo.isClassAuthAnnotated()) {
			ConfigurationEntity confEntity = new ConfigurationEntity(
					methodInfo.getHttpMethod());
			confEntity.addPattern(methodInfo.getPattern());
			String pattern = methodInfo.isAuthAnnotated() ? 
					methodInfo.getAuthPattern() : methodInfo.getClassAuthPattern();
			confEntity.appendAuthorizationExpression(pattern);		
			this.patternEntities.add(confEntity);
			LOGGER.info("Added pattern entity" + confEntity);
		}		
  }
  
  private MethodAnnotationInfo extractMethodAnnotationInfo(
		  VisibilityAnnotationTag vat, List<Tag> classAnnotations) {
	  MethodAnnotationInfo methodInfo = new MethodAnnotationInfo();
	  methodInfo.setClassAnnotationInfo(
			  this.extractClassAnnotationInfo(classAnnotations));
	  for (AnnotationTag annoTag : vat.getAnnotations()) {  
	      if (parseAnnotationTag(annoTag, methodInfo)) {
	    	  //LOGGER.info("Annotation read:" + annoTag);      
	      }
	  }
	  return methodInfo;
  }
  
  private ClassAnnotationInfo extractClassAnnotationInfo(List<Tag> classAnnotations) {
	  ClassAnnotationInfo classInfo = new ClassAnnotationInfo(); 
	  for (Tag tag : classAnnotations) {
      if (tag instanceof VisibilityAnnotationTag) {
        VisibilityAnnotationTag vat = (VisibilityAnnotationTag) tag;    
        for (AnnotationTag annotation : vat.getAnnotations()) {
        	if (parseAnnotationTag(annotation, classInfo)) {
        		//LOGGER.info("Annotation read:" + annotation);    
        	}
	      }
        }
      }
	  return classInfo;
  }

  private boolean parseAnnotationTag(AnnotationTag annoTag,
		  AnnotationInfo annotationInfo) {
	  switch(annoTag.getType()) {
	  	// *-Mapping annotation related cases... 
	  	case AnnotationResource.ANNOTATION_TYPE_GETMAPPING: 
	  		extractMappingAnnotation(annoTag.getElems(), annotationInfo, AnnotationResource.HTTP_METHOD_GET);
	  		break;
	  	case AnnotationResource.ANNOTATION_TYPE_POSTMAPPING: 
	  		extractMappingAnnotation(annoTag.getElems(), annotationInfo, AnnotationResource.HTTP_METHOD_POST);
	  		break;
	  	case AnnotationResource.ANNOTATION_TYPE_PUTMAPPING: 
	  		extractMappingAnnotation(annoTag.getElems(), annotationInfo, AnnotationResource.HTTP_METHOD_PUT);
	  		break;  		
	  	case AnnotationResource.ANNOTATION_TYPE_DELETEMAPPING: 
	  		extractMappingAnnotation(annoTag.getElems(), annotationInfo, AnnotationResource.HTTP_METHOD_DELETE);
	  		break;
	  	case AnnotationResource.ANNOTATION_TYPE_PATCHMAPPING:
	  		extractMappingAnnotation(annoTag.getElems(), annotationInfo, AnnotationResource.HTTP_METHOD_PATCH);
	  		break;
	  	case AnnotationResource.ANNOTATION_TYPE_REQUESTMAPPING: 
	  		extractMappingAnnotation(annoTag.getElems(), annotationInfo, null);
	  		break;
	  		
	  	// Auth-annotations related cases...
	  	case AnnotationResource.ANNOTATION_TYPE_SECURED:
	  	case AnnotationResource.ANNOTATION_TYPE_PREAUTHORIZE:
	  	case AnnotationResource.ANNOTATION_TYPE_POSTAUTHORIZE:
	  		extractAuthAnnotation(annoTag.getElems(), annotationInfo); 
	  		break;
	  		
	  	default: return false;
	  }
	  return true;
  }
  
  private void extractAuthAnnotation(Collection<AnnotationElem> elements,
		  AnnotationInfo annotationInfo) {
	  annotationInfo.setHasAuthAnnotation(true);	  
	  for (AnnotationElem annotationElement : elements) {
		  if (annotationElement instanceof AnnotationStringElem) {
			  switch (annotationElement.getName()) {
			  	case "value":
			  		annotationInfo
					 .setAuthPattern(((AnnotationStringElem) annotationElement)
							 .getValue());
			  		break;
	      }
	    }
	  }
  }
    
  private void extractMappingAnnotation(Collection<AnnotationElem> elements,
		 AnnotationInfo annotationInfo, String httpMethod) {	  
	  annotationInfo.setHasMappingAnnotation(true);
	  annotationInfo.setHttpMethod(httpMethod);
	  for (AnnotationElem annotationElement : elements) {
	    if (annotationElement instanceof AnnotationArrayElem) {
	      switch (annotationElement.getName()) {
	        case "value":
	        	annotationInfo
	        	 .setPattern(this.extractPattern(annotationElement));
	          break;
	        case "method":
	        	annotationInfo
	        	 .setHttpMethod(this.extractHttpMethod(annotationElement));
	          break;
	      }
	    }
	  }
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

  public List<AnnotationEntity> getAnnotations() {
    return annotations;
  }
}