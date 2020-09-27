package com.pearl.epub.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class CoverImgSAXParser.
 */
public class CoverImgSAXParser extends DefaultHandler {

    /** The Constant TAG. */
    private static final String TAG = "CoverImgSAXParser";

    /** The is found. */
    private boolean isFound = false;
    
    /** The m cover img path. */
    private String mCoverImgPath;

    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#startDocument()
     */
    @Override
    public void startDocument() throws SAXException {
	super.startDocument();
	System.out.println("[CALLBACK] void startDocument()");
    }

    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {
	System.out.println("void startElement(uri:" + uri + ", localName:"
		+ localName + ", qName:" + qName + ", attributes:" + attributes
		+ ")");

	if (!isFound && "img".equalsIgnoreCase(localName)) {
	    mCoverImgPath = attributes.getValue("src");
	    System.out.println("mCoverImgPath: " + mCoverImgPath);
	    isFound = true;
	}
    }

    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length)
	    throws SAXException {
	super.characters(ch, start, length);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	super.endElement(uri, localName, qName);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    @Override
    public void endDocument() throws SAXException {
	super.endDocument();
	System.out.println("[CALLBACK] void endDocument()");
    }

    /**
     * Returns Book Cover Image.
     *
     * @param htmlPath the html path
     * @return the book cover
     * @throws ParserConfigurationException the parser configuration exception
     * @throws SAXException the sAX exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public String getBookCover(String htmlPath)
	    throws ParserConfigurationException, SAXException, IOException {
	final SAXParserFactory spf = SAXParserFactory.newInstance();
	final SAXParser sp = spf.newSAXParser();
	final XMLReader xr = sp.getXMLReader();
	xr.setContentHandler(this);
	xr.parse(new InputSource(new FileReader(new File(htmlPath))));
	return mCoverImgPath;
    }

}
