package com.pearl.network.downloadmanager.downloadhandlers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import android.util.Log;

import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadHandlerInterface;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.downloadmanager.utils.DownloadStatus;
import com.pearl.network.request.ResponseStatus;

// TODO: Auto-generated Javadoc
/**
 * The Class DefaultDownloadHandler.
 */
public class DefaultDownloadHandler implements DownloadHandlerInterface {
    
    /** The start time. */
    protected Date startTime;
    
    /** The estimated download time. */
    protected Date estimatedDownloadTime;

    /** The download params. */
    protected Download downloadParams;
    
    /** The download callback. */
    protected DownloadCallback downloadCallback;

    /** The download status. */
    protected DownloadStatus downloadStatus;
    
    /** The overall progress. */
    protected int overallProgress;
    
    /** The total parts. */
    protected int totalParts;
    
    /** The current part. */
    protected int currentPart;
    
    /** The current part progress. */
    protected int currentPartProgress;

    /** The helper_id. */
    protected int helper_id;
    
    /** The tag. */
    private final String TAG = "Default Downloader";

    /** The app data. */
    protected ApplicationData appData;

    /** The current download response. */
    protected byte[] currentDownloadResponse;

    /**
     * Instantiates a new default download handler.
     *
     * @param appData the app data
     */
    public DefaultDownloadHandler(ApplicationData appData) {
	this.appData = appData;

	downloadStatus = new DownloadStatus();
    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.DownloadHandlerInterface#startDownload(com.pearl.network.downloadmanager.utils.Download, com.pearl.network.downloadmanager.utils.DownloadCallback, int)
     */
    @Override
    public final void startDownload(Download download,
	    DownloadCallback downloadCallback, int temp_id) {
	helper_id = temp_id;

	this.startDownload(download, downloadCallback);
    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.DownloadHandlerInterface#startDownload(com.pearl.network.downloadmanager.utils.Download, com.pearl.network.downloadmanager.utils.DownloadCallback)
     */
    @Override
    public final void startDownload(Download download,
	    DownloadCallback downloadCallback) {
	downloadParams = download;
	this.downloadCallback = downloadCallback;
	startTime = new Date();

	totalParts = 1;
	currentPart = 1;
	currentPartProgress = 0;
	updateStatus(ResponseStatus.IN_PROGRESS);
	updateCurrentPartProgress(0);
	updateOverallProgress();

	if (download(downloadParams)) {
	    downloadStatus.setStaus(ResponseStatus.SUCCESS);
	} else {
	    downloadStatus.setStaus(ResponseStatus.FAIL);
	}
    }

    /**
     * Download.
     *
     * @param download the download
     * @return true, if successful
     */
    protected boolean download(Download download) {
	Logger.verbose("Default Downloader",
		"Downloading from url:" + download.getUrl());
	Logger.warn(TAG, "URL is:" + download.getUrl());

	URL url;
	int count;
	try {
	    url = new URL(download.getUrl());
	    final HttpURLConnection huc = (HttpURLConnection) url.openConnection();
	    // huc.connect();
	    huc.setConnectTimeout(1500);

	    Log.w("tag", "DOWNLOAD: HTTP Status = " + huc.getResponseCode());

	    if (huc.getResponseCode() != HttpURLConnection.HTTP_OK) {
		updateStatus(ResponseStatus.FAIL);
		return false;
	    }

	    // this will be useful so that you can show a typical 0-100%
	    // progress bar
	    final int lenghtOfFile = huc.getContentLength();
	    Log.w("tag", "Length of file = (" + download.getUrl() + ")"
		    + lenghtOfFile);

	    // download the file
	    final InputStream input = new BufferedInputStream(huc.getInputStream());
	    String newfilename = "download.xml";

	    if (null != download.getNewFilename()) {
		newfilename = download.getNewFilename();
	    }

	    final String toFolder = download.getToFolder();
	    final String toFilename = newfilename;
	    final String tempFilename = "temp_" + newfilename;
	    final byte data[];
	    if(lenghtOfFile!=-1){
	    	data = new byte[lenghtOfFile];
	    }else 
	    	data = new byte[1024];
	     
	    long total = 0;

	    deleteFile(toFolder + tempFilename);

	    while ((count = input.read(data)) != -1) {
		total += count;

		// publishing the progress....
		final int progress = (int) (total * 100 / lenghtOfFile);
		updateCurrentPartProgress(progress < 4 ? progress
			: progress - 2);
		updateOverallProgress();

		final byte[] temp_bytes = new byte[count];
		for (int i = 0; i < count; i++) {
		    temp_bytes[i] = data[i];
		}

		updateFile(temp_bytes, toFolder, tempFilename);
		ApplicationData.writeToFile(new String(temp_bytes),
			appData.getAppTempPath() + "jsondownloader.txt");
	    }
	    

	    renameFile(toFolder + toFilename, toFolder + toFilename + "_bak");
	    if (renameFile(toFolder + tempFilename, toFolder + toFilename)) {
		deleteFile(toFolder + toFilename + "_bak");
	    } else {
		renameFile(toFolder + toFilename + "_bak", toFolder
			+ toFilename);
	    }

	    input.close();
	    updateCurrentPartProgress(100);
	    updateOverallProgress();
	   // Logger.warn("Default download handler", "file after writing after downloading is:"+ApplicationData.readFile(toFolder+ toFilename));	    
	    updateStatus(ResponseStatus.SUCCESS);

	    return true;
	} catch (final Exception e) {
	    Logger.error("Default DownloadHandler", "Download Failed:+++++"
		    + ApplicationData.getExceptionStackTrace(e));
	    updateStatus(ResponseStatus.FAIL);
	    return false;
	}

    }

    /**
     * Rename file.
     *
     * @param oldFilename the old filename
     * @param newFilename the new filename
     * @return true, if successful
     */
    protected boolean renameFile(String oldFilename, String newFilename) {
	// File (or directory) with old name
	final File file = new File(oldFilename);

	// File (or directory) with new name
	final File file2 = new File(newFilename);

	// Rename file (or directory)
	return file.renameTo(file2);
    }

    /**
     * Delete file.
     *
     * @param filename the filename
     * @return true, if successful
     */
    protected boolean deleteFile(String filename) {
	final File f1 = new File(filename);
	return f1.delete();
    }

    /**
     * Update file.
     *
     * @param data the data
     * @param toFolder the to folder
     * @param newFilename the new filename
     * @return true, if successful
     */
    protected boolean updateFile(byte[] data, String toFolder,
	    String newFilename) {
	if (!isFileExistsInLocal(toFolder)) {
	    final File fileFolder = new File(toFolder);

	    fileFolder.mkdirs();

	    fileFolder.setWritable(true);
	    fileFolder.setReadable(true);
	}

	FileOutputStream fos;
	try {
	    fos = new FileOutputStream(toFolder + newFilename, true);
	    fos.write(data);
	} catch (final Exception e) {
	    return false;
	}

	return true;
    }

    /**
     * Save file.
     *
     * @param folder the folder
     * @param filename the filename
     * @param content the content
     * @return true, if successful
     */
    protected boolean saveFile(String folder, String filename, byte[] content) {
	try {
	    if (!isFileExistsInLocal(folder)) {
		final File fileFolder = new File(folder);

		fileFolder.mkdirs();

		fileFolder.setWritable(true);
		fileFolder.setReadable(true);
	    }

	    if (folder == null) {
		folder = "";
	    }

	    final File newFile = new File(folder + filename);

	    final FileOutputStream fos = new FileOutputStream(newFile);
	    fos.write(content);
	    fos.close();

	    newFile.setWritable(true);
	    newFile.setReadable(true);
	} catch (final Exception e) {
	    Logger.warn("save:", e.toString());

	    return false;
	}

	return true;
    }

    /**
     * Checks if is file exists in local.
     *
     * @param filename the filename
     * @return true, if is file exists in local
     */
    protected boolean isFileExistsInLocal(String filename) {
	final File file = new File(filename);
	if (file.exists()) {
	    return true;
	}

	return false;
    }

    /**
     * Update current part progress.
     *
     * @param prog the prog
     */
    protected void updateCurrentPartProgress(int prog) {
	currentPartProgress = prog;
    }

    /**
     * Update overall progress.
     */
    protected void updateOverallProgress() {// int prog){
	// TODO correct the formula
	final int prog = currentPartProgress / totalParts * currentPart;
	overallProgress = prog;

	// Logger.info("Download Progress","Overall("+prog+") | Current Part Progress("+currentPartProgress+")");

	if (downloadCallback != null) {
	    // Samreen - commented the below code to display the exatc progress
	    // rather than dividing by 2
	    // downloadCallback.onProgressUpdate(prog);
	    downloadCallback.onProgressUpdate(currentPartProgress);
	}
    }

    /**
     * Update status.
     *
     * @param status the status
     */
    protected void updateStatus(String status) {
	downloadStatus.setStaus(status);

	if (downloadCallback != null) {
	    if (ResponseStatus.SUCCESS.equals(status)) {
		downloadCallback.onSuccess("success");
	    } else if (ResponseStatus.FAIL.equals(status)) {
		downloadCallback.onFailure("fail");
	    }
	}
    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.DownloadHandlerInterface#pauseDownload()
     */
    @Override
    public void pauseDownload() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.DownloadHandlerInterface#resumeDownload()
     */
    @Override
    public void resumeDownload() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.DownloadHandlerInterface#cancelDownload()
     */
    @Override
    public void cancelDownload() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.DownloadHandlerInterface#getCurrentDownloadingPart()
     */
    @Override
    public int getCurrentDownloadingPart() {
	return currentPart;
    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.DownloadHandlerInterface#getCurrentPartProgress()
     */
    @Override
    public int getCurrentPartProgress() {
	return currentPartProgress;
    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.DownloadHandlerInterface#getEstimatedTimeInSecs()
     */
    @Override
    public int getEstimatedTimeInSecs() {
	// TODO
	return 0;
    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.DownloadHandlerInterface#getNoOfParts()
     */
    @Override
    public int getNoOfParts() {
	return totalParts;
    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.DownloadHandlerInterface#getOverallProgress()
     */
    @Override
    public int getOverallProgress() {
	return overallProgress;
    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.DownloadHandlerInterface#getOverallProgressString()
     */
    @Override
    public String getOverallProgressString() {
	return overallProgress + "% downloaded";
    }

    /* (non-Javadoc)
     * @see com.pearl.network.downloadmanager.DownloadHandlerInterface#getStatus()
     */
    @Override
    public DownloadStatus getStatus() {
	return downloadStatus;
    }
}