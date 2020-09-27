package com.pearl.ui.models;

import java.io.Serializable;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class FeedbackDataModel.
 */
public class FeedbackDataModel implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The user id. */
    private String userId;

    /** The feedback data. */
    private String feedbackData;

    /** The feedback type. */
    private String feedbackType;

    /** The date. */
    private Date date;

    /**
     * Gets the user id.
     *
     * @return the userId
     */
    public String getUserId() {
	return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
	this.userId = userId;
    }

    /**
     * Gets the feedback data.
     *
     * @return the feedbackData
     */
    public String getFeedbackData() {
	return feedbackData;
    }

    /**
     * Sets the feedback data.
     *
     * @param feedbackData the feedbackData to set
     */
    public void setFeedbackData(String feedbackData) {
	this.feedbackData = feedbackData;
    }

    /**
     * Gets the feedback type.
     *
     * @return the feedbackType
     */
    public String getFeedbackType() {
	return feedbackType;
    }

    /**
     * Sets the feedback type.
     *
     * @param feedbackType the feedbackType to set
     */
    public void setFeedbackType(String feedbackType) {
	this.feedbackType = feedbackType;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public Date getDate() {
	return date;
    }

    /**
     * Sets the date.
     *
     * @param date the date to set
     */
    public void setDate(Date date) {
	this.date = date;
    }

}
