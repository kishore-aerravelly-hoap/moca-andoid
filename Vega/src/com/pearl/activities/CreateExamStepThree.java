package com.pearl.activities;

/**
 * @author sahitya pasnoor
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.android.ui.ShowProgressBar;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.chat.server.response.BaseResponse;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.StudentDetailsParser;
import com.pearl.parsers.json.TeacherExamParser;
import com.pearl.ui.helpers.examination.MultiSelectSpinner;
import com.pearl.ui.models.StatusType;
import com.pearl.ui.models.Student;
import com.pearl.user.teacher.examination.ServerExam;
import com.pearl.user.teacher.examination.ServerExamDetails;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateExamStepThree.
 */
public class CreateExamStepThree extends Activity {

    /** The _server exam details. */
    private ServerExamDetails _serverExamDetails = null;
    
    /** The subject_spinner. */
    private Spinner grade_spinner, subject_spinner;
    
    /** The section_spinner. */
    private MultiSelectSpinner section_spinner;
    
    /** The app data. */
    private ApplicationData appData;
    /** The help. */
	private ImageView help;

    /** The grade list. */
    private List<String> gradeList;
    
    /** The subject list. */
    private List<String> subjectList;
    
    /** The section list. */
    private List<String> sectionList;
    
    /** The data from response. */
    private List<List<String>> dataFromResponse;
    
    /** The _server requests. */
    private ServerRequests _serverRequests;
    
    /** The tag. */
    private final String tag = "CreateExamStepThree";
    
    /** The subject. */
    private String grade, subject;
    
    /** The students ids. */
    private String studentsIds;
    
    /** The handler. */
    private Handler handler;
    
    /** The response. */
    private BaseResponse response;
    
    /** The sections. */
    private String sections="",sectionsagain="";
    
    /** The uncheck. */
    private CheckBox check;
    
    /** The studentselection. */
    private boolean studentselection;
    
    /** The menu button. */
    private Button menuButton;
    
    /** The grid. */
    private GridView grid;
    
    /** The selected students. */
    private List<Student> selectedStudents;
    
    /** The test id. */
    private String testId = null;
    
    /** The adptt. */
    private ImageAdapter adptt;
    
    /** The filter students list. */
    private Button filterStudentsList;
    
    /** The progress bar. */
    private ProgressDialog progressBar;
    
    /** The result. */
    private int progressBarStatus = 0, result = 0;
    
    /** The submit. */
    private Button submit;
    
    /** The filter students flag. */
    private boolean flag = false, filterStudentsFlag = false;
    
    /** The student listfrom parser. */
    private List<Student> studentListfromParser = null;
    
    /** The activity context. */
    private Context activityContext;
    private int  i=0;
    
    private List<String> historySections;

    // private Handler progressBarHandler = new Handler();

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.create_test_step_three);
	appData = (ApplicationData) getApplication();
	_serverRequests = new ServerRequests(CreateExamStepThree.this);
	grade_spinner = (Spinner) findViewById(R.id.grade_list);
	subject_spinner = (Spinner) findViewById(R.id.subject_list);
	section_spinner = (com.pearl.ui.helpers.examination.MultiSelectSpinner) findViewById(R.id.section_list);
	help = (ImageView) findViewById(R.id.create_test_step3_help);
	check = (CheckBox) findViewById(R.id.stepthree_check_all);
	//uncheck = (CheckBox) findViewById(R.id.stepthree_uncheck_all);
	grid = (GridView) findViewById(R.id.stepthree_student_list);
	submit = (Button) findViewById(R.id.submitStepThree);
	menuButton = (Button) findViewById(R.id.examMenu);
	filterStudentsList = (Button) findViewById(R.id.stepthree_filter_student_list);
	
	
	subject_spinner.setEnabled(false);
	section_spinner.setEnabled(false);
	final Bundle bundle = this.getIntent().getExtras();
	if (bundle != null)
	    testId = getIntent().getExtras().getString(VegaConstants.TEST_ID);
	_serverExamDetails = new ServerExamDetails();
	handler = new Handler();
	filterStudentsList.setVisibility(View.INVISIBLE);

	progressBar = new ProgressDialog(CreateExamStepThree.this);
	progressBar.setCancelable(true);
	progressBar.setMessage("Please wait ...");
	progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	progressBar.setProgress(0);
	progressBar.setMax(100);
	progressBar.show();
	activityContext = CreateExamStepThree.this;
	historySections = new ArrayList<String>();

	new Thread(new Runnable() {

	    @Override
	    public void run() {
		while (progressBarStatus < 100) {

		    progressBarStatus = requestToServerForGrades(ServerRequests.TEACHER_GRADE_AND_SUBJECT_LIST);
		    try {
			Thread.sleep(1000);
		    } catch (final InterruptedException e) {
			e.printStackTrace();
		    }
		    handler.post(new Runnable() {

			@Override
			public void run() {
			    progressBar.setProgress(progressBarStatus);
			}
		    });
		    if (progressBarStatus >= 100) {
			try {
			    Thread.sleep(2000);
			} catch (final InterruptedException e) {
			    e.printStackTrace();
			}

			progressBar.dismiss();
		    }

		}

	    }
	}).start();
	grade_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

	    @Override
	    public void onItemSelected(AdapterView<?> parent, View v,
		    int position, long id) {
		//((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
		grade = gradeList.get(position);
		ShowProgressBar.showProgressBar("Please wait downloading subjects",downloadSubjectList(_serverRequests.getRequestURL(ServerRequests.SUBJECT_LIST_GRADE_TEACHER, "")), CreateExamStepThree.this);
		//ShowProgressBar.showProgressBar("Please wait downloading sections",requestToServerForSectionList(	ServerRequests.TEACHER_SECTION_LIST, grade, subject), CreateExamStepThree.this);
		;
		grid.setAdapter(null);
		if(section_spinner != null){
			section_spinner.resetSpinner();			
		}
		check.setVisibility(View.INVISIBLE);
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {
		grade = gradeList.get(0);
		setVisisbility(View.GONE);
		//requestToServerForSectionList(	ServerRequests.TEACHER_SECTION_LIST, grade, subject);

	    }
	});

	subject_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

	    @Override
	    public void onItemSelected(AdapterView<?> parent, View v,
		    int position, long id) {
		// _serverExamDetails.setGrade(subjectList.get(position));
		//((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
		subject = subjectList.get(position);
		section_spinner.setEnabled(false);
		requestToServerForSectionList(	ServerRequests.TEACHER_SECTION_LIST, grade, subject);

	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {
		subject = subjectList.get(0);
		requestToServerForSectionList(ServerRequests.TEACHER_SECTION_LIST, grade, subject);
	//	setVisisbility(View.GONE);
	    }
	});
	
	filterStudentsList.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		
		final AppStatus status = new AppStatus();
		if (status.isOnline(activityContext)) {
			check.setChecked(false);
		    sections = "";
		    if (section_spinner != null
			    &&! section_spinner.getSelectedStrings().isEmpty()) {
			for (final String string : section_spinner
				.getSelectedStrings()) {
			    sections += string + ",";
			}
			if (sections != null && sections.toString() != null) {
				Logger.info(tag,
					    "sections status if shelf..." + sections);
			    flag = true;
			    if (sections.endsWith(",")) {
				sections = sections.substring(0,
					sections.length() - 1);
			    }
			    handler.post(new Runnable() {

				@Override
				public void run() {
				    filterStudentsFlag = true;
				    ShowProgressBar
					    .showProgressBar(
						    "fetching students list",
						    requestToServerForStudentList(
							    ServerRequests.TEACHER_STUDENT_LIST,
							    grade, sections),
						    CreateExamStepThree.this);

				}
			    });

			}
		    } else {
			Toast.makeText(CreateExamStepThree.this,
				"Please select the section", Toast.LENGTH_LONG)
				.show();
		    }
		} else {
		    Toast.makeText(activityContext,
			    "Please connect to internet in order to proceed",
			    Toast.LENGTH_LONG).show();
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

	submit.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
	    	if(!sections.equalsIgnoreCase(sectionsagain)){
	    		sections="";
	    		sectionsagain="";
	    		for (final String string : section_spinner
						.getSelectedStrings()) {
		    		sectionsagain += string + ",";
					}
		    	Logger.info(tag,
					    "sectionsagain1 status if shelf..." + sectionsagain);
	    		Logger.info(tag,
					    "sectionsagain2 status if shelf..." + sectionsagain);
	    		 handler.post(new Runnable() {

	 				@Override
	 				public void run() {
	 				    filterStudentsFlag = true;
	 				    ShowProgressBar
	 					    .showProgressBar(
	 						    "fetching students list for changed sections",
	 						    requestToServerForStudentList(
	 							    ServerRequests.TEACHER_STUDENT_LIST,
	 							    grade, sectionsagain),
	 						    CreateExamStepThree.this);

	 				}
	 			    });
	    		 sections=sectionsagain;
	    	}
	    			
		if (filterStudentsFlag) {
		    studentsIds = "";
		    if (selectedStudents != null && selectedStudents.size() > 0) {
			Toast.makeText(getBaseContext(),
				"Students are added to Test",
				Toast.LENGTH_SHORT).show();

			for (int size = 0; size < selectedStudents.size(); size++) {
			    studentsIds += selectedStudents.get(size)
				    .getStudentId() + ",";
			}
			setVisisbility(View.VISIBLE);
			Log.i(tag, "SERVER LIST IS " + studentsIds);
			if (writeStepThreeDataToFile() && flag) {
			    final Intent displayPaper = new Intent(
				    CreateExamStepThree.this,
				    CreateTestStep4.class);
			    displayPaper
				    .putExtra(VegaConstants.TEST_ID, testId);
			    startActivity(displayPaper);

			} else {
			    Toast.makeText(getApplicationContext(),
				    "Select all fields", Toast.LENGTH_LONG)
				    .show();
			}
		    } else {
			Toast.makeText(getBaseContext(),
				"Select at least one student",
				Toast.LENGTH_SHORT).show();
		    }
		} else {
		    sendData();

		}
	    }
	});

	section_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

	    @Override
	    public void onItemSelected(AdapterView<?> arg0, View arg1,
		    int arg2, long arg3) {
		((TextView) arg0.getChildAt(arg2)).setTextColor(Color.BLACK);
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {
		((TextView) arg0.getChildAt(0)).setTextColor(Color.BLACK);
	    }
	});
	help.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			showDialog();
		}
	});
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
	super.onResume();
	if (!appData.isLoginStatus()) {

	    final Intent login = new Intent(this, LoginActivity.class);
	    startActivity(login);
	    Logger.info(tag,
		    "LOGIN status if shelf..." + appData.isLoginStatus());
	    finish();

	}
    }

    /**
     * Request to server for grades.
     *
     * @param url the url
     * @return the int
     */
    int requestToServerForGrades(String url) {

	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());

	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);

	final PostRequestHandler request = new PostRequestHandler(
		_serverRequests.getRequestURL(url, ""), params,
		new DownloadCallback() {

		    ObjectMapper objMapper = new ObjectMapper();

		    @Override
		    public void onSuccess(String downloadedString) {

			try {
			    response = objMapper.readValue(downloadedString,
				    BaseResponse.class);

			    if (response.getStatus().equals(StatusType.FAILURE)) {
				Toast.makeText(getApplicationContext(),
					"No grades", Toast.LENGTH_LONG).show();
			    }
			    getGradeAndSubjectFromResponse();
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
     * Request to server for section list.
     *
     * @param url the url
     * @param grade the grade
     * @param subject the subject
     * @return the int
     */
    int requestToServerForSectionList(String url, String grade, String subject) {

	final StringParameter teacherIdParam = new StringParameter("teacherId",
		appData.getUserId());
	final StringParameter gradeParam = new StringParameter("grade", grade);
	final StringParameter subjectParam = new StringParameter("subject",
		subject);
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherIdParam);
	params.add(gradeParam);
	params.add(subjectParam);

	final PostRequestHandler request = new PostRequestHandler(
		_serverRequests.getRequestURL(url, ""), params,
		new DownloadCallback() {
		    ObjectMapper objMapper = new ObjectMapper();

		    @Override
		    public void onSuccess(String downloadedString) {

			try {
			    response = objMapper.readValue(downloadedString,
				    BaseResponse.class);

			    if (response.getStatus().equals(StatusType.FAILURE)) {
				Toast.makeText(getApplicationContext(),
					"No grades", Toast.LENGTH_LONG).show();
				ShowProgressBar.progressBar.dismiss();
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
	return 100;
    }

    /**
     * Request to server for student list.
     *
     * @param url the url
     * @param grade the grade
     * @param Section the section
     * @return the int
     */
    int requestToServerForStudentList(String url, String grade, String Section) {

	final StringParameter teacherIdParams = new StringParameter(
		"teacherId", appData.getUserId());

	final StringParameter gradeParams = new StringParameter("grade", grade);
	final StringParameter sectionParams = new StringParameter(
		"sectionlist", Section);

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

			    if (response.getStatus().equals(StatusType.SUCCESS)
				    && response.getData().toString().isEmpty()
				    || response.getStatus().equals(
					    StatusType.FAILURE)) {
				Toast.makeText(getApplicationContext(),
					"No grades", Toast.LENGTH_LONG).show();
				check.setVisibility(View.INVISIBLE);
				//uncheck.setVisibility(View.INVISIBLE);
			    }
			    studentListfromParser = StudentDetailsParser
				    .getStudentsList(response.getData()
					    .toString());
			    if (!filterStudentsFlag) {
				studentsIds="";
				if(studentListfromParser.isEmpty()){
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							 ShowProgressBar.progressBar.dismiss();
							Toast.makeText(getApplicationContext(), "No students in this section. Please select another section to proceed", Toast.LENGTH_SHORT).show();
							
						}
					});
				}else{
					for (Student iterartor : studentListfromParser) {
						studentsIds += iterartor.getStudentId()
								+ ",";
					}

					if (writeStepThreeDataToFile() && flag) {
						 ShowProgressBar.progressBar.dismiss();
						final Intent displayPaper = new Intent(
								CreateExamStepThree.this,
								CreateTestStep4.class);
						displayPaper.putExtra(
								VegaConstants.TEST_ID, testId);
						startActivity(displayPaper);
					}
					else{
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								 ShowProgressBar.progressBar.dismiss();
								Toast.makeText(getApplicationContext(), "Unexpected error,please try submitting the test again...", Toast.LENGTH_SHORT).show();
								
							}
						});
						 
					}
				}
			    } else{
			    	runOnUiThread(new Runnable() {
						@Override
						public void run() {
							getStudentListFromResponse();
							ShowProgressBar.progressBar.dismiss();
						}
					});
			    }
			} catch (final Exception e) {
			    Logger.error(tag, e);
			}

		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
			result = progressPercentage;
		    }

		    @Override
		    public void onFailure(String failureMessage) {
		    	ShowProgressBar.progressBar.dismiss();
		    }
		});
	request.request();
	return result;
    }

    /**
     * Gets the grade and subject from response.
     *
     * @return the grade and subject from response
     */
    @SuppressWarnings("unchecked")
    void getGradeAndSubjectFromResponse() {
	dataFromResponse = (List<List<String>>) response.getData();
	gradeList = dataFromResponse.get(0);
	//subjectList = dataFromResponse.get(1);
	Log.i(tag, "MY GRADE IS" + gradeList);
	setGrade();
    }

    /**
     * Sets the grade.
     */
    void setGrade() {
	handler.post(new Runnable() {

	    @Override
	    public void run() {
		final ArrayAdapter<String> gradeAdapter = new ArrayAdapter<String>(
			CreateExamStepThree.this,
			R.layout.spinner_list_item, gradeList);
		gradeAdapter
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		grade_spinner.setAdapter(gradeAdapter);
		subject_spinner.setEnabled(true);
		/*final ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(
			CreateExamStepThree.this,
			android.R.layout.simple_spinner_item, subjectList);
		subjectAdapter
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		subject_spinner.setAdapter(subjectAdapter);
		filterStudentsList.setVisibility(View.VISIBLE);*/

	    }
	});
    }

    /**
     * Gets the section list from response.
     *
     * @return the section list from response
     */
    @SuppressWarnings("unchecked")
    void getSectionListFromResponse() {
	sectionList = (List<String>) response.getData();
	setSectionList();

    }

    /**
     * Sets the section list.
     */
    void setSectionList() {
	handler.post(new Runnable() {

	    @Override
	    public void run() {
		section_spinner.setItems(sectionList);
		section_spinner.setEnabled(true);
		subject_spinner.setEnabled(true);
		if(ShowProgressBar.progressBar != null)
			ShowProgressBar.progressBar.dismiss();
	    }
	});
    }

    /**
     * Gets the student list from response.
     *
     * @return the student list from response
     */
    void getStudentListFromResponse() {

	try {

	    if (response.getData() != null) {
		grid.setVisibility(View.VISIBLE);
		studentListfromParser = StudentDetailsParser
			.getStudentsList(response.getData().toString());
	    }
	} catch (final Exception e) {
	    Logger.error(tag, e);
	}
	if (!filterStudentsFlag) {

	} else {
		if(studentListfromParser.isEmpty()){
			Toast.makeText(getApplicationContext(),
					"No Sections alloted",
					Toast.LENGTH_SHORT).show();
		grid.setAdapter(null);
		//adptt.notifyDataSetChanged();
		}
	    adptt = new ImageAdapter(CreateExamStepThree.this,
		    studentListfromParser);

	    handler.post(new Runnable() {

		@Override
		public void run() {
		    if (studentListfromParser.size() > 0) {
			check.setVisibility(View.VISIBLE);
			//uncheck.setVisibility(View.VISIBLE);
			submit.setVisibility(View.VISIBLE);
			bindEvents();
		    } else {
			Toast.makeText(getApplicationContext(),
				"Please select section in order to proceed",
				Toast.LENGTH_SHORT).show();
		    }
		}
	    });
	}

    }

    /**
     * Send data.
     */
    public void sendData() {

	submit.setVisibility(View.VISIBLE);
	if (AppStatus.getInstance(activityContext).isOnline(activityContext)) {
	    sections = "";
	    sectionsagain="";
	    if (section_spinner != null
		    && section_spinner.getSelectedStrings() != null && !section_spinner.getSelectedStrings().isEmpty()) {
		for (final String string : section_spinner.getSelectedStrings()) {
		    sections += string + ",";
		}
		if (sections != null && sections.toString() != null) {
		    flag = true;
		    if (sections.endsWith(",")) {
			sections = sections.substring(0, sections.length() - 1);
		    }
		    
		    handler.post(new Runnable() {

			@Override
			public void run() {
			    ShowProgressBar
				    .showProgressBar(
					    "Fetching students list.....",
					    requestToServerForStudentList(
						    ServerRequests.TEACHER_STUDENT_LIST,
						    grade, sections),
					    CreateExamStepThree.this);
			}
		    });

		}
	    } else {
		Toast.makeText(CreateExamStepThree.this,
			"Please select the section", Toast.LENGTH_LONG).show();
	    }
	} else {
	    Toast.makeText(activityContext,
		    "Please connect to internet in order to proceed",
		    Toast.LENGTH_LONG).show();
	}
	
	sectionsagain=sections;
	
    }

    /**
     * Bind events.
     */
    public void bindEvents() {
	grid.setAdapter(adptt);
	adptt.notifyDataSetChanged();

	check.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (check.isChecked()) {
		    //uncheck.setChecked(false);
		    studentselection = true;
		    grid.setAdapter(adptt);
		} else {
		    //uncheck.setChecked(false);
		   // check.setChecked(true);
		    studentselection = false;
		    adptt = new ImageAdapter(getApplicationContext(), studentListfromParser);
		    grid.setAdapter(adptt);
		}
	    }
	});

	/*uncheck.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (uncheck.isChecked()) {
		    check.setChecked(false);
		    studentselection = false;
		    grid.setAdapter(adptt);
		} else {
		    check.setChecked(false);
		    uncheck.setChecked(true);
		    studentselection = false;
		    grid.setAdapter(adptt);;
		}
	    }
	});*/


    }
    /**
	 * Show dialog.
	 */
	private void showDialog() {
		i = 0;
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pearl_tips_layout);
		RelativeLayout layout = (RelativeLayout) dialog
				.findViewById(R.id.tips_layout);
		final TextView tips = (TextView) dialog.findViewById(R.id.tips);
		ImageView previous = (ImageView) dialog.findViewById(R.id.previous);
		ImageView next = (ImageView) dialog.findViewById(R.id.next);
		layout.setBackgroundResource(R.drawable.attendance_help);
		final List<String> list = HelpParser.getHelpContent(
				"create_test_assign_students.txt", this);
		if (list != null && list.size() > 0) {
			tips.setText(list.get(0));
		}
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list != null && list.size() > 0) {
					if (i < (list.size() - 1)) {
						i = i + 1;
						tips.setEnabled(true);
						tips.setText(list.get(i));
					} else
						tips.setEnabled(false);
				}
			}
		});
		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list != null && list.size() > 0) {
					if (i > 0) {
						i = i - 1;
						tips.setEnabled(true);
						tips.setText(list.get(i));
					} else
						tips.setEnabled(false);
				}
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

    }

    /**
     * Retrive step two file and update.
     *
     * @return the server exam
     */
    ServerExam retriveStepTwoFileAndUpdate() {
	String stepTwoContent = null;
	ServerExam _serverExam = new ServerExam();
	if (ApplicationData.isFileExists(appData.getStep2FileName(testId))) {
	    try {
		stepTwoContent = ApplicationData.readFile(appData
			.getStep2FileName(testId));
	    } catch (final Exception e) {
		Logger.error(tag, e);
	    }
	    _serverExam = TeacherExamParser.getExamFromString(stepTwoContent);
	    Logger.info(tag, "step2 content is:" + stepTwoContent);
	    _serverExam = TeacherExamParser
		    .parseJsonToSeverExamObject(stepTwoContent);
	    Logger.warn(tag, "server exam object after parsing is:"
		    + _serverExam);
	    _serverExam = setStepThreeData(_serverExam);
	} else {
	    Log.i(tag,
		    "ERROR OCCURRED While finding file from Step TWO TO THREE");
	}
	return _serverExam;
    }

    /**
     * Sets the step three data.
     *
     * @param exam the exam
     * @return the server exam
     */
    public ServerExam setStepThreeData(ServerExam exam) {
	((ServerExamDetails) exam.getExam().getExamDetails()).setGrade(grade);
	((ServerExamDetails) exam.getExam().getExamDetails())
		.setSubject(subject);
	((ServerExamDetails) exam.getExam().getExamDetails())
		.setSection(sections);
	((ServerExamDetails) exam.getExam().getExamDetails())
		.setStudentId(studentsIds);
	((ServerExamDetails) exam.getExam().getExamDetails())
		.setSectionList(section_spinner.getSelectedStrings());
	return exam;
    }

    /**
     * Write step three data to file.
     *
     * @return true, if successful
     */
    boolean writeStepThreeDataToFile() {
	boolean write_flag = false;
	final ObjectMapper _objMapper = new ObjectMapper();
	String _stepFourServerExamContent = null;

	try {
	    _stepFourServerExamContent = _objMapper
		    .writeValueAsString(retriveStepTwoFileAndUpdate());
	    ApplicationData.writeToFile(_stepFourServerExamContent,
		    appData.getStep4FilePath(testId)
			    + ApplicationData.STEP4_FILE_NAME);
	    write_flag = true;
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
	return write_flag;
    }

    /**
     * Sets the visisbility.
     *
     * @param visibility the new visisbility
     */
    private void setVisisbility(int visibility) {
	submit.setVisibility(visibility);
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
	    selectedStudents = new ArrayList<Student>();
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
	public View getView(final int position, View convertView,
		ViewGroup parent) {
	    ViewHolder holder;

	    if (convertView == null) {
		holder = new ViewHolder();

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

	    if (check.isChecked()) {
	    	selectedStudents = new ArrayList<Student>();
		holder.checkbox.setChecked(studentselection);
		selectedStudents.addAll(studentsList);
		checkBoxState[position] = true;
		for(int i=0; i<studentsList.size(); i++)
			checkBoxState[i] = true;
	    } /*else if (check.isChecked()) {
		holder.checkbox.setChecked(studentselection);
		checkBoxState[position] = true;
	    }*/
	    else {
	  // checkBoxState[position] = false;
	   holder.checkbox.setChecked(checkBoxState[position]);
		
	    }

	    holder.checkbox.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		/*
		    check.setChecked(true);
		   // uncheck.setChecked(false);
		    if (((CheckBox) v).isChecked()) {
			checkBoxState[position] = false;
			selectedStudents.add(studentsList.get(position)); 
			//selectedStudents.remove(studentsList.get(position));
		    }else {
		    	if(check.isChecked()&&selectedStudents.size() != studentsList.size()){
			    	checkBoxState[position]=false;
			    	check.setChecked(false);
			    	selectedStudents.remove(studentsList.get(position));
		    	}
			
			//checkBoxState[position] = false;
			//check.setChecked(false);
		    }*/
		
			if (((CheckBox) v).isChecked()) {
				checkBoxState[position] = true;
				if (!selectedStudents.contains(studentsList
						.get(position)))
					selectedStudents.add(studentsList.get(
							position));
				if(!check.isChecked() && selectedStudents.size()>0 
						&& selectedStudents.size() == studentsList.size())
					check.setChecked(true);
			} else {
				
				checkBoxState[position] = false;
				if (selectedStudents.contains(studentsList
						.get(position)))
					selectedStudents.remove(studentsList.get(
							position));
				//selectedBooks.add(booksList.get(position));
				if(check.isChecked() && selectedStudents.size() != studentsList.size() 
						&& selectedStudents.size()>0){
					check.setChecked(false);
					check.setText("Check All");
					selectedStudents.remove(studentsList.get(position));
				}/*else if(!check.isChecked() && selectedStudents.size() == studentsList.size() 
						&& selectedStudents.size()>0){
					check.setChecked(true);
				}*/
			}}
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
    
    /**
     * Download subject list.
     *
     * @param url the url
     * @return the int
     */
    private int downloadSubjectList(String url){

		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("grade", grade);
		StringParameter teacherIdParam = new StringParameter("teacherId", appData.getUserId());
		Logger.warn(tag, "selected grade as param is:"+grade);
		params.add(teacherIdParam);
		params.add(gradeParam);
		Logger.warn(tag, "url is: "+url);
		PostRequestHandler requestHandler = new PostRequestHandler(url, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ObjectMapper objMapper = new ObjectMapper();
				try {
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);
				    ShowProgressBar.progressBar.dismiss();
				    if (response.getData() == null ||
				    		response.getStatus().equals(StatusType.FAILURE)) {
					Toast.makeText(getApplicationContext(),
						"No grades", Toast.LENGTH_LONG).show();
				    }
				    if(response != null && response.getData() != null){
				    	subjectList = (List<String>)response.getData();
				    	for(int i=0; i<subjectList.size(); i++){
				    		if(null == subjectList.get(i))
				    			subjectList.remove(i);
				    	}
				    	setSubjectListAdapter(subjectList);
				    } else if(response != null && response.getData() == null){
				    	subjectList = new ArrayList<String>();
				    	setSubjectListAdapter(subjectList);
				    }
				    subject_spinner.setOnItemSelectedListener(new onSubjectItemSelectedListener());
				} catch (final Exception e) {
				    Logger.error(tag, e);
				}
			}
			
			@Override
			public void onProgressUpdate(int progressPercentage) {
				Logger.warn(tag, "download in progress");
			}
			
			@Override
			public void onFailure(String failureMessage) {
				Logger.error(tag, "failed to download");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
					    ShowProgressBar.progressBar.dismiss();
						Toast.makeText(getApplicationContext(),"exam type"+R.string.Unable_to_reach_pearl_server, Toast.LENGTH_LONG).show();						
					}
				});
			}
		});
		requestHandler.request();
		return 100;
	}
    
    /**
     * Sets the subject list adapter.
     *
     * @param subjectsList the new subject list adapter
     */
    private void setSubjectListAdapter(List<String> subjectsList){
    	runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
		    	final ArrayAdapter<String> subjectAdapter = new ArrayAdapter<String>(
		    			CreateExamStepThree.this,
		    			R.layout.spinner_list_item, subjectList);
		    		subjectAdapter
		    			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    		subject_spinner.setAdapter(subjectAdapter);
		    		filterStudentsList.setVisibility(View.VISIBLE);				
			}
		});
    }
    
    /**
     * The listener interface for receiving onSubjectItemSelected events.
     * The class that is interested in processing a onSubjectItemSelected
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addonSubjectItemSelectedListener<code> method. When
     * the onSubjectItemSelected event occurs, that object's appropriate
     * method is invoked.
     *
     * @see onSubjectItemSelectedEvent
     */
    private class onSubjectItemSelectedListener implements OnItemSelectedListener{

	    /* (non-Javadoc)
    	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
    	 */
    	@Override
	    public void onItemSelected(AdapterView<?> parent, View v,
		    int position, long id) {
		// _serverExamDetails.setGrade(subjectList.get(position));
		((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
		subject = subjectList.get(position);
		section_spinner.setItems(new String[]{"Select"});
		section_spinner.setSelection(0);
		grid.setAdapter(null);
		check.setVisibility(View.INVISIBLE);
		if(section_spinner != null){
			section_spinner.resetSpinner();			
		}
		requestToServerForSectionList(	ServerRequests.TEACHER_SECTION_LIST, grade, subject);
	    }

	    /* (non-Javadoc)
    	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
    	 */
    	@Override
	    public void onNothingSelected(AdapterView<?> arg0) {
		subject = subjectList.get(0);
		requestToServerForSectionList(ServerRequests.TEACHER_SECTION_LIST, grade, subject);
	//	setVisisbility(View.GONE);
	    }
	}
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
    	if(historySections.size() != 0 && section_spinner.getSelectedStrings().size() != 0){
    		List<String> combinedList = new ArrayList<String>();
    		combinedList.addAll( historySections );
    		combinedList.addAll( section_spinner.getSelectedStrings() );
    		if(historySections.size() > section_spinner.getSelectedStrings().size()){
    			combinedList.removeAll(section_spinner.getSelectedStrings());
    		}else
    			combinedList.removeAll(historySections);
            
    		System.out.println(combinedList);
    		if(combinedList.size() > 0 ){
    			grid.setAdapter(null);
    			filterStudentsFlag = false;
    			check.setVisibility(View.INVISIBLE);
    		}
    	}
    	historySections = section_spinner.getSelectedStrings();
    	return super.dispatchKeyEvent(event);
    }
}
