package com.pearl.ui.helpers.examination;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.activities.QuestionsActivity;
import com.pearl.application.ApplicationData;
import com.pearl.examination.Answer;
import com.pearl.examination.AnswerChoice;
import com.pearl.examination.Question;
import com.pearl.examination.questiontype.FillInTheBlankQuestion;
import com.pearl.examination.questiontype.LongAnswerQuestion;
import com.pearl.examination.questiontype.MultipleChoiceQuestion;
import com.pearl.examination.questiontype.OrderingQuestion;
import com.pearl.examination.questiontype.ShortAnswerQuestion;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class QuestionsUI.
 */
public class QuestionsUI {
    
    /** The questions ui. */
    private static QuestionsUI questionsUI = null;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The answer choice list. */
    private List<AnswerChoice> answerChoiceList;
    
    /** The ans choice. */
    private AnswerChoice ansChoice;
    
    /** The ans choice list. */
    List<AnswerChoice> ansChoiceList;
    
    /** The answer. */
    private Answer answer;
    
    /** The appended answer for fib. */
    private final String appendedAnswerForFIB = "";
    
    /** The Mtf q. */
    private MultipleChoiceQuestion MtfQ;
    
    /** The ord. */
    private OrderingQuestion ord;
    
    /** The ans. */
    private Answer ans;
    
    /** The question type. */
    String questionType = null;
    
    /** The choice. */
    private AnswerChoice choice;
    
    /** The _view status. */
    boolean _viewStatus = false;
    
    /** The data. */
    String[] data;
    
    /** The _hash map. */
    public HashMap<Integer, AnswerChoice> _hashMap;
    
    /** The hash map ord. */
    public HashMap<Integer, AnswerChoice> hashMapOrd;

    // List<String> _matchAnswers;

    /**
     * Instantiates a new questions ui.
     */
    public QuestionsUI() {
	Logger.warn("", "-----Constructor");
	_hashMap = new HashMap<Integer, AnswerChoice>();
	hashMapOrd = new HashMap<Integer, AnswerChoice>();
    }

    /**
     * Gets the single instance of QuestionsUI.
     *
     * @return single instance of QuestionsUI
     */
    public static QuestionsUI getInstance() {
	questionsUI = new QuestionsUI();

	return questionsUI;
    }

    /**
     * Builds the.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout build(Question question, QuestionsActivity activity,
	    LinearLayout layout) {

	question.setViewed(true);

	Logger.info("QuestionsUI",
		"Building question Type " + question.getType());

	if (Question.MULTIPLECHOICE_QUESTION.equals(question.getType().trim())
		|| Question.TRUE_OR_FALSE_QUESTION.equals(question.getType()
			.trim())) {
	    return buildTrueOrFalse(question, activity, layout);
	} else if (Question.SHORT_ANSWER_QUESTION.equals(question.getType()
		.trim())) {
	    questionType = "SAQ";
	    return buildShortAnswerQuestion(question, activity, layout);
	} else if (Question.LONG_ANSWER_QUESTION.equals(question.getType()
		.trim())) {
	    questionType = "LAQ";
	    return buildLongAnswerQuestion(question, activity, layout);
	} else if (Question.FILL_IN_THE_BLANK_QUESTION.equals(question
		.getType().trim())) {
	    questionType = "FIB";
	    return buildFillInTheBlanksQuestion(question, activity, layout);
	} else if (Question.ORDERING_QUESTION.equals(question.getType().trim())) {
	    questionType = "OQ";
	    return buildOrderingQuestion(question, activity, layout);
	} else if (Question.MATCH_THE_FOLLOWING.equals(question.getType()
		.trim())) {
	    questionType = "MTF";
	    return buildMatchtheFollowing(question, activity, layout);
	} else {
	    questionType = "Q";
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
	    final QuestionsActivity activity, LinearLayout layout) {
	final ShortAnswerQuestion shortAnswerQuestion = (ShortAnswerQuestion) question;
	answerChoiceList = new LinkedList<AnswerChoice>();
	question.setViewed(true);
	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(LayoutParams(700, 505));
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    new LinearLayout.LayoutParams(

		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.MATCH_PARENT);

	    layout.setOrientation(LinearLayout.VERTICAL);
	    layout.setGravity(0);
	}
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		700, 505);

	defaultParams.setMargins(30, 10, 5, 60);

	// Create Hints VIew
	final TextView hintsTextView = new TextView(activity);
	hintsTextView.setId(2);

	if (null != question.getHint() && "".equals(question.getHint())) {
	    hintsTextView.setVisibility(View.GONE);
	} else {
	    hintsTextView.setText(question.getHint().toString());
	}

	// Create Answer Edit Text view
	final EditText answerEditText = new EditText(activity);
	answerEditText.setId(3);
	answerEditText.setHint("Your Answer here..");
	answerEditText.setGravity(Gravity.TOP);

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	answerParams.setMargins(80, 5, 65, 5);
	answerParams.height = 200;

	// Add to layout
	layout.addView(getQuestionView(question, activity));
	layout.addView(hintsTextView, defaultParams);
	layout.addView(answerEditText, answerParams);

	answerEditText.setText(question.getStudentAnswer().getAnswerString());

	answerEditText.addTextChangedListener(new TextWatcher() {
	    @Override
	    public void onTextChanged(CharSequence text, int arg1, int arg2,
		    int arg3) {
		Logger.info("Questions UI", "SAQ - Answer is set as "
			+ answerEditText.getText().toString());
		if (answerEditText.getText().toString().trim().equals("")) {
		    question.setIsAnswered(false);
		} else {
		    question.setIsAnswered(true);
		}
		// appData.getCurrentExam().getQuestions().get(1).setis
		final Answer ans = new Answer();
		ans.setAnswerString(answerEditText.getText().toString());

		shortAnswerQuestion.setAnswer(ans);

		Logger.info("Questions UI", "Answer is set as "
			+ answerEditText.getText().toString());
	    }

	    @Override
	    public void beforeTextChanged(CharSequence arg0, int arg1,
		    int arg2, int arg3) {

	    }

	    @Override
	    public void afterTextChanged(Editable arg0) {
		Logger.warn("", "SAQ - in after text changed");
	    }
	});
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
     * Layout params.
     *
     * @param i the i
     * @param j the j
     * @return the android.view. view group. layout params
     */
    private android.view.ViewGroup.LayoutParams LayoutParams(int i, int j) {
	// TODO Auto-generated method stub
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
    public LinearLayout buildLongAnswerQuestion(final Question question,
	    final QuestionsActivity activity, LinearLayout layout) {
	final LongAnswerQuestion shortAnswerQuestion = (LongAnswerQuestion) question;
	question.setViewed(true);
	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    new LinearLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT);

	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    new LinearLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT);
	    layout.setOrientation(LinearLayout.VERTICAL);
	    layout.setGravity(0);
	}
	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.FILL_PARENT);
	defaultParams.setMargins(30, 10, 5, 50);

	// Create Hints VIew
	final TextView Text = new TextView(activity);
	Text.setId(2);
	if (null != question.getHint() && "".equals(question.getHint())) {
	    Text.setVisibility(View.GONE);
	} else {
	    Text.setText(question.getHint().toString());
	}

	// Create Answer Edit Text view
	final EditText answerEditText = new EditText(activity);
	answerEditText.setId(3);
	answerEditText.setHint("Your Answer here..");
	answerEditText.setGravity(Gravity.TOP);

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	answerParams.setMargins(80, 5, 65, 5);
	answerParams.height = 200;

	// Add to layout
	layout.addView(getQuestionView(question, activity));
	layout.addView(Text, defaultParams);
	layout.addView(answerEditText, answerParams);

	answerEditText.setText(question.getStudentAnswer().getAnswerString());

	answerEditText.addTextChangedListener(new TextWatcher() {
	    @Override
	    public void onTextChanged(CharSequence text, int arg1, int arg2,
		    int arg3) {
		if (answerEditText.getText().toString().trim().equals("")) {
		    question.setIsAnswered(false);
		} else {
		    question.setIsAnswered(true);
		}

		final Answer ans = new Answer();
		ans.setAnswerString(answerEditText.getText().toString());

		shortAnswerQuestion.setAnswer(ans);

		Logger.info("Questions UI", "Answer is set as "
			+ answerEditText.getText().toString());
	    }

	    @Override
	    public void beforeTextChanged(CharSequence arg0, int arg1,
		    int arg2, int arg3) {

	    }

	    @Override
	    public void afterTextChanged(Editable arg0) {
	    }
	});
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
	    final QuestionsActivity activity, LinearLayout layout) {
	final FillInTheBlankQuestion fbQuestion = (FillInTheBlankQuestion) question;
	ansChoice = new AnswerChoice();
	ansChoiceList = new LinkedList<AnswerChoice>();
	answer = new Answer();
	question.setViewed(true);
	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);

	}
	final String quest = question.getQuestion();

	int start = 0;
	int end = quest.indexOf("___", start);
	int count = 0;
	final Display display = activity.getWindowManager().getDefaultDisplay();
	final int maxWidth = display.getWidth() - 10;
	LinearLayout linearLayout = new LinearLayout(activity);
	final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	params.setMargins(30, 20, 5, 10);
	linearLayout.setLayoutParams(params);
	linearLayout.setOrientation(LinearLayout.HORIZONTAL);
	int widthSoFar = 0;

	final LinearLayout.LayoutParams questionParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	questionParams.setMargins(15, 10, 5, 20);

	final LinearLayout.LayoutParams marksParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	marksParams.setMargins(900, 10, 5, 0);

	final TextView questionNum = new TextView(activity);
	final TextView marksAlloted = new TextView(activity);

	questionNum.setText("Q "
		+ (question.getQuestionOrderNo() >= 0 ? question
			.getQuestionOrderNo() : "") + ": Fill in the blanks ");
	// + "                       [" + question.getMarksAlloted() + "M]");
	questionNum.setTextColor(Color.parseColor("#000000"));
	// questionNum.setPadding(0, 0, 50, 10);
	marksAlloted.setText("[ " + question.getMarksAlloted() + "M ]");
	marksAlloted.setTextColor(Color.parseColor("#000000"));
	questionNum.setTextSize(26);
	marksAlloted.setTextSize(26);

	// marksAlloted.setPadding(100, 10, 0, 0);
	layout.addView(marksAlloted, marksParams);
	layout.addView(questionNum, questionParams);

	while (end >= 0) {
	    final TextView text = new TextView(activity);
	    final EditText blank = new EditText(activity);
	    blank.setWidth(200);
	    blank.setMaxLines(1);

	    // text.setId(inc++);
	    if (quest.startsWith("___"))
		text.setText(quest.substring(start, end));
	    else
		text.setText(quest.substring(start + 1, end));

	    text.setTextColor(Color.parseColor("#000000"));
	    text.setTextSize(24);

	    String str = null;
	    if (text != null && text.getText() != null) {
		str = text.getText().toString();
	    }
	    if (start == 0) {
		text.setText(quest.substring(start, end) + "");

	    }
	    text.measure(0, 0);
	    blank.measure(0, 0);

	    widthSoFar += text.getMeasuredWidth() + blank.getMeasuredWidth();

	    Logger.error("", "BUILDFIB - str:" + str);
	    if (widthSoFar >= maxWidth) {
		Logger.warn("", "BUILDFIB - create layout");
		layout.addView(linearLayout);

		linearLayout = new LinearLayout(activity);
		final LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(
			android.view.ViewGroup.LayoutParams.FILL_PARENT,
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		innerParams.setMargins(30, 10, 30, 20);
		linearLayout.setLayoutParams(innerParams);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);

		linearLayout.addView(text);
		linearLayout.addView(blank);
		widthSoFar = text.getMeasuredWidth() + blank.getMeasuredWidth();
		Logger.info("", "BUILDFIB - width so far:" + widthSoFar);
	    } else {
		Logger.warn("", "BUILDFIB - adding");
		linearLayout.addView(text);
		linearLayout.addView(blank);
	    }

	    if (fbQuestion.getAnswerAt(count) != null) {
		blank.setText(fbQuestion.getAnswerAt(count) + "");
	    }
	    final int pos = count;

	    blank.setText(fbQuestion.getAnswerAt(pos));

	    blank.addTextChangedListener(new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence text, int arg1,
			int arg2, int arg3) {
		    fbQuestion.setAnswerAt(pos, blank.getText().toString());
		    if (blank.getText().toString().trim().equals("")) {
			fbQuestion.setIsAnswered(false);
		    } else {
			fbQuestion.setIsAnswered(true);
		    }
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1,
			int arg2, int arg3) {

		}

		@Override
		public void afterTextChanged(Editable arg0) {
		}
	    });
	    blank.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {

		    fbQuestion.setAnswerAt(pos, blank.getText().toString());
		    if (blank.getText().toString().trim().equals("")) {
			fbQuestion.setIsAnswered(false);
		    } else {
			fbQuestion.setIsAnswered(true);
		    }
		}
	    });

	    layout.setOnTouchListener(new OnTouchListener() {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {

		    fbQuestion.setAnswerAt(pos, blank.getText().toString());
		    if (blank.getText().toString().trim().equals("")) {
			fbQuestion.setIsAnswered(false);
		    } else {
			fbQuestion.setIsAnswered(true);
		    }
		    return false;
		}
	    });

	    count++;
	    if (quest.startsWith("___")) {
		start = end + 3;
	    } else
		start = end + 2;
	    if (start <= quest.length()) {
		end = quest.indexOf("___", start);
	    }
	}

	// adding rest of the question string
	final TextView text = new TextView(activity);
	if (start < quest.length()) {
	    if (quest.startsWith("___")) {
		text.setText(quest.substring(start));
	    } else
		text.setText(quest.substring(start + 1));
	} else
	    text.setText(quest.substring(start));
	text.setTextColor(Color.parseColor("#000000"));
	text.setTextSize(26);
	if (widthSoFar >= maxWidth) {
	    Logger.warn("tag",
		    "BUILDFIB - creating layout to add remaining text");
	    layout.addView(linearLayout);

	    linearLayout = new LinearLayout(activity);
	    final LinearLayout.LayoutParams innerParams = new LinearLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	    innerParams.setMargins(30, 10, 30, 20);
	    linearLayout.setLayoutParams(innerParams);
	    linearLayout.setOrientation(LinearLayout.HORIZONTAL);

	    linearLayout.addView(text);
	    widthSoFar = text.getMeasuredWidth();
	} else {
	    Logger.warn("tag", "BUILDFIB - add remaining text");
	    linearLayout.addView(text);
	}
	layout.addView(linearLayout);

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
	    final QuestionsActivity activity, LinearLayout layout) {
    	//List<AnswerChoice> answerChoiceList = new ArrayList<AnswerChoice>();
    	ans = new Answer();
	if (question.getType().equals(Question.MULTIPLECHOICE_QUESTION)) {
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
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT);
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    Logger.warn("QuesionsUI", "BUILDTFQ - layout is  not null");
	    new LinearLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT);
	    layout.setOrientation(LinearLayout.VERTICAL);
	}

	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
	defaultParams.setMargins(70, 10, 5, 50);
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

		if (choice.isSelected()) {
		    option.setChecked(true);
		    tfQuestion.setAnswer(choice);
		} else {
		    option.setChecked(false);
		}

		option.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
			}
		});
		
		
		option.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			tfQuestion.setAnswer(choice);

			choice.setSelected(true);
			Logger.warn("QuestionsUI", "Choiceeeeeee - "+tfQuestion.getAnswer().getChoices());
			Logger.warn("QuestionsUI", "2 Choiceeeeeee - "+choices);
			for (final AnswerChoice answerChoice : choices) {
			    if (answerChoice.getId() != choice.getId()) {
				answerChoice.setSelected(false);
			    }
			}

			tfQuestion.setIsAnswered(true);
			/*Answer studentAnswer = new Answer();
			studentAnswer.setAnswerString("");
			studentAnswer.setChoices(choice);
			ans.setChoices(choice);
			tfQuestion.setStudentAnswer(studentAnswer);*/

			Logger.verbose("Questions UI", "TF Question:- "
				+ tfQuestion.toString()
				+ " | Current Choice:- " + choice.toString());
			answerChoiceList = new LinkedList<AnswerChoice>();
			for (int j = 0; j < choices.size(); j++) {
				final AnswerChoice studentChoice = new AnswerChoice();
				//studentChoice.setSelected(choice.isSelected());
				if (studentChoice.getId() != choices.get(j).getId()) {
					studentChoice.setSelected(false);
				    }
				if (null != choices.get(j)) {
					Logger.warn("QuestionsUI", "Choiceeeeeee in loop- "+choices);
				    studentChoice.setPosition(choices.get(j).getPosition());
				    studentChoice
				    .setTitle(choices.get(j).getTitle());
				    studentChoice.setId(choices.get(j).getId());
				    studentChoice.setSelected(choices.get(j).isSelected());

				} else {
				    studentChoice.setTitle(choices.get(j).getTitle());
				    studentChoice.setId(choices.get(j).getId());
				    studentChoice.setPosition(0);

				}
				answerChoiceList.add(studentChoice);
			    }
		    ans.setChoices(answerChoiceList);
		    tfQuestion.setStudentAnswer(ans);
		    Logger.error("QuestionsUI", "Final choice:"+tfQuestion.getStudentAnswer());
		    }
		});
		radioGroup.addView(option);
	    }
	    final ScrollView sv = new ScrollView(activity);
	    sv.addView(radioGroup);
	    layout.addView(getQuestionView(question, activity));
	    layout.addView(sv, defaultParams);
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
	    final QuestionsActivity activity, LinearLayout layout) {
	question.setViewed(true);
	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    new LinearLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT);
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    /*
	     * layout.setLayoutParams(new
	     * android.widget.LinearLayout.LayoutParams( 700, 600));
	     * layout.setOrientation(LinearLayout.VERTICAL);
	     */
	}

	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.FILL_PARENT);
	defaultParams.setMargins(40, 5, 60, 30);

	List<AnswerChoice> factsList = null;

	ord = (OrderingQuestion) question;
	ListView orderingQuestionsList = null;

	Logger.warn("", "ord is:" + ord);
	if (ord != null) {
	    factsList = ord.getChoices();
	    orderingQuestionsList = new ListView(activity);
	    OrderingQuestionAdapter ordAdapter;

	    if (ord.getStudentAnswer().getChoices() != null) {
		Logger.info("", "MTFQ Question details are========in IF");
		ordAdapter = new OrderingQuestionAdapter(activity,
			android.R.layout.simple_selectable_list_item, ord
			.getStudentAnswer().getChoices());

	    } else {
		Logger.info("", "MTFQ Question details are========IN else");
		ordAdapter = new OrderingQuestionAdapter(activity,
			android.R.layout.simple_selectable_list_item, factsList);
		// hashMapOrd = new HashMap<Integer, AnswerChoice>();
	    }
	    orderingQuestionsList.setAdapter(ordAdapter);
	    orderingQuestionsList.setCacheColorHint(Color.parseColor("#EFF0ED"));
	}

	// Add to layout
	layout.addView(getQuestionView(question, activity));
	layout.addView(orderingQuestionsList, defaultParams);
	return layout;
    }

    /**
     * Gets the question view.
     *
     * @param q the q
     * @param activity the activity
     * @return the question view
     */
    private View getQuestionView(Question q, Activity activity) {
	// Initialize default parameters
	LinearLayout questionLayout = null;
	LinearLayout marksandQestiontype = null;
	LinearLayout questionNu = null;
	q.setViewed(true);
	if (questionLayout == null) {
	    // Initialize Linear Layout
	    questionLayout = new LinearLayout(activity);
	    new LinearLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT, 85);
	    questionLayout.setOrientation(LinearLayout.HORIZONTAL);

	    questionNu = new LinearLayout(activity);
	    new LinearLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT);
	    questionNu.setOrientation(LinearLayout.VERTICAL);

	    marksandQestiontype = new LinearLayout(activity);
	    new LinearLayout.LayoutParams(60,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	    marksandQestiontype.setOrientation(LinearLayout.HORIZONTAL);
	}

	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	defaultParams.setMargins(15, 10, 0, 50);

	final LinearLayout.LayoutParams defaultParams1 = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	defaultParams1.setMargins(0, 10, 15, 50);

	final LinearLayout.LayoutParams questionTypeparams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	questionTypeparams.setMargins(50, 10, 15, 0);

	final LinearLayout.LayoutParams marksParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	marksParams.setMargins(900, 10, 15, 0);

	final TextView questionNo = new TextView(activity);
	final TextView questionView = new TextView(activity);
	final TextView marksAlloted = new TextView(activity);
	final TextView quesType = new TextView(activity);

	questionNo.setTextSize(24);
	questionView.setTextSize(24);
	marksAlloted.setTextSize(24);
	quesType.setTextSize(24);

	questionNo.setTextColor(Color.parseColor("#000000"));
	questionView.setTextColor(Color.parseColor("#000000"));
	marksAlloted.setTextColor(Color.parseColor("#000000"));
	quesType.setTextColor(Color.parseColor("#000000"));

	questionView.setMovementMethod(new ScrollingMovementMethod());
	questionView.setMaxLines(6);

	/*String questions = StringEscapeUtils
				.unescapeEcmaScript(q.getQuestion());*/
	final String questions = q.getQuestion();

	questionNo
	.setText("Q "
		+ (q.getQuestionOrderNo() > 0 ? q
			.getQuestionOrderNo() : "") + ": ");
	questionView.setText(questions);
	marksAlloted.setText("[ " + q.getMarksAlloted() + "M ]");

	quesType.setText(questionType);
	// questionNo.setGravity(Gravity.LEFT);
	questionNu.addView(marksandQestiontype);
	questionNu.addView(questionLayout);
	questionLayout.addView(questionNo, defaultParams);
	questionLayout.addView(questionView, defaultParams1);
	marksandQestiontype.addView(marksAlloted, marksParams);
	marksandQestiontype.addView(quesType, questionTypeparams);
	return questionNu;
    }

    /**
     * HELPER ADAPTERS.
     */
    private class FactsArrayAdapter extends BaseAdapter {
	
	/** The m inflater. */
	private final LayoutInflater mInflater;
	
	/** The items. */
	private final List<AnswerChoice> items;

	/**
	 * Instantiates a new facts array adapter.
	 *
	 * @param context the context
	 * @param factsList the facts list
	 */
	public FactsArrayAdapter(Context context, List<AnswerChoice> factsList) {
	    mInflater = LayoutInflater.from(context);
	    items = factsList;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
	    return items.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int pos) {
	    return pos;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int pos) {
	    return pos;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int pos, View convertView, ViewGroup parent) {

	    ViewHolder holder;

	    if (convertView == null) {
		convertView = mInflater.inflate(
			android.R.layout.simple_list_item_1, null);
		// convertView.setBackgroundColor(Color.WHITE);
		holder = new ViewHolder();
		holder.item = (TextView) convertView
			.findViewById(android.R.id.text1);
		convertView.setTag(holder);
	    } else {
		holder = (ViewHolder) convertView.getTag();
	    }

	    holder.item.setText(items.get(pos).getTitle());
	    Logger.warn("QuestionsUI", "GetTitle---->"
		    + items.get(pos).getTitle());
	    holder.item.setTextColor(Color.parseColor("#000000"));

	    return convertView;
	}

	/**
	 * The Class ViewHolder.
	 */
	class ViewHolder {
	    
    	/** The item. */
    	TextView item;
	}
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
	    final QuestionsActivity activity, LinearLayout layout) {
	question.setViewed(true);
	if (layout == null) {
	    Logger.warn("QuesionsUI", "BUILDMFQ - layout is null");
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	}
	MtfQ = (MultipleChoiceQuestion) question;
	ans = new Answer();
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.FILL_PARENT);
	defaultParams.setMargins(40, 10, 10, 20);

	final LinearLayout.LayoutParams inlineParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	inlineParams.setMargins(30, 10, 60, 40);

	final ListView mulitpleChoiceList = new ListView(activity);
	MatchtheFollowingAdapter matchTheFollowing;

	if (question.getStudentAnswer().getChoices() != null) {
	    Logger.info("", "MTFQ Question details are========in IF");
	    matchTheFollowing = new MatchtheFollowingAdapter(activity,
		    android.R.layout.simple_selectable_list_item, question
		    .getStudentAnswer().getChoices());

	} else {
	    Logger.info("", "MTFQ Question details are========IN else");
	    matchTheFollowing = new MatchtheFollowingAdapter(activity,
		    android.R.layout.simple_selectable_list_item,
		    question.getChoices());
	    // _hashMap=new HashMap<Integer, AnswerChoice>();
	}
	mulitpleChoiceList.setAdapter(matchTheFollowing);
	mulitpleChoiceList.setCacheColorHint(Color.parseColor("#EFF0ED"));
	Logger.info("",
		"MTFQ Question details are========" + question.getQuestion());
	Logger.info(
		"",
		"MTFQ Question details are======OrderNo"
			+ question.getQuestionOrderNo());

	MtfQ.setQuestion(question.getQuestion());
	MtfQ.setQuestionOrderNo(question.getQuestionOrderNo());
	layout.addView(getQuestionView(question, activity));
	layout.addView(mulitpleChoiceList, defaultParams);
	return layout;
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
	
	/** The ctx. */
	Context ctx;
	
	/** The match_answers. */
	List<String> match_answers = new LinkedList<String>();
	
	/** The layout resource id. */
	int layoutResourceId;
	
	/** The view holder. */
	MatchTheFollowingViewHolder viewHolder = null;

	/**
	 * Instantiates a new matchthe following adapter.
	 *
	 * @param context the context
	 * @param resource the resource
	 * @param choices the choices
	 */
	public MatchtheFollowingAdapter(Context context, int resource,
		List<AnswerChoice> choices) {
	    super(context, resource, choices);
	    Log.i("____________________", "ITEM VALUE----" + choices.size());
	    ctx = context;
	    item = choices;
	    layoutResourceId = resource;
	    data = new String[item.size()];
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
	    if (view == null) {
		viewHolder = new MatchTheFollowingViewHolder();
		final LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
		view = inflater.inflate(R.layout.matchthefollowing_listrow,	parent, false);
		viewHolder.Sno = (TextView) view.findViewById(R.id.sno);
		viewHolder.question = (TextView) view
			.findViewById(R.id.questionName);
		viewHolder.answerChoice = (TextView) view
			.findViewById(R.id.answerChoice);
		viewHolder.answer = (EditText) view.findViewById(R.id.Answer);

		view.setTag(viewHolder);

	    } else {
		viewHolder = (MatchTheFollowingViewHolder) view.getTag();
	    }
	    viewHolder.answer.setTag(position);
	    data[position] = "";
	    viewHolder.Sno.setText(item.get(position).getMatch_FieldOne());
	    viewHolder.question.setText(item.get(position).getTitle());
	    viewHolder.answerChoice.setText(item.get(position)
		    .getMatch_FieldFour());
	    if (MtfQ.isAnswered() && MtfQ.getStudentAnswer().getChoices() != null) {
		for(int i = 0; i < MtfQ.getStudentAnswer().getChoices().size(); i++){
		    _hashMap.put(i, MtfQ.getStudentAnswer().getChoices().get(i));
		    if(MtfQ.getStudentAnswer().getChoices().get(i).getMatch_FieldThree()!= null){
			data[i] = MtfQ.getStudentAnswer().getChoices().get(i).getMatch_FieldThree();
		    } else {
			data[i]	= "";
		    }
		}
	    } else if(_hashMap.size() != 0){
		for(int i=0; i<_hashMap.size(); i++){
		    data[i] = _hashMap.get(i).getMatch_FieldThree();
		}
	    }

	    final InputFilter filter = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start,
			int end, Spanned dest, int dstart, int dend) {
		    for (int i = start; i < end; i++) {
			if (!Character.isLetterOrDigit(source.charAt(i))) {
			    return "";
			}
		    }
		    return null;
		}
	    };
	    final InputFilter length = new InputFilter.LengthFilter(1);

	    viewHolder.answer.setFilters(new InputFilter[] { filter, length });
	    viewHolder.answer.setText(data[position]);
	    viewHolder.answer.setOnFocusChangeListener(new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
		    // TODO Auto-generated method stub
		    if (!hasFocus){
			choice = new AnswerChoice();
			final EditText edit = (EditText) v;
			Logger.warn("_____SAMPLE____", "___POSITION___" +edit.getTag() 
				+ "____VALUE___" +edit.getText().toString());
			data[(Integer)edit.getTag()] = edit.getText().toString();
			if (data[(Integer)edit.getTag()].length() == 0 || data[(Integer)edit.getTag()].equals("")) {
			    choice.setMatch_FieldThree("");
			} else {
			    choice.setMatch_FieldThree(edit.getText().toString());
			}
			choice.setMatch_FieldOne(item.get((Integer)edit.getTag())
				.getMatch_FieldOne());
			choice.setMatch_FieldFour(item.get((Integer)edit.getTag())
				.getMatch_FieldFour());
			choice.setTitle(item.get((Integer)edit.getTag()).getTitle());
			apppendAns = apppendAns + edit.getText().toString().toString() + ",";
			ans.setAnswerString(apppendAns);
			_hashMap.put((Integer)edit.getTag(), choice);
		    }
		    answerChoiceList = new LinkedList<AnswerChoice>();
		    for (int j = 0; j < item.size(); j++) {
			final AnswerChoice studentChoice = new AnswerChoice();
			if (null != _hashMap.get(j)) {
			    studentChoice.setMatch_FieldThree(_hashMap.get(j)
				    .getMatch_FieldThree());
			    studentChoice.setMatch_FieldOne(_hashMap.get(j)
				    .getMatch_FieldOne());
			    studentChoice.setTitle(_hashMap.get(j).getTitle());
			    studentChoice.setMatch_FieldFour(_hashMap.get(j)
				    .getMatch_FieldFour());
			    studentChoice.setPosition(_hashMap.get(j)
				    .getPosition());
			    studentChoice.setSelected(true);

			} else {
			    studentChoice.setTitle(item.get(j).getTitle());
			    studentChoice.setMatch_FieldOne(item.get(j)
				    .getMatch_FieldOne());
			    studentChoice.setMatch_FieldThree("");
			    studentChoice.setMatch_FieldFour(item.get(j)
				    .getMatch_FieldFour());

			}
			answerChoiceList.add(studentChoice);
		    }

		    ans.setChoices(answerChoiceList);
		    MtfQ.setStudentAnswer(ans);
		    
		    for (int iterator = 0; iterator < MtfQ.getStudentAnswer()
			    .getChoices().size(); iterator++) {
			if (MtfQ.getStudentAnswer().getChoices().get(iterator)
				.getMatch_FieldThree().toString().trim().length() != 0) {
			    MtfQ.setIsAnswered(true);
			    break;
			} else {
			    MtfQ.setIsAnswered(false);
			}
		    }
		}
	    });

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

    /**
     * The Class MatchTheFollowingViewHolder.
     */
    static class MatchTheFollowingViewHolder {
	
	/** The answer choice. */
	TextView Sno, question, answerChoice;
	
	/** The answer. */
	EditText answer;
    }

    /**
     * The Class OrderingQuestionViewHolder.
     */
    static class OrderingQuestionViewHolder {
	
	/** The teacher order. */
	TextView question, teacherOrder;
	
	/** The answer. */
	EditText answer;
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
	
	/** The layout resource id. */
	int layoutResourceId;
	
	/** The ctx. */
	Activity ctx;
	
	/** The view holder. */
	OrderingQuestionViewHolder viewHolder = null;

	/**
	 * Instantiates a new ordering question adapter.
	 *
	 * @param context the context
	 * @param resource the resource
	 * @param choices the choices
	 */
	public OrderingQuestionAdapter(Activity context, int resource,
		List<AnswerChoice> choices) {
	    super(context, resource, choices);
	    Log.i("____________________", "ITEM VALUE----" + choices.size());
	    ctx = context;
	    item = choices;
	    layoutResourceId = resource;
	    data=new String[item.size()];
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
	    ans = new Answer();
	    if (view == null) {
		viewHolder = new OrderingQuestionViewHolder();
		final LayoutInflater inflater = ctx.getLayoutInflater();
		view = inflater.inflate(R.layout.ordering, parent, false);
		viewHolder.question = (TextView) view
			.findViewById(R.id.teacher_choice);
		viewHolder.teacherOrder = (TextView) view
			.findViewById(R.id.teacher_order);
		viewHolder.answer = (EditText) view
			.findViewById(R.id.student_order);

		view.setTag(viewHolder);

	    } else {
		viewHolder = (OrderingQuestionViewHolder) view.getTag();
	    }
	    viewHolder.answer.setTag(position);
	    data[position] = "";
	    viewHolder.teacherOrder.setText(item.get(position).getId() + "");
	    viewHolder.question.setText(item.get(position).getTitle());
	    if (ord.getStudentAnswer().getChoices() != null && ord.isAnswered()) {
		for(int i = 0; i < ord.getStudentAnswer().getChoices().size(); i++){
		    hashMapOrd.put(i, ord.getStudentAnswer().getChoices().get(i));
		    if(ord.getStudentAnswer().getChoices().get(i).getPosition() != 0){
			data[i] = ord.getStudentAnswer().getChoices().get(i).getPosition()+"";
		    } else {
			data[i]	= "";
		    }
		}
	    }else if(hashMapOrd.size() != 0){
		for(int i=0; i<hashMapOrd.size(); i++){
		    data[i] = hashMapOrd.get(i).getPosition()+"";
		}
	    }

	    final InputFilter[] filters = new InputFilter[2];


	    filters[0] = new InputFilter(){

		@Override 
		public CharSequence filter(CharSequence source, int start,
			int end, Spanned dest, int dstart, int dend) { if (end > start) {

			    final char[] acceptedChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'}; 
			    for (int index = start; index < end; index++) {
				if (!new String(acceptedChars).contains(String.valueOf(source.charAt(index))))
				{ 
				    return ""; 
				}
			    }
			}
			return null; 
		}
	    };
	    filters[1]=new InputFilter.LengthFilter(1);
	    viewHolder.answer.setFilters(new InputFilter[]{filters[0],filters[1]});
	    viewHolder.answer.setText(data[position]);
	    viewHolder.answer.setOnFocusChangeListener(new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
		    // TODO Auto-generated method stub
		    if (!hasFocus){
			choice = new AnswerChoice();
			final EditText edit = (EditText) v;
			Logger.warn("_____SAMPLE____", "___POSITION___" +edit.getTag() 
				+ "____VALUE___" +edit.getText().toString());
			data[(Integer)edit.getTag()] = edit.getText().toString();
			if (data[(Integer)edit.getTag()].length() == 0 || data[(Integer)edit.getTag()].equals("")) {
			    choice.setPosition(0);
			    choice.setMatch_FieldThree("");
			} else {
			    choice.setPosition(Integer.parseInt(edit.getText().toString()));
			    choice.setMatch_FieldThree(edit.getText().toString());
			}
			choice.setTitle(item.get((Integer)edit.getTag()).getTitle());
			choice.setId(item.get((Integer)edit.getTag()).getId());
			hashMapOrd.put((Integer)edit.getTag(), choice);
		    }
		    answerChoiceList = new LinkedList<AnswerChoice>();
		    for (int j = 0; j < item.size(); j++) {
			final AnswerChoice studentChoice = new AnswerChoice();
			if (null != hashMapOrd.get(j)) {
			    studentChoice.setPosition(hashMapOrd.get(j)
				    .getPosition());
			    studentChoice.setMatch_FieldThree(hashMapOrd.get(j)
					    .getMatch_FieldThree());
			    studentChoice
			    .setTitle(hashMapOrd.get(j).getTitle());
			    studentChoice.setId(hashMapOrd.get(j).getId());
			    studentChoice.setSelected(true);

			} else {
			    studentChoice.setTitle(item.get(j).getTitle());
			    studentChoice.setId(item.get(j).getId());
			    studentChoice.setPosition(0);
			    studentChoice.setMatch_FieldThree("");
			}
			answerChoiceList.add(studentChoice);
		    }
		    ans.setChoices(answerChoiceList);
		    ord.setStudentAnswer(ans);
		    Logger.warn("", "choices list:"+ord.getStudentAnswer().getChoices().size());
		    for (int iterator = 0; iterator < ord.getStudentAnswer()
				    .getChoices().size(); iterator++) {
		    	Logger.warn("", "iterator:"+iterator);	
				if (ord.getStudentAnswer().getChoices().get(iterator)
					.getMatch_FieldThree().trim().length() != 0) {
				    ord.setIsAnswered(true);
				    break;
				} else
				    ord.setIsAnswered(false);
			    }
		    /*for (int iterator = 0; iterator <= ord.getStudentAnswer()
			    .getChoices().size(); iterator++) {
			if (String.valueOf(ord.getStudentAnswer().getChoices().get(iterator)
				.getPosition()).toString().trim() != "") {
			    ord.setIsAnswered(true);
			    break;
			} else
			    ord.setIsAnswered(false);
		    }*/

		}
	    });

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