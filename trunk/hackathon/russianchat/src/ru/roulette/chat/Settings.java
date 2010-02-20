package ru.roulette.chat;

import java.io.IOException;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;

public class Settings extends Activity implements Callback {

	private static SurfaceView surfaceView;
	private static Camera camera;
	private static SurfaceHolder surfaceHolder;
	private static boolean previewRunning;

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
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (previewRunning) {
			camera.stopPreview();
		}
		Camera.Parameters p = camera.getParameters();
		p.setPreviewSize(width, height);
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
	
	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] imageData, Camera c) {
			Chat.setImage(imageData);
			Settings.this.finish();
		}
	};

}
