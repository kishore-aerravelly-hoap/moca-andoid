package com.pearl.parsers.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.pearl.books.Book;
import com.pearl.books.CoverImage;

// TODO: Auto-generated Javadoc
/**
 * The Class BookslistParser.
 */
public class BookslistParser extends DefaultHandler {
    
    /** The current element. */
    Boolean currentElement = false;
    
    /** The current value. */
    String currentValue = null;
    
    /** The book. */
    Book book;

    /** The books list. */
    public ArrayList<Book> booksList = null;
    
    /** The subject list. */
    List<String> subjectList = new ArrayList<String>();

    /**
     * Gets the books list.
     *
     * @return the books list
     */
    public ArrayList<Book> getBooksList() {
	return booksList;
    }

    /**
     * Sets the books list.
     *
     * @param booksList the new books list
     */
    public void setBooksList(ArrayList<Book> booksList) {
	this.booksList = booksList;
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

	if (localName.equals("books")) {
	    // Initiate books list
	    booksList = new ArrayList<Book>();
	} else if (localName.equals("book")) {
	    // Initiate/re-initiate book
	    book = new Book();
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
	if (booksList == null || book == null)
	    return; // start only after this gets initialized

	/** set value */
	if (localName.equalsIgnoreCase("id")) {
	    Log.d("in EndELement", "current valu:" + currentValue);
	    book.setBookId(Integer.parseInt(currentValue));
	} else if (localName.equalsIgnoreCase("title")) {
	    book.getMetaData().setTitle(currentValue);
	} else if (localName.equalsIgnoreCase("subject")) {
	    subjectList.add(currentValue);
	    book.getMetaData().setSubjects(subjectList);
	} else if (localName.equalsIgnoreCase("description")) {
	    book.getMetaData().setDescription(currentValue);
	} else if (localName.equalsIgnoreCase("coverpage")) {
	    final CoverImage coverpage = new CoverImage();
	    coverpage.setLocalPath("");
	    coverpage.setWebPath(currentValue);

	    book.setCoverpage(coverpage);
	} else if (localName.equalsIgnoreCase("filename")) {
	    book.setFilename(currentValue);
	} else if (localName.equalsIgnoreCase("downloadstatus")) {
	    /*
	     * if(DownloadStatus.SUCCESS.equals(currentValue.trim().toUpperCase()
	     * )){ book.setDownloadStatus(DownloadStatus.SUCCESS); }else
	     * if(DownloadStatus
	     * .IN_PROGRESS.equals(currentValue.trim().toUpperCase())){
	     * book.setDownloadStatus(DownloadStatus.IN_PROGRESS); }else
	     * if(DownloadStatus
	     * .FAIL.equals(currentValue.trim().toUpperCase())){
	     * book.setDownloadStatus(DownloadStatus.FAIL); }else
	     * if(DownloadStatus
	     * .ABORT.equals(currentValue.trim().toUpperCase())){
	     * book.setDownloadStatus(DownloadStatus.ABORT); }else
	     * if(DownloadStatus
	     * .DELETED.equals(currentValue.trim().toUpperCase())){
	     * book.setDownloadStatus(DownloadStatus.DELETED); }else {
	     * book.setDownloadStatus(DownloadStatus.NOT_DONE); }
	     */
	} else if (localName.equalsIgnoreCase("book")) {
	    // TODO make this generic

	    /*
	     * if(book.getFilename().toLowerCase().endsWith(".epub")){
	     * book.getMetaData().setBookFormat(BookFormat.EPUB); }else
	     * if(book.getFilename().toLowerCase().endsWith(".xml")){
	     * book.getMetaData().setBookFormat(BookFormat.XML); }else{
	     * book.getMetaData().setBookFormat(BookFormat.UNKNOWN); }
	     */

	    booksList.add(book);
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