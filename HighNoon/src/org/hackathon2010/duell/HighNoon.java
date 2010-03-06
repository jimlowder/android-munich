package org.hackathon2010.duell;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class HighNoon extends Activity {

	public static final String TAG = "HiNo";
	private static boolean mock = true;
	
	private SensorManager sensorManager;
	private Sensor orientationSensor;
	protected Sound flute;
	protected Sound howl;
	protected Sound bang;
	protected Sound background;
	private Controller controller;
	protected Handler handler = new Handler();
	Button restartBtn;
	Button bangBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.w(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		final Window win = getWindow(); 
		final int screenHeight = win.getWindowManager().getDefaultDisplay().getHeight(); 
		final int screenWidth = win.getWindowManager().getDefaultDisplay().getWidth(); 
		win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		String s = "W: " + screenWidth + " / Height: " + screenHeight;
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
		
		if (mock)
			setContentView(R.layout.mock);
		else
			setContentView(R.layout.main);
		
		initSounds();
		initUI();
		if (mock) initMockUI();

		controller = new Controller(this);
		try {
			controller.setTransmitter(createTransmitter());
		} catch (Exception e) {
			finish();
			return;
		}

		controller.reset();
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(new OrientationListener(controller), orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);

		Log.d(TAG, "...done onCreate");
	}

	private Transmitter createTransmitter() throws Exception {
		if (mock)
			return new Transmitter() {
				public void localGunUp() {}
				public void localGunShot() {}
				public void localGunIntermediate() {}
				public void localGunDown() {}
				public void connect() throws Exception {}
				public void close() {}
			};
		else
			return new LocalListener(controller);		
	}
	
	private void initUI() {
		restartBtn = (Button) findViewById(R.id.restart);
		restartBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// TODO
			}
		});

		Button quitBtn = (Button) findViewById(R.id.quit);
		quitBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				shutdownTransmitter();
				HighNoon.this.finish();
			}
		});

		bangBtn = (Button) findViewById(R.id.bang);
		bangBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				bang.start(false);
				controller.localGunShot();
			}
		});
		
        // quitBtn.getRootView().setBackgroundResource(R.drawable.maze);
		setBackgroundResource(R.drawable.wait_for_opponent);
	}
	
	public void setBackgroundResource(int resid) {
		getWindow().getDecorView().setBackgroundResource(resid);
	}
	
	private void initMockUI() {
		Button remoteGunDownBtn = (Button) findViewById(R.id.remoteGunDown);
		remoteGunDownBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				controller.remoteGunDown();
			}
		});

		Button remoteGunUpBtn = (Button) findViewById(R.id.remoteGunUp);
		remoteGunUpBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				controller.remoteGunUp();
			}
		});

		Button remoteGunIntermediateBtn = (Button) findViewById(R.id.remoteGunIntermediate);
		remoteGunIntermediateBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				controller.remoteGunIntermediate();
			}
		});

		Button remoteBangBtn = (Button) findViewById(R.id.remoteBang);
		remoteBangBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				bang.start(false);
				controller.remoteGunShot();
			}
		});
	}


	private void initSounds() {
		flute = new Sound(this, R.raw.halunken);
		howl = new Sound(this, R.raw.howl);
		bang = new Sound(this, R.raw.bang);
		background = new Sound(this, R.raw.onceuponatime);		
	}

	
	@Override
	protected void onPause() {
		background.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.w(TAG, "onResume");
		controller.reset();
		try {
			controller.connect();
		} catch (Exception e) {
			Log.d(TAG, "Exception in localBT: " + e);
		}
		
		super.onResume();
	}

	@Override
	protected void onRestart() {
		Log.w(TAG, "onRestart");
		super.onRestart();
		Log.d(TAG, "...done onRestart");
	}


	@Override
	protected void onStart() {
		Log.w(TAG, "onStart");
		super.onStart();
		Log.d(TAG, "...done onStart");
	}

	@Override
	protected void onStop() {
		Log.w(TAG, "onStop");
		shutdownTransmitter();
		shutUp();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.w(TAG, "onDestroy");
		shutdownTransmitter();
		shutUp();
		super.onDestroy();
	}

	private void shutdownTransmitter() {
		if (controller != null) {
			controller.close();
			controller = null;
		}
	}

	public void shutUp() {
		flute.stop();
		howl.stop();
		bang.stop();
		background.stop();
	}
	
//	public void Log.d(TAG, final String s) {
//		runOnUiThread(new Runnable() {
//			public void run() {
//				long t = System.currentTimeMillis() - creationTime;
//				anzeige.append("\n" + t + ": ");
//				anzeige.append(s);
//			}
//		});
//	}
//
//
//	public void showOrientation(final String s) {
//		runOnUiThread(new Runnable() {
//			public void run() {
//				singleLine.setText(s);
//			}
//		});
//	}	
}