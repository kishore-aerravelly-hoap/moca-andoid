package com.pearl.network.request.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class ImproperResponseHandlerException.
 */
public class ImproperResponseHandlerException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new improper response handler exception.
     *
     * @param e the e
     */
    public ImproperResponseHandlerException(Exception e) {
	super(e);
    }

    /**
     * Instantiates a new improper response handler exception.
     *
     * @param e the e
     */
    public ImproperResponseHandlerException(String e) {
	super(e);
    }
}
