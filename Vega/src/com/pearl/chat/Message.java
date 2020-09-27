package com.pearl.chat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class Message.
 */
public class Message {

    /** The id. */
    private String id;
    
    /** The checked value. */
    private String checkedValue;
    
    /** The selected radio type. */
    private String selectedRadioType;
    
    /** The user id. */
    private String userId;
    
    /** The message. */
    private String message;
    
    /** The likes. */
    private List<String> likes;
    
    /** The comments. */
    private List<Comment> comments;
    
    /** The timestamp. */
    private Date timestamp;
    
    /** The user name. */
    private String userName;
    
    /** The restriction. */
    private String restriction;
    
    /** The to user list. */
    private List<String> toUserList;
    
    /** The related users. */
    private List<String> relatedUsers;

    /**
     * Instantiates a new message.
     */
    public Message() {
	super();
    }

    /**
     * Instantiates a new message.
     *
     * @param userId the user id
     * @param message the message
     */
    public Message(String userId, String message) {
	this.userId = userId;
	this.message = message;
	restriction = "NONE";

	likes = new ArrayList<String>();
	comments = new ArrayList<Comment>();
	toUserList = new ArrayList<String>();
	relatedUsers = new ArrayList<String>();

	timestamp = new Date();

	relatedUsers.add(userId);
    }

    /**
     * Instantiates a new message.
     *
     * @param userId the user id
     * @param message the message
     * @param toUserList the to user list
     */
    public Message(String userId, String message, List<String> toUserList) {
	this.userId = userId;
	this.message = message;
	this.toUserList = toUserList;
	restriction = "CUSTOM";
	relatedUsers = toUserList;

	likes = new ArrayList<String>();
	comments = new ArrayList<Comment>();
	toUserList = new ArrayList<String>();
	timestamp = new Date();
	relatedUsers.add(userId);
    }

    /**
     * Like.
     *
     * @param userId the user id
     */
    public void like(String userId) {
	likes.add(userId);
	relatedUsers.add(userId);
    }

    /**
     * Adds the comment.
     *
     * @param userId the user id
     * @param message the message
     */
    public void addComment(String userId, String message) {
	final Comment comment = new Comment(userId, message);
	Logger.warn("Message", "comment is" + message);
	comments.add(comment);
	relatedUsers.add(userId);
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(String id) {
	this.id = id;
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
     * Gets the likes.
     *
     * @return the likes
     */
    public List<String> getLikes() {
	return likes;
    }

    /**
     * Sets the likes.
     *
     * @param likes the new likes
     */
    public void setLikes(List<String> likes) {
	this.likes = likes;
    }

    /**
     * Gets the comments.
     *
     * @return the comments
     */
    public List<Comment> getComments() {
	return comments;
    }

    /**
     * Sets the comments.
     *
     * @param comments the new comments
     */
    public void setComments(List<Comment> comments) {
	this.comments = comments;
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
     * Gets the restriction.
     *
     * @return the restriction
     */
    public String getRestriction() {
	return restriction;
    }

    /**
     * Sets the restriction.
     *
     * @param restriction the new restriction
     */
    public void setRestriction(String restriction) {
	this.restriction = restriction;
    }

    /**
     * Gets the to user list.
     *
     * @return the to user list
     */
    public List<String> getToUserList() {
	return toUserList;
    }

    /**
     * Sets the to user list.
     *
     * @param toUserList the new to user list
     */
    public void setToUserList(List<String> toUserList) {
	this.toUserList = toUserList;
    }

    /**
     * Gets the related users.
     *
     * @return the related users
     */
    public List<String> getRelatedUsers() {
	return relatedUsers;
    }

    /**
     * Sets the related users.
     *
     * @param relatedUsers the new related users
     */
    public void setRelatedUsers(List<String> relatedUsers) {
	this.relatedUsers = relatedUsers;
    }

    /**
     * Gets the checked value.
     *
     * @return the checked value
     */
    public String getCheckedValue() {
	return checkedValue;
    }

    /**
     * Sets the checked value.
     *
     * @param checkedValue the new checked value
     */
    public void setCheckedValue(String checkedValue) {
	this.checkedValue = checkedValue;
    }

    /**
     * Gets the selected radio type.
     *
     * @return the selected radio type
     */
    public String getSelectedRadioType() {
	return selectedRadioType;
    }

    /**
     * Sets the selected radio type.
     *
     * @param selectedRadioType the new selected radio type
     */
    public void setSelectedRadioType(String selectedRadioType) {
	this.selectedRadioType = selectedRadioType;
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
