package com.pearl.epub.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.util.Log;

import com.pearl.epub.exception.EbookException;

// TODO: Auto-generated Javadoc
/**
 * The Class DOMUtil.
 */
public class DOMUtil {
    
    /** The Constant TAG. */
    private static final String TAG = "DOMUtil";

    /**
     * Returns DOM object by file name.
     *
     * @param fileName the file name
     * @return the dom
     * @throws EbookException the ebook exception
     */
    public static Document getDom(String fileName) throws EbookException {
	return getDom(new File(fileName));
    }

    /**
     * Returns DOM object by file object.
     *
     * @param file the file
     * @return the dom
     * @throws EbookException the ebook exception
     */
    public static Document getDom(File file) throws EbookException {
	FileInputStream fis = null;
	try {
	    fis = new FileInputStream(file);
	} catch (final FileNotFoundException e) {
	    e.printStackTrace();
	    throw new EbookException(e.getMessage());
	}
	return getDom(fis);
    }

    /**
     * Returns DOM object by input stream.
     *
     * @param istream the istream
     * @return the dom
     * @throws EbookException the ebook exception
     */
    public static Document getDom(InputStream istream) throws EbookException {
	Document doc = null;

	try {
	    final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
		    .newInstance();
	    final DocumentBuilder documentBuilder = documentBuilderFactory
		    .newDocumentBuilder();
	    doc = documentBuilder.parse(istream);

	    final DOMImplementation domImpl = doc.getImplementation();
	    if (domImpl.hasFeature("Core", "2.0")) {
		Log.i(TAG, "DOM Core 2.0 is supported.");
	    } else if (domImpl.hasFeature("Core", "5.0")) {
		Log.i(TAG, "DOM Core 5.0 is supported.");
	    }
	} catch (final IOException e) {
	    e.printStackTrace();
	    throw new EbookException(e.getMessage());
	} catch (final SAXException e) {
	    e.printStackTrace();
	    throw new EbookException(e.getMessage());
	} catch (final ParserConfigurationException e) {
	    e.printStackTrace();
	    throw new EbookException(e.getMessage());
	}

	return doc;
    }

}
