package com.example.android;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;


public class HomePage extends OptionMenu {

	/** Method called when activity is created */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
 
    }
    
    /** Method called when user clicks on SettingsButton */
    public void OpenSettings (View view){
    	
    	Intent intent = new Intent(this, SettingsPage.class);
    	startActivity(intent);
    }
    
    /** Method called when user clicks on LaunchButton */
    public void OpenMoodPage (View view){
    	
    	Intent intent = new Intent(this, MoodPage.class);
    	startActivity(intent);
    }
    
    
}

