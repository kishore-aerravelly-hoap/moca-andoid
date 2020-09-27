package com.pearl.activities;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.network.request.ServerRequests;

// TODO: Auto-generated Javadoc
/**
 * The Class AnouncementActivity.
 */
public class AnouncementActivity extends BaseActivity {
    
    /** The message. */
    EditText message;
    
    /** The anounce button. */
    Button anounceButton;

    /** The app data. */
    ApplicationData appData;

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.anouncement);

	message = (EditText) findViewById(R.id.message);
	anounceButton = (Button) findViewById(R.id.anounceButton);

	appData = (ApplicationData) getApplication();

	anounceButton.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Logger.warn(tag, "Entered anounce button on click");

		String anouncement = message.getText().toString();

		try {
		    anouncement = URLEncoder.encode(message.getText()
			    .toString(), "UTF-8");
		} catch (final UnsupportedEncodingException e) {
		    Logger.error("Anouncement Message", e);
		}

		final ServerRequests serverRequests = new ServerRequests(
			getApplicationContext());
		final String url = serverRequests.getRequestURL(
			ServerRequests.ANOUNCEMENT, anouncement);

		new Thread(new Runnable() {
		    @Override
		    public void run() {
			URL requestURL;
			try {
			    Logger.info("AnouncementActivity-Runnable",
				    "Entered here");

			    requestURL = new URL(url);
			    requestURL.openStream();
			} catch (final Exception e) {
			    Logger.error("Anouncement Activity", e);
			}
		    }
		}).start();

		final Intent returnIntent = new Intent();
		returnIntent.putExtra("anouncement", message.getText()
			.toString());
		AnouncementActivity.this.setResult(RESULT_OK, returnIntent);
		AnouncementActivity.this.finish();

		Logger.warn(tag, "anouncement :" + message.getText().toString());
	    }
	});

	setFinishOnTouchOutside(true);
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onResume()
     */
    @Override
    public void onResume() {
	super.onResume();
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "announcemnet";
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkAvailable()
     */
    @Override
    public void onNetworkAvailable() {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onNetworkUnAvailable()
     */
    @Override
    public void onNetworkUnAvailable() {
	// TODO Auto-generated method stub

    }
}