package com.pearl.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.analytics.models.AndroidAnalytics;
import com.pearl.analytics.models.AndroidAnalyticsList;
import com.pearl.analytics.view.ComparativeStudyAcrossSectionsAnalytics;
import com.pearl.analytics.view.ComparativeStudyAcrossTestsAnalytics;
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
import com.pearl.parsers.json.SubjectPointsParser;
import com.pearl.ui.models.StatusType;
import com.pearl.ui.models.Student;

// TODO: Auto-generated Javadoc
/**
 * The Class Tempanalytics.
 */
public class Tempanalytics extends Activity{

	/** The _across sections. */
	private Button _acrossTests, getchart, _acrossSections;
	
	/** The _section two. */
	private Spinner _sectionOne,_grade,_testName,_subjectName, _sectionTwo; 
	
	/** The across sections pointer. */
	private ImageView acrossTestsPointer, acrossSectionsPointer;
	
	/** The tag. */
	private static String tag = "";
	
	/** The section two list adapter. */
	private ArrayAdapter<String> gradesListAdapter, subjectsListAdapter, studentsListAdapter, sectionOneListAdapter, testsListAdapter,
		sectionTwoListAdapter;
	
	/** The student listfrom parser. */
	private List<Student> studentListfromParser;
	
	/** The chart layout. */
	private RelativeLayout spinnersLayout,chartLayout;
	
	/** The app data. */
	private ApplicationData appData;
	
	/** The no data. */
	private TextView noData;
	
	/** The charts frame layout. */
	private FrameLayout chartsFrameLayout;
	
	/** The server requests. */
	private ServerRequests serverRequests;
	
	/** The tests list. */
	private List<String> gradeList, subjectList, sectionList, testsList;
	
	/** The android analytics. */
	private AndroidAnalytics androidAnalytics;
	
	/** The test list map. */
	private Map<String, String> testListMap;
	
	/** The twosec. */
	private String twosec;
	
	/** The return result. */
	private int noOfSections,returnResult;
	
	/** The selected section two. */
	private String selectedGrade, selectedSubject, selectedSection, chartType, selectedStudentId, selectedTest, selectedSectionTwo;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pearl_analytics_layout);
		tag = getClass().getSimpleName();
		appData = (ApplicationData)getApplication();
		_acrossSections = (Button)findViewById(R.id.analytics_across_subjects);
		_acrossTests = (Button)findViewById(R.id.analytics_across_tests);
		_grade = (Spinner)findViewById(R.id.analytics_grade_spinner);
		acrossTestsPointer = (ImageView) findViewById(R.id.across_test_highlight);
		acrossSectionsPointer = (ImageView) findViewById(R.id.across_section_highlight);
		_sectionOne = (Spinner)findViewById(R.id.analytics_section1_spinner);
		//_studentName = (Spinner)findViewById(R.id.analytics_student_name_spinner);
		_sectionTwo = (Spinner) findViewById(R.id.analytics_section2);
		_subjectName = (Spinner)findViewById(R.id.analytics_subject_spinner);
		_testName = (Spinner)findViewById(R.id.analytics_test_spinner);
		noData = (TextView) findViewById(R.id.empty_analytics);
		getchart = (Button) findViewById(R.id.get_chart);
		chartsFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		chartLayout = (RelativeLayout) findViewById(R.id.analytics);
		Toast.makeText(this, "Please click on the button above to view analytics", Toast.LENGTH_LONG).show();
		serverRequests = new ServerRequests(this);
		chartLayout.setEnabled(false);
		noOfSections = 2;
		spinnersLayout = (RelativeLayout) findViewById(R.id.relativeLayout3);
		spinnersLayout.setVisibility(View.INVISIBLE);
		_acrossSections.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				spinnersLayout.setVisibility(View.VISIBLE);
				_sectionTwo.setVisibility(View.VISIBLE);
				_testName.setVisibility(View.VISIBLE);
				_subjectName.setVisibility(View.VISIBLE);
				noData.setVisibility(View.GONE);
				//_studentName.setVisibility(View.VISIBLE);
				acrossSectionsPointer.setVisibility(View.VISIBLE);
				acrossTestsPointer.setVisibility(View.GONE);
				//_studentName.setVisibility(View.GONE);
				chartLayout.setVisibility(View.GONE);
				chartType = VegaConstants.COMPARATIVE_STUDY_ACCROSS_SECTIONS;
				String requestUrl = serverRequests.getRequestURL(ServerRequests.GRADE_LIST, "");
			ShowProgressBar.showProgressBar("Downloading data", 	getAnalyticsDetailsFromServer(appData.getUserId(), "", "", "", requestUrl, "grade subjetcs list"), Tempanalytics.this);
			}
		});
		
		/*_acrossSubjects.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				spinnersLayout.setVisibility(View.VISIBLE);
				//_sectionTwo.setVisibility(View.GONE);
				_testName.setVisibility(View.GONE);
				_subjectName.setVisibility(View.GONE);
				_studentName.setVisibility(View.VISIBLE);
				acrossSubjetcsPointer.setVisibility(View.VISIBLE);
				acrossTestsPointer.setVisibility(View.GONE);
				chartLayout.setVisibility(View.GONE);
				chartType = VegaConstants.COMPARATIVE_STUDY_ACCROSS_SUBJECTS;
				String requestUrl = serverRequests.getRequestURL(ServerRequests.GRADE_LIST, "");
				getAnalyticsDetailsFromServer(appData.getUserId(), "", "", "", requestUrl, "grade subjetcs list");
			}
		});*/
		
		_acrossTests.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				spinnersLayout.setVisibility(View.VISIBLE);
				_sectionTwo.setVisibility(View.GONE);
				_subjectName.setVisibility(View.GONE);
				//_studentName.setVisibility(View.GONE);
				noData.setVisibility(View.GONE);
				_testName.setVisibility(View.VISIBLE);
				acrossSectionsPointer.setVisibility(View.GONE);
				acrossTestsPointer.setVisibility(View.VISIBLE);
				chartLayout.setVisibility(View.GONE);
				chartType = VegaConstants.COMPARATIVE_STUDY_ACCROSS_TESTS;
				String requestUrl = serverRequests.getRequestURL(ServerRequests.GRADE_LIST, "");
				ShowProgressBar.showProgressBar("Downloading data", getAnalyticsDetailsFromServer(appData.getUserId(), "", "", "", requestUrl, "grade subjetcs list"), Tempanalytics.this);
			}
		});
		
		_grade.setOnItemSelectedListener(new OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
		    	Logger.warn(tag, "grade spinner selected");
		    	_testName.setEnabled(false);
		    	_sectionOne.setEnabled(false);
		    	_sectionTwo.setEnabled(false);
		    	_subjectName.setEnabled(false);
				selectedGrade = _grade.getSelectedItem().toString();
				String url = serverRequests.getRequestURL(ServerRequests.SECTION_LIST_FOR_GRADE, "");
				getAnalyticsDetailsFromServer(appData.getUserId(), selectedGrade, "", "", url, "sections list");
				if(gradeList.size() == 0)
					Logger.warn(tag, "no grades");
				//getSectionListFromServer(url, grade, "");
			}

		    @Override
		    public void onNothingSelected(AdapterView<?> arg0) {
		    	selectedGrade = gradeList.get(0);

		    }
		});
			
		_sectionOne.setOnItemSelectedListener(new OnItemSelectedListener() {
			
		    @Override
		    public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
		    	Logger.warn(tag, "section spinner selected"+_sectionOne.getSelectedItem().toString());
		    	_testName.setEnabled(false);
				selectedSection = _sectionOne.getSelectedItem().toString();
				if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_SUBJECTS)){
					String url = serverRequests.getRequestURL(ServerRequests.TEACHER_STUDENT_LIST, "");
					getAnalyticsDetailsFromServer(appData.getUserId(), selectedGrade, "", selectedSection, url, "students list");
				}else if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_TESTS)){
					String requestUrl = serverRequests.getRequestURL(ServerRequests.ANALYTICS_TESTS_FOR_GRADE_SECTION, "");
					getAnalyticsDetailsFromServer(appData.getUserId(), selectedGrade, "", selectedSection, requestUrl, "tests list");
				}else if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_SECTIONS)){
				
				    
				    /*List<String> section2 = sectionList;
					sectionTwoListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, section2);
					sectionTwoListAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
					_sectionTwo.setAdapter(sectionTwoListAdapter);*/
				}
			}

		    @Override
		    public void onNothingSelected(AdapterView<?> arg0) {
		    	Logger.warn(tag, "section one nothing selected"+sectionList.get(0));
		    	selectedSection = sectionList.get(0);
		    }
		});
		
		_sectionTwo.setOnItemSelectedListener(new OnItemSelectedListener() {
			
		    @Override
		    public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
		    	_testName.setEnabled(false);
			selectedSectionTwo = _sectionTwo.getSelectedItem().toString();
			if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_SECTIONS)){
				String url = serverRequests.getRequestURL(ServerRequests.SUBJECTS_FOR_GRADE_SECTION, "");
				getAnalyticsDetailsFromServer(appData.getUserId(), selectedGrade, "", selectedSection, url, "subject list");
			}else if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_TESTS)){
				String requestUrl = serverRequests.getRequestURL(ServerRequests.ANALYTICS_TESTS_FOR_GRADE_SECTION, "");
				getAnalyticsDetailsFromServer(appData.getUserId(), selectedGrade, "", selectedSection, requestUrl, "tests list");
			}
			}

		    @Override
		    public void onNothingSelected(AdapterView<?> arg0) {
			if(sectionList.size()>1)
			    selectedSectionTwo = sectionList.get(1);
			    	else
			    	_sectionTwo.setEnabled(false);
		    	//selectedSectionTwo = sectionList.get(1);
		    }
		});
		
		_subjectName.setOnItemSelectedListener(new OnItemSelectedListener() {
			
		    @Override
		    public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
		    	_testName.setEnabled(false);
			selectedSubject = _subjectName.getSelectedItem().toString();
			Logger.warn(tag, "concatenated section is:"+selectedSection + ", "+ _sectionTwo.getSelectedItem().toString());
			twosec = selectedSection +","+ _sectionTwo.getSelectedItem().toString();
			if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_SECTIONS)){
				String url = serverRequests.getRequestURL(ServerRequests.COMMON_TEST_FOR_DIFFERENT_SECTIONS, "");
				getAnalyticsDetailsFromServer(appData.getUserId(), selectedGrade, selectedSubject, twosec, url, "tests list");
			}else if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_TESTS)){
				String requestUrl = serverRequests.getRequestURL(ServerRequests.ANALYTICS_TESTS_FOR_GRADE_SECTION, "");
				getAnalyticsDetailsFromServer(appData.getUserId(), selectedGrade, "", selectedSection, requestUrl, "tests list");
			}
			}

		    @Override
		    public void onNothingSelected(AdapterView<?> arg0) {
		    	selectedSection = sectionList.get(0);
		    }
		});
		
		_testName.setOnItemSelectedListener(new OnItemSelectedListener() {

		    @Override
		    public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
		    	List<String> key = new ArrayList<String>(testListMap.keySet());
		    	selectedTest = key.get(position);
		    	if(testsList != null && testsList.size() == 0)
					Logger.warn(tag, "no tests");
			}
		    @Override
		    public void onNothingSelected(AdapterView<?> ag0) {
		    	List<String> key = new ArrayList<String>(testListMap.keySet());
		    	selectedTest = key.get(0);
		    }
		});
		
		getchart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    
				if(checkForEmptyFields()){
					Toast.makeText(getApplicationContext(), "Make sure all fields are entered", Toast.LENGTH_SHORT).show();
				}else{
				String url = "";
				if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_TESTS)){
					url = serverRequests.getRequestURL(ServerRequests.ANALYTICS_ACROSS_TESTS, "");
					getAnalyticsAcrossTests(url);
				}else if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_SECTIONS)){
						url = serverRequests.getRequestURL(ServerRequests.ANALYTICS_ACROSS_SECTIONS, "");
						//Will be using the same request of 1st chart 
						getAnalyticsAcrossTests(url);
					}
				  }
				}
		});
	}
	
	/**
	 * gets grade and section details from server.
	 *
	 * @param url the url
	 * @return the grade subject details from server
	 */
	private void getGradeSubjectDetailsFromServer(String url){
		StringParameter idParam = new StringParameter("teacherId", appData.getUserId());
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		params.add(idParam);
		PostRequestHandler requestHandler = new PostRequestHandler(url, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ObjectMapper objMapper = new ObjectMapper();

				try {
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);

				    if (response.getData() == null ||
				    		response.getStatus().equals(StatusType.FAILURE)) {
					Toast.makeText(getApplicationContext(),
						"No grades", Toast.LENGTH_LONG).show();
				    }
				    getGradeSubjetcsList(response);
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
				Toast.makeText(getApplicationContext(),R.string.Unable_to_reach_pearl_server, Toast.LENGTH_LONG).show();
			}
		});
		requestHandler.request();
	}
	
	/**
	 * gets subjects list from server.
	 *
	 * @param url the url
	 * @param grade the grade
	 * @param subject the subject
	 * @return the section list from server
	 */
	private void getSectionListFromServer(String url, String grade, String subject){

		StringParameter idParam = new StringParameter("teacherId", appData.getUserId());
		StringParameter gradeParam = new StringParameter("grade", grade);
		StringParameter subjectParam = new StringParameter("subject", subject);
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		params.add(idParam);
		params.add(gradeParam);
		params.add(subjectParam);
		PostRequestHandler requestHandler = new PostRequestHandler(url, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ObjectMapper objMapper = new ObjectMapper();

				try {
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);

				    if (response.getData() == null ||
				    		response.getStatus().equals(StatusType.FAILURE)) {
					Toast.makeText(getApplicationContext(),
						"No grades", Toast.LENGTH_LONG).show();
				    }
				    getSectionsList(response);
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
			}
		});
		requestHandler.request();
	
	}
	
	/**
	 * gets students list from server.
	 *
	 * @param url the url
	 * @param grade the grade
	 * @param section the section
	 * @return the students list from server
	 */
	private void getStudentsListFromServer(String url, String grade, String section){


		StringParameter idParam = new StringParameter("teacherId", appData.getUserId());
		StringParameter gradeParam = new StringParameter("grade", grade);
		StringParameter sectionParam = new StringParameter("sectionlist", section);
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		params.add(idParam);
		params.add(gradeParam);
		params.add(sectionParam);
		PostRequestHandler requestHandler = new PostRequestHandler(url, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ObjectMapper objMapper = new ObjectMapper();

				try {
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);

				    if (response.getData() == null ||
				    		response.getStatus().equals(StatusType.FAILURE)) {
					Toast.makeText(getApplicationContext(),
						"No grades", Toast.LENGTH_LONG).show();
				    }
				   // getStudentsList(response);
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
			}
		});
		requestHandler.request();
	
	
	}
	
	/**
	 * gets the details that are required to generate the analyitcs.
	 *
	 * @param teacherId the teacher id
	 * @param grade the grade
	 * @param subject the subject
	 * @param section the section
	 * @param requestUrl the request url
	 * @param type the type
	 * @return the analytics details from server
	 */
	private int getAnalyticsDetailsFromServer(String teacherId, String grade,
			String subject, String section, String requestUrl, final String type){
	    returnResult=0;
		ArrayList<RequestParameter> params;
		if(type.equalsIgnoreCase(VegaConstants.GRADE_SUBJECTS_LIST)){
			params = new ArrayList<RequestParameter>();
		}else if(type.equalsIgnoreCase(VegaConstants.STUDENTS_LIST)){
			StringParameter idParam = new StringParameter("teacherId", teacherId);
			StringParameter gradeParam = new StringParameter("grade", grade);
			StringParameter sectionParam = new StringParameter("sectionlist", section);
			params = new ArrayList<RequestParameter>();
			params.add(idParam);
			params.add(gradeParam);
			params.add(sectionParam);
		}else if(type.equalsIgnoreCase(VegaConstants.SECTIONS_LIST)){
			StringParameter gradeParam = new StringParameter("gradeName", grade);
			params = new ArrayList<RequestParameter>();
			params.add(gradeParam);
		}else if(type.equalsIgnoreCase(VegaConstants.TESTS_LIST)){
			//gradeName,sectionName,userId
			Logger.warn(tag, "test grade:"+grade);
			Logger.warn(tag, "test section name:"+section);
			Logger.warn(tag, "test teaher Id:"+teacherId);
			StringParameter gradeParam = new StringParameter("gradeName", grade);
			StringParameter sectionParam = new StringParameter("sectionName", section);
			StringParameter userIdParam = new StringParameter("userId", teacherId);
			StringParameter subjectParam = new StringParameter("subjectName", subject);
			params = new ArrayList<RequestParameter>();
			params.add(gradeParam);
			params.add(sectionParam);
			params.add(userIdParam);
			params.add(subjectParam);
		}else if(type.equalsIgnoreCase(VegaConstants.SUBJECT_LIST)){
		        StringParameter gradeParam = new StringParameter("gradeName", grade);
			StringParameter sectionParam = new StringParameter("sectionName", section);
			params = new ArrayList<RequestParameter>();
			params.add(gradeParam);
			params.add(sectionParam);
		}else{
			Logger.error(tag, "mismatched type");
			params = new ArrayList<RequestParameter>();
		}
		Logger.warn(tag, "url for getting "+type+" is: "+requestUrl);
		PostRequestHandler requestHandler = new PostRequestHandler(requestUrl, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ObjectMapper objMapper = new ObjectMapper();

				try {
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);

				    if (response.getData() == null ||
				    		response.getStatus().equals(StatusType.FAILURE)) {
					Toast.makeText(getApplicationContext(),
						"No grades", Toast.LENGTH_LONG).show();
				    }
				    returnResult=100;
				
				    ShowProgressBar.progressBar.dismiss();
				    if(type.equalsIgnoreCase(VegaConstants.GRADE_SUBJECTS_LIST)){
				    	getGradeSubjetcsList(response);
					}/*else if(type.equalsIgnoreCase(VegaConstants.STUDENTS_LIST)){
						getStudentsList(response);
					}*/else if(type.equalsIgnoreCase(VegaConstants.SECTIONS_LIST)){
						getSectionsList(response);
					} else if(type.equalsIgnoreCase(VegaConstants.SUBJECT_LIST)){
						getSubjectList(response);
					}else if(type.equalsIgnoreCase(VegaConstants.TESTS_LIST)){
						getTestsList(response);
					}else{
						Logger.error(tag, "mismatched type");
					}
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
						Toast.makeText(getApplicationContext(),R.string.Unable_to_reach_pearl_server, Toast.LENGTH_LONG).show();						
					}
				});
			}
		});
		requestHandler.request();
		return 100;
	}
	
	
	/**
	 * gets analytics detials for tests.
	 *
	 * @param url the url
	 * @return the analytics across tests
	 */
	private void getAnalyticsAcrossTests(String url){
		StringParameter testIdParam = new StringParameter("testId", selectedTest);
		StringParameter gradeParam = new StringParameter("gradeName", selectedGrade);
		StringParameter sectionParam = null;
		if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_TESTS))
			sectionParam = new StringParameter("sectionName", selectedSection);
		else if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_SECTIONS))
			sectionParam = new StringParameter("sectionName", twosec);
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		params.add(testIdParam);
		params.add(gradeParam);
		params.add(sectionParam);
		PostRequestHandler postRequest = new PostRequestHandler(url, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				Logger.warn(tag, "download string is:"+downloadedString);
				ObjectMapper objMapper = new ObjectMapper();
				try {
					BaseResponse response = objMapper.readValue(downloadedString,
						    BaseResponse.class);
					Logger.warn(tag, "response for point is:"+response.getData());
					AndroidAnalyticsList analyticsList = new AndroidAnalyticsList();;
					if(response != null && response.getData() != null){
						String responseData = response.getData().toString();
						analyticsList = SubjectPointsParser.getAndroidAnalyticsFromList(responseData);
						Logger.warn(tag, "data "+responseData);
						for(int i=0; i<analyticsList.getAndroidAnalyitcsList().size(); i++){
							if(analyticsList.getAndroidAnalyitcsList().get(i).getSubjectPoints().size() == 0){
								setVisibilityForGraph();
							}else{
								if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_TESTS))
								generateAnalyticsAcrossTestsChart(analyticsList.getAndroidAnalyitcsList().get(0));
								
								else if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_SECTIONS))
									generateAnalyticsAcrossSections(analyticsList);
								
							}
						}
						
					}else{
						Logger.warn(tag, "data is null");
						setVisibilityForGraph();
					}
				} catch (JsonParseException e) {
					Logger.error(tag, e);
				} catch (JsonMappingException e) {
					Logger.error(tag, e);
				} catch (IOException e) {
					Logger.error(tag, e);
				}
			}
			
			@Override
			public void onProgressUpdate(int progressPercentage) {
				
			}
			
			@Override
			public void onFailure(String failureMessage) {
				Logger.error(tag, "failed to send request"+failureMessage);
			}
		});
		postRequest.request();
	
	}
	
	
	/**
	 * gets grades and subjects list from the response obtained.
	 *
	 * @param response the response
	 * @return the grade subjetcs list
	 */
	private void getGradeSubjetcsList(BaseResponse response){
		gradeList = (List<String>) response.getData();
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				setGradesListAdapter();
			}
		});
	}
	
	/**
	 * Retrieves students list from the response.
	 *
	 * @param response the response
	 * @return the sections list
	 */
	/*private void getStudentsList(BaseResponse response){
		try {
			if (response.getData() != null) {
				studentListfromParser = StudentDetailsParser
						.getStudentsList(response.getData().toString());
				final List<String> studentsList = new ArrayList<String>();
				for (Student student : studentListfromParser) {
					studentsList.add(student.getStudentFullName());
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						setStudentsListAdapter(studentsList);
					}
				});
			}
		} catch (final Exception e) {
		    Logger.error(tag, e);
		}
	}*/
	
	/**
	 * Retrieves sections lit
	 * @param response
	 */
	private void getSectionsList(BaseResponse response){
		sectionList = (List<String>) response.getData();
		Logger.warn(tag, "section list size is:"+sectionList.size());
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				setSectionListAdapter(sectionList);				
			}
		});
	}
	
	/**
	 * Gets the subject list.
	 *
	 * @param response the response
	 * @return the subject list
	 */
	private void getSubjectList(BaseResponse response) {
	    // TODO Auto-generated method stub
	    subjectList = (List<String>) response.getData();
		Logger.warn(tag, "subject list size is:"+subjectList.size());
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				setSubjectListAdapter(subjectList);				
			}
		});
	}
	
	/**
	 * gets list of tests for the grade and section.
	 *
	 * @param response the response
	 * @return the tests list
	 */
	private void getTestsList(BaseResponse response){
		testListMap = (Map<String, String>) response.getData();
		Collection<String> values = testListMap.values();
		final List<String> testList = new ArrayList<String>(values);
		Logger.warn(tag, "values...."+testList.size());
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				setTestsListAdapter(testList);
			}
		});
		/*Iterator itr = testListMap.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<String, String> entry = (Map.Entry<String, String>) itr.next();
			String key = entry.getKey();
			String value = entry.getValue();
			Logger.info(tag, "key value is:"+ key +", " + value);
		}
		
		
		List<String> keys = new ArrayList<String>(testListMap.keySet());
		Logger.info(tag, "keyyyyyyyyyyyyyyyyy"+keys.size());*/
		
	}
	
	/**
	 * genaretes required chart.
	 */
	private void generateAnalyticsAcrossSubjectsChart() {
		int count = 0;
		for(int i=0; i<androidAnalytics.getSubjectPoints().size(); i++){
			if(androidAnalytics.getSubjectPoints().get(0).getTotalMarks() == 0)
				count++;
		}
		if(count == androidAnalytics.getSubjectPoints().size()){
			chartsFrameLayout.setVisibility(View.GONE);
			chartLayout.setVisibility(View.GONE);
			noData.setVisibility(View.VISIBLE);
		}else{
			ComparativeStudyAcrossSectionsAnalytics piechart = new ComparativeStudyAcrossSectionsAnalytics();
			chartsFrameLayout.setVisibility(View.VISIBLE);
			chartLayout.setVisibility(View.VISIBLE);
			noData.setVisibility(View.GONE);
			//chartLayout.addView(piechart);
		}
		
	}
	
	/**
	 * sets adapter for grades and subjects.
	 */
	private void setGradesListAdapter(){
		Logger.warn(tag, "need to set the adapter");
		ArrayAdapter<String> gradesListAdapter = new ArrayAdapter<String>(Tempanalytics.this,
				android.R.layout.simple_spinner_item, gradeList);
		gradesListAdapter
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		if(gradeList.isEmpty())
			_grade.setEnabled(false);
		else{
			_grade.setEnabled(true);
    		_grade.setAdapter(gradesListAdapter);
		}
		//books.setOnItemSelectedListener(new BookListItemSelectListerner());
	}
	
	/**
	 * sets adapter for studentslist.
	 *
	 * @param testList the new tests list adapter
	 */
	/*private void setStudentsListAdapter(List<String> studentsList){
		studentsListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, studentsList);
		studentsListAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		_studentName.setAdapter(studentsListAdapter);
	}*/
	
	/**
	 * sets the adapter for tests list
	 */
	private void setTestsListAdapter(List<String> testList){
		testsListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, testList);
		testsListAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		if(testList.isEmpty())
			_testName.setEnabled(false);
		else{
			_testName.setEnabled(true);
			_testName.setAdapter(testsListAdapter);
		}
	}
	
	/**
	 * sets adapter for sections list.
	 *
	 * @param sectionList the new section list adapter
	 */
	private void setSectionListAdapter(List<String> sectionList){
	    sectionOneListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sectionList);
		sectionOneListAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		if(sectionList.isEmpty())
			_sectionOne.setEnabled(false);
		else{
			_sectionOne.setEnabled(true);
			_sectionOne.setAdapter(sectionOneListAdapter);
		}
		
		sectionTwoListAdapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sectionList);
		sectionTwoListAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		if(sectionList.isEmpty())
			_sectionTwo.setEnabled(false);
		else{
		    _sectionTwo.setEnabled(true);
		    _sectionTwo.setAdapter(sectionTwoListAdapter);
		}
	}
	
	/**
	 * Sets the subject list adapter.
	 *
	 * @param subjectList the new subject list adapter
	 */
	private void setSubjectListAdapter(List<String> subjectList){
		subjectsListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjectList);
		subjectsListAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		if(subjectList.isEmpty())
			_subjectName.setEnabled(false);
		else{
			_subjectName.setEnabled(true);
			_subjectName.setAdapter(subjectsListAdapter);
		}
	}
	
	/**
	 * generates tests analytics.
	 *
	 * @param analytics the analytics
	 * @throws JsonProcessingException the json processing exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void generateAnalyticsAcrossTestsChart(final AndroidAnalytics analytics) throws JsonProcessingException, IOException{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				noData.setVisibility(View.GONE);
				ComparativeStudyAcrossTestsAnalytics barChart = new ComparativeStudyAcrossTestsAnalytics();
				View barChartView = barChart.generateChart(getApplicationContext(),analytics);
				barChartView.setEnabled(false);
				chartsFrameLayout.setVisibility(View.VISIBLE);
				chartLayout.setVisibility(View.VISIBLE);
				chartLayout.removeAllViews();
				chartLayout.addView(barChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
				          LayoutParams.FILL_PARENT));
			}
		});
	}
	
	/**
	 * generates chart based on the details passed.
	 *
	 * @param analytics the analytics
	 */
	private void generateAnalyticsAcrossSections(final AndroidAnalyticsList analytics){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				noData.setVisibility(View.GONE);
				ComparativeStudyAcrossSectionsAnalytics lineChart = new ComparativeStudyAcrossSectionsAnalytics();
				AndroidAnalytics sectionOneAnalytics = analytics.getAndroidAnalyitcsList().get(0);
				AndroidAnalytics sectionTwoAnalyitcs = analytics.getAndroidAnalyitcsList().get(1);
				List<String> sections = new ArrayList<String>();
				sections.add("Section "+selectedSection);
				sections.add("Section "+selectedSectionTwo);
				
				View v = lineChart.generateChart(getApplicationContext(), sectionOneAnalytics, sectionTwoAnalyitcs, sections);
				v.setEnabled(false);
				chartsFrameLayout.setVisibility(View.VISIBLE);
				chartLayout.setVisibility(View.VISIBLE);
				chartLayout.removeAllViews();
				chartLayout.addView(v, new LayoutParams(LayoutParams.FILL_PARENT,
				          LayoutParams.FILL_PARENT));
			}
		});
	}
	
	/**
	 * Check for empty fields.
	 *
	 * @return true, if successful
	 */
	private boolean checkForEmptyFields() {
		boolean a = false;
		if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_TESTS)){
			if(_grade.getSelectedItem() == null|| _sectionOne.getSelectedItem() == null|| _testName.getSelectedItem() == null){
				 a = true;
			}else{
				 a = false;
			}
		}else if(chartType.equalsIgnoreCase(VegaConstants.COMPARATIVE_STUDY_ACCROSS_SECTIONS)){
			if(_grade.getSelectedItem() == null|| _sectionOne.getSelectedItem() == null|| _testName.getSelectedItem() == null|| _sectionTwo.getSelectedItem() == null|| _subjectName.getSelectedItem() == null){
				a = true;
			}else{
				a = false;
			}
			if(TextUtils.equals(_sectionOne.getSelectedItem().toString(),_sectionTwo.getSelectedItem().toString())) {
			    Toast.makeText(Tempanalytics.this,"Both sections cannot be same",Toast.LENGTH_LONG).show();
			    a=true;
			}
		}
		return a;
	}
	
	/**
	 * Sets the visibility for graph.
	 */
	private void setVisibilityForGraph(){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				chartsFrameLayout.setVisibility(View.VISIBLE);
				chartLayout.setVisibility(View.GONE);
				noData.setVisibility(View.VISIBLE);				
			}
		});
	}
}
