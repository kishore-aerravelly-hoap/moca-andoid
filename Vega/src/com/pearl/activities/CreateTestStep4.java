package com.pearl.activities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.pearl.AppStatus.AppStatus;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.book.guesturehandlers.GestureActionHandler;
import com.pearl.examination.ExamResult;
import com.pearl.examination.exceptions.QuestionDoesNotExistsException;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ResponseStatus;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.TeacherExamParser;
import com.pearl.ui.helpers.examination.Preview4UI;
import com.pearl.ui.models.BaseRequest;
import com.pearl.ui.models.BaseResponse;
import com.pearl.ui.models.StatusType;
import com.pearl.user.teacher.examination.Exam;
import com.pearl.user.teacher.examination.ExamDetails;
import com.pearl.user.teacher.examination.Question;
import com.pearl.user.teacher.examination.ServerExam;
import com.pearl.user.teacher.examination.ServerExamDetails;



// TODO: Auto-generated Javadoc
/**
 * The Class CreateTestStep4.
 */
public class CreateTestStep4 extends BaseActivity implements
GestureActionHandler {

    /** The max_marks. */
    private TextView subject, duration,testName,max_marks;
    
    /** The section. */
    private TextView grade, section;
    
    /** The questions layout. */
    private LinearLayout questionsLayout;
    
    /** The exam. */
    private Exam exam;
    
    /** The exam details. */
    private ExamDetails examDetails;
    
    /** The exam id. */
    private  String examId;
    
    /** The i. */
    private int i = 0;
    
    /** The baseresponse. */
    private BaseResponse baseresponse;
    
    /** The edit_button. */
    private    Button submitButton,menuButton,edit_button;;
    
    /** The help. */
    ImageView help,sectiondrop;
    
    /** The exam result. */
    private ExamResult examResult;
    
    /** The exam total marks. */
    private TextView examTotalMarks;
    
    /** The question no. */
    private int questionNo = 0;
    
    /** The total no of questions. */
    private int totalNoOfQuestions; // Total number of questions for exam
    
    /** The question. */
    private Question question;
    
    /** The test id. */
    private String testId=null;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The handler. */
    private Handler handler;
    
    /** The _server exam details. */
    private ServerExamDetails _serverExamDetails=null;
    
    /** The _submission status. */
    private boolean _submissionStatus;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.create_test_step_5);
	questionsLayout = (LinearLayout) findViewById(R.id.questions_layout);
	appData = (ApplicationData) getApplication();
	subject = (TextView) findViewById(R.id.subject_answer);
	grade = (TextView) findViewById(R.id.grade_answer);
	help = (ImageView) findViewById(R.id.preview_help);
	duration = (TextView) findViewById(R.id.duration_answer);
	testName=(TextView)findViewById(R.id.test_name_answer);
	max_marks=(TextView)findViewById(R.id.max_marks_step4);
	sectiondrop=(ImageView)findViewById(R.id.image_data);
	// examTotalMarks = (TextView) findViewById(R.id.total_marks_answer);
	section = (TextView) findViewById(R.id.section_answer);
	menuButton = (Button) findViewById(R.id.examMenu);
	submitButton = (Button) findViewById(R.id.submittt);
	edit_button=(Button)findViewById(R.id.edit_question);
	// make a call to download and parse the json
	String content = null;
	edit_button.setVisibility(View.GONE);
	final Bundle bundle = getIntent().getExtras();
	if(bundle != null)
	    testId = bundle.getString(VegaConstants.TEST_ID);
	handler=new Handler();
	try {
	    content = ApplicationData.readFile(appData.getStep4FilePath(getIntent().getExtras().getString(VegaConstants.TEST_ID))+ApplicationData.STEP4_FILE_NAME);
	} catch (final Exception e) {
	    Logger.error(tag, e);
	}

	/*ObjectMapper mapper = new ObjectMapper();
		String jsonData = "";*/
	ServerExam baseResponse = null;

	try {
	    baseResponse=TeacherExamParser.parseJsonToSeverExamObject(content);
	    _serverExamDetails=TeacherExamParser.parseJsonToSeverExamDetailsObject(content);
	    //baseResponse = mapper.readValue(content, ServerExam.class);
	} catch (final Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	//jsonData = baseResponse.getData().toString();
	Logger.warn(tag, "json data from server is:" + baseResponse);

	try {
	    //	exam = TeacherExamParser.getExamdetailsforStep4(jsonData);
	    exam=baseResponse.getExam();
	    Logger.warn("------>", "parsed exam " + exam);
	} catch (final Exception e1) {
	    Logger.error(tag, e1);
	}
	final ActivitySwipeDetector swipeHandler = new ActivitySwipeDetector(this);
	questionsLayout.setOnTouchListener(swipeHandler);
	updateUI();
	bindEvents();
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	if(!appData.isLoginStatus()) {

	    final Intent login = new Intent(this, LoginActivity.class);
	    startActivity(login);
	    Logger.info(tag,
		    "LOGIN status if shelf..." + appData.isLoginStatus());
	    finish();

	}
    }
    
    /**
     * Show dialog.
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
		final List<String> list = HelpParser.getHelpContent("quizzard_preview.txt", this);
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

    /**
     * Update ui.
     */
    private void updateUI() {

	try {
	    if (exam != null) {
		question = exam.getQuestionWithNumber(questionNo);
		totalNoOfQuestions = exam.getQuestions().size();
	    }

	} catch (final QuestionDoesNotExistsException e) {
	    Logger.error(tag, e);
	}

	subject.setText("Subject : "+exam.getExamDetails().getSubject());
	testName.setText(exam.getExamDetails().getTitle());
	max_marks.setText("Total Marks : "+exam.getExamDetails().getMaxPoints());
	grade.setText("Grade : "+_serverExamDetails.getGrade());
	if(_serverExamDetails.getSection().length()  > 8){
		section.setText("Section : "+_serverExamDetails.getSection().substring(0, 10)+"....");
		sectiondrop.setVisibility(View.VISIBLE);
		}else{
			section.setText("Section : "+_serverExamDetails.getSection());
			sectiondrop.setVisibility(View.INVISIBLE);
		}
	duration.setText("Duration : "+exam.getExamDetails().getDuration() +" Mins");
	clearLayout(questionsLayout);
	updateQuestionsLayout(question, questionsLayout);
    }
  
    /**
     * downloads the list of questions.
     */

    private void downloadQuestionsList() {

    }

    /**
     * Update questions layout.
     *
     * @param question the question
     * @param questionsLayout the questions layout
     * @return the linear layout
     */
    private LinearLayout updateQuestionsLayout(Question question,
	    LinearLayout questionsLayout) {

	final Preview4UI ui = Preview4UI.getInstance();
	return ui.build(question, this, questionsLayout);
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
     * Bind events.
     */
    public void bindEvents() {
    	
    	help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});

	submitButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		alertSubmit();
	    }
	});
	menuButton.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View paramView) {
		final Intent localIntent = new Intent(getApplicationContext(),
			ActionBar.class);
		startActivity(localIntent);
		menuButton.invalidate();

	    }

	});
	
	sectiondrop.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showSecDialog();
			
		}
	});

    }

    /**
     * Alert submit.
     */
    private void alertSubmit(){

	final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
		CreateTestStep4.this);
	alertDialog.setTitle("Add Multiple Sections");
	alertDialog.setMessage("Click 'Yes' to schedule test for other sections!");
	alertDialog.setPositiveButton("Yes",
		new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog,
		    int which) {
		submitEntireExamtoServer(testId,false,activityContext,appData);
		handler.post(new Runnable() {

		    @Override
		    public void run() {

			final Intent i=new Intent(CreateTestStep4.this,TeahcerExamMultiScheduleActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra(VegaConstants.TEST_ID, testId);
			startActivity(i);
			finish();

		    }
		});


	    }
	})
	.setNegativeButton("No",new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog,int id) {
		final AppStatus status = new AppStatus();
		if (status.isOnline(activityContext)) {
		    submitButton.setEnabled(true);
		    _submissionStatus=true;
		    submitEntireExamtoServer(testId,true,activityContext,appData);	
		} else {
		    Toast.makeText(activityContext,
			    "Please connect to internet in order to proceed",
			    Toast.LENGTH_LONG).show();
		}
		dialog.cancel();
	    }
	});
	// Showing Alert Message
	alertDialog.show();

    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#moveToNextPage()
     */
    @Override
    public void moveToNextPage() {
	Logger.warn(tag, "SWIPE - question no in move to next is:" + questionNo);
	if (questionNo < totalNoOfQuestions - 1) {
	    questionNo++;
	    Logger.warn(tag, "number of questions" + totalNoOfQuestions);
	    Logger.warn(tag, "at next" + questionNo);
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
	    Logger.warn(tag, "at previous" + questionNo);
	} else {
	    Logger.info(tag, "SWIPe -  we are on first question");
	    Toast.makeText(this, R.string.first_question, toastDisplayTime)
	    .show();
	}
	updateUI();
    }

    /**
     * Submit entire examto server.
     *
     * @param _testId the _test id
     * @param submissionStatus the submission status
     * @param ctx the ctx
     * @param appdata the appdata
     */
    public void submitEntireExamtoServer(final String _testId,final boolean submissionStatus,final Context ctx,final ApplicationData appdata) {
	// TODO Auto-generated method stub
	handler=new Handler();	
	final ProgressDialog loadingDialog;
	loadingDialog = new ProgressDialog(ctx);
	loadingDialog.setMessage("Loading..");
	loadingDialog.setIndeterminate(true);
	loadingDialog.setCancelable(false);

	loadingDialog.setTitle("Please Wait..");
	loadingDialog
	.setMessage("Uploading the Final Question Paper to the server");
	loadingDialog.show();
	final BaseRequest baseRequest = new BaseRequest();
	baseRequest.setAuth( appdata.getUser().getId());
	String entireExamContent = null;
	if (ApplicationData.isFileExists(appdata.getStep4FileName(_testId))) {
	    try {
		entireExamContent = ApplicationData.readFile(appdata.getStep4FileName(_testId));
	    } catch (final Exception e) {
		Logger.error(tag, e);
	    }
	} else {
	    Log.i(tag, "Error form file in step3");
	}
	String data=null;
	final ObjectMapper mapper = new ObjectMapper();
	try {
	    baseRequest.setData(entireExamContent);
	    data=mapper.writeValueAsString(baseRequest);
	    Logger.warn(tag, "data before submitting is:"+data);
	} catch (final Exception e1) {
	    Logger.error(tag, e1);
	}
	final StringParameter sp = new StringParameter("serverExm",data);
	final ArrayList<RequestParameter> postParams = new ArrayList<RequestParameter>();
	postParams.add(sp);

	final ServerRequests sr = new ServerRequests(ctx);
	final PostRequestHandler postRequestObj = new PostRequestHandler(sr.getRequestURL(ServerRequests.TEACHER_FINAL_QUESTIONPAPER,	""), postParams, new DownloadCallback() {

	    @Override
	    public void onSuccess(String downloadedString) {
		final ObjectMapper mapper = new ObjectMapper();
		BaseResponse response;
		try {
		    response = mapper.readValue(downloadedString,BaseResponse.class);

		    response.getStatus();
		    if (StatusType.SUCCESS.toString()
			    .equalsIgnoreCase(
				    ResponseStatus.SUCCESS)) {
			runOnUiThread(new Runnable() {

			    @Override
			    public void run() {

				handler.post(new Runnable() {

				    @Override
				    public void run() {
					loadingDialog.hide();
					Toast.makeText(
						ctx,
						"Question paper is submitted to server",
						toastDisplayTime)
						.show();
					if(submissionStatus) {
					    try {
						deleteObject(_testId,appdata);
						final Intent teacher=new Intent(ctx,TeacherExamsActivity.class);
						teacher.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						ctx.startActivity(teacher);
						//startActivity(teacher);
						finish();
					    } catch (final JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					    } catch (final IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					    }
					}
				    }
				});

			    }
			});

		    } else {
			response.getStatus();
			if (StatusType.FAILURE
				.toString()
				.equals(ResponseStatus.FAIL)) {
			    loadingDialog.hide();
			    Logger.warn(tag,
				    "Unable to submit question paper");
			}
		    }
		} catch (final Exception e) {
		    Logger.error(tag, e);
		}
	    }

	    @Override
	    public void onProgressUpdate(
		    int progressPercentage) {

	    }

	    @Override
	    public void onFailure(String failureMessage) {
		Logger.warn(tag,
			"Failed to submit Exam");
		handler.post(new Runnable() {

		    @Override
		    public void run() {

			loadingDialog.hide();
			Toast.makeText(ctx, "Unable to reach Pearl server", Toast.LENGTH_SHORT).show();	

		    }
		});


	    }
	});
	postRequestObj.request();


    }

    /**
     * Delete object.
     *
     * @param testId the test id
     * @param appdata the appdata
     * @throws JsonProcessingException the json processing exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void deleteObject(String testId,ApplicationData appdata) throws JsonProcessingException, IOException{
	List<ServerExamDetails> examList = null;
	if(ApplicationData.isFileExists(appdata.getTestsListFileName())){
	    String content = null;
	    try {
		content = ApplicationData.readFile(appdata.getTestsListFileName());
	    } catch (final IOException e) {
		e.printStackTrace();
	    }
	    examList = TeacherExamParser.getExamsList(content);
	}
	boolean idFound = false;
	final Iterator<ServerExamDetails> iterator = examList.iterator();
	while(iterator.hasNext()){
	    final ExamDetails examDetails = iterator.next();
	    this.examDetails = examDetails;
	    if(examDetails.getExamId().equals(testId)){
		Logger.warn("", "list doessnt contain id:"+testId);
		idFound = true;
	    }else
		Logger.info("", "ID - Id already exists");
	}
	if(idFound){
	    Logger.info("", "ID - id found delete id :"+examDetails.getExamId());
	    final boolean flag = examList.remove(examDetails);
	    if(flag){
		Logger.warn("", "succesfully removed from list");
		ApplicationData.deleteFolderOrFileRecursively(new File(appdata.getExamFilesPath(testId)));
	    }
	    else
		Logger.warn("", "unable to remove from list");
	}
	else
	    Logger.warn("", "ID - 2 id alreasy exists");

	for(int i=0; i<examList.size(); i++){
	    Logger.info("", "list size before writing is:"+examList.size());
	    Logger.info("", "list before writing is:"+examList.get(i).getTitle());			
	}
	final ObjectMapper om = new ObjectMapper();
	final String examListJson = om.writeValueAsString(examList);
	Logger.warn("", "string before writing is:"+examListJson);
	ApplicationData.writeToFile("{\"serverExamDetailsList\":" + examListJson + "}",
		appdata.getTestsListFileName());
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return tag;
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
     * Show dialog for sections.
     */
    private void showSecDialog(){
    	final AlertDialog.Builder dialog = new AlertDialog.Builder(CreateTestStep4.this);
		dialog.setTitle("Sections");
		dialog.setMessage(_serverExamDetails.getSection());
		dialog.show();
    }
    


}
