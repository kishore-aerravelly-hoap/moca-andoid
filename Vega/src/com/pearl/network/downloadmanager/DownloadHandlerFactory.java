package com.pearl.network.downloadmanager;

import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.downloadhandlers.BookDownloadHandler;
import com.pearl.network.downloadmanager.downloadhandlers.BooksListDownloadHandler;
import com.pearl.network.downloadmanager.downloadhandlers.DefaultDownloadHandler;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadType;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating DownloadHandler objects.
 */
public class DownloadHandlerFactory {
    
    /**
     * Gets the single instance of DownloadHandlerFactory.
     *
     * @param download the download
     * @param application the application
     * @return single instance of DownloadHandlerFactory
     */
    public static DownloadHandlerInterface getInstance(Download download,
	    ApplicationData application) {
	/**
	 * TODO If possible make below operations generic..
	 **/
	Logger.info("DownloadHandlerFactory",
		"Preparing to load Download handler of type "
			+ download.getDownloadType().getType());

	if (DownloadType.BOOK_LIST.equals(download.getDownloadType().getType())) {
	    return new BooksListDownloadHandler(application);
	} else if (DownloadType.BOOK.equals(download.getDownloadType()
		.getType())) {
	    return new BookDownloadHandler(application);
	} else {
	    Logger.warn("DownloadHandlerFactory",
		    "Loading Default Download Handler");

	    // Default download functionality
	    return new DefaultDownloadHandler(application);
	}
    }
}