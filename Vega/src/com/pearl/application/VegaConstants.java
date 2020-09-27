package com.pearl.application;

public interface VegaConstants {

	// DATABASE CONSTANTS

	public static final String ALERT_DISPLAY_TIME = "'ALERT_DISPLAY_TIME'";
	public static final String HISTORY_ACTIVITY = "'HISTORY_ACTIVITY'";
	public static final String NOTES_AUTO_SAVE_TIME = "'NOTES_AUTO_SAVE_TIME'";
	public static final String DATE_FORMAT = "'DATE_FORMAT'";
	public static final String HISTORY_LOGGED_IN_USER_ID = "'HISTORY_LOGGED_IN_USER_ID'";
	public static final String HISTORY_BOOK_ID = "'HISTORY_BOOK_ID'";
	public static final String HISTORY_PAGE_NUMBER = "'HISTORY_PAGE_NUMBER'";
	public static final String HISTORY_BOOK_TYPE = "'HISTORY_BOOK_TYPE'";
	public static final String HISTORY_SUBJECT = "'HISTORY_SUBJECT'";
	public static final String HISTORY_NOTEBOOK_ID = "'HISTORY_NOTEBOOK_ID'";
	public static final String SERVER_BASE_PATH = "'SERVER_BASE_PATH'";
	public static final String TEST_SERVER_PATH = "'TEST_SERVER_PATH'";
	public static final String HISTORY_EXAM_ID = "'HISTORY_EXAM_ID'";
	public static final String HISTORY_QUESTION_NUM = "'HISTORY_QUESTION_NUM'";
	public static final String LAST_ATTENDANCE_DATE = "'LAST_ATTENDANCE_DATE'";
	public static final String PERIOD_DURATION = "'PERIOD_DURATION'";
	public static final String CAN_TAKE_ATTENDANCE = "'CAN_TAKE_ATTENDANCE'";
	public static final String EXAM_SERVER_IP = "'EXAM_SERVER_IP'";
	public static final String HISTORY_QUESTIONS_ACTIVITY = "'HISTORY_QUESTIONS_ACTIVITY'";
	public static final String DEVICE_ID = "'DEVICE_ID'";
	public static final String TEXT_SIZE = "'TEXT_SIZE'";
	public static final String NOTIFICATION_TIME = "'NOTIFICATION_TIME'";
	public static final String EXAM_COUNT = "'EXAM_COUNT'";
	public static final String BOOKS_COUNT = "'BOOKS_COUNT'";
	public static final String NOTICE_COUNT = "'NOTICE_COUNT'";
	public static final String MESSAGE_COUNT = "'MESSAGE_COUNT'";
	public static final String ATTENDANCE_COUNT = "'ATTENDANCE_COUNT'";
	public static final String SUBJECT_COUNT = "'SUBJECT_COUNT'";
	public static final String ADDITIONAL_INFO_COUNT = "'ADDITIONAL_INFO_COUNT'";
	public static final String DRM_ALLOWED = "'DRM_ALLOWED'";
	public static final String THEME = "'THEME'";
	public static final String HISTORY_DEVICEID = "'HISTORY_DEVICEID'";
	public static final String BOOK_SELECTED_POSITION_ONLINE = "'BOOK_SELECTED_POSITION_ONLINE'";
	public static final String BOOK_SELECTED_POSITION_SHELF = "'BOOK_SELECTED_POSITION_SHELF'";
	public static final String TEST_APPROVE_COUNT = "'TEST_APPROVE_COUNT'";
	public static final String TEST_REJECTED_COUNT = "'TEST_REJECTED_COUNT'";
	public static final String TEST_TO_BE_EVALUATED = "'TEST_TO_BE_EVALUATED'";
	public static final String TEST_PUBLISHED_COUNT = "'TEST_PUBLISHED_COUNT'";
	public static final String TEST_RESULTS_PUBLISH_COUNT = "'TEST_RESULTS_PUBLISH_COUNT'";
	public static final String TEST_RESULTS_TO_BE_PUBLISH_COUNT = "'TEST_RESULTS_TO_BE_PUBLISH_COUNT'";
	public static final String TEST_EVALUATED_COUNT = "'TEST_EVALUATED_COUNT'";
	public static final String SHELF_LIST_SELECTED_HISTORY = "'SHELF_LIST_SELECTED_HISTORY'";

	// Intent Constants
	// Quizzard
	public static final String EXAM_ID = "exam_id";
	public static final String QUESTION_NUM = "question_no";
	public static final String OPEN_BOOKS_LIST = "open_book_list";

	// Login
	public static final String LOGIN_UNAUTHORIZED = "Authentication failed.";

	public static final String BOOK_ID = "bookId";
	public static final String PAGE_NUM = "pageNo";
	public static final String SUBJECT = "Subject";
	public static final String USER_ID = "user_id";
	public static final String DATE = "Date";

	// Chat
	public static final String CHAT_LIKED = "";

	// Dashboard constants
	public static final String DASHBOARD_NOTICE = "'DASHBOARD_NOTICE'";
	public static final String DASHBOARD_ATTENDANCE = "'DASHBOARD_ATTENDANCE'";
	public static final String DASHBOARD_CHAT = "'DASHBOARD_CHAT'";
	public static final String DASHBOARD_NOOTBOOK = "'DASHBOARD_NOTEBOOK'";
	public static final String DASHBOARD_QUIZZARD = "'DASHBOARD_QUIZZARD'";
	public static final String DASHBOARD_EREADER = "'DASHBOARD_EREADER'";
	public static final String DASHBOARD_FEEDBACK = "'DASHBOARD_FEEDBACK'";
	public static final String DASHBOARD_FAQ = "'DASHBOARD_FAQ'";
	public static final String ANALYTICS = "'ANALYTICS'";

	public static final String BOOK_ERROR = "book_error";
	
	
	//Teacher - Quizzard
	public static final String TEST_ID = "test_id";

	//Attendance
	public static final String ATTENDANCE_TYPE = "attendance_type";
	public static final String ATTENDANCE_SUBJECT = "subject";
	
	//Analytics
	public static final String CHART_TYPE = "chart_type";
	public static final String COMPARATIVE_STUDY_ACCROSS_TESTS = "Performance analysis of students in a section";
	public static final String COMPARATIVE_STUDY_ACCROSS_SUBJECTS = "Comparative study across subjects";
	public static final String COMPARATIVE_STUDY_ACCROSS_SECTIONS = "Comparison across sections for a subject and exam type";
	public static final String PASS_FAIL_PERCENTAGE_ACROSS_SECTIONS = "Pass/Fail percentage across sections";
	public static final String PASS_FAIL_PERCENTAGE_ACROSS_SECTIONS_FOR_A_SUBJECT = "Pass/Fail percentage across sections for a subject";
	public static final String PASS_FAIL_PERCENTAGE_ACROSS_GRADES = "Pass/Fail percentage across grades";
	public static final String PERFORMANCE_OF_A_STUDENT_IN_ALL_SUBJECTS_FOR_EXAM_TYPE = "Performance of a student in all the subjects for an exam type";
	public static final String YEAR_WISE_COMPARISION = "Year wise comparison";
	public static final String AVERAGE_PERCENTAGE_OF_PASS_MARKS_FOR_A_SECTION = "Average percentage of pass marks for a section";
	public static final String SECTION_WISE_PERFORMANCE_FOR_A_EXAM_TYPE = "section wise performance for an particular exam type";
	public static final String COUNT_OF_STUDENTS_IN_A_GRADE_RANGE = "Count of students in a particular grade range";
	public static final String PERFORMANCE_OF_STUDENT_IN_ALL_EXAM_TYPES = "Performance of a student in all the exam types";
	public static final String PERFORMANCE_COMAPRISION_AMONG_SECTIONS_FOR_SUBJECT_IN_ALL_EXAM_TYPES = "Comparison across sections for a subject in all exam types";
	public static final String PASS_FAIL_PERCENTAGE_IN_SECTION = "Pass/Fail percentage in a section";
	public static final String PERFORMANCE_COMPARISON_AMONG_SECTIONS_FOR_ALL_EXAM_TYPES_SUBJECTS = "Comparison across sections for a subject in all exam types";
	
	public static final String GRADE_SUBJECTS_LIST = "grade subjetcs list";
	public static final String SECTIONS_LIST = "sections list";
	public static final String SUBJECT_LIST = "subject list";
	public static final String STUDENTS_LIST = "students list";
	public static final String TESTS_LIST = "tests list";
	
}
