package com.example.android;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class ColorVersusMoodPieChart extends Chart {
	
	private GraphicalView pieChartView;
	
	private int minForMoodCategory;
	
	private int maxForMoodCategory;

	public ColorVersusMoodPieChart(String newId, String newName) {
		super(newId, newName);
		
		if (this.getId().equals("4")) {
			minForMoodCategory = 0;
			maxForMoodCategory = 3;
		} else if (this.getId().equals("3")) {
			minForMoodCategory = 4;
			maxForMoodCategory = 6;
		} else if (this.getId().equals("2")) {
			minForMoodCategory = 7;
			maxForMoodCategory = 10;
		}
	}

	@Override
	public GraphicalView makeChart(Context context) {
		if (pieChartView==null) {
			DefaultRenderer renderer = getRenderer();
			pieChartView = ChartFactory.getPieChartView(context, getDataset(context), renderer);
		}
		return pieChartView;
	}

	public CategorySeries getDataset(Context context) {
		results.moveToFirst();
		
		int[] nrOfObservationsInCategory = new int[4];
		
		while (!results.isAfterLast()) {
			int columnIndexMood = results.getColumnIndex("MOOD");
			int mood = results.getInt(columnIndexMood);
			int columnIndexHue = results.getColumnIndex("HUE_CATEGORY");
			int hue = results.getInt(columnIndexHue);
			
			// Only using the data when in right category
			if ((minForMoodCategory <= mood) && (mood <= maxForMoodCategory)) {
				nrOfObservationsInCategory[hue-1]++;
			}
			results.moveToNext();
		}
		
		int sum = 0;
		
		for (int i=0;i<nrOfObservationsInCategory.length;i++) {
			sum = sum + nrOfObservationsInCategory[i];
		}
		
		CategorySeries dataset = new CategorySeries("data");
		dataset.add(context.getString(R.string.vigorous), nrOfObservationsInCategory[0]);
		dataset.add(context.getString(R.string.nature), nrOfObservationsInCategory[1]);
		dataset.add(context.getString(R.string.ocean), nrOfObservationsInCategory[2]);
		dataset.add(context.getString(R.string.flower), nrOfObservationsInCategory[3]);
		
		return dataset;
	}

	public DefaultRenderer getRenderer() {
	    DefaultRenderer renderer = new DefaultRenderer();
	    renderer.setLegendTextSize(30);
	    renderer.setLegendHeight(100);
	    
	    SimpleSeriesRenderer seriesRendererYellow = new SimpleSeriesRenderer();
	    seriesRendererYellow.setColor(Color.YELLOW);
	    renderer.addSeriesRenderer(0, seriesRendererYellow);
	    
	    SimpleSeriesRenderer seriesRendererGreen = new SimpleSeriesRenderer();
	    seriesRendererGreen.setColor(Color.GREEN);
	    renderer.addSeriesRenderer(1, seriesRendererGreen);
	    
	    SimpleSeriesRenderer seriesRendererBlue = new SimpleSeriesRenderer();
	    seriesRendererBlue.setColor(Color.BLUE);
	    renderer.addSeriesRenderer(2, seriesRendererBlue);
	    
	    SimpleSeriesRenderer seriesRendererPurple = new SimpleSeriesRenderer();
	    seriesRendererPurple.setColor(Color.MAGENTA);
	    renderer.addSeriesRenderer(3, seriesRendererPurple);
	    
	    renderer.setShowLegend(true);
	    renderer.setShowLabels(false);
	    renderer.setShowGrid(true);
	    
	    return renderer;
	}

}
