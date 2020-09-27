package com.pearl.logger;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class Logger.
 */
public class Logger {
    
    /**
     * Disable.
     *
     * @param tag the tag
     * @param msg the msg
     */
    public static void disable(String tag, String msg) {
	// TODO comment out below line before deploying
	Log.d(tag, msg);
    }

    /**
     * Warn.
     *
     * @param tag the tag
     * @param msg the msg
     */
    public static void warn(String tag, String msg) {
	Log.w(tag, msg);
    }

    /**
     * Error.
     *
     * @param tag the tag
     * @param msg the msg
     */
    public static void error(String tag, String msg) {
	Log.e(tag, msg);
    }

    /**
     * Error.
     *
     * @param tag the tag
     * @param e the e
     */
    public static void error(String tag, Exception e) {
	final StringWriter sw = new StringWriter();
	e.printStackTrace(new PrintWriter(sw));

	Log.e(tag, sw.toString());
    }

    /**
     * Info.
     *
     * @param tag the tag
     * @param msg the msg
     */
    public static void info(String tag, String msg) {
	Log.i(tag, msg);
    }

    /**
     * Verbose.
     *
     * @param tag the tag
     * @param msg the msg
     */
    public static void verbose(String tag, String msg) {
	Log.v(tag, msg);
    }

    /**
     * Debug.
     *
     * @param tag the tag
     * @param msg the msg
     */
    public static void debug(String tag, String msg) {
	Log.d(tag, msg);
    }

    /**
     * Impossible.
     *
     * @param tag the tag
     * @param msg the msg
     */
    public static void impossible(String tag, String msg) {
	Log.wtf(tag, msg);
    }
}