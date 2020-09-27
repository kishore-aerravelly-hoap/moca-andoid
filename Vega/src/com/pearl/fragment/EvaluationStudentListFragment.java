package com.pearl.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.activities.AwaitingEvaluationListActivity;
import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.ExamEvaluationParser;
import com.pearl.parsers.json.StudentDetailsParser;
import com.pearl.ui.models.Student;

// TODO: Auto-generated Javadoc
/**
 * The Class EvaluationStudentListFragment.
 */
public class EvaluationStudentListFragment extends ListFragment {

    /** The handler. */
    private Handler handler;
    
    /** The server requests. */
    private ServerRequests serverRequests;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The exam id. */
    private String examId;
    
    /** The student id. */
    private String studentId;
    
    /** The students list. */
    private List<Student> studentsList;
    
    /** The students list view. */
    private ListView studentsListView;
    
    /** The Constant tag. */
    private static final String tag = "EvaluationStudentListFragment";
    
    /** The student list adapter. */
    StudentListAdapter studentListAdapter;
    private Context context;
    
    private ProgressDialog progressBar;
    
    private View view;


    /* (non-Javadoc)
     * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
    	Logger.warn(tag, "view group :"+container+" inflater :"+inflater);
    	
	view = inflater.inflate(R.layout.evaluation_student_list, container,false);
	studentsListView = (ListView) view.findViewById(android.R.id.list);
	context = getActivity();
	return view;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	studentId = studentsList.get(position).getStudentId();
	final String name = studentsList.get(position).getFirstName();
	((ExamEvaluationSheetFragment) getFragmentManager().findFragmentById(
		R.id.AnswerSheetFragment)).dataFromFragment(studentId, examId, name, true, studentsList.size());
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	Logger.warn(tag, "on resume");
	if (AppStatus.getInstance(getActivity().getApplicationContext()).isOnline(
		getActivity().getApplicationContext())) {
	    downloadStudentsListForEvaluation(examId, getActivity());
	}else{
	    Toast.makeText(getActivity(), R.string.check_internet_connection, 10000).show();
	}
    }

    /**
     * Download students list for evaluation.
     *
     * @param examId the exam id
     * @param activity the activity
     */
    public void downloadStudentsListForEvaluation(String examId, final FragmentActivity activity){
    Logger.warn(tag, "download");
    progressBar = new ProgressDialog(view.getContext());
	progressBar.setCancelable(true);
	progressBar.setMessage("Downloading tests please wait ...");
	progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	progressBar.setProgress(0);
	progressBar.setMax(100);
	progressBar.show();
    	handler = new Handler();
    appData = (ApplicationData)activity.getApplication();
    serverRequests = new ServerRequests(activity);
	final String url = serverRequests.getRequestURL(ServerRequests.EXAM_EVALUATION);
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	final StringParameter sp = new StringParameter("teacherId", appData.getUserId());
	final StringParameter sp1 = new StringParameter("testId", examId);
	params.add(sp);
	params.add(sp1);
	Logger.warn(tag, "url for getting student list is:"+url +" with params: teacherId"+appData.getUserId() 
		+" and testId: "+examId);
	final PostRequestHandler post = new PostRequestHandler(url, params, new DownloadCallback() {

	    @Override
	    public void onSuccess(String downloadedString) {
		final Object data = ExamEvaluationParser.parseEvaluation(downloadedString);
		activity.runOnUiThread(new Runnable() {

		    @Override
		    public void run() {
			getStudentList(data, activity);
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
    }

    /**
     * Gets the student list.
     *
     * @param data the data
     * @param activity the activity
     * @return the student list
     */
    private void getStudentList(Object data, FragmentActivity activity){
	String studentsDetailsList = null;
	if(data != null)
	    studentsDetailsList = data.toString();
	if(studentsDetailsList != null && studentsDetailsList != ""){
	    try {
		studentsList = StudentDetailsParser.getStudentsList(studentsDetailsList);
	    } catch (final JsonProcessingException e) {
		e.printStackTrace();
	    } catch (final IOException e) {
		e.printStackTrace();
	    }
	}
	if(studentsList.size() == 0){
	    final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
	    alert.setTitle("");
	    alert.setMessage("You have evaluated test for all the students");
	    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
		    dialog.cancel();
		    final Intent intent = new Intent(getActivity(), AwaitingEvaluationListActivity.class);
		    startActivity(intent);
		    getActivity().finish();
		}
	    }).show();

	}
	populateStudentsList(activity);
    }

    /**
     * Populate students list.
     *
     * @param activity the activity
     */
    private void populateStudentsList(final FragmentActivity activity){
    	Logger.warn(tag, "setting adapter");
	activity.runOnUiThread(new Runnable() {
		
		@Override
		public void run() {
			studentListAdapter = new StudentListAdapter(activity, 
					android.R.layout.simple_selectable_list_item, studentsList);
			studentsListView.setAdapter(studentListAdapter);
			progressBar.dismiss();
			studentListAdapter.notifyDataSetChanged();
		}
	});
    }

    /**
     * The Class StudentListAdapter.
     */
    private class StudentListAdapter extends ArrayAdapter<Student>{
	
	/** The item. */
	List<Student> item;

	/**
	 * Instantiates a new student list adapter.
	 *
	 * @param context the context
	 * @param textViewResourceId the text view resource id
	 * @param item the item
	 */
	public StudentListAdapter(Context context, int textViewResourceId, List<Student> item) {
	    super(context, textViewResourceId, item);
	    this.item = item;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent){
	    TextView studentName = null;
	    if(view == null){
		final LayoutInflater inflater = (LayoutInflater)this.getContext().
			getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.student_list_row, null);
	    }

	    final Student student = item.get(position);
	    studentName = (TextView)view.findViewById(R.id.student_name);
	    studentName.setText(student.getStudentFullName());
	    studentName.setTextColor(Color.parseColor("#C14F4F"));
	    return view;
	}
    }

    /**
     * Data from fragment activity.
     *
     * @param examId the exam id
     */
    public void dataFromFragmentActivity(String examId){
	this.examId = examId;
    }

 /*   public void dataFromEvaluationSheetFragment(final String downloadedString){
	final Object data = ExamEvaluationParser.parseEvaluation(downloadedString);
	getStudentList(data);
    }*/
    
    /**
  * Download.
  *
  * @param examId the exam id
  * @param activity the activity
  */
 public void download(String examId, FragmentActivity activity){
    	downloadStudentsListForEvaluation(examId, activity);
    }

}
