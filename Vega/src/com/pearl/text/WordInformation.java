package com.pearl.text;

import java.util.ArrayList;
import java.util.List;

import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class WordInformation.
 */
public class WordInformation {
    
    /** The word. */
    private String word;
    
    /** The pronounciation. */
    private String pronounciation;
    
    /** The meanings. */
    private List<WordMeaning> meanings;
    
    /** The origin. */
    private String origin;

    /**
     * Instantiates a new word information.
     */
    public WordInformation() {
	word = "";
	meanings = new ArrayList<WordMeaning>();
	origin = "";

	populateWordMeaningAndOrigin();
    }

    /**
     * Populate word meaning and origin.
     */
    public void populateWordMeaningAndOrigin() {

    }

    /**
     * Gets the formated meaning.
     *
     * @return the formated meaning
     */
    public String getFormatedMeaning() {
	String response = "<b>" + word + "</b>:<br/><br/>";
	int count = 1;

	for (final WordMeaning meaning : meanings) {
	    response += "<i>" + meaning.getPartOfSpeech() + ":</i><br/>";
	    response += "<ol>";
	    for (final String desc : meaning.getMeanings()) {
		response += "<li>";
		response += "	" + count++ + ". " + desc + "<br/>";
		response += "</li>";
	    }
	    response += "</ol><br/>";
	}

	Logger.error("wordinformation->getformatedmeaning", "response:"
		+ response);

	return response;
    }

    /**
     * Gets the word.
     *
     * @return the word
     */
    public String getWord() {
	return word;
    }

    /**
     * Gets the origin.
     *
     * @return the origin
     */
    public String getOrigin() {
	return origin;
    }

    /**
     * Sets the word.
     *
     * @param word the word to set
     */
    public void setWord(String word) {
	this.word = word;
    }

    /**
     * Sets the origin.
     *
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
	this.origin = origin;
    }

    /**
     * Gets the pronounciation.
     *
     * @return the pronounciation
     */
    public String getPronounciation() {
	return pronounciation;
    }

    /**
     * Sets the pronounciation.
     *
     * @param pronounciation the pronounciation to set
     */
    public void setPronounciation(String pronounciation) {
	this.pronounciation = pronounciation;
    }

    /**
     * Sets the meanings.
     *
     * @param meanings the meanings to set
     */
    public void setMeanings(List<WordMeaning> meanings) {
	this.meanings = meanings;
    }

    /**
     * Gets the meanings.
     *
     * @return the meanings
     */
    public List<WordMeaning> getMeanings() {
	return meanings;
    }
}