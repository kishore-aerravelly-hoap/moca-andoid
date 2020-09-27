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
import com.pearl.analytics.models.Percentage;
import com.pearl.analytics.models.PercentageList;
import com.pearl.analytics.view.StudentCountForARangeInAGradeChartView;
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
import com.pearl.parsers.json.AnalyticsParser;
import com.pearl.ui.models.StatusType;

public class StudentCountForARangeInAGradeActivity extends Activity {
	

	private Spinner gradeSpinner, examCategorySpinner, rangeSpinner;
	private Button getChart;
	private ServerRequests serverRequests;
	private int returnResult;
	private ApplicationData appData;
	private TextView noData, chartName;
	private RelativeLayout chartLayout;
	private PercentageList parsedRangeList;
	private FrameLayout chartsFrameLayout;
	private Context context;
	private boolean chartFlag;
	private List<String> gradeList, examCategoryList, rangeList;
	private String tag, selectedGrade, selectedCategory, selectedRange;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_count_for_a_grade_range);
		tag = "PassFailPercentageAcrossSectionsChart";
		serverRequests = new ServerRequests(this);
		chartFlag = true;
		gradeSpinner = (Spinner) findViewById(R.id.chart6_grade);
		chartsFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		examCategorySpinner = (Spinner) findViewById(R.id.chart6_exam_category);
		chartLayout = (RelativeLayout) findViewById(R.id.analytics);
		rangeSpinner = (Spinner) findViewById(R.id.chart6_grade_range);
		getChart = (Button) findViewById(R.id.chart6_get_chart);
		noData = (TextView) findViewById(R.id.chart6_empty_analytics);
		chartName = (TextView) findViewById(R.id.chart_name_6);
		chartName.setText(VegaConstants.COUNT_OF_STUDENTS_IN_A_GRADE_RANGE);
		appData = (ApplicationData) getApplication();
		context = this;
		String requestUrl = serverRequests.getRequestURL(ServerRequests.GRADE_LIST, "");
		getChart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkForEmptyFields()){
					Toast.makeText(getApplicationContext(), "Make sure all fields are entered", Toast.LENGTH_SHORT).show();
				}else{
					String url = serverRequests.getRequestURL(ServerRequests.STUDENT_COUNT_FOR_A_RANGE_IN_A_GRADE, "");
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
			rangeSpinner.setEnabled(false);
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
					ShowProgressBar.progressBar.cancel();
					Toast.makeText(getApplicationContext(), R.string.no_exams_for_grade, Toast.LENGTH_LONG).show();
				}
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
			rangeSpinner.setEnabled(true);
			String url = serverRequests.getRequestURL(ServerRequests.PERCENTAGE_RANGE, "");
			downloadRangeDetaisl(url);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
	
	private void downloadRangeDetaisl(String url){
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
				    	String data = response.getData().toString();
				    	parsedRangeList = AnalyticsParser.parsePercentageRangeDetails(data);
				    	for(int i=0; i<parsedRangeList.getPercentageList().size(); i++){
				    		if(null == parsedRangeList.getPercentageList().get(i))
				    			parsedRangeList.getPercentageList().remove(i);
				    	}
				    	runOnUiThread(new Runnable() {
							@Override
							public void run() {
								setRangeAdapter(parsedRangeList.getPercentageList());
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
	}
	
	private void setRangeAdapter(List<Percentage> rangeList){
		Logger.warn(tag, "need to set the adapter");
		if(rangeList.size() == 0){
			Toast.makeText(getApplicationContext(), R.string.no_range, Toast.LENGTH_LONG).show();
		}
		List<String> list = new ArrayList<String>();
		for(int i=0; i<rangeList.size(); i++){
			list.add(rangeList.get(i).getFromValue()+"-"+rangeList.get(i).getToValue());			
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter            
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rangeSpinner.setAdapter(adapter);
		rangeSpinner.setOnItemSelectedListener(new onRangeItemSelectedListener());
		ShowProgressBar.progressBar.dismiss();
		selectedRange = list.get(0);
		if(chartFlag == true && gradeList.size() != 0 && examCategoryList.size() != 0 && list.size() != 0 ){
			String url = serverRequests.getRequestURL(ServerRequests.STUDENT_COUNT_FOR_A_RANGE_IN_A_GRADE, "");
			ShowProgressBar.showProgressBar("Downloading data", downloadChartData(url), this);			
		}
		 
	}
	
	private class onRangeItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			selectedRange = rangeSpinner.getSelectedItem().toString();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
	
	private boolean checkForEmptyFields() {
		if(gradeSpinner.getSelectedItem() == null  || examCategorySpinner.getSelectedItem() == null || rangeSpinner.getSelectedItem() == null )
			return true;
		else
			return false;
	}
	
	private int downloadChartData(String url){
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("grade", selectedGrade);
		StringParameter examTypeParam = new StringParameter("examType", selectedCategory);
		StringParameter resultType = new StringParameter("percentRange", selectedRange);
		params.add(gradeParam);
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
							public void run() {
								chartLayout.setVisibility(View.GONE);
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
		StudentCountForARangeInAGradeChartView barChart = new StudentCountForARangeInAGradeChartView();
		View barChartView = barChart.generateChart(getApplicationContext(), list);
		barChartView.setEnabled(false);
		chartsFrameLayout.setVisibility(View.VISIBLE);
		chartLayout.setVisibility(View.VISIBLE);
		noData.setVisibility(View.GONE);
		chartLayout.removeAllViews();
		chartLayout.addView(barChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
		          LayoutParams.FILL_PARENT));	
	}
}
