package com.pearl.epub.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;

import com.pearl.books.pages.exception.InvalidSearchTermException;
import com.pearl.epub.TableOfContent;
import com.pearl.epub.TableOfContent.Chapter;
import com.pearl.epub.core.EpubEngine;
import com.pearl.epub.exception.EbookException;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ResponseStatus;

// TODO: Auto-generated Javadoc
/**
 * The Class Search.
 */
public class Search extends AsyncTask<String, Integer, ArrayList<SearchResult>>{

    /** The search results. */
    private static ArrayList<SearchResult> searchResults;
    TableOfContent toc;
    private DownloadCallback dc;
    
    public Search(String searchTerm,
    	    String basePath, TableOfContent toc, String salt, DownloadCallback dc){
    	this.toc = toc;
    	this.dc = dc;
    	execute(searchTerm, basePath, salt);
    }

    /**
     * Do search.
     *
     * @param searchTerm the search term
     * @param basePath the base path
     * @param toc the toc
     * @param salt the salt
     * @return the array list
     */
    public static ArrayList<SearchResult> doSearch(String searchTerm,
	    String basePath, TableOfContent toc, String salt) {
	try {
	    final int maxTOC = toc.getChapterList().size();
	    int page = 1;
	    searchResults = new ArrayList<SearchResult>();
	    int k = 0;
	    for (int i = 0; i < maxTOC; i++) {
		final Chapter chapter = toc.getChapter(i);
		final String chapterPath = basePath + "/" + chapter.getUrl();
		final String html = EpubEngine.preprocess(chapterPath, "", "", null,salt);
		final List<String> pages = Arrays.asList(org.apache.commons.lang3.StringUtils.splitByWholeSeparator(html, "</h6>"));

		if(k>0){
		    page+=1;
		}else{
		    k=0;
		}

		for (int j = 0; j < pages.size()-1; j++) {
		    String pageContent = pages.get(j);
		    pageContent = Html.fromHtml(pageContent).toString();
		    searchResults.addAll(getSearchResultsInPage(i + 1, page,pageContent, searchTerm));
		    page++; // increment page count
		}
		k++;
		page--;
		//page=page+1;
	    }
	} catch(final EbookException eBookException) {

	} catch(final Exception  exception) {

	}

	return searchResults;
    }

    /**
     * Gets the search results in page.
     *
     * @param chapterNo the chapter no
     * @param pageNo the page no
     * @param pageContent the page content
     * @param searchTerm the search term
     * @return the search results in page
     * @throws InvalidSearchTermException the invalid search term exception
     */
    public static ArrayList<SearchResult> getSearchResultsInPage(int chapterNo,
	    int pageNo, String pageContent, String searchTerm)
		    throws InvalidSearchTermException {

	if (searchTerm == null || "".equals(searchTerm))
	    throw new InvalidSearchTermException(
		    "You can't have an empty search term");

	final ArrayList<SearchResult> pageSearchResult = new ArrayList<SearchResult>();

	// Do string operations and populate the arraylist !
	if (pageContent.toLowerCase().contains(searchTerm.toLowerCase())) {
	    final String content = pageContent.toLowerCase();

	    int findPos = content.indexOf(searchTerm.toLowerCase());

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
		final SearchResult searchResult = new SearchResult(chapterNo, pageNo,
			finalDesc);
		pageSearchResult.add(searchResult);
		findPos = content.indexOf(searchTerm.toLowerCase(), findPos + 1);
	    }
	} else {
	    Log.i("Page->searchResults", "Page " + pageNo+"-  does not contain '"
		    + searchTerm + "'");
	}

	return pageSearchResult;
    }

	@Override
	protected ArrayList<SearchResult> doInBackground(String... params) {
		ArrayList<SearchResult> list = new ArrayList<SearchResult>();
		//ArrayList<SearchResult> list = doSearch(params[0], params[1], toc, params[2]);
		dc.onSuccess(ResponseStatus.SUCCESS);
		return list;
	}

}