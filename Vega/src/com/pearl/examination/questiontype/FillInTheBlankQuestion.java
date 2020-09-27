package com.pearl.examination.questiontype;

import java.util.ArrayList;
import java.util.List;

import com.pearl.examination.Question;

// TODO: Auto-generated Javadoc
/**
 * The Class FillInTheBlankQuestion.
 */
public class FillInTheBlankQuestion extends Question {
    
    /** The hint answers. */
    protected List<String> hintAnswers;

    /** The total blanks. */
    protected int totalBlanks = 0;
    
    /** The answers. */
    protected List<String> answers;

    /**
     * Instantiates a new fill in the blank question.
     */
    public FillInTheBlankQuestion() {
	type = FILL_IN_THE_BLANK_QUESTION;

	answers = new ArrayList<String>();
    }

    /* (non-Javadoc)
     * @see com.pearl.examination.Question#setQuestion(java.lang.String)
     */
    @Override
    public void setQuestion(String ques) {
	super.setQuestion(ques);
	totalBlanks = calculateTotalBlanks();

	for (int i = 0; i < totalBlanks; i++) {
	    answers.add("");
	}
    }

    /**
     * Calculate total blanks.
     *
     * @return the int
     */
    protected int calculateTotalBlanks() {
	int start = 0;
	int end = question.indexOf("__", start);
	int count = 0;

	while (end >= 0) {
	    count++;

	    start = end + 2;
	    if (start <= question.length()) {
		end = question.indexOf("__", start);
	    }
	}

	return count;
    }

    /**
     * Sets the hint answers.
     *
     * @param hints the new hint answers
     */
    public void setHintAnswers(List<String> hints) {
	hintAnswers = hints;
    }

    /**
     * Gets the hint answers.
     *
     * @return the hint answers
     */
    public List<String> getHintAnswers() {
	return hintAnswers;
    }

    /**
     * Gets the answers.
     *
     * @return the answers
     */
    public List<String> getAnswers() {
	return answers;
    }

    /**
     * Sets the answers.
     *
     * @param answers the answers to set
     */
    public void setAnswers(List<String> answers) {
	this.answers = answers;
    }

    /**
     * Sets the answer at.
     *
     * @param pos the pos
     * @param answer the answer
     */
    public void setAnswerAt(int pos, String answer) {
	if (pos < answers.size()) {
	    answers.set(pos, answer);
	} else {
	    answers.add(answer);
	}
    }

    /**
     * Gets the answer at.
     *
     * @param pos the pos
     * @return the answer at
     */
    public String getAnswerAt(int pos) {
	return this.getAnswers().get(pos);
    }

    /**
     * Gets the total blanks.
     *
     * @return the totalBlanks
     */
    public int getTotalBlanks() {
	return totalBlanks;
    }

    /**
     * Sets the total blanks.
     *
     * @param totalBlanks the totalBlanks to set
     */
    public void setTotalBlanks(int totalBlanks) {
	this.totalBlanks = totalBlanks;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "FillInTheBlankQuestion [hintAnswers=" + hintAnswers
		+ ", totalBlanks=" + totalBlanks + ", answers=" + answers
		+ ", calculateTotalBlanks()=" + calculateTotalBlanks()
		+ ", getClass()=" + getClass() + "]";
    }
}