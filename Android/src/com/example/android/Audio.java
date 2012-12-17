/**This activity allows the user to record 5 minutes
 * and afterwards automatically goes to the HomePage.
 * The user enters this activity after taking a picture
 * in the Camera activity.
 * The activity is extension of the OptionMenu activity which provides two buttons */

package com.example.android;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Audio extends OptionMenu {
	
	private static final String LogTag = "AudioCapture";
	
	/*
	 * De variabele met de recorder.
	 */
	private MediaRecorder myRecorder;
	
	/*
	 * De textView die de instructie voor de gebruiker weergeeft.
	 */
	private TextView instruction;
	
	/*
	 * De textView die de teller weergeeft.
	 */
	private TextView myCounter;
	
	/*
	 * De knop waarmee de gebruiker de opname kan starten.
	 */
	private Button startRec;
	
	/*
	 * De naam van de file waarin het geluid wordt opgenomen.
	 */
	private String fileName;
	
	/*
	 * De bitmap met de genomen foto om vervolgens na het opnemen door te geven aan AnalysisService voor
	 * de verdere verwerking.
	 */
	private Bitmap photo;
	
	/*
	 * De mood om vervolgens na het opnemen door te geven aan AnalysisService voor
	 * de verdere verwerking.
	 */
	private int mood;
	
	/*
	 * Een boolean om aan te geven of de gebruiker aan het opnemen is of niet.
	 */
	private boolean isRecording = false;
	
	/*
	 * De teller die het aantal seconden aftelt.
	 */
	private CountDownTimer timer;
	
	/*
	 * De totale tijd die opgenomen wordt.
	 */
	public static final int RecordedTime = 5000;
	
	public Audio(){
		// Het aanmaken van het pad waar de audio file opgeslagen wordt.
		fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName += "/EnvironmentTracker.3gp";
	}
	
	/**Method called when activity is created
	 * finds XML objects*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		// Get the information about the previous two steps of recoding to pass it to the service in the end.
        Bundle extras = getIntent().getExtras();
		photo = (Bitmap) extras.get("Photo");
		mood = extras.getInt("mood");

    }
    
    /**Method called when activity is paused
     * Releases of MediaRecorder and cancels the timer*/
    @Override
    public void onPause() {
        if (isRecording) {
        	timer.cancel();
        	myRecorder.release();
        } else if (myRecorder != null) {
        	myRecorder.release();
        }
        super.onPause();
    }
    
    /**
     * Methode die aangeroepen wordt wanneer de activity wanneer de activiteit opgestart of
     * heropgestart wordt.
     */
    public void onResume() {
    	super.onResume();
    	// De juiste layout wordt opgeroepen en geplaatst.
		setContentView(R.layout.activity_audio);
		
        // find XML objects
        myCounter = (TextView)findViewById(R.id.Counter);
        startRec = (Button)findViewById(R.id.RecordButton);
        instruction = (TextView)findViewById(R.id.SoundInstruction);
        // Als de gebruiker voor de onderbreking aan het opnemen was, wordt meteen een nieuwe
        // opname gestart. In het andere geval (oa bij de creatie) wordt de startknop getoond
        // om de opname te starten.
    	if (isRecording) {
    		startRecording(null);
    	}
    }
    
    /**
     * Deze methode wordt aangeroepen wanneer de gebruiker de oriëntatie van het scherm wijzigt.
     * (zie ook het androidmanifest)
     */
    @Override
    public void onConfigurationChanged (Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	
		// De juiste layout wordt opgeroepen en geplaatst.
		setContentView(R.layout.activity_audio);
	
		startRec = (Button)findViewById(R.id.RecordButton);
		instruction = (TextView)findViewById(R.id.SoundInstruction);
		myCounter = (TextView)findViewById(R.id.Counter);
		
    	// Als de gebruiker aan het opnemen was, blijft de opname lopen en wordt opnieuw de
		// teller weergegeven.
    	if (isRecording) {  
    		instruction.setVisibility(View.GONE);
	        startRec.setVisibility(View.GONE);
	    	myCounter.setVisibility(View.VISIBLE);
    	}
    }

    /**Method called when RecordButton is clicked*/    
    public void startRecording(View view) {
    	
    	//Setup of new MediaRecorder instance
        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setOutputFile(fileName);
        myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            myRecorder.prepare();
                
	        //change the layout of the activity
	        instruction.setVisibility(View.GONE);
	        startRec.setVisibility(View.GONE);
	    	myCounter.setVisibility(View.VISIBLE);
	    	
	    	//Count Down from 5 minutes to 0
	    	timer = new CountDownTimer(RecordedTime, 1000) {
	    		
	    		//Display every minute the time left to the end
	            public void onTick(long millisUntilFinished) {
	                myCounter.setText("" + millisUntilFinished / 1000);
	            }
	            
	            //When finished stop the recorder
	            public void onFinish() {
	                myCounter.setText(getString(R.string.counter_done));
	                stopRecording();
	            }
	    	}.start();
	        
	         // Start Recording
	         myRecorder.start();
	         // Zet isRecording op true, zodanig dat de hele activiteit weet dat de
	         // gebruiker aan het opnemen is.
	         this.isRecording = true;
	         // Vraag al een eerste keer de maximale amplitude. Bij de eerste aanroep wordt het startpunt
	         // voor het meten van de maximale amplitude bepaald.
	         myRecorder.getMaxAmplitude();
        } catch (IOException e) {
            Log.e(LogTag, "prepare() failed");
        }
    }

    /**Method called to stop recording 
     * and open the next activity HomePage*/
    private void stopRecording() {
    	if (isRecording) {
    		// Stop de opname
	        myRecorder.stop();
	        // Zet isRecording op false zodanig dat de hele activity weet dat de gebruiker niet meer
	        // aan het opnemen is.
	        this.isRecording = false;
	        
	        // Vraag de maximale amplitude op sinds de eerste keer dat deze functie werd aangeroepen (in
	        // startRecording)
	        int maxAmplitude = myRecorder.getMaxAmplitude();
	        
	        // Reset en release de mediarecorder.
	        myRecorder.reset();	        
	        myRecorder.release();
	        myRecorder = null;
	        
	        // Opstarten van analyse en verwerking van de gegevens
	        Intent intentPhoto = new Intent(this,AnalysisService.class);
	        // Het meegeven van de gegevens.
	    	intentPhoto.putExtra("Photo", photo);
	    	intentPhoto.putExtra("mood", mood);
	    	intentPhoto.putExtra("Amplitude", maxAmplitude);
	    	startService(intentPhoto);
	        
	    	// Keer terug naar de homepage.
	        Intent intent = new Intent(this, HomePage.class);
	        // Extra info meegeven naar de HomePage activiteit zodanig dat deze weet dat net
	        // geluid is opgenomen.
	        intent.putExtra("FromAudio", true);
	        startActivity(intent);
    	}
    }
}
