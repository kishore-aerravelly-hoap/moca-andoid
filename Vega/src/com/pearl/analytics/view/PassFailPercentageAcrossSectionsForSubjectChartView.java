package com.pearl.analytics.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.View;

import com.pearl.analytics.models.GradePassFailPercentageList;
import com.pearl.logger.Logger;

public class PassFailPercentageAcrossSectionsForSubjectChartView extends
		AbstractDemoChart {

	public View getChart(Context context, GradePassFailPercentageList analyticsDetails, String selectedResultType){/*
		
		XYSeries series;
		List<XYSeries> seriesList = new ArrayList<XYSeries>();
		List<XYSeriesRenderer> rendererList= new ArrayList<XYSeriesRenderer>();
		XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
		XYSeriesRenderer renderer;
		
		int x[] = new int[analyticsDetails.getPercentageList().size()];
		int colors[] = new int[analyticsDetails.getPercentageList().size()];
		for(int i=0; i<analyticsDetails.getPercentageList().size(); i++){
			series = new XYSeries("");
			 renderer = new XYSeriesRenderer();
			Random random = new Random();
	    	int color = Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
	    	colors[i] = color;
	    	x[i] = i;
	    	
	    	renderer.setLineWidth(2.0f);
	    	//set renderer values
	    	renderer.setColor(colors[i]);
	    	renderer.setChartValuesSpacing(0.5f);
	    	renderer.setChartValuesTextSize(18);
	    	renderer.setFillPoints(true);
	    	renderer.setDisplayChartValues(true);
	    	// set series values
	    	if (selectedResultType.equalsIgnoreCase("pass"))
	    		series.add(x[i], Double.parseDouble(analyticsDetails.getPercentageList().get(i).getPassPercentage()));
			else
				series.add(x[i], Double.parseDouble(analyticsDetails.getPercentageList().get(i).getFailPercentage()));
	    	

	    	//Add series and renderer to the list
	    	seriesList.add(series);
	    	rendererList.add(renderer);
		}
    	
		for(int i=0; i<seriesList.size(); i++){
			dataSet.addSeries(seriesList.get(i));
		}
		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        double[] range = {0,5,0,5};
		multiRenderer.setMargins(new int[] { 50, 50, 50, 50 });
		multiRenderer.setAxisTitleTextSize(16);
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitleTextSize(30);
		multiRenderer.setLabelsTextSize(15);
		multiRenderer.setShowLegend(false);
		//multiRenderer.setBarSpacing(-0.01);
		multiRenderer.setAxesColor(Color.WHITE);
		multiRenderer.setXAxisMin(-1);
		multiRenderer.setXAxisMax(analyticsDetails.getPercentageList().size());
		multiRenderer.setYAxisMax(120);
		multiRenderer.setYAxisMin(0);
		multiRenderer.setXLabelsAlign(Align.CENTER);
		multiRenderer.setYLabelsAlign(Align.RIGHT);
		multiRenderer.setPanEnabled(true, false);
		multiRenderer.setPanLimits(new double[]{-1,analyticsDetails.getPercentageList().size()+ 5, 0, 120});
		multiRenderer.setZoomEnabled(false, false);
		multiRenderer.setChartTitle("");
		
		multiRenderer.setXTitle("Sections");
		multiRenderer.setYTitle("% of marks");
		
		for(int i=0; i<x.length; i++){
			multiRenderer.addXTextLabel(x[i] - 1, analyticsDetails.getPercentageList().get(i).getSection());
			Logger.info("", "x values: "+multiRenderer.getXTextLabel((double)i));
		}
		
		XYSeries[]  seriess = dataSet.getSeries();
		Logger.verbose("", "seriess length "+seriess.length);
		for(int i=0; i<seriess.length; i++){
			//Logger.verbose("", "index: "+seriess[i].getIndexForKey((double)i));
			Logger.verbose("", "x: "+seriess[i].getX(0));
			//Logger.verbose("", "y: "+seriess[i].getY(0));
		}
				
		//Add all the renderers to multirenderer
		for(int i=0; i<rendererList.size(); i++){
			multiRenderer.addSeriesRenderer(rendererList.get(i));
			//multiRenderer.setShowLabels(true);
			multiRenderer.setBarSpacing(-0.5);
			//multiRenderer.setBarWidth(4.6f);
			multiRenderer.setInScroll(true);
		}
		
		return ChartFactory.getBarChartView(context, dataSet, multiRenderer, Type.DEFAULT);
		
		

		double[] values = new double[analyticsDetails.getPercentageList()
				.size()];
		String[] sections = new String[analyticsDetails.getPercentageList()
				.size()];
		XYSeriesRenderer countRenderer = new XYSeriesRenderer();
		int[] colors = new int[analyticsDetails.getPercentageList().size()];
		for (int i = 0; i < analyticsDetails.getPercentageList().size(); i++) {
			if (selectedResultType.equalsIgnoreCase("pass"))
				values[i] = Double.parseDouble(analyticsDetails
						.getPercentageList().get(i).getPassPercentage());
			else
				values[i] = Double.parseDouble(analyticsDetails
						.getPercentageList().get(i).getFailPercentage());
			Random random = new Random();
	    	int color = Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
			
			sections[i] = analyticsDetails.getPercentageList().get(i).getSection();
			
			 
		}

		// Creating an XYSeries for Income
		XYSeries countSeries = new XYSeries("");
		// Adding data to Income and Expense Series
		Logger.warn("", "x length is:" + sections.length);
		try {
			for (int i = 0; i < sections.length; i++) {
				countSeries.add(i, values[i]);
			}
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "" + e);
		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(countSeries);

		// Creating XYSeriesRenderer to customize incomeSeries
		countRenderer.setLineWidth(0.9f);
		countRenderer.setColor(Color.parseColor("#FFA07A"));
		countRenderer.setChartValuesSpacing(2.0f);
		countRenderer.setDisplayChartValues(true);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		// multiRenderer.setYLabels(yAxisMax);
		multiRenderer.setYAxisMax(120);
		multiRenderer.setYAxisMin(0);
		multiRenderer.setXAxisMin(-1);
		//multiRenderer.setXAxisMax(analyticsDetails.getPercentageList().size()+5);
		multiRenderer.setXTitle("Sections");
		multiRenderer.setYTitle(selectedResultType + " %");
		multiRenderer.setBarSpacing(5);
		//multiRenderer.setBarWidth(10f);
		multiRenderer.setInScroll(false);
		multiRenderer.setZoomEnabled(false, false);
		multiRenderer.setXLabelsAlign(Align.CENTER);
		multiRenderer.setYLabelsAlign(Align.RIGHT);
		multiRenderer.setPanEnabled(true, false);
		multiRenderer.setShowLegend(false);
		multiRenderer.setPanLimits(new double[]{0,0,0,0});
		multiRenderer.setMargins(new int[] { 10, 40, 10, 40 });
		try {
			for (int i = 0; i < sections.length; i++) {
				multiRenderer.addXTextLabel(i, sections[i] + "");
			}
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "" + e);
		}

		multiRenderer.addSeriesRenderer(countRenderer);

		// Creating a Line Chart
		return ChartFactory.getBarChartView(context, dataset, multiRenderer,
				Type.DEFAULT);
	*/
		double[] values = new double[analyticsDetails.getPercentageList()
				.size()];
		String[] sections = new String[analyticsDetails.getPercentageList()
				.size()];
		int[] colors = new int[analyticsDetails.getPercentageList().size()];
		for (int i = 0; i < analyticsDetails.getPercentageList().size(); i++) {
			if (selectedResultType.equalsIgnoreCase("pass"))
				values[i] = Double.parseDouble(analyticsDetails
						.getPercentageList().get(i).getPassPercentage());
			else
				values[i] = Double.parseDouble(analyticsDetails
						.getPercentageList().get(i).getFailPercentage());
			sections[i] = analyticsDetails.getPercentageList().get(i)
					.getSection();
			/*
			 * Random random = new Random(); int color = Color.argb(255,
			 * random.nextInt(255), random.nextInt(255), random.nextInt(255));
			 * colors[i] = color;
			 */
		}

		// Creating an XYSeries for Income
		XYSeries countSeries = new XYSeries("");
		// Adding data to Income and Expense Series
		Logger.warn("", "x length is:" + sections.length);
		try {
			for (int i = 0; i < sections.length; i++) {
				countSeries.add(i, values[i]);
			}
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "" + e);
		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(countSeries);

		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer countRenderer = new XYSeriesRenderer();
		countRenderer.setColor(Color.parseColor("#00FFFF"));
		countRenderer.setLineWidth(0.9f);
		countRenderer.setChartValuesSpacing(2.0f);
		countRenderer.setDisplayChartValues(true);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setAxisTitleTextSize(20);
		multiRenderer.setBarSpacing(1);
		multiRenderer.setBarWidth(15f);
		// multiRenderer.setYLabels(yAxisMax);
		multiRenderer.setYAxisMax(120);
		multiRenderer.setYAxisMin(0);
		multiRenderer.setPointSize(5f);
		multiRenderer.setXAxisMin(-1);
		multiRenderer.setXAxisMax(5);
		multiRenderer.setXTitle("Sections");
		multiRenderer.setYTitle(selectedResultType + " %");
		multiRenderer.setInScroll(false);
		multiRenderer.setLabelsTextSize(15);
		multiRenderer.setZoomEnabled(false, false);
		multiRenderer.setXLabelsAlign(Align.CENTER);
		multiRenderer.setYLabelsAlign(Align.RIGHT);
		multiRenderer.setPanEnabled(true, false);
		multiRenderer.setShowLegend(false);
		multiRenderer.setPanLimits(new double[] { -1,
				analyticsDetails.getPercentageList().size() + 3, 0, 120 });
		multiRenderer.setMargins(new int[] { 10, 40, 10, 40 });
		try {
			for (int i = 0; i < sections.length; i++) {
				multiRenderer.addXTextLabel(i, sections[i] + "");
			}
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "" + e);
		}

		multiRenderer.addSeriesRenderer(countRenderer);

		// Creating a Line Chart
		return ChartFactory.getBarChartView(context, dataset, multiRenderer,
				Type.DEFAULT);
	}
}
