/**
 * 
 */
package com.pearl.activities;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.application.VegaConfiguration;
import com.pearl.database.handlers.FeedbackHandler;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ResponseStatus;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.ui.models.BaseRequest;
import com.pearl.ui.models.BaseResponse;
import com.pearl.ui.models.FeedbackDataModel;
import com.pearl.ui.models.StatusType;

// TODO: Auto-generated Javadoc
/**
 * The Class FeedbackActivity.
 */
public class FeedbackActivity extends BaseActivity implements
AdapterView.OnItemSelectedListener {

    /** The list options. */
    private final String[] listOptions = { "Performance", "Ease Of Application",
    "Network Issues" };
    
    /** The feedback data. */
    private String feedbackData;
    
    /** The sync. */
    private final String sync = "no";
    
    /** The selected item. */
    private String selectedItem;
    
    /** The submit feedback. */
    Button submitFeedback;
    
    /** The menu button. */
    Button menuButton;

    /** The feedback list. */
    Spinner feedbackList;
    
    /** The data. */
    EditText data;
    
    /** The feedback handler. */
    FeedbackHandler feedbackHandler;
    
    /** The progress. */
    ProgressDialog progress;
    
    /** The h. */
    public static Handler h;

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.feedback);
	vegaConfig = new VegaConfiguration(this);
	feedbackList = (Spinner) findViewById(R.id.feedbackList);
	// feedbackList.setBackgroundColor(Color.parseColor("#909090"));
	data = (EditText) findViewById(R.id.feedbackData);
	submitFeedback = (Button) findViewById(R.id.submitFeedback);
	// closeFeedback = (ImageView) findViewById(R.id.closeFeedback);
	menuButton = (Button) findViewById(R.id.Feedback_menu_button);
	menuButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub

		final Intent newActivity = new Intent(getApplicationContext(),
			ActionBar.class);
		startActivity(newActivity);

	    }
	});

	h = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
		super.handleMessage(msg);

		switch (msg.what) {

		case 0:
		    finish();
		    break;
		}
	    }
	};
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	if (!appData.isLoginStatus()) {
	    final Intent loginIntent = new Intent(this, LoginActivity.class);
	    startActivity(loginIntent);
	    finish();
	}
	feedbackHandler = new FeedbackHandler(this);

	feedbackList.setOnItemSelectedListener(this);
	final ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, listOptions);
	aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	feedbackList.setAdapter(aa);
	submitFeedback.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		/*
		 * progress=new ProgressDialog(activityContext);
		 * progress.setTitle("Your feedback is being submitted..");
		 * progress.show();
		 */
	    		if(AppStatus.getInstance(activityContext).isOnline(activityContext)){
	    			if(data.getText().toString().trim().length() == 0){
	    	    		Toast.makeText(getApplicationContext(), "Please provide your feedback to submit.", Toast.LENGTH_LONG).show();
	    	    	}else{
	    	    		// show progress bar here
	    	    		submitFeedback();	    	    		
	    	    	}
	    		}
	    		else
	    		    Toast.makeText(activityContext, "Please connect to wifi network",Toast.LENGTH_LONG).show();
	    }
	});

    }

    /**
     * Saves the Feedback in the database.
     */
    private void submitFeedback() {

	selectedItem = feedbackList.getSelectedItem().toString();
	Logger.warn(tag, "selectedItem is:" + selectedItem);
	feedbackData = data.getText().toString();
	Logger.warn(tag, "feedback Data is :" + feedbackData);

	final FeedbackDataModel feedbackModel = new FeedbackDataModel();
	feedbackModel.setFeedbackData(feedbackData);
	feedbackModel.setFeedbackType(selectedItem);
	Logger.warn(tag, "feedback Datamodel is  is :" + feedbackModel);
	feedbackModel.setUserId(appData.getUserId());
	feedbackModel.setDate(new Date());
	final ObjectMapper mapper = new ObjectMapper();
	final BaseRequest baseRequest = new BaseRequest();
	String feedbackJSONstring = null;

	try {
	    feedbackJSONstring = mapper.writeValueAsString(feedbackModel);
	    Logger.warn(tag, "feedback json string is :" + feedbackJSONstring);
	} catch (final JsonGenerationException e) {
	    Logger.error(feedbackJSONstring, e);
	} catch (final JsonMappingException e) {
		Logger.error(feedbackJSONstring, e);
	} catch (final IOException e) {
		Logger.error(feedbackJSONstring, e);
	}

	baseRequest.setData(feedbackJSONstring);
	try {
	    feedbackJSONstring = mapper.writeValueAsString(baseRequest);
	} catch (final JsonGenerationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (final JsonMappingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (final IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	final StringParameter sp = new StringParameter("feedbackData",
		feedbackJSONstring);
	Logger.warn(tag, "feedback json stringParameter is :" + sp);

	final ArrayList<RequestParameter> postParams = new ArrayList<RequestParameter>();
	postParams.add(sp);
	final ServerRequests sr = new ServerRequests(activityContext);

	final PostRequestHandler postRequestObj = new PostRequestHandler(
		sr.getRequestURL(ServerRequests.FEEDBACK_SUBMIT, ""),
		postParams, new DownloadCallback() {

		    @Override
		    public void onSuccess(String downloadedString) {
			Logger.warn(tag, "request sent:" + downloadedString);
			Logger.warn(tag, downloadedString.toString());
			final ObjectMapper mapper = new ObjectMapper();
			BaseResponse response;
			try {
			    response = mapper.readValue(downloadedString,
				    BaseResponse.class);
			    //hide the progress
			    if (response.getData() == null) {
				Logger.info(tag,
					"Attendance parser DATA is NULL");
			    } else {
				Logger.warn(tag, "message from response is:"
					+ response.getMessage());
				Logger.warn(tag, "status from response is:"
					+ response.getStatus());

				response.getStatus();
				if (StatusType.SUCCESS.toString().equals(
					ResponseStatus.SUCCESS)) {
				    runOnUiThread(new Runnable() {

					@Override
					public void run() {
					    Toast.makeText(
						    activityContext,
						    R.string.FEEDBACK_SUBMISION_SUCCESS,
						    Toast.LENGTH_LONG).show();
					    resetFields();
					}
				    });

				} else {
				    response.getStatus();
				    if (StatusType.FAILURE.toString().equals(
					    ResponseStatus.FAIL)) {
					Toast.makeText(
						activityContext,
						R.string.FEEDBACK_SUBMISION_FAILURE,
						Toast.LENGTH_LONG).show();
				    }
				}
			    }
			} catch (final Exception e) {
			    Logger.error(tag, e);
			}
		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {

		    }

		    @Override
		    public void onFailure(String failureMessage) {

		    }
		});

	postRequestObj.request();

    }

    /**
     * Closes the Feedback window.
     */

    /**
     * Sends the Feedback to the Server
     */
    public void sendFeedback() {

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onDestroy()
     */
    @Override
    public void onDestroy() {
	super.onDestroy();
	feedbackHandler.close();
    }

    /* (non-Javadoc)
     * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position,
	    long id) {

    }

    /* (non-Javadoc)
     * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "Feedback";
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
     */
    @Override
    public void onNetworkAvailable() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
     */
    @Override
    public void onNetworkUnAvailable() {
	// TODO Auto-generated method stub

    }
    
    private void resetFields(){
    	data.setText("");
    	final ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
    			android.R.layout.simple_spinner_item, listOptions);
    		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		feedbackList.setAdapter(aa);
    	
    }
}
