package com.pearl.analytics.models;

import java.io.Serializable;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class StudentPercentage.
 */
public class StudentPercentage implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The student id. */
	private String studentId;
	
	/** The section. */
	private String section;
	
	/** The exam type. */
	private String examType;
	
	/** The subjects. */
	private List<String> subjects;
	
	/** The exam types. */
	private List<String> examTypes;
	
	/** The percentages. */
	private List<String> percentages;
	
	/** The grade. */
	private String grade;
	
	/** The subject. */
	String subject;
	
	/** The years. */
	private List<String> years;
	
	
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
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	/**
	 * Gets the years.
	 *
	 * @return the years
	 */
	public List<String> getYears() {
		return years;
	}
	
	/**
	 * Sets the years.
	 *
	 * @param years the years to set
	 */
	public void setYears(List<String> years) {
		this.years = years;
	}
	
	/**
	 * Gets the student id.
	 *
	 * @return the student id
	 */
	public String getStudentId() {
		return studentId;
	}
	
	/**
	 * Sets the student id.
	 *
	 * @param studentId the new student id
	 */
	public void setStudentId(String studentId) {
		this.studentId = studentId;
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
	 * Gets the subjects.
	 *
	 * @return the subjects
	 */
	public List<String> getSubjects() {
		return subjects;
	}
	
	/**
	 * Sets the subjects.
	 *
	 * @param subjects the new subjects
	 */
	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}
	
	/**
	 * Gets the exam types.
	 *
	 * @return the exam types
	 */
	public List<String> getExamTypes() {
		return examTypes;
	}
	
	/**
	 * Sets the exam types.
	 *
	 * @param examTypes the new exam types
	 */
	public void setExamTypes(List<String> examTypes) {
		this.examTypes = examTypes;
	}
	
	/**
	 * Gets the percentages.
	 *
	 * @return the percentages
	 */
	public List<String> getPercentages() {
		return percentages;
	}
	
	/**
	 * Sets the percentages.
	 *
	 * @param percentages the new percentages
	 */
	public void setPercentages(List<String> percentages) {
		this.percentages = percentages;
	}
	
	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StudentPercentage [studentId=" + studentId + ", section="
				+ section + ", examType=" + examType + ", subjects=" + subjects
				+ ", examTypes=" + examTypes + ", percentages=" + percentages
				+ "]";
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
	
	
	
	
}
