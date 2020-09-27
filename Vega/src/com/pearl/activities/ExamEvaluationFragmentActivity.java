package com.pearl.activities;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
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
import com.pearl.application.VegaConstants;
import com.pearl.fragment.EvaluationStudentListFragment;
import com.pearl.help.HelpParser;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamEvaluationFragmentActivity.
 */
public class ExamEvaluationFragmentActivity extends FragmentActivity {

    /** The exam id. */
    private String examId;
    private Button menuButton;
    private ImageView help;
    private int i = 0;

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.test_evaluation); 
	menuButton = (Button) findViewById(R.id.examMenu);
	help = (ImageView) findViewById(R.id.evaluation_help);

	menuButton.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			final Intent i = new Intent(
					ExamEvaluationFragmentActivity.this, ActionBar.class);
			startActivity(i);			
		}
	});
	
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
	final EvaluationStudentListFragment studentListFragment = (EvaluationStudentListFragment) getSupportFragmentManager()
		.findFragmentById(R.id.studentListFragment);
	final Bundle bundle = getIntent().getExtras();
	if(bundle != null)
	    examId = bundle.getString(VegaConstants.EXAM_ID);
	studentListFragment.dataFromFragmentActivity(examId);

	getSupportFragmentManager().findFragmentById(R.id.AnswerSheetFragment);
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
		AssetManager manager = getAssets();
		final List<String> list = HelpParser.getHelpContent("quizzard_evaluation.txt", manager);
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
