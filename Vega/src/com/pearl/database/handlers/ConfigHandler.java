package com.pearl.database.handlers;

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

import com.pearl.application.VegaConfigurationDetails;

// TODO: Auto-generated Javadoc
/**
 * The Class ConfigHandler.
 */
public class ConfigHandler extends ContentProvider {

    /** The Constant databaseName. */
    private static final String databaseName = "config.db";
    
    /** The Constant tableName. */
    private static final String tableName = "configTable";
    
    /** The Constant ROW_ID. */
    public static final String ROW_ID = "_id";
    
    /** The Constant TOAST_MESSAGE_DISPLAY_TIME. */
    public static final String TOAST_MESSAGE_DISPLAY_TIME = "toastDisplayTime";
    
    /** The Constant ATTRIBUTE. */
    public static final String ATTRIBUTE = "ATTRIBUTE";
    
    /** The Constant VALUE. */
    public static final String VALUE = "VALUE";
    
    /** The Constant databaseVersion. */
    private static final int databaseVersion = 127;
    
    /** The tag. */
    private static String TAG = "ConfigHandler";
    
    /** The context. */
    private static Context context;
    
    /** The sql db. */
    private SQLiteDatabase sqlDB;
    
    /** The insert stmt. */
    private SQLiteStatement insertStmt;
    
    /** The db helper. */
    private DatabaseHelper dbHelper;
    /*
     * private static final String domain = "pearl.pressmart.com"; private
     * static final String project = "/pearl"; private static final String
     * WEB_URL = "http://"+domain + project;
     */
 //private static final String WEB_URL = "http://172.16.255.105:8080/pearl";
/** The Constant WEB_URL. */
    //private static final String WEB_URL = "http://pearlmoca.cloudapp.net:8080/pearl/";
    private static final String WEB_URL = "http://192.168.0.108:8080/pearl/";
    /**
     * The Class DatabaseHelper.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

	/**
	 * Instantiates a new database helper.
	 *
	 * @param context the context
	 */
	DatabaseHelper(Context context) {
	    super(context, databaseName, null, databaseVersion);
	    ConfigHandler.context = context;
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL("CREATE TABLE "
		    + tableName
		    + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, ATTRIBUTE TEXT, VALUE TEXT, EDITABLE TEXT, ATTRIBUTE_GROUP TEXT)");

	    // Time configuration
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('ALERT_DISPLAY_TIME',30000,'YES','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('NOTES_AUTO_SAVE_TIME',2000,'YES','null')");

	    // date format
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('DATE_FORMAT',12,'YES','null')");

	    // History configurations
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('HISTORY_ACTIVITY','null','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('HISTORY_LOGGED_IN_USER_ID','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('HISTORY_BOOK_ID','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('HISTORY_PAGE_NUMBER','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('HISTORY_SUBJECT','null','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('HISTORY_NOTEBOOK_ID','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('HISTORY_EXAM_ID','null','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('HISTORY_QUESTION_NUM','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('HISTORY_QUESTIONS_ACTIVITY','NULL','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('HISTORY_DEVICEID','null','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('BOOK_SELECTED_POSITION_SHELF',0,'NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('BOOK_SELECTED_POSITION_ONLINE',0,'NO','null')");

	    // Attendance Configurations
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('CAN_TAKE_ATTENDANCE','flase','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('LAST_ATTENDANCE_DATE','Thu Jan 01 13:30:00 GMT+05:30 1980','NO','null')");
	    // Server paths

	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('SERVER_BASE_PATH','"
		    + WEB_URL + "','YES','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('SERVER_PHP_PATH','"
		    + WEB_URL + "','YES','null')");

	    // EReader configurations
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('THEME','theme1.css','YES','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('TEXT_SIZE','1','YES','null')");

	    // Notification counts configuration
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('NOTIFICATION_TIME','2000','YES','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('BOOKS_COUNT','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('NOTICE_COUNT','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('MESSAGE_COUNT','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('ATTENDANCE_COUNT','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('EXAM_COUNT','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('ADDITIONAL_INFO_COUNT','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('SUBJECT_COUNT','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('TEST_APPROVE_COUNT','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('TEST_REJECTED_COUNT','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('TEST_TO_BE_EVALUATED','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('TEST_PUBLISHED_COUNT','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('TEST_RESULTS_PUBLISH_COUNT','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('TEST_EVALUATED_COUNT','0','NO','null')");
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('TEST_RESULTS_TO_BE_PUBLISH_COUNT','0','NO','null')");
	    db.execSQL("insert into "
			    + tableName
			    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('SHELF_LIST_SELECTED_HISTORY','1','NO','null')");

	    // DRM configurations
	    db.execSQL("insert into "
		    + tableName
		    + "(ATTRIBUTE, VALUE, EDITABLE, ATTRIBUTE_GROUP) values('DRM_ALLOWED','YES','YES','null')");
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    db.execSQL("DROP TABLE IF EXISTS " + tableName);
	    onCreate(db);
	}
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#delete(android.net.Uri, java.lang.String, java.lang.String[])
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
	// TODO Auto-generated method stub
	return 0;
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
	// get database to insert records
	sqlDB = dbHelper.getWritableDatabase();
	// insert record in user table and get the row number of recently
	// inserted record
	final long rowId = sqlDB.insert(tableName, "", contentvalues);
	if (rowId > 0) {
	    final Uri rowUri = ContentUris
		    .appendId(
			    VegaConfigurationDetails.ConfigDetails.CONTENT_URI
			    .buildUpon(),
			    rowId).build();
	    getContext().getContentResolver().notifyChange(rowUri, null);
	    return rowUri;
	}
	throw new SQLException("Failed to insert row into " + uri);
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#onCreate()
     */
    @Override
    public boolean onCreate() {
	dbHelper = new DatabaseHelper(getContext());
	return dbHelper == null ? false : true;
    }

    /* (non-Javadoc)
     * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
	    String[] selectionArgs, String sortOrder) {
	final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	final SQLiteDatabase db = dbHelper.getReadableDatabase();
	qb.setTables(tableName);
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
	sqlDB = dbHelper.getWritableDatabase();
	// insert record in user table and get the row number of recently
	// inserted record
	final long rowId = sqlDB.update(tableName, contentvalues, "ATTRIBUTE =" + s,
		null);
	if (rowId > 0) {
	    final Uri rowUri = ContentUris
		    .appendId(
			    VegaConfigurationDetails.ConfigDetails.CONTENT_URI
			    .buildUpon(),
			    rowId).build();
	    getContext().getContentResolver().notifyChange(rowUri, null);
	    // return rowUri;
	    return (int) rowId;
	}
	throw new SQLException("Failed to insert row into " + uri);
    }

}