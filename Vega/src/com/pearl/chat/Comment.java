package com.pearl.chat;

import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class Comment.
 */
public class Comment {

    /** The user id. */
    private String userId;
    
    /** The user name. */
    private String userName;
    
    /** The comment. */
    private String comment;
    
    /** The timestamp. */
    private Date timestamp;

    /**
     * Instantiates a new comment.
     */
    public Comment() {
	super();
    }

    /**
     * Instantiates a new comment.
     *
     * @param userId the user id
     * @param comment the comment
     */
    public Comment(String userId, String comment) {
	this.userId = userId;
	this.comment = comment;
	timestamp = new Date();
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public String getUserId() {
	return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId the new user id
     */
    public void setUserId(String userId) {
	this.userId = userId;
    }

    /**
     * Gets the comment.
     *
     * @return the comment
     */
    public String getComment() {
	return comment;
    }

    /**
     * Sets the comment.
     *
     * @param comment the new comment
     */
    public void setComment(String comment) {
	this.comment = comment;
    }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public Date getTimestamp() {
	return timestamp;
    }

    /**
     * Sets the timestamp.
     *
     * @param timestamp the new timestamp
     */
    public void setTimestamp(Date timestamp) {
	this.timestamp = timestamp;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
	return userName;
    }

    /**
     * Sets the user name.
     *
     * @param userName the new user name
     */
    public void setUserName(String userName) {
	this.userName = userName;
    }

}
