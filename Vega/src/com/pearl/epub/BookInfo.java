package com.pearl.epub;

import java.util.ArrayList;
import java.util.Arrays;

// TODO: Auto-generated Javadoc
/**
 * The Class BookInfo.
 */
public class BookInfo {

    /**
     * Book Types<br>
     * <ul>
     * <li>EPUB
     * <li>TEXT
     * </ul>.
     */
    public static enum BOOK_TYPE {
	
	/** The epub. */
	EPUB, 
 /** The text. */
 TEXT, 
 /** The html. */
 HTML
    }

    /** The _id. */
    private long _id;
    
    /** The book type. */
    private BOOK_TYPE bookType;
    
    /** The title. */
    private String title;
    
    /** The author. */
    private String author;
    
    /** The publisher. */
    private String publisher;
    
    /** The date. */
    private String date;
    
    /** The subject. */
    private String subject;
    
    /** The language. */
    private String language;
    
    /** The right. */
    private String right;
    
    /** The isbn. */
    private String isbn;
    
    /** The cover img. */
    private byte[] coverImg;
    
    /** The path. */
    private String path;

    /** The css path. */
    private String cssPath;

    /** The manifest list. */
    private final ArrayList<Manifest> manifestList = new ArrayList<BookInfo.Manifest>();
    
    /** The spine list. */
    private final ArrayList<String> spineList = new ArrayList<String>();

    /**
     * The Class Manifest.
     */
    public class Manifest {
	
	/** The id. */
	public String id;
	
	/** The href. */
	public String href;
	
	/** The media type. */
	public String mediaType;

	/**
	 * Instantiates a new manifest.
	 *
	 * @param id the id
	 * @param href the href
	 * @param mediaType the media type
	 */
	public Manifest(String id, String href, String mediaType) {
	    this.id = id;
	    this.href = href;
	    this.mediaType = mediaType;
	}
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public long getId() {
	return _id;
    }

    /**
     * Sets the id.
     *
     * @param _id the new id
     */
    public void setId(long _id) {
	this._id = _id;
    }

    /**
     * Gets the book type.
     *
     * @return the book type
     */
    public BOOK_TYPE getBookType() {
	return bookType;
    }

    /**
     * Sets the book type.
     *
     * @param bookType the new book type
     */
    public void setBookType(BOOK_TYPE bookType) {
	this.bookType = bookType;
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
     * @param title the new title
     */
    public void setTitle(String title) {
	this.title = title;
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
     * @param author the new author
     */
    public void setAuthor(String author) {
	this.author = author;
    }

    /**
     * Gets the publisher.
     *
     * @return the publisher
     */
    public String getPublisher() {
	return publisher;
    }

    /**
     * Sets the publisher.
     *
     * @param publisher the new publisher
     */
    public void setPublisher(String publisher) {
	this.publisher = publisher;
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

    /**
     * Gets the language.
     *
     * @return the language
     */
    public String getLanguage() {
	return language;
    }

    /**
     * Sets the language.
     *
     * @param language the new language
     */
    public void setLanguage(String language) {
	this.language = language;
    }

    /**
     * Gets the right.
     *
     * @return the right
     */
    public String getRight() {
	return right;
    }

    /**
     * Sets the right.
     *
     * @param right the new right
     */
    public void setRight(String right) {
	this.right = right;
    }

    /**
     * Gets the isbn.
     *
     * @return the isbn
     */
    public String getIsbn() {
	return isbn;
    }

    /**
     * Sets the isbn.
     *
     * @param isbn the new isbn
     */
    public void setIsbn(String isbn) {
	this.isbn = isbn;
    }

    /**
     * Gets the cover img.
     *
     * @return the cover img
     */
    public byte[] getCoverImg() {
	return coverImg;
    }

    /**
     * Sets the cover img.
     *
     * @param coverImg the new cover img
     */
    public void setCoverImg(byte[] coverImg) {
	this.coverImg = coverImg;
    }

    /**
     * Gets the path.
     *
     * @return the path
     */
    public String getPath() {
	return path;
    }

    /**
     * Sets the path.
     *
     * @param path the new path
     */
    public void setPath(String path) {
	this.path = path;
    }

    /**
     * Gets the css path.
     *
     * @return the css path
     */
    public String getCssPath() {
	return cssPath;
    }

    /**
     * Sets the css path.
     *
     * @param cssPath the new css path
     */
    public void setCssPath(String cssPath) {
	this.cssPath = cssPath;
    }

    /**
     * Gets the manifest item.
     *
     * @param i the i
     * @return the manifest item
     */
    public Manifest getManifestItem(int i) {
	return manifestList.get(i);
    }

    /**
     * Gets the manifest list.
     *
     * @return the manifest list
     */
    public ArrayList<Manifest> getManifestList() {
	return manifestList;
    }

    /**
     * Adds the manifest.
     *
     * @param manifest the manifest
     */
    public void addManifest(Manifest manifest) {
	manifestList.add(manifest);
    }

    /**
     * Gets the spine item.
     *
     * @param i the i
     * @return the spine item
     */
    public String getSpineItem(int i) {
	return spineList.get(i);
    }

    /**
     * Gets the spine list.
     *
     * @return the spine list
     */
    public ArrayList<String> getSpineList() {
	return spineList;
    }

    /**
     * Adds the spine.
     *
     * @param spine the spine
     */
    public void addSpine(String spine) {
	spineList.add(spine);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "BookInfo [_id=" + _id + ", bookType=" + bookType + ", title="
		+ title + ", author=" + author + ", publisher=" + publisher
		+ ", date=" + date + ", subject=" + subject + ", language="
		+ language + ", right=" + right + ", isbn=" + isbn
		+ ", coverImg=" + Arrays.toString(coverImg) + ", path=" + path
		+ ", cssPath=" + cssPath + ", manifestList=" + manifestList
		+ ", spineList=" + spineList + "]";
    }

}
