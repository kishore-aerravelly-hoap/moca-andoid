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
import com.pearl.analytics.models.GradePassFailPercentageList;
import com.pearl.analytics.view.PassFailPercentageAcrossSectionsForSubjectChartView;
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
import com.pearl.ui.models.StatusType;

public class PassFailPercentageAcrossSectionsForASubjectActivity extends
		Activity {

	private Spinner gradeSpinner, examCategorySpinner, passFailSpinner, subjectSpinner;
	private Button getChart;
	private TextView chartName;
	private ServerRequests serverRequests;
	private Context context;
	private int returnResult;
	private TextView noData;
	private RelativeLayout chartLayout;
	private FrameLayout chartsFrameLayout;
	private boolean chartFlag;
	private List<String> gradeList, examCategoryList, subjectList;
	private String tag, selectedGrade, selectedCategory, selectedresultType, selectedSubject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pass_fail_percentage_across_sections_for_a_subject);
		tag = "PassFailPercentageAcrossSectionsChart";
		chartFlag = true;
		serverRequests = new ServerRequests(this);
		gradeSpinner = (Spinner) findViewById(R.id.chart2_grade_spinner);
		chartsFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		examCategorySpinner = (Spinner) findViewById(R.id.chart2_exam_category);
		chartLayout = (RelativeLayout) findViewById(R.id.analytics);
		passFailSpinner = (Spinner) findViewById(R.id.chart2_pass_fail);
		getChart = (Button) findViewById(R.id.chart2_get_chart);
		noData = (TextView) findViewById(R.id.chart2_empty_analytics);
		subjectSpinner = (Spinner) findViewById(R.id.chart2_subject);
		chartName = (TextView) findViewById(R.id.chart_name_2);
		chartName.setText(VegaConstants.PASS_FAIL_PERCENTAGE_ACROSS_SECTIONS_FOR_A_SUBJECT);
		String requestUrl = serverRequests.getRequestURL(ServerRequests.GRADE_LIST, "");
		context = this;
		getChart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

			    
				if(checkForEmptyFields()){
					Toast.makeText(getApplicationContext(), "Make sure all fields are entered", Toast.LENGTH_SHORT).show();
				}else{
					String url = serverRequests.getRequestURL(ServerRequests.PASS_FAIL_PERCENT_ACROSS_SECTIONS_FOR_SUBJECT, "");
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
			passFailSpinner.setEnabled(false);
			String url = serverRequests.getRequestURL(ServerRequests.EXAM_TYPES_FOR_GRADES, "");
			downloadExamTypeForGrades(url, selectedGrade);
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
						setExamCategoryListAdapter(examCategoryList);
				    }else  if(response != null && response.getData() == null){
				    	final List<String> examCategoryList = new ArrayList<String>();
				    	setExamCategoryListAdapter(examCategoryList);
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
	
	private void setExamCategoryListAdapter(final List<String> examCategoryList){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(examCategoryList.size() == 0){
					ShowProgressBar.progressBar.cancel();
					Toast.makeText(getApplicationContext(), R.string.no_exams_for_grade, Toast.LENGTH_LONG).show();	
				}else
				Logger.warn(tag, "need to set the adapter");
				ArrayAdapter<String> examCategoryListAdapter = new ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_spinner_item, examCategoryList);
				examCategoryListAdapter            
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				if(examCategoryList.isEmpty())
					examCategorySpinner.setEnabled(false);
				else{
					examCategorySpinner.setEnabled(true);
				}
				examCategorySpinner.setAdapter(examCategoryListAdapter);				
			}
		});
	}
	
	private class ExamCategoryItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectedCategory = examCategorySpinner.getSelectedItem().toString();
			subjectSpinner.setEnabled(true);
			passFailSpinner.setEnabled(false);
			String url = serverRequests.getRequestURL(ServerRequests.SUBJECT_FOR_GRADE, "");
			downloadSubjectList(url);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
	
	private void downloadSubjectList(String url){

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
				}else
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
				
			}
		});
	}
	
	private class onSubjectItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectedSubject = subjectSpinner.getSelectedItem().toString();
			passFailSpinner.setEnabled(true);
			setPassFailAdapter();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
	
	private void setPassFailAdapter(){
		List<String> list = new ArrayList<String>();
		if(!subjectList.isEmpty()){
			list.add("Pass");
			list.add("Fail");			
		}
		Logger.warn(tag, "need to set the adapter");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter            
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		passFailSpinner.setAdapter(adapter);
		passFailSpinner.setOnItemSelectedListener(new OptionItemSelectedListener());
		ShowProgressBar.progressBar.dismiss();
		selectedresultType = "pass";
		if(chartFlag == true && gradeList.size() != 0 && examCategoryList.size() != 0 && subjectList.size() != 0){
			String url = serverRequests.getRequestURL(ServerRequests.PASS_FAIL_PERCENT_ACROSS_SECTIONS_FOR_SUBJECT, "");
			ShowProgressBar.showProgressBar("Downloading data", downloadChartData(url), context);		
		}
	}
	
	private class OptionItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectedresultType = passFailSpinner.getSelectedItem().toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
	
	private boolean checkForEmptyFields() {
		if(gradeSpinner.getSelectedItem() == null  || examCategorySpinner.getSelectedItem() == null || passFailSpinner.getSelectedItem() == null )
			return true;
		else
			return false;
	}
	
	private int downloadChartData(String url){
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("grade", selectedGrade);
		StringParameter subjectParam = new StringParameter("subject", selectedSubject);
		StringParameter examTypeParam = new StringParameter("examType", selectedCategory);
		StringParameter resultType = new StringParameter("resultType", selectedresultType);
		params.add(gradeParam);
		params.add(subjectParam);
		params.add(examTypeParam);
		params.add(resultType);
		Logger.warn(tag, "url fs: "+url);
		PostRequestHandler requestHandler = new PostRequestHandler(url, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ShowProgressBar.progressBar.dismiss();
				ObjectMapper objMapper = new ObjectMapper();
				chartFlag = false;
				try {
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);
					if(response != null && response.getData() != null){
						String data = response.getData().toString();
						data = "{\"percentageList\":"+data +"}";
						GradePassFailPercentageList list = AnalyticsParser.parseGradePassFailPercentageListDetails(data);
						if(list.getPercentageList().size() != 0)
							generateChart(list);
					}else if(response != null && response.getData() == null){
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
	
	public void generateChart(final GradePassFailPercentageList list){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				chartsFrameLayout.setVisibility(View.VISIBLE);
				noData.setVisibility(View.VISIBLE);
				chartLayout.setVisibility(View.GONE);
				setChart(list);
			}
		});
	}

	private void setChart(GradePassFailPercentageList list){
		PassFailPercentageAcrossSectionsForSubjectChartView pieChart = new PassFailPercentageAcrossSectionsForSubjectChartView();
		View pieChartView = pieChart.getChart(getApplicationContext(), list, selectedresultType);
		pieChartView.setEnabled(false);
		chartsFrameLayout.setVisibility(View.VISIBLE);
		chartLayout.setVisibility(View.VISIBLE);
		noData.setVisibility(View.GONE);
		chartLayout.removeAllViews();
		chartLayout.addView(pieChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
		          LayoutParams.FILL_PARENT));	
	}
}
