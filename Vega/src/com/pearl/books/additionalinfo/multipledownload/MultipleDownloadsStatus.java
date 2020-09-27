package com.pearl.books.additionalinfo.multipledownload;

// TODO: Auto-generated Javadoc
/**
 * The Class MultipleDownloadsStatus.
 */
public class MultipleDownloadsStatus {

    /** The Constant SUCCESS. */
    public static final String SUCCESS = "SUCCESS";
    
    /** The Constant IN_PROGRESS. */
    public static final String IN_PROGRESS = "IN_PROGRESS";
    
    /** The Constant DOWNLOAD_ERROR. */
    public static final String DOWNLOAD_ERROR = "DOWNLOAD_ERROR";
    
    /** The Constant NOT_DONE. */
    public static final String NOT_DONE = "NOT_DONE";

    /** The status. */
    String status;
    
    /** The url. */
    String url;

    /**
     * Instantiates a new multiple downloads status.
     */
    public MultipleDownloadsStatus() {
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
