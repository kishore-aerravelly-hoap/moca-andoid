package com.pearl.chat.server.response;

import java.io.Serializable;

import com.pearl.ui.models.StatusType;

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
    private String message;
    
    /** The status. */
    private StatusType status;
    
    /** The data. */
    private Object data;

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
    public Object getData() {
	return data;
    }

    /**
     * Sets the data.
     *
     * @param data the new data
     */
    public void setData(Object data) {
	this.data = data;
    }

}
