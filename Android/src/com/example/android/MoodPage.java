/**This activity allows the user to enter his mood.
 * By pushing the OK button the user can go to the Camera activity.
 * The user enters this activity after clicking on the Start Button
 * in the HomePage activity or by clicking a notification from the app.
 * The activity is extension of the OptionMenu activity which provides two buttons */

package com.example.android;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.content.Intent;

	public class MoodPage extends OptionMenu  implements OnSeekBarChangeListener {

		/** Creation objects */
		private SeekBar Bar ;
		private TextView Rate;
		private int value = 5;
		private int max =10;
	  	
		/** Method called to create activity*/
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
	        
			// Get objects from XML
			setContentView(R.layout.activity_mood_page);
	        
			Bar = (SeekBar)findViewById(R.id.MoodSeekbar); 
			Bar.setOnSeekBarChangeListener(this);  // Create Listener
			Rate = (TextView)findViewById(R.id.MoodRate);

			// Intial Setup
			Bar.setMax(max);
			Bar.setProgress(value);
			Rate.setText(String.format("%d", value));  

		}
	                                                   
	    /** Method called when user clicks on NextButton */
	    public void OpenCamera (View view){
	    	
	    	//Open Next Camera Activity and give the mood to it
	    	Intent intent = new Intent(this, Camera.class);
	    	intent.putExtra("mood", Bar.getProgress());
	    	startActivity(intent);	
	    }

	    /**Method called when SeekBar Bar is changed 
	     * When Seekbar is changed a new value for the TextView is set*/
	    @Override  
	    public void onProgressChanged(SeekBar Bar, int progress, boolean fromUser) {
	     
	    	// Adapt value TextView Rate to the new value of SeekBar Bar
	    	if(fromUser) {  
	    		int value = progress;  
	    		Rate.setText(String.format("%d", value));
	    	}  
	    }  
	     
	    @Override   
	    public void onStartTrackingTouch(SeekBar Bar) {  

	    }  
	     
	    @Override  
	    public void onStopTrackingTouch(SeekBar Bar) {  

	    }
	    
	}
