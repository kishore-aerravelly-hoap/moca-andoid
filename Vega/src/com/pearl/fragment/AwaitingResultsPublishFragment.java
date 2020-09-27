package com.pearl.fragment;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.android.ui.ShowProgressBar;
import com.pearl.application.ApplicationData;
import com.pearl.examination.ExamDetails;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.JSONParser;
import com.pearl.ui.models.BaseResponse;
import com.pearl.ui.models.Student;

// TODO: Auto-generated Javadoc
/**
 * The Class AwaitingResultsPublishFragment.
 */
public class AwaitingResultsPublishFragment extends ListFragment {

    /** The _no exams. */
    private TextView _noExams;
    
    /** The tag. */
    private final String tag = "ResultsPublish_ExamListFragment";
    
    /** The _results publish listener. */
    private ResultsPublishInterface _resultsPublishListener;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The server request. */
    private ServerRequests serverRequest;
    
    /** The exam details list. */
    private ArrayList<ExamDetails> examDetailsList;
    
    /** The students list. */
    private List<Student> studentsList;
    
    /** The _results publish list. */
    private ListView _resultsPublishList;
    
    /** The handler. */
    private Handler handler;
    
    /** The list. */
    private ListView list;
    
    /** The progress bar. */
    ProgressDialog progressBar;
    
    /** The progress bar status. */
    private int progressBarStatus = 0;
    
    /** The progress bar handler. */
    private final Handler progressBarHandler = new Handler();
    
    private Context context;

    /**
     * The Interface ResultsPublishInterface.
     */
    public interface ResultsPublishInterface {
	
	/**
	 * On click.
	 *
	 * @param s the s
	 */
	public void onClick(String s);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	final View view = inflater.inflate(
		R.layout.results_publish_exams_list_fragment, container, false);
	_noExams = (TextView) view
		.findViewById(R.id.result_publish_no_exams_textView);
	appData = (ApplicationData) getActivity().getApplication();
	list = (ListView) view.findViewById(android.R.id.list);
	serverRequest = new ServerRequests(getActivity());
	_resultsPublishList = (ListView) view.findViewById(android.R.id.list);
	handler = new Handler(Looper.getMainLooper());
	progressBar = new ProgressDialog(view.getContext());
	progressBar.setCancelable(true);
	progressBar.setMessage("Downloading tests please wait ...");
	progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	progressBar.setProgress(0);
	progressBar.setMax(100);
	progressBar.show();
	context = getActivity();
	return view;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	try {
	    _resultsPublishListener = (ResultsPublishInterface) getActivity();
	} catch (final ClassCastException e) {
	    Log.e(tag, "IN ON ATTACH" + e);
	}
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle state) {
	super.onSaveInstanceState(state);

	state.putInt("1", getListView().getCheckedItemPosition());
    }

    /**
     * Sets the listener.
     *
     * @param listener the new listener
     */
    public void setListener(ResultsPublishInterface listener) {
	_resultsPublishListener = listener;
    }

    /**
     * Enable persistent selection.
     */
    public void enablePersistentSelection() {
	getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	l.setItemChecked(position, true);

	if (l != null) {
	    // _myApprovalListener.onClick(examDetailsList.get(position).getExamId());

	    ((AwaitingResultsPublishStudentsList) getFragmentManager()
		    .findFragmentById(
			    R.id.awaiting_results_publish_students_list_fragment_id))
			    .dataFromResultsPublish_ExamListFragment(examDetailsList
				    .get(position).getExamId(), true);
	}

    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	if(AppStatus.getInstance(getActivity().getApplicationContext()).isOnline(getActivity().getApplicationContext())){
		getExamListFormServer();
	}else{
		 Toast.makeText(getActivity(), "Please check Wifi connection", Toast.LENGTH_LONG).show();
			

    }
	handler.post(new Runnable() {

	    @Override
	    public void run() {
		while (progressBarStatus < 100) {

		    progressBarStatus = getExamListFormServer();
		    try {
			Thread.sleep(100);
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
			    Thread.sleep(200);
			} catch (final InterruptedException e) {
			    e.printStackTrace();
			}
			//progressBar.dismiss();
		    }

		}

	    }
	});
    }

    /**
     * Gets the exam list form server.
     *
     * @return the exam list form server
     */
    public int getExamListFormServer() {

	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);

	final PostRequestHandler postRequest = new PostRequestHandler(
		serverRequest.getRequestURL(
			ServerRequests.TEACHER_AWAITING_RESULTS_PUBLISH_LIST,
			""), params, new DownloadCallback() {

		    ObjectMapper objMapper = new ObjectMapper();
		    BaseResponse response;

		    @Override
		    public void onSuccess(String downloadedString) {
			// TODO Auto-generated method stub

			try {

			    response = objMapper.readValue(downloadedString,
				    BaseResponse.class);
			    // TODO Auto-generated method stub
			    if (response.getMessage().equalsIgnoreCase(
				    "No Test Paper Found.")) {

				handler.post(new Runnable() {

				    @Override
				    public void run() {
					// TODO Auto-generated method stub
					_noExams.setText("No tests for you..");
					list.setVisibility(View.GONE);
					_noExams.setVisibility(View.VISIBLE);
					_resultsPublishList
					.setVisibility(View.GONE);
				    }
				});
			    }
			    if (response.getData() != null) {

				/*
				 * //Object[]
				 * object=(Object[])response.getData(); String
				 * examdetails=(String)object[0]; String
				 * studentList=(String)object[1];
				 */

				examDetailsList = JSONParser
					.getExamsList(response.getData());

				handler.post(new Runnable() {
				    @Override
				    public void run() {
					_noExams.setVisibility(View.GONE);
					list.setVisibility(View.VISIBLE);
					setListAdapter(new ResultsPublishListadapter(
						context,
						android.R.layout.simple_selectable_list_item,
						examDetailsList));
					progressBar.dismiss();
				    }
				});
			    }else{
			    	progressBar.dismiss();
			    }

			}catch(JsonMappingException je) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getActivity(), "Download error.Contact Administrator",Toast.LENGTH_LONG).show();						
					}
				});
			}
			catch (final Exception e) {
			   
			    Logger.error("TeacherApprovalList", e);
			}

		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void onFailure(String failureMessage) {
		    	
			// TODO Auto-generated method stub
		    	progressBar.dismiss();
		    }
		});
	postRequest.request();
	return 100;
    }

    /**
     * Data from students list.
     */
    public void dataFromStudentsList() {
	getExamListFormServer();
    }

    /**
     * The Class ResultsPublishListadapter.
     */
    class ResultsPublishListadapter extends ArrayAdapter<ExamDetails> {

	/** The exam name. */
	TextView grade, section, examName;
	
	/** The _exam details list. */
	List<ExamDetails> _examDetailsList;

	/**
	 * Instantiates a new results publish listadapter.
	 *
	 * @param context the context
	 * @param resource the resource
	 * @param items the items
	 */
	public ResultsPublishListadapter(Context context, int resource,
		List<ExamDetails> items) {
	    super(context, resource, items);
	    Log.i(tag, "i m in Adapter------------- CONTSRUCTOR");
	    // TODO Auto-generated constructor stub
	    _examDetailsList = items;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
	    // View view=convertView;

	    if (view == null) {
		final LayoutInflater infalter = (LayoutInflater) this.getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = infalter.inflate(
			R.layout.results_publish_exams_list_row, parent, false);

	    }
	    grade = (TextView) view.findViewById(R.id.results_publish_Grade);
	    section = (TextView) view
		    .findViewById(R.id.results_publish_section);
	    examName = (TextView) view
		    .findViewById(R.id.results_publish_examName);
	    grade.setText(_examDetailsList.get(position).getGrade());
	    if(_examDetailsList.get(position).getSection() == null)
	    	section.setText(" ,");
	    else{
	    	String sectionName = _examDetailsList.get(position).getSection();	  
	    	sectionName = (sectionName != null && sectionName.length() > 8 )?sectionName.substring(0, 7)+"..":sectionName;
	    	section.setText(sectionName);
	    	/*List<String> list  = new ArrayList<String>(Arrays.asList(_examDetailsList.get(position).getSection().split(",")));
	    	Logger.warn(tag, "list size is:"+list.size());
	    	String text = (list != null && list.size()>2)?list.get(0)+","+list.get(1)+"..":_examDetailsList.get(position).getSection()+"";
	    	section.setText(text);*/
	    }
	    String exam_name=_examDetailsList.get(position).getTitle();
	    exam_name=(exam_name!=null && exam_name.length()>12)?exam_name.substring(0, 12)+"..":exam_name;
	    examName.setText(exam_name);
	    return view;
	}

    }

}
