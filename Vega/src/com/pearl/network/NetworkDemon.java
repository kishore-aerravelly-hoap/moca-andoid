package com.pearl.network;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class NetworkDemon.
 */
public class NetworkDemon {
    
    /** The connectivity manager. */
    private ConnectivityManager connectivityManager;
    
    /** The is connected to internet. */
    private boolean isConnectedToInternet = false;

    /** The demon. */
    private Thread demon;
    
    /** The is running. */
    private boolean isRunning = false;

    // TODO remove just for time being
    /**
     * Instantiates a new network demon.
     */
    public NetworkDemon() {
	isConnectedToInternet = true;
    }

    // FIXME getting nullpointer exception
    /**
     * Instantiates a new network demon.
     *
     * @param connectivity_manager the connectivity_manager
     */
    public NetworkDemon(ConnectivityManager connectivity_manager) {
	connectivityManager = connectivity_manager;

	startNetworkDemon();
    }

    /**
     * Checks if is connection availabe.
     *
     * @return true, if is connection availabe
     */
    public boolean isConnectionAvailabe() {
	return isConnectedToInternet;
    }

    /**
     * Checks if is connected to internet.
     *
     * @return true, if is connected to internet
     */
    private boolean isConnectedToInternet() {
	final NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

	if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	    return true;
	}

	return false;
    }

    /**
     * Stop network demon.
     */
    public void stopNetworkDemon() {
	isRunning = false;
    }

    // Need to update the status here
    /**
     * Start network demon.
     */
    public void startNetworkDemon() {
	if (!isRunning) {
	    isRunning = true;

	    demon = new Thread(new Runnable() {
		@Override
		public void run() {
		    while (isRunning) {
			if (isConnectedToInternet()) {
			    isConnectedToInternet = true;
			} else {
			    isConnectedToInternet = false;
			}

			try {
			    Thread.sleep(2000);
			} catch (final InterruptedException e) {
			    Logger.error("NetworkDemon - InterruptException", e);
			}

			// Logger.warn("NetworkDemon",
			// "Connection status"+isConnectedToInternet);
		    }
		}
	    });

	    demon.setDaemon(true);
	    demon.start();
	}
    }
}
