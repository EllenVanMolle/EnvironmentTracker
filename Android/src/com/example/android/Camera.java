package com.example.android;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

public class Camera extends Activity {
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved
                Toast.makeText(this, "Picture Taken", Toast.LENGTH_LONG).show();
                // Open new activity
                Intent intent = new Intent(this, Audio.class);
            	startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        } 
    }
    
    //Methode opgeroepen als gebruiker klikt op de OK button
    public void startCamera (View view){
    	
    	/**Camera makes a request to capture a picture through an existing camera app 
         * and then returns control back to your application. */
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        
        /**Start image capture intent*/
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
       
        }
}
