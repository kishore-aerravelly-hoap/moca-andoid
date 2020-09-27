package com.pearl.activities;

/**
 * @author sahitya pasnoor
 */

import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.fragment.TeacherAwaitingApprovalListFragment;
import com.pearl.fragment.TeacherAwaitingApprovalListFragment.TeacherAwaitingApprovalListListener;
import com.pearl.help.HelpParser;

// TODO: Auto-generated Javadoc
/**
 * The Class MyAwaitingApprovalActivity.
 */
public class MyAwaitingApprovalActivity extends FragmentActivity implements
		TeacherAwaitingApprovalListListener {
	private Button menuButton;

	/** The details inline. */
	private boolean detailsInline = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.my_approval_fragment_activity);
		ImageView help = (ImageView) findViewById(R.id.teacher_approval_help);
		menuButton = (Button) findViewById(R.id.examMenu);
		help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog();
			}
		});
		
		menuButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent i = new Intent(MyAwaitingApprovalActivity.this,
						ActionBar.class);
				startActivity(i);
			}
		});

		final TeacherAwaitingApprovalListFragment teacherApproval = (TeacherAwaitingApprovalListFragment) getSupportFragmentManager()
				.findFragmentById(R.id.my_approval_list);

		teacherApproval.setListener(this);

		final Fragment frag = getSupportFragmentManager().findFragmentById(
				R.id.my_approval_exam);
		detailsInline = frag != null
				&& getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

		if (detailsInline)
			teacherApproval.enablePersistentSelection();

	}

	/**
	 * Show dialog.
	 */
	private void showDialog() {
		int i = 0;
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.pearl_tips_layout);
		RelativeLayout layout = (RelativeLayout) dialog
				.findViewById(R.id.tips_layout);
		final TextView tips = (TextView) dialog.findViewById(R.id.tips);
		ImageView previous = (ImageView) dialog.findViewById(R.id.previous);
		ImageView next = (ImageView) dialog.findViewById(R.id.next);
		layout.setBackgroundResource(R.drawable.attendance_help);
		final List<String> list = HelpParser.getHelpContent(
				"quizzard_awating_aprroval.txt", this);
		if (list != null && list.size() > 0) {
			tips.setText(list.get(0));
		}
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int i = 0;
				if (list != null && list.size() > 0) {
					if (i < (list.size() - 1)) {
						i = i + 1;
						tips.setEnabled(true);
						tips.setText(list.get(i));
					} else
						tips.setEnabled(false);
				}
			}
		});
		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int i = 0;
				if (list != null && list.size() > 0) {
					if (i > 0) {
						i = i - 1;
						tips.setEnabled(true);
						tips.setText(list.get(i));
					} else
						tips.setEnabled(false);
				}
			}
		});
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pearl.fragment.TeacherAwaitingApprovalListFragment.
	 * TeacherAwaitingApprovalListListener#onClick(java.lang.String)
	 */
	@Override
	public void onClick(String s) {
		// TODO Auto-generated method stub

	}

}
