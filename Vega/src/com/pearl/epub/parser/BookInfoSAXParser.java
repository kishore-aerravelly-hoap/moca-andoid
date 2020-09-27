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

import com.pearl.epub.BookInfo;

// TODO: Auto-generated Javadoc
/**
 * The Class BookInfoSAXParser.
 */
public class BookInfoSAXParser extends DefaultHandler {

    /** The Constant TAG. */
    private static final String TAG = "BookInfoSAXParser";

    /** The book info. */
    private final BookInfo bookInfo = new BookInfo();

    /** The m start tag. */
    private String mStartTag;

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
	/*
	 * System.out.println("void startElement(uri:" + uri + ", localName:" +
	 * localName + ", qName:" + qName + ", attributes:" + attributes + ")");
	 */

	mStartTag = localName;

	if ("item".equalsIgnoreCase(localName)) {
	    final String id = attributes.getValue("id");
	    final String href = attributes.getValue("href");
	    final String mediaType = attributes.getValue("media-type");
	    /*
	     * System.out.println("id: " + id); System.out.println("href: " +
	     * href); System.out.println("mediaType: " + mediaType);
	     */

	    bookInfo.addManifest(new BookInfo().new Manifest(id, href,
		    mediaType));

	    // save css path
	    if ("css".equalsIgnoreCase(id)) {
		bookInfo.setCssPath(href);
	    }
	} else if ("itemref".equalsIgnoreCase(localName)) {
	    final String idref = attributes.getValue("idref");
	    // System.out.println("idref: " + idref);

	    bookInfo.addSpine(idref);
	}
    }

    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length)
	    throws SAXException {
	final StringBuffer sb = new StringBuffer();
	for (int i = start; i < start + length; i++) {
	    sb.append(ch[i]);
	}
	final String text = sb.toString();

	if ("title".equalsIgnoreCase(mStartTag)) {
	    System.out.println("title: " + text);
	    bookInfo.setTitle(text);
	} else if ("creator".equalsIgnoreCase(mStartTag)) {
	    System.out.println("creator: " + text);
	    bookInfo.setAuthor(text);
	} else if ("publisher".equalsIgnoreCase(mStartTag)) {
	    System.out.println("publisher: " + text);
	    bookInfo.setPublisher(text);
	} else if ("identifier".equalsIgnoreCase(mStartTag)) {
	    System.out.println("identifier: " + text);
	    bookInfo.setIsbn(text);
	} else if ("language".equalsIgnoreCase(mStartTag)) {
	    System.out.println("language: " + text);
	    bookInfo.setLanguage(text);
	}
    }

    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	/*
	 * System.out.println("void endElement(uri:" + uri + ", localName:" +
	 * localName + ", qName:" + qName + ")");
	 */

	if (mStartTag.equalsIgnoreCase(localName)) {
	    mStartTag = "";
	}
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
     * Returns BookInfo.
     *
     * @param opfPath the opf path
     * @return the book info
     * @throws ParserConfigurationException the parser configuration exception
     * @throws SAXException the sAX exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public BookInfo getBookInfo(String opfPath)
	    throws ParserConfigurationException, SAXException, IOException {

	final SAXParserFactory spf = SAXParserFactory.newInstance();
	final SAXParser sp = spf.newSAXParser();
	final XMLReader xr = sp.getXMLReader();
	xr.setContentHandler(this);
	xr.parse(new InputSource(new FileReader(new File(opfPath))));
	return bookInfo;
    }

}
