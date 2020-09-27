package com.pearl.books;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class BookMetaData.
 */
public class BookMetaData implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The title. */
    private String title;
    
    /** The description. */
    private String description;
    
    /** The authors. */
    private List<Author> authors;
    
    /** The language. */
    private String language;
    
    /** The publishers. */
    private List<String> publishers;
    
    /** The publish date. */
    private Date publishDate;
    
    /** The subject. */
    private List<String> subject;
    
    /** The book format. */
    private BookFormat bookFormat;
    
    /** The rights. */
    private String rights;

    /**
     * Instantiates a new book meta data.
     */
    public BookMetaData() {
	super();

	authors = new ArrayList<Author>();
	publishers = new ArrayList<String>();
	subject = new ArrayList<String>();
	bookFormat = new BookFormat();
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * Gets the authors.
     *
     * @return the authors
     */
    public List<Author> getAuthors() {
	return authors;
    }

    /**
     * Gets the language.
     *
     * @return the language
     */
    public String getLanguage() {
	return language;
    }

    /**
     * Gets the publishers.
     *
     * @return the publisher
     */
    public List<String> getPublishers() {
	return publishers;
    }

    /**
     * Gets the publish date.
     *
     * @return the publishDate
     */
    public Date getPublishDate() {
	return publishDate;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public List<String> getSubject() {
	return subject;
    }

    /**
     * Gets the book format.
     *
     * @return the bookFormat
     */
    /*
     * public String getBookFormat() { return bookFormat.getFormat(); }
     */

    public BookFormat getBookFormat() {
	return bookFormat;
    }

    /**
     * Gets the rights.
     *
     * @return the rights
     */
    public String getRights() {
	return rights;
    }

    /**
     * Sets the title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * Sets the authors.
     *
     * @param authors the authors to set
     */
    public void setAuthors(List<Author> authors) {
	this.authors = authors;
    }

    /**
     * Sets the language.
     *
     * @param language the language to set
     */
    public void setLanguage(String language) {
	this.language = language;
    }

    /**
     * Sets the publishers.
     *
     * @param publishers the new publishers
     */
    public void setPublishers(List<String> publishers) {
	this.publishers = publishers;
    }

    /**
     * Sets the publish date.
     *
     * @param publishDate the publishDate to set
     */
    public void setPublishDate(Date publishDate) {
	this.publishDate = publishDate;
    }

    /**
     * Sets the subjects.
     *
     * @param subject the subject to set
     */
    public void setSubjects(List<String> subject) {
	this.subject = subject;
    }

    /**
     * Sets the book format.
     *
     * @param bookFormat the bookFormat to set
     */
    /*
     * public void setBookFormat(String bookFormat) {
     * this.bookFormat.setFormat(bookFormat); }
     */

    public void setBookFormat(BookFormat bookFormat) {
	this.bookFormat = bookFormat;
    }

    /**
     * Sets the rights.
     *
     * @param rights the rights to set
     */
    public void setRights(String rights) {
	this.rights = rights;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }
}