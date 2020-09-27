package com.pearl.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.pearl.R;

/**
 * The Class TeacherNoticeBoardFragmentActivity.
 */
public class TeacherNoticeBoardFragmentActivity extends FragmentActivity{
    

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle arg0) {
	super.onCreate(arg0);
	setContentView(R.layout.teacher_notice_board);
	getSupportFragmentManager()
	.findFragmentById(R.id.notice_board_create);
    }



}
