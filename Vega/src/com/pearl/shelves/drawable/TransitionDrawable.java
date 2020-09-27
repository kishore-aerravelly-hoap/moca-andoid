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

/*
 * Copyright (C) 2008 The Android Open Source Project
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
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

// TODO: Auto-generated Javadoc
/**
 * The Class TransitionDrawable.
 */
public class TransitionDrawable extends LayerDrawable implements
Drawable.Callback {

    /**
     * A transition is about to start.
     */
    private static final int TRANSITION_STARTING = 0;

    /** The transition has started and the animation is in progress. */
    private static final int TRANSITION_RUNNING = 1;

    /** No transition will be applied. */
    private static final int TRANSITION_NONE = 2;

    /**
     * The current state of the transition. One of {@link #TRANSITION_STARTING},
     * {@link #TRANSITION_RUNNING} and {@link #TRANSITION_NONE}
     */
    private int mTransitionState = TRANSITION_NONE;

    /** The m reverse. */
    private boolean mReverse;
    
    /** The m start time millis. */
    private long mStartTimeMillis;
    
    /** The m from. */
    private int mFrom;
    
    /** The m to. */
    private int mTo;
    
    /** The m duration. */
    private int mDuration;
    
    /** The m state. */
    private final TransitionState mState;

    /**
     * Instantiates a new transition drawable.
     *
     * @param layers the layers
     */
    public TransitionDrawable(Drawable... layers) {
	this(new TransitionState(null, null), layers);
    }

    /**
     * Instantiates a new transition drawable.
     */
    TransitionDrawable() {
	this(new TransitionState(null, null));
    }

    /**
     * Instantiates a new transition drawable.
     *
     * @param state the state
     */
    private TransitionDrawable(TransitionState state) {
	super(state);
	mState = state;
    }

    /**
     * Instantiates a new transition drawable.
     *
     * @param state the state
     * @param layers the layers
     */
    private TransitionDrawable(TransitionState state, Drawable... layers) {
	super(state, layers);
	mState = state;
    }

    /* (non-Javadoc)
     * @see com.pearl.shelves.drawable.LayerDrawable#createConstantState(com.pearl.shelves.drawable.LayerDrawable.LayerState)
     */
    @Override
    LayerState createConstantState(LayerState state) {
	return new TransitionState((TransitionState) state, this);
    }

    /**
     * Begin the second layer on top of the first layer.
     * 
     * @param durationMillis
     *            The length of the transition in milliseconds
     */
    public void startTransition(int durationMillis) {
	mFrom = 0;
	mTo = 255;
	mState.mAlpha = 0;
	mState.mDuration = mDuration = durationMillis;
	mReverse = false;
	mTransitionState = TRANSITION_STARTING;
	invalidateSelf();
    }

    /**
     * Show only the first layer.
     */
    public void resetTransition() {
	mState.mAlpha = 0;
	mTransitionState = TRANSITION_NONE;
	invalidateSelf();
    }

    /**
     * Reverses the transition, picking up where the transition currently is. If
     * the transition is not currently running, this will start the transition
     * with the specified duration. If the transition is already running, the
     * last known duration will be used.
     * 
     * @param duration
     *            The duration to use if no transition is running.
     */
    public void reverseTransition(int duration) {
	final long time = SystemClock.uptimeMillis();
	// Animation is over
	if (time - mStartTimeMillis > mState.mDuration) {
	    if (mState.mAlpha == 0) {
		mFrom = 0;
		mTo = 255;
		mState.mAlpha = 0;
		mReverse = false;
	    } else {
		mFrom = 255;
		mTo = 0;
		mState.mAlpha = 255;
		mReverse = true;
	    }
	    mDuration = mState.mDuration = duration;
	    mTransitionState = TRANSITION_STARTING;
	    invalidateSelf();
	    return;
	}

	mReverse = !mReverse;
	mFrom = mState.mAlpha;
	mTo = mReverse ? 0 : 255;
	mDuration = (int) (mReverse ? time - mStartTimeMillis
		: mState.mDuration - (time - mStartTimeMillis));
	mTransitionState = TRANSITION_STARTING;
    }

    /* (non-Javadoc)
     * @see com.pearl.shelves.drawable.LayerDrawable#draw(android.graphics.Canvas)
     */
    @Override
    public void draw(Canvas canvas) {
	boolean done = true;
	final TransitionState state = mState;

	switch (mTransitionState) {
	case TRANSITION_STARTING:
	    mStartTimeMillis = SystemClock.uptimeMillis();
	    done = false;
	    mTransitionState = TRANSITION_RUNNING;
	    break;

	case TRANSITION_RUNNING:
	    if (mStartTimeMillis >= 0) {
		float normalized = (float) (SystemClock.uptimeMillis() - mStartTimeMillis)
			/ mDuration;
		done = normalized >= 1.0f;
		normalized = Math.min(normalized, 1.0f);
		state.mAlpha = (int) (mFrom + (mTo - mFrom) * normalized);
	    }
	    break;
	}

	final int alpha = state.mAlpha;
	final boolean crossFade = state.mCrossFade;
	final Rec[] array = mLayerState.mArray;
	Drawable d;

	d = array[0].mDrawable;
	if (crossFade) {
	    d.setAlpha(255 - alpha);
	}
	d.draw(canvas);
	if (crossFade) {
	    d.setAlpha(0xFF);
	}

	if (alpha > 0) {
	    d = array[1].mDrawable;
	    d.setAlpha(alpha);
	    d.draw(canvas);
	    d.setAlpha(0xFF);
	}

	if (!done) {
	    invalidateSelf();
	}
    }

    /**
     * Enables or disables the cross fade of the drawables. When cross fade is
     * disabled, the first drawable is always drawn opaque. With cross fade
     * enabled, the first drawable is drawn with the opposite alpha of the
     * second drawable.
     * 
     * @param enabled
     *            True to enable cross fading, false otherwise.
     */
    public void setCrossFadeEnabled(boolean enabled) {
	mState.mCrossFade = enabled;
    }

    /**
     * Indicates whether the cross fade is enabled for this transition.
     * 
     * @return True if cross fading is enabled, false otherwise.
     */
    public boolean isCrossFadeEnabled() {
	return mState.mCrossFade;
    }

    /**
     * The Class TransitionState.
     */
    static class TransitionState extends LayerState {
	
	/** The m alpha. */
	int mAlpha = 0;
	
	/** The m duration. */
	int mDuration;
	
	/** The m cross fade. */
	boolean mCrossFade;

	/**
	 * Instantiates a new transition state.
	 *
	 * @param orig the orig
	 * @param owner the owner
	 */
	TransitionState(TransitionState orig, TransitionDrawable owner) {
	    super(orig, owner);
	}

	/* (non-Javadoc)
	 * @see com.pearl.shelves.drawable.LayerDrawable.LayerState#newDrawable()
	 */
	@Override
	public Drawable newDrawable() {
	    return new TransitionDrawable(this);
	}

	/* (non-Javadoc)
	 * @see com.pearl.shelves.drawable.LayerDrawable.LayerState#getChangingConfigurations()
	 */
	@Override
	public int getChangingConfigurations() {
	    return mChangingConfigurations;
	}
    }
}
