package com.pearl.activities;

import android.os.Bundle;

import com.pearl.R;
import com.pearl.examination.Exam;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamDetailsActivity.
 */
public class ExamDetailsActivity extends BaseActivity {
    
    /** The exam. */
    private Exam exam;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.exam_details);

	updateUI();
    }

    /**
     * Update ui.
     */
    public void updateUI() {
	// TODO
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "Showing Details of the exam '"
		+ exam.getExamDetails().getTitle() + "'";
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