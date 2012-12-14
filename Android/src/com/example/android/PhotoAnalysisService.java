package com.example.android;

import java.util.Calendar;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.*;
import android.os.Bundle;
import android.util.Log;

public class PhotoAnalysisService extends IntentService {

	public PhotoAnalysisService() {
		super(PhotoAnalysisService.class.getName());
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// Analyseren van de foto
		Bundle extras = arg0.getExtras();
		Bitmap bitmap = (Bitmap) extras.get("Photo");
		int mood = extras.getInt("mood");
		int amplitude = extras.getInt("Amplitude");
		Log.d("Mood in service", Integer.toString(mood));
		Log.d("Amplitude in service", Integer.toString(amplitude));
		
		int [] nrPixInHueCategory = new int[4];
		float totalSaturation = 0;
		float totalBrightness = 0;
		
		for(int x=0; x < bitmap.getWidth();x++) {
			for (int y=0; y < bitmap.getHeight();y++) {
				int colorOfThePixel = bitmap.getPixel(x, y);
				float[] hsv = new float[3];
				Color.colorToHSV(colorOfThePixel,hsv);
			     /*
			      * hsv[0] : Hue (0 .. 360) 
			      * hsv[1] : Saturation (0...1) 
			      * hsv[2] : Value (0...1)
			      */
				
				if (hsv[0] <= 90) {
	         		nrPixInHueCategory[0] = nrPixInHueCategory[0] + 1;
				} else if (hsv[0] <= 180) {
	         		nrPixInHueCategory[1] = nrPixInHueCategory[1] + 1;
				} else if (hsv[0] <= 270) {
	         		nrPixInHueCategory[2] = nrPixInHueCategory[2] + 1;
				} else if (hsv[0] <= 360) {
	         		nrPixInHueCategory[3] = nrPixInHueCategory[3] + 1;
				} else {
	         		Log.e("Hue","Hue is incorrect.");
	         	}
	         				
	         	totalSaturation = totalSaturation + hsv[1];
	         				
	         	totalBrightness = totalBrightness + hsv[2];
			}
		}
		
        int totalPixels = bitmap.getWidth() * bitmap.getHeight();
        
        Log.d("NrPixelsHue1", Integer.toString(nrPixInHueCategory[0]));
        Log.d("NrPixelsHue2", Integer.toString(nrPixInHueCategory[1]));
        Log.d("NrPixelsHue3", Integer.toString(nrPixInHueCategory[2]));
        Log.d("NrPixelsHue4", Integer.toString(nrPixInHueCategory[3]));
        
        // Bepalen welke categorie het meeste voorkomt.
        int maxNrOfPixels = 0;
        int hueClass = 0;
        for (int i=0; i<4;i++) {
        	if (maxNrOfPixels < nrPixInHueCategory[i]) {
        		maxNrOfPixels = nrPixInHueCategory[i];
        		hueClass = i+1;
        	}
        }
         	
        float saturation = (totalSaturation / totalPixels)*100;
        float brightness = (totalBrightness / totalPixels)*100;
        Log.d("HueClass", Integer.toString(hueClass));
        Log.d("Saturation", Float.toString(saturation));
        Log.d("Brightness", Float.toString(brightness));
        
        double decibel;
        amplitude = 500;
        
        // Calculate the decibels
        if (amplitude == 0) {
        	decibel = 0;
        } else {
        	decibel = 20 * Math.log10(amplitude/0.2);
        }
        
        // Opslaan van de gegevens in de database
        EnvironmentTrackerOpenHelper databaseOpenHelper = new EnvironmentTrackerOpenHelper(getApplicationContext());
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues(5);
        values.put("MOOD", mood);
        values.put("DAY", Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        values.put("HUE_CATEGORY", hueClass);
        values.put("SATURATION", saturation);
        values.put("BRIGHTNESS", brightness);
        values.put("DECIBEL", decibel);
        int row = (int) database.insert("Observation", null, values);
        Log.d("ROW", Integer.toString(row));
        
        database.releaseReference();
	}
	

}
