package com.example.android;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Audio extends Activity {
	
	private static final String LogTag = "AudioCapture";
	
	private MediaRecorder MyRecorder = null;
	private TextView Instruction;
	private TextView MyCounter;
	private Button StartRec;
	
	private String FileName;
	
	public static final int RecordedTime = 10000;
	
	public Audio(){
		FileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileName += "/SoundRecord.3gp";
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        
        MyCounter = (TextView)findViewById(R.id.Counter);
        StartRec = (Button)findViewById(R.id.RecordButton);
        Instruction = (TextView)findViewById(R.id.SoundInstruction);
        
    }
    
    @Override
    public void onPause() {
        super.onPause();
        if (MyRecorder != null) {
            MyRecorder.release();
            MyRecorder = null;
        }
    }

        
    private void startRecording() {
    	
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
        
        Instruction.setVisibility(View.GONE);
        StartRec.setVisibility(View.GONE);
    	MyCounter.setVisibility(View.VISIBLE);
    	
    	new CountDownTimer(RecordedTime, 1000) {

            public void onTick(long millisUntilFinished) {
                MyCounter.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                MyCounter.setText("Done!");
                stopRecording();
            }
         }.start();
        
        MyRecorder.start();
        
        		
    }

    private void stopRecording() {
        MyRecorder.stop();
        MyRecorder.release();
        MyRecorder = null;
        Toast.makeText(this, "Sound Recorded", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, HomePage.class);     
        intent.putExtra("FromAudio", true);
        startActivity(intent);
    }

    public void startRecording (View view){
    	        
    	startRecording();
    	
    }
}
