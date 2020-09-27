package com.pearl.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class VegaWidgetProvider.
 */
public class VegaWidgetProvider extends AppWidgetProvider {

    /** The Constant LOG. */
    private static final String LOG = "com.pearl.widget";

    /* (non-Javadoc)
     * @see android.appwidget.AppWidgetProvider#onUpdate(android.content.Context, android.appwidget.AppWidgetManager, int[])
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
	    int[] appWidgetIds) {

	// Get all ids
	final ComponentName thisWidget = new ComponentName(context,
		VegaWidgetProvider.class);
	appWidgetManager.getAppWidgetIds(thisWidget);

	// Build the intent to call the service
	/*
	 * Intent intent = new Intent(context.getApplicationContext(),
	 * NotficationService.class);
	 * intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
	 */

	// Update the widgets via the service
	// context.startService(intent);
    }
}