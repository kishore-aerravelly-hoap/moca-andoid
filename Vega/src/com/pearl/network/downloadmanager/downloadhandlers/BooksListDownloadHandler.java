package com.pearl.network.downloadmanager.downloadhandlers;

import java.util.ArrayList;

import com.pearl.application.ApplicationData;
import com.pearl.books.Book;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ResponseStatus;
import com.pearl.parsers.XMLParser;

// TODO: Auto-generated Javadoc
/**
 * The Class BooksListDownloadHandler.
 */
public class BooksListDownloadHandler extends DefaultDownloadHandler {
    
    /**
     * Instantiates a new books list download handler.
     *
     * @param appData the app data
     */
    public BooksListDownloadHandler(ApplicationData appData) {
	super(appData);
	Logger.warn("BooksListDownloadHandler",
		" In BooksListDownloadHandler constructor");
    }

    // override parent - book downloading specific download methods
    /**
     * Download.
     *
     * @param download the download
     * @param downloadCallback the download callback
     * @return true, if successful
     */
    public boolean download(Download download, DownloadCallback downloadCallback) {
	totalParts = 1;
	currentPart = 1;
	updateCurrentPartProgress(0);
	updateOverallProgress();
	updateStatus(ResponseStatus.IN_PROGRESS);

	final DefaultDownloadHandler downloader = new DefaultDownloadHandler(appData);

	// downloading the book
	downloader.startDownload(download, new DownloadCallback() {
	    @Override
	    public void onSuccess(String downloadedString) {
	    }

	    @Override
	    public void onProgressUpdate(int progressPercentage) {
		updateCurrentPartProgress(progressPercentage);
		updateOverallProgress();
	    }

	    @Override
	    public void onFailure(String failureMessage) {
		updateStatus(ResponseStatus.FAIL);
	    }
	});

	if (!ResponseStatus.FAIL.equals(downloadStatus.getStatus())) {
	    // find cover page or any downloadable items
	    extractCoverPageURLsAndStartDownloading();
	}

	return false;
    }

    /**
     * Extract cover page ur ls and start downloading.
     */
    private void extractCoverPageURLsAndStartDownloading() {
	final ArrayList<Book> books = XMLParser.getBooksList(downloadParams
		.getToFolder() + downloadParams.getNewFilename());

	totalParts += books.size();

	String url = "";
	String extention = "";
	for (final Book b : books) {
	    url = b.getCoverpage().getWebPath();
	    extention = ".png";

	    final int pos = url.lastIndexOf(".");
	    if (pos == -1) {
		extention = ".png";
	    } else {
		extention = url.substring(pos, url.length());
	    }

	    if (!ApplicationData
		    .isFileExists(appData.getBookFilesPath(b.getBookId())
			    + ApplicationData.COVER_IMAGE_NAME + extention)) {
		final Download c_download = new Download(url,
			appData.getBookFilesPath(b.getBookId()),
			ApplicationData.COVER_IMAGE_NAME + extention);

		final DownloadManager dm = new DownloadManager(appData, c_download);
		dm.startDownload(new DownloadCallback() {
		    @Override
		    public void onSuccess(String downloadedString) {
			BooksListDownloadHandler.this.currentPart += 1;
		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
			// TODO update the overall progress accordingly
		    }

		    @Override
		    public void onFailure(String failureMessage) {
			BooksListDownloadHandler.this.currentPart += 1;
		    }
		});
	    }
	}
    }
}