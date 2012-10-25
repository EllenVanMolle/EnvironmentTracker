package com.example.android;

import java.util.Calendar;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.widget.TimePicker;
import android.widget.Toast;


public class SettingsPage extends OptionMenu implements OnSharedPreferenceChangeListener {
	
	private static final String KEY_PREF_NOT = "pref_Notification"; // KeyValue of Notifications
	private static final String KEY_PREF_START = "pref_StartNotTime";
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
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        prefs.registerOnSharedPreferenceChangeListener(this);
        
        
    }
    
    /** Method called when activity is Resumed*/
    @Override
    protected void onResume() {
        super.onResume();
        prefs.registerOnSharedPreferenceChangeListener(this);
    }
    
    /** Method called when activity is interrupted*/
    @Override
    protected void onPause() {
        super.onPause();
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }
    
    /** Method called when shared preference is changed*/
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key){
    	if (key.equals(KEY_PREF_NOT)){
    		// get current value of Pref_notification
    		boolean Return = prefs.getBoolean(key,false);
    		if (Return){  
    			Toast.makeText(this, "Enabled", Toast.LENGTH_LONG).show();
    			Alarm();
    		}
    		
    		else {Toast.makeText(this, "Disabled", Toast.LENGTH_LONG).show();
    		cancelAlarm();}
    	}
    }
    
    
       
    /** Method called when notification are enabled to set the alarm */
    private void Alarm(){
    	
    	int hour = prefs.getInt(KEY_PREF_START + ".hour",8);
    	int minute = prefs.getInt(KEY_PREF_START + ".minute", 0);
    	
    
    	Calendar calNow = Calendar.getInstance();
    	Calendar calSet = (Calendar) calNow.clone();
    	
    	calSet.set(Calendar.HOUR_OF_DAY, hour);
    	calSet.set(Calendar.MINUTE, minute);
    	calSet.set(Calendar.SECOND, 0);
    	calSet.set(Calendar.MILLISECOND, 0);
	
    	if(calSet.compareTo(calNow) <= 0){
		//Today Set time passed, count to tomorrow
		calSet.add(Calendar.DATE, 1);
	}
	
    	setAlarm(calSet);
    	
    	
    }
    
    private void setAlarm(Calendar targetCal){
		
		Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 
					targetCal.getTimeInMillis(), 
					TimeUnit.MINUTES.toMillis(5),
					pendingIntent);
			Toast.makeText(this, "Alarm is set@" + targetCal.getTime(), Toast.LENGTH_LONG).show();

	}
	
	private void cancelAlarm(){

		Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_LONG).show();
		
		Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);

	}
		
    	
}

