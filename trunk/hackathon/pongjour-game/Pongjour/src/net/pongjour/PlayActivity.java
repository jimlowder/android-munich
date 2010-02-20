package net.pongjour;

import net.pongjour.view.PongView;
import net.pongjour.view.PongView.PongThread;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PlayActivity extends Activity {

    private PongThread mPongThread;
    private PongView mPongView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.pong_playfield_layout);
        
        mPongView = (PongView) findViewById(R.id.pong_playfield);
        mPongThread = mPongView.getThread();
        
        mPongView.setTextView((TextView) findViewById(R.id.text));
        
        mPongThread.setState(PongThread.STATE_READY);
	}
}
