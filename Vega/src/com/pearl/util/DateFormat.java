package com.pearl.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class DateFormat.
 */
public class DateFormat {

    /** The Constant SEC. */
    private static final long SEC = 1 * 1000;
    
    /** The Constant MIN. */
    private static final long MIN = 60 * SEC;
    
    /** The Constant HOUR. */
    private static final long HOUR = 60 * MIN;
    
    /** The Constant DAY. */
    private static final long DAY = 24 * HOUR;
    
    /** The Constant WEEK. */
    private static final long WEEK = 7 * DAY;
    
    /** The Constant YEAR. */
    private static final long YEAR = 365 * DAY;
    
    /** The diff time. */
    private static long diffTime;
    
    /** The tag. */
    private static String tag = "DateFormat";
    
    /** The current time. */
    private static Calendar currentTime;
    
    /** The database time. */
    private static Calendar databaseTime;
    
    /** The d1. */
    private static Date d1 = null;
    
    /** The Constant NOTEBOOK. */
    private static final String NOTEBOOK = "NOTEBOOK";
    
    /** The Constant CHAT. */
    private static final String CHAT = "CHAT";
    
    /** The Constant BOOKMARKSlIST. */
    private static final String BOOKMARKSlIST = "BOOKMARKS_NOTES_lIST";
    
    /** The Constant NOTICEBOARD_DATE. */
    private static final String NOTICEBOARD_DATE = "NOTICE_BOARD_DATE";

    /**
     * Gets the formatted time.
     *
     * @param date the date
     * @param userTimeFormat the user time format
     * @param type the type
     * @return the formatted time
     */
    public static String getFormattedTime(String date, String userTimeFormat,
	    String type) {
	String result = null;
	intialize(date);
	// System.out.println("current time is:" + currentTime.getTime());
	final long milliseconds1 = currentTime.getTimeInMillis();
	final long milliseconds2 = databaseTime.getTimeInMillis();
	diffTime = milliseconds1 - milliseconds2;
	// Fri Dec 16 11:36:56 GMT+05:30 2011
	result = calculateDiff(d1, userTimeFormat, type);
	return result;
    }

    /**
     * Gets the formatted time.
     *
     * @param d1 the d1
     * @param type the type
     * @return the formatted time
     */
    public static String getFormattedTime(Date d1, String type) {
	// Logger.error(tag, "date in getformatted time is:"+d1.toString());
	String result = null;
	final SimpleDateFormat sdf = new SimpleDateFormat(
		"EEEEEEE MMMMMMMMMM dd HH:mm:ss zz yyyy");
	final String date = sdf.format(d1);
	intialize(date);
	diffTime = currentTime.getTimeInMillis()
		- databaseTime.getTimeInMillis();
	// Logger.warn(tag,
	// "current time is:"+currentTime.getTime()+" and database time is:"+databaseTime.getTime());
	// Logger.warn(tag, "diff time is:"+diffTime);
	result = calculateDiff(d1, "", type);
	System.out.println("result is:" + result);
	return result;
    }

    /**
     * Convert.
     *
     * @param diffTime the diff time
     * @return the long
     */
    private static long convert(long diffTime) {
	long time = 0;
	if (diffTime <= MIN) {
	    time = diffTime / 1000;
	} else if (diffTime <= HOUR) {
	    time = diffTime / (60 * 1000);
	} else if (diffTime <= DAY) {
	    time = diffTime / (60 * 60 * 1000);
	}
	return time;
    }

    /**
     * Intialize.
     *
     * @param date the date
     */
    public static void intialize(String date) {
	currentTime = Calendar.getInstance();
	databaseTime = Calendar.getInstance();
	final SimpleDateFormat sdf = new SimpleDateFormat(
		"EEE MMM dd HH:mm:ss zz yyyy");
	try {
	    // Logger.warn(tag, "date in date format is:" + date);
	    d1 = sdf.parse(date);
	    databaseTime.setTime(d1);
	    // System.out.println("database time is:" + databaseTime.getTime());
	} catch (final ParseException e) {
	    Logger.info(tag, e.toString());
	}
    }

    /**
     * Calculate diff.
     *
     * @param date the date
     * @param userTimeFormat the user time format
     * @param type the type
     * @return the string
     */
    public static String calculateDiff(Date date, String userTimeFormat,
	    String type) {
	String result = null;
	// System.out.println("date in calculate diff is:"+date);
	if (diffTime <= MIN) {
	    if (type.equals(CHAT))
		return diffTime / 1000 + " seconds ago";
	    else if (type.equals(NOTEBOOK) || type.equals(BOOKMARKSlIST))
		return "Few moments ago";
	    else if (type.equals(NOTICEBOARD_DATE))
		noticeDisplayTime(userTimeFormat);

	} else if (diffTime <= HOUR) {
	    final long minutes = convert(diffTime);
	    if (type.equals(CHAT))
		return minutes + " minutes ago";
	    else if (type.equals(NOTEBOOK) || type.equals(BOOKMARKSlIST)) {
		if (minutes <= 10)
		    result = "Few minutes back";
		else
		    result = minutes + " Minutes back";
	    } else if (type.equals(NOTICEBOARD_DATE))
		noticeDisplayTime(userTimeFormat);

	} else if (diffTime <= DAY) {
	    if (!userTimeFormat.equals("")) {
		if (userTimeFormat.equals("12")) {
		    final SimpleDateFormat dateFormat = new SimpleDateFormat(
			    "hh:mm a");
		    result = "at " + dateFormat.format(d1);
		} else {
		    final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		    result = "at " + dateFormat.format(d1);
		}
	    } else {
		result = "at " + new SimpleDateFormat("HH:mm").format(date);
	    }
	} else if (diffTime <= 2 * DAY) {
	    if (type.equals(CHAT) || type.equals(NOTICEBOARD_DATE))
		result = "Yesterday at "
			+ new SimpleDateFormat("HH:mm").format(date);
	    else if (type.equals(NOTEBOOK) || type.equals(BOOKMARKSlIST)
		    || type.equals(NOTICEBOARD_DATE))
		result = "Yesterday";
	} else if (diffTime <= WEEK) {
	    if (type.equals(CHAT) || type.equals(NOTICEBOARD_DATE))
		return result = new SimpleDateFormat("EEEEEEE").format(date)
		+ " at " + new SimpleDateFormat("HH:mm").format(date);
	} else if (diffTime <= YEAR) {
	    System.out.println("less than year");
	    if (type.equals(CHAT) || type.equals(NOTICEBOARD_DATE))
		result = new SimpleDateFormat("MMMMMMMMMM dd").format(date)
		+ " at " + new SimpleDateFormat("HH:mm").format(date);
	    else if (type.equals(NOTEBOOK) || type.equals(BOOKMARKSlIST)
			 || type.equals(NOTICEBOARD_DATE))
		
		result = "oldnotes";

	    else if (type.equals(NOTEBOOK) || type.equals(BOOKMARKSlIST)
		    || type.endsWith(NOTICEBOARD_DATE))
		result = "on "
			+ new SimpleDateFormat("MMMMMMMMMM dd").format(date);
	} else {
	    int cYear = 0;
	    int dbYear = 0;
	    final String cTime = currentTime.getTime().toString();
	    final String dbTime = databaseTime.getTime().toString();
	    final StringTokenizer st = new StringTokenizer(cTime, " ");
	    while (st.hasMoreTokens()) {
		cYear = Integer.parseInt(st.nextToken());
	    }
	    final StringTokenizer st2 = new StringTokenizer(dbTime, " ");
	    while (st2.hasMoreTokens()) {
		dbYear = Integer.parseInt(st2.nextToken());
	    }
	    final int x = cYear - dbYear;
	    if (x == 1)
		result = x + " Year ago";
	    else
		result = x + " Years ago";
	}
	return result;
    }

    /**
     * Notice display time.
     *
     * @param userTimeFormat the user time format
     * @return the string
     */
    public static String noticeDisplayTime(String userTimeFormat) {
	String posted_time = null;
	if (userTimeFormat.equals("12")) {
	    final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
	    posted_time = "Posted at " + dateFormat.format(d1);
	} else {
	    final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
	    posted_time = "Posted at " + dateFormat.format(d1);
	}
	return posted_time;
    }
}
