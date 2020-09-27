package com.pearl.activities;
import java.util.List;

import android.app.Dialog;
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
import com.pearl.fragment.ResultsPublish_ExamListFragment;
import com.pearl.fragment.ResultsPublish_ExamListFragment.ResultsPublishInterface;
import com.pearl.help.HelpParser;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultsPublishFragmentActivity.
 */
public class ResultsPublishFragmentActivity extends FragmentActivity implements ResultsPublishInterface{

    /** The details inline. */
    private boolean detailsInline = false;
    
    /** The i. */
    private int i = 0;

    private Button menuButton;
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle arg0) {
	super.onCreate(arg0);
	setContentView(R.layout.results_publish_fragment_activity);
	menuButton = (Button) findViewById(R.id.examMenu);
	

	ImageView help = (ImageView) findViewById(R.id.results_publish_help);
	
	menuButton.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			final Intent i = new Intent(
					ResultsPublishFragmentActivity.this, ActionBar.class);
			startActivity(i);
		}
	});
	
	help.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showDialog();
		}
	});

	final ResultsPublish_ExamListFragment resultsPublish = (ResultsPublish_ExamListFragment) getSupportFragmentManager()
		.findFragmentById(R.id.results_publish_exam_list_fragment_id);

	resultsPublish.setListener(this);
	final Fragment frag = getSupportFragmentManager().findFragmentById(
		R.id.results_publish_students_list_fragment_id);
	detailsInline = frag != null && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	if (detailsInline)
	    resultsPublish.enablePersistentSelection();

    }

    /* (non-Javadoc)
     * @see com.pearl.fragment.ResultsPublish_ExamListFragment.ResultsPublishInterface#onClick(java.lang.String)
     */
    @Override
    public void onClick(String s) {

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
		final List<String> list = HelpParser.getHelpContent("quizzard_results_publish.txt", this);
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
