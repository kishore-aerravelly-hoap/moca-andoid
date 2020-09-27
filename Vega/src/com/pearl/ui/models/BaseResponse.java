/**
 * 
 */
package com.pearl.ui.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseResponse.
 *
 * @author kpamulapati
 */
public class BaseResponse implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The message. */
    protected String message;
    
    /** The status. */
    protected StatusType status;
    
    /** The data. */
    protected String data;

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
	return message;
    }

    /**
     * Sets the message.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
	this.message = message;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public StatusType getStatus() {
	return status;
    }

    /**
     * Sets the status.
     *
     * @param status the status to set
     */
    public void setStatus(StatusType status) {
	this.status = status;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public String getData() {
	return data;
    }

    /**
     * Sets the data.
     *
     * @param data the data to set
     */
    public void setData(String data) {
	this.data = data;
    }

}
