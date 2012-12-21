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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class VerticalBarChartWithMood extends Chart {
	
	private String columnName;

	public VerticalBarChartWithMood(String newId, String newName) {
		super(newId, newName);
		
		// Determine which data needs to be used for this graph.
		if (getId().equals("5")) {
			this.columnName = "SATURATION";
		} else if (getId().equals("6")) {
			this.columnName = "BRIGHTNESS";
		}
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
		// Open the database to read and get the mood and saturation or brightness of all observations.
		EnvironmentTrackerOpenHelper openhelper = new EnvironmentTrackerOpenHelper(ResultsContent.context);
		SQLiteDatabase database = openhelper.getReadableDatabase();
		String[] columns = new String[2];
		columns[0] = "MOOD";
		columns[1] = this.columnName;
		Cursor results = database.query(true, "Observation", columns, null, null, null, null, null, null);
		
		// Make sure the cursor is at the start.		
		if (!results.isFirst()) {
			results.moveToFirst();
		}
		
		int[] meanForMood = new int[11];
		int[] nrMood = new int[11];
		
		// Overloop de verschillende observaties.		
		while (!results.isAfterLast()) {
			int mood = results.getInt(0);
			int variable = results.getInt(1);
			
			// Tel de variabele (sat of brightness) bij de juiste mood waarde.
			meanForMood[mood] = meanForMood[mood] + variable;
			// Verhoog het aantal in de juiste mood categorie.
			nrMood[mood] = nrMood[mood] + 1;
			results.moveToNext();
		}
		
		// Berkenen het gemiddelde van de variabele voor elke mood waarde.		
		for (int i=0; i < 11; i++) {
			if (nrMood[i] == 0) {
				meanForMood[i] = 0;
			} else {
				meanForMood[i] = meanForMood[i]/nrMood[i];
			}
		}
		
		// Plaats de gegevens in een dataset voor de grafiek.
		XYMultipleSeriesDataset myData = new XYMultipleSeriesDataset();
	     XYSeries dataSeries = new XYSeries("data");
	     	 for (int i=0;i<=10;i++) {
	     		 dataSeries.add(i, meanForMood[i]);
	     	 }
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
	    // We willen een verticale grafiek.
	    renderer.setOrientation(Orientation.VERTICAL);
	    //right, top, left, bottom (turned vertical)
	    renderer.setMargins(new int[] {20, 70, 50, 30});
	    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	    r.setColor(Color.RED);
	    renderer.addSeriesRenderer(r);
	    
	    // Stel de juiste labels in afhankelijk van de variabele.
	    if (getName() == "Saturation") {
		    renderer.setChartTitle(context.getString(R.string.moodVersusSaturation));
		    renderer.setYTitle(context.getString(R.string.saturation_graph));
	    }
	    if (getName() == "Brightness") {
		    renderer.setChartTitle(context.getString(R.string.moodVersusBrightness));
		    renderer.setYTitle(context.getString(R.string.brightness_graph));
	    }
	    renderer.setXTitle(context.getString(R.string.mood));
	    renderer.setXAxisMin(0);
	    renderer.setXAxisMax(10);
	    renderer.setYAxisMin(0);
	    renderer.setYAxisMax(100);
	    renderer.setShowLegend(false);
	    renderer.setYLabelsAlign(Align.CENTER);
	    renderer.setBackgroundColor(Color.BLACK);
	    renderer.setShowLegend(false);
	    renderer.setInScroll(false);
	    
	    return renderer;
	}

}

