package com.pearl.search;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchCriteria.
 */
public class SearchCriteria {
    
    /** The query string. */
    private String queryString;
    
    /** The author name. */
    private String authorName;

    /**
     * Instantiates a new search criteria.
     *
     * @param queryString the query string
     */
    public SearchCriteria(String queryString) {
	this.queryString = queryString;
    }

    /**
     * Gets the query string.
     *
     * @return the queryString
     */
    public String getQueryString() {
	return queryString;
    }

    /**
     * Gets the author name.
     *
     * @return the authorName
     */
    public String getAuthorName() {
	return authorName;
    }

    /**
     * Sets the query string.
     *
     * @param queryString the queryString to set
     */
    public void setQueryString(String queryString) {
	this.queryString = queryString;
    }

    /**
     * Sets the author name.
     *
     * @param authorName the authorName to set
     */
    public void setAuthorName(String authorName) {
	this.authorName = authorName;
    }

}
