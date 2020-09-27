package com.pearl.ui.helpers.examination;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.logger.Logger;
import com.pearl.user.teacher.examination.Question;
import com.pearl.user.teacher.examination.questiontype.FillInTheBlankQuestion;


// TODO: Auto-generated Javadoc
/**
 * The Class ExamEvaluationUI.
 */
public class ExamEvaluationUI {

    /** The evaluation ui. */
    private static ExamEvaluationUI evaluationUI = null;
    
    /** The Constant tag. */
    private static final String tag = "EVALUATIONUI";
    
    /** The manual correct marks. */
    private TextView manualCorrectMarks;

    /**
     * Instantiates a new exam evaluation ui.
     */
    private ExamEvaluationUI() {
    }

    /**
     * Gets the single instance of ExamEvaluationUI.
     *
     * @return single instance of ExamEvaluationUI
     */
    public static ExamEvaluationUI getInstance() {
	if (evaluationUI == null) {
	    evaluationUI = new ExamEvaluationUI();
	}

	return evaluationUI;
    }

    /**
     * Builds the.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @param manualCorrectMarks the manual correct marks
     * @return the linear layout
     */
    public LinearLayout build(Question question, FragmentActivity activity,
	    LinearLayout layout, TextView manualCorrectMarks) {
	this.manualCorrectMarks = manualCorrectMarks;

	question.setViewed(true);

	Logger.info("ANSWERRESULTSUI", "ANSWER - Building question Type "
		+ question.getType());

	if (Question.SHORT_ANSWER_QUESTION.equalsIgnoreCase(question.getType()
		.trim())) {
	    return buildShortAnswerQuestion(question, activity, layout);
	} else if (Question.LONG_ANSWER_QUESTION.equalsIgnoreCase(question.getType()
		.trim())) {
	    return buildLongAnswerQuestion(question, activity, layout);
	} else if (Question.FILL_IN_THE_BLANK_QUESTION.equalsIgnoreCase(question
		.getType().trim())) {
	    return buildFillInTheBlanksQuestion(question, activity, layout);
	} else {
	    Logger.error("QuestionsUI", "***Failed to Build question Type "
		    + question.getType());
	}
	return null;
    }

    /**
     * Builds the short answer question.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout buildShortAnswerQuestion(final Question question,
	    final FragmentActivity activity, LinearLayout layout) {
	int id = 1;
	double scrnSize= screenSizes(activity);
	
	final EditText teacherComments = new EditText(activity);
	teacherComments.setId(id++);
	teacherComments.setHint("Your comment here....");
	teacherComments.setTextColor(Color.parseColor("#000000"));
	teacherComments.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});

	final TextView studentAnswerTextView = new TextView(activity);
	studentAnswerTextView.setId(id++);
	studentAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	studentAnswerTextView.setText("Student Answer :");
	
	final TextView teacherAnswerTextView = new TextView(activity);
	teacherAnswerTextView.setId(id++);
	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	teacherAnswerTextView.setText("Teacher Answer :");

	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	}

	layout.setOrientation(LinearLayout.VERTICAL);

	new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.FILL_PARENT);

	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

	final LinearLayout.LayoutParams answerParams;
	// answerParams.height = 100;

	final TextView studentAnswer = new TextView(activity);
	studentAnswer.setId(id++);
	final TextView teacherAnswer = new TextView(activity);
	teacherAnswer.setId(id++);


	if(question != null){
		if (question.getCorrectAnswer() != null) {
			teacherAnswer.setText(question.getCorrectAnswer()
				.getAnswerString());
		    } else
			teacherAnswer.setText("Answer is empty");
		    teacherAnswer.setTextColor(Color.parseColor("#000000"));
	    if (question.getStudentAnswer() != null) {
		if (!question.getStudentAnswer().getAnswerString().isEmpty()) {
		    studentAnswer.setText(question.getStudentAnswer()
			    .getAnswerString());
		} else {
		    studentAnswer.setText(" Student did not attempt the question");
		}
	    }
	}
	studentAnswer.setTextColor(Color.parseColor("#000000"));
	Logger.warn(tag, "teacher description is: "+question.getDescription());
	if(question.getDescription() != null && question.getDescription() != "")
	    teacherComments.setText(question.getDescription().toString());
	teacherComments.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	    	if(s.length() > 100)
	    		Toast.makeText(activity, "your comments can't exceed 100 characters", Toast.LENGTH_LONG ).show();
	    	else
	    		question.setDescription(teacherComments.getText().toString());
	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	    }
	});
	
	if(scrnSize >8){
		defaultParams.setMargins(40, 10, 30, 10);
		answerParams = new LinearLayout.LayoutParams(550,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		answerParams.setMargins(45, 10, 30, 10);
		
	}else if(scrnSize <= 8 && scrnSize > 5){
		defaultParams.setMargins(30, 10, 20, 10);
		answerParams = new LinearLayout.LayoutParams(450,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		answerParams.setMargins(25, 10, 20, 10);
		teacherAnswerTextView.setTextSize(16);
		studentAnswerTextView.setTextSize(16);
		teacherAnswer.setTextSize(16);
		teacherComments.setTextSize(16);
		studentAnswer.setTextSize(16);
	}else{
		defaultParams.setMargins(20, 10, 10, 10);
		answerParams = new LinearLayout.LayoutParams(300,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		answerParams.setMargins(25, 10, 10, 10);
		teacherAnswerTextView.setTextSize(12);
		studentAnswerTextView.setTextSize(12);
		teacherAnswer.setTextSize(12);
		teacherComments.setTextSize(12);
		studentAnswer.setTextSize(12);
	}

	// Add to layout
	teacherAnswer.setMovementMethod(new ScrollingMovementMethod());
	teacherAnswer.setMaxLines(3);
	studentAnswer.setMovementMethod(new ScrollingMovementMethod());
	studentAnswer.setMaxLines(3);
	layout.addView(getQuestionView(question, activity));
	layout.addView(studentAnswerTextView, answerParams);
	layout.addView(studentAnswer, answerParams);
	layout.addView(teacherAnswerTextView, answerParams);
	layout.addView(teacherAnswer,answerParams);
	layout.addView(teacherComments, answerParams);

	// Return Layout
	return layout;
    }

    /**
     * Builds the long answer question.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout buildLongAnswerQuestion(final Question question,
	    final FragmentActivity activity, LinearLayout layout) {
    	
    	double scrnSize =screenSizes(activity);
	int id = 1;
	final EditText teacherComment = new EditText(activity);
	teacherComment.setId(id++);
	teacherComment.setHint("Your comment here.....");
	teacherComment.setTextColor(Color.parseColor("#000000"));
	teacherComment.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
	
	final TextView studentAnswerTextView = new TextView(activity);
	studentAnswerTextView.setId(id++);
	studentAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	studentAnswerTextView.setText("Student Answer :");
	
	final TextView TeacherAnswerTextView = new TextView(activity);
	TeacherAnswerTextView.setId(id++);
	TeacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	TeacherAnswerTextView.setText("Teacher Answer :");

	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	}

	layout.setOrientation(LinearLayout.VERTICAL);

	new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.FILL_PARENT);

	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

	final LinearLayout.LayoutParams answerParams ;
	// answerParams.height = 100;

	final TextView studentAnswer = new TextView(activity);
	studentAnswer.setId(id++);
	final TextView teacherAnswer = new TextView(activity);
	teacherAnswer.setId(id++);


	if(question != null){
		 if (question.getCorrectAnswer() != null) {
				teacherAnswer.setText(question.getCorrectAnswer()
					.getAnswerString());
			    } else
				teacherAnswer.setText("Answer is empty");
			    teacherAnswer.setTextColor(Color.parseColor("#000000"));
	    if (question.getStudentAnswer() != null) {
		if (!question.getStudentAnswer().getAnswerString().isEmpty()) {
		    studentAnswer.setText(question.getStudentAnswer()
			    .getAnswerString());
		} else {
		    studentAnswer.setText(" Student did not attempt the question");
		}
	    }
	}
	studentAnswer.setTextColor(Color.parseColor("#000000"));
	Logger.warn(tag, "teacher description is:"+question.getDescription());
	if(question.getDescription() != null && question.getDescription() != "")
	    teacherComment.setText(question.getDescription().toString());
	teacherComment.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	    	if(s.length() > 100)
	    		Toast.makeText(activity, "your comments can't exceed 100 characters", Toast.LENGTH_LONG ).show();
	    	else
	    		question.setDescription(teacherComment.getText().toString());
	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	    }
	});
	
	if(scrnSize >8){
		defaultParams.setMargins(40, 10, 30, 10);
		answerParams = new LinearLayout.LayoutParams(550,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		answerParams.setMargins(45, 10, 30, 10);
		
	}else if(scrnSize <= 8 && scrnSize > 5){
		defaultParams.setMargins(30, 10, 20, 10);
		answerParams = new LinearLayout.LayoutParams(450,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		answerParams.setMargins(25, 10, 20, 10);
		TeacherAnswerTextView.setTextSize(16);
		studentAnswerTextView.setTextSize(16);
		teacherAnswer.setTextSize(16);
		teacherComment.setTextSize(16);
		studentAnswer.setTextSize(16);
	}else{
		defaultParams.setMargins(20, 10, 10, 10);
		answerParams = new LinearLayout.LayoutParams(300,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		answerParams.setMargins(25, 10, 10, 10);
		TeacherAnswerTextView.setTextSize(12);
		studentAnswerTextView.setTextSize(12);
		teacherAnswer.setTextSize(12);
		teacherComment.setTextSize(12);
		studentAnswer.setTextSize(12);
	}

	// Add to layout
	teacherAnswer.setMovementMethod(new ScrollingMovementMethod());
	studentAnswer.setMovementMethod(new ScrollingMovementMethod());
	teacherAnswer.setMaxLines(3);
	studentAnswer.setMaxLines(3);
	layout.addView(getQuestionView(question, activity));
	layout.addView(studentAnswerTextView, answerParams);
	layout.addView(studentAnswer, answerParams);
	layout.addView(TeacherAnswerTextView,answerParams);
	layout.addView(teacherAnswer,answerParams);
	
	layout.addView(teacherComment, answerParams);

	return layout;
    }

    /**
     * Builds the fill in the blanks question.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout buildFillInTheBlanksQuestion(final Question question,
	    final FragmentActivity activity, LinearLayout layout) {
    	
    	double scrnSize = screenSizes(activity);
	int id = 1;
	String finalStudentAnswer = "";
	String finalTeacherAnswer = "";

	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	}

	layout.setOrientation(LinearLayout.VERTICAL);

	new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.FILL_PARENT);

	final LinearLayout.LayoutParams outerParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.FILL_PARENT);

	final LinearLayout innerLayout = new LinearLayout(activity);
	innerLayout.setOrientation(LinearLayout.VERTICAL);

	// Initialize default parameters
	final LinearLayout.LayoutParams inlineParams;	

	// layout.addView(getQuestionView(question, activity));
	final TextView teacherAnswerTextView = new TextView(activity);
	final TextView teacherAnswerText = new TextView(activity);
	final TextView studentAnswerTextView = new TextView(activity);
	final TextView studentAnswerText = new TextView(activity);
	final EditText teacherComments = new EditText(activity);

	teacherAnswerText.setId(id++);
	teacherAnswerText.setMaxLines(3);
	teacherAnswerText.setMovementMethod(new ScrollingMovementMethod());
	teacherAnswerTextView.setId(id++);
	studentAnswerText.setId(id++);
	studentAnswerText.setMaxLines(3);
	studentAnswerText.setMovementMethod(new ScrollingMovementMethod());
	studentAnswerTextView.setId(id++);
	teacherComments.setId(id++);

	teacherAnswerText.setTextColor(Color.parseColor("#000000"));
	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	studentAnswerText.setTextColor(Color.parseColor("#000000"));
	studentAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	teacherComments.setTextColor(Color.parseColor("#000000"));
	teacherComments.setHint("Your comments here...");
	teacherComments.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
	 final FillInTheBlankQuestion fib = (FillInTheBlankQuestion)question;
	if (question.getStudentAnswer() != null
		&& question.getStudentAnswer().getChoices() != null) {
	    //final String[] arr = new String[fib.getAnswers().size()];
	    final String[] teacherarr = new String[question.getStudentAnswer().getChoices().size()];
	    
	    List<String> finalList=new ArrayList<String>();
	    finalList.add("");
	    finalList.add(" ");
	    fib.getAnswers().removeAll(finalList);
	    
	    for(int i=0; i<fib.getAnswers().size();i++){
		if(fib.getAnswers().get(i) != null && fib.getAnswers().get(i).length() > 0){
			
		   if(i == fib.getAnswers().size() - 1)
			   finalStudentAnswer += i+1 + ". " + fib.getAnswers().get(i)+ ".";
		   else
			   finalStudentAnswer += i+1 + ". " + fib.getAnswers().get(i)+ ", ";
		}
	    }
	    if (finalStudentAnswer.endsWith(",")) {
		finalStudentAnswer = finalStudentAnswer.substring(0,
			finalStudentAnswer.length() - 1);

	    }try {
	    	
	    	if(fib.getAnswers().size() == 0){
	    		studentAnswerText.setText(" Student did not attempt the question");
	    	}else
	    		 studentAnswerText.setText(finalStudentAnswer);
	   /* for(int j=0;j<fib.getAnswers().size();j++){
		if(!fib.getAnswers().get(j).isEmpty()){
		    studentAnswerText.setText(finalStudentAnswer);
		}else{
		    studentAnswerText.setText(" Student did not attempt the question");
		}
	    }*/
	    }catch(Exception e) {
		Logger.error(getClass().getSimpleName(), e);
	    }
	}
	    for(int k=0; k<question.getStudentAnswer().getChoices().size(); k++){
	    	if(k <question.getStudentAnswer().getChoices().size() -1)
	    	     finalTeacherAnswer += k+1 + ". " + question.getStudentAnswer().getChoices().get(k).getTitle()+ ", ";
	    	else
	    		finalTeacherAnswer += k+1 + ". " + question.getStudentAnswer().getChoices().get(k).getTitle()+ ".";
	    }
	teacherAnswerTextView.setText("Teacher Answer :");
	teacherAnswerText.setText(finalTeacherAnswer);
	studentAnswerTextView.setText("Student Answer :");

	teacherComments.setText(question.getDescription().toString());
	teacherComments.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
	    	if(s.length() > 100)
	    		Toast.makeText(activity, "your comments can't exceed 100 characters", Toast.LENGTH_LONG ).show();
	    	else
	    		question.setDescription(teacherComments.getText().toString());
	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	    }
	});
	
	if(scrnSize >8){
		outerParams.setMargins(40, 10, 30, 10);
		inlineParams = new LinearLayout.LayoutParams(550,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		inlineParams.setMargins(5, 10, 30, 0);
		
	}else if(scrnSize <= 8 && scrnSize > 5){
		outerParams.setMargins(30, 10, 20, 10);
		inlineParams = new LinearLayout.LayoutParams(450,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		inlineParams.setMargins(5, 10, 20, 0);
		teacherAnswerTextView.setTextSize(16);
		studentAnswerTextView.setTextSize(16);
		teacherAnswerText.setTextSize(16);
		teacherComments.setTextSize(16);
		studentAnswerText.setTextSize(16);
	}else{
		outerParams.setMargins(20, 10, 10, 10);
		inlineParams = new LinearLayout.LayoutParams(300,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		inlineParams.setMargins(5, 10, 10, 0);
		teacherAnswerTextView.setTextSize(12);
		studentAnswerTextView.setTextSize(12);
		teacherAnswerText.setTextSize(12);
		teacherComments.setTextSize(12);
		studentAnswerText.setTextSize(12);
	}

	innerLayout.addView(studentAnswerTextView, inlineParams);
	innerLayout.addView(studentAnswerText, inlineParams);
	innerLayout.addView(teacherAnswerTextView, inlineParams);
	innerLayout.addView(teacherAnswerText, inlineParams);
	innerLayout.addView(teacherComments, inlineParams);
	layout.addView(getQuestionView(question, activity));
	layout.addView(innerLayout, outerParams);

	// Return Layout
	return layout;
    }

    /**
     * Gets the question view.
     *
     * @param q the q
     * @param activity the activity
     * @return the question view
     */
    private LinearLayout getQuestionView(final Question q, final Activity activity) {
    	
    	double screenSize= screenSizes(activity);
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

	final LinearLayout questionsLayout = new LinearLayout(activity);
	questionsLayout.setOrientation(LinearLayout.VERTICAL);

	// Initialize default parameters
	final LinearLayout.LayoutParams questionsParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	
	final LinearLayout.LayoutParams questionsParams1 = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	
	final TextView questionView = new TextView(activity);
	questionView
	.setText("Q"
		+ (q.getQuestionOrderNo() > 0 ? q
			.getQuestionOrderNo() : "") + ": "
			+ q.getQuestion());// +"      Marks Alloted : "+q.getMarksAlloted());
	questionView.setTextColor(Color.parseColor("#000000"));
	questionView.setId(0);
	questionView.setLayoutParams(defaultParams);
	questionView.setMovementMethod(new ScrollingMovementMethod());
	questionView.setMaxLines(3);

	final EditText maxPoints = new EditText(activity);
	maxPoints.setEnabled(false);
	maxPoints.setText("Marks Allotted :"+q.getMarksAlloted());
	
	if(screenSize >8){
		defaultParams.setMargins(40, 10, 20, 10);
		questionsParams.setMargins(10, 50, 15, 7);
		questionsParams1.setMargins(520, 0, 10, 0);
		questionView.setTextSize(20);
	}else if(screenSize <= 8 && screenSize > 5){
		defaultParams.setMargins(30, 5, 10, 5);
		questionsParams.setMargins(10, 30, 15, 7);
		questionsParams1.setMargins(420, 0, 5, 0);
		maxPoints.setTextSize(16);
		questionView.setTextSize(16);
	}else{
		defaultParams.setMargins(20, 5, 10, 5);
		questionsParams.setMargins(10, 15, 15, 7);
		questionsParams1.setMargins(320, 0, 5, 0);
		maxPoints.setTextSize(14);
		questionView.setTextSize(14);
	}


	questionView.setGravity(Gravity.LEFT);
	questionsLayout.addView(questionView, questionsParams);
	//questionsLayout.addView(marks, questionsParams);
	questionsLayout.addView(maxPoints, questionsParams1);

	return questionsLayout;
    }
    
    public Double screenSizes(Context activity){
    	DisplayMetrics dm = new DisplayMetrics();
	     ((Activity)activity).getWindowManager().getDefaultDisplay().getMetrics(dm);
	     double sx = Math.pow(dm.widthPixels/dm.xdpi,2);
	     double sy = Math.pow(dm.heightPixels/dm.ydpi,2);
	     final double screenInches = Math.sqrt(sx+sy);
	     return screenInches;

    }

}
