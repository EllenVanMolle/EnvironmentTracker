package com.example.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class ResultsContent {

	/**
	 * An array of the graphs.
	 */
	public static List<Chart> charts = new ArrayList<Chart>();

	/**
	 * A map of the graphs, by ID.
	 */
	public static Map<String, Chart> chartsMap = new HashMap<String, Chart>();
	
	public static Context context;
	
	static {	
		// Add the graphs to the list.
		context = ResultListFragment.getParameter();
		Log.d("Toevoegen", "Het toevoegen van de grafieken.");
		addItem(new ColorVersusMoodChart("1", "Color"));
		addItem(new ColorVersusMoodPieChart("2", "Color: Unhappy"));
		addItem(new ColorVersusMoodPieChart("3", "Color: Neutral"));
		addItem(new ColorVersusMoodPieChart("4", "Color: Happy"));
		addItem(new VerticalBarChartWithMood("5", "Saturation"));
		addItem(new VerticalBarChartWithMood("6", "Brightness"));
		addItem(new TimeVersusMood("7", "Time"));
		addItem(new MoodVersusDecibel("8", "Decibel: Unhappy"));
		addItem(new MoodVersusDecibel("9", "Decibel: Neutral"));
		addItem(new MoodVersusDecibel("10", "Decibel: Happy"));
	}

	private static void addItem(Chart item) {
		charts.add(item);
		chartsMap.put(item.getId(), item);
	}
}
