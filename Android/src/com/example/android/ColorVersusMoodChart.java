package com.example.android;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class ColorVersusMoodChart extends Chart {
	
	private GraphicalView chartView;

	public ColorVersusMoodChart(String newId, String newName) {
		super(newId, newName);
	}
	
	@Override
	public GraphicalView makeChart(Context context) {
		
		if (chartView==null)
		{
		XYMultipleSeriesRenderer renderer = getRenderer(context);
		chartView  = ChartFactory.getBarChartView(context, getDataset(),renderer, Type.DEFAULT);
		}
		return chartView;
	}

	public XYMultipleSeriesDataset getDataset() {
		// Get the data needed
		
		results.moveToFirst();
		
		int[] meanMoodCategoryHue = new int[4];
		int[] nrMoodCategoryHue = new int[4];
		
		while (!results.isAfterLast()) {
			int columnIndexMood = results.getColumnIndex("MOOD");
			int mood = results.getInt(columnIndexMood);
			int columnIndexHue = results.getColumnIndex("HUE_CATEGORY");
			int hue = results.getInt(columnIndexHue);
			
			meanMoodCategoryHue[hue-1] = meanMoodCategoryHue[hue-1] + mood;
			nrMoodCategoryHue[hue-1]++;
			results.moveToNext();
		}
		
		for (int i=1;i<=4;i++) {
			if (nrMoodCategoryHue[i-1] == 0) {
				meanMoodCategoryHue[i-1] = 0;
			} else {
				meanMoodCategoryHue[i-1] = meanMoodCategoryHue[i-1]/nrMoodCategoryHue[i-1];
			}
		}
		
		XYMultipleSeriesDataset myData = new XYMultipleSeriesDataset();
	     XYSeries dataSeries = new XYSeries("data");
	         dataSeries.add(1,meanMoodCategoryHue[0]);
	         dataSeries.add(2,meanMoodCategoryHue[1]);
	         dataSeries.add(3,meanMoodCategoryHue[2]);
	         dataSeries.add(4,meanMoodCategoryHue[3]);
	         myData.addSeries(dataSeries);
	         return myData;
	}

	public XYMultipleSeriesRenderer getRenderer(Context context) {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(30);
	    renderer.setChartTitleTextSize(40);
	    renderer.setLabelsTextSize(20);
	    renderer.setBarSpacing(1);
	    renderer.setXLabels(0);
	    renderer.addXTextLabel(1, context.getString(R.string.vigorous));
	    renderer.addXTextLabel(2, context.getString(R.string.nature)); 
	    renderer.addXTextLabel(3, context.getString(R.string.ocean)); 
	    renderer.addXTextLabel(4, context.getString(R.string.flower));
	    //top, left, bottom, right
	    renderer.setMargins(new int[] {70, 50, 15, 20});
	    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	    r.setColor(Color.MAGENTA);
	    renderer.addSeriesRenderer(r);
	    renderer.setChartTitle(context.getString(R.string.moodVersusHue));
	    renderer.setXTitle(context.getString(R.string.hueCategory));
	    renderer.setYTitle(context.getString(R.string.mood));
	    renderer.setXAxisMin(0);
	    renderer.setXAxisMax(5);
	    renderer.setYAxisMin(0);
	    renderer.setYAxisMax(10);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    renderer.setBackgroundColor(Color.BLACK);
	    renderer.setShowGridY(true);
	    renderer.setShowLegend(false);
	    return renderer;
	}

}
