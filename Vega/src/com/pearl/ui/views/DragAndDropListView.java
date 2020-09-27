package com.pearl.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

// TODO: Auto-generated Javadoc
/**
 * The Class DragAndDropListView.
 */
public class DragAndDropListView extends ListView {
    
    /** The m context. */
    private final Context mContext;
    
    /** The m drag view. */
    private ImageView mDragView;
    
    /** The m window manager. */
    private WindowManager mWindowManager;
    
    /** The m window params. */
    private WindowManager.LayoutParams mWindowParams;
    
    /** The m drag pos. */
    private int mDragPos;
    
    /** The m first drag pos. */
    private int mFirstDragPos;
    
    /** The m drag point. */
    private int mDragPoint;
    
    /** The m coord offset. */
    private int mCoordOffset;

    /** The m drag and drop listener. */
    private DragAndDropListener mDragAndDropListener;

    /** The m upper bound. */
    private int mUpperBound;
    
    /** The m lower bound. */
    private int mLowerBound;
    
    /** The m height. */
    private int mHeight;
    
    /** The m temp rect. */
    private final Rect mTempRect = new Rect();
    
    /** The m drag bitmap. */
    private Bitmap mDragBitmap;
    
    /** The m touch slop. */
    private final int mTouchSlop;
    
    /** The m item height normal. */
    private int mItemHeightNormal;
    
    /** The m item height expanded. */
    private int mItemHeightExpanded;
    
    /** The dnd view id. */
    private int dndViewId;
    
    /** The drag image x. */
    private int dragImageX = 0;
    
    /** The totalchilds. */
    private int totalchilds = 0;

    /**
     * Instantiates a new drag and drop list view.
     *
     * @param context the context
     */
    public DragAndDropListView(Context context) {
	super(context);
	mContext = context;
	mTouchSlop = ViewConfiguration.get(context).getScaledWindowTouchSlop();// etScaledTouchSlop();
	totalchilds = getChildCount();
    }

    /**
     * Instantiates a new drag and drop list view.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public DragAndDropListView(Context context, AttributeSet attrs) {
	super(context, attrs);
	mContext = context;
	mTouchSlop = ViewConfiguration.get(context).getScaledWindowTouchSlop();// etScaledTouchSlop();
	totalchilds = getChildCount();
    }

    /* (non-Javadoc)
     * @see android.widget.AbsListView#onInterceptTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
	if (getChildCount() > 0)
	    totalchilds = getChildCount();

	if (mDragAndDropListener != null) {
	    switch (ev.getAction()) {
	    case MotionEvent.ACTION_DOWN:
		final int x = (int) ev.getX();
		final int y = (int) ev.getY();

		if (x < this.getWidth() - 50) {
		    return false;
		}

		final int itemnum = pointToPosition(x, y);
		System.out.println("Item num :" + itemnum);
		if (itemnum == AdapterView.INVALID_POSITION) {
		    break;
		}
		final View item = getChildAt(itemnum - getFirstVisiblePosition());
		// item.setBackgroundColor(Color.RED);
		mItemHeightNormal = item.getHeight();
		mItemHeightExpanded = mItemHeightNormal * 1;
		mDragPoint = y - item.getTop();
		mCoordOffset = (int) ev.getRawY() - y;
		View dragger = item.findViewById(dndViewId);
		if (dragger == null)
		    dragger = item;

		final Rect r = mTempRect;
		dragger.getDrawingRect(r);

		if (x < r.right * 2) {
		    item.setDrawingCacheEnabled(true);

		    final Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
		    startDragging(bitmap, y);
		    mDragPos = itemnum;
		    mFirstDragPos = mDragPos;
		    mHeight = getHeight();

		    final int touchSlop = mTouchSlop;
		    mUpperBound = Math.min(y - touchSlop, mHeight / 3);
		    mLowerBound = Math.max(y + touchSlop, mHeight * 2 / 3);
		    return false;
		}

		mDragView = null;
		break;
	    }
	}
	return super.onInterceptTouchEvent(ev);
    }

    /**
     * Redraw.
     *
     * @param pos the pos
     */
    public void redraw(int pos) {
	for (int i = 0; i < getChildCount(); i++) {
	    final View v = getChildAt(i);
	    if (i == pos)
		v.setBackgroundColor(Color.parseColor("#E0E0E0"));
	    else
		v.setBackgroundColor(Color.WHITE);

	}
    }

    /**
     * Refresh.
     */
    public void refresh() {
	final long time = Long.parseLong(("" + System.nanoTime()).substring(0, 7));
	final MotionEvent me = MotionEvent.obtain(time, time,
		MotionEvent.ACTION_DOWN, getWidth() - 10, 300, 0);
	onInterceptTouchEvent(me);
	stopDragging();
    }

    /* (non-Javadoc)
     * @see android.widget.ListView#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

	if (getChildCount() > 0)
	    totalchilds = getChildCount();

	if (mDragAndDropListener != null && mDragView != null) {
	    final int action = ev.getAction();
	    switch (action) {
	    case MotionEvent.ACTION_UP:
	    case MotionEvent.ACTION_CANCEL:
		final Rect r = mTempRect;
		mDragView.getDrawingRect(r);
		stopDragging();
		int y = (int) ev.getY();
		int speed = 0;
		if (mDragPos >= 0 && mDragPos < getCount()) {
		    mDragAndDropListener.drop(mFirstDragPos, mDragPos);

		    if (mDragPos < totalchilds - 1)
			setSelectionFromTop(0, 0);
		}
		unExpandViews(false);
		break;

	    case MotionEvent.ACTION_DOWN:
	    case MotionEvent.ACTION_MOVE:
		final int x = (int) ev.getX();
		y = (int) ev.getY();

		if (x < this.getWidth() - 50) {
		    return false;
		}

		dragView(x, y);
		final int itemnum = getItemForPosition(y);
		if (itemnum >= 0) {
		    if (action == MotionEvent.ACTION_DOWN
			    || itemnum != mDragPos) {
			mDragAndDropListener.drag(mDragPos, itemnum);
			mDragPos = itemnum;
			doExpansion();
		    }
		    speed = 0;
		    adjustScrollBounds(y);
		    if (y > mLowerBound) {

			speed = y > (mHeight + mLowerBound) / 2 ? 16 : 4;
		    } else if (y < mUpperBound) {

			speed = y < mUpperBound / 2 ? -16 : -4;
		    }
		    if (speed != 0) {
			int ref = pointToPosition(0, mHeight / 2);
			if (ref == AdapterView.INVALID_POSITION) {
			    // we hit a divider or an invisible view, check
			    // somewhere else
			    ref = pointToPosition(0, mHeight / 2
				    + getDividerHeight() + 64);
			}
			final View v = getChildAt(ref - getFirstVisiblePosition());
			if (v != null) {
			    final int pos = v.getTop();
			    setSelectionFromTop(ref, pos - speed);
			}
		    }
		}
		break;
	    }
	    return true;
	}
	return super.onTouchEvent(ev);
    }

    /**
     * Gets the item for position.
     *
     * @param y the y
     * @return the item for position
     */
    private int getItemForPosition(int y) {
	final int adjustedy = y - mDragPoint - 32;
	int pos = myPointToPosition(0, adjustedy);
	if (pos >= 0) {
	    if (pos <= mFirstDragPos) {
		pos += 1;
	    }
	} else if (adjustedy < 0) {
	    pos = 0;
	}
	return pos;
    }

    /**
     * My point to position.
     *
     * @param x the x
     * @param y the y
     * @return the int
     */
    private int myPointToPosition(int x, int y) {
	final Rect frame = mTempRect;
	final int count = getChildCount();
	for (int i = count - 1; i >= 0; i--) {
	    final View child = getChildAt(i);
	    child.getHitRect(frame);
	    if (frame.contains(x, y)) {
		return getFirstVisiblePosition() + i;
	    }
	}
	return INVALID_POSITION;
    }

    /**
     * Adjust scroll bounds.
     *
     * @param y the y
     */
    private void adjustScrollBounds(int y) {
	if (y >= mHeight / 3) {
	    mUpperBound = mHeight / 3;
	}
	if (y <= mHeight * 2 / 3) {
	    mLowerBound = mHeight * 2 / 3;
	}
    }

    /**
     * Do expansion.
     */
    private void doExpansion() {
	int childnum = mDragPos - getFirstVisiblePosition();
	if (mDragPos > mFirstDragPos) {
	    childnum++;
	}

	final View first = getChildAt(mFirstDragPos - getFirstVisiblePosition());
	for (int i = 0;; i++) {
	    final View vv = getChildAt(i);
	    if (vv == null) {
		break;
	    }
	    int height = mItemHeightNormal;
	    int visibility = View.VISIBLE;
	    if (vv.equals(first)) {

		if (mDragPos == mFirstDragPos) {
		    visibility = View.INVISIBLE;
		} else {
		    height = 1;
		}
	    } else if (i == childnum) {
		System.out.print("I :" + i);
		if (mDragPos < getCount() - 1) {
		    height = mItemHeightExpanded;
		}
	    }
	    final ViewGroup.LayoutParams params = vv.getLayoutParams();
	    params.height = height;
	    vv.setLayoutParams(params);
	    vv.setVisibility(visibility);
	}
    }

    /**
     * Un expand views.
     *
     * @param deletion the deletion
     */
    private void unExpandViews(boolean deletion) {
	for (int i = 0;; i++) {
	    View v = getChildAt(i);
	    if (v == null) {
		if (deletion) {
		    final int position = getFirstVisiblePosition();
		    final int y = getChildAt(0).getTop();
		    setAdapter(getAdapter());
		    setSelectionFromTop(position, y);
		}
		layoutChildren();
		v = getChildAt(i);
		if (v == null) {
		    break;
		}
	    }
	    final ViewGroup.LayoutParams params = v.getLayoutParams();
	    params.height = mItemHeightNormal;
	    v.setLayoutParams(params);
	    v.setVisibility(View.VISIBLE);
	}
    }

    /**
     * Checkfordrop.
     *
     * @param dragPos the drag pos
     */
    public void checkfordrop(int dragPos) {
	if (dragPos < totalchilds - 1)
	    setSelectionFromTop(0, 0);
    }

    /**
     * Start dragging.
     *
     * @param bm the bm
     * @param y the y
     */
    private void startDragging(Bitmap bm, int y) {
	stopDragging();

	mWindowParams = new WindowManager.LayoutParams();
	mWindowParams.gravity = Gravity.TOP;
	mWindowParams.x = dragImageX;
	mWindowParams.y = y - mDragPoint + mCoordOffset;

	mWindowParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
	mWindowParams.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
	mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
		| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
		| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
	mWindowParams.format = PixelFormat.TRANSLUCENT;
	mWindowParams.windowAnimations = 0;

	final ImageView v = new ImageView(mContext);
	Color.parseColor("#e0103010");
	// XXX v.setBackgroundColor(backGroundColor);
	v.setImageBitmap(bm);
	mDragBitmap = bm;

	mWindowManager = (WindowManager) mContext.getSystemService("window");
	mWindowManager.addView(v, mWindowParams);
	mDragView = v;
    }

    /**
     * Drag view.
     *
     * @param x the x
     * @param y the y
     */
    private void dragView(int x, int y) {
	mWindowParams.y = y - mDragPoint + mCoordOffset;
	mWindowManager.updateViewLayout(mDragView, mWindowParams);
    }

    /**
     * Stop dragging.
     */
    private void stopDragging() {
	if (mDragView != null) {
	    final WindowManager wm = (WindowManager) mContext
		    .getSystemService("window");
	    wm.removeView(mDragView);
	    mDragView.setImageDrawable(null);
	    mDragView = null;
	}
	if (mDragBitmap != null) {
	    mDragBitmap.recycle();
	    mDragBitmap = null;
	}
    }

    /**
     * Sets the drag and drop listener.
     *
     * @param l the new drag and drop listener
     */
    public void setDragAndDropListener(DragAndDropListener l) {
	mDragAndDropListener = l;
    }

    /**
     * Sets the dnd view.
     *
     * @param id the new dnd view
     */
    public void setDndView(int id) {
	dndViewId = id;
    }

    /**
     * Sets the drag image x.
     *
     * @param x the new drag image x
     */
    public void setDragImageX(int x) {
	dragImageX = x;
    }

    /**
     * The listener interface for receiving drag events.
     * The class that is interested in processing a drag
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addDragListener<code> method. When
     * the drag event occurs, that object's appropriate
     * method is invoked.
     *
     * @see DragEvent
     */
    public interface DragListener {
	
	/**
	 * Drag.
	 *
	 * @param from the from
	 * @param to the to
	 */
	void drag(int from, int to);
    }

    /**
     * The listener interface for receiving drop events.
     * The class that is interested in processing a drop
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addDropListener<code> method. When
     * the drop event occurs, that object's appropriate
     * method is invoked.
     *
     * @see DropEvent
     */
    public interface DropListener {
	
	/**
	 * Drop.
	 *
	 * @param from the from
	 * @param to the to
	 */
	void drop(int from, int to);
    }

    /**
     * The listener interface for receiving dragAndDrop events.
     * The class that is interested in processing a dragAndDrop
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addDragAndDropListener<code> method. When
     * the dragAndDrop event occurs, that object's appropriate
     * method is invoked.
     *
     * @see DragAndDropEvent
     */
    public interface DragAndDropListener {
	
	/**
	 * Drag.
	 *
	 * @param from the from
	 * @param to the to
	 */
	void drag(int from, int to);

	/**
	 * Drop.
	 *
	 * @param from the from
	 * @param to the to
	 */
	void drop(int from, int to);
    }
}