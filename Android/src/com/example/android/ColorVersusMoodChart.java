package com.example.android;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class ColorVersusMoodChart extends Chart {
	
	private GraphicalView chartView;

	public ColorVersusMoodChart(String newId, String newName) {
		super(newId, newName);
	}
	
	@Override
	public GraphicalView makeChart(Context context) {
		// When the view isn't yet created, create it.
		XYMultipleSeriesRenderer renderer = getRenderer(context);
		chartView  = ChartFactory.getBarChartView(context, getDataset(),renderer, Type.DEFAULT);
		// Return the view so it can be showed.
		return chartView;
	}

	public XYMultipleSeriesDataset getDataset() {
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
		
		int[] meanMoodCategoryHue = new int[4];
		int[] nrMoodCategoryHue = new int[4];
		
		// Overloop de verschillende observaties.
		while (!results.isAfterLast()) {
			int mood = results.getInt(0);
			int hue = results.getInt(1);
			
			// Tel de mood erbij en verhoog het aantal met 1 in de juiste categorie.
			meanMoodCategoryHue[hue-1] = meanMoodCategoryHue[hue-1] + mood;
			nrMoodCategoryHue[hue-1]++;
			results.moveToNext();
		}
		
		// Bereken voor elke hue categorie de gemiddelde mood.
		for (int i=1;i<=4;i++) {
			if (nrMoodCategoryHue[i-1] == 0) {
				meanMoodCategoryHue[i-1] = 0;
			} else {
				meanMoodCategoryHue[i-1] = meanMoodCategoryHue[i-1]/nrMoodCategoryHue[i-1];
			}
		}
		
		// Plaats de gegevens samen in een dataset voor de grafiek.
		XYMultipleSeriesDataset myData = new XYMultipleSeriesDataset();
	     XYSeries dataSeries = new XYSeries("data");
	         dataSeries.add(1,meanMoodCategoryHue[0]);
	         dataSeries.add(2,meanMoodCategoryHue[1]);
	         dataSeries.add(3,meanMoodCategoryHue[2]);
	         dataSeries.add(4,meanMoodCategoryHue[3]);
	         myData.addSeries(dataSeries);
	         return myData;
	}

	/**
	 * Stel de verschillende opties voor de grafiek in.
	 */
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
