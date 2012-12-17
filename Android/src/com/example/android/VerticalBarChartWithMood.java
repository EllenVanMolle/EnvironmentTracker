package com.example.android;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class VerticalBarChartWithMood extends Chart {
	
	private GraphicalView barChartView;
	
	private String columnName;

	public VerticalBarChartWithMood(String newId, String newName) {
		super(newId, newName);
		
		if (getId().equals("5")) {
			this.columnName = "SATURATION";
		} else if (getId().equals("6")) {
			this.columnName = "BRIGHTNESS";
		}
	}

	@Override
	public GraphicalView makeChart(Context context) {
		if (barChartView==null)
		{
		XYMultipleSeriesRenderer renderer = getRenderer();
		setChartSettings(renderer);
		barChartView  = ChartFactory.getBarChartView(context, getDataset(),renderer, Type.DEFAULT);
		}
		return barChartView;
	}
	
	public XYMultipleSeriesDataset getDataset() {
		// Get the data needed
		
		if (!results.isFirst()) {
			results.moveToFirst();
		}
		
		int[] meanForMood = new int[10];
		int[] nrMood = new int[10];
		
		
		while (!results.isAfterLast()) {
			int columnIndexMood = results.getColumnIndex("MOOD");
			int mood = results.getInt(columnIndexMood);
			int columnIndexVariable = results.getColumnIndex(this.columnName);
			int variable = results.getInt(columnIndexVariable);
			
			meanForMood[mood-1] = meanForMood[mood-1] + variable;
			nrMood[mood-1] = nrMood[mood-1] + 1;
			results.moveToNext();
		}
		
		for (int i=0; i < 10; i++) {
			if (nrMood[i] == 0) {
				meanForMood[i] = 0;
			} else {
				meanForMood[i] = meanForMood[i]/nrMood[i];
			}
		}
		
		XYMultipleSeriesDataset myData = new XYMultipleSeriesDataset();
	     XYSeries dataSeries = new XYSeries("data");
	     	 for (int i=1;i<=10;i++) {
	     		 dataSeries.add(i, meanForMood[i-1]);
	     	 }
	         myData.addSeries(dataSeries);
	         return myData;
	}

	public XYMultipleSeriesRenderer getRenderer() {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(30);
	    renderer.setChartTitleTextSize(40);
	    renderer.setLabelsTextSize(20);
	    renderer.setBarSpacing(1);
	    renderer.setOrientation(Orientation.VERTICAL);
	    //right, top, left, bottom (turned vertical)
	    renderer.setMargins(new int[] {20, 70, 50, 30});
	    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	    r.setColor(Color.RED);
	    renderer.addSeriesRenderer(r);
	    return renderer;
	}

	public void setChartSettings(XYMultipleSeriesRenderer renderer) {
	    renderer.setChartTitle("Mood versus " + getName());
	    renderer.setXTitle("Mood");
	    renderer.setYTitle(getName());
	    renderer.setXAxisMin(0);
	    renderer.setXAxisMax(10);
	    renderer.setYAxisMin(0);
	    renderer.setYAxisMax(100);
	    renderer.setShowLegend(false);
	    renderer.setYLabelsAlign(Align.CENTER);
	    renderer.setBackgroundColor(Color.BLACK);
	    renderer.setShowLegend(false);
	    renderer.setInScroll(false);
	}

}

