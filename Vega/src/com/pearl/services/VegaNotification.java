package com.pearl.services;


import java.io.File;
import java.util.ArrayList;

import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.downloadmanager.utils.DownloadType;
import com.pearl.network.request.ServerRequests;
import com.pearl.parsers.json.NoticeBoardParser;
import com.pearl.ui.models.NoticeBoardResponse;
import com.pearl.ui.models.NotificationCount;
import com.pearl.ui.models.VegaNotices;

// TODO: Auto-generated Javadoc
/**
 * The Class VegaNotification.
 */
public class VegaNotification {
    
    /** The user id. */
    private final String userId;
    
    /** The application data. */
    private final ApplicationData applicationData;
    
    /** The tag. */
    private final String TAG = "VegaNotification";
    
    /** The notif count. */
    NotificationCount notifCount;
    
    /** The vega config. */
    VegaConfiguration vegaConfig;
    
    /** The server requests. */
    private final ServerRequests serverRequests;
    
    /** The Constant NOTICE_BOARD_INFO. */
    public static final String NOTICE_BOARD_INFO = "info";
    
    /** The Constant ATTENDENCE. */
    public static final String ATTENDENCE = "attendence";
    
    /** The Constant BOOKS. */
    public static final String BOOKS = "books";
    
    /** The Constant EVENTS. */
    public static final String EVENTS = "events";
    
    /** The Constant NOTICEBOARD. */
    public static final String NOTICEBOARD = "notice";
    
    /** The Constant EXAM. */
    public static final String EXAM = "exam to be taken";
    
    /** The Constant CHAT. */
    public static final String CHAT = "MESSAGE";
    
    /** The Constant ADDITIONAL_INFO. */
    public static final String ADDITIONAL_INFO = "additionalInfo";
    
    /** The Constant SUBJECT. */
    public static final String SUBJECT = "subject";
    
    /** The Constant RESULTS_TO_BE_PUBLISHED. */
    public static final String RESULTS_TO_BE_PUBLISHED = "result to be published";
    
    /** The Constant RESULTS_PUBLISHED. */
    public static final String RESULTS_PUBLISHED = "result published";
    
    /** The Constant TEST_TO_BE_APPROVED. */
    public static final String TEST_TO_BE_APPROVED = "test to be approved";
    
    /** The Constant TEST_TO_BE_PUBLISHED. */
    public static final String TEST_TO_BE_PUBLISHED = "test to be published";
    
    /** The Constant TEST_REJECTED. */
    public static final String TEST_REJECTED = "rejected test";
    
    /** The Constant TEST_TO_BE_EVALAUTED. */
    public static final String TEST_TO_BE_EVALAUTED = "test to be evaluated";
    
    /** The Constant CHAT_LIKE. */
    public static final String CHAT_LIKE = "chatLike";
    
    /** The Constant CHAT_COMMENT. */
    public static final String CHAT_COMMENT = "chatCommented";
    
    /** The Constant EPUB_MODIFIED. */
    public static final String EPUB_MODIFIED = "epubModified";

    /** The info. */
    private final ArrayList<String> info = new ArrayList<String>();
    
    /** The url. */
    private final String url;

    /**
     * Instantiates a new vega notification.
     *
     * @param appData the app data
     */
    public VegaNotification(ApplicationData appData) {

	applicationData = appData;
	userId = applicationData.getUserId();
	Logger.warn("EEEEEEEEEEE", "user Id is:" + userId);
	serverRequests = new ServerRequests(applicationData);
	vegaConfig = new VegaConfiguration(applicationData);
	url = serverRequests.getRequestURL(ServerRequests.NOTICEBOARD, ""
		+ userId);
	Logger.warn("Vega notification", "in vega notification constructor");
	if (userId != null) {
	    try {
		downloadInfo();
	    } catch (final Exception e) {

		e.printStackTrace();
	    }
	}
	applicationData.getApplicationContext();
    }

    /**
     * gets the count of quiz,score and messages.
     *
     * @param notificationType the notification type
     * @return the count
     */
    public int getCount(String notificationType) {

	Logger.warn("Vega Notification", "notification type is:"
		+ notificationType);

	int attCount = 0;
	int noticecount = 0;
	int msgCount = 0;
	int booksCount = 0;
	int subjectcount = 0;

	try {
	    attCount = Integer.parseInt(vegaConfig
		    .getValue(VegaConstants.ATTENDANCE_COUNT));
	    noticecount = Integer.parseInt(vegaConfig
		    .getValue(VegaConstants.NOTICE_COUNT));
	    Logger.info("Vega Notif",
		    "NOTICE - NOTICE COUNT value from config=" + noticecount);
	    msgCount = Integer.parseInt(vegaConfig
		    .getValue(VegaConstants.MESSAGE_COUNT));
	    booksCount = Integer.parseInt(vegaConfig
		    .getValue(VegaConstants.BOOKS_COUNT));
	    Integer.parseInt(vegaConfig
		    .getValue(VegaConstants.EXAM_COUNT));
	    subjectcount = Integer.parseInt(vegaConfig
		    .getValue(VegaConstants.SUBJECT_COUNT));
	} catch (final InvalidConfigAttributeException e) {
	    Logger.warn("VegaNotif", e.toString());
	}

	try {

	    if (notificationType
		    .equalsIgnoreCase(VegaNotification.NOTICEBOARD)) {
		return noticecount;
	    } else if (notificationType
		    .equalsIgnoreCase(VegaNotification.BOOKS)) {
		return booksCount;
	    } else if (notificationType
		    .equalsIgnoreCase(VegaNotification.ATTENDENCE)) {
		return attCount;
	    } else if (notificationType.equalsIgnoreCase(VegaNotification.CHAT)) {
		return msgCount;
	    } else if (notificationType
		    .equalsIgnoreCase(VegaNotification.SUBJECT)) {
		return subjectcount;
	    } else {
		return 0; // ideally should throw InvalidNotificationType
		// exception
	    }
	} catch (final Exception e) {
	    return 0;
	}
    }

    /**
     * Gets the notice board info.
     *
     * @return the notice board info
     */
    public VegaNotices getNoticeBoardInfo() {

	NoticeBoardResponse noticeBoardResponse = null;
	try {
	    noticeBoardResponse = NoticeBoardParser
		    .getNoticeBoardContent(new File(applicationData
			    .getNoticeBoardFile()));
	} catch (final Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	/*
	 * String content = "";
	 * 
	 * String filePath =
	 * applicationData.getNoticeBoardFilePath()+ApplicationData
	 * .NOTIFICATION_FILENAME; try { content =
	 * ApplicationData.readFile(filePath); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * ObjectMapper mapper = new ObjectMapper(); ObjectReader reader =
	 * mapper.reader(NoticeBoardResponse.class);
	 * Logger.warn("vega notification",
	 * "content is:-------------------"+content);
	 * 
	 * NoticeBoardResponse noticeBoardResponse = null; try {
	 * noticeBoardResponse = reader.readValue(content); } catch
	 * (JsonProcessingException e) { Logger.error("VegaNotification", e); }
	 * catch (IOException e) { Logger.error("VegaNotification", e); }
	 */
	Logger.warn("VegaNotification", "NOTICEBOARD - noticeboard data is:"
		+ noticeBoardResponse.getData().toString());
	final VegaNotices notices = noticeBoardResponse.getData();
	Logger.warn("VegaNotification", "NOTICEBOARD - notices size is:"
		+ notices.getUserNotices().size());

	return notices;
    }

    /**
     * Download info.
     *
     * @throws Exception the exception
     */
    private void downloadInfo() throws Exception {
	try {
	    Logger.warn(TAG, "In download()");
	    final DownloadType downloadType = new DownloadType();
	    downloadType.setType(DownloadType.DEFAULT);
	    Logger.warn(TAG, "filePath is:" + applicationData.getInfoFilePath());
	    Logger.warn(TAG, "URL is:" + url + "File name is:"
		    + ApplicationData.NOTIFICATION_FILENAME);
	    final Download download = new Download(url,
		    applicationData.getInfoFilePath(),
		    ApplicationData.NOTIFICATION_FILENAME);
	    final DownloadManager notificationDownloader = new DownloadManager(
		    applicationData, download);
	    notificationDownloader.startDownload(new DownloadCallback() {
		@Override
		public void onSuccess(String downloadedString) {
		    Logger.warn("Vega Notification", "Download success");
		    try {
			getNoticeBoardInfo();
		    } catch (final Exception e) {

			e.printStackTrace();
		    }
		}

		@Override
		public void onProgressUpdate(int progressPercentage) {
		    // TODO Auto-generated method stub

		}

		@Override
		public void onFailure(String failureMessage) {

		}
	    });
	} catch (final Exception e) {

	    e.printStackTrace();
	}
    }

    /**
     * downloads the XML.
     *
     * @param dc the dc
     */

    public void download(DownloadCallback dc) {
	try {
	    Logger.warn(TAG, "In download()");
	    final DownloadType downloadType = new DownloadType();
	    downloadType.setType(DownloadType.DEFAULT);

	    Logger.warn(TAG, "filePath is:" + applicationData.getInfoFilePath());
	    Logger.warn(TAG, "URL is:" + url + "File name is:"
		    + ApplicationData.NOTIFICATION_FILENAME);
	    final Download download = new Download(downloadType, url,
		    applicationData.getInfoFilePath(),
		    ApplicationData.NOTIFICATION_FILENAME);
	    final DownloadManager notificationDownloader = new DownloadManager(
		    applicationData, download);
	    notificationDownloader.startDownload(dc);
	} catch (final Exception e) {

	    e.printStackTrace();
	}
    }

   
}
