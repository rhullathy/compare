package com.vw.compare.common;
/**
 * 
 * @author BCMAKG0
 *
 */
public class CompareServiceException extends Exception {

	/**
	 * default serial version id.
	 */
	private static final long serialVersionUID = 1L;
	
	private int errorCode = 0;

	 /**
     * A new exception with a custom message.
     * 
     * @param aMessage exception message.
     */
    public CompareServiceException(final int errorCode)
    {
        super();
        this.errorCode = errorCode;
    }
    
    /**
     * Constructor to take exception.
     * 
     * @param e generic exception.
     */
    public CompareServiceException(final Throwable e)
    {
        super(e);
    }
    
    /**
     * A new exception with underlying cause and custom message.
     * 
     * @param message exception message.
     * @param aReason root cause.
     */
    public CompareServiceException(final int errorCode, final Throwable aReason)
    {
        super(aReason.getMessage(), aReason);
        this.errorCode = errorCode;
    }

    public CompareServiceException() {}

	public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    

}
