package com.pearl.booklist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pearl.books.Book;
import com.pearl.books.BookMetaData;

// TODO: Auto-generated Javadoc
/**
 * The Class BookList.
 */
public class BookList implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The book list. */
    private List<Book> bookList;
    
    /** The app version. */
    String appVersion;
    
    /** The last modified. */
    Date lastModified;

    /**
     * Instantiates a new book list.
     */
    public BookList() {
	bookList = new ArrayList<Book>();
    }

    /**
     * Gets the app version.
     *
     * @return the app version
     */
    public String getAppVersion() {
	return appVersion;
    }

    /**
     * Sets the app version.
     *
     * @param appVersion the new app version
     */
    public void setAppVersion(String appVersion) {
	this.appVersion = appVersion;
    }

    /**
     * Gets the last modified.
     *
     * @return the last modified
     */
    public Date getLastModified() {
	return lastModified;
    }

    /**
     * Sets the last modified.
     *
     * @param lastModified the new last modified
     */
    public void setLastModified(Date lastModified) {
	this.lastModified = lastModified;
    }

    /**
     * Gets the book list.
     *
     * @return the book list
     */
    public List<Book> getBookList() {
	return bookList;
    }

    /**
     * Sets the book list.
     *
     * @param bookList the new book list
     */
    public void setBookList(List<Book> bookList) {
	this.bookList = bookList;
    }

    /**
     * Removes the book.
     *
     * @param bookid the bookid
     */
    public void removeBook(int bookid) {
	for (int i = 0; i < bookList.size(); i++) {
	    final Book temp = bookList.get(i);
	    if (temp.getBookId() == bookid) {
		bookList.remove(temp);
	    }
	}
    }

    /**
     * Adds the book.
     */
    public void addBook(){
	final Book book = new Book();
	final BookMetaData bookMetaData = new BookMetaData();
	book.setBookId(0);
	bookMetaData.setTitle("Default");
	book.setMetaData(bookMetaData);
	bookList.add(book);
    }
}
