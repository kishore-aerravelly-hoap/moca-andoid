package com.pearl.examination.answertype;

import com.pearl.examination.AnswerChoice;

// TODO: Auto-generated Javadoc
/**
 * The Class OrderingAnswerChoice.
 */
public class OrderingAnswerChoice extends AnswerChoice {
    
    /** The teacher position. */
    protected int teacherPosition;

    /**
     * Gets the teacher position.
     *
     * @return the teacherPosition
     */
    @Override
    public int getTeacherPosition() {
	return teacherPosition;
    }

    /**
     * Sets the teacher position.
     *
     * @param teacherPosition the teacherPosition to set
     */
    @Override
    public void setTeacherPosition(int teacherPosition) {
	this.teacherPosition = teacherPosition;
    }
}