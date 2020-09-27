package com.pearl.user.teacher.examination.questiontype;

import java.util.List;

import com.pearl.user.teacher.examination.AnswerChoice;

// TODO: Auto-generated Javadoc
/**
 * The Class OrderingQuestion.
 */
public class OrderingQuestion extends MultipleChoiceQuestion {
    
    /** The is dnd. */
    private final boolean isDnd = false;
    
    /** The student ordered answer. */
    private List<AnswerChoice> studentOrderedAnswer;

    /**
     * Instantiates a new ordering question.
     */
    public OrderingQuestion() {
	type = ORDERING_QUESTION;
    }

    /**
     * Gets the student ordered answer choices.
     *
     * @return the student ordered answer choices
     */
    public List<AnswerChoice> getStudentOrderedAnswerChoices(){
	return studentOrderedAnswer;
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

    /**
     * Sets the answer.
     *
     * @param choices the new answer
     */
    public void setAnswer(List<AnswerChoice> choices) {
	studentOrderedAnswer = choices;

	String comaSepAnswerIds = "";

	for (final AnswerChoice answerChoice : choices) {
	    if (!"".equals(comaSepAnswerIds)) {
		comaSepAnswerIds += ", ";
	    }

	    comaSepAnswerIds += answerChoice.getId();
	}

	studentAnswer.setAnswerString(comaSepAnswerIds);
    }

    /* (non-Javadoc)
     * @see com.pearl.user.teacher.examination.Question#getAnswerXML()
     */
    @Override
    public String getAnswerXML(){
	String xml = "";

	if(null != studentOrderedAnswer){
	    xml += "<answered>true</answered>";
	    for (final AnswerChoice fact : studentOrderedAnswer) {
		xml += fact.toXML();
	    }
	}else{
	    xml += "<answered>false</answered>";
	    for (final AnswerChoice fact : choices) {
		xml += fact.toXML();
	    }
	}

	return xml;
    }
}