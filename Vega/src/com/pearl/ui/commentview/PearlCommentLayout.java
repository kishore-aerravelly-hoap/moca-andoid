package com.pearl.ui.commentview;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pearl.chat.Comment;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class PearlCommentLayout.
 */
public class PearlCommentLayout extends RelativeLayout {
    
    /** The Constant tag. */
    private static final String tag = "pearlCommentLayout";
    
    /** The context. */
    private Context context;

    /**
     * Instantiates a new pearl comment layout.
     *
     * @param context the context
     */
    public PearlCommentLayout(Context context) {
	super(context);
    }

    /**
     * Gets the comment view.
     *
     * @param context the context
     * @param comment the comment
     * @param clicked the clicked
     * @return the comment view
     */
    public RelativeLayout getCommentView(Context context,
	    List<Comment> comment, boolean clicked) {
	RelativeLayout rootLayout = new RelativeLayout(context);
	this.context = context;
	final int commentSize = comment.size();
	Logger.warn(tag, "comment size in getCommentView is:" + commentSize);
	Logger.warn(tag, "clicked is:" + clicked);
	if (commentSize > 2) {
	    if (clicked) {
		Logger.warn(tag, "displaying all comments");
		rootLayout = this.displayComments(commentSize, rootLayout,
			comment);
	    } else
		rootLayout = this.displayComments(2, rootLayout, comment);
	} else if (commentSize <= 2)
	    rootLayout = this.displayComments(commentSize, rootLayout, comment);

	return rootLayout;
    }

    /**
     * Display comments.
     *
     * @param size the size
     * @param rootLayout the root layout
     * @param comment the comment
     * @return the relative layout
     */
    private RelativeLayout displayComments(int size, RelativeLayout rootLayout,
	    List<Comment> comment) {
	for (int i = 0; i < size; i++) {

	    Logger.warn(tag, "std name is:" + comment.get(i).getUserId());
	    Logger.warn(tag, "comment is:" + comment.get(i).getComment());
	    Logger.warn(tag, "date is:" + comment.get(i).getTimestamp());

	    Logger.warn("layout", "comment size is:" + comment.size());
	    final TextView stdName = new TextView(context);
	    stdName.setText(comment.get(i).getUserId());
	    stdName.setTextColor(Color.parseColor("#ff0000"));

	    final TextView commentView = new TextView(context);
	    commentView.setTextColor(Color.parseColor("#000000"));
	    commentView.setText(comment.get(i).getComment());

	    final TextView commentDate = new TextView(context);
	    commentDate.setText(converttimeStampToDate(comment.get(i)
		    .getTimestamp()));
	    commentDate.setTextColor(Color.parseColor("#000000"));

	    final ImageView image = new ImageView(context);
	    image.setScaleX(0.7f);
	    image.setScaleY(0.7f);

	    image.setId(i + 1);
	    // stdName.setId(i+2);
	    // commentDate.setId((i*2)+1);

	    new RelativeLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		    android.view.ViewGroup.LayoutParams.FILL_PARENT);

	    final RelativeLayout.LayoutParams commentParams = new RelativeLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

	    final RelativeLayout.LayoutParams nameParams = new RelativeLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

	    final RelativeLayout.LayoutParams dateParams = new RelativeLayout.LayoutParams(
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
		    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

	    final RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
		    40, 40);

	    if (i > 0) {
		imageParams.addRule(RelativeLayout.BELOW, i);
		dateParams.addRule(RelativeLayout.BELOW, image.getId());
	    }

	    rootLayout.addView(image, imageParams);

	    nameParams.addRule(RelativeLayout.BELOW, i);
	    nameParams.addRule(RelativeLayout.RIGHT_OF, image.getId());

	    nameParams.setMargins(0, 10, 0, 0);
	    commentParams.setMargins(150, 10, 0, 0);
	    dateParams.setMargins(0, 25, 0, 0);

	    commentParams.addRule(RelativeLayout.BELOW, i);
	    commentParams.addRule(RelativeLayout.RIGHT_OF, image.getId());

	    dateParams.addRule(RelativeLayout.RIGHT_OF, image.getId());
	    dateParams.addRule(RelativeLayout.BELOW, image.getId());

	    rootLayout.addView(stdName, nameParams);
	    rootLayout.addView(commentView, commentParams);
	    rootLayout.addView(commentDate, dateParams);
	}
	return rootLayout;

    }

    /**
     * Converttime stamp to date.
     *
     * @param timeStamp the time stamp
     * @return the string
     */
    private String converttimeStampToDate(Date timeStamp) {
	final Calendar cal = Calendar.getInstance();
	cal.setTime(timeStamp);
	return cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE);
    }
}
