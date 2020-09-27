/*
 * Copyright (C) 2008 The Android Open Source Project, Romain Guy
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

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

// TODO: Auto-generated Javadoc
/**
 * The Class LayerDrawable.
 */
class LayerDrawable extends Drawable implements Drawable.Callback {
    
    /** The m layer state. */
    LayerState mLayerState;

    /** The m padding l. */
    private int[] mPaddingL;
    
    /** The m padding t. */
    private int[] mPaddingT;
    
    /** The m padding r. */
    private int[] mPaddingR;
    
    /** The m padding b. */
    private int[] mPaddingB;

    /** The m tmp rect. */
    private final Rect mTmpRect = new Rect();
    
    /** The m parent. */
    private Drawable mParent;
    
    /** The m block set bounds. */
    private boolean mBlockSetBounds;

    /**
     * Instantiates a new layer drawable.
     *
     * @param layers the layers
     */
    LayerDrawable(Drawable... layers) {
	this(null, layers);
    }

    /**
     * Instantiates a new layer drawable.
     *
     * @param state the state
     * @param layers the layers
     */
    LayerDrawable(LayerState state, Drawable... layers) {
	this(state);
	final int length = layers.length;
	final Rec[] r = new Rec[length];

	final LayerState layerState = mLayerState;
	for (int i = 0; i < length; i++) {
	    r[i] = new Rec();
	    r[i].mDrawable = layers[i];
	    layers[i].setCallback(this);
	    layerState.mChildrenChangingConfigurations |= layers[i]
		    .getChangingConfigurations();
	}
	layerState.mNum = length;
	layerState.mArray = r;

	ensurePadding();
    }

    /**
     * Instantiates a new layer drawable.
     *
     * @param state the state
     */
    LayerDrawable(LayerState state) {
	final LayerState as = createConstantState(state);
	mLayerState = as;
	if (as.mNum > 0) {
	    ensurePadding();
	}
    }

    /**
     * Creates the constant state.
     *
     * @param state the state
     * @return the layer state
     */
    LayerState createConstantState(LayerState state) {
	return new LayerState(state, this);
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable.Callback#invalidateDrawable(android.graphics.drawable.Drawable)
     */
    @Override
    public void invalidateDrawable(Drawable who) {
	invalidateSelf();
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable.Callback#scheduleDrawable(android.graphics.drawable.Drawable, java.lang.Runnable, long)
     */
    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
	scheduleSelf(what, when);
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable.Callback#unscheduleDrawable(android.graphics.drawable.Drawable, java.lang.Runnable)
     */
    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
	unscheduleSelf(what);
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#draw(android.graphics.Canvas)
     */
    @Override
    public void draw(Canvas canvas) {
	final Rec[] array = mLayerState.mArray;
	final int N = mLayerState.mNum;
	for (int i = 0; i < N; i++) {
	    array[i].mDrawable.draw(canvas);
	}
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#getChangingConfigurations()
     */
    @Override
    public int getChangingConfigurations() {
	return super.getChangingConfigurations()
		| mLayerState.mChangingConfigurations
		| mLayerState.mChildrenChangingConfigurations;
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#getPadding(android.graphics.Rect)
     */
    @Override
    public boolean getPadding(Rect padding) {
	padding.left = 0;
	padding.top = 0;
	padding.right = 0;
	padding.bottom = 0;
	final Rec[] array = mLayerState.mArray;
	final int N = mLayerState.mNum;
	for (int i = 0; i < N; i++) {
	    reapplyPadding(i, array[i]);
	    padding.left = Math.max(padding.left, mPaddingL[i]);
	    padding.top = Math.max(padding.top, mPaddingT[i]);
	    padding.right = Math.max(padding.right, mPaddingR[i]);
	    padding.bottom = Math.max(padding.bottom, mPaddingB[i]);
	}
	return true;
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#setVisible(boolean, boolean)
     */
    @Override
    public boolean setVisible(boolean visible, boolean restart) {
	final boolean changed = super.setVisible(visible, restart);
	final Rec[] array = mLayerState.mArray;
	final int N = mLayerState.mNum;
	for (int i = 0; i < N; i++) {
	    array[i].mDrawable.setVisible(visible, restart);
	}
	return changed;
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#setDither(boolean)
     */
    @Override
    public void setDither(boolean dither) {
	final Rec[] array = mLayerState.mArray;
	final int N = mLayerState.mNum;
	for (int i = 0; i < N; i++) {
	    array[i].mDrawable.setDither(dither);
	}
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#setAlpha(int)
     */
    @Override
    public void setAlpha(int alpha) {
	final Rec[] array = mLayerState.mArray;
	final int N = mLayerState.mNum;
	for (int i = 0; i < N; i++) {
	    array[i].mDrawable.setAlpha(alpha);
	}
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#setColorFilter(android.graphics.ColorFilter)
     */
    @Override
    public void setColorFilter(ColorFilter cf) {
	final Rec[] array = mLayerState.mArray;
	final int N = mLayerState.mNum;
	for (int i = 0; i < N; i++) {
	    array[i].mDrawable.setColorFilter(cf);
	}
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#getOpacity()
     */
    @Override
    public int getOpacity() {
	return mLayerState.getOpacity();
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#isStateful()
     */
    @Override
    public boolean isStateful() {
	return mLayerState.isStateful();
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#onStateChange(int[])
     */
    @Override
    protected boolean onStateChange(int[] state) {
	final Rec[] array = mLayerState.mArray;
	final int N = mLayerState.mNum;
	boolean paddingChanged = false;
	boolean changed = false;
	for (int i = 0; i < N; i++) {
	    final Rec r = array[i];
	    if (r.mDrawable.setState(state)) {
		changed = true;
	    }
	    if (reapplyPadding(i, r)) {
		paddingChanged = true;
	    }
	}
	if (paddingChanged) {
	    onBoundsChange(getBounds());
	}
	return changed;
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#onLevelChange(int)
     */
    @Override
    protected boolean onLevelChange(int level) {
	final Rec[] array = mLayerState.mArray;
	final int N = mLayerState.mNum;
	boolean paddingChanged = false;
	boolean changed = false;
	for (int i = 0; i < N; i++) {
	    final Rec r = array[i];
	    if (r.mDrawable.setLevel(level)) {
		changed = true;
	    }
	    if (reapplyPadding(i, r)) {
		paddingChanged = true;
	    }
	}
	if (paddingChanged) {
	    onBoundsChange(getBounds());
	}
	return changed;
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#setBounds(int, int, int, int)
     */
    @Override
    public void setBounds(int left, int top, int right, int bottom) {
	if (mBlockSetBounds)
	    return;

	final int width = mLayerState.mArray[0].mDrawable.getIntrinsicWidth();
	left -= (width - (right - left)) / 2.0f;
	right = left + width;
	bottom = top + getIntrinsicHeight();
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
     * @see android.graphics.drawable.Drawable#onBoundsChange(android.graphics.Rect)
     */
    @Override
    protected void onBoundsChange(Rect bounds) {
	final Rec[] array = mLayerState.mArray;
	final int N = mLayerState.mNum;
	int padL = 0, padT = 0, padR = 0, padB = 0;
	for (int i = 0; i < N; i++) {
	    final Rec r = array[i];
	    r.mDrawable.setBounds(bounds.left + r.mInsetL + padL, bounds.top
		    + r.mInsetT + padT, bounds.right - r.mInsetR - padR,
		    bounds.bottom - r.mInsetB - padB);
	    padL += mPaddingL[i];
	    padR += mPaddingR[i];
	    padT += mPaddingT[i];
	    padB += mPaddingB[i];
	}
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#getIntrinsicWidth()
     */
    @Override
    public int getIntrinsicWidth() {
	int width = -1;
	final Rec[] array = mLayerState.mArray;
	final int N = mLayerState.mNum;
	int padL = 0, padR = 0;
	for (int i = 0; i < N; i++) {
	    final Rec r = array[i];
	    final int w = r.mDrawable.getIntrinsicWidth() + r.mInsetL + r.mInsetR
		    + padL + padR;
	    if (w > width) {
		width = w;
	    }
	    padL += mPaddingL[i];
	    padR += mPaddingR[i];
	}
	return width;
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#getIntrinsicHeight()
     */
    @Override
    public int getIntrinsicHeight() {
	int height = -1;
	final Rec[] array = mLayerState.mArray;
	final int N = mLayerState.mNum;
	int padT = 0, padB = 0;
	for (int i = 0; i < N; i++) {
	    final Rec r = array[i];
	    final int h = r.mDrawable.getIntrinsicHeight() + r.mInsetT + r.mInsetB
		    + +padT + padB;
	    if (h > height) {
		height = h;
	    }
	    padT += mPaddingT[i];
	    padB += mPaddingB[i];
	}
	return height;
    }

    /**
     * Reapply padding.
     *
     * @param i the i
     * @param r the r
     * @return true, if successful
     */
    private boolean reapplyPadding(int i, Rec r) {
	final Rect rect = mTmpRect;
	r.mDrawable.getPadding(rect);
	if (rect.left != mPaddingL[i] || rect.top != mPaddingT[i]
		|| rect.right != mPaddingR[i] || rect.bottom != mPaddingB[i]) {
	    mPaddingL[i] = rect.left;
	    mPaddingT[i] = rect.top;
	    mPaddingR[i] = rect.right;
	    mPaddingB[i] = rect.bottom;
	    return true;
	}
	return false;
    }

    /**
     * Ensure padding.
     */
    private void ensurePadding() {
	final int N = mLayerState.mNum;
	if (mPaddingL != null && mPaddingL.length >= N) {
	    return;
	}
	mPaddingL = new int[N];
	mPaddingT = new int[N];
	mPaddingR = new int[N];
	mPaddingB = new int[N];
    }

    /* (non-Javadoc)
     * @see android.graphics.drawable.Drawable#getConstantState()
     */
    @Override
    public ConstantState getConstantState() {
	if (mLayerState.canConstantState()) {
	    mLayerState.mChangingConfigurations = super
		    .getChangingConfigurations();
	    return mLayerState;
	}
	return null;
    }

    /**
     * The Class Rec.
     */
    static class Rec {
	
	/** The m drawable. */
	public Drawable mDrawable;
	
	/** The m inset b. */
	public int mInsetL, mInsetT, mInsetR, mInsetB;
	
	/** The m id. */
	public int mId;
    }

    /**
     * The Class LayerState.
     */
    static class LayerState extends ConstantState {
	
	/** The m num. */
	int mNum;
	
	/** The m array. */
	Rec[] mArray;

	/** The m changing configurations. */
	int mChangingConfigurations;
	
	/** The m children changing configurations. */
	int mChildrenChangingConfigurations;

	/** The m have opacity. */
	private boolean mHaveOpacity = false;
	
	/** The m opacity. */
	private int mOpacity;

	/** The m have stateful. */
	private boolean mHaveStateful = false;
	
	/** The m stateful. */
	private boolean mStateful;

	/** The m checked constant state. */
	private boolean mCheckedConstantState;
	
	/** The m can constant state. */
	private boolean mCanConstantState;

	/**
	 * Instantiates a new layer state.
	 *
	 * @param orig the orig
	 * @param owner the owner
	 */
	LayerState(LayerState orig, LayerDrawable owner) {
	    if (orig != null) {
		final Rec[] origRec = orig.mArray;
		final int N = orig.mNum;

		mNum = N;
		mArray = new Rec[N];

		mChangingConfigurations = orig.mChangingConfigurations;
		mChildrenChangingConfigurations = orig.mChildrenChangingConfigurations;

		for (int i = 0; i < N; i++) {
		    final Rec r = mArray[i] = new Rec();
		    final Rec or = origRec[i];
		    r.mDrawable = or.mDrawable.getConstantState().newDrawable();
		    r.mDrawable.setCallback(owner);
		    r.mInsetL = or.mInsetL;
		    r.mInsetT = or.mInsetT;
		    r.mInsetR = or.mInsetR;
		    r.mInsetB = or.mInsetB;
		    r.mId = or.mId;
		}

		mHaveOpacity = orig.mHaveOpacity;
		mOpacity = orig.mOpacity;
		mHaveStateful = orig.mHaveStateful;
		mStateful = orig.mStateful;
		mCheckedConstantState = mCanConstantState = true;
	    } else {
		mNum = 0;
		mArray = null;
	    }
	}

	/* (non-Javadoc)
	 * @see android.graphics.drawable.Drawable.ConstantState#newDrawable()
	 */
	@Override
	public Drawable newDrawable() {
	    return new LayerDrawable(this);
	}

	/* (non-Javadoc)
	 * @see android.graphics.drawable.Drawable.ConstantState#getChangingConfigurations()
	 */
	@Override
	public int getChangingConfigurations() {
	    return mChangingConfigurations;
	}

	/**
	 * Gets the opacity.
	 *
	 * @return the opacity
	 */
	public final int getOpacity() {
	    if (mHaveOpacity) {
		return mOpacity;
	    }

	    final int N = mNum;
	    final Rec[] array = mArray;
	    int op = N > 0 ? array[0].mDrawable.getOpacity()
		    : PixelFormat.TRANSPARENT;
	    for (int i = 1; i < N; i++) {
		op = Drawable.resolveOpacity(op,
			array[i].mDrawable.getOpacity());
	    }
	    mOpacity = op;
	    mHaveOpacity = true;
	    return op;
	}

	/**
	 * Checks if is stateful.
	 *
	 * @return true, if is stateful
	 */
	public final boolean isStateful() {
	    if (mHaveStateful) {
		return mStateful;
	    }

	    boolean stateful = false;
	    final int N = mNum;
	    final Rec[] array = mArray;
	    for (int i = 0; i < N; i++) {
		if (array[i].mDrawable.isStateful()) {
		    stateful = true;
		    break;
		}
	    }

	    mStateful = stateful;
	    mHaveStateful = true;
	    return stateful;
	}

	/**
	 * Can constant state.
	 *
	 * @return true, if successful
	 */
	public synchronized boolean canConstantState() {
	    final Rec[] array = mArray;
	    if (!mCheckedConstantState && array != null) {
		mCanConstantState = true;
		final int N = mNum;
		for (int i = 0; i < N; i++) {
		    if (array[i].mDrawable.getConstantState() == null) {
			mCanConstantState = false;
			break;
		    }
		}
		mCheckedConstantState = true;
	    }

	    return mCanConstantState;
	}
    }
}
