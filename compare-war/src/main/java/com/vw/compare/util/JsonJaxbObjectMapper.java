package com.vw.compare.util;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

public class JsonJaxbObjectMapper extends ObjectMapper{
	public JsonJaxbObjectMapper()
	{
		super();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
	    // make deserializer use JAXB annotations (only)
	    this.getDeserializationConfig().setAnnotationIntrospector(introspector);
	    // make serializer use JAXB annotations (only)
	    this.getSerializationConfig().setAnnotationIntrospector(introspector);
	    this.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
	}

}