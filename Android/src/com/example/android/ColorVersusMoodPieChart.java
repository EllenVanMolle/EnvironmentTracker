package com.example.android;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

public class ColorVersusMoodPieChart extends Chart {
	
	private int minMood;
	
	private int maxMood;

	public ColorVersusMoodPieChart(String newId, String newName) {
		super(newId, newName);
		
		// Bepaal voor deze grafiek de minimale en maximale mood
		if (this.getId().equals("4")) {
			minMood = 0;
			maxMood = 3;
		} else if (this.getId().equals("3")) {
			minMood = 4;
			maxMood = 6;
		} else if (this.getId().equals("2")) {
			minMood = 7;
			maxMood = 10;
		}
}

	@Override
	public GraphicalView makeChart(Context context) {
		// When the view isn't yet created, create it.
		DefaultRenderer renderer = getRenderer();
		chartView = ChartFactory.getPieChartView(context, getDataset(context), renderer);
		// Return the view so it can be showed.
		return chartView;
	}

	public CategorySeries getDataset(Context context) {
		// Get the data needed
		// Open the database to read and get the mood and the hue category of all observations.
		EnvironmentTrackerOpenHelper openhelper = new EnvironmentTrackerOpenHelper(ResultsContent.context);
		SQLiteDatabase database = openhelper.getReadableDatabase();
		String[] columns = new String[2];
		columns[0] = "MOOD";
		columns[1] = "HUE_CATEGORY";
		Cursor results = database.query(true, "Observation", columns, null, null, null, null, null, null);
		
		// Make sure the cursor is at the start.
		results.moveToFirst();
		
		int[] nrOfObservationsInCategory = new int[4];
		
		while (!results.isAfterLast()) {
			int mood = results.getInt(0);
			int hue = results.getInt(1);
			
			// Only using the data when in right category
			if ((minMood <= mood) && (mood <= maxMood)) {
				// Add 1 more to the number of observations in the right category.
				nrOfObservationsInCategory[hue-1]++;
			}
			results.moveToNext();
		}
		
		// Put the data in a dataset for the pie chart.
		CategorySeries dataset = new CategorySeries("data");
		dataset.add(context.getString(R.string.vigorous), nrOfObservationsInCategory[0]);
		dataset.add(context.getString(R.string.nature), nrOfObservationsInCategory[1]);
		dataset.add(context.getString(R.string.ocean), nrOfObservationsInCategory[2]);
		dataset.add(context.getString(R.string.flower), nrOfObservationsInCategory[3]);
		
		return dataset;
	}

	/**
	 * Stel de verschillende opties voor de grafiek in.
	 */
	public DefaultRenderer getRenderer() {
	    DefaultRenderer renderer = new DefaultRenderer();
	    renderer.setLegendTextSize(30);
	    renderer.setLegendHeight(100);
	    
	    // Stel de opties voor elke hue categorie afzonderlijk in.
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
	    
	    // Stel algemene opties in.
	    renderer.setShowLegend(true);
	    renderer.setShowLabels(false);
	    renderer.setShowGrid(true);
	    
	    return renderer;
	}

}
