/**This activity allows the user to navigate to several other activities
 * eg. SettingsPage activity and MoodPage activity
 * The user enters this activity when launching the app and 
 * after making an audio recording. The editText is adapted 
 * depending on the previous activity
 * The activity is extension of the OptionMenu activity which provides two buttons */

package com.example.android;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;


public class HomePage extends OptionMenu {
		
	private boolean FromAudio = false;
	private TextView textview;
	
	/**Method called when activity is created */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        
        // Get extra information from intent
        Intent i = getIntent();
        FromAudio = i.getBooleanExtra("FromAudio",false);
          
        textview = (TextView)findViewById(R.id.Welcome);
        
        // Change Textview text if the previous activity was Audio
        if (FromAudio == true)
        { textview.setText("Thank you!");}
        else 
        { textview.setText("Welcome!");}
    }
    
    /**Method called when user clicks on SettingsButton 
     * open SettingsPage activity*/
    public void openSettings (View view){
    	
    	FromAudio = false;
    	Intent intent = new Intent(this, SettingsPage.class);
    	startActivity(intent);
    }
    
    /**Method called when user clicks on LaunchButton
     * open MoodPage activity */
    public void openMoodPage (View view){
    	
    	FromAudio = false;
    	Intent intent = new Intent(this, MoodPage.class);
    	startActivity(intent);
    }
    
    public void openResults (View view) {
    	Intent intentResults = new Intent(this, ResultListActivity.class);
    	startActivity(intentResults);
    }
    
}

