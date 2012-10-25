package com.example.android;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class HomePage extends OptionMenu {

		
	private boolean FromAudio = false;
	private TextView textview;
	
	/** Method called when activity is created */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toast.makeText(this, "From Audio =" + FromAudio, Toast.LENGTH_LONG).show();
        Intent i = getIntent();
        FromAudio = i.getBooleanExtra("FromAudio",false);
        Toast.makeText(this, "From Audio =" + FromAudio, Toast.LENGTH_LONG).show();
        
        textview = (TextView)findViewById(R.id.Welcome);
        
        if (FromAudio == true)
        { textview.setText("Thank you!");}
        else 
        { textview.setText("Welcome!");}
     
    }
    
    /** Method called when user clicks on SettingsButton */
    public void OpenSettings (View view){
    	
    	FromAudio = false;
    	Intent intent = new Intent(this, SettingsPage.class);
    	startActivity(intent);
    }
    
    /** Method called when user clicks on LaunchButton */
    public void OpenMoodPage (View view){
    	
    	FromAudio = false;
    	Intent intent = new Intent(this, MoodPage.class);
    	startActivity(intent);
    }
    
    
}

