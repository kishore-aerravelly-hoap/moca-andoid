package com.pearl.network.downloadmanager;

import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.downloadmanager.utils.DownloadStatus;

// TODO: Auto-generated Javadoc
/**
 * The Interface DownloadHandlerInterface.
 */
public interface DownloadHandlerInterface {
    
    /**
     * Start download.
     *
     * @param download the download
     * @param downloadCallback the download callback
     * @param temp_id the temp_id
     */
    void startDownload(Download download, DownloadCallback downloadCallback,
	    int temp_id);

    /**
     * Start download.
     *
     * @param download the download
     * @param downloadCallback the download callback
     */
    void startDownload(Download download, DownloadCallback downloadCallback);

    /**
     * Pause download.
     */
    void pauseDownload();

    /**
     * Resume download.
     */
    void resumeDownload();

    /**
     * Cancel download.
     */
    void cancelDownload();

    /**
     * Gets the status.
     *
     * @return the status
     */
    public DownloadStatus getStatus();

    /**
     * Gets the no of parts.
     *
     * @return the no of parts
     */
    public int getNoOfParts();

    /**
     * Gets the current downloading part.
     *
     * @return the current downloading part
     */
    public int getCurrentDownloadingPart();

    /**
     * Gets the current part progress.
     *
     * @return the current part progress
     */
    public int getCurrentPartProgress();

    /**
     * Gets the overall progress.
     *
     * @return the overall progress
     */
    public int getOverallProgress();

    /**
     * Gets the overall progress string.
     *
     * @return the overall progress string
     */
    public String getOverallProgressString();

    /**
     * Gets the estimated time in secs.
     *
     * @return the estimated time in secs
     */
    public int getEstimatedTimeInSecs();
}