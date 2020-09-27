/**
 * @author spasnoor
 */

package com.pearl.activities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.android.ui.ShowProgressBar;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.chat.server.response.BaseResponse;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.StudentDetailsParser;
import com.pearl.parsers.json.TeacherExamParser;
import com.pearl.ui.helpers.examination.MultiSelectSpinner;
import com.pearl.ui.models.CommonUtility;
import com.pearl.ui.models.StatusType;
import com.pearl.ui.models.Student;
import com.pearl.user.teacher.examination.Exam;
import com.pearl.user.teacher.examination.ServerExam;
import com.pearl.user.teacher.examination.ServerExamDetails;
import com.pearl.util.SeedGenerator;

// TODO: Auto-generated Javadoc
/**
 * The Class TeahcerExamMultiScheduleActivity.
 */
public class TeahcerExamMultiScheduleActivity extends Activity 
{
    
    /** The _end date. */
    private EditText _testName, _duration, _startDate, _endDate;
    
    /** The _grade. */
    private TextView _grade;
    
    /** The _section spinner. */
    private MultiSelectSpinner _sectionSpinner;
    
    /** The exam menu. */
    private ImageView examMenu;
    
    /** The _cancel. */
    private Button _getStudentsList, _submit, _cancel;
    
    /** The filter students flag. */
    private boolean filterStudentsFlag;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The from date picker. */
    private CustomDateTimePickerActivity fromDatePicker;
    
    /** The to date picker. */
    private CustomDateTimePickerActivity toDatePicker;
    
    /** The set fromdate. */
    private long setFromdate;
    
    /** The set todate. */
    private long setTodate;
    
    /** The selected from date. */
    private Calendar selectedFromDate;
    
    /** The strfromdate. */
    private String strfromdate;
    
    /** The strtodate. */
    private String strtodate;
    
    /** The context. */
    private Activity context;
    
    /** The Constant FROM_DATE. */
    private static final String FROM_DATE = "fromdate";
    
    /** The Constant TO_DATE. */
    private static final String TO_DATE = "todate";
    
    /** The from date cal. */
    private Calendar fromDateCal;
    
    /** The to date cal. */
    private Calendar toDateCal;
    
    /** The date2. */
    private Date date1, date2;
    
    /** The set duration. */
    private long setDuration;
    
    /** The _to date. */
    private long _toDate;
    
    /** The Diff duration. */
    private long DiffDuration;
    
    /** The _ server exam base response. */
    private ServerExam _ServerExamBaseResponse = null;
    
    /** The exam. */
    private final Exam exam = null;
    
    /** The _server requests. */
    private ServerRequests _serverRequests;
    
    /** The subject. */
    private String grade, subject;
    
    /** The section list new. */
    private List<String> sectionListNew, historySections;
    
    /** The section list old. */
    private List<String> sectionListOld;
    
    /** The tag. */
    private final String tag = getClass().getSimpleName();
    
    /** The response. */
    private BaseResponse response;
    
    /** The handler. */
    private Handler handler;
    
    /** The test idref. */
    private String testIdOld, testIdNew, testName,testIdref;
    
    /** The _uncheck. */
    private CheckBox _check, _uncheck;
    
    /** The studentselection. */
    private boolean studentselection;
    
    /** The selected students. */
    private List<Student> selectedStudents=new LinkedList<Student>();
    
    /** The student listfrom parser. */
    private List<Student> studentListfromParser = null;
    
    /** The adptt. */
    private ImageAdapter adptt;
    
    /** The grid. */
    private GridView grid;
    
    /** The sections. */
    private String sections;
    
    /** The students ids. */
    private String studentsIds;
    
    /** The _previously selected sections. */
    private String _previouslySelectedSections = "";
    
    /** The _step four acitivty. */
    private CreateTestStep4 _stepFourAcitivty;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.teacher_exam_multi_schedule);

	_testName = (EditText) findViewById(R.id.multi_schedule_edit_test_name);
	_testName.requestFocus();
	_duration = (EditText) findViewById(R.id.multi_schedule_duration);
	_startDate = (EditText) findViewById(R.id.multi_schedule_start_date);
	_endDate = (EditText) findViewById(R.id.multi_schedule_end_date);
	_startDate.setLongClickable(false);
	_endDate.setLongClickable(false);
	_sectionSpinner = (MultiSelectSpinner) findViewById(R.id.multi_schedule_section_list);
	_getStudentsList = (Button) findViewById(R.id.multi_schedule_get_student_list);
	_submit = (Button) findViewById(R.id.multi_schedule_submit);
	_cancel = (Button) findViewById(R.id.multi_schedule_cancel);
	_grade = (TextView) findViewById(R.id.multi_schedule_grade);
	_check = (CheckBox) findViewById(R.id.multi_schedule_checkall);
	_uncheck = (CheckBox) findViewById(R.id.multi_schedule_uncheckall);
	examMenu = (ImageView) findViewById(R.id.examMenu);
	grid = (GridView) findViewById(R.id.multi_schedule_stepthree_student_list);
	appData = (ApplicationData) getApplication();
	_stepFourAcitivty = new CreateTestStep4();
	sectionListNew = new ArrayList<String>();
	sectionListOld = new ArrayList<String>();
	historySections = new ArrayList<String>();
	grid.setVisibility(View.GONE);
	context = this;
	handler = new Handler();

	_serverRequests = new ServerRequests(
		TeahcerExamMultiScheduleActivity.this);
	testIdOld = getIntent().getExtras().getString(VegaConstants.TEST_ID);
	getFinalStepFileData();
	_cancel.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		final Intent teacher = new Intent(getApplicationContext(),
			TeacherExamsActivity.class);
		teacher.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
			| Intent.FLAG_ACTIVITY_CLEAR_TASK
			| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(teacher);
	    }
	});
	_duration.addTextChangedListener(new TextWatcher() {

	    @Override
	    public void onTextChanged(CharSequence s, int start, int before,
		    int count) {

	    }

	    @Override
	    public void beforeTextChanged(CharSequence s, int start, int count,
		    int after) {

	    }

	    @Override
	    public void afterTextChanged(Editable s) {

		if (!s.toString().isEmpty()) {
		    final long num = Long.parseLong(s.toString()) * 1000 * 60;
		    if (num > 0 && num <= DiffDuration) {
			setDuration = num;
		    } else {
			_duration.setHint("Enter valid time");
			_duration.setText("");
			_duration.setHintTextColor(Color.parseColor("#FF5050"));
		    }
		} else {

		}
	    }
	});
	examMenu.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		final Intent intent = new Intent(getApplicationContext(),
			ActionBar.class);
		startActivity(intent);
	    }
	});


    }


    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
	super.onResume();
	if(!appData.isLoginStatus()) {

	    final Intent login = new Intent(this, LoginActivity.class);
	    startActivity(login);
	    Logger.info(tag,
		    "LOGIN status if shelf..." + appData.isLoginStatus());
	    finish();

	}
	 bindEvents();
    }

    /**
     * Submit on click.
     *
     * @param v the v
     */
    public void submitOnClick(View v) {
    	if(AppStatus.getInstance(this).isOnline(this)) 
	  getSectionsList();
	  else 
	    Toast.makeText(getApplicationContext(),
		    "Please connect to internet in order to proceed",
		    Toast.LENGTH_LONG).show();
    	if(filterStudentsFlag)
    		validateSubmission();

    }

    /**
     * Gets the students list on click.
     *
     * @param v the v
     * @return the students list on click
     */
    synchronized public void  getStudentsListOnClick(View v) {
	filterStudentsFlag = true;
	if (AppStatus.getInstance(getApplicationContext()).isOnline(
		getApplicationContext())) {
	    if(!getSectionsList()) {
		Toast.makeText(TeahcerExamMultiScheduleActivity.this,
			"Please select the section", Toast.LENGTH_LONG).show();
	    }

	  /*  sections = "";
	    if (_sectionSpinner != null && _sectionSpinner.getSelectedStrings() != null && !_sectionSpinner.getSelectedStrings().isEmpty()) {
		for (final String string : _sectionSpinner.getSelectedStrings()) {
		    sections += string + ",";
		    sectionListOld.add(string);
		}
		if (sectionListNew.isEmpty()) {
		    grid.setVisibility(View.GONE);
		    Toast.makeText(TeahcerExamMultiScheduleActivity.this,
			    "No sections to display", Toast.LENGTH_LONG).show();
		}
		if (sections != null && sections.toString() != null) {
		    if (sections.endsWith(",")) {
			sections = sections.substring(0, sections.length() - 1);
		    }
		    filterStudentsFlag = true;
		    requestToServerForStudentList(
			    ServerRequests.TEACHER_STUDENT_LIST, sections);
		   
		}
	    } else {
		Toast.makeText(TeahcerExamMultiScheduleActivity.this,
			"Please select the section", Toast.LENGTH_LONG).show();
	    }*/
	} else {
	    Toast.makeText(getApplicationContext(),
		    "Please connect to internet in order to proceed",
		    Toast.LENGTH_LONG).show();
	}

    }

    /**
     * Start date on click.
     *
     * @param v the v
     */
    public void startDateOnClick(View v) {
	handler.post(new Runnable() {

	    @Override
	    public void run() {
		fromDatePicker = new CustomDateTimePickerActivity(
			context,
			Calendar.getInstance(),
			toDateCal,
			new CustomDateTimePickerActivity.ICustomDateTimeListener() {
			    @Override
			    public void onSet(Dialog dialog,
				    Calendar calendarSelected,
				    Date dateSelected, int year,
				    String monthFullName,
				    String monthShortName, int monthNumber,
				    int date, String weekDayFullName,
				    String weekDayShortName, int hour24,
				    int hour12, int min, int sec, String AM_PM) {
				final String pattern = "dd/MM/yyyy,HH:mm";
				selectedFromDate = calendarSelected;
				final Calendar calendarCurrentDate = Calendar
					.getInstance();
				final int currentYear = calendarCurrentDate
					.get(Calendar.YEAR);
				final int currentMonth = calendarCurrentDate
					.get(Calendar.MONTH);
				final int currentDate = calendarCurrentDate
					.get(Calendar.DATE);
				final int currentMin = calendarCurrentDate
					.get(Calendar.MINUTE);
				final int currentHour = calendarCurrentDate
					.get(Calendar.HOUR_OF_DAY);

				Log.w("", "calendarSelected" + calendarSelected);
				Log.w("", "date " + currentDate
					+ " current min " + currentMin
					+ " hour" + currentHour);
				_startDate.setText("");
				/*_startDate.setHintTextColor(Color
					.parseColor("#FF5050"));*/
				monthNumber++;
				String m = monthNumber +"";
				m = m.length() == 1?("0"+monthNumber) : (monthNumber+"");
				date1 = dateSelected;
				String d = calendarSelected
						.get(Calendar.DAY_OF_MONTH) +"";
				d = (d.length() == 1) ? "0"+d : d;  
				if (dateSelected.getTime() >= calendarCurrentDate
					.getTime().getTime()) {
				    _startDate.setText(""
					    + d
					    + "/"
					    + (m)
					    + "/"
					    + year
					    + ","
					    + hour24
					    + ":"
					    + (String.valueOf(min).length() > 1 ? min
						    : "0" + min) + " ");
				    strfromdate = _startDate.getText()
					    .toString();
				    // exam.getExamDetails().setStartTime(setFromdate);
				    Log.i("-----", "============ "
					    + setFromdate);
				    try {
					date1 = getDate(strfromdate, pattern);
					_endDate.setText(CommonUtility.formatTime(
						new Date(date1.getTime()
							+ 60 * 1000 * 60),
							pattern));
					final String tempDate = _endDate.getText()
						.toString();
					if (date1 != null) {
					    setFromdate = date1.getTime();
					}
					if (_endDate.getText() != null
						&& !tempDate.isEmpty())
					    ;
					{
					    date2 = getDate(tempDate, pattern);
					    setTodate = date2.getTime();
					}
					if (dateComparision(date1, date2)) {
					    doFunction();
					}
				    } catch (final Exception e) {

				    }

				} else {
				    _endDate.setHint("Select correct date and time");
				    _endDate.setHintTextColor(Color.parseColor("#FF5050"));
				    date1 = null;
				    _duration.setText("");
				}
			    }

			    @Override
			    public void onCancel() {

			    }
			});
		fromDatePicker.set24HourFormat(true);
		fromDatePicker.setDate(Calendar.getInstance());
		fromDatePicker.showDialog(FROM_DATE);

	    }
	});

    }

    /**
     * End date on click.
     *
     * @param v the v
     */
    public void endDateOnClick(View v) {
	handler.post(new Runnable() {

	    @Override
	    public void run() {
		if (strfromdate == null
			|| _startDate != null && _startDate.getText().toString()
			.isEmpty())

		{

		    _endDate.setText("");
		    _endDate.setHint("please select start date");
		    _endDate.setHintTextColor(Color.parseColor("#FF5050"));

		} else {
		    Log.w("",
			    "from date before making a cal to change to date is:"
				    + strfromdate);
		    toDatePicker = new CustomDateTimePickerActivity(
			    context,
			    fromDateCal,
			    toDateCal,
			    new CustomDateTimePickerActivity.ICustomDateTimeListener() {
				@Override
				public void onSet(Dialog dialog,
					Calendar calendarSelected,
					Date dateSelected, int year,
					String monthFullName,
					String monthShortName, int monthNumber,
					int date, String weekDayFullName,
					String weekDayShortName, int hour24,
					int hour12, int min, int sec,
					String AM_PM) {
				    final String pattern = "dd/MM/yyyy,HH:mm";

				    final Calendar calendarCurrentDate = Calendar
					    .getInstance();
				    calendarCurrentDate
				    .get(Calendar.YEAR);
				    calendarCurrentDate
				    .get(Calendar.MONTH);
				    calendarCurrentDate
				    .get(Calendar.DATE);
				    calendarCurrentDate
				    .get(Calendar.MINUTE);
				    calendarCurrentDate
				    .get(Calendar.HOUR_OF_DAY);
				    monthNumber++;
					String m = monthNumber +"";
					m = m.length() == 1?("0"+monthNumber) : (monthNumber+"");
				    _endDate.setText("");
				    String d = calendarSelected
							.get(Calendar.DAY_OF_MONTH) +"";
					d = (d.length() == 1) ? "0"+d : d;  
				    final String toDateString = ""
					    + d
					    + "/"
					    + (m)
					    + "/"
					    + year
					    + ","
					    + hour24
					    + ":"
					    + (String.valueOf(min).length() > 1 ? min
						    : "0" + min) + " ";
				    try {
					date2 = getDate(toDateString, pattern);
				    } catch (final Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				    }
				    if (calendarSelected
					    .before(selectedFromDate)
					    || date1.compareTo(date2) == 0) {
					_endDate.setHint("please select correct date");
					_endDate.setHintTextColor(Color
						.parseColor("#FF5050"));
					date2 = null;
					_duration.setText("");

				    } else {

					_endDate.setText(
						d
						+ "/"
						+ (m)
						+ "/"
						+ year
						+ ","
						+ hour24
						+ ":"
						+ (String.valueOf(min)
							.length() > 1 ? min
								: "0" + min) + " ");
					strtodate = _endDate.getText()
						.toString();

					try {
					    date2 = getDate(strtodate, pattern);
					    if (date2 != null) {
						setTodate = date2.getTime();
					    }
					} catch (final Exception e) {

					}

					doFunction();
				    }
				}

				@Override
				public void onCancel() {
				}

			    });
		    toDatePicker.set24HourFormat(true);

		    toDatePicker.setDate(Calendar.getInstance());
		    toDatePicker.showDialog(TO_DATE);

		}
	    }
	});
    }

    /**
     * Date comparision.
     *
     * @param date1 the date1
     * @param date2 the date2
     * @return true, if successful
     */
    private boolean dateComparision(Date date1, Date date2) {
	return date1 != null && date2 != null ? date1.compareTo(date2) < 0 ? date2
		.compareTo(date1) >= 0 : date1.compareTo(date2) < 0
		: !(date1 != null && date2 != null);

    }

    /**
     * Gets the date.
     *
     * @param date the date
     * @param pattern the pattern
     * @return the date
     * @throws Exception the exception
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
     * Do function.
     */
    public void doFunction() {

	try {
	    long durationToset;

	    if (date1.getTime() > date2.getTime())
		durationToset = date1.getTime() - date2.getTime();
	    else
		durationToset = date2.getTime() - date1.getTime();

	    final Date d = new Date();
	    d.setTime(durationToset);
	    final long time = d.getTime();
	    setDuration = time;
	    DiffDuration = time;
	    // exam.getExamDetails().setDuration(time);
	    _duration.setText(String.valueOf(time / (1000 * 60)));
	    checkData();
	} catch (final ParseException e) {
	    e.printStackTrace();
	} catch (final Exception e) {
	    e.printStackTrace();
	}

    }

    /**
     * Check data.
     */
    public void checkData() {
	if (_testName.getText().toString().trim().length() != 0
		) {
	}else if(Integer.parseInt(_duration.getText().toString())==0){
	    Toast.makeText(TeahcerExamMultiScheduleActivity.this,"Invalid Time",
		    Toast.LENGTH_LONG).show();

	}
    }

    /**
     * Retrieve the final step file and add new details.
     *
     * @return the final step file data
     */

    private void getFinalStepFileData() {
	String content = "";

	try {
	    content = ApplicationData.readFile(appData
		    .getStep4FilePath(testIdOld)
		    + ApplicationData.STEP4_FILE_NAME);
	} catch (final Exception e) {

	}
	Logger.warn(tag, "Content is:" + content);
	_ServerExamBaseResponse = TeacherExamParser
		.parseJsonToSeverExamObject(content);
	if (_ServerExamBaseResponse != null
		&& _ServerExamBaseResponse.getExam() != null
		&& _ServerExamBaseResponse.getExam().getExamDetails() != null) {
	    grade = ((ServerExamDetails) _ServerExamBaseResponse.getExam()
		    .getExamDetails()).getGrade();
	    String testName = ((ServerExamDetails) _ServerExamBaseResponse.getExam()
			    .getExamDetails()).getTitle();
	    subject = ((ServerExamDetails) _ServerExamBaseResponse.getExam()
		    .getExamDetails()).getSubject();
	    _previouslySelectedSections += ((ServerExamDetails) _ServerExamBaseResponse
		    .getExam().getExamDetails()).getSection();
	    sectionListOld = ((ServerExamDetails) _ServerExamBaseResponse
		    .getExam().getExamDetails()).getSectionList();
	    if (sectionListOld == null)
		sectionListOld = new ArrayList<String>();
	    try {
		_stepFourAcitivty.deleteObject(testIdOld, appData);
	    } catch (final JsonProcessingException e) {
		e.printStackTrace();
	    } catch (final IOException e) {
		e.printStackTrace();
	    }
	    _testName.setText(testName);
	    _grade.setText("Schedule test for "+grade);
	}
	requestToServerForSectionList();

    }

    /**
     * Sets the new question paper data.
     */
    private void setNewQuestionPaperData() {

	studentsIds = "";
	testName = _testName.getText().toString();
	if (selectedStudents != null && selectedStudents.size() > 0) {
	    runOnUiThread(new Runnable() {
	        
	        @Override
	        public void run() {
	            Toast.makeText(getBaseContext(), "Students are added to Test",
			    Toast.LENGTH_SHORT).show();	    	
	        }
	    });

	    for (int size = 0; size < selectedStudents.size(); size++) {
		studentsIds += selectedStudents.get(size).getStudentId() + ",";
	    }
	}
	testIdNew = SeedGenerator.getNextSeed();
	testIdref=SeedGenerator.getNextSeed();
	_ServerExamBaseResponse.getExam().getExamDetails().setExamId(testIdNew);
	_ServerExamBaseResponse.getExam().getExamDetails().setTitle(testName);
	_ServerExamBaseResponse.getExam().getExamDetails()
	.setDuration(setDuration/(1000*60));
	_ServerExamBaseResponse.getExam().setId(testIdref);
	_ServerExamBaseResponse.getExam().getExamDetails()
	.setStartTime(setFromdate);
	_ServerExamBaseResponse.getExam().getExamDetails()
	.setEndTime(setTodate);
	((ServerExamDetails)_ServerExamBaseResponse.getExam().getExamDetails()).setStartDate(date1);
	((ServerExamDetails)_ServerExamBaseResponse.getExam().getExamDetails()).setEndDate(date2);
	((ServerExamDetails) _ServerExamBaseResponse.getExam().getExamDetails())
	.setSection(sections);
	((ServerExamDetails) _ServerExamBaseResponse.getExam().getExamDetails())
	.setStudentId(studentsIds);
	_check.setVisibility(View.INVISIBLE);
	_uncheck.setVisibility(View.INVISIBLE);
    }

    /**
     * Write data to file.
     *
     * @return true, if successful
     */
    private boolean writeDataToFile() {
	final ObjectMapper _objMapper = new ObjectMapper();
	String _stepFourServerExamContent = null;
	try {
	    _stepFourServerExamContent = _objMapper
		    .writeValueAsString(_ServerExamBaseResponse);
	    ApplicationData.writeToFile(_stepFourServerExamContent,
		    appData.getStep4FilePath(testIdNew)
		    + ApplicationData.STEP4_FILE_NAME);
	    return true;
	} catch (final JsonGenerationException e) {
	    Log.i(tag,
		    "ERROR OCCURRED While Writing the  file from Step TWO TO THREE---JsonGenerationException");
	} catch (final JsonMappingException e) {
	    Log.i(tag,
		    "ERROR OCCURRED While Writing the  file from Step TWO TO THREE---JsonMappingException");
	} catch (final IOException e) {
	    Log.i(tag,
		    "ERROR OCCURRED While Writing the  file from Step TWO TO THREE--IOException");
	}
	//

	return false;
    }

    /**
     * Alert submit.
     */
    private void alertSubmit() {

	final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
		TeahcerExamMultiScheduleActivity.this);
	alertDialog.setTitle("Add Multiple Sections");
	alertDialog
	.setMessage("Click 'Yes' to submit same question paper for other sections!");
	alertDialog.setPositiveButton("Yes",
		new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(final DialogInterface dialog, int which) {
		if (writeDataToFile()) {
		    _stepFourAcitivty.submitEntireExamtoServer(
			    testIdNew, false,
			    TeahcerExamMultiScheduleActivity.this,
			    appData);
		    handler.post(new Runnable() {

			@Override
			public void run() {
			    testIdOld = testIdNew;
			    resetFields();

			    // _sectionSpinner.seta
			    // setContentView(R.layout.teacher_exam_multi_schedule);
			    getFinalStepFileData();
			    dialog.cancel();

			}
		    });
		}

	    }
	}).setNegativeButton("No",
		new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int id) {
		if(writeDataToFile()) {
		    if (AppStatus.getInstance(getApplicationContext())
			    .isOnline(getApplicationContext())) {

			_stepFourAcitivty.submitEntireExamtoServer(
				testIdNew, true,
				TeahcerExamMultiScheduleActivity.this,
				appData);
		    } else {
			Toast.makeText(
				getApplicationContext(),
				"Please connect to internet in order to proceed",
				Toast.LENGTH_LONG).show();
		    }
		    dialog.cancel();
		}
	    }
	});

	// Showing Alert Message
	alertDialog.show();

    }

    /**
     * Request to server for section list.
     */
    void requestToServerForSectionList() {
	// getFinalStepFileData();
	final StringParameter teacherIdParam = new StringParameter("teacherId",
		appData.getUserId());
	final StringParameter gradeParam = new StringParameter("grade", grade);
	final StringParameter subjectParam = new StringParameter("subject", subject);
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherIdParam);
	params.add(gradeParam);
	params.add(subjectParam);

	final PostRequestHandler request = new PostRequestHandler(
		_serverRequests.getRequestURL(
			ServerRequests.TEACHER_SECTION_LIST, ""), params,
			new DownloadCallback() {
		    ObjectMapper objMapper = new ObjectMapper();

		    @Override
		    public void onSuccess(String downloadedString) {

			try {
			    response = objMapper.readValue(downloadedString,
				    BaseResponse.class);
			    Logger.warn(tag, "response is :" + downloadedString);
			    if (response.getStatus().equals(StatusType.FAILURE)) {
				Toast.makeText(getApplicationContext(),
					"No grades", Toast.LENGTH_LONG).show();
			    }
			    getSectionListFromResponse();
			} catch (final Exception e) {
			    Logger.error(tag, e);
			}

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
     * Gets the section list from response.
     *
     * @return the section list from response
     */
    @SuppressWarnings("unchecked")
    void getSectionListFromResponse() {
	Logger.warn(tag, "sections detaisl are:" + response.getData());
	if (response != null && response.getData() != null) {
	    sectionListNew = (List<String>) response.getData();
	    setSectionList();
	}
    }

    /**
     * Sets the section list.
     */
    void setSectionList() {
	handler.post(new Runnable() {

	    @Override
	    public void run() {
		if (sectionListNew != null && sectionListOld != null
			&& sectionListNew.containsAll(sectionListOld)) {
		    sectionListNew.removeAll(sectionListOld);
		}
		if (sectionListNew.isEmpty()) {
		    handler.post(new Runnable() {

			@Override
			public void run() {
			    showAlertForNoSections();
			}
		    });

		} else {
		    _sectionSpinner.setItems(sectionListNew);

		}

	    }

	});

    }

    /**
     * Show alert for no sections.
     */
    private void showAlertForNoSections() {
	final AlertDialog.Builder alert = new Builder(
		TeahcerExamMultiScheduleActivity.this);
	alert.setIcon(R.drawable.warning)
	.setTitle("No Sections")
	.setMessage(
		"Test for all sections is already scheduled,No need to reschedule the test")
		.setCancelable(false)
		.setNegativeButton("OK ",
			new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog,
			    int which) {
			final Intent i = new Intent(
				TeahcerExamMultiScheduleActivity.this,
				TeacherExamsActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();

		    }
		}).create().show();
    }

    /**
     * Request to server for student list.
     *
     * @param url the url
     * @param Section the section
     */
    int requestToServerForStudentList(String url, String Section) {

	final StringParameter teacherIdParams = new StringParameter("teacherId",
		appData.getUserId());

	final StringParameter gradeParams = new StringParameter("grade", grade);
	final StringParameter sectionParams = new StringParameter("sectionlist",
		Section);

	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherIdParams);
	params.add(gradeParams);
	params.add(sectionParams);

	final PostRequestHandler request = new PostRequestHandler(
		_serverRequests.getRequestURL(url, ""), params,
		new DownloadCallback() {

		    ObjectMapper objMapper = new ObjectMapper();

		    @Override
		    public void onSuccess(String downloadedString) {

			try {
			    response = objMapper.readValue(downloadedString,
				    BaseResponse.class);

			    if ((response.getStatus()
 				    .equals(StatusType.SUCCESS) && response
				    .getData().toString().trim().length()==0)
				    
				    || response.getStatus()
				    .equals(StatusType.FAILURE)) {
				Toast.makeText(getApplicationContext(),
					"No grades", Toast.LENGTH_LONG).show();
				_check.setVisibility(View.INVISIBLE);
				_uncheck.setVisibility(View.INVISIBLE);
			    }
				
				if(!filterStudentsFlag) {
				    if (response.getData() != null) {
					studentListfromParser = StudentDetailsParser
						.getStudentsList(response.getData().toString());
					/*studentsIds="";
					for (Student iterartor : studentListfromParser) {
					    studentsIds += iterartor.getStudentId()
						    + ",";
				    }*/
					selectedStudents.addAll(studentListfromParser);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							validateSubmission();							
						}
					});
				    }
				
				    
				}else {
				    runOnUiThread(new Runnable() {
					@Override
					public void run() {
					    getStudentListFromResponse();	
					}
				    });
				}
			} catch (final Exception e) {
			    Logger.error(tag, e);
			}

		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {

		    }

		    @Override
		    public void onFailure(String failureMessage) {

		    }
		});
	request.request();
	return 100;
    }

    /**
     * Gets the student list from response.
     *
     * @return the student list from response
     */
    synchronized void getStudentListFromResponse() {


	try {

	    if (response.getData() != null) {
		studentListfromParser = StudentDetailsParser
			.getStudentsList(response.getData().toString());
	    }
	} catch (final Exception e) {
	    Logger.error(tag, e);
	}
	if (studentListfromParser.size() > 0) {
	//	setNewQuestionPaperData();
	    handler.post(new Runnable() {

		@Override
		public void run() {
		    _check.setVisibility(View.VISIBLE);
		    _uncheck.setVisibility(View.GONE);
		    _submit.setVisibility(View.VISIBLE);

		    adptt = new ImageAdapter(TeahcerExamMultiScheduleActivity.this,
			    studentListfromParser);
		    adptt.notifyDataSetChanged();
		    grid.setVisibility(View.VISIBLE);
		    grid.setAdapter(adptt);
		}
	    });

	} else {
		 _check.setVisibility(View.GONE);
		    _uncheck.setVisibility(View.GONE);
		    //_submit.setVisibility(View.VISIBLE);

		    
		    grid.setVisibility(View.GONE);
		grid.setAdapter(null);
	    Toast.makeText(TeahcerExamMultiScheduleActivity.this ,
		    "There are no students for this section. Please select another section",
		    Toast.LENGTH_SHORT).show();
	}


    }

    /**
     * Bind events.
     */
    public void bindEvents() {
    	/*grid.setAdapter(adptt);
    	adptt.notifyDataSetChanged();
*/
	_check.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (_check.isChecked()) {
		    //_uncheck.setChecked(false);
		    studentselection = true;
		    grid.setAdapter(adptt);
		} else {
		    /*_uncheck.setChecked(false);
		    _check.setChecked(true);*/
		    studentselection = false;
		    adptt = new ImageAdapter(getApplicationContext(), studentListfromParser);
		    grid.setAdapter(adptt);
		}
	    }
	});


	_uncheck.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (_uncheck.isChecked()) {
		    _check.setChecked(false);
		    studentselection = false;
		    grid.setAdapter(adptt);
		} else {
		    _check.setChecked(false);
		    _uncheck.setChecked(true);
		    studentselection = false;
		    grid.setAdapter(adptt);
		}
	    }
	});

    }

    /**
     * The Class ImageAdapter.
     */
    private class ImageAdapter extends BaseAdapter {
	
	/** The m inflater. */
	private final LayoutInflater mInflater;
	
	/** The context. */
	private final Context context;
	
	/** The students list. */
	private final List<Student> studentsList;
	
	/** The check box state. */
	boolean[] checkBoxState;

	/**
	 * Instantiates a new image adapter.
	 *
	 * @param context the context
	 * @param studentsList the students list
	 */
	public ImageAdapter(Context context, List<Student> studentsList) {
	    this.context = context;
	    this.studentsList = studentsList;
	    mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    checkBoxState = new boolean[studentsList.size()];
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
	    return studentsList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
	    return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
	    return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	synchronized public View getView(final int position, View convertView,
		ViewGroup parent) {
	    ViewHolder holder;

	    if (convertView == null) {
		holder = new ViewHolder();

		selectedStudents = new ArrayList<Student>();
		convertView = new View(context);
		convertView = mInflater.inflate(
			R.layout.create_test_step_3_list_row, null);
		holder.checkbox = (CheckBox) convertView
			.findViewById(R.id.studentcheckBox);
		convertView.setTag(holder);
	    } else {
		holder = (ViewHolder) convertView.getTag();
	    }
	    holder.checkbox.setId(position);
	    holder.checkbox.setText(studentsList.get(position).getFirstName());

	    if (_check.isChecked()) {
		holder.checkbox.setChecked(studentselection);
		selectedStudents.addAll(studentsList);
		checkBoxState[position] = false;
	    } /*else if (_uncheck.isChecked()) {
		holder.checkbox.setChecked(studentselection);
		checkBoxState[position] = false;
	    } */ else 
	    {
				holder.checkbox.setChecked(checkBoxState[position]);
			}

	    holder.checkbox.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			filterStudentsFlag=false;
		    _check.setChecked(false);
		   // _uncheck.setChecked(false);
		    if (((CheckBox) v).isChecked()) {
			checkBoxState[position] = true;
			selectedStudents.add(studentsList.get(position));
		    } else {
			selectedStudents.remove(studentsList.get(position));
			checkBoxState[position] = false;
		    }
		}
	    });
	    holder.id = position;

	    handler.removeMessages(0);
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



    /**
     * Reset fields.
     */
    private void resetFields() {
	_testName.setText("");
	_testName.setHint(R.string.underconstruction_test_name);
	_duration.setText("");
	_duration.setHint(R.string.step1_duration);
	_endDate.setText("");
	_endDate.setHint(R.string.step1_to_date_label);
	_startDate.setText("");
	_startDate.setHint(R.string.step1_from_date_label);
	final List<String> empty = new ArrayList<String>();
	empty.add("");
	_sectionSpinner.resetSpinner();
	/*if(!adptt.isEmpty())
	    adptt.notifyDataSetChanged();*/
	selectedStudents = new ArrayList<Student>();
	sections = "";
	grid.setVisibility(View.GONE);
	_check.setVisibility(View.GONE);
	_uncheck.setVisibility(View.GONE);
	if(_check.isChecked())
		_check.setChecked(true);
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if(keyCode==KeyEvent.KEYCODE_BACK) {
	    Toast.makeText(TeahcerExamMultiScheduleActivity.this, "Invalid action.Please submit the test", Toast.LENGTH_LONG).show();
	    return true;
	}else
	    return super.onKeyDown(keyCode, event);

    }

	/**
	 * Validate submission.
	 *
	 * @return the string
	 */
	private void validateSubmission() {
		String message = "";
		if (_testName.getText().toString().trim().length() == 0
				|| _startDate.getText().toString().trim().length() == 0
				|| _endDate.getText().toString().trim().length() == 0
				|| _duration.getText().toString().trim().length() == 0
				//|| filterStudentsFlag 
				|| selectedStudents.size() < 1
				|| sections.trim().length() == 0){
    		//String message = validateSubmission();
			if (_testName.getText().toString().trim().length() == 0) {
				_testName.setHint("Enter test name");
				_testName.setHintTextColor(Color.parseColor("#FF5050"));
				message = "Test Name";
				int n = 0;
			}
			if (_startDate.getText().toString().isEmpty()) {
				_startDate.setHint("Set start date");
				_startDate.setHintTextColor(Color.parseColor("#FF5050"));
				if (message.length() > 0)
					message = message + ", " + "Start date";
				else
					message = "Start date";
			}
			if (_endDate.getText().toString().isEmpty()) {
				_endDate.setHint("Set end date");
				_endDate.setHintTextColor(Color.parseColor("#FF5050"));
				if (message.length() > 0)
					message = message + ", " + "End date";
				else
					message = "End date";
			}
			/*if (filterStudentsFlag ) {
				if (message.length() > 0)
					message = message + ", " + "Student list";
				else
					message = "Student list ";
			}*/
			if (filterStudentsFlag && selectedStudents.size() < 1) {
		
					//message = "No Student Available ";
				if (message.length() > 0)
					message = message + ", " + "Student list";
				else
					message = "Student list ";
			}
			if (_duration.getText().toString().isEmpty()) {
				_duration.setHint("Set duration");
				_duration.setHintTextColor(Color.parseColor("#FF5050"));
				if (message.length() > 0)
					message = message + ", " + "Duration";
				else
					message = "Duration";
			}
			
			if(sections.isEmpty()) {
			    if (message.length() > 0)
				message = message + ", " + "Section";
			else
				message = "Section";
			}
			if (message.length() > 1) {
				message = message
						+ " can't be empty to submit the test";
			}
    		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    	    
    	}else {
    	    setNewQuestionPaperData();
    	    alertSubmit();    	    
    	}
		
		//return message;
	}
	
	/**
	 * Gets the sections list.
	 *
	 * @return the sections list
	 */
	private boolean getSectionsList() {
	    sections = "";
	    if (_sectionSpinner != null && _sectionSpinner.getSelectedStrings() != null && !_sectionSpinner.getSelectedStrings().isEmpty()) {
		for (final String string : _sectionSpinner.getSelectedStrings()) {
		    sections += string + ",";
		    sectionListOld.add(string);
		}
		if (sectionListNew.isEmpty()) {
		    grid.setVisibility(View.GONE);
		    grid.setAdapter(null);
		    adptt.notifyDataSetChanged();
		    Toast.makeText(TeahcerExamMultiScheduleActivity.this,
			    "No sections to display", Toast.LENGTH_LONG).show();
		}
		if (sections != null && sections.toString() != null) {
		    if (sections.endsWith(",")) {
			sections = sections.substring(0, sections.length() - 1);
		    }
		    requestToServerForStudentList(
			    ServerRequests.TEACHER_STUDENT_LIST, sections);
		   
		}
		 return true;
	    }else {
	    	validateSubmission();
		return false;
	    }
	}
	
	 @Override
	    public boolean dispatchKeyEvent(KeyEvent event) {
	    	for(int i=0; i<_sectionSpinner.getSelectedStrings().size(); i++)
	    		Logger.info(tag, "MS - slected strings in dispatch event is:"+_sectionSpinner.getSelectedStrings().get(i));
	    	if(historySections.size() != 0 && _sectionSpinner.getSelectedStrings().size() != 0){
	    		List<String> combinedList = new ArrayList<String>();
	    		combinedList.addAll( historySections );
	    		combinedList.addAll( _sectionSpinner.getSelectedStrings() );
	    		if(historySections.size() > _sectionSpinner.getSelectedStrings().size()){
	    			combinedList.removeAll(_sectionSpinner.getSelectedStrings());
	    		}else
	    			combinedList.removeAll(historySections);
	            
	    		System.out.println(combinedList);
	    		if(combinedList.size() > 0 ){
	    			grid.setAdapter(null);
	    			filterStudentsFlag = false;
	    			_check.setVisibility(View.INVISIBLE);
	    			_uncheck.setVisibility(View.INVISIBLE);
	    		}
	    	}
	    	historySections = _sectionSpinner.getSelectedStrings();
	    	return super.dispatchKeyEvent(event);
	    }
}
