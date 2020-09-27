package com.pearl.search;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchResult.
 */
public class SearchResult {
    
    /** The page no. */
    private final int pageNo;
    
    /** The chapter no. */
    private final int chapterNo;
    
    /** The matching string. */
    private final String matchingString;
    
    /** The occurence. */
    private final int occurence;

    /**
     * Instantiates a new search result.
     *
     * @param chapterNo the chapter no
     * @param pageNo the page no
     * @param matchingString the matching string
     */
    public SearchResult(int chapterNo, int pageNo, String matchingString) {
	this.pageNo = pageNo;
	this.chapterNo = chapterNo;
	this.matchingString = matchingString;
	occurence = 1;
    }

    /**
     * Instantiates a new search result.
     *
     * @param chapterNo the chapter no
     * @param pageNo the page no
     * @param matchingString the matching string
     * @param occurence the occurence
     */
    public SearchResult(int chapterNo, int pageNo, String matchingString,
	    int occurence) {
	this.pageNo = pageNo;
	this.chapterNo = chapterNo;
	this.matchingString = matchingString;
	this.occurence = occurence;
    }

    /**
     * Gets the page no.
     *
     * @return the pageNo
     */
    public int getPageNo() {
	return pageNo;
    }

    /**
     * Gets the chapter no.
     *
     * @return the chapterNo
     */
    public int getChapterNo() {
	return chapterNo;
    }

    /**
     * Gets the matching string.
     *
     * @return the matchingString
     */
    public String getMatchingString() {
	return matchingString;
    }

    /**
     * Gets the occurene.
     *
     * @return the occurene
     */
    public int getOccurene() {
	return occurence;
    }
}
