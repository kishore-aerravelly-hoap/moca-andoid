package com.pearl.parsers.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.application.ApplicationData;
import com.pearl.booklist.BookList;
import com.pearl.books.Book;
import com.pearl.logger.Logger;
import com.pearl.ui.models.BaseResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class BookListParser.
 */
public class BookListParser {

    /** The Constant tag. */
    private static final String tag = "BookListParser";

    /**
     * Gets the books list.
     *
     * @param filePath the file path
     * @return the books list
     * @throws JsonParseException the json parse exception
     * @throws JsonMappingException the json mapping exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static BookList getBooksList(String filePath)
	    throws JsonParseException, JsonMappingException, IOException {
	BookList booksList = new BookList();
	final ObjectMapper mapper = new ObjectMapper();
	BaseResponse jsonResponse = null;

	Logger.warn(
		tag,
		"Available books list content is:"
			+ ApplicationData.readFile(filePath));

	jsonResponse = mapper.readValue(new File(filePath), BaseResponse.class);

	final String jsonData = jsonResponse.getData();
	Logger.warn(tag, "--- message is:" + jsonResponse.getMessage());
	if (jsonData != null) {
	    Logger.warn(tag, "Json form server is:" + jsonData);
	    final ObjectReader reader = mapper.reader(BookList.class);
	    try {
		Logger.warn(
			tag,
			"reader value is:"
				+ reader.readValue(jsonData.toString()));
		booksList = reader.readValue(jsonData.toString());
	    } catch (final JsonProcessingException e) {
		Logger.error(tag, e);
	    } catch (final IOException e) {
		Logger.error(tag, e);
	    }
	} else {
	    Logger.warn(tag, "json data is null");
	}
	return booksList;
    }

    /**
     * Gets the book list.
     *
     * @param searchString the search string
     * @param filePath the file path
     * @return the book list
     * @throws JsonParseException the json parse exception
     * @throws JsonMappingException the json mapping exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static BookList getBookList(String searchString, String filePath)
	    throws JsonParseException, JsonMappingException, IOException {
	Logger.info(tag, "SHELF SEARCH - in getBooksList(1,2)");
	Logger.warn(tag, "SHELF SEARCH - search string is:" + searchString);
	final BookList booksList = getBooksList(filePath);
	final ArrayList<Book> searchList = new ArrayList<Book>();
	// loop through the books list
	Logger.info(tag, "SHELF SEARCH - booksList size is:"
		+ booksList.getBookList().size());

	if (booksList.getBookList().size() != 0) {
	    for (int i = 0; i < booksList.getBookList().size(); i++) {
		Logger.error(tag, "SHELF SEARCH - -- FOR");
		Logger.warn(tag, "SHELF SEARCH - title is:"
			+ booksList.getBookList().get(i).getMetaData()
			.getTitle());
		if (booksList.getBookList().get(i).getMetaData().getTitle()
			.toLowerCase().contains(searchString.toLowerCase())) {
		    Logger.warn(tag, "SHELF SEARCH - title in IF is:"
			    + booksList.getBookList().get(i).getMetaData()
			    .getTitle());

		    searchList.add(booksList.getBookList().get(i));
		}
	    }
	}
	booksList.setBookList(searchList);
	Logger.warn(tag, "SHELF SEARCH - book list size after searching is:"
		+ booksList.getBookList().size());
	Logger.warn(tag,
		"SHELF SEARCH - Search list size is:" + searchList.size());
	for (int i = 0; i < searchList.size(); i++) {
	    Logger.warn(tag,
		    "SHELF SEARCH - Search list is:"
			    + searchList.get(i).getBookId());
	}

	return booksList;
    }
}
