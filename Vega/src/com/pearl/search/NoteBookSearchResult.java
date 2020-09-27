package com.pearl.search;

import java.util.ArrayList;
import java.util.List;

import com.pearl.books.NoteBook;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class NoteBookSearchResult.
 */
public class NoteBookSearchResult {

    /** The searchable string. */
    String searchableString;
    
    /** The search results. */
    List<String> searchResults;
    
    /** The tag. */
    String tag = "NoteBookSearchResult";
    
    /** The search term. */
    String searchTerm;
    
    /** The search string. */
    String searchString;
    
    /** The search content. */
    String searchContent;
    
    /** The note book. */
    NoteBook noteBook;
    
    /** The id. */
    int id;
    
    /** The notes desc. */
    String notesDesc;

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
     * Gets the notes desc.
     *
     * @return the notes desc
     */
    public String getNotesDesc() {
	return notesDesc;
    }

    /**
     * Sets the notes desc.
     *
     * @param notesDesc the new notes desc
     */
    public void setNotesDesc(String notesDesc) {
	this.notesDesc = notesDesc;
    }

    /** The search range. */
    private final int SEARCH_RANGE = 10;

    /**
     * Instantiates a new note book search result.
     */
    public NoteBookSearchResult() {
	searchResults = new ArrayList<String>();
    }

    /**
     * Gets the search term index.
     *
     * @param searchableString the searchable string
     * @param searchTerm the search term
     * @return the search term index
     */
    public List<Integer[]> getSearchTermIndex(String searchableString,
	    String searchTerm) {
	this.searchableString = searchableString.toLowerCase();
	final List<Integer[]> list = new ArrayList<Integer[]>();
	int index = searchableString.indexOf(searchTerm.toLowerCase());
	Logger.info(tag, "indexof search tem in caps is " + index);
	while (index >= 0) {
	    final Integer result[] = new Integer[2];
	    final int startIndex = index;
	    final int endIndex = index + searchTerm.length();
	    result[0] = startIndex;
	    result[1] = endIndex;
	    list.add(result);
	    index = searchableString.indexOf(searchTerm,
		    index + searchTerm.length());
	}
	return list;
    }

    /**
     * Process search.
     *
     * @param searchContent the search content
     * @param start the start
     * @param searchTerm the search term
     * @return the list
     */
    public List<String> processSearch(String searchContent, int start,
	    String searchTerm) {
	this.searchTerm = searchTerm.toLowerCase();
	searchString = searchContent;
	Logger.warn(tag, "SearchString in process search is:" + searchString);
	Logger.warn(tag, "Search term in process search is:" + searchTerm);
	final int position = searchString.toLowerCase().indexOf(this.searchTerm);
	Logger.info(tag, "Posotion in ProcessSearch is" + position);
	if (position >= 0) {
	    searchResults.add(searchString.substring(getMin(position),
		    getMax(position)));
	    final String c = getSearchContent(searchString, getMax(position) + 1);
	    if (c != null)
		getNextIndex(c);
	    return searchResults;
	} else {
	    return searchResults;
	}
    }

    /*
     * public List<String> processSearch(String searchContent, int start, String
     * searchTerm){ this.searchTerm = searchTerm; //for(int i=0;
     * i<searchContent.size(); i++){ Logger.warn(tag,
     * "search content size is:"+searchContent.size()); searchString =
     * searchContent.get(i).getNotesDesc().toString(); Logger.warn(tag,
     * "searchString list is:"+searchString); int position =
     * searchString.indexOf(searchTerm); Logger.info(tag,
     * "Posotion in ProcessSearch list is"+position); if(position > 0){
     * searchResults.add((searchString.substring(getMin(position),
     * getMax(position)))); String c = getSearchContent(searchString,
     * getMax(position)+1); Logger.warn(tag,"c is:"+c); if(c != null)
     * getNextIndex(c); } //} return searchResults; }
     */

    /**
     * Gets the next index.
     *
     * @param content the content
     * @return the next index
     */
    private void getNextIndex(String content) {
	final int position = content.indexOf(searchTerm);
	Logger.info(tag, "Posotion in getNextIndex is" + position);
	processSearch(content, position, searchTerm);
    }

    /**
     * Gets the min.
     *
     * @param pos the pos
     * @return the min
     */
    private int getMin(int pos) {
	if (pos - SEARCH_RANGE < 0)
	    return 0;
	else
	    return pos - SEARCH_RANGE;
    }

    /**
     * Gets the max.
     *
     * @param pos the pos
     * @return the max
     */
    private int getMax(int pos) {
	if (pos + SEARCH_RANGE > searchString.length())
	    return searchString.length();
	else
	    return pos + SEARCH_RANGE;
    }

    /**
     * Gets the search content.
     *
     * @param content the content
     * @param position the position
     * @return the search content
     */
    private String getSearchContent(String content, int position) {
	if (position > content.length())
	    return null;
	else {
	    return content.substring(position, content.length());
	}

    }

}
