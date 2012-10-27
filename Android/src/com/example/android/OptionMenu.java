/**This activity defines a menu with two buttons Exit and Home
 * most of the other activities are extensions of this activity */

package com.example.android;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class OptionMenu extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_menu);
    }

    /** Menu Creation*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_option_menu, menu);
        return true;
    }
    
    /** Method for Menu Selection*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	//When menu_stop is selected
            case R.id.menu_stop: 
            	//Say Goodbye
            	Toast.makeText(this, "Goodbye!", Toast.LENGTH_LONG).show();
            	//Leave the application
            	ExitApp();
            	return true;
            case R.id.menu_home:
            	//Go to HomePage
            	GoHome();
            	return true;
            default:
        	return super.onOptionsItemSelected(item);
        }	
    }
    
    /** Method for leaving the application*/
    private void ExitApp() {
    	Intent intent = new Intent(Intent.ACTION_MAIN);
    	intent.addCategory(Intent.CATEGORY_HOME);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(intent);
    }
    
    /** Method for going to HomePage */
    private void GoHome() {
    	Intent intent = new Intent(this, HomePage.class);
    	startActivity(intent);

    }
}
