package com.pearl.user.teacher.examination;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class MatchTheFollowingQuestionAnswer.
 */
public class MatchTheFollowingQuestionAnswer extends MultiChoiceQuestionAnswer
implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;


    /** The checked string. */
    private String checkedString;
    
    /** The checked text. */
    private String checkedText;
    
    /**
     * Gets the checked string.
     *
     * @return the checkedString
     */
    @Override
    public String getCheckedString() {
	return checkedString;
    }
    
    /**
     * Sets the checked string.
     *
     * @param checkedString the checkedString to set
     */
    @Override
    public void setCheckedString(String checkedString) {
	this.checkedString = checkedString;
    }
    
    /**
     * Gets the checked text.
     *
     * @return the checkedText
     */
    @Override
    public String getCheckedText() {
	return checkedText;
    }
    
    /**
     * Sets the checked text.
     *
     * @param checkedText the checkedText to set
     */
    @Override
    public void setCheckedText(String checkedText) {
	this.checkedText = checkedText;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "MatchTheFollowingQuestionAnswer [checkedString="
		+ checkedString + ", checkedText=" + checkedText
		+ ", getAnswerDes()=" + getAnswerDes() + ", getPosition()="
		+ getPosition() + ", getCheckedValue()=" + getCheckedValue()
		+ ", toString()=" + super.toString() + ", getClass()="
		+ getClass() + ", hashCode()=" + hashCode() + "]";
    }


}
