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
import com.pearl.analytics.models.GradePassFailPercentage;
import com.pearl.analytics.view.PassFailPercentageInaSectionChartView;
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
import com.pearl.ui.models.Student;

public class PassFailPercentageForaSectionActivity extends Activity {
	

	private Spinner gradeSpinner, examCategorySpinner, sectionSpinner;
	private Button getChart;
	private ServerRequests serverRequests;
	private int returnResult;
	private Context context;
	private TextView noData, chartName;
	private RelativeLayout chartLayout;
	private List<Student> studentListfromParser;
	private FrameLayout chartsFrameLayout;
	private boolean chartFlag;
	private List<String> gradeList,examCategoryList,sectionList;
	private String tag, selectedGrade, selectedCategory, selectedSection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pass_fail_percentage_for_a_section);
		tag = "PassFailPercentageForaSectionActivity";
		chartFlag = true;
		serverRequests = new ServerRequests(this);
		gradeSpinner = (Spinner) findViewById(R.id.chart5_grade);
		chartsFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		examCategorySpinner = (Spinner) findViewById(R.id.chart5_exam_category);
		chartLayout = (RelativeLayout) findViewById(R.id.analytics);
		sectionSpinner = (Spinner) findViewById(R.id.chart5_section);
		getChart = (Button) findViewById(R.id.chart5_get_chart);
		noData = (TextView) findViewById(R.id.chart5_empty_analytics);
		chartName = (TextView) findViewById(R.id.chart_name_5);
		chartName.setText(VegaConstants.PASS_FAIL_PERCENTAGE_IN_SECTION);
		context = this;
		String requestUrl = serverRequests.getRequestURL(ServerRequests.GRADE_LIST, "");
		getChart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

			    
				if(checkForEmptyFields()){
					Toast.makeText(getApplicationContext(), "Make sure all fields are entered", Toast.LENGTH_SHORT).show();
				}else{
					String url = serverRequests.getRequestURL(ServerRequests.PASS_FAIL_PERCENTAGE_FOR_SECTION, "");
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
				    ShowProgressBar.progressBar.dismiss();
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
				    }else if(response != null && response.getData() == null){
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
					Toast.makeText(getApplicationContext(), R.string.no_exams_for_grade, Toast.LENGTH_LONG).show();
				}
				Logger.warn(tag, "need to set the adapter");
				ArrayAdapter<String> examCategoryListAdapter = new ArrayAdapter<String>(getApplicationContext(),
						android.R.layout.simple_spinner_item, examCategoryList);
				examCategoryListAdapter            
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				examCategorySpinner.setAdapter(examCategoryListAdapter);
				if(examCategoryList.isEmpty())
					examCategorySpinner.setEnabled(false);
				else{
					examCategorySpinner.setEnabled(true);
					selectedCategory = examCategoryList.get(0);
				}
				ShowProgressBar.progressBar.dismiss();
				if(chartFlag && gradeList.size() != 0 && examCategoryList.size() != 0 && sectionList.size() != 0){
					String url = serverRequests.getRequestURL(ServerRequests.PASS_FAIL_PERCENTAGE_FOR_SECTION, "");
					ShowProgressBar.showProgressBar("Downloading data", downloadChartData(url), context);			
				}
			}
		});
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
				    	for(int i=0; i<sectionList.size(); i++){
				    		if(null == sectionList.get(i))
				    			sectionList.remove(i);
				    	}
				    	setSectionsListAdapter(sectionList);
				    }else if(response != null && response.getData() == null){
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
			String url = serverRequests.getRequestURL(ServerRequests.EXAM_TYPES_FOR_GRADES, "");
			downloadExamTypeForGrades(url, selectedGrade);
	}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
		
	private boolean checkForEmptyFields() {
		if(gradeSpinner.getSelectedItem() == null  || examCategorySpinner.getSelectedItem() == null || sectionSpinner.getSelectedItem() == null)
			return true;
		else
			return false;
	}
	
	private int downloadChartData(String url){
	
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("grade", selectedGrade);
		StringParameter sectionParam = new StringParameter("section", selectedSection);
		StringParameter examTypeParam = new StringParameter("examType", selectedCategory);
		params.add(gradeParam);
		params.add(sectionParam);
		params.add(examTypeParam);
		Logger.warn(tag, "url fs: "+url);
		PostRequestHandler requestHandler = new PostRequestHandler(url, params, new DownloadCallback() {
			
			@Override
			public void onSuccess(String downloadedString) {
				ObjectMapper objMapper = new ObjectMapper();
				try {
					BaseResponse response = objMapper.readValue(downloadedString,
					    BaseResponse.class);
					chartFlag = false;
					if(response != null && response.getData() != null){
						String data = response.getData().toString();
						GradePassFailPercentage object = AnalyticsParser.parseGradePassFailPercentageDetails(data);
						generateChart(object);
					}else if(response != null && response.getData() == null)
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								chartsFrameLayout.setVisibility(View.VISIBLE);
								noData.setVisibility(View.VISIBLE);	
								chartLayout.setVisibility(View.GONE);
							}
						});
					ShowProgressBar.progressBar.dismiss();
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
	
	public void generateChart(final GradePassFailPercentage data){

		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				chartsFrameLayout.setVisibility(View.VISIBLE);
				noData.setVisibility(View.VISIBLE);
				chartLayout.setVisibility(View.GONE);
				/*if(Double.parseDouble(data.getPassPercentage()) != 0
						|| Double.parseDouble(data.getFailPercentage()) != 0)*/
					setChart(data);
			}
		});
	}
	
	private void setChart(GradePassFailPercentage data){
		PassFailPercentageInaSectionChartView pieChart = new PassFailPercentageInaSectionChartView();
		View pieChartView = pieChart.getChart(getApplicationContext(), data);
		pieChartView.setEnabled(false);
		chartsFrameLayout.setVisibility(View.VISIBLE);
		chartLayout.setVisibility(View.VISIBLE);
		noData.setVisibility(View.GONE);
		chartLayout.removeAllViews();
		chartLayout.addView(pieChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
		          LayoutParams.FILL_PARENT));	
	}
}