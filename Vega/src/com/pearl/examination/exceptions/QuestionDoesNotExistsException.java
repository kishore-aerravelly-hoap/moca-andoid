package com.pearl.examination.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class QuestionDoesNotExistsException.
 */
public class QuestionDoesNotExistsException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7532235259819082029L;

    /**
     * Instantiates a new question does not exists exception.
     */
    public QuestionDoesNotExistsException() {
	super();
    }

    /**
     * Instantiates a new question does not exists exception.
     *
     * @param detailMessage the detail message
     */
    public QuestionDoesNotExistsException(String detailMessage) {
	super(detailMessage);
    }

    /**
     * Instantiates a new question does not exists exception.
     *
     * @param throwable the throwable
     */
    public QuestionDoesNotExistsException(Throwable throwable) {
	super(throwable);
	// TODO Auto-generated constructor stub
    }

    /**
     * Instantiates a new question does not exists exception.
     *
     * @param detailMessage the detail message
     * @param throwable the throwable
     */
    public QuestionDoesNotExistsException(String detailMessage,
	    Throwable throwable) {
	super(detailMessage, throwable);
	// TODO Auto-generated constructor stub
    }
}