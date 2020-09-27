package com.pearl.network.downloadmanager.utils;

// TODO: Auto-generated Javadoc
/**
 * The Class DownloadType.
 */
public class DownloadType {
    
    /** The Constant BOOK. */
    public static final String BOOK = "Book";
    
    /** The Constant BOOK_LIST. */
    public static final String BOOK_LIST = "BookList";
    
    /** The Constant NOTIFIER. */
    public static final String NOTIFIER = "Info";
    
    /** The Constant DEFAULT. */
    public static final String DEFAULT = "Default";

    /** The type. */
    private String type;

    /**
     * Instantiates a new download type.
     */
    public DownloadType() {
	type = DownloadType.DEFAULT;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
	return type;
    }

    /**
     * Sets the type.
     *
     * @param type the type to set
     */
    public void setType(String type) {
	this.type = type;
    }
}