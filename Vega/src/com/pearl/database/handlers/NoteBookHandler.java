package com.pearl.database.handlers;

/**
 * @author Samreen
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;

import com.pearl.application.VegaConfigurationDetails;
import com.pearl.books.pages.NoteBookPage;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class NoteBookHandler.
 */
public class NoteBookHandler extends ContentProvider {

    /** The Constant databaseName. */
    private static final String databaseName = "Notebook.db";
    
    /** The Constant databaseVersion. */
    private static final int databaseVersion = 5;
    
    /** The Constant tableName. */
    private static final String tableName = "noteBook";
    
    /** The Constant tag. */
    private static final String tag = "NoteBookHandler";
    
    /** The Constant NOTES_TEXT. */
    public static final String NOTES_TEXT = "Notes";
    
    /** The Constant DATE. */
    public static final String DATE = "Date";
    
    /** The Constant NOTES_ID. */
    public static final String NOTES_ID = "_id";
    
    /** The context. */
    private static Context context;
    
    /** The database helper. */
    private DatabaseHelper databaseHelper;
    
    /** The insert stmt. */
    private SQLiteStatement insertStmt;
    
    /** The notes. */
    private NoteBookPage notes;
    
    /** The sql db. */
    private SQLiteDatabase sqlDB;

    /* (non-Javadoc)
     * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
     */
    @Override
    public int delete(Uri uri, String selection,String[] selectionArgs) {

	Log.i(tag, "PROVIDER - content provider delete()");

	sqlDB = databaseHelper.getWritableDatabase();

	final long rowId = sqlDB.delete(tableName, selection, selectionArgs);
	Logger.warn(tag, "deleted row id is:"+rowId);
	if (rowId > 0) {
	    final Uri rowUri = ContentUris
		    .appendId(
			    VegaConfigurationDetails.ConfigDetails.CONTENT_URI_notebook
			    .buildUpon(), rowId).build();
	    getContext().getContentResolver().notifyChange(rowUri, null);

	}
	return (int)rowId;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(Uri uri) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentvalues) {
	Logger.warn(tag, "NOTEBOOK - insert");
	// get database to insert records
	sqlDB = databaseHelper.getWritableDatabase();
	// insert record in user table and get the row number of recently
	// inserted record
	final long rowId = sqlDB.insert(tableName, "", contentvalues);
	Logger.warn(tag, "inserted row id is:"+rowId);
	if (rowId > 0) {
	    final Uri rowUri = ContentUris
		    .appendId(
			    VegaConfigurationDetails.ConfigDetails.CONTENT_URI_notebook
			    .buildUpon(), rowId).build();
	    getContext().getContentResolver().notifyChange(rowUri, null);
	    return rowUri;
	}
	throw new SQLException("Failed to insert row into " + uri);
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
	    String[] selectionArgs, String sortOrder) {
	Logger.warn(tag, "NOTEBOOK - query");
	final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	final SQLiteDatabase db = databaseHelper.getReadableDatabase();
	qb.setTables(tableName);
	Logger.warn(tag, "Search query is:"+qb.query(db, projection, selection, null, null, null,
		sortOrder));
	final Cursor c = qb.query(db, projection, selection, null, null, null,
		sortOrder);
	c.setNotificationUri(getContext().getContentResolver(), uri);
	return c;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#update(android.net.Uri, android.content.ContentValues, java.lang.String, java.lang.String[])
     */
    @Override
    public int update(Uri uri, ContentValues contentvalues, String s,
	    String[] as) {
	Logger.warn(tag, "NOTEBOOK - update");
	sqlDB = databaseHelper.getWritableDatabase();
	// insert record in user table and get the row number of recently
	// inserted record
	final long rowId = sqlDB.update(tableName, contentvalues, NOTES_ID +"="  + s,
		null);
	if (rowId > 0) {
	    final Uri rowUri = ContentUris
		    .appendId(
			    VegaConfigurationDetails.ConfigDetails.CONTENT_URI_notebook
			    .buildUpon(), rowId).build();
	    getContext().getContentResolver().notifyChange(rowUri, null);
	    // return rowUri;
	    return (int) rowId;
	}
	throw new SQLException("Failed to insert row into " + uri);
    }
    
    /**
     * Close.
     */
    public void close() {
	if (databaseHelper != null)
	    databaseHelper.close();
	sqlDB.close();
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
	Cursor cursor = null;
	try {
	    cursor = db.rawQuery("select * from " + tableName + " limit 1",
		    null);
	    if (cursor != null) {
		listOfColumns = new ArrayList<String>(Arrays.asList(cursor
			.getColumnNames()));
	    }
	} catch (final Exception e) {
	    Logger.error(tag, e.toString());
	} finally {
	    if (cursor != null)
		cursor.close();
	}
	return listOfColumns;
    }

    /**
     * The Class DatabaseHelper.
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
	    Logger.warn(tag, "NOTEBOOK - creating table");
	    db.execSQL("CREATE TABLE "
		    + tableName
		    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, UserId TEXT, Subject TEXT, Notes TEXT,"
		    + "Status TEXT, Date TEXT)");
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    final List<String> columns = NoteBookHandler.getColumns(db, tableName);
	    Logger.warn(tag, "NOTEBOOK - upgrade");
	    db.execSQL("ALTER table " + tableName + " RENAME TO temp_"
		    + tableName);
	    db.execSQL("CREATE TABLE "
		    + tableName
		    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, UserId TEXT, Subject TEXT, Notes TEXT,"
		    + "Status TEXT, Date TEXT)");
	    columns.retainAll(NoteBookHandler.getColumns(db, tableName));
	    final String cols = StringUtils.join(columns, ",");
	    db.execSQL(String.format(
		    "INSERT INTO %s (%s) SELECT %s from temp_%s", tableName,
		    cols, cols, tableName));
	    db.execSQL("DROP table temp_" + tableName);
	}
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#onCreate()
     */
    @Override
    public boolean onCreate() {
	Log.w("", "PROVIDER - content provider onCreate()");
	databaseHelper = new DatabaseHelper(getContext());
	return databaseHelper == null ? false : true;
    }
}
