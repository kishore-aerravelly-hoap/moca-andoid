package com.pearl.database.handlers;

/**
 * 
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.pearl.books.pages.Note;
import com.pearl.database.DatabaseHandler;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class NotesHandler.
 */
public class NotesHandler implements DatabaseHandler {

    /** The Constant databaseName. */
    private static final String databaseName = "notes.db";
    
    /** The Constant tableName. */
    private static final String tableName = "createNotes";
    
    /** The Constant databaseVersion. */
    private static final int databaseVersion = 5;
    
    /** The Constant ROW_ID. */
    public static final String ROW_ID = "_id";
    
    /** The Constant PAGE_NO. */
    public static final String PAGE_NO = "pageNo";
    
    /** The Constant NOTES_DESC. */
    public static final String NOTES_DESC = "notesDesc";
    
    /** The Constant DATE. */
    public static final String DATE = "date";
    
    /** The Constant HIGHLIGHTED_TEXT. */
    public static final String HIGHLIGHTED_TEXT = "highlightedText";
    
    /** The Constant BOOK_ID. */
    public static final String BOOK_ID = "bookId";
    
    /** The Constant TAG. */
    private static final String TAG = "NotesHandler";
    
    /** The context. */
    private final Context context;
    
    /** The db. */
    private final SQLiteDatabase db;
    
    /** The insert stmt. */
    private final SQLiteStatement insertStmt;
    
    /** The database helper. */
    DatabaseHelper databaseHelper;
    
    /** The Constant insert. */
    private static final String insert = "insert into "
	    + tableName
	    + "(bookId, pageNo, notesDesc, highlightedText, Date) values(?,?,?,?,?)";

    /**
     * Constructor used to instantiate the database.
     *
     * @param context the context
     */
    public NotesHandler(Context context) {
	this.context = context;
	databaseHelper = new DatabaseHelper(this.context);
	db = databaseHelper.getWritableDatabase();
	insertStmt = db.compileStatement(insert);
    }

    /**
     * Inserts notes description the user has written along with the specified
     * parameters in the database.
     *
     * @param bookId the book id
     * @param pageNo the page no
     * @param notesDesc the notes desc
     * @param highlightedText the highlighted text
     * @param date the date
     * @return the long
     */
    public long insertNotes(long bookId, int pageNo, String notesDesc,
	    String highlightedText, String date) {
	insertStmt.bindLong(1, bookId);
	insertStmt.bindLong(2, pageNo);
	insertStmt.bindString(3, notesDesc);
	insertStmt.bindString(4, highlightedText);
	insertStmt.bindString(5, date);
	return insertStmt.executeInsert();
    }

    /**
     * Fetches only the specified rows depending on the condition provided for a
     * book.
     *
     * @return cursor that contains the data that is relevant
     */
    /*
     * public Cursor fetch(long bookId) { Cursor cursor =
     * this.db.query(tableName, new String[] { "_id", "pageNo", "notesDesc",
     * "highlightedText", "date" }, "bookId=?", new String[] {
     * String.valueOf(bookId) }, null, null, "pageNo"); if (cursor != null) {
     * cursor.moveToFirst(); } return cursor;
     * 
     * }
     */

    /**
     * Fetches all the rows from the database
     * 
     * @return cursor that contains whole data
     */
    public Cursor fetchAll() {
	final Cursor cursor = db.query(tableName, new String[] { "_id",
		"pageNo", "notesDesc", "highlightedText", "date" }, null, null,
		null, null, "pageNo");
	return cursor;
    }

    /**
     * Update.
     *
     * @param id the id
     * @param pageNo the page no
     * @param bookId the book id
     * @param notesDesc the notes desc
     * @param date the date
     */
    public void update(int id, int pageNo, long bookId, String notesDesc,
	    String date) {
	final ContentValues values = new ContentValues();
	values.put("pageNo", pageNo);
	values.put("bookId", bookId);
	values.put("notesDesc", notesDesc);
	values.put("date", date);
	db.update(tableName, values, "_id=" + id, null);
    }

    /**
     * Fetch.
     *
     * @param bookId the book id
     * @return the list
     */
    public List<Note> fetch(long bookId) {

	final List<Note> list = new ArrayList<Note>();
	final Cursor cursor = db.query(tableName, new String[] { "_id",
		"pageNo", "notesDesc", "highlightedText", "date" }, "bookId=?",
		new String[] { String.valueOf(bookId) }, null, null, "pageNo");
	if (cursor.moveToFirst()) {
	    do {
		final Note notes = new Note();
		notes.setId(cursor.getInt(0));
		notes.setDescription(cursor.getString(2));
		notes.setPageNum(cursor.getInt(1));
		Logger.info(TAG, "pageNumber is:" + cursor.getInt(1));
		notes.setDate(cursor.getString(4));
		list.add(notes);
	    } while (cursor.moveToNext());
	}
	if (cursor != null && !cursor.isClosed()) {
	    cursor.close();
	}
	Logger.info(TAG, "list size is:" + list.size());
	return list;

    }

    /**
     * Deletes the notes.
     *
     * @param bookId the book id
     * @param pageNo the page no
     * @param id the id
     */

    public void delete(long bookId, int pageNo, int id) {
	db.delete(tableName, "bookId =? AND pageNo=? AND _id=?",
		new String[] { String.valueOf(bookId), String.valueOf(pageNo), String.valueOf(id)});
    }

    /**
     * Upadtes the notes Description.
     *
     * @param bookId the book id
     * @param pageNo the page no
     * @param notesDesc the notes desc
     */
    public void update(long bookId, int pageNo, String notesDesc) {

	/*
	 * db.update(tableName, null, "bookId = ? AND pageNo=? AND notesDesc=?",
	 * new String[] { String.valueOf(bookId), String.valueOf(pageNo),
	 * String.valueOf(notesDesc)});
	 */
    }

    /**
     * Need to be called to close the database connection that was opened for
     * doing transaction.
     */

    @Override
    public void close() {
	// if (databaseHelper != null)
	// databaseHelper.close();
	// this.db.close();
    }

    /**
     * Gets the columns.
     *
     * @param db the db
     * @param tableName the table name
     * @return the columns
     */
    public static List<String> getColumns(SQLiteDatabase db, String tableName) {
	List<String> listOfColumns = null;
	Logger.verbose("NotesHandler", "tableName in getColumns()" + tableName);
	Cursor cursor = null;
	try {
	    cursor = db.rawQuery("select * from " + tableName + " limit 1",
		    null);
	    // cursor = db.query(tableName, null, null, null, null, null, null);
	    if (cursor != null) {
		listOfColumns = new ArrayList<String>(Arrays.asList(cursor
			.getColumnNames()));
	    }
	} catch (final Exception e) {
	    Logger.error("NotesHandler", e.toString());
	} finally {
	    if (cursor != null)
		cursor.close();
	}
	return listOfColumns;
    }

    /**
     * Inner class for interacting with database in code.
     */
    public static class DatabaseHelper extends SQLiteOpenHelper {
	
	/**
	 * Instantiates a new database helper.
	 *
	 * @param context the context
	 */
	DatabaseHelper(Context context) {
	    super(context, databaseName, null, databaseVersion);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
	    Logger.verbose("NotesHandler", "In onCreate");
	    db.execSQL("CREATE TABLE "
		    + tableName
		    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, bookId INTEGER, pageNo INTEGER, notesDesc TEXT,"
		    + "highlightedText TEXT, date TEXT)");
	}

	/**
	 * Used to upgarde the database version.
	 *
	 * @param db the db
	 * @param oldVersion the old version
	 * @param newVersion the new version
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Logger.verbose("NotesHandler", "Old version" + oldVersion);
	    Logger.verbose("NotesHandler", "new version" + newVersion);
	    Logger.verbose("NotesHandler",
		    "Upgrading database, this will drop tables and recreate.");
	    final List<String> columns = BookmarkHandler.getColumns(db, tableName);
	    Logger.verbose("NotesHandler", "tableName is:" + tableName);
	    db.execSQL("ALTER table " + tableName + " RENAME TO temp_"
		    + tableName);
	    Logger.verbose("NotesHandler", "tableName after altering is:"
		    + tableName);
	    db.execSQL("CREATE TABLE "
		    + tableName
		    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, bookId INTEGER, pageNo INTEGER,  notesDesc TEXT, highlightedText TEXT, date TEXT)");
	    columns.retainAll(NotesHandler.getColumns(db, tableName));
	    final String cols = StringUtils.join(columns, ",");
	    db.execSQL(String.format(
		    "INSERT INTO %s (%s) SELECT %s from temp_%s", tableName,
		    cols, cols, tableName));
	    db.execSQL("DROP table temp_" + tableName);

	}
    }
}
