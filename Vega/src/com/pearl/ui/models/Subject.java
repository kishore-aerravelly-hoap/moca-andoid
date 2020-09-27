/**
 * 
 */
package com.pearl.ui.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Subject.
 *
 * @author kpamulapati
 */
public class Subject implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The subject id. */
    private String subjectId;
    
    /** The subject name. */
    private String subjectName;
    
    /** The status. */
    private String status;
    
    /** The created user id. */
    private String createdUserId;
    
    /** The created date. */
    private String createdDate;
    
    /** The updated user id. */
    private String updatedUserId;
    
    /** The updated date. */
    private String updatedDate;

    /**
     * Gets the subject id.
     *
     * @return the subjectId
     */
    public String getSubjectId() {
	return subjectId;
    }

    /**
     * Sets the subject id.
     *
     * @param subjectId the subjectId to set
     */
    public void setSubjectId(String subjectId) {
	this.subjectId = subjectId;
    }

    /**
     * Gets the subject name.
     *
     * @return the subjectName
     */
    public String getSubjectName() {
	return subjectName;
    }

    /**
     * Sets the subject name.
     *
     * @param subjectName the subjectName to set
     */
    public void setSubjectName(String subjectName) {
	this.subjectName = subjectName;
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
     * @param status the status to set
     */
    public void setStatus(String status) {
	this.status = status;
    }

    /**
     * Gets the created user id.
     *
     * @return the createdUserId
     */
    public String getCreatedUserId() {
	return createdUserId;
    }

    /**
     * Sets the created user id.
     *
     * @param createdUserId the createdUserId to set
     */
    public void setCreatedUserId(String createdUserId) {
	this.createdUserId = createdUserId;
    }

    /**
     * Gets the created date.
     *
     * @return the createdDate
     */
    public String getCreatedDate() {
	return createdDate;
    }

    /**
     * Sets the created date.
     *
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(String createdDate) {
	this.createdDate = createdDate;
    }

    /**
     * Gets the updated user id.
     *
     * @return the updatedUserId
     */
    public String getUpdatedUserId() {
	return updatedUserId;
    }

    /**
     * Sets the updated user id.
     *
     * @param updatedUserId the updatedUserId to set
     */
    public void setUpdatedUserId(String updatedUserId) {
	this.updatedUserId = updatedUserId;
    }

    /**
     * Gets the updated date.
     *
     * @return the updatedDate
     */
    public String getUpdatedDate() {
	return updatedDate;
    }

    /**
     * Sets the updated date.
     *
     * @param updatedDate the updatedDate to set
     */
    public void setUpdatedDate(String updatedDate) {
	this.updatedDate = updatedDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Subject [subjectId=" + subjectId + ", subjectName="
		+ subjectName + ", status=" + status + ", createdUserId="
		+ createdUserId + ", createdDate=" + createdDate
		+ ", updatedUserId=" + updatedUserId + ", updatedDate="
		+ updatedDate + "]";
    }

}
