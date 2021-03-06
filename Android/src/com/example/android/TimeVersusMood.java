package com.example.android;

import java.util.Calendar;

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

public class TimeVersusMood extends Chart {
	
	public TimeVersusMood(String newId, String newName) {
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
		// Open the database to read and get the mood and the day of all observations.
		EnvironmentTrackerOpenHelper openhelper = new EnvironmentTrackerOpenHelper(ResultsContent.context);
		SQLiteDatabase database = openhelper.getReadableDatabase();
		String[] columns = new String[2];
		columns[0] = "MOOD";
		columns[1] = "DAY";
		Cursor results = database.query(true, "Observation", columns, null, null, null, null, null, null);
		
		// Make sure the cursor is at the start.
		results.moveToFirst();
		
		int meanMoodDay1 = 0;
		int meanMoodDay2 = 0;
		int meanMoodDay3 = 0;
		int meanMoodDay4 = 0;
		int meanMoodDay5 = 0;
		int meanMoodDay6 = 0;
		int meanMoodDay7 = 0;
		int nrMoodDay1 = 0;
		int nrMoodDay2 = 0;
		int nrMoodDay3 = 0;
		int nrMoodDay4 = 0;
		int nrMoodDay5 = 0;
		int nrMoodDay6 = 0;
		int nrMoodDay7 = 0;		
		
		// Overloop de verschillende observaties.
		while (results.moveToNext()) {
			int mood = results.getInt(0);
			int day = results.getInt(1);
			
			// Verhoog de mood met de gevonden mood en het aantal met 1 bij de gevonden dag.
			if (day == Calendar.MONDAY) {
				meanMoodDay1 = meanMoodDay1 + mood;
				nrMoodDay1++;
			} else if (day == Calendar.TUESDAY) {
				meanMoodDay2 = meanMoodDay2 + mood;
				nrMoodDay2++;
			} else if (day == Calendar.WEDNESDAY) {
				meanMoodDay3 = meanMoodDay3 + mood;
				nrMoodDay3++;
			} else if (day == Calendar.THURSDAY) {
				meanMoodDay4 = meanMoodDay4 + mood;
				nrMoodDay4++;
			} else if (day == Calendar.FRIDAY) {
				meanMoodDay5 = meanMoodDay5 + mood;
				nrMoodDay5++;
			} else if (day == Calendar.SATURDAY) {
				meanMoodDay6 = meanMoodDay6 + mood;
				nrMoodDay6++;
			} else if (day == Calendar.SUNDAY) {
				meanMoodDay7 = meanMoodDay7 + mood;
				nrMoodDay7++;
			}
		}
		
		// Bereken de gemiddelde mood voor elke dag.
		if (nrMoodDay1 == 0) {
			meanMoodDay1 = 0;
		} else {
			meanMoodDay1 = meanMoodDay1/nrMoodDay1;
		} if (nrMoodDay2 == 0) {
			meanMoodDay2 = 0;
		} else {
			meanMoodDay2 = meanMoodDay2/nrMoodDay2;
		} if (nrMoodDay3 == 0) {
			meanMoodDay3 = 0;
		} else {
			meanMoodDay3 = meanMoodDay3/nrMoodDay3;
		} if (nrMoodDay4 == 0) {
			meanMoodDay4 = 0;
		} else {
			meanMoodDay4 = meanMoodDay4/nrMoodDay4;
		} if (nrMoodDay5 == 0) {
			meanMoodDay5 = 0;
		} else {
			meanMoodDay5 = meanMoodDay5/nrMoodDay5;
		} if (nrMoodDay6 == 0) {
			meanMoodDay6 = 0;
		} else {
			meanMoodDay6 = meanMoodDay6/nrMoodDay6;
		} if (nrMoodDay7 == 0) {
			meanMoodDay7 = 0;
		} else {
			meanMoodDay7 = meanMoodDay7/nrMoodDay7;
		}
		
		// Plaats de gegevens in de dataset voor de grafiek.
		XYMultipleSeriesDataset myData = new XYMultipleSeriesDataset();
	     XYSeries dataSeries = new XYSeries("data");
	         dataSeries.add(1,meanMoodDay1);
	         dataSeries.add(2,meanMoodDay2);
	         dataSeries.add(3,meanMoodDay3);
	         dataSeries.add(4,meanMoodDay4);
	         dataSeries.add(5,meanMoodDay5);
	         dataSeries.add(6,meanMoodDay6);
	         dataSeries.add(7,meanMoodDay7);
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
	    renderer.addXTextLabel(1, context.getString(R.string.monday));
	    renderer.addXTextLabel(2, context.getString(R.string.tuesday)); 
	    renderer.addXTextLabel(3, context.getString(R.string.wednesday)); 
	    renderer.addXTextLabel(4, context.getString(R.string.thursday));
	    renderer.addXTextLabel(5, context.getString(R.string.friday));
	    renderer.addXTextLabel(6, context.getString(R.string.saturday));
	    renderer.addXTextLabel(7, context.getString(R.string.sunday));
	    //top, left, bottom, right
	    renderer.setMargins(new int[] {70, 50, 15, 20});
	    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	    r.setColor(Color.RED);
	    renderer.addSeriesRenderer(r);
	    renderer.setChartTitle(context.getString(R.string.weekdayVersusMood));
	    renderer.setXTitle(context.getString(R.string.day));
	    renderer.setYTitle(context.getString(R.string.mood));
	    renderer.setXAxisMin(0);
	    renderer.setXAxisMax(8);
	    renderer.setYAxisMin(0);
	    renderer.setYAxisMax(10);
	    renderer.setShowLegend(false);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    return renderer;
	}
}
