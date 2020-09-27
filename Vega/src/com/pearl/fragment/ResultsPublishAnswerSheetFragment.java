package com.pearl.fragment;
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
import com.pearl.chat.server.response.BaseResponse;
import com.pearl.examination.Exam;
import com.pearl.examination.ExamResult;
import com.pearl.examination.Question;
import com.pearl.examination.exceptions.QuestionDoesNotExistsException;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.ExamResultParser;
import com.pearl.ui.helpers.examination.ResultsPublishAnswerSheet;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultsPublishAnswerSheetFragment.
 */
public class ResultsPublishAnswerSheetFragment extends Fragment implements
GestureActionHandler {
    
    /** The tag. */
    String tag = "ResultsPublishAnswerSheetFragment";
    
    /** The _exam list to be approved. */
    private LinearLayout _examListToBeApproved,linearlayout1;
    
    /** The app data. */
    ApplicationData appData;
    
    /** The marks awarded. */
    TextView laSallename, startDate, endDate, time, examName, section, marks,
    question,marksAwarded;
    
    /** The Exam id. */
    String ExamId;
    
    /** The Student id. */
    String StudentId;
    
    /** The exam. */
    Exam exam;
  
    
    /** The exam result. */
    ExamResult examResult;
    
    /** The server request. */
    ServerRequests serverRequest;
    
    /** The question no. */
    private int questionNo = 0;
    
    /** The questions. */
    Question questions;
    
    /** The total no of questions. */
    int totalNoOfQuestions = 0;
    
    /** The handler. */
    Handler handler;
    
    /** The view. */
    View view;

    private ProgressDialog progressBar;
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	view = inflater.inflate(R.layout.results_publish_answer_sheet,
		container, false);
	/*laSallename = (TextView) view
		.findViewById(R.id.results_publish_answer_sheet_lasalleName);*/
	startDate = (TextView) view
		.findViewById(R.id.results_publish_answer_sheet_startDate);
	endDate = (TextView) view
		.findViewById(R.id.results_publish_answer_sheet_endDate);
	time = (TextView) view
		.findViewById(R.id.results_publish_answer_sheet_time);
	examName = (TextView) view
		.findViewById(R.id.results_publish_answer_sheet_examName);
	section = (TextView) view
		.findViewById(R.id.results_publish_answer_sheet_section);
	marks = (TextView) view
		.findViewById(R.id.results_publish_answer_sheet_Marks);
	question = (TextView) view
		.findViewById(R.id.results_publish_answer_sheet_questions);
	_examListToBeApproved = (LinearLayout) view
		.findViewById(R.id.results_publish_answerSheetLayout);
	linearlayout1 = (LinearLayout) view.findViewById(R.id.results_publish_linear_layout);
	marksAwarded=(TextView)view.findViewById(R.id.totalMarksAwarded);
	appData = (ApplicationData) getActivity().getApplication();
	serverRequest = new ServerRequests(getActivity()
		.getApplicationContext());
	final ActivitySwipeDetector swipeHandler = new ActivitySwipeDetector(this);
	_examListToBeApproved.setOnTouchListener(swipeHandler);
	handler = new Handler();

	view.setVisibility(View.GONE);
	return view;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();

    }

    /**
     * Request to server.
     *
     * @param testID the test id
     * @param StudentID the student id
     */
    public int requestToServer(String testID, String StudentID) {

	final StringParameter studentId = new StringParameter("studentId", StudentID);
	final StringParameter testId = new StringParameter("testId", testID);
	final StringParameter type = new StringParameter("type","publish");

	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(testId);
	params.add(studentId);
	params.add(type);

	final PostRequestHandler request = new PostRequestHandler(
		serverRequest.getRequestURL(
			ServerRequests.RESULTS_PUBLISH_ANSWERSHEET, ""),
			params, new DownloadCallback() {
		    @Override
		    public void onSuccess(String downloadedString) {
			// TODO Auto-generated method stub
			try {
			    /*
			     * ApplicationData.writeToFile( downloadedString,
			     * appData.getTeacherExamFilePath(
			     * appData.getUserId(), ExamId) +
			     * ApplicationData.RESULTS_PUBLISH_ANSWERSHEET);
			     */
			    Log.i("RESULTS_PUBLISH_ANSWERSHEET",
				    "-----------------------------in download");

			    parseExamObJ(downloadedString);
			} catch (final Exception e) {
			    Log.e("@@@@@@@@@", "ERRROR");
			}

		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
		    }

		    @Override
		    public void onFailure(String failureMessage) {

		    	handler.post(new Runnable() {
					
					@Override
					public void run() {
						progressBar.dismiss();
					}
				});
		    	Toast.makeText(getActivity().getApplicationContext(), R.string.Unable_to_reach_pearl_server, Toast.LENGTH_SHORT).show();
		    }
		});
	request.request();
	return 100;
    }

    /**
     * Update ui.
     */
    public void updateUI() {
    	linearlayout1.setVisibility(View.VISIBLE);
	try {
	    if (exam != null) {
		if (exam.getExamDetails() != null) {

		    Log.i("@@@@@@@@@@@@",
			    "exam.getExamDetails().getMaxPoints()"
				    + exam.getExamDetails().getMaxPoints());

		    section.setText("Subject : "+exam.getExamDetails().getSubject()+" |Grade : "+exam.getExamDetails().getGrade()+" |Section : "+exam.getExamDetails().getSection()+ " |Category : "+exam.getExamDetails().getExamCategory());
		    section.setTextColor(Color.BLACK);
		    
		    
		    if((examResult.getCorrectAnsTotalMarks()+examResult.getManualCorrectAnsTotalMarks())>((long)(examResult.getCorrectAnsTotalMarks()+examResult.getManualCorrectAnsTotalMarks()))) {
			double maxpts=examResult.getCorrectAnsTotalMarks()+examResult.getManualCorrectAnsTotalMarks();
			  marksAwarded.setText((maxpts)+"M");
		    }else {
			int maxptts=(int)Math.round(examResult.getCorrectAnsTotalMarks()+examResult.getManualCorrectAnsTotalMarks());
			 marksAwarded.setText((maxptts)+"M");
		    }
		           marks.setText(exam.getExamDetails().getMaxPoints()+"M");
		           examName.setText("Test : "+exam.getExamDetails().getTitle());
		    time.setText(exam.getExamDetails().getDuration() + " mins");
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
			    startdate.length()).trim()+"");
		    endDate.setText(enddate.substring(endDateIndex,
			    enddate.length()).trim()+"");

		    if (exam.getQuestions() != null
			    && exam.getQuestions().size() > 0) {
			questions = exam.getQuestionWithNumber(questionNo);
		    }
		    clearLayout(_examListToBeApproved);
		    if (questions != null) {
			updateAnsweerSheetLayout(questions,	_examListToBeApproved);
		    } else
			Logger.warn("@@@@@@@@@@@@@", "Question object is null");
		}
	    } else {
		Logger.warn("@@@@@@@@@@", "One of te objects is null");
	    }

	} catch (final QuestionDoesNotExistsException e) {
	    Logger.error("@@@@@@@@@@@@@@", e);
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
	// appData.setCurrentExamQuestionNo(question.getQuestionOrderNo());
	Log.i(tag, "In uodatew Layout.........");
	final ResultsPublishAnswerSheet ui = ResultsPublishAnswerSheet.getInstance();
	return ui.build(questions, getActivity(), _examListToBeApproved);
    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#moveToNextPage()
     */
    @Override
    public void moveToNextPage() {
	// TODO Auto-generated method stub
	Logger.warn("@@@@@@@@@@", "SWIPE - question no in move to next is:"
		+ questionNo);
	if (questionNo < totalNoOfQuestions - 1) {
	    questionNo++;

	} else {
	    Logger.warn("@@@@@@@@@@", "SWIPE - we are on last question");
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
	Logger.info("@@@@@", "SWIPE - question no in move to previous is:"
		+ questionNo);
	if (questionNo > 0) {
	    questionNo--;
	} else {
	    Logger.info("@@@@@@@@@@@", "SWIPe -  we are on first question");
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
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#toggleBookmarksNotesListVisibility()
     */
    @Override
    public void toggleBookmarksNotesListVisibility() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#scroll()
     */
    @Override
    public void scroll() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#toggleTableOfcontentsListVisibility()
     */
    @Override
    public void toggleTableOfcontentsListVisibility() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#onJustTouch()
     */
    @Override
    public void onJustTouch() {
	// TODO Auto-generated method stub
    }

    /**
     * Parses the exam ob j.
     *
     * @param content the content
     */
    @SuppressWarnings("unchecked")
    public void parseExamObJ(String content) {
	Log.i(tag, "I am in parseExamObJ");
	final ObjectMapper objMapper = new ObjectMapper();
	BaseResponse response;
	try {
	    response = objMapper.readValue(content, BaseResponse.class);
	    if (response != null) {
		final ArrayList<String> examObj = (ArrayList<String>) response
			.getData();
		final String answerSheet = examObj.get(0);
		examResult = ExamResultParser.getExamResult(answerSheet);
		exam = examResult.getExam();
	    }
	} catch (final Exception e) {
	    Logger.error(tag, e);

	}
	handler.post(new Runnable() {

	    @Override
	    public void run() {
	    	updateUI();
	    	progressBar.dismiss();
	    }
	});

    }

    /**
     * Data from students list frag.
     *
     * @param examid the examid
     * @param studentid the studentid
     */
    public void dataFromStudentsListFrag(String examid, String studentid) {
	view.setVisibility(View.VISIBLE);
	ExamId = examid;
	StudentId = studentid;
	questionNo = 0;
	progressBar = new ProgressDialog(view.getContext());
	progressBar.setCancelable(true);
	progressBar.setMessage("Downloading tests please wait ...");
	progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	progressBar.setProgress(0);
	progressBar.setMax(100);
	progressBar.show();
	requestToServer(ExamId, StudentId);
	/*
	 * if (!ApplicationData.isFileExists(appData.getTeacherExamFilePath(
	 * appData.getUserId(), ExamId) +
	 * ApplicationData.RESULTS_PUBLISH_ANSWERSHEET))
	 * requestToServer(ExamId,StudentId); else { try {
	 * parseExamObJ(ApplicationData.readFile(appData
	 * .getTeacherExamFilePath(appData.getUserId(), ExamId) +
	 * ApplicationData.RESULTS_PUBLISH_ANSWERSHEET)); } catch (Exception e)
	 * { Log.e("ExamToBeApproved", "EXCEPTION" + e); } }
	 */
    }

}
