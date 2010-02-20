package ru.roulette.chat;

import java.io.IOException;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.widget.Button;

public class Settings extends Activity implements Callback {

	private static SurfaceView surfaceView;
	private static Camera camera;
	private static SurfaceHolder surfaceHolder;
	private static boolean previewRunning;
	private static Button takePicture;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.settings);
		surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		takePicture = (Button) findViewById(R.id.takePicture);
		takePicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				camera.takePicture(shutterCallback, rawCallback, pictureCallback);
				
			}
		});
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (previewRunning) {
			camera.stopPreview();
		}
		Camera.Parameters p = camera.getParameters();
		p.setPreviewSize(200, 200);
		p.setPictureSize(200, 200);
		camera.setParameters(p);
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		camera.startPreview();
		previewRunning = true;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		previewRunning = false;
		camera.release();
	}
	
	Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] imageData, Camera c) {
			Log.i("Settings", "onPictureTaken() called");
			Chat.setImage(imageData);
			Settings.this.finish();
		}
	};
	
	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			Log.d("Settings", "onShutter'd");
		}
	};

	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d("Settings", "onPictureTaken - raw");
		}
	};

}
