package com.vw.compare.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;

class ModifiedHttpServletRequest extends HttpServletRequestWrapper {
	private static final Logger log=Logger.getLogger(ModifiedHttpServletRequest.class);
	private ServletRequest servletRequest;

	/**
	 * The accept headers.
	 */
	private Vector<String> acceptHeaders = new Vector<String>(1);
	
	/**
	 * The Content-Type headers.
	 */
	private Vector<String> contentTypeHeaders = new Vector<String>(1);
	
	/**
	 * Creates a new ModifiedHttpServletRequest.
	 * @param request the original request.
	 * @param acceptHeader the modified accept header.
	 */
	
	private String content;
	public ModifiedHttpServletRequest(HttpServletRequest request, String contentTypeHeader, String acceptHeader, String content) {
		super(request);
		servletRequest=request;

		log.debug("Content-Type: "+contentTypeHeader);
		log.debug("Accept: "+acceptHeader);
		
		if(contentTypeHeader!=null)
		{
			this.contentTypeHeaders.add(contentTypeHeader);
			this.content=content;
		}
		
		if(acceptHeader!=null)	
			this.acceptHeaders.add(acceptHeader);
	}
	
	/**
	 * {@inheritDoc}
	 * @see javax.servlet.http.HttpServletRequestWrapper#getHeader(java.lang.String)
	 */
	@Override
	public String getHeader(String name) {
		if (name.equalsIgnoreCase("Content-Type") && contentTypeHeaders.size()>0)
			return contentTypeHeaders.firstElement();
		else if (name.equalsIgnoreCase("Accept") && acceptHeaders.size()>0)
			return acceptHeaders.firstElement();
		else
			return super.getHeader(name);
	}
	
	/**
	 * {@inheritDoc}
	 * @see javax.servlet.http.HttpServletRequestWrapper#getHeaders(java.lang.String)
	 */
	@Override
	public Enumeration<?> getHeaders(String name) {
		if (name.equalsIgnoreCase("Content-Type") && contentTypeHeaders.size()>0)
			return contentTypeHeaders.elements();
		else if (name.equalsIgnoreCase("Accept") && acceptHeaders.size()>0)
			return acceptHeaders.elements();
		else
			return super.getHeaders(name);
	}
	
	/**
	 * {@inheritDoc}
	 * @see javax.servlet.http.HttpServletRequestWrapper#getHeaderNames()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Enumeration<?> getHeaderNames() {
		if(super.getHeaderNames() != null) {
	        // Get all header names
	        List<String> names = Collections.list(super.getHeaderNames());

	        // Search for "Content-Type" header
	        boolean contains = false;
	        for(String name: names)
	            if(name.equalsIgnoreCase("Content-Type"))
	                contains = true;
	        
	        // Add Content-Type header name if does not exist
	        if(!contains)
	            names.add("Content-Type");
	        
	        // Search for "Accept" header
	        contains = false;
	        for(String name: names)
	            if(name.equalsIgnoreCase("Accept"))
	                contains = true;
	        
	        // Add Accept header name if does not exist
	        if(!contains)
	            names.add("Accept");
	        
	        return Collections.enumeration(names);
	    }
		else {
			return super.getHeaderNames();
		}
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException
	{
		if(content!=null)
		{
			return new ServletInputStream(){
				private ByteArrayInputStream stream=new ByteArrayInputStream(servletRequest.getParameter(content).getBytes());
				@Override
				public int read() throws IOException {
					return stream.read();
				}};
		}
		else
			return super.getInputStream();
			
	}	
}
