package com.pearl.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.activities.ActivitySwipeDetector;
import com.pearl.activities.TeacherPublishFragmentActivity;
import com.pearl.android.ui.ShowProgressBar;
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
 * The Class TeacherPublishExamListFragment.
 */
public class TeacherPublishExamListFragment extends Fragment implements
GestureActionHandler {
    
    /** The _exam list to be approved. */
    private LinearLayout _examListToBeApproved,linearLayout;
    
    
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The publish button. */
    private Button publishButton;
    
    /** The question. */
    private TextView startDate, endDate, time, examName, section, marks,
    question;
    
    /** The Exam id. */
    private String ExamId=null;
    
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

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	view = inflater.inflate(
		R.layout.teacher_publish_questionpaper, container,
		false);
	startDate = (TextView) view
		.findViewById(R.id.publish_startDate);
	endDate = (TextView) view
		.findViewById(R.id.publish_endDate);
	/*time = (TextView) view.findViewById(R.id.publish_time);*/
	examName = (TextView) view
		.findViewById(R.id.publish_examName);
	section = (TextView) view
		.findViewById(R.id.publish_section);
	marks = (TextView) view.findViewById(R.id.publish_Marks_1);
	question = (TextView) view
		.findViewById(R.id.publish_questions_1);
	_examListToBeApproved = (LinearLayout) view
		.findViewById(R.id.publish_ExamListToBeApproved);
	publishButton=(Button)view.findViewById(R.id.publish_button);
	linearLayout = (LinearLayout) view.findViewById(R.id.testpublish_layout);
	appData = (ApplicationData) getActivity().getApplication();
	serverRequest = new ServerRequests(getActivity()
		.getApplicationContext());
	final ActivitySwipeDetector swipeHandler = new ActivitySwipeDetector(this);
	_examListToBeApproved.setOnTouchListener(swipeHandler);
	handler = new Handler();
	view.setVisibility(View.GONE);

	publishButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
	    	
	    	if(expired)
	    		Toast.makeText(getActivity(), "you cannot Publish test as it is expired",Toast.LENGTH_SHORT).show();
	    	else{
	    		if(ExamId!=null || ExamId.trim().length()!=0)
				    ShowProgressBar.showProgressBar("Publishing test", requestToServer(ExamId, expired), getActivity());	    		
	    	}
	    }
	});

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
     */
    public int requestToServer() {
	Log.i("@@@@@@@@@@@@@@@@@@@@@",
		"EXAM ID IN METHOD IS" + ExamId);
	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());
	final StringParameter examIdslist = new StringParameter("testId", ExamId);
	final StringParameter type=new StringParameter("type", "publish");
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);
	params.add(examIdslist);
	params.add(type);
	final PostRequestHandler postRequest = new PostRequestHandler(
		serverRequest.getRequestURL(
			ServerRequests.TEACHER_EXAMS_TO_BE_APPROVED, ""), params,
			new DownloadCallback() {
		    @Override
		    public void onSuccess(String downloadedString) {
			// TODO Auto-generated method stub
			final ObjectMapper objMapper = new ObjectMapper();
			BaseResponse response;

			try {
			    response = objMapper.readValue(downloadedString, BaseResponse.class);
			    if (response != null) {

				exam = ExamParser.getExamFromString(response.getData());

			    }
			} catch (final Exception e) {
			    e.printStackTrace();
			}
			handler.post(new Runnable() {

			    @Override
			    public void run() {
				updateUI();
				ShowProgressBar.progressBar.dismiss();
			    }
			});


		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {

		    }

		    @Override
		    public void onFailure(String failureMessage) {

		    }
		});
	postRequest.request();
	return 100;
    }
    
    /**
     * Request to server.
     *
     * @param examId the exam id
     */
    public int requestToServer( String examId,Boolean expired) {
	Log.i("@@@@@@@@@@@@@@@@@@@@@",
		"EXAM ID IN METHOD IS" + examId.toString());
	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());
	final StringParameter examIdslist = new StringParameter("examIdslist", examId);
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);
	params.add(examIdslist);

	final PostRequestHandler postRequest = new PostRequestHandler(
		serverRequest.getRequestURL(
			ServerRequests.TEACHER_PUBLISH_SUBMIT, ""), params,
			new DownloadCallback() {
		    ObjectMapper objMapper = new ObjectMapper();
		    BaseResponse baseResponse;

		    @Override
		    public void onSuccess(String downloadedString) {
			// TODO Auto-generated method stub
			try {
			    baseResponse = objMapper.readValue(
				    downloadedString, BaseResponse.class);

			    if (baseResponse.getStatus().toString()
				    .equalsIgnoreCase("SUCCESS"))
				handler.post(new Runnable() {

				    @Override
				    public void run() {
				    	ShowProgressBar.progressBar.dismiss();

					  Toast.makeText(getActivity(),
							"YOU ARE DONE",
							Toast.LENGTH_LONG).show();
				synchronized (TeacherPublishExamListFragment.this) {
				  
					final Intent i=new Intent(getActivity(),TeacherPublishFragmentActivity.class);
					startActivity(i);
					getActivity().finish();
				}	

				    }
				});

			} catch (final Exception e) {
			    Log.e("TeacherPublishList", "@@@@@@@@@@@@ ERROR "
				    + e);
			}
		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void onFailure(String failureMessage) {

			handler.post(new Runnable() {
			    @Override
			    public void run() {
				// Log.e("TeacherPublishList",
				// "@@@@@@@@@@@@ ON FAILURE  ERROR "+failureMessage);
				Toast.makeText(getActivity(),
					"TEST NOT PUBLISHED ",
					Toast.LENGTH_LONG).show();
				ShowProgressBar.progressBar.dismiss();

			    }
			});

		    }
		});
	postRequest.request();
	return 100;
    }
    
    /**
     * Update ui.
     */
    public void updateUI() {
    	linearLayout.setVisibility(View.VISIBLE);

	try {
	    if (exam != null) {
		if (exam.getExamDetails() != null) {
		    section.setText("Subject : "
			    + exam.getExamDetails().getSubject() + " |Grade : "
			    + exam.getExamDetails().getGrade() + " |Section : "
			    + exam.getExamDetails().getSection()+" |Duration : "+ exam.getExamDetails().getDuration()+" mins"
			    + " |Category : "+exam.getExamDetails().getExamCategory());
		    section.setTextColor(Color.BLACK);
		    examName.setText("Test : "
			    + exam.getExamDetails().getTitle());
		    marks.setText( exam.getExamDetails().getMaxPoints() + "M");
		   /* time.setText("Duration "
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
     * Data from list frag.
     *
     * @param examid the examid
     */
    boolean expired;
    public void dataFromListFrag(String examid,boolean expired) {
	view.setVisibility(View.VISIBLE);
	ExamId = examid;
	this.expired=expired;
	ShowProgressBar.showProgressBar("Please wait fetching exam.", requestToServer(),getActivity());

    }
}
