package com.pearl.network.downloadmanager.utils;

import com.pearl.network.request.ResponseStatus;

// TODO: Auto-generated Javadoc
/**
 * The Class DownloadStatus.
 */
public class DownloadStatus extends ResponseStatus {
    
    /** The Constant DELETED. */
    public static final String DELETED = "DELETED";

    /** The status. */
    private String status;

    /**
     * Instantiates a new download status.
     */
    public DownloadStatus() {
	status = ResponseStatus.NOT_DONE;
    }

    /* (non-Javadoc)
     * @see com.pearl.network.request.ResponseStatus#setStaus(java.lang.String)
     */
    @Override
    public DownloadStatus setStaus(String s) {
	status = s;

	return this;
    }

    /* (non-Javadoc)
     * @see com.pearl.network.request.ResponseStatus#getStatus()
     */
    @Override
    public String getStatus() {
	return status;
    }
}
