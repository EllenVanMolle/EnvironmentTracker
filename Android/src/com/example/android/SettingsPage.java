/**This activity allows the user to set some preferences
 * and also sets an alarm depending on these preferences
 * The user enters this activity after clicking on Settings button 
 * in the HomePage activity.
 * The activity is extension of the OptionMenu activity which provides two buttons */

package com.example.android;

import java.util.Calendar;

import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SettingsPage extends OptionMenu implements OnSharedPreferenceChangeListener {
	
	private static final String KEY_PREF_NOT = "pref_Notification"; // KeyValue of Notifications
	private static final String KEY_PREF_START = "pref_StartNotTime"; // KeyValue of StartTime van notifications
	private static final String KEY_PREF_STOP = "pref_StopNotTime"; // KeyValue of StopTime van notifications
	private static final String KEY_PREF_INTERVAL = "pref_NotInterval"; // KeyValue of Notification Interval
	
	private SharedPreferences prefs;
	
	final static int RQS_1 = 1;
	
	/** Method called on creation of activity*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Add fragment to activity and display fragment as the main content
        getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();
        
        //Ensure initialization of default values
        PreferenceManager.setDefaultValues(this, R.xml.settings, true);
        
        //Create PrefenceManager
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        prefs.registerOnSharedPreferenceChangeListener(this);
    }
    
    /** Method called when activity is Resumed */
    @Override
    protected void onResume() {
        super.onResume();
        prefs.registerOnSharedPreferenceChangeListener(this);
    }
    
    /** Method called when activity is interrupted */
    @Override
    protected void onPause() {
        super.onPause();
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }
    
    /** Method called when one of the Preferences is changed */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key){
    	
    	// If the CheckBox Notification preference is changed 
    	if (key.equals(KEY_PREF_NOT)){
    		// get current value of Pref_Notification
    		boolean Return = prefs.getBoolean(key,false);
    		
    		// If CheckBox Notification is enabled set the alarm
    		if (Return){  
    			//Toast.makeText(this, "Enabled", Toast.LENGTH_LONG).show();
    			setAlarm();
    		}
    		
    		// If CheckBox Notification is disabled cancel the alarm
    		else {
    			//Toast.makeText(this, "Disabled", Toast.LENGTH_LONG).show();
    			cancelAlarm();
    		}
    	}
    	// If the StartTime preference is changed
    	else if (key.equals(KEY_PREF_START + ".minute") || key.equals(KEY_PREF_START + ".hour"))
    		{resetAlarm();
    		}
    	// If the StopTime preference is changed
    	else if (key.equals(KEY_PREF_STOP + ".minute") || key.equals(KEY_PREF_STOP + ".hour"))
			{resetAlarm();
		}
    	// If the Interval Notification preference is changed
    	else if (key.equals(KEY_PREF_INTERVAL)){
    		resetAlarm();
    	}
    	else{};
    }
       
   /** Method called to set the alarm */
   private void setAlarm() {
	   
	   int interval = prefs.getInt(KEY_PREF_INTERVAL, 120);
	   
	   //Get a calendar set to the current time
	   Calendar calNow = Calendar.getInstance();
	   calNow.add(Calendar.MINUTE,interval);    	
	
	   Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);		
	   PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
	
	   //Create an AlarmManager
	   AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	   
	   alarmManager.set(AlarmManager.RTC, calNow.getTimeInMillis(), pendingIntent);

	   alarmManager.setRepeating(AlarmManager.RTC, 
					calNow.getTimeInMillis(), 
					TimeUnit.MINUTES.toMillis(interval),
					pendingIntent);
			//Toast.makeText(this, "Alarm is set: " + calNow.getTime(), Toast.LENGTH_LONG).show();

	}
   
    /** Method called to reset the alarm*/
    private void resetAlarm(){
    	cancelAlarm();
    	setAlarm();
 	
    }
	
    /** Method called to cancel the alarm*/
	private void cancelAlarm(){

		//Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

	}
		
    	
}