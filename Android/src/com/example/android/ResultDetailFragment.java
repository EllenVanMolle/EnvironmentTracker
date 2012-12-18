package com.example.android;

import org.achartengine.GraphicalView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment representing a single Result detail screen. This fragment is
 * either contained in a {@link ResultListActivity} in two-pane mode (on
 * tablets) or a {@link ResultDetailActivity} on handsets.
 */
public class ResultDetailFragment extends Fragment {
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Chart chart;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ResultDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the chart content specified by the fragment
			// arguments.
			chart = ResultsContent.chartsMap.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the chartview from the indicated chart.
		GraphicalView oldChartView = chart.getChartView();
		// If the chartview does exist, make sure the previous one is gone.
		if (oldChartView != null) {
			oldChartView.repaint();
			oldChartView.setVisibility(View.GONE);
			if (oldChartView.getParent() != null) {
				((ViewGroup) oldChartView.getParent()).removeView(oldChartView);
			}
		}
		// Always, make a new graph with updated data.
		GraphicalView chartView = chart.makeChart(getActivity());
		// If the chartview has already a parent, remove the chartview from this parent.
		if (chartView.getParent() != null) {
			((ViewGroup) chartView.getParent()).removeView(chartView);
		}
		// return the chartview to show
		return chartView;
	}
	
	/*
	 * 	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the chartview from the indicated chart.
		GraphicalView chartView = chart.getChartView();
		// If the chartview does exist, reuse it and make sure the previous one is gone.
		if (chartView != null) {
			chartView.repaint();
			chartView.setVisibility(View.GONE);
		} else {
			// When the chartview does not exist yet, make a new one.
			chartView = chart.makeChart(getActivity());
		}
		// If the chartview has already a parent, remove the chartview from this parent.
		if (chartView.getParent() != null) {
			((ViewGroup) chartView.getParent()).removeView(chartView);
		}
		// return the chartview to show
		return chartView;
	}
}
	 */
}
