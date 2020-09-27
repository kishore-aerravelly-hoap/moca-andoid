package com.pearl.AppStatus;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class AppStatus.
 */
public class AppStatus {
    
    /** The instance. */
    private static AppStatus instance = new AppStatus();
    
    /** The connectivity manager. */
    ConnectivityManager connectivityManager;
    
    /** The mobile info. */
    NetworkInfo wifiInfo, mobileInfo;
    
    /** The context. */
    static Context context;
    
    /** The connected. */
    boolean connected = false;

    /**
     * Gets the single instance of AppStatus.
     *
     * @param ctx the ctx
     * @return single instance of AppStatus
     */
    public static AppStatus getInstance(Context ctx) {

	context = ctx;
	return instance;
    }

    /**
     * Checks if is online.
     *
     * @param con the con
     * @return the boolean
     */
    public Boolean isOnline(Context con) {

	try {
	    connectivityManager = (ConnectivityManager) con
		    .getSystemService(Context.CONNECTIVITY_SERVICE);

	    final NetworkInfo networkInfo = connectivityManager
		    .getActiveNetworkInfo();
	    connected = networkInfo != null && networkInfo.isAvailable()
		    && networkInfo.isConnected();
	    return connected;

	} catch (final Exception e) {
	    System.out
	    .println("CheckConnectivity Exception: " + e.getMessage());
	    Log.v("connectivity", e.toString());
	}

	return connected;
    }
}
