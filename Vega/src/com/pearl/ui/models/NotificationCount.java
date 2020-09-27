package com.pearl.ui.models;

import java.io.Serializable;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class NotificationCount.
 */
public class NotificationCount implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id. */
    private String id;
    
    /** The user id. */
    private String userId;
    
    /** The time stamp. */
    private Date timeStamp;
    
    /** The new msg count. */
    private int newMsgCount;
    
    /** The new attendance count. */
    private int  newAttendanceCount;
    
    /** The new books count. */
    private int  newBooksCount;
    
    /** The new exam count. */
    private int  newExamCount;// notify the student
    
    /** The new additional info count. */
    private int newAdditionalInfoCount;
    
    /** The new cal event count. */
    private int newCalEventCount;
    
    /** The new notice count. */
    private int newNoticeCount;
    
    /** The new like count. */
    private int newLikeCount;
    
    /** The new comment count. */
    private int newCommentCount;
    
    /** The new result publish count. */
    private int newResultPublishCount;// notify the student and related roles that results have been published
    
    /** The new test create count. */
    private int newTestCreateCount;// need to approve
    
    /** The new test approved count. */
    private int newTestApprovedCount; // need to publish the exam
    
    /** The new test rejected count. */
    private int newTestRejectedCount; // need to edit - FUTURE
    
    /** The new epub modification count. */
    private int newEpubModificationCount;
    
    /** The new exam evaluated count. */
    private int newExamEvaluatedCount;// need to publish the results
    
    /** The test to be evaluated count. */
    private int testToBeEvaluatedCount; //need to evaluate
    
    private int newCreatedStudentCount;
    private int attendanceConfigCount;


    /**
     * Gets the new epub modification count.
     *
     * @return the newEpubModificationCount
     */
    public int getNewEpubModificationCount() {
	return newEpubModificationCount;
    }
    
    /**
     * Sets the new epub modification count.
     *
     * @param newEpubModificationCount the newEpubModificationCount to set
     */
    public void setNewEpubModificationCount(int newEpubModificationCount) {
	this.newEpubModificationCount = newEpubModificationCount;
    }
    /*		public NotificationCount(String userId) {
			//super();
			this.userId = userId;
			this.timeStamp = new Date();
			this.newMsgCount = 0;
			this.newAttendanceCount = 0;
			this.newBooksCount = 0;
			this.newExamCount = 0;
			this.newAdditionalInfoCount = 0;
			this.newCalEventCount = 0;
			this.newNoticeCount=0;
			this.newLikeCount=0;
			this.newCommentCount=0;
			this.newExamEvaluatedCount=0;
		}*/
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
     * Gets the time stamp.
     *
     * @return the time stamp
     */
    public Date getTimeStamp() {
	return timeStamp;
    }
    
    /**
     * Sets the time stamp.
     *
     * @param timeStamp the new time stamp
     */
    public void setTimeStamp(Date timeStamp) {
	this.timeStamp = timeStamp;
    }
    
    /**
     * Gets the new msg count.
     *
     * @return the new msg count
     */
    public int getNewMsgCount() {
	return newMsgCount;
    }
    
    /**
     * Sets the new msg count.
     *
     * @param newMsgCount the new new msg count
     */
    public void setNewMsgCount(int newMsgCount) {
	this.newMsgCount = newMsgCount;
    }
    
    /**
     * Gets the new attendance count.
     *
     * @return the new attendance count
     */
    public int getNewAttendanceCount() {
	return newAttendanceCount;
    }
    
    /**
     * Sets the new attendance count.
     *
     * @param newAttendanceCount the new new attendance count
     */
    public void setNewAttendanceCount(int newAttendanceCount) {
	this.newAttendanceCount = newAttendanceCount;
    }
    
    /**
     * Gets the new books count.
     *
     * @return the new books count
     */
    public int getNewBooksCount() {
	return newBooksCount;
    }
    
    /**
     * Sets the new books count.
     *
     * @param newBooksCount the new new books count
     */
    public void setNewBooksCount(int newBooksCount) {
	this.newBooksCount = newBooksCount;
    }
    
    /**
     * Gets the new exam count.
     *
     * @return the new exam count
     */
    public int getNewExamCount() {
	return newExamCount;
    }
    
    /**
     * Sets the new exam count.
     *
     * @param newExamCount the new new exam count
     */
    public void setNewExamCount(int newExamCount) {
	this.newExamCount = newExamCount;
    }
    
    /**
     * Gets the new additional info count.
     *
     * @return the new additional info count
     */
    public int getNewAdditionalInfoCount() {
	return newAdditionalInfoCount;
    }
    
    /**
     * Sets the new additional info count.
     *
     * @param newAdditionalInfoCount the new new additional info count
     */
    public void setNewAdditionalInfoCount(int newAdditionalInfoCount) {
	this.newAdditionalInfoCount = newAdditionalInfoCount;
    }
    
    /**
     * Gets the new cal event count.
     *
     * @return the new cal event count
     */
    public int getNewCalEventCount() {
	return newCalEventCount;
    }
    
    /**
     * Sets the new cal event count.
     *
     * @param newCalEventCount the new new cal event count
     */
    public void setNewCalEventCount(int newCalEventCount) {
	this.newCalEventCount = newCalEventCount;
    }
    
    /**
     * Gets the new notice count.
     *
     * @return the new notice count
     */
    public int getNewNoticeCount() {
	return newNoticeCount;
    }
    
    /**
     * Sets the new notice count.
     *
     * @param newNoticeCount the new new notice count
     */
    public void setNewNoticeCount(int newNoticeCount) {
	this.newNoticeCount = newNoticeCount;
    }

    /**
     * Gets the new like count.
     *
     * @return the new like count
     */
    public int getNewLikeCount() {
	return newLikeCount;
    }
    
    /**
     * Sets the new like count.
     *
     * @param newLikeCount the new new like count
     */
    public void setNewLikeCount(int newLikeCount) {
	this.newLikeCount = newLikeCount;
    }
    
    /**
     * Gets the new comment count.
     *
     * @return the new comment count
     */
    public int getNewCommentCount() {
	return newCommentCount;
    }
    
    /**
     * Sets the new comment count.
     *
     * @param newCommentCount the new new comment count
     */
    public void setNewCommentCount(int newCommentCount) {
	this.newCommentCount = newCommentCount;
    }

    /**
     * Gets the new result publish count.
     *
     * @return the newResultPublishCount
     */
    public int getNewResultPublishCount() {
	return newResultPublishCount;
    }
    
    /**
     * Sets the new result publish count.
     *
     * @param newResultPublishCount the newResultPublishCount to set
     */
    public void setNewResultPublishCount(int newResultPublishCount) {
	this.newResultPublishCount = newResultPublishCount;
    }

    /**
     * Gets the new test create count.
     *
     * @return the newTestCreateCount
     */
    public int getNewTestCreateCount() {
	return newTestCreateCount;
    }
    
    /**
     * Sets the new test create count.
     *
     * @param newTestCreateCount the newTestCreateCount to set
     */
    public void setNewTestCreateCount(int newTestCreateCount) {
	this.newTestCreateCount = newTestCreateCount;
    }
    
    /**
     * Gets the new test approved count.
     *
     * @return the newTestApprovedCount
     */
    public int getNewTestApprovedCount() {
	return newTestApprovedCount;
    }
    
    /**
     * Sets the new test approved count.
     *
     * @param newTestApprovedCount the newTestApprovedCount to set
     */
    public void setNewTestApprovedCount(int newTestApprovedCount) {
	this.newTestApprovedCount = newTestApprovedCount;
    }
    
    /**
     * Gets the new test rejected count.
     *
     * @return the newTestRejectedCount
     */
    public int getNewTestRejectedCount() {
	return newTestRejectedCount;
    }
    
    /**
     * Sets the new test rejected count.
     *
     * @param newTestRejectedCount the newTestRejectedCount to set
     */
    public void setNewTestRejectedCount(int newTestRejectedCount) {
	this.newTestRejectedCount = newTestRejectedCount;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Notification [userId=" + userId + ", timeStamp="
		+ timeStamp + ", newMsgCount=" + newMsgCount
		+ ", newAttendanceCount=" + newAttendanceCount + ", newBooksCount="
		+ newBooksCount + ", newExamCount=" + newExamCount
		+ ", newAdditionalInfoCount=" + newAdditionalInfoCount
		+ ", newCalEventCount=" + newCalEventCount + ", newNoticeCount="
		+ newNoticeCount + ", newLikeCount=" + newLikeCount
		+ ", newCommentCount=" + newCommentCount
		+ ", newResultPublishCount=" + newResultPublishCount
		+ ", newTestCreateCount=" + newTestCreateCount
		+ ", newTestApprovedCount=" + newTestApprovedCount
		+ ", newTestRejectedCount=" + newTestRejectedCount
		+ ", newEpubModificationCount=" + newEpubModificationCount
		+ ", newExamEvaluatedCount=" + newExamEvaluatedCount + "]";
    }
    
    /**
     * Gets the new exam evaluated count.
     *
     * @return the newExamEvaluatedCount
     */
    public int getNewExamEvaluatedCount() {
	return newExamEvaluatedCount;
    }
    
    /**
     * Sets the new exam evaluated count.
     *
     * @param newExamEvaluatedCount the newExamEvaluatedCount to set
     */
    public void setNewExamEvaluatedCount(int newExamEvaluatedCount) {
	this.newExamEvaluatedCount = newExamEvaluatedCount;
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
     * @param id the id to set
     */
    public void setId(String id) {
	this.id = id;
    }
    
    /**
     * Gets the test to be evaluated count.
     *
     * @return the testToBeEvaluatedCount
     */
    public int getTestToBeEvaluatedCount() {
	return testToBeEvaluatedCount;
    }
    
    /**
     * Sets the test to be evaluated count.
     *
     * @param testToBeEvaluatedCount the testToBeEvaluatedCount to set
     */
    public void setTestToBeEvaluatedCount(int testToBeEvaluatedCount) {
	this.testToBeEvaluatedCount = testToBeEvaluatedCount;
    }

	public int getNewCreatedStudentCount() {
		return newCreatedStudentCount;
	}

	public void setNewCreatedStudentCount(int newCreatedStudentCount) {
		this.newCreatedStudentCount = newCreatedStudentCount;
	}

	public int getAttendanceConfigCount() {
		return attendanceConfigCount;
	}

	public void setAttendanceConfigCount(int attendanceConfigCount) {
		this.attendanceConfigCount = attendanceConfigCount;
	}

}
