package com.example.android;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.android.database.DatabaseHandler;
import com.example.android.model.EnvironmentData;
import com.example.android.model.PictureData;
import com.example.android.model.SoundData;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * <p>
 * <code>ResultPage</code> activity class.
 * </p>
 * @author		Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 */
public class ResultPage extends FragmentActivity implements ActionBar.TabListener {
	
	private DatabaseHandler handler;

	private final List<ResultPage.Record> records = new ArrayList<ResultPage.Record>();
	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current tab position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_page);
		
		// -------------------------------------------------------
		// LOADING DATA
		//
		/*
		 * Instantiate a new DatabaseHandler :
		 */
		handler = new DatabaseHandler(this);
		/*
		 * Dummy data :
		 */
		handler.insertTestData();
		/*
		 * Get all records :
		 */
		List<EnvironmentData> list = handler.getEnvironmentDataCRUD().select();
		// Fill list.
		for (EnvironmentData d : list) {
			PictureData p = handler.getPictureDataCRUD().select(d.getPictureID());
			SoundData s = handler.getSoundDataCRUD().select(d.getSoundID());
			records.add(new Record(d, p, s));
		}
		handler.close();
		
		// -------------------------------------------------------
		// INITIALIZING TABS
		//
		// Set up the action bar to show tabs.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// For each of the sections in the app, add a tab to the action bar.
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section1)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section2)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.title_section3)
				.setTabListener(this));
	}
	
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, show the tab contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER,
				tab.getPosition() + 1);
		fragment.setArguments(args);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current tab position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current tab position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_result_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	/**
	 * 
	 * @author 
	 *
	 */
	private class Record implements Serializable {
		
		private final EnvironmentData 	environmentData;
		private final PictureData		pictureData;
		private final SoundData			soundData;
		
		/**
		 * @param environmentData
		 * @param pictureData
		 * @param soundData
		 */
		Record(EnvironmentData environmentData,
				PictureData pictureData, SoundData soundData) {
			this.environmentData = environmentData;
			this.pictureData = pictureData;
			this.soundData = soundData;
		}
		
		public EnvironmentData getEnvironmentData() {
			return environmentData;
		}
		
		public PictureData getPictureData() {
			return pictureData;
		}
		
		public SoundData getSoundData() {
			return soundData;
		}
	}
}
