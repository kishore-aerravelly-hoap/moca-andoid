package com.pearl.activities;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.book.guesturehandlers.GestureActionHandler;
import com.pearl.examination.ExamResult;
import com.pearl.examination.Question;
import com.pearl.examination.exceptions.QuestionDoesNotExistsException;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.AnswerSheetParser;
import com.pearl.parsers.json.ExamResultParser;
import com.pearl.ui.helpers.examination.AnswerResultUI;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamResultsActivity.
 */
public class ExamResultsActivity extends BaseActivity implements
GestureActionHandler {
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The server request. */
    private ServerRequests serverRequest;
    
    /** The exam id. */
    private String examId;
    
    /** The exam result. */
    private ExamResult examResult;
    
    /** The answers layout. */
    private LinearLayout answersLayout;
    
    /** The exam title. */
    private TextView examTitle;
    
    /** The subject. */
    private TextView subject;
    
    /** The date. */
    private TextView date;
    
    /** The student name. */
    private TextView studentName;
    
    /** The grade. */
    private TextView grade;
    
    /** The exam total marks. */
    private TextView examTotalMarks;
    
    /** The marks awarded. */
    private TextView marksAwarded;
    
    /** The marks alloted. */
    private TextView marksAlloted;
    
    /** The student total marks. */
    private TextView studentTotalMarks;
    // private TextView section;
    /** The question no. */
    private int questionNo = 0; // Question number of exam result object
    
    /** The total no of questions. */
    private int totalNoOfQuestions; // Total number of questions for exam
    
    /** The question. */
    private Question question; // Question object of exam result
    
    /** The teacher name. */
    private String teacherName;
    private RelativeLayout header_layout;
    private ImageView help;
    
    /** The wifi. */
    WifiManager wifi;
    private ImageView expandableNameanswer;
    private Context context;
    private int i = 0;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.exam_result);
	Toast.makeText(
		getApplicationContext(),
		"To move from one question to other question swipe on Answersheet",
		10000).show();
	appData = (ApplicationData) getApplication();
	serverRequest = new ServerRequests(this);
	context = this;
	answersLayout = (LinearLayout) findViewById(R.id.answers_layout);
	examTitle = (TextView) findViewById(R.id.exam_title_answer);
	subject = (TextView) findViewById(R.id.subject_answer);
	date = (TextView) findViewById(R.id.start_time_answer);
	studentName = (TextView) findViewById(R.id.student_name_answer);
	grade = (TextView) findViewById(R.id.grade_answer);
	studentTotalMarks = (TextView) findViewById(R.id.total_marks_student);
	examTotalMarks = (TextView) findViewById(R.id.total_marks_answer);
	marksAwarded = (TextView) findViewById(R.id.marks_awarded);
	marksAlloted = (TextView) findViewById(R.id.marks_alloted);
	wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	expandableNameanswer=(ImageView) findViewById(R.id.expandableName_answer);
	help = (ImageView) findViewById(R.id.question_list_help);
	header_layout = (RelativeLayout)findViewById(R.id.header_layout);
	final Bundle bundle = this.getIntent().getExtras();
	if (bundle != null) {
	    examId = bundle.getString(VegaConstants.EXAM_ID);
	}
	final ActivitySwipeDetector swipeHandler = new ActivitySwipeDetector(this);
	answersLayout.setOnTouchListener(swipeHandler);

	if (wifi.isWifiEnabled()) {
	    downloadAnswerSheet();
	} else if (ApplicationData.isFileExists(appData
		.getEvaluatedAnswerSheetFile(examId))) {
	    final List<String> data = AnswerSheetParser.getAnswerSheet(appData
		    .getEvaluatedAnswerSheetFile(examId));
	    Logger.warn(tag, "data after prsing is:" + data);
	    if (data != null)
		Logger.warn(tag,
			"string data after prsing is:" + data.toString());
	    parseAnswerSheet(data);
	    Toast.makeText(
		    getApplicationContext(),
		    "To move from one question to other question swipe on Answersheet",
		    10000).show();
	    updateUI();
	}
	final Button backbutton = (Button) findViewById(R.id.back_button);
	backbutton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		final Intent iii = new Intent(ExamResultsActivity.this,
			ExamListActivity.class);
		startActivity(iii);
		ExamResultsActivity.this.finish();
	    }
	});
	 help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
    }

    /**
     * Update ui.
     */
    public void updateUI() {

	// getQuestionAndAnswersFromJson();

	try {
	    if (examResult != null) {
		if (examResult.getExam() != null
			&& examResult.getExam().getExamDetails() != null) {
		    examTitle.setText(examResult.getExam().getExamDetails()
			    .getTitle());
		    subject.setText(examResult.getExam().getExamDetails()
			    .getSubject());

		    final Calendar cal = Calendar.getInstance();
		    cal.setTimeInMillis(examResult.getExam().getExamDetails()
			    .getUploadedDate());
		    Logger.info(tag, examResult.getExam().getExamDetails()
			    .getUploadedDate()
			    + "::" + cal.getTime().toString());

		    final Date examStartDateTime = new Date(examResult.getExam()
			    .getExamDetails().getUploadedDate());
		    final Date currentDateTime = new Date();

		    final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			    "EEE, d MMM, ''yy ");
		    final String startdate = dateFormatter
			    .format(examStartDateTime);

		    Logger.info(tag + "::Timings", "Exam start Date is "
			    + examStartDateTime.toString()
			    + " Current Date is " + currentDateTime.toString());

		    final int startDateIndex = startdate.indexOf(" ");
		    date.setText(startdate.substring(startDateIndex,
			    startdate.length()));
		    // date.setText(new
		    // Date(examResult.getExam().getExamDetails().getStartDate()).toString());
		    //studentName.setText(appData.getUser().getSecondName());
		    //String tempName="samreen nnnnnnnnnnnnnnnnn";
		    String tempName=appData.getUser().getSecondName();
			//student.setText(appData.getUser().getSecondName());// .getFirstName());
			
			if(tempName.length()>18)
			{
				tempName=tempName.substring(0,17) + "..";
				//make the image visible
				expandableNameanswer.setVisibility(View.VISIBLE);
				}
			else
			{
				//set the image visibility to invisible
				expandableNameanswer.setVisibility(View.INVISIBLE);
			}
			studentName.setText(tempName);
			expandableNameanswer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder=new AlertDialog.Builder(context);
					builder.setTitle("Your full name");
					builder.setMessage(appData.getUser().getSecondName());
					builder.setCancelable(true);
					builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					builder.show();
					
				}
			});
		
		    grade.setText(examResult.getExam().getExamDetails()
			    .getGrade()
			    + ", "
			    + examResult.getExam().getExamDetails()
			    .getSection());
		    // section.setText("Section: "+examResult.getExam().getExamDetails().getSection());
		    final double totalmarks = examResult.getGrandTotalMarks();
		    Logger.warn("Total Marks",
			    "GrandTotal:" + String.valueOf(totalmarks));
		    Logger.warn(tag,
			    "TotalMarks: " + examResult.getTotalMarks());
		    examTotalMarks.setText("Total Marks : "+examResult.getTotalMarks());
		    studentTotalMarks.setText("Marks Obtained : "+String.valueOf(examResult
			    .getManualCorrectAnsTotalMarks()
			    + examResult.getCorrectAnsTotalMarks())
			    + " / "
			    + String.valueOf(examResult.getTotalMarks()));
		    Logger.warn(tag, "Question list is:"
			    + examResult.getExam().getQuestions());
		    if (examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().size() != 0) {
			question = examResult.getExam().getQuestionWithNumber(
				questionNo);
		    }
		    clearLayout(answersLayout);
		    if (question != null) {
			marksAlloted.setText("Marks Allotted : "
				+ question.getMarksAlloted() + "");
			marksAwarded.setText("Marks Awarded : "
				+ question.getMarksAwarded() + "");
			updateAnsweerSheetLayout(question, answersLayout);
		    } else
			Logger.warn(tag, "Question object is null");
		    header_layout.setVisibility(View.VISIBLE);
		}
	    } else {
		Logger.warn(tag, "One of te objects is null");
	    }

	} catch (final QuestionDoesNotExistsException e) {
	    Logger.error(tag, e);
	}
    }
    

    /**
     * Download answer sheet.
     */
    private void downloadAnswerSheet() {
	loadingDialog.setTitle("Getting answer sheet..");
	loadingDialog
	.setMessage("Please be patient.. downloading answer sheet from the server");
	loadingDialog.show();
	PostRequestHandler prh = null;
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	final StringParameter sp1 = new StringParameter("testId", examId);
	final StringParameter sp2 = new StringParameter("studentId",
		appData.getUserId());
	final StringParameter sp3 = new StringParameter("type",
		"publish");
	params.add(sp1);
	params.add(sp2);
	params.add(sp3);
	final String requestUrl = serverRequest
		.getRequestURL(ServerRequests.RESULTS_ANSWERSHEET);
	Logger.warn(tag, "url for getting answer paper is:"+requestUrl
		+ "with params testId:"+examId+" and studentId:"+appData.getUserId());
	prh = new PostRequestHandler(requestUrl, params,
		new DownloadCallback() {

	    @Override
	    public void onSuccess(String downloadedString) {
		Logger.warn(tag, "answer sheet downloaded"
			+ downloadedString);
		ApplicationData.writeToFile(downloadedString,
			appData.getEvaluatedAnswerSheetFile(examId));
		final List<String> data = AnswerSheetParser
			.getAnswerSheet(appData
				.getEvaluatedAnswerSheetFile(examId));
		Logger.warn(tag, "data after prsing is:" + data);
		if (data != null)
		    Logger.warn(tag, "string data after prsing is:"
			    + data.toString());
		parseAnswerSheet(data);
		runOnUiThread(new Runnable() {

		    @Override
		    public void run() {
			loadingDialog.hide();
			Toast.makeText(
				getApplicationContext(),
				"To move from one question to other question swipe on Answersheet",
				10000).show();
			updateUI();
		    }
		});
	    }

	    @Override
	    public void onProgressUpdate(int progressPercentage) {
		// TODO Auto-generated method stub
		Logger.warn(tag, "ANSWER -  in progress");

	    }

	    @Override
	    public void onFailure(String failureMessage) {
		Logger.error(tag,
			"failed to get answer sheet from server");
		runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
			loadingDialog.hide();
			if (ApplicationData.isFileExists(appData
				.getEvaluatedAnswerSheetFile(examId))) {
			    final List<String> data = AnswerSheetParser.getAnswerSheet(appData
				    .getEvaluatedAnswerSheetFile(examId));
			    Logger.warn(tag, "data after prsing is:"
				    + data);
			    if (data != null) {
				Logger.warn(tag,
					"string data after prsing is:"
						+ data.toString());
				parseAnswerSheet(data);
			    }
			    updateUI();
			}else{
			    Toast.makeText(
				    activityContext,
				    R.string.Unable_to_reach_pearl_server,
				    1000).show();
			}
		    }
		});
	    }
	});
	prh.request();
    }

    /**
     * Parses the answer sheet.
     *
     * @param data the data
     */
    private void parseAnswerSheet(List<String> data) {
	Logger.warn(tag, "in parse answer sheet list size is: " + data.size()
		+ " and data is:" + data);
	String resultData = null;
	if (data != null && data.size() != 0) {
	    final Object[] obj = data.toArray();
	    if (obj[0] != null)
		resultData = obj[0].toString();
	    if (obj[1] != null)
		teacherName = obj[1].toString();
	}
	if (resultData != null) {
	    Logger.warn(tag, "exam data is:" + resultData);
	    try {
		examResult = ExamResultParser
			.getExamResult(resultData);
		totalNoOfQuestions = examResult.getExam().getQuestions().size();
	    } catch (final Exception e) {
		Logger.error(tag, e);
		downloadAnswerSheet();
	    }
	} else {
	    Logger.warn(tag, "exam result is null");
	    Toast.makeText(this, "Exam result is null", toastDisplayTime)
	    .show();
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
	    return new LinearLayout(activityContext);
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

	final AnswerResultUI ui = AnswerResultUI.getInstance();
	return ui.build(question, this, answerLayout);
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
	    Toast.makeText(this, "You are on last question", toastDisplayTime)
	    .show();
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
	    Toast.makeText(this, R.string.first_question, toastDisplayTime)
	    .show();
	}
	updateUI();
    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#toggleHeaderFooterVisibility()
     */
    
    private void showDialog(){
    	i = 0;
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pearl_tips_layout);
		RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.tips_layout);
		final TextView tips = (TextView) dialog.findViewById(R.id.tips);
		ImageView previous = (ImageView) dialog.findViewById(R.id.previous);
		ImageView next = (ImageView) dialog.findViewById(R.id.next);
		layout.setBackgroundResource(R.drawable.attendance_help);
		final List<String> list = HelpParser.getHelpContent("quizzard_answer sheet.txt", this);
		if(list != null && list.size() > 0){
			tips.setText(list.get(0));
		}
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(list != null && list.size() > 0){
					if(i < (list.size() - 1)){
						i = i +1;
						tips.setEnabled(true);
						tips.setText(list.get(i));						
					}
					else
						tips.setEnabled(false);
				}
			}
		});
		previous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(list != null && list.size() > 0){
					if(i > 0){
						i = i -1;
						tips.setEnabled(true);
						tips.setText(list.get(i));						
					}
					else
						tips.setEnabled(false);
				}
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
    }
    
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
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#onJustTouch()
     */
    @Override
    public void onJustTouch() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "ExamResultsActivity";
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
     */
    @Override
    public void onNetworkAvailable() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
     */
    @Override
    public void onNetworkUnAvailable() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
	// TODO Auto-generated method stub
	finish();
    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#toggleTableOfcontentsListVisibility()
     */
    @Override
    public void toggleTableOfcontentsListVisibility() {
	// TODO Auto-generated method stub

    }
}