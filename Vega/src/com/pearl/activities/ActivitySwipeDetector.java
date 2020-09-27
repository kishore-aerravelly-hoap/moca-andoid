package com.pearl.activities;

import android.view.MotionEvent;
import android.view.View;

import com.pearl.book.guesturehandlers.GestureActionHandler;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ActivitySwipeDetector.
 */
public class ActivitySwipeDetector implements View.OnTouchListener {
    
    /** The Constant TAG. */
    static final String TAG = "ActivitySwipeDetector";
    
    /** The gesture handler. */
    private final GestureActionHandler gestureHandler;
    
    /** The Constant MIN_Y_DISTANCE. */
    static final int MIN_Y_DISTANCE = 100;
    
    /** The Constant MIN_X_DISTANCE. */
    static final int MIN_X_DISTANCE = 5;
    
    /** The up y. */
    private float downX, downY, upX, upY;
    
    /** The page no. */
    int pageNo;

    /**
     * Instantiates a new activity swipe detector.
     *
     * @param gs the gs
     */
    public ActivitySwipeDetector(GestureActionHandler gs) {
	gestureHandler = gs;
    }

    /**
     * Called when the movement is from Right to Left.
     */
    public void onRightToLeftSwipe() {
	Logger.warn(TAG, "Gesture - RightToLeftSwipe!");
	// Toast.makeText((Context)
	// gestureHandler,"Moving to the next page-->right to Left",
	// Toast.LENGTH_SHORT).show();
	Logger.info("Gesture",
		"Gesture - Moving to the next page-->right to Left");
	gestureHandler.moveToNextPage();
	gestureHandler.toggleBookmarksNotesListVisibility();
    }

    /**
     * Called when the movement is from Left to Right.
     */
    public void onLeftToRightSwipe() {
	Logger.warn(TAG, "Gesture - LeftToRightSwipe!");
	// Toast.makeText((Context) gestureHandler,
	// "moving to the previous page-->Left to right",
	// Toast.LENGTH_SHORT).show();
	gestureHandler.moveToPreviousPage();
	gestureHandler.toggleBookmarksNotesListVisibility();
	gestureHandler.toggleTableOfcontentsListVisibility();
    }

    /**
     * Called when the movement is from Top to Bottom.
     */
    public void onTopToBottomSwipe() {
	// Toast.makeText((Context) gestureHandler, "top to bottom",
	// Toast.LENGTH_SHORT).show();
	Logger.warn(TAG, "Gesture - onTopToBottomSwipe!");
	gestureHandler.scroll();
	gestureHandler.toggleBookmarksNotesListVisibility();
	gestureHandler.toggleTableOfcontentsListVisibility();
    }

    /**
     * Called when the movement is from Bottom to Top.
     */
    public void onBottomToTopSwipe() {
	// Toast.makeText((Context) gestureHandler, "bottom to top",
	// Toast.LENGTH_SHORT).show();
	Logger.warn(TAG, "Gesture - onBottomToTopSwipe!");
	gestureHandler.scroll();
	gestureHandler.toggleBookmarksNotesListVisibility();
	gestureHandler.toggleTableOfcontentsListVisibility();
    }

    /**
     * Called when touched on the screen.
     *
     * @param v the v
     * @param event the event
     * @return boolean indicating the motion
     */

    @Override
    public boolean onTouch(View v, MotionEvent event) {
	boolean isTouch = true;
	Logger.info(TAG, "event.getAction() is:" + event.getAction());
	switch (event.getAction()) {
	case MotionEvent.ACTION_DOWN: {
	    downX = event.getX();
	    downY = event.getY();
	    // onLongClick(v);
	    // Toast.makeText((Context) gestureHandler,
	    // "Action_Down event, downX:"+downX+ "downY:"+downY+
	    // "event is:"+event.toString(), Toast.LENGTH_SHORT).show();
	    return true;
	}

	case MotionEvent.ACTION_UP: {
	    upX = event.getX();
	    upY = event.getY();
	    final float deltaX = downX - upX;
	    final float deltaY = downY - upY;

	    // Toast.makeText((Context) gestureHandler, "deltaX is:"+deltaX,
	    // Toast.LENGTH_SHORT).show();
	    // Toast.makeText((Context) gestureHandler, "deltaY is:"+deltaY,
	    // Toast.LENGTH_SHORT).show();

	    // swipe vertical?
	    if (Math.abs(deltaY) > MIN_Y_DISTANCE) {
		// Toast.makeText((Context) gestureHandler,
		// "Swipe vertical, deltaY:"+deltaY, Toast.LENGTH_SHORT).show();
		// top or down
		isTouch = false;
		Logger.info(TAG, "Gesture - delta y condition Swipe done");
		if (deltaY < 0) {
		    this.onTopToBottomSwipe();
		    return true;
		}
		if (deltaY > 0) {
		    this.onBottomToTopSwipe();
		    return true;
		}
	    } else {
		// Toast.makeText((Context) gestureHandler,
		// "Else Swipe was only " + Math.abs(deltaX) +
		// " long, need at least " + MIN_DISTANCE,
		// Toast.LENGTH_SHORT).show();
		// assuming it to be a touch for getting header / footer layout

		Logger.info(TAG, "Gesture - delta y condition Swipe was only "
			+ Math.abs(deltaX) + " long, need at least "
			+ MIN_Y_DISTANCE);
	    }

	    // swipe horizontal?
	    if (Math.abs(deltaX) > MIN_X_DISTANCE) {
		// Toast.makeText((Context) gestureHandler,
		// "Swipe horizontal, Math.abs(deltaX):"+Math.abs(deltaX),
		// Toast.LENGTH_SHORT).show();
		// left or right
		isTouch = false;
		Logger.info(TAG, "Gesture - delta x condition Swipe done "
			+ Math.abs(deltaX));
		if (deltaX < 0) {
		    this.onLeftToRightSwipe();
		    return true;
		}
		if (deltaX > 0) {
		    this.onRightToLeftSwipe();
		    return true;
		}
	    } else {
		// Toast.makeText(gestureHandler, "Else Swipe was only " +
		// Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE,
		// Toast.LENGTH_SHORT).show();
		// assuming it to be a touch for getting header / footer layout
		Logger.info(TAG, "Gesture - delta x condition Swipe was only "
			+ Math.abs(deltaX) + " long, need at least "
			+ MIN_X_DISTANCE);
	    }

	    if (isTouch) {
		final float x = event.getX();
		Logger.warn(TAG, "x value is:" + x);
		Logger.info(TAG, "touch is:" + isTouch);
		gestureHandler.toggleHeaderFooterVisibility();
		gestureHandler.toggleBookmarksNotesListVisibility();
		gestureHandler.onJustTouch();
	    }
	    return true;
	}
	}
	return false;
    }


}