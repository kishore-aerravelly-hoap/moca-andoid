package com.pearl.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.analytics.models.GradePassFailPercentageList;
import com.pearl.analytics.view.YearWiseComparisionChartView;
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

public class YearWiseComparisionActivity extends Activity {

	private Spinner gradeSpinner, yearOneSpinner, yearTwoSpinner;
	private Button getChart;
	private ServerRequests serverRequests;
	private int returnResult;
	private TextView noData, chartName;
	private RelativeLayout chartLayout;
	private FrameLayout chartsFrameLayout;
	private Context context;
	private boolean chartFlag;
	private String tag, selectedGrade, selectedYear1, selectedYear2;
	private List<String> yearsList,gradeList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.year_wise_comparision);
		tag = "PassFailPercentageAcrossSectionsChart";
		serverRequests = new ServerRequests(this);
		gradeSpinner = (Spinner) findViewById(R.id.chart11_grade);
		chartsFrameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		chartLayout = (RelativeLayout) findViewById(R.id.analytics);
		yearOneSpinner = (Spinner) findViewById(R.id.chart11_year1);
		yearTwoSpinner = (Spinner) findViewById(R.id.chart11_year2);
		getChart = (Button) findViewById(R.id.chart11_get_chart);
		chartName = (TextView) findViewById(R.id.chart_name_11);
		chartName.setText(VegaConstants.YEAR_WISE_COMPARISION);
		noData = (TextView) findViewById(R.id.chart11_empty_analytics);
		context = this;
		chartFlag = true;
		String requestUrl = serverRequests.getRequestURL(ServerRequests.GRADE_LIST, "");
		getChart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!checkForEmptyFields()){
					String url = serverRequests.getRequestURL(ServerRequests.YEAR_WISE_COMPARISION, "");
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
			String url = serverRequests.getRequestURL(ServerRequests.GRADE_YEARS, "");
			downloadYearDetailsForaGrade(url);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			Logger.warn(tag, "nothing selected:"+gradeSpinner.getSelectedItem().toString());
		}
	}
	
	private void downloadYearDetailsForaGrade(String url){
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
				    	runOnUiThread(new Runnable() {
							public void run() {
								ShowProgressBar.progressBar.cancel();
								Toast.makeText(getApplicationContext(),
										"No years for this selection.Please change your selection", Toast.LENGTH_LONG).show();
							}
						});
				    }
				    if(response != null && response.getData() != null){
				    	yearsList = (List<String>)response.getData();
				    	for(int i=0; i<yearsList.size(); i++){
				    		if(null == yearsList.get(i))
				    			yearsList.remove(i);
				    	}
				    	runOnUiThread(new Runnable() {
							@Override
							public void run() {
								setYearListAdapter(yearsList);
							}
						});
				    }
				    yearOneSpinner.setOnItemSelectedListener(new onYearOneListItemSelectorListener());
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
	
	private void setYearListAdapter(List<String> yearList){
		Logger.warn(tag, "need to set the adapter");
		if(yearList.size() == 0){
			Toast.makeText(getApplicationContext(), R.string.no_years, Toast.LENGTH_LONG).show();
		}
		ArrayAdapter<String> sectionOneListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, yearList);
		sectionOneListAdapter            
			.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		if(yearList.isEmpty())
			yearOneSpinner.setEnabled(false);
		else{
			yearOneSpinner.setEnabled(true);
		}
		yearOneSpinner.setAdapter(sectionOneListAdapter);
	}
	
	private class onYearOneListItemSelectorListener implements OnItemSelectedListener{
	
	@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectedYear1 = yearOneSpinner.getSelectedItem().toString();
			List<String> list = new ArrayList<String>();
			Iterator<String> itr = yearsList.iterator();
			while (itr.hasNext()) {
				list.add(itr.next());
			}
			
			Iterator<String> iterator = list.iterator();
			while (iterator.hasNext()) {
				if(selectedYear1.equals(iterator.next())){
					iterator.remove();
				}
			}
			ArrayAdapter<String> sectiontTwoListAdapter = new ArrayAdapter<String>(getApplicationContext(),
					android.R.layout.simple_spinner_item, list);
			sectiontTwoListAdapter            
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			if(list.isEmpty())
				yearTwoSpinner.setEnabled(false);
			else{
				yearTwoSpinner.setEnabled(true);
			}
			yearTwoSpinner.setAdapter(sectiontTwoListAdapter);
			yearTwoSpinner.setOnItemSelectedListener(new onYearTwoListItemSelectorListener());
			ShowProgressBar.progressBar.dismiss();
			if(chartFlag && gradeList.size() != 0 && yearsList.size() != 0){
				ShowProgressBar.progressBar.dismiss();
				if(yearOneSpinner != null && yearTwoSpinner != null && yearOneSpinner.getChildCount() ==0 && yearTwoSpinner.getChildCount() == 0){
					selectedYear1 = yearOneSpinner.getItemAtPosition(0).toString();
					selectedYear2 = yearTwoSpinner.getItemAtPosition(0).toString();
					String url = serverRequests.getRequestURL(ServerRequests.YEAR_WISE_COMPARISION, "");
					ShowProgressBar.showProgressBar("Downloading data", downloadChartData(url), context);
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
	}
	
	private class onYearTwoListItemSelectorListener implements OnItemSelectedListener{
		
		@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selectedYear2 = yearTwoSpinner.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		}
	
	private boolean checkForEmptyFields() {
		if(yearOneSpinner.getChildCount() == 0 || yearTwoSpinner.getChildCount() == 0){
			Toast.makeText(getApplicationContext(), "Only 1 year is available, you need 2 years for comparison please populate data and try again.", Toast.LENGTH_SHORT).show();
			return true;
		} else if(TextUtils.equals(yearOneSpinner.getSelectedItem().toString(),yearTwoSpinner.getSelectedItem().toString())){
			Toast.makeText(getApplicationContext(), "Selected years should not equal.Please select different year", Toast.LENGTH_SHORT).show();
			return true;
		}else
			return false;
	}
	
	private int downloadChartData(String url){
	
		ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
		StringParameter gradeParam = new StringParameter("grade", selectedGrade);
		StringParameter yearOneParam = new StringParameter("year1", selectedYear1);
		StringParameter yearTwoParam = new StringParameter("year2", selectedYear2);
		params.add(gradeParam);
		params.add(yearOneParam);
		params.add(yearTwoParam);
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
						data = "{\"percentageList\":"+data +"}";
						GradePassFailPercentageList list = AnalyticsParser.parseGradePassFailPercentageListDetails(data);
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

		noData.setVisibility(View.GONE);
		YearWiseComparisionChartView barChart = new YearWiseComparisionChartView();
		List<String> year = new ArrayList<String>();
		year.add(selectedYear1);
		year.add(selectedYear2);
		View barChartView = barChart.getChartView(this, list, year);
		chartsFrameLayout.setVisibility(View.VISIBLE);
		chartLayout.setVisibility(View.VISIBLE);
		chartLayout.removeAllViews();
		chartLayout.addView(barChartView, new LayoutParams(LayoutParams.WRAP_CONTENT,
		          LayoutParams.FILL_PARENT));	
	}
}
