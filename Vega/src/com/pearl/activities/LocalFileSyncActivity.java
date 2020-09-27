package com.pearl.activities;


import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class LocalFileSyncActivity.
 */
public class LocalFileSyncActivity extends BaseActivity {
    
    /** The sync. */
    Button sync;
    
    /** The app data. */
    ApplicationData appData;
    
    /** The hidden file name. */
    File hiddenFileName;
    
    /** The synced. */
    boolean synced;
    
    /** The new file name. */
    String newFileName = ".PearlAppData";
    
    /** The h. */
    public static Handler h;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.sync_local_file);
	sync = (Button) findViewById(R.id.sync_with_local);
	appData = (ApplicationData) getApplication();
	sync.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		syncWithExistingFile();
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
    }

    /**
     * Sync with existing file.
     */
    private void syncWithExistingFile() {
	final File oldFile = new File(appData.getAppPrivateFilesPath());
	if (oldFile.exists()) {
	    hiddenFileName = new File(Environment.getExternalStorageDirectory()
		    .toString() + "/" + newFileName);
	    hiddenFileName.mkdirs();
	    boolean renameFlag;
	    renameFlag = oldFile.renameTo(hiddenFileName);
	    if (renameFlag) {
		Logger.warn(tag, "sync - renaming dir status is: " + renameFlag);
		synced = true;
		appData.setAppPrivateFilesPath(newFileName);
		Logger.warn(
			tag,
			"sync - hidden file name is:"
				+ appData.getAppPrivateFilesPath());
		if (hiddenFileName.exists())
		    Logger.warn(tag, "sync - hidden file exists");
		else
		    Logger.info(tag, "sync - hidden file does not exists");
		if (hiddenFileName.isHidden())
		    Logger.warn(tag, "sync - file is hidden");
		else
		    Logger.warn(tag, "sync - file is not hidden");
	    } else {
		Logger.warn(tag, "sync - file not renamed");
	    }
	}
	if (synced) {
	    Logger.warn(tag, "sync - synced enable shelf button");
	    final Intent i = new Intent(this, DashboardActivity.class);
	    i.putExtra("LOGIN", "LOGIN");
	    i.putExtra("USERID", appData.getUserId());
	    startActivity(i);
	    finish();
	} else {
	    Logger.warn(tag, "sync - not synced");
	}
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	// TODO Auto-generated method stub
	return null;
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
