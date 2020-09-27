package com.pearl.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class UserIDMismatchException.
 */
public class UserIDMismatchException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7303358722879815944L;

    /**
     * Instantiates a new user id mismatch exception.
     */
    public UserIDMismatchException() {
	super();
    }

    /**
     * Instantiates a new user id mismatch exception.
     *
     * @param detailMessage the detail message
     */
    public UserIDMismatchException(String detailMessage) {
	super(detailMessage);
    }

    /**
     * Instantiates a new user id mismatch exception.
     *
     * @param throwable the throwable
     */
    public UserIDMismatchException(Throwable throwable) {
	super(throwable);
    }

    /**
     * Instantiates a new user id mismatch exception.
     *
     * @param detailMessage the detail message
     * @param throwable the throwable
     */
    public UserIDMismatchException(String detailMessage, Throwable throwable) {
	super(detailMessage, throwable);
    }

}
