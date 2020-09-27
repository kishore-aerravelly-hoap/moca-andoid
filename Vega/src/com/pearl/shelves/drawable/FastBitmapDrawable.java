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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The Class FastBitmapDrawable.
 */
public class FastBitmapDrawable extends Drawable {
    
    /** The m bitmap. */
    private final Bitmap mBitmap;

    /**
     * Instantiates a new fast bitmap drawable.
     *
     * @param b the b
     */
    public FastBitmapDrawable(Bitmap b) {
	mBitmap = b;
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#draw(android.graphics.Canvas)
     */
    @Override
    public void draw(Canvas canvas) {
	Log.v("FastBitmapDrawable", "draw");
	canvas.drawBitmap(mBitmap, 0.0f, 0.0f, null);
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#getOpacity()
     */
    @Override
    public int getOpacity() {
	return PixelFormat.TRANSLUCENT;
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#setAlpha(int)
     */
    @Override
    public void setAlpha(int alpha) {
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#setColorFilter(android.graphics.ColorFilter)
     */
    @Override
    public void setColorFilter(ColorFilter cf) {
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

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#getMinimumWidth()
     */
    @Override
    public int getMinimumWidth() {
	return mBitmap.getWidth();
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#getMinimumHeight()
     */
    @Override
    public int getMinimumHeight() {
	return mBitmap.getHeight();
    }

    /**
     * Gets the bitmap.
     *
     * @return the bitmap
     */
    public Bitmap getBitmap() {
	return mBitmap;
    }
}
