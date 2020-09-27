/**
 * 
 */
package com.pearl.ui.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class TeacherTestComment.
 *
 * @author ENG2
 */
public class TeacherTestComment implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id. */
    private String id;
    
    /** The from user id. */
    private String fromUserId;
    
    /** The to user id. */
    private String toUserId;
    
    /** The comment. */
    private String comment;
    
    /** The exam id. */
    private String examId;
    
    /** The exam title. */
    private String examTitle;
    
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
     * @param id the id to set
     */
    public void setId(String id) {
	this.id = id;
    }
    
    /**
     * Gets the from user id.
     *
     * @return the fromUserId
     */
    public String getFromUserId() {
	return fromUserId;
    }
    
    /**
     * Sets the from user id.
     *
     * @param fromUserId the fromUserId to set
     */
    public void setFromUserId(String fromUserId) {
	this.fromUserId = fromUserId;
    }
    
    /**
     * Gets the to user id.
     *
     * @return the toUserId
     */
    public String getToUserId() {
	return toUserId;
    }
    
    /**
     * Sets the to user id.
     *
     * @param toUserId the toUserId to set
     */
    public void setToUserId(String toUserId) {
	this.toUserId = toUserId;
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
     * @param comment the comment to set
     */
    public void setComment(String comment) {
	this.comment = comment;
    }
    
    /**
     * Gets the exam id.
     *
     * @return the examId
     */
    public String getExamId() {
	return examId;
    }
    
    /**
     * Sets the exam id.
     *
     * @param examId the examId to set
     */
    public void setExamId(String examId) {
	this.examId = examId;
    }
    
    /**
     * Gets the exam title.
     *
     * @return the examTitle
     */
    public String getExamTitle() {
	return examTitle;
    }
    
    /**
     * Sets the exam title.
     *
     * @param examTitle the examTitle to set
     */
    public void setExamTitle(String examTitle) {
	this.examTitle = examTitle;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "TeacherTestComment [id=" + id + ", fromUserId=" + fromUserId
		+ ", toUserId=" + toUserId + ", comment=" + comment
		+ ", examId=" + examId + ", examTitle=" + examTitle + "]";
    }
}
