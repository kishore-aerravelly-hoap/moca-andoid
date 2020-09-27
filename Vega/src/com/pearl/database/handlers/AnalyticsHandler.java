/**
 * @author Samreen
 */
package com.pearl.database.handlers;

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

import com.pearl.application.ApplicationData;
import com.pearl.database.DatabaseHandler;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class AnalyticsHandler.
 */
public class AnalyticsHandler implements DatabaseHandler {
    
    /** The Constant databaseName. */
    private static final String databaseName = "analytics.db";
    
    /** The Constant tableName. */
    private static final String tableName = "analytics";
    
    /** The Constant databaseVersion. */
    private static final int databaseVersion = 2;
    
    /** The user id. */
    private final String userId;
    
    /** The Constant ROW_ID. */
    public static final String ROW_ID = "_id";
    
    /** The Constant END_TIME. */
    public static final String END_TIME = "endTime";
    
    /** The Constant TAG. */
    private static final String TAG = "AnalyticsHandler";
    
    /** The Constant insert. */
    private static final String insert = "insert into "
	    + tableName
	    + "(userId, startTime, endTime, activity, type, remarks, coloumn1, coloumn2, coloumn3, coloumn4, coloumn5) values (?,?,?,?,?,?,?,?,?,?,?)";
    
    /** The context. */
    private final Context context;
    
    /** The app data. */
    private final ApplicationData appData;
    
    /** The db. */
    private final SQLiteDatabase db;
    
    /** The insert stmt. */
    private final SQLiteStatement insertStmt;
    
    /** The database helper. */
    DatabaseHelper databaseHelper;

    /**
     * Constructor used to instantiate the database.
     *
     * @param context the context
     * @param appData the app data
     */
    public AnalyticsHandler(Context context, ApplicationData appData) {
	this.context = context;
	databaseHelper = new DatabaseHelper(this.context);
	db = databaseHelper.getWritableDatabase();
	insertStmt = db.compileStatement(insert);
	this.appData = appData;
	userId = appData.getUserId();
    }

    /**
     * Inserts the description the user has written along with the specified
     * parameters in the database.
     *
     * @param activity the activity
     * @param type the type
     * @param remarks the remarks
     * @param coloumn1 the coloumn1
     * @param coloumn2 the coloumn2
     * @param coloumn3 the coloumn3
     * @param coloumn4 the coloumn4
     * @param coloumn5 the coloumn5
     * @return the long
     */
    public long log(String activity, String type, String remarks,
	    String coloumn1, String coloumn2, String coloumn3, String coloumn4,
	    String coloumn5) {
	Logger.verbose(TAG, "insert");
	final String startTime = appData.getDate();
	final String eTime = "null";
	insertStmt.bindString(1, userId);
	insertStmt.bindString(2, startTime);
	insertStmt.bindString(3, eTime);
	insertStmt.bindString(4, activity);
	insertStmt.bindString(5, type);
	insertStmt.bindString(6, remarks);
	insertStmt.bindString(7, coloumn1);
	insertStmt.bindString(8, coloumn2);
	insertStmt.bindString(9, coloumn3);
	insertStmt.bindString(10, coloumn4);
	insertStmt.bindString(11, coloumn5);
	return insertStmt.executeInsert();
    }

    /**
     * Log.
     *
     * @param time the time
     * @param activity the activity
     * @param type the type
     * @param remarks the remarks
     * @param coloumn1 the coloumn1
     * @param coloumn2 the coloumn2
     * @param coloumn3 the coloumn3
     * @param coloumn4 the coloumn4
     * @param coloumn5 the coloumn5
     * @return the long
     */
    public long log(String time, String activity, String type, String remarks,
	    String coloumn1, String coloumn2, String coloumn3, String coloumn4,
	    String coloumn5) {
	final String endTime = "";
	insertStmt.bindString(1, userId);
	insertStmt.bindString(2, time);
	insertStmt.bindString(3, endTime);
	insertStmt.bindString(4, activity);
	insertStmt.bindString(5, type);
	insertStmt.bindString(6, remarks);
	insertStmt.bindString(7, coloumn1);
	insertStmt.bindString(8, coloumn2);
	insertStmt.bindString(9, coloumn3);
	insertStmt.bindString(10, coloumn4);
	insertStmt.bindString(11, coloumn5);
	return insertStmt.executeInsert();
    }

    /**
     * End.
     *
     * @param id the id
     */
    public void end(long id) {
	final String endTime = appData.getDate();
	final ContentValues values = new ContentValues();
	values.put("endTime", endTime);
	db.update(tableName, values, "_id=" + id, null);
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
	Logger.verbose(TAG, "tableName in getColumns()" + tableName);
	Cursor cursor = null;
	try {
	    cursor = db.rawQuery("select * from " + tableName + " limit 1",
		    null);
	    if (cursor != null) {
		listOfColumns = new ArrayList<String>(Arrays.asList(cursor
			.getColumnNames()));
	    }
	} catch (final Exception e) {
	    Logger.error(TAG, e.toString());
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
     * The Class DatabaseHelper.
     */
    public class DatabaseHelper extends SQLiteOpenHelper {
	
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
		    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, startTime TEXT, endTime TEXT, activity TEXT, type TEXT, remarks TEXT, coloumn1 TEXT, coloumn2 TEXT, coloumn3 TEXT, coloumn4 TEXT, coloumn5 TEXT)");

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
	    Logger.verbose(TAG, "Old version" + oldVersion);
	    Logger.verbose(TAG, "new version" + newVersion);
	    Logger.verbose(TAG,
		    "Upgrading database, this will drop tables and recreate.");
	    final List<String> columns = BookmarkHandler.getColumns(db, tableName);
	    Logger.verbose(TAG, "tableName is:" + tableName);
	    db.execSQL("ALTER table " + tableName + " RENAME TO temp_"
		    + tableName);
	    Logger.verbose("BookmarkHandler", "tableName after altering is:"
		    + tableName);
	    db.execSQL("CREATE TABLE "
		    + tableName
		    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, userId INTEGER, startTime TEXT, endTime TEXT, activity TEXT, type TEXT, remarks TEXT, coloumn1 TEXT, coloumn2 TEXT, coloumn3 TEXT, coloumn4 TEXT, coloumn5 TEXT)");
	    columns.retainAll(AnalyticsHandler.getColumns(db, tableName));
	    final String cols = StringUtils.join(columns, ",");
	    db.execSQL(String.format(
		    "INSERT INTO %s (%s) SELECT %s from temp_%s", tableName,
		    cols, cols, tableName));
	    db.execSQL("DROP table temp_" + tableName);
	}
    }
}
