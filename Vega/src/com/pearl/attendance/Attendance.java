package com.pearl.attendance;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.pearl.student.Student;
import com.pearl.ui.models.PearlConfigKeyValues;

// TODO: Auto-generated Javadoc
/**
 * The Class Attendance.
 *
 * @author Samreen
 */
public class Attendance {
    
    /** The grade. */
    private String grade;
    
    /** The section. */
    private String section;
    
    /** The session. */
    private String session;
    
    /** The period. */
    private String period;
    
    /** The strength. */
    @JsonIgnore
    private int strength;
    
    /** The present. */
    @JsonIgnore
    private int present;
    
    /** The absent. */
    @JsonIgnore
    private int absent;
    
    /** The date. */
    @JsonIgnore
    private String date;

    /** The studentlist. */
    private List<Student> studentlist;

    /** The config values. */
    private List<PearlConfigKeyValues> configValues;

    /**
     * Instantiates a new attendance.
     */
    public Attendance() {
	super();
    }

    /**
     * Gets the studentlist.
     *
     * @return the studentlist
     */
    public List<Student> getStudentlist() {
	return studentlist;
    }

    /**
     * Sets the studentlist.
     *
     * @param studentlist the new studentlist
     */
    public void setStudentlist(List<Student> studentlist) {
	this.studentlist = studentlist;
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
     * Gets the date.
     *
     * @return the date
     */
    public String getDate() {
	return date;
    }

    /**
     * Sets the date.
     *
     * @param date the new date
     */
    public void setDate(String date) {
	this.date = date;
    }

    /**
     * Gets the strength.
     *
     * @return the strength
     */
    public int getStrength() {
	return strength;
    }

    /**
     * Sets the strength.
     *
     * @param strength the new strength
     */
    public void setStrength(int strength) {
	this.strength = strength;
    }

    /**
     * Gets the present.
     *
     * @return the present
     */
    public int getPresent() {
	return present;
    }

    /**
     * Sets the present.
     *
     * @param present the new present
     */
    public void setPresent(int present) {
	this.present = present;
    }

    /**
     * Gets the absent.
     *
     * @return the absent
     */
    public int getAbsent() {
	return absent;
    }

    /**
     * Sets the absent.
     *
     * @param absent the new absent
     */
    public void setAbsent(int absent) {
	this.absent = absent;
    }

    /**
     * Gets the config values.
     *
     * @return the config values
     */
    public List<PearlConfigKeyValues> getConfigValues() {
	return configValues;
    }

    /**
     * Sets the config values.
     *
     * @param configValues the new config values
     */
    public void setConfigValues(List<PearlConfigKeyValues> configValues) {
	this.configValues = configValues;
    }

    /**
     * Gets the session.
     *
     * @return the session
     */
    public String getSession() {
	return session;
    }

    /**
     * Sets the session.
     *
     * @param session the new session
     */
    public void setSession(String session) {
	this.session = session;
    }

    /**
     * Gets the period.
     *
     * @return the period
     */
    public String getPeriod() {
	return period;
    }

    /**
     * Sets the period.
     *
     * @param period the new period
     */
    public void setPeriod(String period) {
	this.period = period;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Attendance [absent=" + absent + ", configValues="
		+ configValues + ", date=" + date + ", grade=" + grade
		+ ", present=" + present + ", setion=" + section
		+ ", strength=" + strength + ", studentlist=" + studentlist
		+ "]";
    }
}
