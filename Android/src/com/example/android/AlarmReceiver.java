package com.example.android;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();
		buildNotification(context);
		
	}
	
	private void buildNotification(Context context){
		NotificationManager notificationManager 
		= (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	
		
		Intent intent = new Intent(context, MoodPage.class);
		PendingIntent pendingIntent 
		= PendingIntent.getActivity(context, 0, intent, 0);
		
		Notification notification = new NotificationCompat.Builder(context)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("Environment Tracker")
		.setContentText("Time to collect some data!")
		.setTicker("Click Me!")
		.setDefaults(0xffffffff) // all notification properties will be inherited from system defaults
		.setContentIntent(pendingIntent)
		.setAutoCancel(true)
		.build();
		
		notificationManager.notify(R.drawable.ic_launcher, notification);
	}

}

