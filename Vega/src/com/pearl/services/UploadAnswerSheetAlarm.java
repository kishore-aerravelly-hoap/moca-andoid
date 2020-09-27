package com.pearl.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.pearl.application.ApplicationData;
import com.pearl.examination.ExamDetails;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.JSONParser;
import com.pearl.ui.models.BaseRequest;

// TODO: Auto-generated Javadoc
/**
 * The Class UploadAnswerSheetAlarm.
 */
public class UploadAnswerSheetAlarm extends BroadcastReceiver {

    /** The app data. */
    private ApplicationData appData;
    
    /** The tag. */
    private final String tag = "UploadAnswerSheetAlarm";
    
    /** The exams list. */
    private ArrayList<ExamDetails> examsList = null;
    
    /** The content. */
    private String content = null;
    
    /** The wf manager. */
    private WifiManager wfManager;
    
    /** The context. */
    private Context context;

    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
	appData = (ApplicationData)context.getApplicationContext();
	this.context = context;
	Logger.warn(tag, "in onRecieve");
	wfManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	try {
	    if(ApplicationData.isFileExists(appData.getExamsListFile())){
		content = ApplicationData.readFile(appData.getExamsListFile());				
	    }
	} catch (final IOException e) {
	    Logger.error(tag, e);
	}
	try {
	    Logger.warn(tag, "Content before parsing is:"+content);
	    examsList = JSONParser.getExamsList(content);
	    Logger.warn(tag, "object after parsing is:"+examsList);
	} catch (final JsonProcessingException e) {
	    Logger.error(tag, e);
	} catch (final IOException e) {
	    Logger.error(tag, e);
	}

	if(examsList != null)
	    getExamIdsToBeUploaded();
	else
	    Logger.error(tag, "examslist is null");
    }

    /**
     * Gets the exam ids to be uploaded.
     *
     * @return the exam ids to be uploaded
     */
    private void getExamIdsToBeUploaded(){
	for(int i=0; i<examsList.size(); i++){
	    Logger.warn(tag, "retakable? :"+examsList.get(i).isRetakeable());
	    if(!examsList.get(i).isRetakeable()){
		if (ApplicationData.isFileExists(appData
			.getExamSubmittedAnswersFileName(examsList.get(i)
				.getExamId()))
				&& !ApplicationData.isFileExists(appData
					.getExamUploadedAnswersFileName(examsList
						.get(i).getExamId()))){
		    uploadAnswerSheetToServer(examsList.get(i)
			    .getExamId());
		}
	    }else
		Logger.warn(tag, " exam is retakable");
	}
    }

    /**
     * Upload answer sheet to server.
     *
     * @param examId the exam id
     */
    private void uploadAnswerSheetToServer(final String examId){

	if (wfManager.isWifiEnabled()) {
	    {
		final ArrayList<RequestParameter> postParams = new ArrayList<RequestParameter>();

		final String submitFilePath = appData.getExamSubmittedAnswersFileName(examId);
		final BaseRequest baseRequest = new BaseRequest();
		baseRequest.setAuth(appData.getAuthID());

		String examJsonString = "";
		try {
		    examJsonString = ApplicationData
			    .readFile(submitFilePath);

		} catch (final Exception e) {
		    Logger.error(tag, e);
		}

		Logger.info(tag,
			"EXAM - json for submitting exam is"
				+ examJsonString);

		baseRequest.setData(examJsonString);

		final ObjectMapper mapper = new ObjectMapper();
		String examSubmitRequestJsonString = "";

		try {
		    examSubmitRequestJsonString = mapper
			    .writeValueAsString(baseRequest);
		} catch (final Exception e) {
		    Logger.error(tag, e);
		}

		Logger.info(tag,
			examSubmitRequestJsonString);

		final StringParameter sp = new StringParameter(
			"postdata",
			examSubmitRequestJsonString);
		postParams.add(sp);

		final ServerRequests sr = new ServerRequests(
			context);

		try {
		    final PostRequestHandler postRequestObj = new PostRequestHandler(
			    sr.getRequestURL(ServerRequests.EXAM_SUBMIT),
			    postParams,
			    new DownloadCallback() {
				@Override
				public void onSuccess(
					String downloadedString) {
				    Logger.info(tag,
					    downloadedString);

				    final File submittedFile = new File(
					    appData.getExamSubmittedAnswersFileName(examId));
				    final File uploadedFile = new File(
					    appData.getExamUploadedAnswersFileName(examId));
				    Logger.warn(
					    "File is Existed---->",
					    "true"
						    + appData
						    .getExamUploadedAnswersFileName(examId));
				    submittedFile
				    .renameTo(uploadedFile);
				}

				@Override
				public void onProgressUpdate(
					int progressPercentage) {

				}

				@Override
				public void onFailure(
					String failureMessage) {
				    Logger.error(tag, "failed to uplaod");
				}
			    });

		    postRequestObj.request();
		} catch (final Exception e) {
		    Logger.error(tag, e);
		}

	    }
	} else {
	    Logger.error(tag, "No internet connection");
	}

    }
}


