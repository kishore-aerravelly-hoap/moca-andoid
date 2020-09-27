package com.pearl.android.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

// TODO: Auto-generated Javadoc
/**
 * The Class ShowProgressBar.
 */
public class ShowProgressBar {
    
    /** The progress bar. */
    public static ProgressDialog progressBar;
    
    /** The progress bar status. */
    private static int progressBarStatus = 0;
    
    /** The proggress thread. */
    private static Thread proggressThread;
    
    /** The Constant progressBarHandler. */
    private final static Handler progressBarHandler = new Handler();
    
    /**
     * Show progress bar.
     *
     * @param message the message
     * @param percent the percent
     * @param ctx the ctx
     */
    public  static void  showProgressBar(String message,final int percent,Context ctx ) {
	progressBar=new ProgressDialog(ctx); 
	progressBar.setCancelable(true);
	progressBar.setCanceledOnTouchOutside(true);
	progressBar.setMessage(message);
	progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	progressBar.setProgress(0);
	progressBar.setMax(100);
	progressBar.setCancelable(true);
	progressBar.show();
	proggressThread = new Thread(new Runnable() {

	    @Override
	    public void run() {
		while (progressBarStatus < 100) { 

		    progressBarStatus = percent;
		    try {
			Thread.sleep(1000);
		    } catch (final InterruptedException e) {
			e.printStackTrace();
		    }

		    progressBarHandler.post(new Runnable() {

			@Override
			public void run() {
			    progressBar.setProgress(progressBarStatus);
			
			}
		    });
		    if (progressBarStatus >= 100) {

			try {
			    Thread.sleep(500);
			} catch (final InterruptedException e) {
			    e.printStackTrace();
			}
			progressBarHandler.removeCallbacks(proggressThread);
			progressBar.dismiss();
		    }

		}
		/* if (progressBarStatus >= 100) {

			try {
			    Thread.sleep(500);
			} catch (final InterruptedException e) {
			    e.printStackTrace();
			}
			progressBarHandler.removeCallbacks(proggressThread);
			progressBar.dismiss();
		    }
*/
	    }
	});
	proggressThread.start();
    }
}
