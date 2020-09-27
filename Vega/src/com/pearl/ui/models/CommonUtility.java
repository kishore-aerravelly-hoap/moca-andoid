/**
 * 
 */
package com.pearl.ui.models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class CommonUtility.
 *
 * @author kpamulapati
 */
public class CommonUtility {

    /** The Constant DATE_FORMAT. */
    public static final String DATE_FORMAT = "yyyy-MM-ddTHH:mm:ssZ";

    /**
     * Gets the date time.
     *
     * @return the date time
     */
    public static String getDateTime() {
	final DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
	final Calendar cal1 = Calendar.getInstance();
	final String currentDate = dateFormat1.format(cal1.getTime());
	// Logger.info("CurrentDate Time ===> " + currentDate);
	return currentDate;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public static String getDate() {
	final DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	final Calendar cal1 = Calendar.getInstance();
	final String currentDate = dateFormat1.format(cal1.getTime());
	// logger.info("CurrentDate Time ===> " + currentDate);
	return currentDate;
    }

    /**
     * Gets the time.
     *
     * @return the time
     */
    public static String getTime() {
	final DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	final Calendar cal1 = Calendar.getInstance();
	final String currentDate = dateFormat1.format(cal1.getTime());
	// logger.info("CurrentDate Time ===> " + currentDate);
	return currentDate;
    }

    /**
     * Gets the month.
     *
     * @return the month
     */
    public static String getMonth() {
	final Date date = new Date();
	final SimpleDateFormat sdf = new SimpleDateFormat("MMM");
	final String month = sdf.format(date);
	// logger.info("Month ===> " + month);
	return month;
    }

    /**
     * Convert date to date.
     *
     * @param date the date
     * @param format the format
     * @return the date
     */
    public static Date convertDateToDate(Date date, String format) {
	final SimpleDateFormat form = new SimpleDateFormat(format);
	return getDateTime(form.format(date));
    }

    /**
     * Convert date to string.
     *
     * @param date the date
     * @param format the format
     * @return the string
     */
    public static String convertDateToString(Date date, String format) {
	final SimpleDateFormat form = new SimpleDateFormat(format);
	return form.format(date);
    }

    /**
     * Format time.
     *
     * @param time the time
     * @param format the format
     * @return the string
     */
    public static String formatTime(Date time, String format) {
	final SimpleDateFormat form = new SimpleDateFormat(format);
	return form.format(time);
    }

    /**
     * Gets the date time.
     *
     * @param date the date
     * @return the date time
     */
    public static Date getDateTime(String date) {
	DateFormat dateFormat = null;
	Date modifiedDate = null;
	try {
	    dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	    modifiedDate = dateFormat.parse(date);
	} catch (final ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return modifiedDate;
    }

    /**
     * Convert string to date.
     *
     * @param date the date
     * @param format the format
     * @return the date
     */
    public static Date convertStringToDate(String date, String format) {
	DateFormat dateFormat = null;
	Date modifiedDate = null;
	try {
	    dateFormat = new SimpleDateFormat(format);
	    modifiedDate = dateFormat.parse(date);
	} catch (final ParseException e) {
	    // TODO Auto-generated catch block

	    e.printStackTrace();
	}
	return modifiedDate;
    }

    /**
     * Gets the date format with time.
     *
     * @return the date format with time
     */
    public static Date getDateFormatWithTime() {
	Date dateObject = null;
	try {
	    final String currentDate = getDateTime();
	    final SimpleDateFormat dateFormat1 = new SimpleDateFormat(
		    "yyyy-MM-dd  HH:mm:ss");
	    dateObject = dateFormat1.parse(currentDate);
	    dateFormat1.applyPattern("yyyy-MM-dd  HH:mm:ss");
	} catch (final ParseException e) {
	    e.printStackTrace();
	}
	// logger.info("CurrentDate Time ===> " + dateObject);
	return dateObject;
    }

    /**
     * Gets the total minutes for current time.
     *
     * @param date the date
     * @return the total minutes for current time
     */
    public static int getTotalMinutesForCurrentTime(Date date) {
	// Date date = CommonUtility.getDateFormatWithTime();
	final long mins = date.getMinutes();
	final long hrsMins = date.getHours() * 60;
	final long totalMinutes = mins + hrsMins;
	final int total = (int) totalMinutes;
	return total;
    }
}
