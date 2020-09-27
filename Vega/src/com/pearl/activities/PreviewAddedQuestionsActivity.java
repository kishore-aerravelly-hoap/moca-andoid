package com.pearl.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.book.guesturehandlers.GestureActionHandler;
import com.pearl.examination.exceptions.QuestionDoesNotExistsException;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.parsers.json.TeacherExamParser;
import com.pearl.ui.helpers.examination.Preview4UI;
import com.pearl.user.teacher.examination.Exam;
import com.pearl.user.teacher.examination.Question;
import com.pearl.user.teacher.examination.ServerExam;

// TODO: Auto-generated Javadoc
/**
 * The Class PreviewAddedQuestionsActivity.
 */
public class PreviewAddedQuestionsActivity extends BaseActivity implements
GestureActionHandler {

    /** The max_marks. */
    private TextView duration, testName, max_marks;
    
    /** The questions layout. */
    private LinearLayout questionsLayout;
    
    /** The exam. */
    private Exam exam;
    
    /** The exam id. */
    private   String examId;
    
    /** The help. */
    private ImageView help,sectiondrop;
    
    /** The i. */
    int i=0;
    
    /** The edit_button. */
    private    Button submitButton,menuButton,edit_button ;
    
    /** The question no. */
    private int questionNo = 0;
    
    /** The total no of questions. */
    private int totalNoOfQuestions; // Total number of questions for exam
    
    /** The question. */
    private Question question;
    
    /** The test id. */
    private String testId = null;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The list. */
    private    List<Question> list=new ArrayList<Question>();

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.create_test_step_5);
	questionsLayout = (LinearLayout) findViewById(R.id.questions_layout);
	appData = (ApplicationData) getApplication();
	help = (ImageView) findViewById(R.id.preview_help);
	duration = (TextView) findViewById(R.id.duration_answer);
	duration.setVisibility(View.VISIBLE);
	testName = (TextView) findViewById(R.id.test_name_answer);
	max_marks = (TextView) findViewById(R.id.textView1);
	menuButton = (Button) findViewById(R.id.examMenu);
	submitButton = (Button) findViewById(R.id.submittt);
	edit_button=(Button)findViewById(R.id.edit_question);
	sectiondrop=(ImageView)findViewById(R.id.image_data);
	submitButton.setWidth(200);
	menuButton.setVisibility(View.VISIBLE);
	sectiondrop.setVisibility(View.INVISIBLE);
	String content = null;
	final Bundle bundle = getIntent().getExtras();
	if (bundle != null)
	    testId = bundle.getString(VegaConstants.TEST_ID);
	try {
	    content = ApplicationData.readFile(appData
		    .getStep2FilePath(getIntent().getExtras().getString(
			    VegaConstants.TEST_ID))
			    + ApplicationData.STEP2_FILE_NAME);
	} catch (final Exception e) {
	    Logger.error(tag, e);
	}

	ServerExam baseResponse = null;

	try {
	    baseResponse = TeacherExamParser
		    .parseJsonToSeverExamObject(content);
	} catch (final Exception e) {
	    e.printStackTrace();
	}

	Logger.warn(tag, "json data from server is:" + baseResponse);

	try {
	    exam = baseResponse.getExam();
	    Logger.warn("------>", "parsed exam " + exam);
	} catch (final Exception e1) {
	    Logger.error(tag, e1);
	}
	final ActivitySwipeDetector swipeHandler = new ActivitySwipeDetector(this);
	questionsLayout.setOnTouchListener(swipeHandler);
	updateUI();
	bindEvents();
    }

    /**
     * Update ui.
     */
    private void updateUI() {
	try {
	    if (exam != null) {
	    list=	exam.getQuestions();

		question = exam.getQuestionWithNumber(questionNo);
		totalNoOfQuestions = exam.getQuestions().size();
	    }

	} catch (final QuestionDoesNotExistsException e) {
	    Logger.error(tag, e);
	}
	max_marks.setText("Preview Test Paper");
	testName.setText(exam.getExamDetails().getTitle());
	duration.setText(exam.getExamDetails().getDuration() + "min");
	submitButton.setText("Back");
	clearLayout(questionsLayout);
	updateQuestionsLayout(question, questionsLayout);
    }

    /**
     * downloads the list of questions.
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

	submitButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		final Intent i = new Intent(PreviewAddedQuestionsActivity.this,
			CreateTestStep2Activity.class);
		i.putExtra(VegaConstants.TEST_ID, testId);
		startActivity(i);
		finish();
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
	edit_button.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		SharedPreferences sharedPreferences=PreviewAddedQuestionsActivity.this.getSharedPreferences("com.pearl.prefs",MODE_PRIVATE);
		Gson gson=new Gson();
		
		String question=gson.toJson(PreviewAddedQuestionsActivity.this.question);
		Editor editor=sharedPreferences.edit();
		
		editor.putString("QUESTION_obj",question);
		editor.putString("edit_question_id",PreviewAddedQuestionsActivity.this.question.getId());
		editor.putString("question_orderNo",String.valueOf(PreviewAddedQuestionsActivity.this.question.getQuestionOrderNo()));
		editor.commit();
		//list.remove(question);
		//exam.setQuestions(list);
		final Intent i = new Intent(PreviewAddedQuestionsActivity.this,
				CreateTestStep2Activity.class);
		
		i.putExtra("edit_question",true);
			i.putExtra(VegaConstants.TEST_ID, testId);
			startActivity(i);
			PreviewAddedQuestionsActivity.this.overridePendingTransition(R.anim.push_right_in,
		    		R.anim.push_right_out);
	
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

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return tag;
    }

}
