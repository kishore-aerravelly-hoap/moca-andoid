package com.pearl.services;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.activities.AttendanceActivity;
import com.pearl.activities.ChatWallActivity;
import com.pearl.activities.ExamEvaluationFragmentActivity;
import com.pearl.activities.ExamListActivity;
import com.pearl.activities.MyApprovalFragmemtActivity;
import com.pearl.activities.NoticeBoardActivity;
import com.pearl.activities.OnlineActivity;
import com.pearl.activities.RejectedTestsFragmentActivity;
import com.pearl.activities.ResultsPublishFragmentActivity;
import com.pearl.activities.TeacherAwaitingResultsFragmentActivity;
import com.pearl.activities.TeacherPublishFragmentActivity;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.helpers.calendar.CalEventList;
import com.pearl.helpers.calendar.Events;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.parsers.json.ExamListParser;
import com.pearl.parsers.json.NotificationParser;
import com.pearl.ui.models.NotificationCount;
import com.pearl.ui.models.RoleType;
import com.pearl.widget.VegaWidgetProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class NotficationService.
 */
public class NotficationService extends Service {
    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */

    public ApplicationData applicationData;
    
    /** The tag. */
    private final String tag = "NotificationService";
    
    /** The vega config. */
    private VegaConfiguration vegaConfig;
    
    /** The server requests. */
    private ServerRequests serverRequests;

    /** The notice board url. */
    private String noticeBoardUrl;
    
    /** The calendar events url. */
    private String calendarEventsUrl;
    
    /** The exam url. */
    private String examUrl;
    
    /** The available books url. */
    private String availableBooksUrl;
    
    /** The attendance url. */
    private String attendanceUrl;
    
    /** The chat url. */
    private String chatUrl;

    /** The cal event list. */
    private CalEventList calEventList;

    /** The previous attendance count. */
    int previousAttendanceCount;
    
    /** The previous books count. */
    int previousBooksCount;
    
    /** The previous notice count. */
    int previousNoticeCount;
    
    /** The previous exam count. */
    int previousExamCount;
    
    /** The previous message count. */
    int previousMessageCount;
    
    /** The previous additional info count. */
    int previousAdditionalInfoCount;
    
    /** The previous results published count. */
    int previousTestToBeApprovedCount,previousTestToBePublishedCount,previousTestToBeEvaluatedCount,
    previousTestsRejectedCount,previousTestResultsToBePublishedCount,previousResultsPublishedCount;

    /** The attendence count. */
    int attendenceCount;
    
    /** The books count. */
    int booksCount;
    
    /** The event count. */
    int eventCount;
    
    /** The notice count. */
    int noticeCount;
    
    /** The exam count. */
    int examCount;
    
    /** The messsage count. */
    int messsageCount;
    
    /** The additional info count. */
    int additionalInfoCount;
    
    /** The notification time. */
    int notificationTime;
    
    /** The results published count. */
    int testToBeApprovedCount,testToBePublishedCount,testToBeEvaluatedCount,testsRejectedCount,
    testResultsToBePublishedCount,resultsPublishedCount;
    
    /** The epub mofied count. */
    int epubMofiedCount;

    /** The remote views. */
    private RemoteViews remoteViews;
    // int totalCount;

    /** The all widget ids. */
    int[] allWidgetIds;
    
    /** The app widget manager. */
    AppWidgetManager appWidgetManager;
    
    /** The notification manager. */
    NotificationManager notificationManager;

    /**
     * The Class LocalBinder.
     */
    public class LocalBinder extends Binder {
	
	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	NotficationService getService() {
	    return NotficationService.this;
	}
    }

    /* (non-Javadoc)
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
    @Override
    public int onStartCommand(Intent intent, int startId, int flags) {
	Log.i(tag, "Called");

	remoteViews = new RemoteViews(getApplicationContext().getPackageName(),
		R.layout.widget_layout);
	appWidgetManager = AppWidgetManager.getInstance(this
		.getApplicationContext());

	final ComponentName thisWidget = new ComponentName(this,
		VegaWidgetProvider.class);
	allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

	if (applicationData.getUserId() != null) {
	    remoteViews.setViewVisibility(R.id.loggedOutotification, View.GONE);

	    remoteViews.setViewVisibility(R.id.WidgetLayout1, View.GONE);
	    remoteViews
	    .setViewVisibility(R.id.NoNetworkNotification, View.GONE);
	    remoteViews
	    .setViewVisibility(R.id.noNotificationsLayout, View.GONE);

	    remoteViews.setViewVisibility(R.id.wait_message, View.VISIBLE);

	} else {
	    remoteViews.setViewVisibility(R.id.loggedOutotification,
		    View.VISIBLE);
	    remoteViews.setViewVisibility(R.id.WidgetLayout1, View.GONE);
	    remoteViews
	    .setViewVisibility(R.id.NoNetworkNotification, View.GONE);
	    remoteViews
	    .setViewVisibility(R.id.noNotificationsLayout, View.GONE);

	    remoteViews.setViewVisibility(R.id.wait_message, View.GONE);
	}
	appWidgetManager.updateAppWidget(allWidgetIds, remoteViews);
	stopSelf();
	// super.onStart(intent, startId);
	return START_STICKY;
    }

    /**
     * Update widget count.
     *
     * @param remoteViews the remote views
     * @param id the id
     * @param count the count
     * @param type the type
     * @return the remote views
     */
    private RemoteViews updateWidgetCount(RemoteViews remoteViews, int id,
	    int count, String type) {
	if(count > 0){
	    remoteViews.setViewVisibility(id, View.VISIBLE);
	    remoteViews.setTextViewText(id, count + " " + formatMessage(count, type));			
	}
	return remoteViews;
    }

    /* (non-Javadoc)
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {

	// Display a notification about us starting. We put an icon in the
	// status bar.
	serverRequests = new ServerRequests(this);
	applicationData = (ApplicationData) getApplication();
	Logger.error(tag, "user in notification service is:::"
		+ applicationData.getUserId());

	if (applicationData.getUserId() != null) {
	    noticeBoardUrl = serverRequests.getRequestURL(
		    ServerRequests.NOTICEBOARD, applicationData.getUserId());
	    calendarEventsUrl = serverRequests
		    .getRequestURL(ServerRequests.CALENDAR_EVENTS,
			    applicationData.getUserId());
	    examUrl = serverRequests.getRequestURL(ServerRequests.EXAMS_LIST,
		    applicationData.getUserId());
	    availableBooksUrl = serverRequests.getRequestURL(
		    ServerRequests.AVAILABLEBOOKSLIST,
		    applicationData.getUserId());
	    attendanceUrl = serverRequests.getRequestURL(
		    ServerRequests.ATTENDANCE, applicationData.getUser()
		    .getGradeName(), applicationData.getUserId());
	    chatUrl = serverRequests.getRequestURL(ServerRequests.CHAT_WALL,
		    applicationData.getUserId());
	} else {
	    Logger.error(tag, "user is null");
	}

	vegaConfig = new VegaConfiguration(this);
	try {
	    notificationTime = Integer.parseInt(vegaConfig
		    .getValue(VegaConstants.NOTIFICATION_TIME));
	} catch (final InvalidConfigAttributeException e) {
	    Logger.warn(tag, e.toString());
	}
	final Timer timer = new java.util.Timer();
	notificationTime = notificationTime * 60000;
	timer.schedule(new Notifier(), 3000, 40000);
    }

    /* (non-Javadoc)
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
	return mBinder;
    }

    // This is the object that receives interactions from clients. See
    // RemoteService for a more complete example.
    /** The m binder. */
    private final IBinder mBinder = new LocalBinder();

    /**
     * The Class Notifier.
     */
    class Notifier extends TimerTask {
	
	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {

	    // vegaNotification.download(); // download file everytime
	    allNotifications();
	}
    }

    /**
     * Notifies the user.
     *
     * @param messageType the message type
     * @param messageCount the message count
     * @param image the image
     * @param type the type
     * @throws ColumnDoesNoteExistsException the column does note exists exception
     */
    private void generateNotification(String messageType, int messageCount,
	    int image, String type) throws ColumnDoesNoteExistsException {

	if (messageCount != 0) {
	    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	    final String text = "You have " + messageCount +" "+ formatMessage(messageCount, type);

	    final Notification notification = new Notification(image, text,
		    System.currentTimeMillis());
	    notification.defaults |= Notification.DEFAULT_SOUND;
	    notification.defaults = Notification.FLAG_AUTO_CANCEL;
	    notification.defaults |= Notification.DEFAULT_VIBRATE;
	    final Intent intent = new Intent();
	    intent.putExtra("NOTIF", "Notif");
	    Logger.warn(tag, "user id is:"+applicationData.getUserId());
	    if(applicationData.getUserId() != null){
		if (type.equalsIgnoreCase(VegaNotification.ATTENDENCE)) {
		    intent.setClass(this, AttendanceActivity.class);
		}
		if (type.equalsIgnoreCase(VegaNotification.BOOKS)) {
		    intent.setClass(this, OnlineActivity.class);
		}
		if (type.equalsIgnoreCase(VegaNotification.EXAM)) {
			try {
				if(vegaConfig.getValue(VegaConstants.HISTORY_EXAM_ID) == null)
					intent.setClass(this, ExamListActivity.class);
			} catch (InvalidConfigAttributeException e) {
				e.printStackTrace();
			}
		}
		if (type.equalsIgnoreCase(VegaNotification.NOTICEBOARD)) {
		    intent.setClass(this, NoticeBoardActivity.class);
		}
		if (type.equalsIgnoreCase(VegaNotification.CHAT)) {
		    intent.setClass(this, ChatWallActivity.class);
		}
		if (type.equalsIgnoreCase(VegaNotification.EVENTS)) {
		    intent.setClassName("com.android.calendar",
			    "com.android.calendar.AgendaActivity");
		} 
		if(type.equalsIgnoreCase(VegaNotification.TEST_TO_BE_APPROVED)){
		    intent.setClass(this,MyApprovalFragmemtActivity.class);
		}
		if(type.equalsIgnoreCase(VegaNotification.TEST_TO_BE_PUBLISHED)){
		    intent.setClass(this, TeacherPublishFragmentActivity.class);
		}
		if(type.equalsIgnoreCase(VegaNotification.TEST_REJECTED)){
		    intent.setClass(this,RejectedTestsFragmentActivity.class);
		}
		if(type.equalsIgnoreCase(VegaNotification.TEST_TO_BE_EVALAUTED)){
		    intent.setClass(this, ExamEvaluationFragmentActivity.class);
		}
		if(type.equalsIgnoreCase(VegaNotification.RESULTS_TO_BE_PUBLISHED)){
		    intent.setClass(this, TeacherAwaitingResultsFragmentActivity.class);
		}
		if(type.equalsIgnoreCase(VegaNotification.RESULTS_PUBLISHED)){

		    switch (checkUserRoletype()) {
		    case 1:
			intent.setClass(this, ResultsPublishFragmentActivity.class);
			break;
		    case 2:
			intent.setClass(this, ExamListActivity.class);
			break;
		    default:
			break;
		    }
		}
		else {
		    Logger.info("TYPE MISMATCH",
			    "TYPE MISMATCH for NOTIFICATION TYPE");
		}
	    }else{
		Toast.makeText(this, R.string.logged_out, 10000).show();
	    }
	    Logger.warn(tag, "user id is:"+applicationData.getUserId());
	    final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
		    intent, notification.defaults);
	    notification.setLatestEventInfo(this, messageType, text,
		    pendingIntent);
	    Logger.warn(tag, "notifyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
	    notificationManager.notify(messageType, messageCount, notification);
	} else {
	    // Logger.error(tag, "Count is zero so dont notify the user");
	}

    }

    /**
     * All notifications.
     */
    public void allNotifications() {

	if (!AppStatus.getInstance(getApplicationContext()).isOnline(
		getApplicationContext())) {
	    for (final int widgetId : allWidgetIds) {
		remoteViews = new RemoteViews(getApplicationContext()
			.getPackageName(), R.layout.widget_layout);
		remoteViews.setViewVisibility(R.id.NoNetworkNotification,
			View.VISIBLE);
		remoteViews.setViewVisibility(R.id.noNotificationsLayout,
			View.GONE);
		remoteViews.setViewVisibility(R.id.WidgetLayout1, View.GONE);

		appWidgetManager.updateAppWidget(widgetId, remoteViews);
	    }
	} else {
	    // TODO check for user object is not null

	    if (applicationData.getUserId() != null) {
		final String url = serverRequests.getRequestURL(
			ServerRequests.SERVICE, applicationData.getUserId());

		//DownloadManager dm = new DownloadManager(applicationData, download);
		// TODO Downlaod the file using the download manager rather than
		// using URL()
		String examId = null;
		try{
			examId = vegaConfig.getValue(VegaConstants.HISTORY_EXAM_ID);
		}catch(Exception e){
			e.printStackTrace();
		}
		Logger.warn(tag, "exam nid before requesting notifications is:"+examId);
		if(examId == null){
		sendRequestToServer(url, new DownloadCallback() {

		    @Override
		    public void onSuccess(String downloadedString) {
			// String filePath =
			// applicationData.getNotificationCountFilePath() +
			// ApplicationData.NOTIFICATION_COUNT_FILENAME;
			Logger.warn(tag, "downloaded string for notificatin is:"+downloadedString);
			ApplicationData.writeToFile(downloadedString,
				applicationData.getNotificationCountFile());
			String content;
			try {
			    content = ApplicationData.readFile(applicationData
				    .getNotificationCountFile());
			} catch (final IOException e) {
			    content = "";
			    Logger.error("Notification service", e);
			}

			try {
			    previousExamCount = Integer.parseInt(vegaConfig
				    .getValue(VegaConstants.EXAM_COUNT));
			    previousTestToBeApprovedCount = Integer.parseInt(vegaConfig.getValue(VegaConstants.TEST_APPROVE_COUNT));
			    previousTestsRejectedCount = Integer.parseInt(vegaConfig.getValue(VegaConstants.TEST_REJECTED_COUNT));
			    previousTestToBePublishedCount = Integer.parseInt(vegaConfig.getValue(VegaConstants.TEST_PUBLISHED_COUNT));
			    previousResultsPublishedCount = Integer.parseInt(vegaConfig.getValue(VegaConstants.TEST_RESULTS_PUBLISH_COUNT));
			    previousTestToBeEvaluatedCount = Integer.parseInt(vegaConfig.getValue(VegaConstants.TEST_TO_BE_EVALUATED));
			    /*previousAttendanceCount = Integer.parseInt(vegaConfig
									.getValue(VegaConstants.ATTENDANCE_COUNT));
							previousBooksCount = Integer.parseInt(vegaConfig
									.getValue(VegaConstants.BOOKS_COUNT));

							previousMessageCount = Integer.parseInt(vegaConfig
									.getValue(VegaConstants.MESSAGE_COUNT));
							previousNoticeCount = Integer.parseInt(vegaConfig
									.getValue(VegaConstants.NOTICE_COUNT));
							previousAdditionalInfoCount = Integer.parseInt(vegaConfig
									.getValue(VegaConstants.ADDITIONAL_INFO_COUNT));
							Logger.info(tag,
									"NOTICE - Notice count wen getting from config is:"
											+ previousNoticeCount);*/
			} catch (final InvalidConfigAttributeException e) {
			    previousAttendanceCount = 0;
			    previousBooksCount = 0;
			    previousNoticeCount = 0;
			    previousExamCount = 0;
			    previousMessageCount = 0;

			    Logger.error(tag, e);
			}

			NotificationCount notifCount;
			notifCount = null;
			try {
			    notifCount = NotificationParser
				    .getNotificationFile(content);
			} catch (final Exception e1) {
			    Logger.error(tag, e1);
			}
			if(notifCount != null){
			    booksCount = notifCount.getNewBooksCount();
			    attendenceCount = notifCount.getNewAttendanceCount();
			    //eventCount = notifCount.getNewCalEventCount();
			    noticeCount = notifCount.getNewNoticeCount();
			    examCount = notifCount.getNewExamCount();
			    messsageCount = notifCount.getNewMsgCount();
			    additionalInfoCount = notifCount.getNewAdditionalInfoCount();
			    testToBeApprovedCount = notifCount.getNewTestCreateCount();
			    testToBePublishedCount = notifCount.getNewTestApprovedCount();
			    testsRejectedCount = notifCount.getNewTestRejectedCount();
			    testResultsToBePublishedCount = notifCount.getNewExamEvaluatedCount();
			    resultsPublishedCount = notifCount.getNewResultPublishCount();
			    testToBeEvaluatedCount = notifCount.getTestToBeEvaluatedCount();
			}

			try {
			    previousExamCount = previousExamCount + examCount;
			    previousResultsPublishedCount = previousResultsPublishedCount + resultsPublishedCount;
			    previousTestsRejectedCount = previousTestsRejectedCount + testsRejectedCount;
			    previousTestResultsToBePublishedCount = previousTestResultsToBePublishedCount + testResultsToBePublishedCount;
			    previousTestToBeApprovedCount = previousTestToBeApprovedCount + testToBeApprovedCount;
			    previousTestToBeEvaluatedCount = previousTestToBeEvaluatedCount + testToBeEvaluatedCount;
			    previousTestToBePublishedCount = previousTestToBePublishedCount + testToBePublishedCount;

			    vegaConfig.setValue(VegaConstants.EXAM_COUNT,
				    previousExamCount);
			    vegaConfig.setValue(VegaConstants.TEST_APPROVE_COUNT, previousTestToBeApprovedCount);
			    vegaConfig.setValue(VegaConstants.TEST_REJECTED_COUNT, previousTestsRejectedCount);
			    vegaConfig.setValue(VegaConstants.TEST_PUBLISHED_COUNT, previousTestToBePublishedCount);
			    vegaConfig.setValue(VegaConstants.TEST_RESULTS_TO_BE_PUBLISH_COUNT, previousTestResultsToBePublishedCount);
			    vegaConfig.setValue(VegaConstants.TEST_RESULTS_PUBLISH_COUNT, previousResultsPublishedCount);
			    vegaConfig.setValue(VegaConstants.TEST_TO_BE_EVALUATED, previousTestToBeEvaluatedCount);
			    /*previousAttendanceCount = previousAttendanceCount
									+ attendenceCount;
							previousBooksCount = previousBooksCount
									+ booksCount;

							previousMessageCount = previousMessageCount
									+ messsageCount;
							previousNoticeCount = previousNoticeCount
									+ noticeCount;
							Logger.info("PREVIOUS NOTICE COUNT",
									"NOTICE - pre notice count after adding"
											+ previousNoticeCount);

							previousAdditionalInfoCount = previousAdditionalInfoCount
									+ additionalInfoCount;

							vegaConfig.setValue(VegaConstants.ATTENDANCE_COUNT,
									previousAttendanceCount);
							vegaConfig.setValue(VegaConstants.BOOKS_COUNT,
									previousBooksCount);
							vegaConfig.setValue(VegaConstants.NOTICE_COUNT,
									previousNoticeCount);

							vegaConfig.setValue(
									VegaConstants.ADDITIONAL_INFO_COUNT,
									previousAdditionalInfoCount);
							vegaConfig.setValue(VegaConstants.MESSAGE_COUNT,
									previousMessageCount);

							Logger.warn(tag, "new msg count before setting is:"
									+ (previousMessageCount + messsageCount));*/

			} catch (final ColumnDoesNoteExistsException e) {
			    Logger.error("Notificationservice", e);
			}

			// take previous counts for all
			try{
			    generateNotification(VegaNotification.EXAM,
				    examCount,
				    R.drawable.notification_icon, "exam to be taken");
			    generateNotification(VegaNotification.TEST_TO_BE_APPROVED,
				    testToBeApprovedCount,
				    R.drawable.notification_icon, "Test to be approved");
			    generateNotification(VegaNotification.RESULTS_PUBLISHED,
				    resultsPublishedCount,
				    R.drawable.notification_icon, "Result published");
			    generateNotification(VegaNotification.RESULTS_TO_BE_PUBLISHED,
				    testResultsToBePublishedCount,
				    R.drawable.notification_icon, "Result to be published");
			    generateNotification(VegaNotification.TEST_TO_BE_PUBLISHED,
				    testToBePublishedCount,
				    R.drawable.notification_icon, "Test to be published");
			    generateNotification(VegaNotification.TEST_REJECTED,
				    testsRejectedCount,
				    R.drawable.notification_icon, "Rejected test");
			    generateNotification(VegaNotification.TEST_TO_BE_EVALAUTED, 
				    testToBeEvaluatedCount, R.drawable.notification_icon, "test to be evaluated");

			    generateNotification(VegaNotification.ATTENDENCE,
				    previousAttendanceCount,
				    R.drawable.notification_icon, "attendence");
			    generateNotification(VegaNotification.EVENTS,
				    eventCount, R.drawable.notification_icon,
				    "events");
			    generateNotification(VegaNotification.BOOKS,
				    previousBooksCount,
				    R.drawable.notification_icon, "books");
			    generateNotification(VegaNotification.NOTICEBOARD,
				    previousNoticeCount,
				    R.drawable.notification_icon, "notice");
			    generateNotification(VegaNotification.CHAT,
				    previousMessageCount,
				    R.drawable.notification_icon, "MESSAGE");
			    generateNotification(VegaNotification.ADDITIONAL_INFO,
				    previousAdditionalInfoCount,
				    R.drawable.notification_icon, "additionalInfo");
			}catch(final ColumnDoesNoteExistsException e){
			    Logger.error(tag, e);
			}

			/*Logger.warn(tag, "booksCount:" + booksCount);
						Logger.warn(tag, "eventCount" + eventCount);
						Logger.warn(tag, "Notice Count logger=" + noticeCount);
						Logger.warn(tag, "msgCount:" + messsageCount);
						Logger.warn(tag, "additionalInfoCount:"
								+ additionalInfoCount);*/

			/*if (noticeCount > 0) {
							sendRequestToServer(noticeBoardUrl,
									new DownloadCallback() {

										@Override
										public void onSuccess(
												String downloadedString) {
											Logger.warn(tag,
													"******** noticeboard downloaded successfully");
											ApplicationData
													.writeToFile(
															downloadedString,
															applicationData
																	.getNoticeBoardFile());
										}

										@Override
										public void onProgressUpdate(
												int progressPercentage) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onFailure(
												String failureMessage) {
											// TODO Auto-generated method stub

										}
									});
						}*/
			if (examCount > 0) {
			    sendRequestToServer(examUrl,
				    new DownloadCallback() {

				@Override
				public void onSuccess(
					String downloadedString) {
				    Logger.warn(tag,
					    "******** exams list downloaded successfully");
				    try {
					ExamListParser
					.getExamFile(
						downloadedString,
						applicationData
						.getExamsListFile());
				    } catch (final JsonParseException e) {
					// TODO Auto-generated catch
					// block
					e.printStackTrace();
				    } catch (final JsonMappingException e) {
					// TODO Auto-generated catch
					// block
					e.printStackTrace();
				    } catch (final IOException e) {
					// TODO Auto-generated catch
					// block
					e.printStackTrace();
				    }
				}

				@Override
				public void onProgressUpdate(
					int progressPercentage) {
				    // TODO Auto-generated method stub

				}

				@Override
				public void onFailure(
					String failureMessage) {
				    // TODO Auto-generated method stub

				}
			    });
			}
			/*if (eventCount > 0) {
							sendRequestToServer(calendarEventsUrl,
									new DownloadCallback() {

										@Override
										public void onSuccess(
												String downloadedString) {
											Logger.warn(tag, "events are:"
													+ downloadedString);
											ApplicationData
													.writeToFile(
															downloadedString,
															applicationData
																	.getCalEventsPath()
																	+ ApplicationData.CALENDAR_EVENTS);
											ObjectMapper mapper = new ObjectMapper();
											try {
												BaseResponse response = mapper
														.readValue(
																downloadedString,
																BaseResponse.class);
												String jsonData = response
														.getData().toString();
												if (jsonData != null) {
													Logger.warn(tag,
															"Json form server is:"
																	+ jsonData);
													ObjectReader reader = mapper
															.reader(CalEventList.class);
													Logger.warn(
															tag,
															"reader value is:"
																	+ reader.readValue(
																			jsonData)
																			.toString());
													calEventList = reader
															.readValue(jsonData);
													// eventsList =
													// reader.readValue(jsonData);
													setEvent();
												}
											} catch (JsonProcessingException e) {
												Logger.error(tag,
														"Json Parsing exception"
																+ e);
											} catch (IOException e) {
												Logger.error(tag, e);
											}

										}

										@Override
										public void onProgressUpdate(
												int progressPercentage) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onFailure(
												String failureMessage) {
											// TODO Auto-generated method stub

										}
									});
						}
						if (booksCount > 0) {
							sendRequestToServer(availableBooksUrl,
									new DownloadCallback() {

										@Override
										public void onSuccess(
												String downloadedString) {
											Logger.warn(tag,
													"******** exams list downloaded successfully");
											ApplicationData
													.writeToFile(
															downloadedString,
															applicationData
																	.getAvailableBooksListXML());
										}

										@Override
										public void onProgressUpdate(
												int progressPercentage) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onFailure(
												String failureMessage) {
											// TODO Auto-generated method stub

										}
									});
						}

						if (messsageCount > 0) {
							sendRequestToServer(chatUrl,
									new DownloadCallback() {

										@Override
										public void onSuccess(
												String downloadedString) {
											Logger.warn(tag,
													"******** exams list downloaded successfully");
											ApplicationData.writeToFile(
													downloadedString,
													applicationData
															.getChatWallFile());
										}

										@Override
										public void onProgressUpdate(
												int progressPercentage) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onFailure(
												String failureMessage) {
											// TODO Auto-generated method stub

										}
									});
						}

						if (attendenceCount > 0) {
							sendRequestToServer(attendanceUrl,
									new DownloadCallback() {

										@Override
										public void onSuccess(
												String downloadedString) {
											Logger.warn(tag,
													"******** exams list downloaded successfully");
											ApplicationData
													.writeToFile(
															downloadedString,
															applicationData
																	.getAttendanceFile());
										}

										@Override
										public void onProgressUpdate(
												int progressPercentage) {
											// TODO Auto-generated method stub

										}

					
										@Override
										public void onFailure(
												String failureMessage) {
											// TODO Auto-generated method stub

										}
									});
						}*/

			final int totalCount = previousAdditionalInfoCount
				+ previousAttendanceCount + previousBooksCount
				+ previousExamCount + previousMessageCount
				+ previousNoticeCount+ previousResultsPublishedCount 
				+ previousTestResultsToBePublishedCount + previousTestsRejectedCount 
				+ previousTestToBeApprovedCount + previousTestToBeEvaluatedCount 
				+ previousTestToBePublishedCount;
			for (final int widgetId : allWidgetIds) {

			    if (totalCount == 0) {
				remoteViews.setViewVisibility(
					R.id.WidgetLayout1, View.GONE);
				remoteViews.setViewVisibility(
					R.id.NoNetworkNotification, View.GONE);
				remoteViews.setViewVisibility(
					R.id.noNotificationsLayout,
					View.VISIBLE);
				remoteViews.setViewVisibility(
					R.id.wait_message, View.GONE);
			    } else {

				remoteViews.setViewVisibility(
					R.id.WidgetLayout1, View.VISIBLE);
				remoteViews.setViewVisibility(
					R.id.noNotificationsLayout, View.GONE);

				remoteViews.setViewVisibility(
					R.id.wait_message, View.GONE);
				remoteViews.setTextViewText(R.id.widgetHeading,
					"Pearl says you have");

				remoteViews = updateWidgetCount(remoteViews,
					R.id.widgetNoticeCount,
					previousNoticeCount,
					VegaNotification.NOTICEBOARD);
				remoteViews = updateWidgetCount(remoteViews,
					R.id.widgetBooksCount,
					previousBooksCount,
					VegaNotification.BOOKS);
				remoteViews = updateWidgetCount(remoteViews,
					R.id.widgetAttendanceCount,
					previousAttendanceCount,
					VegaNotification.ATTENDENCE);
				remoteViews = updateWidgetCount(remoteViews,
					R.id.widgetMsgCount,
					previousMessageCount,
					VegaNotification.CHAT);
				remoteViews = updateWidgetCount(remoteViews,
					R.id.widgetEventCount, eventCount,
					VegaNotification.EVENTS);
				remoteViews = updateWidgetCount(remoteViews,
					R.id.widgetAddInfoCount,
					previousAdditionalInfoCount,
					VegaNotification.ADDITIONAL_INFO);
				remoteViews = updateWidgetCount(remoteViews,
					R.id.widgetQuizCount,
					previousExamCount,
					VegaNotification.EXAM);
				remoteViews = updateWidgetCount(remoteViews,
					R.id.widgetTestApproveCount,
					previousTestToBeApprovedCount,
					VegaNotification.TEST_TO_BE_APPROVED);
				remoteViews = updateWidgetCount(remoteViews,
					R.id.widgetTestPublishCount,
					previousTestToBePublishedCount,
					VegaNotification.TEST_TO_BE_PUBLISHED);

			    }
			    appWidgetManager.updateAppWidget(widgetId,
				    remoteViews);
			}
		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void onFailure(String failureMessage) {
			// TODO Auto-generated method stub

		    }
		});
    	}
	    } else {
		Logger.warn(tag, "user object is null");
	    }
	}

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

    /**
     * Send request to server.
     *
     * @param url the url
     * @param dc the dc
     */
    public void sendRequestToServer(final String url, final DownloadCallback dc) {
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		try {
		    final URL u = new URL(url);
		    final BufferedReader br = new BufferedReader(
			    new InputStreamReader(u.openStream()));
		    String str;
		    String responseString = "";
		    while ((str = br.readLine()) != null) {
			responseString = str;
		    }
		    dc.onSuccess(responseString);
		} catch (final MalformedURLException e) {
		    Logger.error(tag, e);
		} catch (final IOException e) {
		    // Toast.makeText(getApplicationContext(),
		    // "Unable to connect the server",Toast.LENGTH_LONG);
		    sendRequestToServer(url, dc);

		    Logger.error(tag, e);
		}
	    }
	}).start();
    }

    /**
     * Format message.
     *
     * @param count the count
     * @param type the type
     * @return the string
     */
    private String formatMessage(int count, String type){
	String formattedMessage = "";
	if (count == 1) {
	    /*if (type.equalsIgnoreCase("notice")) {
				formattedMessage = "new Notice";
			} else if (type.equalsIgnoreCase("books")) {
				formattedMessage = "new book in library";
			} */if (type.equalsIgnoreCase("exam to be taken")) {
			    formattedMessage = "new exam to be taken";
			} /*else if (type.equalsIgnoreCase("MESSAGE")) {
				formattedMessage = "new message";
			} else if (type.equalsIgnoreCase("attendence")) {
				formattedMessage = "new student joined";
			} else if (type.equalsIgnoreCase("events")) {
				formattedMessage = "new event";
			} else if (type.equalsIgnoreCase("additionalInfo")) {
				formattedMessage = "new image for book";
			}*/
			else if(type.equalsIgnoreCase("test to be approved"))
			    formattedMessage = "new test to be approved";
			else if(type.equalsIgnoreCase("rejected test"))
			    formattedMessage = "new test rejected";
			else if(type.equalsIgnoreCase("test to be published"))
			    formattedMessage = "new test to be published";
			else if(type.equalsIgnoreCase("test to be evaluated"))
			    formattedMessage = "new test to be evaluated";
			else if(type.equalsIgnoreCase("result to be published"))
			    formattedMessage = "new test to be published";
			else if(type.equalsIgnoreCase("result published"))
			    formattedMessage = "result published";
	} else {
	    /*if (type.equalsIgnoreCase("notice")) {
				formattedMessage = "new Notices";
			} else if (type.equalsIgnoreCase("books")) {
				formattedMessage = "new books in library";
			} else */if (type.equalsIgnoreCase("exam to be taken")) {
			    formattedMessage = "new exams to be taken";
			} /*else if (type.equalsIgnoreCase("MESSAGE")) {
				formattedMessage = "new messages";
			} else if (type.equalsIgnoreCase("attendence")) {
				formattedMessage = "new students joined";
			} else if (type.equalsIgnoreCase("events")) {
				formattedMessage = "new events";
			} else if (type.equalsIgnoreCase("additionalInfo")) {
				formattedMessage = "new images for book";
			}*/
			else if(type.equalsIgnoreCase("test to be approved"))
			    formattedMessage = "new tests to be approved";
			else if(type.equalsIgnoreCase("rejected test"))
			    formattedMessage = "new tests rejected";
			else if(type.equalsIgnoreCase("test to be published"))
			    formattedMessage = "new tests to be published";
			else if(type.equalsIgnoreCase("test to be evaluated"))
			    formattedMessage = "new tests to be evaluated";
			else if(type.equalsIgnoreCase("result to be published"))
			    formattedMessage = "new tests to be published";
			else if(type.equalsIgnoreCase("result published"))
			    formattedMessage = "result published";

	}
	return formattedMessage;
    }

    /**
     * Check user roletype.
     *
     * @return the int
     */
    public int checkUserRoletype() {
	int Role=0; 

	if (applicationData.getUser() != null && applicationData.getUser()
		.getUserType() != null
		&& (applicationData.getUser().getUserType()
			.equalsIgnoreCase(RoleType.SUBJECTHEAD.name())
			|| applicationData.getUser().getUserType()
			.equalsIgnoreCase(RoleType.PRINCIPLE.name()) || applicationData
			.getUser().getUserType()
			.equalsIgnoreCase(RoleType.HOMEROOMTEACHER.name())
			||applicationData.getUser().getUserType().equalsIgnoreCase(RoleType.TEACHER.name()))) {
	    Role=1;
	}else if(applicationData.getUser() != null && applicationData.getUser()
		.getUserType() != null
		&& applicationData.getUser().getUserType()
		.equalsIgnoreCase(RoleType.STUDENT.name())){
	    Role=2;

	}
	return Role;
    }

}
