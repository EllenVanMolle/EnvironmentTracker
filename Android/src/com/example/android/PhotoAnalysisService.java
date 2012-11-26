package com.example.android;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.util.Log;

public class PhotoAnalysisService extends IntentService {

	public PhotoAnalysisService() {
		super(PhotoAnalysisService.class.getName());
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		Bundle extras = arg0.getExtras();
		Bitmap bitmap = (Bitmap) extras.getParcelable(CLIPBOARD_SERVICE);
		
		for(int x=0; x < bitmap.getHeight();x++) {
			for (int y=0; y < bitmap.getWidth();y++) {
				int colorOfThePixel = bitmap.getPixel(x, y);
				float[] hsv = null;
				Color.colorToHSV(colorOfThePixel,hsv);
				Log.d("TAG", "Na hsv");
			}
		}
	}
	

}
