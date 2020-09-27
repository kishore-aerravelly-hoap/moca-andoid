package com.pearl.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.application.VegaConstants;
import com.pearl.logger.Logger;
import com.pearl.network.request.ServerRequests;

// TODO: Auto-generated Javadoc
/**
 * The Class ChatActivity.
 */
public class ChatActivity extends BaseActivity {
    
    /** The user id. */
    String userId;
    
    /** The server requests. */
    ServerRequests serverRequests;
    
    /** The chat_view. */
    private WebView chat_view;
    
    /** The chat menu button. */
    private Button chatMenuButton;
    
    /** The refresh chat. */
    private ImageView refreshChat;
    
    /** The chat_not_network. */
    TextView chat_not_network;
    
    /** The activity. */
    final Activity activity = this;
    
    /** The h. */
    public static Handler h;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
	setContentView(R.layout.chat_ui);
	serverRequests = new ServerRequests(this);
	chatMenuButton = (Button) findViewById(R.id.chat_menu_button);
	refreshChat = (ImageView) findViewById(R.id.refreshChat);
	chat_not_network = (TextView) findViewById(R.id.Chat_no_network);

	final Bundle bundle = getIntent().getExtras();
	if (bundle != null) {

	    userId = bundle.getString(VegaConstants.USER_ID);
	}
	final String url = serverRequests.getRequestURL(ServerRequests.CHAT_WALL,
		userId);
	Logger.warn(tag, "Chat url is:" + url);

	getWindow().setFeatureInt(Window.FEATURE_PROGRESS,
		Window.PROGRESS_VISIBILITY_ON);

	chat_view = (WebView) findViewById(R.id.chat_web_UI);
	chat_view.getSettings().setJavaScriptEnabled(true);

	chat_view.setWebChromeClient(new WebChromeClient() {
	    @Override
	    public void onProgressChanged(WebView view, int progress) {
		activity.setTitle("Loading...");
		activity.setProgress(progress * 100);

		if (progress == 100)
		    activity.setTitle("complete");
	    }
	});

	chat_view.setWebViewClient(new WebViewClient() {
	    @Override
	    public void onReceivedError(WebView view, int errorCode,
		    String description, String failingUrl) {
		// Handle the error
	    }

	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	    }
	});

	chatMenuButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		final Intent actionBar = new Intent(v.getContext(), ActionBar.class);
		startActivity(actionBar);
	    }
	});

	chat_view.loadUrl(url);

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

	refreshChat.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub
		chat_view.reload();
	    }
	});

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	// TODO Auto-generated method stub
	return "ChatActivity";
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
     */
    @Override
    public void onNetworkAvailable() {
	// TODO Auto-generated method stub
	runOnUiThread(new Runnable() {

	    @Override
	    public void run() {
		// TODO Auto-generated method stub
		chat_view.setVisibility(View.VISIBLE);
		chat_not_network.setVisibility(View.GONE);

	    }
	});
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
     */
    @Override
    public void onNetworkUnAvailable() {
	runOnUiThread(new Runnable() {

	    @Override
	    public void run() {
		// TODO Auto-generated method stub
		chat_not_network.setVisibility(View.VISIBLE);
		chat_view.setVisibility(View.GONE);
	    }
	});
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onResume()
     */
    @Override
    public void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	if (!appData.isLoginStatus()) {
	    final Intent loginIntent = new Intent(this, LoginActivity.class);
	    startActivity(loginIntent);
	    finish();
	}
    }
}
