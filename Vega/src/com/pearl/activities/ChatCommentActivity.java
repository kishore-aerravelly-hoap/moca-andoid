package com.pearl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.pearl.R;

// TODO: Auto-generated Javadoc
/**
 * The Class ChatCommentActivity.
 */
public class ChatCommentActivity extends BaseActivity {

    /** The comment view. */
    EditText commentView;
    
    /** The comment. */
    Button comment;
    
    /** The Constant COMMENT. */
    private static final String COMMENT = "COMMENT";
    
    /** The Constant MESSAGE_ID. */
    private static final String MESSAGE_ID = "MESSAGE_ID";
    
    /** The bundle. */
    private Bundle bundle;

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	;
	setContentView(R.layout.chat_comment_activity);
	commentView = (EditText) findViewById(R.id.writeComment);
	comment = (Button) findViewById(R.id.commentButton);
	bundle = this.getIntent().getExtras();

	comment.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		final Intent returnIntent = new Intent();
		returnIntent
		.putExtra(COMMENT, commentView.getText().toString());
		returnIntent.putExtra(MESSAGE_ID, bundle.getString(MESSAGE_ID));
		setResult(RESULT_OK, returnIntent);
		finish();
	    }
	});

	setFinishOnTouchOutside(true);
    }

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#getActivityType()
     */
    @Override
    public String getActivityType() {
	return "chat comment";
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
