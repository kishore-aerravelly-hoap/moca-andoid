/**
 * 
 */
package com.pearl.activities;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.application.VegaConfiguration;
import com.pearl.database.handlers.NotesHandler;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class NotesActivity.
 */
public class NotesActivity extends BaseActivity {

    /** The page no. */
    private int pageNo;
    
    /** The book id. */
    private long bookId;
    
    /** The notes desc. */
    private String notesDesc;
    
    /** The date. */
    private String date;
    
    /** The notes id. */
    private int notesId;
    
    /** The highlighted text. */
    private final String highlightedText = "Text";
    
    /** The notes handler. */
    private NotesHandler notesHandler;
    // EditText notes;
    /** The tag. */
    private final String tag = "NotesActivity";
    
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
	Logger.warn(tag, "NOTES - in notes onCreate()");
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.notes);
	vegaConfig = new VegaConfiguration(this);
	notesHandler = new NotesHandler(this);
	// notes = (EditText) findViewById(R.id.createNotes);
	desc = (EditText) findViewById(R.id.createNotes);
	desc.setSelection(desc.getText().length());

	bundle = this.getIntent().getExtras();
	if (bundle != null) {
	    pageNo = bundle.getInt("PageNo");
	    bookId = bundle.getInt("bookId");
	    notesDesc = bundle.getString("NotesDesc");
	    notesId = bundle.getInt("Row Id");
	    Logger.info(tag, "bookId is:" + bookId);
	    Logger.info(tag, "date in onCreate() is:" + date);
	    Logger.info(tag, "notes id is: " + notesId);
	}
	Logger.warn(tag, "Notes Desc is:" + notesDesc);
	final SimpleDateFormat currentTime = new SimpleDateFormat(
		"EEE MMM dd HH:mm:ss zz yyyy");
	;
	date = currentTime.format(new Date());
	Logger.verbose(tag, "date in on create is:" + date);
	setColoumn1(bookId + "");
	setColoumn2(pageNo + "");
	desc.setText(notesDesc);
	setFinishOnTouchOutside(true);
    }

    /**
     * Called to save notes when tapped outside.
     */
    @Override
    public void onStop() {
	super.onStop();
	notesDesc = desc.getText().toString();
	if (notesId == 0) {
	    saveNotes(bookId, notesDesc, highlightedText, pageNo, date);

	    Logger.info(tag, "inserted new note record as " + bookId + "-"
		    + notesDesc + "-" + highlightedText + "-" + pageNo + "-"
		    + date);
	} else {
	    Logger.info(tag, "notesId is" + notesId);
	    updateNotesDesc(notesId, pageNo, bookId, notesDesc, date);

	    Logger.info(tag, "updated note record with id " + notesId + " as "
		    + bookId + "-" + notesDesc + "-" + highlightedText + "-"
		    + pageNo + "-" + date);
	}
	if (notesHandler != null)
	    notesHandler.close();
	// finish();
	Logger.info(tag, "NOTES - in notes onStop()");
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	Logger.warn(tag, "NOTES - in notes onResume()");
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onStart()
     */
    @Override
    public void onStart() {
	super.onStart();
	Logger.warn(tag, "NOTES - in notes onStart()");
    }

    /**
     * Saves the notes the user has created for a particular page.
     *
     * @param bookId the book id
     * @param notesDesc the notes desc
     * @param highlightedText the highlighted text
     * @param pageNo the page no
     * @param date the date
     * @throws SQLiteException the sQ lite exception
     */
    private void saveNotes(long bookId, String notesDesc,
	    String highlightedText, int pageNo, String date)
		    throws SQLiteException {
	try {
	    if (!"".equalsIgnoreCase(notesDesc.trim())&& desc.toString().trim().length()!=0) {
		notesHandler = new NotesHandler(this);

		notesHandler.insertNotes(bookId, pageNo, notesDesc,
			highlightedText, date);
		Toast.makeText(this, R.string.notesSaved, toastDisplayTime)
		.show();
	    } else {

		Toast.makeText(this, R.string.notesAlert, toastDisplayTime)
		.show();
	    }
	} catch (final Exception e) {
	    throw new SQLiteException(e.toString());
	}
    }

    /**
     * Update notes desc.
     *
     * @param id the id
     * @param pageNo the page no
     * @param bookId the book id
     * @param notesDesc the notes desc
     * @param date the date
     */
    public void updateNotesDesc(int id, int pageNo, long bookId,
	    String notesDesc, String date) {
	final String updatedNotesDesc = desc.getText().toString();
	Logger.info(tag, "updated bookmark description: " + updatedNotesDesc);
	Logger.info(tag, "page No in update: " + pageNo);
	Logger.info(tag, "bookId in update: " + bookId);
	Logger.info(tag, "id in update : " + id);
	Logger.info(tag, "date in update :" + date);
	if (!"".equalsIgnoreCase(updatedNotesDesc)) {
	    notesHandler.update(id, pageNo, bookId, updatedNotesDesc, date);
	} else {
	    notesHandler.delete(bookId, pageNo, notesId);
	}
    }

    /**
     * Called to close the opened database connections.
     *
     * @return the activity type
     */

    @Override
    public String getActivityType() {
	return "notes_created";
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
