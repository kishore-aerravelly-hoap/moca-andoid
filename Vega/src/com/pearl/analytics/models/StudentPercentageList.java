package com.pearl.analytics.models;

import java.io.Serializable;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class StudentPercentageList.
 */
public class StudentPercentageList implements Serializable {

	/** The subject count. */
	private int subjectCount;
	
	/** The exam type count. */
	private int examTypeCount;
	
	/** The student percentages. */
	private List<StudentPercentage> studentPercentages;
	
	/** The total subject list. */
	private List<String> totalSubjectList;
	
	/**
	 * Gets the subject count.
	 *
	 * @return the subject count
	 */
	public int getSubjectCount() {
		return subjectCount;
	}
	
	/**
	 * Sets the subject count.
	 *
	 * @param subjectCount the new subject count
	 */
	public void setSubjectCount(int subjectCount) {
		this.subjectCount = subjectCount;
	}
	
	/**
	 * Gets the exam type count.
	 *
	 * @return the exam type count
	 */
	public int getExamTypeCount() {
		return examTypeCount;
	}
	
	/**
	 * Sets the exam type count.
	 *
	 * @param examTypeCount the new exam type count
	 */
	public void setExamTypeCount(int examTypeCount) {
		this.examTypeCount = examTypeCount;
	}
	
	/**
	 * Gets the student percentages.
	 *
	 * @return the student percentages
	 */
	public List<StudentPercentage> getStudentPercentages() {
		return studentPercentages;
	}
	
	/**
	 * Sets the student percentages.
	 *
	 * @param studentPercentages the new student percentages
	 */
	public void setStudentPercentages(List<StudentPercentage> studentPercentages) {
		this.studentPercentages = studentPercentages;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StudentPercentageDetails [subjectCount=" + subjectCount
				+ ", examTypeCount=" + examTypeCount + ", studentPercentages="
				+ studentPercentages + "]";
	}
	
	/**
	 * Gets the total subject list.
	 *
	 * @return the total subject list
	 */
	public List<String> getTotalSubjectList() {
		return totalSubjectList;
	}
	
	/**
	 * Sets the total subject list.
	 *
	 * @param totalSubjectList the new total subject list
	 */
	public void setTotalSubjectList(List<String> totalSubjectList) {
		this.totalSubjectList = totalSubjectList;
	}
	
	
}
