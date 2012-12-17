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
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Audio extends OptionMenu {
	
	private static final String LogTag = "AudioCapture";
	
	private MediaRecorder myRecorder;
	private TextView instruction;
	private TextView myCounter;
	private Button startRec;
	
	private String fileName;
	
	private Bitmap photo;
	private int mood;
	
	private boolean isRecording = false;;
	private int secondsTillStopRecording;
	private CountDownTimer timer;
	
	public static final int RecordedTime = 5000; // Number of milliseconds of recording
	
	public Audio(){
		fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        fileName += "/EnvironmentTracker.3gp";
	}
	
	/**Method called when activity is created
	 * finds XML objects*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        
		// Get the information about the previous two steps of recoding to pass it to the service in the end.
        Bundle extras = getIntent().getExtras();
		photo = (Bitmap) extras.get("Photo");
		mood = extras.getInt("mood");
        
        // find XML objects
        myCounter = (TextView)findViewById(R.id.Counter);
        startRec = (Button)findViewById(R.id.RecordButton);
        instruction = (TextView)findViewById(R.id.SoundInstruction);  
    }
    
    /**Method called when activity is paused
     * Releases of MediaRecorder*/
    @Override
    public void onPause() {
        if (isRecording) {
        	secondsTillStopRecording = Integer.parseInt(myCounter.getText().toString());
        	timer.cancel();
        	myRecorder.release();
        	myCounter.setText(getString(R.string.counter_paused));
        } else if (myRecorder != null) {
        	myRecorder.release();
        }
        super.onPause();
    }
    
    public void onResume() {
    	super.onResume();
    	if (isRecording) {
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
			    	timer = new CountDownTimer(secondsTillStopRecording * 1000, 1000) {
			    		
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
			        
			         //Start Recording
			         myRecorder.start();
			         myRecorder.getMaxAmplitude();
		        } catch (IOException e) {
		            Log.e(LogTag, "prepare() failed");
		        }
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
	        
	         //Start Recording
	         myRecorder.start();
	         this.isRecording = true;
	         myRecorder.getMaxAmplitude();
        } catch (IOException e) {
            Log.e(LogTag, "prepare() failed");
        }
    }

    /**Method called to stop recording 
     * and open the next activity HomePage*/
    private void stopRecording() {
    	if (isRecording) {
	        myRecorder.stop();
	        this.isRecording = false;
	        
	        int maxAmplitude = myRecorder.getMaxAmplitude();
	        
	        myRecorder.reset();	        
	        myRecorder.release();
	        myRecorder = null;
	        
	        // Opstarten van analyse en verwerking van de gegevens
	        
	        Intent intentPhoto = new Intent(this,AnalysisService.class);
	    	intentPhoto.putExtra("Photo", photo);
	    	intentPhoto.putExtra("mood", mood);
	    	intentPhoto.putExtra("Amplitude", maxAmplitude);
	    	startService(intentPhoto);
	        
	        Intent intent = new Intent(this, HomePage.class);     
	        intent.putExtra("FromAudio", true); //Additional info send to HomePage activity
	        startActivity(intent);
    	}
    }
}
