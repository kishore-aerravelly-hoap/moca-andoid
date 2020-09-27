package com.pearl.books.readers.handlers;

import java.util.List;

import com.pearl.books.BookMetaData;
import com.pearl.books.CoverImage;
import com.pearl.books.pages.Page;

// TODO: Auto-generated Javadoc
/**
 * The Interface BookReaderHandlerInterface.
 */
public interface BookReaderHandlerInterface {
    
    /**
     * Open book.
     *
     * @return true, if successful
     */
    boolean openBook();

    /**
     * Gets the pages count.
     *
     * @return the pages count
     */
    int getPagesCount();

    /**
     * Gets the cover image.
     *
     * @return the cover image
     */
    CoverImage getCoverImage(); // --

    /**
     * Gets the meta data.
     *
     * @return the meta data
     */
    BookMetaData getMetaData();

    /**
     * Gets the page at pos.
     *
     * @param pageNo the page no
     * @return the page at pos
     */
    Page getPageAtPos(int pageNo);

    /**
     * Gets the pages.
     *
     * @return the pages
     */
    List<Page> getPages();

    /**
     * Gets the resources path.
     *
     * @return the resources path
     */
    String getResourcesPath();

    /**
     * Close book.
     *
     * @return true, if successful
     */
    boolean closeBook();
}