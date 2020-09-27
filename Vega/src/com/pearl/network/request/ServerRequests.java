package com.pearl.network.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;

import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerRequests.
 */
public class ServerRequests {

    // Server url's
    /** The webserver basepath. */
    public static String WEBSERVER_BASEPATH = "";// will be updated with
    // configured value
    /** The webserver php basepath. */
    public static String WEBSERVER_PHP_BASEPATH = "";// will be updated with
    // configured value
    /** The webserver exam basepath. */
    public static String WEBSERVER_EXAM_BASEPATH = "";// will be updated with
    // configured value

    // Login
    /** The Constant LOGIN_AUTHENTATION. */
    public static final String LOGIN_AUTHENTATION = "LOGIN_AUTHENTICATION";

    // Notification
    /** The Constant NOTICEBOARD. */
    public static final String NOTICEBOARD = "NOTICEBOARD";
    
    /** The Constant TEACHER_NOTICEBOARD_DATA. */
    public static final String TEACHER_NOTICEBOARD_DATA = "TEACHER_NOTICEBOARD_DATA";
    
    /** The Constant TEACHER_NOTICE_SUBMIT. */
    public static final String TEACHER_NOTICE_SUBMIT = "TEACHER_NOTICE_SUBMIT";
    
    /** The Constant TEACHER_MY_NOTICES. */
    public static final String TEACHER_MY_NOTICES= "TEACHER_MY_NOTICES";

    // Version
    /** The Constant DOWNLOAD_LATEST_VERSION. */
    public static final String DOWNLOAD_LATEST_VERSION = "DOWNLOAD_LATEST_VERSION";

    // Book Download Url's
    /** The Constant AVAILABLEBOOKSLIST. */
    public static final String AVAILABLEBOOKSLIST = "AVAILABLEBOOKSLIST";
    
    /** The Constant DOWNLOADED_BOOKSLIST. */
    public static final String DOWNLOADED_BOOKSLIST = "DOWNLOADED_BOOKSLIST";
    
    /** The Constant DOWNLOAD_BOOK. */
    public static final String DOWNLOAD_BOOK = "DOWNLOAD_BOOK";
    
    /** The Constant DOWNLOAD_SUCCESS. */
    public static final String DOWNLOAD_SUCCESS = "DOWNLOAD_SUCCESS";
    
    /** The Constant DELETE_BOOK. */
    public static final String DELETE_BOOK = "DELETE_BOOK";
    
    /** The Constant COVERIMAGE_PATH. */
    public static final String COVERIMAGE_PATH = "COVERIMAGE_PATH";

    // Announcement Url
    /** The Constant ANOUNCEMENT. */
    public static final String ANOUNCEMENT = "ANOUNCEMENT";

    // Test url
    /** The Constant QUIZ_HOME_STUDENT. */
    public static final String QUIZ_HOME_STUDENT = "QUIZ_HOME_STUDENT";

    /** The Constant QUIZ_HOME_TEACHER. */
    public static final String QUIZ_HOME_TEACHER = "QUIZ_HOME_TEACHER";
    
    /** The Constant QUIZ_RESULT. */
    public static final String QUIZ_RESULT = "QUIZ_RESULT";

    // Vedio
    /** The Constant VIDEO_URL. */
    public static final String VIDEO_URL = "VIDEO_URL";
    
    /** The Constant ADDITIONAL_INFO. */
    public static final String ADDITIONAL_INFO = "ADDITIONAL_INFO";

    // Chat url's
    /** The Constant CHAT_WALL. */
    public static final String CHAT_WALL = "CHAT_WALL";
    
    /** The Constant CHAT_LIKE. */
    public static final String CHAT_LIKE = "LIKE";
    
    /** The Constant CHAT_MESSAGE_COUNT. */
    public static final String CHAT_MESSAGE_COUNT = "COUNT";
    
    /** The Constant CHAT_COMMENT. */
    public static final String CHAT_COMMENT = "COMMENT";
    
    /** The Constant CHAT_POST. */
    public static final String CHAT_POST = "POST";

    // Calendar url
    /** The Constant CALENDAR_EVENTS. */
    public static final String CALENDAR_EVENTS = "CALENDAR_EVENTS";

    // Attendance url's
    /** The Constant ATTENDANCE. */
    public static final String ATTENDANCE = "ATTENDANCE";
    
    /** The Constant ATTENDANCE_SUBMIT. */
    public static final String ATTENDANCE_SUBMIT = "ATTENDANCE_SUBMIT";
    
    /** The Constant LAST_ATTENDANCE_TAKEN_TIME. */
    public static final String LAST_ATTENDANCE_TAKEN_TIME = "LAST_ATTENDANCE_TAKEN_TIME";
    
    /** The Constant ATTENDANCE_PERIOD_CONFIG. */
    public static final String ATTENDANCE_PERIOD_CONFIG = "ATTENDANCE_PERIOD_CONFIG";

    // Test url's
    /** The Constant EXAMS_LIST. */
    public static final String EXAMS_LIST = "EXAMS_LIST";
    
    /** The Constant QUESTIONS_LIST. */
    public static final String QUESTIONS_LIST = "QUESTIONS_LIST";
    
    /** The Constant EXAM_SUBMIT. */
    public static final String EXAM_SUBMIT = "EXAM_SUBMIT";
    
    /** The Constant RESULTS_ANSWERSHEET. */
    public static final String RESULTS_ANSWERSHEET = "RESULTS_ANSWERSHEET";
    
    /** The Constant EXAMS_LIST_TEACHER. */
    public static final String EXAMS_LIST_TEACHER = "EXAMS_LIST_TEACHER";
    
    /** The Constant TEACHER_EXAMS_TO_BE_APPROVED. */
    public static final String TEACHER_EXAMS_TO_BE_APPROVED = "TEACHER_EXAMS_TO_BE_APPROVED";
    
    /** The Constant TEACHER_APPROVED_EXAMS. */
    public static final String TEACHER_APPROVED_EXAMS = "TEACHER_APPROVED_EXAMS";
    
    /** The Constant TEACHER_REJECTED_EXAMS. */
    public static final String TEACHER_REJECTED_EXAMS = "TEACHER_REJECTED_EXAMS";
    
    /** The Constant TEACHER_PUBLISH_EXAM_LIST. */
    public static final String TEACHER_PUBLISH_EXAM_LIST = "TEACHER_PUBLISH_EXAM_LIST";
    
    /** The Constant TEACHER_PUBLISH_SUBMIT. */
    public static final String TEACHER_PUBLISH_SUBMIT = "TEACHER_PUBLISH_SUBMIT";
    
    /** The Constant TEACHER_AWAITING_APPROVAL_EXAM_LIST. */
    public static final String TEACHER_AWAITING_APPROVAL_EXAM_LIST = "TEACHER_AWAITING_APPROVAL_EXAM_LIST";
    
    /** The Constant TEACHER_AWAITING_RESULTS_PUBLISH_LIST. */
    public static final String TEACHER_AWAITING_RESULTS_PUBLISH_LIST = "TEACHER_AWAITING_RESULTS_PUBLISH_LIST";
    
    /** The Constant TEACHER_RESULTS_PUBLISH. */
    public static final String TEACHER_RESULTS_PUBLISH = "TEACHER_RESULTS_PUBLISH";
    
    /** The Constant RESULTS_PUBLISH_EXAMS_LIST. */
    public static final String RESULTS_PUBLISH_EXAMS_LIST = "RESULTS_PUBLISH_EXAMS_LIST";
    
    /** The Constant RESULTS_PUBLISH_STUDENTS_LIST. */
    public static final String RESULTS_PUBLISH_STUDENTS_LIST = "RESULTS_PUBLISH_STUDENTS_LIST";
    
    /** The Constant RESULTS_PUBLISH_ANSWERSHEET. */
    public static final String RESULTS_PUBLISH_ANSWERSHEET = "RESULTS_PUBLISH_ANSWERSHEET";
    
    /** The Constant REJECTED_TESTS_LIST. */
    public static final String REJECTED_TESTS_LIST = "REJECTED_TESTS_LIST";
    
    /** The Constant REJECTED_TESTS_QUESTIONPAPER. */
    public static final String REJECTED_TESTS_QUESTIONPAPER = "REJECTED_TESTS_QUESTIONPAPER";
    
    /** The Constant REJECTED_TEST_COMMENTS. */
    public static final String REJECTED_TEST_COMMENTS = "REJECTED_TEST_COMMENTS";
    
    /** The Constant TEACHER_GRADE_AND_SUBJECT_LIST. */
    public static final String TEACHER_GRADE_AND_SUBJECT_LIST = "TEACHER_GRADE_AND_SUBJECT_LIST";
    
    /** The Constant TEACHER_SECTION_LIST. */
    public static final String TEACHER_SECTION_LIST = "TEACHER_SECTION_LIST";
    
    /** The Constant TEACHER_STUDENT_LIST. */
    public static final String TEACHER_STUDENT_LIST = "TEACHER_STUDENT_LIST";
    
    /** The Constant TEACHER_FINAL_QUESTIONPAPER. */
    public static final String TEACHER_FINAL_QUESTIONPAPER = "TEACHER_FINAL_QUESTIONPAPER";
    
    /** The Constant AWAITING_EXAM_EVALUATION_LIST. */
    public static final String AWAITING_EXAM_EVALUATION_LIST = "AWAITING_EXAM_EVALUATION_LIST";
    
    /** The Constant EXAM_EVALUATION. */
    public static final String EXAM_EVALUATION = "EXAM_EVALUATION";
    
    /** The Constant EXAM_EVALUATION_FOR_STUDENT. */
    public static final String EXAM_EVALUATION_FOR_STUDENT = "EXAM_EVALUATION_FOR_STUDENT";
    
    /** The Constant EXAM_EVALUATION_SUBMIT. */
    public static final String EXAM_EVALUATION_SUBMIT = "EXAM_EVALUATION_SUBMIT";
    
    /** The Constant TEST_CATEGORY. */
    public static final String TEST_CATEGORY = "TEST_CATEGORY";
    
    /** The Constant GRADE_YEARS. */
    public static final String GRADE_YEARS = "GRADE_YEARS";
    
    /** The Constant ACADEMIC_YEAR. */
    public static final String ACADEMIC_YEAR = "ACADEMIC_YEAR";
    
    /** The Constant FETCH_FROM_QUESTION_BANK. */
    public static final String FETCH_FROM_QUESTION_BANK = "FETCH_FROM_QUESTION_BANK";
    // Question Type

    /** The Constant QUESTION_TYPE. */
    public static final String QUESTION_TYPE = "QUESTION_TYPE";

    // Subjects list
    /** The Constant SUBJECTS_LIST. */
    public static final String SUBJECTS_LIST = "String SUBJECTS_LIST";

    // Feedback Submit url

    /** The Constant FEEDBACK_SUBMIT. */
    public static final String FEEDBACK_SUBMIT = "FEEDBACK_SUBMIT";

    // service
    /** The Constant SERVICE. */
    public static final String SERVICE = "SERVICE";
    
    //Analytics
    /** The Constant GRADE_LIST. */
    public static final String GRADE_LIST = "GRADE_LIST";
    
    /** The Constant SECTION_LIST_FOR_GRADE. */
    public static final String SECTION_LIST_FOR_GRADE = "SECTION_LIST";
    
    /** The Constant SUBJECTS_FOR_GRADE_SECTION. */
    public static final String SUBJECTS_FOR_GRADE_SECTION = "SUBJECTS_LIST";
    
    /** The Constant ANALYTICS_ACROSS_SUBJETCS. */
    public static final String ANALYTICS_ACROSS_SUBJETCS = "STUDENT_MARKS";
    
    /** The Constant STUDENT_LIST. */
    public static final String STUDENT_LIST = "STUDENT_LIST";
    
    /** The Constant SUBJECT_LIST_FOR_A_STUDENT. */
    public static final String SUBJECT_LIST_FOR_A_STUDENT = "SUBJECT_LIST_FOR_A_STUDENT";
    
    /** The Constant ANALYTICS_TESTS_FOR_GRADE_SECTION. */
    public static final String ANALYTICS_TESTS_FOR_GRADE_SECTION = "GRADE_SECTION_TESTS";
    
    /** The Constant ANALYTICS_ACROSS_TESTS. */
    public static final String ANALYTICS_ACROSS_TESTS = "ANALYTICS_ACROSS_TESTS";
    
    /** The Constant COMMON_TEST_FOR_DIFFERENT_SECTIONS. */
    public static final String COMMON_TEST_FOR_DIFFERENT_SECTIONS = "COMMON_TEST_FOR_DIFFERENT_SECTIONS";
    
    /** The Constant ANALYTICS_ACROSS_SECTIONS. */
    public static final String ANALYTICS_ACROSS_SECTIONS = "ANALYTICS_ACROSS_SECTIONS";
    
    /** The Constant EXAM_TYPES_FOR_GRADES. */
    public static final String EXAM_TYPES_FOR_GRADES = "EXAM_TYPES";
    
    /** The Constant SUBJECT_FOR_GRADE. */
    public static final String SUBJECT_FOR_GRADE = "SUBJECT_FOR_GRADE";
    
    /** The Constant PASS_FAIL_PERCENT_ACROSS_SECTIONS. */
    public static final String PASS_FAIL_PERCENT_ACROSS_SECTIONS = "PASS_FAIL_PERCENT_ACROSS_SECTIONS";//1
	
	/** The Constant PASS_FAIL_PERCENT_ACROSS_SECTIONS_FOR_SUBJECT. */
	public static final String PASS_FAIL_PERCENT_ACROSS_SECTIONS_FOR_SUBJECT = "PASS_FAIL_PERCENT_ACROSS_SECTIONS_FOR_SUBJECT";//2
	
	/** The Constant PASS_FAIL_PERCENT_ACROSS_GRADES. */
	public static final String PASS_FAIL_PERCENT_ACROSS_GRADES = "PASS_FAIL_PERCENT_ACROSS_GRADES";//3
	
	/** The Constant STUDENT_PERFORMANCE_IN_ALL_SUBJECTS. */
	public static final String STUDENT_PERFORMANCE_IN_ALL_SUBJECTS = "STUDENT_PERFORMANCE_IN_ALL_SUBJECTS";//4
	
	/** The Constant STUDENT_COUNT_FOR_A_RANGE_IN_A_GRADE. */
	public static final String STUDENT_COUNT_FOR_A_RANGE_IN_A_GRADE = "STUDENT_COUNT_FOR_A_RANGE_IN_A_GRADE";//5
	
	/** The Constant PASS_FAIL_PERCENTAGE_FOR_SECTION. */
	public static final String PASS_FAIL_PERCENTAGE_FOR_SECTION = "PASS_FAIL_PERCENTAGE_FOR_SECTION";//6
	
	/** The Constant PERCENTAGE_RANGE. */
	public static final String PERCENTAGE_RANGE = "PERCENTAGE_RANGE";
	
	/** The Constant PERFORMANCE_OF_STUDENT_IN_ALL_EXAM_TYPES. */
	public static final String PERFORMANCE_OF_STUDENT_IN_ALL_EXAM_TYPES = "PERFORMANCE_OF_STUDENT_IN_ALL_EXAM_TYPES";//7
	
	/** The Constant PERFORMANCE_OF_TWO_SECTIONS_FOR_ALL_EXAM_TYPES_SUBJECTS. */
	public static final String PERFORMANCE_OF_TWO_SECTIONS_FOR_ALL_EXAM_TYPES_SUBJECTS = "PERFORMANCE_OF_TWO_SECTIONS_FOR_ALL_EXAM_TYPES_SUBJECTS";//8
	
	/** The Constant YEAR_WISE_COMPARISION. */
	public static final String YEAR_WISE_COMPARISION = "YEAR_WISE_COMPARISION";
	
	//SUBJECT_LIST_GRADE_TEACHER
	public static final String SUBJECT_LIST_GRADE_TEACHER = "SUBJECT_LIST_GRADE_TEACHER";
    /** The vega config. */
    private final VegaConfiguration vegaConfig;

    /**
     * Instantiates a new server requests.
     *
     * @param context the context
     */
    public ServerRequests(Context context) {
	vegaConfig = new VegaConfiguration(context);

	try {
	    WEBSERVER_BASEPATH = vegaConfig
		    .getValue(VegaConstants.SERVER_BASE_PATH);

	    Logger.verbose("ServerRequests", "basepath is:"
		    + WEBSERVER_BASEPATH);

	    this.close();
	} catch (final InvalidConfigAttributeException e) {
	    Logger.warn("ServerRequests", e.toString());
	}
    }

    /**
     * Gets the url.
     *
     * @param type the type
     * @return the url
     */
    public String getURL(String type) {

	if (LOGIN_AUTHENTATION.equals(type)) {
	    return WEBSERVER_BASEPATH
		    + "/rest/login.html?userName=$$&password=$$&mac=$$";
	} else if (NOTICEBOARD.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/noticeboard/$$";
	} else if (TEACHER_NOTICEBOARD_DATA.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/radiotypes/";
	} else if (TEACHER_NOTICE_SUBMIT.equalsIgnoreCase(type)) {
	    return WEBSERVER_BASEPATH + "/rest/noticeSubmission";
	} else if(TEACHER_MY_NOTICES.endsWith(type)) {
	    return WEBSERVER_BASEPATH + "/rest/userNoticeAndSendNotice";
	}else if (DOWNLOAD_LATEST_VERSION.equals(type)) {
	    return WEBSERVER_PHP_BASEPATH + "/apk_upgrades/pearl.apk";
	} else if (AVAILABLEBOOKSLIST.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/student/availablebooks/$$";
	    // TODO Phani
	    // return
	    // WEBSERVER_BASEPATH+"/pearl/rest/student/availablebooks/$$?auth=$$";
	} else if (DOWNLOADED_BOOKSLIST.equals(type)) {
	    // TODO Phani
	    // return
	    // WEBSERVER_BASEPATH+"/pearl/rest/student/downloadedbooks/$$?auth=$$";
	    return WEBSERVER_BASEPATH + "/rest/student/downloadedbooks/$$";
	} else if (DOWNLOAD_BOOK.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/student/bookdownload/$$/$$/$$";
	} else if (DOWNLOAD_SUCCESS.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/student/savebook/$$/$$";
	} else if (DELETE_BOOK.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/student/deletebook/$$/$$";
	} else if (ANOUNCEMENT.equals(type)) {
	    return WEBSERVER_PHP_BASEPATH
		    + "/Tablet/code/notification.php?announcement=$$";
	} else if (QUIZ_HOME_STUDENT.equals(type)) {
	    return WEBSERVER_PHP_BASEPATH
		    + "/public/code/tablet_latest_index.php?userid=$$";
	} else if (QUIZ_RESULT.equals(type)) {
	    return WEBSERVER_PHP_BASEPATH
		    + "/public/code/tablet_student_results_tests.php?studentid=$$";
	} else if (VIDEO_URL.equals(type)) {
	    return WEBSERVER_BASEPATH + "/Vega/VideoGallery/Video8.mp4";
	} else if (EXAMS_LIST.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/automatedtest/examlist/$$";
	} else if (EXAMS_LIST_TEACHER.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/teacher/myapprovallist/";
	} else if (TEACHER_AWAITING_APPROVAL_EXAM_LIST.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/teacher/awaitingapprovallist/";
	} else if (TEACHER_EXAMS_TO_BE_APPROVED.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/teacher/showquestionpaper/";
	} else if (TEACHER_AWAITING_RESULTS_PUBLISH_LIST.equals(type)) {
	    return WEBSERVER_BASEPATH
		    + "/rest/teacher/awaitingresultspublishlist/";
	} else if (RESULTS_PUBLISH_EXAMS_LIST.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/teacher/resultspublishlist/";
	} else if (RESULTS_PUBLISH_STUDENTS_LIST.equals(type)) {
	    return WEBSERVER_BASEPATH
		    + "/rest/teacher/resultspublishstudentslist/";
	} else if (RESULTS_PUBLISH_ANSWERSHEET.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/test/examresult/";
	} else if (REJECTED_TESTS_LIST.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/teacher/rejecttestlist/";
	} else if (REJECTED_TESTS_QUESTIONPAPER.equals(type)) {
	    return WEBSERVER_BASEPATH
		    + "/rest/teacher/showrejectedquestionpaper/";
	} else if (REJECTED_TEST_COMMENTS.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/teacher/showcomments/";
	} else if (TEACHER_GRADE_AND_SUBJECT_LIST.equalsIgnoreCase(type)) {
	    return WEBSERVER_BASEPATH + "/rest/test/teachergradeandsubjectlist";
	} else if (TEACHER_SECTION_LIST.equalsIgnoreCase(type)) {
	    return WEBSERVER_BASEPATH + "/rest/test/teachersectionlist";
	} else if (TEACHER_STUDENT_LIST.equalsIgnoreCase(type)) {
	    return WEBSERVER_BASEPATH + "/rest/test/teacherstudentlist";
	} else if (TEACHER_FINAL_QUESTIONPAPER.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/test/finalQuestionPaper";
	} else if (TEACHER_RESULTS_PUBLISH.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/teacher/awaitingresultspublish/";
	} else if (QUESTIONS_LIST.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/automatedtest/display/";
	} else if (EXAM_SUBMIT.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/automatedtest/submit";
	} else if (RESULTS_ANSWERSHEET.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/test/examresult/";
	} else if (TEACHER_APPROVED_EXAMS.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/teacher/approvetest/";
	} else if (TEACHER_REJECTED_EXAMS.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/teacher/rejecttest/";
	} else if (EXAM_EVALUATION.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/test/studentListTestPaper";
	} else if (EXAM_EVALUATION_FOR_STUDENT.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/test/studentExamPaper";
	} else if (TEACHER_PUBLISH_EXAM_LIST.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/teacher/publishlist/";
	} else if (TEACHER_PUBLISH_SUBMIT.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/teacher/submitpublishlist/";
	} else if (AWAITING_EXAM_EVALUATION_LIST.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/test/awaitingEvaluationList/";
	} else if (EXAM_EVALUATION_SUBMIT.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/test/evaluatedTest/submit";
	}else if(FETCH_FROM_QUESTION_BANK.equals(type)) {
	    return WEBSERVER_BASEPATH +"/rest/questionsList";
	}else if (CHAT_WALL.equals(type)) {
	    return WEBSERVER_BASEPATH + "/mobilemessage.html?userId=$$";
	} else if (CHAT_LIKE.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/message/like/$$/$$";
	} else if (CHAT_MESSAGE_COUNT.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/message/new/count/$$/$$";
	} else if (CHAT_COMMENT.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/message/comment/$$/$$/$$/$$";
	} else if (CHAT_POST.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/message/post/$$/$$/$$";
	} else if (CALENDAR_EVENTS.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/student/notifications/$$";
	} else if (QUIZ_HOME_TEACHER.equals(type)) {
	    return WEBSERVER_PHP_BASEPATH
		    + "/public/code/tablet_latest_index.php?userid=$$";
	} else if (ATTENDANCE.equals(type)) {
	    return WEBSERVER_BASEPATH
		    + "/rest/student/gradesectionstudents/$$/$$";
	} else if (ATTENDANCE_SUBMIT.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/attendance/submit";
	} else if (ADDITIONAL_INFO.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/additionalInfo/$$/$$/$$/$$";
	} else if (COVERIMAGE_PATH.equals(type)) {
	    return WEBSERVER_BASEPATH
		    + "/rest/student/converimagedownload/$$/$$";
	} else if (SUBJECTS_LIST.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/test/studentSubjects/$$";
	} else if (LAST_ATTENDANCE_TAKEN_TIME.equals(type)) {
	    return WEBSERVER_BASEPATH
		    + "/rest/attendance/lastattendancetakendate/$$/$$";
	} else if (ATTENDANCE_PERIOD_CONFIG.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/attendance/periodconfig/$$/$$";
	} else if (SERVICE.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/notification/$$";
	} else if (FEEDBACK_SUBMIT.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/feedback/";
	} else if (QUESTION_TYPE.equals(type)) {
	    return WEBSERVER_BASEPATH + "/rest/questiontype";
	}else if(ANALYTICS_ACROSS_SUBJETCS.equals(type)){
		return WEBSERVER_BASEPATH+"/rest/dashBoard/studentPointsForGradeSection/";
	}else if(ANALYTICS_TESTS_FOR_GRADE_SECTION.equals(type)){
		return WEBSERVER_BASEPATH +"/rest/dashBoard/gradeSectionTests/";
	}else if(ANALYTICS_ACROSS_TESTS.equals(type)){
		return WEBSERVER_BASEPATH +"/rest/dashBoard/AllPointsForGradeSectionTest/";
	} else if(GRADE_LIST.equals(type)){
		return WEBSERVER_BASEPATH + "/rest/dashBoard/grades/";
	}else if(SECTION_LIST_FOR_GRADE.equals(type)){
		return WEBSERVER_BASEPATH + "/rest/dashBoard/sectionsForGrade/";
	}else if(SUBJECTS_FOR_GRADE_SECTION.equals(type)){
		return WEBSERVER_BASEPATH + "/rest/dashBoard/subjectsForGradeSection/";
	}else if(COMMON_TEST_FOR_DIFFERENT_SECTIONS.equals(type)){
		return WEBSERVER_BASEPATH + "/rest/dashBoard/commonGradeSectionsTests/";
	} else if(ANALYTICS_ACROSS_SECTIONS.equals(type)){
		return WEBSERVER_BASEPATH +"/rest/dashBoard/AllPointsForGradeSectionTest/";
	}else if(TEST_CATEGORY.equals(type))
		return WEBSERVER_BASEPATH + "/rest/examtypeMaster";
	else if(EXAM_TYPES_FOR_GRADES.equals(type))
		return WEBSERVER_BASEPATH +"/rest/examTypes/";
	else if(PASS_FAIL_PERCENT_ACROSS_SECTIONS.equals(type))
		return WEBSERVER_BASEPATH +"/rest/dashBoard/getGradeSectionPassFailPercenetages";
	else if(PASS_FAIL_PERCENT_ACROSS_SECTIONS_FOR_SUBJECT.equals(type))
		return WEBSERVER_BASEPATH +"/rest/dashBoard/getSubjectPassFailPercenetages";
	else if(PASS_FAIL_PERCENT_ACROSS_GRADES.equals(type))
		return WEBSERVER_BASEPATH +"/rest/dashBoard/getStudentsPassFailPercenetages";
	else if(STUDENT_PERFORMANCE_IN_ALL_SUBJECTS.equals(type))
		return WEBSERVER_BASEPATH +"/rest/dashBoard/getStudentPercentage";
	else if(STUDENT_COUNT_FOR_A_RANGE_IN_A_GRADE.equals(type))
		return WEBSERVER_BASEPATH +"/rest/dashBoard/getStudentCountForPercentageRange";
	else if(SUBJECT_FOR_GRADE.equals(type))
		return WEBSERVER_BASEPATH +"/rest/subjectsForGrade";
	else if(STUDENT_LIST.equals(type))
		return WEBSERVER_BASEPATH+"/rest/studentListForGradeSection";
	else if(PASS_FAIL_PERCENTAGE_FOR_SECTION.equals(type))
		return WEBSERVER_BASEPATH+"/rest/dashBoard/getSectionPercentage";
	else if(PERCENTAGE_RANGE.equals(type))
		return WEBSERVER_BASEPATH+"/rest/getPercentageRanges";
	else if(PERFORMANCE_OF_STUDENT_IN_ALL_EXAM_TYPES.equals(type))
		return WEBSERVER_BASEPATH+"/rest/dashBoard/getStudentPercentageForExamTypes";
	else if(PERFORMANCE_OF_TWO_SECTIONS_FOR_ALL_EXAM_TYPES_SUBJECTS.equals(type))
		return WEBSERVER_BASEPATH+"/rest/dashBoard/comparisonOfPercentageForSections";
	else if(YEAR_WISE_COMPARISION.equals(type))
		return WEBSERVER_BASEPATH+"/rest/dashBoard/comparisonOfGradePercentages";
	else if(GRADE_YEARS.equals(type))
		return WEBSERVER_BASEPATH+"/rest/dashBoard/gradeYears";
	else if(ACADEMIC_YEAR.equals(type))
		return WEBSERVER_BASEPATH+"/rest/academicYear";
	else if(SUBJECT_LIST_FOR_A_STUDENT.equals(type))
		return WEBSERVER_BASEPATH+"/rest/dashBoard/subjectsAssingedForGradeSectionStudent";
	// write the else if condition (SUBJECT_LIST_GRADE_TEACHER)
	else if(SUBJECT_LIST_GRADE_TEACHER.equals(type))
		return WEBSERVER_BASEPATH+"/rest/subjectListForTeacherAndGrade";
	else {
	    Logger.info("ServerRequestTypes", "request type:" + type);

	    return QUIZ_HOME_STUDENT;
	}
    }

    /**
     * Gets the request url.
     *
     * @param type the type
     * @param params the params
     * @return the request url
     */
    public String getRequestURL(String type, String... params) {
	final int paramscount = params.length;
	String request = getURL(type);

	for (int i = 0; i < paramscount; i++) {
	    final int pos = request.indexOf("$$");
	    if (request.contains("$$")) {
		try {
		    request = request.substring(0, pos)
			    + URLEncoder.encode(params[i], "UTF-8")
			    + request.substring(pos + 2, request.length());
		} catch (final UnsupportedEncodingException e) {
		    request = request.substring(0, pos) + params[i]
			    + request.substring(pos + 2, request.length());
		}
	    }
	}

	// TODO Phani
	/*
	 * Replace last $$ with Auth token. If last $$ is not present, ignore
	 * it. It can be case of login / upgrade URL.
	 * 
	 * Read auth token from the ConfigHandler (this is not editable by
	 * student)
	 */

	return request.trim();
    }

    /**
     * Gets the request url.
     *
     * @param external the external
     * @param url the url
     * @param params the params
     * @return the request url
     */
    public String getRequestURL(boolean external, String url, String... params) {
	final int paramscount = params.length;

	for (int i = 0; i < paramscount; i++) {
	    final int pos = url.indexOf("$$");
	    if (url.contains("$$")) {
		try {
		    url = url.substring(0, pos)
			    + URLEncoder.encode(params[i], "UTF-8")
			    + url.substring(pos + 2, url.length());
		} catch (final UnsupportedEncodingException e) {
		    url = url.substring(0, pos) + params[i]
			    + url.substring(pos + 2, url.length());
		}
	    }
	}

	return url.trim();
    }

    /**
     * Close.
     */
    public void close() {
	// vegaConfig.close();
    }

}