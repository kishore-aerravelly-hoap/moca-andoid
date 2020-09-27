package com.pearl.ui.models;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class VegaNotices.
 */
public class VegaNotices {
    
    /** The user notices. */
    List<Notice> userNotices;

    /**
     * Gets the user notices.
     *
     * @return the user notices
     */
    public List<Notice> getUserNotices() {
	return userNotices;
    }

    /**
     * Sets the user notices.
     *
     * @param userNotices the new user notices
     */
    public void setUserNotices(List<Notice> userNotices) {
	this.userNotices = userNotices;
    }
}