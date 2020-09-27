package com.pearl.activities;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.R.string;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.examination.Exam;
import com.pearl.examination.Question;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.JSONParser;
import com.pearl.services.NotficationService;
import com.pearl.ui.models.ExamResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class QuestionsListActivity.
 */
public class QuestionsListActivity extends BaseActivity {
    
    /** The exam title. */
    TextView examTitle;
    
    /** The subject. */
    TextView subject;
    
    /** The millis. */
    long millis;
    
    /** The timer. */
    TextView timer;
    
    /** The teacher. */
    TextView teacher;
    
    /** The marks. */
    TextView marks;
    
    /** The student. */
    TextView student;
    
    /** The grade. */
    TextView grade;
    
    /** The i. */
    private int i = 0;
    
    /** The help. */
    ImageView backToShelf, help;

    /** The list. */
    private ListView list;
    
    /** The enteredtime. */
    long enteredtime;
    
    /** The question num. */
    private String questionNum;

    /** The exam timer. */
    Timer examTimer;
    
    /** The exam time out. */
    private boolean examTimeOut = false;

    /** The exam id. */
    private String examId;
    
    /** The exam. */
    private Exam exam;
    
    /** The questions list. */
    private List<Question> questionsList;

    /** The exam server url. */
    private String examServerURL = "";
    
    /** The h. */
    public static Handler h;
    
    private ImageView expandableName;
    
    /** The pm. */
    PowerManager pm;
    
    /** The wl. */
    PowerManager.WakeLock wl;
    
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
	setContentView(R.layout.questions_list);

	// Saving the activity name for two activities (QuestionsListActivity
	// and QuestionsActivity) in DB inorder to restrict
	// the navigation to Notice board when back to exam is clicked from the
	// shelf activity in resume

	final Bundle bundle = this.getIntent().getExtras();
	if (bundle != null) {
	    enteredtime = bundle.getLong("enteredTime");
	}
	context = this;
	final SharedPreferences appSharedPrefs = PreferenceManager
		.getDefaultSharedPreferences(this.getApplicationContext());

	final Editor prefsEditor = appSharedPrefs.edit();
	if (enteredtime != 0) {
	    prefsEditor.putLong("MyObject", enteredtime);
	    prefsEditor.commit();
	}
	String name = this.getClass().getName();
	final StringTokenizer st = new StringTokenizer(name, ".");
	while (st.hasMoreTokens()) {
	    name = st.nextToken();
	}
	try {
	    examServerURL = vegaConfig.getValue(VegaConstants.EXAM_SERVER_IP);
	    vegaConfig.setValue(VegaConstants.HISTORY_QUESTIONS_ACTIVITY, name);
	} catch (final InvalidConfigAttributeException e) {
	    Logger.error(tag, e);

	    examServerURL = "http://172.16.202.132:8088";
	} catch (final ColumnDoesNoteExistsException e) {
	    Logger.error(tag, e);
	}

	examTitle = (TextView) findViewById(R.id.exam_title);
	subject = (TextView) findViewById(R.id.subject);

	timer = (TextView) findViewById(R.id.timer);
	marks = (TextView) findViewById(R.id.marks);
	student = (TextView) findViewById(R.id.student);
	grade = (TextView) findViewById(R.id.student_grade);
	backToShelf = (ImageView) findViewById(R.id.backToShelf);
	help = (ImageView) findViewById(R.id.question_list_help);
//retrieve id here
	expandableName=(ImageView) findViewById(R.id.expandableName);
	list = (ListView) findViewById(R.id.questionsList);

	// q = new Question();
	final Bundle bundle1 = this.getIntent().getExtras();
	if (bundle1 != null) {
	    examId = bundle1.getString(VegaConstants.EXAM_ID);
	    Logger.warn(tag, "examId fron intent is:" + examId);
	} else {
	    // get the exam id from database.
	    try {
		examId = vegaConfig.getValue(VegaConstants.HISTORY_EXAM_ID);
	    } catch (final InvalidConfigAttributeException e) {
		Logger.error(tag, e);
	    }
	    Logger.warn(tag, "exam id is:" + examId);
	}
	//String tempName="samreen nnnnnnnnnnnnnnnnn";
	String tempName=appData.getUser().getSecondName();
	//student.setText(appData.getUser().getSecondName());// .getFirstName());
	
	if(tempName.length()>18)
	{
		tempName=tempName.substring(0,17) + "..";
		//make the image visible
		expandableName.setVisibility(View.VISIBLE);
		}
	else
	{
		//set the image visibility to invisible
		expandableName.setVisibility(View.INVISIBLE);
	}
	student.setText(tempName);
	expandableName.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Logger.warn(tag, "name clicked");
			// TODO Auto-generated method stub
			 AlertDialog.Builder builder = new AlertDialog.Builder(context);
			 builder.setTitle("Your full name");

             builder.setMessage(appData.getUser().getSecondName());
            // builder.setMessage("samreen nnnnnnnnnnnnnnnnn");
             builder.setCancelable(true);
             
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
			});

             builder.show();
			
		}
	});
	// on click of the exoandable image show an alert dialog
	//student.setText("samreen nnnnnnnnnnnnnnnnn");
	// TODO XXX hardcoded
	// grade.setText("Grade: IV");
	grade.setText(appData.getGradeName() + "," + appData.getSectionName());

	appData.setActivityName("QuestionsListActivity");

	backToShelf.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO pass bookslist as intent
		final Intent intent = new Intent(QuestionsListActivity.this,
			ShelfActivity.class);
		intent.putExtra(VegaConstants.EXAM_ID, examId);
		intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

		startActivity(intent);

		finish();
	    }
	});

	list.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> a, View v, int position,
		    long id) {
		Logger.warn(tag, "position onclick is:" + position);
		Logger.warn(tag, "question id on click is:"
			+ questionsList.get(position).getId());
		try {
		    /*
		     * vegaConfig.setValue(VegaConstants.QUESTION_NUM,
		     * questionsList.get(position).getId());
		     */
		    vegaConfig.setValue(VegaConstants.HISTORY_QUESTION_NUM,
			    position);
		} catch (final ColumnDoesNoteExistsException e) {
		    Logger.error(tag, e);
		}
		Logger.warn(tag, "Question id when clicked on list is:"
			+ questionsList.get(position).getId());
		questionsList.get(position).setViewed(true);
		exam.setQuestions(questionsList);
		final Intent intent = new Intent(QuestionsListActivity.this,
			QuestionsActivity.class);
		intent.putExtra(VegaConstants.QUESTION_NUM, position);
		intent.putExtra(VegaConstants.EXAM_ID, examId);
		startActivity(intent);
		finish();
	    }
	});
	
	 help.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showDialog();
		}
	});

	pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "QuestionsActivity");
	wl.acquire();

	final KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
	final KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
	lock.disableKeyguard();

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
	super.onPause();
	wl.release();
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	examTimer = new Timer();

	try {
	    examServerURL = vegaConfig.getValue(VegaConstants.EXAM_SERVER_IP);
	} catch (final InvalidConfigAttributeException e) {
	    Logger.error(tag, e);

	    examServerURL = "http://172.16.202.132:8088";
	}

	Logger.info(tag, "examServerURL:" + examServerURL);

	populateQuestionsList();
	/*
	 * Log.i("QUESTIONS LIST ACTIVITY", "OPEN BOOK EXAM STATUS is" +
	 * exam.isOpenBookExam());
	 */
	if (exam != null && exam.isOpenBookExam())
	    appData.setOpenBookExamBookList(exam.getOpenBooks());
	else
	    Logger.warn(tag, "OPEN BOOK - dont set the list");
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
		final List<String> list = HelpParser.getHelpContent("quizzard_ques_list_student.txt", this);
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
     * Populate questions list.
     */
    private void populateQuestionsList() {
	final String filePath = appData.getExamFilesPath(examId)
		+ ApplicationData.EXAM_FILENAME;
	final String AnswersFilePath = appData.getExamFilesPath(examId)
		+ ApplicationData.EXAM_ANSWERS_SAVE_FILENAME;
	Logger.warn(tag,
		"exam in populateQuestionsList is:" + appData.getCurrentExam());
	Logger.warn(tag, "answers file exists? " + AnswersFilePath);
	if (appData.getCurrentExam() != null)
	    Logger.warn(tag, "exam id from app data is:"
		    + appData.getCurrentExam().getExamDetails().getExamId());
	if (appData.getCurrentExam() != null
		&& appData.getCurrentExam().getExamDetails().getExamId()
		.equals(examId)) {
	    Logger.warn(tag, "get exam object from app data");
	    exam = appData.getCurrentExam();

	    Logger.info(tag, "Entered to - app data");

	    updateUI();
	} else {
	    if (ApplicationData.isFileExists(AnswersFilePath)) {
		Logger.info(tag, "Entered to - Answers path");

		try {
		    new File(appData.getExamFileName(examId));

		    // exam = ExamParser.getExam(file);
		    exam = JSONParser.getExam(AnswersFilePath);
		} catch (final Exception e) {
		    Logger.error(tag, e);

		    exam = new Exam();
		}

		updateUI();
	    } else if (ApplicationData.isFileExists(filePath)) {
		Logger.info(tag, "Entered to - exam file path");

		try {
		    exam = JSONParser.getExam(filePath);
		} catch (final Exception e) {
		    Logger.error(tag, e);

		    exam = new Exam();
		}

		updateUI();
	    } else {
		Logger.info(tag, "Entered to - download exam file path");

		loadingDialog.setTitle("Please wait..");
		loadingDialog
		.setMessage("Downloading/Refreshing the Question Paper..");
		loadingDialog.show();

		final ServerRequests serverRequests = new ServerRequests(
			activityContext);
		final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();

		// TODO XXX hardcoded params
		final StringParameter sp1 = new StringParameter("studentId",
			appData.getUserId());
		final StringParameter sp2 = new StringParameter("testId", examId);

		params.add(sp1);
		params.add(sp2);

		final PostRequestHandler postRequest = new PostRequestHandler(
			serverRequests
			.getRequestURL(ServerRequests.QUESTIONS_LIST),
			params, new DownloadCallback() {
			    @Override
			    public void onFailure(final String failureMessage) {
				runOnUiThread(new Runnable() {
				    @Override
				    public void run() {
					Toast.makeText(
						activityContext,
						R.string.unable_to_fetch_the_details
						+ failureMessage, 2000);

					loadingDialog.hide();
				    }
				});
			    }

			    @Override
			    public void onProgressUpdate(int progressPercentage) {
			    }

			    @Override
			    public void onSuccess(final String downloadedString) {
				runOnUiThread(new Runnable() {
				    @Override
				    public void run() {
					Logger.info(tag, "downloadString:"
						+ downloadedString);

					exam = saveExamJsonToFile(downloadedString);

					if (null == exam) {
					    Toast.makeText(
						    activityContext,
						    R.string.unable_to_fetch_the_details,
						    2000);
					}

					loadingDialog.hide();
				    }
				});
			    }
			});

		postRequest.request();
	    }
	}
    }

    /**
     * Update ui.
     */
    public void updateUI() {
	appData.setCurrentExam(exam);
	if (exam != null && exam.getQuestions() != null) {
	    questionsList = exam.getQuestions();
	    if (exam.isOpenBookExam()) {
		Logger.warn(tag, "OPENBOOK - This is open book");
		backToShelf.setVisibility(View.VISIBLE);
	    } else {
		Logger.warn(tag, "OPENBOOK - This is not open book");
	    }
	    if (questionsList != null && questionsList.size() > 0) {
		Logger.warn(tag,
			"questionslist size is:" + questionsList.size());

		final QuestionsListAdapter questionsListAdapter = new QuestionsListAdapter(
			this, android.R.layout.simple_selectable_list_item,
			questionsList);
		list.setAdapter(questionsListAdapter);
		questionsListAdapter.notifyDataSetChanged();

		examTitle.setText(exam.getExamDetails().getTitle());

		subject.setText(exam.getExamDetails().getSubject());
		// teacher.setText(exam.getTeacher()); TODO
		marks.setText(exam.getExamDetails().getMaxPoints() > 0 ? "Total Marks : "
			+ exam.getExamDetails().getMaxPoints()
			: "");

		examTimer.schedule(new UpdateTimeTask(), 100, 1000);
	    }
	} else {
	    // Deleting saved files
	    ApplicationData.deleteFolderOrFileRecursively(new File(appData
		    .getExamFileName(examId)));
	    Toast.makeText(
		    activityContext,
		    "Error in downloading question paper contact administrator",
		    Toast.LENGTH_LONG).show();
	    final Intent serviceIntent=new Intent(QuestionsListActivity.this,NotficationService.class);
	    final Intent intent = new Intent(QuestionsListActivity.this,
		    ExamListActivity.class);

	    overridePendingTransition(R.anim.push_right_in,
		    R.anim.push_right_out);
	    startService(serviceIntent);
	    startActivity(intent);
	}
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
	    Logger.warn(tag, "Stored Time is" + enteredtime);
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
	    /*
	     * Logger.warn(tag,
	     * "Duration is: "+exam.getExamDetails().getDuration());
	     * Logger.warn(tag,
	     * "Current time is"+System.currentTimeMillis()+"EndDate is: "
	     * +exam.getExamDetails().getEndDate());
	     */
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
			    final QuestionsActivity QA = new QuestionsActivity();
			    final boolean saveStatus = QA
				    .saveExam(
					    ApplicationData.EXAM_ANSWERS_SUBMIT_FILENAME,
					    examId, appData, exam);
			    if (saveStatus) {
				QA.isSubmitted = true;
				exam.getExamDetails().setState(
					Exam.YET_TO_UPLOAD);
				final Exam examNew = QA.updateExamDetails(exam);
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
										R.string.exam_saved_successfully, 2000)
										.show();*/
				if(!examNew.getExamDetails().isRetakeable())
				    QA.uploadExam(examId);
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
				examTimer.cancel();
				final Intent intent = new Intent(
					QuestionsListActivity.this,
					ExamListActivity.class);
				final Intent serviceIntet=new Intent(QuestionsListActivity.this ,NotficationService.class);
				startService(serviceIntet);
				startActivity(intent);
				finish();
			    }
			    Log.i("QESTIONSLISTACTIVITY--", "saveStatus---"
				    + saveStatus + "examTime---" + examTimeOut);

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
			} else
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

    /**
     * The Class QuestionsListAdapter.
     */
    public class QuestionsListAdapter extends ArrayAdapter<Question> {
	
	/** The items. */
	List<Question> items;

	/**
	 * Instantiates a new questions list adapter.
	 *
	 * @param context the context
	 * @param textViewResourceId the text view resource id
	 * @param questionsList the questions list
	 */
	public QuestionsListAdapter(Context context, int textViewResourceId,
		List<Question> questionsList) {
	    super(context, textViewResourceId, questionsList);
	    items = questionsList;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView,
		ViewGroup parent) {
	    View v = convertView;
	    if (v == null) {
		final LayoutInflater vi = (LayoutInflater) this.getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.questions_list_row, null);
	    }

	    final TextView questionType = (TextView) v
		    .findViewById(R.id.questionType);
	    final TextView ques = (TextView) v.findViewById(R.id.ques);
	    final TextView marks = (TextView) v.findViewById(R.id.marksAlloted);
	    final TextView question_no = (TextView)v. findViewById(R.id.list_question_no);
	    final TextView question_status = (TextView)v. findViewById(R.id.current_question);
	    final ImageView bookmark = (ImageView) v
		    .findViewById(R.id.quesListBookmark);
	    final ImageView seen = (ImageView) v.findViewById(R.id.seen);
	    final ImageView answered = (ImageView) v
		    .findViewById(R.id.answered);
	    final Question q = items.get(position);
	    Logger.warn(tag, "is marked:" + q.isMarked());
	    Logger.error(tag, "isAnswered:" + q.isViewed());

	    if (q.isMarked()) {
		bookmark.setImageResource(R.drawable.ques_bookmarked);
	    } else {
		bookmark.setImageResource(R.drawable.ques_unbookmarked);
	    }

	    if (q.isAnswered()) {

		answered.setImageResource(R.drawable.answered);
	    } else {
		answered.setImageResource(R.drawable.not_answered);
	    }

	    if (q.isViewed()) {
		seen.setImageResource(R.drawable.eye);
	    } else {
		seen.setImageResource(R.drawable.ceye);
	    }

	    marks.setText(q.getMarksAlloted() + "M");

	    if (q != null) {
		int pos = 0;
		String question = q.getQuestion();
		if (question.length() > 80) {
		    pos = question.substring(80).indexOf("  ");
		    question = question.substring(0, 80 + pos) + "...";

		} else {
		    Log.i("__________________", "question length is "
			    + question.length());
		    pos = question.substring(question.length()).indexOf("");
		    question = question.substring(0, question.length());

		}
		Logger.info(tag, "QUESTION is" + question);
		question_no.setText(q.getQuestionOrderNo()+":");
		ques.setText(question);
		if(q.getQuestionOrderNo() == appData
			.getCurrentExamQuestionNo()){
			question_status.setVisibility(View.VISIBLE);			
		}
		/*ques.setText((q.getQuestionOrderNo() == appData
			.getCurrentExamQuestionNo() ? ">" + q
				.getQuestionOrderNo() : q.getQuestionOrderNo())
				+ ": " + question);
*/
		if (q.getType().equals(Question.FILL_IN_THE_BLANK_QUESTION)) {
		    questionType.setText("FIB");
		} else if (q.getType().equals(Question.LONG_ANSWER_QUESTION)) {
		    questionType.setText("LAQ");
		} else if (q.getType().equals(Question.MULTIPLECHOICE_QUESTION)) {
		    questionType.setText("MCQ");
		} else if (q.getType().equals(Question.ORDERING_QUESTION)) {
		    questionType.setText("OQ");
		} else if (q.getType().equals(Question.SHORT_ANSWER_QUESTION)) {
		    questionType.setText("SAQ");
		} else if (q.getType().equals(Question.TRUE_OR_FALSE_QUESTION)) {
		    questionType.setText("TFQ");
		} else if (q.getType().equals(Question.MATCH_THE_FOLLOWING)) {
		    questionType.setText("MTF");
		} else {
		    questionType.setText("Q");
		}

		questionNum = q.getId();
		Logger.info(tag, "question id is:" + questionNum);
		bookmark.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View v) {
			Logger.warn(tag, "position is:" + position);
			final Question ques = toggleBookmarkStatus(q, position,
				bookmark);
			Logger.warn(tag, "----- status--- :" + ques.isMarked());
			// exam.getQuestions().get(position).setMarked(ques.isMarked());
		    }
		});
	    }
	    return v;
	}

	/**
	 * Toggle bookmark status.
	 *
	 * @param q the q
	 * @param pos the pos
	 * @param bookmark the bookmark
	 * @return the question
	 */
	private Question toggleBookmarkStatus(Question q, int pos,
		ImageView bookmark) {
	    final boolean status = q.isMarked();

	    Logger.warn(tag, "status in toggle is:" + status);
	    if (status) {
		bookmark.setImageResource(R.drawable.ques_unbookmarked);
	    } else {
		bookmark.setImageResource(R.drawable.ques_bookmarked);
	    }
	    Logger.warn(tag, "status before setting is:" + !status);
	    q.setMarked(!status);
	    return q;
	}
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "QuestionsListActivity";
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onStop()
     */
    @Override
    public void onStop() {
	super.onStop();

	// examTimer.cancel();
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
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	// TODO Auto-generated method stub
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    Log.i("--------------------", "TRUE keycode...");
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
	Log.i("--------------------", "SUNNY CALLING onDestroy for testing the exam status");
	examTimer.cancel();

    }
    
}
