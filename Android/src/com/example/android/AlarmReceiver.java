/**This class receives broadcast from the AlarmManager 
 * and answers by sending a notification.
 * Note that the notification is only send if the time is between
 * the start time and stop time from the user preferences*/

package com.example.android;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	/**
	 * a variable that allows us to retrieve the preferences of the user
	 */
	private SharedPreferences prefs;
	
	private static final String KEY_PREF_START = "pref_StartNotTime"; // Keyvalue for StartNotTime
	private static final String KEY_PREF_STOP = "pref_StopNotTime";// Keyvalue for StopNotTime
	
	/**Method called when a broadcast is received*/
	@Override
	public void onReceive(Context context, Intent arg1) {
		
		//Create a PreferenceManager to have access to the user preferences 
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		//Check if time is right according to user preferences 
		if (CheckTime(context)){
			//Send a notification
			buildNotification(context); }
	}
	
	/**Method to create a notification*/
	private void buildNotification(Context context){
		
		//Create a NotificationManager to help you create a notification
		NotificationManager notificationManager 
			= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	
		//Intent to start the MoodPage activity
		Intent intent = new Intent(context, MoodPage.class);
		
		PendingIntent pendingIntent 
			= PendingIntent.getActivity(context, 0, intent, 0);
		
		//Building a notification
		Notification notification = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_launcher) //icon that appears in left corner of screen
		.setContentTitle(context.getString(R.string.app_name))
		.setContentText(context.getString(R.string.notification_content))
		.setTicker(context.getString(R.string.notification_ticker)) //text that appears on top of the screen when notification is received
		.setDefaults(0xffffffff) // all notification properties will be inherited from system defaults
		.setContentIntent(pendingIntent) //action taken when notification is opened
		.setAutoCancel(true)
		.build();
		
		//Send notification
		notificationManager.notify(R.drawable.ic_launcher, notification);
	}
	
	/**Method to check whether time is in*/
	private boolean CheckTime(Context context){
		
		//Get user time preferences on start and stop time
		int StartHour = prefs.getInt(KEY_PREF_START + ".hour", 8);
  		int StartMinute = prefs.getInt(KEY_PREF_START + ".minute", 0);
		
  		int StopHour = prefs.getInt(KEY_PREF_STOP + ".hour", 8);
  		int StopMinute =prefs.getInt(KEY_PREF_STOP + ".minute", 0);
  		
		//Get a calendar set to the current time
  		Calendar calNow = Calendar.getInstance();
    	
  		// Get current time of the day (24h) and minutes
    	int HourNow = calNow.get(Calendar.HOUR_OF_DAY);
    	int MinuteNow = calNow.get(Calendar.MINUTE);
    	
    	//return true if time is between start and stop hour, otherwise return false
    	if (HourNow > StartHour && HourNow < StopHour){
    		return true;}
    	else if (HourNow == StartHour && MinuteNow >= StartMinute){
    		return true;}
    	else if (HourNow == StopHour && MinuteNow <= StopMinute){
    		return true;}
		else {
			return false;}
		
    	}
	}

