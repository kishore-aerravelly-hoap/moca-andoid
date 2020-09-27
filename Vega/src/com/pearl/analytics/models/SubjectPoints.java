package com.pearl.analytics.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class SubjectPoints.
 */
public class SubjectPoints implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The subject. */
	private String subject;
	
	/** The total marks. */
	private double totalMarks;
	
	/** The student name. */
	private String studentName;
	
	/** The test wise marks. */
	private double testWiseMarks;
	
	/** The marks. */
	private double marks;
	
	/** The count. */
	private int count;
	
	/** The range. */
	private String range;

	/**
	 * Gets the marks.
	 *
	 * @return the marks
	 */
	public double getMarks() {
		return marks;
	}

	/**
	 * Sets the marks.
	 *
	 * @param marks the new marks
	 */
	public void setMarks(double marks) {
		this.marks = marks;
	}

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
	 * @param count the new count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Gets the student name.
	 *
	 * @return the student name
	 */
	public String getStudentName() {
		return studentName;
	}

	/**
	 * Sets the student name.
	 *
	 * @param studentName the new student name
	 */
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	
	/**
	 * Gets the test wise marks.
	 *
	 * @return the test wise marks
	 */
	public double getTestWiseMarks() {
		return testWiseMarks;
	}

	/**
	 * Sets the test wise marks.
	 *
	 * @param testWiseMarks the new test wise marks
	 */
	public void setTestWiseMarks(double testWiseMarks) {
		this.testWiseMarks = testWiseMarks;
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
	 * @param subject the new subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Gets the total marks.
	 *
	 * @return the total marks
	 */
	public double getTotalMarks() {
		return totalMarks;
	}

	/**
	 * Sets the total marks.
	 *
	 * @param totalMarks the new total marks
	 */
	public void setTotalMarks(Double totalMarks) {
		this.totalMarks = totalMarks;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubjectPoints [subject=" + subject + ", totalMarks="
				+ totalMarks + ", studentName=" + studentName
				+ ", testWiseMarks=" + testWiseMarks + ", marks=" + marks
				+ ", count=" + count + "]";
	}

	/**
	 * Gets the range.
	 *
	 * @return the range
	 */
	public String getRange() {
		return range;
	}

	/**
	 * Sets the range.
	 *
	 * @param range the range to set
	 */
	public void setRange(String range) {
		this.range = range;
	}
	

}
