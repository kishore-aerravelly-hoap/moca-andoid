/**
 * 
 */
package com.pearl.activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.application.VegaConfiguration;
import com.pearl.database.handlers.BookmarkHandler;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class BookmarksActivity.
 */
public class BookmarksActivity extends BaseActivity {

    /** The bookmark desc. */
    private String bookmarkDesc;
    
    /** The bookmark id. */
    private int bookmarkId;
    
    /** The page no. */
    private int pageNo;
    
    /** The book id. */
    private int bookId;
    
    /** The date. */
    private String date;
    
    /** The bookmark handler. */
    private BookmarkHandler bookmarkHandler;
    
    /** The desc. */
    EditText desc;
    
    /** The bundle. */
    Bundle bundle;

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.bookmarks);
	vegaConfig = new VegaConfiguration(this);
	bookmarkHandler = new BookmarkHandler(this);
	desc = (EditText) findViewById(R.id.createBookmarkDesc);
	bundle = this.getIntent().getExtras();
	if (bundle != null) {
	    pageNo = bundle.getInt("PageNo");
	    bookId = bundle.getInt("bookId");
	    bookmarkDesc = bundle.getString("BookmarkDesc");
	    bookmarkId = bundle.getInt("Row Id");
	} else {
	    Logger.info(tag, "Bundle is null");
	}
	final SimpleDateFormat currentTime = new SimpleDateFormat(
		"EEE MMM dd HH:mm:ss zz yyyy");
	;
	date = currentTime.format(new Date());
	Logger.verbose(tag, "date in on create is:" + date);
	setColoumn1(bookId + "");
	setColoumn2(pageNo + "");
	desc.setText(bookmarkDesc);
	desc.setSelection(desc.getText().length());
	setFinishOnTouchOutside(true);
	overridePendingTransition(R.anim.grow_from_bottomright_to_topleft,
		R.anim.shrink_from_topleft_to_bottomright);
    }

    /**
     * Called to bookmark the page when leaving by tapping outside.
     */
    @Override
    public void onPause() {
	super.onPause();
	bookmarkDesc = desc.getText().toString();
	if (bookmarkId == 0) {
	    Logger.info(tag, "bookmarkDesc is" + bookmarkDesc);
	    saveBookmark(pageNo, bookId, bookmarkDesc, date);
	} else {
	    Logger.info(tag, "bookmarkId is" + bookmarkId);
	    updateBookmarkDesc(bookmarkId, pageNo, bookId, bookmarkDesc, date);
	}
	finish();
    }

    /**
     * Marks the page as bookmarked and saves the description the user has
     * created for the bookmark.
     *
     * @param pageNo the page no
     * @param bookId the book id
     * @param bookmarkDesc the bookmark desc
     * @param date the date
     */
    private void saveBookmark(int pageNo, long bookId, String bookmarkDesc,
	    String date) {

	if (!bookmarkDesc.trim().equalsIgnoreCase("")&&desc.toString().trim().length()!=0) {
	    Logger.warn(tag, "bookmarkDesc is" + bookmarkDesc);
	    Logger.warn(tag, "BOOKMARKACTIVITY - page no  is" + pageNo);
	    bookmarkHandler.toggleStatus(bookId, pageNo, bookmarkDesc, date);

	    Toast.makeText(this, R.string.bookmarkSaved, toastDisplayTime)
	    .show();
	} else {
	    Toast.makeText(this, R.string.bookmarkAlert, toastDisplayTime)
	    .show();
	}

    }

    /**
     * Update bookmark desc.
     *
     * @param id the id
     * @param pageNo the page no
     * @param bookId the book id
     * @param bookmarkDesc the bookmark desc
     * @param date the date
     */
    public void updateBookmarkDesc(int id, int pageNo, long bookId,
	    String bookmarkDesc, String date) {
	final String updatedBookmarkDesc = desc.getText().toString();
	if (!"".equalsIgnoreCase(updatedBookmarkDesc)) {
	    bookmarkHandler.update(id, pageNo, bookId, updatedBookmarkDesc,
		    date);
	} else {
	    bookmarkHandler.delete(bookId, pageNo);
	}
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onDestroy()
     */
    @Override
    public void onDestroy() {
	super.onDestroy();
	bookmarkHandler.close();
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "bookmarks_created";
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
     */
    @Override
    public void onNetworkAvailable() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
     */
    @Override
    public void onNetworkUnAvailable() {
	// TODO Auto-generated method stub

    }

}
