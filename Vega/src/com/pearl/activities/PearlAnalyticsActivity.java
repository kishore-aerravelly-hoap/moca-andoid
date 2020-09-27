package com.pearl.activities;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.android.ui.ShowProgressBar;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.chat.server.response.BaseResponse;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.ui.models.StatusType;

// TODO: Auto-generated Javadoc
/**
 * The Class PearlAnalyticsActivity.
 */
public class PearlAnalyticsActivity extends Activity{
	
	/** The _getchart. */
	private Button _getchart;
	
	/** The chart type. */
	private String tag,chartType;
	
	/** The server requests. */
	private ServerRequests serverRequests;
	
	/** The app data. */
	private ApplicationData appData;
	
	/** The return result. */
	private int returnResult;
	
	/** The awarded grade spinner. */
	private Spinner sectionOneSpinner, gradeSpinner, testNameSpinner, subjectNameSpinner, examCategorySpinner,
				yearSpinner, studentNameSpinner, passFailSpinner, awardedGradeSpinner; 
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pearl_analytics_layout);
		Bundle bundle = getIntent().getExtras();
		tag = getClass().getSimpleName();
		appData = (ApplicationData)getApplication();
		serverRequests = new ServerRequests(this);
		gradeSpinner = (Spinner)findViewById(R.id.analytics_grade_spinner);
		//TODO 	analytics_studentName
		sectionOneSpinner = (Spinner)findViewById(R.id.analytics_section);
		testNameSpinner = (Spinner) findViewById(R.id.analytics_test_name);
		examCategorySpinner = (Spinner)findViewById(R.id.analytics_exam_category);
		yearSpinner = (Spinner)findViewById(R.id.analytics_year);
		studentNameSpinner = (Spinner) findViewById(R.id.analytics_studentName);
		passFailSpinner = (Spinner) findViewById(R.id.analytics_pass_fail);
		awardedGradeSpinner = (Spinner) findViewById(R.id.analytics_awarded_grade);
		if(bundle != null){
			chartType = bundle.getString(VegaConstants.CHART_TYPE);
			int index = chartType.indexOf("\r");
			chartType = chartType.substring(0, index);
			getChartType(chartType);
		}
		else{
			Toast.makeText(this, "Invalid selection", Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * Gets the chart type.
	 *
	 * @param chartType the chart type
	 * @return the chart type
	 */
	private void getChartType(String chartType){
		if(chartType.equalsIgnoreCase(VegaConstants.PASS_FAIL_PERCENTAGE_ACROSS_SECTIONS)){
			sectionOneSpinner.setVisibility(View.GONE);
			testNameSpinner.setVisibility(View.GONE);
			yearSpinner.setVisibility(View.GONE);
			awardedGradeSpinner.setVisibility(View.GONE);
			studentNameSpinner.setVisibility(View.GONE);
			getChartDetailsForPassFailPercentageAcrossSections();
		}
	}
	
	/**
	 * Gets the chart details for pass fail percentage across sections.
	 *
	 * @return the chart details for pass fail percentage across sections
	 */
	private void getChartDetailsForPassFailPercentageAcrossSections(){
		String requestUrl = serverRequests.getRequestURL(ServerRequests.GRADE_LIST, "");
		ShowProgressBar.showProgressBar("Downloading data", getGradesList(requestUrl), this);
	}
	
	/**
	 * Gets the grades list.
	 *
	 * @param requestUrl the request url
	 * @return the grades list
	 */
	private int getGradesList(String requestUrl){

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
				    ShowProgressBar.progressBar.dismiss();
				    if (response.getData() == null ||
				    		response.getStatus().equals(StatusType.FAILURE)) {
					Toast.makeText(getApplicationContext(),
						"No grades", Toast.LENGTH_LONG).show();
				    }
				    if(response != null && response.getData() != null){
				    	final List<String> gradeList = (List<String>)response.getData();
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
						Toast.makeText(getApplicationContext(),R.string.Unable_to_reach_pearl_server, Toast.LENGTH_LONG).show();						
					}
				});
			}
		});
		requestHandler.request();
		return 100;
	}
	
	/**
	 * Sets the grades list adapter.
	 *
	 * @param gradeList the new grades list adapter
	 */
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
			gradeSpinner.setAdapter(gradesListAdapter);
		}
	}
	
	/**
	 * The listener interface for receiving gradeItemSelected events.
	 * The class that is interested in processing a gradeItemSelected
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addGradeItemSelectedListener<code> method. When
	 * the gradeItemSelected event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see GradeItemSelectedEvent
	 */
	class GradeItemSelectedListener implements OnItemSelectedListener{

		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Logger.warn(tag, "item selected is:"+gradeSpinner.getSelectedItem().toString());
			examCategorySpinner.setEnabled(false);
			passFailSpinner.setEnabled(false);
			downloadDataBasedOnChart();
		}

		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
		 */
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			Logger.warn(tag, "nothing selected:"+gradeSpinner.getSelectedItem().toString());
		}
	}
	
	/**
	 * Download data based on chart.
	 */
	private void downloadDataBasedOnChart(){
		if(chartType.equals(VegaConstants.PASS_FAIL_PERCENTAGE_ACROSS_SECTIONS)){
			String url = serverRequests.getRequestURL(ServerRequests.EXAM_TYPES_FOR_GRADES, "");
			ShowProgressBar.showProgressBar("Downloading Data", downloadExamTypeForGrades(url), this);
		}
	}
	
	/**
	 * Download exam type for grades.
	 *
	 * @param url the url
	 * @return the int
	 */
	private int downloadExamTypeForGrades(String url){
		returnResult = 0;
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		Logger.warn(tag, "url fs: "+url);
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
				    	final List<String> gradeList = (List<String>)response.getData();
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
						Toast.makeText(getApplicationContext(),R.string.Unable_to_reach_pearl_server, Toast.LENGTH_LONG).show();						
					}
				});
			}
		});
		requestHandler.request();
		return 100;
	}
}
