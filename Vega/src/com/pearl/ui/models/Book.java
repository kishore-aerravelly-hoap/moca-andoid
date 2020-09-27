/**
 * 
 */
package com.pearl.ui.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Book.
 *
 * @author kpamulapati
 */
public class Book implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id. */
    private String id;
    
    /** The name. */
    private String name;
    
    /** The author. */
    private String author;
    
    /** The title. */
    private String title;
    
    /** The description. */
    private String description;
    
    /** The file name. */
    private String fileName;
    
    /** The coverage page. */
    private String coveragePage;
    
    /** The genre. */
    private String genre;
    
    /** The publication date. */
    private String publicationDate;
    
    /** The download status. */
    private String downloadStatus;
    
    /** The bookmark status. */
    private String bookmarkStatus;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * Sets the id.
     *
     * @param id the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Gets the author.
     *
     * @return the author
     */
    public String getAuthor() {
	return author;
    }

    /**
     * Sets the author.
     *
     * @param author the author to set
     */
    public void setAuthor(String author) {
	this.author = author;
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
     * Sets the title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
	this.title = title;
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

    /**
     * Gets the file name.
     *
     * @return the fileName
     */
    public String getFileName() {
	return fileName;
    }

    /**
     * Sets the file name.
     *
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    /**
     * Gets the coverage page.
     *
     * @return the coveragePage
     */
    public String getCoveragePage() {
	return coveragePage;
    }

    /**
     * Sets the coverage page.
     *
     * @param coveragePage the coveragePage to set
     */
    public void setCoveragePage(String coveragePage) {
	this.coveragePage = coveragePage;
    }

    /**
     * Gets the genre.
     *
     * @return the genre
     */
    public String getGenre() {
	return genre;
    }

    /**
     * Sets the genre.
     *
     * @param genre the genre to set
     */
    public void setGenre(String genre) {
	this.genre = genre;
    }

    /**
     * Gets the publication date.
     *
     * @return the publicationDate
     */
    public String getPublicationDate() {
	return publicationDate;
    }

    /**
     * Sets the publication date.
     *
     * @param publicationDate the publicationDate to set
     */
    public void setPublicationDate(String publicationDate) {
	this.publicationDate = publicationDate;
    }

    /**
     * Gets the download status.
     *
     * @return the downloadStatus
     */
    public String getDownloadStatus() {
	return downloadStatus;
    }

    /**
     * Sets the download status.
     *
     * @param downloadStatus the downloadStatus to set
     */
    public void setDownloadStatus(String downloadStatus) {
	this.downloadStatus = downloadStatus;
    }

    /**
     * Gets the bookmark status.
     *
     * @return the bookmarkStatus
     */
    public String getBookmarkStatus() {
	return bookmarkStatus;
    }

    /**
     * Sets the bookmark status.
     *
     * @param bookmarkStatus the bookmarkStatus to set
     */
    public void setBookmarkStatus(String bookmarkStatus) {
	this.bookmarkStatus = bookmarkStatus;
    }

}
