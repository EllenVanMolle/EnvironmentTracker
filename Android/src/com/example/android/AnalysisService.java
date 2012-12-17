package com.example.android;

import java.util.Calendar;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.*;
import android.os.Bundle;
import android.util.Log;


/*
 * Deze klasse wordt aangeroepen om de foto en het geluid van de omgeving te analyseren en de bekomen resultaten
 * vervolgens op te slaan in de database. We gebruiken een IntentService zodanig dat deze bewerkingen asynchroon
 * in een aparte thread worden uitgevoerd. Dit is mogelijk aangezien er geen interactie met de gebruiker nodig 
 * is.
 */
public class AnalysisService extends IntentService {

	public AnalysisService() {
		super(AnalysisService.class.getName());
	}

	/* 
	 * Deze methode wordt uitgevoerd wanneer een andere activiteit of service de methode startService aanroept.
	 */
	@Override
	protected void onHandleIntent(Intent arg0) {
		// We halen de doorgegeven gegevens voor de analyse op.
		Bundle extras = arg0.getExtras();
		Bitmap bitmap = (Bitmap) extras.get("Photo");
		int mood = extras.getInt("mood");
		int amplitude = extras.getInt("Amplitude");
		
		// We analyseren de kleuren van de foto.
		int [] nrPixInHueCategory = new int[4];
		float totalSaturation = 0;
		float totalBrightness = 0;
		
		// We overlopen de verschillende pixels.
		for(int x=0; x < bitmap.getWidth();x++) {
			for (int y=0; y < bitmap.getHeight();y++) {
				// Voor elke pixel wordt de kleur opgevraagd.
				int colorOfThePixel = bitmap.getPixel(x, y);
				float[] hsv = new float[3];
				// Voor de kleur van de pixel worden de hsv-waarden teruggegeven in hsv.
				Color.colorToHSV(colorOfThePixel,hsv);
			     /*
			      * hsv[0] : Hue (0 .. 360) 
			      * hsv[1] : Saturation (0...1) 
			      * hsv[2] : Value (0...1)
			      */
				
				// Tel het aantal pixels voor elke hue categorie.
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
	         	// Bereken de totale saturatie			
	         	totalSaturation = totalSaturation + hsv[1];
	         	// Bereken de totale brightness			
	         	totalBrightness = totalBrightness + hsv[2];
			}
		}
		// Bereken het totale aantal pixels.
        int totalPixels = bitmap.getWidth() * bitmap.getHeight();
        
        // Bepalen welke hue categorie het meeste voorkomt.
        int maxNrOfPixels = 0;
        int hueClass = 0;
        for (int i=0; i<4;i++) {
        	if (maxNrOfPixels < nrPixInHueCategory[i]) {
        		maxNrOfPixels = nrPixInHueCategory[i];
        		hueClass = i+1;
        	}
        }
        // De gemiddelde saturation and brightness berekenen.
        float saturation = (totalSaturation / totalPixels)*100;
        float brightness = (totalBrightness / totalPixels)*100;
        
        // De amplitude omzetten naar decibels.
        double decibel;
        
        if (amplitude == 0) {
        	decibel = 0;
        } else {
        	decibel = 20 * Math.log10(amplitude/0.2);
        }
        
        // Opslaan van de gegevens in de database.
        // De database openen om gegevens weg te schrijven.
        EnvironmentTrackerOpenHelper databaseOpenHelper = new EnvironmentTrackerOpenHelper(getApplicationContext());
        SQLiteDatabase database = databaseOpenHelper.getWritableDatabase();
        
        // Wegschrijven van de gegevens zelf.
        ContentValues values = new ContentValues(5);
        values.put("MOOD", mood);
        values.put("DAY", Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        values.put("HUE_CATEGORY", hueClass);
        values.put("SATURATION", saturation);
        values.put("BRIGHTNESS", brightness);
        values.put("DECIBEL", decibel);
        database.insert("Observation", null, values);
        
        database.releaseReference();
	}
	
}
