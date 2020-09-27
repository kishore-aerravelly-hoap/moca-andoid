/**
 * 
 */
package com.pearl.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.auth.AuthenticationException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.MalformedJsonException;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.BadTokenException;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.baseresponse.login.AndroidUser;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ResponseStatus;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.security.PearlSecurity;
import com.pearl.services.NotficationService;
import com.pearl.services.UploadAnswerSheetAlarm;
import com.pearl.ui.models.BaseResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class LoginActivity.
 */
public class LoginActivity extends BaseActivity {
	
	/** The user name. */
	private String userName;
	
	/** The password. */
	private String password;
	
	/** The user. */
	private AndroidUser user;
	
	/** The entered user name. */
	EditText enteredUserName;
	
	/** The entered password. */
	EditText enteredPassword;
	
	/** The submit. */
	Button submit;
	
	/** The version id. */
	TextView versionId;
	
	/** The version. */
	String version;
	
	/** The salt. */
	String salt;
	
	/** The ps. */
	PearlSecurity ps;
	
	/** The app data. */
	ApplicationData appData;

    /** The progress. */
    private ProgressDialog progress;
    
    /** The vega config. */
    private VegaConfiguration vegaConfig;
    
    /** The server request. */
    private ServerRequests serverRequest;
    
    /** The m_sz wlanmac. */
    private String m_szWLANMAC;
    
    /** The json response. */
    private BaseResponse jsonResponse;
    
    /** The new file name. */
    private final String newFileName = ".PearlAppData";
    
    /** The user details. */
    AndroidUser userDetails = null;
    
    /** The am. */
    private AlarmManager am;

	/** The h. */
	public static Handler h;

	/**
	 * Called when the activity is first created.
	 *
	 * @param savedInstanceState the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vegaConfig = new VegaConfiguration(this);
		appData = (ApplicationData) getApplication();
		serverRequest = new ServerRequests(getApplicationContext());
		appData.resetApplicationData();
		// Logger.warn(tag, "DEVICEID - device id in login is:" + deviceId);
		salt = PearlSecurity.getSaltForLogin(deviceId);
		// Logger.warn(tag, "LOGIN - salt is:" + salt);
		ps = new PearlSecurity(salt);
		WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
		// Logger.info(tag, "---------------------MAC number of device is===="
		// + m_szWLANMAC);
		File f = new File(Environment.getExternalStorageDirectory() + "/"
				+ newFileName);
		if (f.exists()) {
			Logger.warn(tag, "File " + newFileName + " exists");
			appData.setAppPrivateFilesPath(newFileName);
		}
		am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		buildUI();
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((ReadbookActivity.h != null || ShelfActivity.h != null || OnlineActivity.h != null)
				&& (keyCode == KeyEvent.KEYCODE_BACK)) {
			Logger.info(tag, "Readbook is Not null===");
			Intent revert = new Intent(LoginActivity.this, LoginActivity.class);
			revert.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			revert.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			revert.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			// revert.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(revert);
			// overridePendingTransition(0,0);
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Builds the ui.
	 */
	private void buildUI() {
		// Logger.warn(tag, "in build UI");
		try {
			String loggedInUserId = vegaConfig
					.getValue(VegaConstants.HISTORY_LOGGED_IN_USER_ID);
			String activity = vegaConfig
					.getValue(VegaConstants.HISTORY_ACTIVITY);
			String examId = vegaConfig.getValue(VegaConstants.HISTORY_EXAM_ID);
			Logger.warn(tag, "Logged in user = " + loggedInUserId
					+ " Activity to launch = " + activity);
			if ((!loggedInUserId.equals("0")) && (!activity.equals("null"))) {

				// Logger.warn(tag,
				// "LOGIN - Valid case, move to default activity");
				// user =
				// appData.getUserDetailsFromLastLoginResponse(loggedInUserId);
				user = appData
						.getUserDetailsFromLastLoginJSONResponse(loggedInUserId);
				/*
				 * Logger.info(tag, "LOGIN - user id in login activity is:" +
				 * user.getId());
				 */
				/*
				 * Logger.info(tag, "LOGIN - user first name is:" +
				 * user.getFirstName());
				 */
			
				if (user != null) {
					
					appData.setUser(user);
					appData.setLoginStatus(true);
					startService(new Intent(LoginActivity.this,NotficationService.class));
					if (activity.equals("DashboardActivity")) {
						Intent dashBoard = new Intent(this,
								DashboardActivity.class);
						dashBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
						dashBoard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(dashBoard);
						startActivity(dashBoard);
						finish();
					} else if (activity.equals("NoticeBoardActivity")) {
						Intent noticeBoardIntent = new Intent();
						noticeBoardIntent.setClass(this,
								NoticeBoardActivity.class);
						startActivity(noticeBoardIntent);
						finish();
					} else if (activity.equals("ReadbookActivity")) {
						// Logger.info(tag, "LOGIN - read book");
						int pageNo = Integer.parseInt(vegaConfig
								.getValue(VegaConstants.HISTORY_PAGE_NUMBER));
						int bookId = Integer.parseInt(vegaConfig
								.getValue(VegaConstants.HISTORY_BOOK_ID));
						// Logger.warn(tag, "book id form config handler is:" +
						// bookId);
						// Logger.warn(tag, "page no from config handler is:" +
						// pageNo);
						Intent readbookIntent = new Intent();
						readbookIntent.setClass(this, ReadbookActivity.class);
						readbookIntent.putExtra("pageNo", pageNo);
						readbookIntent.putExtra("book_id", bookId);
						startActivity(readbookIntent);
						finish();
					} else if (activity.equals("ShelfActivity")) {
						Intent intent = new Intent(LoginActivity.this,
								LoginActivity.class);
						Logger.warn(tag, "ExamId is:" + examId);
						intent.setClass(this, ShelfActivity.class);
						startActivity(intent);
						finish();
					} else if (activity.equals("OnlineActivity")) {
						Intent intent = new Intent();
						intent.setClass(this, OnlineActivity.class);
						startActivity(intent);
						finish();
					} else if (activity.equals("NoteBookActivity")) {
						String subject = vegaConfig
								.getValue(VegaConstants.HISTORY_SUBJECT);
						Logger.warn(tag, "subject value is:" + subject);
						int noteBookId = Integer.parseInt(vegaConfig
								.getValue(VegaConstants.HISTORY_NOTEBOOK_ID));
						String userId = vegaConfig
								.getValue(VegaConstants.HISTORY_LOGGED_IN_USER_ID);
						Intent intent = new Intent();
						intent.putExtra("Subject", subject);
						intent.putExtra("NoteBookId", noteBookId);
						intent.putExtra("user_id", userId);
						intent.setClass(this, NoteBookActivity.class);
						startActivity(intent);

						finish();
					} else if (activity.equals("SubjectsListActivity")) {
						Intent intent = new Intent();
						intent.setClass(this, SubjectsListActivity.class);
						startActivity(intent);

						finish();
					} else if (activity.equals("ExamListActivity")) {
						Intent intent = new Intent();
						intent.setClass(this, ExamListActivity.class);
						startActivity(intent);
						finish();
					} else if (activity.equals("ExamListActivity")) {
						Intent intent = new Intent();
						intent.setClass(this, ExamListActivity.class);
						startActivity(intent);
						finish();
					} else if (activity.equals("FeedbackActivity")) {
						Intent intent = new Intent();
						intent.setClass(this, FeedbackActivity.class);
						startActivity(intent);
						finish();
					} else if (activity.equals("FAQActivity")) {
						Intent intent = new Intent();
						intent.setClass(this, FAQActivity.class);
						startActivity(intent);
						finish();
					} else if (activity.equals("QuestionsActivity")) {
						Logger.warn("Getting to --------->", "QuestionActivity");
						Intent intent = new Intent(this,
								DashboardActivity.class);
						intent.putExtra(
								"question_no",
								Integer.parseInt(vegaConfig
										.getValue(VegaConstants.HISTORY_QUESTION_NUM)));
						intent.putExtra("Exam_Id", examId);
						startActivity(intent);
						finish();
					} else if (activity.equals("QuestionsListActivity")) {
						Logger.warn("Getting to --------->", "QuestionList");
						Intent intent = new Intent(this,
								DashboardActivity.class);
						// intent.putExtra("Exam_Id", examId);
						startActivity(intent);
						finish();
					} else if (activity.equals("ChatActivity")) {
						Intent intent = new Intent(this, ChatActivity.class);
						startActivity(intent);
						finish();
					} else {
						Intent dashBoardIntent = new Intent();
						dashBoardIntent.setClass(this, DashboardActivity.class);
						startActivity(dashBoardIntent);

						finish();

					}
				} else {
					Logger.warn(tag,
							"This isn't a valid case, go back to login screen");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// ToastMessageForExceptions(R.string.LOGINSCREEN_EXCEPTION);
			Logger.warn(tag, e.toString());
		}
		setRepeatingAlarmToUploadAnswerSheet();
		buildLoginUI();
	}

	/**
	 * Builds the login ui.
	 */
	private void buildLoginUI() {
		Logger.info(tag, "Build login UI");
		setContentView(R.layout.login);

		enteredUserName = (EditText) findViewById(R.id.userName);
		enteredPassword = (EditText) findViewById(R.id.password);
		submit = (Button) findViewById(R.id.signin);
		/*
		 * serverChoice = (ToggleButton) findViewById(R.id.server_choice); try {
		 * if (vegaConfig.getValue(VegaConstants.SERVER_BASE_PATH)
		 * .equalsIgnoreCase("http://172.16.255.105:8080/pearl")) {
		 * serverChoice.setText("La Salle server");
		 * 
		 * } else if (vegaConfig.getValue(VegaConstants.SERVER_BASE_PATH)
		 * .equalsIgnoreCase("http://pearl.pressmart.com/pearl")) {
		 * serverChoice.setText("sg server"); } } catch
		 * (InvalidConfigAttributeException e) { Logger.warn(tag, e.toString());
		 * }
		 */
		// versionId = (TextView) findViewById(R.id.version);
		// user = new User();
		user = new AndroidUser();

		try {
			version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			version = "1.0";
			ToastMessageForExceptions(R.string.LOGINSCREEN_PACKAGE_NOT_FOUND_EXCEPTION);

			Logger.error("Application Data", e);
		}

		Logger.info("version", version);
		appData.setVersion(version);

		progress = new ProgressDialog(this);
		progress.setCancelable(false);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					userName = enteredUserName.getText().toString();
					password = enteredPassword.getText().toString();

					// Allowing the user to login even when the network is
					// unavailable
					if (userName.trim().length() > 0
							&& password.trim().length() > 0) {
						if (isNetworkStatus()) {
							progress.setTitle("Authenticating..");
							progress.setMessage("Please wait..");
							try {
								progress.show();
							} catch (BadTokenException e) {
								Logger.error(tag, e);
							}

							downloadLoginFile(userName, password);
						} else {
							Toast.makeText(activityContext,
									"Please check your internet connection",
									Toast.LENGTH_LONG).show();
							if (ApplicationData.isFileExists(appData
									.getLastUserLoginResponseXML())) {
								// Logger.warn(tag, "file already exists");
								authenticateUser(getLoginDetailsFromJson());
							} else {
								progress.hide();
								Toast.makeText(
										activityContext,
										R.string.network_connection_unreachable,
										toastDisplayTime).show();
							}
						}
					} else
						Toast.makeText(activityContext,
								"User name/Password cannot be empty ",
								Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					try {
						if (isNetworkStatus())
							downloadLoginFile(userName, password);
						else
							getLoginDetailsFromJson();
					} catch (Exception e2) {
						Logger.error("LoginActivity", e);
					}
					Logger.error("LoginActivity", e);
				}
			}
		});

	}

	/**
	 * Checks whether the person is the intended person to access the
	 * Application.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @throws AuthenticationException the authentication exception
	 */
	private void downloadLoginFile(String userName, String password)
			throws AuthenticationException {
		// Logger.info(tag, "in authenticate user");
		Logger.info(tag, "user id is:" + appData.getUserId());

		// url = serverRequest.getRequestURL(true, url +
		// "/Tablet/code/login.php?username=$$&userpassword=$$",userName,password);
		// Logger.warn(tag,
		// "Root file name is:"+appData.getAppPrivateFilesPath());
		String requestUrl = serverRequest.getRequestURL(
				ServerRequests.LOGIN_AUTHENTATION, userName, password,
				m_szWLANMAC);
		sendRequestToServer(requestUrl, new DownloadCallback() {

			@Override
			public void onSuccess(String downloadedString) {
				ApplicationData.writeToFile(downloadedString,
						appData.getLastUserLoginResponseXML());
				Logger.info("LoginActivity",
						"%%%%%%%%%%%%%%%%%DownLoaded String..."
								+ downloadedString);
				try {

					authenticateUser(getLoginDetailsFromJson());
				} catch (MalformedJsonException e) {
					Logger.error(tag, e);
				}
			}

			@Override
			public void onProgressUpdate(int progressPercentage) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(String failureMessage) {
				Logger.error(tag, "LOGIN - Failure");
				if (ApplicationData.isFileExists(appData
						.getLastUserLoginResponseXML())) {
					// Logger.warn(tag, "file already exists");
					try {
						authenticateUser(getLoginDetailsFromJson());
					} catch (MalformedJsonException e) {
						Logger.error(tag, e);
					}
				} else {
					progress.hide();
					Toast.makeText(activityContext,
							R.string.network_connection_unreachable,
							toastDisplayTime).show();
				}
			}
		});
	}

	/**
	 * Authenticates the user based on the response given by Json.
	 *
	 * @param status the status
	 */
	private void authenticateUser(boolean status) {
		Logger.warn(tag, "temp path is:" + appData.getAppPrivateFilesPath());
		if (status) {
			appData.setLoginStatus(true);
			setRepeatingAlarmToUploadAnswerSheet();
			// Logger.warn(tag, "take the user to shelf");
			File f = new File(Environment.getExternalStorageDirectory()
					.toString() + "/" + newFileName);
			
			if(!ApplicationData.isFileExists(appData.getQuestionTypeFileName()))
			{
				sendRequestToServerForQuestionType();
			}
			//if(!ApplicationData.isFileExists(appData.getTestCategoryFileName()))
				sendRequestToServerForTestCategory();
				
				sendRequestToServerForAcademicYear();
			if (f.exists()) {
				Logger.warn(tag, "file exists");
				appData.setAppPrivateFilesPath(newFileName);
				startService(new Intent(LoginActivity.this,NotficationService.class));
				Intent i = new Intent(LoginActivity.this,
						DashboardActivity.class);
				i.putExtra("LOGIN", "LOGIN");
				i.putExtra("USERID", appData.getUserId());
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				overridePendingTransition(
						R.anim.grow_from_bottomright_to_topleft,
						R.anim.shrink_from_topleft_to_bottomright);
				startActivity(i);
				finish();
				} else {
				Logger.warn(tag, "file doesnot exists");
				startService(new Intent(LoginActivity.this,NotficationService.class));
				Intent i = new Intent(this, LocalFileSyncActivity.class);
				startActivity(i);
				finish();
			}
			// Logger.info(tag,
			// "new path is "+appData.getAppPrivateFilesPath());
		} else {
			Logger.warn(tag, "---else is executing........................");
			Logger.warn(tag, "---user details object is:" + userDetails);
			Logger.warn(tag, "User name from android:" + userName);
			Logger.warn(tag, "password from andriod is :" + password);
			// Logger.warn(tag, "---JSON message" + jsonResponse.getMessage());
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (userDetails == null) {
						Toast.makeText(activityContext,
								jsonResponse.getMessage(), Toast.LENGTH_LONG)
								.show();
					} else if (userDetails != null
							&& userDetails.getPassword() != null
							&& !userDetails.getPassword().equals(userName)
							|| userDetails.getFirstName() != null
							&& !userDetails.getFirstName().equals(password)) {
						Toast.makeText(activityContext,
								"Please check your credentials",
								Toast.LENGTH_SHORT).show();
					}
					progress.hide();
				}
			});
		}
	}

	/**
	 * Parses the json downloaded.
	 *
	 * @return the login details from json
	 * @throws MalformedJsonException the malformed json exception
	 */
	private boolean getLoginDetailsFromJson() throws MalformedJsonException {
		ObjectMapper mapper = new ObjectMapper();
		boolean flag = false;
		try {
			String content = ApplicationData.readFile(appData.getAppTempPath()
					+ ApplicationData.LAST_LOGIN_RESPONSE_FILENAME);
			jsonResponse = mapper.readValue(content, BaseResponse.class);
			/*
			 * Logger.warn(tag, "LOGIN - response status is:" +
			 * jsonResponse.getStatus());
			 */

			if (jsonResponse.getStatus().toString()
					.equals(ResponseStatus.FAILURE)) {
				Logger.warn(tag, "LOGIN - failed---------------");
				flag = false;
			} else {
				Logger.warn(tag, "--- message is:" + jsonResponse.getMessage());
				if (jsonResponse.getData() != null) {
					try {
						String jsonData = jsonResponse.getData().toString();
						// Logger.warn(tag, "Json form server is:" + jsonData);
						ObjectReader reader = mapper.reader(AndroidUser.class);
						userDetails = reader.readValue(jsonData);
						/*
						 * We are using password field to save username and
						 * firstname field to save password to sync with the
						 * response we are getting from server
						 */
						if (userDetails != null) {

							if (userDetails.getPassword() != null
									&& userDetails.getPassword().equals(
											userName)
									&& userDetails.getFirstName() != null
									&& userDetails.getFirstName().equals(
											password)) {

								flag = true;
								appData.setUser(userDetails);
							}
						} else {
							Logger.warn(tag, "user object is null");
						}
					} catch (Exception e1) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								ToastMessageForExceptions(R.string.JSON_PARSING_EXCEPTION);
							}
						});
						e1.printStackTrace();
					}
					/*
					 * Logger.warn(tag, "user details is:" +
					 * userDetails.getGradeId());
					 */
					// TODO Phani
					// Read the auth data and update your ConfigHandler.
					try {
						vegaConfig.setValue(
								VegaConstants.HISTORY_LOGGED_IN_USER_ID,
								userDetails.getId());
					} catch (ColumnDoesNoteExistsException e) {
						ToastMessageForExceptions(R.string.COLUMN_DOES_NOT_EXIST_NOTICEBOARDACTIVITY);
						e.printStackTrace();
					}
				}
			}
		} catch (JsonProcessingException e) {
			Logger.error(tag, "Json Parsing exception" + e);
		} catch (IOException e) {
			Logger.error(tag, e);
		}
		return flag;
	}

	/**
	 * Send request to server for question type.
	 */
	private void sendRequestToServerForQuestionType() {
		PostRequestHandler request = new PostRequestHandler(
				serverRequest.getRequestURL(ServerRequests.QUESTION_TYPE, ""),
				null, new DownloadCallback() {

					@Override
					public void onSuccess(String downloadedString) {

						/*List<String> questionTypes = Arrays
								.asList(downloadedString.split(","));*/

						ApplicationData.writeToFile(downloadedString,appData.getQuestionTypeFileName());
					}

					@Override
					public void onProgressUpdate(int progressPercentage) {

					}

					@Override
					public void onFailure(String failureMessage) {

					}
				});
		request.request();
	}

	/**
	 * Send request to server.
	 *
	 * @param requestUrl the request url
	 * @param dc the dc
	 */
	private void sendRequestToServer(final String requestUrl,
			final DownloadCallback dc) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Logger.warn(tag, "Login -  URL :" + requestUrl);
					URL url = new URL(requestUrl);
					URLConnection connection = url.openConnection();
					connection.setConnectTimeout(15000);
					// connection.setReadTimeOut(2000);
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					String line;
					String responseString = null;
					while ((line = reader.readLine()) != null) {
						responseString = line;
						System.out.println("----:" + line);
					}
					reader.close();
					dc.onSuccess(responseString);
					System.out.println("----Connnection---:"
							+ connection.getExpiration());
				} catch (Exception e) {
					Logger.error(tag, e);

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (ApplicationData.isFileExists(appData
									.getLastUserLoginResponseXML())) {
								// Logger.warn(tag, "file already exists");
								try {
									authenticateUser(getLoginDetailsFromJson());
									
								} catch (MalformedJsonException e) {
									Logger.error(tag, e);
								}
							} else {
								progress.hide();
								Toast.makeText(activityContext,
										R.string.Unable_to_reach_pearl_server,
										toastDisplayTime).show();
							}
						}
					});
				}
			}
		}).start();
	}

	/**
	 * Retains the state the user was in, before looging out.
	 *
	 * @return the activity type
	 */
	/**
	 * Checks whether the user is logged in
	 * 
	 * @return flag depending on the status(whether the user is logged in or
	 *         logged out)
	 */

	@Override
	public String getActivityType() {
		return "Logged_in";
	}

	/* (non-Javadoc)
	 * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
	 */
	@Override
	public void onNetworkAvailable() {

	}

	/* (non-Javadoc)
	 * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
	 */
	@Override
	public void onNetworkUnAvailable() {
	}
	
	 /**
 	 * Sets the repeating alarm to upload answer sheet.
 	 */
 	public void setRepeatingAlarmToUploadAnswerSheet() {
		Intent intent = new Intent(this, UploadAnswerSheetAlarm.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				(60* 60 * 1000), pendingIntent);
	}
	 
	/**
	 * Send request to server for test category.
	 */
	public void sendRequestToServerForTestCategory(){
		Download download = new Download(serverRequest.getRequestURL(
				ServerRequests.TEST_CATEGORY, ""),
				appData.getCurrentUserFilesPath(),
				ApplicationData.TEST_CATEGORY_FILE);
		DownloadManager downloadManager = new DownloadManager(appData, download);
		downloadManager.startDownload(new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				Logger.warn(tag, "exam categories downlaoded successfully");
			}
			
			@Override
			public void onProgressUpdate(int progressPercentage) {
				
			}
			
			@Override
			public void onFailure(String failureMessage) {
				
			}
		});
		
	}
	
	/**
	 * Send request to server for academic year.
	 */
	private void sendRequestToServerForAcademicYear(){
		Download download = new Download(serverRequest.getRequestURL(
				ServerRequests.ACADEMIC_YEAR, ""),
				appData.getCurrentUserFilesPath(),
				ApplicationData.ACADEMIC_YEAR_FILE);
		DownloadManager downloadManager = new DownloadManager(appData, download);
		downloadManager.startDownload(new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				Logger.warn(tag, "academic year downlaoded successfully");
			}
			
			@Override
			public void onProgressUpdate(int progressPercentage) {
				
			}
			
			@Override
			public void onFailure(String failureMessage) {
				
			}
		});
	}
}