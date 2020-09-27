package com.pearl.ui.helpers.examination;


import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
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
 * The Class RejectedTestUI.
 */
public class RejectedTestUI {

    /** The Rejected test ui. */
    private static RejectedTestUI RejectedTestUI = null;
    
    /** The answer choice list. */
    private List<AnswerChoice> answerChoiceList;
    
    /** The choice. */
    private AnswerChoice choice;

    /**
     * Instantiates a new rejected test ui.
     */
    private RejectedTestUI() {
    }

    /**
     * Gets the single instance of RejectedTestUI.
     *
     * @return single instance of RejectedTestUI
     */
    public static RejectedTestUI getInstance() {
	if (RejectedTestUI == null) {
	    RejectedTestUI = new RejectedTestUI();
	}

	return RejectedTestUI;
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
	    return buildShortAnswerQuestion(question, activity, layout);
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
	int id = 1;

	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	}
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	defaultParams.setMargins(40, 10, 30, 10);

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	answerParams.setMargins(45, 10, 30, 10);
	// answerParams.height = 100;

	final EditText teacherAnswer = new EditText(activity);
	teacherAnswer.setId(id++);
	teacherAnswer.setBackgroundColor(Color.parseColor("#FFFFFF"));
	teacherAnswer.setEnabled(false);
	if(question != null && question.getCorrectAnswer() != null){
		teacherAnswer.setText(question.getCorrectAnswer().getAnswerString());
	}else
		teacherAnswer.setText("");

	// Add to layout
	layout.addView(getQuestionView(question, activity));
	layout.addView(teacherAnswer, answerParams);

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
	int id = 1;
	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	}
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT, 130);
	defaultParams.setMargins(40, 10, 30, 10);

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	answerParams.setMargins(45, 10, 30, 10);
	// answerParams.height = 100;

	final EditText teacherAnswer = new EditText(activity);
	teacherAnswer.setId(id++);
	teacherAnswer.setEnabled(false);
	teacherAnswer.setBackgroundColor(Color.parseColor("#FFFFFF"));
	if(question != null && question.getCorrectAnswer() != null){
		teacherAnswer.setText(question.getCorrectAnswer().getAnswerString());
	}else
		teacherAnswer.setText("");

	// Add to layout
	layout.addView(getQuestionView(question, activity));
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
	    final FragmentActivity activity, LinearLayout layout) {
    	int id = 1;
    	String teacherAnswer = "";
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
    	outerParams.setMargins(45, 5, 30, 50);

    	final LinearLayout innerLayout = new LinearLayout(activity);
    	innerLayout.setOrientation(LinearLayout.VERTICAL);

    	// Initialize default parameters
    	final LinearLayout.LayoutParams inlineParams = new LinearLayout.LayoutParams(
    		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
    		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    	inlineParams.setMargins(5, 10, 30, 0);

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
    	
    	final TextView teacherComment = new TextView(activity);
    	teacherComment.setId(id++);
    	teacherComment.setTextSize(16);
    	if(question != null && question.getDescription() != null){
    			teacherComment.setText(question.getDescription());
    		}
    	    else{
    	    	teacherComment.setText("No Comments");
    	    }

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

    	if (finalTeacherAnswer.endsWith(",")) {
    	    finalTeacherAnswer = finalTeacherAnswer.substring(0,
    		    finalTeacherAnswer.length() - 1);
    	}
    	teacherAnswerText.setText(finalTeacherAnswer);

    	innerLayout.addView(teacherAnswerTextVIew, inlineParams);
    	innerLayout.addView(teacherAnswerText, inlineParams);
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
	int id = 1;
	final MultipleChoiceQuestion tfQuestion = (MultipleChoiceQuestion) question;

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
	}

	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	defaultParams.setMargins(40, 5, 50, 5);
	
	final RadioGroup.LayoutParams radioParams = new RadioGroup.LayoutParams(
			android.view.ViewGroup.LayoutParams.FILL_PARENT,
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	radioParams.setMargins(36, 5, 50, 5);

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	answerParams.setMargins(36, 5, 50, 5);
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

	    // Create Option Radio buttons and add to Options Container Layout
	    final RadioGroup radioGroup = new RadioGroup(activity);
	    radioGroup.setId(id++);
	    radioGroup.setOrientation(LinearLayout.VERTICAL);
	    //radioGroup.setLayoutParams(radioParams);

	    if (choices != null) {
		for (final AnswerChoice choice : choices) {
		    Logger.info("QuestionsUI - TrueorFalse",
			    "Preparing choice as " + choice.toString());

		    final RadioButton option = new RadioButton(activity);
		    option.setId(id++);// TODO verify
		    option.setTextColor(Color.parseColor("#000000"));
		    option.setText(choice.getTitle());
		    option.setTextSize(18);
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

		    radioGroup.addView(option, radioParams);
		}
		final ScrollView sv = new ScrollView(activity);
		sv.addView(radioGroup);
		layout.addView(sv, defaultParams);
	    }

	}
	if(tfQuestion != null && tfQuestion.getChoices() != null) {

	    Logger.info("BuildTrueOfFalse Question",
		    "STUDENT - Question to build is " + question.getQuestion()
		    + " with " + tfQuestion.getChoices().size()
		    + " choices");
	    Logger.warn("BuildTrueOfFalse Question",
		    "STUDENT - Question details " + question.toString());
	    final List<AnswerChoice> choices = tfQuestion.getChoices();

	    // Create Option Radio buttons and add to Options Container Layout
	    final RadioGroup radioGroup = new RadioGroup(activity);
	    radioGroup.setId(id++);
	    radioGroup.setOrientation(LinearLayout.VERTICAL);
	    //radioGroup.setLayoutParams(radioParams);

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
			    "ANSWER - radio button visibility for student : "
				    + choice.isSelected() + "  title is "
				    + choice.getTitle());
		    if (choice.isTeacherSelected()) {
			option.setChecked(true);
		    } else {
			option.setChecked(false);
		    }
		    radioGroup.addView(option, radioParams);
		}
		final ScrollView sv = new ScrollView(activity);
		sv.addView(radioGroup);
		layout.addView(sv, defaultParams);
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
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		700, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	defaultParams.setMargins(40, 10, 30, 10);

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		750, 400);
	answerParams.setMargins(45, 2, 30, 2);

	layout.addView(getQuestionView(question, activity));
	Logger.warn("", "ORD - choices are:" + ordQuestion.getCorrectAnswer());

	if (ordQuestion != null) {
	    final ListView orderingQuestionList = new ListView(activity);
	    final OrderingQuestionAdapter orderingTeacher = new OrderingQuestionAdapter(
		    activity, ordQuestion.getChoices(), "TEACHER");
	    orderingQuestionList.setAdapter(orderingTeacher);
	    orderingQuestionList.setCacheColorHint(Color.parseColor("#E9E9E9"));
	    layout.addView(orderingQuestionList, answerParams);
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
	if (layout == null) {
	    Logger.warn("QuesionsUI", "BUILDMFQ - layout is null");
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
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

	final MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) question;

	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	defaultParams.setMargins(40, 10, 30, 10);

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		750, 400);
	answerParams.setMargins(45, 2, 10, 2);

	layout.addView(getQuestionView(question, activity));

	if (multipleChoiceQuestion != null) {
	    Logger.warn("TEACHER EXAMUI ", "BUILD MTF - layout is  not null");
	    final ListView matchTheFollowingList = new ListView(activity);
	    final MatchtheFollowingAdapter matchTheFollowingTeacher = new MatchtheFollowingAdapter(
		    activity, multipleChoiceQuestion.getChoices());
	    matchTheFollowingList.setAdapter(matchTheFollowingTeacher);
	    matchTheFollowingList.setCacheColorHint(Color.parseColor("#E9E9E9"));
	    layout.addView(matchTheFollowingList, answerParams);
	}

	/*
	 * if (multipleChoiceQuestion != null &&
	 * multipleChoiceQuestion.getStudentAnswer().getChoices() != null) {
	 */
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
	defaultParams.setMargins(40, 10, 20, 10);

	final LinearLayout questionsLayout = new LinearLayout(activity);
	questionsLayout.setOrientation(LinearLayout.HORIZONTAL);

	// Initialize default parameters
	final LinearLayout.LayoutParams questionsParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	questionsParams.setMargins(30, 10, 15, 10);

	final TextView questionView = new TextView(activity);
	questionView
	.setText("Q "
		+ (q.getQuestionOrderNo() > 0 ? q
			.getQuestionOrderNo() : "") + ": "
			+ q.getQuestion());// +"      Marks Alloted : "+q.getMarksAlloted());
	questionView.setTextColor(Color.parseColor("#000000"));
	questionView.setId(0);
	questionView.setLayoutParams(defaultParams);
	questionView.setTextSize(28);
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
	    question.setLayoutParams(new android.widget.LinearLayout.LayoutParams(320, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	    answerChoice.setLayoutParams(new  android.widget.LinearLayout.LayoutParams(320, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	    question.setPadding(20, 0, 20, 20);
	    answerChoice.setPadding(20, 0, 0, 0);
	    Log.i("____________________",
		    "MATCH - answer is:"
			    + item.get(position).getMatch_FieldThree());
	    Sno.setText(item.get(position).getMatch_FieldOne());
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
}
