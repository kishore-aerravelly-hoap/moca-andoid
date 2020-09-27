package com.tresbu.studentview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

// TODO: Auto-generated Javadoc
/**
 * The Class TestFragment.
 */
public class TestFragment extends Fragment {

    /** The magznumber. */
    private int magznumber;

    /**
     * Instantiates a new test fragment.
     */
    public TestFragment() {
	super();
    }

    /**
     * Constructor for being created explicitly.
     *
     * @param position the position
     */
    public TestFragment(int position) {
	magznumber = position;
    }

    /**
     * If we are being created with saved state, restore our state.
     *
     * @param saved the saved
     */
    @Override
    public void onCreate(Bundle saved) {
	super.onCreate(saved);
	if (null != saved) {
	    magznumber = saved.getInt("magznumber");
	}
    }

    /**
     * Save the number of Androids to be displayed.
     *
     * @param toSave the to save
     */
    @Override
    public void onSaveInstanceState(Bundle toSave) {
	toSave.putInt("magznumber", magznumber);
    }

    /**
     * Make a grid to view the magazines.
     *
     * @param inflater the inflater
     * @param container the container
     * @param saved the saved
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle saved) {

	final Context c = getActivity().getApplicationContext();

	final LinearLayout l = new LinearLayout(c);
	// l.setBackgroundColor(R.drawable.nb_search_inner_bg);
	final LayoutParams params = new LayoutParams(1000, 700, 0);

	l.setLayoutParams(params);

	final LinearLayout l2 = new LinearLayout(c);
	// l.setBackgroundColor(00000000);
	final LayoutParams p = new LayoutParams(500, 500, 0);
	l2.setLayoutParams(p);

	return l;
    }
}
