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

import com.pearl.books.pages.BookMark;
import com.pearl.database.DatabaseHandler;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class BookmarkHandler.
 */
public class BookmarkHandler implements DatabaseHandler {

    /** The Constant databaseName. */
    private static final String databaseName = "bookmark.db";
    
    /** The Constant tableName. */
    private static final String tableName = "bookmarkStatus";
    
    /** The Constant databaseVersion. */
    private static final int databaseVersion = 6;
    
    /** The Constant ROW_ID. */
    public static final String ROW_ID = "_id";
    
    /** The Constant PAGE_NO. */
    public static final String PAGE_NO = "pageNo";
    
    /** The Constant BOOKMARK_DESC. */
    public static final String BOOKMARK_DESC = "bookmarkDesc";
    
    /** The Constant DATE. */
    public static final String DATE = "date";
    
    /** The Constant BOOK_ID. */
    public static final String BOOK_ID = "bookId";
    
    /** The Constant insert. */
    private static final String insert = "insert into " + tableName
	    + "(bookId, pageNo, bookmarkDesc, date) values (?,?,?,?)";
    
    /** The tag. */
    private final String TAG = "BookmarkHandler";
    
    /** The context. */
    private final Context context;
    
    /** The db. */
    private final SQLiteDatabase db;
    
    /** The insert stmt. */
    private final SQLiteStatement insertStmt;
    // private BookMark bookmark;
    /** The database helper. */
    DatabaseHelper databaseHelper;

    /**
     * Constructor used to instantiate the database.
     *
     * @param context the context
     */
    public BookmarkHandler(Context context) {
	this.context = context;
	databaseHelper = new DatabaseHelper(this.context);
	db = databaseHelper.getWritableDatabase();
	insertStmt = db.compileStatement(insert);
    }

    /**
     * Inserts the description the user has written along with the specified
     * parameters in the database.
     *
     * @param bookId the book id
     * @param pageNo the page no
     * @param bookmarkDesc the bookmark desc
     * @param date the date
     * @return the long
     */
    public long insertBookmarkDesc(long bookId, int pageNo,
	    String bookmarkDesc, String date) {
	insertStmt.bindLong(1, bookId);
	insertStmt.bindLong(2, pageNo);
	insertStmt.bindString(3, bookmarkDesc);
	insertStmt.bindString(4, date);
	return insertStmt.executeInsert();
    }

    /**
     * Removes the bookmark for a page which was bookmarked earlier.
     *
     * @param bookId the book id
     * @param pageNo the page no
     */
    public void delete(long bookId, int pageNo) {
	db.delete(tableName, "bookId =? AND pageNo=?",
		new String[] { String.valueOf(bookId), String.valueOf(pageNo) });

    }

    /**
     * Removes all the bookmarks.
     */
    public void deleteAll() {
	db.delete(tableName, null, null);
    }

    /**
     * Fetches only the specified rows(the bookmarked pages) depending on the
     * condition provided for a book.
     *
     * @param bookId the book id
     * @return list that contains the data that is relevant
     */

    public List<BookMark> fetch(long bookId) {

	final List<BookMark> list = new ArrayList<BookMark>();
	final Cursor cursor = db.query(tableName, new String[] { "_id",
		"pageNo", "bookmarkDesc", "date" }, "bookId=?",
		new String[] { String.valueOf(bookId) }, null, null, "pageNo");
	if (cursor.moveToFirst()) {
	    do {
		final BookMark bookmark = new BookMark();
		bookmark.setId(cursor.getInt(0));
		bookmark.setDescription(cursor.getString(2));
		bookmark.setPageNum(cursor.getInt(1));
		Logger.info(TAG, "pageNumber is:" + cursor.getInt(1));
		bookmark.setDate(cursor.getString(3));

		list.add(bookmark);
	    } while (cursor.moveToNext());
	}
	if (cursor != null && !cursor.isClosed()) {
	    cursor.close();
	}
	Logger.info(TAG, "list size is:" + list.size());
	return list;

    }

    /**
     * Fetch bookmark desc.
     *
     * @param bookId the book id
     * @param pageNo the page no
     * @return the cursor
     */
    public Cursor fetchBookmarkDesc(long bookId, int pageNo) {
	final Cursor cursor = db.query(tableName, new String[] { "_id",
	"bookmarkDesc" }, "bookId=? AND pageNo=?", new String[] {
		String.valueOf(bookId), String.valueOf(pageNo) }, null, null,
		null);
	if (cursor != null) {
	    cursor.moveToFirst();
	} else {
	    Logger.warn(TAG, "trying to access an empty table");
	}
	return cursor;
    }

    /**
     * Fetches all the rows from the database.
     *
     * @return cursor that contains whole data
     */
    public Cursor fetchAll() {
	final Cursor cursor = db.query(tableName, null, null, null, null, null,
		null);
	return cursor;
    }

    /**
     * Update.
     *
     * @param id the id
     * @param pageNo the page no
     * @param bookId the book id
     * @param bookmarkDesc the bookmark desc
     * @param date the date
     */
    public void update(int id, int pageNo, long bookId, String bookmarkDesc,
	    String date) {
	final ContentValues values = new ContentValues();
	values.put("pageNo", pageNo);
	values.put("bookId", bookId);
	values.put("bookmarkDesc", bookmarkDesc);
	values.put("date", date);
	db.update(tableName, values, "_id=" + id, null);
    }

    /**
     * Depending on the status specific operation is performed (whether to
     * insert the page if not bookmarked or to delete the page if already
     * bookmarked.
     *
     * @param bookId the book id
     * @param pageNo the page no
     * @param bookmarkDesc the bookmark desc
     * @param date the date
     * @return status
     */
    public boolean toggleStatus(long bookId, int pageNo, String bookmarkDesc,
	    String date) {
	final boolean bookmarked = getBookmarkStatus(bookId, pageNo);

	if (bookmarked) {
	    Logger.warn("toggle status", "already bookmarked");
	    delete(bookId, pageNo);
	} else {
	    Logger.warn("toggle status", "not bookmarked");
	    insertBookmarkDesc(bookId, pageNo, bookmarkDesc, date);
	}
	return true;
    }

    /**
     * Toggle status.
     *
     * @param bookId the book id
     * @param pageNo the page no
     * @return true, if successful
     */
    public boolean toggleStatus(long bookId, int pageNo) {
	final boolean bookmarked = getBookmarkStatus(bookId, pageNo);
	if (bookmarked) {
	    return true;
	} else
	    return false;
    }

    /**
     * Checks the bookmark status i.e.,whether the page has been has been
     * already bookmarked or not
     *
     * @param bookId the book id
     * @param pageNo the page no
     * @return the status
     */
    public boolean getBookmarkStatus(long bookId, int pageNo) {
	Logger.warn("BookmarkHandler", "getBookmarkstatus");
	Logger.warn("BookmarkHandler", "Page No is: " + pageNo);

	/*
	 * db .rawQuery("select bookId, pageNo, bookmarkDesc from " + tableName
	 * + " where Subject=?)", new String[] { String.valueOf(subject) });
	 */
	final Cursor cursor = db
		.query(tableName,
			new String[] { "bookId", "pageNo" },
			"bookId =? AND pageNo=?",
			new String[] { String.valueOf(bookId),
			String.valueOf(pageNo) }, null, null, null);
	if (cursor.moveToFirst()) {
	    do {
		Logger.warn("Bookmarkhandler", "in if");
		return true; // as we have the record, it is bookmarked
	    } while (cursor.moveToNext());
	} else {
	    // The table has no rows
	}
	if (cursor != null && !cursor.isClosed()) {
	    cursor.close(); // should raise exception over here, for now return
	    // false
	}
	return false;
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
	Logger.verbose("BookmarkHandler", "tableName in getColumns()"
		+ tableName);
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
	    Logger.error("BookmarkHandler", e.toString());
	} finally {
	    if (cursor != null)
		cursor.close();
	}
	return listOfColumns;
    }

    /**
     * Need to be called to close the database connection that was opened for
     * doing transaction.
     */
    @Override
    public void close() {
	/*
	 * this.db.close(); databaseHelper.close();
	 */
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
	    db.execSQL("CREATE TABLE "
		    + tableName
		    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, bookId INTEGER, pageNo INTEGER, bookmarkDesc TEXT, date TEXT)");

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
	    Logger.verbose("BookmarkHandler", "Old version" + oldVersion);
	    Logger.verbose("BookmarkHandler", "new version" + newVersion);
	    Logger.verbose("BookmarkHandler",
		    "Upgrading database, this will drop tables and recreate.");
	    final List<String> columns = BookmarkHandler.getColumns(db, tableName);
	    Logger.verbose("BookmarkHandler", "tableName is:" + tableName);
	    db.execSQL("ALTER table " + tableName + " RENAME TO temp_"
		    + tableName);
	    Logger.verbose("BookmarkHandler", "tableName after altering is:"
		    + tableName);
	    db.execSQL("CREATE TABLE "
		    + tableName
		    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, bookId INTEGER, pageNo INTEGER, bookmarkDesc TEXT, date TEXT)");
	    columns.retainAll(BookmarkHandler.getColumns(db, tableName));
	    final String cols = StringUtils.join(columns, ",");
	    db.execSQL(String.format(
		    "INSERT INTO %s (%s) SELECT %s from temp_%s", tableName,
		    cols, cols, tableName));
	    db.execSQL("DROP table temp_" + tableName);

	    /*
	     * Logger.warn("Example",
	     * "Upgrading database, this will drop tables and recreate.");
	     * db.execSQL("DROP TABLE IF EXISTS " + tableName); onCreate(db);
	     */
	}
    }

}
