package de.ctg.catfur;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

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
	private boolean isStartup = true;
	

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
        			MediaPlayer player = MediaPlayer.create(this, R.raw.catmeow);
        			player.start();
        			if (isStartup){
        				isStartup = false;
        				ImageView imageView = (ImageView) findViewById(R.id.ImageView01);
        				imageView.setImageResource(R.drawable.catsfur);
        			}
        			
        			} else {
        				
        				long upTime = SystemClock.elapsedRealtime();
        				if ((upTime - lastPetTime) < crawlLimit) {
        					Log.d("Cat", "Crawl: dx,dy/lastPetTime="+deltaX+';'+deltaY+'/'+lastPetTime);
                			MediaPlayer player = MediaPlayer.create(this, R.raw.catpurr2);
                			player.start();
        				} else {
                			MediaPlayer player = MediaPlayer.create(this, R.raw.catmeow);
                			player.start();
                			player.start();
            				Log.d("Cat", "Pet: dx,dy/lastPetTime="+deltaX+';'+deltaY+'/'+lastPetTime);
            				lastPetTime = upTime;
            				
        					
        				}
        				
        			}
           	
    	}
    	
    	return super.onTouchEvent(event);
    }
}