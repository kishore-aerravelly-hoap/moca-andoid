package com.pearl.ui.helpers.examination;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.pearl.R;
import com.pearl.activities.CreateExamStepThree;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * A Spinner view that does not dismiss the dialog displayed when the control is "dropped down"
 * and the user presses it. This allows for the selection of more than one option.
 */
public class MultiSelectSpinner extends Spinner implements OnMultiChoiceClickListener {
    
    /** The _items. */
    String[] _items = null;
    
    /** The _selection. */
    boolean[] _selection = null;

    /** The _proxy adapter. */
    ArrayAdapter<String> _proxyAdapter;
    AlertDialog dialog = null;
    

    /**
     * Constructor for use when instantiating directly.
     *
     * @param context the context
     */
    public MultiSelectSpinner(Context context) {
	super(context);


	_proxyAdapter = new ArrayAdapter<String>(context, R.layout.spinner_list_item);
	super.setAdapter(_proxyAdapter);
    }

    /**
     * Constructor used by the layout inflater.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public MultiSelectSpinner(Context context, AttributeSet attrs) {
	super(context, attrs);

	_proxyAdapter = new ArrayAdapter<String>(context, R.layout.spinner_list_item);
	super.setAdapter(_proxyAdapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
    	Logger.warn("", "MS - onclick");
	if (_selection != null && which < _selection.length) {
	    _selection[which] = isChecked;

	    _proxyAdapter.clear();
	    _proxyAdapter.add(buildSelectedItemString());
	    setSelection(0);
	}
	else {
	    throw new IllegalArgumentException("Argument 'which' is out of bounds.");
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean performClick() {
    	Logger.warn("", "MS - perform click");
	final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
	builder.setMultiChoiceItems(_items, _selection, this);
	builder.setOnKeyListener(new Dialog.OnKeyListener() {

        @Override
        public boolean onKey(DialogInterface arg0, int keyCode,
                KeyEvent event) {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
            	Logger.warn("", "MS - back key pressed");
            }
            for(int i=0; i<_items.length; i++){
            	Logger.error("Multiselect", "selected items are "+_items[i]);
            }
            dialog.dismiss();
            return true;
        }
    });
	dialog = builder.show();
	builder.setOnCancelListener(new OnCancelListener() {
		
		@Override
		public void onCancel(DialogInterface dialog) {
			Logger.warn("", "MS - dialog cancelled");
		}
	});
	builder.setCancelable(false);
	return true;
    }

    /**
     * MultiSelectSpinner does not support setting an adapter. This will throw an exception.
     *
     * @param adapter the new adapter
     */
    @Override
    public void setAdapter(SpinnerAdapter adapter) {
	throw new RuntimeException("setAdapter is not supported by MultiSelectSpinner.");
    }

    /**
     * Sets the options for this spinner.
     *
     * @param items the new items
     */
    public void setItems(String[] items) {
	_items = items;
	_selection = new boolean[_items.length];

	Arrays.fill(_selection, false);
    }

    /**
     * Sets the options for this spinner.
     *
     * @param items the new items
     */
    public void setItems(List<String> items) {
	_items = items.toArray(new String[items.size()]);
	_selection = new boolean[_items.length];

	Arrays.fill(_selection, false);
    }

    /**
     * Sets the selected options based on an array of string.
     *
     * @param selection the new selection
     */
    public void setSelection(String[] selection) {
	for (final String sel : selection) {
	    for (int j = 0; j < _items.length; ++j) {
		if (_items[j].equals(sel)) {
		    _selection[j] = true;
		}
	    }
	}
    }
    
    /**
     * Reset spinner.
     */
    public void resetSpinner(){
    	if(_selection != null){
    		for(int which=0; which<_selection.length; which++){
    	    	if (_selection != null && which < _selection.length) {
    	    	    _selection[which] = false;

    	    	    _proxyAdapter.clear();
    	    	    _proxyAdapter.add(buildSelectedItemString());
    	    	    setSelection(0);
    	    	}
    	    	else {
    	    	    throw new IllegalArgumentException("Argument 'which' is out of bounds.");
    	    	}
    	    	}
    	}
    }

    /**
     * Sets the selected options based on a list of string.
     *
     * @param selection the new selection
     */
    public void setSelection(List<String> selection) {
	for (final String sel : selection) {
	    for (int j = 0; j < _items.length; ++j) {
		if (_items[j].equals(sel)) {
		    _selection[j] = true;
		}
	    }
	}
    }

    /**
     * Sets the selected options based on an array of positions.
     *
     * @param selectedIndicies the new selection
     */
    public void setSelection(int[] selectedIndicies) {
	for (final int index : selectedIndicies) {
	    if (index >= 0 && index < _selection.length) {
		_selection[index] = true;
	    }
	    else {
		throw new IllegalArgumentException("Index " + index + " is out of bounds.");
	    }
	}
    }

    /**
     * Returns a list of strings, one for each selected item.
     *
     * @return the selected strings
     */
    public List<String> getSelectedStrings() {
	final List<String> selection = new LinkedList<String>();
	if(_items != null){
		for (int i = 0; i < _items.length; ++i) {
		    if (_selection[i]) {
			selection.add(_items[i]);
		    }
		}		
	}
	return selection;
    }

    /**
     * Returns a list of positions, one for each selected item.
     *
     * @return the selected indicies
     */
    public List<Integer> getSelectedIndicies() {
	final List<Integer> selection = new LinkedList<Integer>();
	for (int i = 0; i < _items.length; ++i) {
	    if (_selection[i]) {
		selection.add(i);
	    }
	}
	return selection;
    }

    /**
     * Builds the string for display in the spinner.
     * @return comma-separated list of selected items
     */
    private String buildSelectedItemString() {
	final StringBuilder sb = new StringBuilder();
	boolean foundOne = false;

	for (int i = 0; i < _items.length; ++i) {
	    if (_selection[i]) {
		if (foundOne) {
		    sb.append(", ");
		}
		foundOne = true;

		sb.append(_items[i]);
	    }
	}

	return sb.toString();
    }
}
