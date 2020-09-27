package com.pearl.activities;
/**
 * @author spasnoor
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.ui.models.RoleType;

// TODO: Auto-generated Javadoc
/**
 * The Class TeacherExamsActivity.
 */
public class TeacherExamsActivity extends Activity implements OnClickListener {

    /** The rejected_tests. */
    private RelativeLayout create_test, publish_test, my_approval,
    awaiting_approval, awaiting_evaluation, awaiting_results_publish,
    results_publihed_tests, rejected_tests;
    
    /** The app data. */
    private ApplicationData appData;
    
    /** The menu button. */
    private ImageView menuButton;
    
    private TextView myApproval;
    
    /** The tag. */
    private final String tag="TeacherExamsActivity";

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.teacher_exams_activities);
	appData = (ApplicationData) getApplication();
	create_test = (RelativeLayout) findViewById(R.id.create_test);
	publish_test = (RelativeLayout) findViewById(R.id.publish_test);
	my_approval = (RelativeLayout) findViewById(R.id.teacher_my_approval);
	awaiting_approval = (RelativeLayout) findViewById(R.id.teacher_awaiting_approval);
	awaiting_evaluation = (RelativeLayout) findViewById(R.id.teacher_awaiting_evaluation);
	awaiting_results_publish = (RelativeLayout) findViewById(R.id.awaitng_results_publish);
	results_publihed_tests = (RelativeLayout) findViewById(R.id.results_published_tests);
	rejected_tests = (RelativeLayout) findViewById(R.id.teacher_rejected_test);
	menuButton=(ImageView)findViewById(R.id.teacher_menu_button);
	myApproval = (TextView) findViewById(R.id.my_approval);

	setRestrictionsForTeacher();

    }

    /**
     * Check for teacher role type.
     *
     * @return the int
     */
    public int checkForTeacherRoleType() {
	int Role = 0;
	if(appData!=null && appData.getUser()!=null && appData.getUser().getUserType()!=null){

	    if (appData.getUser().getUserType()
		    .equalsIgnoreCase(RoleType.HOMEROOMTEACHER.name())
		    || appData.getUser().getUserType()
		    .equalsIgnoreCase(RoleType.TEACHER.name())) {
		Role = 1;

	    } else if (appData.getUser().getUserType()
		    .equalsIgnoreCase(RoleType.PRINCIPLE.name())
		    || appData.getUser().getUserType()
		    .equalsIgnoreCase(RoleType.SUBJECTHEAD.name())) {
		Role = 2;
	    }
	    return Role;
	}
	return 0;
    }

    /**
     * Sets the restrictions for teacher.
     */
    public void setRestrictionsForTeacher() {
	Log.i(tag,"I am in setRestrictionsFor teacher method");
	switch (checkForTeacherRoleType()) {
	case 1: {
		myApproval.setText("You don't have access to approve exams");
	    my_approval.setClickable(false);
	    break;
	}
	case 2:
	    my_approval.setVisibility(View.VISIBLE);
	    my_approval.setOnClickListener(this);
	    break;
	case 0:
	    final Intent nullUser=new Intent(TeacherExamsActivity.this,DashboardActivity.class);
	    appData.getUser();
	    startActivity(nullUser);
	    finish();
	default:
	    break;
	}



	create_test.setOnClickListener(this);
	publish_test.setOnClickListener(this);
	awaiting_approval.setOnClickListener(this);
	awaiting_results_publish.setOnClickListener(this);
	results_publihed_tests.setOnClickListener(this);
	awaiting_evaluation.setOnClickListener(this);
	rejected_tests.setOnClickListener(this);
	menuButton.setOnClickListener(this);
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
	super.onResume();
	if(!appData.isLoginStatus()) {

	    final Intent login = new Intent(this, LoginActivity.class);
	    startActivity(login);
	    Logger.info(tag,
		    "LOGIN status if shelf..." + appData.isLoginStatus());
	    finish();

	}
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.create_test:
	    final Intent createTest = new Intent(TeacherExamsActivity.this,
		    UnderConstructionTestsActivity.class);
	    startActivity(createTest);
	    //finish();
	    break;
	case R.id.teacher_my_approval:
	    final Intent i = new Intent(TeacherExamsActivity.this,
		    MyApprovalFragmemtActivity.class);
	    startActivity(i);
	    //finish();
	    break;

	case R.id.publish_test:
	    final Intent i1 = new Intent(TeacherExamsActivity.this,
		    TeacherPublishFragmentActivity.class);
	    startActivity(i1);
	    break;
	case R.id.teacher_awaiting_approval:
	    final Intent i2 = new Intent(TeacherExamsActivity.this,
		    MyAwaitingApprovalActivity.class); 
	    startActivity(i2);
	    break;

	case R.id.teacher_awaiting_evaluation:
	    final Intent i7 = new Intent(TeacherExamsActivity.this,
		    AwaitingEvaluationListActivity.class); 
	    startActivity(i7);
	    break;
	case R.id.awaitng_results_publish:
	    final Intent i3 = new Intent(TeacherExamsActivity.this,
		    TeacherAwaitingResultsFragmentActivity.class);
	    startActivity(i3);
	    break;
	case R.id.results_published_tests:
	    final Intent i4 = new Intent(TeacherExamsActivity.this,
		    ResultsPublishFragmentActivity.class);
	    startActivity(i4);
	    break;
	case R.id.teacher_rejected_test:
	    final Intent i5 = new Intent(TeacherExamsActivity.this,
		    RejectedTestsFragmentActivity.class);
	    startActivity(i5);
	    break;
	case R.id.teacher_menu_button:
	    final Intent i8=new Intent(TeacherExamsActivity.this,ActionBar.class);
	    startActivity(i8);
	    break;

	}
    }

}
