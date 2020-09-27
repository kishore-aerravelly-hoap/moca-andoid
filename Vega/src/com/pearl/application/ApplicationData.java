/**
 * 
 */
package com.pearl.application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;

import com.pearl.R;
import com.pearl.baseresponse.login.AndroidUser;
import com.pearl.booklist.BookList;
import com.pearl.books.Book;
import com.pearl.examination.Exam;
import com.pearl.exceptions.UserIDMismatchException;
import com.pearl.logger.Logger;
import com.pearl.network.NetworkDemon;
import com.pearl.parsers.xml.exceptions.XmlParserHandlerException;
import com.pearl.ui.models.BaseResponse;
import com.pearl.ui.models.LoginResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationData.
 */
@ReportsCrashes(formKey = "", // will not be used
mailTo = "pearlsupportlog@pressmart.com", mode = ReportingInteractionMode.SILENT, resDialogText = R.string.crash_dialog_text, resDialogIcon = R.drawable.warning, // optional.
customReportContent = { ReportField.ANDROID_VERSION,
	ReportField.APP_VERSION_NAME, ReportField.APP_VERSION_CODE,
	ReportField.CRASH_CONFIGURATION, ReportField.DEVICE_ID,
	ReportField.LOGCAT, ReportField.USER_CRASH_DATE,
	ReportField.STACK_TRACE, ReportField.PHONE_MODEL,
	ReportField.PACKAGE_NAME },
	deleteUnapprovedReportsOnApplicationStart=true,
	maxNumberOfRequestRetries=3,
	 
	resDialogTitle = R.string.crash_dialog_title,// optional. default is your
// application name
resDialogOkToast = R.string.crash_dialog_ok_toast)
public class ApplicationData extends Application {
    
    /** The version name. */
    String versionName;
    
    /** The question num. */
    int questionNum;

    /** The user auth id. */
    private String userAuthId = "";
    // private User user;
    /** The user. */
    private AndroidUser user;
    
    /** The question type. */
    private List<String> questionType;

    /**
     * Gets the question type.
     *
     * @return the question type
     */
    public List<String> getQuestionType() {
	return questionType;
    }

    /**
     * Sets the question type.
     *
     * @param questionType the new question type
     */
    public void setQuestionType(List<String> questionType) {
	this.questionType = questionType;
    }

    /** The book. */
    private Book book;
    
    /** The downloaded books list. */
    private ArrayList<Book> downloadedBooksList;
    
    /** The available books list. */
    private ArrayList<Book> availableBooksList;
    
    /** The open exambooks list. */
    private List<String> openExambooksList;
    
    /** The class monitor. */
    private boolean classMonitor = false;
    
    /** The vega config. */
    private VegaConfiguration vegaConfig;

    /** The current exam. */
    private Exam currentExam;
    
    /** The current exam question no. */
    private int currentExamQuestionNo = 0;
    
    /** The app path. */
    private String appPath = "com.vega";
    
    /** The login status. */
    private boolean loginStatus = false;;
    
    /** The exam id. */
    private String examId;

    /** The network connectivity. */
    private NetworkDemon networkConnectivity;

    /** The Constant ENCRYPTION_PASSWORD. */
    public static final String ENCRYPTION_PASSWORD = "vegaapplication";

    /** The Constant AVAILABLEBOOKSLIST_FILENAME. */
    public static final String AVAILABLEBOOKSLIST_FILENAME = "availablebooks.txt"; // online
    
    /** The Constant DOWNLOADEDBOOKSLIST_FILENAME. */
    public static final String DOWNLOADEDBOOKSLIST_FILENAME = "downloadedbooks.txt"; // shelf
    
    /** The Constant NOTIFICATION_COUNT_FILENAME. */
    public static final String NOTIFICATION_COUNT_FILENAME = "NotifiCount.txt";
    
    /** The Constant NOTIFICATION_FILENAME. */
    public static final String NOTIFICATION_FILENAME = "Notif.txt";
    
    /** The Constant BOOKNAME_PREFIX. */
    public static final String BOOKNAME_PREFIX = "book_";
    
    /** The Constant COVER_IMAGE_NAME. */
    public static final String COVER_IMAGE_NAME = "coverpage";
    
    /** The Constant DICTIONAY_FILENAME. */
    public static final String DICTIONAY_FILENAME = "dictionary_meaning.xml";
    
    /** The Constant LAST_LOGIN_RESPONSE_FILENAME. */
    public static final String LAST_LOGIN_RESPONSE_FILENAME = "last_loginrequest.txt";
    
    /** The Constant EXAMSLIST_FILENAME. */
    public static final String EXAMSLIST_FILENAME = "examslist.txt";
    
    /** The Constant EXAM_FILENAME. */
    public static final String EXAM_FILENAME = "exam.txt";
    
    /** The Constant EXAM_ANSWERS_SAVE_FILENAME. */
    public static final String EXAM_ANSWERS_SAVE_FILENAME = "examanswers_save.txt";
    
    /** The Constant EXAM_ANSWERS_SUBMIT_FILENAME. */
    public static final String EXAM_ANSWERS_SUBMIT_FILENAME = "examanswers_submit.txt";
    
    /** The Constant EXAM_ANSWERS_UPLOAD_FILENAME. */
    public static final String EXAM_ANSWERS_UPLOAD_FILENAME = "examanswers_uploaded.txt";
    
    /** The Constant ANSWER_SHEET_FILENAME. */
    public static final String ANSWER_SHEET_FILENAME = "answer_sheet.txt";
    
    /** The Constant ADDITIONAL_INFO_FILENAME. */
    public static final String ADDITIONAL_INFO_FILENAME = "additionalinfo.txt";
    
    /** The Constant CHAT_WALL_FILENAME. */
    public static final String CHAT_WALL_FILENAME = "chatwall.txt";
    
    /** The Constant CHAT_BASE_RESPONSE_FILE. */
    public static final String CHAT_BASE_RESPONSE_FILE = "chatResponse.txt";
    
    /** The Constant ATTENDANCE. */
    public static final String ATTENDANCE = "attendance.txt";
    
    /** The Constant ATTENDANCE_RESPONSE. */
    public static final String ATTENDANCE_RESPONSE = "attendanceResponse.txt";
    
    /** The Constant ATTENDANCE_TAKEN_DATE. */
    public static final String ATTENDANCE_TAKEN_DATE = "lastAttendanceTakenDetails.txt";
    
    /** The Constant CALENDAR_EVENTS. */
    public static final String CALENDAR_EVENTS = "calendar.txt";
    
    /** The Constant SUBJECTS_LIST_FILE. */
    public static final String SUBJECTS_LIST_FILE = "subjectsList.txt";
    
    /** The Constant STEP1_FILE_NAME. */
    public static final String STEP1_FILE_NAME = "step1.txt";
    
    /** The Constant STEP2_FILE_NAME. */
    public static final String STEP2_FILE_NAME = "step2.txt";
    
    /** The Constant STEP3_FILE_NAME. */
    public static final String STEP3_FILE_NAME = "step3.txt";
    
    /** The Constant STEP4_FILE_NAME. */
    public static final String STEP4_FILE_NAME = "step4.txt";
    
    /** The Constant UNDER_CONSTRUCTION_TESTS_LIST. */
    public static final String UNDER_CONSTRUCTION_TESTS_LIST = "testlist.txt";
    
    /** The Constant QUESTION_TYPE_FILE. */
    public static final String QUESTION_TYPE_FILE = "questionType.txt";
    
    /** The Constant TEST_CATEGORY_FILE. */
    public static final String TEST_CATEGORY_FILE = "test_categories.txt";
    
    /** The Constant ACADEMIC_YEAR_FILE. */
    public static final String ACADEMIC_YEAR_FILE = "academic_year.txt";

    /** Teacher Approval Rejection,Comments files for Exams. */

    public static final String TEACHER_APPROVAL_EXAM_LIST = "my_approval_exam_list.txt";
    
    /** The Constant TEACHER_APPROVAL_EXAM. */
    public static final String TEACHER_APPROVAL_EXAM = "my_approval_exams.txt";
    
    /** The Constant TEACHER_REJECTED_EXAM_LIST. */
    public static final String TEACHER_REJECTED_EXAM_LIST = "my_rejected_exam_list.txt";
    
    /** The Constant TEACHER_REJECTED_EXAM. */
    public static final String TEACHER_REJECTED_EXAM = "my_rejected_exams.txt";
    
    /** The Constant TEACHER_REJECTED_COMMENTS. */
    public static final String TEACHER_REJECTED_COMMENTS = "my_rejected_comments.txt";
    
    /** The Constant TEACHER_AWAITING_APPROVAL_EXAM_LIST. */
    public static final String TEACHER_AWAITING_APPROVAL_EXAM_LIST = "my_approval_exams_list.txt";
    
    /** The Constant TEACHER_AWAITING_APPROVAL_EXAMS. */
    public static final String TEACHER_AWAITING_APPROVAL_EXAMS = "my_approval_exams.txt";
    
    /** The Constant RESULTS_PUBLISH_ANSWERSHEET. */
    public static final String RESULTS_PUBLISH_ANSWERSHEET = "results_publish_answersheet.txt";
    
    /** The Constant RESULTS_PUBLISH_STUDENTS_LIST. */
    public static final String RESULTS_PUBLISH_STUDENTS_LIST = "results_publish_students_list.txt";
    
    /** The Constant REJCTED_TESTS_LIST. */
    public static final String REJCTED_TESTS_LIST = "rejected_tests_list.txt";
    
    /** The Constant REJCTED_TEST_QUESTIONPAPER. */
    public static final String REJCTED_TEST_QUESTIONPAPER = "rejected_test_question_paper.txt";
    
    /** The Constant REJCTED_TEST_COMMENTS. */
    public static final String REJCTED_TEST_COMMENTS = "rejected_test_comments.txt";
    // canvas image
    /** The Constant CANVAS_IMAGE_FILE_NAME_PRE. */
    public static final String CANVAS_IMAGE_FILE_NAME_PRE = "canvas_page_";
    
    /** The Constant CANVAS_IMAGE_SAVE_FORMAT. */
    public static final String CANVAS_IMAGE_SAVE_FORMAT = "png";

    /** The device id. */
    private String deviceId;
    
    /** The activity name. */
    public String activityName = "";

    /**
     * Reset application data.
     */
    public void resetApplicationData() {
	user = new AndroidUser();
	book = new Book();
	downloadedBooksList = new ArrayList<Book>();
	availableBooksList = new ArrayList<Book>();
	// openExambooksList = new ArrayList<Integer>();

	networkConnectivity = new NetworkDemon(
		(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));

	// TODO remove below statement
	// networkConnectivity = new NetworkDemon();
    }

    /**
     * Sets the version.
     *
     * @param version the new version
     */
    public void setVersion(String version) {
	this.versionName = version;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public String getVersion() {
	return this.versionName;
    }

    /**
     * Setters.
     *
     * @param authId the auth id
     * @return the application data
     */
    public ApplicationData setAuthID(String authId) {
	userAuthId = authId;

	return this;
    }

    /**
     * Sets the user.
     *
     * @param user the user
     * @return the application data
     */
    public ApplicationData setUser(AndroidUser user) {
	this.user = user;

	return this;
    }

    /**
     * Sets the book.
     *
     * @param book the book
     * @return the application data
     */
    public ApplicationData setBook(Book book) {
	this.book = book;
	return this;
    }

    /**
     * Sets the downloaded book list.
     *
     * @param downloaded_books_list the downloaded_books_list
     * @return the application data
     */
    public ApplicationData setDownloadedBookList(
	    ArrayList<Book> downloaded_books_list) {
	this.downloadedBooksList = downloaded_books_list;

	return this;
    }

    /**
     * Sets the available books list.
     *
     * @param available_books_list the available_books_list
     * @return the application data
     */
    public ApplicationData setAvailableBooksList(
	    ArrayList<Book> available_books_list) {
	this.availableBooksList = available_books_list;

	return this;
    }

    /**
     * Gets the current exam.
     *
     * @return the current exam
     */
    public Exam getCurrentExam() {
	return this.currentExam;
    }

    /**
     * Sets the current exam.
     *
     * @param ex the new current exam
     */
    public void setCurrentExam(Exam ex) {
	this.currentExam = ex;
    }

    /**
     * Getters.
     *
     * @return the auth id
     */
    public String getAuthID() {
	return userAuthId;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public AndroidUser getUser() {
	if (user == null) {
	    user = new AndroidUser();
	}

	return user;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public String getUserId() {
	return getUser().getId();
    }

    /**
     * Gets the book.
     *
     * @return the book
     */
    public Book getBook() {
	return book;
    }

    /**
     * Gets the book id.
     *
     * @return the book id
     */
    public int getBookId() {
	return book.getBookId();
    }

    /**
     * Gets the grade.
     *
     * @return the grade
     */
    public String getGrade() {
	return user.getGradeId();
    }

    /**
     * Gets the grade name.
     *
     * @return the grade name
     */
    public String getGradeName() {
	return user.getGradeName();
    }

    /**
     * Gets the section name.
     *
     * @return the section name
     */
    public String getSectionName() {
	return user.getSectionName();
    }

    /**
     * Gets the download books list.
     *
     * @return the download books list
     */
    public ArrayList<Book> getDownloadBooksList() {
	return downloadedBooksList;
    }

    /**
     * Gets the available books list.
     *
     * @return the available books list
     */
    public ArrayList<Book> getAvailableBooksList() {
	return availableBooksList;
    }

    /**
     * Gets the user object from last login response.
     *
     * @return the user object from last login response
     * @throws JsonParseException the json parse exception
     * @throws JsonMappingException the json mapping exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public AndroidUser getUserObjectFromLastLoginResponse()
	    throws JsonParseException, JsonMappingException, IOException {
	vegaConfig = new VegaConfiguration(getApplicationContext());
	String originalContent = readFile(getLastUserLoginResponseXML());
	ObjectMapper mapper = new ObjectMapper();
	LoginResponse loginResponse = mapper.readValue(originalContent,
		LoginResponse.class);

	return loginResponse.getUser();
    }

    /**
     * Gets the user details from last login json response.
     *
     * @param uid the uid
     * @return the user details from last login json response
     * @throws XmlParserHandlerException the xml parser handler exception
     * @throws UserIDMismatchException the user id mismatch exception
     * @throws JsonParseException the json parse exception
     * @throws JsonMappingException the json mapping exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public AndroidUser getUserDetailsFromLastLoginJSONResponse(String uid)
	    throws XmlParserHandlerException, UserIDMismatchException,
	    JsonParseException, JsonMappingException, IOException {
	if (isFileExists(getLastUserLoginResponseXML())) {
	    Logger.warn("App Data", "getUserDetailsFromLastLoginResponse - IF");
	    AndroidUser temp_user = getUserObjectFromLastLoginResponse();

	    if (temp_user != null && temp_user.getId().equals(uid)) {
		Logger.error(
			"ApplicationData - getUserDetailsFromLastLoginResponse:",
			"new user id is " + temp_user.getId());

		return temp_user;
	    } else {
		throw new UserIDMismatchException();
	    }
	}

	return null;
    }

    /**
     * Gets the book.
     *
     * @param bookId the book id
     * @return the book
     */
    public Book getBook(int bookId) {
	// List<Book> books =
	// XMLParser.getBooksList(getDownloadedBooksListXML());
	BookList booksList = new BookList();
	ObjectMapper mapper = new ObjectMapper();
	BaseResponse jsonResponse = null;
	if (ApplicationData.isFileExists(getDownloadedBooksListXML())) {
	    try {
		jsonResponse = mapper.readValue(new File(
			getDownloadedBooksListXML()), BaseResponse.class);
	    } catch (JsonParseException e) {
		Logger.error("ApplicationData- JsonParseException  ", e);
	    } catch (JsonMappingException e) {
		Logger.error("ApplicationData- JsonMappingException", e);
	    } catch (IOException e) {
		Logger.error("ApplicationData- IOException", e);
	    }
	    String jsonData = jsonResponse.getData().toString();
	    Logger.warn("ApplicationData",
		    "--- message is:" + jsonResponse.getMessage());
	    if (jsonData != null) {
		Logger.warn("ApplicationData", "Json form server is:"
			+ jsonData);
		ObjectReader reader = mapper.reader(BookList.class);
		try {
		    Logger.warn("ApplicationData",
			    "reader value is:" + reader.readValue(jsonData));
		    booksList = reader.readValue(jsonData);
		} catch (JsonProcessingException e) {
		    Logger.error("ApplicationData", e);
		} catch (IOException e) {
		    Logger.error("ApplicationData", e);
		}
	    } else {
		Logger.warn("ApplicationData", "json data is null");
	    }

	    Logger.error("appdata- getBook", "books list json path is "
		    + getDownloadedBooksListXML() + "; books size is "
		    + booksList.getBookList().size());

	    for (Book book : booksList.getBookList()) {
		Logger.info("appdata-getBook - bookId",
			"id:" + book.getBookId());

		if (bookId == book.getBookId()) {
		    return book;
		}
	    }

	    Logger.error("APPLICATIONDATA -getBook - bookId",
		    "no matching book with id "

		    + bookId + " found!");
	} else {
	    Logger.info("APPLICATIONDATA- GETBOOK",
		    "NO BOOKS FOUND IN LOCAL STORAGE");
	}
	return null;
    }

    /**
     * Booleans Getters.
     *
     * @return true, if is network connection available
     */
    public boolean isNetworkConnectionAvailable() {
	if (networkConnectivity == null) {
	    networkConnectivity = new NetworkDemon(
		    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
	}

	return this.networkConnectivity.isConnectionAvailabe();
    }

    /**
     * Helper functions.
     *
     * @return the app private files path
     */
    /*
     * File Paths getter methods -- START --
     */
    public String getAppPrivateFilesPath() {
	return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + appPath + "/";// "/data/data/com.vega/files/";
    }

    /**
     * Sets the app private files path.
     *
     * @param newPath the new app private files path
     */
    public void setAppPrivateFilesPath(String newPath) {
	appPath = newPath;
    }

    /**
     * Gets the users file path.
     *
     * @return the users file path
     */
    public String getUsersFilePath() {
	return getAppPrivateFilesPath() + "users/";
    }

    /**
     * Gets the teacher file path.
     *
     * @param teacherId the teacher id
     * @return the teacher file path
     */
    public String getTeacherFilePath(String teacherId) {
	return getUsersFilePath() + "user_" + teacherId + "/";
    }

    /**
     * Gets the teacher exam file path.
     *
     * @param teacherId the teacher id
     * @param examid the examid
     * @return the teacher exam file path
     */
    public String getTeacherExamFilePath(String teacherId, String examid) {
	return getTeacherFilePath(teacherId) + "ApprovalList_" + examid + "/";
    }

    /**
     * Gets the current user files path.
     *
     * @return the current user files path
     */
    public String getCurrentUserFilesPath() {
	return getUsersFilePath() + "user_" + getUserId() + "/";
    }

    /**
     * Gets the info file path.
     *
     * @return the info file path
     */
    public String getInfoFilePath() {
	return getCurrentUserFilesPath() + "info/";
    }

    /**
     * Gets the books files path.
     *
     * @return the books files path
     */
    public String getBooksFilesPath() {
	// return getAppPrivateFilesPath() + "books/";
	return getCurrentUserFilesPath() + "books/";
    }

    /**
     * Gets the book files path.
     *
     * @param book_id the book_id
     * @return the book files path
     */
    public String getBookFilesPath(long book_id) {
	long b = book_id;

	if (book_id == 0) {
	    b = getBookId();
	}

	return getBooksFilesPath() + "book_" + b + "/";
    }

    /**
     * Gets the book canvas files path.
     *
     * @param book_id the book_id
     * @return the book canvas files path
     */
    public String getBookCanvasFilesPath(int book_id) {
	long b = book_id;

	if (book_id == 0) {
	    b = getBookId();
	}

	return getBooksFilesPath() + "book_" + b + "/canvas/";
    }

    /**
     * Gets the book canvas file name.
     *
     * @param book_id the book_id
     * @param pageNo the page no
     * @return the book canvas file name
     */
    public String getBookCanvasFileName(int book_id, int pageNo) {
	return getBookCanvasFilesPath(book_id)
		+ ApplicationData.CANVAS_IMAGE_FILE_NAME_PRE + pageNo + "."
		+ ApplicationData.CANVAS_IMAGE_SAVE_FORMAT;
    }

    /**
     * Gets the complete book file path.
     *
     * @param book the book
     * @return the complete book file path
     */
    public String getCompleteBookFilePath(Book book) {
	return getBookFilesPath(book.getBookId()) + book.getFilename();
    }

    /**
     * Gets the books lists path.
     *
     * @return the books lists path
     */
    public String getBooksListsPath() {
	// return getCurrentUserFilesPath() + "bookslists/";
	return getBooksFilesPath() + "bookslists/";
    }

    /**
     * Gets the available books lists path.
     *
     * @return the available books lists path
     */
    public String getAvailableBooksListsPath() {
	return getBooksListsPath();
    }

    /**
     * Gets the available books list xml.
     *
     * @return the available books list xml
     */
    public String getAvailableBooksListXML() {
	return getAvailableBooksListsPath() + AVAILABLEBOOKSLIST_FILENAME;// "availablebooks.xml";
    }

    /**
     * Gets the downloaded books lists path.
     *
     * @return the downloaded books lists path
     */
    public String getDownloadedBooksListsPath() {
	return getBooksListsPath();
    }

    /**
     * Gets the downloaded books list xml.
     *
     * @return the downloaded books list xml
     */
    public String getDownloadedBooksListXML() {
	return getDownloadedBooksListsPath() + DOWNLOADEDBOOKSLIST_FILENAME;// "downloadedbooks.xml";
    }

    /**
     * Gets the image gallery path for page.
     *
     * @param pageNo the page no
     * @return the image gallery path for page
     */
    public String getImageGalleryPathForPage(int pageNo) {
	return getImageGalleryPath() + "/images_" + pageNo + "/";
    }

    /**
     * Gets the image gallery path.
     *
     * @return the image gallery path
     */
    public String getImageGalleryPath() {
	return getBookFilesPath(book.getBookId()) + "/images/";
    }

    /**
     * Gets the additional info image path.
     *
     * @return the additional info image path
     */
    public String getAdditionalInfoImagePath() {
	return getImageGalleryPath();
    }

    /**
     * Gets the additional info video path.
     *
     * @return the additional info video path
     */
    public String getAdditionalInfoVideoPath() {
	return getVideoGalleryPath();
    }

    /**
     * Gets the video gallery path.
     *
     * @return the video gallery path
     */
    public String getVideoGalleryPath() {
	return getBookFilesPath(book.getBookId()) + "/videos/";
    }

    /**
     * Gets the video gallery path for page.
     *
     * @param pageNo the page no
     * @return the video gallery path for page
     */
    public String getVideoGalleryPathForPage(int pageNo) {
	return getVideoGalleryPath() + "/videos_" + pageNo + "/";
    }

    /**
     * Gets the document path for page.
     *
     * @param pageNo the page no
     * @return the document path for page
     */
    public String getDocumentPathForPage(int pageNo) {
	return getAdditionalInfoDocumentPath() + "/documnet_" + pageNo + "/";
    }

    /**
     * Gets the additional info document path.
     *
     * @return the additional info document path
     */
    public String getAdditionalInfoDocumentPath() {
	return getBookFilesPath(book.getBookId()) + "/documents/";
    }

    /**
     * Gets the dictionary meaning files path.
     *
     * @return the dictionary meaning files path
     */
    public String getDictionaryMeaningFilesPath() {
	return getCurrentUserTempPath();
    }

    /**
     * Gets the dictionary meaning xml.
     *
     * @return the dictionary meaning xml
     */
    public String getDictionaryMeaningXML() {
	return getDictionaryMeaningFilesPath() + DICTIONAY_FILENAME;// "downloadedbooks.xml";
    }

    /**
     * Gets the notification count file path.
     *
     * @return the notification count file path
     */
    public String getNotificationCountFilePath() {
	return getInfoFilePath();
    }

    /**
     * Gets the notification count file.
     *
     * @return the notification count file
     */
    public String getNotificationCountFile() {
	// + NOTIFICATION_FILENAME;
	return getNotificationCountFilePath() + NOTIFICATION_COUNT_FILENAME;
    }

    /**
     * Gets the notice board file path.
     *
     * @return the notice board file path
     */
    public String getNoticeBoardFilePath() {
	return getInfoFilePath();
    }

    /**
     * Gets the notice board file.
     *
     * @return the notice board file
     */
    public String getNoticeBoardFile() {
	// + NOTIFICATION_FILENAME;
	return getNoticeBoardFilePath() + NOTIFICATION_FILENAME;
    }

    /**
     * Gets the app temp path.
     *
     * @return the app temp path
     */
    public String getAppTempPath() {
	return getAppPrivateFilesPath() + "temp_data/";
    }

    /**
     * Gets the exams file path.
     *
     * @return the exams file path
     */
    public String getExamsFilePath() {
	return getCurrentUserFilesPath() + "exams/";
    }

    /**
     * Gets the question type file name.
     *
     * @return the question type file name
     */
    public String getQuestionTypeFileName() {
	return getCurrentUserFilesPath() + QUESTION_TYPE_FILE;

    }

    /**
     * Gets the step2 file path.
     *
     * @param testId the test id
     * @return the step2 file path
     */
    public String getStep2FilePath(String testId) {
	return getExamFilesPath(testId);
    }

    /**
     * Gets the step2 file name.
     *
     * @param testId the test id
     * @return the step2 file name
     */
    public String getStep2FileName(String testId) {
	return getStep1FilePath(testId) + STEP2_FILE_NAME;
    }

    /**
     * Gets the step3 file path.
     *
     * @param testId the test id
     * @return the step3 file path
     */
    public String getStep3FilePath(String testId) {
	return getExamFilesPath(testId);
    }

    /**
     * Gets the step3 file name.
     *
     * @param testId the test id
     * @return the step3 file name
     */
    public String getStep3FileName(String testId) {
	return getStep1FilePath(testId) + STEP3_FILE_NAME;
    }

    /**
     * Gets the step4 file path.
     *
     * @param testId the test id
     * @return the step4 file path
     */
    public String getStep4FilePath(String testId) {
	return getExamFilesPath(testId);
    }

    /**
     * Gets the step4 file name.
     *
     * @param testId the test id
     * @return the step4 file name
     */
    public String getStep4FileName(String testId) {
	return getStep1FilePath(testId) + STEP4_FILE_NAME;
    }

    /**
     * Gets the step1 file path.
     *
     * @param testId the test id
     * @return the step1 file path
     */
    public String getStep1FilePath(String testId) {
	return getExamFilesPath(testId);
    }

    /**
     * Gets the step1 file name.
     *
     * @param testId the test id
     * @return the step1 file name
     */
    public String getStep1FileName(String testId) {
	return getStep1FilePath(testId) + STEP1_FILE_NAME;
    }

    /**
     * Gets the tests list file name.
     *
     * @return the tests list file name
     */
    public String getTestsListFileName() {
	return getExamsListFilePath() + UNDER_CONSTRUCTION_TESTS_LIST;
    }

    /**
     * Gets the exams list xml path.
     *
     * @return the exams list xml path
     */
    public String getExamsListXMLPath() {
	return getExamsFilePath();
    }

    /**
     * Gets the exams list file path.
     *
     * @return the exams list file path
     */
    public String getExamsListFilePath() {
	return getExamsFilePath();
    }

    /**
     * Gets the exams list file.
     *
     * @return the exams list file
     */
    public String getExamsListFile() {
	return getExamsListFilePath() + EXAMSLIST_FILENAME;
    }

    /**
     * Gets the exams list xml.
     *
     * @return the exams list xml
     */
    public String getExamsListXML() {
	return getExamsListXMLPath() + EXAMSLIST_FILENAME;
    }

    /**
     * Gets the exam files path.
     *
     * @param examId the exam id
     * @return the exam files path
     */
    public String getExamFilesPath(String examId) {
	return getExamsFilePath() + "exam_" + examId + "/";
    }

    /**
     * Gets the exam file name.
     *
     * @param examId the exam id
     * @return the exam file name
     */
    public String getExamFileName(String examId) {
	return getExamFilesPath(examId) + EXAM_FILENAME;
    }

    /**
     * Gets the exam saved answers file name.
     *
     * @param examId the exam id
     * @return the exam saved answers file name
     */
    public String getExamSavedAnswersFileName(String examId) {
	return getExamFilesPath(examId) + EXAM_ANSWERS_SAVE_FILENAME;
    }

    /**
     * Gets the exam submitted answers file name.
     *
     * @param examId the exam id
     * @return the exam submitted answers file name
     */
    public String getExamSubmittedAnswersFileName(String examId) {
	return getExamFilesPath(examId) + EXAM_ANSWERS_SUBMIT_FILENAME;
    }

    /**
     * Gets the exam uploaded answers file name.
     *
     * @param examId the exam id
     * @return the exam uploaded answers file name
     */
    public String getExamUploadedAnswersFileName(String examId) {
	return getExamFilesPath(examId) + EXAM_ANSWERS_UPLOAD_FILENAME;
    }

    /**
     * Gets the last user login response xml path.
     *
     * @return the last user login response xml path
     */
    public String getLastUserLoginResponseXMLPath() {
	return getAppTempPath();
    }

    /**
     * Gets the last user login response xml.
     *
     * @return the last user login response xml
     */
    public String getLastUserLoginResponseXML() {
	return getAppTempPath() + LAST_LOGIN_RESPONSE_FILENAME;
    }

    /**
     * Gets the app apk upgrades path.
     *
     * @return the app apk upgrades path
     */
    public String getAppAPKUpgradesPath() {
	return getAppPrivateFilesPath() + "upgrades/";
    }

    /**
     * Gets the evaluated answer sheet path.
     *
     * @param examId the exam id
     * @return the evaluated answer sheet path
     */
    public String getEvaluatedAnswerSheetPath(String examId) {
	return getExamFilesPath(examId) + "answerSheet/";
    }

    /**
     * Gets the evaluated answer sheet file.
     *
     * @param examId the exam id
     * @return the evaluated answer sheet file
     */
    public String getEvaluatedAnswerSheetFile(String examId) {
	return getEvaluatedAnswerSheetPath(examId) + ANSWER_SHEET_FILENAME;
    }

    /**
     * Gets the user temp path.
     *
     * @return the user temp path
     */
    public String getUserTempPath() {
	return getUsersFilePath() + "temp_data/";
    }

    /**
     * Gets the current user temp path.
     *
     * @return the current user temp path
     */
    public String getCurrentUserTempPath() {
	return getCurrentUserFilesPath() + "temp_data/";
    }

    /**
     * Gets the epub temp path.
     *
     * @return the epub temp path
     */
    public String getEpubTempPath() {
	return getAppTempPath() + "epub_data/";
    }

    /**
     * Gets the user book reader temp path.
     *
     * @return the user book reader temp path
     */
    public String getUserBookReaderTempPath() {
	return getCurrentUserTempPath() + "book_temp_data/";
    }

    /**
     * Gets the user book reader temp path.
     *
     * @param bookId the book id
     * @return the user book reader temp path
     */
    public String getUserBookReaderTempPath(int bookId) {
	return getUserBookReaderTempPath() + "book_" + bookId + "/";
    }

    /**
     * Gets the chat wall path.
     *
     * @return the chat wall path
     */
    public String getChatWallPath() {
	return getAppTempPath();
    }

    /**
     * Gets the chat wall file.
     *
     * @return the chat wall file
     */
    public String getChatWallFile() {
	return getChatWallPath() + CHAT_WALL_FILENAME;
    }

    /**
     * Gets the chat base response file path.
     *
     * @return the chat base response file path
     */
    public String getChatBaseResponseFilePath() {
	return getAppTempPath();
    }

    /**
     * Gets the attendance path.
     *
     * @return the attendance path
     */
    public String getAttendancePath() {
	return getCurrentUserFilesPath();
    }

    /**
     * Gets the attendance file.
     *
     * @return the attendance file
     */
    public String getAttendanceFile() {
	return getAttendancePath() + ATTENDANCE;
    }

    /**
     * Gets the last attendance taken file path.
     *
     * @return the last attendance taken file path
     */
    public String getLastAttendanceTakenFilePath() {
	return getAppTempPath();
    }

    /**
     * Gets the last attendance taken file.
     *
     * @return the last attendance taken file
     */
    public String getLastAttendanceTakenFile() {
	return getAppTempPath() + ATTENDANCE_TAKEN_DATE;
    }

    /**
     * Gets the cal events path.
     *
     * @return the cal events path
     */
    public String getCalEventsPath() {
	return getAppTempPath();
    }

    /**
     * Gets the subject list path.
     *
     * @return the subject list path
     */
    public String getSubjectListPath() {
	return getCurrentUserFilesPath();
    }

    /**
     * Gets the subjects list file.
     *
     * @return the subjects list file
     */
    public String getSubjectsListFile() {
	return getSubjectListPath() + SUBJECTS_LIST_FILE;
    }

    /**
     * Write to file.
     *
     * @param text the text
     * @param filename the filename
     * @return true, if successful
     */
    public static boolean writeToFile(String text, String filename) {
	// Logger.error("Application data", "Writing data to file");
	File file = new File(filename);

	int end = filename.lastIndexOf("/");
	String folder = "";

	if (end > 0) {
	    folder = filename.substring(0, end);

	    File parentFolder = new File(folder);
	    createDir(parentFolder);
	}

	/*
	 * Logger.error("App Data", "Path Seperator:" + File.pathSeparatorChar +
	 * " Folder:" + folder + " at position:" + end + "  File");
	 */

	Writer output = null;
	try {
	    output = new BufferedWriter(new FileWriter(file));
	    output.write(text);
	    output.close();
	} catch (Exception e) {
	    Logger.error("Application Data", e);

	    return false;
	}

	return true;
    }

    /**
     * Write to file.
     *
     * @param content the content
     * @param filename the filename
     * @return true, if successful
     */
    public static boolean writeToFile(byte[] content, String filename) {
	File newFile = new File(filename);

	int end = filename.lastIndexOf("/");
	String folder = "";

	if (end > 0) {
	    folder = filename.substring(0, end);
	    File parentFolder = new File(folder);
	    createDir(parentFolder);
	}

	FileOutputStream fos = null;
	try {
	    fos = new FileOutputStream(newFile);
	    fos.write(content);
	    fos.close();
	} catch (Exception e) {
	    Logger.error("App Data", e);
	    return false;
	}

	return true;
    }

    /**
     * Checks if is file exists.
     *
     * @param filename the filename
     * @return true, if is file exists
     */
    public static boolean isFileExists(String filename) {
	File file = new File(filename);
	if (file.exists()) {
	    return true;
	}

	return false;
    }

    /**
     * Creates the dir.
     *
     * @param dir the dir
     */
    private static void createDir(File dir) {
	if (dir.exists()) {
	    return;
	}
	Log.v("EbookReader", "Creating dir " + dir.getName());
	if (!dir.mkdirs()) {
	    throw new RuntimeException("Can not create dir " + dir);
	}
    }

    /**
     * Delete folder or file recursively.
     *
     * @param fileOrDirectory the file or directory
     */
    public static void deleteFolderOrFileRecursively(File fileOrDirectory) {
	if (fileOrDirectory.isDirectory())
	    for (File child : fileOrDirectory.listFiles())
		deleteFolderOrFileRecursively(child);

	// TODO FIX : this also leads to not deleteing some un expected files
	// that contain 'coverpage' string in its name
	if (!fileOrDirectory.getName().contains("coverpage")) {
	    Log.w("Deleting file/folder", fileOrDirectory.getName());

	    fileOrDirectory.delete();
	} else {
	    Log.w("Skipping Deletion of file/folder", fileOrDirectory.getName());
	}
    }

    /*
     * File Paths Helper methods -- END --
     */

    /*
     * Date Helper methods --START--
     */
    /**
     * Gets the date.
     *
     * @return the date
     */
    public String getDate() {
	DateFormat dateFormat = new SimpleDateFormat();
	return dateFormat.format(new Date());

    }

    /**
     * Gets the date.
     *
     * @param date the date
     * @return the date
     */
    public String getDate(String date) {
	DateFormat dateFormat = new SimpleDateFormat(date);
	return dateFormat.format(new Date());
    }

    /*
     * Date Helper methods --END--
     */

    /**
     * Setting the Cover Page Image.
     *
     * @return the current book cover image path
     */
    public String getCurrentBookCoverImagePath() {
	String coverpageUrl;

	try {
	    coverpageUrl = book.getCoverpage().getWebPath().trim();
	} catch (Exception e) {
	    coverpageUrl = "";
	}

	int pos = coverpageUrl.lastIndexOf(".");
	String extention;
	if (pos == -1) {
	    extention = ".png";
	} else {
	    extention = coverpageUrl.substring(pos, coverpageUrl.length());
	}
	return this.getBookFilesPath(book.getBookId()) + COVER_IMAGE_NAME
		+ extention;
    }

    /**
     * Gets the exception stack trace.
     *
     * @param e the e
     * @return the exception stack trace
     */
    public static String getExceptionStackTrace(Exception e) {
	StringWriter sw = new StringWriter();
	e.printStackTrace(new PrintWriter(sw));
	return sw.toString();
    }

    /* (non-Javadoc)
     * @see android.app.Application#onTerminate()
     */
    @Override
    public void onTerminate() {
	networkConnectivity.stopNetworkDemon();
    }

    /**
     * Sets the open book exam book list.
     *
     * @param booksList the new open book exam book list
     */
    public void setOpenBookExamBookList(List<String> booksList) {
	this.openExambooksList = booksList;
    }

    /**
     * Gets the open book exam book list.
     *
     * @return the open book exam book list
     */
    public List<String> getOpenBookExamBookList() {
	return openExambooksList;
    }

    /**
     * Gets the question num.
     *
     * @return the question num
     */
    public int getQuestionNum() {
	return questionNum;
    }

    /**
     * Sets the question num.
     *
     * @param questionNum the new question num
     */
    public void setQuestionNum(int questionNum) {
	this.questionNum = questionNum;
    }

    /**
     * Gets the current exam question no.
     *
     * @return the currentExamQuestionNo
     */
    public int getCurrentExamQuestionNo() {
	return currentExamQuestionNo;
    }

    /**
     * Sets the current exam question no.
     *
     * @param currentExamQuestionNo the currentExamQuestionNo to set
     */
    public void setCurrentExamQuestionNo(int currentExamQuestionNo) {
	this.currentExamQuestionNo = currentExamQuestionNo;
    }

    /**
     * Read file.
     *
     * @param filePath the file path
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String readFile(String filePath) throws IOException {
	File file = new File(filePath);
	StringBuffer contents = new StringBuffer();
	BufferedReader reader = null;

	reader = new BufferedReader(new FileReader(file), 9048);
	String text = null;

	// repeat until all lines are read
	while ((text = reader.readLine()) != null) {
	    contents.append(text);
	}

	if (reader != null) {
	    reader.close();
	}
	// show file contents here
	return contents.toString();
    }

    /**
     * Read file.
     *
     * @param file the file
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static String readFile(File file) throws IOException {
	StringBuffer contents = new StringBuffer();
	BufferedReader reader = null;

	reader = new BufferedReader(new FileReader(file), 7956);
	String text = null;

	// repeat until all lines are read
	while ((text = reader.readLine()) != null) {
	    contents.append(text);// .append(System.getProperty("line.separator"));
	}

	if (reader != null) {
	    reader.close();
	}
	// show file contents here
	return contents.toString();
    }

    /**
     * Gets the activity name.
     *
     * @return the activity name
     */
    public String getActivityName() {
	return activityName;
    }

    /**
     * Sets the activity name.
     *
     * @param activityName the new activity name
     */
    public void setActivityName(String activityName) {
	this.activityName = activityName;
    }

    /**
     * Gets the auth id.
     *
     * @return the auth id
     */
    public String getAuthId() {
	return null;
    }

    /**
     * Checks if is login status.
     *
     * @return true, if is login status
     */
    public boolean isLoginStatus() {
	return loginStatus;
    }

    /**
     * Sets the login status.
     *
     * @param loginStatus the new login status
     */
    public void setLoginStatus(boolean loginStatus) {
	this.loginStatus = loginStatus;
    }

    /**
     * Gets the exam id.
     *
     * @return the exam id
     */
    public String getExamId() {
	return currentExam.getId();
    }

    /**
     * Sets the exam id.
     *
     * @param examId the new exam id
     */
    public void setExamId(String examId) {
	this.currentExam.setId(examId);
    }

    /* (non-Javadoc)
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate() {
	/*ACRA.init(this);
	MailSender mail = new MailSender();
	ACRA.getErrorReporter().setReportSender(mail);*/
	super.onCreate();
    }

    /**
     * Sets the device id.
     *
     * @param deviceId the new device id
     */
    public void setDeviceId(String deviceId) {
	this.deviceId = deviceId;
    }

    /**
     * Gets the device id.
     *
     * @return the device id
     */
    public String getDeviceId() {
	return deviceId;

    }
    
    /**
     * Gets the test category file name.
     *
     * @return the test category file name
     */
    public String getTestCategoryFileName(){
    	return getCurrentUserFilesPath() + TEST_CATEGORY_FILE;
    }

    /**
     * Gets the academic year details file name.
     *
     * @return the academic year details file name
     */
    public String getAcademicYearDetailsFileName(){
    	return getCurrentUserFilesPath()+ ACADEMIC_YEAR_FILE;
    }
}