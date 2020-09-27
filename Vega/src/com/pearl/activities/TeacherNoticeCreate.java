package com.pearl.activities;

/**
 * @author spasnoor
 */

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.android.ui.ShowProgressBar;
import com.pearl.application.ApplicationData;
import com.pearl.chat.server.response.BaseResponse;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.ParentDetailsparser;
import com.pearl.parsers.json.TeacherDetailsParser;
import com.pearl.ui.models.Notice;
import com.pearl.ui.models.Parent;
import com.pearl.ui.models.StatusType;
import com.pearl.ui.models.Teacher;

/**
 * The Class TeacherNoticeCreate.
 */
public class TeacherNoticeCreate extends Activity {

	/** The message. */
	private EditText title, message;

	/** The spinner. */
	private GridView spinner;

	/** The calender. */
	private ImageView calender;

	/** The category selector. */
	private RadioGroup categorySelector;

	/** The selector. */
	private RadioButton selector;

	/** The app data. */
	private ApplicationData appData;

	/** The server requests. */
	private ServerRequests serverRequests;

	/** The date time. */
	private TextView dateTime;

	/** The date_and_time. */
	private CustomDateTimePickerActivity date_and_time;

	/** The from date. */
	private Calendar fromDate;

	/** The to date. */
	private Calendar toDate;

	/** The sections. */
	private List<String> sections = new LinkedList<String>();

	/** The base response. */
	private BaseResponse response, baseResponse;

	/** The checked value. */
	private String CHECKED_VALUE = "";

	/** The menu. */
	private Button sendNotice, menu;

	/** The Ids. */
	private List<String> Ids = new ArrayList<String>();

	/** The selectedpersons. */
	private HashMap<Integer, String> selectedpersons = new HashMap<Integer, String>();

	/** The ids hash map. */
	private HashMap<Integer, String> idsHashMap = new HashMap<Integer, String>();

	/** The hanlder. */
	private Handler hanlder;

	/** The final date. */
	private String finalDate;

	/** The uncheck. */
	private CheckBox check;//, uncheck;

	/** The studentselection. */
	private boolean studentselection;

	/** The items. */
	private List<String> items;

	/** The parent list. */
	private List<Parent> parentList;

	/** The teacher list. */
	private List<Teacher> teacherList;

	/** The return value. */
	private int returnValue = 0;

	/** The adapter. */
	private GridAdapter adapter;

	/** The date selected. */
	private Date dateSelected;

	/** The ids list. */
	private List<String> idsList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher_notice_build);
		title = (EditText) findViewById(R.id.create_notice_title);
	//	message = (EditText) findViewById(R.id.create_notice_message);
		message=(EditText)findViewById(R.id.create_notice_message);
		dateTime = (TextView) findViewById(R.id.create_notice_date_and_time);
		spinner = (GridView) findViewById(R.id.spinner_selector);
		calender = (ImageView) findViewById(R.id.create_notice_calender);
		categorySelector = (RadioGroup) findViewById(R.id.create_notice_sendto_selection);
		sendNotice = (Button) findViewById(R.id.send_notice);
		check = (CheckBox) findViewById(R.id.noitce_checkAll);
		//uncheck = (CheckBox) findViewById(R.id.notice_uncheckAll);
		menu = (Button) findViewById(R.id.menu);
		appData = (ApplicationData) getApplication();
		serverRequests = new ServerRequests(TeacherNoticeCreate.this);
		Ids = new LinkedList<String>();
		idsList = new ArrayList<String>();
		hanlder = new Handler();

		menu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(TeacherNoticeCreate.this, ActionBar.class);
				startActivity(i);
			}
		});
		check.setChecked(false);
		check.setText("Check All");
		check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (check.isChecked()) {
					//uncheck.setChecked(false);
					studentselection = true;
					//check.setText("UnCheck All");
					idsList = new ArrayList<String>();
					if (parentList != null) {
						spinner.setAdapter(new GridAdapter(
								getApplicationContext(), parentList, null, null));
						for (Parent ll : parentList) {
							idsList.add(ll.getParentId());
						}
					}
			
					else if (teacherList != null) {
						spinner.setAdapter(new GridAdapter(
								getApplicationContext(), null, teacherList,
								null));
						for (Teacher ll : teacherList) {
							idsList.add(ll.getTeacherId());
						}
					}


					else if (items != null) {
						spinner.setAdapter(new GridAdapter(
								getApplicationContext(), null, null, items));
						for (String ll : items) {
							idsList.add(ll.toString());
						}
					}
				
					
				}
				
				else {
					
					check.setText("Check All");
					studentselection = false;
					
				
					if (parentList != null) {
						spinner.setAdapter(new GridAdapter(
								getApplicationContext(), parentList, null, null));
						for (Parent ll : parentList) {
							idsList.remove(ll.getParentId());
						}
					} else if (teacherList != null) {
						spinner.setAdapter(new GridAdapter(
								getApplicationContext(), null, teacherList,
								null));
						for (Teacher ll : teacherList) {
							idsList.remove(ll.getTeacherId());
						}

					}

					else if (items != null) {
						spinner.setAdapter(new GridAdapter(
								getApplicationContext(), null, null, items));
						for (String ll : items) {
							idsList.remove(ll.toString());
						}
					}
				
					
					
					/*
						 * uncheck.setChecked(false); check.setChecked(true);
						 * studentselection = true; if (parentList != null)
						 * spinner.setAdapter(new GridAdapter(
						 * getApplicationContext(), parentList, null, null));
						 * else if (teacherList != null) {
						 * spinner.setAdapter(new GridAdapter(
						 * getApplicationContext(), null, teacherList, null));
						 * 
						 * } else if (items != null) spinner.setAdapter(new
						 * GridAdapter( getApplicationContext(), null, null,
						 * items));
						 */
					
				}
			}
		});

/*		uncheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (uncheck.isChecked()) {
					check.setChecked(false);
					studentselection = false;
					if (parentList != null) {
						spinner.setAdapter(new GridAdapter(
								getApplicationContext(), parentList, null, null));
						for (Parent ll : parentList) {
							idsList.remove(ll.getParentId());
						}
					} else if (teacherList != null) {
						spinner.setAdapter(new GridAdapter(
								getApplicationContext(), null, teacherList,
								null));
						for (Teacher ll : teacherList) {
							idsList.remove(ll.getTeacherId());
						}

					}

					else if (items != null) {
						spinner.setAdapter(new GridAdapter(
								getApplicationContext(), null, null, items));
						for (String ll : items) {
							idsList.remove(ll.toString());
						}
					}
				} else {
						 * check.setChecked(false); uncheck.setChecked(true);
						 * studentselection = false; if (parentList != null)
						 * spinner.setAdapter(new GridAdapter(
						 * getApplicationContext(), parentList, null, null));
						 * else if (teacherList != null) spinner.setAdapter(new
						 * GridAdapter( getApplicationContext(), null,
						 * teacherList, null)); else if (items != null)
						 * spinner.setAdapter(new GridAdapter(
						 * getApplicationContext(), null, null, items));
						 
				}
			}
		});*/

		categorySelector
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if(AppStatus.getInstance(getApplicationContext()).isOnline(getApplicationContext())){
							idsHashMap = new LinkedHashMap<Integer, String>();
							sections = new LinkedList<String>();
							switch (checkedId) {
							case R.id.radio_all:
								CHECKED_VALUE = "all";
								check.setVisibility(View.GONE);
							//	uncheck.setVisibility(View.GONE);
								check.setChecked(false);
								//uncheck.setChecked(false);
								spinner.setEnabled(false);
								spinner.setVisibility(View.INVISIBLE);
								spinner.refreshDrawableState();
								break;
							case R.id.radio_teachers:
								CHECKED_VALUE = "teachers";
								check.setVisibility(View.VISIBLE);
								//uncheck.setVisibility(View.VISIBLE);
								check.setChecked(false);
							//	uncheck.setChecked(false);
								spinner.setEnabled(true);
								spinner.setVisibility(View.VISIBLE);
								ShowProgressBar.showProgressBar(
										"Downloading teachers list ",
										requestToServer("teachers"),
										TeacherNoticeCreate.this);

								break;
							case R.id.radio_sections:
								CHECKED_VALUE = "sections";
								check.setChecked(false);
								check.setVisibility(View.VISIBLE);
								//uncheck.setVisibility(View.VISIBLE);
								//uncheck.setChecked(false);
								spinner.setEnabled(true);
								spinner.setVisibility(View.VISIBLE);
								ShowProgressBar.showProgressBar(
										"Downloading sections list ",
										requestToServer("sections"),
										TeacherNoticeCreate.this);

								break;
							case R.id.radio_parents:
								CHECKED_VALUE = "parents";
								check.setChecked(false);
								spinner.setEnabled(true);
								check.setVisibility(View.VISIBLE);
							//	uncheck.setVisibility(View.VISIBLE);
								spinner.setVisibility(View.VISIBLE);
								//uncheck.setChecked(false);
								ShowProgressBar.showProgressBar(
										"Downloading parents list ",
										requestToServer("parents"),
										TeacherNoticeCreate.this);
								break;
							default:
								break;
							}
						}else{
							Toast.makeText(getApplicationContext(), "Please check Wifi connection", Toast.LENGTH_LONG).show();
						}
						
					}
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		if (appData.getUser() == null) {
			Intent i = new Intent(TeacherNoticeCreate.this,
					DashboardActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
			finish();
		}
	}

	/**
	 * Calender view on click.
	 * 
	 * @param v
	 *            the v
	 */
	public void calenderViewOnClick(View v) {

		date_and_time = new CustomDateTimePickerActivity(
				TeacherNoticeCreate.this, fromDate, toDate,
				new CustomDateTimePickerActivity.ICustomDateTimeListener() {

					@Override
					public void onSet(Dialog dialog, Calendar calendarSelected,
							Date dateSelected, int year, String monthFullName,
							String monthShortName, int monthNumber, int date,
							String weekDayFullName, String weekDayShortName,
							int hour24, int hour12, int min, int sec,
							String AM_PM) {
						if (dateSelected.after(new Date())) {
						    TeacherNoticeCreate.this.dateSelected=dateSelected;
						    monthNumber++;
						    String s = monthNumber+"";
						    if(s.length() == 1){
						    	s = "0"+(monthNumber);
						    	//monthNumber = Integer.parseInt(s2);
						    	Logger.info("","show monthnumber");
						    }
						    
						    String dayOfMonth1 = calendarSelected.get(Calendar.DAY_OF_MONTH)+"";
						    if(dayOfMonth1.length() == 1){
						    	dayOfMonth1 = "0"+dayOfMonth1;
						    }
						    	
						    dateTime.setText(""
							    + dayOfMonth1
							    + "/"
							    + s
							    + "/"
							    + year
							    + ","
							    + hour24
							    + ":"
							    + (String.valueOf(min).length() > 1 ? min
								    : "0" + min) +":"+(String.valueOf(sec).length() > 1 ? sec
									    : "0" + sec)+"");
						    finalDate = monthNumber + "/" + date + "/"
								    + year + " " + hour24 + ":" + min + ":"
								    + sec;
						} else {
							Toast.makeText(TeacherNoticeCreate.this,
									"You cannot select previous date",
									Toast.LENGTH_LONG).show();
						}
					}

					@Override
					public void onCancel() {

					}

				});

		date_and_time.showDialog("fromdate");

	}

	/**
	 * Send notice on click.
	 * 
	 * @param v
	 *            the v
	 */
	public void sendNoticeOnClick(View v) {
		if (title.getText().toString().trim().length() != 0
				&& message.getText().toString().trim().length() != 0
				&& dateTime.getText().toString().trim().length() != 0) {

			if (TextUtils.equals(CHECKED_VALUE, "all") && idsList.isEmpty()) {
				if (dateSelected.after(new Date())) {
					if (buildNotice()) {
						if(AppStatus.getInstance(getApplicationContext()).isOnline(getApplicationContext())){
							ShowProgressBar.showProgressBar(
									"Submitting notice to server",
									sendNoticeToServer(baseResponse),
									TeacherNoticeCreate.this);
						}else
							Toast.makeText(TeacherNoticeCreate.this,
									"Please check Wifi connection", Toast.LENGTH_LONG)
									.show();
					} else {
						Toast.makeText(TeacherNoticeCreate.this,
								"Failed to post notice", Toast.LENGTH_LONG)
								.show();
					}
				} else {
					Toast.makeText(TeacherNoticeCreate.this,
							"Change your notice time", Toast.LENGTH_LONG)
							.show();
				}
			} else if ((TextUtils.equals(CHECKED_VALUE, "teachers")
					|| TextUtils.equals(CHECKED_VALUE, "parents") || TextUtils
						.equals(CHECKED_VALUE, "sections"))
					&& !idsList.isEmpty()) {
				if (dateSelected.after(new Date())) {
					if (buildNotice()) {

						ShowProgressBar.showProgressBar(
								"Submitting notice to server",
								sendNoticeToServer(baseResponse),
								TeacherNoticeCreate.this);
					} else {
						Toast.makeText(TeacherNoticeCreate.this,
								"Failed to post notice", Toast.LENGTH_LONG)
								.show();
					}
				} else {
					Toast.makeText(TeacherNoticeCreate.this,
							"Change your notice time", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				if((TextUtils.equals(CHECKED_VALUE, "teachers"))&&(idsList.isEmpty())){
				Toast.makeText(TeacherNoticeCreate.this,"Please select at least one teacher to proceed ",
						Toast.LENGTH_LONG).show();
			}
				else if((TextUtils.equals(CHECKED_VALUE,"parents"))&&(idsList.isEmpty())){
					Toast.makeText(TeacherNoticeCreate.this, "Please select at least one parent to proceed",
							Toast.LENGTH_LONG).show();
				}
				else if((TextUtils.equals(CHECKED_VALUE, "sections"))&&(idsList.isEmpty())){
					Toast.makeText(TeacherNoticeCreate.this,"Please select at least one section to proceed",
							Toast.LENGTH_LONG).show();
				}else if((TextUtils.equals(CHECKED_VALUE, ""))&&(idsList.isEmpty())){
					Toast.makeText(TeacherNoticeCreate.this,"Please select at least one option to proceed",
							Toast.LENGTH_LONG).show();
				}
			}
				
		} else{
			if(title.getText().toString().trim().length() == 0
					&& message.getText().toString().trim().length()== 0
					&& dateTime.getText().toString().trim().length() == 0){
				Toast.makeText(TeacherNoticeCreate.this, "Title, Message, Due Date are empty",
						Toast.LENGTH_LONG).show();
			}
			else  if (title.getText().toString().trim().length() == 0) {
				Toast.makeText(TeacherNoticeCreate.this, "Title is empty",
						Toast.LENGTH_LONG).show();
			}
			 else if (message.getText().toString().trim().length() == 0) {
				Toast.makeText(TeacherNoticeCreate.this, "Message is empty",
						Toast.LENGTH_LONG).show();
			} else if (dateTime.getText().toString().trim().length() == 0) {
				Toast.makeText(TeacherNoticeCreate.this, "Due Date is empty",
						Toast.LENGTH_LONG).show();
			}
		
		}
	}

	/**
	 * Send notice to server.
	 * 
	 * @param notice
	 *            the notice
	 * @return the int
	 */
	private int sendNoticeToServer(BaseResponse notice) {
		returnValue = 0;
		Log.i("----", "I m in getExamListFormServer()");

		final StringParameter teacherId = new StringParameter("userId",
				appData.getUserId());

		final StringParameter data = new StringParameter("notice", notice);
		final StringParameter date = new StringParameter("dueDate", finalDate);

		final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		params.add(teacherId);
		params.add(data);
		params.add(date);

		final PostRequestHandler postRequest = new PostRequestHandler(
				serverRequests.getRequestURL(
						ServerRequests.TEACHER_NOTICE_SUBMIT, ""), params,
				new DownloadCallback() {

					ObjectMapper objMapper = new ObjectMapper();
					BaseResponse baseresponse;

					@Override
					public void onSuccess(String downloadedString) {

						try {
							baseresponse = objMapper.readValue(
									downloadedString, BaseResponse.class);
						} catch (final JsonParseException e) {
							e.printStackTrace();
						} catch (final JsonMappingException e) {
							e.printStackTrace();
						} catch (final IOException e) {
							e.printStackTrace();
						}
						if (TextUtils.equals(baseresponse.getStatus()
								.toString(), "SUCCESS")) {
							hanlder.post(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(getApplicationContext(),
											"Successfully posted notice",
											Toast.LENGTH_LONG).show();
									final Intent i = new Intent(
											TeacherNoticeCreate.this,
											TeacherNoticeBoardFragmentActivity.class);
									i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
									returnValue = 100;
									startActivity(i);
									finish();

								}
							});

						} else if (TextUtils.equals(baseresponse.getStatus()
								.toString(), "FAILURE")) {
							hanlder.post(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(getApplicationContext(),
											"Failed to post notice",
											Toast.LENGTH_LONG).show();

								}
							});
						}

					}

					@Override
					public void onProgressUpdate(int progressPercentage) {
						returnValue = progressPercentage;
					}

					@Override
					public void onFailure(String failureMessage) {
						hanlder.post(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getApplicationContext(),
										"Failed to post notice",
										Toast.LENGTH_LONG).show();

							}
						});
					}
				});
		postRequest.request();
		return returnValue;
	}

	/**
	 * Request to server.
	 * 
	 * @param radioType
	 *            the radio type
	 * @return the int
	 */
	private int requestToServer(final String radioType) {
		returnValue = 0;
		Log.i("----", "I m in getExamListFormServer()");

		final StringParameter teacherId = new StringParameter("userId",
				appData.getUserId());

		final StringParameter type = new StringParameter("radioType", radioType);

		final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		params.add(teacherId);
		params.add(type);

		final PostRequestHandler postRequest = new PostRequestHandler(
				serverRequests.getRequestURL(
						ServerRequests.TEACHER_NOTICEBOARD_DATA, ""), params,
				new DownloadCallback() {

					ObjectMapper objMapper = new ObjectMapper();

					@Override
					public void onSuccess(String downloadedString) {

						try {
							response = objMapper.readValue(downloadedString,
									BaseResponse.class);
							if (response.getData() != null) {
								hanlder.post(new Runnable() {

									@Override
									public void run() {
										if (TextUtils.equals("teachers",
												radioType)) {
											spinner.setEnabled(true);
											final List<Teacher> teachersList = TeacherDetailsParser
													.getTeachersList(response
															.getData()
															.toString());
											teacherList = teachersList;
											adapter = new GridAdapter(
													TeacherNoticeCreate.this,
													null, teachersList, null);

											spinner.setAdapter(adapter);
											adapter.notifyDataSetChanged();

											for (final Iterator<Teacher> iterator = teachersList
													.iterator(); iterator
													.hasNext();) {
												sections.add(iterator.next()
														.getTeacherFullName());
											}
											for (int i = 0; i < teachersList
													.size(); i++) {
												idsHashMap.put(i, teachersList
														.get(i).getTeacherId());
											}

										} else if (TextUtils.equals("sections",
												radioType)) {
											spinner.setEnabled(true);
											try {
												JSONArray responseArray = new JSONArray(
														response.getData()
																.toString());
												for (int i = 0; i < responseArray
														.length(); i++) {
													sections.add(responseArray
															.get(i).toString()
															.replace(",", "-"));
												}
											} catch (JSONException e) {
												e.printStackTrace();
											}
											items = sections;
											adapter = new GridAdapter(
													TeacherNoticeCreate.this,
													null, null, sections);
											spinner.setAdapter(adapter);
											adapter.notifyDataSetChanged();
											for (int i = 0; i < items.size(); i++) {
												idsHashMap.put(i, items.get(i));
											}

										} else if (TextUtils.equals("parents",
												radioType)) {
											spinner.setEnabled(true);
											final List<Parent> parentsList = ParentDetailsparser
													.getParentsList(response
															.getData()
															.toString());
											parentList = parentsList;

											adapter = new GridAdapter(
													TeacherNoticeCreate.this,
													parentsList, null, null);
											spinner.setAdapter(adapter);
											adapter.notifyDataSetChanged();
											// GridAdapter p;

											for (final Iterator<Parent> iterator = parentsList
													.iterator(); iterator
													.hasNext();) {
												sections.add(iterator.next()
														.getParentName());

											}
											for (int i = 0; i < parentsList
													.size(); i++) {
												idsHashMap.put(i, parentsList
														.get(i).getParentId());
											}

										}
									}
								});
								ShowProgressBar.progressBar.dismiss();
								returnValue = 100;
							}
						} catch (final Exception e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onProgressUpdate(int progressPercentage) {
						returnValue = progressPercentage;
					}

					@Override
					public void onFailure(String failureMessage) {
						ShowProgressBar.progressBar.dismiss();
					}
				});
		postRequest.request();
		return returnValue;
	}

	/**
	 * Builds the notice.
	 * 
	 * @return true, if successful
	 */
	private boolean buildNotice() {
		final Notice buildNotice = new Notice();
		String noticeData, Ids = "";
		Date date = null;
		try {
			date = getDate(dateTime.getText().toString(), "dd/MM/yyyy,HH:mm:ss");
		} catch (final Exception e1) {
			e1.printStackTrace();
		}

		baseResponse = new BaseResponse();
		buildNotice.setTime(date.getHours() + ":" + date.getMinutes() + ":"
				+ date.getSeconds());
		buildNotice.setTitle(title.getText().toString());
		buildNotice.setMsg(message.getText().toString());
		if (!TextUtils.equals(CHECKED_VALUE, "all")) {
			if (!idsList.isEmpty()) {

				// if(!check.isChecked() && !uncheck.isChecked())
				// {

				/*
				 * for (final Iterator<String> ite =
				 * selectedpersons.values().iterator(); ite .hasNext();) { if
				 * (!idsHashMap.isEmpty()) { String ids=ite.next();
				 * if(idsHashMap.containsValue(ids)){ this.Ids.add(ids); } } }
				 */

				for (final Iterator<String> ite = idsList.iterator(); ite
						.hasNext();) {
					if (!idsHashMap.isEmpty()) {
						String ids = ite.next();
						if (idsHashMap.containsValue(ids)) {
							this.Ids.add(ids);
						}
					}
				}

				for (final Iterator<String> ite = this.Ids.iterator(); ite
						.hasNext();) {

					Ids += "::" + ite.next();
				}

				/*
				 * }else {
				 * 
				 * }
				 */
			} else {

				Ids = "";
			}

		} else {
			Ids = "";
		}

		buildNotice.setCheckedValue(Ids);
		buildNotice.setSelectedRadioType(CHECKED_VALUE);
		buildNotice.setNoticeFromName(appData.getUser().getSecondName());

		try {
			final Gson gson = new Gson();
			noticeData = gson.toJson(buildNotice, Notice.class);
			baseResponse.setData(noticeData);
			baseResponse.setMessage("Teacher notice data");
			baseResponse.setStatus(StatusType.SUCCESS);
			return true;
		} catch (final Exception e) {
			baseResponse.setStatus(StatusType.FAILURE);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Gets the date.
	 * 
	 * @param date
	 *            the date
	 * @param pattern
	 *            the pattern
	 * @return the date
	 * @throws Exception
	 *             the exception
	 */
	public Date getDate(String date, String pattern) throws Exception {
		final SimpleDateFormat dateFormate = new SimpleDateFormat(pattern);
		
		try {
			return dateFormate.parse(date);
		} catch (final Exception e) {
			throw e;
		}
	}

	/**
	 * Gets the time.
	 * 
	 * @param duration
	 *            the duration
	 * @return the time
	 */
	String getTime(long duration) {

		final Date dat = new Date(duration);

		final String time = dat.getHours() + ":" + dat.getMinutes() + ":"
				+ dat.getSeconds();

		return time;
	}

	/**
	 * The Class GridAdapter.
	 */
	class GridAdapter extends BaseAdapter {

		/** The m inflater. */
		private final LayoutInflater mInflater;

		/** The check box state. */
		boolean[] checkBoxState;

		/** The items. */
		List<String> items;

		/** The parent list. */
		List<Parent> parentList;

		/** The teacher list. */
		List<Teacher> teacherList;

		/** The ctx. */
		Context ctx;

		/**
		 * Instantiates a new grid adapter.
		 * 
		 * @param context
		 *            the context
		 * @param parentListtext
		 *            the parent list
		 * @param teacherList
		 *            the teacher list
		 * @param strList
		 *            the str list
		 */
		public GridAdapter(Context context, List<Parent> parentList,
				List<Teacher> teacherList, List<String> strList) {
			this.ctx = context;
			items = new LinkedList<String>();
			idsList = new ArrayList<String>();
			// selectedpersons = new HashMap<Integer, String>();
			if (parentList != null) {
				TeacherNoticeCreate.this.teacherList = null;
				for (Parent parent : parentList) {

					items.add(parent.getParentName());

				}

				this.parentList = parentList;
			} else if (teacherList != null) {
				TeacherNoticeCreate.this.parentList = null;
				this.teacherList = teacherList;

				for (Teacher teacher : teacherList) {

					items.add(teacher.getFirstName() + " "
							+ teacher.getSecondName());

				}
			} else if (strList != null) {
				TeacherNoticeCreate.this.parentList = null;
				TeacherNoticeCreate.this.teacherList = null;
				this.items = strList;
			}
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			checkBoxState = new boolean[items.size()];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return items.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			return position;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return position;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;

			if (convertView == null) {
				holder = new ViewHolder();

				// selectedpersons = new HashMap<Integer, String>();
				convertView = new View(ctx);
				convertView = mInflater.inflate(
						R.layout.create_test_step_3_list_row, null);
				holder.checkbox = (CheckBox) convertView
						.findViewById(R.id.studentcheckBox);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.checkbox.setId(position);
			holder.checkbox.setText(items.get(position).toString());
			Logger.warn("TeacherNoticeCreate", "inflaate - studentselection "+studentselection);
			if (check.isChecked()) {
				Logger.info("TeacherNoticeCreate", "inflaate - check all is checked");
				holder.checkbox.setChecked(studentselection);


				/*if (parentList != null) {
					for (Parent ll : parentList) {
						idsList.add(ll.getParentId());
						
					}
				} else if (teacherList != null) {
					for (Teacher ll : teacherList) {
						idsList.add(ll.getTeacherId());
					}
				}

				else if (items != null) {
					for (String ll : items) {
						idsList.add(ll.toString());
					}
				}*/
				for(int i=0; i<items.size(); i++)
				checkBoxState[i] = true;
			}
			/*else if (uncheck.isChecked()) {
				Logger.info("TeacherNoticeCreate", "inflaate - uncheck is checked");
				holder.checkbox.setChecked(studentselection);
				if (parentList != null) {
					for (Parent ll : parentList) {
						idsList.remove(ll.getParentId());
					}
				} else if (teacherList != null) {
					for (Teacher ll : teacherList) {
						idsList.remove(ll.getTeacherId());
					}
				}
				else if (items != null) {
					for (String ll : items) {
						idsList.remove(ll.toString());
					}
				}

				checkBoxState[position] = false;
			} */else {
				
				Logger.info("TeacherNoticeCreate", "inflaate - check all unchecked");
				Logger.warn("TeacherNoticeCreate", "inflaate - checkboxstate is "+checkBoxState[position]);
				holder.checkbox.setChecked(checkBoxState[position]);
				//clearAllSelectedBooks();
				//openBooksSelected = false;
				//gridView.setAdapter(booksadpt);
				//check.setText("Check All");
			}
		/*	convertView country = convertView.get(position);
			   holder.code.setText(" (" +teacherList  country.getCode() + ")");
			   holder.name.setText(country.getName());
			   holder.name.setChecked(country.isSelected());
			   holder.name.setTag(country);
			 
			   return convertView;*/

			holder.checkbox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					check.setChecked(false);
					//uncheck.setChecked(false);
					Logger.warn("", "  "+checkBoxState);
					if (((CheckBox) v).isChecked()) {
						
						checkBoxState[position] = true;
						if (parentList != null) {
							idsList.add(parentList.get(position).getParentId());
						} else if (teacherList != null) {
							idsList.add(teacherList.get(position)
									.getTeacherId());
						} else if (items != null) {
							idsList.add(items.get(position).toString());
						}
						if(!check.isChecked() && idsList.size() > 0 && items.size() == idsList.size()){
							check.setChecked(true);
						}

					} else {
						//if(check.getText().toString().equalsIgnoreCase("Check All")){
						check.setText("Check All");
						checkBoxState[position] = false;
						if (parentList != null) {
							idsList.remove(parentList.get(position)
									.getParentId());
						} else if (teacherList != null) {
							idsList.remove(teacherList.get(position)
									.getTeacherId());
						
						}
						/*else if(check.isChecked() && idsList.size() != TeacherNoticeCreate.this.teacherList.size() 
								&& idsList.size()>0){
							check.setChecked(false);
							check.setText("Check All");
						}*/
						else if (items != null) {
							idsList.remove(items.get(position).toString());
							/* if(check.isChecked() && idsList.size() != TeacherNoticeCreate.this.teacherList.size() 
									&& idsList.size()>0){
								check.setChecked(false);
								check.setText("Check All");
							 }*/
						}

						
					}
				}
			});
			holder.id = position;

			return convertView;
		}

	}

	/**
	 * The Class ViewHolder.
	 */
	class ViewHolder {

		/** The checkbox. */
		CheckBox checkbox;

		/** The id. */
		int id;
	}

}
