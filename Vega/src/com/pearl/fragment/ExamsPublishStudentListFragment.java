package com.pearl.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.activities.AttendanceActivity.ViewHolder;
import com.pearl.android.ui.ShowProgressBar;
import com.pearl.application.ApplicationData;
import com.pearl.examination.ExamDetails;
import com.pearl.fragment.TeacherApprovalListFragment.ApprovalListadapter;
import com.pearl.fragment.TeacherApprovalListFragment.ApprovalListadapter.Holder;
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
 * The Class ExamsPublishStudentListFragment.
 */
public class ExamsPublishStudentListFragment extends ListFragment {

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
    private ProgressDialog progressBar;
    
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
	list=(ListView)view.findViewById(android.R.id.list);
	serverRequest = new ServerRequests(getActivity());
	_resultsPublishList = (ListView) view.findViewById(android.R.id.list);
	handler = new Handler();
	progressBar = new ProgressDialog(view.getContext());
	progressBar.setCancelable(true);
	progressBar.setMessage("Downloading tests please wait...");
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
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	if(AppStatus.getInstance(getActivity().getApplicationContext()).isOnline(getActivity().getApplicationContext())){
		getExamListFormServer();
	}else{
		 Toast.makeText(getActivity(), "Please check Wifi connection", Toast.LENGTH_LONG).show();
			

    }

	new Thread(new Runnable() {

	    @Override
	    public void run() {
		while (progressBarStatus < 100) {

		    progressBarStatus =getExamListFormServer();
		    try {
			Thread.sleep(1000);
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
			//progressBar.dismiss();
		    }

		}

	    }
	}).start();


    }

    /* (non-Javadoc)
     * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	l.setItemChecked(position, true);
	if (l != null) {
	    Log.i("999999999999999999999999999999", "ON CLICK");
	    ((TeacherPublishExamListFragment) getFragmentManager()
		    .findFragmentById(
			    R.id.teacher_exam_publish_frag))
			    .dataFromListFrag(examDetailsList.get(position).getExamId(),examDetailsList.get(position).isExpired());
	}

    }

    /**
     * Gets the exam list form server.
     *
     * @return the exam list form server
     */
    public int getExamListFormServer() {
	Log.i("----", "I m in getExamListFormServer()");

	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);

	final PostRequestHandler postRequest = new PostRequestHandler(
		serverRequest.getRequestURL(
			ServerRequests.TEACHER_PUBLISH_EXAM_LIST, ""), params,
			new DownloadCallback() {

		    ObjectMapper objMapper = new ObjectMapper();
		    BaseResponse baseResponse;
		    @Override
		    public void onSuccess(String downloadedString) {
			// TODO Auto-generated method stub

			try {
			    baseResponse = objMapper.readValue(downloadedString,
				    BaseResponse.class);
			    // TODO Auto-generated method stub
			    if (baseResponse.getMessage().equalsIgnoreCase(
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
			    if (baseResponse.getData() != null) {

				examDetailsList = JSONParser
					.getExamsList(baseResponse.getData());

				handler.post(new Runnable() {
				    @Override
				    public void run() {
					_noExams.setVisibility(View.GONE);
					list.setVisibility(View.VISIBLE);
					sortExamsList();
					progressBar.dismiss();
				    }
				});
			    }else{
			    	progressBar.dismiss();
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
		    	progressBar.dismiss();
			Log.e("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"@@@@@@@@@@@ in on Failure");
		    }
		});
	postRequest.request();
	return 100;
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
	/*@Override
	public int getCount() {
		return this.items.size();
	}*/
	@Override
	public View getView(int position, View view, ViewGroup parent) {
	    // View view=convertView;
	    Log.i(tag, "i m in Adapter--------------");
	    Holder holder=new Holder();
	    if (view == null) {
		Log.i(tag, "i m in Adapter");
		final LayoutInflater infalter = (LayoutInflater) this.getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = infalter.inflate(
			R.layout.results_publish_exams_list_row, parent, false);
		
	    view.setTag(holder);
	    } else {
			holder = (Holder) view.getTag();
		}
	    holder.grade = (TextView) view.findViewById(R.id.results_publish_Grade);
	    holder.section = (TextView) view
		    .findViewById(R.id.results_publish_section);
	    holder.examName = (TextView) view
		    .findViewById(R.id.results_publish_examName);
	    holder.expired = (TextView) view.findViewById(R.id.publish_approvalexpired);
	    holder.grade.setText(_examDetailsList.get(position).getGrade());
	    holder.section.setText(_examDetailsList.get(position).getSection()+",");
	    if(_examDetailsList.get(position).getSection() == null)
	    	holder.section.setText(" ,");
	    else{
	    	String sectionName = _examDetailsList.get(position).getSection();	  
	    	sectionName = (sectionName != null && sectionName.length() > 10 )?sectionName.substring(0, 6)+"..":sectionName;
	    	holder.section.setText(sectionName);
	    	/*List<String> list  = new ArrayList<String>(Arrays.asList(_examDetailsList.get(position).getSection().split(",")));
	    	Logger.warn(tag, "list size is:"+list.size());
	    	String text = (list != null && list.size()>2)?list.get(0)+","+list.get(1)+"...":_examDetailsList.get(position).getSection()+"";
	    	section.setText(text);*/
	    }
	    String exam_name=_examDetailsList.get(position).getTitle();
	    exam_name=(exam_name!=null && exam_name.length()>12)?exam_name.substring(0, 10)+"...":exam_name;
	    holder.examName.setText(exam_name);
	    if(_examDetailsList.get(position).isExpired())
	    	holder.expired.setVisibility(View.VISIBLE);
	    else
	    	holder.expired.setVisibility(View.INVISIBLE);
	    return view;
	}

    }
    
    private void sortExamsList(){

    	//write the functionality here
    	//reinitialize examDetailsList
    	//add all fresh and expired list to examDetailsList
  
		final ArrayList<ExamDetails> freshlist = new ArrayList<ExamDetails>();
		final ArrayList<ExamDetails> expiredlist = new ArrayList<ExamDetails>();
		//use calendar object and get the current date
		final Calendar cal = Calendar.getInstance();
		final Date currentDate = new Date();
    	for (int i=0; i<examDetailsList.size(); i++){
    		//get start date from the examDetailsList
    		final Date startDate = new Date(examDetailsList.get(i).getStartDate());
    		final Date endDate = new Date(examDetailsList.get(i).getEndDate());
    		if(currentDate.after(startDate)||currentDate.before(startDate)){
    			if(currentDate.before(endDate)){
    				freshlist.add(examDetailsList.get(i));
    			}
    			else if(currentDate.after(endDate)){
    		       expiredlist.add(examDetailsList.get(i));
    		       examDetailsList.get(i).setExpired(true);
    			}
    		}
    	}
    	examDetailsList=new ArrayList<ExamDetails>();
    	// sort fresh list and expired list and then add to the final list
    	Collections.sort(freshlist,new Comparator<ExamDetails>() {

			//@Override
			public int compare(ExamDetails lhs, ExamDetails rhs) {
				long startTimeLHS=lhs.getStartDate();
				long endTimeRHS=rhs.getStartDate();
				return String.valueOf(startTimeLHS).compareTo(String.valueOf(endTimeRHS));
			}
		});
    	Collections.sort(expiredlist,new Comparator<ExamDetails>() {

			//@Override
			public int compare(ExamDetails lhs, ExamDetails rhs) {
				long startTimeLHS=lhs.getStartDate();
				long endTimeRHS=rhs.getStartDate();
				return String.valueOf(endTimeRHS).compareTo(String.valueOf(startTimeLHS));
			}
		});
    	examDetailsList.addAll(freshlist);
    	examDetailsList.addAll(expiredlist);
    	setListAdapter(new ResultsPublishListadapter(
			    context,
			    android.R.layout.simple_selectable_list_item,
			    examDetailsList));
		    progressBar.dismiss();
    
    }
    
    public class Holder{
	    
    	TextView grade, section, examName,expired;
    	
    }

}
