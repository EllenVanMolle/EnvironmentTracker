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
import android.graphics.Color;
import android.graphics.Paint.Align;

public class TimeVersusMood extends Chart {
	
	private GraphicalView barChartView;

	public TimeVersusMood(String newId, String newName) {
		super(newId, newName);
	}
	
	@Override
	public GraphicalView makeChart(Context context) {
		if (barChartView==null)
		{
		XYMultipleSeriesRenderer renderer = getRenderer();
		setChartSettings(renderer);
		barChartView  = ChartFactory.getBarChartView(context, getDataset(),renderer, Type.DEFAULT);
		}
		return barChartView;
	}

	public XYMultipleSeriesDataset getDataset() {
		if (!results.isFirst()) {
			results.moveToFirst();
		}
		
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
		
		while (results.moveToNext()) {
			int columnIndexMood = results.getColumnIndex("MOOD");
			int mood = results.getInt(columnIndexMood);
			int columnIndexDay = results.getColumnIndex("DAY");
			int day = results.getInt(columnIndexDay);
			
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

	public XYMultipleSeriesRenderer getRenderer() {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(30);
	    renderer.setChartTitleTextSize(40);
	    renderer.setLabelsTextSize(20);
	    renderer.setBarSpacing(1);
	    renderer.setXLabels(0);
	    renderer.addXTextLabel(1, "Mon");
	    renderer.addXTextLabel(2, "Tue"); 
	    renderer.addXTextLabel(3, "Wed"); 
	    renderer.addXTextLabel(4, "Thu");
	    renderer.addXTextLabel(5, "Fri");
	    renderer.addXTextLabel(6, "Sat");
	    renderer.addXTextLabel(7, "Sun");
	    //top, left, bottom, right
	    renderer.setMargins(new int[] {70, 50, 15, 20});
	    SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	    r.setColor(Color.RED);
	    renderer.addSeriesRenderer(r);
	    return renderer;
	}

	public void setChartSettings(XYMultipleSeriesRenderer renderer) {
	    renderer.setChartTitle("Weekday versus Mood");
	    renderer.setXTitle("Day");
	    renderer.setYTitle("Mood");
	    renderer.setXAxisMin(0);
	    renderer.setXAxisMax(8);
	    renderer.setYAxisMin(0);
	    renderer.setYAxisMax(10);
	    renderer.setShowLegend(false);
	    renderer.setYLabelsAlign(Align.RIGHT);
	}

}
