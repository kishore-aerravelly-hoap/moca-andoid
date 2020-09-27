package com.pearl.activities;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.AppStatus.AppStatus;
import com.pearl.application.ApplicationData;
import com.pearl.help.HelpParser;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.downloadmanager.utils.DownloadType;
import com.pearl.network.request.ServerRequests;
import com.pearl.parsers.json.NoticeBoardParser;
import com.pearl.ui.models.Notice;
import com.pearl.ui.models.NoticeBoardResponse;
import com.pearl.ui.models.Role;
import com.pearl.ui.models.VegaNotices;

// TODO: Auto-generated Javadoc
/**
 * The Class NoticeBoardActivity.
 */
public class NoticeBoardActivity extends FragmentActivity {
    
    /** The Constant ANOUNCEMENT_REQUEST_CODE. */
    public static final int ANOUNCEMENT_REQUEST_CODE = 1;
    
    /** The Constant FAIL_RESPONSE_CODE. */
    public static final int FAIL_RESPONSE_CODE = 2;
    
    /** The Constant OK_RESPONSE_CODE. */
    public static final int OK_RESPONSE_CODE = 1;
    
    /** The anouncement. */
    private ImageView anouncement;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The sent from. */
    private TextView dueDate,sentFrom;
    
    /** The menu button. */
    private Button menuButton;
    
    /** The help. */
    private ImageView message, help;
    
    /** The notice. */
    private List<Notice> notice;
    
    /** The title_view. */
    private TextView noticeBoard,title_view;
    
    /** The notice title view. */
    private ListView noticeTitleView;
    
    /** The notice_due_date. */
    private String notice_due_date;
    
    /** The on click. */
    private boolean onClick;
    
    /** The refresh notice board. */
    private ImageView refreshNoticeBoard;
    
    /** The server requests. */
    private ServerRequests serverRequests;
    
    /** The set alpha. */
    private boolean setAlpha;
    
    /** The student role. */
    private Role studentRole;
    
    /** The i. */
    private int i = 0;
    
    /** The context. */
    private Context context;
    
    /** The teacher quiz. */
    private ImageView teacherQuiz;
    
    /** The teacher role. */
    private Role teacherRole;
    
    /** The upgrade. */
    private ImageView upgrade;
    
    /** The progress. */
    private ProgressDialog progress;
    
    /** The tag. */
    private final String tag="NoticeBoardActivity";
    
    /** The app status. */
    private AppStatus appStatus;

    /** The h. */
    public static Handler h;
    
    /** The position. */
    private int position;

    /**
     * Update notice board.
     */
    private void updateNoticeBoard() {
	try {

	    final NoticeBoardResponse noticeBoardResponse = NoticeBoardParser
		    .getNoticeBoardContent(new File(appData
			    .getNoticeBoardFile()));
	    Logger.warn("VegaNotification",
		    "NOTICEBOARD - noticeboard data is:"
			    + noticeBoardResponse.getData().toString());
	    final VegaNotices notices = noticeBoardResponse.getData();
	    Logger.warn("VegaNotification", "NOTICEBOARD - notices size is:"
		    + notices.getUserNotices().size());
	    notice = notices.getUserNotices();
	    final NoticeBoardAdapter localNoticeBoardAdapter = new NoticeBoardAdapter(
		    this, android.R.layout.simple_selectable_list_item, notice);
	    noticeTitleView.setAdapter(localNoticeBoardAdapter);
	    // localNoticeBoardAdapter.setSelectedItem(0);
	    noticeTitleView
	    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(
				
			AdapterView<?> paramAdapterView,
			View paramView, int position, long paramLong) {
			noticeBoard.scrollTo(0, 0);
			title_view.scrollTo(0, 0);
		    noticeBoard.setText(notice.get(position).getMsg());
		    paramView.setSelected(true);
		    paramView.getFocusables(position);
		    final SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
		    		"dd/MM/yyyy HH:mm:ss a");
		    title_view.setText(notice.get(position).getTitle());
		    sentFrom.setText(" From :"+notice.get(position).getUserFromName());
		    dueDate.setText("This notice is valid upto "+localSimpleDateFormat.format(notice.get(position).getDueDate()));
		    NoticeBoardActivity.this.position=position;
		}
	    });

	    noticeTitleView.setSelector(R.drawable.notice_selectbar);
	    if (notice.size() != 0) {
		noticeBoard.setText(notice.get(0).getMsg());
		final SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss a ");
		title_view.setText(notice.get(0).getTitle());
		dueDate.setText("This notice is valid upto "
			+ localSimpleDateFormat.format(notice.get(0)
				.getDueDate()));
		sentFrom.setText(" From :"+notice.get(0).getUserFromName());
	    } else {
		noticeBoard.setText("You don't have any notices to show");
	    }
	} catch (final Exception localException) {
	}

    }

    /**
     * Convert time stamp to date.
     *
     * @param paramLong the param long
     * @return the string
     */
    public String convertTimeStampToDate(long paramLong) {
	final Calendar localCalendar = Calendar.getInstance();
	localCalendar.setTimeInMillis(paramLong);
	return localCalendar.getTime().toString();
    }

    /**
     * Date format.
     *
     * @param paramString the param string
     * @return the string
     */
    public String dateFormat(String paramString) {
	final StringTokenizer localStringTokenizer = new StringTokenizer(paramString,
		"GMT");
	for (String str = null;; str = localStringTokenizer.nextToken())
	    if (!localStringTokenizer.hasMoreElements())
		return str;
    }



    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.notice_board);
	serverRequests = new ServerRequests(this);
	appData = (ApplicationData) getApplication();
	noticeBoard = (TextView) findViewById(R.id.noticeBoard);
	dueDate = (TextView) findViewById(R.id.notice_due_date);
	noticeTitleView = (ListView) findViewById(R.id.notice_title);
	refreshNoticeBoard = (ImageView) findViewById(R.id.notice_refresh);
	sentFrom=(TextView)findViewById(R.id.notice_sent_from);
	title_view=(TextView)findViewById(R.id.notice_title_view);
	help = (ImageView) findViewById(R.id.student_notice_help);
	context = this;
	title_view.setMovementMethod(ScrollingMovementMethod.getInstance());
	noticeBoard.setMovementMethod(ScrollingMovementMethod.getInstance());
	appStatus=new AppStatus();
	progress = new ProgressDialog(this);

	try {
	    downloadNoticeBoardInfo();
	} catch (final Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	menuButton = (Button) findViewById(R.id.LasallemenuButton);
	menuButton.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View paramView) {
		final Intent localIntent = new Intent(getApplicationContext(),
			ActionBar.class);
		startActivity(localIntent);
		menuButton.invalidate();

	    }

	});

	help.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showDialog();
		}
	});
	
	h = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
		super.handleMessage(msg);

		switch (msg.what) {

		case 0:
		    finish();
		    break;
		}
	    }
	};

	refreshNoticeBoard.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Logger.warn(tag, "Refresh noticed board clicked.....");
		Logger.info(tag, "ONCLICK VALUE IS" + onClick);

		if (appStatus.isOnline(getApplicationContext())) {

		    runOnUiThread(new Runnable() {

			@Override
			public void run() {
			    try {
				downloadNoticeBoardInfo();

			    } catch (final Exception e) {
				// TODO Auto-generated catch block
				Logger.info(tag, "" + e);
			    }
			}
		    });
		} else {
		    Logger.warn(tag, "NOTICE - clicked when offline");
		    runOnUiThread(new Runnable() {

			@Override
			public void run() {
			    Toast.makeText(getApplicationContext(),
				    R.string.network_connection_unreachable,
				    Toast.LENGTH_SHORT).show();
			}
		    });
		}
	    }
	});
    }

    /**
     * Show dialog.
     */
    private void showDialog(){
    	i = 0;
		Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pearl_tips_layout);
		RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.tips_layout);
		final TextView tips = (TextView) dialog.findViewById(R.id.tips);
		ImageView previous = (ImageView) dialog.findViewById(R.id.previous);
		ImageView next = (ImageView) dialog.findViewById(R.id.next);
		layout.setBackgroundResource(R.drawable.attendance_help);
		final List<String> list = HelpParser.getHelpContent("student_notice_board.txt", context);
		if(list != null && list.size() > 0){
			tips.setText(list.get(0));
		}
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
    
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onDestroy()
     */
    @Override
    public void onDestroy() {
	super.onDestroy();
    }
    
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
	if (!appData.isLoginStatus()) {
	    final Intent loginIntent = new Intent(this, LoginActivity.class);
	    startActivity(loginIntent);
	    finish();
	}
	serverRequests = new ServerRequests(this);
	Logger.warn(tag, "In onResume");

    }


    /**
     * The Class NoticeBoardAdapter.
     */
    private class NoticeBoardAdapter extends ArrayAdapter<Notice> {
	
	/** The item. */
	List<Notice> item;

	/**
	 * Instantiates a new notice board adapter.
	 *
	 * @param context the context
	 * @param textViewResourceId the text view resource id
	 * @param notice the notice
	 */
	public NoticeBoardAdapter(Context context, int textViewResourceId,
		List<Notice> notice) {
	    super(context, textViewResourceId, notice);
	    item = notice;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {

	    if (view == null) {
		final LayoutInflater vi = (LayoutInflater) this.getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.notice_board_list_row, null);
	    }

	    final TextView title = (TextView) view
		    .findViewById(R.id.noticeBoardTitle);
	    final TextView date = (TextView) view.findViewById(R.id.noticeBoardDate);
	    if (position == NoticeBoardActivity.this.position) {
	    	view.setSelected(true);
	    	view.setPressed(true);
	    	view.setBackgroundColor(Color.parseColor("#FFD79C"));
        } else {
        	view.setSelected(false);
        	view.setPressed(false);
        	view.setBackgroundColor(Color.parseColor("#913ccd"));
		}
	    final Notice notice = item.get(position);
	    Logger.warn(BaseActivity.tag,
		    "NOTICEBOARD - title is:" + notice.getTitle());
	    Logger.info("NOTICEBOARD TITILE", "NOTICEBOARD title size is"
		    + notice.getTitle().length());
	    if (notice.getTitle().length() > 25) {
		title.setText(notice.getTitle().substring(0, 25) + " ...");
	    } else {
		title.setText(notice.getTitle() + "");
	    }
	    final SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
	    		"dd/MM/yyyy HH:mm:ss a");
	    if(notice.getNoticeFromName()!=null) {
	    date.setText("Posted "
			+ localSimpleDateFormat.format(notice.getNoticeSentDate()));
	    
	    }
	    else
		Log.w("NoticeBoardActivity", "notice sent date is null");

	    return view;
	}
    }

    /**
     * Download notice board info.
     *
     * @throws Exception the exception
     */
    public void downloadNoticeBoardInfo() throws Exception {
	try {
	    progress.setTitle("Loading notices");
	    progress.setCancelable(false);
	    progress.show();
	    Logger.warn(tag, "In Noticeboard download()");
	    final DownloadType downloadType = new DownloadType();
	    downloadType.setType(DownloadType.DEFAULT);
	    final String url = serverRequests.getRequestURL(
		    ServerRequests.NOTICEBOARD, "" + appData.getUserId());

	    Logger.warn(tag, "filePath is:" + appData.getInfoFilePath());
	    Logger.warn(tag, "URL is:" + url + "File name is:"
		    + ApplicationData.NOTIFICATION_FILENAME);
	    final Download download = new Download(url, appData.getInfoFilePath(),
		    ApplicationData.NOTIFICATION_FILENAME);
	    final DownloadManager notificationDownloader = new DownloadManager(
		    appData, download);

	    notificationDownloader.startDownload(new DownloadCallback() {
		@Override
		public void onSuccess(String downloadedString) {
		    Logger.warn("Vega Notification", "Download success");
		    try {
			progress.hide();
			updateNoticeBoard();
		    } catch (final Exception e) {
			//	ToastMessageForExceptions(R.string.DOWNLOADINFO_EXCEPTION_VEGANOTIFICATION);

			e.printStackTrace();
		    }
		}

		@Override
		public void onProgressUpdate(int progressPercentage) {
		    // TODO Auto-generated method stub

		}
		@Override
		public void onFailure(String failureMessage) {
		    // TODO Auto-generated method stub
		    progress.cancel();
		    Toast.makeText(getApplicationContext(),R.string.Unable_to_reach_pearl_server,
			    Toast.LENGTH_LONG).show();
		}
	    });
	} catch (final Exception e) {

	    //ToastMessageForExceptions(R.string.DOWNLOADINFO_EXCEPTION_VEGANOTIFICATION);
	    e.printStackTrace();
	}
    }

}
