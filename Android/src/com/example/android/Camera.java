/**This activity allows the user to take a picture
 * and afterwards automatically goes to the next step the Audio activity.
 * The user enters this activity after clicking the OK button 
 * in the MoodPage activity.
 * The activity is extension of the OptionMenu activity which provides two buttons*/

package com.example.android;

import android.os.Bundle;
import android.provider.MediaStore;
import android.content.Intent;
import android.view.View;

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
              
                // Open new activity
                Intent intent = new Intent(this, Audio.class);
            	startActivity(intent);
            } 
            else if (resultCode == RESULT_CANCELED) { // User cancelled the image capture
            } 
            else { // Image capture failed, advise user
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
