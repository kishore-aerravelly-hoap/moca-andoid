package com.pearl.fragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.activities.ActionBar;
import com.pearl.activities.ActivitySwipeDetector;
import com.pearl.activities.AwaitingEvaluationListActivity;
import com.pearl.activities.MyAwaitingApprovalActivity;
import com.pearl.application.ApplicationData;
import com.pearl.book.guesturehandlers.GestureActionHandler;
import com.pearl.examination.exceptions.QuestionDoesNotExistsException;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.ExamEvaluationParser;
import com.pearl.parsers.json.ExamResultParser;
import com.pearl.ui.helpers.examination.ExamEvaluationUI;
import com.pearl.ui.models.BaseRequest;
import com.pearl.user.teacher.examination.Question;
import com.pearl.user.teacher.examination.ServerExamDetails;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamEvaluationSheetFragment.
 */
public class ExamEvaluationSheetFragment extends Fragment implements GestureActionHandler{

    /** The handler. */
    private Handler handler;
    
    /** The server requests. */
    private ServerRequests serverRequests;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The exam id. */
    private String examId;
    
    /** The context. */
    private Context context;
    
    /** The name. */
    private String studentId,name;
    
    /** The exam result. */
    com.pearl.user.teacher.examination.ExamResult	examResult;
    
    /** The Constant tag. */
    private static final String tag = "ExamEvaluationSheetFragment";
    
    /** The student count. */
    private int totalNoOfQuestions, studentCount;
    
    /** The i. */
    private int questionNo = 0, i = 0;
    
    /** The evaluation layout. */
    private LinearLayout evaluationLayout;
    
    /** The marks. */
    private EditText marks;
    
    /** The relative layout1. */
    private LinearLayout linearLayout8;
    
    /** The relative layout2. */
    private RelativeLayout relativeLayout2;
    
    /** The Constant NEXT_QUESTION. */
    private static final String NEXT_QUESTION = "next question";
    
    /** The Constant PREVIOUS_QUESTION. */
    private static final String PREVIOUS_QUESTION = "previous question";
    
    /** The correct marks view. */
    private TextView correctMarksView;
    
    /** The correct count view. */
    private TextView correctCountView;
    
    /** The frame2. */
    private TextView wrongAnswerCountView,frame2;
    
    /** The automated total marks. */
    private TextView grandTotal,automatedTotalQuestions,automatedCorrectAnswerCount,automatedWrongAnswerCount,automatedTotalMarks;
    
    /** The subject. */
    private TextView studentName,grade,section,examDate,maxMarks,examTitle,subject;
    
    /** The help. */
    private ImageView submit;
    
    /** The frame1. */
    private RelativeLayout rootLayout,frame1;
    
    /** The correct answer marks map. */
    private HashMap<Integer, Double> correctAnswerMarksMap;
    
    /** The wrong answer count. */
    private HashMap<Integer, Double> wrongAnswerCount;
    
    /** The correct answer count. */
    private HashMap<Integer, Double> correctAnswerCount;
    
    /** The loading dialog. */
    private ProgressDialog loadingDialog,progressBar;
    
    /** The new flag. */
    private boolean newFlag;
    
    private View v;

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	v = inflater.inflate(R.layout.exam_evaluation_sheet, container,false);
	handler = new Handler();
	serverRequests = new ServerRequests(getActivity());
	appData = (ApplicationData)getActivity().getApplication();
	evaluationLayout = (LinearLayout)v.findViewById(R.id.questions_eval);
	correctMarksView = (TextView) v.findViewById(R.id.manual_correct_answer_marks);
	correctCountView = (TextView) v.findViewById(R.id.manual_correct_answer_count);
	wrongAnswerCountView = (TextView) v.findViewById(R.id.manual_wrong_answer_count);
	rootLayout = (RelativeLayout) v.findViewById(R.id.evaluation_root_layout);
	submit = (ImageView) v.findViewById(R.id.submitEvaluatedPaper);
	grandTotal = (TextView) v.findViewById(R.id.grand_total_count);
	studentName = (TextView) v.findViewById(R.id.student_name);
	subject = (TextView)v.findViewById(R.id.subject_name);
	grade = (TextView) v.findViewById(R.id.grade);
	section = (TextView) v.findViewById(R.id.section);

	examDate = (TextView) v.findViewById(R.id.exam_date);
	maxMarks = (TextView) v.findViewById(R.id.max_marks);
	relativeLayout2 = (RelativeLayout)v.findViewById(R.id.relativeLayout2);
	linearLayout8 = (LinearLayout)v.findViewById(R.id.text_view);
	examTitle = (TextView) v.findViewById(R.id.test_name);
	automatedCorrectAnswerCount = (TextView)v.findViewById(R.id.automated_corect_count);
	automatedTotalMarks = (TextView)v.findViewById(R.id.automated_total_marks);
	automatedTotalQuestions = (TextView)v.findViewById(R.id.automated_ques_count);
	automatedWrongAnswerCount = (TextView)v.findViewById(R.id.automated_wrong_count);
	frame1 = (RelativeLayout)v.findViewById(R.id.exam_details_layout);
	frame2 = (TextView)v.findViewById(R.id.frame2);
	final ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector(this);
	context = getActivity().getApplicationContext();
	evaluationLayout.setOnTouchListener(activitySwipeDetector);
	correctAnswerMarksMap = new HashMap<Integer, Double>();
	wrongAnswerCount = new HashMap<Integer, Double>();
	correctAnswerCount = new HashMap<Integer, Double>();
	loadingDialog = new ProgressDialog(getActivity());
	bindEvents();
	return v;
    }

    /**
     * Bind events.
     */
    private void bindEvents(){
	submit.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if(examResult==null ||examResult.getExam()==null || examResult.getExam().getQuestions()==null) {
		    Toast.makeText(getActivity(), "Please select the student in order to proceed",Toast.LENGTH_LONG).show();
		 //   marks.setVisibility(View.INVISIBLE);
		}else {
		    for(int i=0; i<examResult.getExam().getQuestions().size(); i++){
			Logger.error(tag, "RESULT - marks : "+examResult.getExam().getQuestions().get(i).getMarksAwarded());
		    }
		    if(!submitValidation()){
			final String url = serverRequests.getRequestURL(ServerRequests.EXAM_EVALUATION_SUBMIT, "");
			try {
			    if (AppStatus.getInstance(getActivity().getApplicationContext()).isOnline(
				    getActivity().getApplicationContext())) {
				submit.setClickable(false);
				submitEvaluatedPaper(url);
				//marks.setVisibility(View.GONE);
			    }else{
				Toast.makeText(getActivity(), R.string.check_internet_connection, 10000).show();
			    }
			} catch (final JsonGenerationException e) {
			    Logger.error(tag, e);
			} catch (final JsonMappingException e) {
			    Logger.error(tag, e);
			} catch (final IOException e) {
			    Logger.error(tag, e);
			}					
		    }
		}
	    }
	});
    }
    

    /**
     * Data from fragment.
     *
     * @param studentId the student id
     * @param examId the exam id
     * @param name the name
     * @param newFlag the new flag
     * @param studentCount the student count
     */
    public void dataFromFragment(String studentId, String examId, String name,boolean newFlag, int studentCount){
	this.studentId = studentId;
	this.examId = examId;
	this.name = name;
	this.newFlag=newFlag;
	this.studentCount = studentCount;
	progressBar = new ProgressDialog(v.getContext());
	progressBar.setCancelable(true);
	progressBar.setMessage("Downloading tests please wait ...");
	progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	progressBar.setProgress(0);
	progressBar.setMax(100);
	progressBar.show();
	if (AppStatus.getInstance(getActivity().getApplicationContext()).isOnline(
		getActivity())) {
	    final String url = serverRequests.getRequestURL(ServerRequests.EXAM_EVALUATION_FOR_STUDENT, "");
	    downloadExamForStudent(url);
	}else{
	    Toast.makeText(getActivity(), R.string.check_internet_connection, 10000).show();
	}
    }

    /**
     * Download exam for student.
     *
     * @param url the url
     */
    private void downloadExamForStudent(String url){

	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	final StringParameter sp1 = new StringParameter("testId", examId);
	final StringParameter sp2 = new StringParameter("studentId", studentId);
	params.add(sp1);
	params.add(sp2);

	Logger.warn(tag, "url for getting exam is:"+url +" with params: testId"+examId 
		+" and studentId: "+studentId);
	final PostRequestHandler post = new PostRequestHandler(url, params, new DownloadCallback() {

	    @Override
	    public void onSuccess(String downloadedString) {
		final Object data = ExamEvaluationParser.parseEvaluation(downloadedString);
		handler.post(new Runnable() {
		    @Override
		    public void run() {
			getExamForEvaluation(data);						
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
	post.request();
    }

    /**
     * Gets the exam for evaluation.
     *
     * @param data the data
     * @return the exam for evaluation
     */
    private void getExamForEvaluation(Object data){
	if(newFlag) {
	    correctAnswerMarksMap = new HashMap<Integer, Double>();
	    wrongAnswerCount = new HashMap<Integer, Double>();
	    correctAnswerCount = new HashMap<Integer, Double>();
	}
	String examResultData = null;
	if(data != null && data != "")
	    examResultData = data.toString();
	Logger.warn(tag, "exam result object is:"+examResultData);
	if(examResultData != null && examResultData != ""){
	    examResult = ExamResultParser.getTeacherExamResult(examResultData);
	    if(studentId != null && studentId != ""){
		examResult.setStudentId(studentId);
		totalNoOfQuestions = examResult.getExam().getQuestions().size();				
	    }
	}
	updateStudentData();
	frame1.setVisibility(View.VISIBLE);
	frame2.setVisibility(View.INVISIBLE);
	//marks.setVisibility(View.VISIBLE);
	updateUI("");
	progressBar.dismiss();
    }

    /**
     * Update student data.
     */
    private void updateStudentData(){
	linearLayout8.setVisibility(View.VISIBLE);
	relativeLayout2.setVisibility(View.VISIBLE);
	studentName.setText(name);
	section.setText(((ServerExamDetails)examResult.getExam().getExamDetails()).getSection());
	grade.setText(((ServerExamDetails)examResult.getExam().getExamDetails()).getGrade());
	examTitle.setText(examResult.getExam().getExamDetails().getTitle());
	maxMarks.setText(examResult.getExam().getExamDetails().getMaxPoints()+"");
	subject.setText(examResult.getExam().getExamDetails().getSubject());
	final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"EEE, d MMM yyyy");
	final String startdate = dateFormatter
		.format(((ServerExamDetails)examResult.getExam().getExamDetails()).getStartDate());
	final int startDateIndex = startdate.indexOf(" ");
	examDate.setText(startdate.substring(startDateIndex,
		startdate.length()).trim());
	automatedCorrectAnswerCount.setText(examResult.getCorrectAnsweredCount()+"");
	automatedTotalMarks.setText(examResult.getCorrectAnsTotalMarks()+"");
	automatedTotalQuestions.setText(examResult.getAutomatedTotalQuestions()+"");
	automatedWrongAnswerCount.setText(examResult.getWrongAnsweredCount()+"");
	correctMarksView.setText(0+"");
	correctCountView.setText(0+"");
	wrongAnswerCountView.setText(0+"");
	grandTotal.setText(0+"");
    }

    /**
     * Update ui.
     *
     * @param type the type
     */
    private void updateUI(String type){
	if(examResult != null && examResult.getExam() != null){
	    try {
		com.pearl.user.teacher.examination.Question question = null;
		boolean flag = true;
		if(questionNo <= totalNoOfQuestions -1 && questionNo >= 0)
		    question = examResult.getExam().getQuestionWithNumber(questionNo);
		if(question != null){
		    while(flag){
			if(question.getType().equalsIgnoreCase(Question.TRUE_OR_FALSE_QUESTION) ||
				question.getType().equalsIgnoreCase(Question.MULTIPLECHOICE_QUESTION) ||
				question.getType().equalsIgnoreCase(Question.MATCH_THE_FOLLOWING) ||
				question.getType().equalsIgnoreCase(Question.ORDERING_QUESTION) && questionNo >= 0){
			    if(type.equalsIgnoreCase(NEXT_QUESTION) || type.equalsIgnoreCase(""))
				questionNo++;
			    else if(type.equalsIgnoreCase(PREVIOUS_QUESTION)
				    && questionNo > 0){
				questionNo--;								
			    }
			    if(questionNo < totalNoOfQuestions && questionNo > 0)
				question = examResult.getExam().getQuestionWithNumber(questionNo);
			    else{
				Logger.warn(tag, "WHILE - reached to the last question and the last question is not under" +
					"manual evaluation");
				break;				
			    }
			}
			else{
			    flag = false;
			}
		    }	
		}
		if(!flag){
		    if(question != null){
			clearLayout(evaluationLayout);
			updateEvaluationLayout(question, evaluationLayout, correctMarksView);
		    }
		    else
			Logger.warn(tag, "flag is true");
		}
	    } catch (final QuestionDoesNotExistsException e) {
		Logger.error(tag, e);
	    }
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
     * Update evaluation layout.
     *
     * @param question the question
     * @param answerLayout the answer layout
     * @param manualCorrectMarks the manual correct marks
     */
    private void updateEvaluationLayout(com.pearl.user.teacher.examination.Question question,
	    LinearLayout answerLayout, TextView manualCorrectMarks) {
    	if(marks != null){
    		rootLayout.removeViewInLayout(marks);
    	}
	marks = new EditText(getActivity());
	marks.setHint("Marks");
	marks.setTextColor(Color.BLACK);
	setMarks(question);
	/*marks.setHeight(35);
	marks.setWidth(75);
*/	marks.setGravity(Gravity.LEFT);
	marks.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,1)});
	final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
		100,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	params.setMargins(590, 155, 60, 0);
	/*params.addRule(RelativeLayout.BELOW, R.id.text_view);
	params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.questions_eval);*/
	final ExamEvaluationUI ui = ExamEvaluationUI.getInstance();
	ui.build(question, getActivity(), answerLayout, manualCorrectMarks);

	rootLayout.addView(marks, params);
    }

    /**
     * Sets the marks.
     *
     * @param q the new marks
     */
    private void setMarks(final com.pearl.user.teacher.examination.Question q){
	if(q.isMarksAllotedByTeacher()){
	    final Double d = q.getMarksAwarded();
	    final int index = d.toString().lastIndexOf(".");
	    if(index > -1 && d.toString().endsWith("0")){
		marks.setText(d.toString().substring(0,  index));
	    }else
		marks.setText(q.getMarksAwarded()+"");			
	}
	else
	    marks.setText("");
	marks.setTextColor(Color.BLACK);
	marks.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
	marks.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {/*

				boolean flag = false;
				Logger.info("Questions UI", "MARKS UI- marks awarded "
						+ marks.getText().toString());
				if(marks.getText().toString().equalsIgnoreCase("")){
					q.setMarksAwarded(0.0);
				}
				if(!marks.getText().toString().equalsIgnoreCase("") 
						&& !marks.getText().toString().equals(null)){
					String m1 = marks.getText().toString();
					if(marks.getText().toString().equalsIgnoreCase(".")){
						m1 = "0"+m1;
					}
					double marksAwarded = Double.parseDouble(m1);
					flag = checkForBoundaryConditions(q.getMarksAlloted(), marksAwarded, q, marks);
					q.setMarksAllotedByTeacher(true);
					if(flag){
						marks.setText("");
					}else{
						getMarksAnalysis(q);
						if(validateMarks(marks.getText().toString())){
							marks.setError("Only 1 and 5 digits are allowed after decimal");
							marks.setText("");

						}
					}
				}else{
					q.setMarksAllotedByTeacher(false);
				}

	     */}

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void afterTextChanged(Editable s) {


		boolean flag = false;
		Logger.info("Questions UI", "MARKS UI- marks awarded "
			+ marks.getText().toString());
		if(marks.getText().toString().equalsIgnoreCase("")){
		    q.setMarksAwarded(0.0);
		}
		if(!marks.getText().toString().equalsIgnoreCase("") 
			&& !marks.getText().toString().equals(null)){
		    String m1 = marks.getText().toString();
		    if(marks.getText().toString().equalsIgnoreCase(".")){
			m1 = "0"+m1;
		    }
		    final double marksAwarded = Double.parseDouble(m1);
		    flag = checkForBoundaryConditions(q.getMarksAlloted(), marksAwarded, q, marks);
		    q.setMarksAllotedByTeacher(true);
		    if(flag){
			marks.setText("");
		    }else{
			getMarksAnalysis(q);
			if(validateMarks(marks.getText().toString())){
			    marks.setError("Only 1 and 5 digits are allowed after decimal");
			    marks.setText("");

			}
		    }
		}else{
		    q.setMarksAllotedByTeacher(false);
		}



	    }
	});
    }

    /**
     * Validate marks.
     *
     * @param marks the marks
     * @return true, if successful
     */
    private boolean validateMarks(String marks){
	final int index = marks.lastIndexOf(".");
	if(index >= 0){
	    if(!marks.substring(index+1).equalsIgnoreCase("") && !marks.substring(index+1).equals(null)){
		final int num = Integer.valueOf(marks.substring(index+1));
		if(num>0 && num<=4 || num>5 && num <=9){
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * The Class DecimalDigitsInputFilter.
     */
    public class DecimalDigitsInputFilter implements InputFilter {

	/** The m pattern. */
	Pattern mPattern;

	/**
	 * Instantiates a new decimal digits input filter.
	 *
	 * @param digitsBeforeZero the digits before zero
	 * @param digitsAfterZero the digits after zero
	 */
	public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
	    mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," 
		    + (digitsAfterZero-1) + "})?)||(\\.)?");
	}

	/* (non-Javadoc)
	 * @see android.text.InputFilter#filter(java.lang.CharSequence, int, int, android.text.Spanned, int, int)
	 */
	@Override
	public CharSequence filter(CharSequence source, int start, int end,
		Spanned dest, int dstart, int dend) {
	    final Matcher matcher=mPattern.matcher(dest);       
	    if(!matcher.matches())
		return "";
	    return null;
	}
    }

    /**
     * Check for boundary conditions.
     *
     * @param maxMarks the max marks
     * @param marksGiven the marks given
     * @param question the question
     * @param marksView the marks view
     * @return true, if successful
     */
    private boolean checkForBoundaryConditions(int maxMarks, double marksGiven, 
	    com.pearl.user.teacher.examination.Question question, EditText marksView){
	final double minMarks = 0.0;
	boolean flag = false;
	if(marksGiven < minMarks || marksGiven > maxMarks){
	    marksView.setError("Please specify marks between "+minMarks+" and "+maxMarks);
	    flag = true;
	} else if(marksGiven >= minMarks && marksGiven <= maxMarks){
	    question.setMarksAwarded(marksGiven);
	    flag = false;
	}
	return flag;
    }

    /**
     * Gets the marks analysis.
     *
     * @param question the question
     * @return the marks analysis
     */
    private void getMarksAnalysis(Question question){
	if(question != null){
	    double totalCorrectAnswerMarks = 0;
	    if(!question.getType().equalsIgnoreCase(Question.TRUE_OR_FALSE_QUESTION) ||
		    !question.getType().equalsIgnoreCase(Question.MULTIPLECHOICE_QUESTION) ||
		    !question.getType().equalsIgnoreCase(Question.MATCH_THE_FOLLOWING) ||
		    !question.getType().equalsIgnoreCase(Question.ORDERING_QUESTION)){
		if(question.getMarksAwarded() >= 0.0 || question.isMarksAllotedByTeacher()){
		    correctAnswerMarksMap.put(question.getQuestionOrderNo(), question.getMarksAwarded());
		    for(final Map.Entry<Integer, Double> entry : correctAnswerMarksMap.entrySet()){
			final int position = entry.getKey();
			final Double marks = entry.getValue();
			totalCorrectAnswerMarks += marks;
			if(marks == 0.0 && question.isMarksAllotedByTeacher()){
			    wrongAnswerCount.put(position, marks);
			    correctAnswerCount.remove(position);
			} if(marks > 0.0){
			    correctAnswerCount.put(position, marks);
			    wrongAnswerCount.remove(position);
			}
		    }
		}else{
		    Logger.error(tag, "MARKS - teacher dint give marks");
		}
		updateMarksCount(totalCorrectAnswerMarks);
	    }
	}
	else
	    Logger.warn(tag, "MARKS - cant get marks as questions is null");
    }

    /**
     * Update marks count.
     *
     * @param totalManualCorrectMarks the total manual correct marks
     */
    private void updateMarksCount(double totalManualCorrectMarks){
	correctMarksView.setText(totalManualCorrectMarks+"");
	final double total = totalManualCorrectMarks + examResult.getCorrectAnsTotalMarks();
	grandTotal.setText(total+"");
	correctCountView.setText(correctAnswerCount.size()+"");
	wrongAnswerCountView.setText(wrongAnswerCount.size()+"");
	examResult.setGrandTotalMarks(total);
	examResult.setManualCorrectAnsweredCount(correctAnswerCount.size());
	examResult.setManualWrongAnsweredCount(wrongAnswerCount.size());
	examResult.setManualCorrectAnsTotalMarks(totalManualCorrectMarks);
    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#moveToNextPage()
     */
    @Override
    public void moveToNextPage() {
	if (questionNo <= totalNoOfQuestions - 1) {
	    questionNo++;
	    updateUI(NEXT_QUESTION);
	} else {
	    if(totalNoOfQuestions > 0)
		Toast.makeText(getActivity(), "You are on last question", 10000)
		.show();
	}
    }

    /* (non-Javadoc)
     * @see com.pearl.book.guesturehandlers.GestureActionHandler#moveToPreviousPage()
     */
    @Override
    public void moveToPreviousPage() {
	if(questionNo > 0){
	    questionNo--;
	    updateUI(PREVIOUS_QUESTION);
	}else{
	    if(totalNoOfQuestions > 0)
		Toast.makeText(getActivity(),R.string.first_question, 10000)
		.show();
	}
    }

    /**
     * Submit evaluated paper.
     *
     * @param url the url
     * @throws JsonGenerationException the json generation exception
     * @throws JsonMappingException the json mapping exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void submitEvaluatedPaper(String url) throws JsonGenerationException, JsonMappingException, IOException{
	final ObjectMapper mapper = new ObjectMapper();
	final BaseRequest baseRequest = new BaseRequest();
	baseRequest.setData(mapper.writeValueAsString(examResult));
	final String requestString = mapper.writeValueAsString(baseRequest);
	Logger.warn(tag, "final request String for submitting is:"+requestString);
	ApplicationData.writeToFile(requestString, appData.getAppTempPath()+"evaluatedString.txt");
	final StringParameter sp =  new StringParameter("postdata", requestString);
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(sp);
	loadingDialog.setTitle("Please wait..");
	loadingDialog
	.setMessage("Uploading evaluated sheet...");
	loadingDialog.show();
	final PostRequestHandler post = new PostRequestHandler(url, params, new DownloadCallback() {

	    @Override
	    public void onSuccess(final String downloadedString) {
		Logger.warn(tag, "Response after submitting is:"+downloadedString);
		handler.post(new Runnable() {

		    @Override
		    public void run() {
			loadingDialog.hide();
			correctMarksView.setText(0+"");
			correctCountView.setText(0+"");
			wrongAnswerCountView.setText(0+"");
			grandTotal.setText(0+"");
			//marks.setVisibility(View.GONE);
			//evaluationLayout.setVisibility(View.GONE);
			//rootLayout.refreshDrawableState();
			frame1.setVisibility(View.INVISIBLE);
			frame2.setVisibility(View.VISIBLE);
			relativeLayout2.setVisibility(View.GONE);
			marks.setVisibility(View.GONE);
			//marks.setVisibility(View.GONE);
			automatedCorrectAnswerCount.setText(0+"");
			automatedTotalMarks.setText(0+"");
			automatedTotalQuestions.setText(0+"");
			automatedWrongAnswerCount.setText(0+"");
			questionNo = 0;
			
			submit.setClickable(true);
			Intent i = null;
			if((studentCount -1) > 0){
				EvaluationStudentListFragment fragment = new EvaluationStudentListFragment();
				((EvaluationStudentListFragment) getFragmentManager().findFragmentById(
						R.id.studentListFragment)).download(examId , getActivity());
			}else{
				i=new Intent(getActivity(),AwaitingEvaluationListActivity.class);
				startActivity(i);
				getActivity().finish();
			}
		    }
		});
	    } 

	    @Override
	    public void onProgressUpdate(int progressPercentage) {
		Logger.warn(tag, "File uplaod in progress");
	    }

	    @Override
	    public void onFailure(String failureMessage) {
		Logger.error(tag, "File dint upload");
		handler.post(new Runnable() {

		    @Override
		    public void run() {
			loadingDialog.hide();		
		    }
		});
	    }
	});
	post.request();
    }

    /**
     * Submit validation.
     *
     * @return true, if successful
     */
    private boolean submitValidation(){
	if(examResult != null && examResult.getExam() != null){
	    final List<Question> questionsList = examResult.getExam().getQuestions();
	    if(questionsList != null){
		final Iterator<Question> iterator = questionsList.iterator();
		while(iterator.hasNext()){
		    final Question ques = iterator.next();
		    if(ques.getType().equalsIgnoreCase(Question.TRUE_OR_FALSE_QUESTION) ||
			    ques.getType().equalsIgnoreCase(Question.MULTIPLECHOICE_QUESTION) ||
			    ques.getType().equalsIgnoreCase(Question.MATCH_THE_FOLLOWING) ||
			    ques.getType().equalsIgnoreCase(Question.ORDERING_QUESTION)){

		    }else{
			if(ques.getMarksAwarded() == 0.0 && !ques.isMarksAllotedByTeacher()){
			    final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			    dialog.setTitle("Alert");
			    dialog.setMessage("Marks should not be empty");
			    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
				    dialog.dismiss();
				}
			    });
			    dialog.show();
			    return true;
			}						
		    }
		}					
	    }
	}
	return false;
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
     * The Class Holder.
     */
    class Holder{
    	
	    /** The marks view. */
	    EditText marksView;
    }
}
