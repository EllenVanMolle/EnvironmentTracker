package com.example.android;

import com.example.android.analysis.ImageAnalyzer;
import com.example.android.model.EnvironmentData;
import com.example.android.model.PictureData;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

/**
 * <p>
 * This activity allows the user to take a picture and afterwards
 * automatically goes to the next step the Audio activity.
 * The user enters this activity after clicking the OK button 
 * in the MoodPage activity. The activity is extension of the
 * OptionMenu activity which provides two buttons.
 * </p>
 * 
 * The Camera class is an Activity.
 * @author		Liselot De Brabandere, Joris Schelfaut, Ellen Van Molle
 * @version		9 November 2012
 */
public class Camera extends OptionMenu  {
	
	//private static final int CAPTURE_IMAGE_ACTIVITY_RQ = 100;
	private static final int 	CAMERA_REQUEST = 1888;
	private ImageAnalyzer 		imageAnalyzer;
	private int					environmentID;
	private PictureData			pictureData;
	private EnvironmentData		environmentData;
	public  static final String	KEY_PARCEL_PIC_DAT = PictureData.class.getName();
	public  static final String	KEY_PARCEL_ENV_DAT = EnvironmentData.class.getName();
	
	/**
	 * Constructor for the Camera class.
	 */
	public Camera() {
		super();
	}
	
	/**
	 * Method called when activity is created
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_camera);
        // Get parcel from MoodPage
        // TODO : will it matter from which page the user came from?
        try {
        	Bundle b = this.getIntent().getExtras();
        	if(b != null) {
        		this.environmentData = b.getParcelable(MoodPage.KEY_PARCEL);
        	}
        } catch(Exception e) {
        	// Error handling
        }
    }
    
    /**
     * Take a picture and afterwards go automatically 
     * to the next step: the Audio activity.
     * 
     * @note	http://stackoverflow.com/questions/2314958/using-the-camera-activity-in-android
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (requestCode == CAPTURE_IMAGE_ACTIVITY_RQ) {
    	if (requestCode == CAMERA_REQUEST) {
    		// Image captured and saved
            if (resultCode == Activity.RESULT_OK) {
            	try {
            		Bitmap image = (Bitmap) data.getExtras().get("data");
            		imageAnalyzer = new ImageAnalyzer(image, this.environmentID);
            		this.pictureData = imageAnalyzer.executeAnalysis();
            	} catch (Exception exception) {
            		// Show error message.
            	} finally {
            		// Create new intent :
	                Intent intent = new Intent(this, Audio.class);
            		try {
            			// Create a bundle and Parcelable to pass to the next activity :
            			Bundle bundle = new Bundle();
            			bundle.putParcelable(Camera.KEY_PARCEL_PIC_DAT, this.pictureData);
            			bundle.putParcelable(Camera.KEY_PARCEL_ENV_DAT, this.environmentData);
            			// Add bundle to intent
            			intent.putExtras(bundle);
            		} catch (Exception e) {
            			// TODO : Error handling
            		} finally {
            			// Open Next activity (Camera Activity) :
            			startActivity(intent);
            		}
            	}
            }  else if (resultCode == RESULT_CANCELED) {
            	// User cancelled the image capture
            }  else {
            	// Image capture failed, advise user
            }
        } 
    }

	/**
	 * <p>
     * Method called when OK button is clicked. Camera makes a
     * request to capture a picture through an existing
     * camera app and then returns control back to your application.
     * Then it starts with image capture intent.
     * </p>
     * 
     * @note	http://stackoverflow.com/questions/5991319/capture-image-from-camera-and-display-in-activity 
     */
    public void startCamera (View view){
    	// Camera makes a request to capture a picture through an existing camera app 
        // and then returns control back to your application.
    	Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	// Start image capture intent
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
        // startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_RQ);
    }
}
