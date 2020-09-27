package com.pearl.analytics.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class GradePassFailPercentage.
 */
public class GradePassFailPercentage implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The grade. */
	private String grade;
	
	/** The subject. */
	private String subject;
	
	/** The section. */
	private String section;
	
	/** The exam type. */
	private String examType;
	
	/** The student percentage. */
	private String studentPercentage;
	
	/** The pass percentage. */
	private String passPercentage;
	
	/** The fail percentage. */
	private String failPercentage;
	
	/** The student count. */
	private String studentCount;
	
	/** The year. */
	private String year;
	
	/**
	 * Gets the year.
	 *
	 * @return the year
	 */
	public String getYear() {
		return year;
	}
	
	/**
	 * Sets the year.
	 *
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
	
	/**
	 * Gets the exam type.
	 *
	 * @return the exam type
	 */
	public String getExamType() {
		return examType;
	}
	
	/**
	 * Sets the exam type.
	 *
	 * @param examType the new exam type
	 */
	public void setExamType(String examType) {
		this.examType = examType;
	}
	
	/**
	 * Gets the student count.
	 *
	 * @return the student count
	 */
	public String getStudentCount() {
		return studentCount;
	}
	
	/**
	 * Sets the student count.
	 *
	 * @param studentCount the new student count
	 */
	public void setStudentCount(String studentCount) {
		this.studentCount = studentCount;
	}
	
	/**
	 * Gets the student percentage.
	 *
	 * @return the student percentage
	 */
	public String getStudentPercentage() {
		return studentPercentage;
	}
	
	/**
	 * Sets the student percentage.
	 *
	 * @param studentPercentage the new student percentage
	 */
	public void setStudentPercentage(String studentPercentage) {
		this.studentPercentage = studentPercentage;
	}
	
	/**
	 * Gets the pass percentage.
	 *
	 * @return the pass percentage
	 */
	public String getPassPercentage() {
		return passPercentage;
	}
	
	/**
	 * Sets the pass percentage.
	 *
	 * @param passPercentage the new pass percentage
	 */
	public void setPassPercentage(String passPercentage) {
		this.passPercentage = passPercentage;
	}
	
	/**
	 * Gets the fail percentage.
	 *
	 * @return the fail percentage
	 */
	public String getFailPercentage() {
		return failPercentage;
	}
	
	/**
	 * Sets the fail percentage.
	 *
	 * @param failPercentage the new fail percentage
	 */
	public void setFailPercentage(String failPercentage) {
		this.failPercentage = failPercentage;
	}
	
	/**
	 * Gets the grade.
	 *
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}
	
	/**
	 * Sets the grade.
	 *
	 * @param grade the new grade
	 */
	public void setGrade(String grade) {
		this.grade = grade;
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
	 * Gets the section.
	 *
	 * @return the section
	 */
	public String getSection() {
		return section;
	}
	
	/**
	 * Sets the section.
	 *
	 * @param section the new section
	 */
	public void setSection(String section) {
		this.section = section;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GradePassFailPercentage [grade=" + grade + ", subject="
				+ subject + ", section=" + section + ", examType=" + examType
				+ ", studentPercentage=" + studentPercentage
				+ ", passPercentage=" + passPercentage + ", failPercentage="
				+ failPercentage + ", studentCount=" + studentCount + "]";
	}
	
	
}
