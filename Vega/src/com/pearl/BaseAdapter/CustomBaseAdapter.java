package com.pearl.BaseAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pearl.R;
import com.pearl.application.VegaConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomBaseAdapter.
 */
public class CustomBaseAdapter extends BaseAdapter {
    
    /** The m inflater. */
    private final LayoutInflater mInflater;
    
    /** The type. */
    String type;
    
    /** The data. */
    String data;

    /*
     * public CustomBaseAdapter(Context context) { mInflater =
     * LayoutInflater.from(context); }
     */
    /**
     * Instantiates a new custom base adapter.
     *
     * @param context the context
     * @param data the data
     * @param type the type
     */
    public CustomBaseAdapter(Context context, String data, String type) {
	this.type = type;
	mInflater = LayoutInflater.from(context);
	this.data = data;

    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
	return 1;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
	return position;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
	return position;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;

	if (convertView == null) {
	    convertView = mInflater.inflate(R.layout.base_list, null);

	    holder = new ViewHolder();

	    holder.basic_data = (TextView) convertView
		    .findViewById(R.id.basic_data);
	    if (type.equalsIgnoreCase(VegaConstants.DASHBOARD_NOTICE)) {
		holder.basic_data.setTextColor(Color.parseColor("#802220"));
	    } else if (type
		    .equalsIgnoreCase(VegaConstants.DASHBOARD_ATTENDANCE)) {
		holder.basic_data.setTextColor(Color.parseColor("#731231"));

	    } else if (type.equalsIgnoreCase(VegaConstants.DASHBOARD_CHAT)) {
		holder.basic_data.setTextColor(Color.parseColor("#872e0c"));

	    } else if (type.equalsIgnoreCase(VegaConstants.DASHBOARD_NOOTBOOK)) {
		holder.basic_data.setTextColor(Color.parseColor("#5b5801"));

	    } else if (type.equalsIgnoreCase(VegaConstants.DASHBOARD_QUIZZARD)) {
		holder.basic_data.setTextColor(Color.parseColor("#592d60"));

	    } else if (type.equalsIgnoreCase(VegaConstants.DASHBOARD_EREADER)) {
		holder.basic_data.setTextColor(Color.parseColor("#205d63"));

	    } else if (type.equalsIgnoreCase(VegaConstants.DASHBOARD_FEEDBACK)) {
		holder.basic_data.setTextColor(Color.parseColor("#2f5200"));

	    } else if (type.equalsIgnoreCase(VegaConstants.DASHBOARD_FAQ)) {
		holder.basic_data.setTextColor(Color.parseColor("#826229"));

	    }else if (type.equalsIgnoreCase(VegaConstants.ANALYTICS)) {
		holder.basic_data.setTextColor(Color.parseColor("#002043"));

	    }
	    holder.basic_data.setText(data);

	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}

	return convertView;
    }

    /**
     * The Class ViewHolder.
     */
    static class ViewHolder {
	
	/** The basic_data. */
	TextView basic_data;

    }
}