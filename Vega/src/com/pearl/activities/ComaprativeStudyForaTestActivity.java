package com.pearl.activities;

import java.util.ArrayList;
import java.util.List;

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
import com.pearl.analytics.models.AndroidAnalyticsList;
import com.pearl.analytics.view.ComparativeStudyAcrossTestsAnalytics;
import com.pearl.android.ui.ShowProgressBar;
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

public class ComaprativeStudyForaTestActivity extends Activity {

	private Spinner gradeSpinner, sectionSpinner,  examTypeSpinner, subjectSpinner;
	private Button getChart;
	private ServerRequests serverRequests;
	private int returnResult;
	private Context context;
	private TextView noData, chartName;
	private RelativeLayout chartLayout;
	private List<String> gradeList, sectionList, examTypeList, subjectList;
	private List<Student> studentListfromParser;
	private FrameLayout chartsFrameLayout;
	private boolean chartFlag;
	private String tag, selectedGrade, selectedSection, selectedExamType, selectedSubject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comaprative_study_for_a_test);
		tag = "PassFailPercentageAcrossSectionsChart";
		serverRequests = new ServerRequests(this);
		chartFlag = true;
		gradeSpinner = (Spinner) findViewById(R.id.chart10_grade);
		chartsFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		chartLayout = (RelativeLayout) findViewById(R.id.analytics);
		sectionSpinner = (Spinner) findViewById(R.id.chart10_section);
		getChart = (Button) findViewById(R.id.chart10_get_chart);
		noData = (TextView) findViewById(R.id.chart10_empty_analytics);
		subjectSpinner = (Spinner) findViewById(R.id.chart10_subject);
		chartName = (TextView) findViewById(R.id.chart_name_9);
		chartName.setText(VegaConstants.COMPARATIVE_STUDY_ACCROSS_TESTS);
		examTypeSpinner = (Spinner) findViewById(R.id.chart10_exam_type);
		context = this;
		String requestUrl = serverRequests.getRequestURL(ServerRequests.GRADE_LIST, "");
		getChart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

			    
				if(checkForEmptyFields()){
					Toast.makeText(getApplicationContext(), "Make sure all fields are entered", Toast.LENGTH_SHORT).show();
				}else{
					String url = serverRequests.getRequestURL(ServerRequests.ANALYTICS_ACROSS_SECTIONS, "");
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
				    sectionSpinner.setOnItemSelectedListener(new onSectionListItemSelectorListener());
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
				}else
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
	
	private class onSectionListItemSelectorListener implements OnItemSelectedListener{
	
	@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectedSection = sectionSpinner.getSelectedItem().toString();
			String url = serverRequests.getRequestURL(ServerRequests.EXAM_TYPES_FOR_GRADES, "");
			downloadExamTypeDetails(url);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
	
	private void downloadExamTypeDetails(String url){
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
					
				    }
				    if(response != null && response.getData() != null){
				    	examTypeList = (List<String>) response.getData();
				    	for(int i=0; i<examTypeList.size(); i++){
				    		if(null == examTypeList.get(i))
				    			examTypeList.remove(i);
				    	}
				    	setExamListAdapter(examTypeList);
				    }else if(response != null && response.getData() == null){
				    	examTypeList = new ArrayList<String>();
				    	setExamListAdapter(examTypeList);
				    }
				    examTypeSpinner.setOnItemSelectedListener(new onEaxmTypeListItemSelectorListener());
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
	
	private void setExamListAdapter(final List<String> examList){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(examList.size() == 0){
					ShowProgressBar.progressBar.cancel();
					Toast.makeText(getApplicationContext(), R.string.no_exams_for_grade, Toast.LENGTH_LONG).show();
				}else
				Logger.warn(tag, "need to set the adapter");
				ArrayAdapter<String> subjectListAdapter = new ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_spinner_item, examList);
				subjectListAdapter            
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				if(examList.isEmpty())
					examTypeSpinner.setEnabled(false);
				else{
					examTypeSpinner.setEnabled(true);
				}
				examTypeSpinner.setAdapter(subjectListAdapter);				
			}
		});
	}
	
	private class onEaxmTypeListItemSelectorListener implements OnItemSelectedListener{
	
	@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			selectedExamType = examTypeSpinner.getSelectedItem().toString();
			String url = serverRequests.getRequestURL(ServerRequests.SUBJECTS_FOR_GRADE_SECTION, "");
			downloadSubjectDetails(url);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
	
	private void downloadSubjectDetails(String url){
		returnResult = 0;
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("gradeName", selectedGrade);
		StringParameter sectionParam = new StringParameter("sectionName", selectedSection);
		Logger.warn(tag, "selected grade as param is:"+selectedGrade);
		params.add(gradeParam);
		params.add(sectionParam);
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
					
				    }
				    if(response != null && response.getData() != null){
				    	subjectList = (List<String>) response.getData();
				    	for(int i=0; i<subjectList.size(); i++){
				    		if(null == subjectList.get(i))
				    			subjectList.remove(i);
				    	}
				    	setSubjectListAdapter(subjectList);
				    }else if(response != null && response.getData() == null){
				    	List<String> subjectList = new ArrayList<String>();
				    	setSubjectListAdapter(subjectList);
				    }
				    subjectSpinner.setOnItemSelectedListener(new onSubjectListItemSelectorListener());
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
	
	private void setSubjectListAdapter(final List<String> subjectList){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(subjectList.size() == 0){
					Toast.makeText(getApplicationContext(), R.string.no_subjects_for_exam, Toast.LENGTH_LONG).show();
				}
				Logger.warn(tag, "need to set the adapter");
				ArrayAdapter<String> subjectListAdapter = new ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_spinner_item, subjectList);
				subjectListAdapter            
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				Logger.warn(tag, "subject size is:"+subjectList.size());
				if(subjectList.isEmpty())
					subjectSpinner.setEnabled(false);
				else{
					subjectSpinner.setEnabled(true);
				}
				subjectSpinner.setAdapter(subjectListAdapter);
				ShowProgressBar.progressBar.dismiss();
				if(chartFlag && sectionList.size() != 0 && gradeList.size() != 0 && subjectList.size() != 0 && examTypeList.size() != 0){
					selectedSubject = subjectList.get(0);
					String url = serverRequests.getRequestURL(ServerRequests.ANALYTICS_ACROSS_SECTIONS, "");
					ShowProgressBar.showProgressBar("Downloading data", downloadChartData(url), context);					
				}
			}
		});
	}
	
	private class onSubjectListItemSelectorListener implements OnItemSelectedListener{
	
	@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			selectedSubject = subjectSpinner.getSelectedItem().toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
		
	private boolean checkForEmptyFields() {
		if(gradeSpinner.getSelectedItem() == null  || sectionSpinner.getSelectedItem() == null
				|| examTypeSpinner.getSelectedItem() == null || subjectSpinner.getSelectedItem() == null)
			return true;
		else
			return false;
	}
	
	private int downloadChartData(String url){
	
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("gradeName", selectedGrade);
		StringParameter sectionOneParam = new StringParameter("sectionName", selectedSection);
		StringParameter examTypeParam = new StringParameter("examType", selectedExamType);
		StringParameter subjectParam = new StringParameter("subject", selectedSubject);
		params.add(gradeParam);
		params.add(sectionOneParam);
		params.add(examTypeParam);
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
						AndroidAnalyticsList analyticsList = SubjectPointsParser.getAndroidAnalyticsFromList(data);
						for(int i=0; i<analyticsList.getAndroidAnalyitcsList().size(); i++){
						generateChart(analyticsList);
					}}else if(response != null && response.getData() == null){
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								chartLayout.setVisibility(View.INVISIBLE);
								noData.setVisibility(View.VISIBLE);
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
	
	public void generateChart(final AndroidAnalyticsList list){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				chartsFrameLayout.setVisibility(View.VISIBLE);
				noData.setVisibility(View.VISIBLE);
				chartLayout.setVisibility(View.GONE);
				if(list.getAndroidAnalyitcsList().get(0).getSubjectPoints().size() > 0)
					setChart(list);
			}
		});
	}
	
	private void setChart(AndroidAnalyticsList list){

		noData.setVisibility(View.GONE);
		ComparativeStudyAcrossTestsAnalytics lineChart = new ComparativeStudyAcrossTestsAnalytics();
		View lineChartView = lineChart.generateChart(getApplicationContext(), list.getAndroidAnalyitcsList().get(0));
		chartsFrameLayout.setVisibility(View.VISIBLE);
		chartLayout.setVisibility(View.VISIBLE);
		chartLayout.removeAllViews();
		chartLayout.addView(lineChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
		          LayoutParams.FILL_PARENT));	
	}
}
