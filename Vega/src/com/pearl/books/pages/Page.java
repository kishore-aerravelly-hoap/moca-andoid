package com.pearl.books.pages;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;

import com.pearl.books.pages.exception.InvalidSearchTermException;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class Page.
 */
public class Page implements Readable {
    
    /** The page title. */
    private String pageTitle;
    
    /** The page content. */
    private String pageContent;

    /** The page no. */
    private int pageNo = -1;

    /** The total sub pages. */
    private int totalSubPages = 0;
    
    /** The sub page start pg no. */
    private int subPageStartPgNo = 0;
    
    /** The sub page end pg no. */
    private int subPageEndPgNo = 0;

    /** The book mark. */
    private BookMark bookMark;
    
    /** The notes. */
    private ArrayList<Note> notes;

    /** The resources path. */
    private String resourcesPath;
    
    /** The raw content. */
    private String rawContent;

    /**
     * Instantiates a new page.
     */
    public Page() {
	resourcesPath = "";
    }

    /**
     * Gets the page no.
     *
     * @return the page no
     */
    public int getPageNo() {
	return pageNo;
    }

    /**
     * Sets the page no.
     *
     * @param page_no the new page no
     */
    public void setPageNo(int page_no) {
	pageNo = page_no;
    }

    /**
     * Gets the title.
     *
     * @return the pageTitle
     */
    public String getTitle() {
	return pageTitle;
    }

    /**
     * Sets the page title.
     *
     * @param pageTitle the pageTitle to set
     */
    public void setPageTitle(String pageTitle) {
	this.pageTitle = pageTitle;
    }

    /**
     * Gets the resources path.
     *
     * @return the resources path
     */
    public String getResourcesPath() {
	return resourcesPath;
    }

    /**
     * Sets the resources path.
     *
     * @param resourcesPath the new resources path
     */
    public void setResourcesPath(String resourcesPath) {
	this.resourcesPath = resourcesPath;
    }

    /**
     * Gets the page content.
     *
     * @return the pageContent
     */
    public String getPageContent() {
	return pageContent;
    }

    /**
     * Gets the page content.
     *
     * @param hightlightString the hightlight string
     * @param occurence the occurence
     * @return the page content
     */
    public String getPageContent(String hightlightString, int occurence) {
	int occr = 1;
	int index = 0;
	Logger.info("Page", "SEARCH - page content is:" + pageContent
		+ " highlight string is:" + hightlightString + " occurence is:"
		+ occurence);
	if (hightlightString != null && hightlightString.trim() != "") {
	    index = pageContent.indexOf(hightlightString);

	    final String highlight = "<span style='background-color:#fff2a8 !important;font-style:italic !important;'>"
		    + hightlightString + "</span>";

	    while (index > -1) {
		Logger.warn("Page", "SEARCH - index is " + index);

		if (occr == occurence) {
		    pageContent = pageContent.substring(0, index)
			    + highlight
			    + pageContent.substring(index
				    + hightlightString.length());
		    Logger.warn("Page",
			    "SEARCH - page content in Second IF is:"
				    + pageContent);
		    break;
		}

		index = pageContent.indexOf(hightlightString, index + 1);
		occr++;
	    }
	    /*
	     * return pageContent; .replace( hightlightString,
	     * "<span style='background-color:#fff2a8 !important;font-style:italic !important;'>"
	     * + hightlightString + "</span>");
	     */
	}

	Logger.info("Page", "SEARCH - after IF highlightString:"
		+ hightlightString + "| occurence:" + occr + "| finalindex:"
		+ index);
	Logger.warn("Page", "SEARCH - page content after computation is:"
		+ pageContent);

	return pageContent;
    }

    /**
     * Sets the page content.
     *
     * @param pageContent the pageContent to set
     */
    public void setPageContent(String pageContent) {
	this.pageContent = pageContent;
    }

    /**
     * Gets the book mark.
     *
     * @return the bookMark
     */
    public BookMark getBookMark() {
	return bookMark;
    }

    /**
     * Sets the book mark.
     *
     * @param bookMark the bookMark to set
     */
    public void setBookMark(BookMark bookMark) {
	this.bookMark = bookMark;
    }

    /**
     * Gets the notes.
     *
     * @return the notes
     */
    public ArrayList<Note> getNotes() {
	return notes;
    }

    /**
     * Sets the notes.
     *
     * @param notes the notes to set
     */
    public void setNotes(ArrayList<Note> notes) {
	this.notes = notes;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "<Page to string>";
    }

    /**
     * Checks for search term.
     *
     * @param searchTerm the search term
     * @return true, if successful
     */
    public boolean hasSearchTerm(String searchTerm) {
	if (getRawContent().contains(searchTerm)) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Gets the search results.
     *
     * @param searchTerm the search term
     * @return the search results
     * @throws InvalidSearchTermException the invalid search term exception
     */
    public ArrayList<String> getSearchResults(String searchTerm)
	    throws InvalidSearchTermException {

	if (searchTerm == null || "".equals(searchTerm))
	    throw new InvalidSearchTermException(
		    "You can't have an empty search term");

	final ArrayList<String> searchResult = new ArrayList<String>();

	// Do string operations and populate the arraylist !
	if (getRawContent().contains(searchTerm)) {
	    final String content = getRawContent();

	    int findPos = content.indexOf(searchTerm);

	    while (findPos >= 0) {
		String finalDesc = "";

		int startPos = 0;
		if (findPos > 50) {
		    startPos = findPos - 50;
		}

		int endPos = findPos + searchTerm.length() + 50;
		if (content.length() < endPos) {
		    endPos = content.length();
		}

		// TODO need to refine the string, it should not start from the
		// middle of a word and should not end at the middle of the word
		finalDesc = content.substring(startPos, endPos) + "..";

		searchResult.add(finalDesc);
		Logger.info("Page->searchResults", "Page - " + this.getTitle()
			+ " contains '" + searchTerm + "' as '" + finalDesc
			+ "'");

		findPos = content.indexOf(searchTerm, findPos + 1);
	    }
	} else {
	    Logger.info("Page->searchResults", "Page - " + this.getTitle()
		    + " does not contain '" + searchTerm + "'");
	}

	return searchResult;
    }

    /**
     * Gets the raw content.
     *
     * @return the raw content
     */
    private String getRawContent() {
	if (rawContent == null || "".equals(rawContent)) {
	    return rawContent = this.getPageContent();
	}

	rawContent = "";
	int startTag = 0, endTag = 0, currentCursorLocation = 0;
	while (currentCursorLocation < pageContent.length()) {
	    startTag = pageContent.indexOf("<", currentCursorLocation);
	    endTag = pageContent.indexOf(">", currentCursorLocation);

	    if (startTag == -1) { // reached end of tags, check if trailing
		// content available
		if (currentCursorLocation < pageContent.length()) {
		    rawContent += pageContent.substring(currentCursorLocation,
			    pageContent.length());
		}
		break; // now that we are through break out of loop
	    } else {
		if (currentCursorLocation < startTag)
		    rawContent += pageContent.substring(currentCursorLocation,
			    startTag - 1);
		currentCursorLocation = endTag + 1;
	    }
	}

	Logger.verbose("Page->getRawContent", rawContent);

	return rawContent;
    }

    /* (non-Javadoc)
     * @see java.lang.Readable#read(java.nio.CharBuffer)
     */
    @Override
    public int read(CharBuffer arg0) throws IOException {

	return 0;
    }

    /**
     * Gets the total sub pages.
     *
     * @return the totalSubPages
     */
    public int getTotalSubPages() {
	return totalSubPages;
    }

    /**
     * Sets the total sub pages.
     *
     * @param totalSubPages the totalSubPages to set
     */
    public void setTotalSubPages(int totalSubPages) {
	this.totalSubPages = totalSubPages;
    }

    /**
     * Gets the sub page start pg no.
     *
     * @return the subPageStartPgNo
     */
    public int getSubPageStartPgNo() {
	return subPageStartPgNo;
    }

    /**
     * Gets the sub page end pg no.
     *
     * @return the subPageEndPgNo
     */
    public int getSubPageEndPgNo() {
	return subPageEndPgNo;
    }

    /**
     * Sets the sub page start pg no.
     *
     * @param subPageStartPgNo the subPageStartPgNo to set
     */
    public void setSubPageStartPgNo(int subPageStartPgNo) {
	this.subPageStartPgNo = subPageStartPgNo;
    }

    /**
     * Sets the sub page end pg no.
     *
     * @param subPageEndPgNo the subPageEndPgNo to set
     */
    public void setSubPageEndPgNo(int subPageEndPgNo) {
	this.subPageEndPgNo = subPageEndPgNo;
    }
}
