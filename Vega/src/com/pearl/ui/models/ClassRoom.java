package com.pearl.ui.models;

import java.io.Serializable;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class ClassRoom.
 */
public class ClassRoom implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The grade. */
    private String grade;
    
    /** The subject. */
    private String subject;
    
    /** The sections. */
    private List<String> sections;
    
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
     * Gets the sections.
     *
     * @return the sections
     */
    public List<String> getSections() {
	return sections;
    }
    
    /**
     * Sets the sections.
     *
     * @param sections the sections to set
     */
    public void setSections(List<String> sections) {
	this.sections = sections;
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
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ClassRoom [grade=" + grade + ", subject=" + subject
		+ ", sections=" + sections + "]";
    }

}


