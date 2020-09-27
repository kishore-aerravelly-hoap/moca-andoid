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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.activities.CreateTestStep4;
import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.user.teacher.examination.Answer;
import com.pearl.user.teacher.examination.AnswerChoice;
import com.pearl.user.teacher.examination.Question;
import com.pearl.user.teacher.examination.questiontype.MultipleChoiceQuestion;
import com.pearl.user.teacher.examination.questiontype.OrderingQuestion;


// TODO: Auto-generated Javadoc
/**
 * The Class CreateTestStep4UI.
 */
public class CreateTestStep4UI {
    
    /** The createteststep5 ui. */
    private static CreateTestStep4UI createteststep5UI = null;
    
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
    
    /** The _hash map. */
    public HashMap<Integer, AnswerChoice> _hashMap;
    
    /** The hash map ord. */
    public HashMap<Integer, AnswerChoice> hashMapOrd;

    // List<String> _matchAnswers;

    /**
     * Instantiates a new creates the test step4 ui.
     */
    public CreateTestStep4UI() {
	Logger.warn("", "-----Constructor");
	_hashMap = new HashMap<Integer, AnswerChoice>();
	hashMapOrd = new HashMap<Integer, AnswerChoice>();
    }

    /**
     * Gets the single instance of CreateTestStep4UI.
     *
     * @return single instance of CreateTestStep4UI
     */
    public static CreateTestStep4UI getInstance() {
	createteststep5UI = new CreateTestStep4UI();

	return createteststep5UI;
    }

    /**
     * Builds the.
     *
     * @param question the question
     * @param activity the activity
     * @param layout the layout
     * @return the linear layout
     */
    public LinearLayout build(Question question, CreateTestStep4 activity,
	    LinearLayout layout) {

	//question.setViewed(true);

	Logger.info("QuestionsUI",
		"Building question Type " + question.getType());

	if (Question.MULTIPLECHOICE_QUESTION.equalsIgnoreCase(question.getType().trim())
		|| Question.TRUE_OR_FALSE_QUESTION.equalsIgnoreCase(question.getType()
			.trim())) {
	    if(Question.MULTIPLECHOICE_QUESTION.equalsIgnoreCase(question.getType().trim()))
		questionType = "MCQ";
	    else
		questionType = "TFQ";
	    return buildTrueOrFalse(question, activity, layout);
	} else if (Question.SHORT_ANSWER_QUESTION.equalsIgnoreCase(question.getType()
		.trim())) {
	    questionType = "SAQ";
	    return buildShortAnswerQuestion(question, activity, layout);
	} else if (Question.LONG_ANSWER_QUESTION.equalsIgnoreCase(question.getType()
		.trim())) {
	    questionType = "LAQ";
	    return buildLongAnswerQuestion(question, activity, layout);
	} else if (Question.FILL_IN_THE_BLANK_QUESTION.equalsIgnoreCase(question
		.getType().trim())) {
	    questionType = "FIB";
	    return buildFillInTheBlanksQuestion(question, activity, layout);
	} else if (Question.ORDERING_QUESTION.equalsIgnoreCase(question.getType().trim())) {
	    questionType = "OQ";
	    return buildOrderingQuestion(question, activity, layout);
	} else if (Question.MATCH_THE_FOLLOWING.equalsIgnoreCase(question.getType()
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
	    final CreateTestStep4 activity, LinearLayout layout) {
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
		800, 505);

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
	answerEditText.setEnabled(false);
	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	answerParams.setMargins(45, 5, 45, 5);
	answerParams.height = 200;

	// Add to layout
	layout.addView(getQuestionView(question, activity));
	layout.addView(hintsTextView, defaultParams);
	layout.addView(answerEditText, answerParams);
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
	    final CreateTestStep4 activity, LinearLayout layout) {
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
	answerEditText.setEnabled(false);

	final LinearLayout.LayoutParams answerParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.FILL_PARENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	answerParams.setMargins(45, 5, 45, 5);
	answerParams.height = 200;

	// Add to layout
	layout.addView(getQuestionView(question, activity));
	layout.addView(Text, defaultParams);
	layout.addView(answerEditText, answerParams);

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
	    final CreateTestStep4 activity, LinearLayout layout) {
	ansChoice = new AnswerChoice();
	ansChoiceList = new LinkedList<AnswerChoice>();
	answer = new Answer();
	question.setViewed(true);
	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	}
	layout.addView(getQuestionView(question, activity));
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
	    final CreateTestStep4 activity, LinearLayout layout) {

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
		1050, android.view.ViewGroup.LayoutParams.FILL_PARENT);
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

		/*
		 * if (choice.isSelected()) { option.setChecked(true);
		 * tfQuestion.setAnswer(choice); } else {
		 * option.setChecked(false); }
		 */

		/*
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
		 */
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
	    final CreateTestStep4 activity, LinearLayout layout) {
	question.setViewed(true);
	if (layout == null) {
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    new LinearLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	    layout.setOrientation(LinearLayout.VERTICAL);
	} else {
	    new LinearLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT);
	    layout.setOrientation(LinearLayout.VERTICAL);

	}

	// Initialize default parameters
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
		800, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	defaultParams.setMargins(40, 10, 30, 10);

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
	    orderingQuestionsList.setLayoutParams(defaultParams);
	    orderingQuestionsList.setAdapter(ordAdapter);
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
	//q.setViewed(true);
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

	questionNo.setTextSize(26);
	questionView.setTextSize(26);
	marksAlloted.setTextSize(26);
	quesType.setTextSize(26);

	questionNo.setTextColor(Color.parseColor("#000000"));
	questionView.setTextColor(Color.parseColor("#000000"));
	marksAlloted.setTextColor(Color.parseColor("#000000"));
	quesType.setTextColor(Color.parseColor("#000000"));

	questionView.setMovementMethod(new ScrollingMovementMethod());
	questionView.setMaxLines(6);

	final String questions = q.getQuestion();
	/*String questions = StringEscapeUtils
				.unescapeEcmaScript(q.getQuestion());*/

	questionNo
	.setText("Q "
		+ (q.getQuestionOrderNo() > 0 ? q
			.getQuestionOrderNo() : "") + ": ");
	questionView.setText(questions);
	marksAlloted.setText("[ " + q.getMarksAlloted() + "M ]");

	quesType.setText(questionType);
	questionNo.setGravity(Gravity.LEFT);
	questionNu.addView(marksandQestiontype);
	questionNu.addView(questionLayout);
	questionLayout.addView(questionNo, defaultParams);
	questionLayout.addView(questionView, defaultParams1);
	marksandQestiontype.addView(marksAlloted, marksParams);
	//marksandQestiontype.addView(quesType, questionTypeparams);
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
	    final CreateTestStep4 activity, LinearLayout layout) {
	question.setViewed(true);
	if (layout == null) {
	    Logger.warn("QuesionsUI", "BUILDMFQ - layout is null");
	    // Initialize Linear Layout
	    layout = new LinearLayout(activity);
	    layout.setLayoutParams(new LayoutParams(
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	    layout.setOrientation(LinearLayout.VERTICAL);
	}
	MtfQ = (MultipleChoiceQuestion) question;
	ans = new Answer();
	final LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
			android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
	defaultParams.setMargins(40, 10, 30, 10);

	final LinearLayout.LayoutParams inlineParams = new LinearLayout.LayoutParams(
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	inlineParams.setMargins(30, 10, 60, 40);

	final ListView mulitpleChoiceList = new ListView(activity);
	MatchtheFollowingAdapter matchTheFollowing;

	if (question.getStudentAnswer().getChoices() != null) {
	    Logger.info("", "MTFQ Question details are========in IF");
	    matchTheFollowing = new MatchtheFollowingAdapter(activity,
		    android.R.layout.simple_selectable_list_item, MtfQ
		    .getStudentAnswer().getChoices());

	} else {
	    Logger.info("", "MTFQ Question details are========IN else");
	    matchTheFollowing = new MatchtheFollowingAdapter(activity,
		    android.R.layout.simple_selectable_list_item,MtfQ.getChoices());
	    // _hashMap=new HashMap<Integer, AnswerChoice>();
	}
	mulitpleChoiceList.setAdapter(matchTheFollowing);
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
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
	    if (view == null) {
		viewHolder = new MatchTheFollowingViewHolder();
		Log.i("____________________",
			"View is Null now going to inflate");
		final LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
		view = inflater.inflate(R.layout.matchthefollowing_listrow,
			parent, false);
		viewHolder.Sno = (TextView) view.findViewById(R.id.sno);
		viewHolder.question = (TextView) view
			.findViewById(R.id.questionName);
		viewHolder.answerChoice = (TextView) view
			.findViewById(R.id.answerChoice);
		viewHolder.answer = (EditText) view.findViewById(R.id.Answer);
		viewHolder.answer.setEnabled(false);
		view.setTag(viewHolder);

	    } else {
		Log.i("____________________", "View is Null now going");
		viewHolder = (MatchTheFollowingViewHolder) view.getTag();
	    }

	    if (MtfQ.isAnswered()
		    && MtfQ.getStudentAnswer().getChoices() != null) {
		Log.i("____________________",
			"=====================MATCH THE FOLLOWING");
		viewHolder.Sno.setText(MtfQ.getStudentAnswer().getChoices()
			.get(position).getMatch_FieldOne());
		viewHolder.question.setText(MtfQ.getStudentAnswer()
			.getChoices().get(position).getTitle());
		viewHolder.answerChoice.setText(MtfQ.getStudentAnswer()
			.getChoices().get(position).getMatch_FieldFour());
		viewHolder.answer.setText(MtfQ.getStudentAnswer().getChoices()
			.get(position).getMatch_FieldThree());

	    } else {
		Log.i("____________________",
			"=====================MATCH THE FOLLOWING"
				+ item.get(0));
		viewHolder.Sno.setText(item.get(position).getMatch_FieldOne());
		viewHolder.question.setText(item.get(position).getTitle());
		viewHolder.answerChoice.setText(item.get(position)
			.getMatch_FieldFour());
		Logger.info("", "Choices Size is  in adapter  " + item.size());
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

	    viewHolder.answer.addTextChangedListener(new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start,
			int before, int count) {
		    Logger.warn("", "MTF - on text change: " + s);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start,
			int count, int after) {
		    // TODO Auto-generated method stub
		    Logger.warn("", "MTF - before text change: " + s);

		}

		@Override
		public void afterTextChanged(Editable s) {

		    Logger.info("",
			    "MTF -  value in after text change is: s value is -- "
				    + s.toString());
		    choice = new AnswerChoice();

		    if (s.length() == 0 || s == null) {
			Logger.info("",
				"MTF - +++++++++++++++++++++++++++++++++++++Empty string..");
			choice.setMatch_FieldThree("");
		    } else {
			Logger.info("",
				"MTF - +++++++++++++++++++++++++++++++++++++string is not empty..");
			choice.setMatch_FieldThree(s.toString());
			Logger.info("",
				"Choices are ......getMatch_FieldThree                "
					+ choice.getMatch_FieldThree());
		    }

		    choice.setMatch_FieldOne(item.get(position)
			    .getMatch_FieldOne());
		    choice.setMatch_FieldFour(item.get(position)
			    .getMatch_FieldFour());
		    choice.setTitle(item.get(position).getTitle());
		    apppendAns = apppendAns + s.toString() + ",";
		    ans.setAnswerString(apppendAns);

		    _hashMap.put(position, choice);
		    Logger.warn("", "MTF - hash map size is:" + _hashMap.size());
		    Logger.warn("", "MTF - hash map data is:" + _hashMap);
		    Logger.info("",
			    "Choices are ......getMatch_FieldOne                "
				    + item.get(position).getMatch_FieldOne());
		    Logger.info("",
			    "Choices are ......getTitle                          "
				    + item.get(position).getTitle());
		    Logger.info("",
			    "Choices are ......getMatch_FieldFour               "
				    + item.get(position).getMatch_FieldFour());
		    Logger.info("",
			    "Choices are ......getMatch_FieldFour                "
				    + item.get(position).getPosition());
		    Logger.info("",
			    "Choices Size is                 " + item.size());

		    answerChoiceList = new LinkedList<AnswerChoice>();
		    for (int j = 0; j < item.size(); j++) {
			final AnswerChoice studentChoice = new AnswerChoice();
			if (null != _hashMap.get(j)) {
			    studentChoice.setMatch_FieldThree(_hashMap.get(j)
				    .getMatch_FieldThree());
			    Logger.warn("____________",
				    "studentChoice filed is:"
					    + _hashMap.get(j)
					    .getMatch_FieldThree());
			    studentChoice.setMatch_FieldOne(_hashMap.get(j)
				    .getMatch_FieldOne());
			    studentChoice.setTitle(_hashMap.get(j).getTitle());
			    studentChoice.setMatch_FieldFour(_hashMap.get(j)
				    .getMatch_FieldFour());
			    studentChoice.setPosition(_hashMap.get(j)
				    .getPosition());
			    studentChoice.setSelected(true);
			    Log.i("QUI",
				    "MTF -answerChoice LIst afterTextChanged in for loop---"
					    + studentChoice);

			} else {
			    Log.i("QUI",
				    "MTF -answerChoice LIst afterTextChanged in else---");
			    studentChoice.setTitle(item.get(j).getTitle());
			    studentChoice.setMatch_FieldOne(item.get(j)
				    .getMatch_FieldOne());
			    studentChoice.setMatch_FieldThree("");
			    studentChoice.setMatch_FieldFour(item.get(j)
				    .getMatch_FieldFour());

			}
			answerChoiceList.add(studentChoice);
		    }
		    Log.i("QUI", "MTF -answerChoice LIst afterTextChanged ---"
			    + answerChoiceList);

		    ans.setChoices(answerChoiceList);
		    Log.i("QUI", "MTF -Answer LIst afterTextChanged ---" + ans);
		    MtfQ.setStudentAnswer(ans);
		    Log.i("QUI", "MTF -studentAnswer LIst afterTextChanged ---"
			    + MtfQ.getStudentAnswer());
		    /*
		     * if((answer.getText().toString().trim()).equals("")) {
		     * MtfQ.setIsAnswered(false); }else{ MtfQ.setAnswered(true);
		     * }
		     */
		    for (int iterator = 0; iterator <= MtfQ.getStudentAnswer()
			    .getChoices().size(); iterator++) {
			if (MtfQ.getStudentAnswer().getChoices().get(position)
				.getMatch_FieldThree() == "") {
			    MtfQ.setAnswered(false);
			} else
			    MtfQ.setAnswered(true);
		    }

		    // MtfQ.setAnswered(true);
		}
	    });
	    return view;

	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getCount()
	 */
	@Override
	public int getCount() {

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
	Context ctx;
	
	/** The view holder. */
	OrderingQuestionViewHolder viewHolder = null;

	/**
	 * Instantiates a new ordering question adapter.
	 *
	 * @param context the context
	 * @param resource the resource
	 * @param choices the choices
	 */
	public OrderingQuestionAdapter(Context context, int resource,
		List<AnswerChoice> choices) {
	    super(context, resource, choices);
	    Log.i("____________________", "ITEM VALUE----" + choices.size());
	    ctx = context;
	    item = choices;
	    layoutResourceId = resource;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
	    ans = new Answer();

	    if (view == null) {
		viewHolder = new OrderingQuestionViewHolder();
		Log.i("____________________",
			"View is Null now going to inflate");
		final LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
		view = inflater.inflate(R.layout.ordering, parent, false);
		viewHolder.question = (TextView) view
			.findViewById(R.id.teacher_choice);
		viewHolder.teacherOrder = (TextView) view
			.findViewById(R.id.teacher_order);
		viewHolder.answer = (EditText) view
			.findViewById(R.id.student_order);
		viewHolder.answer.setEnabled(false);
		view.setTag(viewHolder);

	    } else {
		Log.i("____________________", "View is Null now going");
		viewHolder = (OrderingQuestionViewHolder) view.getTag();
	    }

	    if (ord.getStudentAnswer().getChoices() != null && ord.isAnswered()) {
		Log.i("QUI", "ORDRING in if  and is Answerd;");

		viewHolder.question.setText(ord.getStudentAnswer().getChoices()
			.get(position).getTitle());
		viewHolder.teacherOrder.setText(ord.getStudentAnswer()
			.getChoices().get(position).getId()
			+ "");
		if (ord.getStudentAnswer().getChoices().get(position)
			.getPosition() == 0) {
		    viewHolder.answer.setText("");
		} else {
		    viewHolder.answer.setText(ord.getStudentAnswer()
			    .getChoices().get(position).getPosition()
			    + "");
		}
	    } else {
		Log.i("QUI", "ORDRING in else and is Answerd;");
		Log.i("____________________", "ORD - " + item.get(0));
		viewHolder.teacherOrder
		.setText(item.get(position).getId() + "");
		viewHolder.question.setText(item.get(position).getTitle());
		if (item.get(position).getPosition() == 0)
		    viewHolder.answer.setText("");
		else
		    viewHolder.answer.setText(item.get(position).getPosition()
			    + "");
		Logger.info("", "Choices Size is  in adapter  " + item.size());
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

	    viewHolder.answer.addTextChangedListener(new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start,
			int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start,
			int count, int after) {
		    // TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {

		    Logger.info("",
			    "ORD -  value in after text change is: s value is -- "
				    + s.toString());
		    choice = new AnswerChoice();

		    if (s.length() == 0 || s == null) {
			Logger.info("",
				"ORD - +++++++++++++++++++++++++++++++++++++Empty string..");
			choice.setPosition(0);
		    } else {
			Logger.warn(
				"",
				"ORD - final value is: "
					+ Integer.parseInt(s.toString()));
			choice.setPosition(Integer.parseInt(s.toString()));
			Logger.warn("QuestionsUI", "ORD - String was not empty");
		    }

		    choice.setTitle(item.get(position).getTitle());
		    choice.setId(item.get(position).getId());

		    hashMapOrd.put(position, choice);
		    Logger.warn("",
			    "ORD - hash map size is:" + hashMapOrd.size());
		    Logger.warn("", "ORD - hash map data is:" + hashMapOrd);
		    Logger.info("",
			    "ORD - Choices are ......getTitle                          "
				    + item.get(position).getTitle());
		    Logger.info("",
			    "ORD - Choices are ......Position                "
				    + item.get(position).getPosition());
		    Logger.info("", "ORD - Choices Size is                 "
			    + item.size());

		    answerChoiceList = new LinkedList<AnswerChoice>();
		    for (int j = 0; j < item.size(); j++) {
			final AnswerChoice studentChoice = new AnswerChoice();
			if (null != hashMapOrd.get(j)) {
			    studentChoice.setPosition(hashMapOrd.get(j)
				    .getPosition());
			    studentChoice
			    .setTitle(hashMapOrd.get(j).getTitle());
			    studentChoice.setId(hashMapOrd.get(j).getId());
			    studentChoice.setSelected(true);
			    Log.i("QUI",
				    "ORD - answerChoice LIst afterTextChanged in for loop---"
					    + studentChoice);

			} else {
			    Logger.info("", "ORD - Choices are ......NULL");
			    Logger.info("",
				    "ORD - Choices are ......NULL with position-->"
					    + position);
			    studentChoice.setTitle(item.get(j).getTitle());
			    studentChoice.setId(item.get(j).getId());
			    studentChoice.setPosition(0);

			}
			answerChoiceList.add(studentChoice);
		    }
		    Log.i("QUI", "ORD - answerChoice LIst afterTextChanged ---"
			    + answerChoiceList);
		    ans.setChoices(answerChoiceList);
		    for (int i = 0; i < ans.getChoices().size(); i++) {
			Logger.warn("", "ORD - final choices are:" + ans);
		    }
		    ord.setStudentAnswer(ans);

		    for (int iterator = 0; iterator <= ord.getStudentAnswer()
			    .getChoices().size(); iterator++) {
			if (ord.getStudentAnswer().getChoices().get(position)
				.getPosition() == 0) {
			    ord.setAnswered(false);
			} else
			    ord.setAnswered(true);
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
}