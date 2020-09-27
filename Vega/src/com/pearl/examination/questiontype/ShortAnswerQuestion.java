package com.pearl.examination.questiontype;

import com.pearl.examination.Question;

// TODO: Auto-generated Javadoc
/**
 * The Class ShortAnswerQuestion.
 */
public class ShortAnswerQuestion extends Question {
    
    /** The hints. */
    protected String hints = "";

    /**
     * Instantiates a new short answer question.
     */
    public ShortAnswerQuestion() {
	type = SHORT_ANSWER_QUESTION;
    }

    /**
     * Sets the hints.
     *
     * @param hints the hints
     * @return the short answer question
     */
    public ShortAnswerQuestion setHints(String hints) {
	this.hints = hints;

	return this;
    }

    /**
     * Gets the hints.
     *
     * @return the hints
     */
    public String getHints() {
	return hints;
    }
}