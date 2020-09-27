package com.pearl.Listeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.pearl.activities.ReadbookActivity;
import com.pearl.application.VegaConstants;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving pearlShelfOnClick events.
 * The class that is interested in processing a pearlShelfOnClick
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addPearlShelfOnClickListener<code> method. When
 * the pearlShelfOnClick event occurs, that object's appropriate
 * method is invoked.
 *
 * @see PearlShelfOnClickEvent
 */
public class PearlShelfOnClickListener implements OnClickListener {

    /** The context. */
    private final Context context;
    
    /** The book id. */
    private final int bookId;

    /**
     * Instantiates a new pearl shelf on click listener.
     *
     * @param context the context
     * @param bookId the book id
     */
    public PearlShelfOnClickListener(Context context, int bookId) {
	this.bookId = bookId;
	this.context = context;
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View arg0) {
	// Toast.makeText(context, "Book id on click is:"+bookId,
	// Toast.LENGTH_SHORT).show();
	final Intent i = new Intent();
	i.setClass(context, ReadbookActivity.class);
	i.putExtra(VegaConstants.BOOK_ID, bookId);
	context.startActivity(i);
    }

}
