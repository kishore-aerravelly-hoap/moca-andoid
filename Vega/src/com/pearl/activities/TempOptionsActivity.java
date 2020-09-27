package com.pearl.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.application.VegaConfiguration;
import com.pearl.application.VegaConstants;
import com.pearl.exceptions.ColumnDoesNoteExistsException;
import com.pearl.exceptions.InvalidConfigAttributeException;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class TempOptionsActivity.
 */
public class TempOptionsActivity extends BaseActivity implements
AdapterView.OnItemSelectedListener {

    /** The mst. */
    EditText mst;
    
    /** The est. */
    EditText est;
    
    /** The pd. */
    EditText pd;
    
    /** The save. */
    Button save;
    
    /** The config alert. */
    TextView configAlert;
    
    /** The exam server. */
    EditText examServer;
    
    /** The morning time. */
    String morningTime;
    
    /** The evetime. */
    String evetime;
    
    /** The period duration. */
    String periodDuration;
    
    /** The att frequency. */
    String attFrequency;
    
    /** The server base path. */
    EditText serverBasePath;
    
    /** The server php path. */
    EditText serverPHPPath;
    
    /** The test server path. */
    EditText testServerPath;
    
    /** The base path. */
    String basePath;
    
    /** The php path. */
    String phpPath;
    
    /** The test path. */
    String testPath;
    
    /** The attendance frequency spinner. */
    Spinner attendanceFrequencySpinner;
    
    /** The spinner items. */
    String spinnerItems[] = new String[] { "daily", "sessionly", "periodically" };

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.temp_options);
	vegaConfig = new VegaConfiguration(this);

	configAlert = (TextView) findViewById(R.id.attConfigAlert);
	save = (Button) findViewById(R.id.saveTempOptions);
	serverBasePath = (EditText) findViewById(R.id.basePath);

	setInitialValues();
	save.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		basePath = serverBasePath.getText().toString();

		try {

		    vegaConfig.setValue(VegaConstants.SERVER_BASE_PATH,
			    basePath);

		} catch (final ColumnDoesNoteExistsException e) {
		    Logger.error(tag, e);
		}
		configAlert.setVisibility(View.VISIBLE);
	    }
	});
	setFinishOnTouchOutside(true);
    }

    /**
     * Sets the initial values.
     */
    private void setInitialValues() {
	try {

	    serverBasePath.setText(vegaConfig
		    .getValue(VegaConstants.SERVER_BASE_PATH));

	} catch (final InvalidConfigAttributeException e) {
	    Logger.error(tag, e);
	}

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "Temp Options";
    }

    /* (non-Javadoc)
     * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
	    long arg3) {
	// TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
     */
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub

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
