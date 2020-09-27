package com.pearl.parsers.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.pearl.examination.Exam;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamsListParser.
 */
public class ExamsListParser extends DefaultHandler {
    
    /** The current element. */
    private Boolean currentElement = false;
    
    /** The current value. */
    private String currentValue = null;

    /** The exams. */
    private ArrayList<Exam> exams;
    
    /** The temp exam. */
    private Exam tempExam;

    /** The temp books ids list. */
    private ArrayList<Integer> tempBooksIdsList;

    /**
     * Gets the exams list.
     *
     * @return the exams list
     */
    public ArrayList<Exam> getExamsList() {
	return exams;
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

	currentElement = true;

	// TODO initialization based on tag names for rest of the tags and
	// properties also
	if (localName.equalsIgnoreCase("testlist")) {
	    exams = new ArrayList<Exam>();
	} else if (localName.equalsIgnoreCase("test")) {
	    tempExam = new Exam();
	} else if (localName.equalsIgnoreCase("books")) {
	    tempBooksIdsList = new ArrayList<Integer>();
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

	currentElement = false;

	/** set value */
	// TODO update objects and properties by comparing with localName
	if (localName.equalsIgnoreCase("id")) {
	    tempExam.getExamDetails().setExamId(currentValue.trim());
	} else if (localName.equalsIgnoreCase("name")) {
	    tempExam.getExamDetails().setTitle(currentValue);
	} else if (localName.equalsIgnoreCase("description")) {
	    tempExam.getExamDetails().setDescription(currentValue);
	} else if (localName.equalsIgnoreCase("subject")) {
	    tempExam.getExamDetails().setSubject(currentValue);
	} else if (localName.equalsIgnoreCase("starttime")) {
	    tempExam.getExamDetails()
	    .setStartDate(Long.parseLong(currentValue));
	} else if (localName.equalsIgnoreCase("endtime")) {
	    tempExam.getExamDetails().setEndDate(Long.parseLong(currentValue));
	} else if (localName.equalsIgnoreCase("duration")) {
	    tempExam.getExamDetails().setDuration(
		    Integer.parseInt(currentValue));
	} else if (localName.equalsIgnoreCase("status")) {
	    tempExam.getExamDetails().setState(currentValue);

	    Logger.error("ExamsListParser", "=> Exam State is " + currentValue);
	} else if (localName.equalsIgnoreCase("retakable")) {
	    tempExam.getExamDetails().setRetakeable(
		    Boolean.parseBoolean(currentValue));
	} else if (localName.equalsIgnoreCase("result")) {
	    int res_id;

	    try {
		res_id = Integer.parseInt(currentValue);
	    } catch (final Exception e) {
		res_id = 0;
	    }

	    tempExam.getExamDetails().setResultId(res_id);
	} else if (localName.equalsIgnoreCase("maxpoints")) {
	    tempExam.getExamDetails().setMaxPoints(
		    Integer.parseInt(currentValue));
	} else if (localName.equalsIgnoreCase("passpoints")) {
	    tempExam.getExamDetails().setMinPoints(
		    Integer.parseInt(currentValue));
	} else if (localName.equalsIgnoreCase("negativepoints")) {
	    tempExam.getExamDetails().setNegativePoints(
		    Boolean.parseBoolean(currentValue));
	} else if (localName.equalsIgnoreCase("book")) {
	    tempBooksIdsList.add(Integer.parseInt(currentValue));
	} else if (localName.equalsIgnoreCase("books")) {
	    // TODO IMP need to add int[] here instead of arraylist of integers
	    // tempExam.getExamDetails().setAllowedBookIds(tempBooksIdsList);
	} else if (localName.equalsIgnoreCase("test")) {
	    exams.add(tempExam);

	    Logger.verbose("ExamsListParser->getExamsList()",
		    tempExam.toString());
	} else if (localName.equalsIgnoreCase("testlist")) {
	    // this.word = currentValue;
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