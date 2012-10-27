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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Audio extends OptionMenu {
	
	private static final String LogTag = "AudioCapture";
	
	private MediaRecorder MyRecorder = null;
	private TextView Instruction;
	private TextView MyCounter;
	private Button StartRec;
	
	private String FileName;
	
	public static final int RecordedTime = 5000; // Number of minutes of recording
	
	
	public Audio(){
		FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileName += "/EnvironmentTracker.3gp";
	}
	
	/**Method called when activity is created
	 * finds XML objects*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        
        // find XML objects
        MyCounter = (TextView)findViewById(R.id.Counter);
        StartRec = (Button)findViewById(R.id.RecordButton);
        Instruction = (TextView)findViewById(R.id.SoundInstruction);  
    }
    
    /**Method called when activity is paused
     * Releases of MediaRecorder*/
    @Override
    public void onPause() {
        super.onPause();
        if (MyRecorder != null) {
            MyRecorder.release();
            MyRecorder = null;
        }
    }

    /**Method called when RecordButton is clicked*/    
    public void startRecording(View view) {
    	
    	//Setup of new MediaRecorder instance
        MyRecorder = new MediaRecorder();
        MyRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        MyRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        MyRecorder.setOutputFile(FileName);
   
        MyRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            MyRecorder.prepare();
        } catch (IOException e) {
            Log.e(LogTag, "prepare() failed");
        }
        
        //change the layout of the activity
        Instruction.setVisibility(View.GONE);
        StartRec.setVisibility(View.GONE);
    	MyCounter.setVisibility(View.VISIBLE);
    	
    	//Count Down from 5 minutes to 0
    	new CountDownTimer(RecordedTime, 1000) {
    		
    		//Display every minute the time left to the end
            public void onTick(long millisUntilFinished) {
                MyCounter.setText("" + millisUntilFinished / 1000);
            }
            
            //When finished stop the recorder
            public void onFinish() {
                MyCounter.setText("Done!");
                stopRecording();
            }
         }.start();
        
        //Start Recording
        MyRecorder.start();
        
        		
    }

    /**Method called to stop recording 
     * and open the next activity HomePage*/
    private void stopRecording() {
        MyRecorder.stop();
        MyRecorder.release();
        MyRecorder = null;
        
        Intent intent = new Intent(this, HomePage.class);     
        intent.putExtra("FromAudio", true); //Additional info send to HomePage activity
        startActivity(intent);
    }
}
