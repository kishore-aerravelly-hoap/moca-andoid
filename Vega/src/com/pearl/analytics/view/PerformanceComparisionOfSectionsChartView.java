package com.pearl.analytics.view;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.View;

import com.pearl.analytics.models.StudentPercentageList;
import com.pearl.logger.Logger;

public class PerformanceComparisionOfSectionsChartView extends
		AbstractDemoChart {

	public View generateChart(Context context,
			StudentPercentageList percentageList, List<String> sections) {

		String[] examType = new String[percentageList.getExamTypeCount()];
		double[] sectionOneCount;
		double[] sectionTwoCount;

		String[] titles = new String[sections.size()];
		for (int i = 0; i < sections.size(); i++) {
			titles[i] = sections.get(i);
		}

		// Creating an XYSeries for Income
		XYSeries sectionOneSeries = new XYSeries(titles[0]);
		// Creating an XYSeries for Expense
		XYSeries sectionTwoSeries = new XYSeries(titles[1]);
		// Adding data to Income and Expense Series

		if (percentageList.getStudentPercentages().size() > 0) {
			if (percentageList.getStudentPercentages().get(0) == null) {
				sectionOneCount = new double[0];
			} else {
				sectionOneCount = new double[percentageList
						.getStudentPercentages().get(0).getPercentages().size()];
				for (int i = 0; i < percentageList.getStudentPercentages()
						.get(0).getExamTypes().size(); i++) {
					if (percentageList.getExamTypeCount() == percentageList
							.getStudentPercentages().get(0).getExamTypes()
							.size())
						examType[i] = percentageList.getStudentPercentages()
								.get(0).getExamTypes().get(i);
					sectionOneCount[i] = Double.parseDouble(percentageList
							.getStudentPercentages().get(0).getPercentages()
							.get(i));
				}
			}
			if (percentageList.getStudentPercentages().get(1) == null) {
				sectionTwoCount = new double[0];
			} else {
				sectionTwoCount = new double[percentageList
						.getStudentPercentages().get(1).getPercentages().size()];
				for (int i = 0; i < percentageList.getStudentPercentages()
						.get(1).getExamTypes().size(); i++) {
					if (percentageList.getExamTypeCount() == percentageList
							.getStudentPercentages().get(1).getExamTypes()
							.size())
						examType[i] = percentageList.getStudentPercentages()
								.get(1).getExamTypes().get(i);
					sectionTwoCount[i] = Double.parseDouble(percentageList
							.getStudentPercentages().get(1).getPercentages()
							.get(i));
				}
			}
			if (sectionOneCount.length != 0) {
				for (int i = 0; i < sectionOneCount.length; i++)
					sectionOneSeries.add(i, sectionOneCount[i]);
			}

			if (sectionTwoCount.length != 0) {
				for (int i = 0; i < sectionTwoCount.length; i++)
					sectionTwoSeries.add(i, sectionTwoCount[i]);
			}
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
       // sectionOneRenderer.setOrientation(Orientation.HORIZONTAL);
		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer sectionTwoRenderer = new XYSeriesRenderer();
		sectionTwoRenderer.setColor(Color.YELLOW);
		sectionTwoRenderer.setPointStyle(PointStyle.POINT);
		sectionTwoRenderer.setFillPoints(true);
		sectionTwoRenderer.setLineWidth(2);
		sectionTwoRenderer.setDisplayChartValues(true);
		sectionTwoRenderer.setChartValuesSpacing(0.5f);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		Logger.warn("", "x text label locations: "+multiRenderer.getXTextLabelLocations());
		multiRenderer.setXLabels(0);
		multiRenderer.setYAxisMax(120);
		multiRenderer.setYAxisMin(0);
		multiRenderer.setAxisTitleTextSize(20);
		multiRenderer.setXAxisMin(-1);
		multiRenderer.setXAxisMax(5);
		multiRenderer.setXTitle("Exam Types");
		multiRenderer.setYTitle("% of Marks");
		multiRenderer.setInScroll(false);
		multiRenderer.setXLabelsAlign(Align.CENTER);
		multiRenderer.setYLabelsAlign(Align.RIGHT);
		multiRenderer.setLabelsTextSize(15);
		multiRenderer.setZoomEnabled(false, false);
		multiRenderer.setPanEnabled(true, false);
		multiRenderer.setPanLimits(new double[]{-1,percentageList.getExamTypeCount()+2, 0, 120});
		multiRenderer.setMargins(new int[] { 10, 40, 0, 40 });
		try {
			for (int i = 0; i < examType.length; i++) {
				multiRenderer.addXTextLabel(i, examType[i] + "");
			}
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "INVALID DATA" + e);
		}

		multiRenderer.addSeriesRenderer(sectionOneRenderer);
		multiRenderer.addSeriesRenderer(sectionTwoRenderer);

		// Creating a Line Chart
		return ChartFactory.getCubeLineChartView(context, dataset, multiRenderer, 0.3f);
		//return ChartFactory.getLineChartView(context, dataset, multiRenderer);
	
		

	    
		/*String[] titles = new String[sections.size()];
		for (int i = 0; i < sections.size(); i++) {
			titles[i] = sections.get(i);
		}
		double[] sequence = new double[] { percentageList.getExamTypeCount() };
		for (int i = 0; i < percentageList.getExamTypeCount(); i++) {
			sequence[i] = (i++) + 1;
		}

		String[] examTypes = new String[percentageList.getExamTypeCount()];

		List<double[]> x = new ArrayList<double[]>();
		for (int i = 0; i < titles.length; i++) {
			x.add(sequence);
		}
		List<double[]> values = new ArrayList<double[]>();
		for (int i = 0; i < percentageList.getStudentPercentages().size(); i++) {
			double[] d = new double[] { percentageList.getStudentPercentages()
					.get(i).getPercentages().size() };
			for (int j = 0; j < percentageList.getStudentPercentages().get(i)
					.getPercentages().size(); j++) {
				d[j] = Double
						.parseDouble(percentageList.getStudentPercentages()
								.get(i).getPercentages().get(j));
			}
			if (percentageList.getExamTypeCount() == percentageList
					.getStudentPercentages().get(i).getExamTypes().size()) {
				for (int j = 0; j < percentageList.getStudentPercentages()
						.get(i).getExamTypes().size(); j++) {
					examTypes[j] = percentageList.getStudentPercentages()
							.get(i).getExamTypes().get(j);
				}
			}
			values.add(d);
		}
		
		 * values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4,
		 * 26.1, 23.6, 20.3, 17.2, 13.9 }); values.add(new double[] { 10, 10,
		 * 12, 15, 20, 24, 26, 26, 23, 18, 14, 11 }); values.add(new double[] {
		 * 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6 }); values.add(new
		 * double[] { 35});
		 

		int[] colors = new int[] { Color.BLUE, Color.GREEN };
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE,
				PointStyle.DIAMOND };
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setDisplayChartValues(true);
		}
		setChartSettings(renderer, "Average temperature", "Exam Types",
				"% of Marks", -1, 5, 0, 100, Color.LTGRAY, Color.LTGRAY);
		renderer.setXLabels(0);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.RIGHT);
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setZoomEnabled(false, false);
		renderer.setPanEnabled(true, false);
		renderer.setXAxisMin(0);
		renderer.setPanLimits(new double[] { 0,
				percentageList.getExamTypeCount() + 2, 0, 120 });
		for (int i = 0; i < examTypes.length; i++) {
			renderer.addXTextLabel(i + 1, examTypes[i] + "");
		}

		return ChartFactory.getLineChartView(context,
				buildDataset(titles, x, values), renderer);

	  }*/

	}	
}
