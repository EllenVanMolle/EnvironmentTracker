package com.example.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

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
		context = ResultListFragment.getContext();
		addItem(new ColorVersusMoodChart("1", context.getString(R.string.color_graph)));
		addItem(new ColorVersusMoodPieChart("2", context.getString(R.string.color_happy_graph)));
		addItem(new ColorVersusMoodPieChart("3", context.getString(R.string.color_neutral_graph)));
		addItem(new ColorVersusMoodPieChart("4", context.getString(R.string.color_unhappy_graph)));
		addItem(new VerticalBarChartWithMood("5", context.getString(R.string.saturation_graph)));
		addItem(new VerticalBarChartWithMood("6", context.getString(R.string.brightness_graph)));
		addItem(new TimeVersusMood("7", context.getString(R.string.time_graph)));
		addItem(new MoodVersusDecibel("8", context.getString(R.string.decibel_happy_graph)));
		addItem(new MoodVersusDecibel("9", context.getString(R.string.decibel_neutral_graph)));
		addItem(new MoodVersusDecibel("10", context.getString(R.string.decibel_unhappy_graph)));
	}

	private static void addItem(Chart item) {
		charts.add(item);
		chartsMap.put(item.getId(), item);
	}
}
