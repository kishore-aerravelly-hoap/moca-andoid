package com.pearl.activities;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TimeZone;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.helpers.calendar.CalEventList;
import com.pearl.helpers.calendar.Events;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.ui.models.BaseResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class CalendarEventActivity.
 *
 * @author Samreen
 */
public class CalendarEventActivity extends BaseActivity {

    /** The cal event list. */
    private CalEventList calEventList;
    
    /** The events list. */
    private List<Events> eventsList;
    
    /** The server requests. */
    private static ServerRequests serverRequests;
    
    /** The cal events. */
    TextView calEvents;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.calendar_events);
	calEvents = (TextView) findViewById(R.id.calEvents);
    }

    /**
     * Download.
     */
    private void download() {
	serverRequests = new ServerRequests(appData);
	final String url = serverRequests.getRequestURL(
		com.pearl.network.request.ServerRequests.CALENDAR_EVENTS,
		appData.getUserId() + "");
	final Download calendarEventsDownload = new Download(url,
		appData.getAppTempPath(), ApplicationData.CALENDAR_EVENTS);
	final DownloadManager dm = new DownloadManager(appData,
		calendarEventsDownload);
	dm.startDownload(new DownloadCallback() {
	    @Override
	    public void onFailure(String failureMessage) {
		Logger.error(tag, "failed to download");
		Toast.makeText(activityContext,
			R.string.Unable_to_reach_pearl_server,
			Toast.LENGTH_LONG).show();
	    }

	    @Override
	    public void onProgressUpdate(int progressPercentage) {
		Logger.info("Events list", "" + progressPercentage);
	    }

	    @Override
	    public void onSuccess(String downloadedString) {
		Logger.warn(tag, "Download success");
		getCalendarDetails();
	    }
	});
    }

    /**
     * Gets the calendar details.
     *
     * @return the calendar details
     */
    public void getCalendarDetails() {

	/*
	 * if(appData.isNetworkConnectionAvailable()){ if
	 * ((!appData.isFileExists(appData.getAppTempPath() +
	 * ApplicationData.CALENDAR_EVENTS))) { Logger.warn(tag,
	 * "file doesnot exists so redownload it"); serverRequests = new
	 * ServerRequests(appData); String url = serverRequests .getRequestURL(
	 * com.pearl.network.request.ServerRequests.CALENDAR_EVENTS,
	 * appData.getUserId()+""); Download calendarEventsDownload = new
	 * Download(url, appData.getAppTempPath(),
	 * ApplicationData.CALENDAR_EVENTS); DownloadManager dm = new
	 * DownloadManager(appData, calendarEventsDownload);
	 * dm.startDownload(new DownloadCallback() {
	 * 
	 * @Override public void onFailure(String failureMessage) {
	 * Logger.error(tag, "failed to download"); }
	 * 
	 * @Override public void onProgressUpdate(int progressPercentage) {
	 * Logger.info("Events list", "" + progressPercentage); }
	 * 
	 * @Override public void onSuccess(String downloadedString) {
	 * Logger.warn(tag, "Download success"); getCalendarDetails(); } });
	 * }else{
	 */
	final ObjectMapper mapper = new ObjectMapper();
	try {

	    final BaseResponse response = mapper.readValue(
		    new File(appData.getCalEventsPath()
			    + ApplicationData.CALENDAR_EVENTS),
			    BaseResponse.class);
	    final String jsonData = response.getData().toString();
	    if (jsonData != null) {
		Logger.warn(tag, "Json form server is:" + jsonData);
		final ObjectReader reader = mapper.reader(CalEventList.class);
		Logger.warn(tag, "reader value is:"
			+ reader.readValue(jsonData).toString());
		calEventList = reader.readValue(jsonData);
		// eventsList = reader.readValue(jsonData);
		setEvent();
	    } else
		calEvents.setText("You dont have any events");
	} catch (final JsonProcessingException e) {
	    Logger.error(tag, "Json Parsing exception" + e);
	    /*
	     * runOnUiThread(new Thread(){ public void run(){
	     * calEvents.setText("Oops.. Your events didn't get updated."); }
	     * });
	     */
	} catch (final IOException e) {
	    Logger.error(tag, e);
	}
	// }
	/*
	 * }else{ calEvents.setText("Please check your internet connection"); }
	 */
	/*
	 * String filePath = appData.getAppTempPath() + "calendar.txt"; String
	 * content = ""; try { content = appData.readFile(filePath); } catch
	 * (IOException e1) { Logger.error(tag, e1); }
	 */
	/*
	 * ObjectMapper mapper = new ObjectMapper(); ObjectReader reader =
	 * mapper.reader(CalEventList.class); try { calEventList =
	 * reader.readValue(content); setEvent(); } catch
	 * (JsonProcessingException e) { e.printStackTrace(); } catch
	 * (IOException e) { e.printStackTrace(); }
	 */
    }

    /**
     * Sets the event.
     */
    private void setEvent() {
	long eventId;
	Logger.warn(tag, "set event");
	List<Events> eventsList;
	final Uri event_uri = Uri.parse("content://com.android.calendar/events");
	final Uri reminders_uri = Uri
		.parse("content://com.android.calendar/reminders");

	eventsList = calEventList.getCalEventsList();
	final Cursor cursor = getContentResolver()
		.query(Uri.parse("content://com.android.calendar/calendars"),
			new String[] { "_id", "displayName" }, "selected=1",
			null, null);
	if (cursor != null && cursor.moveToFirst()) {
	    final String[] calNames = new String[cursor.getCount()];
	    final int[] calIds = new int[cursor.getCount()];
	    for (int i = 0; i < calNames.length; i++) {
		// RETRIEVE THE CALENDAR NAMES AND IDS
		// AT THIS STAGE YOU CAN PRINT OUT THE DISPLAY NAMES TO GET AN
		// IDEA OF WHAT CALENDARS THE USER HAS
		Log.w(tag, "calendar id is:" + cursor.getInt(0));
		Log.w("tag", "calendar name is:" + cursor.getString(1));
		calIds[i] = cursor.getInt(0);
		calNames[i] = cursor.getString(1);
		cursor.moveToNext();
	    }
	    cursor.close();
	    if (calIds.length > 0) {
		// WE'RE SAFE HERE TO DO ANY FURTHER WORK
	    }
	}
	for (int i = 0; i < eventsList.size(); i++) {
	    final ContentResolver cr = getContentResolver();
	    ContentValues cvEvent = new ContentValues();
	    cvEvent.put("calendar_id", 1);
	    cvEvent.put("title", calEventList.getCalEventsList().get(i)
		    .getEventName());
	    // cvEvent.put("eventTimezone", "(GMT+05:30)Kolkata");
	    cvEvent.put("dtstart", calEventList.getCalEventsList().get(i)
		    .getFromDate().getTime());
	    // cvEvent.put("organizer", "smirza@gmail.com");
	    cvEvent.put("eventLocation", calEventList.getCalEventsList().get(i)
		    .getLocation());

	    // RRULE:FREQ=WEEKLY;COUNT=10;WKST=SU;BYDAY=TU,TH
	    // cvEvent.put("rrule", "FREQ=DAILY");

	    cvEvent.put("hasAlarm", calEventList.getCalEventsList().get(i)
		    .getHasAlarm());
	    Log.w("AddEvent", "timezone is" + TimeZone.getTimeZone("GMT"));
	    // cvEvent.put("dtend", end);

	    final Uri event = cr.insert(event_uri, cvEvent);
	    Log.w("AddEvent", "uri is:" + event.toString());

	    eventId = Long.parseLong(event.getLastPathSegment());
	    Log.w("AddEvent----", "event id is:" + eventId);

	    // reminder insert
	    cvEvent = new ContentValues();
	    cvEvent.put("event_id", eventId);
	    cvEvent.put("method", 1);
	    for (int j = 0; j < eventsList.get(i).getReminders().size(); j++) {
		Logger.warn(tag, "minutes value is:"
			+ eventsList.get(i).getReminders().get(j));
		cvEvent.put("minutes", eventsList.get(i).getReminders().get(j));
	    }
	    cr.insert(reminders_uri, cvEvent);
	}
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "Calendar_Events";
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	download();
	// getCalendarDetails();
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
     */
    @Override
    public void onNetworkAvailable() {

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
     */
    @Override
    public void onNetworkUnAvailable() {

    }
}
