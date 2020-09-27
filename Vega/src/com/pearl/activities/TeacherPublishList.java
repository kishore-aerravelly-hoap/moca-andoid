package com.pearl.activities;

/**
 * @author spasnoor
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.examination.ExamDetails;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.JSONParser;
import com.pearl.ui.models.BaseResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class TeacherPublishList.
 */
public class TeacherPublishList extends ListActivity {
    
    /** The publish. */
    private Button publish;
    
    /** The no_test_tp_publish. */
    private TextView no_test_tp_publish;
    
    /** The _publish exam details. */
    private ArrayList<ExamDetails> _publishExamDetails;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The server requests. */
    private 	ServerRequests serverRequests;
    
    /** The checked. */
    private List<String> checked;
    
    /** The adapter. */
    private PublishExamListAdapter adapter;
    
    /** The exam ids. */
    private HashMap<Integer, String> examIds;
    
    /** The progress bar handler. */
    private final Handler progressBarHandler = new Handler();
    
    /** The progress bar status. */
    private int progressBarStatus = 0;
    
    /** The progress bar. */
    ProgressDialog progressBar;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.publish_exams);
	publish = (Button) findViewById(R.id.publish);
	no_test_tp_publish=(TextView)findViewById(R.id.no_test_to_publish);
	_publishExamDetails = new ArrayList<ExamDetails>();
	appData = (ApplicationData) getApplication();
	serverRequests = new ServerRequests(this);
	progressBar = new ProgressDialog(this);
	progressBar.setCancelable(true);
	progressBar.setMessage("Downloading test please wait ...");
	progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	progressBar.setProgress(0);
	progressBar.setMax(100);
	progressBar.show();
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		while (progressBarStatus < 100) {

		    progressBarStatus = requestToServer();
		    try {
			Thread.sleep(500);
		    } catch (final InterruptedException e) {
			e.printStackTrace();
		    }

		    progressBarHandler.post(new Runnable() {

			@Override
			public void run() {
			    progressBar.setProgress(progressBarStatus);
			}
		    });
		    if (progressBarStatus >= 100) {

			try {
			    Thread.sleep(2000);
			} catch (final InterruptedException e) {
			    e.printStackTrace();
			}
			progressBar.dismiss();
		    }

		}

	    }
	}).start();
	publish.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub

		if(examIds!=null && examIds.size()>0){
		    for (int i = 0; i <= examIds.size(); i++) {
			if (examIds != null && examIds.get(i) != null)
			    checked.add(examIds.get(i).toString());
			Log.i("##############","I AM CHECKED  ........+Final ched list is....."+ checked);
		    }
		    Log.i("##############",
			    "I AM CHECKED  ........+Final server list is "
				    + checked);
		    requestToServer(checked);
		}else{
		    Toast.makeText(getApplicationContext(), "Select atleast one test to publish",Toast.LENGTH_LONG).show();
		    Log.i("TeacherPublishList", "NULLL VAUE IN TEST");
		}

	    }
	});

    }

    /**
     * Request to server.
     *
     * @param examId the exam id
     */
    public void requestToServer(List<String> examId) {
	Log.i("@@@@@@@@@@@@@@@@@@@@@",
		"EXAM ID IN METHOD IS" + examId.toString());
	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());
	final StringParameter examIdslist = new StringParameter("examIdslist", examId);
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);
	params.add(examIdslist);

	final PostRequestHandler postRequest = new PostRequestHandler(
		serverRequests.getRequestURL(
			ServerRequests.TEACHER_PUBLISH_SUBMIT, ""), params,
			new DownloadCallback() {

		    ObjectMapper objMapper = new ObjectMapper();
		    BaseResponse baseResponse;

		    @Override
		    public void onSuccess(String downloadedString) {
			// TODO Auto-generated method stub
			try {
			    baseResponse = objMapper.readValue(
				    downloadedString, BaseResponse.class);
			    // _publishExamDetails=JSONParser.getExamsList(baseResponse.getData());

			    if (baseResponse.getStatus().toString()
				    .equalsIgnoreCase("SUCCESS"))
				runOnUiThread(new Runnable() {

				    @Override
				    public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(),
						"YOU ARE DONE",
						Toast.LENGTH_LONG).show();
					checked = new ArrayList<String>();
					final Intent i=new Intent(TeacherPublishList.this,TeacherExamsActivity.class);
					startActivity(i);
					finish();

				    }
				});

			} catch (final Exception e) {
			    Log.e("TeacherPublishList", "@@@@@@@@@@@@ ERROR "
				    + e);
			}
		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void onFailure(String failureMessage) {

			runOnUiThread(new Runnable() {
			    @Override
			    public void run() {
				// Log.e("TeacherPublishList",
				// "@@@@@@@@@@@@ ON FAILURE  ERROR "+failureMessage);
				Toast.makeText(getApplicationContext(),
					"TEST NOT PUBLISHED ",
					Toast.LENGTH_LONG).show();

			    }
			});

		    }
		});
	postRequest.request();
    }

    /**
     * Request to server.
     *
     * @return the int
     */
    public int requestToServer() {
	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);

	final PostRequestHandler postRequest = new PostRequestHandler(
		serverRequests.getRequestURL(
			ServerRequests.TEACHER_PUBLISH_EXAM_LIST, ""), params,
			new DownloadCallback() {

		    ObjectMapper objMapper = new ObjectMapper();
		    BaseResponse baseResponse;

		    @Override
		    public void onSuccess(String downloadedString) {
			// TODO Auto-generated method stub
			try {
			    baseResponse = objMapper.readValue(
				    downloadedString, BaseResponse.class);

			    _publishExamDetails = JSONParser
				    .getExamsList(baseResponse.getData());
			    runOnUiThread(new Runnable() {

				@Override
				public void run() {
				    // TODO Auto-generated method stub
				    if (baseResponse.getMessage().equals(
					    "No Test Paper Found.")
					    && baseResponse.getStatus()
					    .toString()
					    .equals("FAILURE")) {
					no_test_tp_publish.setText("No tests to be published");
					no_test_tp_publish.setVisibility(View.VISIBLE);
					Toast.makeText(getApplicationContext(),
						"No Test Paper Found",
						Toast.LENGTH_LONG).show();
				    } else

					publishExamsList();

				}
			    });

			} catch (final Exception e) {
			    Log.e("TeacherPublishList", "@@@@@@@@@@@@ ERROR "
				    + e);
			}
		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void onFailure(String failureMessage) {

		    }
		});
	postRequest.request();
	return 100;
    }

    /**
     * Publish exams list.
     */
    public void publishExamsList() {
	publish.setVisibility(View.VISIBLE);
	no_test_tp_publish.setVisibility(View.GONE);
	adapter = new PublishExamListAdapter(getApplicationContext(),
		android.R.layout.simple_list_item_activated_1,
		_publishExamDetails);
	setListAdapter(adapter);
	adapter.notifyDataSetChanged();
    }

    /**
     * The Class PublishExamListAdapter.
     */
    public class PublishExamListAdapter extends ArrayAdapter<ExamDetails> {

	/** The check box. */
	CheckBox checkBox;
	
	/** The exam name. */
	TextView examName;
	// ArrayList<ExamDetails> _examDetails=new ArrayList<ExamDetails>();

	/** The _exam details. */
	List<ExamDetails> _examDetails = new ArrayList<ExamDetails>();

	/**
	 * Instantiates a new publish exam list adapter.
	 *
	 * @param context the context
	 * @param resource the resource
	 * @param objects the objects
	 */
	public PublishExamListAdapter(Context context, int resource,
		List<ExamDetails> objects) {
	    super(context, resource, objects);
	    // TODO Auto-generated constructor stub
	    _examDetails = objects;
	    checked = new ArrayList<String>();
	    examIds = new HashMap<Integer, String>();

	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
	    // TODO Auto-generated method stub

	    if (view == null) {
		final LayoutInflater inflator = (LayoutInflater) this.getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflator.inflate(R.layout.publish_exams_list_row, null);
	    }
	    checkBox = (CheckBox) view.findViewById(R.id.publish_check);
	    examName = (TextView) view.findViewById(R.id.publish_exams_name);

	    examName.setText(_examDetails.get(position).getTitle());

	    checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
			boolean isChecked) {
		    // TODO Auto-generated method stub
		    if (isChecked) {
			//checkBox.setBackgroundResource(R.drawable.tc_checkbox_selected);
			examIds.put(position, _examDetails.get(position)
				.getExamId());
			// checked.add(_examDetails.get(position).getExamId());
			Log.i("##############",
				"I AM CHECKED ........+LIST IS....." + examIds);
		    } else {
			//	checkBox.setBackgroundResource(R.drawable.tc_checkbox);
			examIds.remove(position);
			Log.i("##############",
				"I AM CHECKED ........+LIST IS.....after removal"
					+ examIds);
		    }

		}
	    });
	    return view;
	}

	/**
	 * Check all.
	 */
	public void checkAll() {
	    for (int i = 0; i < getListView().getCount(); i++) {
		getListView().setItemChecked(i, checkBox.isChecked());
	    }
	}

	/**
	 * Un check all.
	 */
	public void unCheckAll() {
	    for (int i = 0; i < getListView().getCount(); i++) {
		if (checkBox.isChecked())
		    checkBox.toggle();

	    }
	}

    }

}
