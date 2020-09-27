package com.pearl.network.downloadmanager.utils;

// TODO: Auto-generated Javadoc
/**
 * The Interface DownloadCallback.
 */
public interface DownloadCallback {
    
    /**
     * On success.
     *
     * @param downloadedString the downloaded string
     */
    void onSuccess(String downloadedString);

    /**
     * On failure.
     *
     * @param failureMessage the failure message
     */
    void onFailure(String failureMessage);

    /**
     * On progress update.
     *
     * @param progressPercentage the progress percentage
     */
    void onProgressUpdate(int progressPercentage);
}