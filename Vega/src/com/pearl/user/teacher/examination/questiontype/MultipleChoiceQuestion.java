package com.pearl.user.teacher.examination.questiontype;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.pearl.user.teacher.examination.AnswerChoice;
import com.pearl.user.teacher.examination.Question;


// TODO: Auto-generated Javadoc
/**
 * The Class MultipleChoiceQuestion.
 */
public class MultipleChoiceQuestion extends Question{
    
    /** The choices. */
    @SerializedName("choices")
    protected List<AnswerChoice> choices;
    
    /** The has multiple answers. */
    @SerializedName("hasMultipleAnswers")
    protected boolean hasMultipleAnswers = false;

    /**
     * Instantiates a new multiple choice question.
     */
    public MultipleChoiceQuestion(){
	type = MULTIPLECHOICE_QUESTION;

	choices = new ArrayList();
    }

    /**
     * Sets the choices.
     *
     * @param choices the new choices
     */
    public void setChoices(List<AnswerChoice> choices){
	this.choices = choices;
    }

    /**
     * Gets the choices.
     *
     * @return the choices
     */
    public List<AnswerChoice> getChoices(){
	return choices;
    }

    /**
     * Checks if is checks for multiple answers.
     *
     * @return the hasMultipleAnswers
     */
    public boolean isHasMultipleAnswers() {
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