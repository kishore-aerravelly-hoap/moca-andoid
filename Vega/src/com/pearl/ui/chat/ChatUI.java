package com.pearl.ui.chat;

import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.pearl.activities.ChatWallActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class ChatUI.
 */
public class ChatUI {

    /** The chat ui. */
    private static ChatUI chatUI = null;

    /**
     * Instantiates a new chat ui.
     */
    private ChatUI() {
    }

    /**
     * Gets the single instance of ChatUI.
     *
     * @return single instance of ChatUI
     */
    public static ChatUI getInstance() {
	if (chatUI == null) {
	    chatUI = new ChatUI();
	}

	return chatUI;
    }

    /**
     * Builds the.
     *
     * @param activity the activity
     * @param relativeLayout the relative layout
     * @return the relative layout
     */
    public RelativeLayout build(ChatWallActivity activity,
	    RelativeLayout relativeLayout) {
	if (relativeLayout == null) {
	    relativeLayout = new RelativeLayout(activity);
	    relativeLayout.setLayoutParams(new LayoutParams(
		    android.view.ViewGroup.LayoutParams.FILL_PARENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT));
	}
	return null;
    }
}
