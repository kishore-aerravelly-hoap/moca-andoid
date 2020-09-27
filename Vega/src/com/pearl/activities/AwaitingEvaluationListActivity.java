package com.pearl.activities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.android.ui.ShowProgressBar;
import com.pearl.application.ApplicationData;
import com.pearl.application.VegaConstants;
import com.pearl.examination.ExamDetails;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.JSONParser;
import com.pearl.parsers.json.EvaluationListParser;

// TODO: Auto-generated Javadoc
/**
 * The Class AwaitingEvaluationListActivity.
 */
public class AwaitingEvaluationListActivity extends BaseActivity {

    /** The evaluation list. */
    private ListView evaluationList;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The server requests. */
    private ServerRequests serverRequests;
    
    /** The exam details. */
    private ArrayList<ExamDetails> examDetails;
    
    /** The exam id. */
    private String examId;
    
    /** The menu. */
    private ImageView menu;
    
    /** The refresh. */
    private ImageView refresh,help;
    
    private int i=0;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.awaiting_evaluation_list);
	serverRequests = new ServerRequests(this);
	appData = (ApplicationData)getApplication();
	evaluationList = (ListView) findViewById(R.id.awaiting_evaluation_list);
	menu = (ImageView) findViewById(R.id.menu_button_eval);
	refresh = (ImageView) findViewById(R.id.refreshevaluationList);
	help = (ImageView) findViewById(R.id.teacher_approval_help);

	menu.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			final Intent i = new Intent(
					AwaitingEvaluationListActivity.this, ActionBar.class);
			startActivity(i);
		}
	});

	if (AppStatus.getInstance(activityContext).isOnline(
		activityContext)) {
	    downloadEvaluationList();
		ShowProgressBar.showProgressBar("Getting exams list...Please wait..", downloadEvaluationList(), AwaitingEvaluationListActivity.this); 

	}else{
	    Toast.makeText(this, R.string.check_internet_connection, Toast.LENGTH_LONG).show();
	}
	bindEvents();
    }
    
    
    /**
     * Show dialog.
     */
    private void showDialog(){
   	    i = 0;
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pearl_tips_layout);
		RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.tips_layout);
		final TextView tips = (TextView) dialog.findViewById(R.id.tips);
		ImageView previous = (ImageView) dialog.findViewById(R.id.previous);
		ImageView next = (ImageView) dialog.findViewById(R.id.next);
		layout.setBackgroundResource(R.drawable.attendance_help);
		final List<String> list = HelpParser.getHelpContent("Awating_evaluation.txt", this);
		if(list != null && list.size() > 0)
			tips.setText(list.get(0));
		
		next.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(list != null && list.size() > 0){
							if(i < (list.size() - 1)){
								i = i +1;
								tips.setEnabled(true);
								tips.setText(list.get(i));						
							}
							else
								tips.setEnabled(false);
						}
					}
				});
				previous.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(list != null && list.size() > 0){
							if(i > 0){
								i = i -1;
								tips.setEnabled(true);
								tips.setText(list.get(i));						
							}
							else
								tips.setEnabled(false);
						}
					}
				});
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			
		
		}

    /**
     * Download evaluation list.
     *
     * @return the int
     */
    private int downloadEvaluationList(){
	final String url = serverRequests.getRequestURL(ServerRequests.AWAITING_EXAM_EVALUATION_LIST);
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	final StringParameter sp = new StringParameter("teacherId", appData.getUserId());
	params.add(sp);
	Logger.warn(tag, "url for awaiting evaluation list is:"+url +" with params:"+appData.getUserId());
	final PostRequestHandler post = new PostRequestHandler(url, params, new DownloadCallback() {

	    @Override
	    public void onSuccess(String downloadedString) {
		final Object data = EvaluationListParser.parseEvaluationList(downloadedString);
		runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
			getEvaluationList(data);						
		    }
		});
	    }

	    @Override
	    public void onProgressUpdate(int progressPercentage) {

	    }

	    @Override
	    public void onFailure(String failureMessage) {

	    }
	});
	post.request();
	return 100;
    }

    /**
     * Gets the evaluation list.
     *
     * @param data the data
     * @return the evaluation list
     */
    private void getEvaluationList(Object data){
	try {
	    if(data != null && data != ""){
		examDetails = JSONParser.getExamsList(data.toString());
		populateEvaluationList();		
	    }else{
		Toast.makeText(this, R.string.no_exams_to_be_evaluated, toastDisplayTime).show();
	    }
	} catch (final JsonProcessingException e) {
	    e.printStackTrace();
	} catch (final IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Populate evaluation list.
     */
    private void populateEvaluationList(){
    	Collections.sort(examDetails, new Comparator<ExamDetails>() {

			@Override
			public int compare(ExamDetails lhs, ExamDetails rhs) {
				
				return String.valueOf(lhs.getStartDate()).compareTo(String.valueOf(rhs.getStartDate()));
			}
		});
    	
	final EvaluationListAdapter adapter = new EvaluationListAdapter(this, 
		android.R.layout.simple_selectable_list_item, examDetails);
	evaluationList.setAdapter(adapter);
	adapter.notifyDataSetChanged();
	if(ShowProgressBar.progressBar!=null)
	ShowProgressBar.progressBar.dismiss();
    }

    /**
     * The Class EvaluationListAdapter.
     */
    private class EvaluationListAdapter extends ArrayAdapter<ExamDetails>{
	
	/** The item. */
	List<ExamDetails> item;

	/**
	 * Instantiates a new evaluation list adapter.
	 *
	 * @param context the context
	 * @param textViewResourceId the text view resource id
	 * @param item the item
	 */
	public EvaluationListAdapter(Context context, int textViewResourceId, List<ExamDetails> item) {
	    super(context, textViewResourceId, item);
	    this.item = item;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent){
	    if(view == null){
		final LayoutInflater inflater = (LayoutInflater)this.getContext().
			getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.awaiting_evaluation_list_row, null);
	    }
	    final ExamDetails examDetails = item.get(position);
	    final TextView examTitle = (TextView)view.findViewById(R.id.awaiting_exam_title);
	    final TextView subject = (TextView)view.findViewById(R.id.awaiting_subject);
	    final TextView grade = (TextView)view.findViewById(R.id.awaitng_grade);
	    final TextView examDate = (TextView)view.findViewById(R.id.awaiting_exam_date);
	    final TextView section = (TextView)view.findViewById(R.id.awaitng_section);
	    final TextView category = (TextView)view.findViewById(R.id.awaiting_exam_category);
	    final TextView expired = (TextView)view.findViewById(R.id.awaitng_expired);

	    examTitle.setText(examDetails.getTitle());
	    subject.setText(examDetails.getSubject());
	    grade.setText(examDetails.getGrade());
	    category.setText(examDetails.getExamCategory());

	    final SimpleDateFormat dateFormatter = new SimpleDateFormat(
	    		"EEE, d MMM yyyy");
	    final String startdate = dateFormatter
		    .format(examDetails.getStartDate());
	    final int startDateIndex = startdate.indexOf(" ");
	    examDate.setText(startdate.substring(startDateIndex,
		    startdate.length()));
	    section.setText(examDetails.getSection());
	    return view;
	}
    }

     /**
     * Bind events.
     */
    private void bindEvents(){
	evaluationList.setOnItemClickListener(new OnItemClickListener(){

	    @Override
	    public void onItemClick(AdapterView<?> a, View v, int position,
		    long arg3) {
		examId = examDetails.get(position).getExamId();
		final Intent intent = new Intent(activityContext, ExamEvaluationFragmentActivity.class);
		intent.putExtra(VegaConstants.EXAM_ID, examId);
		startActivity(intent);
	    }
	});
	menu.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		final Intent intent = new Intent(activityContext, ActionBar.class);
		startActivity(intent);
	    }
	});

	refresh.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (AppStatus.getInstance(activityContext).isOnline(
			activityContext)) {
			ShowProgressBar.showProgressBar("Getting exams list...Please wait..",  downloadEvaluationList(), activityContext);
		   ;
		}else{
		    Toast.makeText(activityContext, R.string.check_internet_connection, 1000).show();
		}
	    }
	});
	
	help.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showDialog();
		}
	});
	
	
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "AwaitingEvaluationList";
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

}
