package com.pearl.user.teacher.examination;

import java.io.Serializable;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamDetails.
 */
public class ExamDetails implements Serializable{
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The exam id. */
    private String examId;
    
    /** The title. */
    private String title;
    
    /** The description. */
    private String description;
    
    /** The subject. */
    private String subject;
    
    /** The start time. */
    private long startTime;
    
    /** The end time. */
    private long endTime;
    
    /** The duration. */
    private long duration;
    
    /** The state. */
    private String state;
    
    /** The result id. */
    private long resultId;
    
    /** The is retakeable. */
    private boolean isRetakeable;
    
    /** The max points. */
    private int maxPoints;
    
    /** The min points. */
    private int minPoints;
    
    /** The negative points. */
    private boolean negativePoints;
    
    /** The allowed book ids. */
    private ArrayList<Integer> allowedBookIds;
    
    /** The evaluated. */
    private boolean evaluated;
    
    /** The uploaded date. */
    private long uploadedDate;
    
    /** The exam category. */
    private String examCategory;
    
    /**
     * Gets the exam category.
     *
     * @return the examCategory
     */
	public String getExamCategory() {
		return examCategory;
	}

	/**
	 * Sets the exam category.
	 *
	 * @param examCategory the examCategory to set
	 */
	public void setExamCategory(String examCategory) {
		this.examCategory = examCategory;
	}

	/**
	 * Instantiates a new exam details.
	 */
	public ExamDetails(){

    }

    /**
     * Gets the title.
     *
     * @return the name
     */
    public String getTitle() {
	return title;
    }
    
    /**
     * Gets the exam id.
     *
     * @return the id
     */
    public String getExamId() {
	return examId;
    }
    
    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
	return description;
    }
    
    /**
     * Gets the start time.
     *
     * @return the startTime
     */
    public long getStartTime() {
	return startTime;
    }
    
    /**
     * Gets the end time.
     *
     * @return the endTime
     */
    public long getEndTime() {
	return endTime;
    }
    
    /**
     * Gets the state.
     *
     * @return the state
     */
    public String getState() {
	return state;
    }
    
    /**
     * Gets the result id.
     *
     * @return the resultId
     */
    public long getResultId() {
	return resultId;
    }
    
    /**
     * Checks if is retakeable.
     *
     * @return the isRetakeable
     */
    public boolean isRetakeable() {
	return isRetakeable;
    }
    
    /**
     * Gets the max points.
     *
     * @return the maxPoints
     */
    public int getMaxPoints() {
	return maxPoints;
    }
    
    /**
     * Gets the min points.
     *
     * @return the minPoints
     */
    public int getMinPoints() {
	return minPoints;
    }
    
    /**
     * Checks for negative points.
     *
     * @return the negativePoints
     */
    public boolean hasNegativePoints() {
	return negativePoints;
    }
    
    /**
     * Gets the allowed book ids.
     *
     * @return the allowedBookIds
     */
    public ArrayList<Integer> getAllowedBookIds() {
	return allowedBookIds;
    }
    
    /**
     * Sets the title.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
	this.title = title;
    }
    
    /**
     * Sets the exam id.
     *
     * @param examId the new exam id
     */
    public void setExamId(String examId) {
	this.examId = examId;
    }
    
    /**
     * Sets the description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }
    
    /**
     * Sets the start time.
     *
     * @param startTime the startTime to set
     */
    public void setStartTime(long startTime) {
	this.startTime = startTime;
    }
    
    /**
     * Sets the end time.
     *
     * @param endTime the endTime to set
     */
    public void setEndTime(long endTime) {
	this.endTime = endTime;
    }
    
    /**
     * Sets the state.
     *
     * @param state the state to set
     */
    public void setState(String state) {
	this.state = state;
    }
    
    /**
     * Sets the result id.
     *
     * @param resultId the resultId to set
     */
    public void setResultId(long resultId) {
	this.resultId = resultId;
    }
    
    /**
     * Sets the retakeable.
     *
     * @param isRetakeable the isRetakeable to set
     */
    public void setRetakeable(boolean isRetakeable) {
	this.isRetakeable = isRetakeable;
    }
    
    /**
     * Sets the max points.
     *
     * @param maxPoints the maxPoints to set
     */
    public void setMaxPoints(int maxPoints) {
	this.maxPoints = maxPoints;
    }
    
    /**
     * Sets the min points.
     *
     * @param minPoints the minPoints to set
     */
    public void setMinPoints(int minPoints) {
	this.minPoints = minPoints;
    }
    
    /**
     * Sets the negative points.
     *
     * @param hasNegativePoints the new negative points
     */
    public void setNegativePoints(boolean hasNegativePoints) {
	negativePoints = hasNegativePoints;
    }
    
    /**
     * Sets the allowed book ids.
     *
     * @param allowedBookIds the allowedBookIds to set
     */
    public void setAllowedBookIds(ArrayList<Integer> allowedBookIds) {
	this.allowedBookIds = allowedBookIds;
    }
    
    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject() {
	return subject;
    }
    
    /**
     * Sets the subject.
     *
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
	this.subject = subject;
    }

    /**
     * Gets the duration.
     *
     * @return the duration
     */
    public long getDuration() {
	return duration;
    }

    /**
     * Sets the duration.
     *
     * @param duration the duration to set
     */
    public void setDuration(long duration) {
	this.duration = duration;
    }

    /**
     * Checks if is evaluated.
     *
     * @return the evaluated
     */
    public boolean isEvaluated() {
	return evaluated;
    }

    /**
     * Sets the evaluated.
     *
     * @param evaluated the evaluated to set
     */
    public void setEvaluated(boolean evaluated) {
	this.evaluated = evaluated;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ExamDetails [examId=" + examId + ", title=" + title
		+ ", description=" + description + ", subject=" + subject
		+ ", startTime=" + startTime + ", endTime=" + endTime
		+ ", duration=" + duration + ", state=" + state + ", resultId="
		+ resultId + ", isRetakeable=" + isRetakeable + ", maxPoints="
		+ maxPoints + ", minPoints=" + minPoints + ", negativePoints="
		+ negativePoints + ", allowedBookIds=" + allowedBookIds
		+ ", evaluated=" + evaluated + ", uploadedDate=" + uploadedDate
		+ "]";
    }

    /**
     * Gets the uploaded date.
     *
     * @return the uploadedDate
     */
    public long getUploadedDate() {
	return uploadedDate;
    }

    /**
     * Sets the uploaded date.
     *
     * @param uploadedDate the uploadedDate to set
     */
    public void setUploadedDate(long uploadedDate) {
	this.uploadedDate = uploadedDate;
    }
}