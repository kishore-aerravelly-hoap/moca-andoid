package com.pearl.activities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.examination.Exam;
import com.pearl.examination.ExamDetails;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.JSONParser;
import com.pearl.parsers.json.ExamListParser;
import com.pearl.services.NotficationService;
import com.pearl.ui.models.BaseRequest;
import com.pearl.ui.models.ExamResponse;
import com.pearl.ui.models.RoleType;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamListActivity.
 */
public class ExamListActivity extends BaseActivity {
    /*
     * (non-Javadoc)
     * 
     * @see com.pearl.activities.BaseActivity#onDestroy()
     */

    /** The exams array list. */
    private ArrayList<ExamDetails> examsArrayList;
    
    /** The exams list. */
    private ListView examsList;
    
    /** The exam. */
    private Exam exam;
    
    /** The vega config. */
    private VegaConfiguration vegaConfig;
    
    /** The refresh exam list. */
    ImageView refreshExamList;
    
    /** The onclick. */
    boolean onclick = false;
    
    /** The set alpha. */
    boolean setAlpha = false;
    
    /** The menu button. */
    Button menuButton;
    
    /** The n status. */
    boolean nStatus;
    
    /** The server requests. */
    private ServerRequests serverRequests;
    
    /** The wf manager. */
    WifiManager wfManager;
    
    /** The exam server url. */
    private String examServerURL;
    
    /** The progress bar. */
    private ProgressBar progressBar;
    
    /** The emptystatus. */
    TextView emptystatus;
    
    /** The ll. */
    RelativeLayout ll;
    
    /** The reenteredtime. */
    Long pausedtime, reenteredtime;
    
    /** The h. */
    public static Handler h;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.exam_list);
	vegaConfig = new VegaConfiguration(this);
	refreshExamList = (ImageView) findViewById(R.id.refreshExamList);
	serverRequests = new ServerRequests(activityContext);
	emptystatus = (TextView) findViewById(R.id.empty_status);
	ll = (RelativeLayout) findViewById(R.id.linearLayout2);
	// progressBar = (ProgressBar)
	// findViewById(R.id.examListRefreshProgress);

	try {
	    examServerURL = vegaConfig.getValue(VegaConstants.EXAM_SERVER_IP);

	    // XXX
	    // examServerURL = "http://172.16.202.132:8088";
	} catch (final InvalidConfigAttributeException e) {
	    Logger.error(tag, e);
	    // examServerURL = "http://172.16.202.133:8080";
	    ToastMessageForExceptions(R.string.GET_COUNT_INVALID_ATTRIBUTE_EXCEPTION);
	    backToDashboard();
	}
	menuButton = (Button) findViewById(R.id.examMenu);

	wfManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	examsList = (ListView) findViewById(R.id.examsList);

	if (wfManager.isWifiEnabled()) {
	    downloadExamsList();
	} else if (ApplicationData.isFileExists(appData.getExamsListFile())) {
	    Toast.makeText(getApplicationContext(),
		    "Please check your wi-fi connection", Toast.LENGTH_LONG)
		    .show();
	    getExamsList();
	} else {
	    emptystatus.bringToFront();
	    ll.setVisibility(View.GONE);
	    emptystatus.setText(R.string.empty_status1);
	}
	// TODO XXX
	// if(true){//!ApplicationData.isFileExists(appData.getExamsListFile())){
	/*
	 * if(!ApplicationData.isFileExists(appData.getExamsListFile())){
	 * 
	 * }else{ try { String content =
	 * ApplicationData.readFile(appData.getExamsListFile());
	 * Logger.warn(tag, "Exams list json is:"+content); examsArrayList =
	 * JSONParser
	 * .getExamsList(ApplicationData.readFile(appData.getExamsListFile()));
	 * 
	 * examsList.setAdapter(new ExamslistArrayAdapter(activityContext,
	 * R.layout.examlist_row, examsArrayList)); } catch (Exception e) {
	 * Logger.error(tag, e); } }
	 */

	/*
	 * XXX examsList.setOnItemClickListener(new OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> a, View v, int
	 * position, long id) { Intent intent = new Intent(); intent
	 * .putExtra("exam_id", examsArrayList.get(position) .getId()); try{
	 * vegaConfig.setValue(VegaConstants.EXAM_ID,
	 * examsArrayList.get(position).getId());
	 * }catch(ColumnDoesNoteExistsException e){ Logger.warn(tag,
	 * "Invalid attribute 'EXAM_ID':"+e.toString()); }
	 * intent.setClass(v.getContext(), QuestionsListActivity.class);
	 * 
	 * startActivity(intent); } });
	 */

	updateUI();
	bindEvents();

	h = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
		super.handleMessage(msg);

		switch (msg.what) {

		case 0:
		    finish();
		    break;
		}
	    }
	};
	final Bundle bundle = getIntent().getExtras();
	Logger.warn(tag, "00000000000000000:"+bundle);
	if(bundle != null){
	    Logger.warn(tag, "1111111111111111111111111"+bundle.getString("NOTIF"));
	}

    }

    /**
     * Update ui.
     */
    public void updateUI() {
	// TODO
    }

    /**
     * HELPER ADAPTERS.
     */
    private class ExamslistArrayAdapter extends ArrayAdapter<ExamDetails> {
	
	/** The items. */
	private final ArrayList<ExamDetails> items;

	/**
	 * Instantiates a new examslist array adapter.
	 *
	 * @param context the context
	 * @param textViewResourceId the text view resource id
	 * @param items the items
	 */
	public ExamslistArrayAdapter(Context context, int textViewResourceId,
		ArrayList<ExamDetails> items) {
	    super(context, textViewResourceId, items);
	    this.items = items;
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
		v = vi.inflate(R.layout.examlist_row, null);
	    }

	    final ExamDetails exam = items.get(position);

	    if (exam != null) {
		final TextView subject = (TextView) v.findViewById(R.id.subject);
		final TextView title = (TextView) v.findViewById(R.id.title);
		final TextView category = (TextView) v.findViewById(R.id.category);
		// TextView description = (TextView) v.findViewById(R.id.title);
		/*final TextView starttime = (TextView) v.findViewById(R.id.start);
		final TextView endtime = (TextView) v.findViewById(R.id.end);*/
		final TextView duration = (TextView) v.findViewById(R.id.dur);
		final TextView examStatus = (TextView) v
			.findViewById(R.id.exam_status);
		final Button action = (Button) v
			.findViewById(R.id.download_button);
		final Button retake = (Button) v
			.findViewById(R.id.retake_button);
		final Button viewAnswerSheet = (Button) v
			.findViewById(R.id.view_answer_sheet);
		/*
		 * Button startExam = (Button)v.findViewById(R.id.start_button);
		 * //TODO refine the string Button emptyLabel =
		 * (Button)v.findViewById(R.id.empty_button_label);
		 */

		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(exam.getStartDate());
		final Date examStartDateTime = new Date(exam.getStartDate());
		final Date examEndDateTime = new Date(exam.getEndDate());
		final Date currentDateTime = new Date();

		final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"EEE, d MMM, ''yy HH:mm");
		final String startdate = dateFormatter
			.format(examStartDateTime);
		final String enddate = dateFormatter.format(examEndDateTime);

		String actionType = "";

		String desc = exam.getDescription();
		if (null != desc && desc.length() > 90) {
		    int spacePos = 0;

		    spacePos = desc.substring(90).indexOf(" ");
		    desc = desc.substring(0, 90 + spacePos) + "..";
		    // description.setText(desc);
		}
		subject.setText(exam.getSubject());
		// title.setText(DateParser(exam.getStartDate()));
		title.setText(exam.getTitle());
		// description.setText(desc);
		final int endDateIndex = enddate.indexOf(" ");
		final int startDateIndex = startdate.indexOf(" ");
		/*endtime.setText("Ends by  :"
			+ enddate.substring(endDateIndex, enddate.length()));
		starttime.setText("Starts at :"
			+ startdate.substring(startDateIndex,
				startdate.length()));*/
		duration.setText("" + exam.getDuration());
		category.setText(exam.getExamCategory());
		retake.setVisibility(View.GONE);
		action.setVisibility(View.GONE);
		examStatus.setVisibility(View.GONE);
		viewAnswerSheet.setVisibility(View.GONE);

		if (appData.getUser().getUserType()
			.equalsIgnoreCase(RoleType.STUDENT.name())) {
		    if (currentDateTime.before(examStartDateTime)) {// Exam.SCHEDULED.equals(exam.getExamState()))
			if (!isExamPaperDownloaded(exam.getExamId())) {
			    action.setText("Download");

			    action.setVisibility(View.VISIBLE);

			    actionType = "download";
			} else {
			    examStatus.setVisibility(View.VISIBLE);

			    examStatus.setText("Starts at " + startdate
				    + "  Ends on " + enddate);
			    examStatus
			    .setTextColor(Color.parseColor("#000000"));
			}
		    } else if (currentDateTime.after(examStartDateTime)
			    && currentDateTime.before(examEndDateTime)) {
			action.setVisibility(View.VISIBLE);

			if (!isExamPaperDownloaded(exam.getExamId())) {
			    action.setText("Download & Start Exam");

			    actionType = "download and start";
			} else {
			    if (ApplicationData.isFileExists(appData
				    .getExamSubmittedAnswersFileName(exam
					    .getExamId()))) {

				if (exam.isRetakeable()) {
				    action.setText("Upload");
				    retake.setVisibility(View.VISIBLE);
				}else
				    action.setVisibility(View.GONE);

				actionType = "upload";
			    } else if (ApplicationData.isFileExists(appData
				    .getExamSavedAnswersFileName(exam
					    .getExamId()))) {
				/*
				 * if ((appData.getCurrentExam() != null) &&
				 * (appData.getCurrentExam().getId() == exam
				 * .getId())) {
				 */
				action.setText("Continue");

				if (exam.isRetakeable()) {
				    retake.setVisibility(View.VISIBLE);
				}

				actionType = "continue";
			    } else if (ApplicationData.isFileExists(appData
				    .getExamUploadedAnswersFileName(exam
					    .getExamId()))) {
				Logger.warn(tag, "ANSWER - uploaded");
				Logger.warn(tag, "ANSWER - exam is evaluated:"
					+ exam.isEvaluated());
				if (exam.isEvaluated()) {
				    action.setVisibility(View.GONE);
				    if (exam.isRetakeable()) {
					retake.setVisibility(View.VISIBLE);
				    }
				    viewAnswerSheet.setVisibility(View.VISIBLE);
				} else {
				    if (exam.isRetakeable()) {
					retake.setVisibility(View.VISIBLE);
				    }
				    action.setVisibility(View.GONE);
				    viewAnswerSheet.setVisibility(View.GONE);
				    examStatus.setVisibility(View.VISIBLE);
				    /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				    params.setMargins(0, 10, 0, 0);*/
				    examStatus.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				    examStatus
				    .setText("Results yet to release");
				    examStatus.setTextColor(Color
					    .parseColor("#000000"));
				}
			    } else if(exam.isEvaluated()){
				action.setVisibility(View.GONE);
				if (exam.isRetakeable()) {
				    retake.setVisibility(View.VISIBLE);
				}
				viewAnswerSheet.setVisibility(View.VISIBLE);
			    } else if (isExamPaperDownloaded(exam.getExamId())) {
				action.setText("Start Exam");
				actionType = "start";
			    }
			}

		    } else if (currentDateTime.after(examEndDateTime)) {
			retake.setVisibility(View.GONE);
			if (ApplicationData.isFileExists(appData
				.getExamSubmittedAnswersFileName(exam
					.getExamId())) && exam.isRetakeable()) {
			    action.setVisibility(View.VISIBLE);
			    action.setText("Upload");
			    actionType = "upload";
			} else if (ApplicationData.isFileExists(appData
				.getExamUploadedAnswersFileName(exam
					.getExamId()))) {
			    Logger.warn(tag, "END - exam uploaded file exists");
			    Logger.warn(tag, "ANSWER - uploaded");
			    Logger.warn(tag, "ANSWER - exam is evaluated:"
				    + exam.isEvaluated());
			    if (exam.isEvaluated()) {
				action.setVisibility(View.GONE);
				viewAnswerSheet.setVisibility(View.VISIBLE);
			    } else {
				action.setVisibility(View.GONE);
				viewAnswerSheet.setVisibility(View.GONE);
				examStatus.setVisibility(View.VISIBLE);
				examStatus.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				examStatus.setText("Results yet to release");
				examStatus.setTextColor(Color
					.parseColor("#000000"));
			    }
			} else if(exam.isEvaluated()){
			    if (exam.isEvaluated()) {
				action.setVisibility(View.GONE);
				viewAnswerSheet.setVisibility(View.VISIBLE);
			    } else {
				action.setVisibility(View.GONE);
				viewAnswerSheet.setVisibility(View.GONE);
				examStatus.setVisibility(View.VISIBLE);
				examStatus.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				examStatus.setText("Results yet to release");
				examStatus.setTextColor(Color
					.parseColor("#000000"));
			    }
			    Logger.warn(tag,
				    "END - eeeeeeeeeeeeennnnnnnnnnnddddd ");
			}else{
				examStatus.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
				examStatus.setVisibility(View.VISIBLE);
				examStatus.setText("Exam expired");
			    Logger.warn("+__+_++SAD", "Nothing");
			    retake.setVisibility(View.GONE);
			    action.setVisibility(View.GONE);
			    viewAnswerSheet.setVisibility(View.GONE);
			}
		    } else {
		    	examStatus.setText("Exam expired");
		    }
		    /*

		     * else if (Exam.YET_TO_UPLOAD.equals(exam.getState())) {
		     * action.setVisibility(View.VISIBLE);
		     * 
		     * action.setText("Upload");
		     * 
		     * actionType = "upload"; } else if
		     * (Exam.UPLOADING.equals(exam.getState())) {
		     * 
		     * examStatus.setVisibility(View.VISIBLE);
		     * examStatus.setText("Uploading Answer paper");
		     * 
		     * Logger.info(tag, "Exam status is 1 " +
		     * "Uploading Answer paper"); } else if
		     * (Exam.UPLOAD_FAILED.equals(exam.getState())) {
		     * action.setVisibility(View.VISIBLE);
		     * 
		     * action.setText("Upload Failed, Retry");
		     * 
		     * actionType = "upload"; } else if
		     * (Exam.STUDENT_SUBMITTED.equals(exam.getState())) {
		     * examStatus.setVisibility(View.VISIBLE);
		     * 
		     * examStatus.setText("Results yet to Release");
		     * examStatus.setTextColor(Color.parseColor("#000000"));
		     * 
		     * Logger.info(tag, "Exam status is 2 " +
		     * "Results yet to Release"); } else if
		     * (Exam.TEACHER_EVALUATED.equals(exam.getState())) {
		     * examStatus.setVisibility(View.VISIBLE);
		     * 
		     * examStatus.setText("Results yet to Release");
		     * 
		     * Logger.info(tag, "Exam status is 3 " +
		     * "Results yet to Release 2"); } else if
		     * (Exam.RESULTS_RELEASED.equals(exam.getState())) {
		     * action.setVisibility(View.VISIBLE);
		     * 
		     * action.setText("Results");
		     * 
		     * actionType = "results";
		     * 
		     * if (exam.isRetakeable()) {
		     * retake.setVisibility(View.VISIBLE); } }
		     */
		} else if (appData.getUser().getUserType()
			.equalsIgnoreCase(RoleType.SUBJECTHEAD.name())
			|| appData.getUser().getUserType()
			.equalsIgnoreCase(RoleType.PRINCIPLE.name())
			|| appData
			.getUser()
			.getUserType()
			.equalsIgnoreCase(
				RoleType.HOMEROOMTEACHER.name())) {

		    action.setText("Download");

		    action.setVisibility(View.VISIBLE);

		    actionType = "download";
		}


		retake.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			// TODO @kiran : Get confirmation from @samreen and
			// delete below code.. no need to save in config ??!!??
			Logger.warn(tag, "exam id onclick is:"
				+ examsArrayList.get(position).getExamId());
			if (ApplicationData.isFileExists(appData
				.getExamSavedAnswersFileName(exam.getExamId()))) {
			    /*
			     * File file = new
			     * File(appData.getExamSavedAnswersFileName
			     * (exam.getExamId())); file.delete();
			     */
			    final File file1 = new File(appData
				    .getExamSavedAnswersFileName(exam
					    .getExamId()));
			    file1.delete();
			}
			try {
			    vegaConfig.setValue(VegaConstants.HISTORY_EXAM_ID,
				    examsArrayList.get(position).getExamId()
				    + "");
			} catch (final ColumnDoesNoteExistsException e) {
			    Logger.warn(
				    tag,
				    "Invalid attribute 'EXAM_ID':"
					    + e.toString());
			    ToastMessageForExceptions(R.string.COLUMN_DOES_NOT_EXIST_NOTICEBOARDACTIVITY);
			    backToDashboard();
			}

			// appData.setCurrentExam(null);
			final Intent intent = new Intent(ExamListActivity.this,
				QuestionsListActivity.class);
			final Intent serviceIntent=new Intent(ExamListActivity.this,NotficationService.class);

			intent.putExtra("exam_id", examsArrayList.get(position)
				.getExamId());
			intent.putExtra("enteredTime",
				System.currentTimeMillis());
			intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			stopService(serviceIntent);
			startActivity(intent);
			finish();
		    }
		});

		viewAnswerSheet.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			if (wfManager.isWifiEnabled()) {
			    final Intent intent = new Intent(getApplicationContext(),
				    ExamResultsActivity.class);
			    intent.putExtra(VegaConstants.EXAM_ID,
				    exam.getExamId());
			    startActivity(intent);
			} else if (ApplicationData.isFileExists(appData
				.getEvaluatedAnswerSheetFile(exam.getExamId()))) {
			    final Intent intent = new Intent(getApplicationContext(),
				    ExamResultsActivity.class);
			    intent.putExtra(VegaConstants.EXAM_ID,
				    exam.getExamId());
			    startActivity(intent);
			} else {
			    Toast.makeText(getApplicationContext(),
				    "Please check your Wi-Fi connection",
				    Toast.LENGTH_SHORT).show();
			}

		    }
		});

		// TODO XXX hardcoded questionpaper URL
		// Download qpDownload = new
		// Download(examServerURL+"/xmls/exams/questionPaper.veg",
		// appData.getExamFilesPath(exam.getExamId()),
		// ApplicationData.EXAM_FILENAME);
		// final DownloadManager dm = new DownloadManager(appData,
		// qpDownload);

		final String tempActionType = actionType;
		if (!"".equals(actionType)) {
		    action.setOnClickListener(new OnClickListener() {
			String examActionType = tempActionType;

			@Override
			public void onClick(View v) {
			    final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();

			    // TODO XXX hardcoded params

			    PostRequestHandler getQuestionsRequest = null;

			    if (appData.getUser().getUserType()
				    .equalsIgnoreCase(RoleType.STUDENT.name())) {
				final StringParameter sp1 = new StringParameter(
					"studentId", appData.getUserId());
				final StringParameter sp2 = new StringParameter(
					"testId", exam.getExamId());

				params.add(sp1);
				params.add(sp2);

				Logger.error(
					tag,
					"request url  AS STUDENT "
						+ serverRequests
						.getRequestURL(ServerRequests.QUESTIONS_LIST)
						+ " with post values as '"
						+ appData.getUserId() + "',"
						+ exam.getExamId());
				getQuestionsRequest = new PostRequestHandler(
					serverRequests
					.getRequestURL(ServerRequests.QUESTIONS_LIST),
					params);
			    } else if (appData
				    .getUser()
				    .getUserType()
				    .equalsIgnoreCase(
					    RoleType.SUBJECTHEAD.name())
					    || appData
					    .getUser()
					    .getUserType()
					    .equalsIgnoreCase(
						    RoleType.PRINCIPLE.name())
						    || appData
						    .getUser()
						    .getUserType()
						    .equalsIgnoreCase(
							    RoleType.HOMEROOMTEACHER
							    .name())) {
				final StringParameter sp1T = new StringParameter(
					"teacherId", appData.getUserId());
				final StringParameter sp2T = new StringParameter(
					"testId", exam.getExamId());

				params.add(sp1T);
				params.add(sp2T);

				/*
				 * Logger .error( tag,
				 * "request url as teacher is " + serverRequests
				 * .
				 * getRequestURL(ServerRequests.EXAM_PAPER_TEACHER
				 * ) + " with post values as '" +
				 * appData.getUserId() + "'," +
				 * exam.getExamId()); getQuestionsRequest = new
				 * PostRequestHandler( serverRequests
				 * .getRequestURL
				 * (ServerRequests.EXAM_PAPER_TEACHER),params);
				 */
			    }

			    if ("download".equals(examActionType)) {
				if (wfManager.isWifiEnabled()) {

				    // TODO
				    {
					loadingDialog.setTitle("Please wait..");
					loadingDialog
					.setMessage("Downloading the Question Paper..");
					loadingDialog.show();

					getQuestionsRequest
					.request(new DownloadCallback() {
					    @Override
					    public void onFailure(
						    String failureMessage) {
						runOnUiThread(new Runnable() {

						    @Override
						    public void run() {
							// TODO
							// Auto-generated
							// method stub
							Toast.makeText(
								activityContext,
								R.string.unable_to_fetch_the_details,
								2000);

						    }
						});

						loadingDialog.hide();
					    }

					    @Override
					    public void onProgressUpdate(
						    int progressPercentage) {
					    }

					    @Override
					    public void onSuccess(
						    final String downloadedString) {
						runOnUiThread(new Runnable() {
						    @Override
						    public void run() {
							Logger.error(
								tag,
								"downloadString:"
									+ downloadedString);

							saveExamJsonToFile(
								downloadedString,
								exam);

							if (appData
								.getUser()
								.getUserType()
								.equalsIgnoreCase(
									RoleType.STUDENT
									.name())) {

							    action.setVisibility(View.GONE);
							    examStatus
							    .setVisibility(View.VISIBLE);
							    examStatus
							    .setText("Starts at "
								    + startdate
								    + " & Ends on "
								    + enddate);

							    examActionType = "";
							} else if (appData
								.getUser()
								.getUserType()
								.equalsIgnoreCase(
									RoleType.SUBJECTHEAD
									.name())
									|| appData
									.getUser()
									.getUserType()
									.equalsIgnoreCase(
										RoleType.PRINCIPLE
										.name())
										|| appData
										.getUser()
										.getUserType()
										.equalsIgnoreCase(
											RoleType.HOMEROOMTEACHER
											.name())) {
							    try {
								vegaConfig
								.setValue(
									VegaConstants.HISTORY_EXAM_ID,
									examsArrayList
									.get(position)
									.getExamId()
									+ "");
							    } catch (final ColumnDoesNoteExistsException e) {
								e.printStackTrace();
							    }
							    final Intent intent = new Intent();
							    intent.putExtra(
								    VegaConstants.EXAM_ID,
								    exam.getExamId());
							    intent.setClass(
								    getContext(),
								    QuestionsListActivity.class);
							    intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
							    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
							    startActivity(intent);
							    finish();
							}

							loadingDialog
							.hide();
						    }
						});
					    }
					});
					/*
					 * dm.startDownload(new
					 * DownloadCallback() {
					 * 
					 * @Override public void
					 * onSuccess(String downloadedString) {
					 * action.setVisibility(View.GONE);
					 * 
					 * examStatus.setVisibility(View.VISIBLE)
					 * ; examStatus.setText("Starts at " +
					 * startdate + " & Ends on "+enddate);
					 * 
					 * examActionType = "";
					 * 
					 * loadingDialog.hide(); }
					 * 
					 * @Override public void
					 * onProgressUpdate(int
					 * progressPercentage) {
					 * 
					 * }
					 * 
					 * @Override public void
					 * onFailure(String failureMessage) {
					 * action.setText("Retry Download");
					 * 
					 * Toast.makeText(activityContext,
					 * "Download Failed!! Retry",
					 * 1000).show();
					 * 
					 * loadingDialog.hide(); } });
					 */
					// Download the question paper for
					// current
					// exam id in the background

					// Hide download button

					// show downloading progress bar as
					// replacement or..
					// simply show a text as
					// "Started Downloading.."
					// update the message on success or fail
					// or
					// update accordingly
				    }
				} else {
				    Toast.makeText(
					    getApplicationContext(),
					    "Please check your Wi-fi connection",
					    Toast.LENGTH_SHORT).show();
				}
			    } else if ("download and start"
				    .equals(examActionType)) {
				if (wfManager.isWifiEnabled()) {

				    {
					// Show loading animation
					loadingDialog.setTitle("Please wait..");
					loadingDialog
					.setMessage("Downloading the Question Paper..");
					loadingDialog.show();

					getQuestionsRequest
					.request(new DownloadCallback() {
					    @Override
					    public void onSuccess(
						    final String downloadedString) {
						Logger.error(
							tag,
							"download -----> success. downloadString:"
								+ downloadedString);

						runOnUiThread(new Runnable() {
						    @Override
						    public void run() {
							action.setText("Start Exam");
							examActionType = "start";

							saveExamJsonToFile(
								downloadedString,
								exam);
							loadingDialog
							.hide();
						    }
						});
					    }

					    @Override
					    public void onProgressUpdate(
						    int progressPercentage) {
						Logger.warn(tag,
							"download ----> in progress");
					    }

					    @Override
					    public void onFailure(
						    String failureMessage) {
						Logger.warn(tag,
							"download ----> failed");
						runOnUiThread(new Runnable() {
						    @Override
						    public void run() {
							action.setText("Retry Download");

							Toast.makeText(
								activityContext,
								R.string.download_Failed_Retry,
								1000)
								.show();

							loadingDialog
							.hide();
						    }
						});
					    }
					});
					/*
					 * dm.startDownload(new
					 * DownloadCallback() {
					 * 
					 * @Override public void
					 * onSuccess(String downloadedString) {
					 * action.setText("Start Exam");
					 * 
					 * examActionType = "start";
					 * 
					 * loadingDialog.hide(); }
					 * 
					 * @Override public void
					 * onProgressUpdate(int
					 * progressPercentage) {
					 * 
					 * }
					 * 
					 * @Override public void
					 * onFailure(String failureMessage) {
					 * action.setText("Retry Download");
					 * 
					 * Toast.makeText(activityContext,
					 * "Download Failed!! Retry",
					 * 1000).show();
					 * 
					 * loadingDialog.hide(); } });
					 */
					// Download the question paper for
					// current
					// exam id in the background

					// on success full download .. take to
					// questions list activity
				    }
				} else {
				    Toast.makeText(
					    getApplicationContext(),
					    "Please check your wi-fi connection",
					    Toast.LENGTH_SHORT).show();
				}
			    } else if ("continue".equals(examActionType)) {
				{
				    try {
					vegaConfig.setValue(
						VegaConstants.HISTORY_EXAM_ID,
						examsArrayList.get(position)
						.getExamId());
				    } catch (final ColumnDoesNoteExistsException e) {
					Logger.warn(tag,
						"Invalid attribute 'EXAM_ID':"
							+ e);
					ToastMessageForExceptions(R.string.COLUMN_DOES_NOT_EXIST_NOTICEBOARDACTIVITY);
					backToDashboard();
				    }
				    final Intent intent = new Intent();
				    intent.putExtra("exam_id", exam.getExamId());
				    intent.setClass(v.getContext(),
					    QuestionsListActivity.class);
				    intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				    startActivity(intent);

				    finish();
				}
			    } else if ("start".equals(examActionType)) {
				Toast.makeText(activityContext,
					R.string.Exam_Started, 1000).show();
				try {
				    vegaConfig.setValue(
					    VegaConstants.HISTORY_EXAM_ID,
					    examsArrayList.get(position)
					    .getExamId() + "");
				    /*
				     * String comaSepAllowedBooks =
				     * vegaConfig.getValue
				     * (VegaConstants.ALLOWED_BOOKS);
				     * 
				     * for (int allowedBookId :
				     * examsArrayList.get
				     * (position).getAllowedBookIds()) {
				     * if(!comaSepAllowedBooks.equals("")){
				     * comaSepAllowedBooks = ","; }
				     * 
				     * comaSepAllowedBooks += allowedBookId; }
				     * 
				     * vegaConfig.setValue(VegaConstants.
				     * ALLOWED_BOOKS, comaSepAllowedBooks);
				     */
				} catch (final ColumnDoesNoteExistsException e) {
				    Logger.warn(tag,
					    "Invalid attribute 'EXAM_ID':" + e);
				    ToastMessageForExceptions(R.string.COLUMN_DOES_NOT_EXIST_NOTICEBOARDACTIVITY);
				    backToDashboard();
				}
				// Take to Question Activity.. re-load exam
				// object from the downloaded question paper
				{
				    final Intent intent = new Intent();
				    intent.putExtra(VegaConstants.EXAM_ID,
					    exam.getExamId());
				    intent.putExtra("enteredTime",
					    System.currentTimeMillis());
				    intent.setClass(v.getContext(),
					    QuestionsListActivity.class);
				    intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				    startActivity(intent);
				    finish();
				}
			    } else if ("upload".equals(examActionType)) {
				if (wfManager.isWifiEnabled()) {
				    {
					// Show loading animation
					loadingDialog.setTitle("Please Wait..");
					loadingDialog
					.setMessage("Uploading the answer paper to the server");
					loadingDialog.show();

					final ArrayList<RequestParameter> postParams = new ArrayList<RequestParameter>();

					final String filePath = appData
						.getExamFilesPath(exam
							.getExamId())
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
					// */

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
								    appData.getExamSubmittedAnswersFileName(exam
									    .getExamId()));
							    final File uploadedFile = new File(
								    appData.getExamUploadedAnswersFileName(exam
									    .getExamId()));

							    Logger.warn(
								    "File is Existed---->",
								    "true"
									    + appData
									    .getExamUploadedAnswersFileName(exam
										    .getExamId()));
							    submittedFile
							    .renameTo(uploadedFile);

							    runOnUiThread(new Runnable() {
								@Override
								public void run() {
								    loadingDialog
								    .hide();
								    if (exam.isRetakeable()) {
									retake.setVisibility(View.VISIBLE);
								    }
								    action.setVisibility(View.GONE);
								    examStatus
								    .setVisibility(View.VISIBLE);
								    examStatus.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
								    examStatus
								    .setText("Results yet to release");
								    examStatus
								    .setTextColor(Color
									    .parseColor("#000000"));
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
								    action.setText("Retry Upload");
								    action.setVisibility(View.VISIBLE);
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

				    }
				} else {
				    Toast.makeText(
					    getApplicationContext(),
					    "Please check your wi-fi connection",
					    Toast.LENGTH_SHORT).show();
				}
			    } else if ("results".equals(examActionType)) {
				Toast.makeText(activityContext,
					R.string.see_Results, 1000).show();

				// TODO
				{
				    // Take to Results activity with intent
				    // parameter as result id attached with
				    // current exam instance
				}
			    }
			}
		    });
		}
	    }
	    return v;
	}
    }

    /**
     * Gets the exams list.
     *
     * @return the exams list
     */
    private void getExamsList() {

	// if(!ApplicationData.isFileExists(appData.getExamsListFile())){

	// downloadExamsList();

	// return;
	// }
	try{
	    final String content = ApplicationData.readFile(appData
		    .getExamsListFile());
	    Logger.warn(tag, "Exams list json is:" + content);
	    examsArrayList = JSONParser.getExamsList(ApplicationData
		    .readFile(appData.getExamsListFile()));			
	} catch (final Exception e) {
	    Logger.error(tag, e);
	    ToastMessageForExceptions(R.string.JSON_PARSING_EXCEPTION);
	    backToDashboard();
	}
	if (examsArrayList.size() == 0) {
	    emptystatus.bringToFront();
	    emptystatus.setText(R.string.empty_status);
	    examsList.setVisibility(View.GONE);
	    ll.setVisibility(View.GONE);
	} else {
	    emptystatus.setVisibility(View.GONE);
	    examsList.bringToFront();
	}

	final ExamslistArrayAdapter examAdapter = new ExamslistArrayAdapter(
		activityContext, R.layout.examlist_row, examsArrayList);

	examAdapter.sort(new Comparator<ExamDetails>() {

	    @Override
	    public int compare(ExamDetails lhs, ExamDetails rhs) {
		final Date startDate = new Date(lhs.getStartDate());
		final Date endDate = new Date(rhs.getStartDate());
		return startDate.compareTo(endDate);
	    }
	});

	examsList.setAdapter(examAdapter);
	examAdapter.notifyDataSetChanged();
	/*
	 * examsList.setAdapter(new ExamslistArrayAdapter(activityContext,
	 * R.layout.examlist_row, examsArrayList));
	 */
    }

    /**
     * Download exams list.
     */
    public void downloadExamsList() {

	if (appData == null | appData.getUser() == null
		| appData.getUser().getUserType() == null) {

	} else {
	    loadingDialog.setTitle("Please wait..");
	    loadingDialog.setMessage("Fetching/Refreshing exams list");
	    loadingDialog.show();

	    if (appData.getUser().getUserType()
		    .equalsIgnoreCase(RoleType.STUDENT.name())) {
		Logger.info(tag, "EXAMLISTACTIVITY-- USER TYPE IS "
			+ appData.getUser().getUserType());
		requestToServer(ServerRequests.EXAMS_LIST, appData.getUserId());
	    } else if (appData.getUser().getUserType()
		    .equalsIgnoreCase(RoleType.SUBJECTHEAD.name())
		    || appData.getUser().getUserType()
		    .equalsIgnoreCase(RoleType.PRINCIPLE.name())
		    || appData.getUser().getUserType()
		    .equalsIgnoreCase(RoleType.HOMEROOMTEACHER.name())) {
		Logger.info(tag, "EXAMLISTACTIVITY-- USER TYPE IS "
			+ appData.getUser().getUserType());
		requestToServer(ServerRequests.EXAMS_LIST_TEACHER,
			appData.getUserId());
	    }
	}
    }

    /**
     * Request to server.
     *
     * @param userType the user type
     * @param StudentId the student id
     */
    private void requestToServer(String userType, String StudentId) {
	final ServerRequests serverRequests = new ServerRequests(activityContext);
	final PostRequestHandler postRequest = new PostRequestHandler(
		serverRequests.getRequestURL(userType, StudentId), null,
		new DownloadCallback() {
		    @Override
		    public void onFailure(final String failureMessage) {
			Logger.error(tag, "on failure - failed to downlaod");
			runOnUiThread(new Runnable() {

			    @Override
			    public void run() {

				loadingDialog.hide();
				if (ApplicationData.isFileExists(appData
					.getExamsListFile())) {
				    getExamsList();
				} else {
				    if (wfManager.isWifiEnabled()) {
					Toast.makeText(
						activityContext,
						R.string.Unable_to_reach_pearl_server,
						20000).show();
				    } else
					Toast.makeText(
						activityContext,
						R.string.network_connection_unreachable,
						20000).show();
				}
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

				if ("" == downloadedString
					|| "null"
					.equalsIgnoreCase(downloadedString)) {
				    Toast.makeText(
					    activityContext,
					    R.string.unable_to_fetch_the_details,
					    2000);
				} else {
				    examsArrayList = saveExamListJsonToFile(downloadedString);
				    if (examsArrayList != null) {
					if (examsArrayList.size() == 0) {
					    emptystatus.bringToFront();
					    emptystatus
					    .setText(R.string.empty_status);
					    examsList.setVisibility(View.GONE);
					    ll.setVisibility(View.GONE);
					} else {
					    emptystatus
					    .setVisibility(View.GONE);
					    examsList.bringToFront();
					    examsList
					    .setAdapter(new ExamslistArrayAdapter(
						    activityContext,
						    R.layout.examlist_row,
						    examsArrayList));
					}

					getExamsList();
				    } else
					Logger.warn(tag, "exam details is null");
				}
				loadingDialog.hide();
			    }
			});
		    }
		});
	postRequest.request();
    }

    /**
     * Save exam json to file.
     *
     * @param examResponseJsonData the exam response json data
     * @param examDetails the exam details
     * @return true, if successful
     */
    public boolean saveExamJsonToFile(String examResponseJsonData,
	    ExamDetails examDetails) {
	Logger.warn(tag, "in saveExamJsonToFile()");
	final ObjectMapper mapper = new ObjectMapper();
	final ObjectReader reader = mapper.reader(ExamResponse.class);
	ExamResponse examResponse = null;
	Exam tempExam = null;
	try {
	    examResponse = reader.readValue(examResponseJsonData);
	} catch (final JsonProcessingException e1) {
	    ToastMessageForExceptions(R.string.JSON_PROCESSING_EXCEPTION);
	    e1.printStackTrace();
	    backToDashboard();

	} catch (final IOException e1) {
	    ToastMessageForExceptions(R.string.IO_EXCEPTION);
	    e1.printStackTrace();
	    backToDashboard();
	}

	try {
	    Logger.warn(tag, "start  ---> get exam");
	    tempExam = examResponse.getExam();
	    Logger.warn(tag, "end  ---> get exam" + tempExam);
	} catch (final Exception e) {
	    ToastMessageForExceptions(R.string.EXAM_RESPONSE_EXCEPTION);
	    e.printStackTrace();
	    backToDashboard();
	}

	final ObjectMapper omp = new ObjectMapper();
	String examJson = null;
	try {
	    Logger.warn(tag, "start  ---> write to json" + tempExam);
	    examJson = omp.writeValueAsString(tempExam);
	    Logger.warn(tag, "" + "" + tempExam);
	} catch (final JsonGenerationException e) {
	    Logger.error(tag, e);
	    ToastMessageForExceptions(R.string.JSON_GENERATION_EXCEPTION);
	    backToDashboard();

	} catch (final JsonMappingException e) {
	    Logger.error(tag, e);
	    ToastMessageForExceptions(R.string.JSON_MAPPING_EXCEPTION);
	    backToDashboard();
	} catch (final IOException e) {
	    Logger.error(tag, e);
	    ToastMessageForExceptions(R.string.IO_EXCEPTION);
	    backToDashboard();
	}

	if (tempExam != null) {
	    Logger.info(tag, "ExamId =========>" + tempExam.getExamDetails());
	    Logger.info(tag, "ExamId Different Settings============>"
		    + tempExam.getExamDetails().getExamId());
	    Logger.error(
		    tag,
		    "exam json file path:"
			    + appData.getExamFileName(tempExam.getExamDetails()
				    .getExamId()));
	}

	ApplicationData.writeToFile(examJson,
		appData.getExamFileName(examDetails.getExamId()));

	return true;
    }

    /**
     * Save exam list json to file.
     *
     * @param examResponseJsonData the exam response json data
     * @return the array list
     */
    public ArrayList<ExamDetails> saveExamListJsonToFile(
	    String examResponseJsonData) {

	ArrayList<ExamDetails> examList = null;
	try {
	    examList = ExamListParser.getExamFile(examResponseJsonData,
		    appData.getExamsListFile());
	} catch (final JsonParseException e) {
	    e.printStackTrace();
	} catch (final JsonMappingException e) {
	    e.printStackTrace();
	} catch (final IOException e) {
	    e.printStackTrace();
	}
	return examList;
    }

    /**
     * Checks if is exam paper downloaded.
     *
     * @param examId the exam id
     * @return true, if is exam paper downloaded
     */
    public boolean isExamPaperDownloaded(String examId) {
	return ApplicationData.isFileExists(appData.getExamFileName(examId));
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "Showing Student Exams List";
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
     */
    @Override
    public void onNetworkAvailable() {
	Logger.warn(tag, "In onNetworkAvailable()");
	Logger.warn(tag, "network available so enable the refresh button");
	if (!ApplicationData.isFileExists(appData.getExamsListFile())) {
	    runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    Logger.warn(tag, "exam doesnot exists in local");
		    // Toast.makeText(activityContext,
		    // R.string.refresh_button_above_to_downlaod_Exam_List,
		    // toastDisplayTime).show();
		}
	    });
	    onclick = true;
	    setAlpha = false;

	    if (!setAlpha) {
		runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
			refreshExamList.setAlpha(225);
		    }
		});
	    }

	} else {
	    Logger.warn(tag, "file exists in local");
	    onclick = true;
	    runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    // getExamsList();
		    refreshExamList.setAlpha(225);
		}
	    });
	}
    }

    /**
     * Bind events.
     */
    private void bindEvents() {
	refreshExamList.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (onclick) {
		    // new asyncTaskUpdateProgress().execute();
		    // refreshExamList.setVisibility(View.GONE);
		    runOnUiThread(new Runnable() {

			@Override
			public void run() {
			    // getExamsList();
			    downloadExamsList();
			}
		    });
		} else {
		    runOnUiThread(new Runnable() {

			@Override
			public void run() {
			    Toast.makeText(activityContext,
				    R.string.network_connection_unreachable,
				    toastDisplayTime).show();
			}
		    });
		}
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
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onResume()
     */
    @Override
    public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	if (!appData.isLoginStatus()) {
	    final Intent loginIntent = new Intent(this, LoginActivity.class);
	    startActivity(loginIntent);
	    finish();
	}
    }

    /**
     * The Class asyncTaskUpdateProgress.
     */
    public class asyncTaskUpdateProgress extends AsyncTask<Void, Integer, Void> {

	/** The progress. */
	int progress;

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Void result) {
	    // TODO Auto-generated method stub
	    refreshExamList.setVisibility(View.VISIBLE);
	    refreshExamList.setClickable(true);
	    // progressBar.setVisibility(View.GONE);
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
	    // TODO Auto-generated method stub
	    progress = 0;
	    // progressBar.setVisibility(View.VISIBLE);

	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
	    // TODO Auto-generated method stub
	    // progressBar.setProgress(values[0]);
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected Void doInBackground(Void... arg0) {
	    // TODO Auto-generated method stub
	    /*
	     * while (progress < 100) { progress++; publishProgress(progress);
	     * SystemClock.sleep(100); }
	     */
	    return null;
	}
    }

    /**
     * The Class ViewHolder.
     */
    class ViewHolder {
	
	/** The item. */
	TextView item;
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
     */
    @Override
    public void onNetworkUnAvailable() {
	// TODO Auto-generated method stub

	Logger.warn(tag, "In onNetworkUnAvailable()");
	Logger.warn(tag, "network not available so disable the refresh button");
	onclick = false;
	setAlpha = true;
	if (setAlpha) {
	    runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    refreshExamList.setAlpha(70);
		}
	    });
	}
	/*
	 * if(ApplicationData.isFileExists(appData.getExamsListFile())) {
	 * 
	 * String content = ""; try { content =
	 * ApplicationData.readFile(appData.getExamsListFile()); } catch
	 * (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } Logger.warn("exam------>", content); try {
	 * examsArrayList = JSONParser.getExamsList(content); } catch
	 * (JsonProcessingException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { examsList.setAdapter(new
	 * ExamslistArrayAdapter( activityContext, R.layout.examlist_row,
	 * examsArrayList)); } }); } else { runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * Toast.makeText(activityContext,
	 * "please check your network connections", Toast.LENGTH_SHORT).show();
	 * } });
	 * 
	 * }
	 */

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    protected void onNewIntent(Intent intent) {
	// TODO Auto-generated method stub
	super.onNewIntent(intent);
	Logger.warn(tag, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    }

}