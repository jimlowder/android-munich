package de.ctg.catfur;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.util.Log;
import android.view.MotionEvent;

public class catfur extends Activity {
     
	private float catPationTime;  
    private float entryPetTime;
	private long lastPetTime;
	private float diffPetTime;
	private float countPet;
	private float countCrawl;
	private float downX, upX;
	private float downY, upY;
	private float diffX;
	private float diffY;
	private long upTime;
	private long crawlLimit = 1000; // Time in milliseconds 

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.main);    
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	int action = event.getAction();
    	float x = event.getX();
    	float y = event.getY();
    	
   	   	if (action==MotionEvent.ACTION_DOWN) {
        		downX=x;
        		downY=y;
        		Log.d("Cat", "Down: x,y="+downX+';'+downY);
        	}
           	if(action==MotionEvent.ACTION_UP) {
        		upX=x;
        		upY=y;
        		Log.d("Cat", "Up: x,y="+upX+';'+upY);
        		
           		float deltaX = downX - upX;
        		float deltaY = downY - upY;
        	
        		
        		if ((deltaX == 0.0)&&(deltaY==0.0)) {
        			Log.d("Cat", "Touch: dx,dy="+deltaX+';'+deltaY);
        			} else {
        				
        				long upTime = SystemClock.elapsedRealtime();
        				if ((upTime - lastPetTime) < crawlLimit) {
        					Log.d("Cat", "Crawl: dx,dy/lastPetTime="+deltaX+';'+deltaY+'/'+lastPetTime);
        				} else {
            				Log.d("Cat", "Pet: dx,dy/lastPetTime="+deltaX+';'+deltaY+'/'+lastPetTime);
            				lastPetTime = upTime;
            				
        					
        				}
        				
        			}
           	
    	}
    	
    	return super.onTouchEvent(event);
    }
}