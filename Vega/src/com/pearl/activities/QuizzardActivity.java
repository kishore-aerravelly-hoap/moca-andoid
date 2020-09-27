package com.pearl.activities;


import android.os.Bundle;
import android.webkit.WebView;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.network.request.ServerRequests;

// TODO: Auto-generated Javadoc
/**
 * The Class QuizzardActivity.
 */
public class QuizzardActivity extends BaseActivity {
    
    /** The application data. */
    private ApplicationData applicationData;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	applicationData = (ApplicationData) getApplication();
	setContentView(R.layout.quizzard);
	final ServerRequests serverRequests = new ServerRequests(
		getApplicationContext());
	final Bundle bundle = getIntent().getExtras();
	final String urlType = bundle.getString("type");

	final WebView quizwebview = (WebView) findViewById(R.id.quizewebview);
	quizwebview.clearCache(true);
	quizwebview.getSettings().setJavaScriptEnabled(true);
	quizwebview.getSettings().setDomStorageEnabled(true);
	final String userid = applicationData.getUserId();

	if ("teacher".equals(urlType))
	    quizwebview.loadUrl(serverRequests.getRequestURL(
		    ServerRequests.QUIZ_HOME_TEACHER, "" + userid));
	else if ("student".equals(urlType))
	    quizwebview.loadUrl(serverRequests.getRequestURL(
		    ServerRequests.QUIZ_HOME_STUDENT, "" + userid));
	else if ("score".equals(urlType))
	    quizwebview.loadUrl(serverRequests.getRequestURL(
		    ServerRequests.QUIZ_RESULT, "" + userid));
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "quiz";
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