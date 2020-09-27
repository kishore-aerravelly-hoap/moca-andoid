package com.pearl.text;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class WordMeaning.
 */
public class WordMeaning {
    
    /** The word. */
    private String word;
    
    /** The part of speech. */
    private String partOfSpeech;
    
    /** The meanings. */
    private List<String> meanings;
    
    /** The examples. */
    private List<String> examples;

    /**
     * Instantiates a new word meaning.
     *
     * @param word the word
     */
    public WordMeaning(String word) {
	this.word = word;
	partOfSpeech = "";
	meanings = new ArrayList<String>();
	examples = new ArrayList<String>();
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
     * Gets the examples.
     *
     * @return the examples
     */
    public List<String> getExamples() {
	return examples;
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
     * Sets the examples.
     *
     * @param examples the examples to set
     */
    public void setExamples(List<String> examples) {
	this.examples = examples;
    }

    /**
     * Gets the part of speech.
     *
     * @return the partOfSpeech
     */
    public String getPartOfSpeech() {
	return partOfSpeech;
    }

    /**
     * Sets the part of speech.
     *
     * @param partOfSpeech the partOfSpeech to set
     */
    public void setPartOfSpeech(String partOfSpeech) {
	this.partOfSpeech = partOfSpeech;
    }

    /**
     * Gets the meanings.
     *
     * @return the meanings
     */
    public List<String> getMeanings() {
	return meanings;
    }

    /**
     * Adds the meaning.
     *
     * @param meaning the meaning
     */
    public void addMeaning(String meaning) {
	meanings.add(meaning);
    }

    /**
     * Sets the meanings.
     *
     * @param meanings the meanings to set
     */
    public void setMeanings(List<String> meanings) {
	this.meanings = meanings;
    }
}