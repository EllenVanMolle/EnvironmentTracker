package com.example.android;

import java.io.IOException;

import com.example.android.analysis.SoundAnalyzer;
import com.example.android.database.DatabaseHandler;
import com.example.android.model.EnvironmentData;
import com.example.android.model.PictureData;
import com.example.android.model.SoundData;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * This activity allows the user to record 5 seconds and afterwards
 * automatically goes to the HomePage. The user enters this activity after
 * taking a picture in the Camera activity. The activity is extension of the
 * OptionMenu activity which provides two buttons
 * 
 * @author		Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 * @version		10 November 2012
 * 
 * @note http://developer.android.com/guide/topics/media/audio-capture.html
 */
public class Audio extends OptionMenu {

	// Constants :
	private static final String 	LogTag = "AudioCapture";
	
	// Page elements :
	private TextView 				Instruction;
	private TextView 				MyCounter;
	private Button 					StartRec;
	private MediaRecorder 			MyRecorder = null;
	// Name of the audio file.
	private String 					FileName;
	private DatabaseHandler 		handler;
	private long 					environmentID;
	// Parcels
	private EnvironmentData 		environmentData;
	private PictureData 			pictureData;
	private SoundData				soundData;
	
	/**
	 * Number of milliseconds of recording
	 */
	public static final int RecordedTime = 5000;

	/**
	 * Constructor for the <code>Audio</code> activity class.
	 */
	public Audio() {
		super();
		this.setFileName(Environment.getExternalStorageDirectory().getAbsolutePath());
	}
	
	/**
	 * @param path
	 */
	private void setFileName(String path) {
		FileName = path + "/EnvironmentTracker.3gp";
	}
	
	/**
	 * @return	the path and name of the audio file.
	 */
	public String getFileName() {
		return FileName;
	}

	/**
	 * Method called when activity is created finds XML objects
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);
		
		try {
        	Bundle b = this.getIntent().getExtras();
        	if(b != null) {
        		this.environmentData 	= b.getParcelable(Camera.KEY_PARCEL_ENV_DAT);
        		this.pictureData		= b.getParcelable(Camera.KEY_PARCEL_PIC_DAT);
        	}
        } catch(Exception e) {
        	// TODO : Error handling
        }

		// find XML objects
		MyCounter = (TextView) findViewById(R.id.Counter);
		StartRec = (Button) findViewById(R.id.RecordButton);
		Instruction = (TextView) findViewById(R.id.SoundInstruction);
		
		// Initialize database handler
		this.handler = new DatabaseHandler(this);
	}

	/**
	 * Method called when activity is paused Releases of MediaRecorder
	 */
	@Override
	public void onPause() {
		super.onPause();
		if (MyRecorder != null) {
			MyRecorder.release();
			MyRecorder = null;
		}
	}

	/**
	 * Method called when RecordButton is clicked
	 */
	public void startRecording(View view) {

		// Setup of new MediaRecorder instance
		MyRecorder = new MediaRecorder();
		MyRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		MyRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		MyRecorder.setOutputFile(getFileName());
		MyRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			MyRecorder.prepare();
			
			// change the layout of the activity
			Instruction.setVisibility(View.GONE);
			StartRec.setVisibility(View.GONE);
			MyCounter.setVisibility(View.VISIBLE);

			// Count Down from 5 seconds to 0
			new CountDownTimer(RecordedTime, 1000) {

				// Display every second the time left to the end
				public void onTick(long millisUntilFinished) {
					MyCounter.setText("" + millisUntilFinished / 1000);
				}

				// When finished stop the recorder
				public void onFinish() {
					MyCounter.setText("Done!");
					stopRecording();
				}
			}.start();

			// Start Recording
			MyRecorder.start();
		} catch (IOException e) {
			Log.e(LogTag, "prepare() failed: " + e.getLocalizedMessage());
		} finally {}
	}

	/**
	 * <p>
	 * Method called to stop recording,
	 * then start the sound analysis,
	 * and finally open the next activity HomePage.
	 * </p>
	 * <p>
	 * Insert collected data into database.
	 * </p>
	 */
	private void stopRecording() {
		try {
			// Stop recorder
			MyRecorder.stop();
			MyRecorder.release();
			MyRecorder = null;
			
			// Analyze result.
			SoundAnalyzer analyzer = new SoundAnalyzer(environmentID);
			this.soundData = analyzer.executeAnalysis();
			
			// Only write to the database once
			// At this point we are sure all the data is collected
			// otherwise, the user could stop the application half way
			// but we would have written data to the database already,
			// requiring a roll back.
			try {
				// Start transaction - write to database :
				environmentID = this.handler.getEnvironmentDataCRUD().insert(environmentData);
				pictureData.setEnvironmentID(environmentID);
				long picture_id = this.handler.getPictureDataCRUD().insert(pictureData);
				soundData.setEnvironmentID(environmentID);
				long sound_id = this.handler.getSoundDataCRUD().insert(soundData);
				
				// Update the bi-directional relation in environment data :
				this.environmentData.setPictureID(picture_id);
				this.environmentData.setSoundID(sound_id);
				this.handler.getEnvironmentDataCRUD().update(environmentData);
				
			} catch(Exception ex) {
				// Error messages.
				Log.e(LogTag, ex.getClass().getName() + " : " + ex.getLocalizedMessage());
			}
			
		} catch(Exception ex) {
			// Error messages.
			Log.e(LogTag, ex.getClass().getName() + " : " + ex.getLocalizedMessage());
		} finally {
			Intent intent = new Intent(this, HomePage.class);
			// Additional info send to HomePage activity
			intent.putExtra("FromAudio", true);
			startActivity(intent);
		}
	}
}
