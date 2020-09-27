/**
 * 
 */
package com.pearl.books;

import java.io.Serializable;
import java.util.ArrayList;

import com.pearl.books.pages.Page;
import com.pearl.network.downloadmanager.utils.DownloadStatus;

// TODO: Auto-generated Javadoc
/**
 * The Class Book.
 *
 * @author KiranYNG
 */
public class Book implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private String id;
    
    /** The book id. */
    protected int bookId;
    
    /** The coverpage. */
    protected CoverImage coverpage;
    
    /** The meta data. */
    protected BookMetaData metaData;

    /** The is starred. */
    protected boolean isStarred;

    /** The pages. */
    protected ArrayList<Page> pages;
    
    /** The current page. */
    protected int currentPage;

    /** The download status. */
    protected DownloadStatus downloadStatus;
    
    /** The download percentage. */
    protected int downloadPercentage;

    /** The book files directory. */
    protected String bookFilesDirectory = null;
    
    /** The filename. */
    protected String filename;

    /**
     * Instantiates a new book.
     */
    public Book() {
	downloadPercentage = 0;
	metaData = new BookMetaData();
	coverpage = new CoverImage();

	downloadStatus = new DownloadStatus();
    }

    /**
     * Gets the book id.
     *
     * @return the id
     */
    public int getBookId() {
	return bookId;
    }

    /**
     * Gets the coverpage.
     *
     * @return the coverpage
     */
    public CoverImage getCoverpage() {
	return coverpage;
    }

    /**
     * Gets the meta data.
     *
     * @return the metaData
     */
    public BookMetaData getMetaData() {
	return metaData;
    }

    /**
     * Checks if is starred.
     *
     * @return the isStarred
     */
    public boolean isStarred() {
	return isStarred;
    }

    /**
     * Gets the pages.
     *
     * @return the pages
     */
    public ArrayList<Page> getPages() {
	return pages;
    }

    /**
     * Gets the current page.
     *
     * @return the currentPage
     */
    public int getCurrentPage() {
	return currentPage;
    }

    /**
     * Gets the download status.
     *
     * @return the downloadStatus
     */
    /*
     * public String getDownloadStatus() { return downloadStatus.getStatus(); }
     */

    public DownloadStatus getDownloadStatus() {
	return downloadStatus;
    }

    /**
     * Gets the download percentage.
     *
     * @return the downloadPercentage
     */
    public int getDownloadPercentage() {
	return downloadPercentage;
    }

    /**
     * Gets the book files directory.
     *
     * @return the bookFilesDirectory
     */
    public String getBookFilesDirectory() {
	return bookFilesDirectory;
    }

    /**
     * Gets the filename.
     *
     * @return the filename
     */
    public String getFilename() {
	return filename;
    }

    /**
     * Sets the book id.
     *
     * @param bookId the new book id
     */
    public void setBookId(int bookId) {
	this.bookId = bookId;
    }

    /**
     * Sets the coverpage.
     *
     * @param coverpage the coverpage to set
     */
    public void setCoverpage(CoverImage coverpage) {
	this.coverpage = coverpage;
    }

    /**
     * Sets the meta data.
     *
     * @param metaData the metaData to set
     */
    public void setMetaData(BookMetaData metaData) {
	this.metaData = metaData;
    }

    /**
     * Sets the starred.
     *
     * @param isStarred the isStarred to set
     */
    public void setStarred(boolean isStarred) {
	this.isStarred = isStarred;
    }

    /**
     * Sets the pages.
     *
     * @param pages the pages to set
     */
    public void setPages(ArrayList<Page> pages) {
	this.pages = pages;
    }

    /**
     * Sets the current page.
     *
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
	this.currentPage = currentPage;
    }

    /**
     * Sets the download status.
     *
     * @param status the new download status
     */
    /*
     * public void setDownloadStatus(String status) {
     * 
     * this.downloadStatus.setStaus(status); }
     */
    public void setDownloadStatus(DownloadStatus status) {

	downloadStatus = status;
    }

    /**
     * Sets the download percentage.
     *
     * @param downloadPercentage the downloadPercentage to set
     */
    public void setDownloadPercentage(int downloadPercentage) {
	this.downloadPercentage = downloadPercentage;
    }

    /**
     * Sets the book files directory.
     *
     * @param bookFilesDirectory the bookFilesDirectory to set
     */
    public void setBookFilesDirectory(String bookFilesDirectory) {
	this.bookFilesDirectory = bookFilesDirectory;
    }

    /**
     * Sets the filename.
     *
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
	this.filename = filename;
    }

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
     * @param id the new id
     */
    public void setId(String id) {
	this.id = id;
    }
}