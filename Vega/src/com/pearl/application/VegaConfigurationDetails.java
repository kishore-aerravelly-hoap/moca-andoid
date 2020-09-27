package com.pearl.application;

import android.net.Uri;

// TODO: Auto-generated Javadoc
/**
 * The Class VegaConfigurationDetails.
 */
public class VegaConfigurationDetails {

    /**
     * The Class ConfigDetails.
     */
    public static class ConfigDetails {
	// URI
	/** The Constant CONTENT_URI_notebook. */
	public static final Uri CONTENT_URI_notebook = Uri
		.parse("content://com.pearl.contentProvider.notebook");
	
	/** The Constant CONTENT_URI. */
	public static final Uri CONTENT_URI = Uri
		.parse("content://com.pearl.contentProvider");

	// Table columns
	/** The Constant ATTRIBUTE. */
	public static final String ATTRIBUTE = "ATTRIBUTE";
	
	/** The Constant VALUE. */
	public static final String VALUE = "VALUE";
	
	/** The Constant NOTES_ID. */
	public static final String NOTES_ID = "_id";
	
	/** The Constant SUBJECT. */
	public static final String SUBJECT = "Subject";
	
	/** The Constant DATE. */
	public static final String DATE = "Date";
	
	/** The Constant UserId. */
	public static final String UserId = "userId";
	
	/** The Constant NOTES_TEXT. */
	public static final String NOTES_TEXT = "Notes";
	
	/** The Constant Status. */
	public static final String Status = "Status";

    }
}
