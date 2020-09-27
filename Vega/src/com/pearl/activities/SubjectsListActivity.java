package com.pearl.activities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jackson.map.ObjectMapper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pearl.R;
import com.pearl.application.VegaConstants;
import com.pearl.chat.server.response.BaseResponse;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.logger.Logger;
import com.pearl.network.request.ServerRequests;

// TODO: Auto-generated Javadoc
/**
 * The Class SubjectsListActivity.
 */
public class SubjectsListActivity extends BaseActivity {

    /** The subjects list. */
    ListView subjectsList;
    
    /** The list. */
    String list[] = new String[] { "Maths", "Science", "social" };
    
    /** The bundle. */
    Bundle bundle;
    
    /** The user id. */
    String userId;
    
    /** The date. */
    String date;
    
    /** The server requests. */
    private ServerRequests serverRequests;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.subject_list);
	serverRequests = new ServerRequests(this);

	bundle = this.getIntent().getExtras();
	if (bundle != null) {
	    userId = bundle.getString("user_id");
	    date = bundle.getString("Date");
	} else {
	    userId = appData.getUserId();
	}

	final String url = serverRequests.getRequestURL(ServerRequests.SUBJECTS_LIST,
		userId);
	Logger.warn(tag, "url is:" + url);
	getSubjects(url);
	appData.setActivityName(tag);
	subjectsList = (ListView) findViewById(R.id.subjectList);
	subjectsList.setAdapter(new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, list));
	subjectsList.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> a, View v, int position,
		    long id) {
		final String value = subjectsList.getItemAtPosition(position)
			.toString();
		try {
		    vegaConfig.setValue(VegaConstants.HISTORY_SUBJECT, value);
		} catch (final ColumnDoesNoteExistsException e) {
		    Logger.warn(tag, e.toString());
		    ToastMessageForExceptions(R.string.COLUMN_DOES_NOT_EXIST_NOTICEBOARDACTIVITY);
		    backToDashboard();
		}
		final Intent intent = new Intent();
		intent.putExtra("Subject", value);
		Logger.info(tag, "subject value before passing is:" + value);
		Logger.info(tag, "userId before passing is:" + userId);
		intent.putExtra("user_id", userId);
		intent.setClass(v.getContext(), NoteBookActivity.class);
		startActivity(intent);
	    }
	});
    }

    /**
     * Gets the subjects.
     *
     * @param requestUrl the request url
     * @return the subjects
     */
    private void getSubjects(final String requestUrl) {
	Logger.warn(tag, "in getSubjects()");
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		java.net.URL url;
		try {
		    url = new URL(requestUrl);
		    final BufferedReader br = new BufferedReader(
			    new InputStreamReader(url.openStream()));
		    String str;
		    String responsString = null;
		    while ((str = br.readLine()) != null) {
			Logger.warn(tag, "String is:" + str);
			responsString = str;
		    }
		    final ObjectMapper mapper = new ObjectMapper();
		    Logger.warn(tag, "Response string is:" + responsString);
		    final BaseResponse response = mapper.readValue(responsString,
			    BaseResponse.class);
		    Logger.warn(tag, "response data is:" + response.getData());
		    response.getData();

		    if (response.getData() != null) {
			Logger.warn(tag, "response data in string format is:"
				+ response.getData().toString());
		    } else
			Logger.warn(tag, "response data is null");

		} catch (final MalformedURLException e) {
		    Logger.error(tag, e);
		    ToastMessageForExceptions(R.string.MALFORMED_URL_EXCEPTION);
		    backToDashboard();
		} catch (final IOException e) {
		    Logger.error(tag, e);
		    ToastMessageForExceptions(R.string.IO_EXCEPTION);
		    backToDashboard();

		}
	    }
	}).start();
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	// TODO Auto-generated method stub
	return "list_of_subjects";
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
