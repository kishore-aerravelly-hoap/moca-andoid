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

package com.pearl.shelves.drawable;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.pearl.R;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class SpotlightDrawable.
 */
public class SpotlightDrawable extends Drawable {
    
    /** The m bitmap. */
    private final Bitmap mBitmap;
    
    /** The m paint. */
    private final Paint mPaint;
    
    /** The m view. */
    private final ViewGroup mView;

    /** The m offset disabled. */
    private boolean mOffsetDisabled;
    
    /** The m block set bounds. */
    private boolean mBlockSetBounds;
    
    /** The m parent. */
    private Drawable mParent;

    /**
     * Instantiates a new spotlight drawable.
     *
     * @param context the context
     * @param view the view
     */
    public SpotlightDrawable(Context context, ViewGroup view) {
	this(context, view, R.drawable.spotlight);
	Logger.error("SpotlightDrawable", "constructor");
    }

    /**
     * Instantiates a new spotlight drawable.
     *
     * @param context the context
     * @param view the view
     * @param resource the resource
     */
    public SpotlightDrawable(Context context, ViewGroup view, int resource) {
	Logger.error("SpotlightDrawable", "constructor1");
	mView = view;
	mBitmap = BitmapFactory
		.decodeResource(context.getResources(), resource);

	mPaint = new Paint();
	mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#draw(android.graphics.Canvas)
     */
    @Override
    public void draw(Canvas canvas) {
	Logger.error("SpotlightDrawable", "draw");
	if (mView.hasWindowFocus()) {
	    final Rect bounds = getBounds();
	    canvas.save();
	    canvas.drawBitmap(mBitmap, bounds.left, bounds.top, mPaint);
	}
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#setBounds(int, int, int, int)
     */
    @Override
    public void setBounds(int left, int top, int right, int bottom) {
	Log.e("SpotlightDrawable", "setBounds");
	if (mBlockSetBounds)
	    return;

	if (!mOffsetDisabled) {
	    final int width = getIntrinsicWidth();
	    final View view = mView.getChildAt(0);
	    if (view != null)
		left -= (width - view.getWidth()) / 2.0f;
	    right = left + width;
	    bottom = top + getIntrinsicHeight();
	} else {
	    right = left + getIntrinsicWidth();
	    bottom = top + getIntrinsicHeight();
	}

	super.setBounds(left, top, right, bottom);

	if (mParent != null) {
	    mBlockSetBounds = true;
	    mParent.setBounds(left, top, right, bottom);
	    mBlockSetBounds = false;
	}
    }

    /**
     * Sets the parent.
     *
     * @param drawable the new parent
     */
    public void setParent(Drawable drawable) {
	mParent = drawable;
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#onStateChange(int[])
     */
    @Override
    protected boolean onStateChange(int[] state) {
	invalidateSelf();
	return super.onStateChange(state);
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#setAlpha(int)
     */
    @Override
    public void setAlpha(int alpha) {
	mPaint.setAlpha(alpha);
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#setColorFilter(android.graphics.ColorFilter)
     */
    @Override
    public void setColorFilter(ColorFilter cf) {
	mPaint.setColorFilter(cf);
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#getOpacity()
     */
    @Override
    public int getOpacity() {
	return PixelFormat.TRANSLUCENT;
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#getIntrinsicWidth()
     */
    @Override
    public int getIntrinsicWidth() {
	return mBitmap.getWidth();
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#getIntrinsicHeight()
     */
    @Override
    public int getIntrinsicHeight() {
	return mBitmap.getHeight();
    }

    /**
     * Disable offset.
     */
    public void disableOffset() {
	mOffsetDisabled = true;
    }
}
