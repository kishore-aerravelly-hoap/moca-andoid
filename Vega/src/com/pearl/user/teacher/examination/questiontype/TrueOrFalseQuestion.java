package com.pearl.user.teacher.examination.questiontype;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.gson.annotations.SerializedName;
import com.pearl.user.teacher.examination.AnswerChoice;


// TODO: Auto-generated Javadoc
/**
 * The Class TrueOrFalseQuestion.
 */
public class TrueOrFalseQuestion extends MultipleChoiceQuestion{
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2648133094032687094L;
    
    /** The Student selected choice. */
    @SerializedName("StudentSelectedChoice")
    private AnswerChoice StudentSelectedChoice;

    /**
     * Instantiates a new true or false question.
     */
    public TrueOrFalseQuestion() {
	type = TRUE_OR_FALSE_QUESTION;
	/*
		// Making sure that the choices list does not contain any other choices
		this.choices.removeAll(getChoices());

		AnswerChoice falseChoice = new AnswerChoice();
		falseChoice.setId(0);
		falseChoice.setTitle("False");
		falseChoice.setPosition(2);
		falseChoice.setDescription("");

		// Adding False as an option
		this.choices.add(falseChoice);

		AnswerChoice trueChoice = new AnswerChoice();
		trueChoice.setId(1);
		trueChoice.setTitle("True");
		trueChoice.setPosition(1);
		trueChoice.setDescription("");

		// Adding False as an option
		this.choices.add(trueChoice);*/
    }

    /**
     * Gets the student selected choice.
     *
     * @return the student selected choice
     */
    public AnswerChoice getStudentSelectedChoice(){
	return StudentSelectedChoice;
    }

    /* (non-Javadoc)
     * @see com.pearl.user.teacher.examination.Question#getAnswerXML()
     */
    @Override
    @JsonIgnore
    public String getAnswerXML(){
	String xml = "";


	return xml;
    }

    /**
     * Ignoring setChoice parent method calls by overriding with blank method.
     *
     * @param choices the new choices
     */
    @Override
    public void setChoices(List<AnswerChoice> choices){
	this.choices = choices;
    }

    /**
     * Sets the answer.
     *
     * @param ansChoice the new answer
     */
    public void setAnswer(AnswerChoice ansChoice){
	StudentSelectedChoice = ansChoice;

	studentAnswer.setAnswerString(""+ansChoice.getId());
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