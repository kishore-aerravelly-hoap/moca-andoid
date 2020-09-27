/**
 * 
 */
package com.pearl.books;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class BookFormat.
 */
public class BookFormat implements Serializable {

    /** The Constant PDF. */
    public static final String PDF = "application/pdf";
    
    /** The Constant EPUB. */
    public static final String EPUB = "application/epub+zip";
    
    /** The Constant XML. */
    public static final String XML = "text/xml";
    
    /** The Constant UNKNOWN. */
    public static final String UNKNOWN = "unknown";

    /** The format. */
    private String format;

    /*
     * public BookFormat(){ format = BookFormat.UNKNOWN; }
     */

    /*
     * public void setFormat(String format){ this.format = format; }
     * 
     * public String getFormat(){ return format; }
     */

    /**
     * Sets the format.
     *
     * @param s the s
     * @return the book format
     */
    public BookFormat setFormat(String s) {
	format = s;

	return this;
    }

    /**
     * Gets the format.
     *
     * @return the format
     */
    public String getFormat() {
	return format;
    }
}