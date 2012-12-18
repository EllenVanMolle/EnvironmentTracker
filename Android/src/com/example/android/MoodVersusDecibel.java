package com.example.android;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DialRenderer;
import org.achartengine.renderer.DialRenderer.Type;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
		// When the view isn't yet created, create it.
		DialRenderer renderer = getRenderer();
		dialChartView = ChartFactory.getDialChartView(context, getDataset(context), renderer);
		// Return the view so it can be showed.
		return dialChartView;
	}
	
	private CategorySeries getDataset(Context context) {
		// Get the data needed
		// Open the database to read and get the mood and the decibels of all observations.
		EnvironmentTrackerOpenHelper openhelper = new EnvironmentTrackerOpenHelper(ResultsContent.context);
		SQLiteDatabase database = openhelper.getReadableDatabase();
		String[] columns = new String[2];
		columns[0] = "MOOD";
		columns[1] = "DECIBEL";
		Cursor results = database.query(true, "Observation", columns, null, null, null, null, null, null);
		
		// Make sure the cursor is at the start.
		results.moveToFirst();
		int nrOfObservation = 0;
		double meanDec = 0;
		double minDec = 200;
		double maxDec = 0;
		
		// Overloop de verschillende observaties.
		while (!results.isAfterLast()) {
			int mood = results.getInt(0);
			double dec = results.getDouble(1);
			
			// Only using the data when in right category
			if ((minForMoodCategory <= mood) && (mood <= maxForMoodCategory)) {
				// Verhoog het aantal observaties (in de juiste categorie van mood)
				nrOfObservation++;
				// Tel de mood erbij.
				meanDec = meanDec + dec;
				// Bepaal of deze observatie het nieuwe minimum is.
				if (dec < minDec) {
					minDec = dec;
				}
				// Bepaal of deze observatie het nieuwe maximum is.
				if (dec > maxDec) {
					maxDec = dec;
				}
			}
			results.moveToNext();
		}
		
		// Bereken de gemiddelde decibels.
		if (nrOfObservation != 0) {
			meanDec = meanDec / nrOfObservation;
		}
		
		// Plaats de data in een dataset voor de grafiek.
		CategorySeries dataset = new CategorySeries("Sound indicator");
		dataset.add(context.getString(R.string.minimum), minDec);
	    dataset.add(context.getString(R.string.mean), meanDec);
	    dataset.add(context.getString(R.string.maximum), maxDec);
		return dataset;
	}
	
	/**
	 * Stel de verschillende opties voor de grafiek in.
	 */
	private DialRenderer getRenderer() {
		DialRenderer renderer = new DialRenderer();
	    renderer.setChartTitleTextSize(40);
	    renderer.setLabelsTextSize(20);
	    renderer.setLegendTextSize(30);
	    // Stel de opties voor het min, gemiddelde en max apart in.
	    SimpleSeriesRenderer rendererMinimum = new SimpleSeriesRenderer();
	    rendererMinimum.setColor(Color.CYAN);
	    renderer.addSeriesRenderer(rendererMinimum);
	    SimpleSeriesRenderer rendererMean = new SimpleSeriesRenderer();
	    rendererMean.setColor(Color.BLACK);
	    renderer.addSeriesRenderer(rendererMean);
	    SimpleSeriesRenderer rendererMaximum = new SimpleSeriesRenderer();
	    rendererMaximum.setColor(Color.BLUE);
	    renderer.addSeriesRenderer(rendererMaximum);
	    
	    // Stel de algemene opties in.
	    renderer.setLabelsTextSize(20);
	    renderer.setLabelsColor(Color.BLACK);
	    renderer.setShowLabels(true);
	    renderer.setVisualTypes(new DialRenderer.Type[] {Type.NEEDLE, Type.ARROW, Type.NEEDLE});
	    renderer.setMinValue(0);
	    renderer.setMaxValue(200);
	    
		return renderer;
	}
	
}
