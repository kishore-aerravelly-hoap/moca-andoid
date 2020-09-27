/**
 * Copyright (C) 2009 Samsung Electronics Co., Ltd. All rights reserved.
 *
 * Mobile Communication Division,
 * Digital Media & Communications Business, Samsung Electronics Co., Ltd.
 *
 * This software and its documentation are confidential and proprietary
 * information of Samsung Electronics Co., Ltd.  No part of the software and
 * documents may be copied, reproduced, transmitted, translated, or reduced to
 * any electronic medium or machine-readable form without the prior written
 * consent of Samsung Electronics.
 *
 * Samsung Electronics makes no representations with respect to the contents,
 * and assumes no responsibility for any errors that might appear in the
 * software and documents. This publication and the contents hereof are subject
 * to change without notice.
 */
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

import com.pearl.epub.TableOfContent;

// TODO: Auto-generated Javadoc
/**
 * The Class TocSAXParser.
 */
public class TocSAXParser extends DefaultHandler {

    /** The Constant TAG. */
    private static final String TAG = "TocSAXParser";

    /** The seq. */
    private int seq = 0;
    
    /** The m start tag. */
    private String mStartTag;
    
    /** The prev url. */
    private String prevUrl;

    /** The toc. */
    private final TableOfContent toc = new TableOfContent();
    
    /** The chapter. */
    private TableOfContent.Chapter chapter = new TableOfContent.Chapter();

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
	super.startElement(uri, localName, qName, attributes);

	mStartTag = localName;

	if ("content".equalsIgnoreCase(localName)) {
	    final String src = attributes.getValue("src");
	    final int index = src.lastIndexOf("#");

	    final String url = index > 0 ? src.substring(0, index) : src;
	    final String anchor = index > 0 ? src.substring(index + 1) : "";
	    System.out.println("url: " + url);
	    System.out.println("anchor: " + anchor);

	    if (!url.equals(prevUrl)) {
		chapter.setSeq(String.valueOf(++seq));
		chapter.setUrl(url);
		chapter.setAnchor(anchor);

		// add this navPoint to TOC
		toc.addChapter(chapter);
		chapter = new TableOfContent.Chapter();
	    }

	    prevUrl = url;
	}
    }

    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] ch, int start, int length)
	    throws SAXException {
	super.characters(ch, start, length);

	final StringBuffer sb = new StringBuffer();
	for (int i = start; i < start + length; i++) {
	    sb.append(ch[i]);
	}
	final String value = sb.toString();

	if ("text".equalsIgnoreCase(mStartTag)) {
	    System.out.println("title: " + value);
	    chapter.setTitle(value);
	}
    }

    /* (non-Javadoc)
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	super.endElement(uri, localName, qName);

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
     * Returns TOC.
     *
     * @param tocPath the toc path
     * @return the table of contents
     * @throws ParserConfigurationException the parser configuration exception
     * @throws SAXException the sAX exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public TableOfContent getTableOfContents(String tocPath)
	    throws ParserConfigurationException, SAXException, IOException {
	final SAXParserFactory spf = SAXParserFactory.newInstance();
	final SAXParser sp = spf.newSAXParser();
	final XMLReader xr = sp.getXMLReader();
	xr.setContentHandler(this);
	xr.parse(new InputSource(new FileReader(new File(tocPath))));
	return toc;
    }

}
