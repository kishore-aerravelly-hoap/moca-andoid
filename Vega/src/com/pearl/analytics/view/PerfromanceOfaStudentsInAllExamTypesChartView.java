package com.pearl.analytics.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

import com.pearl.analytics.models.StudentPercentageList;
import com.pearl.logger.Logger;

public class PerfromanceOfaStudentsInAllExamTypesChartView extends AbstractDemoChart {

	public View generateChart(Context context, StudentPercentageList list) {/*
		int x[] = new int[list.getExamTypeCount()];
		int colors[] = new int[list.getExamTypeCount()];
		XYSeries series;
		List<XYSeries> seriesList = new ArrayList<XYSeries>();
		List<XYSeriesRenderer> rendererList= new ArrayList<XYSeriesRenderer>();
		XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
		XYSeriesRenderer renderer;
		
		for(int i=0; i<list.getExamTypeCount(); i++){
			series = new XYSeries("");
			 renderer = new XYSeriesRenderer();
			Random random = new Random();
	    	int color = Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
	    	colors[i] = color;
	    	x[i] = i;
	    	
	    	//set renderer values
	    	renderer.setColor(colors[i]);
	    	renderer.setChartValuesSpacing(0.5f);
	    	renderer.setChartValuesTextSize(18);
	    	renderer.setFillPoints(true);
	    	renderer.setDisplayChartValues(true);
	    	// set series values
	    	series.add(x[i], Double.parseDouble(list.getStudentPercentages().get(0).getPercentages().get(i)));

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
		multiRenderer.setXAxisMax(list.getExamTypeCount()+10);
		multiRenderer.setYAxisMax(120);
		multiRenderer.setYAxisMin(0);
		multiRenderer.setXLabelsAlign(Align.CENTER);
		multiRenderer.setYLabelsAlign(Align.RIGHT);
		multiRenderer.setPanEnabled(true, false);
		multiRenderer.setPanLimits(new double[]{-1,list.getExamTypeCount()+10, 0, 120});
		multiRenderer.setZoomEnabled(false, false);
		multiRenderer.setChartTitle("");
		multiRenderer.setXTitle("Exam Types");
		multiRenderer.setYTitle("% of marks");
		
		for(int i=0; i<x.length; i++){
			multiRenderer.addXTextLabel(x[i], list.getStudentPercentages().get(0).getExamTypes().get(i));
		}
		//Add all the renderers to multirenderer
		for(int i=0; i<rendererList.size(); i++){
			multiRenderer.addSeriesRenderer(rendererList.get(i));
		}
		
		return ChartFactory.getBarChartView(context, dataSet, multiRenderer, Type.DEFAULT);
	*/
	
		double[] values = new double[0];
		String[] examTypes = new String[0];
		for (int i = 0; i < list.getStudentPercentages().size(); i++) {
			values = new double[list.getStudentPercentages().get(i).getPercentages().size()];
			examTypes = new String[list.getStudentPercentages().get(i).getExamTypes().size()];
			for(int j=0; j<list.getStudentPercentages().get(i).getPercentages().size(); j++){
				values[j] = Double.parseDouble(list.getStudentPercentages().get(i)
						.getPercentages().get(j));
			}
			for(int k=0; k<list.getStudentPercentages().get(i).getExamTypes().size(); k++){
				examTypes[k] = list.getStudentPercentages().get(i).getExamTypes().get(k);
			}
		}

		// Creating an XYSeries for Income
		XYSeries countSeries = new XYSeries("");
		// Adding data to Income and Expense Series
		Logger.warn("", "x length is:" + examTypes.length);
		try {
			for (int i = 0; i < examTypes.length; i++) {
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
		countRenderer.setColor(Color.parseColor("#8467D7"));
		countRenderer.setLineWidth(0.9f);
		countRenderer.setChartValuesSpacing(2.0f);
		countRenderer.setDisplayChartValues(true);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		// multiRenderer.setYLabels(yAxisMax);
		multiRenderer.setYAxisMax(120);
		multiRenderer.setYAxisMin(0);
		multiRenderer.setBarSpacing(1);
		multiRenderer.setAxisTitleTextSize(20);
		multiRenderer.setXAxisMin(-1);
		multiRenderer.setXAxisMax(5);
		multiRenderer.setXTitle("Exam Types");
		multiRenderer.setBarWidth(15f);
		multiRenderer.setYTitle("% of marks");
		multiRenderer.setInScroll(false);
		multiRenderer.setLabelsTextSize(15);
		multiRenderer.setZoomEnabled(false, false);
		multiRenderer.setXLabelsAlign(Align.CENTER);
		multiRenderer.setYLabelsAlign(Align.RIGHT);
		multiRenderer.setShowLegend(false);
		
		if(list.getExamTypeCount() > 3){
			multiRenderer.setPanEnabled(true, false);
			multiRenderer.setPanLimits(new double[] { -1,
			list.getStudentPercentages().size() + 7, 0, 120 });
		}else{
			multiRenderer.setPanEnabled(false,false);
		}
		multiRenderer.setMargins(new int[] { 10, 40, 10, 40 });
		try {
			for (int i = 0; i < examTypes.length; i++) {
				multiRenderer.addXTextLabel(i, examTypes[i] + "");
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
