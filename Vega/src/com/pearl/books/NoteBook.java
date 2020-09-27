package com.pearl.books;

import java.util.Comparator;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class NoteBook.
 */
public class NoteBook implements Comparator<NoteBook> {
    
    /** The title. */
    private String title;
    
    /** The description. */
    private String description;
    
    /** The date. */
    private Date date;
    
    /** The subject. */
    private String subject;
    
    /** The id. */
    private int id;

    /**
     * Instantiates a new note book.
     */
    public NoteBook() {
	super();
    }

    /**
     * Instantiates a new note book.
     *
     * @param title the title
     * @param description the description
     */
    public NoteBook(String title, String description) {
	super();
	this.title = title;
	this.description = description;
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
     * Gets the notes description.
     *
     * @return the notes description
     */
    public String getNotesDescription() {
	return description;
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * Sets the notes description.
     *
     * @param description the new notes description
     */
    public void setNotesDescription(String description) {
	this.description = description;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public Date getDate() {
	return date;
    }

    /**
     * Sets the date.
     *
     * @param date the new date
     */
    public void setDate(Date date) {
	this.date = date;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
	return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(int id) {
	this.id = id;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject() {
	return subject;
    }

    /**
     * Sets the subject.
     *
     * @param subject the new subject
     */
    public void setSubject(String subject) {
	this.subject = subject;
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(NoteBook object1, NoteBook object2) {
	return object2.getDate().compareTo(object1.getDate());
    }
}
