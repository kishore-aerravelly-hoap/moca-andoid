package com.pearl.parsers.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.pearl.logger.Logger;
import com.pearl.services.Notification;

// TODO: Auto-generated Javadoc
/**
 * The Class NotificationParser.
 */
public class NotificationParser extends DefaultHandler {
    
    /** The current element. */
    Boolean currentElement = false;
    
    /** The current value. */
    String currentValue = "";
    
    /** The tag. */
    private final String TAG = "NotificationParser";
    
    /** The notification. */
    public Notification notification;

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
	Logger.info(TAG, localName + " Starting element");
	if (localName.equals("notifications")) {
	    // Initiate books list
	    Logger.info(TAG, "Creating notification object");
	    notification = new Notification();
	} else if (localName.equals("count")) {
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

	/** set value */
	if (localName.equalsIgnoreCase("tests")) {
	    Logger.info(
		    TAG,
		    "tests current Value and integer converted value is:"
			    + currentValue + " "
			    + Integer.parseInt(currentValue));
	    notification.setTestsCount(Integer.parseInt(currentValue));
	} else if (localName.equalsIgnoreCase("score")) {
	    Logger.info(TAG, "score current Value is:" + currentValue);
	    notification.setScoreCount(Integer.parseInt(currentValue));
	} else if (localName.equalsIgnoreCase("messages")) {
	    Logger.info(
		    TAG,
		    "messages current Value and integer converted value is:"
			    + currentValue + " "
			    + Integer.parseInt(currentValue));
	    notification.setMessageCount(Integer.parseInt(currentValue));
	} else if (localName.equalsIgnoreCase("info")) {
	    Logger.info(TAG, "Message Information:" + currentValue);
	    notification.setMessageInfo(currentValue);
	}

	currentElement = false;
	currentValue = "";
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
	    currentValue = currentValue
		    + new String(ch, start, length).trim();

	    currentValue = currentValue.trim();
	}
    }
}