/**This activity allows the user to take a picture
 * and afterwards automatically goes to the next step the Audio activity.
 * The user enters this activity after clicking the OK button 
 * in the MoodPage activity.
 * The activity is extension of the OptionMenu activity which provides two buttons*/

package com.example.android;

import android.os.Bundle;
import android.provider.MediaStore;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

public class Camera extends OptionMenu  {
	
	private static final int CAPTURE_IMAGE_ACTIVITY_RQ = 100;
	
	/**Method called when activity is created*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_RQ) {
            if (resultCode == RESULT_OK) {// Image captured and saved
            	
            	Bundle extras = data.getExtras();
            	Bitmap photo = (Bitmap) extras.get("data");
            	int mood = this.getIntent().getIntExtra("mood", -1);
            	
            	if (mood == -1) {
            		Toast.makeText(this, R.string.alert_dialog_error_message_camera, Toast.LENGTH_LONG).show();
            		mood = 5;
            	}
     
              
                // Open new activity and give the mood and the photo
                Intent intent = new Intent(this, Audio.class);
                intent.putExtra("Photo", photo);
                intent.putExtra("mood", mood);
            	startActivity(intent);
            } 
            else if (resultCode == RESULT_CANCELED) { 
            	// User cancelled the image capture
            	Toast.makeText(this, R.string.alert_user_cancelled_camera, Toast.LENGTH_LONG).show();
            } 
            else { 
            	// Image capture failed, advise user
            	Toast.makeText(this, R.string.alert_camera_failed, Toast.LENGTH_LONG).show();
            }
        } 
    }
    
    /**Method called when OK button is clicked*/
    public void startCamera (View view){
    	
    	//Camera makes a request to capture a picture through an existing camera app 
        //and then returns control back to your application. 
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        
        //Start image capture intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_RQ);
       
        }
}
