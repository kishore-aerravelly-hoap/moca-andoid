package com.pearl.examination.questiontype;

import java.util.ArrayList;
import java.util.List;

import com.pearl.examination.AnswerChoice;
import com.pearl.examination.Question;

// TODO: Auto-generated Javadoc
/**
 * The Class MultipleChoiceQuestion.
 */
public class MultipleChoiceQuestion extends Question {
    
    /** The choices. */
    protected List<AnswerChoice> choices;
    
    /** The has multiple answers. */
    protected boolean hasMultipleAnswers = false;
    
    /** The Student selected choice. */
    protected AnswerChoice StudentSelectedChoice;

    /**
     * Instantiates a new multiple choice question.
     */
    public MultipleChoiceQuestion() {
	type = MULTIPLECHOICE_QUESTION;

	choices = new ArrayList<AnswerChoice>();
    }

    /* (non-Javadoc)
     * @see com.pearl.examination.Question#setChoices(java.util.List)
     */
    @Override
    public void setChoices(List<AnswerChoice> choices) {
	this.choices = choices;
    }

    /* (non-Javadoc)
     * @see com.pearl.examination.Question#getChoices()
     */
    @Override
    public List<AnswerChoice> getChoices() {
	return choices;
    }

    /**
     * Gets the student selected choice.
     *
     * @return the student selected choice
     */
    public AnswerChoice getStudentSelectedChoice() {
	return StudentSelectedChoice;
    }

    /**
     * Sets the answer.
     *
     * @param ansChoice the new answer
     */
    public void setAnswer(AnswerChoice ansChoice) {
	StudentSelectedChoice = ansChoice;

	studentAnswer.setAnswerString("" + ansChoice.getId());
    }

    /* (non-Javadoc)
     * @see com.pearl.examination.Question#toString()
     */
    @Override
    public String toString() {
	String str = "";

	str += "Question:" + question + " | Choices:";

	for (final AnswerChoice choice : choices) {
	    str += choice.toString();
	}

	return str;
    }

    /**
     * Gets the checks for multiple answers.
     *
     * @return the hasMultipleAnswers
     */
    public boolean getHasMultipleAnswers() {
	return hasMultipleAnswers;
    }

    /**
     * Sets the checks for multiple answers.
     *
     * @param hasMultipleAnswers the hasMultipleAnswers to set
     */
    public void setHasMultipleAnswers(boolean hasMultipleAnswers) {
	this.hasMultipleAnswers = hasMultipleAnswers;
    }
}