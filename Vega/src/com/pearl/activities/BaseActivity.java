package com.pearl.activities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.widget.Toast;

import com.pearl.AppStatus.AppStatus;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.database.handlers.AnalyticsHandler;
import com.pearl.exceptions.AnalyticsTypeNotDefinedException;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseActivity.
 */
public abstract class BaseActivity extends Activity {
    
    /** The app data. */
    protected ApplicationData appData;
    
    /** The tag. */
    protected static String tag;
    
    /** The user time format. */
    protected String userTimeFormat;
    
    /** The toast display time. */
    protected int toastDisplayTime;
    
    /** The notification time. */
    protected int notificationTime;
    
    /** The activity context. */
    protected static Context activityContext;
    
    /** The device id. */
    protected static String deviceId = "";
    
    /** The network status. */
    private boolean networkStatus;
    
    /** The timer. */
    private Timer timer;
    
    /** The history device id. */
    private String historyDeviceId;

    /** The menu. */
    protected Menu menu;
    
    /** The loading dialog. */
    protected ProgressDialog loadingDialog;

    /** The home menuitem. */
    public final VegaMenuItem HOME_MENUITEM = this.new VegaMenuItem("Home", 1)
    .setOrder(1);
    
    /** The feedback menuitem. */
    public final VegaMenuItem FEEDBACK_MENUITEM = this.new VegaMenuItem(
	    "Feedback", 2).setOrder(9);
    
    /** The faq menuitem. */
    public final VegaMenuItem FAQ_MENUITEM = this.new VegaMenuItem("FAQ", 3)
    .setOrder(9);
    /*
     * public final VegaMenuItem SIGNIN_MENUITEM = this.new VegaMenuItem(
     * "Sign In", 4).setOrder(10);
     */
    /** The signout menuitem. */
    public final VegaMenuItem SIGNOUT_MENUITEM = this.new VegaMenuItem(
	    "Sign Out", 5).setOrder(10);
    
    /** The options menuitem. */
    public final VegaMenuItem OPTIONS_MENUITEM = this.new VegaMenuItem(
	    "Options", 6).setOrder(6);
    
    /** The temp options menuitem. */
    public final VegaMenuItem TEMP_OPTIONS_MENUITEM = this.new VegaMenuItem(
	    "Temp Options", 8).setOrder(1);
    
    /** The backtoexam menuitem. */
    public final VegaMenuItem BACKTOEXAM_MENUITEM = this.new VegaMenuItem(
	    "Back to Exam", 7).setOrder(7);

    /** The activity name. */
    protected String activityName;
    
    /** The activity type. */
    protected String activityType;
    
    /** The analytics id. */
    protected long analyticsId;
    
    /** The name. */
    private String name = null;
    
    /** The analytics handler. */
    private AnalyticsHandler analyticsHandler;
    
    /** The vega config. */
    protected VegaConfiguration vegaConfig;

    /** The coloumn1. */
    protected String coloumn1 = "null";
    
    /** The coloumn2. */
    protected String coloumn2 = "null";
    
    /** The coloumn3. */
    protected String coloumn3 = "null";
    
    /** The coloumn4. */
    protected String coloumn4 = "null";
    
    /** The coloumn5. */
    protected String coloumn5 = "null";
    
    /** The remarks. */
    protected String remarks = "null";

    /**
     * The Class VegaMenuItem.
     */
    protected class VegaMenuItem {
	
	/** The title. */
	private final String title;
	
	/** The id. */
	private int id;
	
	/** The order. */
	private int order;
	
	/** The group. */
	private int group;

	/**
	 * Instantiates a new vega menu item.
	 *
	 * @param title the title
	 * @param id the id
	 */
	public VegaMenuItem(String title, int id) {
	    this.title = title;
	    group = 0;
	    this.id = id;
	    order = 5;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the id
	 * @return the vega menu item
	 */
	public VegaMenuItem setId(int id) {
	    this.id = id;
	    return this;
	}

	/**
	 * Sets the order.
	 *
	 * @param o the o
	 * @return the vega menu item
	 */
	public VegaMenuItem setOrder(int o) {
	    order = 0;
	    return this;
	}

	/**
	 * Sets the group.
	 *
	 * @param g the g
	 * @return the vega menu item
	 */
	public VegaMenuItem setGroup(int g) {
	    group = g;
	    return this;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
	    return id;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
	    return title;
	}

	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public int getOrder() {
	    return order;
	}

	/**
	 * Gets the group.
	 *
	 * @return the group
	 */
	public int getGroup() {
	    return group;
	}

	// public abstract void onSelect();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	activityContext = this;
	appData = (ApplicationData) getApplication();
	vegaConfig = new VegaConfiguration(this);
	name = this.getClass().getName();
	getActivityName();
	try {
	    historyDeviceId = vegaConfig
		    .getValue(VegaConstants.HISTORY_DEVICEID);
	    if (historyDeviceId.equalsIgnoreCase("null")) {
		Logger.warn(tag, "DEVICEID - history deviceid is:"
			+ historyDeviceId);
		deviceId = getDeviceId();
		vegaConfig.setValue(VegaConstants.HISTORY_DEVICEID, deviceId);
	    } else {
		Logger.warn(tag,
			"DEVICEID - history deviceid is not null and the value is:"
				+ historyDeviceId);
		deviceId = historyDeviceId;
	    }
	} catch (final InvalidConfigAttributeException e) {
	    Logger.warn(tag, "" + e);
	} catch (final ColumnDoesNoteExistsException e) {
	    Logger.warn(tag, "" + e);
	}
	Logger.warn(tag, "DEVICEID - id in base activity is:" + deviceId);
	toastDisplayTime = getToastTimeFromDB();
	final StringTokenizer st = new StringTokenizer(name, ".");
	while (st.hasMoreTokens()) {
	    activityName = st.nextToken();
	}
	tag = activityName;
	Logger.info(tag, "id in onCreate is:" + analyticsId);
	Logger.info(tag, "Activity name is:" + activityName);
	loadingDialog = new ProgressDialog(activityContext);
	loadingDialog.setMessage("Loading..");
	loadingDialog.setIndeterminate(true);
	loadingDialog.setCancelable(false);

	analyticsHandler = new AnalyticsHandler(this, appData);
    }

    /**
     * Gets the coloumn1.
     *
     * @return the coloumn1
     */
    public String getColoumn1() {
	return coloumn1;
    }

    /**
     * Sets the coloumn1.
     *
     * @param coloumn1 the new coloumn1
     */
    public void setColoumn1(String coloumn1) {
	this.coloumn1 = coloumn1;
    }

    /**
     * Gets the coloumn2.
     *
     * @return the coloumn2
     */
    public String getColoumn2() {
	return coloumn2;
    }

    /**
     * Sets the coloumn2.
     *
     * @param coloumn2 the new coloumn2
     */
    public void setColoumn2(String coloumn2) {
	this.coloumn2 = coloumn2;
    }

    /**
     * Gets the coloumn3.
     *
     * @return the coloumn3
     */
    public String getColoumn3() {
	return coloumn3;
    }

    /**
     * Sets the coloumn3.
     *
     * @param coloumn3 the new coloumn3
     */
    public void setColoumn3(String coloumn3) {
	this.coloumn3 = coloumn3;
    }

    /**
     * Gets the coloumn4.
     *
     * @return the coloumn4
     */
    public String getColoumn4() {
	return coloumn4;
    }

    /**
     * Sets the coloumn4.
     *
     * @param coloumn4 the new coloumn4
     */
    public void setColoumn4(String coloumn4) {
	this.coloumn4 = coloumn4;
    }

    /**
     * Gets the coloumn5.
     *
     * @return the coloumn5
     */
    public String getColoumn5() {
	return coloumn5;
    }

    /**
     * Sets the coloumn5.
     *
     * @param coloumn5 the new coloumn5
     */
    public void setColoumn5(String coloumn5) {
	this.coloumn5 = coloumn5;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);
	this.menu = menu;
	attachMenuItemToMenu(menu, HOME_MENUITEM);
	attachMenuItemToMenu(menu, FEEDBACK_MENUITEM);
	attachMenuItemToMenu(menu, FAQ_MENUITEM);
	attachMenuItemToMenu(menu, OPTIONS_MENUITEM);
	attachMenuItemToMenu(menu, TEMP_OPTIONS_MENUITEM);
	attachMenuItemToMenu(menu, SIGNOUT_MENUITEM);
	return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
	if (HOME_MENUITEM.getId() == item.getItemId()) {
	    final Intent intent = new Intent(this, ShelfActivity.class);
	    try {
		vegaConfig.setValue(VegaConstants.NOTICE_COUNT, 0);
	    } catch (final ColumnDoesNoteExistsException e) {
		Logger.warn(tag, e.toString());
	    }
	    startActivity(intent);
	    finish();
	} else if (FEEDBACK_MENUITEM.getId() == item.getItemId()) {
	    final Intent i = new Intent(this, ShelfActivity.class);
	    try {
		vegaConfig.setValue(VegaConstants.NOTICE_COUNT, 0);
	    } catch (final ColumnDoesNoteExistsException e) {
		Logger.warn(tag, e.toString());
	    }
	    startActivity(i);
	    finish();
	} else if (FAQ_MENUITEM.getId() == item.getItemId()) {
	    final Intent faq = new Intent(this, ShelfActivity.class);
	    try {
		vegaConfig.setValue(VegaConstants.NOTICE_COUNT, 0);
	    } catch (final ColumnDoesNoteExistsException e) {
		Logger.warn(tag, e.toString());
	    }
	    startActivity(faq);
	    finish();
	} else if (SIGNOUT_MENUITEM.getId() == item.getItemId()) {
	    Logger.warn(tag, "signing out...");
	    try {
		/*
		 * vegaConfig.setValue(VegaConstants.HISTORY_ACTIVITY,
		 * "LoginActivity");
		 */
		vegaConfig.setValue(VegaConstants.HISTORY_BOOK_ID, "0");
		vegaConfig.setValue(VegaConstants.HISTORY_PAGE_NUMBER, "0");
		vegaConfig.setValue(VegaConstants.HISTORY_LOGGED_IN_USER_ID,
			"0");
		vegaConfig.setValue(VegaConstants.HISTORY_ACTIVITY, "null");
		vegaConfig.setValue(VegaConstants.HISTORY_BOOK_TYPE, "null");
		vegaConfig.setValue(VegaConstants.HISTORY_NOTEBOOK_ID, "0");
		vegaConfig.setValue(VegaConstants.HISTORY_SUBJECT, "null");
	    } catch (final ColumnDoesNoteExistsException e) {
		e.printStackTrace();
	    }
	    final Intent loginActivity = new Intent(this, LoginActivity.class);
	    startActivity(loginActivity);
	    finish();
	} else if (OPTIONS_MENUITEM.getId() == item.getItemId()) {
	    final Intent configIntent = new Intent(this,
		    VegaConfigurationActivity.class);
	    try {
		vegaConfig.setValue(VegaConstants.NOTICE_COUNT, 0);
	    } catch (final ColumnDoesNoteExistsException e) {
		Logger.warn(tag, e.toString());
	    }
	    startActivity(configIntent);
	} else if (BACKTOEXAM_MENUITEM.getId() == item.getItemId()) {
	    /*
	     * int QuestionNum = 0; try { QuestionNum =
	     * Integer.parseInt(vegaConfig
	     * .getValue(VegaConstants.QUESTION_NUM)); } catch
	     * (InvalidConfigAttributeException e) { Logger.error(tag, e); }
	     * 
	     * Intent intent = new Intent(this, QuestionsActivity.class);
	     * intent.putExtra("question_no", QuestionNum);
	     * startActivity(intent);
	     */
	    String examId;
	    try {
		examId = vegaConfig.getValue(VegaConstants.HISTORY_EXAM_ID);
	    } catch (final InvalidConfigAttributeException e) {
		examId = "";
		Logger.error(tag, e);
	    }
	    final Intent intent = new Intent(this, ShelfActivity.class);
	    intent.putExtra(VegaConstants.EXAM_ID, examId);
	    startActivity(intent);
	    finish();
	} else if (TEMP_OPTIONS_MENUITEM.getId() == item.getItemId()) {
	    final Intent intent = new Intent(this, ShelfActivity.class);
	    startActivity(intent);
	}
	return true;
    }

    /**
     * Attach menu item to menu.
     *
     * @param menu the menu
     * @param menuItem the menu item
     * @return true, if successful
     */
    protected boolean attachMenuItemToMenu(Menu menu, VegaMenuItem menuItem) {
	menu.add(menuItem.getGroup(), menuItem.getId(), menuItem.getOrder(),
		menuItem.getTitle());

	return true;
    }

    /**
     * Detach menu item from menu.
     *
     * @param menu the menu
     * @param menuItem the menu item
     * @return true, if successful
     */
    protected boolean detachMenuItemFromMenu(Menu menu, VegaMenuItem menuItem) {
	menu.removeItem(menuItem.getId());

	return true;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	startTimer();
	try {
	    if (getActivityType() == null) {
		throw new AnalyticsTypeNotDefinedException();
	    }
	    if (activityName == null) {
		Logger.verbose(tag, "Activity name in onresume is:"
			+ getActivityType());

	    } else {
		Logger.info(tag, "type is:" + getActivityType());
		analyticsId = analyticsHandler.log(activityName,
			getActivityType(), remarks, coloumn1, coloumn2,
			coloumn3, coloumn4, coloumn5);
		Logger.info(tag, "id is:" + analyticsId);
	    }
	    if (isActivityLoggable()) {
		Logger.warn(tag,
			"activity name before setting in base activity is:"
				+ activityName);
		vegaConfig.setValue(VegaConstants.HISTORY_ACTIVITY,
			activityName);
	    }
	} catch (final Exception e) {
	    Logger.error(tag, e);
	}
    }

    /**
     * Start timer.
     */
    private void startTimer() {
	timer = new Timer();
	timer.schedule(new UpdateTimerTask(), 1000, 70000);
    }

    /**
     * The Class UpdateTimerTask.
     */
    public class UpdateTimerTask extends TimerTask {

	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
	    if (!AppStatus.getInstance(activityContext).isOnline(
		    activityContext)) {
		Logger.warn(tag, "no network");
		setNetworkStatus(false);
		onNetworkUnAvailable();
	    } else {
		Logger.warn(tag, "connected");
		setNetworkStatus(true);
		onNetworkAvailable();
	    }
	}
    }

    /**
     * Checks if is network status.
     *
     * @return true, if is network status
     */
    public boolean isNetworkStatus() {
	return networkStatus;
    }

    /**
     * Sets the network status.
     *
     * @param networkStatus the new network status
     */
    public void setNetworkStatus(boolean networkStatus) {
	this.networkStatus = networkStatus;
    }

    /**
     * Checks if is activity loggable.
     *
     * @return true, if is activity loggable
     */
    private boolean isActivityLoggable() {

	/*
	 * Certain activities are not loggable in to the HISTORY_ACTIVITY, for
	 * them return false;
	 */
	if (activityName.equals("NotesActivity")
		|| activityName.equals("BookmarksActivity")
		|| activityName.equals("DictionaryDialogActivity")
		|| activityName.equals("ImageGalleryActivity")
		|| activityName.equals("AnouncementActivity")
		|| activityName.equals("VegaConfigurationActivity")
		|| activityName.equals("CalendarEventActivity"))
	    return false;
	else
	    return true;
    }

    /**
     * Gets the toast time from db.
     *
     * @return the toast time from db
     */
    private int getToastTimeFromDB() {
	try {
	    toastDisplayTime = Integer.parseInt(vegaConfig
		    .getValue(VegaConstants.ALERT_DISPLAY_TIME)) * 1000;
	    Logger.warn(tag, "TOAST - time is:" + toastDisplayTime);
	    userTimeFormat = vegaConfig.getValue(VegaConstants.DATE_FORMAT);
	} catch (final InvalidConfigAttributeException e) {
	    toastDisplayTime = 20000;
	    Logger.error(tag, e);
	}
	return toastDisplayTime;
    }

    /*
     * @Override public void onPause() { super.onPause(); if(analyticsId == 0){
     * 
     * }else{ analyticsHandler.end(analyticsId); } }
     */

    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    public void onDestroy() {
	super.onDestroy();
	// timer.cancel();
	analyticsHandler.close();
	// vegaConfig.close();

    }

    /**
     * Gets the device id.
     *
     * @return the device id
     */
    public String getDeviceId() {
	// 1 compute IMEI
	final TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
	final String m_szImei = TelephonyMgr.getDeviceId(); // Requires
	// READ_PHONE_STATE

	// 2 compute DEVICE ID
	final String m_szDevIDShort = "35"
		+ // we make this look like a valid IMEI
		Build.BOARD.length() % 10 + Build.BRAND.length() % 10
		+ Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
		+ Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
		+ Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
		+ Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
		+ Build.TAGS.length() % 10 + Build.TYPE.length() % 10
		+ Build.USER.length() % 10; // 13 digits

	// 3 android ID - unreliable
	final String m_szAndroidID = Secure.getString(getContentResolver(),
		Secure.ANDROID_ID);

	// 4 wifi manager, read MAC address - requires
	// android.permission.ACCESS_WIFI_STATE or comes as null
	final WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	final String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

	// 5 Bluetooth MAC address android.permission.BLUETOOTH required
	BluetoothAdapter m_BluetoothAdapter = null; // Local Bluetooth adapter
	m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	final String m_szBTMAC = m_BluetoothAdapter.getAddress();

	// 6 SUM THE IDs
	final String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID
		+ m_szWLANMAC + m_szBTMAC;
	MessageDigest m = null;
	try {
	    m = MessageDigest.getInstance("MD5");
	} catch (final NoSuchAlgorithmException e) {
	    e.printStackTrace();
	}
	m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
	final byte p_md5Data[] = m.digest();

	deviceId = new String();
	for (final byte element : p_md5Data) {
	    final int b = 0xFF & element;
	    // if it is a single digit, make sure it have 0 in front (proper
	    // padding)
	    if (b <= 0xF)
		deviceId += "0";
	    // add number to string
	    deviceId += Integer.toHexString(b);
	}
	deviceId = deviceId.toUpperCase();
	appData.setDeviceId(deviceId);
	return deviceId;
    }

    /**
     * Gets the activity type.
     *
     * @return the activity type
     */
    public abstract String getActivityType();

    /**
     * On network available.
     */
    public abstract void onNetworkAvailable();

    /**
     * On network un available.
     */
    public abstract void onNetworkUnAvailable();

    /**
     * Toast message for exceptions.
     *
     * @param displayMessage the display message
     */
    public void ToastMessageForExceptions(int displayMessage) {
	Toast.makeText(activityContext, displayMessage, toastDisplayTime)
	.show();

    }

    /**
     * Back to dashboard.
     */
    protected void backToDashboard() {
	final Intent intent = new Intent(this, LoginActivity.class);
	startActivity(intent);
	finish();
    }

    /**
     * Gets the activity name.
     *
     * @return the activity name
     */
    public String getActivityName() {
	name = this.getClass().getName();
	final StringTokenizer st = new StringTokenizer(name, ".");
	while (st.hasMoreTokens()) {
	    activityName = st.nextToken();
	}
	return tag = activityName;

    }
}