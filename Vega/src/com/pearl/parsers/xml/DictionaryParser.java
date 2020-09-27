package com.pearl.parsers.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.pearl.logger.Logger;
import com.pearl.text.WordInformation;
import com.pearl.text.WordMeaning;

// TODO: Auto-generated Javadoc
/**
 * The Class DictionaryParser.
 */
public class DictionaryParser extends DefaultHandler {
    
    /** The current element. */
    private Boolean currentElement = false;
    
    /** The current value. */
    private String currentValue = null;

    /** The word. */
    private String word;
    
    /** The meaning. */
    private WordInformation meaning;

    /** The temp word meanings. */
    private List<WordMeaning> tempWordMeanings;

    /** The temp word meaning. */
    private WordMeaning tempWordMeaning;

    // TODO this is used as flag to skip processing other/related meanings.. if
    // we need to handle that we need to update the logic accordingly
    /** The first meaning extraction completed. */
    private boolean firstMeaningExtractionCompleted = false;

    /**
     * Gets the meaning.
     *
     * @return the meaning
     */
    public String getMeaning() {
	Logger.error("dictionary parser", meaning.getFormatedMeaning()
		+ "<< in dictionary parser");

	return meaning.getFormatedMeaning();
    }

    /**
     * Called when tag starts ( ex:- <name>Book Name</name> -- <name> ).
     *
     * @param uri the uri
     * @param localName the local name
     * @param qName the q name
     * @param attributes the attributes
     * @throws SAXException the sAX exception
     */
    @Override
    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {

	if (firstMeaningExtractionCompleted) {
	    return;
	}

	currentElement = true;

	if (localName.equalsIgnoreCase("entry")) {
	    // TODO restricting only to one meaning.. need to make this to
	    // multiple if necessary
	    if (meaning == null) {
		meaning = new WordInformation();

		tempWordMeanings = new ArrayList<WordMeaning>();
	    }
	} else if (localName.equalsIgnoreCase("display_form")) {
	    meaning.setWord(currentValue);

	    word = currentValue;
	} else if (localName.equalsIgnoreCase("pron")) {
	    meaning.setPronounciation(currentValue);
	} else if (localName.equalsIgnoreCase("partofspeech")) {
	    tempWordMeaning = new WordMeaning(word);

	    Logger.warn("DictionaryParser",
		    "part of speech is " + attributes.getValue("pos"));

	    tempWordMeaning.setPartOfSpeech(attributes.getValue("pos"));
	}
    }

    /**
     * Called when tag closing ( ex:- <name>Book Name</name> -- </name> ).
     *
     * @param uri the uri
     * @param localName the local name
     * @param qName the q name
     * @throws SAXException the sAX exception
     */
    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {

	if (firstMeaningExtractionCompleted) {
	    return;
	}

	currentElement = false;

	if (meaning == null) {
	    return; // start only after this gets initialized
	}

	/** set value */
	if (localName.equalsIgnoreCase("display_form")) {
	    meaning.setWord(currentValue);

	    word = currentValue;

	    Logger.error("DictionaryParser", "Word:" + word);
	} else if (localName.equalsIgnoreCase("pron")) {
	    meaning.setPronounciation(currentValue);
	    Logger.error("DictionaryParser", "Pronounciation:" + currentValue);
	} else if (localName.equalsIgnoreCase("def")) {
	    tempWordMeaning.addMeaning(currentValue);

	    Logger.error("DictionaryParser", "Meaning:" + currentValue);
	} else if (localName.equalsIgnoreCase("partofspeech")) {
	    Logger.error("DictionaryParser", "NEW MEANING - begins");

	    tempWordMeanings.add(tempWordMeaning);

	    tempWordMeaning = null;
	} else if (localName.equalsIgnoreCase("entry")) {
	    meaning.setMeanings(tempWordMeanings);

	    Logger.verbose("DictionaryParser", meaning.toString());

	    firstMeaningExtractionCompleted = true;
	}
    }

    /**
     * Called to get tag characters ( ex:- <name>Book Name</name> -- to get
     * 'Book Name' Character ).
     *
     * @param ch the ch
     * @param start the start
     * @param length the length
     * @throws SAXException the sAX exception
     */
    @Override
    public void characters(char[] ch, int start, int length)
	    throws SAXException {
	if (currentElement) {
	    currentValue = new String(ch, start, length);
	    currentElement = false;

	    currentValue = currentValue.trim();
	}
    }
}