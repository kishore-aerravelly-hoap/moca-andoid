package com.pearl.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class VegaConfigurationActivity.
 */
public class VegaConfigurationActivity extends BaseActivity implements
AdapterView.OnItemSelectedListener {

    /** The config alert. */
    TextView configAlert;
    
    /** The toast time. */
    EditText toastTime;
    
    /** The saveconfig vlaues. */
    Button saveconfigVlaues;
    
    /** The drm. */
    Spinner drm;
    
    /** The toast display. */
    String toastDisplay;
    
    /** The drm text. */
    String drmText;
    
    /** The drm list. */
    String drmList[] = new String[] { "NO", "YES" };

    /** The vega config. */
    private VegaConfiguration vegaConfig;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.vega_configuration);
	vegaConfig = new VegaConfiguration(this);
	toastTime = (EditText) findViewById(R.id.toastTime);
	configAlert = (TextView) findViewById(R.id.configAlert);
	saveconfigVlaues = (Button) findViewById(R.id.saveConfiguration);

	setInitialValues();
	saveconfigVlaues.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Logger.warn(tag, "in onclick()");
		toastDisplay = toastTime.getText().toString();
		if (/* (Integer.parseInt(toastTime.getText().toString()) < 1 )|| */toastTime
			.getText().toString().equals("")
			|| toastTime.getText().equals(null)) {
		    final AlertDialog.Builder alert = new AlertDialog.Builder(
			    activityContext);
		    alert.setTitle("Set display time");
		    alert.setIcon(R.drawable.warning_f);
		    alert.setMessage("Display time cannot be empty");
		    alert.setPositiveButton("OK",
			    new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
				int which) {
			    // TODO Auto-generated method stub
			    toastTime.setText("" + 1000);
			}
		    });
		    alert.setNegativeButton("Cancel",
			    new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
				int which) {
			    // TODO Auto-generated method stub
			    dialog.dismiss();
			}
		    });

		    final AlertDialog alertTime = alert.create();
		    alertTime.show();

		}
		try {
		    vegaConfig.setValue(VegaConstants.ALERT_DISPLAY_TIME,
			    toastDisplay);
		} catch (final Exception e) {
		    Logger.error(tag, e.toString());
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
	    toastDisplay = vegaConfig
		    .getValue(VegaConstants.ALERT_DISPLAY_TIME);
	    toastTime.setText(toastDisplay);

	} catch (final Exception e) {
	    Logger.error(tag, e.toString());
	}
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onDestroy()
     */
    @Override
    public void onDestroy() {
	super.onDestroy();
	// vegaConfig.close();
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {

	return "CONFIGURATION";
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
