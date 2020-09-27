package com.pearl.network.downloadmanager.utils;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class Download.
 */
public class Download {
    
    /** The url. */
    private String url;
    
    /** The to folder. */
    private String toFolder;
    
    /** The new filename. */
    private String newFilename;
    
    /** The download type. */
    private DownloadType downloadType;

    /** The context. */
    private Context context;

    /**
     * Instantiates a new download.
     *
     * @param downloadType the download type
     * @param url the url
     * @param to_folder the to_folder
     * @param new_filename the new_filename
     */
    public Download(DownloadType downloadType, String url, String to_folder,
	    String new_filename) {
	this.downloadType = downloadType;

	this.url = url;
	toFolder = to_folder;
	newFilename = new_filename;
    }

    /**
     * Instantiates a new download.
     *
     * @param url the url
     * @param to_folder the to_folder
     * @param new_filename the new_filename
     */
    public Download(String url, String to_folder, String new_filename) {
	downloadType = new DownloadType();
	downloadType.setType(DownloadType.DEFAULT);

	this.url = url;
	toFolder = to_folder;
	newFilename = new_filename;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
	return url;
    }

    /**
     * Sets the url.
     *
     * @param url the url to set
     */
    public void setUrl(String url) {
	this.url = url;
    }

    /**
     * Gets the to folder.
     *
     * @return the toFolder
     */
    public String getToFolder() {
	return toFolder;
    }

    /**
     * Sets the to folder.
     *
     * @param toFolder the toFolder to set
     */
    public void setToFolder(String toFolder) {
	this.toFolder = toFolder;
    }

    /**
     * Gets the new filename.
     *
     * @return the newFilename
     */
    public String getNewFilename() {
	return newFilename;
    }

    /**
     * Sets the new filename.
     *
     * @param newFilename the newFilename to set
     */
    public void setNewFilename(String newFilename) {
	this.newFilename = newFilename;
    }

    /**
     * Gets the download type.
     *
     * @return the download type
     */
    public DownloadType getDownloadType() {
	return downloadType;
    }

    /**
     * Sets the download type.
     *
     * @param downloadType the new download type
     */
    public void setDownloadType(DownloadType downloadType) {
	this.downloadType = downloadType;
    }

    /**
     * Gets the context.
     *
     * @return the context
     */
    public Context getContext() {
	return context;
    }

    /**
     * Sets the context.
     *
     * @param context the context to set
     */
    public void setContext(Context context) {
	this.context = context;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	String str = "";

	str += "Url:" + url;
	str += " | DownloadType:" + downloadType.getType();
	str += " | To folder:" + toFolder;
	str += " | New File name:" + newFilename;

	return str;
    }
}