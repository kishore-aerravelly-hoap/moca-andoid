package com.pearl.fragment;
/**
 * @author spasnoor
 */

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
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.JSONParser;
import com.pearl.ui.models.BaseResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class TeacherApprovalListFragment.
 */
@SuppressWarnings("unused")
public class TeacherApprovalListFragment extends ListFragment {

    /** The tag. */
    private final String tag = getClass().getSimpleName().toString();

    /** The _my approval listener. */
    private	ApprovalListListener _myApprovalListener;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The server request. */
    private ServerRequests serverRequest;
    
    /** The exam details list. */
    private ArrayList<ExamDetails> examDetailsList;
    
    /** The _ my list. */
    private	 ListView _MyList;
    
    /** The noexams. */
    private TextView noexams;
    
    /** The handler. */
    private Handler handler;
    
    /** The progress bar. */
    private ProgressDialog progressBar;
    
    /** The progress bar status. */
    private int progressBarStatus = 0;
    
    /** The progress bar handler. */
    private final Handler progressBarHandler = new Handler();
    
    private Context context;
     
    private ExamDetails examDetails;
    /**
     * The listener interface for receiving approvalList events.
     * The class that is interested in processing a approvalList
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addApprovalListListener<code> method. When
     * the approvalList event occurs, that object's appropriate
     * method is invoked.
     *
     * @see ApprovalListEvent
     */
    public interface ApprovalListListener {
	
	/**
	 * On click.
	 *
	 * @param s the s
	 */
	public void onClick(String s);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);

	try {
	    _myApprovalListener = (ApprovalListListener) activity;
	} catch (final ClassCastException e) {
	    Log.e(tag, "------ClassCastException due to " + e);
	}
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	final View view = inflater.inflate(R.layout.teacher_approval_fragment,
		container, false);
	examDetailsList = new ArrayList<ExamDetails>();
	appData = (ApplicationData) getActivity().getApplication();
	serverRequest = new ServerRequests(getActivity());
	_MyList = (ListView) view.findViewById(android.R.id.list);
	noexams = (TextView) view.findViewById(R.id.noexams_frag);
	handler = new Handler();

	progressBar = new ProgressDialog(view.getContext());
	progressBar.setCancelable(true);
	progressBar.setMessage("Downloading tests please wait ...");
	progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	progressBar.setProgress(0);
	progressBar.setMax(100);
	context = getActivity();
	return view;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
	super.onListItemClick(l, v, position, id);
	l.setItemChecked(position, true);
	
	//context = getActivity();
	if (l != null) {
	    // _myApprovalListener.onClick(examDetailsList.get(position).getExamId());

	    Log.i("999999999999999999999999999999", "ON CLICK");
		((ExamToBeApproved) getFragmentManager().findFragmentById(R.id.teacher_exam_tobe_approved_frag))
		    .dataFromListFrag(examDetailsList.get(position).getExamId(),examDetailsList.get(position).isExpired());
	}
	//progressBar.dismiss();
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
    public void setListener(ApprovalListListener listener) {
	_myApprovalListener = listener;
    }

    /**
     * Enable persistent selection.
     */
    public void enablePersistentSelection() {
	getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	// savedInstanceState.getString("1");

    }

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	if(AppStatus.getInstance(getActivity().getApplicationContext()).isOnline(getActivity().getApplicationContext())){
		progressBar.show();
		getExamListFormServer();
	}else{
			 Toast.makeText(getActivity(), "Please check Wifi connection", Toast.LENGTH_LONG).show();
				

	    }

	new Thread(new Runnable() {

	    @Override
	    public void run() {
		while (progressBarStatus < 100) {

		    progressBarStatus = 100;
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
	Log.e("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
		"POST URL iS"
			+ serverRequest
			.getRequestURL(ServerRequests.EXAMS_LIST_TEACHER)
			+ " params is   " + appData.getUserId());
	final PostRequestHandler postRequest = new PostRequestHandler(
		serverRequest.getRequestURL(ServerRequests.EXAMS_LIST_TEACHER,
			""), params, new DownloadCallback() {

		    ObjectMapper objMapper = new ObjectMapper();
		    BaseResponse response;

		    @Override
		    public void onSuccess(String downloadedString) {

			try {
			    ApplicationData.writeToFile(
				    downloadedString,
				    appData.getTeacherFilePath(appData
					    .getUserId())
					    + ApplicationData.TEACHER_APPROVAL_EXAM_LIST);

			    response = objMapper.readValue(downloadedString,
				    BaseResponse.class);
			    if (response.getMessage().equalsIgnoreCase(
				    "No Test Paper Found.")) {
				handler.post(new Runnable() {

				    @Override
				    public void run() {
					noexams.setText("No tests for you..");
					noexams.setVisibility(View.VISIBLE);
					_MyList.setVisibility(View.GONE);
				    }
				});
			    } else {
			    }
			    if (response.getData() != null) {
				// JSONParser dataParser=new JSONParser();
				examDetailsList = JSONParser
					.getExamsList(response.getData());
				
				handler.postDelayed(new Runnable() {
				    @Override
				    public void run() {
					noexams.setVisibility(View.GONE);
					sortExamsList();
					//make a call to sortExamsList
					
					if(examDetailsList!=null || examDetailsList.size()>0){
						
						Logger.info(tag, "exam details after parsing is : "+examDetailsList);
					}

				    }
				},10);
				progressBar.dismiss();
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
			Log.e("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"@@@@@@@@@@@ in on progress");

		    }

		    @Override
		    public void onFailure(String failureMessage) {
			Log.e("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",
				"@@@@@@@@@@@ in on Failure");
			progressBar.dismiss();
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					progressBar.dismiss();
					Toast.makeText(getActivity().getApplicationContext(), R.string.Unable_to_reach_pearl_server, Toast.LENGTH_SHORT).show();
				}
			});
		    }
		});
	postRequest.request();
	return 100;
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
    	setListAdapter(new ApprovalListadapter(
			    context,
			    android.R.layout.simple_selectable_list_item,
			    examDetailsList));
		    progressBar.dismiss();
    }

    /**
     * The Class ApprovalListadapter.
     */
    class ApprovalListadapter extends ArrayAdapter<ExamDetails> {

	/** The exam name. */
	TextView grade, section, examName,expired;
	
	/** The _exam details list. */
	List<ExamDetails> _examDetailsList;

	/**
	 * Instantiates a new approval listadapter.
	 *
	 * @param context the context
	 * @param resource the resource
	 * @param items the items
	 */
	public ApprovalListadapter(Context context, int resource,
		List<ExamDetails> items) {
	    super(context, resource, items);
	    Log.i("-----", "i m in Adapter------------- CONTSRUCTOR");
	    _examDetailsList = items;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	 public class Holder{
		    
	    	TextView grade, section, examName,expired;
	    	
	    }
	@Override
	public View getView(int position, View view, ViewGroup parent) {
	    // View=new view();
	    Log.i("-----", "i m in Adapter--------------");
	    Holder holder=new Holder();
	    if (view == null) {
		Log.i("-----", "i m in Adapter");
		final LayoutInflater infalter = (LayoutInflater) this.getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = infalter.inflate(R.layout._list_row, parent, false);
	    //make a call to holder class and create an instance of the class 
		holder.grade = (TextView) view.findViewById(R.id.approvalGrade);
		holder.section = (TextView) view.findViewById(R.id.approvalsection);
		holder.examName = (TextView) view.findViewById(R.id.approvalexamName);
		holder.expired =(TextView)view.findViewById(R.id.approvalexpired);
	    view.setTag(holder);
	    }else {
	    	holder = (Holder) view.getTag();
	}
		holder.grade.setText(_examDetailsList.get(position).getGrade());
	  // grade.setText(_examDetailsList.get(position).getGrade());
	    Logger.warn(tag, "sectionnnnnnnnnnnnnnnnn"+_examDetailsList.get(position).getSection());
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
    
    //write a class holder
    // declare the view that are designed in the xml that is used as layout in the list
   
}
