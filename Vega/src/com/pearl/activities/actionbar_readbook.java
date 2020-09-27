package com.pearl.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.logger.Logger;
import com.pearl.ui.models.RoleType;

public class actionbar_readbook extends Activity implements OnClickListener,
Runnable {
    private RelativeLayout menulay;
    private Timer myTimer;
    private ApplicationData appData;
    private  VegaConfiguration vegaConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.actionbar_readbook_lay);

	appData = (ApplicationData) getApplication();
	vegaConfig = new VegaConfiguration(this);

	menulay = (RelativeLayout) findViewById(R.id.menu);
	myTimer = new Timer();
	myTimer.schedule(new TimerTask() {
	    @Override
	    public void run() {
		TimerMethod();
	    }

	}, 10000);
	final Button menub = (Button) findViewById(R.id.actionbar_menu_button);
	menub.setOnClickListener(this);
	findViewById(R.id.notice_board);

	findViewById(R.id.attendance);

	findViewById(R.id.chat);

	final Button note_book = (Button)findViewById(R.id.note_book);
	note_book.setOnClickListener(this);

	final Button quizzard = (Button) findViewById(R.id.quiz);
	quizzard.setOnClickListener(this);

	final Button ereader = (Button) findViewById(R.id.e_reader);
	ereader.setOnClickListener(this);

	final Button feedback = (Button) findViewById(R.id.feed_back);
	feedback.setOnClickListener(this);

	final Button faqs = (Button) findViewById(R.id.faqs);
	faqs.setOnClickListener(this);
	final Button options = (Button) findViewById(R.id.options_button);
	options.setOnClickListener(this);

	final Button signOut = (Button) findViewById(R.id.signout);
	signOut.setOnClickListener(this);

	final Button analytics = (Button) findViewById(R.id.actionbar_read_book_analytics);
	analytics.setOnClickListener(this);
		

	final ImageView serverStatus = (ImageView) findViewById(R.id.server_status_read);
	try {
	    if (vegaConfig.getValue(VegaConstants.SERVER_BASE_PATH)
		    .equalsIgnoreCase("http://pearlmoca.cloudapp.net:8080/pearl")) {
		serverStatus.setImageResource(R.drawable.orange);
	    } else if (vegaConfig.getValue(VegaConstants.SERVER_BASE_PATH)
		    .equalsIgnoreCase("http://172.16.255.100:8080/pearl")) {
		serverStatus.setImageResource(R.drawable.green1);
	    } else {
		serverStatus.setImageResource(R.drawable.circle_red);
	    }
	} catch (final InvalidConfigAttributeException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	menulay.setSoundEffectsEnabled(true);
	setFinishOnTouchOutside(false);

		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.actionbar_menu_button:
			menulay.setVisibility(View.GONE);
			Intent dash_board = new Intent(this, DashboardActivity.class);
			startActivity(dash_board);
			finish();
			break;
		case R.id.notice_board:
		    menulay.setVisibility(View.GONE);
			switch (checkUserRoletype()) {
			case 1:{
			    Log.i(getClass().getSimpleName(), "I am--------------- teacher"+appData.getUser().getUserType());
			    final Intent quiz_boardTeacher = new Intent(this, TeacherNoticeBoardFragmentActivity.class);
			    startActivity(quiz_boardTeacher);
			    break;
			}
			case 2:
			{
			    Log.i(getClass().getSimpleName(), "I am--------------- Student"+appData.getUser().getUserType());
			    final Intent quiz_boardStudent = new Intent(this, NoticeBoardActivity.class);
			    startActivity(quiz_boardStudent);
			    break;
			}
			default:
			    break;
			}
		    break;
		    
		case R.id.attendance:
			menulay.setVisibility(View.GONE);
			if(appData.getUser().isClassMonitor() || appData
					.getUser().getUserType().equalsIgnoreCase(
						RoleType.HOMEROOMTEACHER.name())){
				Intent attendece_board = new Intent(getApplicationContext(),AttendanceActivity.class);
				try {
					vegaConfig.setValue(VegaConstants.ATTENDANCE_COUNT, 0);
				} catch (ColumnDoesNoteExistsException e) {
					e.printStackTrace();
				}
				startActivity(attendece_board);
				
			}else{
				Toast.makeText(getApplicationContext(),	"You are not eligible to take attendance" ,Toast.LENGTH_LONG).show();
			}
			
			break;
		case R.id.chat:
			menulay.setVisibility(View.GONE);
			Intent chat_board = new Intent(this, ChatActivity.class);

			Logger.info(
					"ACTION BAR",
					"ACTION BAR_CHAT WALL ACTIVITY uesr id is ...."
							+ appData.getUserId());
			chat_board.putExtra(VegaConstants.USER_ID, appData.getUserId());
			try {
				vegaConfig.setValue(VegaConstants.MESSAGE_COUNT, 0);
			} catch (ColumnDoesNoteExistsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			startActivity(chat_board);
			finish();
			break;
		case R.id.note_book:
			menulay.setVisibility(View.GONE);
			Intent noteboardB = new Intent(this, NoteBookActivity.class);
			startActivity(noteboardB);
			finish();
			break;
		case R.id.quiz:
			switch (checkUserRoletype()) {
			case 1:{
				Log.i("Action bar", "I am--------------- teacher"+appData.getUser().getUserType());
				Intent quiz_boardTeacher = new Intent(this, TeacherExamsActivity.class);
				startActivity(quiz_boardTeacher);
				break;
			}
			case 2:
			{
				Log.i("Action bar", "I am--------------- Student"+appData.getUser().getUserType());
				Intent quiz_boardStudent = new Intent(this, ExamListActivity.class);
				try {
					vegaConfig.setValue(VegaConstants.EXAM_COUNT, 0);
				} catch (ColumnDoesNoteExistsException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				startActivity(quiz_boardStudent);
				break;
			}
			default:
				break;
			}
			menulay.setVisibility(View.GONE);
			finish();
			break;
		case R.id.e_reader:
			menulay.setVisibility(View.GONE);

			Intent Eread = new Intent(this, ShelfActivity.class);
			try {
				vegaConfig.setValue(VegaConstants.BOOKS_COUNT, 0);
			} catch (ColumnDoesNoteExistsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			startActivity(Eread);
			finish();
			break;
		case R.id.feed_back:
			menulay.setVisibility(View.GONE);
			Intent feed = new Intent(this, FeedbackActivity.class);
			startActivity(feed);
			finish();
			break;
		case R.id.faqs:
			menulay.setVisibility(View.GONE);
			Intent faq = new Intent(this, FAQActivity.class);
			startActivity(faq);
			finish();
			break;

		case R.id.options_button:
			menulay.setVisibility(View.GONE);
			Intent options = new Intent(this, VegaConfigurationActivity.class);
			startActivity(options);
			finish();
			break;
		case R.id.actionbar_read_book_analytics:
			Intent intent = new Intent(this, PearlAnalyticsList.class);
			startActivity(intent);
			finish();
			break;
		case R.id.signout:
			menulay.setVisibility(View.GONE);
			Logger.info("ACTIONBAR-", "IAM GOING TO LOGIN SCREEN");
			Logger.info("ACTION BAR ACTIVITY",
					"user data is at log out is before appdadta=null  "
							+ appData.getUserId());
			Logger.info("ACTION BAR ACTIVITY", "user data is at sign out is  "
					+ appData.getUserId());

			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Sign Out")
					.setMessage("Are you sure you want to Sign Out?")
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent signOut = new Intent(
											getApplicationContext(),
											LoginActivity.class);
									signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
									signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									signOut.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
									startActivity(signOut);
									finish();
									try {
									    vegaConfig.setValue(
												VegaConstants.HISTORY_ACTIVITY,
												"LoginActivity");
										vegaConfig
												.setValue(
														VegaConstants.HISTORY_LOGGED_IN_USER_ID,
														"0");
									} catch (ColumnDoesNoteExistsException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} finally {

										finish();
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
			menulay.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	private void TimerMethod() {
		this.runOnUiThread(Timer_Tick);
	}

	private Runnable Timer_Tick = new Runnable() {
		@Override
		public void run() {
			menulay.setVisibility(View.GONE);
			finish();
		}

	};
	public int checkUserRoletype() {
		int Role=0; 
		if ((appData.getUser() != null && appData.getUser()
				.getUserType() != null)
				&& (appData.getUser().getUserType()
						.equalsIgnoreCase(RoleType.SUBJECTHEAD.name())
						|| appData.getUser().getUserType()
								.equalsIgnoreCase(RoleType.PRINCIPLE.name()) || appData
						.getUser().getUserType()
						.equalsIgnoreCase(RoleType.HOMEROOMTEACHER.name())
						||appData.getUser().getUserType().equalsIgnoreCase(RoleType.TEACHER.name()))) {
			Logger.info("Action bar", "DASDHBOARDACTIVITY--checkUserRoletype  USER TYPE IS "
					+ appData.getUser().getUserType());
			Role=1;
		}else if((appData.getUser() != null && appData.getUser()
				.getUserType() != null)
				&& (appData.getUser().getUserType()
						.equalsIgnoreCase(RoleType.STUDENT.name()))){
			Logger.info("Action", "DASDHBOARDACTIVITY--checkUserRoletype  USER TYPE IS "
					+ appData.getUser().getUserType());
			Role=2;
			
		}
		return Role;
	}

	@Override
	public void run() {
	}

}
