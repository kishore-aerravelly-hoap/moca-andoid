/**
 * 
 */
package com.pearl.books.pages;

// TODO: Auto-generated Javadoc
/**
 * The Class Note.
 */
public class Note {
    
    /** The title. */
    private String title;
    
    /** The description. */
    private String description;
    
    /** The page num. */
    private int pageNum;
    
    /** The date. */
    private String date;
    
    /** The id. */
    private int id;

    /**
     * Instantiates a new note.
     */
    public Note() {
	super();
    }

    /**
     * Instantiates a new note.
     *
     * @param title the title
     * @param description the description
     */
    public Note(String title, String description) {
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
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
	return description;
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
     * Sets the description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Gets the page num.
     *
     * @return the page num
     */
    public int getPageNum() {
	return pageNum;
    }

    /**
     * Sets the page num.
     *
     * @param pageNum the new page num
     */
    public void setPageNum(int pageNum) {
	this.pageNum = pageNum;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public String getDate() {
	return date;
    }

    /**
     * Sets the date.
     *
     * @param date the new date
     */
    public void setDate(String date) {
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
}