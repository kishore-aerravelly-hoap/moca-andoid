/*
 * Copyright (C) 2008 Romain Guy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.pearl.shelves.view;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewConfiguration;
import android.widget.GridView;

import com.pearl.R;
import com.pearl.logger.Logger;
import com.pearl.shelves.drawable.SpotlightDrawable;
import com.pearl.shelves.drawable.TransitionDrawable;

// TODO: Auto-generated Javadoc
/**
 * The Class ShelvesView.
 */
public class ShelvesView extends GridView {
    
    /** The m shelf background. */
    private Bitmap mShelfBackground;
    
    /** The m shelf width. */
    private int mShelfWidth;
    
    /** The m shelf height. */
    private int mShelfHeight;

    /*
     * private Bitmap mWebLeft; private Bitmap mWebRight; private int
     * mWebRightWidth;
     */

    /**
     * Instantiates a new shelves view.
     *
     * @param context the context
     */
    public ShelvesView(Context context) {
	super(context);
	Logger.warn("ShelvesView", "constructor");
	init(context);
    }

    /**
     * Instantiates a new shelves view.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public ShelvesView(Context context, AttributeSet attrs) {
	super(context, attrs);
	Logger.warn("ShelvesView", "constructor1");
	load(context, attrs, 0);
	init(context);
    }

    /**
     * Instantiates a new shelves view.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    public ShelvesView(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);
	Logger.warn("ShelvesView", "constructor2");
	load(context, attrs, defStyle);
	init(context);
    }

    /**
     * Load.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    private void load(Context context, AttributeSet attrs, int defStyle) {
	final TypedArray a = context.obtainStyledAttributes(attrs,
		R.styleable.ShelvesView, defStyle, 0);

	final Resources resources = getResources();
	final int background = a.getResourceId(
		R.styleable.ShelvesView_shelfBackground, 0);
	final Bitmap shelfBackground = BitmapFactory.decodeResource(resources,
		background);
	if (shelfBackground != null) {
	    mShelfWidth = shelfBackground.getWidth();
	    mShelfHeight = shelfBackground.getHeight();
	    mShelfBackground = shelfBackground;
	}

	// mWebLeft = BitmapFactory.decodeResource(resources,
	// R.drawable.web_left);

	// final Bitmap webRight = BitmapFactory.decodeResource(resources,
	// R.drawable.web_right);
	// mWebRightWidth = webRight.getWidth();
	// mWebRight = webRight;

	a.recycle();
    }

    /**
     * Inits the.
     *
     * @param context the context
     */
    private void init(Context context) {
	Logger.warn("ShelvesView", "init");
	final StateListDrawable drawable = new StateListDrawable();

	final SpotlightDrawable start = new SpotlightDrawable(context, this);
	start.disableOffset();
	final SpotlightDrawable end = new SpotlightDrawable(context, this,
		R.drawable.spotlight_blue);
	end.disableOffset();
	final TransitionDrawable transition = new TransitionDrawable(start, end);
	drawable.addState(new int[] { android.R.attr.state_pressed },
		transition);

	final SpotlightDrawable normal = new SpotlightDrawable(context, this);
	drawable.addState(new int[] {}, normal);

	normal.setParent(drawable);
	transition.setParent(drawable);

	setSelector(drawable);
	setDrawSelectorOnTop(false);
    }

    /* (non-Javadoc)
     * @see android.widget.AbsListView#dispatchDraw(android.graphics.Canvas)
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
	final int count = getChildCount();
	final int top = count > 0 ? getChildAt(0).getTop() : 0;
	final int shelfWidth = mShelfWidth;
	final int shelfHeight = mShelfHeight;
	final int width = getWidth();
	final int height = getHeight();
	final Bitmap background = mShelfBackground;

	for (int x = 0; x < width; x += shelfWidth) {
	    for (int y = top; y < height; y += shelfHeight) {
		canvas.drawBitmap(background, x, y, null);
	    }
	}

	/*
	 * if (count == 0) { canvas.drawBitmap(mWebLeft, 0.0f, top + 1, null);
	 * canvas.drawBitmap(mWebRight, width - mWebRightWidth, top +
	 * shelfHeight + 1, null); }
	 */

	super.dispatchDraw(canvas);
    }

    /* (non-Javadoc)
     * @see android.view.View#setPressed(boolean)
     */
    @Override
    public void setPressed(boolean pressed) {
	super.setPressed(pressed);
	Log.w("ShelvesView", "In set pressed function");
	final Drawable current = getSelector().getCurrent();
	if (current instanceof TransitionDrawable) {
	    if (pressed) {
		Log.w("ShelvesView", "In set pressed function - TRUE");
		((TransitionDrawable) current)
		.startTransition(ViewConfiguration
			.getLongPressTimeout());
	    } else {
		Log.w("ShelvesView", "In set pressed function - FALSE");
		((TransitionDrawable) current).resetTransition();
	    }
	}
    }
}
