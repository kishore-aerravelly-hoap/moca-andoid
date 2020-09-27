package com.pearl.search;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang3.StringUtils;

import android.text.Html;
import android.util.Log;

import com.pearl.books.pages.exception.InvalidSearchTermException;
import com.pearl.epub.TableOfContent;
import com.pearl.epub.TableOfContent.Chapter;
import com.pearl.epub.core.EpubEngine;
import com.pearl.epub.exception.EbookException;

// TODO: Auto-generated Javadoc
/**
 * The Class Search.
 */
public class Search {

    /** The search results. */
    private static ArrayList<SearchResult> searchResults;

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
	    String basePath, TableOfContent toc, String salt) throws EbookException {
	try {
	    final int maxTOC = toc.getChapterList().size();
	    int page = 1;
	    searchResults = new ArrayList<SearchResult>();

	    for (int i = 0; i < maxTOC; i++) {
		final Chapter chapter = toc.getChapter(i);
		final String chapterPath = basePath + "/" + chapter.getUrl();
		String html = null;
			try {
				html = EpubEngine.preprocess(chapterPath, "", "", null,
					salt);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		final String[] pages = StringUtils.splitByWholeSeparator(html,
			"<div class='page'>");

		for (int j = 1; j < pages.length; j++) {
		    String pageContent = pages[j];
		    pageContent = Html.fromHtml(pageContent).toString();
		    searchResults.addAll(getSearchResultsInPage(i + 1, page,
			    pageContent, searchTerm));
		    page++; // increment page count
		}
	    }
	} catch (final InvalidSearchTermException exception) {

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
	if (pageContent.contains(searchTerm)) {
	    final String content = pageContent;

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
		final SearchResult searchResult = new SearchResult(chapterNo, pageNo,
			finalDesc);
		pageSearchResult.add(searchResult);
		findPos = content.indexOf(searchTerm, findPos + 1);
	    }
	} else {
	    Log.i("Page->searchResults", "Page -  does not contain '"
		    + searchTerm + "'");
	}

	return pageSearchResult;
    }

}