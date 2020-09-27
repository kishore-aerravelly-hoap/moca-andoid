package com.pearl.ui.helpers.examination;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.activities.SelectQuestionsActivity;
import com.pearl.logger.Logger;
import com.pearl.user.teacher.examination.AnswerChoice;
import com.pearl.user.teacher.examination.Question;
import com.pearl.user.teacher.examination.questiontype.MultipleChoiceQuestion;
import com.pearl.user.teacher.examination.questiontype.OrderingQuestion;



// TODO: Auto-generated Javadoc
/**
 * The Class QuestionListFromDB.
 */
public class QuestionListFromDB {
    
    /**
     * Builds the short answer question.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout buildShortAnswerQuestion(final Question question,
	    final SelectQuestionsActivity activity, LinearLayout layout) {
	int id = 1;
	final TextView teacherAnswerTextView = new TextView(activity);
	teacherAnswerTextView.setId(id++);
	teacherAnswerTextView.setText("Teacher Answer:");
	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	final TextView teacherAllotedMarks = new TextView(activity);
	teacherAllotedMarks.setId(id++);
	teacherAllotedMarks.setText("Allotted marks :"+question.getMarksAlloted());
	teacherAllotedMarks.setTextColor(Color.parseColor("#A52A2A"));
	
	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} 	// Initialize default parameters
	  layout.setOrientation(LinearLayout.VERTICAL);
	layout.setLayoutParams(new LayoutParams(1120, 150));
	final LinearLayout childLayout = new LinearLayout(activity);
	final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	//layoutParams.setMargins(70, 20, 0, 0);
	childLayout.setOrientation(LinearLayout.HORIZONTAL);
	childLayout.addView(teacherAnswerTextView, layoutParams);
	childLayout.addView(teacherAllotedMarks, layoutParams);
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		50);
	defaultParams.setMargins(70, 5, 30, 0);

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		50);
	answerParams.setMargins(15, 5, 30, 0);
	// answerParams.height = 100;
	
	

	final TextView teacherAnswer = new TextView(activity);
	teacherAnswer.setId(id++);
	teacherAnswer.setTextSize(16);
	
	final TextView teacherCommentView = new TextView(activity);
	teacherCommentView.setId(id++);
	teacherCommentView.setText("Teacher Comment: ");
	teacherCommentView.setTextColor(Color.parseColor("#A52A2A"));

	final TextView teacherComment = new TextView(activity);
	teacherComment.setId(id++);
	
	if (question != null) {
	    if (question.getCorrectAnswer() != null) {
	    	if(question.getCorrectAnswer().getAnswerString().toString().trim() != "")
		teacherAnswer.setText(question.getCorrectAnswer()
			.getAnswerString());
	    	else
	    		teacherAnswer.setText("Answer is empty");
	    } 
	    teacherAnswer.setTextColor(Color.parseColor("#000000"));
	}
	
	teacherAnswer.setMovementMethod(new ScrollingMovementMethod());
	teacherAnswer.setMaxLines(8);
	teacherComment.setTextSize(16);
	// Add to layout
	layout.addView(getQuestionView(question, activity));
	layout.addView(childLayout, defaultParams);
	layout.addView(teacherAnswer, layoutParams);

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
	    final SelectQuestionsActivity activity, LinearLayout layout) {
	int id = 1;
	final TextView teacherAnswerTextView = new TextView(activity);
	teacherAnswerTextView.setId(id++);
	teacherAnswerTextView.setText("Teacher Answer:   ");
	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	final TextView teacherAllotedMarks = new TextView(activity);
	teacherAllotedMarks.setId(id++);
	teacherAllotedMarks.setText("Allotted marks :"+question.getMarksAlloted());
	teacherAllotedMarks.setTextColor(Color.parseColor("#A52A2A"));
	teacherAllotedMarks.setWidth(50);

	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} 
	   layout.setOrientation(LinearLayout.VERTICAL);
	layout.setLayoutParams(new LayoutParams(1120, 150));
	final LinearLayout childLayout = new LinearLayout(activity);
		final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	//layoutParams.setMargins(70, 20, 0, 0);
	childLayout.setOrientation(LinearLayout.HORIZONTAL);
	childLayout.addView(teacherAnswerTextView, layoutParams);
	childLayout.addView(teacherAllotedMarks, layoutParams);
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		40);
	defaultParams.setMargins(70, 5, 30, 0);

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		40);
	answerParams.setMargins(70, 5, 30, 0);
	

	final TextView teacherAnswer = new TextView(activity);
	teacherAnswer.setId(id++);
	teacherAnswer.setTextSize(16);

	if (question != null) {
	    if (question.getCorrectAnswer() != null) {
	    	if(!question.getCorrectAnswer().getAnswerString().toString().trim().equals(""))
		teacherAnswer.setText(question.getCorrectAnswer()
			.getAnswerString());
	    	else
	    		teacherAnswer.setText("Answer is empty");
	    } 
	    teacherAnswer.setTextColor(Color.parseColor("#000000"));

	}

	// Add to layout
	teacherAnswer.setMovementMethod(new ScrollingMovementMethod());
	teacherAnswer.setMaxLines(8);
	layout.addView(getQuestionView(question, activity));
	layout.addView(childLayout, defaultParams);
	layout.addView(teacherAnswer, answerParams);


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
	    final SelectQuestionsActivity activity, LinearLayout layout) {
	int id = 1;
	String teacherAnswer = "";
	String finalTeacherAnswer = "";

	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	}

	layout.setOrientation(LinearLayout.VERTICAL);

	 layout.setLayoutParams(new LayoutParams(
			    1120,
			    150));

	final LinearLayout.LayoutParams outerParams = new LinearLayout.LayoutParams(700,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	outerParams.setMargins(70, 20, 0, 0);

	final LinearLayout innerLayout = new LinearLayout(activity);
	innerLayout.setOrientation(LinearLayout.HORIZONTAL);

	// Initialize default parameters
	final LinearLayout.LayoutParams inlineParams = new LinearLayout.LayoutParams(
		500,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	//inlineParams.setMargins(35, 0, 0, 0);

	// layout.addView(getQuestionView(question, activity));

	final TextView teacherAnswerTextVIew = new TextView(activity);

	final TextView teacherAnswerText = new TextView(activity);

	teacherAnswerText.setId(id++);
	teacherAnswerTextVIew.setId(id++);

	teacherAnswerText.setTextColor(Color.parseColor("#000000"));
	teacherAnswerText.setMaxLines(7);
	teacherAnswerText.setMovementMethod(new ScrollingMovementMethod());
	teacherAnswerText.setTextSize(16);

	teacherAnswerTextVIew.setTextColor(Color.parseColor("#A52A2A"));
	final TextView teacherAllotedMarks = new TextView(activity);
	teacherAllotedMarks.setId(id++);
	teacherAllotedMarks.setText("Allotted marks :"+question.getMarksAlloted());
	teacherAllotedMarks.setTextColor(Color.parseColor("#A52A2A"));


	// looping through the choices list to get the answer
	if (question.getAnswer() != null
		&& question.getCorrectAnswer().getChoices() != null) {
	    int position = 0;
	    for (int i = 0; i < question.getCorrectAnswer().getChoices().size(); i++) {
		position = question.getCorrectAnswer().getChoices().get(i)
			.getTeacherPosition();
		teacherAnswer = question.getCorrectAnswer().getChoices().get(i)
			.getTitle();
		Logger.info("", "FIB - teacher answer: " + teacherAnswer );
		if (question.getCorrectAnswer().getChoices().size() > 1) {
		    finalTeacherAnswer += position + ". " + teacherAnswer
			    + "    ";
		} else {
		    finalTeacherAnswer = position + ". " + teacherAnswer;
		}
	    }
	}



	if (finalTeacherAnswer.endsWith(",")) {
	    finalTeacherAnswer = finalTeacherAnswer.substring(0,
		    finalTeacherAnswer.length() - 1);
	}

	// setting the answers
	teacherAnswerText.setText(finalTeacherAnswer);
	

	teacherAnswerTextVIew.setText("Teacher Answer:     ");
	
	innerLayout.addView(teacherAnswerTextVIew, inlineParams);
	innerLayout.addView(teacherAllotedMarks, inlineParams);

	layout.addView(getQuestionView(question, activity));
	layout.addView(innerLayout, outerParams);
	layout.addView(teacherAnswerText, outerParams);
	

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
	    final SelectQuestionsActivity activity, LinearLayout layout) {


   /* 	if (question.getType().equals(Question.MULTIPLECHOICE_QUESTION)) {
    	    questionType = "MCQ";
    	} else if (question.getType().equals(Question.TRUE_OR_FALSE_QUESTION)) {
    	    questionType = "TFQ";
    	}
    	int ids = 1;
    	final MultipleChoiceQuestion tfQuestion = (MultipleChoiceQuestion) question;
    	question.setViewed(true);
    	if (layout == null) {
    	    Logger.warn("QuesionsUI", "BUILDTFQ - layout is null");
    	    // Initialize Linear Layout
    	    layout = new LinearLayout(activity);
    	    new LinearLayout.LayoutParams(
    	    		android.view.ViewGroup.LayoutParams.FILL_PARENT,150);
    	    layout.setOrientation(LinearLayout.VERTICAL);
    	} else {
    	    Logger.warn("QuesionsUI", "BUILDTFQ - layout is  not null");
    	    new LinearLayout.LayoutParams(
    		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
    		    150);
    	    layout.setOrientation(LinearLayout.VERTICAL);
    	}

    	// Initialize default parameters
    	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
    		1050, LayoutParams.WRAP_CONTENT);
    	defaultParams.setMargins(30, 10, 5, 50);
    	Logger.info("BuildTrueOfFalse Question",
    		"PARSER - MCQ - Question to build is " + question.getQuestion());
    	if (tfQuestion.getChoices() != null) {

    	    Logger.info("BuildTrueOfFalse Question", "Question to build is "
    		    + question.getQuestion() + " with "
    		    + tfQuestion.getChoices().size() + " choices");
    	    Logger.info("BuildTrueOfFalse Question", "Question details "
    		    + question.toString());
    	    final List<AnswerChoice> choices = tfQuestion.getChoices();

    	    // Create Option Radio buttons and add to Options Container Layout
    	    final RadioGroup radioGroup = new RadioGroup(activity);
    	    radioGroup.setId(ids++);
    	    radioGroup.setOrientation(LinearLayout.VERTICAL);
    	    radioGroup.setLayoutParams(defaultParams);

    	    for (final AnswerChoice choice : choices) {
    		Logger.info("QuestionsUI - TrueorFalse", "Preparing choice as "
    			+ choice.toString());

    		final RadioButton option = new RadioButton(activity);
    		option.setId(ids++);// TODO verify
    		option.setTextColor(Color.parseColor("#000000"));
    		option.setText(choice.getTitle());
    		option.setPadding(50, 2, 50, 2);
    		option.setEnabled(false);

    		
    		 * if (choice.isSelected()) { option.setChecked(true);
    		 * tfQuestion.setAnswer(choice); } else {
    		 * option.setChecked(false); }
    		 

    		
    		 * option.setOnClickListener(new OnClickListener() {
    		 * 
    		 * @Override public void onClick(View v) {
    		 * tfQuestion.setAnswer(choice);
    		 * 
    		 * choice.setSelected(true); for (AnswerChoice answerChoice :
    		 * choices) { if (answerChoice.getId() != choice.getId()) {
    		 * answerChoice.setSelected(false); } }
    		 * 
    		 * tfQuestion.setIsAnswered(true);
    		 * 
    		 * Logger.verbose("Questions UI", "TF Question:- " +
    		 * tfQuestion.toString() + " | Current Choice:- " +
    		 * choice.toString());
    		 * 
    		 * } });
    		 
    		radioGroup.addView(option);
    	    }
    	    final ScrollView sv = new ScrollView(activity);
    	    sv.addView(radioGroup);
    	    layout.addView(getQuestionView(question, activity));
    	    layout.addView(radioGroup, defaultParams);
    	} else {
    	    Logger.warn("QuestionsUI", "choices is null");
    	}
    	// Add to layout

    	// Return Layout
    	return layout;*/
        
    	
	/*int id = 1;
	int ids=0;
	final MultipleChoiceQuestion tfQuestion = (MultipleChoiceQuestion) question;
	final TextView teacherAnswerTextView = new TextView(activity);

	teacherAnswerTextView.setId(id++);

	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));

	teacherAnswerTextView.setText("Teacher Answer:       Teacher allotted marks :"+question.getMarksAlloted());

	if (layout == null) {
	    Logger.warn("QuesionsUI", "BUILDTFQ - layout is null");
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    1120,
		    300));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} 
	  layout.setLayoutParams(new LayoutParams(
			    1120,
			    300));
	 layout.setOrientation(LinearLayout.VERTICAL);
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		700,300);
	defaultParams.setMargins(30, 0, 0, 0);

	final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(900,300);
	layoutParams.setMargins(0, 10, 0, 0);
	
	final LinearLayout.LayoutParams scroll_viewParams = new LinearLayout.LayoutParams(600,250);
	layoutParams.setMargins(30, 5, 0, 0);
	
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
	    final List<AnswerChoice> choices = tfQuestion.getChoices();

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
		    option.setId(ids++);// TODO verify
		    option.setTextColor(Color.parseColor("#000000"));
		    option.setText(choice.getTitle());
		    option.setPadding(50, 0, 20, 10);
		    option.setClickable(false);
		    Logger.warn("QuestionsUI - TrueorFalse",
			    "ANSWER - radio button visibility for teacher : "
				    + choice.isTeacherSelected()
				    + "  title is " + choice.getTitle());

			if(choices.size()>2){
				option.setChecked(choice.isTeacherSelected());
			}else if(choices.size()<=2){
				option.setChecked(choice.isTeacherSelected());	
			}
		radioGroup.addView(option);
		}
		layout.addView(teacherAnswerTextView, defaultParams);
		scrollView1.addView(radioGroup, scroll_viewParams);
		final LinearLayout lly = new LinearLayout(activity);
		lly.setOrientation(LinearLayout.VERTICAL);
		lly.addView(scrollView1, layoutParams);
		layout.addView(scrollView1, defaultParams);
	    }

	}
	
	// Add to layout
	// Return Layout
	return layout;*/
    	

    	int id = 1;
    	int ids=0;
    	final MultipleChoiceQuestion tfQuestion = (MultipleChoiceQuestion) question;
    	final TextView teacherAnswerTextView = new TextView(activity);

    	teacherAnswerTextView.setId(id++);

    	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));

    	teacherAnswerTextView.setText("Teacher Answer:");

    	final TextView teacherAllotedMarks = new TextView(activity);
    	teacherAllotedMarks.setId(id++);
    	teacherAllotedMarks.setText("Allotted marks:"+question.getMarksAlloted());
    	teacherAllotedMarks.setTextColor(Color.parseColor("#A52A2A"));
    	if (layout == null) {
    	    Logger.warn("QuesionsUI", "BUILDTFQ - layout is null");
    	    // Initialize Linear Layout
    	    layout = new LinearLayout(activity);
    	    layout.setLayoutParams(new LayoutParams(
    		    900,
    		    300));
    	    layout.setOrientation(LinearLayout.VERTICAL);
    	} 
    	  layout.setLayoutParams(new LayoutParams(
    			    900,
    			    300));
    	 layout.setOrientation(LinearLayout.VERTICAL);
    	 //take a new linear layout whose orientation is horizontal
    	 //add two text views to the linear layout
    	// Initialize default parameters
 		final LinearLayout childLayout = new LinearLayout(activity);
 		final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	layoutParams.setMargins(20, 20, 0, 0);
    	childLayout.setOrientation(LinearLayout.HORIZONTAL);
    	childLayout.addView(teacherAnswerTextView, layoutParams);
    	childLayout.addView(teacherAllotedMarks, layoutParams);
    	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
    		700,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	defaultParams.setMargins(50, 20, 0, 0);

    	/*final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(900,android.view.ViewGroup.LayoutParams.FILL_PARENT);
    	layoutParams.setMargins(20, 20, 0, 0);*/
    	
    	final LinearLayout.LayoutParams scroll_viewParams = new LinearLayout.LayoutParams(900,250);
    	scroll_viewParams.setMargins(30, 5, 0, 0);
    	
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
    	    final List<AnswerChoice> choices = tfQuestion.getChoices();

    	    final ScrollView scrollView1 = new ScrollView(activity);

    	    // Create Option Radio buttons and add to Options Container Layout
    	    final RadioGroup radioGroup = new RadioGroup(activity);
    	    radioGroup.setId(id++);
    	    radioGroup.setOrientation(LinearLayout.VERTICAL);
    	    radioGroup.setLayoutParams(new LinearLayout.LayoutParams(900,250));

    	    if (choices != null) {
    		for (final AnswerChoice choice : choices) {
    		    Logger.info("QuestionsUI - TrueorFalse",
    			    "Preparing choice as " + choice.toString());

    		    final RadioButton option = new RadioButton(activity);
    		    option.setId(ids++);// TODO verify
    		    option.setTextColor(Color.parseColor("#000000"));
    		    option.setText(choice.getTitle());
    		    option.setPadding(50, 0, 20, 10);
    		    option.setClickable(false);
    		    Logger.warn("QuestionsUI - TrueorFalse",
    			    "ANSWER - radio button visibility for teacher : "
    				    + choice.isTeacherSelected()
    				    + "  title is " + choice.getTitle());

    			if(choices.size()>2){
    				option.setChecked(choice.isTeacherSelected());
    			}else if(choices.size()<=2){
    				option.setChecked(choice.isTeacherSelected());	
    			}
    		radioGroup.addView(option);
    		}
    		//instead of adding these two text views to the parent linear layout add new linear layout
    		layout.addView(childLayout, defaultParams);

    		scrollView1.addView(radioGroup, scroll_viewParams);
    		/*final LinearLayout lly = new LinearLayout(activity);
    		lly.setOrientation(LinearLayout.VERTICAL);
    		lly.addView(scrollView1, layoutParams);*/
    		layout.addView(scrollView1, defaultParams);
    	    }

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
	    final SelectQuestionsActivity activity, LinearLayout layout) {
	int id = 1;
	final TextView teacherAnswerTextView = new TextView(activity);

	teacherAnswerTextView.setId(id++);

	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));

	teacherAnswerTextView.setText("Teacher Answer:       ");
	final TextView teacherAllotedMarks = new TextView(activity);
	teacherAllotedMarks.setId(id++);
	teacherAllotedMarks.setText("Allotted marks :"+question.getMarksAlloted());
	teacherAllotedMarks.setTextColor(Color.parseColor("#A52A2A"));
	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    new LinearLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	    layout.setOrientation(LinearLayout.VERTICAL);
	} 
	 layout.setOrientation(LinearLayout.VERTICAL);
	final OrderingQuestion ordQuestion = (OrderingQuestion) question;
	final LinearLayout childLayout = new LinearLayout(activity);
		final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	layoutParams.setMargins(20, 20, 0, 0);
	childLayout.setOrientation(LinearLayout.HORIZONTAL);
	childLayout.addView(teacherAnswerTextView, layoutParams);
	childLayout.addView(teacherAllotedMarks, layoutParams);
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		875, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	defaultParams.setMargins(40, 10, 30, 10);


	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		850, 300);
	answerParams.setMargins(45, 2, 30, 2);

	layout.addView(getQuestionView(question, activity));
	layout.addView(childLayout, defaultParams);

	Logger.warn("", "ORD - choices are:" + ordQuestion.getCorrectAnswer());
	
	if (ordQuestion != null && ordQuestion.getCorrectAnswer() != null
		&& ordQuestion.getCorrectAnswer().getChoices() != null) {
	    final ListView orderingQuestionList = new ListView(activity);
	    orderingQuestionList.setScrollbarFadingEnabled(false);
	    orderingQuestionList.setCacheColorHint(Color.parseColor("#FFFECA"));
	    final OrderingQuestionAdapter orderingTeacher = new OrderingQuestionAdapter(
		    activity, ordQuestion.getCorrectAnswer().getChoices(),
		    "TEACHER");
	    orderingQuestionList.setAdapter(orderingTeacher);
	    layout.addView(orderingQuestionList, defaultParams);
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
	    final SelectQuestionsActivity activity, LinearLayout layout) {
	int id = 1;
	if (layout == null) {
	    Logger.warn("QuesionsUI", "BUILDMFQ - layout is null");
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    1120,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	}
	 layout.setOrientation(LinearLayout.VERTICAL);
	 layout.setLayoutParams(new LayoutParams(1120, 350));
	 
	final TextView teacherAnswerTextView = new TextView(activity);
	teacherAnswerTextView.setId(id++);
	teacherAnswerTextView.setText("Teacher Answer:       ");
	teacherAnswerTextView.setTextColor(Color.parseColor("#A52A2A"));
	//create text view for teacher alloted marks
	//teacherAnswerTextView.setText("Teacher allotted marks :"+question.getMarksAlloted());
	final TextView teacherAllotedMarks = new TextView(activity);
	teacherAllotedMarks.setId(id++);
	teacherAllotedMarks.setText("Allotted marks :"+question.getMarksAlloted());
	teacherAllotedMarks.setTextColor(Color.parseColor("#A52A2A"));
	final LinearLayout childLayout = new LinearLayout(activity);
	final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(20, 20, 0, 0);
        childLayout.setOrientation(LinearLayout.HORIZONTAL);
        childLayout.addView(teacherAnswerTextView, layoutParams);
        childLayout.addView(teacherAllotedMarks, layoutParams);

	final MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) question;

	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		1000,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	defaultParams.setMargins(30, 10, 30, 5);

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		1000, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	answerParams.setMargins(30,10, 0, 0);
	
	layout.addView(getQuestionView(question, activity));
	layout.addView(childLayout, defaultParams);

	//add alloted marks view

	if (multipleChoiceQuestion != null
		&& multipleChoiceQuestion.getCorrectAnswer() != null
		&& multipleChoiceQuestion.getCorrectAnswer().getChoices() != null) {
	    final ListView matchTheFollowingList = new ListView(activity);
	    matchTheFollowingList.setScrollbarFadingEnabled(false);
	    matchTheFollowingList.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
	    matchTheFollowingList.setCacheColorHint(Color.parseColor("#FFFECA"));
	    final MatchtheFollowingAdapter matchTheFollowingTeacher = new MatchtheFollowingAdapter(
		    activity, multipleChoiceQuestion.getCorrectAnswer()
		    .getChoices());
	    matchTheFollowingList.setAdapter(matchTheFollowingTeacher);
	    matchTheFollowingTeacher.notifyDataSetChanged();
	    layout.addView(matchTheFollowingList, defaultParams);
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
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	defaultParams.setMargins(40, 0, 5, 0);

	final LinearLayout questionsLayout = new LinearLayout(activity);
	questionsLayout.setOrientation(LinearLayout.VERTICAL);

	// Initialize default parameters
	final LinearLayout.LayoutParams questionsParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	questionsParams.setMargins(30, 0, 15, 0);

	final TextView questionView = new TextView(activity);
	questionView
	.setText("Q"
		+ (q.getQuestionOrderNo() > 0 ? q
			.getQuestionOrderNo() : "") + ": "
			+ q.getQuestion());// +"      Marks allotted : "+q.getMarksAlloted());
	questionView.setTextColor(Color.parseColor("#000000"));
	questionView.setId(0);
	questionView.setLayoutParams(defaultParams);
	questionView.setTextSize(18);
	questionView.setMovementMethod(new ScrollingMovementMethod());
	questionView.setMaxLines(3);

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

	    //question.setLayoutParams(new LayoutParams(200,00));
	    Log.i("____________________",
		    "MATCH - answer is:"
			    + item.get(position).getMatch_FieldThree());
	    Sno.setText(item.get(position).getMatch_FieldOne());
	    question.setText(item.get(position).getTitle());
	    answerChoice.setText(item.get(position).getMatch_FieldFour());
	    answer.setText(item.get(position).getMatch_FieldThree());

	    return view;

	}

	

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

}
