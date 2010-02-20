package org.hackathon2010.duell;

import it.gerdavax.android.bluetooth.LocalBluetoothDevice;
import it.gerdavax.android.bluetooth.BluetoothDevice.BluetoothClasses;

import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HighNoon extends Activity {

	private SensorManager sensorManager;
	private Sensor accelerometer;
	private TextView anzeige;
	private LocalBluetoothDevice localBT;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		anzeige = (TextView) findViewById(R.id.anzeige);

		Button startBtn = (Button) findViewById(R.id.start);
		startBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// sensorManager.registerListener(sensorListener, accelerometer,
				// SensorManager.SENSOR_DELAY_FASTEST);
			}
		});

		Button stopBtn = (Button) findViewById(R.id.stop);
		stopBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//
			}
		});

		Button pengBtn = (Button) findViewById(R.id.peng);
		pengBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//
			}
		});

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// accelerometer =
		// sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		List<Sensor> allSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
		for (Sensor s : allSensors) {
			String sensorName = s.getName();
			anzeige.append(sensorName + "\n");
		}
		anzeige.append("--- das waren die Sensoren ---");

		// anzeige.setText("");
		try {
			localBT = LocalBluetoothDevice.initLocalDevice(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		stopOrDestroy();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		stopOrDestroy();
		super.onDestroy();
	}

	private void stopOrDestroy() {
		if (localBT != null) {
			localBT.close();
		}
	}
}