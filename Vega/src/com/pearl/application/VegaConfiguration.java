package com.pearl.application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;

// TODO: Auto-generated Javadoc
/**
 * The Class VegaConfiguration.
 */
public class VegaConfiguration {
    
    /** The context. */
    private final Context context;
    
    /** The tag. */
    private final String tag = "VegaConfiguration";

    /** The Constant ALERT_DISPLAY_TIME. */
    public static final String ALERT_DISPLAY_TIME = "ALERT_DISPLAY_TIME";
    
    /** The Constant NOTES_AUTO_SAVE_TIME. */
    public static final String NOTES_AUTO_SAVE_TIME = "NOTES_AUTO_SAVE_TIME";
    
    /** The Constant DATE_FORMAT. */
    public static final String DATE_FORMAT = "DATE_FORMAT";
    
    /** The Constant HISTORY_ACTIVITY. */
    public static final String HISTORY_ACTIVITY = "HISTORY_ACTIVITY";
    
    /** The Constant HISTORY_LOGGED_IN_USER_ID. */
    public static final String HISTORY_LOGGED_IN_USER_ID = "HISTORY_LOGGED_IN_USER_ID";
    
    /** The Constant HISTORY_BOOK_ID. */
    public static final String HISTORY_BOOK_ID = "HISTORY_BOOK_ID";
    
    /** The Constant HISTORY_PAGE_NUMBER. */
    public static final String HISTORY_PAGE_NUMBER = "HISTORY_PAGE_NUMBER";
    
    /** The Constant HISTORY_NOTEBOOK_ID. */
    public static final String HISTORY_NOTEBOOK_ID = "HISTORY_NOTEBOOK_ID";
    
    /** The Constant SUBJECT. */
    public static final String SUBJECT = "SUBJECT";

    /**
     * Instantiates a new vega configuration.
     *
     * @param context the context
     */
    public VegaConfiguration(Context context) {
	// configHandler = new ConfigHandler(context);
	this.context = context;
    }

    /**
     * Gets the value.
     *
     * @param attribute the attribute
     * @return the value
     * @throws InvalidConfigAttributeException the invalid config attribute exception
     */
    public String getValue(String attribute)
	    throws InvalidConfigAttributeException {
	String value = "";
	final String columns[] = new String[] { VegaConfigurationDetails.ConfigDetails.VALUE };
	final Uri myUri = VegaConfigurationDetails.ConfigDetails.CONTENT_URI;
	final Cursor cur = context.getContentResolver().query(myUri, columns,
		"ATTRIBUTE=" + attribute, null, null);
	if (cur.moveToFirst()) {
	    do {
		value = cur
			.getString(cur
				.getColumnIndex(VegaConfigurationDetails.ConfigDetails.VALUE));
		// Toast.makeText(this, id + " " + userName, 10000).show();
	    } while (cur.moveToNext());
	}
	return value;
    }

    /**
     * Sets the value.
     *
     * @param attribute the attribute
     * @param value the value
     * @throws ColumnDoesNoteExistsException the column does note exists exception
     */
    public void setValue(String attribute, String value)
	    throws ColumnDoesNoteExistsException {
	final ContentValues values = new ContentValues();
	values.put(VegaConfigurationDetails.ConfigDetails.VALUE, value);
	context.getContentResolver().update(
		VegaConfigurationDetails.ConfigDetails.CONTENT_URI, values,
		attribute, null);
    }

    /**
     * Sets the value.
     *
     * @param attribute the attribute
     * @param value the value
     * @throws ColumnDoesNoteExistsException the column does note exists exception
     */
    public void setValue(String attribute, int value)
	    throws ColumnDoesNoteExistsException {
	final String strValue = value + "";
	setValue(attribute, strValue);
    }

    /*
     * public void close(){ configHandler.close(); if(configCursor != null)
     * configCursor.close(); }
     */

}
