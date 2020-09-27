package com.pearl.examination;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class AnswerChoice.
 */
public class AnswerChoice implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    protected int id;
    
    /** The title. */
    protected String title;
    
    /** The description. */
    protected String description;
    
    /** The position. */
    protected int position;
    
    /** The is selected. */
    protected boolean isSelected = false;
    
    /** The selected. */
    protected boolean selected = false;
    
    /** The teacher selected. */
    protected boolean teacherSelected = false;

    /** The teacher position. */
    protected int teacherPosition;
    
    /** The student position. */
    protected int studentPosition;
    
    /** The match_ field one. */
    protected String match_FieldOne;
    
    /** The match_ field three. */
    protected String match_FieldThree;
    
    /** The match_ field four. */
    protected String match_FieldFour;

    /**
     * Instantiates a new answer choice.
     */
    public AnswerChoice() {

    }

    /**
     * Gets the teacher position.
     *
     * @return the teacherPosition
     */
    public int getTeacherPosition() {
	return teacherPosition;
    }

    /**
     * Sets the teacher position.
     *
     * @param teacherPosition the teacherPosition to set
     */
    public void setTeacherPosition(int teacherPosition) {
	this.teacherPosition = teacherPosition;
    }

    /**
     * Gets the match_ field one.
     *
     * @return the match_FieldOne
     */
    public String getMatch_FieldOne() {
	return match_FieldOne;
    }

    /**
     * Sets the match_ field one.
     *
     * @param match_FieldOne the match_FieldOne to set
     */
    public void setMatch_FieldOne(String match_FieldOne) {
	this.match_FieldOne = match_FieldOne;
    }

    /**
     * Gets the match_ field three.
     *
     * @return the match_FieldThree
     */
    public String getMatch_FieldThree() {
	return match_FieldThree;
    }

    /**
     * Sets the match_ field three.
     *
     * @param match_FieldThree the match_FieldThree to set
     */
    public void setMatch_FieldThree(String match_FieldThree) {
	this.match_FieldThree = match_FieldThree;
    }

    /**
     * Gets the match_ field four.
     *
     * @return the match_FieldFour
     */
    public String getMatch_FieldFour() {
	return match_FieldFour;
    }

    /**
     * Sets the match_ field four.
     *
     * @param match_FieldFour the match_FieldFour to set
     */
    public void setMatch_FieldFour(String match_FieldFour) {
	this.match_FieldFour = match_FieldFour;
    }

    /**
     * Instantiates a new answer choice.
     *
     * @param id the id
     * @param title the title
     * @param description the description
     * @param position the position
     */
    public AnswerChoice(int id, String title, String description, int position) {
	this.id = id;
	this.title = title;
	this.description = description;
	this.position = position;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
	return id;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * Gets the position.
     *
     * @return the position
     */
    public int getPosition() {
	return position;
    }

    /**
     * Sets the id.
     *
     * @param id the id to set
     */
    public void setId(int id) {
	this.id = id;
    }

    /**
     * Sets the title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * Sets the description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Sets the position.
     *
     * @param position the position to set
     */
    public void setPosition(int position) {
	this.position = position;
    }

    /**
     * To xml.
     *
     * @return the string
     */
    public String toXML() {
	String xml = "";

	xml += "<answer>";
	xml += "	<id>" + id + "</id>";
	xml += "	<description>" + title + "</description>";
	xml += "	<explanation>" + title + "</explanation>";
	xml += "	<selected>" + (isSelected() ? "true" : "false")
		+ "</selected>";
	xml += "	<position>" + position + "</position>";
	xml += "</answer>";

	return xml;
    }

    /**
     * Checks if is teacher selected.
     *
     * @return the isSelected
     */
    /*
     * public boolean isSelected() { return isSelected; }
     *//**
     * @param isSelected
     *            the isSelected to set
     */
    /*
     * public void setSelected(boolean isSelected) { this.isSelected =
     * isSelected; }
     */

    /**
     * @return the teacherSelected
     */
    public boolean isTeacherSelected() {
	return teacherSelected;
    }

    /**
     * Sets the teacher selected.
     *
     * @param teacherSelected the teacherSelected to set
     */
    public void setTeacherSelected(boolean teacherSelected) {
	this.teacherSelected = teacherSelected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "AnswerChoice [id=" + id + ", title=" + title + ", description="
		+ description + ", position=" + position + ", isSelected="
		+ isSelected + ", selected=" + selected + ", teacherSelected="
		+ teacherSelected + ", teacherPosition=" + teacherPosition
		+ ", match_FieldOne=" + match_FieldOne + ", match_FieldThree="
		+ match_FieldThree + ", match_FieldFour=" + match_FieldFour
		+ "]";
    }

    /**
     * Checks if is selected.
     *
     * @return the selected
     */
    public boolean isSelected() {
	return selected;
    }

    /**
     * Sets the selected.
     *
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
	this.selected = selected;
    }
}