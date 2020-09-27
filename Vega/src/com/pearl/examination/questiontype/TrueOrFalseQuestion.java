package com.pearl.examination.questiontype;

import java.util.ArrayList;
import java.util.List;

import com.pearl.examination.AnswerChoice;

// TODO: Auto-generated Javadoc
/**
 * The Class TrueOrFalseQuestion.
 */
public class TrueOrFalseQuestion extends MultipleChoiceQuestion {
    
    /** The Student selected choice. */
    private AnswerChoice StudentSelectedChoice;

    /**
     * Instantiates a new true or false question.
     */
    public TrueOrFalseQuestion() {
	type = TRUE_OR_FALSE_QUESTION;

	choices = new ArrayList<AnswerChoice>();
	/*
	 * // Making sure that the choices list does not contain any other
	 * choices this.choices.removeAll(getChoices());
	 * 
	 * AnswerChoice falseChoice = new AnswerChoice(); falseChoice.setId(0);
	 * falseChoice.setTitle("False"); falseChoice.setPosition(2);
	 * falseChoice.setDescription("");
	 * 
	 * // Adding False as an option this.choices.add(falseChoice);
	 * 
	 * AnswerChoice trueChoice = new AnswerChoice(); trueChoice.setId(1);
	 * trueChoice.setTitle("True"); trueChoice.setPosition(1);
	 * trueChoice.setDescription("");
	 * 
	 * // Adding False as an option this.choices.add(trueChoice);
	 */
    }

    /* (non-Javadoc)
     * @see com.pearl.examination.questiontype.MultipleChoiceQuestion#getStudentSelectedChoice()
     */
    @Override
    public AnswerChoice getStudentSelectedChoice() {
	return StudentSelectedChoice;
    }

    /* (non-Javadoc)
     * @see com.pearl.examination.Question#getAnswerXML()
     */
    @Override
    public String getAnswerXML() {
	String xml = "";

	if (null != choices) {
	    for (final AnswerChoice fact : choices) {
		if (null != StudentSelectedChoice
			&& StudentSelectedChoice.getId() == fact.getId()) {
		    fact.setSelected(true);
		}

		xml += fact.toXML();
	    }
	} else {
	    xml = "<answer></answer>";
	}

	return xml;
    }

    /**
     * Ignoring setChoice parent method calls by overriding with blank method.
     *
     * @param choices the new choices
     */
    @Override
    public void setChoices(List<AnswerChoice> choices) {
	this.choices = choices;
    }

    /* (non-Javadoc)
     * @see com.pearl.examination.questiontype.MultipleChoiceQuestion#setAnswer(com.pearl.examination.AnswerChoice)
     */
    @Override
    public void setAnswer(AnswerChoice ansChoice) {
	StudentSelectedChoice = ansChoice;

	studentAnswer.setAnswerString("" + ansChoice.getId());
    }

    /**
     * Sets the student selected choice.
     *
     * @param studentSelectedChoice the studentSelectedChoice to set
     */
    public void setStudentSelectedChoice(AnswerChoice studentSelectedChoice) {
	StudentSelectedChoice = studentSelectedChoice;
    }
}