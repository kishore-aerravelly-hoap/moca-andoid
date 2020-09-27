package com.pearl.fragment;

/**
 * @author spasnoor
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.chat.server.response.BaseResponse;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.TeacherTestCommentParser;
import com.pearl.ui.models.TeacherTestComment;

// TODO: Auto-generated Javadoc
/**
 * The Class RejectedComments.
 */
public class RejectedComments extends Fragment implements
android.view.View.OnClickListener {
    
    /** The tag. */
    private final String tag = "RejectedComments";
    
    /** The comments. */
    private Map<String, String> comments;
    
    /** The _server requests. */
    private ServerRequests _serverRequests;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The _teacher comments list. */
    private List<TeacherTestComment> _teacherCommentsList;
    
    /** The _received comments. */
    private TextView _sentComments, _receivedComments;
    
    /** The test id. */
    private String testId = "";
    
    /** The handler. */
    private Handler handler;
    
    /** The view. */
    private View view;
    
    /** The sent. */
    private final String SENT = "Sent";
    
    /** The received. */
    private final String RECEIVED = "Received";

    /** The comments list view. */
    private ListView commentsListView;

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	view = inflater.inflate(R.layout.rejected_comments, container, false);
	_sentComments = (TextView) view.findViewById(R.id.sentComments);
	_receivedComments = (TextView) view.findViewById(R.id.receivedComments);
	appData = (ApplicationData) getActivity().getApplication();
	_serverRequests = new ServerRequests(getActivity());
	_sentComments.setOnClickListener(this);
	_receivedComments.setOnClickListener(this);
	handler = new Handler();
	comments = new HashMap<String, String>();
	view.setVisibility(View.GONE);
	return view;
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.sentComments:
	    Log.i(tag, "i am in onClick of sent comments");
	    getCommentsFromServer(SENT);
	    break;
	case R.id.receivedComments:
	    Log.i(tag, "i am in onClick of received comments");
	    getCommentsFromServer(RECEIVED);
	default:
	    break;
	}
    }

    /**
     * Gets the comments from server.
     *
     * @param type the type
     * @return the comments from server
     */
    private void getCommentsFromServer(final String type) {

	final StringParameter teacherId = new StringParameter("teacherId",
		appData.getUserId());
	final StringParameter examId = new StringParameter("testId", testId);
	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);
	params.add(examId);

	final PostRequestHandler postRequest = new PostRequestHandler(
		_serverRequests.getRequestURL(
			ServerRequests.REJECTED_TEST_COMMENTS, ""), params,
			new DownloadCallback() {


		    ObjectMapper objMapper = new ObjectMapper();
		    BaseResponse response;

		    @SuppressWarnings("unchecked")
		    @Override
		    public void onSuccess(String downloadedString) {
			try {
			    response = objMapper.readValue(downloadedString,
				    BaseResponse.class);

			    if (response.getMessage().equalsIgnoreCase(
				    "Comments are not available")
				    || response.getStatus().equals("FAILURE")) {
				handler.post(new Runnable() {

				    @Override
				    public void run() {
					Log.e(tag, "DATA IS NULL");
					Toast.makeText(
						getActivity()
						.getApplicationContext(),
						"No Comments", Toast.LENGTH_LONG)
						.show();
				    }
				});
			    }
			    if (response.getData() != null) {
				final Object obj = response.getData();
				comments = (Map<String, String>) obj;
				if (comments != null) {
				    final Iterator<String> iterator = comments
					    .keySet().iterator();
				    while (iterator.hasNext()) {
					final String data = iterator.next();
					if(!comments.keySet().contains(type)) {
					    handler.post(new Runnable() {

						@Override
						public void run() {
						    Toast.makeText(
							    getActivity()
							    .getApplicationContext(),
							    "No Comments", Toast.LENGTH_LONG)
							    .show();

						}
					    }) ; 
					}else if(type.equalsIgnoreCase(data)) {
					    final String jsonString = comments
						    .get(data);
					    _teacherCommentsList = TeacherTestCommentParser
						    .getTeacherTestCommentList(jsonString);
					    if (_teacherCommentsList != null
						    && !_teacherCommentsList
						    .isEmpty()) {
						handler.post(new Runnable() {

						    @Override
						    public void run() {
							showComments(_teacherCommentsList);
						    }
						});

					    }
					}
				    }
				}

			    }

			} catch (final Exception e) {
			    Logger.error(tag, e);
			}

			Log.e(tag, "in on success");
		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
		    }

		    @Override
		    public void onFailure(String failureMessage) {
		    }
		});
	postRequest.request();

    }

    /**
     * Data to rejected comments.
     *
     * @param testId the test id
     */
    public void dataToRejectedComments(String testId) {
	view.setVisibility(View.VISIBLE);
	this.testId = testId;
    }

    /**
     * Data from rejected exam.
     *
     * @return the string
     */
    public String dataFromRejectedExam() {
	return testId;
    }

    /**
     * Show comments.
     *
     * @param commentsList the comments list
     */
    public void showComments(List<TeacherTestComment> commentsList) {
	final AlertDialog.Builder _alert = new Builder(getActivity());
	final LayoutInflater infalter = (LayoutInflater) getActivity()
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View view;

	view = infalter.inflate(R.layout.comments_view, null, false);

	commentsListView = (ListView) view
		.findViewById(R.id.comments_view_list);
	((ViewGroup) commentsListView.getParent()).removeView(commentsListView);
	final CommentsListAdapter adapter = new CommentsListAdapter(getActivity(),
		android.R.layout.simple_selectable_list_item, commentsList);
	commentsListView.refreshDrawableState();
	commentsListView.setAdapter(adapter);
	//commentsListView.setBackgroundColor(Color.parseColor("#FFFFFF"));
	_alert.setView(commentsListView);
	_alert.setTitle("Comments");
	_alert.setNeutralButton("Close", new OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {
		dialog.cancel();

	    }
	});

	final AlertDialog dialog = _alert.create();
	dialog.show();

    }

    /**
     * The Class CommentsListAdapter.
     */
    public class CommentsListAdapter extends ArrayAdapter<TeacherTestComment> {
	
	/** The _local comments list. */
	List<TeacherTestComment> _localCommentsList;
	
	/** The _comment view. */
	TextView _commentView;

	/**
	 * Instantiates a new comments list adapter.
	 *
	 * @param context the context
	 * @param resource the resource
	 * @param objects the objects
	 */
	public CommentsListAdapter(Context context, int resource,
		List<TeacherTestComment> objects) {
	    super(context, resource, objects);
	    _localCommentsList = objects;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View view = convertView;

	    if (view == null) {
		final LayoutInflater inflator = getActivity().getLayoutInflater();
		view = inflator.inflate(R.layout.comments_list_row, null);

		_commentView = (TextView) view
			.findViewById(R.id.comments_view_text);

		_commentView.setText(_localCommentsList.get(position)
			.getComment());
	    }

	    return view;
	}
    }

}
