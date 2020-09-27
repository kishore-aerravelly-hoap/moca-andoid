package com.pearl.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.attendance.Attendance;
import com.pearl.attendance.AttendanceType;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ResponseStatus;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.AttendanceParser;
import com.pearl.student.Student;
import com.pearl.ui.models.BaseRequest;
import com.pearl.ui.models.BaseResponse;
import com.pearl.ui.models.BaseResponse1;
import com.pearl.ui.models.PearlConfigKeyValues;
import com.pearl.ui.models.PeriodConfig;
import com.pearl.ui.models.StatusType;
import com.pearl.util.AttendanceDatesComparision;

/**
 * 
 * @author Samreen
 * 
 */
public class AttendanceActivity extends BaseActivity {

	ViewHolder holder;
	boolean[] toggleButtonState;

	TextView grade;
	TextView section;
	TextView totStrength;
	TextView totPresent;
	TextView totAbsent;
	TextView subName,subTextView;
	Button markAllPresent;
	Button markAllAbsent;
	Button submit;
	TextView sortByName;
	TextView sortById;
	ImageView refreshAttendance, help;
	ProgressBar refreshProgress;
	LinearLayout attendanceHeader;
	LinearLayout attendanceBody;
	RelativeLayout attendanceFooter;
	RelativeLayout attendanceAlertBlock;
	TextView attendanceTakenAlert;
	TextView attencedance_frequency;
	String morningSessionTime, eveningSessionTime;
	int periodDuration;
	Context context;
	Button menuButton;
	private int present;
	private int absent;
	private int strength;
	private String attendanceFrequency = "";
	private String subject;
	private Attendance attendance;
	private Student student = Student.getInstance();
	ListView attendanceListView;

	ServerRequests serverRequests;

	public static Handler h;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attendance);
		context = this;
		grade = (TextView) findViewById(R.id.grade);
		section = (TextView) findViewById(R.id.Section);
		totStrength = (TextView) findViewById(R.id.strength);
		totPresent = (TextView) findViewById(R.id.totPresent);
		totAbsent = (TextView) findViewById(R.id.totAbsent);
		menuButton = (Button) findViewById(R.id.LasallemenuButton);
		help = (ImageView) findViewById(R.id.attendance_help);
		attencedance_frequency = (TextView) findViewById(R.id.Attendance_title);

		subName = (TextView) findViewById(R.id.attendance_subject);
		subTextView = (TextView)findViewById(R.id.subjectText);
		attendanceListView = (ListView) findViewById(R.id.attendance_list);
		submit = (Button) findViewById(R.id.submitAtt);
		sortById = (TextView) findViewById(R.id.sortById);
		sortByName = (TextView) findViewById(R.id.sortByName);
		refreshAttendance = (ImageView) findViewById(R.id.refreshAttendance);
		attendanceBody = (LinearLayout) findViewById(R.id.attendanceBody);
		attendanceFooter = (RelativeLayout) findViewById(R.id.attendanceFooter);
		refreshProgress = (ProgressBar) findViewById(R.id.attendance_refresh_progress);
		vegaConfig = new VegaConfiguration(this);
		serverRequests = new ServerRequests(this);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			attendanceFrequency = bundle.getString(VegaConstants.ATTENDANCE_TYPE);
			subject = bundle.getString(VegaConstants.ATTENDANCE_SUBJECT);
		}
		refreshAttendance.bringToFront();
		
		refreshAttendance.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Logger.info("Attendance activity", "Clicked on refresh button");
				if (AppStatus.getInstance(activityContext).isOnline(activityContext)) {
					new asyncTaskUpdateProgress().execute();
					refreshAttendance.setVisibility(View.INVISIBLE);
					refreshAttendance.setClickable(false);
					downloadAttendanceFromServer();
				} else {
					Toast.makeText(activityContext,
							R.string.network_connection_unreachable,
							toastDisplayTime).show();
				}
			}
		});
		
		menuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent newActivity = new Intent(getApplicationContext(),
						ActionBar.class);
				startActivity(newActivity);
			}
		});
		try {
			getAttendance();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (!appData.isLoginStatus()) {
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivity(loginIntent);
			finish();
		}
		try {
			bindEvents();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void bindEvents() throws Exception {
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!AppStatus.getInstance(activityContext).isOnline(activityContext)) {
				    Toast.makeText(v.getContext(),
						R.string.submit_when_disabled, toastDisplayTime)
						.show();
				}else {
					showDialog();
				}
			}
		});

		sortById.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				student.setSortById(true);
				sortStudentRecords();
				displayStudentList(attendance.getStudentlist());
			}
		});

		sortByName.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				student.setSortById(false);
				sortStudentRecords();
				displayStudentList(attendance.getStudentlist());
			}
		});
		
		help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.pearl_tips_layout);
				RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.tips_layout);
				TextView tips = (TextView) dialog.findViewById(R.id.tips);
				ImageView previous = (ImageView) dialog.findViewById(R.id.previous);
				ImageView next = (ImageView) dialog.findViewById(R.id.next);
				layout.setBackgroundResource(R.drawable.attendance_help);
				tips.setText(R.string.attendance_help);
				previous.setEnabled(false);
				next.setEnabled(false);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
		});
	}

	private void getAttendance() throws NullPointerException {
		// loadingDialog.setTitle("Please wait");
		// loadingDialog.show();
		if (!ApplicationData.isFileExists(appData.getAttendancePath()
				+ ApplicationData.ATTENDANCE)) {
			downloadAttendanceFromServer();
			return;
		}
		try {
			attendance = AttendanceParser.getAttendanceFile(appData
					.getAttendancePath() + ApplicationData.ATTENDANCE);
		} catch (JsonParseException e) {
			ToastMessageForExceptions(R.string.JSON_PARSING_EXCEPTION);
			downloadAttendanceFromServer();
			e.printStackTrace();
		} catch (JsonMappingException e) {
			ToastMessageForExceptions(R.string.JSON_MAPPING_EXCEPTION);
			downloadAttendanceFromServer();
			e.printStackTrace();
		} catch (IOException e) {
			ToastMessageForExceptions(R.string.IO_EXCEPTION);
			e.printStackTrace();
		}
		if(attendanceFrequency == null){
			getAttendanceDetails(attendance);	
			AttendanceDatesComparision.compareDates("1264321877000", morningSessionTime,
					eveningSessionTime, Integer.parseInt(userTimeFormat), attendanceFrequency,
					periodDuration);
		}
		updateUI();

	}
	
		
	private void downloadAttendanceFromServer() {
		
		String url = serverRequests.getRequestURL(ServerRequests.ATTENDANCE,
				appData.getGradeName(), appData.getSectionName());
		Download downloadAttendance = new Download(url,
				appData.getAttendancePath(), ApplicationData.ATTENDANCE);
		DownloadManager dm = new DownloadManager(appData, downloadAttendance);
		dm.startDownload(new DownloadCallback() {

			@Override
			public void onSuccess(final String downloadedString) {
				runOnUiThread(new Thread() {
					@Override
					public void run() {
						loadingDialog.hide();
						try {
							getAttendance();
						} catch (NullPointerException e) {
							Logger.error(tag, e);
						}
						if(!refreshAttendance.isEnabled())
							refreshAttendance.setEnabled(true);
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


	public class ViewHolder {
		ToggleButton toggleButton;
		TextView studentId, studentName;
	}

	private void displayStudentList(List<Student> list) {
		AttendanceListArrayAdapter attendanceAdapter = new AttendanceListArrayAdapter(
				this, R.layout.attendance_list_row, list);
		attendanceListView.setAdapter(attendanceAdapter);
		attendanceAdapter.notifyDataSetChanged();
	}

	private void sortStudentRecords() {
		Collections.sort(attendance.getStudentlist(), student);
	}

	private class AttendanceListArrayAdapter extends ArrayAdapter<Student> {
		List<Student> items;

		public AttendanceListArrayAdapter(Context context,
				int textViewResourceId, List<Student> items) {
			super(context, textViewResourceId, items);
			this.items = items;
			toggleButtonState = new boolean[items.size()];

		}

		@Override
		public int getCount() {
			return this.items.size();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			holder = new ViewHolder();
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.attendance_list_row, null);
				holder.studentId = (TextView) convertView
						.findViewById(R.id.studentId);
				holder.studentName = (TextView) convertView
						.findViewById(R.id.studentName);
				holder.toggleButton = (ToggleButton) convertView
						.findViewById(R.id.isPresent);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final Student student = items.get(position);

			holder.studentId.setText(student.getRoleCallId());
			holder.studentName.setText(student.getName());
			holder.toggleButton.setChecked(toggleButtonState[position]);
			holder.toggleButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int index = (Integer) v.getTag();
					Logger.warn(tag, "STATUS - checked:"+((ToggleButton) v).isChecked());
					if (((ToggleButton) v).isChecked()) {
						attendance.getStudentlist().get(position).setIspresent(true);
						toggleButtonState[index] = true;
						((ToggleButton) v)
								.setButtonDrawable(R.drawable.attendance_p_btn);
						absent = absent - 1;
						present = present + 1;

						totPresent.setText(present + "");
						totAbsent.setText(absent + "");
					} else {
						attendance.getStudentlist().get(position).setIspresent(false);
						((ToggleButton) v)
								.setButtonDrawable(R.drawable.attendance_a_btn);
						toggleButtonState[index] = false;
						present = present - 1;
						absent = absent + 1;
						totPresent.setText(present + "");
						totAbsent.setText(absent + "");
					}
				}
			});
			if (toggleButtonState[position]) {
				holder.toggleButton
						.setButtonDrawable(R.drawable.attendance_p_btn);
			} else {
				holder.toggleButton
						.setButtonDrawable(R.drawable.attendance_a_btn);
			}
			holder.toggleButton.setTag(new Integer(position));

			return convertView;
		}
	}

	private void updateUI(){
		attencedance_frequency.setText(attendanceFrequency);
		grade.setText(appData.getGradeName() + "");
		section.setText(appData.getSectionName());
		totStrength.setText(attendance.getStudentlist().size() + "");
		totPresent.setText(0 + "");
		present = 0;
		totAbsent.setText(attendance.getStudentlist().size() + "");
		sortStudentRecords();
		strength = attendance.getStudentlist().size();
		absent = strength;
		if(attendanceFrequency.equalsIgnoreCase(AttendanceType.SUBMIT_PERIODICALLY.getAttendanceType())){
			subName.setVisibility(View.VISIBLE);
			subTextView.setVisibility(View.VISIBLE);
			subName.setText(subject);
		}
		displayStudentList(attendance.getStudentlist());
	}
	
	private void submitAttendanceToServer(String attendenceSubmitRequestJsonString){
		StringParameter sp = new StringParameter("postdata",
				attendenceSubmitRequestJsonString);
		ArrayList<RequestParameter> postParams = new ArrayList<RequestParameter>();
		postParams.add(sp);

		ServerRequests sr = new ServerRequests(activityContext);

		PostRequestHandler postRequestObj = new PostRequestHandler(
				sr.getRequestURL(ServerRequests.ATTENDANCE_SUBMIT,
						""), postParams, new DownloadCallback() {

					@Override
					public void onSuccess(String downloadedString) {
						Logger.warn(tag, "request sent:"
								+ downloadedString);
						ObjectMapper mapper = new ObjectMapper();
						BaseResponse response;
						try {
							response = mapper.readValue(
									downloadedString,
									BaseResponse.class);

							if (StatusType.SUCCESS.toString()
									.equalsIgnoreCase(
											ResponseStatus.SUCCESS)) {
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										loadingDialog.hide();
										Toast.makeText(
												activityContext,
												R.string.attendance_submitted_successfully,
												toastDisplayTime)
												.show();
										Intent i = new Intent(activityContext, DashboardActivity.class);
										startActivity(i);
										finish();
									}
								});

							} else {
								response.getStatus();
								if (StatusType.FAILURE
										.toString()
										.equals(ResponseStatus.FAIL)) {
									Logger.warn(tag,
											"Unable to submit attendance");
								}
							}
						} catch (Exception e) {
							Logger.error(tag, e);
						}
					}

					@Override
					public void onProgressUpdate(
							int progressPercentage) {

					}

					@Override
					public void onFailure(String failureMessage) {
						Logger.warn(tag,
								"Failed to submit attendance");
						loadingDialog.cancel();

					}
				});

		postRequestObj.request();
	}
	
	private void getAttendanceDetails(Attendance attendance){
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
		Logger.warn(tag,
				"Attendance frequency in checkAttendanceFrequency() is:"
						+ attendanceFrequency);
	
	}
	
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
						if(response.getMessage().equalsIgnoreCase("Not a school time."))
							finish();
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

	public class asyncTaskUpdateProgress extends AsyncTask<Void, Integer, Void> {

		int progress;

		@Override
		protected void onPostExecute(Void result) {
			refreshAttendance.setClickable(true);
			refreshAttendance.setVisibility(View.VISIBLE);
			refreshProgress.setVisibility(View.GONE);
		}

		@Override
		protected void onPreExecute() {
			progress = 0;
			refreshProgress.setVisibility(View.VISIBLE);

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			refreshProgress.setProgress(values[0]);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			while (progress < 100) {
				progress++;
				publishProgress(progress);
				SystemClock.sleep(100);
			}
			return null;
		}
	}
	
	private void showDialog(){
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.attendace_sibmit_dialog);
		dialog.setTitle("Title...");

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.text);
		RelativeLayout layout = (RelativeLayout)dialog.findViewById(R.id.dialoge_layout);
		Button dialogButton = (Button) dialog.findViewById(R.id.dialogue_yes);
		Button no = (Button)dialog.findViewById(R.id.dialogue_no);
		
		text.setText("Are you sure you want to submit attendance?");
		layout.setBackgroundColor(Color.parseColor("#efabc3"));
		dialogButton.setText("Submit");
		//dialogButton.setBackgroundResource(R.drawable.feedback_submit_btn);
		no.setText("Cancel");
		//no.setBackgroundResource(R.drawable.feedback_submit_btn);
		
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				showDialog();
				Attendance att = new Attendance();
				att.setGrade(appData.getGradeName());

				att.setSection(appData.getSectionName());
				att.setStudentlist(attendance.getStudentlist());

				if (attendanceFrequency.equalsIgnoreCase(AttendanceType.SUBMIT_SESSIONLY.getAttendanceType())) {
					if (AttendanceDatesComparision.getSession()
							.equalsIgnoreCase("M")) {
						att.setSession("M");
					} else if (AttendanceDatesComparision.getSession()
							.equalsIgnoreCase("A")) {
						att.setSession("A");
					}
				}
				if (attendanceFrequency
						.equalsIgnoreCase(AttendanceType.SUBMIT_PERIODICALLY.getAttendanceType())) {
					att.setPeriod(subject);
				}
				ObjectMapper mapper = new ObjectMapper();
				totPresent.setText(present + "");
				totAbsent.setText(absent + "");
				loadingDialog.setTitle("Loading.....");
				loadingDialog
						.setMessage("Please wait till the attendance gets submitted");
				loadingDialog.show();
				BaseRequest baseRequest = new BaseRequest();
				baseRequest.setAuth("asdf");

				String attendenceSubmitRequestJsonString = null;
				try {

					attendenceSubmitRequestJsonString = mapper
							.writeValueAsString(att);

					baseRequest.setData(attendenceSubmitRequestJsonString);

					attendenceSubmitRequestJsonString = mapper
							.writeValueAsString(baseRequest);
					Logger.info(tag,
							"attendenceSubmitRequestJsonString after sending to the base is:"
									+ attendenceSubmitRequestJsonString);
					submitAttendanceToServer(attendenceSubmitRequestJsonString);
				} catch (Exception e1) {
					Logger.error(tag, e1);
				}
				dialog.dismiss();
			}
		});
		
		no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();				
			}
		});

		dialog.show();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		finish();
	}

	@Override
	public String getActivityType() {
		return "Attendance";
	}

	@Override
	public void onNetworkAvailable() {
		
	}

	@Override
	public void onNetworkUnAvailable() {
		
	}

}
