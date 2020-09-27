package com.pearl.analytics.view;

import java.util.ArrayList;
import java.util.List;
import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
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

public class ComparativeStudyAcrossSectionsAnalytics extends AbstractDemoChart{

	public View generateChart(Context context, AndroidAnalytics sectionOneAnalytics, AndroidAnalytics sectionTwoAnalytics, List<String> sections){
		
		List<double[]> xAxisValues = new ArrayList<double[]>();
		List<double[]> yAxis = new ArrayList<double[]>();
		String[] range = new String[0];
	    double[] sectionOneCount = new double[sectionOneAnalytics.getSubjectPoints().size()];
	    double[] sectionTwoCount = new double[sectionTwoAnalytics.getSubjectPoints().size()];
	    
	    String[] titles = new String[sections.size()];
	    for(int i=0; i< sections.size(); i++){
	    	titles[i] = sections.get(i);
	    }
	    
	    if(sectionOneAnalytics.getSubjectPoints().size() > 0)
	    	range = new String[sectionOneAnalytics.getSubjectPoints().size()];
	    else if(sectionTwoAnalytics.getSubjectPoints().size() > 0)
	    	range = new String[sectionTwoAnalytics.getSubjectPoints().size()];
	    
	    for(int i=0; i<sectionOneAnalytics.getSubjectPoints().size(); i++){
	    	range[i] = sectionOneAnalytics.getSubjectPoints().get(i).getRange();
	    	sectionOneCount[i] = sectionOneAnalytics.getSubjectPoints().get(i).getCount();
	    }
	    
	    for(int i=0; i<sectionTwoAnalytics.getSubjectPoints().size(); i++){
	    	range[i] = sectionTwoAnalytics.getSubjectPoints().get(i).getRange();
	    	sectionTwoCount[i] = sectionTwoAnalytics.getSubjectPoints().get(i).getCount();
	    }
	    
        // Creating an  XYSeries for Income
        XYSeries sectionOneSeries = new XYSeries(titles[0]);
        // Creating an  XYSeries for Expense
        XYSeries sectionTwoSeries = new XYSeries(titles[1]);
        // Adding data to Income and Expense Series
        try {
        Logger.warn("", "x length is:"+range.length);
        for(int i=0;i<range.length;i++){
        	if(sectionOneCount.length != 0)
        		sectionOneSeries.add(i, sectionOneCount[i]);
        	if(sectionTwoCount.length != 0)
        		sectionTwoSeries.add(i,sectionTwoCount[i]);
        }}catch(Exception e) {
            Logger.error(getClass().getSimpleName(), e);
        }
 
        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(sectionOneSeries);
        // Adding Expense Series to dataset
        dataset.addSeries(sectionTwoSeries);
        
 
        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer sectionOneRenderer = new XYSeriesRenderer();
        sectionOneRenderer.setColor(Color.GREEN);
        sectionOneRenderer.setPointStyle(PointStyle.DIAMOND);
        sectionOneRenderer.setFillPoints(true);
        sectionOneRenderer.setLineWidth(2);
        sectionOneRenderer.setDisplayChartValues(true);
 
        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer sectionTwoRenderer = new XYSeriesRenderer();
        sectionTwoRenderer.setColor(Color.YELLOW);
        sectionTwoRenderer.setPointStyle(PointStyle.SQUARE);
        sectionTwoRenderer.setFillPoints(true);
        sectionTwoRenderer.setLineWidth(2);
        sectionTwoRenderer.setDisplayChartValues(true);
 
        int yAxisMax = (int)(sectionOneAnalytics.getCount() + sectionOneAnalytics.getCount());
        if(sectionOneAnalytics.getCount() > sectionTwoAnalytics.getCount()){
        	yAxisMax = (int)sectionOneAnalytics.getCount();
        }else
        	yAxisMax = (int)sectionTwoAnalytics.getCount();
	    if(yAxisMax == 2)
	    	yAxisMax = yAxisMax * 3;
	    if(yAxisMax > 2 && yAxisMax <6)
	    	yAxisMax = yAxisMax *2;
	    if(yAxisMax == 1)
	    	yAxisMax = yAxisMax * 6;
	    
        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setYAxisMax(yAxisMax);
        multiRenderer.setXAxisMin(-1);
        multiRenderer.setXAxisMax(5);
        multiRenderer.setAxisTitleTextSize(20);
        multiRenderer.setXTitle("% of Marks");
        multiRenderer.setYTitle("Count Of Students");
        multiRenderer.setInScroll(false);
        multiRenderer.setLabelsTextSize(15);
        multiRenderer.setXLabelsAlign(Align.CENTER);
        multiRenderer.setYLabelsAlign(Align.RIGHT, 0);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(true, false);
        double maxLimit = 0;
        if(sectionOneAnalytics.getSubjectPoints().size() > sectionTwoAnalytics.getSubjectPoints().size()){
        	maxLimit = sectionOneAnalytics.getSubjectPoints().size();
        } else if(sectionOneAnalytics.getSubjectPoints().size() > sectionTwoAnalytics.getSubjectPoints().size()){
        	maxLimit = sectionTwoAnalytics.getSubjectPoints().size();
        } else if(sectionOneAnalytics.getSubjectPoints().size() == sectionTwoAnalytics.getSubjectPoints().size()){
        	maxLimit = sectionTwoAnalytics.getSubjectPoints().size();
        } 
		multiRenderer.setPanLimits(new double[]{-1,maxLimit, 0, 120});
        multiRenderer.setMargins(new int[]{10, 40, 0, 40});
        try {
        for(int i=0;i<range.length;i++){
        	multiRenderer.addXTextLabel(i, range[i]+"");
        }
        }catch(Exception e) {
            Log.e(getClass().getSimpleName(), "INVALID DATA"+e);
        }
       
        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(sectionOneRenderer);
        multiRenderer.addSeriesRenderer(sectionTwoRenderer);
 
        // Creating a Line Chart
        return ChartFactory.getLineChartView(context, dataset, multiRenderer);
	}
	
	
}
