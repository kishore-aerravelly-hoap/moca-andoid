package com.pearl.books.additionalinfo.multipledownload;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.downloadmanager.utils.DownloadObject;

// TODO: Auto-generated Javadoc
/**
 * The Class MultipleDownloads.
 */
public class MultipleDownloads {

    /** The Constant tag. */
    private static final String tag = "Multiple downloads";
    
    /** The Constant SUCCESS. */
    private static final String SUCCESS = "SUCCESS";
    
    /** The Constant IN_PROGRESS. */
    private static final String IN_PROGRESS = "IN_PROGRESS";
    
    /** The Constant DOWNLOAD_ERROR. */
    private static final String DOWNLOAD_ERROR = "DOWNLOAD_ERROR";
    
    /** The download status. */
    public DownloadObject downloadStatus = new DownloadObject();
    
    /** The ds. */
    public List<DownloadObject> ds = new ArrayList<DownloadObject>();

    /**
     * Instantiates a new multiple downloads.
     *
     * @param downloadInfo the download info
     * @param appData the app data
     */
    public MultipleDownloads(final List<DownloadObject> downloadInfo,
	    ApplicationData appData) {
	Logger.warn(tag, "MultipleDownloads constructor");
	Logger.warn(tag, "" + downloadInfo.size());
	for (int i = 0; i < downloadInfo.size(); i++) {
	    final String u = downloadInfo.get(i).getUrl();
	    final String name = downloadInfo.get(i).getName();

	    final StringTokenizer st = new StringTokenizer(u, "/");
	    String fileName = null;
	    while (st.hasMoreTokens()) {
		fileName = st.nextToken();
	    }

	    Logger.warn(tag, "downloading URL is:" + u);
	    final Download imageDownload = new Download(u,
		    appData.getImageGalleryPath(), fileName);
	    final DownloadManager downloadManager = new DownloadManager(appData,
		    imageDownload);

	    downloadManager.startDownload(new DownloadCallback() {
		DownloadObject ds = new DownloadObject();

		@Override
		public void onSuccess(String downloadedString) {
		    Logger.warn(tag, "Image downloaded successfully");
		    ds.setStatus(SUCCESS);
		    ds.setUrl(u);
		    ds.setName(name);
		    updateStatus(ds);

		}

		@Override
		public void onProgressUpdate(int progressPercentage) {
		    downloadStatus.setStatus(IN_PROGRESS);
		}

		@Override
		public void onFailure(String failureMessage) {
		    downloadStatus.setStatus(DOWNLOAD_ERROR);
		    ds.setStatus(DOWNLOAD_ERROR);
		    updateStatus(ds);
		}
	    });

	}
    }

    /**
     * Update status.
     *
     * @param status the status
     */
    public void updateStatus(DownloadObject status) {
	Logger.warn(tag, "in update status");
	ds.add(status);
	Logger.warn(tag, "ds size is:" + ds.size());
	for (int i = 0; i < ds.size(); i++) {
	    Logger.warn(tag, "ds object status for postion:" + i + " is:"
		    + ds.get(i).getStatus());
	    Logger.warn(tag, "ds object status for postion:" + i + " is:"
		    + ds.get(i).getUrl());
	}
    }
}
