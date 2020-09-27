/**
 * 
 */
package com.pearl.user.teacher.examination;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultCount.
 *
 * @author Eng1
 */
public class ResultCount implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The marks awarded. */
    private int marksAwarded;
    
    /** The correct answered count. */
    private int correctAnsweredCount;
    
    /** The wrong answered count. */
    private int wrongAnsweredCount;
    
    /** The unanswered count. */
    private int unansweredCount;
    
    /** The answered count. */
    private int answeredCount;
    
    /**
     * Gets the marks awarded.
     *
     * @return the marksAwarded
     */
    public int getMarksAwarded() {
	return marksAwarded;
    }
    
    /**
     * Sets the marks awarded.
     *
     * @param marksAwarded the marksAwarded to set
     */
    public void setMarksAwarded(int marksAwarded) {
	this.marksAwarded = marksAwarded;
    }
    
    /**
     * Gets the correct answered count.
     *
     * @return the correctAnsweredCount
     */
    public int getCorrectAnsweredCount() {
	return correctAnsweredCount;
    }
    
    /**
     * Sets the correct answered count.
     *
     * @param correctAnsweredCount the correctAnsweredCount to set
     */
    public void setCorrectAnsweredCount(int correctAnsweredCount) {
	this.correctAnsweredCount = correctAnsweredCount;
    }
    
    /**
     * Gets the wrong answered count.
     *
     * @return the wrongAnsweredCount
     */
    public int getWrongAnsweredCount() {
	return wrongAnsweredCount;
    }
    
    /**
     * Sets the wrong answered count.
     *
     * @param wrongAnsweredCount the wrongAnsweredCount to set
     */
    public void setWrongAnsweredCount(int wrongAnsweredCount) {
	this.wrongAnsweredCount = wrongAnsweredCount;
    }
    
    /**
     * Gets the unanswered count.
     *
     * @return the unansweredCount
     */
    public int getUnansweredCount() {
	return unansweredCount;
    }
    
    /**
     * Sets the unanswered count.
     *
     * @param unansweredCount the unansweredCount to set
     */
    public void setUnansweredCount(int unansweredCount) {
	this.unansweredCount = unansweredCount;
    }
    
    /**
     * Gets the answered count.
     *
     * @return the answeredCount
     */
    public int getAnsweredCount() {
	return answeredCount;
    }
    
    /**
     * Sets the answered count.
     *
     * @param answeredCount the answeredCount to set
     */
    public void setAnsweredCount(int answeredCount) {
	this.answeredCount = answeredCount;
    }

}
