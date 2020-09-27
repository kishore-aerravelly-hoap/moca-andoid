package com.pearl.activities;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.fragment.AwaitingResultsPublishFragment;
import com.pearl.help.HelpParser;

public class TeacherAwaitingResultsFragmentActivity extends FragmentActivity {

    private boolean detailsInline = false;
    private Button menuButton;
    private int i=0;
    private ImageView help;

    @Override
    protected void onCreate(Bundle arg0) {
	super.onCreate(arg0);
	setContentView(R.layout.awaiting_results_publish_fragment);
	menuButton = (Button) findViewById(R.id.examMenu);
	help = (ImageView) findViewById(R.id.teacher_approval_help);
	help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog();
			}
		});

	final ApplicationData appData = (ApplicationData)getApplicationContext();
	
	if(appData.getUserId() == null){
	    final AlertDialog.Builder alert = new AlertDialog.Builder(this);
	    alert.setTitle("");
	    alert.setMessage(R.string.logged_out);
	    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
		    final Intent i = new Intent(getApplicationContext(),LoginActivity.class);
		    startActivity(i);
		    dialog.cancel();
		}
	    });
	    alert.show();
	}	
	
	menuButton.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final Intent i = new Intent(
					TeacherAwaitingResultsFragmentActivity.this, ActionBar.class);
			startActivity(i);			
		}
	});
	

	final AwaitingResultsPublishFragment resultsPublish = (AwaitingResultsPublishFragment) getSupportFragmentManager()
		.findFragmentById(R.id.awaiting_results_publish_exam_list_fragment_id);

	final Fragment frag = getSupportFragmentManager().findFragmentById(
		R.id.awaiting_results_publish_students_list_fragment_id);
	detailsInline = frag != null && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	if (detailsInline)
	    resultsPublish.enablePersistentSelection();

    }
    
    /**
     * Show dialog.
     */
    private void showDialog(){
   	    i = 0;
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pearl_tips_layout);
		RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.tips_layout);
		final TextView tips = (TextView) dialog.findViewById(R.id.tips);
		ImageView previous = (ImageView) dialog.findViewById(R.id.previous);
		ImageView next = (ImageView) dialog.findViewById(R.id.next);
		layout.setBackgroundResource(R.drawable.attendance_help);
		final List<String> list = HelpParser.getHelpContent("awaiting_results_publish.txt", this);
		if(list != null && list.size() > 0){
			tips.setText(list.get(0));
		}
		
	next.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(list != null && list.size() > 0){
						if(i < (list.size() - 1)){
							i = i +1;
							tips.setEnabled(true);
							tips.setText(list.get(i));						
						}
						else
							tips.setEnabled(false);
					}
				}
			});
			previous.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(list != null && list.size() > 0){
						if(i > 0){
							i = i -1;
							tips.setEnabled(true);
							tips.setText(list.get(i));						
						}
						else
							tips.setEnabled(false);
					}
				}
			});
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
    }	

}
