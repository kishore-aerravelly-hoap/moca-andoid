package com.pearl.activities;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.examination.Answer;
import com.pearl.examination.AnswerChoice;
import com.pearl.examination.Exam;
import com.pearl.examination.Question;
import com.pearl.examination.exceptions.QuestionDoesNotExistsException;
import com.pearl.examination.questiontype.MultipleChoiceQuestion;
import com.pearl.examination.questiontype.OrderingQuestion;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.FileParameter;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.ExamParser;
import com.pearl.services.NotficationService;
import com.pearl.ui.helpers.examination.QuestionsUI;
import com.pearl.ui.models.BaseRequest;
import com.pearl.ui.models.ExamResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class QuestionsActivity.
 */
public class QuestionsActivity extends BaseActivity {

    /** The exam. */
    private Exam exam;
    
    /** The question. */
    Question question;

    /** The exam timer. */
    Timer examTimer;
    
    /** The exam id. */
    String examId;
    
    /** The question no. */
    int questionNo;
    
    /** The i. */
    int totalQuestions, i =0;
    
    /** The exam time out. */
    boolean examTimeOut = false;

    /** The exam title. */
    TextView examTitle;
    
    /** The subject. */
    TextView subject;
    
    /** The quizzard. */
    TextView quizzard;
    
    /** The teacher. */
    TextView teacher;
    
    /** The marks. */
    TextView marks;
    
    /** The student. */
    TextView student;
    
    /** The section. */
    TextView grade, section;
    
    /** The Questionno. */
    TextView Questionno;
    //WifiManager wifiManager;
    /** The app status. */
    AppStatus appStatus;
    
    /** The question layout1. */
    LinearLayout questionLayout,questionLayout1;
    
    /** The timer. */
    TextView timer;
    
    /** The back to shelf. */
    ImageView backToShelf;
    
    /** The prev button. */
    Button prevButton;
    
    /** The next button. */
    Button nextButton;
    
    /** The attempted. */
    TextView status, attempted;

    /** The question no view. */
    TextView questionNoView;
    
    /** The only marked. */
    CheckBox onlyMarked;
    
    /** The help. */
    ImageView bookmark, help;
    
    /** The submit exam button. */
    Button submitExamButton;
    
    /** The save exam button. */
    Button saveExamButton;
    
    /** The back to questions list. */
    Button backToQuestionsList;
    
    /** The controls layout. */
    LinearLayout controlsLayout;
    
    /** The millis. */
    Long enteredtime, millis;
    
    /** The exam server url. */
    private String examServerURL;
    
    /** The is submitted. */
    boolean isSubmitted = false;
    
    /** The pm. */
    PowerManager pm;
    
    /** The wl. */
    PowerManager.WakeLock wl;
    
    /** The resume status. */
    private boolean resumeStatus;
    private ImageView expandableNameanswer1;
    private Context context;

    /* (non-Javadoc)
     * @see android.app.Activity#onAttachedToWindow()
     */
    @Override
    public void onAttachedToWindow() {
	this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
	super.onAttachedToWindow();
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.questions_activity);

	String name = this.getClass().getName();
	final StringTokenizer st = new StringTokenizer(name, ".");
	while (st.hasMoreTokens()) {
	    name = st.nextToken();
	}
	appStatus=new AppStatus();
	initUIObjects();
	bindEvents();
	try {
	    examServerURL = vegaConfig.getValue(VegaConstants.EXAM_SERVER_IP);
	    vegaConfig.setValue(VegaConstants.HISTORY_QUESTIONS_ACTIVITY, name);
	} catch (final InvalidConfigAttributeException e) {
	    Logger.error(tag, e);

	    examServerURL = "http://172.16.202.132:8088";
	} catch (final ColumnDoesNoteExistsException e) {
	    Logger.error(tag, e);
	    e.printStackTrace();
	}

	final Bundle bundle = this.getIntent().getExtras();
	if (bundle != null) {
	    examId = bundle.getString(VegaConstants.EXAM_ID);
	    enteredtime = bundle.getLong("enteredTime");
	    questionNo = bundle.getInt(VegaConstants.QUESTION_NUM) >= 0 ? bundle
		    .getInt(VegaConstants.QUESTION_NUM) : 0;
		    Logger.info(tag, "Question No from bundle is:" + questionNo);
	} else {
	    // Practically it should not reach here or else .. get exam Id and
	    // questionNo from 'Resume'
	    examId = "";
	    questionNo = 0;

	    Logger.error(tag,
		    "Reached to bundle else part which should never happen");
	}
	final SharedPreferences appSharedPrefs = PreferenceManager
		.getDefaultSharedPreferences(this.getApplicationContext());

	final Editor prefsEditor = appSharedPrefs.edit();
	if (enteredtime != 0) {
	    prefsEditor.putLong("MyObject", enteredtime);
	    prefsEditor.commit();
	}
	appData.setActivityName("QuestionsActivity");

	pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "QuestionsActivity");
	wl.acquire();

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();

	// questionNo = 0;
	if (appData.getCurrentExam() == null) {
	    // Exam is null so get the exam id from config
	    try {
		examId = vegaConfig.getValue(VegaConstants.HISTORY_EXAM_ID);
	    } catch (final InvalidConfigAttributeException e) {
		Logger.error(tag, e);
	    }
	}
	populateExamDetails(examId);
	if (exam != null) {
	    totalQuestions = exam.getQuestions().size();
	    appData.setOpenBookExamBookList(exam.getOpenBooks());
	    if (exam.isOpenBookExam()) {
		backToShelf.setVisibility(View.VISIBLE);
	    }
	    Logger.warn(tag, "books allowed:"
		    + exam.getExamDetails().getAllowedBookIds());

	    // @test exam.setStartTime(new Date().getTime());
	    // @test exam.setEndTime(new Date().getTime()+(2*60*1000));
	    Logger.error(tag, "ST:" + exam.getExamDetails().getStartDate()
		    + "| ET:" + exam.getExamDetails().getEndDate());
	}
	examTimer = new Timer();
	examTimer.schedule(new UpdateTimeTask(), 1000, 1000);
	updateUI();
    }

    /**
     * Inits the ui objects.
     */
    private void initUIObjects() {
	questionLayout = (LinearLayout) findViewById(R.id.question_layout);
	context = this;
	examTitle = (TextView) findViewById(R.id.exam_title);
	subject = (TextView) findViewById(R.id.subject);
	quizzard = (TextView) findViewById(R.id.quizzard);
	marks = (TextView) findViewById(R.id.marks);
	student = (TextView) findViewById(R.id.student);
	grade = (TextView) findViewById(R.id.student_grade);
	timer = (TextView) findViewById(R.id.timer);
	prevButton = (Button) findViewById(R.id.prev_button);
	nextButton = (Button) findViewById(R.id.next_button);
	questionNoView = (TextView) findViewById(R.id.questionNo);
	bookmark = (ImageView) findViewById(R.id.quesBookmark);
	onlyMarked = (CheckBox) findViewById(R.id.marked);
	submitExamButton = (Button) findViewById(R.id.submit_button);
	saveExamButton = (Button) findViewById(R.id.save_button);
	help = (ImageView) findViewById(R.id.question_help);

	attempted = (TextView) findViewById(R.id.textView9);
	expandableNameanswer1=(ImageView) findViewById(R.id.expandableName_answer1);

	backToShelf = (ImageView) findViewById(R.id.backToShelf);
	backToQuestionsList = (Button) findViewById(R.id.backToQuestionsList);

	controlsLayout = (LinearLayout) findViewById(R.id.controls_layout);
	
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
		final List<String> list = HelpParser.getHelpContent("quizzard_questions_student.txt", this);
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

	Logger.error(tag, "exam id is " + exam.getExamDetails().getExamId());

	examTitle.setText(exam.getExamDetails().getTitle());

	subject.setText(exam.getExamDetails().getSubject());

	// teacher.setText(exam.getTeacher()); TODO
	marks.setText(exam.getExamDetails().getMaxPoints() > 0 ? "Total Marks : "
		+ exam.getExamDetails().getMaxPoints() + ""
		: "");
	
	//student.setText(appData.getUser().getSecondName());// .getFirstName());
	//String tempName="samreen nnnnnnnnnnnnnnnnn";
    String tempName=appData.getUser().getSecondName();
	if(tempName.length()>18)
	{
		tempName=tempName.substring(0,17) + "..";
		//make the image visible
		expandableNameanswer1.setVisibility(View.VISIBLE);
		}
	else
	{
		//set the image visibility to invisible
		expandableNameanswer1.setVisibility(View.INVISIBLE);
	}
	student.setText(tempName);
	expandableNameanswer1.setOnClickListener(new OnClickListener() {
		
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
	// TODO XXX hardcoded
	grade.setText(appData.getGradeName() + "," + appData.getSectionName());
	// grade.setText("Grade:" + appData.getUser().getGrade());
	// section.setText("Section: " +appData.getSectionName());
	attempted.setText(getExamAnalysis());
	// question.getQuestionOrderNo(); and set it
	// Questionno.setText(question.getQuestionOrderNo()+"");
	try {
	    question = exam.getQuestionWithNumber(questionNo);

	    Logger.info(tag + "Question at " + questionNo + " is ",
		    question.getQuestion());
	} catch (final QuestionDoesNotExistsException e) {
	    Logger.error(tag, e);
	}

	updateNavigationUI();

	// clear prev question fields
	clearLayout(questionLayout);
	Logger.warn(tag, "bookmark status in QuestionsActivity is:"
		+ exam.getQuestions().get(questionNo).isMarked());
	if (exam.getQuestions().get(questionNo).isMarked()) {
	    bookmark.setImageResource(R.drawable.ques_bookmarked);
	} else {
	    bookmark.setImageResource(R.drawable.ques_unbookmarked);
	}

	getBooksForExam();

	if(Question.FILL_IN_THE_BLANK_QUESTION.equalsIgnoreCase(question.getType().trim())){
	    questionLayout1 = new LinearLayout(this);
	    final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	    params.setMargins(30, 20, 5, 10);
	    questionLayout1.setLayoutParams(params);
	    questionLayout1.setOrientation(LinearLayout.VERTICAL);
	    updateQuestionLayout(question, questionLayout1);
	    final ScrollView sv = new ScrollView(this);
	    sv.addView(questionLayout1);
	    questionLayout.addView(sv);

	}else{
	    updateQuestionLayout(question, questionLayout);
	}
    }

    /**
     * Bind events.
     */
    public void bindEvents() {
	onlyMarked.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		updateNavigationUI();
	    }
	});

	prevButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		if (onlyMarked.isChecked() == false) {
		    if (questionNo >= 0) {
			questionNo--;
			Logger.warn(tag,
				"OPENBOOK - QUESTION NO - in previous:"
					+ questionNo);
			try {
			    vegaConfig.setValue(
				    VegaConstants.HISTORY_QUESTION_NUM,
				    questionNo);
			} catch (final ColumnDoesNoteExistsException e) {
			    Logger.error(tag, e);
			}
		    }
		} else {
		    Logger.warn(tag, "in else");
		    for (int i = questionNo - 1; i >= 0; i--) {
			if (exam.getQuestions().get(i).getType()
				.equals("TRUE_OR_FALSE_QUESTION")) {
			    controlsLayout.setVisibility(View.VISIBLE);
			}
			if (exam.getQuestions().get(i).isMarked()) {
			    questionNo = i;
			    break;
			}
		    }
		}

		updateUI();
	    }
	});

	nextButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		if (onlyMarked.isChecked() == false) {
		    if (questionNo < totalQuestions - 1) {
			questionNo++;
			try {
			    vegaConfig.setValue(
				    VegaConstants.HISTORY_QUESTION_NUM,
				    questionNo);
			} catch (final ColumnDoesNoteExistsException e) {
			    Logger.error(tag, e);
			}
		    }
		} else {
		    for (int i = questionNo + 1; i < exam.getQuestions()
			    .size(); i++) {
			if (exam.getQuestions().get(i).getType()
				.equals("TRUE_OR_FALSE_QUESTION")) {
			    controlsLayout.setVisibility(View.VISIBLE);
			}
			if (exam.getQuestions().get(i).isMarked()) {
			    questionNo = i;
			    break;
			}
		    }
		}
		updateUI();
	    }
	});

	bookmark.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		if (!exam.getQuestions().get(questionNo).isMarked()) {
		    exam.getQuestions().get(questionNo).setMarked(true);
		    bookmark.setImageResource(R.drawable.ques_bookmarked);

		    Toast.makeText(activityContext,
			    R.string.marked_Successfully, 1000);
		} else {
		    exam.getQuestions().get(questionNo).setMarked(false);
		    bookmark.setImageResource(R.drawable.ques_unbookmarked);

		    Toast.makeText(activityContext,
			    R.string.unmarked_Successfully, Toast.LENGTH_SHORT);
		}
	    }
	});

	saveExamButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		final boolean saveStatus = saveExam(ApplicationData.EXAM_ANSWERS_SAVE_FILENAME);

		final ObjectMapper mapper = new ObjectMapper();
		try {
		    // mapper.writeValue(new File("exam-save.json"),
		    // appData.getCurrentExam());

		    Logger.info(tag + ": json value",
			    mapper.writeValueAsString(appData.getCurrentExam()));
		} catch (final Exception e) {
		    Logger.error(tag + ": save to json", e);
		}

		Toast.makeText(
			activityContext,
			saveStatus ? R.string.saved_Successfully
				: R.string.save_failed, Toast.LENGTH_SHORT)
				.show();
	    }
	});

	submitExamButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		final boolean saveStatus = saveExam(ApplicationData.EXAM_ANSWERS_SUBMIT_FILENAME);
		final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
			QuestionsActivity.this);
		alertDialog.setTitle("Submit");
		alertDialog.setIcon(R.drawable.submit_exam);
		alertDialog.setMessage("Are you sure you want to submit exam?");
		alertDialog.setPositiveButton("YES",
			new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog,
			    int which) {
			if (saveStatus) {
			    isSubmitted = true;
			    exam.getExamDetails().setState(
				    Exam.YET_TO_UPLOAD);
			    exam.getExamDetails().setUploadedDate(
				    System.currentTimeMillis());
			    final Exam examNew = updateExamDetails(exam);
			    // Deleting saved files
			    ApplicationData
			    .deleteFolderOrFileRecursively(new File(
				    appData.getExamSavedAnswersFileName(examNew
					    .getExamDetails()
					    .getExamId())));

			    // submitToServerAndFinishExam(); // Moved
			    // submission to
			    // exams list activity

			    appData.setCurrentExam(null);
			    appData.setCurrentExamQuestionNo(-1);

			    /*Toast.makeText(activityContext,
											R.string.exam_saved_successfully,
											Toast.LENGTH_SHORT).show();*/

			    // TODO update the status based on
			    // submission response
			    examNew.getExamDetails().setState(
				    Exam.STUDENT_SUBMITTED);
			    // Setting the Exam id to 0 once exam the
			    // submitted
			    try {
				vegaConfig.setValue(
					VegaConstants.HISTORY_EXAM_ID,
					null);
				vegaConfig
				.setValue(
					VegaConstants.HISTORY_QUESTIONS_ACTIVITY,
					null);
				appData.setCurrentExam(null);
			    } catch (final ColumnDoesNoteExistsException e) {
				Logger.error(tag, e);
			    }
			    examTimer.cancel();
			    Logger.warn(tag, "exam is retakable:"+exam.getExamDetails().isRetakeable());
			    if(!exam.getExamDetails().isRetakeable())
				uploadExam(examId);
			    final Intent serviceIntet=new Intent(QuestionsActivity.this, NotficationService.class);

			    final Intent intent = new Intent(
				    QuestionsActivity.this,
				    ExamListActivity.class);
			    startService(serviceIntet);
			    startActivity(intent);
			    QuestionsActivity.this.finish();
			}
		    }
		});
		alertDialog.setNegativeButton("NO",
			new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog,
			    int which) {
			dialog.cancel();
		    }
		});

		// Showing Alert Message
		alertDialog.show();

	    }
	});

	backToShelf.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO pass bookslist as intent
		final Intent intent = new Intent(QuestionsActivity.this,
			ShelfActivity.class);
		intent.putExtra(VegaConstants.EXAM_ID, examId);
		Logger.warn(tag,
			"OPENBOOK - Question number while passing in intent is:"
				+ questionNo);
		intent.putExtra(VegaConstants.HISTORY_QUESTION_NUM, questionNo);
		startActivity(intent);
		finish();
	    }
	});

	backToQuestionsList.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Logger.warn(tag, "exam when going back to questions list is:"
			+ appData.getCurrentExam());
		final Intent intent = new Intent(QuestionsActivity.this,
			QuestionsListActivity.class);
		intent.putExtra("exam_id", examId);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	    }
	});
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onStop()
     */
    @Override
    public void onStop() {
	super.onStop();
	if (!isSubmitted) {
	    saveExam(ApplicationData.EXAM_ANSWERS_SAVE_FILENAME);
	}

	examTimer.cancel();
    }

    /**
     * Gets the exam analysis.
     *
     * @return the exam analysis
     */
    private String getExamAnalysis() {

	String analysis1 = "";
	int marksAttempted = 0;
	exam.getQuestions().size();
	int totalMarksCount = 0;
	int attemptedQuestions=0;
	for (final Question question : exam.getQuestions()) {
	    if (question.isAnswered()) {
	    	attemptedQuestions++;
		marksAttempted += question.getMarksAlloted();
	    }

	    totalMarksCount += question.getMarksAlloted();
	}

	analysis1 = " Questions Attempted : " + String.valueOf(attemptedQuestions )+ "/"
		+ exam.getNumberOfQuestions();

	return analysis1;

    }

    /**
     * The Class UpdateTimeTask.
     */
    class UpdateTimeTask extends TimerTask {
	
	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
	    // final long millis = System.currentTimeMillis() -
	    // exam.getStartDate();
	    final SharedPreferences sharedPreferences = PreferenceManager
		    .getDefaultSharedPreferences(getApplicationContext());
	    enteredtime = sharedPreferences.getLong("MyObject", 0);
	    if (exam.getExamDetails().getEndDate() - System
		    .currentTimeMillis() > enteredtime
		    + exam.getExamDetails().getDuration() * 60000 - System
		    .currentTimeMillis()) {

		millis = enteredtime
			+ exam.getExamDetails().getDuration() * 60000
			- System.currentTimeMillis();
	    } else {
		millis = exam.getExamDetails().getEndDate()
			- System.currentTimeMillis();
	    }

	    final int seconds = (int) (millis / 1000) % 60;
	    final int minutes = (int) (millis / (1000 * 60) % 60);
	    final int hours = (int) (millis / (1000 * 60 * 60) % 24);

	    runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    if (millis < 1) {
			examTimer.cancel();

			examTimeOut = true;
			if (examTimeOut) {
			    final boolean saveStatus = saveExam(ApplicationData.EXAM_ANSWERS_SUBMIT_FILENAME);
			    if (saveStatus) {
				isSubmitted = true;
				exam.getExamDetails().setState(
					Exam.YET_TO_UPLOAD);
				final Exam examNew = updateExamDetails(exam);
				// Deleting saved files
				ApplicationData
				.deleteFolderOrFileRecursively(new File(
					appData.getExamSavedAnswersFileName(examNew
						.getExamDetails()
						.getExamId())));

				// submitToServerAndFinishExam(); // Moved
				// submission to
				// exams list activity

				appData.setCurrentExam(null);
				appData.setCurrentExamQuestionNo(-1);

				Toast.makeText(activityContext,
					R.string.exam_saved_successfully, 2000)
					.show();
				if(!exam.getExamDetails().isRetakeable())
				    uploadExam(examId);
				// TODO update the status based on submission
				// response
				examNew.getExamDetails().setState(
					Exam.STUDENT_SUBMITTED);
				// Setting the Exam id to 0 once exam the
				// submitted
				try {
				    vegaConfig
				    .setValue(
					    VegaConstants.HISTORY_EXAM_ID,
					    null);
				    vegaConfig
				    .setValue(
					    VegaConstants.HISTORY_QUESTIONS_ACTIVITY,
					    null);
				    appData.setCurrentExam(null);
				} catch (final ColumnDoesNoteExistsException e) {
				    Logger.error(tag, e);
				}
				final Intent intent = new Intent(
					QuestionsActivity.this,
					ExamListActivity.class);

				examTimer.cancel();
				startActivity(intent);
				finish();
			    }
			}

			return;
		    }

		    timer.setText(String.format("%02d:%02d:%02d", hours,
			    minutes, seconds));
		    if (hours == 00 && minutes == 04 && seconds == 00) {
			if (ShelfActivity.h != null) {
			    Log.i("QESTIONSACTIVITY--",
				    "I AM IN IF LOOP Driven by ReadBook NOt null----");
			    final ShelfActivity shelfActivity = new ShelfActivity();
			    shelfActivity
			    .readbookTimer(hours, minutes, seconds);
			}
			if (ReadbookActivity.h != null) {
			    Log.i("QESTIONSACTIVITY--",
				    "I AM IN IF LOOP Driven by ReadBook NOt null----");
			    final ReadbookActivity readBook = new ReadbookActivity();
			    readBook.readbookTimer(hours, minutes, seconds);
			}

			Log.i("QESTIONSACTIVITY--",
				"I AM IN IF LOOP Driven by MINUTES----");
			try {
			    final AlertDialog.Builder _examAlert = new AlertDialog.Builder(
				    activityContext);
			    _examAlert.setTitle("Hurry up..");
			    _examAlert.setIcon(R.drawable.exam_warning);
			    _examAlert
			    .setMessage("Four minutes left for exam to complete");
			    _examAlert.setPositiveButton("OK",
				    new DialogInterface.OnClickListener() {
				@Override
				public void onClick(
					DialogInterface dialog,
					int which) {
				    // TODO Auto-generated method stub
				    dialog.dismiss();
				}
			    });
			    final AlertDialog dialog = _examAlert.create();
			    dialog.show();
			} catch (final android.view.WindowManager.BadTokenException e) {
			    Log.e(tag, " Unable to add window" + e);
			}

		    }
		}
	    });
	}
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	Log.i("______________________________", "-----------ONPAUSE");
    }

    /**
     * Submit to server and finish exam.
     */
    private void submitToServerAndFinishExam() {
	// Submitting to server
	{
	    final String filePath = appData.getExamFilesPath(examId) + "/"
		    + ApplicationData.EXAM_ANSWERS_SUBMIT_FILENAME;// appData.getAppTempPath()
	    // +
	    // "questionPaper.xml";
	    final File file = new File(filePath);

	    final FileParameter fp = new FileParameter("file", file, "text/xml");

	    // StringParameter sp1 = new StringParameter("name", "joe");
	    // StringParameter sp2 = new StringParameter("age", "25");

	    final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();

	    // params.add(sp1);
	    // params.add(sp2);
	    params.add(fp);

	    try {
		final PostRequestHandler postRequest = new PostRequestHandler(
			examServerURL + "/upload.php?studentid="
				+ appData.getUserId() + "&gradeid="
				+ appData.getGrade() + "&examid="
				+ exam.getExamDetails().getExamId(), params,
				new DownloadCallback() {
			    @Override
			    public void onSuccess(String downloadedString) {
				Logger.info(tag, downloadedString);
			    }

			    @Override
			    public void onProgressUpdate(int progressPercentage) {

			    }

			    @Override
			    public void onFailure(String failureMessage) {

			    }
			});

		postRequest.request();
	    } catch (final Exception e) {
		Logger.error(tag, e);
	    }
	}

	appData.setCurrentExam(null);
	appData.setCurrentExamQuestionNo(-1);

	Toast.makeText(activityContext, R.string.exam_submitted_successfully,
		2000).show();

	// TODO update the status based on submission response
	exam.getExamDetails().setState(Exam.STUDENT_SUBMITTED);

	final Intent intent = new Intent(QuestionsActivity.this,
		ExamListActivity.class);
	intent.putExtra("paused", millis);
	Logger.warn(tag, "Paused Time: " + String.valueOf(millis));
	startActivity(intent);
	finish();
    }

    /**
     * Update navigation ui.
     */
    private void updateNavigationUI() {
	if (onlyMarked.isChecked()) {
	    int count = 0;
	    int markedCount = 0;
	    int currentQuestionMarkRank = -1;

	    if (question.isMarked()) {
		for (final Question ques : exam.getQuestions()) {
		    if (ques.isMarked()) {
			if (count == questionNo) {
			    currentQuestionMarkRank = markedCount;
			}

			markedCount++;
		    }

		    count++;
		}

		// show current page text view as { (actual quesion
		// no)bookmarkcount/total bookmarkeds }
		questionNoView.setText("(" + (questionNo + 1) + ") "
			+ (currentQuestionMarkRank + 1) + "/" + markedCount);

		Logger.error("Questiosn Activity", currentQuestionMarkRank
			+ "/" + markedCount);

		// show/hide buttons accordingly
		if (currentQuestionMarkRank >= markedCount - 1) {
		    nextButton.setEnabled(false);
		} else {
		    nextButton.setEnabled(true);
		}

		if (currentQuestionMarkRank <= 0) {
		    prevButton.setEnabled(false);
		} else {
		    prevButton.setEnabled(true);
		}
	    } else {
		int loopQuestioNo = 0;
		boolean showNext = false;
		boolean showPrev = false;

		for (final Question ques : exam.getQuestions()) {
		    if (ques.isMarked()) {
			markedCount++;

			if (questionNo < loopQuestioNo) {
			    showNext = true;
			}

			if (questionNo > loopQuestioNo) {
			    showPrev = true;
			}
		    }

		    if (ques.equals(question)) {
			currentQuestionMarkRank = markedCount;
		    }

		    loopQuestioNo++;
		}

		// show only current question no
		questionNoView.setText("" + (questionNo + 1));

		// show/hide buttons accordingly
		nextButton.setEnabled(showNext);
		prevButton.setEnabled(showPrev);
	    }
	} else {
	    // show normal current page text view
	    // show/hide buttons accordingly
	    updateNavigationUIToDefault();
	}
    }

    /**
     * Update navigation ui to default.
     */
    private void updateNavigationUIToDefault() {
	questionNoView.setText(questionNo + 1 + "/" + totalQuestions);

	if (questionNo <= 0) {
	    prevButton.setEnabled(false);
	} else {
	    prevButton.setEnabled(true);
	}

	if (questionNo >= totalQuestions - 1) {
	    nextButton.setEnabled(false);
	} else {
	    nextButton.setEnabled(true);
	}
    }

    /**
     * Save exam.
     *
     * @param filename the filename
     * @return true, if successful
     */
    private boolean saveExam(String filename) {
	if (null == filename || filename.trim().length() == 0) {
	    filename = ApplicationData.EXAM_ANSWERS_SAVE_FILENAME;
	}

	try {
	    final String filePath = appData.getExamFilesPath(examId) + "/" + filename;// appData.getAppTempPath()
	    // +
	    // "questionPaper.xml";

	    String answerSaveString = "";
	    exam.getExamDetails().setUploadedDate(System.currentTimeMillis());
	    exam.getExamDetails().setCompleteDate(0);
	    // answerSaveString = exam.toXML();

	    // TODO uncomment this and comment out above line to use json
	    // instead of xml
	    final ObjectMapper mapper = new ObjectMapper();
	    Logger.warn(tag, "questions sizeee is:"
		    + exam.getQuestions().size());

	    answerSaveString = mapper.writeValueAsString(exam);// exam.toXML()
	    Logger.warn(tag, "answer string when submitting is:"
		    + answerSaveString);

	    return ApplicationData.writeToFile(answerSaveString, filePath);
	    /*
	     * return ApplicationData.writeToFile(AEScrypto.encrypt(
	     * answerSaveString.getBytes(),
	     * ApplicationData.ENCRYPTION_PASSWORD), filePath);
	     */
	} catch (final Exception e) {
	    Logger.error(tag, e);

	    return false;
	}
    }

    /**
     * Save exam.
     *
     * @param filename the filename
     * @param examid the examid
     * @param appdata the appdata
     * @param exam the exam
     * @return true, if successful
     */
    boolean saveExam(String filename, String examid, ApplicationData appdata,
	    Exam exam) {
	if (null == filename || filename.trim().length() == 0) {
	    filename = ApplicationData.EXAM_ANSWERS_SAVE_FILENAME;
	}

	try {
	    final String filePath = appdata.getExamFilesPath(examid) + "/" + filename;// appData.getAppTempPath()
	    // +
	    // "questionPaper.xml";

	    String answerSaveString = "";

	    // answerSaveString = exam.toXML();

	    // TODO uncomment this and comment out above line to use json
	    // instead of xml
	    final ObjectMapper mapper = new ObjectMapper();
	    Logger.warn(tag, "questions sizeee is:"
		    + exam.getQuestions().size());

	    answerSaveString = mapper.writeValueAsString(exam);// exam.toXML()
	    Logger.warn(tag, "answer string when submitting is:"
		    + answerSaveString);

	    return ApplicationData.writeToFile(answerSaveString, filePath);
	    /*
	     * return ApplicationData.writeToFile(AEScrypto.encrypt(
	     * answerSaveString.getBytes(),
	     * ApplicationData.ENCRYPTION_PASSWORD), filePath);
	     */
	} catch (final Exception e) {
	    Logger.error(tag, e);

	    return false;
	}
    }

    /**
     * Gets the books for exam.
     *
     * @return the books for exam
     */
    private void getBooksForExam() {
	if (null != exam.getExamDetails().getAllowedBookIds()
		&& exam.getExamDetails().getAllowedBookIds().size() == 0) {
	    backToShelf.setVisibility(View.GONE);
	}
    }

    /**
     * HELPER FUNCTIONS.
     *
     * @param examId the exam id
     */
    private void populateExamDetails(final String examId) {
	Logger.warn(tag, "exam id in populateexam details is:" + examId);
	// TODO Hard coded temporarily to temp question paper xml file
	final String filePath = appData.getExamFilesPath(examId)
		+ ApplicationData.EXAM_FILENAME;// appData.getAppTempPath() +
	// "questionPaper.xml";
	final String fileAnswersPath = appData.getExamFilesPath(examId)
		+ ApplicationData.EXAM_ANSWERS_SAVE_FILENAME;// appData.getAppTempPath()
	// +
	// "questionPaper.xml";

	Logger.error(tag, "filePath is " + filePath);

	// Before getting from server.. check if already exists in local
	if (null != appData.getCurrentExam()
		&& appData.getCurrentExam().getExamDetails().getExamId()
		.equals(examId)) {
	    exam = appData.getCurrentExam();

	    Logger.info(tag, "Entered to - app data");
	} else if (ApplicationData.isFileExists(fileAnswersPath)) {
	    // this.exam = XMLParser.getExam(fileAnswersPath);
	    try {
		exam = ExamParser.getExam(fileAnswersPath);
	    } catch (final Exception e) {
		Logger.error(tag, e);
	    }

	    Logger.info(tag, "Entered to - Answer file path");
	} else if (ApplicationData.isFileExists(filePath)) {
	    // this.exam = XMLParser.getExam(filePath);
	    try {
		exam = ExamParser.getExam(filePath);
	    } catch (final Exception e) {
		Logger.error(tag, e);
	    }

	    Logger.info(tag, "Entered to - exam file path");
	} else { // If does not exists.. download
	    Logger.info(tag, "Entered to - File download");

	    loadingDialog.setTitle("Please wait..");
	    loadingDialog
	    .setMessage("Downloading/Refreshing the Question Paper..");
	    loadingDialog.show();

	    final ServerRequests serverRequests = new ServerRequests(activityContext);
	    final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();

	    // TODO XXX hardcoded params
	    final StringParameter sp1 = new StringParameter("studentId",
		    appData.getUserId());
	    final StringParameter sp2 = new StringParameter("testId", examId);

	    params.add(sp1);
	    params.add(sp2);

	    final PostRequestHandler postRequest = new PostRequestHandler(
		    serverRequests.getRequestURL(ServerRequests.QUESTIONS_LIST),
		    params, new DownloadCallback() {
			@Override
			public void onFailure(String failureMessage) {
			    Logger.warn("ExamList--->", "OnFailure in post");
			    runOnUiThread(new Runnable() {
				@Override
				public void run() {
				    Toast.makeText(activityContext,
					    "Unable to fetch the deails",
					    Toast.LENGTH_SHORT).show();

				    loadingDialog.hide();
				}
			    });
			}

			@Override
			public void onProgressUpdate(int progressPercentage) {
			    Logger.warn("ExamList--->", "OnFailure in progress");
			}

			@Override
			public void onSuccess(final String downloadedString) {
			    runOnUiThread(new Runnable() {
				@Override
				public void run() {
				    Logger.info(tag, "downloadString:"
					    + downloadedString);
				    Logger.warn("ExamList--->",
					    "OnSuccess in post");
				    exam = saveExamJsonToFile(downloadedString);

				    if (null == exam) {
					Toast.makeText(
						activityContext,
						R.string.unable_to_fetch_the_details,
						Toast.LENGTH_SHORT).show();
				    }

				    loadingDialog.hide();
				}
			    });
			}
		    });

	    postRequest.request();

	    /*
	     * // TODO XXX hardcoded questionpaper URL Download qpDownload = new
	     * Download(this.examServerURL+"/xmls/exams/questionPaper.veg",
	     * appData.getExamFilesPath(examId), ApplicationData.EXAM_FILENAME);
	     * DownloadManager dm = new DownloadManager(appData, qpDownload);
	     * 
	     * dm.startDownload(new DownloadCallback() {
	     * 
	     * @Override public void onSuccess(String downloadedString) {
	     * populateExamDetails(exam.getExamDetails().getId()); // -- Calling
	     * as recursive function
	     * 
	     * loadingDialog.hide(); }
	     * 
	     * @Override public void onProgressUpdate(int progressPercentage) {
	     * 
	     * }
	     * 
	     * @Override public void onFailure(String failureMessage) {
	     * populateExamDetails(exam.getExamDetails().getId());
	     * 
	     * loadingDialog.hide(); } });
	     */
	}
    }

    /**
     * Update question layout.
     *
     * @param question the question
     * @param questionLayout the question layout
     * @return the linear layout
     */
    private LinearLayout updateQuestionLayout(Question question,
	    LinearLayout questionLayout) {
	appData.setCurrentExamQuestionNo(question.getQuestionOrderNo());

	final QuestionsUI ui = QuestionsUI.getInstance();
	return ui.build(question, this, questionLayout);
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
     * Save exam json to file.
     *
     * @param examResponseJsonData the exam response json data
     * @return the exam
     */
    public Exam saveExamJsonToFile(String examResponseJsonData) {
	try {
	    final ObjectMapper mapper = new ObjectMapper();
	    final ObjectReader reader = mapper.reader(ExamResponse.class);
	    final ExamResponse examResponse = reader.readValue(examResponseJsonData);
	    final Exam tempExam = examResponse.getExam();

	    final ObjectMapper omp = new ObjectMapper();
	    final String examJson = omp.writeValueAsString(tempExam);

	    ApplicationData.writeToFile(examJson, appData
		    .getExamFileName(tempExam.getExamDetails().getExamId()));

	    return tempExam;
	} catch (final Exception e) {
	    Logger.error(tag, e);

	    return null;
	}
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "Exam Questions";
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);
	detachMenuItemFromMenu(menu, HOME_MENUITEM);
	detachMenuItemFromMenu(menu, FEEDBACK_MENUITEM);
	detachMenuItemFromMenu(menu, FAQ_MENUITEM);
	detachMenuItemFromMenu(menu, SIGNOUT_MENUITEM);
	detachMenuItemFromMenu(menu, OPTIONS_MENUITEM);
	attachMenuItemToMenu(menu, BACKTOEXAM_MENUITEM);
	return true;
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

    /*
     * @Override public void onBackPressed() { Intent intent = new Intent(this,
     * QuestionsListActivity.class); startActivity(intent); }
     */

    /**
     * Update exam details.
     *
     * @param exam the exam
     * @return the exam
     */
    public Exam updateExamDetails(Exam exam) {
	final List<AnswerChoice> modifiedChoice = new LinkedList<AnswerChoice>();
	final List<Question> questions = new LinkedList<Question>();
	final List<Question> questionsList = exam.getQuestions();
	if (questionsList != null && questionsList.size() > 0) {
	    final Iterator<Question> iterator = questionsList.iterator();
	    while (iterator.hasNext()) {
		final Question question = iterator.next();
		if (question.getType().equals(Question.MATCH_THE_FOLLOWING)) {
		    Log.i("QA",
			    "QUESTIONSACTIVITY--------------- in UpdateExamDetails");
		    final MultipleChoiceQuestion mfq = (MultipleChoiceQuestion) question;
		    final List<AnswerChoice> correctAnswerChoices = mfq.getChoices();
		    final List<AnswerChoice> studentAnswerChoices = mfq
			    .getStudentAnswer().getChoices();
		    if (correctAnswerChoices != null
			    && studentAnswerChoices != null
			    && correctAnswerChoices.size() > 0
			    && studentAnswerChoices.size() > 0) {
			Log.i("QA",
				"QUESTIONSACTIVITY--------------- in UpdateExamDetails in IF");
			for (int i = 0; i < correctAnswerChoices.size(); i++) {
			    final AnswerChoice correctAnswer = correctAnswerChoices
				    .get(i);
			    if (i < studentAnswerChoices.size()) {
				final AnswerChoice studentAnswer = studentAnswerChoices
					.get(i);
				correctAnswer.setSelected(studentAnswer
					.isSelected());
				correctAnswer
				.setTitle(studentAnswer.getTitle());
				correctAnswer.setMatch_FieldOne(studentAnswer
					.getMatch_FieldOne());
				correctAnswer.setMatch_FieldThree(studentAnswer
					.getMatch_FieldThree());
				correctAnswer.setMatch_FieldFour(studentAnswer
					.getMatch_FieldFour());
			    }
			    modifiedChoice.add(correctAnswer);
			}

		    } else {
			Log.i("QA",
				"QUESTIONSACTIVITY--------------- in UpdateExamDetails in else");
			for (int i = 0; i < correctAnswerChoices.size(); i++) {
			    final AnswerChoice correctAnswer = correctAnswerChoices
				    .get(i);
			    correctAnswer.setSelected(false);
			    correctAnswer.setTitle(correctAnswer.getTitle());
			    correctAnswer.setMatch_FieldOne(correctAnswer
				    .getMatch_FieldOne());
			    correctAnswer.setMatch_FieldThree("");
			    correctAnswer.setMatch_FieldFour(correctAnswer
				    .getMatch_FieldFour());
			    modifiedChoice.add(correctAnswer);
			}
		    }
		    final Answer modifiedAnswer = mfq.getStudentAnswer();
		    modifiedAnswer.setChoices(modifiedChoice);
		    mfq.setStudentAnswer(modifiedAnswer);
		    questions.add(mfq);
		} else if (question.getType()
			.equals(Question.ORDERING_QUESTION)) {
		    Log.i("QA",
			    "QUESTIONSACTIVITY--------------- in UpdateExamDetails ORDERING_QUESTION");
		    final OrderingQuestion OQ = (OrderingQuestion) question;
		    final List<AnswerChoice> correctAnswerChoices = OQ.getChoices();
		    final List<AnswerChoice> studentAnswerChoices = OQ
			    .getStudentAnswer().getChoices();
		    if (correctAnswerChoices != null
			    && studentAnswerChoices != null
			    && correctAnswerChoices.size() > 0
			    && studentAnswerChoices.size() > 0) {
			Log.i("QA",
				"QUESTIONSACTIVITY--------------- in UpdateExamDetails in IF");
			for (int i = 0; i < correctAnswerChoices.size(); i++) {
			    final AnswerChoice correctAnswer = correctAnswerChoices
				    .get(i);
			    if (i < studentAnswerChoices.size()) {
				final AnswerChoice studentAnswer = studentAnswerChoices
					.get(i);
				correctAnswer.setSelected(studentAnswer
					.isSelected());
				correctAnswer
				.setTitle(studentAnswer.getTitle());
				correctAnswer.setId(studentAnswer.getId());
				correctAnswer.setPosition(studentAnswer
					.getPosition());
			    }
			    modifiedChoice.add(correctAnswer);
			}

		    } else {
			Log.i("QA",
				"QUESTIONSACTIVITY--------------- in UpdateExamDetails in else");
			for (int i = 0; i < correctAnswerChoices.size(); i++) {
			    final AnswerChoice correctAnswer = correctAnswerChoices
				    .get(i);
			    correctAnswer.setSelected(false);
			    correctAnswer.setTitle(correctAnswer.getTitle());
			    correctAnswer.setId(correctAnswer.getId());
			    correctAnswer.setPosition(0);
			    modifiedChoice.add(correctAnswer);
			}
		    }
		    final Answer modifiedAnswer = OQ.getStudentAnswer();
		    modifiedAnswer.setChoices(modifiedChoice);
		    OQ.setStudentAnswer(modifiedAnswer);
		    questions.add(OQ);
		} else {
		    questions.add(question);
		}
	    }
	}

	exam.setQuestions(questions);

	return exam;
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	// TODO Auto-generated method stub
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    return true;
	}
	return super.onKeyDown(keyCode, event);
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onDestroy()
     */
    @Override
    public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	examTimer.cancel();
	wl.release();
    }

    /**
     * Upload exam.
     *
     * @param examId the exam id
     */
    public void uploadExam(final String examId){
	if(appStatus.isOnline(activityContext)){
	    // Show loading animation
	    loadingDialog.setTitle("Please Wait..");
	    loadingDialog
	    .setMessage("Uploading the answer paper to the server");
	    loadingDialog.show();

	    final ArrayList<RequestParameter> postParams = new ArrayList<RequestParameter>();

	    final String filePath = appData
		    .getExamFilesPath(examId)
		    + "/"
		    + ApplicationData.EXAM_ANSWERS_SUBMIT_FILENAME;// appData.getAppTempPath()
	    // +
	    // "questionPaper.xml";

	    // * For sending json request
	    final BaseRequest baseRequest = new BaseRequest();
	    baseRequest.setAuth(appData.getAuthID());

	    String examJsonString = "";
	    try {
		examJsonString = ApplicationData
			.readFile(filePath);

		// examJsonString =
		// AEScrypto.decrypt(filePath,
		// ApplicationData.ENCRYPTION_PASSWORD).toString();
	    } catch (final Exception e) {
		Logger.error(tag, e);
		ToastMessageForExceptions(R.string.JSON_PARSING_EXCEPTION);
		backToDashboard();
	    }

	    Logger.info(tag,
		    "EXAM - json for submitting exam is"
			    + examJsonString);

	    baseRequest.setData(examJsonString);

	    final ObjectMapper mapper = new ObjectMapper();
	    String examSubmitRequestJsonString = "";

	    try {
		examSubmitRequestJsonString = mapper
			.writeValueAsString(baseRequest);
	    } catch (final Exception e) {
		Logger.error(tag, e);
		ToastMessageForExceptions(R.string.JSON_PARSING_EXCEPTION);
		backToDashboard();
	    }

	    Logger.info(tag,
		    examSubmitRequestJsonString);

	    final StringParameter sp = new StringParameter(
		    "postdata",
		    examSubmitRequestJsonString);
	    postParams.add(sp);

	    final ServerRequests sr = new ServerRequests(
		    activityContext);

	    try {
		final PostRequestHandler postRequestObj = new PostRequestHandler(
			sr.getRequestURL(ServerRequests.EXAM_SUBMIT),
			postParams,
			new DownloadCallback() {
			    @Override
			    public void onSuccess(
				    String downloadedString) {
				Logger.info(tag,
					downloadedString);

				final File submittedFile = new File(
					appData.getExamSubmittedAnswersFileName(examId));
				final File uploadedFile = new File(
					appData.getExamUploadedAnswersFileName(examId));

				Logger.warn(
					"File is Existed---->",
					"true"
						+ appData
						.getExamUploadedAnswersFileName(examId));
				submittedFile
				.renameTo(uploadedFile);

				runOnUiThread(new Runnable() {
				    @Override
				    public void run() {
					loadingDialog
					.hide();
					Toast.makeText(activityContext, "Exam uploaded successfully", 10000).show();
				    }
				});
			    }

			    @Override
			    public void onProgressUpdate(
				    int progressPercentage) {

			    }

			    @Override
			    public void onFailure(
				    String failureMessage) {
				runOnUiThread(new Runnable() {
				    @Override
				    public void run() {
					Logger.warn(
						tag,
						"UPLOAD - failed to upload");
					Toast.makeText(
						activityContext,
						R.string.Unable_to_reach_pearl_server,
						toastDisplayTime)
						.show();
					loadingDialog
					.hide();
				    }
				});
			    }
			});

		postRequestObj.request();
	    } catch (final Exception e) {
		Logger.error(tag, e);
		ToastMessageForExceptions(R.string.FILE_SUBMISSION_EXCEPTION);
		backToDashboard();
	    }
	}else{
	    Toast.makeText(this, R.string.failed_to_upload, 10000).show();
	    Logger.warn(tag, "unable to upload as wifi is disabled");
	}
    }
}