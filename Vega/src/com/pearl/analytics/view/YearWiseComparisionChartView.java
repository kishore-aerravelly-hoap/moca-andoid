package com.pearl.analytics.view;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.text.Layout.Alignment;
import android.view.View;

import com.pearl.analytics.models.GradePassFailPercentageList;

public class YearWiseComparisionChartView extends AbstractDemoChart {

	public View getChartView(Context context, GradePassFailPercentageList list, List<String> yearsList) {
	    String[] titles = new String[2] ;
	    titles[0] = "pass";
	    titles[1] = "fail";
	    
	    String[] years = new String[2];
	    years[0] = list.getPercentageList().get(0).getYear();
	    years[1] = list.getPercentageList().get(1).getYear();
	    
	    List<double[]> values = new ArrayList<double[]>();
	    
	    values.add(new double[] { Double.parseDouble(list.getPercentageList().get(0).getPassPercentage()), Double.parseDouble(list.getPercentageList().get(1).getPassPercentage())});
	    values.add(new double[] { Double.parseDouble(list.getPercentageList().get(0).getFailPercentage()), Double.parseDouble(list.getPercentageList().get(1).getFailPercentage())});

	    XYSeries series = new XYSeries(titles[0]);
	    series.add(1, Double.parseDouble(list.getPercentageList().get(0).getPassPercentage()));
	    series.add(2, Double.parseDouble(list.getPercentageList().get(1).getPassPercentage()));
	    
	    XYSeries series1 = new XYSeries(titles[1]);
	    series1.add(1, Double.parseDouble(list.getPercentageList().get(0).getFailPercentage()));
	    series1.add(2, Double.parseDouble(list.getPercentageList().get(1).getFailPercentage()));
	    
	    XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();
	    dataSet.addSeries(series);
	    dataSet.addSeries(series1);
	    
	    XYSeriesRenderer renderer = new XYSeriesRenderer();
	    renderer.setColor(Color.BLUE);
	    XYSeriesRenderer renderer2 = new XYSeriesRenderer();
	    renderer2.setColor(Color.CYAN);
	    
	    XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();;
	    multiRenderer.addSeriesRenderer(renderer);
	    multiRenderer.addSeriesRenderer(renderer2);
	    multiRenderer.setXLabels(0);
	    multiRenderer.setAxisTitleTextSize(20);
        multiRenderer.setXAxisMin(0);
        multiRenderer.setYAxisMin(0);
        multiRenderer.setYAxisMax(120);
        multiRenderer.setXAxisMax(3);
        multiRenderer.setXTitle("Years");
        multiRenderer.getSeriesRendererAt(0).setDisplayChartValues(true);
        multiRenderer.getSeriesRendererAt(1).setDisplayChartValues(true);
        multiRenderer.getSeriesRendererAt(0).setChartValuesTextAlign(Align.RIGHT);
        multiRenderer.setYTitle("Survey");
        multiRenderer.setInScroll(false);
        multiRenderer.setLabelsTextSize(15);
        multiRenderer.setXLabelsAlign(Align.CENTER);
        multiRenderer.setYLabelsAlign(Align.RIGHT);
        multiRenderer.setZoomEnabled(false, false);
        multiRenderer.setPanEnabled(true, false);
        //multiRenderer.setBarSpacing(2);
		multiRenderer.setBarWidth(35f);
        multiRenderer.setPanLimits(new double[]{0,0,0,0});
        multiRenderer.setMargins(new int[]{10, 40, 0, 40});
        for(int i=0;i<years.length;i++){
        	multiRenderer.addXTextLabel(i+1, years[i]);
        }
	    
	    
	    
	   /* XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	    setChartSettings(renderer, "Monthly sales in the last 2 years", "Month", "Units sold", 0.5,
	        12.5, 0, 24000, Color.GRAY, Color.LTGRAY);
	    renderer.setOrientation(Orientation.HORIZONTAL);
	    renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
	    renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
	    renderer.setXLabels(8);
	    renderer.setYLabels(10);
	    renderer.setXLabelsAlign(Align.LEFT);
	    renderer.setYLabelsAlign(Align.LEFT);
	    renderer.setPanEnabled(true, false);
	    // renderer.setZoomEnabled(false);
	    renderer.setZoomRate(1.1f);
	    renderer.setBarSpacing(0.5f);*/
	    return ChartFactory.getBarChartView(context, dataSet, multiRenderer,
	        Type.DEFAULT);
	  }
}
