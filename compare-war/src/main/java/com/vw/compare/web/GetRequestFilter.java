package com.vw.compare.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class GetRequestFilter implements Filter {

	private static final Logger log = Logger.getLogger(GetRequestFilter.class);

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		ServletRequest modifiedRequest=request;
		String contentType=null;
		String content=null;
		if(((HttpServletRequest)request).getMethod().equalsIgnoreCase("GET"))
		{
			if(((HttpServletRequest)request).getParameter("json")!=null)
			{
				contentType="application/json;charset=UTF-8";
				content="json";
			}
			else if(((HttpServletRequest)request).getParameter("xml")!=null)
			{
				contentType="application/xml;charset=UTF-8";
				content="xml";
			}
		}
		else
		{
			contentType=request.getContentType();
		}
		
		String accept=null;
		String requestUri=((HttpServletRequest)request).getRequestURI();
		
		log.debug("Request Uri: "+requestUri);
		
		if(requestUri.lastIndexOf(".")>=0)
		{
			String mediaType=requestUri.substring(requestUri.lastIndexOf(".")+1);
			
			if(mediaType.equalsIgnoreCase("xml"))
				accept="application/xml;charset=UTF-8";
			else if(mediaType.equalsIgnoreCase("json"))
				accept="application/json;charset=UTF-8";
			
			modifiedRequest=new ModifiedHttpServletRequest(((HttpServletRequest)request),contentType,accept,content);
		}
		
		
		chain.doFilter(modifiedRequest, response);

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

}
