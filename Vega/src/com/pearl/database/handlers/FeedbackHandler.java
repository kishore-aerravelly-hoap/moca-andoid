package com.pearl.database.handlers;

/**
 * 
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.pearl.database.DatabaseHandler;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class FeedbackHandler.
 */
public class FeedbackHandler implements DatabaseHandler {

    /** The Constant databaseName. */
    private static final String databaseName = "feedback.db";
    
    /** The Constant tableName. */
    private static final String tableName = "feedback";
    
    /** The Constant Feedback_Data. */
    public static final String Feedback_Data = "feedbackData";
    
    /** The Constant Item. */
    public static final String Item = "selectedItem";
    
    /** The Constant Sync. */
    public static final String Sync = "sync";
    
    /** The Constant row_id. */
    private static final String row_id = "_id";
    
    /** The Constant databaseVersion. */
    private static final int databaseVersion = 5;
    
    /** The context. */
    private final Context context;
    
    /** The Constant insert. */
    private static final String insert = "insert into " + tableName
	    + "(feedbackData, selectedItem, sync) values(?,?,?)";
    
    /** The database helper. */
    DatabaseHelper databaseHelper;
    
    /** The tag. */
    private final String TAG = "BookmarkHandler";
    
    /** The db. */
    SQLiteDatabase db;
    
    /** The insert stmt. */
    SQLiteStatement insertStmt;

    /**
     * Constructor used to instantiate the database.
     *
     * @param context the context
     */
    public FeedbackHandler(Context context) {
	this.context = context;
	databaseHelper = new DatabaseHelper(this.context);
	db = databaseHelper.getWritableDatabase();
	// databaseHelper.onUpgrade(db, oldVersion, newVersion)
	insertStmt = db.compileStatement(insert);
    }

    /**
     * Inserts the feedback the user has written along with the specified
     * parameters in the database.
     *
     * @param feedbackData the feedback data
     * @param selectedItem the selected item
     * @param sync the sync
     * @return the long
     */
    public Long insertFeedback(String feedbackData, String selectedItem,
	    String sync) {
	insertStmt.bindString(1, feedbackData);
	insertStmt.bindString(2, selectedItem);
	insertStmt.bindString(3, sync);
	return insertStmt.executeInsert();
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
	Logger.verbose("FeedbackHandler", "tableName in getColumns()"
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
	    Logger.error("FeedbackHandler", e.toString());
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
	databaseHelper.close();
	db.close();
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
	    Logger.verbose("FeedbackHandler", "database version is:"
		    + databaseVersion);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
	    Logger.verbose("FeedbackHandler", "In onCreate()");
	    db.execSQL("CREATE TABLE "
		    + tableName
		    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, sync TEXT, feedbackData TEXT,"
		    + "selectedItem TEXT)");
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
	    Logger.verbose("FeedbackHandler", "Old version" + oldVersion);
	    Logger.verbose("FeedbackHandler", "new version" + newVersion);
	    Logger.verbose("FeedbackHandler",
		    "Upgrading database, this will drop tables and recreate.");
	    final List<String> columns = FeedbackHandler.getColumns(db, tableName);
	    Logger.verbose("FeedbackHandler", "tableName is:" + tableName);
	    db.execSQL("ALTER table " + tableName + " RENAME TO temp_"
		    + tableName);
	    Logger.verbose("FeedbackHandler", "tableName after altering is:"
		    + tableName);
	    db.execSQL("CREATE TABLE "
		    + tableName
		    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT, sync TEXT, feedbackData TEXT, selectedItem TEXT)");
	    columns.retainAll(FeedbackHandler.getColumns(db, tableName));
	    final String cols = StringUtils.join(columns, ",");
	    db.execSQL(String.format(
		    "INSERT INTO %s (%s) SELECT %s from temp_%s", tableName,
		    cols, cols, tableName));
	    // db.execSQL("DROP TABLE IF EXISTS " + tableName);
	    db.execSQL("DROP table temp_" + tableName);
	    // onCreate(db);

	    /*
	     * Logger.warn("Example",
	     * "Upgrading database, this will drop tables and recreate.");
	     * db.execSQL("DROP TABLE IF EXISTS " + tableName); onCreate(db);
	     */
	}
    }

}
