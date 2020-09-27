package com.pearl.analytics.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class AndroidAnalytics.
 */
public class AndroidAnalytics implements Serializable{
 
 /** The Constant serialVersionUID. */
	
	private static final long serialVersionUID = 1L;
	
	/** The count. */
	private long count;
	
	/** The max marks. */
	private double maxMarks;
	
	/** The subject points. */
	List<SubjectPoints> subjectPoints = new ArrayList<SubjectPoints>();
	
	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public long getCount() {
		return count;
	}
	
	/**
	 * Sets the count.
	 *
	 * @param count the count to set
	 */
	public void setCount(long count) {
		this.count = count;
	}
	
	/**
	 * Gets the max marks.
	 *
	 * @return the maxMarks
	 */
	public double getMaxMarks() {
		return maxMarks;
	}
	
	/**
	 * Sets the max marks.
	 *
	 * @param maxMarks the maxMarks to set
	 */
	public void setMaxMarks(double maxMarks) {
		this.maxMarks = maxMarks;
	}
	
	/**
	 * Gets the subject points.
	 *
	 * @return the subjectPoints
	 */
	public List<SubjectPoints> getSubjectPoints() {
		return subjectPoints;
	}
	
	/**
	 * Sets the subject points.
	 *
	 * @param subjectPoints the subjectPoints to set
	 */
	public void setSubjectPoints(List<SubjectPoints> subjectPoints) {
		this.subjectPoints = subjectPoints;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AndroidDashBoard [count=" + count + ", maxMarks=" + maxMarks
				+ ", subjectPoints=" + subjectPoints + "]";
	}
	
}
