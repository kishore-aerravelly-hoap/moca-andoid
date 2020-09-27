package com.pearl.chat;

import java.util.List;

import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageList.
 */
public class MessageList {
    
    /** The message list. */
    List<Message> messageList;
    
    /** The message. */
    String message;
    
    /** The status. */
    String status;
    
    /** The data. */
    String data;

    /**
     * Instantiates a new message list.
     */
    public MessageList() {
	super();
    }

    /**
     * Gets the message list.
     *
     * @return the message list
     */
    public List<Message> getMessageList() {
	return messageList;
    }

    /**
     * Sets the message list.
     *
     * @param messageList the new message list
     */
    public void setMessageList(List<Message> messageList) {
	this.messageList = messageList;
    }

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
     * @param message the new message
     */
    public void setMessage(String message) {
	this.message = message;
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

    /**
     * Gets the data.
     *
     * @return the data
     */
    public String getData() {
	Logger.warn("Samreen", "in getdata:" + data);
	return data;
    }

    /**
     * Sets the data.
     *
     * @param data the new data
     */
    public void setData(String data) {
	this.data = data;
    }

}
