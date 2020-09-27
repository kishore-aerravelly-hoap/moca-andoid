package com.pearl.ui.helpers.examination;


import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.examination.AnswerChoice;
import com.pearl.examination.Question;
import com.pearl.examination.questiontype.MultipleChoiceQuestion;
import com.pearl.examination.questiontype.OrderingQuestion;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultsPublishAnswerSheet.
 */
public class ResultsPublishAnswerSheet
{
    
    /** The answer sheet ui. */
    private static ResultsPublishAnswerSheet answerSheetUI = null;
    
    /** The answer choice list. */
    private List<AnswerChoice> answerChoiceList;
    
    /** The choice. */
    private AnswerChoice choice;

    /**
     * Gets the single instance of ResultsPublishAnswerSheet.
     *
     * @return single instance of ResultsPublishAnswerSheet
     */
    public static ResultsPublishAnswerSheet getInstance()
    {
	if (answerSheetUI == null)
	    answerSheetUI = new ResultsPublishAnswerSheet();
	return answerSheetUI;
    }

    /**
     * Builds the.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout build(Question question, FragmentActivity activity,
	    LinearLayout layout) {

	question.setViewed(true);

	Logger.info("ANSWERRESULTSUI", "ANSWER - Building question Type "
		+ question.getType());

	if (Question.MULTIPLECHOICE_QUESTION.equals(question.getType().trim())
		|| Question.TRUE_OR_FALSE_QUESTION.equals(question.getType()
			.trim())) {
	    return buildTrueOrFalse(question, activity, layout);
	} else if (Question.SHORT_ANSWER_QUESTION.equals(question.getType()
		.trim())) {
	    return buildLongAnswerQuestion(question, activity, layout);
	} else if (Question.LONG_ANSWER_QUESTION.equals(question.getType()
		.trim())) {
	    return buildLongAnswerQuestion(question, activity, layout);
	} else if (Question.FILL_IN_THE_BLANK_QUESTION.equals(question
		.getType().trim())) {
	    return buildFillInTheBlanksQuestion(question, activity, layout);
	} else if (Question.ORDERING_QUESTION.equals(question.getType().trim())) {
	    return buildOrderingQuestion(question, activity, layout);
	} else if (Question.MATCH_THE_FOLLOWING.equals(question.getType()
		.trim())) {
	    return buildMatchtheFollowing(question, activity, layout);
	} else {
	    Logger.error("QuestionsUI", "***Failed to Build question Type "
		    + question.getType());
	}

	return null;
    }

    /**
     * TODO add listeners for your button as well, in the listener you would
     * call activity.prev() or activity.next()
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout buildShortAnswerQuestion(final Question question,
	    final FragmentActivity activity, LinearLayout layout) {
    	double scrnSize = screenSize(activity);
	int id = 1;
	final TextView teacherAnswerTextView = new TextView(activity);
	teacherAnswerTextView.setId(id++);
	teacherAnswerTextView.setText("Teacher Answer:       Teacher allotted marks :"+question.getMarksAlloted());
	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));

	final TextView studentAnswerTextView = new TextView(activity);
	studentAnswerTextView.setId(id++);
	studentAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	studentAnswerTextView.setText("Student Answer:       Student awarded marks :"+question.getMarksAwarded());

	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    /*
	     * ayout.setLayoutParams(new
	     * android.widget.LinearLayout.LayoutParams(
	     * android.view.ViewGroup.LayoutParams.FILL_PARENT, 550));
	     * layout.setOrientation(LinearLayout.VERTICAL);
	     */
	}
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		50);
	defaultParams.setMargins(40, 5, 30, 0);

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		50);
	answerParams.setMargins(45, 5, 30, 0);
	// answerParams.height = 100;
	final LinearLayout.LayoutParams studentAnswerParams = new LinearLayout.LayoutParams(
			android.view.ViewGroup.LayoutParams.FILL_PARENT,
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	studentAnswerParams.setMargins(45, 5, 30, 0);
	

	final TextView teacherAnswer = new TextView(activity);
	teacherAnswer.setId(id++);
	teacherAnswer.setTextSize(16);
	
	final TextView teacherCommentView = new TextView(activity);
	teacherCommentView.setId(id++);
	studentAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	teacherCommentView.setText("Teacher Comment: ");
	teacherCommentView.setTextColor(Color.parseColor("#A52A2A"));

	final TextView studentAnswer = new TextView(activity);
	studentAnswer.setId(id++);
	final TextView teacherComment = new TextView(activity);
	teacherComment.setId(id++);
	
	if (question != null) {
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
	    studentAnswer.setTextColor(Color.parseColor("#000000"));
	    if(question.getDescription() != null){
			teacherComment.setText(question.getDescription());
		}
	    else{
	    	teacherComment.setText("No comments");
	    }
	}
	
	if(scrnSize >8){
		teacherAnswer.setMaxLines(8);
		studentAnswer.setMaxLines(6);
	}else if(scrnSize <= 8 && scrnSize > 5){
		teacherAnswerTextView.setTextSize(16);
		studentAnswerTextView.setTextSize(16);
		teacherAnswer.setMaxLines(6);
		studentAnswer.setMaxLines(4);
		teacherAnswer.setTextSize(16);
		teacherComment.setTextSize(16);
		studentAnswer.setTextSize(16);
	}else{
		teacherAnswer.setMaxLines(4);
		studentAnswer.setMaxLines(3);
		teacherAnswerTextView.setTextSize(12);
		studentAnswerTextView.setTextSize(12);
		teacherAnswer.setTextSize(12);
		teacherComment.setTextSize(12);
		studentAnswer.setTextSize(12);
	}
	
	teacherAnswer.setMovementMethod(new ScrollingMovementMethod());
	
	studentAnswer.setMovementMethod(new ScrollingMovementMethod());
	
	studentAnswer.setTextSize(16);
	teacherComment.setTextSize(16);
	// Add to layout
	layout.addView(getQuestionView(question, activity));
	layout.addView(teacherAnswerTextView, answerParams);
	layout.addView(teacherAnswer, answerParams);
	layout.addView(studentAnswerTextView, answerParams);
	layout.addView(studentAnswer, studentAnswerParams);
	layout.addView(teacherCommentView, answerParams);
	layout.addView(teacherComment, answerParams);

	// Return Layout
	return layout;
    }

    /**
     * TODO add listeners for your button as well, in the listener you would
     * call activity.prev() or activity.next()
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout buildLongAnswerQuestion(final Question question,
	    final FragmentActivity activity, LinearLayout layout) {
    	
    	double scrnSize =screenSize(activity);
	int id = 1;
	final TextView teacherAnswerTextView = new TextView(activity);
	teacherAnswerTextView.setId(id++);
	teacherAnswerTextView.setText("Teacher Answer:       Teacher allotted marks :"+question.getMarksAlloted());
	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));

	final TextView studentAnswerTextView = new TextView(activity);
	studentAnswerTextView.setId(id++);
	studentAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	studentAnswerTextView.setText("Student Answer:      Student awarded marks :"+question.getMarksAwarded());

	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    /*
	     * layout.setLayoutParams(new
	     * android.widget.LinearLayout.LayoutParams(
	     * android.view.ViewGroup.LayoutParams.FILL_PARENT, 550));
	     * layout.setOrientation(LinearLayout.VERTICAL);
	     */
	}
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		40);
	defaultParams.setMargins(40, 5, 30, 0);

	final LinearLayout.LayoutParams answerParams;
	
	final LinearLayout.LayoutParams studentAnswerParams = new LinearLayout.LayoutParams(
			android.view.ViewGroup.LayoutParams.FILL_PARENT,
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	studentAnswerParams.setMargins(45, 5, 30, 0);
	// answerParams.height = 100;

	final TextView teacherAnswer = new TextView(activity);
	teacherAnswer.setId(id++);
	teacherAnswer.setTextSize(16);

	final TextView studentAnswer = new TextView(activity);
	studentAnswer.setId(id++);
	studentAnswer.setTextSize(16);
	
	final TextView teacherComment = new TextView(activity);
	teacherComment.setId(id++);
	teacherComment.setTextSize(16);
	
	if(question != null && question.getDescription() != null){
		if(question.getDescription().trim().length() != 0)
			teacherComment.setText(question.getDescription());
		else
			teacherComment.setText("No Comments");
	}else
		teacherComment.setText("No Comments");
	
	final TextView teacherCommentView = new TextView(activity);
	teacherCommentView.setId(id++);
	teacherCommentView.setText("Teacher Comment:");
	teacherCommentView.setTextColor(Color.parseColor("#A52A2A"));

	if (question != null) {
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
	    studentAnswer.setTextColor(Color.parseColor("#000000"));
	}
	
	if(scrnSize >8){
		 answerParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,40);
		 answerParams.setMargins(45, 5, 30, 0);
		
	}else if(scrnSize <= 8 && scrnSize > 5){
		 answerParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,40);
		 answerParams.setMargins(45, 5, 30, 0);
		 teacherAnswerTextView.setTextSize(16);
		 studentAnswerTextView.setTextSize(16);
		 teacherAnswer.setTextSize(16);
		 teacherComment.setTextSize(16);
		 studentAnswer.setTextSize(16);
	}else{
		 answerParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,40);
		 answerParams.setMargins(45, 5, 30, 0);
		 teacherAnswerTextView.setTextSize(12);
		 studentAnswerTextView.setTextSize(12);
		 teacherAnswer.setTextSize(12);
		 teacherComment.setTextSize(12);
		 studentAnswer.setTextSize(12);
	}

	// Add to layout
	teacherAnswer.setMovementMethod(new ScrollingMovementMethod());
	teacherAnswer.setMaxLines(8);
	studentAnswer.setMovementMethod(new ScrollingMovementMethod());
	studentAnswer.setMaxLines(6);
	layout.addView(getQuestionView(question, activity));
	layout.addView(teacherAnswerTextView, answerParams);
	layout.addView(teacherAnswer, answerParams);
	layout.addView(studentAnswerTextView, answerParams);
	layout.addView(studentAnswer, studentAnswerParams);
	layout.addView(teacherCommentView, answerParams);
	layout.addView(teacherComment, answerParams);

	/*
	 * answerEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	 * 
	 * @Override public void onFocusChange(View v, boolean hasFocus) {
	 * if(!hasFocus){ Answer ans = new Answer();
	 * ans.setAnswerString(answerEditText.getText().toString());
	 * 
	 * shortAnswerQuestion.setAnswer(ans);
	 * 
	 * Logger.info("Questions UI",
	 * "Answer is set as "+answerEditText.getText().toString()); } } });
	 */

	// Return Layout
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
    	
    	double scrnSize = screenSize(activity);
	int id = 1;
	String teacherAnswer = "";
	String studentAnswer = "";
	String finalTeacherAnswer = "";
	String finalStudentAnswer = "";

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
	final LinearLayout.LayoutParams inlineParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	

	// layout.addView(getQuestionView(question, activity));

	final TextView teacherAnswerTextVIew = new TextView(activity);
	final TextView studentAnswerTextView = new TextView(activity);

	final TextView teacherAnswerText = new TextView(activity);
	final TextView studentAnswerText = new TextView(activity);

	teacherAnswerText.setId(id++);
	studentAnswerText.setId(id++);
	teacherAnswerTextVIew.setId(id++);
	studentAnswerTextView.setId(id++);

	teacherAnswerText.setTextColor(Color.parseColor("#000000"));
	teacherAnswerText.setMaxLines(7);
	teacherAnswerText.setMovementMethod(new ScrollingMovementMethod());
	studentAnswerText.setTextColor(Color.parseColor("#000000"));
	studentAnswerText.setMovementMethod(new ScrollingMovementMethod());

	teacherAnswerTextVIew.setTextColor(Color.parseColor("#A52A2A"));
	studentAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	
	final TextView teacherComment = new TextView(activity);
	teacherComment.setId(id++);
	
	if(question != null && question.getDescription() != null){
		if(question.getDescription().trim().length() != 0)
			teacherComment.setText(question.getDescription());
		else
	    	teacherComment.setText("No Comments");
	}else
		teacherComment.setText("No Comments");
	
	final TextView teacherCommentView = new TextView(activity);
	teacherCommentView.setId(id++);
	teacherCommentView.setText("Teacher Comment:");
	teacherCommentView.setTextColor(Color.parseColor("#A52A2A"));

	// looping through the choices list to get the answer
	if (question.getCorrectAnswer() != null
		&& question.getCorrectAnswer().getChoices() != null) {
	    int position = 0;
	    for (int i = 0; i < question.getCorrectAnswer().getChoices().size(); i++) {
		position = question.getCorrectAnswer().getChoices().get(i)
			.getTeacherPosition();
		teacherAnswer = question.getCorrectAnswer().getChoices().get(i)
			.getTitle();
		Logger.info("", "FIB - teacher answer: " + teacherAnswer );
		if (question.getCorrectAnswer().getChoices().size() > 1) {
		    Logger.warn("",
			    "ANSWER - FIB - teacherAnswer before appending is:"
				    + teacherAnswer);
		    Logger.warn("",
			    "ANSWER - FIB - final teacherAnswer before appending is:"
				    + finalTeacherAnswer);
		    finalTeacherAnswer += position + ". " + teacherAnswer
			    + "    ";
		    Logger.warn("",
			    "ANSWER - FIB - final teacherAnswer after appending is: "
				    + finalTeacherAnswer);
		} else {
		    finalTeacherAnswer = position + ". " + teacherAnswer;
		}
	    }
	}

	if (question.getStudentAnswer() != null
		&& question.getStudentAnswer().getChoices() != null) {
	    int position = 0;
	    for (int i = 0; i < question.getStudentAnswer().getChoices().size(); i++) {
		studentAnswer = question.getStudentAnswer().getChoices().get(i)
			.getTitle();
		position = question.getStudentAnswer().getChoices().get(i)
			.getPosition();
		Logger.info("", "FIB -  student answer: " + studentAnswer);
		if (question.getStudentAnswer().getChoices().size() > 1)
		    finalStudentAnswer += position + ". " + studentAnswer
		    + "    ";
		else
		    finalStudentAnswer = position + ". " + studentAnswer;
	    }
	}

	if (finalStudentAnswer.endsWith(",")) {
	    finalStudentAnswer = finalStudentAnswer.substring(0,
		    finalStudentAnswer.length() - 1);
	    Logger.warn("",
		    "ANSWER - FIB - length is:"
			    + (finalStudentAnswer.length() - 1));
	    Logger.warn("", "ANSWER - FIB  - final answer is:"
		    + finalStudentAnswer);
	}
	if (finalTeacherAnswer.endsWith(",")) {
	    finalTeacherAnswer = finalTeacherAnswer.substring(0,
		    finalTeacherAnswer.length() - 1);
	}

	// setting the answers
	teacherAnswerText.setText(finalTeacherAnswer);
	if (!question.getStudentAnswer().getAnswerString().isEmpty()) {
	    studentAnswerText.setText(finalStudentAnswer);
	    studentAnswerText.setMaxLines(7);
	    studentAnswerText.setMovementMethod(new ScrollingMovementMethod());
	} else {
	    studentAnswerText.setText(" Student did not attempt the question");
	}

	teacherAnswerTextVIew.setText("Teacher Answer:       Teacher allotted marks :"+question.getMarksAlloted());
	studentAnswerTextView.setText("Student Answer:       Student awarded marks :"+question.getMarksAwarded());
	
	if(scrnSize >8){
		outerParams.setMargins(45, 5, 30, 50);
		inlineParams.setMargins(5, 10, 30, 0);
		
	}else if(scrnSize <= 8 && scrnSize > 5){
		outerParams.setMargins(40, 5, 30, 30);
		inlineParams.setMargins(5, 10, 20, 0);
		teacherAnswerTextVIew.setTextSize(16);
		studentAnswerTextView.setTextSize(16);
		teacherAnswerText.setTextSize(16);
		teacherComment.setTextSize(16);
		studentAnswerText.setTextSize(16);
	}else{
		outerParams.setMargins(35, 5, 20, 20);
		inlineParams.setMargins(5, 10, 10, 0);
		teacherAnswerTextVIew.setTextSize(12);
		studentAnswerTextView.setTextSize(12);
		teacherAnswerText.setTextSize(12);
		teacherComment.setTextSize(12);
		studentAnswerText.setTextSize(12);
	}

	innerLayout.addView(teacherAnswerTextVIew, inlineParams);
	innerLayout.addView(teacherAnswerText, inlineParams);
	innerLayout.addView(studentAnswerTextView, inlineParams);
	innerLayout.addView(studentAnswerText, inlineParams);
	innerLayout.addView(teacherCommentView, inlineParams);
	innerLayout.addView(teacherComment, inlineParams);
	layout.addView(getQuestionView(question, activity));
	layout.addView(innerLayout, outerParams);
	
	// Return Layout
	return layout;
    }

    /**
     * Builds the true or false.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout buildTrueOrFalse(final Question question,
	    final FragmentActivity activity, LinearLayout layout) {
    	double screenS = screenSize(activity);
	int id = 1;
	final MultipleChoiceQuestion tfQuestion = (MultipleChoiceQuestion) question;
	final TextView teacherAnswerTextView = new TextView(activity);
	final TextView studentAnswerTextView = new TextView(activity);
	final TextView studentAnswerView = new TextView(activity);

	teacherAnswerTextView.setId(id++);
	studentAnswerTextView.setId(id++);
	studentAnswerView.setId(id++);

	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	studentAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));

	teacherAnswerTextView.setText("Teacher Answer:       Teacher allotted marks :"+question.getMarksAlloted());
	studentAnswerTextView.setText("Student Answer:       Student awarded marks :"+question.getMarksAwarded());

	if (layout == null) {
	    Logger.warn("QuesionsUI", "BUILDTFQ - layout is null");
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    Logger.warn("QuesionsUI", "BUILDTFQ - layout is  not null");
	    /*
	     * layout.setLayoutParams(new
	     * android.widget.LinearLayout.LayoutParams(
	     * android.view.ViewGroup.LayoutParams.FILL_PARENT, 550));
	     * layout.setOrientation(LinearLayout.VERTICAL);
	     */
	}

	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	defaultParams.setMargins(40, 0, 10, 10);

	final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
		150);
	layoutParams.setMargins(45, 0, 10, 10);
	Logger.info("BuildTrueOfFalse Question",
		"PARSER - MCQ - Question to build is " + question.getQuestion());

	layout.addView(getQuestionView(question, activity));
	
	if (tfQuestion != null && tfQuestion.getCorrectAnswer() != null
		&& tfQuestion.getCorrectAnswer().getChoices() != null) {

	    Logger.info("BuildTrueOfFalse Question", "Question to build is "
		    + question.getQuestion() + " with "
		    + tfQuestion.getCorrectAnswer().getChoices().size()
		    + " choices");
	    Logger.info("BuildTrueOfFalse Question", "Question details "
		    + question.toString());
	    final List<AnswerChoice> choices = tfQuestion.getCorrectAnswer()
		    .getChoices();

	    final ScrollView scrollView1 = new ScrollView(activity);

	    // Create Option Radio buttons and add to Options Container Layout
	    final RadioGroup radioGroup = new RadioGroup(activity);
	    radioGroup.setId(id++);
	    radioGroup.setOrientation(LinearLayout.VERTICAL);

	    if (choices != null) {
		for (final AnswerChoice choice : choices) {
		    Logger.info("QuestionsUI - TrueorFalse",
			    "Preparing choice as " + choice.toString());
		    final RadioButton option = new RadioButton(activity);
		    option.setId(id++);// TODO verify
		    option.setTextColor(Color.parseColor("#000000"));
		    option.setText(choice.getTitle());
		    option.setPadding(50, 2, 50, 2);
		    option.setClickable(false);
		    Logger.warn("QuestionsUI - TrueorFalse",
			    "ANSWER - radio button visibility for teacher : "
				    + choice.isTeacherSelected()
				    + "  title is " + choice.getTitle());

		    if (choice.isTeacherSelected()) {
			option.setChecked(true);
		    } else {
			option.setChecked(false);
		    }
		    if(screenS<= 5){
			    option.setTextSize(12);
			    option.setButtonDrawable(R.drawable.radio_selector);
		    }else if(screenS> 5 && screenS<=8){
			    option.setTextSize(16);
		    }else{
		    	option.setTextSize(18);
		    }
		    radioGroup.addView(option);
		}
		
		layout.addView(teacherAnswerTextView, defaultParams);
		scrollView1.addView(radioGroup, layoutParams);
		final LinearLayout lly = new LinearLayout(activity);
		lly.addView(scrollView1, layoutParams);
		layout.addView(lly, defaultParams);
	    }

	}
	if (tfQuestion != null && tfQuestion.getChoices() != null) {

	    Logger.info("BuildTrueOfFalse Question",
		    "STUDENT - Question to build is " + question.getQuestion()
		    + " with " + tfQuestion.getChoices().size()
		    + " choices");
	    Logger.warn("BuildTrueOfFalse Question",
		    "STUDENT - Question details " + question.toString());
	    final List<AnswerChoice> choices = tfQuestion.getChoices();

	    final ScrollView scrollView2 = new ScrollView(activity);
	    // Create Option Radio buttons and add to Options Container Layout
	    final RadioGroup radioGroup = new RadioGroup(activity);
	    radioGroup.setId(id++);
	    radioGroup.setOrientation(LinearLayout.VERTICAL);
	    // radioGroup.setLayoutParams(defaultParams);
	    layout.addView(studentAnswerTextView, defaultParams);
	    boolean flag = false;
	    if (choices != null) {
	    	for(int i=0; i< choices.size(); i++){
	    		if(choices.get(i).isSelected()){
	    			flag = true;
	    			break;
	    		}
	    		else{
	    			flag = false;
	    		}
	    	}
	    	
	    	if(!flag){
	    		studentAnswerView.setText("Student did not attempt the question");
	    		layout.addView(studentAnswerView,layoutParams);
	    	}else{
	    		for (final AnswerChoice choice : choices) {
	    		    Logger.info("QuestionsUI - TrueorFalse",
	    			    "Preparing choice as " + choice.toString());
	    		    final RadioButton option = new RadioButton(activity);
	    		    option.setId(id++);// TODO verify
	    		    option.setTextColor(Color.parseColor("#000000"));
	    		    option.setText(choice.getTitle());
	    		    option.setPadding(50, 2, 50, 2);
	    		    option.setClickable(false);
	    		    Logger.warn("QuestionsUI - TrueorFalse",
	    			    "ANSWER - radio button visibility for student : "
	    				    + choice.isSelected() + "  title is "
	    				    + choice.getTitle());
	    		    if (choice.isSelected()) {
	    			option.setChecked(true);
	    		    } else {
	    			option.setChecked(false);
	    		    }
	    		    radioGroup.addView(option);
	    		}
	    		final LinearLayout lly1 = new LinearLayout(activity);
	    		scrollView2.addView(radioGroup, layoutParams);
	    		lly1.addView(scrollView2, layoutParams);
	    		layout.addView(lly1, defaultParams);
	    	}
	    }
	} else {
	    Logger.warn("QuestionsUI", "choices is null");
	}
	// Add to layout
	// Return Layout
	return layout;
    }

    /**
     * Builds the ordering question.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout buildOrderingQuestion(final Question question,
	    final FragmentActivity activity, LinearLayout layout) {
    	
    	double scrnSize = screenSize(activity);
	int id = 1;
	final TextView teacherAnswerTextView = new TextView(activity);
	final TextView studentAnswerTextView = new TextView(activity);

	teacherAnswerTextView.setId(id++);
	studentAnswerTextView.setId(id++);

	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	studentAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));

	teacherAnswerTextView.setText("Teacher Answer:       Teacher allotted marks :"+question.getMarksAlloted());
	studentAnswerTextView.setText("Student Answer:       Student awarded marks :"+question.getMarksAwarded());
	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    new LinearLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    /*
	     * layout.setLayoutParams(new
	     * android.widget.LinearLayout.LayoutParams( 800, 600));
	     * layout.setOrientation(LinearLayout.VERTICAL);
	     */
	}

	final OrderingQuestion ordQuestion = (OrderingQuestion) question;

	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams ;

	final LinearLayout.LayoutParams answerParams;
	
	if(scrnSize >8){
		defaultParams = new LinearLayout.LayoutParams(700, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		defaultParams.setMargins(40, 10, 30, 10);
		answerParams = new LinearLayout.LayoutParams(600, 150);
		answerParams.setMargins(45, 2, 30, 2);
		teacherAnswerTextView.setTextSize(16);
		studentAnswerTextView.setTextSize(16);
	}else if(scrnSize <= 8 && scrnSize > 5){
		defaultParams = new LinearLayout.LayoutParams(600,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		defaultParams.setMargins(40, 10, 30, 10);
		answerParams = new LinearLayout.LayoutParams(500, 150);
		answerParams.setMargins(45, 2, 20, 2);
		teacherAnswerTextView.setTextSize(16);
		studentAnswerTextView.setTextSize(16);
	}else{
		defaultParams = new LinearLayout.LayoutParams(500,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		defaultParams.setMargins(40, 10, 30, 10);
		answerParams = new LinearLayout.LayoutParams(400, 150);
		answerParams.setMargins(45, 2, 15, 2);
		teacherAnswerTextView.setTextSize(14);
		studentAnswerTextView.setTextSize(14);
	}
	layout.addView(getQuestionView(question, activity));
	layout.addView(teacherAnswerTextView, defaultParams);
	Logger.warn("", "ORD - choices are:" + ordQuestion.getCorrectAnswer());
	
	if (ordQuestion != null && ordQuestion.getCorrectAnswer() != null
		&& ordQuestion.getCorrectAnswer().getChoices() != null) {
	    final ListView orderingQuestionList = new ListView(activity);
	    final OrderingQuestionAdapter orderingTeacher = new OrderingQuestionAdapter(
		    activity, ordQuestion.getCorrectAnswer().getChoices(),
		    "TEACHER");
	    orderingQuestionList.setAdapter(orderingTeacher);
	    layout.addView(orderingQuestionList, answerParams);
	}

	layout.addView(studentAnswerTextView, defaultParams);
	if (ordQuestion != null && ordQuestion.getStudentAnswer() != null
		&& ordQuestion.getStudentAnswer().getChoices() != null) {
	    final ListView orderingQuestionList = new ListView(activity);
	    final OrderingQuestionAdapter orderingQuestionTeacher = new OrderingQuestionAdapter(
		    activity, ordQuestion.getStudentAnswer().getChoices(),
		    "STUDENT");
	    orderingQuestionList.setAdapter(orderingQuestionTeacher);
	    layout.addView(orderingQuestionList, answerParams);
	} else {
	    final TextView studentAnswer = new TextView(activity);
	    studentAnswer.setText("Student did not attempt the question");
	    studentAnswer.setTextColor(Color.parseColor("#000000"));
	    if(scrnSize >8){
			studentAnswer.setTextSize(16);
		}else if(scrnSize <= 8 && scrnSize > 5){
			studentAnswer.setTextSize(16);
		}else{
			studentAnswer.setTextSize(14);
		}
	    layout.addView(studentAnswer, answerParams);
	}
	return layout;
    }

    /**
     * Builds the matchthe following.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout buildMatchtheFollowing(final Question question,
	    final FragmentActivity activity, LinearLayout layout) {
    	
    	double scrnSize= screenSize(activity);
	int id = 1;
	if (layout == null) {
	    Logger.warn("QuesionsUI", "BUILDMFQ - layout is null");
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    800,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    Logger.warn("QuesionsUI ", "BUILD MTF - layout is  not null");
	    /*
	     * layout.setLayoutParams(new
	     * android.widget.LinearLayout.LayoutParams(
	     * android.view.ViewGroup.LayoutParams.MATCH_PARENT, 550));
	     * layout.setOrientation(LinearLayout.VERTICAL);
	     */
	}

	final TextView teacherAnswerTextView = new TextView(activity);
	teacherAnswerTextView.setId(id++);
	teacherAnswerTextView.setText("Teacher Answer:       Teacher allotted marks :"+question.getMarksAlloted());
	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));

	final TextView studentAnswerTextView = new TextView(activity);
	studentAnswerTextView.setId(id++);
	studentAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	studentAnswerTextView.setText("Student Answer:       Student awarded marks :"+question.getMarksAwarded());

	final MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) question;

	final LinearLayout.LayoutParams defaultParams; 

	final LinearLayout.LayoutParams answerParams;
	
	if(scrnSize >8){
		defaultParams = new LinearLayout.LayoutParams(600,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		defaultParams.setMargins(40, 10, 30, 10);
		answerParams = new LinearLayout.LayoutParams(700, 150);
		answerParams.setMargins(45, 2, 15, 2);
		teacherAnswerTextView.setTextSize(16);
		studentAnswerTextView.setTextSize(16);
	}else if(scrnSize <= 8 && scrnSize > 5){
		defaultParams = new LinearLayout.LayoutParams(600,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		defaultParams.setMargins(40, 10, 10, 10);
		answerParams = new LinearLayout.LayoutParams(600, 150);
		answerParams.setMargins(45, 2, 15, 2);
		teacherAnswerTextView.setTextSize(16);
		studentAnswerTextView.setTextSize(16);
	}else{
		defaultParams = new LinearLayout.LayoutParams(600,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		defaultParams.setMargins(40, 10, 10, 10);
		answerParams = new LinearLayout.LayoutParams(500, 150);
		answerParams.setMargins(45, 2, 15, 2);
		teacherAnswerTextView.setTextSize(14);
		studentAnswerTextView.setTextSize(14);
	}
	layout.addView(getQuestionView(question, activity));
	layout.addView(teacherAnswerTextView, defaultParams);

	if (multipleChoiceQuestion != null
		&& multipleChoiceQuestion.getCorrectAnswer() != null
		&& multipleChoiceQuestion.getCorrectAnswer().getChoices() != null) {
	    final ListView matchTheFollowingList = new ListView(activity);
	    final MatchtheFollowingAdapter matchTheFollowingTeacher = new MatchtheFollowingAdapter(
		    activity, multipleChoiceQuestion.getCorrectAnswer()
		    .getChoices());
	    matchTheFollowingList.setAdapter(matchTheFollowingTeacher);
	    layout.addView(matchTheFollowingList, answerParams);
	}

	layout.addView(studentAnswerTextView, defaultParams);
	/*
	 * if (multipleChoiceQuestion != null &&
	 * multipleChoiceQuestion.getStudentAnswer().getChoices() != null) {
	 */
	Logger.warn("",
		"MTF - answered: " + multipleChoiceQuestion.isAnswered());
	if (multipleChoiceQuestion != null
		&& multipleChoiceQuestion.getStudentAnswer() != null
		&& multipleChoiceQuestion.getStudentAnswer().getChoices() != null) {
	    final ListView matchTheFollowingList = new ListView(activity);
	    final MatchtheFollowingAdapter matchTheFollowingTeacher = new MatchtheFollowingAdapter(
		    activity, multipleChoiceQuestion.getStudentAnswer()
		    .getChoices());
	    matchTheFollowingList.setAdapter(matchTheFollowingTeacher);
	    layout.addView(matchTheFollowingList, answerParams);
	} else {
		final TextView studentAnswer= new TextView(activity);
	    studentAnswer.setText("Student did not attempt the question");
	    studentAnswer.setTextColor(Color.parseColor("#000000"));
	    if(scrnSize >8){
			studentAnswer.setTextSize(16);
		}else if(scrnSize <= 8 && scrnSize > 5){
			studentAnswer.setTextSize(16);
		}else{
			studentAnswer.setTextSize(14);
		}
	    layout.addView(studentAnswer, answerParams);
	}
	
	return layout;
    }

    /**
     * Gets the question view.
     *
     * @param q the q
     * @param activity the activity
     * @return the question view
     */
    private LinearLayout getQuestionView(Question q, Activity activity) {
    	
    	double screenSize =screenSize(activity);
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	

	final LinearLayout questionsLayout = new LinearLayout(activity);
	questionsLayout.setOrientation(LinearLayout.HORIZONTAL);

	// Initialize default parameters
	final LinearLayout.LayoutParams questionsParams = new LinearLayout.LayoutParams(
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
	questionView.setMaxLines(2);
	
	if(screenSize >8){
		defaultParams.setMargins(40, 45, 15, 10);
		questionsParams.setMargins(30, 45, 15, 10);
		questionView.setTextSize(20);
	}else if(screenSize <= 8 && screenSize > 5){
		defaultParams.setMargins(40, 20, 15, 5);
		questionsParams.setMargins(30, 20, 15, 5);
		questionView.setTextSize(16);
	}else{
		defaultParams.setMargins(40, 10, 15, 5);
		questionsParams.setMargins(30, 10, 15, 5);
		questionView.setTextSize(14);
	}

	questionView.setGravity(Gravity.LEFT);
	questionsLayout.addView(questionView, questionsParams);

	return questionsLayout;
    }

    /**
     * The Class MatchtheFollowingAdapter.
     */
    public class MatchtheFollowingAdapter extends ArrayAdapter<AnswerChoice> {

	/** The answer choice. */
	TextView Sno, question, answerChoice;
	
	/** The answer. */
	EditText answer;
	
	/** The item. */
	List<AnswerChoice> item;
	
	/** The apppend ans. */
	String apppendAns = "";
	
	/** The hash map. */
	HashMap<Integer, AnswerChoice> hashMap = new HashMap<Integer, AnswerChoice>();

	/**
	 * Instantiates a new matchthe following adapter.
	 *
	 * @param context the context
	 * @param choices the choices
	 */
	public MatchtheFollowingAdapter(Context context,
		List<AnswerChoice> choices) {
	    super(context, R.layout.matchthefollowing_listrow, choices);
	    item = choices;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View view, ViewGroup parent) {

	    if (view == null) {
		Log.i("____________________",
			"View is Null now going to inflate");
		final LayoutInflater inflater = (LayoutInflater) this.getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.matchthefollowing_listrow,
			null);

	    }
	    Sno = (TextView) view.findViewById(R.id.sno);
	    question = (TextView) view.findViewById(R.id.questionName);
	    answerChoice = (TextView) view.findViewById(R.id.answerChoice);
	    answer = (EditText) view.findViewById(R.id.Answer);
	    answer.setEnabled(false);

	    Log.i("____________________",
		    "MATCH - answer is:"
			    + item.get(position).getMatch_FieldThree());
	    Sno.setText(item.get(position).getMatch_FieldOne());
	    question.setLayoutParams(new android.widget.LinearLayout.LayoutParams(300, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	    answerChoice.setLayoutParams(new  android.widget.LinearLayout.LayoutParams(300, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	    question.setPadding(20, 0, 10, 20);
	    answerChoice.setPadding(20, 0, 10, 0);
	    question.setText(item.get(position).getTitle());
	    answerChoice.setText(item.get(position).getMatch_FieldFour());
	    answer.setText(item.get(position).getMatch_FieldThree());

	    return view;

	}

	/*
	 * @Override public int getCount() { // TODO Auto-generated method stub
	 * Log.i("____________________", "In getCount Method" + item.size());
	 * return item.size(); }
	 */

    }

    /**
     * The Class OrderingQuestionAdapter.
     */
    public class OrderingQuestionAdapter extends ArrayAdapter<AnswerChoice> {

	/** The question. */
	TextView teacherOrder, question;
	
	/** The answer. */
	EditText answer;
	
	/** The item. */
	List<AnswerChoice> item;
	
	/** The apppend ans. */
	String apppendAns = "";
	
	/** The type. */
	String type = "";
	
	/** The hash map. */
	HashMap<Integer, AnswerChoice> hashMap = new HashMap<Integer, AnswerChoice>();

	/**
	 * Instantiates a new ordering question adapter.
	 *
	 * @param context the context
	 * @param choices the choices
	 * @param type the type
	 */
	public OrderingQuestionAdapter(Context context,
		List<AnswerChoice> choices, String type) {
	    super(context, R.layout.ordering, choices);
	    item = choices;
	    this.type = type;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
	    if (view == null) {

		final LayoutInflater inflater = (LayoutInflater) this.getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.ordering, null);

	    }

	    teacherOrder = (TextView) view.findViewById(R.id.teacher_order);
	    question = (TextView) view.findViewById(R.id.teacher_choice);
	    answer = (EditText) view.findViewById(R.id.student_order);
	    answer.setEnabled(false);

	    teacherOrder.setText(item.get(position).getId() + "");
	    question.setText(item.get(position).getTitle());

	    if (type.equalsIgnoreCase("TEACHER")) {
		answer.setText(item.get(position).getTeacherPosition() + "");
	    }
	    if (type.equalsIgnoreCase("STUDENT")) {

		answer.setText(item.get(position).getPosition() + "");

	    }

	    return view;

	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getCount()
	 */
	@Override
	public int getCount() {
	    // TODO Auto-generated method stub
	    return item.size();
	}

    }
    
    public double screenSize(Context activity){
    	DisplayMetrics dm = new DisplayMetrics();
        ((Activity)activity).getWindowManager().getDefaultDisplay().getMetrics(dm);
        double sx = Math.pow(dm.widthPixels/dm.xdpi,2);
        double sy = Math.pow(dm.heightPixels/dm.ydpi,2);
        double screenInches = Math.sqrt(sx+sy);
    return screenInches;
    }

}