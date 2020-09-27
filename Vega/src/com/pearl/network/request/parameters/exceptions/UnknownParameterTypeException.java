package com.pearl.network.request.parameters.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class UnknownParameterTypeException.
 */
public class UnknownParameterTypeException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new unknown parameter type exception.
     */
    public UnknownParameterTypeException() {
	super();
    }

    /**
     * Instantiates a new unknown parameter type exception.
     *
     * @param detailMessage the detail message
     */
    public UnknownParameterTypeException(String detailMessage) {
	super(detailMessage);
    }

    /**
     * Instantiates a new unknown parameter type exception.
     *
     * @param throwable the throwable
     */
    public UnknownParameterTypeException(Throwable throwable) {
	super(throwable);
    }

    /**
     * Instantiates a new unknown parameter type exception.
     *
     * @param detailMessage the detail message
     * @param throwable the throwable
     */
    public UnknownParameterTypeException(String detailMessage,
	    Throwable throwable) {
	super(detailMessage, throwable);
    }
}
