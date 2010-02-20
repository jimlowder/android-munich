package org.hackathon2010.duell;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.util.Log;

public class Sound {
	private Activity activity;
	private int resourceId;
	private MediaPlayer mediaPlayer;
	
    public Sound(Activity activity, int resourceId) {
		this.activity = activity;
		this.resourceId = resourceId;
	}

	public void start(final boolean forEver) {
		mediaPlayer = MediaPlayer.create(activity, resourceId);
		
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				if (forEver) {
					mp.start();
				} else {
					Log.i("Verification", "...player complete, releasing");
					mp.release();
					mediaPlayer = null;
				}
			}
        });

        mediaPlayer.setOnErrorListener(new OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Log.e("Verification", "Error during media play: " + what + "/" + extra);
				return false;
			}
        });
        
        Log.i("Verification", "player starting... " + String.format("0x%x", resourceId));
        mediaPlayer.start();
	}

	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}
