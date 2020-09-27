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

import com.pearl.analytics.models.GradePassFailPercentageList;
import com.pearl.logger.Logger;

public class StudentCountForARangeInAGradeChartView extends AbstractDemoChart {

	public View generateChart(Context context, GradePassFailPercentageList list) {/*
		int x[] = new int[list.getPercentageList().size()];
		int colors[] = new int[list.getPercentageList().size()];
		XYSeries series;
		List<XYSeries> seriesList = new ArrayList<XYSeries>();
		List<XYSeriesRenderer> rendererList= new ArrayList<XYSeriesRenderer>();
		XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
		XYSeriesRenderer renderer;
		
		for(int i=0; i<list.getPercentageList().size(); i++){
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
	    	series.add(x[i], Double.parseDouble(list.getPercentageList().get(i).getStudentCount()));

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
		multiRenderer.setYLabelsAlign(Align.RIGHT);
		multiRenderer.setChartTitleTextSize(30);
		multiRenderer.setLabelsTextSize(15);
		multiRenderer.setLegendTextSize(20);
		multiRenderer.setShowLegend(false);
		multiRenderer.setLegendHeight(40);
		multiRenderer.setBarSpacing(-0.7);
		multiRenderer.setAxesColor(Color.WHITE);
		multiRenderer.setXLabelsAlign(Align.CENTER);
		//multiRenderer.setInitialRange(range, 1);
		multiRenderer.setXAxisMin(-1);
		multiRenderer.setXAxisMax(list.getPercentageList().size());
		multiRenderer.setYAxisMax(20);
		multiRenderer.setYAxisMin(0);
		
		
		multiRenderer.setYLabelsAlign(Align.RIGHT);
		// multiRenderer.s(Align.LEFT);
		multiRenderer.setPanEnabled(true, false);
		multiRenderer.setPanLimits(new double[]{-1,list.getPercentageList().size()+10, 0, 120});
		multiRenderer.setZoomEnabled(false, false);
		multiRenderer.setChartTitle("");
		multiRenderer.setXTitle("Sections");
		multiRenderer.setYTitle("Count of Students");
		
		for(int i=0; i<x.length; i++){
			multiRenderer.addXTextLabel(x[i], list.getPercentageList().get(i).getSection());
		}
		//Add all the renderers to multirenderer
		for(int i=0; i<rendererList.size(); i++)
			multiRenderer.addSeriesRenderer(rendererList.get(i));
		
		return ChartFactory.getBarChartView(context, dataSet, multiRenderer, Type.DEFAULT);
	*/
		
		double[] values = new double[list.getPercentageList()
				.size()];
		String[] grades = new String[list.getPercentageList()
				.size()];
		int[] colors = new int[list.getPercentageList().size()];
		for (int i = 0; i < list.getPercentageList().size(); i++) {
				values[i] = Double.parseDouble(list
						.getPercentageList().get(i).getStudentCount());
			grades[i] = list.getPercentageList().get(i).getSection();
			/*
			 * Random random = new Random(); int color = Color.argb(255,
			 * random.nextInt(255), random.nextInt(255), random.nextInt(255));
			 * colors[i] = color;
			 */
		}

		// Creating an XYSeries for Income
		XYSeries countSeries = new XYSeries("");
		// Adding data to Income and Expense Series
		Logger.warn("", "x length is:" + grades.length);
		try {
			for (int i = 0; i < grades.length; i++) {
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
		countRenderer.setColor(Color.parseColor("#FF7F50"));
		countRenderer.setLineWidth(1.9f);
		countRenderer.setChartValuesSpacing(2.0f);
		countRenderer.setDisplayChartValues(true);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		// multiRenderer.setYLabels(yAxisMax);
		multiRenderer.setYAxisMax(120);
		multiRenderer.setYAxisMin(0);
		multiRenderer.setAxisTitleTextSize(20);
		multiRenderer.setXAxisMin(-1);
		multiRenderer.setXAxisMax(5);
		multiRenderer.setXTitle("Grades");
		multiRenderer.setYTitle("Count of Students");
		multiRenderer.setBarSpacing(5);
		multiRenderer.setInScroll(false);
		multiRenderer.setLabelsTextSize(15);
		multiRenderer.setZoomEnabled(false, false);
		multiRenderer.setXLabelsAlign(Align.CENTER);
		multiRenderer.setYLabelsAlign(Align.RIGHT);
		multiRenderer.setPanEnabled(true, false);
		multiRenderer.setShowLegend(false);
		multiRenderer.setPanLimits(new double[] { -1,
				list.getPercentageList().size() + 3, 0, 120 });
		multiRenderer.setMargins(new int[] { 10, 40, 10, 40 });
		try {
			for (int i = 0; i < grades.length; i++) {
				multiRenderer.addXTextLabel(i, grades[i] + "");
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
