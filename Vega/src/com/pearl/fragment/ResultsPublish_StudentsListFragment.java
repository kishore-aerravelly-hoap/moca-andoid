package com.pearl.fragment;

/**
 * @author spasnoor
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pearl.R;
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
 * The Class ResultsPublish_StudentsListFragment.
 */
public class ResultsPublish_StudentsListFragment extends ListFragment {

    /** The _no exams. */
    private TextView _noExams;
    
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
		R.layout.results_publish_exams_list_fragment, container, false);
	_noExams = (TextView) view
		.findViewById(R.id.result_publish_no_exams_textView);
	appData = (ApplicationData) getActivity().getApplication();
	serverRequest = new ServerRequests(getActivity());
	_resultsPublishList = (ListView) view.findViewById(android.R.id.list);
	handler = new Handler();
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
	    ((ResultsPublishAnswerSheetFragment) getFragmentManager()
		    .findFragmentById(R.id.results_publish_answersheet))
		    .dataFromStudentsListFrag(testId,
			    _studentsList.get(position).getStudentId());
	}

    }

    /**
     * Data from results publish_ exam list fragment.
     *
     * @param testId the test id
     */
    public void dataFromResultsPublish_ExamListFragment(String testId) {
	this.testId = testId;
	getExamListFormServer(testId);
    }

    /**
     * Gets the exam list form server.
     *
     * @param ExamId the exam id
     * @return the exam list form server
     */
    public void getExamListFormServer(String ExamId) {
	Log.i("----", "I m in getExamListFormServer()");

	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());
	final StringParameter studentsTestId = new StringParameter("testId", ExamId);
	final StringParameter type = new StringParameter("type","publish");
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
			    ApplicationData.writeToFile(
				    downloadedString,
				    appData.getTeacherFilePath(appData
					    .getUserId())
					    + ApplicationData.RESULTS_PUBLISH_STUDENTS_LIST);

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
					_noExams.setVisibility(View.GONE);
					if(_studentsList!=null && _studentsList.size()>0) {
						
						Collections.sort(_studentsList,new Comparator<Student>() {

							@Override
							public int compare(Student lhs, Student rhs) {

								return lhs.getFirstName().compareTo(rhs.getFirstName());
							}
						});
					    setListAdapter(new ResultsPublishListadapter(
						    context,
						    android.R.layout.simple_selectable_list_item,
						    _studentsList));
					}
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
		    }
		});
	postRequest.request();
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
		//	view.setBackgroundResource(R.drawable.notice_list_select);
	    }
	    studentName = (TextView) view
		    .findViewById(R.id.results_publish_studentName);

	    studentName.setText(_studentList.get(position).getFirstName() + " "
		    + _studentList.get(position).getSecondName());

	    return view;
	}

    }

}
