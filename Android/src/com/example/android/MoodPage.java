package com.example.android;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.app.Activity;
import android.content.Intent;

public class MoodPage extends Activity implements OnSeekBarChangeListener {

		/** Creation objects */
	  	private SeekBar Bar ;
	    private TextView Rate;
	  	
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
	        int value = 5;
	        int max =10;
	        Bar.setMax(max);
	        Bar.setProgress(value);
	        Rate.setText(String.format("%d", value));  

	    }
	                                                   
	    /** Method called when user clicks on NextButton */
	    public void OpenCamera (View view){
	    	
	    	//Open Next Activity (Camera)
	    	Intent intent = new Intent(this, Camera.class);
	    	startActivity(intent);
	    	
	    }

	    /** Method called when SeekBar Bar is changed */
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
	     // TODO Auto-generated method stub (defined in OnSeekBarChangeListener)  
	    }  
	     
	    @Override  
	    public void onStopTrackingTouch(SeekBar Bar) {  
	     // TODO Auto-generated method stub (defined in OnSeekBarChangeListener)  
	    }
	    
	}
