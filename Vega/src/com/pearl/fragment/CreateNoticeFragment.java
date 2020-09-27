package com.pearl.fragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.activities.ActionBar;
import com.pearl.activities.TeacherNoticeCreate;
import com.pearl.activities.AttendanceActivity.ViewHolder;
import com.pearl.android.ui.ShowProgressBar;
import com.pearl.application.ApplicationData;
import com.pearl.chat.server.response.BaseResponse;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.request.ServerRequests;
import com.pearl.network.request.handlers.PostRequestHandler;
import com.pearl.network.request.parameters.RequestParameter;
import com.pearl.network.request.parameters.StringParameter;
import com.pearl.parsers.json.NoticeBoardParser;
import com.pearl.ui.models.Notice;
import com.pearl.ui.models.StatusType;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateNoticeFragment.
 */
public class CreateNoticeFragment extends Fragment {

    /** The menu button. */
    private Button createNotice, myNotices, receivedNotice, menuButton;
    
    /** The notice_to_details. */
    private TextView noticeBoard, notice_title, notice_dueDate,
	    notice_to_details;
    
    /** The sent notices. */
    private ListView sentNotices;
    
    /** The help. */
    private ImageView notice_highlight_sent, notice_highlight_received,help;
    
    /** The inflater. */
    private LayoutInflater inflater;
    
    /** The view. */
    private View view;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The server requests. */
    private ServerRequests serverRequests;
    
    /** The _handler. */
    private Handler _handler;
    
    /** The items. */
    private List<Notice> items;
    
    /** The position. */
    private int position;
    
    
    /** The notice click value. */
    private int noticeClickValue = 0;
    
    /** The notice type. */
    private String noticeType;
    
    private int noticeClickedPosition;

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	final View view = inflater.inflate(R.layout.teacher_noticeboard,
		container, false);
	createNotice = (Button) view.findViewById(R.id.create_notice);
	sentNotices = (ListView) view.findViewById(R.id.notices_list);
	menuButton = (Button) view.findViewById(R.id.LasallemenuButton);
	help = (ImageView) view.findViewById(R.id.teacher_notice_help);
	appData = (ApplicationData) getActivity().getApplication();
	serverRequests = new ServerRequests(getActivity());
	myNotices = (Button) view.findViewById(R.id.sent_notices);
	receivedNotice = (Button) view.findViewById(R.id.received_notice);
	noticeBoard = (TextView) view.findViewById(R.id.noticeBoard);
	notice_title = (TextView) view.findViewById(R.id.notice_title);
	notice_dueDate = (TextView) view.findViewById(R.id.notice_due_date);
	notice_to_details = (TextView) view
		.findViewById(R.id.notice_to_details);

	notice_highlight_sent = (ImageView) view
		.findViewById(R.id.notice_highlight_sent);
	notice_highlight_received = (ImageView) view
		.findViewById(R.id.notice_highlight_received);
	_handler = new Handler();
	noticeBoard.setScrollContainer(true);
	noticeBoard.setMovementMethod(ScrollingMovementMethod.getInstance());
	notice_title.setMovementMethod(ScrollingMovementMethod.getInstance());
	noticeBoard.setText("Welcome " + "\n"
		+ appData.getUser().getSecondName());
	noticeBoard.setScrollContainer(true);

	bindEvents();
	
	receivedNotice.post(new Runnable() {
		
		@Override
		public void run() {
			receivedNotice.performClick();
			
		}
	});

	return view;
    }

    /**
     * Bind events.
     */
    private void bindEvents() {

	menuButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		final Intent i = new Intent(getActivity(), ActionBar.class);
		startActivity(i);
	    }
	});
	createNotice.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		if (!AppStatus.getInstance(getActivity()).isOnline(
			getActivity())) {
		    noticeBoard.setText("Please check your wifi connection");
		    Toast.makeText(getActivity(),
			    "Please check your wifi connection",
			    Toast.LENGTH_LONG).show();
		} else {
		    final Intent i = new Intent(getActivity()
			    .getApplicationContext(), TeacherNoticeCreate.class);
		    startActivity(i);
		}
	    }
	});
	myNotices.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		notice_dueDate.setText("");
		noticeBoard.setText("");
		notice_title.setText("");
		notice_to_details.setText("");
		notice_highlight_received.setVisibility(View.GONE);
		notice_highlight_sent.setVisibility(View.VISIBLE);
		if (!AppStatus.getInstance(getActivity()).isOnline(
			getActivity())) {
		    noticeBoard.setText("Please check your wifi connection");
			myNotices.setBackgroundResource(R.drawable.sent_notices_h);
			receivedNotice.setBackgroundResource(R.drawable.received_notices);
		    Toast.makeText(getActivity(),
			    "Please check your wifi connection",
			    Toast.LENGTH_LONG).show();
		}

		else {
		    ShowProgressBar.showProgressBar("Downloading notices",
			    requestToServer("sentNotices"), getActivity());
		}
	    }
	});

	receivedNotice.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		
		notice_dueDate.setText("");
		noticeBoard.setText("");
		notice_title.setText("");
		notice_to_details.setText("");
		notice_highlight_sent.setVisibility(View.GONE);
		notice_highlight_received.setVisibility(View.VISIBLE);
		if (!AppStatus.getInstance(getActivity()).isOnline(
			getActivity())) {
			receivedNotice.setBackgroundResource(R.drawable.received_notices_h);
			myNotices.setBackgroundResource(R.drawable.sent_notices);
		    noticeBoard.setText("Please check your wifi connection");
		    Toast.makeText(getActivity(),
			    "Please check your wifi connection",
			    Toast.LENGTH_LONG).show();
		} else {
		    ShowProgressBar.showProgressBar("Downloading notices",
			    requestToServer("userNotices"), getActivity());
		}
	    }
	});
	sentNotices.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
	    	/*noticeBoard.scrollTo(0, 0);
	    	notice_title.scrollTo(0, 0);*/
		if (TextUtils.equals(noticeType, "sentNotices")) {
		    notice_to_details.setText("Click here to know recipients ");
		    notice_to_details.setVisibility(View.VISIBLE);
		    notice_to_details.bringToFront();
		} else {
		    notice_to_details.setText("Click here to know sender");

		    notice_to_details.setVisibility(View.VISIBLE);
		    notice_to_details.bringToFront();
		}
		notice_to_details.setVisibility(View.VISIBLE);
		notice_title.setText(items.get(position).getTitle());
		noticeBoard.setText(items.get(position).getMsg());
		final SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss ");
		notice_dueDate.setText("This notice is valid upto "
			+ localSimpleDateFormat.format(items.get(position)
				.getDueDate()));
		CreateNoticeFragment.this.position = position;
	    }
	});

	notice_to_details.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		TextView from, to;
		GridView to_view;
		List<String> list=new ArrayList<String>();

		final AlertDialog.Builder alert = new AlertDialog.Builder(
			getActivity());

		inflater = (LayoutInflater) getActivity().getSystemService(
			Context.LAYOUT_INFLATER_SERVICE);
		view = inflater
			.inflate(R.layout.sent_notices_dialog_view, null);
		from = (TextView) view.findViewById(R.id.notice_from);
		to = (TextView) view.findViewById(R.id.notice_to);
		to.setMovementMethod(ScrollingMovementMethod.getInstance());
		to.setVerticalScrollBarEnabled(true);
		
		to_view=(GridView)view.findViewById(R.id.notice_to_spinner_selector);
		alert.setView(view);
		if (TextUtils.equals(noticeType, "sentNotices")) {
			from.setVisibility(View.GONE);
		    to.setVisibility(View.VISIBLE);
		    to_view.setVisibility(View.VISIBLE);
		    if (items.get(position).getUserToName().endsWith(",")){
		    	to.setText("To : ");
		    	list= Arrays.asList(items.get(position).getUserToName().split(","));
		    	ArrayAdapter<String> adp=new ArrayAdapter<String> (getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,list);
		    	to_view.setAdapter(adp);
		    }
		    else{
		    	to.setText("To : "
						+ items.get(position).getUserToName());
		    	to_view.setVisibility(View.GONE);
		    }
			
		} else if (TextUtils.equals(noticeType, "userNotices")) {
			from.setVisibility(View.VISIBLE);
		    to.setVisibility(View.GONE);
		    to_view.setVisibility(View.GONE);
		    if (items.get(position).getUserFromName().endsWith(","))
			from.setText("From : "
				+ items.get(position)
					.getUserFromName()
					.substring(
						0,
						items.get(position)
							.getUserFromName()
							.length() - 1));
		    else
			from.setText("From : "
				+ items.get(position).getUserFromName());

		}
		//alert.setTitle("My Notices");
		final AlertDialog dialog = alert.create();
		dialog.setCancelable(true);
		dialog.show();

	    }
	});
	
	help.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {/*
			Dialog dialog = new Dialog(getActivity().getApplicationContext());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.pearl_tips_layout);
			RelativeLayout layout = (RelativeLayout)dialog.findViewById(R.id.tips_layout);
			layout.setBackgroundResource(R.id.)
		*/}
	});

    }

     /**
      * Request to server.
      *
      * @param type the type
      * @return the int
      */
     private int requestToServer(final String type) {
	noticeClickValue=0;
	Log.i("----", "I m in request to server");

	final StringParameter teacherId = new StringParameter("userId",
		appData.getUserId());

	final StringParameter noticetype = new StringParameter("noticeType",
		type);

	final ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
	params.add(teacherId);
	params.add(noticetype);

	final PostRequestHandler postRequest = new PostRequestHandler(
		serverRequests.getRequestURL(ServerRequests.TEACHER_MY_NOTICES,
			""), params, new DownloadCallback() {

		    ObjectMapper objMapper = new ObjectMapper();
		    BaseResponse response = new BaseResponse();
		    List<Notice> _notice;

		    @Override
		    public void onSuccess(String downloadedString) {

			try {

			    response = objMapper.readValue(downloadedString,
				    BaseResponse.class);

			    if (response.getData().toString() != null
				    && TextUtils.equals(response.getStatus()
					    .toString(), StatusType.SUCCESS
					    .toString())) {

				_handler.post(new Runnable() {

				    @Override
				    public void run() {

					try {
					    _notice = NoticeBoardParser
						    .getNotice(response
							    .getData()
							    .toString());
					} catch (final JsonProcessingException e) {
					    e.printStackTrace();
					} catch (final IOException e) {
					    e.printStackTrace();
					}

					if (_notice.isEmpty()) {
						sentNotices.setAdapter(new SentNoticesAdapter(getActivity(),android.R.layout.simple_selectable_list_item,new ArrayList<Notice>()));
						Toast.makeText(getActivity(),
						    "No notices to display",
						    Toast.LENGTH_SHORT).show();
					    ShowProgressBar.progressBar.dismiss();
					}else{

					noticeType = type;
					SentNoticesAdapter adapter = new SentNoticesAdapter(
						getActivity(),
						android.R.layout.simple_selectable_list_item,
						_notice);
					adapter.notifyDataSetChanged();
				adapter.sort(new Comparator<Notice>() {

				    @Override
				    public int compare(Notice lhs, Notice rhs) {
					Date sentDate=lhs.getNoticeSentDate();
					Date endDate=rhs.getNoticeSentDate();
					if(sentDate!=null && endDate!=null)
					return endDate.compareTo(sentDate);
					else
					    return 0; 
				    }
				    
				});
					
					sentNotices.setAdapter(adapter);
					sentNotices.requestFocus();
					sentNotices.performItemClick(sentNotices.getChildAt(0), 0, sentNotices.getFirstVisiblePosition());
					//sentNotices.setSelection(0);
					sentNotices.setFastScrollEnabled(true);
					if(noticeType.equalsIgnoreCase("sentNotices")){
						myNotices.setBackgroundResource(R.drawable.sent_notices_h);
						receivedNotice.setBackgroundResource(R.drawable.received_notices);
					}else{
						receivedNotice.setBackgroundResource(R.drawable.received_notices_h);
						myNotices.setBackgroundResource(R.drawable.sent_notices);
					}
					ShowProgressBar.progressBar.dismiss();
					}
				    }
				});
			    } else {
				noticeBoard
					.setText("Unable to download the notices, Please try after some time");
			    }
			} catch (final Exception e) {
			    e.printStackTrace();
			}
			noticeClickValue=100;
		    }

		    @Override
		    public void onProgressUpdate(int progressPercentage) {
		    }

		    @Override
		    public void onFailure(String failureMessage) {
		    	ShowProgressBar.progressBar.dismiss();
		    }
		
		});
	postRequest.request();
	return noticeClickValue;
    }

    /**
     * The Class SentNoticesAdapter.
     */
    class SentNoticesAdapter extends ArrayAdapter<Notice> {
	/** The local simple date format. */
	SimpleDateFormat localSimpleDateFormat;

	/**
	 * Instantiates a new sent notices adapter.
	 *
	 * @param context the context
	 * @param resource the resource
	 * @param objects the objects
	 */
	public SentNoticesAdapter(Context context, int resource,
		List<Notice> objects) {
	    super(context, resource, objects);

	    items = objects;

	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View view = convertView;
	    Holder holder = new Holder();
	    if (view == null) {
		final LayoutInflater inflater = (LayoutInflater) getActivity()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater
			.inflate(R.layout.comments_list_row, null, false);
		holder._noticeTitle = (TextView) view
			    .findViewById(R.id.comments_view_text);
		holder._noticeDate = (TextView) view.findViewById(R.id.notice_fromdate);
		view.setTag(holder);
	    }else {
			holder = (Holder) view.getTag();
		}
	    if (position == CreateNoticeFragment.this.position) {
	    	view.setSelected(true);
	    	view.setPressed(true);
	    	view.setBackgroundColor(Color.parseColor("#FFD79C"));
        } else {
        	view.setSelected(false);
        	view.setPressed(false);
        	view.setBackgroundColor(Color.parseColor("#913ccd"));
		}
	    holder._noticeTitle.setTextColor(Color.BLACK);
	    holder._noticeDate.setTextColor(Color.BLACK);
	    holder._noticeTitle.setTextSize(17);
	    holder._noticeDate.setTextSize(17);
	    holder._noticeTitle.setText("" + items.get(position).getTitle());
	    localSimpleDateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
	    if (items.get(position).getNoticeSentDate() == null) {

	    } else
	    	holder._noticeDate.setText(localSimpleDateFormat.format(items.get(
			position).getNoticeSentDate()));
	    return view;
	}
    }

    public class Holder{
    	
    	TextView _noticeTitle, _noticeDate;
    }
}
