package com.pearl.network.downloadmanager.utils;

// TODO: Auto-generated Javadoc
/**
 * The Class DownloadObject.
 */
public class DownloadObject {

    /** The url. */
    String url;
    
    /** The name. */
    String name;
    
    /** The status. */
    String status;

    /** The Constant SUCCESS. */
    public static final String SUCCESS = "SUCCESS";
    
    /** The Constant IN_PROGRESS. */
    public static final String IN_PROGRESS = "IN_PROGRESS";
    
    /** The Constant DOWNLOAD_ERROR. */
    public static final String DOWNLOAD_ERROR = "DOWNLOAD_ERROR";
    
    /** The Constant NOT_DONE. */
    public static final String NOT_DONE = "NOT_DONE";

    /**
     * Instantiates a new download object.
     */
    public DownloadObject() {
	status = NOT_DONE;
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
     * @param url the new url
     */
    public void setUrl(String url) {
	this.url = url;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
	return status;
    }

    /**
     * Sets the status.
     *
     * @param status the new status
     */
    public void setStatus(String status) {
	this.status = status;
    }

}
