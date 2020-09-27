package com.pearl.analytics.view;

import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.pearl.analytics.models.GradePassFailPercentage;

public class PassFailPercentageInaSectionChartView extends AbstractDemoChart{


	public View getChart(Context context, GradePassFailPercentage analyticsDetails){

	    double[] values = new double[2];
	    int[] colors = new int[2];
	    values[0] = Double.parseDouble(analyticsDetails.getPassPercentage());
	    values[1] = Double.parseDouble(analyticsDetails.getFailPercentage());
	    for(int i=0; i<2; i++){
		    Random random = new Random();
		    int color = Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
		    colors[i] = color;	
	    }
	    
	    DefaultRenderer renderer = buildCategoryRenderer(colors);
	    renderer.setShowLabels(true);
	    renderer.setShowLegend(false);
	    renderer.setZoomEnabled(false);
	    renderer.setPanEnabled(false);
	    renderer.setChartTitleTextSize(20);
	    CategorySeries series = new CategorySeries("");
	    series.add("pass, "+values[0], values[0]);
	    series.add("fail, "+values[1], values[1]);
	    return ChartFactory.getPieChartView(context, series,
		        renderer);
	}

}
