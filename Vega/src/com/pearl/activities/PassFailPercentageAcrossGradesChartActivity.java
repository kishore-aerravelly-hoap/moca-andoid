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
import com.pearl.analytics.view.PassFailPercentageAcrossGradesChartView;
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

public class PassFailPercentageAcrossGradesChartActivity extends Activity {

	private Spinner examCategorySpinner, passFailSpinner;
	private Button getChart;
	private ServerRequests serverRequests;
	private int returnResult;
	private Context context;
	private TextView noData, chartName;
	private RelativeLayout chartLayout;
	private FrameLayout chartsFrameLayout;
	private boolean chartFlag;
	private String tag, selectedCategory, selectedresultType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pass_fail_percentage_across_grades);
		tag = "PassFailPercentageAcrossSectionsChart";
		serverRequests = new ServerRequests(this);
		chartFlag = true;
		chartsFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		examCategorySpinner = (Spinner) findViewById(R.id.chart3_exam_category);
		chartLayout = (RelativeLayout) findViewById(R.id.analytics);
		passFailSpinner = (Spinner) findViewById(R.id.chart3_pass_fail);
		getChart = (Button) findViewById(R.id.chart3_get_chart);
		noData = (TextView) findViewById(R.id.chart3_empty_analytics);
		chartName = (TextView) findViewById(R.id.chart_name_3);
		chartName.setText(VegaConstants.PASS_FAIL_PERCENTAGE_ACROSS_GRADES);
		context = this;
		String requestUrl = serverRequests.getRequestURL(ServerRequests.EXAM_TYPES_FOR_GRADES, "");
		getChart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

			    
				if(checkForEmptyFields()){
					Toast.makeText(getApplicationContext(), "Make sure all fields are entered", Toast.LENGTH_SHORT).show();
				}else{
					String url = serverRequests.getRequestURL(ServerRequests.PASS_FAIL_PERCENT_ACROSS_GRADES, "");
					ShowProgressBar.showProgressBar("Downloading data", downloadChartData(url), context);
				}
			}
		});
		
		ShowProgressBar.showProgressBar("Downloading data", downloadExamTypeForAllGrades(requestUrl), this);
	}
	
	private int downloadExamTypeForAllGrades(String url){
		returnResult = 0;
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("grade", "");
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
				    	final List<String> examCategoryList = (List<String>)response.getData();
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
						Toast.makeText(getApplicationContext(),R.string.Unable_to_reach_pearl_server, Toast.LENGTH_LONG).show();						
					}
				});
			}
		});
		requestHandler.request();
		return 100;
	}
	
	private void setExamCategoryListAdapter(List<String> examCategoryList){
		Logger.warn(tag, "need to set the adapter");
		ArrayAdapter<String> examCategoryListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, examCategoryList);
		examCategoryListAdapter            
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		if(examCategoryList.isEmpty())
			examCategorySpinner.setEnabled(false);
		else{
			examCategorySpinner.setEnabled(true);
			examCategorySpinner.setAdapter(examCategoryListAdapter);
		}
	
	}
	
	private class ExamCategoryItemSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectedCategory = examCategorySpinner.getSelectedItem().toString();
			passFailSpinner.setEnabled(true);
			setPassFailAdapter();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
	
	
	private void setPassFailAdapter(){
		List<String> list = new ArrayList<String>();
		list.add("Pass");
		list.add("Fail");
		Logger.warn(tag, "need to set the adapter");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter            
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		passFailSpinner.setAdapter(adapter);
		ShowProgressBar.progressBar.dismiss();
		passFailSpinner.setOnItemSelectedListener(new OptionItemSelectedListener());
		selectedresultType = "pass";
		if(chartFlag == true){
			String url = serverRequests.getRequestURL(ServerRequests.PASS_FAIL_PERCENT_ACROSS_GRADES, "");
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
		if(examCategorySpinner.getSelectedItem() == null || passFailSpinner.getSelectedItem() == null )
			return true;
		else
			return false;
	}
	
	private int downloadChartData(String url){
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter examTypeParam = new StringParameter("examType", selectedCategory);
		StringParameter resultType = new StringParameter("resultType", selectedresultType);
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
				/*for(int i=0; i<list.getPercentageList().size(); i++){
					if(selectedresultType.equalsIgnoreCase("pass")){
						if(Float.parseFloat(list.getPercentageList().get(i).getPassPercentage()) != 0){
							setChart(list);
						}
					}else if(selectedresultType.equalsIgnoreCase("Fail")){
						if(Float.parseFloat(list.getPercentageList().get(i).getFailPercentage()) != 0){
							setChart(list);
						}
					}
				}*/
				setChart(list);
			}
		});
	}
	
	private void setChart(GradePassFailPercentageList list){
		PassFailPercentageAcrossGradesChartView pieChart = new PassFailPercentageAcrossGradesChartView();
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

