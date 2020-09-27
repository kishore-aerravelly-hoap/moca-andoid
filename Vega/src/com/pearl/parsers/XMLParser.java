package com.pearl.parsers;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

import com.pearl.books.Book;
import com.pearl.logger.Logger;
import com.pearl.parsers.xml.BookslistParser;
import com.pearl.parsers.xml.DictionaryParser;

// TODO: Auto-generated Javadoc
/**
 * The Class XMLParser.
 */
public class XMLParser {
    
    /** The tag. */
    private static String tag = "XMLParser";
    
    /** The book list. */
    public static ArrayList<Book> bookList = null;

    /**
     * Gets the dictionary meaning.
     *
     * @param url the url
     * @return the dictionary meaning
     */
    public static String getDictionaryMeaning(String url) {
	FileInputStream fis;

	try {
	    fis = new FileInputStream(url);
	    Logger.warn(tag, "Notification url is :" + url);
	    /** Handling XML */
	    final SAXParserFactory spf = SAXParserFactory.newInstance();
	    final SAXParser sp = spf.newSAXParser();
	    final XMLReader xr = sp.getXMLReader();

	    final DictionaryParser XMLHandler = new DictionaryParser();
	    xr.setContentHandler(XMLHandler);
	    xr.parse(new InputSource(fis));
	    fis.close();

	    return XMLHandler.getMeaning();
	} catch (final Exception e) {
	    Logger.error(tag, e);

	    return "";
	}
    }

    /**
     * Gets the books list.
     *
     * @param xml_file_path the xml_file_path
     * @return the books list
     */
    public static ArrayList<Book> getBooksList(String xml_file_path) {
	// Get it from local
	FileInputStream fis;
	try {
	    fis = new FileInputStream(xml_file_path);

	    /** Handling XML */
	    final SAXParserFactory spf = SAXParserFactory.newInstance();
	    final SAXParser sp = spf.newSAXParser();
	    final XMLReader xr = sp.getXMLReader();

	    final BookslistParser XMLHandler = new BookslistParser();
	    xr.setContentHandler(XMLHandler);

	    xr.parse(new InputSource(fis));
	    fis.close();

	    return XMLHandler.booksList;
	} catch (final Exception e) {
	    final StringWriter sw = new StringWriter();
	    e.printStackTrace(new PrintWriter(sw));
	    final String stacktrace = sw.toString();

	    Log.d("In first try:", stacktrace);
	}

	return new ArrayList<Book>();
    }

    /**
     * Gets the books list.
     *
     * @param searchString the search string
     * @param xml_file_path the xml_file_path
     * @return the books list
     */
    public static ArrayList<Book> getBooksList(String searchString,
	    String xml_file_path) {
	Logger.info(tag, "in getBooksList(1,2)");
	final ArrayList<Book> booksList = getBooksList(xml_file_path);
	final ArrayList<Book> searchList = new ArrayList<Book>();
	// loop through the books list
	Logger.info(tag, "booksList size is:" + booksList.size());
	try {
	    if (booksList.size() != 0) {
		for (int i = 0; i < booksList.size(); i++) {
		    if (booksList.get(i).getMetaData().getTitle().toLowerCase()
			    .contains(searchString.toLowerCase())) {
			Logger.warn(tag, "title is:"
				+ booksList.get(i).getMetaData().getTitle());

			searchList.add(booksList.get(i));

		    }
		}
	    }

	} catch (final Exception e) {
	    Logger.error(tag, "SearchList is null" + e.toString());
	}

	return searchList;
    }
}