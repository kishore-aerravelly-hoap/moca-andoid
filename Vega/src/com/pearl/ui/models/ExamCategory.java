package com.pearl.ui.models;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamCategory.
 */
public class ExamCategory {

	/** The exam type. */
	private String examType;
	
	/** The final type. */
	private boolean finalType;
	
	/**
	 * Checks if is final type.
	 *
	 * @return true, if is final type
	 */
	public synchronized boolean isFinalType() {
		return finalType;
	}
	
	/**
	 * Sets the final type.
	 *
	 * @param finalType the new final type
	 */
	public synchronized void setFinalType(boolean finalType) {
		this.finalType = finalType;
	}
	
	/** The id. */
	private String id;
	
	/**
	 * Gets the exam type.
	 *
	 * @return the examType
	 */
	public String getExamType() {
		return examType;
	}
	
	/**
	 * Sets the exam type.
	 *
	 * @param examType the examType to set
	 */
	public void setExamType(String examType) {
		this.examType = examType;
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
	
}
