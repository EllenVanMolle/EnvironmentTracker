package com.example.android;

import org.achartengine.GraphicalView;

import android.content.Context;

/**
 * Een abstracte klasse waarvan de specialisaties de verschillende klassen voorstellen.
 */
public abstract class Chart {
	/*
	 * De id van de grafiek. Deze wordt gebruikt om de aangeduide grafiek in de lijst te identificeren.
	 */
	private String id;
	
	/*
	 * De naam van de grafiek. Deze wordt gebruikt om de beschrijving van de grafiek weer te geven in de lijst
	 * met grafieken.
	 */
	private String name;
	
	/*
	 * De view die de grafiek weergeeft.
	 */
	private GraphicalView chartView;
	
	public Chart(String newId, String newName) {
		setId(newId);
		setName(newName);
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
	
	public GraphicalView getChartView() {
		return chartView;
	}
	
	/**
	 * You need to overwrite this method to make sure the right text is shown in the list of the master.
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * Bouwt de grafiek op en geeft de view terug.
	 */
	public abstract GraphicalView makeChart(Context context);
}
