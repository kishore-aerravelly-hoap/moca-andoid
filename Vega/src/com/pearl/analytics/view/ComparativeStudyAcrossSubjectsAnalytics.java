package com.pearl.analytics.view;

import java.util.List;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.plot.CategoryPlot;
import org.afree.chart.plot.PiePlot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.renderer.category.BarRenderer;
import org.afree.chart.title.TextTitle;
import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;
import org.afree.data.general.DefaultPieDataset;
import org.afree.data.general.PieDataset;
import org.afree.graphics.GradientColor;
import org.afree.graphics.SolidColor;
import org.afree.graphics.geom.Font;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.pearl.analytics.models.AndroidAnalytics;
import com.pearl.analytics.models.SubjectPoints;
import com.pearl.application.VegaConstants;

public class ComparativeStudyAcrossSubjectsAnalytics extends DemoView{

    /**
     * constructor
     * @param context
     */
	  public ComparativeStudyAcrossSubjectsAnalytics(Context context, AndroidAnalytics analytics) {
	        super(context);

        CategoryDataset dataset = createDataset(analytics);
        AFreeChart chart = createChart(dataset);

        setChart(chart);
    }

    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    private static CategoryDataset createDataset(AndroidAnalytics analytics) {

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < analytics.getSubjectPoints().size(); i++) {
			dataset.addValue(analytics.getSubjectPoints().get(i)
					.getTotalMarks(), analytics.getSubjectPoints().get(i)
					.getSubject()
					+ ", "
					+ analytics.getSubjectPoints().get(i).getTotalMarks(), "");
		}

        return dataset;

    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private static AFreeChart createChart(CategoryDataset dataset) {
        // create the chart...

    	AFreeChart chart = ChartFactory.createBarChart(
            VegaConstants.COMPARATIVE_STUDY_ACCROSS_SUBJECTS,      // chart title
            "Subject",               // domain axis label
            "Marks Obtained",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );


        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setWeight(1);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.getDomainAxis();
        
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);

        // set up gradient paints for series...
        GradientColor gp0 = new GradientColor(Color.BLUE, Color.rgb(0, 0, 64));
        GradientColor gp1 = new GradientColor(Color.GREEN, Color.rgb(0, 64, 0));
        GradientColor gp2 = new GradientColor(Color.RED, Color.rgb(64, 0, 0));
        renderer.setSeriesPaintType(0, gp0);
        renderer.setSeriesPaintType(1, gp1);
        renderer.setSeriesPaintType(2, gp2);
        renderer.setMaximumBarWidth(0.01f);
        Log.w("","rendered plot is:  "+renderer.getPlot());
        Log.w("", "renderer tool tip text:  "+renderer.getLegendItems().get(0).getToolTipText());

        /*CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));*/
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }
    
   /* private CategoryDataset buidDataSet(){
    	DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
    	dataSet.addValue(value, "", "");
    	return dataSet;
    }*/
    
   /* protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        Log.w("", "--- lenght :"+length);
        for (int i = 0; i < length; i++) {
          CategorySeries series = new CategorySeries(titles[i]);
          double[] v = values.get(i);
          int seriesLength = v.length;
          for (int k = 0; k < seriesLength; k++) {
            series.add(v[k]);
          }
          dataset.addSeries(series.toXYSeries());
        }
        return dataset;
      }*/

	
	
	
	
	

    /**
     * Creates a sample dataset.
     * @return a sample dataset.
     */
    private static PieDataset createDataset(List<SubjectPoints> subjectPointsList) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        for (int i=0; i<subjectPointsList.size(); i++) {
        	dataset.setValue(subjectPointsList.get(i).getSubject()+", "+subjectPointsList.get(i).getTotalMarks(), subjectPointsList.get(i).getTotalMarks());
		}
        //+", "+subjectPointsList.get(i).getTotalMarks()
        return dataset;
    }

    /**
     * Creates a chart.
     * @param dataset the dataset.
     * @return a chart.
     */
    private static AFreeChart createChart(PieDataset dataset) {

        AFreeChart chart = ChartFactory.createPieChart(
        		VegaConstants.COMPARATIVE_STUDY_ACCROSS_SUBJECTS, // chart title
                dataset, // data
                true, // include legend
                true,
                false);
        TextTitle title = chart.getTitle();
        title.setToolTipText("A title tooltip!");
        PiePlot plot = (PiePlot) chart.getPlot();
        //plot.setLabelFont(new Font("SansSerif", Typeface.BOLD_ITALIC, 24));
        plot.setLabelFont(new Font("SansSerif", Typeface.NORMAL, 12));
        if(dataset == null)
        	plot.setNoDataMessage("No data available");        	
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;

    }
}
