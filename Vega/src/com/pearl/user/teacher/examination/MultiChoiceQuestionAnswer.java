/**
 * 
 */
package com.pearl.user.teacher.examination;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class MultiChoiceQuestionAnswer.
 *
 * @author ENG2
 */
public class MultiChoiceQuestionAnswer implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The checked value. */
    private String checkedValue;
    
    /** The answer des. */
    private String answerDes;
    
    /** The position. */
    private int position;

    // Added for match the following
    /** The checked string. */
    private String checkedString;
    
    /** The checked text. */
    private String checkedText;


    /**
     * Gets the answer des.
     *
     * @return the answerDes
     */
    public String getAnswerDes() {
	return answerDes;
    }
    
    /**
     * Sets the answer des.
     *
     * @param answerDes the answerDes to set
     */
    public void setAnswerDes(String answerDes) {
	this.answerDes = answerDes;
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
     * Sets the position.
     *
     * @param position the position to set
     */
    public void setPosition(int position) {
	this.position = position;
    }
    
    /**
     * Gets the checked value.
     *
     * @return the checkedValue
     */
    public String getCheckedValue() {
	return checkedValue;
    }
    
    /**
     * Sets the checked value.
     *
     * @param checkedValue the checkedValue to set
     */
    public void setCheckedValue(String checkedValue) {
	this.checkedValue = checkedValue;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "MultiChoiceQuestionAnswer [checkedValue=" + checkedValue
		+ ", answerDes=" + answerDes + ", position=" + position
		+ ", checkedString=" + checkedString + ", checkedText="
		+ checkedText + "]";
    }
    
    /**
     * Gets the checked string.
     *
     * @return the checkedString
     */
    public String getCheckedString() {
	return checkedString;
    }
    
    /**
     * Sets the checked string.
     *
     * @param checkedString the checkedString to set
     */
    public void setCheckedString(String checkedString) {
	this.checkedString = checkedString;
    }
    
    /**
     * Gets the checked text.
     *
     * @return the checkedText
     */
    public String getCheckedText() {
	return checkedText;
    }
    
    /**
     * Sets the checked text.
     *
     * @param checkedText the checkedText to set
     */
    public void setCheckedText(String checkedText) {
	this.checkedText = checkedText;
    }

}
