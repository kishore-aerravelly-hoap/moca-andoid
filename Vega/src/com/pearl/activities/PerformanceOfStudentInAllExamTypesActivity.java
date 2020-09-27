package com.pearl.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.analytics.models.StudentPercentageList;
import com.pearl.analytics.view.PerfromanceOfaStudentsInAllExamTypesChartView;
import com.pearl.android.ui.ShowProgressBar;
import com.pearl.application.VegaConstants;
import com.pearl.chat.server.response.BaseResponse;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.AnalyticsParser;
import com.pearl.parsers.json.StudentDetailsParser;
import com.pearl.ui.models.StatusType;
import com.pearl.ui.models.Student;

public class PerformanceOfStudentInAllExamTypesActivity extends Activity {

	private Spinner gradeSpinner, sectionSpinner, studentNameSpinner, subjectSpinner;
	private Button getChart;
	private ServerRequests serverRequests;
	private int returnResult;
	private TextView noData, chartName,abbrevations;
	private RelativeLayout chartLayout;
	private List<Student> studentListfromParser;
	private FrameLayout chartsFrameLayout;
	private Context context;
	private List<String> subjectList, gradeList, sectionList,studentList;
	private boolean chartFlag;
	private String tag, selectedGrade, selectedCategory, selectedSection, selectedStudent, selectedSubject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.performance_of_student_in_all_examtypes);
		tag = "PassFailPercentageAcrossSectionsChart";
		serverRequests = new ServerRequests(this);
		chartFlag = true;
		gradeSpinner = (Spinner) findViewById(R.id.chart8_grade);
		chartsFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		chartLayout = (RelativeLayout) findViewById(R.id.analytics);
		sectionSpinner = (Spinner) findViewById(R.id.chart8_section);
		getChart = (Button) findViewById(R.id.chart8_get_chart);
		chartName = (TextView) findViewById(R.id.chart_name_7);
		chartName.setText(VegaConstants.PERFORMANCE_OF_STUDENT_IN_ALL_EXAM_TYPES);
		abbrevations = (TextView) findViewById(R.id.abbrevations);
		noData = (TextView) findViewById(R.id.chart8_empty_analytics);
		studentNameSpinner = (Spinner) findViewById(R.id.chart8_student);
		subjectSpinner = (Spinner) findViewById(R.id.chart8_subject);
		context =this;
		abbrevations.setText("");
		String requestUrl = serverRequests.getRequestURL(ServerRequests.GRADE_LIST, "");
		getChart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkForEmptyFields()){
					Toast.makeText(getApplicationContext(), "Make sure all fields are entered", Toast.LENGTH_SHORT).show();
				}else{
					String url = serverRequests.getRequestURL(ServerRequests.PERFORMANCE_OF_STUDENT_IN_ALL_EXAM_TYPES, "");
					ShowProgressBar.showProgressBar("Downloading data", downloadChartData(url), context);
				}
			}
		});
		ShowProgressBar.showProgressBar("Downloading data", downloadGradesList(requestUrl), this);
	}
	
	private int downloadGradesList(String requestUrl){

		returnResult = 0;
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		Logger.warn(tag, "url fs: "+requestUrl);
		PostRequestHandler requestHandler = new PostRequestHandler(requestUrl, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ObjectMapper objMapper = new ObjectMapper();
				try {
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);
					returnResult=100;
				    if (response.getData() == null ||
				    		response.getStatus().equals(StatusType.FAILURE)) {
					Toast.makeText(getApplicationContext(),
						"No grades", Toast.LENGTH_LONG).show();
				    }
				    if(response != null && response.getData() != null){
				    	gradeList = (List<String>)response.getData();
				    	for(int i=0; i<gradeList.size(); i++){
				    		if(null == gradeList.get(i))
				    			gradeList.remove(i);
				    	}
				    	/*final List<String> modifiedList = new ArrayList<String>();
				    	for(int i=0; i<gradeList.size(); i++){
				    		modifiedList.add(WordUtils.capitalize(gradeList.get(i)));
				    	}*/
				    	runOnUiThread(new Runnable() {
							@Override
							public void run() {
								setGradesListAdapter(gradeList);
							}
						});
				    }
				    gradeSpinner.setOnItemSelectedListener(new GradeItemSelectedListener());
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
						Toast.makeText(getApplicationContext(),"grade"+R.string.Unable_to_reach_pearl_server, Toast.LENGTH_LONG).show();						
					}
				});
			}
		});
		requestHandler.request();
		return 100;
	}
	
	private void setGradesListAdapter(List<String> gradeList){
		Logger.warn(tag, "need to set the adapter");
		ArrayAdapter<String> gradesListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, gradeList);
		gradesListAdapter            
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		if(gradeList.isEmpty())
			gradeSpinner.setEnabled(false);
		else{
			gradeSpinner.setEnabled(true);
		}
		gradeSpinner.setAdapter(gradesListAdapter);
	}
	
	class GradeItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Logger.warn(tag, "item selected is:"+gradeSpinner.getSelectedItem().toString());
			selectedGrade = gradeSpinner.getSelectedItem().toString();
			String url = serverRequests.getRequestURL(ServerRequests.SECTION_LIST_FOR_GRADE, "");
			downloadSectionDetails(url);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			Logger.warn(tag, "nothing selected:"+gradeSpinner.getSelectedItem().toString());
		}
	}
	
	private void downloadSectionDetails(String url){
		returnResult = 0;
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("gradeName", selectedGrade);
		Logger.warn(tag, "selected grade as param is:"+selectedGrade);
		params.add(gradeParam);
		Logger.warn(tag, "url is: "+url);
		PostRequestHandler requestHandler = new PostRequestHandler(url, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ObjectMapper objMapper = new ObjectMapper();
				try {
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);
					returnResult=100;
				    if (response.getData() == null ||
				    		response.getStatus().equals(StatusType.FAILURE)) {
					Toast.makeText(getApplicationContext(),
						"No grades", Toast.LENGTH_LONG).show();
				    }
				    if(response != null && response.getData() != null){
				    	sectionList = (List<String>)response.getData();
				    	for(int i=0; i<sectionList.size(); i++){
				    		if(null == sectionList.get(i))
				    			sectionList.remove(i);
				    	}
				    	setSectionsListAdapter(sectionList);
				    }else if(response != null && response.getData() == null){
				    	List<String> sectionList = new ArrayList<String>();
				    	setSectionsListAdapter(sectionList);
				    }
				    sectionSpinner.setOnItemSelectedListener(new onSectionsListItemSelectorListener());
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
	}
	
	private void setSectionsListAdapter(final List<String> sectionList){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(sectionList.size() == 0){
					Toast.makeText(getApplicationContext(), R.string.no_sections, Toast.LENGTH_LONG).show();
				}
				Logger.warn(tag, "need to set the adapter");
				ArrayAdapter<String> sectionListAdapter = new ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_spinner_item, sectionList);
				sectionListAdapter            
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				if(sectionList.isEmpty())
					sectionSpinner.setEnabled(false);
				else{
					sectionSpinner.setEnabled(true);
				}
				sectionSpinner.setAdapter(sectionListAdapter);
				
			}
		});
	}
	
	private class onSectionsListItemSelectorListener implements OnItemSelectedListener{
	
	@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectedSection = sectionSpinner.getSelectedItem().toString();
			String url = serverRequests.getRequestURL(ServerRequests.STUDENT_LIST, "");
			downloadStudensList(url);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
	
	private void downloadStudensList(String url){
		returnResult = 0;
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("grade", selectedGrade);
		final StringParameter sectionParams = new StringParameter(
				"section", selectedSection);
		Logger.warn(tag, "selected grade as param is:"+selectedGrade);
		params.add(gradeParam);
		params.add(sectionParams);
		Logger.warn(tag, "url is: "+url);
		PostRequestHandler requestHandler = new PostRequestHandler(url, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ObjectMapper objMapper = new ObjectMapper();
				try {
					studentList = new ArrayList<String>();
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);
					returnResult=100;
				    if (response.getData() == null ||
				    		response.getStatus().equals(StatusType.FAILURE)) {
					
				    }
				    if(response != null && response.getData() != null){
				    	studentListfromParser = StudentDetailsParser
							    .getStudentsList(response.getData()
								    .toString());
				    	for(int i=0; i<studentListfromParser.size(); i++){
				    		studentList.add(studentListfromParser.get(i).getStudentFullName());
				    	}
				    	for(int i=0; i<studentList.size(); i++){
				    		if(null == studentList.get(i))
				    			studentList.remove(i);
				    	}
				    	runOnUiThread(new Runnable() {
							@Override
							public void run() {
							  //  ShowProgressBar.progressBar.dismiss();

								setStudentsListAdapter(studentList);
							}
						});
				    }
				    else if(response != null && response.getData() == null){
				    	setStudentsListAdapter(studentList);
				    }
				    studentNameSpinner.setOnItemSelectedListener(new onStudentListItemSelectorListener());
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
	
	}
	
	private void setStudentsListAdapter(final List<String> studentNamesList){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(studentNamesList.size() == 0){
					Toast.makeText(getApplicationContext(), R.string.no_students, Toast.LENGTH_LONG).show();
					ShowProgressBar.progressBar.dismiss();
				}
				Logger.warn(tag, "need to set the adapter");
				ArrayAdapter<String> studentListAdapter = new ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_spinner_item, studentNamesList);
				studentListAdapter            
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				if(studentNamesList.isEmpty())
					studentNameSpinner.setEnabled(false);
				else{
					studentNameSpinner.setEnabled(true);
				}
				studentNameSpinner.setAdapter(studentListAdapter);				
			}
		});
	}
	
	private class onStudentListItemSelectorListener implements OnItemSelectedListener{
	
	@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			selectedStudent = studentListfromParser.get(position).getStudentId();
			String url = serverRequests.getRequestURL(ServerRequests.SUBJECT_LIST_FOR_A_STUDENT, "");
			getSubjectsList(url);
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
		
	private boolean checkForEmptyFields() {
		if(gradeSpinner.getSelectedItem() == null  || sectionSpinner.getSelectedItem() == null
				|| studentNameSpinner.getSelectedItem() == null)
			return true;
		else
			return false;
	}
	
	private int downloadChartData(String url){
	
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("grade", selectedGrade);
		StringParameter sectionParam = new StringParameter("section", selectedSection);
		StringParameter resultType = new StringParameter("studentId", selectedStudent);
		StringParameter subjectParam = new StringParameter("subject", selectedSubject);
		params.add(gradeParam);
		params.add(sectionParam);
		params.add(resultType);
		params.add(subjectParam);
		Logger.warn(tag, "url fs: "+url);
		PostRequestHandler requestHandler = new PostRequestHandler(url, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ObjectMapper objMapper = new ObjectMapper();
				try {
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);
					ShowProgressBar.progressBar.dismiss();
					chartFlag = false;
					if(response != null && response.getData() != null){
						String data = response.getData().toString();
						Logger.warn(tag, "data before parsing:"+response.getData().toString());
						StudentPercentageList list = AnalyticsParser.parseStudentPercentageDetails(data);
						generateChart(list);
					}else if(response != null && response.getData() == null){
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								chartLayout.setVisibility(View.INVISIBLE);
								noData.setVisibility(View.VISIBLE);
								abbrevations.setVisibility(View.INVISIBLE);
							}
						});
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
	
	public void generateChart(final StudentPercentageList list){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				chartsFrameLayout.setVisibility(View.VISIBLE);
				noData.setVisibility(View.VISIBLE);
				chartLayout.setVisibility(View.GONE);
				abbrevations.setVisibility(View.INVISIBLE);
				if(list != null && list.getExamTypeCount() != 0){
					List<String> list1 = new ArrayList<String>();
					Pattern numericPattern = Pattern.compile("(\\d)");
					Pattern stringPattern = Pattern.compile("[a-zA-Z[^-0-9]]");
					String finalString = "";
					for(int i=0; i<list.getStudentPercentages().get(0).getExamTypes().size(); i++){
						String examType = list.getStudentPercentages().get(0).getExamTypes().get(i);
						String[] splittedString = examType.split(" ");
						finalString = "";
						for(int j=0; j<splittedString.length; j++){
							String name = "";
							String numeric = "";
							Matcher numericMatcher = numericPattern.matcher(splittedString[j]);
							Matcher stringMatcher = stringPattern.matcher(splittedString[j]);
							while(numericMatcher.find()){
								String temp = "";
								temp = numericMatcher.group(1);
								numeric = numeric + temp;
							}
							if(stringMatcher.find()){
								name = WordUtils.initials(splittedString[j]);
							}
							finalString = finalString + name + numeric;
						}
						finalString = examType +" "+ finalString +", ";
						list1.add(finalString);
						System.out.println("spliited string "+finalString);
					}	
					String tempString = "";
					for(int i=0; i<list1.size(); i++){
						tempString = tempString + list1.get(i);
					}
					abbrevations.setText(tempString);
					//list.getStudentPercentages().get(0).setExamTypes(list1);
					setChart(list);
				}
			}
		});
	}
	
	private void setChart(StudentPercentageList list){

		noData.setVisibility(View.GONE);
		abbrevations.setVisibility(View.VISIBLE);
		PerfromanceOfaStudentsInAllExamTypesChartView barChart = new PerfromanceOfaStudentsInAllExamTypesChartView();
		View barChartView = barChart.generateChart(getApplicationContext(), list);
		chartsFrameLayout.setVisibility(View.VISIBLE);
		chartLayout.setVisibility(View.VISIBLE);
		chartLayout.removeAllViews();
		chartLayout.addView(barChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
		          LayoutParams.FILL_PARENT));	
	}
	
	private void getSubjectsList(String url){

		returnResult = 0;
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("gradeName", selectedGrade);
		StringParameter sectionParam = new StringParameter("sectionName", selectedSection);
		StringParameter studentParam = new StringParameter("studentId", selectedStudent);
		params.add(gradeParam);
		params.add(sectionParam);
		params.add(studentParam);
		Logger.warn(tag, "url is: "+url);
		PostRequestHandler requestHandler = new PostRequestHandler(url, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ObjectMapper objMapper = new ObjectMapper();
				try {
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);
					returnResult=100;
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
				    subjectSpinner.setOnItemSelectedListener(new onSubjectItemSelectedListener());
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
	}
	
	private void setSubjectListAdapter(final List<String> subjectsList){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(subjectsList.size() == 0){
					Toast.makeText(getApplicationContext(), R.string.no_subjects_for_exam, Toast.LENGTH_LONG).show();
					ShowProgressBar.progressBar.dismiss();
				}
				Logger.warn(tag, "need to set the adapter");
				ArrayAdapter<String> subjectListAdapter = new ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_spinner_item, subjectsList);
				subjectListAdapter            
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				if(subjectsList.isEmpty())
					subjectSpinner.setEnabled(false);
				else{
					subjectSpinner.setEnabled(true);
				}
				subjectSpinner.setAdapter(subjectListAdapter);
				ShowProgressBar.progressBar.dismiss();
				if(chartFlag && gradeList.size() != 0 && sectionList.size() != 0 && gradeList.size() != 0 && subjectsList.size() != 0 && studentList.size() != 0){
					selectedSubject = subjectsList.get(0);
					String url = serverRequests.getRequestURL(ServerRequests.PERFORMANCE_OF_STUDENT_IN_ALL_EXAM_TYPES, "");
					ShowProgressBar.showProgressBar("Downloading data", downloadChartData(url), context);					
				}
			}
		});
	}
	
	private class onSubjectItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectedSubject = subjectSpinner.getSelectedItem().toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}

}
