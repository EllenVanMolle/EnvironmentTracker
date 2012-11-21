package com.example.android;

import java.util.Calendar;
import java.util.Date;

import com.example.android.model.EnvironmentData;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.content.Intent;

/**
 * <p>
 * This activity allows the user to enter his mood. By pushing the OK button the
 * user can go to the Camera activity. The user enters this activity after
 * clicking on the Start Button in the HomePage activity or by clicking a
 * notification from the app. The activity is extension of the OptionMenu
 * activity which provides two buttons
 * </p>
 * 
 * @author Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 * @version 10 November 2012
 */
public class MoodPage extends OptionMenu implements OnSeekBarChangeListener {

	// Page elements and constants :
	private SeekBar 			Bar;
	private TextView 			Rate;
	private int 				value 		= 5;
	private static final int 	MAX_VALUE 	= 10;
	public static final String	KEY_PARCEL 	= EnvironmentData.class.getName();
	
	// Tracked parameters on this page :
	private double 	moodrate;
	// http://stackoverflow.com/questions/5369682/android-get-current-time-and-date
	// Date currentDate = new Date(System.currentTimeMillis());
	private Calendar 	added_on = Calendar.getInstance();
	private double 		longitude = 0;
	private double 		latitude = 0;

	/**
	 * Method called to create activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get objects from XML
		setContentView(R.layout.activity_mood_page);

		Bar = (SeekBar) findViewById(R.id.MoodSeekbar);
		Bar.setOnSeekBarChangeListener(this); // Create Listener
		Rate = (TextView) findViewById(R.id.MoodRate);

		// Intial Setup
		Bar.setMax(MAX_VALUE);
		Bar.setProgress(value);
		Rate.setText(String.format("%d", value));
	}

	/**
	 * Method called when user clicks on NextButton
	 * 
	 * @note passing an object from one activity to another : 	
	 * http://stackoverflow.com/questions/2906925/android-how-do-i-pass-an-object-from-one-activity-to-another
	 */
	public void OpenCamera(View view) {
		// Create an intent :
		Intent intent = new Intent(this, Camera.class);
		
		try {
			// Create a bundle and Parcelable to pass to the next activity :
			Bundle bundle = new Bundle();
			EnvironmentData parcel = new EnvironmentData(added_on, moodrate, longitude, latitude);
			bundle.putParcelable(MoodPage.KEY_PARCEL, parcel);
			// Add bundle to intent
			intent.putExtras(bundle);
		} catch (Exception e) {
			// TODO : Error handling
		} finally {
			// Open Next activity (Camera Activity) :
			startActivity(intent);
		}
	}

	/**
	 * Method called when SeekBar Bar is changed When Seekbar is changed a new
	 * value for the TextView is set
	 */
	@Override
	public void onProgressChanged(SeekBar Bar, int progress, boolean fromUser) {

		// Adapt value TextView Rate to the new value of SeekBar Bar
		if (fromUser) {
			int value = progress;
			Rate.setText(String.format("%d", value));
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar Bar) {
		// TODO Auto-generated method stub (defined in OnSeekBarChangeListener)
	}

	@Override
	public void onStopTrackingTouch(SeekBar Bar) {
		// TODO Auto-generated method stub (defined in OnSeekBarChangeListener)
	}
}
