package com.pearl.fragment;

/**
 * @author spasnoor
 * 
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.activities.ActivitySwipeDetector;
import com.pearl.activities.MyApprovalFragmemtActivity;
import com.pearl.activities.TeacherExamsActivity;
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
 * The Class ExamToBeApproved.
 */
public class ExamToBeApproved extends Fragment implements GestureActionHandler {
    
    /** The reject. */
    private Button approve, reject;
    
    /** The rl. */
    LinearLayout rl;
    // private ImageView menuButton;
    /** The _exam list to be approved. */
    private LinearLayout _examListToBeApproved;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The question. */
    private TextView startDate, endDate, time, examName, section, marks,
	    question;
    
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
    private int totalNoOfQuestions = 0;
    
    /** The handler. */
    private Handler handler;
    
    /** The view. */
    private View view;
    
    /** The tag. */
    private final String tag = "ExamToBeApproved";
    ProgressDialog progressBar;
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	view = inflater.inflate(R.layout.approval_exams, container, false);
	approve = (Button) view.findViewById(R.id.approveButton);
	reject = (Button) view.findViewById(R.id.rejectButton);
	startDate = (TextView) view.findViewById(R.id.startDate);
	endDate = (TextView) view.findViewById(R.id.endDate);
	examName = (TextView) view.findViewById(R.id.examName);
	section = (TextView) view.findViewById(R.id.section_my_approval);
	marks = (TextView) view.findViewById(R.id.Marks);
	question = (TextView) view.findViewById(R.id.questions);
	_examListToBeApproved = (LinearLayout) view
		.findViewById(R.id.ExamListToBeApproved);
	rl = (LinearLayout) view.findViewById(R.id.details_layout);
	// menuButton=(ImageView)view.findViewById(R.id.menu_button);
	appData = (ApplicationData) getActivity().getApplication();
	serverRequest = new ServerRequests(getActivity()
		.getApplicationContext());
	final ActivitySwipeDetector swipeHandler = new ActivitySwipeDetector(
		this);
	_examListToBeApproved.setOnTouchListener(swipeHandler);
	handler = new Handler();

	/*
	 * menuButton.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) {
	 * 
	 * Intent i=new
	 * Intent((ExamToBeApproved.this).getActivity(),ActionBar.class);
	 * startActivity(i);
	 * 
	 * 
	 * 
	 * } });
	 */

	approve.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		
		if(expired)
			Toast.makeText(getActivity(), "You cannot Approve the test as it is expired",Toast.LENGTH_SHORT).show();
		else
			ShowProgressBar.showProgressBar(
					"Approving your test",
					requestToServer(ServerRequests.TEACHER_APPROVED_EXAMS,
						ExamId,expired), getActivity());
	    }
	});

	reject.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		handler.post(new Runnable() {
		    @Override
		    public void run() {
		    	if(expired){
		    		Toast.makeText(getActivity(), "You cannot Reject the test as it is expired",Toast.LENGTH_SHORT).show();
		    	}else{
		    		openDialog();
		    	}
		    }
		});
	    }
	});
	view.setVisibility(View.GONE);
	return view;
    }

    /**
     * Request to server.
     *
     * @param url the url
     * @param examId the exam id
     * @param expired 
     * @return the int
     */
    public int requestToServer(final String url, String examId, boolean expired) {

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
		    ObjectMapper objMapper = new ObjectMapper();
		    BaseResponse response;

		    @Override
		    public void onSuccess(String downloadedString) {
			try {
			    ApplicationData.writeToFile(
				    downloadedString,
				    appData.getTeacherExamFilePath(
					    appData.getUserId(), ExamId)
					    + ApplicationData.TEACHER_APPROVAL_EXAM);
			    if (url.equalsIgnoreCase(ServerRequests.TEACHER_EXAMS_TO_BE_APPROVED)) {
				Log.i(tag, "Iam in download");
				parseExamObJ(downloadedString);
			    }
			    if (url.equalsIgnoreCase(ServerRequests.TEACHER_APPROVED_EXAMS)) {
				response = objMapper.readValue(
					downloadedString, BaseResponse.class);
				if (response != null) {
				    Log.i(tag,
					    "STATUS of request to server is ------"
						    + response.getStatus()
							    .toString());
				    if (response.getStatus().toString()
					    .equalsIgnoreCase("SUCCESS"))

					handler.post(new Runnable() {

					    @Override
					    public void run() {
						new AlertDialog.Builder(
							getActivity())
							.setTitle(
								" Test approved")
							.setMessage(
								"Your test has been approved successfully")
							.setPositiveButton(
								"OK",
								new DialogInterface.OnClickListener() {

								    @Override
								    public void onClick(
									    DialogInterface dialog,
									    int which) {
									dialog.dismiss();
									final Intent main = new Intent(
										ExamToBeApproved.this
											.getActivity(),
										MyApprovalFragmemtActivity.class);
									startActivity(main);
									ExamToBeApproved.this
										.getActivity()
										.finish();

								    }
								}).show();
					    }
					});

				}
			    }
			} catch (final Exception e) {
			    Logger.error(tag,
				    "Exception in requestTo server is" + e);
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
	return 100;
    }

    /**
     * Request to server.
     *
     * @param url the url
     * @param examId the exam id
     * @param Message the message
     * @return the int
     */
    public int requestToServer(final String url, String examId, String Message) {
	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());
	final StringParameter testId = new StringParameter("testId", examId);
	final StringParameter message = new StringParameter("message", Message);
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);
	params.add(testId);
	params.add(message);

	final PostRequestHandler request = new PostRequestHandler(
		serverRequest.getRequestURL(url, ""), params,
		new DownloadCallback() {
		    ObjectMapper objMapper = new ObjectMapper();
		    BaseResponse response;

		    @Override
		    public void onSuccess(String downloadedString) {
			try {

			    if (url.equalsIgnoreCase(ServerRequests.TEACHER_REJECTED_EXAMS)) {
				response = objMapper.readValue(
					downloadedString, BaseResponse.class);
				if (response != null
					&& response.getStatus().toString()
						.equalsIgnoreCase("SUCCESS")) {

				    handler.post(new Runnable() {

					@Override
					public void run() {
					    Toast.makeText(
						    getActivity(),
						    "Successfully rejected test",
						    Toast.LENGTH_LONG).show();
					    final Intent i = new Intent(
						    ExamToBeApproved.this
							    .getActivity(),
						    TeacherExamsActivity.class);
					    startActivity(i);
					    ExamToBeApproved.this.getActivity()
						    .finish();

					}
				    });
				}
			    }
			} catch (final Exception e) {
			    Log.e("@@@@@@@@@", "ERRROR");
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
	return 100;
    }

    /**
     * Update ui.
     */
    public void updateUI() {

	try {
	    if (exam != null) {
		if (exam.getExamDetails() != null) {
		    section.setText("Subject : "
			    + exam.getExamDetails().getSubject() + " |Grade : "
			    + exam.getExamDetails().getGrade() + " |Section : "
			    + exam.getExamDetails().getSection()+" |Duration : "+exam.getExamDetails().getDuration()+" mins"
			    + " |Category : "+exam.getExamDetails().getExamCategory());
		    section.setTextColor(Color.BLACK);
		    marks.setText(exam.getExamDetails().getMaxPoints() + "M");
		 /*   time.setText("Duration "
			    + exam.getExamDetails().getDuration() + "");*/
		    question.setText(exam.getQuestions().size() + "");

		    examName.setText("Test : "
				    +exam.getExamDetails().getTitle());
		    totalNoOfQuestions = exam.getQuestions().size();

		    final Date examStartDateTime = new Date(exam
			    .getExamDetails().getStartDate());
		    final Date examEndDateTime = new Date(exam.getExamDetails()
			    .getEndDate());

		    final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			    "EEE, d MMM yyyy HH:mm ");
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
		Logger.warn(tag, "One of the objects is null");
	    }

	} catch (final QuestionDoesNotExistsException e) {
	    Logger.error(tag, e);
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
     * Open dialog.
     */
    public void openDialog() {
	final LinearLayout linearLayout = new LinearLayout(getActivity());
	linearLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		LayoutParams.FILL_PARENT));
	linearLayout.setPadding(30, 0, 30, 0);

	final EditText comments = new EditText(getActivity());
	comments.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		LayoutParams.FILL_PARENT));
	comments.setImeOptions(EditorInfo.IME_ACTION_DONE);
	comments.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
	comments.setHint("Comment");
	final AlertDialog.Builder dialog = new AlertDialog.Builder(
		getActivity());
	dialog.setTitle("Comment");
	dialog.setMessage("Enter comment to reject test");

	linearLayout.addView(comments);

	dialog.setView(linearLayout);

	dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialoginterface, int buttons) {
	
		dialoginterface.dismiss();
		if (TextUtils.getTrimmedLength(comments.getText().toString()) == 0) {
		    Toast.makeText(getActivity(),
			    "Please Enter Comment for Rejecting",
			    Toast.LENGTH_LONG).show();
		} else {
		    requestToServer(ServerRequests.TEACHER_REJECTED_EXAMS,
			    ExamId, comments.getText().toString());
		    //ShowProgressBar.showProgressBar("Rejecting your exam", , getActivity());
		}

	    }
	});

	dialog.setNegativeButton("Cancel",
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface arg0, int buttons) {
			arg0.dismiss();
		    }
		});
	dialog.show();
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
	    	examName.setVisibility(View.VISIBLE);
	    	section.setVisibility(View.VISIBLE);
	    	rl.setVisibility(View.VISIBLE);
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
    boolean expired;
    public void dataFromListFrag(String examid,boolean expired) {
	view.setVisibility(View.VISIBLE);
	ExamId = examid;
	this.expired = expired;
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
		+ ApplicationData.TEACHER_APPROVAL_EXAM))
	    requestToServer(ServerRequests.TEACHER_EXAMS_TO_BE_APPROVED, ExamId, expired);
	else {
	    try {
		parseExamObJ(ApplicationData.readFile(appData
			.getTeacherExamFilePath(appData.getUserId(), ExamId)
			+ ApplicationData.TEACHER_APPROVAL_EXAM));
	    } catch (final Exception e) {
		Log.e("ExamToBeApproved", "EXCEPTION" + e);
	    }
	}
    }

}
