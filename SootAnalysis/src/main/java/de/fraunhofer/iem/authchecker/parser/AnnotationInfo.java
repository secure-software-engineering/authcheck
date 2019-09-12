package de.fraunhofer.iem.authchecker.parser;

public interface AnnotationInfo {
	public boolean isMappingAnnotated();
	public boolean isAuthAnnotated();
	
	public String getHttpMethod();
	public String getPattern();
	public String getAuthPattern();
	
	public void setHasMappingAnnotation(boolean value);
	public void setHasAuthAnnotation(boolean value);
	
	public void setHttpMethod(String value);
	public void setPattern(String value);
	public void setAuthPattern(String vlaue);
}
