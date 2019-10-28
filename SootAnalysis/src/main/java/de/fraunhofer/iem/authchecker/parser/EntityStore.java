package de.fraunhofer.iem.authchecker.parser;

import java.util.List;

import de.fraunhofer.iem.authchecker.entity.AnnotationEntity;
import de.fraunhofer.iem.authchecker.entity.ConfigurationEntity;

public class EntityStore {
	
	private List<AnnotationEntity> annotations;
	private List<ConfigurationEntity> patterns;
	
	public EntityStore(List<AnnotationEntity> annotations,
			List<ConfigurationEntity> patterns){
		this.annotations = annotations;
		this.patterns = patterns;
	}
	
	public List<AnnotationEntity> getAnnotations() {
		return annotations;
	}
	
	public List<ConfigurationEntity> getPatterns() {
		return patterns;
	}
	
	public void setAnnotations(List<AnnotationEntity> annotations) {
		this.annotations = annotations;
	}
	
	public void setPatterns(List<ConfigurationEntity> patterns) {
		this.patterns = patterns;
	}

	public void addAnnotation(AnnotationEntity annotationEntity) {
		this.annotations.add(annotationEntity);
	}
	
	public void addPattern(ConfigurationEntity configurationEntity) {
		this.patterns.add(configurationEntity);
	}
}