package com.pearl.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.chat.Message;
import com.pearl.chat.MessageList;
import com.pearl.chat.server.response.BaseResponse;
import com.pearl.logger.Logger;
import com.pearl.network.downloadmanager.DownloadManager;
import com.pearl.network.downloadmanager.utils.Download;
import com.pearl.network.downloadmanager.utils.DownloadCallback;
import com.pearl.network.downloadmanager.utils.DownloadType;
import com.pearl.network.request.ServerRequests;
import com.pearl.parsers.json.ChatParser;
import com.pearl.ui.commentview.PearlCommentLayout;
import com.pearl.util.DateFormat;

// TODO: Auto-generated Javadoc
/**
 * The Class ChatWallActivity.
 *
 * @author Samreen
 */
public class ChatWallActivity extends BaseActivity {

    /** The user id. */
    private String userId;
    
    /** The post chat. */
    private ImageView postChat;
    
    /** The refresh wall. */
    private ImageView refreshWall;
    
    /** The server response. */
    private String serverResponse;
    
    /** The chat list. */
    private ListView chatList;
    
    /** The new chat message. */
    private TextView newChatMessage;
    
    /** The count layout. */
    private LinearLayout countLayout;
    
    /** The wall context. */
    private Context wallContext;
    
    /** The server requests. */
    private ServerRequests serverRequests;
    
    /** The time stamp. */
    private long timeStamp;
    
    /** The response message. */
    private String responseMessage = " ";
    
    /** The response data. */
    private String responseData;
    
    /** The wall timer. */
    private Timer wallTimer;
    
    /** The user name. */
    private String userName;
    
    /** The on click. */
    private boolean onClick;
    
    /** The set alpha. */
    private boolean setAlpha;
    
    /** The Constant COMMENT. */
    private static final String COMMENT = "COMMENT";
    
    /** The Constant LIKE. */
    private static final String LIKE = "LIKE";
    
    /** The Constant POST. */
    private static final String POST = "POST";
    
    /** The Constant WALL. */
    private static final String WALL = "WALL";

    /** The Constant CHAT_MESSAGE_COUNT. */
    private static final String CHAT_MESSAGE_COUNT = "COUNT";

    /** The Constant MESSAGE_ID. */
    private static final String MESSAGE_ID = "MESSAGE_ID";
    
    /** The Constant CHAT. */
    private static final String CHAT = "CHAT";
    
    /** The Constant CHAT_COMMENT_REQUEST_CODE. */
    public static final int CHAT_COMMENT_REQUEST_CODE = 1;
    
    /** The Constant CHAT_POST_REQUEST_CODE. */
    public static final int CHAT_POST_REQUEST_CODE = 2;

    /** The is request processed. */
    boolean isRequestProcessed;
    
    /** The base response. */
    BaseResponse baseResponse;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Logger.warn(tag, "caht wall onCreate");
	setContentView(R.layout.chat_wall);
	chatList = (ListView) findViewById(R.id.chatList);
	newChatMessage = (TextView) findViewById(R.id.newChatMessage);
	refreshWall = (ImageView) findViewById(R.id.chatRefresh);
	userId = appData.getUserId();
	postChat = (ImageView) findViewById(R.id.postChatImg);
	serverRequests = new ServerRequests(this);
	wallTimer = new Timer();
	postChat.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (setAlpha) {
		    Toast.makeText(v.getContext(),
			    R.string.network_connection_unreachable,
			    toastDisplayTime).show();
		} else {
		    final Intent intent = new Intent(v.getContext(),
			    ChatPostActivity.class);
		    startActivityForResult(intent, CHAT_POST_REQUEST_CODE);
		}
	    }
	});
	wallContext = this;
	userName = appData.getUser().getFirstName() + ""
		+ appData.getUser().getSecondName();
    }

    /**
     * Bind events.
     */
    private void bindEvents() {
	newChatMessage.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		download(WALL, serverRequests.getRequestURL(
			ServerRequests.CHAT_WALL, userId));
		newChatMessage.setVisibility(View.GONE);
	    }
	});
    }

    /**
     * End timer.
     */
    private void endTimer() {
	wallTimer.cancel();
    }

    /**
     * The Class UpdateTimerTask.
     */
    public class UpdateTimerTask extends TimerTask {
	
	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
	    Logger.verbose(tag, "mUpdateTimeTask - run()");
	    sendRequestToServer(userId, "", CHAT_MESSAGE_COUNT, "", "",
		    timeStamp, new DownloadCallback() {

		@Override
		public void onSuccess(String downloadedString) {
		    runOnUiThread(new Thread() {
			@Override
			public void run() {
			    if (responseData == null) {
				newChatMessage.setVisibility(View.GONE);
			    } else {
				newChatMessage
				.setVisibility(View.VISIBLE);
				newChatMessage
				.setText("You have "
					+ responseData
					+ " new stories");
			    }
			}
		    });
		}

		@Override
		public void onProgressUpdate(int progressPercentage) {
		    // TODO Auto-generated method stub

		}

		@Override
		public void onFailure(String failureMessage) {
		    // TODO Auto-generated method stub

		}
	    });
	    Logger.warn(tag, "timeStamp is:" + timeStamp);
	}
    };

    /**
     * Gets the chat.
     *
     * @return the chat
     */
    private void getChat() {

	if (!ApplicationData.isFileExists(appData.getChatWallFile())) {
	    downloadChatFromServer();
	    return;
	}

	String filePath = null;
	try {
	    filePath = appData.getChatWallPath()
		    + ApplicationData.CHAT_WALL_FILENAME;
	} catch (final Exception e1) {
	    ToastMessageForExceptions(R.string.IO_EXCEPTION);
	    e1.printStackTrace();
	    backToDashboard();
	}
	MessageList messageList = null;
	;
	try {
	    messageList = ChatParser.getChatFile(filePath);
	} catch (final JsonProcessingException e) {
	    ToastMessageForExceptions(R.string.JSON_PROCESSING_EXCEPTION);
	    e.printStackTrace();
	    backToDashboard();
	    ;
	} catch (final IOException e) {
	    ToastMessageForExceptions(R.string.IO_EXCEPTION);
	    e.printStackTrace();
	    backToDashboard();
	}
	if (messageList.getMessageList().size() != 0) {
	    timeStamp = messageList.getMessageList().get(0).getTimestamp()
		    .getTime();
	    Logger.info(tag, "message list size is:"
		    + messageList.getMessageList().size());
	    Logger.warn(tag, "timestamp in getchat is:" + timeStamp);
	    Logger.warn(tag,
		    "message count in getchat is:" + messageList.getData());
	    Logger.warn(
		    tag,
		    "message from messageList is:"
			    + messageList.getMessageList());
	    getWall(messageList);
	} else {
	    Toast.makeText(wallContext, R.string.There_are_no_posts, Toast.LENGTH_LONG)
	    .show();
	}
    }

    /**
     * Gets the wall.
     *
     * @param messageList the message list
     * @return the wall
     */
    private void getWall(MessageList messageList) {
	final ChatWallAdapter chatAdapter = new ChatWallAdapter(this,
		R.layout.chat_wall_list_row, messageList.getMessageList());
	chatList.setAdapter(chatAdapter);
    }

    /**
     * Download chat from server.
     */
    private void downloadChatFromServer() {
	try {
	    final String url = serverRequests.getRequestURL(ServerRequests.CHAT_WALL,
		    userId);

	    loadingDialog.setTitle("Wall Loading.....");
	    loadingDialog.setMessage("Please wait while the wall loads..");
	    loadingDialog.show();

	    final Download download = new Download(url, appData.getChatWallPath(),
		    ApplicationData.CHAT_WALL_FILENAME);
	    final DownloadManager chatWallDownloader = new DownloadManager(appData,
		    download);
	    chatWallDownloader.startDownload(new DownloadCallback() {

		@Override
		public void onSuccess(String downloadedString) {
		    Logger.warn(tag, "download success");
		    runOnUiThread(new Thread() {
			@Override
			public void run() {
			    getChat();
			    loadingDialog.hide();
			}
		    });
		}

		@Override
		public void onProgressUpdate(int progressPercentage) {
		}

		@Override
		public void onFailure(String failureMessage) {
		    Logger.warn(tag, "Download failed");
		    runOnUiThread(new Thread() {
			@Override
			public void run() {
			    loadingDialog.hide();
			    Toast.makeText(activityContext,
				    R.string.Unable_to_reach_pearl_server,
				    Toast.LENGTH_LONG).show();
			}
		    });
		}
	    });
	} catch (final Exception e) {
	    ToastMessageForExceptions(R.string.CHAT_DOWNLOAD_FROM_SERVER_EXCEPTION);
	    e.printStackTrace();
	    backToDashboard();
	}
    }

    /**
     * The Class ChatWallAdapter.
     */
    public class ChatWallAdapter extends ArrayAdapter<Message> {
	
	/** The items. */
	List<Message> items;
	
	/** The view all comments. */
	boolean viewAllComments;
	
	/** The result. */
	String result;

	/**
	 * Instantiates a new chat wall adapter.
	 *
	 * @param context the context
	 * @param textViewResourceId the text view resource id
	 * @param items the items
	 */
	public ChatWallAdapter(Context context, int textViewResourceId,
		List<Message> items) {
	    super(context, textViewResourceId, items);
	    Logger.warn(tag, "Samreen");
	    this.items = items;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View v = convertView;

	    if (v == null) {
		Logger.warn(tag, "v is null");
		final LayoutInflater vi = (LayoutInflater) this.getContext()
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.chat_wall_list_row, null);
	    }
	    Logger.warn(tag, "position is:" + position);
	    final Message message = items.get(position);
	    if (message != null) {
		final LinearLayout ll = new LinearLayout(wallContext);
		final PearlCommentLayout commentLayout = new PearlCommentLayout(
			v.getContext());
		final RelativeLayout listRelativeLayout = (RelativeLayout) v
			.findViewById(R.id.parentWallLayout);
		countLayout = (LinearLayout) v.findViewById(R.id.countLayout);
		final TextView chatMessage = (TextView) v
			.findViewById(R.id.chatMessage);
		final TextView date = (TextView) v.findViewById(R.id.chatDate);
		final TextView studentName = (TextView) v
			.findViewById(R.id.chatStdName);
		final TextView commentLink = (TextView) v
			.findViewById(R.id.commentLink);
		final TextView viewComments = (TextView) v
			.findViewById(R.id.viewAllComments);
		v.findViewById(R.id.linksLayout);

		final TextView like = (TextView) v.findViewById(R.id.like);
		final TextView likeLink = (TextView) v
			.findViewById(R.id.likeLink);
		final TextView commentCount = (TextView) v
			.findViewById(R.id.noOfComments);

		ll.setLayoutParams(new LinearLayout.LayoutParams(
			android.view.ViewGroup.LayoutParams.FILL_PARENT,
			android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		ll.setOrientation(LinearLayout.VERTICAL);

		Logger.warn(tag, "message is:" + message.getMessage());
		chatMessage.setText(message.getMessage() + "");
		studentName.setText(message.getUserId());
		date.setText(DateFormat.getFormattedTime(
			message.getTimestamp(), CHAT));

		getLikesStudentNames(message, like);
		likeLink.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View v) {
			Logger.warn(tag, "Liked a post");

			if (setAlpha) {
			    Toast.makeText(v.getContext(),
				    R.string.network_connection_unreachable,
				    toastDisplayTime).show();
			} else {
			    message.getLikes().size();
			    sendRequestToServer(userId, message.getId(), LIKE,
				    "", "", timeStamp, new DownloadCallback() {

				@Override
				public void onSuccess(
					String downloadedString) {
				    runOnUiThread(new Thread() {
					@Override
					public void run() {
					    Logger.warn(tag,
						    "request sent");
					    if (responseMessage
						    .equalsIgnoreCase("Like action done on the message")) {
						Logger.warn(tag,
							"liked succesfully");
						countLayout
						.setVisibility(View.VISIBLE);
						like.setVisibility(View.VISIBLE);
						like.setText(message
							.getLikes()
							.get(0)
							+ " Likes this");
						message.like(userId
							+ "");
						getChat();
						// getLikesStudentNames(message,
						// like);
					    } else if (responseMessage
						    .equalsIgnoreCase("You already like this")) {
						Logger.warn(tag,
							"u have already liked");
						new AlertDialog.Builder(
							activityContext)
						.setIcon(
							android.R.drawable.ic_dialog_alert)
							.setTitle(
								" Status")
								.setMessage(
									"You have already liked this message")
									.setPositiveButton(
										"OK",
										null)
										.show();
					    }
					}
				    });
				}

				@Override
				public void onProgressUpdate(
					int progressPercentage) {
				    // TODO Auto-generated method stub

				}

				@Override
				public void onFailure(
					String failureMessage) {
				    // TODO Auto-generated method stub

				}
			    });
			    Logger.warn(tag, "Response message wen liked is:"
				    + responseMessage);
			}
		    }
		});

		like.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View v) {

			try {
			    Dialog dialog = new Dialog(v.getContext());
			    final AlertDialog.Builder builder = new AlertDialog.Builder(
				    v.getContext());
			    if (message.getLikes().size() == 0)
				builder.setTitle("No one Liked the message");
			    else {
				builder.setTitle("List of student who liked this message is");
				final ListView modeList = new ListView(v.getContext());
				for (int i = 0; i < message.getLikes().size(); i++)
				    Logger.warn(tag,
					    "list of people who liked the message is:"
						    + message.getLikes().get(i));
				final String[] s1 = message.getLikes().toArray(
					new String[0]); // Collection to array
				final ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(
					v.getContext(),
					R.layout.chat_like_list, R.id.likeName,
					s1);
				modeList.setAdapter(modeAdapter);
				builder.setView(modeList);
			    }
			    dialog = builder.create();
			    dialog.show();
			} catch (final Exception e) {

			    ToastMessageForExceptions(R.string.CHAT_MESSAGE_LIKE_EXCEPTION);
			    e.printStackTrace();
			    backToDashboard();
			}
		    }
		});

		if (message.getComments().size() != 0) {
		    Logger.warn(tag, "comment count in if is:"
			    + message.getComments().size());
		    if (message.getComments().size() == 1)
			commentCount.setText(message.getComments().size()
				+ " Comment");
		    else
			commentCount.setText(message.getComments().size()
				+ " Comments");
		    if (message.getComments().size() > 2) {
			viewComments.setVisibility(View.VISIBLE);
			viewComments.setText("View all "
				+ message.getComments().size() + " comments");
		    }
		    final RelativeLayout rl = commentLayout.getCommentView(
			    v.getContext(), message.getComments(),
			    viewAllComments);
		    addCommentsToView(rl, listRelativeLayout, ll);
		} else {
		    commentCount.setVisibility(View.INVISIBLE);
		    ll.setVisibility(View.GONE);
		    Logger.warn(tag, "there are no comments for this post");
		}

		viewComments.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View v) {
			viewAllComments = true;
			final RelativeLayout rl = commentLayout.getCommentView(
				v.getContext(), message.getComments(),
				viewAllComments);
			addCommentsToView(rl, listRelativeLayout, ll);
		    }
		});

		commentLink.setOnClickListener(new OnClickListener() {

		    @Override
		    public void onClick(View v) {
			if (setAlpha) {
			    Toast.makeText(v.getContext(),
				    R.string.network_connection_unreachable,
				    toastDisplayTime).show();
			} else {
			    Logger.warn(tag, "Commented a post");
			    final Intent intent = new Intent(v.getContext(),
				    ChatCommentActivity.class);
			    Logger.warn(tag,
				    "messageid before passing to intent is:"
					    + message.getId());
			    intent.putExtra(MESSAGE_ID, message.getId());
			    startActivityForResult(intent,
				    CHAT_COMMENT_REQUEST_CODE);
			}
		    }
		});
	    }
	    return v;
	}

	/**
	 * Adds the comments to view.
	 *
	 * @param rl the rl
	 * @param listRelativeLayout the list relative layout
	 * @param ll the ll
	 */
	private void addCommentsToView(RelativeLayout rl,
		RelativeLayout listRelativeLayout, LinearLayout ll) {
	    final RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	    linearParams.addRule(RelativeLayout.BELOW, R.id.viewAllComments);
	    linearParams.setMargins(100, 0, 0, 0);
	    ll.removeViewInLayout(ll);
	    ll.addView(rl);
	    listRelativeLayout.addView(ll, linearParams);
	}

	/**
	 * Gets the likes student names.
	 *
	 * @param message the message
	 * @param like the like
	 * @return the likes student names
	 */
	private void getLikesStudentNames(Message message, TextView like) {
	    if (message.getLikes().size() != 0) {
		Logger.info(tag, "likes  size is:" + message.getLikes().size());
		if (like.getVisibility() == View.GONE) {
		    Logger.warn(tag, "making the text visible");
		    like.setVisibility(View.VISIBLE);
		}
		if (message.getLikes().size() == 1) {
		    Logger.warn(tag, "one user who liked it is:"
			    + message.getLikes().get(0));
		    like.setText(message.getLikes().get(0) + " Likes this");
		} else if (message.getLikes().size() == 2)
		    like.setText(message.getLikes().get(0) + " and "
			    + message.getLikes().get(1) + " Likes this");
		else if (message.getLikes().size() > 2) {
		    final int count = message.getLikes().size() - 2;
		    Logger.warn(tag, "like count after subtracting is:" + count);
		    like.setText(message.getLikes().get(0) + ","
			    + message.getLikes().get(1) + " and" + count
			    + " others Likes this");
		}
	    } else {
		Logger.info(tag, "like size is :" + message.getLikes().size());
		like.setVisibility(View.GONE);
	    }
	}
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	Logger.warn(tag, "in onActivityResult requestCode is:" + requestCode
		+ " result code is:" + resultCode);
	switch (requestCode) {
	case CHAT_POST_REQUEST_CODE:
	    if (resultCode == RESULT_OK) {
		Logger.warn(tag, "chat post request code");
		final String msg = data.getStringExtra(POST);
		if (msg.equals(""))
		    Toast.makeText(this, R.string.no_message_posted,
			    toastDisplayTime).show();
		else {
		    new Message(userId + "", msg);
		    Logger.warn(tag, "Message from intent is:" + msg);
		    sendRequestToServer(userId, "", POST, "", msg, 0,
			    new DownloadCallback() {

			@Override
			public void onSuccess(String downloadedString) {
			    // TODO Auto-generated method stub

			}

			@Override
			public void onProgressUpdate(
				int progressPercentage) {
			    // TODO Auto-generated method stub

			}

			@Override
			public void onFailure(String failureMessage) {
			    // TODO Auto-generated method stub

			}
		    });
		}
		break;
	    }
	case CHAT_COMMENT_REQUEST_CODE:
	    if (resultCode == RESULT_OK) {
		if (data.getStringExtra(COMMENT).equals(""))
		    Toast.makeText(this, R.string.no_comment_posted,
			    toastDisplayTime).show();
		else
		    sendRequestToServer(userId,
			    data.getStringExtra(MESSAGE_ID), COMMENT,
			    data.getStringExtra(COMMENT), "", 0,
			    new DownloadCallback() {

			@Override
			public void onSuccess(String downloadedString) {
			    // TODO Auto-generated method stub

			}

			@Override
			public void onProgressUpdate(
				int progressPercentage) {
			    // TODO Auto-generated method stub

			}

			@Override
			public void onFailure(String failureMessage) {
			    // TODO Auto-generated method stub

			}
		    });
	    }
	}
    }

    /**
     * downloads the XML.
     *
     * @param type the type
     * @param url the url
     */
    public void download(String type, String url) {
	loadingDialog.setTitle("Wall Loading.....");
	loadingDialog.setMessage("Please wait while the wall loads..");
	loadingDialog.show();
	final DownloadCallback dc = new DownloadCallback() {
	    @Override
	    public void onSuccess(String downloadedString) {
		Logger.warn(tag, "download success");
		runOnUiThread(new Thread() {
		    @Override
		    public void run() {
			getChat();
			loadingDialog.hide();
		    }
		});
	    }

	    @Override
	    public void onProgressUpdate(int progressPercentage) {

	    }

	    @Override
	    public void onFailure(String failureMessage) {
		Logger.warn(tag, "Download failed");
		runOnUiThread(new Thread() {
		    @Override
		    public void run() {
			loadingDialog.hide();
			Toast.makeText(activityContext,
				R.string.Failed_to_download_the_file,
				toastDisplayTime).show();
		    }
		});
	    }
	};
	download(url, type, dc);
    }

    /**
     * Download.
     *
     * @param url the url
     * @param type the type
     * @param dc the dc
     */
    public void download(String url, String type, DownloadCallback dc) {
	String filePath = "";
	String fileName = "";
	Logger.warn(tag, "In download()");

	final DownloadType downloadType = new DownloadType();
	downloadType.setType(DownloadType.DEFAULT);

	if (type.equals(WALL)) {
	    filePath = appData.getChatWallPath();
	    fileName = ApplicationData.CHAT_WALL_FILENAME;
	} else if (type.equals(LIKE)
		|| type.equals(COMMENT) || type.equals(POST)) {
	    filePath = appData.getChatBaseResponseFilePath();
	    fileName = ApplicationData.CHAT_BASE_RESPONSE_FILE;
	}

	Logger.warn(tag, "URL is:" + url + " File name is:"
		+ ApplicationData.CHAT_WALL_FILENAME);
	final Download download = new Download(downloadType, url, filePath, fileName);
	final DownloadManager chatWallDownloader = new DownloadManager(appData,
		download);
	chatWallDownloader.startDownload(dc);
    }

    // TODO
    /**
     * Create a field in the application data to set the base response file
     * name. Create a method to read the response file. read that file after a
     * specified amount of time. depending upon the status increment the like
     * count and comment count.
     *
     * @param userId the user id
     * @param messageId the message id
     * @param type the type
     * @param comment the comment
     * @param message the message
     * @param timestamp the timestamp
     * @param dc the dc
     */

    private void sendRequestToServer(String userId, String messageId,
	    final String type, String comment, String message, long timestamp,
	    final DownloadCallback dc) {
	final String requestUrl;
	Logger.warn(tag, "sendRequestToServer - user id is:" + userId
		+ " message id is:" + messageId);
	Logger.warn(tag, "sending request to server");
	Logger.warn(tag, "message is:" + message);
	if (type.equals("COMMENT")) {
	    requestUrl = serverRequests.getRequestURL(
		    ServerRequests.CHAT_COMMENT, userId, messageId, comment,
		    userName);
	} else if (type.equals("LIKE"))
	    requestUrl = serverRequests.getRequestURL(ServerRequests.CHAT_LIKE,
		    userId, messageId);
	else if (type.equals("POST")) {
	    requestUrl = serverRequests.getRequestURL(ServerRequests.CHAT_POST,
		    userId, message, userName);
	} else if (type.equals("COUNT"))
	    requestUrl = serverRequests.getRequestURL(
		    ServerRequests.CHAT_MESSAGE_COUNT, userId, timestamp + "");
	else
	    requestUrl = "";
	try {

	    new Thread(new Runnable() {

		@Override
		public void run() {
		    java.net.URL u;
		    try {
			Logger.error(tag, "processing url is:" + requestUrl);
			u = new URL(requestUrl);
			// Logger.warn(tag, "----content is:"+u.getContent());
			final BufferedReader in = new BufferedReader(
				new InputStreamReader(u.openStream()));
			String str;
			String responseString = null;
			while ((str = in.readLine()) != null) {
			    Logger.error(tag, "String is:" + str);
			    responseString = str;
			}
			final ObjectMapper mapper = new ObjectMapper();
			Logger.warn(tag, "Response string is:" + responseString);
			final BaseResponse response = mapper.readValue(
				responseString, BaseResponse.class);
			if (type.equals("LIKE") || type.equals("COMMENT")
				|| type.equals("POST"))
			    responseMessage = response.getMessage();
			else if (type.equals("COUNT")) {
			    responseMessage = response.getMessage();
			    responseData = response.getData().toString();
			}
			Logger.error(tag, "response message is:"
				+ responseMessage + " and response data is:"
				+ responseData);
			in.close();

			dc.onSuccess(responseMessage);
		    } catch (final Exception e) {
			Logger.warn(tag, "result in method is:"
				+ serverResponse);
			Logger.error("ChatWall---get url", e);
			// ******** run on new UI thread
			ToastMessageForExceptions(R.string.JSON_PARSING_EXCEPTION);

			backToDashboard();
		    }
		}
	    }).start();
	} catch (final Exception e) {
	    Logger.error(tag, e);
	    ToastMessageForExceptions(R.string.JSON_PARSING_EXCEPTION);
	    backToDashboard();

	}
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onResume()
     */
    @Override
    public void onResume() {
	Logger.warn(tag, "chat wall onresume");
	super.onResume();
	bindEvents();
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onDestroy()
     */
    @Override
    public void onDestroy() {
	super.onDestroy();
	endTimer();
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "chat_wall";
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
     */
    @Override
    public void onNetworkAvailable() {
	Logger.warn(tag, "In onNetworkAvailable()");
	Logger.warn(tag, "network available so enable the refresh button");
	if (!ApplicationData.isFileExists(appData.getChatWallFile())) {
	    runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    Toast.makeText(
			    activityContext,
			    R.string.Please_click_the_refresh_button_above_to_get_Wall,
			    toastDisplayTime).show();
		}
	    });
	    onClick = true;
	    setAlpha = false;

	    if (!setAlpha) {
		runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
			refreshWall.setAlpha(225);
		    }
		});
	    }

	} else {
	    Logger.warn(tag, "file exists in local");
	    onClick = true;
	    runOnUiThread(new Runnable() {

		@Override
		public void run() {
		    getChat();
		    refreshWall.setAlpha(225);
		    setAlpha = false;
		}
	    });
	}
	refreshWall.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		if (onClick) {
		    runOnUiThread(new Runnable() {

			@Override
			public void run() {
			    downloadChatFromServer();
			}
		    });
		} else {
		    runOnUiThread(new Runnable() {

			@Override
			public void run() {
			    Toast.makeText(activityContext,
				    R.string.network_connection_unreachable,
				    toastDisplayTime).show();
			}
		    });
		}
	    }
	});
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
     */
    @Override
    public void onNetworkUnAvailable() {

	Logger.warn(tag, "In onNetworkUnAvailable()");
	Logger.warn(tag, "network not available so disable the refresh button");
	onClick = false;
	setAlpha = true;
	if (setAlpha) {
	    runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    refreshWall.setAlpha(70);
		}
	    });
	}
    }
}
