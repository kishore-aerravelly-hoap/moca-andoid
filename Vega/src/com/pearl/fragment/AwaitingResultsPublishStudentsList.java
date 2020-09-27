package com.pearl.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.activities.TeacherAwaitingResultsFragmentActivity;
import com.pearl.android.ui.ShowProgressBar;
import com.pearl.application.ApplicationData;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.StudentDetailsParser;
import com.pearl.ui.models.BaseResponse;
import com.pearl.ui.models.Student;

// TODO: Auto-generated Javadoc
/**
 * The Class AwaitingResultsPublishStudentsList.
 */
public class AwaitingResultsPublishStudentsList extends ListFragment {

    /** The _no exams. */
    private TextView _noExams;
    
    /** The approve. */
    private Button approve;
    
    /** The tag. */
    private final String tag = "ResultsPublish_StudentsListFragment";
    
    /** The _results publish student listener. */
    private ResultsPublishStudentListInterface _resultsPublishStudentListener;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The server request. */
    private ServerRequests serverRequest;
    
    /** The _students list. */
    private List<Student> _studentsList;
    
    /** The _results publish list. */
    private ListView _resultsPublishList;
    
    /** The handler. */
    private Handler handler;
    
    /** The student id. */
    private String testId, studentId;
    
    /** The approve button flag. */
    private boolean approveButtonFlag;
    
    private Context context;

    /**
     * The Interface ResultsPublishStudentListInterface.
     */
    public interface ResultsPublishStudentListInterface {
	
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
		R.layout.awaitng_results_publish_list_frag, container, false);
	_noExams = (TextView) view
		.findViewById(R.id.result_publish_no_exams_textView);
	appData = (ApplicationData) getActivity().getApplication();
	serverRequest = new ServerRequests(getActivity());
	_resultsPublishList = (ListView) view.findViewById(android.R.id.list);
	approve=(Button)view.findViewById(R.id.approve_awaiting_exam);
	approve.setEnabled(true);
	
	
	handler = new Handler();
	context = getActivity();
	return view;
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	
    approve.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		handler.post(new Runnable() {

		    @Override
		    public void run() {
			approve.setEnabled(false);
			ShowProgressBar.showProgressBar("Please wait..", approveExamToServer(testId), getActivity());
			
		    }
		});
	    }
	});
    }
    
    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	try {
	    _resultsPublishStudentListener = (ResultsPublishStudentListInterface) getActivity();
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
    public void setListener(ResultsPublishStudentListInterface listener) {
	_resultsPublishStudentListener = listener;
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
	approve.setVisibility(View.VISIBLE);
	for(int i=0;i<l.getChildCount();i++) {
	    final View v1=l.getChildAt(i);
	    if(i==position) {

		v1.setBackgroundResource(R.drawable.notice_list_select);	
	    }else {
		v1.setBackgroundResource(0);
	    }
	}
	if (l != null) {
	    // _myApprovalListener.onClick(examDetailsList.get(position).getExamId());

	    Log.i("999999999999999999999999999999", "ON CLICK");
	    ((AwaitngResultsPublishAnswerSheet) getFragmentManager()
		    .findFragmentById(R.id.awaiting_results_publish_answersheet))
		    .dataFromStudentsListFrag(testId,
			    _studentsList.get(position).getStudentId());
	}

    }

    /**
     * Data from results publish_ exam list fragment.
     *
     * @param testId the test id
     * @param approveButtonFlag the approve button flag
     */
    public void dataFromResultsPublish_ExamListFragment(String testId,boolean approveButtonFlag) {
	this.testId = testId;
	this.approveButtonFlag=approveButtonFlag;
	ShowProgressBar.showProgressBar("Please wait fetching student list..", getExamListFormServer(testId), getActivity());
    }
    
    /**
     * Approve exam to server.
     *
     * @param examId the exam id
     * @return the int
     */
    public int approveExamToServer(String examId) {
	Log.i("@@@@@@@@@@@@@@@@@@@@@",
		"EXAM ID IN METHOD IS" + examId.toString());
	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());
	final StringParameter testId = new StringParameter("testId", examId);
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);
	params.add(testId);

	final PostRequestHandler postRequest = new PostRequestHandler(
		serverRequest.getRequestURL(
			ServerRequests.TEACHER_RESULTS_PUBLISH, ""), params,
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
				    .equalsIgnoreCase("SUCCESS")) {
				handler.post(new Runnable() {

				    @Override
				    public void run() {

				    	if(ShowProgressBar.progressBar!=null)
				    		ShowProgressBar.progressBar.dismiss();
				    	try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					Toast.makeText(getActivity(),
						"Results Published",
						Toast.LENGTH_LONG).show();
					
					final Intent i=new Intent(getActivity(),TeacherAwaitingResultsFragmentActivity.class);
					startActivity(i);
					getActivity().finish();

				    }
				});
			    }else if(baseResponse.getStatus().toString().equalsIgnoreCase("FAILURE")) {
		
			    }

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
     * Gets the exam list form server.
     *
     * @param ExamId the exam id
     * @return the exam list form server
     */
    public int getExamListFormServer(String ExamId) {
	Log.i("----", "I m in getExamListFormServer()");

	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());
	final StringParameter studentsTestId = new StringParameter("testId", ExamId);
	final StringParameter type = new StringParameter("type", "Unpublish");
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);
	params.add(studentsTestId);
	params.add(type);
	Log.e("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
		"POST URL iS"
			+ serverRequest
			.getRequestURL(ServerRequests.RESULTS_PUBLISH_STUDENTS_LIST)
			+ " params is   " + appData.getUserId());
	final PostRequestHandler postRequest = new PostRequestHandler(
		serverRequest.getRequestURL(
			ServerRequests.RESULTS_PUBLISH_STUDENTS_LIST, ""),
			params, new DownloadCallback() {

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
					_noExams.setVisibility(View.VISIBLE);
					_resultsPublishList
					.setVisibility(View.GONE);
				    }
				});
			    } else {
			    }
			    if (response.getData() != null) {
				// JSONParser dataParser=new JSONParser();
				_studentsList = StudentDetailsParser
					.getStudentsList(response.getData());
				handler.post(new Runnable() {
				    @Override
				    public void run() {
					//approve.setVisibility(View.VISIBLE);
					_noExams.setVisibility(View.GONE);
					Collections.sort(_studentsList,new Comparator<Student>() {

						@Override
						public int compare(Student lhs, Student rhs) {
							return lhs.getFirstName().toString().compareTo(rhs.getFirstName().toString());
						}
					});
				
					ResultsPublishListadapter adapter=new ResultsPublishListadapter(
						context,
							android.R.layout.simple_selectable_list_item,
							_studentsList);
					_resultsPublishList.setAdapter(adapter);
					ShowProgressBar.progressBar.dismiss();
					adapter.notifyDataSetChanged();
				    }
				});
			    }

			} catch (final Exception e) {
			    Log.e("TeacherApprovalList", "EXCEPTION" + e);
			}

			Log.e("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"@@@@@@@@@@@ in on success");
		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
			// TODO Auto-generated method stub
			Log.e("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"@@@@@@@@@@@ in on progress");

		    }

		    @Override
		    public void onFailure(String failureMessage) {
			// TODO Auto-generated method stub
			Log.e("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"@@@@@@@@@@@ in on Failure");
			ShowProgressBar.progressBar.dismiss();
		    }
		});
	postRequest.request();
	return 100;
    }

    /**
     * The Class ResultsPublishListadapter.
     */
    class ResultsPublishListadapter extends ArrayAdapter<Student> {

	/** The student name. */
	TextView studentName;
	
	/** The _student list. */
	List<Student> _studentList;

	/**
	 * Instantiates a new results publish listadapter.
	 *
	 * @param context the context
	 * @param resource the resource
	 * @param items the items
	 */
	public ResultsPublishListadapter(Context context, int resource,
		List<Student> items) {
	    super(context, resource, items);
	    Log.i(tag, "i m in Adapter------------- CONTSRUCTOR");
	    // TODO Auto-generated constructor stub
	    _studentList = items;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
	    // View view=convertView;
	    Log.i(tag, "i m in Adapter--------------");

	    if (view == null) {
		Log.i(tag, "i m in Adapter");
		final LayoutInflater infalter = (LayoutInflater) this.getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = infalter.inflate(
			R.layout.results_publish_student_list_row, parent,
			false);

	    }
	    studentName = (TextView) view
		    .findViewById(R.id.results_publish_studentName);

	    studentName.setText(_studentList.get(position).getFirstName() + " "
		    + _studentList.get(position).getSecondName());

	    return view;
	}

    }

}
