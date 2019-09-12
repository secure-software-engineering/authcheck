package de.fraunhofer.iem.authchecker.parser;

public abstract class AnnotationInfoBase implements AnnotationInfo {
	
	protected boolean hasAuthAnnotation;
	protected boolean hasMappingAnnotation;
	
	private String pattern = "";
	private String authPattern = "";
	
	private String httpMethod;
	
	public boolean isAuthAnnotated() {
		return hasAuthAnnotation;
	}
	
	public boolean isMappingAnnotated() {
		return hasMappingAnnotation;
	}
	
	public String getHttpMethod() {
		return httpMethod;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public String getAuthPattern() {
		return authPattern;
	}
	
	public void setHasAuthAnnotation(boolean hasAuthAnnotation) {
		this.hasAuthAnnotation = hasAuthAnnotation;
	}
	
	public void setHasMappingAnnotation(boolean hasMappingAnnotation) {
		this.hasMappingAnnotation = hasMappingAnnotation;
	}
	
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public void setAuthPattern(String authPattern) {
		this.authPattern = authPattern;
	}
}
