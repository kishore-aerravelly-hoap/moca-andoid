package com.pearl.analytics.view;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.View;

import com.pearl.analytics.models.AndroidAnalytics;
import com.pearl.logger.Logger;

public class ComparativeStudyAcrossTestsAnalytics extends AbstractDemoChart {

	public View generateChart(Context context, AndroidAnalytics analyticsDetails) {
		
		String[] range = new String[analyticsDetails.getSubjectPoints().size()];
	    double[] count = new double[analyticsDetails.getSubjectPoints().size()];
	    
	    for(int i=0; i<analyticsDetails.getSubjectPoints().size(); i++){
	    	range[i] = analyticsDetails.getSubjectPoints().get(i).getRange();
	    	count[i] = analyticsDetails.getSubjectPoints().get(i).getCount();
	    }
	    
	    int[] x = new int[analyticsDetails.getSubjectPoints().size()];
	    int j = 0;
		for(int i=0; i<analyticsDetails.getSubjectPoints().size(); i++){
			x[i] = j;
			j++;
		}
        // Creating an  XYSeries for Income
        XYSeries countSeries = new XYSeries("");
        // Adding data to Income and Expense Series
        Logger.warn("", "x length is:"+range.length);
     try {
        for(int i=0;i<range.length;i++){
        	countSeries.add(x[i], count[i]);
        }
     }catch(Exception e) {
	 Log.e(getClass().getSimpleName(), ""+e);
     }
        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(countSeries);
        
        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer countRenderer = new XYSeriesRenderer();
        countRenderer.setColor(Color.GREEN);
        countRenderer.setLineWidth(0.1f);
        countRenderer.setChartValuesSpacing(2.0f);
        countRenderer.setDisplayChartValues(true);
 
        int yAxisMax = (int)(analyticsDetails.getCount());
	    if(yAxisMax == 2)
	    	yAxisMax = yAxisMax * 3;
	    if(yAxisMax > 2 && yAxisMax <6)
	    	yAxisMax = yAxisMax *2;
	    if(yAxisMax == 1)
	    	yAxisMax = yAxisMax * 6;
	    
        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setAxisTitleTextSize(20);
        //multiRenderer.setYLabels(yAxisMax);
        multiRenderer.setYAxisMax(yAxisMax);
        multiRenderer.setXAxisMin(-1);
        multiRenderer.setXAxisMax(5);
        multiRenderer.setXTitle("% of Marks");
        multiRenderer.setYTitle("Count Of Students");
        multiRenderer.setBarSpacing(2);
        multiRenderer.setBarWidth(15f);
        multiRenderer.setInScroll(false);
        multiRenderer.setLabelsTextSize(15);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setXLabelsAlign(Align.CENTER);
        multiRenderer.setYLabelsAlign(Align.RIGHT);
		//multiRenderer.setPanLimits(new double[]{-1,5+10, 0, 120});
        multiRenderer.setShowLegend(false);
        //multiRenderer.setPanLimits(new double[]{0,0,0,0});
        if(analyticsDetails.getSubjectPoints().size() > 3){
			multiRenderer.setPanEnabled(true, false);
			multiRenderer.setPanLimits(new double[] { -1,
					analyticsDetails.getSubjectPoints().size(),  0, 120 });		
		}else{
			multiRenderer.setPanEnabled(false,false);
		}
        
        
        
        multiRenderer.setMargins(new int[]{10, 40, 10, 40});
        try {
        for(int i=0;i<x.length;i++){
            multiRenderer.addXTextLabel(i, range[i]+"");
        }}catch(Exception e) {
            Log.e(getClass().getSimpleName(), ""+e);
        }
 
        multiRenderer.addSeriesRenderer(countRenderer);
 
        // Creating a Line Chart
        return ChartFactory.getBarChartView(context, dataset, multiRenderer, Type.DEFAULT);
		
	  }

}
