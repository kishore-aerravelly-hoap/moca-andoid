/**
 * 
 */
package com.pearl.activities;

import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.logger.Logger;
import com.pearl.parsers.XMLParser;

// TODO: Auto-generated Javadoc
/**
 * The Class DictionaryDialogActivity.
 */
public class DictionaryDialogActivity extends BaseActivity {
    
    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.dictionary);
	final TextView dictionaryMeaning = (TextView) findViewById(R.id.dictionary_meaning);
	final Bundle bundle = this.getIntent().getExtras();
	final String searchTerm = bundle.getString("SearchString");
	final String meaning = XMLParser.getDictionaryMeaning(appData
		.getDictionaryMeaningXML());
	Logger.warn(tag, "meaning is:" + meaning);
	if (meaning == "") {
	    dictionaryMeaning.setText("There is no such word " + searchTerm);
	    // dictionaryMeaning.
	} else
	    dictionaryMeaning.setText(Html.fromHtml(meaning));
	setFinishOnTouchOutside(true);
    }

    /**
     * Called to save notes when tapped outside.
     */
    @Override
    public void onStop() {
	super.onStop();
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "Dictionary";
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