package com.example.android;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DialRenderer;
import org.achartengine.renderer.DialRenderer.Type;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class MoodVersusDecibel extends Chart {
	
	private GraphicalView dialChartView;
	private int minForMoodCategory;
	private int maxForMoodCategory;

	public MoodVersusDecibel(String newId, String newName) {
		super(newId, newName);
		
		if (this.getId().equals("10")) {
			minForMoodCategory = 0;
			maxForMoodCategory = 3;
		} else if (this.getId().equals("9")) {
			minForMoodCategory = 4;
			maxForMoodCategory = 6;
		} else if (this.getId().equals("8")) {
			minForMoodCategory = 7;
			maxForMoodCategory = 10;
		}
	}

	@Override
	public GraphicalView makeChart(Context context) {
		if (dialChartView==null) {
			DialRenderer renderer = getRenderer();
			setChartSettings(renderer);
			dialChartView = ChartFactory.getDialChartView(context, getDataset(context), renderer);
		}
		return dialChartView;
	}
	
	private CategorySeries getDataset(Context context) {
		
		results.moveToFirst();
		int nrOfObservation = 0;
		double meanDec = 0;
		double minDec = 200;
		double maxDec = 0;
		
		while (!results.isAfterLast()) {
			int columnIndexMood = results.getColumnIndex("MOOD");
			int mood = results.getInt(columnIndexMood);
			int columnIndexDec = results.getColumnIndex("DECIBEL");
			double dec = results.getDouble(columnIndexDec);
			
			// Only using the data when in right category
			if ((minForMoodCategory <= mood) && (mood <= maxForMoodCategory)) {
				nrOfObservation++;
				meanDec = meanDec + dec;
				if (dec < minDec) {
					minDec = dec;
				}
				if (dec > maxDec) {
					maxDec = dec;
				}
			}
			results.moveToNext();
		}
		
		if (nrOfObservation != 0) {
			meanDec = meanDec / nrOfObservation;
		}
		
		CategorySeries dataset = new CategorySeries("Sound indicator");
		dataset.add(context.getString(R.string.minimum), minDec);
	    dataset.add(context.getString(R.string.mean), meanDec);
	    dataset.add(context.getString(R.string.maximum), maxDec);
		return dataset;
	}
	
	private DialRenderer getRenderer() {
		DialRenderer renderer = new DialRenderer();
	    renderer.setChartTitleTextSize(40);
	    renderer.setLabelsTextSize(20);
	    renderer.setLegendTextSize(30);
	    SimpleSeriesRenderer rendererMinimum = new SimpleSeriesRenderer();
	    rendererMinimum.setColor(Color.CYAN);
	    renderer.addSeriesRenderer(rendererMinimum);
	    SimpleSeriesRenderer rendererMean = new SimpleSeriesRenderer();
	    rendererMean.setColor(Color.BLACK);
	    renderer.addSeriesRenderer(rendererMean);
	    SimpleSeriesRenderer rendererMaximum = new SimpleSeriesRenderer();
	    rendererMaximum.setColor(Color.BLUE);
	    renderer.addSeriesRenderer(rendererMaximum);
		return renderer;
	}
	
	private void setChartSettings(DialRenderer renderer) {
	    renderer.setLabelsTextSize(20);
	    renderer.setLabelsColor(Color.BLACK);
	    renderer.setShowLabels(true);
	    renderer.setVisualTypes(new DialRenderer.Type[] {Type.NEEDLE, Type.ARROW, Type.NEEDLE});
	    renderer.setMinValue(0);
	    renderer.setMaxValue(200);
	}
	
}
