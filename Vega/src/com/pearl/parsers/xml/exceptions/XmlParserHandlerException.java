package com.pearl.parsers.xml.exceptions;

// TODO: Auto-generated Javadoc
/**
 * The Class XmlParserHandlerException.
 */
public class XmlParserHandlerException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5716983516648919211L;

    /**
     * Instantiates a new xml parser handler exception.
     */
    public XmlParserHandlerException() {
	super();
    }

    /**
     * Instantiates a new xml parser handler exception.
     *
     * @param detailMessage the detail message
     */
    public XmlParserHandlerException(String detailMessage) {
	super(detailMessage);
    }

    /**
     * Instantiates a new xml parser handler exception.
     *
     * @param throwable the throwable
     */
    public XmlParserHandlerException(Throwable throwable) {
	super(throwable);
    }

    /**
     * Instantiates a new xml parser handler exception.
     *
     * @param detailMessage the detail message
     * @param throwable the throwable
     */
    public XmlParserHandlerException(String detailMessage, Throwable throwable) {
	super(detailMessage, throwable);
    }
}