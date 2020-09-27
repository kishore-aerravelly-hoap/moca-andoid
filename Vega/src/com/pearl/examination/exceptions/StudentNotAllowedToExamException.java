package com.pearl.examination.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class StudentNotAllowedToExamException.
 */
public class StudentNotAllowedToExamException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5307688725885519286L;

    /**
     * Instantiates a new student not allowed to exam exception.
     */
    public StudentNotAllowedToExamException() {
	super();
    }

    /**
     * Instantiates a new student not allowed to exam exception.
     *
     * @param detailMessage the detail message
     */
    public StudentNotAllowedToExamException(String detailMessage) {
	super(detailMessage);
    }

    /**
     * Instantiates a new student not allowed to exam exception.
     *
     * @param throwable the throwable
     */
    public StudentNotAllowedToExamException(Throwable throwable) {
	super(throwable);
    }

    /**
     * Instantiates a new student not allowed to exam exception.
     *
     * @param detailMessage the detail message
     * @param throwable the throwable
     */
    public StudentNotAllowedToExamException(String detailMessage,
	    Throwable throwable) {
	super(detailMessage, throwable);
    }
}
