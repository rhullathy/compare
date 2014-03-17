package com.vw.compare.dao;

import com.vw.compare.common.CompareServiceException;


public class DaoException extends CompareServiceException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Method 'DaoException'
	 * 
	 * @param message
	 */
	public DaoException(final int errorCode)
	{
		super(errorCode);
	}

	/**
	 * Method 'DaoException'
	 * 
	 * @param message
	 * @param throwable
	 */
	public DaoException(final int errorCode, Throwable t)
	{
		super(errorCode, t);
		
	}

	
}
