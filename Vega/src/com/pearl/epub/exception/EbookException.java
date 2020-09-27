package com.pearl.epub.exception;

// TODO: Auto-generated Javadoc
/**
 * The Class EbookException.
 */
public class EbookException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6408954401840213609L;

    /** The message. */
    private String message;

    /**
     * Instantiates a new ebook exception.
     *
     * @param message the message
     */
    public EbookException(String message) {
	this.message = message;
    }

    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
	return message;
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
	this.message = message;
    }

}
