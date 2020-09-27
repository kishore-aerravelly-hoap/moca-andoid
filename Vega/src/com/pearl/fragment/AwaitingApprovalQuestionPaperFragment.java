package com.pearl.fragment;

/**
 * @author spasnoor
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.activities.ActivitySwipeDetector;
import com.pearl.application.ApplicationData;
import com.pearl.book.guesturehandlers.GestureActionHandler;
import com.pearl.examination.Exam;
import com.pearl.examination.Question;
import com.pearl.examination.exceptions.QuestionDoesNotExistsException;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.ExamParser;
import com.pearl.ui.helpers.examination.TeacherExamUI;
import com.pearl.ui.models.BaseResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class AwaitingApprovalQuestionPaperFragment.
 */
public class AwaitingApprovalQuestionPaperFragment extends Fragment implements
GestureActionHandler {
    
    /** The _exam list to be approved. */
    private LinearLayout _examListToBeApproved,linearLayout;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The question. */
    private TextView  startDate, endDate, time, examName, section,
    marks, question;
    
  
   
    
    /** The Exam id. */
    private String ExamId;
    
    /** The exam. */
    private Exam exam;
    
    /** The server request. */
    private ServerRequests serverRequest;
    
    /** The question no. */
    private int questionNo = 0;
    
    /** The questions. */
    private Question questions;
    
    /** The total no of questions. */
    int totalNoOfQuestions = 0;
    
    /** The handler. */
    private Handler handler;
    
    /** The view. */
    private View view;
    
    /** The tag. */
    private final String tag = "AwaitingApprovalQuestionPaperFragment";
    ProgressDialog progressBar;

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	view = inflater.inflate(
		R.layout.teacher_awaitng_approval_question_paper, container,
		false);
	startDate = (TextView) view
		.findViewById(R.id.awaiting_myapproval_startDate);
	endDate = (TextView) view
		.findViewById(R.id.awaiting_myapproval_endDate);
	// time = (TextView) view.findViewById(R.id.awaiting_myapproval_time);
	examName = (TextView) view
		.findViewById(R.id.awaiting_myapproval_examName);
	section = (TextView) view
		.findViewById(R.id.awaiting_myapproval_section);
	marks = (TextView) view.findViewById(R.id.awaiting_myapproval_Marks);
	question = (TextView) view
		.findViewById(R.id.awaiting_myapproval_questions);
	_examListToBeApproved = (LinearLayout) view
		.findViewById(R.id.awaiting_myapproval_ExamListToBeApproved);
	linearLayout = (LinearLayout)view.findViewById(R.id.layout_awaiting_approval);
	appData = (ApplicationData) getActivity().getApplication();
	serverRequest = new ServerRequests(getActivity()
		.getApplicationContext());
	final ActivitySwipeDetector swipeHandler = new ActivitySwipeDetector(this);
	_examListToBeApproved.setOnTouchListener(swipeHandler);
	handler = new Handler();

	view.setVisibility(View.GONE);



	return view;
    }



    /**
     * Request to server.
     *
     * @param url the url
     * @param examId the exam id
     */
    public void requestToServer(final String url, String examId) {

	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());
	final StringParameter testId = new StringParameter("testId", examId);
	final StringParameter type = new StringParameter("type", "approve");

	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);
	params.add(testId);
	params.add(type);

	final PostRequestHandler request = new PostRequestHandler(
		serverRequest.getRequestURL(url, ""), params,
		new DownloadCallback() {
		    @Override
		    public void onSuccess(String downloadedString) {
			// TODO Auto-generated method stub
			try {
			    ApplicationData.writeToFile(
				    downloadedString,
				    appData.getTeacherExamFilePath(
					    appData.getUserId(), ExamId)
					    + ApplicationData.TEACHER_APPROVAL_EXAM);
			    if (url.equalsIgnoreCase(ServerRequests.TEACHER_EXAMS_TO_BE_APPROVED)) {
				Log.i(tag, "I am in download");
				parseExamObJ(downloadedString);
			    }
			} catch (final Exception e) {
			    Logger.error(tag, "I am in onSucccess catch block"
				    + e);
			}

		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {

		    }

		    @Override
		    public void onFailure(String failureMessage) {

		    }
		});
	request.request();
    }

    /**
     * Update ui.
     */
    public void updateUI() {

	try {
	    if (exam != null) {
		if (exam.getExamDetails() != null) {
		    section.setText("Subject : "+exam.getExamDetails().getSubject()+" |Grade : "
			    + exam.getExamDetails().getGrade() + " |Section : "
			    + exam.getExamDetails().getSection()+" |Duration : "+exam.getExamDetails().getDuration()+" mins"
			    + " |Category : "+exam.getExamDetails().getExamCategory());
		    section.setTextColor(Color.BLACK);
		    examName.setText("Test : "+exam.getExamDetails().getTitle());
		    marks.setText( exam.getExamDetails().getMaxPoints() + "M");
		    /*time.setText("Duration "
			    + exam.getExamDetails().getDuration() + "");*/
		    question.setText( exam.getQuestions().size() + "");
		    totalNoOfQuestions = exam.getQuestions().size();

		    final Date examStartDateTime = new Date(exam.getExamDetails()
			    .getStartDate());
		    final Date examEndDateTime = new Date(exam.getExamDetails()
			    .getEndDate());

		    final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			    "EEE, d MMM yyyy HH:mm");
		    final String startdate = dateFormatter
			    .format(examStartDateTime);

		    final String enddate = dateFormatter
			    .format(examEndDateTime);

		    final int startDateIndex = startdate.indexOf(" ");
		    final int endDateIndex = startdate.indexOf(" ");
		    startDate.setText(startdate.substring(startDateIndex,
			    startdate.length()));
		    endDate.setText(enddate.substring(endDateIndex,
			    enddate.length()));

		    if (exam.getQuestions() != null
			    && exam.getQuestions().size() > 0) {
			questions = exam.getQuestionWithNumber(questionNo);
		    }
		    clearLayout(_examListToBeApproved);
		    if (questions != null) {
			updateAnsweerSheetLayout(questions,
				_examListToBeApproved);
		    } else
			Logger.warn(tag, "Question object is null");
		}
	    } else {
		Logger.warn(tag, "One of te objects is null");
	    }

	} catch (final QuestionDoesNotExistsException e) {
	    Logger.error(tag, "I am in Update UI catch block Exception" + e);
	}

    }

    /**
     * Clear layout.
     *
     * @param layout the layout
     * @return the linear layout
     */
    private LinearLayout clearLayout(LinearLayout layout) {

	if (layout == null) {
	    return new LinearLayout(getActivity());
	}
	layout.removeAllViews();

	return layout;
    }

    /**
     * Update answeer sheet layout.
     *
     * @param question the question
     * @param answerLayout the answer layout
     * @return the linear layout
     */
    private LinearLayout updateAnsweerSheetLayout(Question question,
	    LinearLayout answerLayout) {
	final TeacherExamUI ui = TeacherExamUI.getInstance();
	return ui.build(questions, getActivity(), _examListToBeApproved);
    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#moveToNextPage()
     */
    @Override
    public void moveToNextPage() {
	Logger.warn(tag, "SWIPE - question no in move to next is:" + questionNo);
	if (questionNo < totalNoOfQuestions - 1) {
	    questionNo++;

	} else {
	    Logger.warn(tag, "SWIPE - we are on last question");
	    Toast.makeText(getActivity(), "You are on last question",
		    Toast.LENGTH_SHORT).show();
	}
	updateUI();

    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#moveToPreviousPage()
     */
    @Override
    public void moveToPreviousPage() {
	Logger.info(tag, "SWIPE - question no in move to previous is:"
		+ questionNo);
	if (questionNo > 0) {
	    questionNo--;
	} else {
	    Logger.info(tag, "SWIPe -  we are on first question");
	    Toast.makeText(getActivity(), R.string.first_question,
		    Toast.LENGTH_SHORT).show();
	}
	updateUI();

    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#toggleHeaderFooterVisibility()
     */
    @Override
    public void toggleHeaderFooterVisibility() {

    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#toggleBookmarksNotesListVisibility()
     */
    @Override
    public void toggleBookmarksNotesListVisibility() {

    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#scroll()
     */
    @Override
    public void scroll() {

    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#toggleTableOfcontentsListVisibility()
     */
    @Override
    public void toggleTableOfcontentsListVisibility() {

    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#onJustTouch()
     */
    @Override
    public void onJustTouch() {
    }

    /**
     * Parses the exam ob j.
     *
     * @param content the content
     */
    public void parseExamObJ(String content) {
	final ObjectMapper objMapper = new ObjectMapper();
	BaseResponse response;
	try {
	    response = objMapper.readValue(content, BaseResponse.class);
	    if (response != null) {

		exam = ExamParser.getExamFromString(response.getData());

	    }
	} catch (final Exception e) {

	}
	handler.post(new Runnable() {

	    @Override
	    public void run() {
	    	linearLayout.setVisibility(View.VISIBLE);
	    	examName.setVisibility(View.VISIBLE);
	    	section.setVisibility(View.VISIBLE);
	    	updateUI();
	    	progressBar.dismiss();
	    }
	});

    }

    /**
     * Data from list frag.
     *
     * @param examid the examid
     */
    public void dataFromListFrag(String examid) {
    	
	view.setVisibility(View.VISIBLE);
	ExamId = examid;
	questionNo = 0;
	progressBar = new ProgressDialog(view.getContext());
	progressBar.setCancelable(true);
	progressBar.setMessage("Downloading tests please wait ...");
	progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	progressBar.setProgress(0);
	progressBar.setMax(100);
	progressBar.show();
	if (!ApplicationData.isFileExists(appData.getTeacherExamFilePath(
		appData.getUserId(), ExamId)
		+ ApplicationData.TEACHER_AWAITING_APPROVAL_EXAMS))
	    requestToServer(ServerRequests.TEACHER_EXAMS_TO_BE_APPROVED, ExamId);
	else {
	    try {
		parseExamObJ(ApplicationData.readFile(appData
			.getTeacherExamFilePath(appData.getUserId(), ExamId)
			+ ApplicationData.TEACHER_AWAITING_APPROVAL_EXAMS));
	    } catch (final Exception e) {
		Log.e(tag, "EXCEPTION in dataFromListFragment" + e);
	    }
	}
    }

}
