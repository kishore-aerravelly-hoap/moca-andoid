/**
 * 
 */
package com.pearl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pearl.R;

// TODO: Auto-generated Javadoc
/**
 * The Class FAQActivity.
 */
public class FAQActivity extends BaseActivity {

    /** The btn panel1. */
    Button btnPanel1;
    
    /** The btn panel2. */
    Button btnPanel2;
    
    /** The btn panel3. */
    Button btnPanel3;
    
    /** The btn panel4. */
    Button btnPanel4;
    
    /** The menu button. */
    Button menuButton;
    
    /** The layout panel1. */
    View layoutPanel1;
    
    /** The layout panel2. */
    View layoutPanel2;
    
    /** The layout panel3. */
    View layoutPanel3;
    
    /** The layout panel4. */
    View layoutPanel4;
    
    /** The h. */
    public static Handler h;

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.faq);
	btnPanel1 = (Button) findViewById(R.id.btnPanel);
	btnPanel2 = (Button) findViewById(R.id.btnPane2);
	btnPanel3 = (Button) findViewById(R.id.btnPane3);
	btnPanel4 = (Button) findViewById(R.id.btnPane4);

	layoutPanel1 = findViewById(R.id.layoutPanel1);
	layoutPanel1.setVisibility(View.GONE);

	layoutPanel2 = findViewById(R.id.layoutPanel2);
	layoutPanel2.setVisibility(View.GONE);

	layoutPanel3 = findViewById(R.id.layoutPanel3);
	layoutPanel3.setVisibility(View.GONE);

	layoutPanel4 = findViewById(R.id.layoutPanel4);
	layoutPanel4.setVisibility(View.GONE);

	menuButton = (Button) findViewById(R.id.LasallemenuButton);
	updateUI();

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

    }

    /**
     * Updates the UI with the changes.
     */

    private void updateUI() {

	menuButton.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// TODO Auto-generated method stub

		final Intent newActivity = new Intent(getApplicationContext(),
			ActionBar.class);
		startActivity(newActivity);

	    }
	});

	btnPanel1.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		layoutPanel1.setVisibility(View.VISIBLE);
		layoutPanel2.setVisibility(View.GONE);
		layoutPanel3.setVisibility(View.GONE);
		layoutPanel4.setVisibility(View.GONE);
	    }
	});

	btnPanel2.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		layoutPanel1.setVisibility(View.GONE);
		layoutPanel2.setVisibility(View.VISIBLE);
		layoutPanel3.setVisibility(View.GONE);
		layoutPanel4.setVisibility(View.GONE);
	    }
	});

	btnPanel3.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		layoutPanel1.setVisibility(View.GONE);
		layoutPanel2.setVisibility(View.GONE);
		layoutPanel3.setVisibility(View.VISIBLE);
		layoutPanel4.setVisibility(View.GONE);
	    }
	});

	btnPanel4.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		layoutPanel1.setVisibility(View.GONE);
		layoutPanel2.setVisibility(View.GONE);
		layoutPanel3.setVisibility(View.GONE);
		layoutPanel4.setVisibility(View.VISIBLE);
	    }
	});

    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "FAQ";
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
