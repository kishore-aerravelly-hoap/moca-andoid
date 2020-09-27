package com.pearl.network.downloadmanager;

import android.os.AsyncTask;

import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.downloadmanager.utils.DownloadStatus;
import com.pearl.network.request.ResponseStatus;

// TODO: Auto-generated Javadoc
/**
 * The Class DownloadManager.
 */
public class DownloadManager extends AsyncTask<String, Integer, String> {
    
    /** The download. */
    private final Download download;
    
    /** The download callback. */
    private DownloadCallback downloadCallback;
    
    /** The download status. */
    private DownloadStatus downloadStatus;
    
    /** The manager download callback. */
    DownloadCallback managerDownloadCallback;

    /** The progress. */
    private int progress;
    
    /** The no of parts. */
    private int noOfParts;
    
    /** The helper_id. */
    private int helper_id;

    /** The application. */
    private final ApplicationData application;

    /** The is paused. */
    private boolean isPaused = false;

    /** The warnings log. */
    private String warningsLog;

    /**
     * Instantiates a new download manager.
     *
     * @param appData the app data
     * @param download the download
     */
    public DownloadManager(ApplicationData appData, Download download) {
	this.download = download;

	Logger.info("Download Manager",
		"Preparing Download process for downloading:" + download);

	downloadCallback = null;
	downloadStatus = new DownloadStatus();

	progress = 0;
	noOfParts = 1;

	application = appData;
    }

    /**
     * Start download.
     *
     * @param download_callback the download_callback
     */
    public void startDownload(DownloadCallback download_callback) {
	startDownload(download_callback, -1);
    }

    /**
     * Start download.
     *
     * @param download_callback the download_callback
     * @param helper_id the helper_id
     */
    public void startDownload(DownloadCallback download_callback, int helper_id) {
	this.helper_id = helper_id;
	downloadCallback = download_callback;

	this.execute(download.getUrl());
    }

    /**
     * Pause download.
     */
    public void pauseDownload() {
	// TODO pause download functionality here

	isPaused = true;
    }

    /**
     * Resume download.
     */
    public void resumeDownload() {
	// TODO resume download functionality here

	isPaused = false;
    }

    /**
     * Cancel download.
     */
    public void cancelDownload() {
	// TODO cancel download functionality here

	setDownloadStatus(ResponseStatus.ABORT);
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
     */
    @Override
    protected String doInBackground(String... arg0) {
	Logger.error("DM->", "downloading url:" + download.getUrl());

	managerDownloadCallback = new DownloadCallback() {
	    @Override
	    public void onSuccess(String downloadedString) {
		downloadStatus.setStaus(ResponseStatus.SUCCESS);

		// downloadCallback.onSuccess("success");
	    }

	    @Override
	    public void onProgressUpdate(int progressPercentage) {
		downloadStatus.setStaus(ResponseStatus.IN_PROGRESS);

		publishProgress(progressPercentage);
		// DownloadManager.this.onProgressUpdate(progressPercentage);
	    }

	    @Override
	    public void onFailure(String failureMessage) {
		if (downloadCallback != null) {
		    downloadStatus.setStaus(ResponseStatus.FAIL);

		    // downloadCallback.onFailure(failureMessage);
		}
	    }
	};

	final DownloadHandlerInterface downloadHandler = DownloadHandlerFactory
		.getInstance(download, application);
	downloadHandler.startDownload(download, managerDownloadCallback,
		helper_id);

	downloadStatus = downloadHandler.getStatus();
	Logger.warn("DownloadManager", downloadStatus.getStatus());

	if (ResponseStatus.SUCCESS.equals(downloadStatus.getStatus())) {
	    return "success";
	} else {
	    return "fail";
	}
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
     */
    @Override
    protected void onProgressUpdate(Integer... progress) {
	this.progress = progress[0];

	if (downloadCallback != null) {
	    downloadCallback.onProgressUpdate(progress[0]);
	}
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(String status) {
	if (downloadCallback != null) {
	    if ("success".equals(status)) {
		downloadCallback.onSuccess("success");
	    } else {
		downloadCallback.onFailure("fail");
	    }
	}
    }

    /**
     * Gets the warnings.
     *
     * @return the warnings
     */
    public String getWarnings() {
	return warningsLog;
    }

    /**
     * Gets the download status.
     *
     * @return the downloadStatus
     */
    public DownloadStatus getDownloadStatus() {
	return downloadStatus;
    }

    /**
     * Sets the download status.
     *
     * @param status the new download status
     */
    private void setDownloadStatus(String status) {
	// TODO better to validate
	downloadStatus.setStaus(status);
    }

    /**
     * Gets the progress.
     *
     * @return the progress
     */
    public int getProgress() {
	return progress;
    }

    /**
     * Sets the progress.
     *
     * @param progress the progress to set
     */
    private void setProgress(int progress) {
	this.progress = progress;
    }

    /**
     * Gets the no of parts.
     *
     * @return the noOfParts
     */
    public int getNoOfParts() {
	return noOfParts;
    }

    /**
     * Sets the no of parts.
     *
     * @param noOfParts the noOfParts to set
     */
    private void setNoOfParts(int noOfParts) {
	this.noOfParts = noOfParts;
    }
}