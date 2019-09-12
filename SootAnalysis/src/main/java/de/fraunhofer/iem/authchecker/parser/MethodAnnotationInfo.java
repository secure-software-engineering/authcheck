package de.fraunhofer.iem.authchecker.parser;

public class MethodAnnotationInfo extends AnnotationInfoBase {
	
	private ClassAnnotationInfo classAnnotInfo;
	
	@Override
	public boolean isMappingAnnotated() {
		return super.hasMappingAnnotation || classAnnotInfo.hasMappingAnnotation;
	}
	
	@Override
	public String getHttpMethod() {
		return super.getHttpMethod() != null ?
				super.getHttpMethod() : classAnnotInfo.getHttpMethod();
	}
	
	@Override
	public String getPattern() {
		return classAnnotInfo.getPattern() + super.getPattern();
	}
	
	public boolean isClassAuthAnnotated() {
		return classAnnotInfo.isAuthAnnotated();
	}
	
	public String getClassAuthPattern() {
		return classAnnotInfo.getAuthPattern();
	}
		
	public void setClassAnnotationInfo(ClassAnnotationInfo classAnnotInfo) {
		this.classAnnotInfo = classAnnotInfo;
	}
}
