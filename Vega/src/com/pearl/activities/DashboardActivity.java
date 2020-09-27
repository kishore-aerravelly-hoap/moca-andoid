package com.pearl.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.BaseAdapter.CustomBaseAdapter;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.attendance.Attendance;
import com.pearl.attendance.AttendanceType;
import com.pearl.baseresponse.login.AndroidUser;
import com.pearl.books.Book;
import com.pearl.database.config.NoteBookConfig;
import com.pearl.examination.ExamDetails;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.downloadmanager.utils.DownloadType;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.JSONParser;
import com.pearl.parsers.json.AttendanceParser;
import com.pearl.parsers.json.ExamListParser;
import com.pearl.parsers.json.NoticeBoardParser;
import com.pearl.ui.models.BaseResponse;
import com.pearl.ui.models.BaseResponse1;
import com.pearl.ui.models.Notice;
import com.pearl.ui.models.NoticeBoardResponse;
import com.pearl.ui.models.PearlConfigKeyValues;
import com.pearl.ui.models.PeriodConfig;
import com.pearl.ui.models.RoleType;
import com.pearl.ui.models.StatusType;
import com.pearl.util.AttendanceDatesComparision;

// TODO: Auto-generated Javadoc
/**
 * The Class DashboardActivity.
 */
public class DashboardActivity extends BaseActivity implements OnClickListener {
    /*
     * (non-Javadoc)
     * 
     * @see com.pearl.activities.BaseActivity#onDestroy()
     */

    /** Called when the activity is first created. */

    
    /** The Logged student name. */
    TextView LoggedStudentName;
    
    /** The log out. */
    ImageView dashboardUpdates, logOut;
    
    /** The dasshboard updates progress. */
    ProgressBar dasshboardUpdatesProgress;
    
    /** The notice. */
    private List<Notice> notice;
    
    /** The notice adapter. */
    CustomBaseAdapter noticeAdapter;
    
    /** The Analytics adapter. */
    CustomBaseAdapter examAdapter,AnalyticsAdapter;
    
    /** The ereader. */
    CustomBaseAdapter ereader;
    
    /** The vega config. */
    VegaConfiguration vegaConfig;
    
    /** The no_internet. */
    ImageView no_internet;
    
    
    /** The exams array list. */
    private ArrayList<ExamDetails> examsArrayList;
    
    /** The set alpha. */
    boolean setAlpha = false;
    
    /** The subject. */
    String attendanceFrequency = null, subject;
    
    /** The period configresponse string. */
    String lastAttendanceTakenDate, attendanceStatus, periodStartTime,
	    periodEndTime = "13:30:00", periodConfigresponseString;
    
    /** The period duration. */
    int periodDuration;
    
    /** The handler. */
    private Handler handler;
    
    
    /** The attendance. */
    Attendance attendance = null;
    
    /** The Constant NOTICEBOARD. */
    private static final String NOTICEBOARD = "NOTICEBOARD";
    
    /** The Constant AVAILABLEBOOKSLIST. */
    private static final String AVAILABLEBOOKSLIST = "AVAILABLEBOOKSLIST";
    
    /** The Constant DOWNLOADED_BOOKSLIST. */
    private static final String DOWNLOADED_BOOKSLIST = "DOWNLOADED_BOOKSLIST";
    
    /** The Constant EXAMS_LIST. */
    private static final String EXAMS_LIST = "EXAMS_LIST";
    
    /** The teacher notice data. */
    private String teacherNoticeData;
    
    /** The server requests. */
    ServerRequests serverRequests;
    
    /** Grid View  */
    GridView grid;
    
    /** The evening session time. */
    String morningSessionTime = null, eveningSessionTime = null;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.dashboard);
	serverRequests = new ServerRequests(this);
	vegaConfig = new VegaConfiguration(this);
	handler = new Handler();
	logOut = (ImageView) findViewById(R.id.logout_dash);
	logOut.setOnClickListener(this);
	dashboardUpdates = (ImageView) findViewById(R.id.dashboard_updates);
	dasshboardUpdatesProgress = (ProgressBar) findViewById(R.id.dasshboard_updates_progress);
	no_internet = (ImageView) findViewById(R.id.No_internet);
	grid = (GridView)findViewById(R.id.dashboard_grid);
	grid.setAdapter(new MainGridImageAdapter(this.getApplicationContext()));
	
	onNetworkAvailable();
	
	grid.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(position==0){
				switch (checkUserRoletype()) {
				case 1: {
				    Log.i(tag, "I am--------------- teacher"
					    + appData.getUser().getUserType());
				    final Intent quiz_boardTeacher = new Intent(getApplicationContext(),TeacherNoticeBoardFragmentActivity.class);
				    startActivity(quiz_boardTeacher);
				    break;
				}
				case 2: {
				    Log.i(tag, "I am--------------- Student"
					    + appData.getUser().getUserType());
				    final Intent quiz_boardStudent = new Intent(getApplicationContext(), NoticeBoardActivity.class);
				    startActivity(quiz_boardStudent);
				    break;
				}
				default:
				    break;
				}
			}else if(position==1){
				if(appData.getUser().isClassMonitor() || appData
						.getUser().getUserType().equalsIgnoreCase(
							RoleType.HOMEROOMTEACHER.name())){
					Intent attendece_board = new Intent(getApplicationContext(),AttendanceActivity.class);
					notificationsSettings();
					attendece_board.putExtra(VegaConstants.ATTENDANCE_TYPE,	attendanceFrequency);
					attendece_board.putExtra(VegaConstants.ATTENDANCE_SUBJECT,subject);
					try {
						vegaConfig.setValue(VegaConstants.ATTENDANCE_COUNT, 0);
					} catch (ColumnDoesNoteExistsException e) {
						e.printStackTrace();
					}
					startActivity(attendece_board);
					
				}else{
					Toast.makeText(getApplicationContext(),	"You are not eligible to take attendance" ,Toast.LENGTH_LONG).show();
				}
				
			}else if(position==2){
				
				Toast.makeText(getApplicationContext(),	"Still in development" ,Toast.LENGTH_LONG).show();
				/*final Intent chat_board = new Intent(getApplicationContext(), ChatActivity.class);
				notificationsSettings();
				chat_board.putExtra(VegaConstants.USER_ID, appData.getUserId());
				try {
					vegaConfig.setValue(VegaConstants.MESSAGE_COUNT, 0);
				} catch (ColumnDoesNoteExistsException e) {
					e.printStackTrace();
				}
				startActivity(chat_board);*/
				
				
			}else if(position==3){
				switch (checkUserRoletype()) {
				case 1:{
					Toast.makeText(getApplicationContext(), "Not eligible to take notes",Toast.LENGTH_SHORT).show();
				    break;
				}
				case 2:
				{
				    Log.i(getClass().getSimpleName(), "I am--------------- Student"+appData.getUser().getUserType());
				    final Intent noteboardB = new Intent(getApplicationContext(), NoteBookActivity.class);
				    startActivity(noteboardB);
				    finish();
				    break;
				}
				default:
				    break;
				}
				
			}else if(position==4){
				switch (checkUserRoletype()) {
				case 1: {
				    Log.i(tag, "I am--------------- teacher"
					    + appData.getUser().getUserType());
				    final Intent quiz_boardTeacher = new Intent(getApplicationContext(),
					    TeacherExamsActivity.class);
				    startActivity(quiz_boardTeacher);
				    break;
				}
				case 2: {
				    Log.i(tag, "I am--------------- Student"
					    + appData.getUser().getUserType());
				    final Intent quiz_boardStudent = new Intent(getApplicationContext(),
					    ExamListActivity.class);
				    try {
						vegaConfig.setValue(VegaConstants.EXAM_COUNT, 0);
					} catch (ColumnDoesNoteExistsException e) {
						e.printStackTrace();
					}
				    startActivity(quiz_boardStudent);
				    break;
				}
				default:
				    break;
				}
				
			}else if(position==5){
				final Intent ereader_board = new Intent(getApplicationContext(),
						ShelfActivity.class);
					notificationsSettings();
					try {
						vegaConfig.setValue(VegaConstants.BOOKS_COUNT, 0);
					} catch (ColumnDoesNoteExistsException e) {
						e.printStackTrace();
					}
					startActivity(ereader_board);
				
			}else if(position==6){
				final Intent feedback = new Intent(getApplicationContext(), FeedbackActivity.class);
				startActivity(feedback);
				
			}else if(position==7){
				final Intent faqs = new Intent(getApplicationContext(), FAQActivity.class);
				startActivity(faqs);
			}else if(position==8){
				
				switch (checkUserRoletype()) {
			    case 1:{
				Logger.warn("Action bar", "analytics selected");
				Log.i("Action bar", "I am--------------- teacher"+appData.getUser().getUserType());
				Intent intent = new Intent(getApplicationContext(), PearlAnalyticsList.class);
				startActivity(intent);
				finish();
				break;
			    }
				case 2:
			    {
				Log.i("Action bar", "I am--------------- Student"+appData.getUser().getUserType());
				
					   Toast.makeText(getApplicationContext(), "You cannot access this",Toast.LENGTH_SHORT).show();
				break;
			    }
			    default:
				break;
			    }
			}
			
		}
	});
	
	//downloadAttendanceFromServer();
	dashboardUpdates.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		new asyncTaskUpdateProgress().execute();
		dashboardUpdates.setVisibility(View.INVISIBLE);
		dashboardUpdates.setClickable(false);
	
		//checkAttendanceStatus();
		switch (checkUserRoletype()) {
		case 1:
		
		    handler.post(new Runnable() {

			@Override
			public void run() {
			   /*noticeAdapter=new CustomBaseAdapter(
				    DashboardActivity.this,
				    downloadTeacherNotices(),
				    VegaConstants.DASHBOARD_NOTICE);
			   
			    list_notice.setAdapter(noticeAdapter);
			    noticeAdapter.notifyDataSetChanged();*/ 
			    if(appData
				.getUser()
				.getUserType()
				.equalsIgnoreCase(
					RoleType.HOMEROOMTEACHER.name()))
				downloadAttendanceFromServer();

			}
			/*	public void run() {
		    AnalyticsAdapter=new CustomBaseAdapter(
			    DashboardActivity.this,
			   "See  your analytics here",
			    VegaConstants.ANALYTICS);
		    starcare_logo.setClickable(true);
		    Analytics.setAdapter(AnalyticsAdapter);
			
			
		   noticeAdapter=new CustomBaseAdapter(
			    DashboardActivity.this,
			  "Read/Write Notices",
			    VegaConstants.DASHBOARD_NOTICE);
		   
		    list_notice.setAdapter(noticeAdapter);
		    noticeAdapter.notifyDataSetChanged(); 

		}*/
		    });

		    break;
		case 2:
		    handler.post(new Runnable() {

			@Override
			public void run() {
			  /*  list_notice.setAdapter(new CustomBaseAdapter(
				    DashboardActivity.this,
				    checkForNoticeBoardFile(),
				    VegaConstants.DASHBOARD_NOTICE));
			    examAdapter = new CustomBaseAdapter(
				    activityContext, checkForExamFile(),
				    VegaConstants.DASHBOARD_QUIZZARD);
			  
			    examAdapter.notifyDataSetChanged();
			    list_quizzard.setAdapter(examAdapter);*/

			}
		    });
		    break;
		default:
		    break;
		}
	
	    }
	});



	//checkAttendanceStatus();
	setUserName();

	
	dashboardUpdates.post(new Runnable() {
		
		@Override
		public void run() {
		dashboardUpdates.performClick();
			
		}
	});

    }
    
    
    
    public class MainGridImageAdapter extends BaseAdapter {
	    private Context mContext;

	    private class ViewHolder {
    		TextView imageTitle;
    		ImageView imageView;
    	}
	    
	    public MainGridImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return mThumbIds.length;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	View row = convertView;
	    	ViewHolder holder = null;

	    	  if (row == null) {
	    		  LayoutInflater inflater = (LayoutInflater) mContext
	    			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    			row = inflater.inflate(R.layout.shelfgrid_item,parent,false);

	    	   holder = new ViewHolder();
	    	   holder.imageTitle = (TextView) row.findViewById(R.id.textname);
	    	   holder.imageView = (ImageView) row.findViewById(R.id.icon_image);
	    	   row.setTag(holder);
	    	  } else {
	    	   holder = (ViewHolder) row.getTag();
	    	  }

	    	  holder.imageTitle.setText(mNames[position]);
	    	  holder.imageView.setImageResource(mThumbIds[position]);
	    	  return row;

	    }

	    // references to our images
	    private Integer[] mThumbIds = {
	            R.drawable.dash_noticeboard_redbg, R.drawable.dash_schedule_pinkbg,
	            R.drawable.dash_chat_orangebg, R.drawable.dash_notebook_lightgreenbg, R.drawable.dash_quizzard_violetbg,
	            R.drawable.dash_ereader_bluebg, R.drawable.dash_feedback_greenbg, R.drawable.dash_faq_brownbg,
	            R.drawable.dash_starbluebg
	    };
	    
	 // references to our images Names
	    private Integer[] mNames = {
	            R.string.nticeboard_name, R.string.Attendance_name,
	            R.string.Chat_name, R.string.NoteBook_name, R.string.quizzard,
	            R.string.eReader_name, R.string.feedback_name, R.string.Faq_name,
	            R.string.Analytics_name
	    };

    }
    /**
     * Check attendance status.
     */
    private void checkAttendanceStatus() {
	if(ApplicationData.isFileExists(appData.getAttendanceFile())){
		
		try {
			attendance = AttendanceParser.getAttendanceFile(appData.getAttendanceFile());
		} catch (JsonParseException e1) {
			Logger.error(tag, e1);
		} catch (JsonMappingException e1) {
			Logger.error(tag, e1);
		} catch (IOException e1) {
			Logger.error(tag, e1);
		}
		
		downloadLastAttendanceTakenDetails(new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				lastAttendanceTakenDate = AttendanceParser.getLastAttendanceTakenDetails(downloadedString);
				getAttendanceDetails(attendance, lastAttendanceTakenDate);
				if(!attendanceFrequency.equalsIgnoreCase(AttendanceType.SUBMIT_PERIODICALLY.getAttendanceType())){
					attendanceStatus = AttendanceDatesComparision.compareDates(lastAttendanceTakenDate,
							morningSessionTime, eveningSessionTime, Integer.parseInt(userTimeFormat),
							attendanceFrequency, periodDuration);
					Logger.warn(tag, "status is:------------"+attendanceStatus);
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							//setAttendanceStatus(attendanceStatus);
							
						}
					});
				}
				
			}
			
			@Override
			public void onProgressUpdate(int progressPercentage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(String failureMessage) {
				// TODO Auto-generated method stub
				
			}
		});
		
	
	}else{
		if(appData.getUser().isClassMonitor() || appData
				.getUser()
				.getUserType()
				.equalsIgnoreCase(
					RoleType.HOMEROOMTEACHER.name())){
		//	handler.removeCallbacks(updateAttendaceStatus);
			//setAttendanceStatus("Take Attendance");
			downloadAttendanceFromServer();
			}else{
				//handler.removeCallbacks(updateAttendaceStatus);
				//setAttendanceStatus("You are not eligible to take attendance");
			}
	}
}


/**
 * Gets the attendance details.
 *
 * @param attendance the attendance
 * @param lastAttendanceTakenDate the last attendance taken date
 * @return the attendance details
 */
    private void getAttendanceDetails(Attendance attendance, final String lastAttendanceTakenDate){
    	Map<String, String> map = new HashMap<String, String>();
    	List<PearlConfigKeyValues> configValues = attendance.getConfigValues();
    	for (PearlConfigKeyValues key : configValues) {
    		map.put(key.getConfigkey(), key.getConfigvalue());
    	}
    	Logger.warn(tag, "-- Map is:" + map);
    	if (map.containsKey(AttendanceType.ATTENDANCE_TYPE.getAttendanceType())) {
    	/*if (map.containsKey(AttendanceType.ATTENDANCE_TYPE.getAttendanceType())
    			&& map.containsKey(AttendanceType.MORNING_SESSION_TIME_KEY.getAttendanceType())
    			&& map.containsKey(AttendanceType.AFTERNOON_SESSION_TIME_KEY.getAttendanceType())
    			&& map.containsKey(AttendanceType.PERIOD_DURATION_KEY.getAttendanceType())
    			&& map.containsKey(AttendanceType.LAST_ATTENDACE_TAKEN_DATE_KEY.getAttendanceType())) {*/
    		
    		attendanceFrequency = map.get(AttendanceType.ATTENDANCE_TYPE.getAttendanceType());
    		/*morningSessionTime = map.get(AttendanceType.MORNING_SESSION_TIME_KEY.getAttendanceType());
    		eveningSessionTime = map.get(AttendanceType.AFTERNOON_SESSION_TIME_KEY.getAttendanceType());
    		periodDuration = Integer.parseInt(map.get(AttendanceType.PERIOD_DURATION_KEY.getAttendanceType()));*/
    		if (map.get(AttendanceType.ATTENDANCE_TYPE.getAttendanceType()).
    				equalsIgnoreCase(AttendanceType.SUBMIT_PERIODICALLY.getAttendanceType())) {
    			getSubjectAndPeriodDurationFromServer(new DownloadCallback() {

    				@Override
    				public void onSuccess(String downloadedString) {
    					
    					Logger.warn(tag, "Downloaded string in onsuccess is:"
    							+ downloadedString);
    					PeriodConfig periodConfig = AttendanceParser.getPeriodConfiguration(downloadedString);
    					subject = periodConfig.getSubjectName();
    					periodStartTime = periodConfig.getStartTime();
    					periodEndTime = periodConfig.getEndTime();
    					Logger.warn(tag, "subject from json is:" + subject);
    					Logger.warn(tag, "Period start time is:"+ periodStartTime);
    					Logger.warn(tag, "Period end time is:" + periodEndTime);
    					periodDuration = AttendanceDatesComparision
    							.calculateTimeDifference(periodStartTime,
    									periodEndTime);				
    					attendanceStatus = AttendanceDatesComparision.compareDates(lastAttendanceTakenDate,
    							morningSessionTime, eveningSessionTime, Integer.parseInt(userTimeFormat),
    							attendanceFrequency, periodDuration);
    					Logger.warn(tag, "status is:------------"+attendanceStatus);
    					handler.post(new Runnable() {
    						
    						@Override
    						public void run() {
    							//setAttendanceStatus(attendanceStatus);
    						}
    					});
    					Logger.warn(tag, "period duration from server is:"
    							+ periodDuration);
    				}

    				@Override
    				public void onProgressUpdate(int progressPercentage) {

    				}

    				@Override
    				public void onFailure(String failureMessage) {

    				}
    			});
    		}
    	}else
    		Logger.error(tag, "NOOOOOOOOOOOOOOOOOO");
    	Logger.warn(tag,
    			"Attendance frequency in checkAttendanceFrequency() is:"
    					+ attendanceFrequency);

    }


    

    /**
     * Check for notice board file.
     *
     * @return the string
     */
    public String checkForNoticeBoardFile() {
	String data;
	if (ApplicationData.isFileExists(appData.getNoticeBoardFile())) {
	    NoticeBoardResponse noticeBoardResponse = new NoticeBoardResponse();
	    try {
		downloadNoticeBoardInfo();
		noticeBoardResponse = NoticeBoardParser
			.getNoticeBoardContent(new File(appData
				.getNoticeBoardFile()));
	    } catch (final Exception e) {
		final AndroidUser user = new AndroidUser();
		String userId = "";
		try {
		    userId = vegaConfig
			    .getValue(VegaConstants.HISTORY_LOGGED_IN_USER_ID);
		} catch (final InvalidConfigAttributeException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		user.setId(userId);
		appData.setUser(user);
	    }

	    Logger.info("DASHBOARD", "DASHBOARD notice content is-----"
		    + noticeBoardResponse);
	    if(noticeBoardResponse != null && noticeBoardResponse.getData() != null){
	    	if(noticeBoardResponse.getData().getUserNotices() != null){
		    	if (noticeBoardResponse.getData().getUserNotices().size() != 0) {
		    		Logger.info("DASHBOARD", "DASHBOARD notice  respose ==="
		    			+ noticeBoardResponse.getData().getUserNotices().get(0)
		    				.getTitle());
		    		data = noticeBoardResponse.getData().getUserNotices().get(0)
		    			.getTitle();
		    	    } else {
		    		data = "You don't have any notice.";
		    	    }
		    }
	    	else 
	    		data = "You don't have any notice.";
	    }
	    else {
    		data = "You don't have any notice.";
	    }

	} else {
	    data = "No new Notices for you";

	}
	return data;
    }

    /**
     * Check for exam file.
     *
     * @return the string
     */
    public String checkForExamFile() {
	String data = "";

	if (appData.getUser() != null
		&& appData.getUser().getUserType() != null
		&& appData.getUser().getUserType()
			.equalsIgnoreCase(RoleType.STUDENT.name())) {
	    Logger.info(tag, "DASDHBOARDACTIVITY-- USER TYPE IS "
		    + appData.getUser().getUserType());
	    if (ApplicationData.isFileExists(appData.getExamsListFile())) {

		try {
		    Logger.info(tag, "FLOW IS GOING OUT OF DASHBOARD");
		    requestToServer(ServerRequests.EXAMS_LIST, appData.getUserId());
		    final String content = ApplicationData.readFile(appData
			    .getExamsListFile());
		    examsArrayList = JSONParser.getExamsList(content);
		    Log.i(getClass().getSimpleName(),"<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+examsArrayList);
		    if (examsArrayList != null && examsArrayList.size() != 0) {
		    	Collections.sort(examsArrayList,new Comparator<ExamDetails>() {

				    @Override
				    public int compare(ExamDetails lhs,
					    ExamDetails rhs) {
					long startTimeLHS=lhs.getStartTime();
					long endTimeRHS=rhs.getStartTime();
					
					return String.valueOf(endTimeRHS).compareTo(String.valueOf(startTimeLHS));
				    }
				});
			data = "You have a new test "
				+ examsArrayList.get(0).getTitle() + " for "
				+ examsArrayList.get(0).getSubject();
		    } else
			data = "No tests scheduled for you";

		    Logger.info(tag, "data from examarray list is" + data);

		} catch (final JsonProcessingException e) {
		    Logger.error(tag, e);
		} catch (final IOException e) {
		    Logger.error(tag, e);
		}
	    } else {
		data = "No test scheduled for you";
	    }

	} else if (appData.getUser() != null
		&& appData.getUser().getUserType() != null
		&& (appData.getUser().getUserType()
			.equalsIgnoreCase(RoleType.SUBJECTHEAD.name())
			|| appData.getUser().getUserType()
				.equalsIgnoreCase(RoleType.PRINCIPLE.name())
			|| appData
				.getUser()
				.getUserType()
				.equalsIgnoreCase(
					RoleType.HOMEROOMTEACHER.name()) || appData
			.getUser().getUserType()
			.equalsIgnoreCase(RoleType.TEACHER.name()))) {
	    Logger.info(tag, "DASDHBOARDACTIVITY-- USER TYPE IS "
		    + appData.getUser().getUserType());
	    data = "Create/Approve/Reject/Publish/Evaluate Tests";
	}
	return data;
    }

    /*
     * public String checkForAttendanceFile() { String data;
     * if(ApplicationData.isFileExists(appData.getAttendanceFile())) {
     * Attendance attendanceResponse =
     * AttendanceParser.getAttendanceFile(appData.getAttendanceFile()); //data=
     * attendanceResponse.get
     * 
     * }else { data="You do not have any notices.. "; } return data; }
     */
    /**
     * Checkfor book.
     *
     * @return the string
     */
    public String checkforBook() {
	String fileName = "";
	try {
	    if (vegaConfig.getValue(VegaConstants.HISTORY_BOOK_ID) == null) {
		// TODO Strings .xml

		fileName = "You did'nt open any books";
	    } else {
		final int book_id = Integer.parseInt(vegaConfig
			.getValue(VegaConstants.HISTORY_BOOK_ID));
		Book book = null;
		try {
		    book = appData.getBook(book_id);
		} catch (final Exception e) {
		    downloadBookList();
		}
		if (book == null) {
		    fileName = "Download/Read Books";
		} else {
		    fileName = book.getFilename();
		    // Logger.info("SystemOutPut","output is"+fileName.substring(fileName.lastIndexOf("/"),fileName.lastIndexOf(".")));
		    fileName = "Page "
			    + vegaConfig
				    .getValue(VegaConstants.HISTORY_PAGE_NUMBER)
			    + " Of   "
			    + fileName.substring(
				    fileName.lastIndexOf("\\") + 1,
				    fileName.lastIndexOf(".")) + " book";
		    return fileName;
		}
	    }
	} catch (final InvalidConfigAttributeException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return fileName;
    }

    /**
     * Gets the recent saved notes.
     *
     * @return the recent saved notes
     */
    public String getRecentSavedNotes() {
	String text = "";
	NoteBookConfig noteBookConfig = new NoteBookConfig(this);

	String subject = noteBookConfig.fetchSubjectofRecentSavedNotes();
	String defaultNotes = noteBookConfig.fetchRecentSavedNotes();
	if (defaultNotes.length() > 15) {
	    text = defaultNotes.substring(0, 15) + "...." + " for " + subject;
	} else if (defaultNotes.length() == 0) {
	    text = "You do not have any notes to display";
	} else
	    text = defaultNotes + " for " + subject;

	return text;

    }

    /**
     * Check user roletype.
     *
     * @return the int
     */
    public int checkUserRoletype() {
	int Role = 0;

	if (appData.getUser() != null
		&& appData.getUser().getUserType() != null
		&& (appData.getUser().getUserType()
			.equalsIgnoreCase(RoleType.SUBJECTHEAD.name())
			|| appData.getUser().getUserType()
				.equalsIgnoreCase(RoleType.PRINCIPLE.name())
			|| appData
				.getUser()
				.getUserType()
				.equalsIgnoreCase(
					RoleType.HOMEROOMTEACHER.name()) || appData
			.getUser().getUserType()
			.equalsIgnoreCase(RoleType.TEACHER.name()))) {
	    Logger.info(tag,
		    "DASDHBOARDACTIVITY--checkUserRoletype  USER TYPE IS "
			    + appData.getUser().getUserType());
	    Role = 1;
	} else if (appData.getUser() != null
		&& appData.getUser().getUserType() != null
		&& appData.getUser().getUserType()
			.equalsIgnoreCase(RoleType.STUDENT.name())) {
	    Logger.info(tag,
		    "DASDHBOARDACTIVITY--checkUserRoletype  USER TYPE IS "
			    + appData.getUser().getUserType());
	    Role = 2;

	}
	return Role;
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	// TODO Auto-generated method stub
	try {
	    switch (v.getId()) {

	    /*case R.id.notice_dashboard_button:
		switch (checkUserRoletype()) {
		case 1: {
		    Log.i(tag, "I am--------------- teacher"
			    + appData.getUser().getUserType());
		    final Intent quiz_boardTeacher = new Intent(this,
			    TeacherNoticeBoardFragmentActivity.class);
		    startActivity(quiz_boardTeacher);
		    break;
		}
		case 2: {
		    Log.i(tag, "I am--------------- Student"
			    + appData.getUser().getUserType());
		    final Intent quiz_boardStudent = new Intent(this,
			    NoticeBoardActivity.class);
		    startActivity(quiz_boardStudent);
		    break;
		}
		default:
		    break;
		}
		break;
	    case R.id.attendance_dashboard_button:

		Intent attendece_board = new Intent(this,
			AttendanceActivity.class);
		notificationsSettings();
		attendece_board.putExtra(VegaConstants.ATTENDANCE_TYPE,
			attendanceFrequency);
		attendece_board.putExtra(VegaConstants.ATTENDANCE_SUBJECT,
			subject);
		vegaConfig.setValue(VegaConstants.ATTENDANCE_COUNT, 0);

		startActivity(attendece_board);
		break;

	    case R.id.chat_dashboard_button:f
		final Intent chat_board = new Intent(this, ChatActivity.class);
		notificationsSettings();
		chat_board.putExtra(VegaConstants.USER_ID, appData.getUserId());
		vegaConfig.setValue(VegaConstants.MESSAGE_COUNT, 0);
		startActivity(chat_board);

		break;
	    case R.id.notebook_dashboard_button:
		final Intent notebook_board = new Intent(this,
			NoteBookActivity.class);
		notificationsSettings();
		startActivity(notebook_board);

		break;
	    case R.id.quizzard_dashboard_button:
		switch (checkUserRoletype()) {
		case 1: {
		    Log.i(tag, "I am--------------- teacher"
			    + appData.getUser().getUserType());
		    final Intent quiz_boardTeacher = new Intent(this,
			    TeacherExamsActivity.class);
		    startActivity(quiz_boardTeacher);
		    break;
		}
		case 2: {
		    Log.i(tag, "I am--------------- Student"
			    + appData.getUser().getUserType());
		    final Intent quiz_boardStudent = new Intent(this,
			    ExamListActivity.class);
		    vegaConfig.setValue(VegaConstants.EXAM_COUNT, 0);
		    startActivity(quiz_boardStudent);
		    break;
		}
		default:
		    break;
		}
		break;
	    case R.id.ereader_dashboard_button:

		final Intent ereader_board = new Intent(this,
			ShelfActivity.class);
		notificationsSettings();
		vegaConfig.setValue(VegaConstants.BOOKS_COUNT, 0);
		startActivity(ereader_board);
		break;
	    case R.id.feedback_dashboard_button:
		final Intent feedback = new Intent(this, FeedbackActivity.class);
		startActivity(feedback);
		break;
	    case R.id.FAQ_dashboard_button:
		final Intent faqs = new Intent(this, FAQActivity.class);
		startActivity(faqs);
		break;
	    case R.id.relativeLayout2:
			final Intent feedbackLayout = new Intent(this, FeedbackActivity.class);
			startActivity(feedbackLayout);
			break;*/
	    case R.id.logout_dash:
		new AlertDialog.Builder(this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("Sign out")
			.setMessage("Are you sure you want to Sign out?")
			.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog,
					    int which) {
					final Intent signOut = new Intent(
						DashboardActivity.this,
						LoginActivity.class);
					signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(signOut);

					finish();
					try {
					    vegaConfig
						    .setValue(
							    VegaConstants.HISTORY_ACTIVITY,
							    "LoginActivity");
					    vegaConfig
						    .setValue(
							    VegaConstants.HISTORY_LOGGED_IN_USER_ID,
							    "0");
					} catch (final ColumnDoesNoteExistsException e) {
					    e.printStackTrace();
					} finally {
					    appData.setLoginStatus(false);
					    Logger.info(
						    "LOGINSTATUS",
						    "login status in actionBar is"
							    + appData
								    .isLoginStatus());
					}
				    }
				})
			.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog,
					    int which) {
					Logger.warn("Action bar", "no clicked");
				    }
				}).show();

	    }
	} catch (final Exception e) {
	    Logger.error(tag, "INTENT form login Exception");
	}

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	// TODO Auto-generated method stub
	return "DashboardActivity";
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
     */
    @Override
    public void onNetworkAvailable() {
	Logger.warn(tag, "Dashboard activity network check");

	runOnUiThread(new Runnable() {

	    @Override
	    public void run() {
		setAlpha = false;
		setAlphaForDashboard();
		no_internet.setVisibility(View.GONE);

		/*
		 * noticeAdapter = new CustomBaseAdapter(activityContext,
		 * checkForNoticeBoardFile(), VegaConstants.DASHBOARD_NOTICE);
		 * examAdapter = new CustomBaseAdapter(activityContext,
		 * checkForExamFile(), VegaConstants.DASHBOARD_QUIZZARD);
		 * 
		 * 
		 * ereader=new CustomBaseAdapter(activityContext,checkforBook(),
		 * VegaConstants.DASHBOARD_EREADER);
		 * 
		 * 
		 * list_notice.setAdapter(noticeAdapter);
		 * list_quizzard.setAdapter(examAdapter);
		 */
		// list_ereader.setAdapter(ereader);

	    }
	});

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
     */
    @Override
    public void onNetworkUnAvailable() {
	runOnUiThread(new Runnable() {

	    @Override
	    public void run() {
		setAlpha = true;
		setAlphaForDashboard();
		no_internet.setVisibility(View.VISIBLE);
	    }
	});

    }

    /**
     * Notifications settings.
     */
    public void notificationsSettings() {
	final Notification notification_att = new Notification();
	notification_att.flags = Notification.FLAG_AUTO_CANCEL;

    }

    /**
     * Sets the alpha for dashboard.
     */
    private void setAlphaForDashboard() {

	if (setAlpha) {

	    dashboardUpdates.setAlpha(30);
	    dashboardUpdates.setClickable(false);

	} else {

	    dashboardUpdates.setAlpha(225);
	    dashboardUpdates.setClickable(true);
	}

    }

    /*
     * public void downloadNoticeBoardFile(String type) { try { Logger.warn(tag,
     * type); DownloadType downloadType = new DownloadType();
     * downloadType.setType(DownloadType.DEFAULT); String url =
     * serverRequests.getRequestURL( type, "" + appData.getUserId());
     * 
     * Logger.warn(tag, "filePath is:" + appData.getInfoFilePath());
     * Logger.warn(tag, "URL is:" + url + "File name is:" +
     * ApplicationData.NOTIFICATION_FILENAME); Download download = new
     * Download(url, appData.getInfoFilePath(),
     * ApplicationData.NOTIFICATION_FILENAME); DownloadManager
     * notificationDownloader = new DownloadManager( appData, download);
     * 
     * notificationDownloader.startDownload(new DownloadCallback() {
     * 
     * @Override public void onSuccess(String downloadedString) {
     * Logger.warn("Vega Notification", "Download success"); try {
     * 
     * 
     * 
     * } catch (Exception e) {
     * ToastMessageForExceptions(R.string.DOWNLOADINFO_EXCEPTION_VEGANOTIFICATION
     * );
     * 
     * e.printStackTrace(); } }
     * 
     * @Override public void onProgressUpdate(int progressPercentage) { // TODO
     * Auto-generated method stub
     * 
     * }
     * 
     * @Override public void onFailure(String failureMessage) { // TODO
     * Auto-generated method stub progress.cancel();
     * Toast.makeText(activityContext, R.string.Unable_to_reach_pearl_server,
     * Toast.LENGTH_LONG).show(); } }); } catch (Exception e) {
     * 
     * ToastMessageForExceptions(R.string.DOWNLOADINFO_EXCEPTION_VEGANOTIFICATION
     * ); e.printStackTrace(); }
     * 
     * 
     * 
     * 
     * }
     */
    /**
     * Download book list.
     */
    public void downloadBookList() {

	Logger.info("ShelfActivity->Populatebookslist",
		"In else - Downloading books list");

	// BookList booksList = new BookList();

	// Fetching the books list freshly from server
	Toast.makeText(activityContext, R.string.loadingPage, toastDisplayTime)
		.show();

	final DownloadType downloadType = new DownloadType();
	downloadType.setType(DownloadType.BOOK_LIST);
	final String url = serverRequests.getRequestURL(
		ServerRequests.DOWNLOADED_BOOKSLIST, "" + appData.getUserId());
	Logger.warn(tag, "URL for downloaded books is:" + url);
	final Download download = new Download(downloadType, url,
		appData.getBooksListsPath(),
		ApplicationData.DOWNLOADEDBOOKSLIST_FILENAME);

	final DownloadManager booklistDownloadManager = new DownloadManager(
		appData, download);
	booklistDownloadManager.startDownload(new DownloadCallback() {
	    @Override
	    public void onFailure(String failureMessage) {
	    }

	    @Override
	    public void onProgressUpdate(int progressPercentage) {
	    }

	    @Override
	    public void onSuccess(String downloadedString) {
		// Re-Populate the books list
		runOnUiThread(new Thread() {
		    @Override
		    public void run() {
			checkforBook();
		    }
		});
	    }
	});

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    @Override
    public void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	Debug.stopMethodTracing();
	Log.i("---------------", "&&& Going in OnPause==");

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	try{
		if(appData.getUser().isClassMonitor() || appData
				.getUser()
				.getUserType()
				.equalsIgnoreCase(
					RoleType.HOMEROOMTEACHER.name())){
				 Logger.info(tag, "User type is: "+appData.getUser().getUserType());
				 downloadAttendanceFromServer();
				 //handler.postDelayed(updateAttendaceStatus, 1000);	
			} else {
			}
	}catch(Exception e){
		Logger.error(tag, e);
		appData.setLoginStatus(false);
	}
	if (!appData.isLoginStatus()) {
		Intent _intent = new Intent(DashboardActivity.this,
				LoginActivity.class);
		_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(_intent);
	}
  }


/**
 * Sets the attendance status.
 *
 * @param msg the new attendance status
 */
/*private void setAttendanceStatus(String msg){
		try {
		    Logger.warn(tag, "-----------------"+msg);
		    list_attendence.setAdapter(new CustomBaseAdapter(this, msg,
		    		VegaConstants.DASHBOARD_ATTENDANCE));
		    if (msg.equalsIgnoreCase(AttendanceDatesComparision.DAILY_BASED_ATTENDANCE_ALREADY_TAKEN)
		    		|| msg.equalsIgnoreCase(AttendanceDatesComparision.MORNING_ATT_ALREADY_TAKEN)
		    		|| msg.equalsIgnoreCase(AttendanceDatesComparision.AFTERNOON_ATT_ALREADY_TAKEN)
		    		|| msg.equalsIgnoreCase(AttendanceDatesComparision.PERIODIC_ATTENDANCE_ALREADY_TAKEN)
		    		|| msg.equalsIgnoreCase(AttendanceDatesComparision.NOT_SCHOOL_TIME) 
		    		 ||( !appData.getUser().isClassMonitor() && !appData.getUser().getUserType().equalsIgnoreCase("HOMEROOMTEACHER"))
		    		) {
		    	attendence.setEnabled(false);
		    } else {
		    	attendence.setEnabled(true);
		    	attendence.setOnClickListener(this);
		    }
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}
*/	
	/**
	 * Download last attendance taken details.
	 *
	 * @param dc the dc
	 */
	private void downloadLastAttendanceTakenDetails(final DownloadCallback dc){

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL u = new URL(serverRequests.getRequestURL(
							ServerRequests.LAST_ATTENDANCE_TAKEN_TIME, appData
									.getUser().getGradeName(), appData
									.getUser().getSectionName()));
					Logger.warn(tag, "url is:" + u);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(u.openStream()));
					String str;
					String responseString = null;
					while ((str = br.readLine()) != null) {
						responseString = str;
					}
					Logger.warn(tag, "response string is:" + responseString);
					if(responseString != null)
						dc.onSuccess(responseString);
				} catch (MalformedURLException e) {
					Logger.error(tag, e);
				} catch (IOException e) {
					Logger.error(tag, e);
				}
			}
		}).start();
	}
	
	/**
	 * Gets the subject and period duration from server.
	 *
	 * @param dc the dc
	 * @return the subject and period duration from server
	 */
	private void getSubjectAndPeriodDurationFromServer(final DownloadCallback dc) {
		Logger.warn(tag, "in getSubjectFromServer");

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL u = new URL(serverRequests.getRequestURL(
							ServerRequests.ATTENDANCE_PERIOD_CONFIG, appData
									.getUser().getGradeName(), appData
									.getUser().getSectionName()));
					Logger.warn(tag, "url is:" + u);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(u.openStream()));
					String str;
					String responseString = null;
					while ((str = br.readLine()) != null) {
						responseString = str;
					}
					Logger.warn(tag, "response string is:" + responseString);
					ObjectMapper mapper = new ObjectMapper();
					if(responseString != null){
						BaseResponse1 response = mapper.readValue(responseString,
								BaseResponse1.class);
						Logger.warn(tag, "response data is:" + response.getData());
						periodConfigresponseString = response.getMessage();
						if(response.getMessage().equalsIgnoreCase("Not a school time."))
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									//setAttendanceStatus("This is not school time");
								}
							});
						if (response.getData() != null && response.getData() != "") {
							String data = response.getData().toString();
							Logger.warn(tag, "data from response string is:" + data);
							dc.onSuccess(data);
						} else {
							Logger.warn(tag,
									"response for getSubjectAndPeriodDurationFromServer is null");
						}	
					}
				} catch (MalformedURLException e) {
					Logger.error(tag, e);
				} catch (IOException e) {
					Logger.error(tag, e);
				}
			}
		}).start();
	}
	
	/** The update attendace status. */
	private Runnable updateAttendaceStatus = new Runnable(){
		@Override
		public void run(){
			Logger.info(tag, "Hanlder--- checking status");
			checkAttendanceStatus();
			long delay = calculateDelay();
			Logger.warn(tag, "setting delay to:"+delay);
			if(attendanceFrequency != null 
					&&attendanceFrequency.equalsIgnoreCase(AttendanceType.SUBMIT_PERIODICALLY.getAttendanceType())){
				if(!periodConfigresponseString.equalsIgnoreCase("Not a school time."))
					handler.postDelayed(updateAttendaceStatus, delay);
				else
					handler.postDelayed(updateAttendaceStatus, 5*60*1000);
			}
		}
	};
	
	/**
	 * Calculate delay.
	 *
	 * @return the long
	 */
	private long calculateDelay(){
		String time2 = "Thu Jan 01 13:30:00 IST 1970";
		Calendar c = Calendar.getInstance();
		c.get(Calendar.HOUR_OF_DAY);
		c.get(Calendar.MINUTE);
		time2 = c.get(Calendar.HOUR_OF_DAY) +":"+ c.get(Calendar.MINUTE);
		int diffTime = AttendanceDatesComparision.calculateTimeDifference(time2, periodEndTime);
		return (diffTime*1000*60);
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
	    dashboardUpdates.setClickable(true);
	    dashboardUpdates.setVisibility(View.VISIBLE);
	    dasshboardUpdatesProgress.setVisibility(View.GONE);
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {
	    progress = 0;
	    dasshboardUpdatesProgress.setVisibility(View.VISIBLE);

	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
	    dasshboardUpdatesProgress.setProgress(values[0]);
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
	 */
	@Override
	protected Void doInBackground(Void... arg0) {
	    // TODO Auto-generated method stub
	    while (progress < 100) {
		progress++;
		publishProgress(progress);
		SystemClock.sleep(100);
	    }
	    return null;
	}
    }

    /**
     * Sets the user name.
     */
    @SuppressWarnings("unused")
	private void setUserName() {
	if (appData.getUser().getFirstName() == null
		&& appData.getUser().getSecondName() == null) {
	    final Intent nullIntent = new Intent(DashboardActivity.this,
		    LoginActivity.class);
	    startActivity(nullIntent);
	    LoggedStudentName = (TextView) findViewById(R.id.logged_student_name);
	    LoggedStudentName.setText("Hi");
	} else {
	    Logger.warn("DASHBOARD", "USERDETAILS ARE"
		    + appData.getUser().getFirstName() + " "
		    + appData.getUser().getSecondName());
	    LoggedStudentName = (TextView) findViewById(R.id.logged_student_name);
	    String secondName = appData.getUser().getSecondName();
	    if (appData.getUser().getSecondName().contains(" ")) {
		secondName = secondName.substring(0, secondName.indexOf(" "));
	    }
	    LoggedStudentName.setText(secondName);
	}
    }

    /**
     * Download notice board info.
     *
     * @throws Exception the exception
     */
    private void downloadNoticeBoardInfo() throws Exception {
	try {
	    final DownloadType downloadType = new DownloadType();
	    downloadType.setType(DownloadType.DEFAULT);
	    final String url = serverRequests.getRequestURL(
		    ServerRequests.NOTICEBOARD, "" + appData.getUserId());

	    Logger.warn(tag, "filePath is:" + appData.getInfoFilePath());
	    Logger.warn(tag, "URL is:" + url + "File name is:"
		    + ApplicationData.NOTIFICATION_FILENAME);
	    final Download download = new Download(url,
		    appData.getInfoFilePath(),
		    ApplicationData.NOTIFICATION_FILENAME);
	    final DownloadManager notificationDownloader = new DownloadManager(
		    appData, download);

	    notificationDownloader.startDownload(new DownloadCallback() {
		@Override
		public void onSuccess(String downloadedString) {
		    Logger.warn("Vega Notification", "Download success");
		    try {

		    } catch (final Exception e) {

			e.printStackTrace();
		    }
		}

		@Override
		public void onProgressUpdate(int progressPercentage) {
		}

		@Override
		public void onFailure(String failureMessage) {
		    Toast.makeText(getApplicationContext(),
			    R.string.Unable_to_reach_pearl_server,
			    Toast.LENGTH_LONG).show();
		}
	    });
	} catch (final Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Download teacher notices.
     *
     * @return the string
     */
    synchronized private String downloadTeacherNotices() {

	Log.i("----", "I m in request to server");

	final StringParameter teacherId = new StringParameter("userId",
		appData.getUserId());

	final StringParameter noticetype = new StringParameter("noticeType",
		"userNotices");

	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);
	params.add(noticetype);

	final PostRequestHandler postRequest = new PostRequestHandler(
		serverRequests.getRequestURL(ServerRequests.TEACHER_MY_NOTICES,
			""), params, new DownloadCallback() {

		    ObjectMapper objMapper = new ObjectMapper();
		    BaseResponse response = new BaseResponse();
		    List<Notice> _notice;

		    @SuppressWarnings("unchecked")
		    @Override
		    public void onSuccess(String downloadedString) {

			try {

			    response = objMapper.readValue(downloadedString,
				    BaseResponse.class);

			    if (response.getData().toString() != null
				    && TextUtils.equals(response.getStatus()
					    .toString(), StatusType.SUCCESS
					    .toString())) {
				_notice = NoticeBoardParser.getNotice(response
					.getData().toString());

				if (_notice.isEmpty()) {

				    teacherNoticeData = "No notices to display ";
				} else
				    teacherNoticeData = _notice.get(0)
					    .getTitle();
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						/*noticeAdapter=new CustomBaseAdapter(
							    DashboardActivity.this,
							    teacherNoticeData,
							    VegaConstants.DASHBOARD_NOTICE);
						    
						    list_notice.setAdapter(noticeAdapter);
						    noticeAdapter.notifyDataSetChanged();*/
					}
			    });
			    }
			} catch (final Exception e) {
			    e.printStackTrace();
			}
		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
		    }

		    @Override
		    public void onFailure(String failureMessage) {
		    }
		});
	postRequest.request();
	return teacherNoticeData;
    }
	
	/**
	 * Download attendance from server.
	 */
	private void downloadAttendanceFromServer() {
		Logger.info(tag, "att download");
		String url = serverRequests.getRequestURL(ServerRequests.ATTENDANCE,
				appData.getGradeName(), appData.getSectionName());
		Download downloadAttendance = new Download(url,
				appData.getAttendancePath(), ApplicationData.ATTENDANCE);
		DownloadManager dm = new DownloadManager(appData, downloadAttendance);
		dm.startDownload(new DownloadCallback() {

			@Override
			public void onSuccess(final String downloadedString) {
				Logger.warn(tag, "downloaded att string is:"+downloadedString);
				runOnUiThread(new Thread() {
					@Override
					public void run() {
						loadingDialog.hide();
						try {
						    checkAttendanceStatus();
						} catch (NullPointerException e) {
							Logger.error(tag, e);
						}
					}
				});
			}

			@Override
			public void onProgressUpdate(int progressPercentage) {

			}

			@Override
			public void onFailure(String failureMessage) {
				Toast.makeText(activityContext,
						R.string.Unable_to_reach_pearl_server,
						Toast.LENGTH_LONG).show();
			}
		});
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
					//    getExamsList();
					} else {
					    if (!AppStatus.getInstance(activityContext).isOnline(activityContext)) {
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
						    2000).show();
					} else {
					    
					    try {
						examsArrayList = ExamListParser.getExamFile(downloadedString, appData.getExamsListFile());
						
					
					    } catch (JsonParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					    } catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					    } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					    }
					    if (examsArrayList != null) {

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
	
}
