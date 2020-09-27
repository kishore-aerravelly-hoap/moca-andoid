/**
 * 
 */
package com.pearl.ui.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class AcadamicBackGroundInformation.
 */
public class AcadamicBackGroundInformation implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id. */
    private String id;
    
    /** The school name. */
    private String schoolName;
    
    /** The school year. */
    private String schoolYear;
    
    /** The action. */
    private String action;

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

    /**
     * Gets the school year.
     *
     * @return the schoolYear
     */
    public String getSchoolYear() {
	return schoolYear;
    }

    /**
     * Sets the school year.
     *
     * @param schoolYear the schoolYear to set
     */
    public void setSchoolYear(String schoolYear) {
	this.schoolYear = schoolYear;
    }

    /**
     * Gets the action.
     *
     * @return the action
     */
    public String getAction() {
	return action;
    }

    /**
     * Sets the action.
     *
     * @param action the action to set
     */
    public void setAction(String action) {
	this.action = action;
    }

    /**
     * Gets the school name.
     *
     * @return the schoolName
     */
    public String getSchoolName() {
	return schoolName;
    }

    /**
     * Sets the school name.
     *
     * @param schoolName the schoolName to set
     */
    public void setSchoolName(String schoolName) {
	this.schoolName = schoolName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "AcadamicBackGroundInformation [id=" + id + ", schoolName="
		+ schoolName + ", schoolYear=" + schoolYear + ", action="
		+ action + "]";
    }

}
