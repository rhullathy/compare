package com.vw.compare.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.vw.compare.common.CompareServiceException;
import com.vw.compare.domain.Error;
import com.vw.compare.domain.Errors;

public class ExceptionHandlerResolver extends DefaultHandlerExceptionResolver {
	
	private static final Logger log=Logger.getLogger(ExceptionHandlerResolver.class);

    private static final String ERROR = "error";

    private static final String ERRORS = "errors";
	
	private MessageSource messageSource;
	
	public ExceptionHandlerResolver()
	{
		super();
		this.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) {

		if(ex instanceof CompareServiceException)
		{
			return handleInvalidRequestException((CompareServiceException) ex, response);
		}
		if (ex instanceof NoSuchRequestHandlingMethodException) {
			return handleInvalidRequestException((NoSuchRequestHandlingMethodException) ex, response);
		}
		else if (ex instanceof HttpRequestMethodNotSupportedException) {
			return handleInvalidRequestException(ex, response);
		}
		else if (ex instanceof HttpMediaTypeNotSupportedException) {
			return handleInvalidRequestException(ex, response);
		}
		else if (ex instanceof HttpMediaTypeNotAcceptableException) {
			return handleInvalidRequestException(ex, response);
		}
		else if (ex instanceof MissingServletRequestParameterException) {
			return handleInvalidRequestException(ex, response);
		}
		else if (ex instanceof ConversionNotSupportedException) {
			return handleInvalidRequestException(ex, response);
		}
		else if (ex instanceof TypeMismatchException) {
			return handleInvalidRequestException((TypeMismatchException) ex, response);
		}
		else if (ex instanceof HttpMessageNotReadableException) {
			return handleInvalidRequestException(ex, response);
		}
		else if (ex instanceof org.codehaus.jackson.map.JsonMappingException)
		{
			return handleInvalidRequestException(ex, response);
		}
		else if (ex instanceof JAXBException)
		{
			return handleInvalidRequestException(ex, response);
		}
		else
		{
			return handleException(ex, response);
		}
	}

	private ModelAndView handleInvalidRequestException(
			Exception ex, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		log.error(ex, ex);
		Errors errors=new Errors();
                Error error=new Error();
                error.setMessage(ex.getMessage());
                error.setCode(500);      
                errors.getError().add(error);
		return new ModelAndView(ERROR,ERRORS,errors);
	}
	
	private ModelAndView handleInvalidRequestException(CompareServiceException ex, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		Errors errors=new Errors();
		Error error=new Error();
                error.setMessage(messageSource.getMessage(ERROR+"."+ex.getErrorCode(), null, null) == null ? ex.getMessage():
                    messageSource.getMessage(ERROR+"."+ex.getErrorCode(), null, null));
                error.setCode(ex.getErrorCode());      
                errors.getError().add(error);
		return new ModelAndView(ERROR,ERRORS,errors);
	}
	
	private ModelAndView handleException(Exception ex, HttpServletResponse response) {
		log.error(ex, ex);
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		Errors errors=new Errors();
                Error error=new Error();
                error.setMessage(ex.getClass().getName()+"\t"+ex.getMessage());
                error.setCode(501);      
                errors.getError().add(error);
		return new ModelAndView(ERROR,ERRORS,errors);
	}
	public MessageSource getMessageSource() {
		return messageSource;
	}
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
