package com.example.android;

import org.achartengine.GraphicalView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class Chart {
	
	private String id;
	private String name;
	private GraphicalView chartView;
	protected Cursor results;
	
	public Chart(String newId, String newName) {
		setId(newId);
		setName(newName);
		
		// Open the database and get the observations.
		EnvironmentTrackerOpenHelper openhelper = new EnvironmentTrackerOpenHelper(ResultsContent.context);
		SQLiteDatabase database = openhelper.getReadableDatabase();
		results = database.query(true, "Observation", null, null, null, null, null, null, null);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean chartViewAlreadyExists() {
		return (chartView != null);
	}
	
	public GraphicalView getChartView() {
		return chartView;
	}
	
	/**
	 * You need to overwrite this method to make sure the right text is shown in the list of the master.
	 */
	public String toString() {
		return name;
	}
	
	public abstract GraphicalView makeChart(Context context);
}
