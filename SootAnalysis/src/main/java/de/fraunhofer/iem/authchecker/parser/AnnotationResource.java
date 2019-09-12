package de.fraunhofer.iem.authchecker.parser;

public class AnnotationResource {
	public static final String HTTP_METHOD_GET = "GET"; 
	public static final String HTTP_METHOD_PUT = "PUT"; 
	public static final String HTTP_METHOD_POST = "POST"; 
	public static final String HTTP_METHOD_DELETE = "DELETE";
	public static final String HTTP_METHOD_PATCH = "PATCH"; 
	    
	public static final String ANNOTATION_TYPE_REQUESTMAPPING = "Lorg/springframework/web/bind/annotation/RequestMapping;";
	public static final String ANNOTATION_TYPE_GETMAPPING  = "Lorg/springframework/web/bind/annotation/GetMapping;";
	public static final String ANNOTATION_TYPE_POSTMAPPING  = "Lorg/springframework/web/bind/annotation/PostMapping;";
	public static final String ANNOTATION_TYPE_PUTMAPPING  = "Lorg/springframework/web/bind/annotation/PutMapping;";
	public static final String ANNOTATION_TYPE_DELETEMAPPING  = "Lorg/springframework/web/bind/annotation/DeleteMapping;";
	public static final String ANNOTATION_TYPE_PATCHMAPPING  = "Lorg/springframework/web/bind/annotation/PatchMapping;";
	  
	public static final String ANNOTATION_TYPE_SECURED  = "Lorg/springframework/security/access/prepost/Secured;";
	public static final String ANNOTATION_TYPE_PREAUTHORIZE  = "Lorg/springframework/security/access/prepost/PreAuthorize;";
	public static final String ANNOTATION_TYPE_POSTAUTHORIZE  = "Lorg/springframework/security/access/prepost/PostAuthorize;";
}
