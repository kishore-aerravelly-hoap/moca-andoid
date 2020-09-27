package com.pearl.network.downloadmanager.downloadhandlers;

import android.util.Log;

import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ResponseStatus;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.BookdownloadStatusRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class BookDownloadHandler.
 */
public class BookDownloadHandler extends DefaultDownloadHandler {
    
    /** The server requests. */
    private ServerRequests serverRequests;
    
    /** The available books download complete. */
    private boolean availableBooksDownloadComplete = false;
    
    /** The downloaded books download complete. */
    private boolean downloadedBooksDownloadComplete = false;

    /**
     * Instantiates a new book download handler.
     *
     * @param appData the app data
     */
    public BookDownloadHandler(ApplicationData appData) {
	super(appData);
	Logger.warn("BookDownloadHandler", "in BookDownloadHandler constructor");
    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.downloadhandlers.DefaultDownloadHandler#download(com.pearl.network.downloadmanager.utils.Download)
     */
    @Override
    public boolean download(Download download) {
	totalParts = 2;
	currentPart = 1;
	updateCurrentPartProgress(0);
	updateOverallProgress();
	updateStatus(ResponseStatus.IN_PROGRESS);
	serverRequests = new ServerRequests(appData);
	final DefaultDownloadHandler downloader = new DefaultDownloadHandler(appData);
	// downloading the book
	downloader.startDownload(download, new DownloadCallback() {
	    @Override
	    public void onSuccess(String downloadedString) {
		Log.w("tag", "BOOK: Download of book completed");
		downloadUpdatedDownloadList();
	    }

	    @Override
	    public void onProgressUpdate(int progressPercentage) {
		updateCurrentPartProgress(progressPercentage);
		updateOverallProgress();
	    }

	    @Override
	    public void onFailure(String failureMessage) {
		Log.w("tag", "BOOK: Download of book FAILED");
		updateStatus(ResponseStatus.FAIL);
		// We shall not be downloading remaining files, moveing to
		// completed state
		availableBooksDownloadComplete = true;
		downloadedBooksDownloadComplete = true;
	    }
	});

	// Wait till both the downloads are completed
	while (!(availableBooksDownloadComplete && downloadedBooksDownloadComplete)) {
	    Log.w("tag", "BOOK: download in progress"
		    + getOverallProgressString());
	}

	if (ResponseStatus.SUCCESS.equals(downloadStatus.getStatus())) {
	    Log.w("tag", "BOOK: RETURNING TRUE");
	    return true;
	} else {
	    Log.w("tag", "BOOK: RETURNING FALSE");
	    return false;
	}

    }

    /**
     * Download updated download list.
     */
    private void downloadUpdatedDownloadList() {
	Logger.info("BookDownloadHandler", "Download Status is "
		+ downloadStatus.getStatus());
	if (!ResponseStatus.FAIL.equals(downloadStatus.getStatus())) {
	    Logger.info("BookDownloadHandler", "download status is not fail");

	    // requesting to update the download status
	    final BookdownloadStatusRequest bsr = new BookdownloadStatusRequest(
		    serverRequests.getRequestURL(
			    ServerRequests.DOWNLOAD_SUCCESS,
			    appData.getUserId() + "", helper_id + ""));
	    bsr.request();

	    while (bsr.getResponseStatus().equals(ResponseStatus.IN_PROGRESS)) {
	    } // Continue till the response is received

	    Logger.info("BookDownloadHandler",
		    "response status is:" + bsr.getResponseStatus());

	    if (ResponseStatus.SUCCESS.equals(bsr.getResponseStatus())) {
		Logger.warn("-------",
			"path file to down:" + appData.getBooksListsPath());
		// downloading the udpated books list
		final Download bookslistDownload = new Download(
			serverRequests.getRequestURL(
				ServerRequests.DOWNLOADED_BOOKSLIST, ""
					+ appData.getUserId()),
					appData.getBooksListsPath(),
					ApplicationData.DOWNLOADEDBOOKSLIST_FILENAME);

		currentPart = 2;
		updateCurrentPartProgress(0);
		updateOverallProgress();
		final DownloadManager booklistDownloader = new DownloadManager(
			appData, bookslistDownload);

		booklistDownloader.startDownload(new DownloadCallback() {
		    @Override
		    public void onSuccess(String downloadedString) {
			updateStatus(ResponseStatus.SUCCESS);
			downloadedBooksDownloadComplete = true;
		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
			updateCurrentPartProgress(progressPercentage);
			updateOverallProgress();
			downloadedBooksDownloadComplete = false;
		    }

		    @Override
		    public void onFailure(String failureMessage) {
			updateStatus(ResponseStatus.FAIL);
			downloadedBooksDownloadComplete = true;
		    }
		});
	    } else {
		Logger.error(
			"BookDownloadHandler",
			"Book List downlaod Aborted.. because of 'download status update' request failure");

		updateStatus(ResponseStatus.FAIL);
	    }
	    if (ResponseStatus.SUCCESS.equals(bsr.getResponseStatus())) {
		// downloading the udpated books list
		final Download bookslistDownload = new Download(
			serverRequests.getRequestURL(
				ServerRequests.AVAILABLEBOOKSLIST,
				"" + appData.getUserId()),
				appData.getBooksListsPath(),
				ApplicationData.AVAILABLEBOOKSLIST_FILENAME);

		currentPart = 2;
		updateCurrentPartProgress(0);
		updateOverallProgress();
		final DownloadManager booklistDownloader = new DownloadManager(
			appData, bookslistDownload);

		booklistDownloader.startDownload(new DownloadCallback() {
		    @Override
		    public void onSuccess(String downloadedString) {
			updateStatus(ResponseStatus.SUCCESS);
			availableBooksDownloadComplete = true;
		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
			updateCurrentPartProgress(progressPercentage);
			updateOverallProgress();
			availableBooksDownloadComplete = false;
		    }

		    @Override
		    public void onFailure(String failureMessage) {
			updateStatus(ResponseStatus.FAIL);
			availableBooksDownloadComplete = true;
		    }
		});
	    } else {
		Logger.error(
			"BookDownloadHandler",
			"Book List downlaod Aborted.. because of 'download status update' request failure");

		updateStatus(ResponseStatus.FAIL);
	    }
	} else {
	    Logger.error("BookDownloadHandler", "Download Failed");
	}

	// finally checking for overall download status

    }
}