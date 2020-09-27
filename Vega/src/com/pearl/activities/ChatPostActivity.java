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
 * The Class ChatPostActivity.
 */
public class ChatPostActivity extends BaseActivity {

    /** The chat message. */
    EditText chatMessage;
    
    /** The post chat. */
    Button postChat;
    
    /** The Constant POST. */
    public static final String POST = "POST";

    /* (non-Javadoc)
     * @see com.pearl.activities.BaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.chat_post);
	chatMessage = (EditText) findViewById(R.id.postMsg);
	postChat = (Button) findViewById(R.id.postChatButton);
	postChat.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		final Intent ReturnIntent = new Intent();
		ReturnIntent.putExtra(POST, chatMessage.getText().toString());
		setResult(RESULT_OK, ReturnIntent);
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
	return "Chat";
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
