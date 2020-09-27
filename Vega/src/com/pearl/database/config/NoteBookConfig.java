package com.pearl.database.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.pearl.application.VegaConfigurationDetails;
import com.pearl.books.NoteBook;
import com.pearl.books.pages.NoteBookPage;
import com.pearl.database.handlers.NoteBookHandler.DatabaseHelper;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.logger.Logger;
import com.pearl.search.NoteBookSearchResult;

// TODO: Auto-generated Javadoc
/**
 * The Class NoteBookConfig.
 */
public class NoteBookConfig {

    /** The context. */
    private final Context context;
    
    /** The tag. */
    private final String tag = "NotebookConfig";
    
    /** The database helper. */
    private DatabaseHelper databaseHelper;
    
    /** The db. */
    private SQLiteDatabase db;
    
    /** The Constant tableName. */
    private static final String tableName = "noteBook";
    
    /** The notes. */
    private NoteBookPage notes;

    /**
     * Instantiates a new note book config.
     *
     * @param context the context
     */
    public NoteBookConfig(Context context) {
	this.context = context;
    }

    /**
     * Inserts row in to table.
     *
     * @param userId the user id
     * @param subject the subject
     * @param notes the notes
     * @param status the status
     * @param date the date
     * @return the int
     */
    public int insertNotes(String userId, String subject, String notes,
	    String status, String date) {
	final Uri myUri = VegaConfigurationDetails.ConfigDetails.CONTENT_URI_notebook;
	final ContentValues contentValues = new ContentValues();
	contentValues.put(VegaConfigurationDetails.ConfigDetails.UserId, userId);
	contentValues.put(VegaConfigurationDetails.ConfigDetails.SUBJECT, subject);
	contentValues.put(VegaConfigurationDetails.ConfigDetails.NOTES_TEXT, notes);
	contentValues.put(VegaConfigurationDetails.ConfigDetails.Status, status);
	contentValues.put(VegaConfigurationDetails.ConfigDetails.DATE, date);
	final Uri u = context.getContentResolver().insert(myUri, contentValues);
	final int id = (int) ContentUris.parseId(u);
	return id;
    }

    /**
     * Deletes row based on the id passed.
     *
     * @param notesId the notes id
     */
    public void delete(int notesId) {
	final Uri myUri = VegaConfigurationDetails.ConfigDetails.CONTENT_URI_notebook;
	final int deletedRowId = context.getContentResolver().delete(myUri,
		"_id=?", new String[]{String.valueOf(notesId)});
	Logger.warn(tag, "deleted row is:" + deletedRowId);
    }

    /**
     * Fetches all notes based on the id passed.
     *
     * @param notesId the notes id
     * @return the cursor
     */
    public Cursor fetchNotes(int notesId) {
	final String columns[] = new String[] { VegaConfigurationDetails.ConfigDetails.NOTES_ID, VegaConfigurationDetails.ConfigDetails.DATE
		, VegaConfigurationDetails.ConfigDetails.SUBJECT, VegaConfigurationDetails.ConfigDetails.NOTES_TEXT,
		VegaConfigurationDetails.ConfigDetails.Status};
	final Uri myUri = VegaConfigurationDetails.ConfigDetails.CONTENT_URI_notebook;
	final Cursor cur = context.getContentResolver().query(myUri, columns, "_id="+notesId,
		null, null);
	return cur;
    }

    /**
     * Fetches max id.
     *
     * @param Subject the subject
     * @return the cursor
     */
    public Cursor fetchmaxid(String Subject) {
	final String columns[] = new String[] { VegaConfigurationDetails.ConfigDetails.SUBJECT };
	final Uri myUri = VegaConfigurationDetails.ConfigDetails.CONTENT_URI_notebook;
	final Cursor cur = context.getContentResolver().query(myUri, columns,
		"subject=", null, null);
	return cur;
    }

    /**
     * Fetches all the notes for the subject passed.
     *
     * @param subject the subject
     * @return the list
     */
    public List<NoteBook> fetchNotesForSubject(String subject) {

	final List<NoteBook> list = new ArrayList<NoteBook>();
	final SimpleDateFormat sdf = new SimpleDateFormat(
		"EEE MMM dd HH:mm:ss zz yyyy");

	final String columns[] = new String[] { VegaConfigurationDetails.ConfigDetails.NOTES_ID,
		VegaConfigurationDetails.ConfigDetails.NOTES_TEXT, VegaConfigurationDetails.ConfigDetails.DATE };
	final Uri myUri = VegaConfigurationDetails.ConfigDetails.CONTENT_URI_notebook;
	final Cursor cursor = context.getContentResolver().query(myUri, columns,
		"Subject='" +subject+"'", null, null);

	if (cursor.moveToFirst()) {
	    do {
		final NoteBook noteBook = new NoteBook();
		noteBook.setId(cursor.getInt(0));
		noteBook.setNotesDescription(cursor.getString(1));
		try {
		    noteBook.setDate(sdf.parse(cursor.getString(2)));
		} catch (final ParseException e) {
		    Logger.warn(tag, "Date:" + e.toString());
		}
		list.add(noteBook);
	    } while (cursor.moveToNext());
	}
	if (cursor != null && !cursor.isClosed()) {
	    cursor.close();
	}

	return list;
    }

    /**
     * Fetches notes that matches search string.
     *
     * @param searchString the search string
     * @param subject the subject
     * @return the list
     */
    public List<NoteBookPage> fetchSearchString(String searchString,
	    String subject) {
	final List<NoteBookPage> resultList = new ArrayList<NoteBookPage>();
	List<String> formattedDesc = new ArrayList<String>();

	NoteBookSearchResult notesBookSearchResult;
	final String columns[] = new String[] {VegaConfigurationDetails.ConfigDetails.NOTES_TEXT, VegaConfigurationDetails.ConfigDetails.NOTES_ID,
		VegaConfigurationDetails.ConfigDetails.SUBJECT};
	final Uri myUri = VegaConfigurationDetails.ConfigDetails.CONTENT_URI_notebook;
	final Cursor cursor = context.getContentResolver()
		.query(myUri,
			columns,
			"Notes like "+"'%"+searchString+"%' and Subject='"+subject+"'",
			null, null);

	if (cursor.moveToFirst()) {
	    do {
		notesBookSearchResult = new NoteBookSearchResult();
		formattedDesc = notesBookSearchResult.processSearch(
			cursor.getString(0), 0, searchString);
		for (int i = 0; i < formattedDesc.size(); i++) {
		    notes = new NoteBookPage();
		    Logger.warn(tag,
			    "formatted desc is :" + formattedDesc.get(i));
		    formattedDesc.get(i);
		    notes.setId(cursor.getInt(1));
		    notes.setNotes(formattedDesc.get(i));
		    resultList.add(notes);
		}
	    } while (cursor.moveToNext());
	}
	if (cursor != null && !cursor.isClosed()) {
	    cursor.close();
	}
	return resultList;
    }

    /**
     * Fetches recently notes from the database.
     *
     * @return the string
     */
    public String fetchSubjectofRecentSavedNotes(){
		String temp="";
		try{
			String[] columns = new String[] { VegaConfigurationDetails.ConfigDetails.SUBJECT};
			Uri myUri = VegaConfigurationDetails.ConfigDetails.CONTENT_URI_notebook;
			final Cursor cursor = context.getContentResolver().query(
					myUri,
					columns,
					"DATE in (select MAX(DATE ) from " + tableName
					+")", null, null);
			if(cursor.moveToFirst()){
				do{
					temp = cursor.getString(0);
				}while(cursor.moveToNext());
			}
			if (cursor != null) {
				cursor.moveToFirst();
			}
		}catch(Exception e){
			Logger.error(tag, e);
		}
		return temp;
	}

    
    /**
     * Fetch recent saved notes.
     *
     * @return the string
     */
    public String fetchRecentSavedNotes() {
	String recentNotes = "";
	final String[] columns = new String[] { VegaConfigurationDetails.ConfigDetails.NOTES_TEXT };
	final Uri myUri = VegaConfigurationDetails.ConfigDetails.CONTENT_URI_notebook;
	final Cursor cursor = context.getContentResolver().query(
		myUri,
		columns,
		"DATE in (select MAX(DATE ) from " + tableName
		+")", null, null);
	if(cursor.moveToFirst()){
	    do{
		recentNotes = cursor.getString(0);
	    }while(cursor.moveToNext());
	}
	if (cursor != null) {
	    cursor.moveToFirst();
	}
	return recentNotes;
    }

    /**
     * updates the row with new notes.
     *
     * @param notesId the notes id
     * @param notes the notes
     * @param date the date
     * @throws ColumnDoesNoteExistsException the column does note exists exception
     */
    public void update(int notesId, String notes, String date)
	    throws ColumnDoesNoteExistsException {
	final ContentValues values = new ContentValues();
	values.put(VegaConfigurationDetails.ConfigDetails.NOTES_ID, notesId);
	values.put(VegaConfigurationDetails.ConfigDetails.NOTES_TEXT, notes);
	values.put(VegaConfigurationDetails.ConfigDetails.DATE, date);
	context.getContentResolver().update(
		VegaConfigurationDetails.ConfigDetails.CONTENT_URI_notebook,
		values, notesId+"", null);
    }

    /**
     * Fetch next.
     *
     * @param notesId the notes id
     * @param subject the subject
     * @param date the date
     * @return the list
     */
    public List<NoteBook> fetchNext(int notesId, String subject, String date) {
	final SimpleDateFormat sdf = new SimpleDateFormat(
		"EEE MMM dd HH:mm:ss zz yyyy");
	Logger.warn(tag, "date in fetchNext is:" + date);
	final List<NoteBook> list = new ArrayList<NoteBook>();
	final Cursor cursor = db.rawQuery(
		"select _id,Notes,Date from noteBook where subject='" + subject
		+ "' and Date!='" + date + "' order by Date Asc", null);
	if (cursor.moveToFirst()) {
	    do {
		final NoteBook noteBook = new NoteBook();
		noteBook.setId(cursor.getInt(0));
		noteBook.setNotesDescription(cursor.getString(1));
		try {
		    noteBook.setDate(sdf.parse(cursor.getString(2)));
		} catch (final ParseException e) {
		    Logger.info(tag, "Date :" + e.toString());
		}
		list.add(noteBook);
	    } while (cursor.moveToNext());
	}
	return list;
    }

    /**
     * Fetch previous.
     *
     * @param notesId the notes id
     * @param subject the subject
     * @param date the date
     * @return the list
     */
    public List<NoteBook> fetchPrevious(int notesId, String subject, String date) {
	Logger.warn(tag, "date in fetchPrevious is:" + date);
	final SimpleDateFormat sdf = new SimpleDateFormat(
		"EEE MMM dd HH:mm:ss zz yyyy");
	final List<NoteBook> list = new ArrayList<NoteBook>();
	final Cursor cursor = db
		.rawQuery("select _id,Notes,Date from noteBook where subject='"
			+ subject + "' and Date!='" + date
			+ "' order by Date Desc", null);
	if (cursor.moveToFirst()) {
	    do {
		final NoteBook noteBook = new NoteBook();
		noteBook.setId(cursor.getInt(0));
		noteBook.setNotesDescription(cursor.getString(1));
		try {
		    noteBook.setDate(sdf.parse(cursor.getString(2)));
		} catch (final ParseException e) {
		    Logger.info(tag, "Date :" + e.toString());
		}
		list.add(noteBook);
	    } while (cursor.moveToNext());
	}
	return list;
    }

}
