package com.pearl.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.pearl.analytics.models.GradePassFailPercentageList;
import com.pearl.analytics.view.PerformaneOfaStudentInAllSubjetcsChartView;
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

public class PerformanceOfStudentsInAllSubjectsActivity extends Activity {
	

	private Spinner gradeSpinner, examCategorySpinner, sectionSpinner, studentNameSpinner;
	private Button getChart;
	private ServerRequests serverRequests;
	private int returnResult;
	private Context context;
	private TextView noData,chartName,abbrevations;
	private RelativeLayout chartLayout;
	private List<Student> studentListfromParser;
	private FrameLayout chartsFrameLayout;
	private boolean chartFlag; 
	private List<String> gradeList, examCategoryList, sectionList,studentsList;
	private String tag, selectedGrade, selectedCategory, selectedSection, selectedStudent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.performance_of_student_in_all_subjects);
		tag = "PassFailPercentageAcrossSectionsChart";
		serverRequests = new ServerRequests(this);
		chartFlag = true;
		gradeSpinner = (Spinner) findViewById(R.id.chart4_grade);
		chartsFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		examCategorySpinner = (Spinner) findViewById(R.id.chart4_exam_category);
		chartLayout = (RelativeLayout) findViewById(R.id.analytics);
		sectionSpinner = (Spinner) findViewById(R.id.chart4_section);
		getChart = (Button) findViewById(R.id.chart4_get_chart);
		chartName = (TextView) findViewById(R.id.chart_name_4);
		abbrevations = (TextView) findViewById(R.id.abbrevations);
		chartName.setText(VegaConstants.PERFORMANCE_OF_A_STUDENT_IN_ALL_SUBJECTS_FOR_EXAM_TYPE);
		noData = (TextView) findViewById(R.id.chart4_empty_analytics);
		studentNameSpinner = (Spinner) findViewById(R.id.chart4_student_name);
		context = this;
		String requestUrl = serverRequests.getRequestURL(ServerRequests.GRADE_LIST, "");
		getChart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

			    
				if(checkForEmptyFields()){
					Toast.makeText(getApplicationContext(), "Make sure all fields are entered", Toast.LENGTH_SHORT).show();
				}else{
					String url = serverRequests.getRequestURL(ServerRequests.STUDENT_PERFORMANCE_IN_ALL_SUBJECTS, "");
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
			examCategorySpinner.setEnabled(false);
			String url = serverRequests.getRequestURL(ServerRequests.SECTION_LIST_FOR_GRADE, "");
			downloadSectionDetails(url);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			Logger.warn(tag, "nothing selected:"+gradeSpinner.getSelectedItem().toString());
		}
	}
	
	private int downloadExamTypeForGrades(String url, String selectedGrade){
		returnResult = 0;
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("grade", selectedGrade);
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
				    	examCategoryList = (List<String>)response.getData();
				    	for(int i=0; i<examCategoryList.size(); i++){
				    		if(null == examCategoryList.get(i))
				    			examCategoryList.remove(i);
				    	}
				    	runOnUiThread(new Runnable() {
							@Override
							public void run() {
								setExamCategoryListAdapter(examCategoryList);
							}
						});
				    }
				    examCategorySpinner.setOnItemSelectedListener(new ExamCategoryItemSelectedListener());
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
	
	private void setExamCategoryListAdapter(List<String> examCategoryList){
		if(examCategoryList.size() == 0){
			Toast.makeText(getApplicationContext(), R.string.no_exams_for_grade, Toast.LENGTH_LONG).show();
			ShowProgressBar.progressBar.dismiss();
		}
		Logger.warn(tag, "need to set the adapter");
		ArrayAdapter<String> examCategoryListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, examCategoryList);
		examCategoryListAdapter            
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		examCategorySpinner.setAdapter(examCategoryListAdapter);
		if(examCategoryList.isEmpty())
			examCategorySpinner.setEnabled(false);
		else{
			selectedCategory = examCategoryList.get(0);
			examCategorySpinner.setEnabled(true);
		}
		ShowProgressBar.progressBar.dismiss();
		if(chartFlag && gradeList.size() != 0  && studentsList.size() != 0 && sectionList.size() != 0 && examCategoryList.size() != 0){
			String url = serverRequests.getRequestURL(ServerRequests.STUDENT_PERFORMANCE_IN_ALL_SUBJECTS, "");
			ShowProgressBar.showProgressBar("Downloading data", downloadChartData(url), context);			
		}
	}
	
	private class ExamCategoryItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectedCategory = examCategorySpinner.getSelectedItem().toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
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
				    	//ShowProgressBar.progressBar.dismiss();
				    	for(int i=0; i<sectionList.size(); i++){
				    		if(null == sectionList.get(i))
				    			sectionList.remove(i);
				    	}
						

				    	setSectionsListAdapter(sectionList);
				    }else  if(response != null && response.getData() == null){
				    	final List<String> sectionList = new ArrayList<String>();
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
					ShowProgressBar.progressBar.dismiss();
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
					studentsList = new ArrayList<String>();
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
				    		studentsList.add(studentListfromParser.get(i).getStudentFullName());
				    	}
				    	for(int i=0; i<studentsList.size(); i++){
				    		if(null == studentsList.get(i))
				    			studentsList.remove(i);
				    	}
				    	runOnUiThread(new Runnable() {
							@Override
							public void run() {
								setStudentsListAdapter(studentsList);
							}
						});
				    }else if(response != null && response.getData() == null)
				    	setStudentsListAdapter(studentsList);
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
			String url = serverRequests.getRequestURL(ServerRequests.EXAM_TYPES_FOR_GRADES, "");
			downloadExamTypeForGrades(url, selectedGrade);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
		
	private boolean checkForEmptyFields() {
		if(gradeSpinner.getSelectedItem() == null  || examCategorySpinner.getSelectedItem() == null || sectionSpinner.getSelectedItem() == null
				|| studentNameSpinner.getSelectedItem() == null)
			return true;
		else
			return false;
	}
	
	private int downloadChartData(String url){
	
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("grade", selectedGrade);
		StringParameter sectionParam = new StringParameter("section", selectedSection);
		StringParameter examTypeParam = new StringParameter("examType", selectedCategory);
		StringParameter resultType = new StringParameter("studentId", selectedStudent);
		params.add(gradeParam);
		params.add(sectionParam);
		params.add(examTypeParam);
		params.add(resultType);
		Logger.warn(tag, "url fs: "+url);
		PostRequestHandler requestHandler = new PostRequestHandler(url, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ObjectMapper objMapper = new ObjectMapper();
				try {
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);
					chartFlag = false;
					ShowProgressBar.progressBar.dismiss();
					if(response != null && response.getData() != null){
						String data = response.getData().toString();
						data = "{\"percentageList\":"+data +"}";
						GradePassFailPercentageList list = AnalyticsParser.parseGradePassFailPercentageListDetails(data);
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
	
	public void generateChart(final GradePassFailPercentageList list){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				chartsFrameLayout.setVisibility(View.VISIBLE);
				noData.setVisibility(View.VISIBLE);
				chartLayout.setVisibility(View.GONE);
				abbrevations.setVisibility(View.INVISIBLE);
				/*for(int i=0; i<list.getPercentageList().size(); i++){
					if(Double.parseDouble(list.getPercentageList().get(i).getStudentPercentage()) != 0)
						setChart(list);
				}*/
				if(list != null && list.getPercentageList().size() != 0){
					String formattedName = "";
					List<String> list1 = new ArrayList<String>();
					for(int i=0; i<list.getPercentageList().size(); i++){
						String subject = list.getPercentageList().get(i).getSubject();
						String numeric = "";
						//String abbrevatedString = WordUtils.initials(examType);
						Pattern p = Pattern.compile("(\\d)");
						Matcher m = p.matcher(subject);
						while(m.find())
						{
							numeric = m.group(1);
						}
						String subString = subject.substring(0,3);
						if(numeric != "" && numeric != null)
							formattedName = formattedName + subject +" "+ subString + numeric+", ";
						else
							formattedName = formattedName + subject +" "+ subString  +", ";
						list1.add(subString + numeric);
						list.getPercentageList().get(i).setSubject(subString + " "+ numeric);
					}	
					abbrevations.setText(formattedName);
					setChart(list);
				}
			}
		});
	}
	
	private void setChart(GradePassFailPercentageList list){

		noData.setVisibility(View.GONE);
		abbrevations.setVisibility(View.VISIBLE);
		PerformaneOfaStudentInAllSubjetcsChartView barChart = new PerformaneOfaStudentInAllSubjetcsChartView();
		View barChartView = barChart.generateChart(getApplicationContext(), list);
		barChartView.setEnabled(false);
		chartsFrameLayout.setVisibility(View.VISIBLE);
		chartLayout.setVisibility(View.VISIBLE);
		chartLayout.removeAllViews();
		chartLayout.addView(barChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
		          LayoutParams.FILL_PARENT));	
	}
}