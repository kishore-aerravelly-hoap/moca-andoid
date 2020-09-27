package com.pearl.examination;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

// TODO: Auto-generated Javadoc
/**
 * The Class Answer.
 */
public class Answer {
    
    /** The answer string. */
    protected String answerString;
    
    /** The choices. */
    List<AnswerChoice> choices;

    /**
     * Sets the answer string.
     *
     * @param ans the new answer string
     * @return the answerAsString
     */
    public void setAnswerString(String ans) {
	answerString = ans;
    }

    /**
     * Gets the answer string.
     *
     * @return the answer string
     */
    public String getAnswerString() {
	return answerString;
    }

    /**
     * Gets the choices.
     *
     * @return the choices
     */
    public List<AnswerChoice> getChoices() {
	return choices;
    }

    /**
     * Sets the choices.
     *
     * @param choices the choices to set
     */
    public void setChoices(List<AnswerChoice> choices) {
	this.choices = choices;
    }

    /**
     * To xml.
     *
     * @return the string
     */
    @JsonIgnore
    public String toXML() {
	String xml = "";

	xml += "<answer>";
	xml += this.getAnswerString();
	xml += "</answer>";

	return xml;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Answer [answerString=" + answerString + ", choices=" + choices
		+ "]";
    }

}