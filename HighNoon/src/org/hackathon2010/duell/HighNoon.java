package org.hackathon2010.duell;

import it.gerdavax.android.bluetooth.BluetoothException;
import it.gerdavax.android.bluetooth.LocalBluetoothDevice;
import it.gerdavax.android.bluetooth.LocalBluetoothDeviceListener;
import it.gerdavax.android.bluetooth.RemoteBluetoothDevice;
import it.gerdavax.android.bluetooth.RemoteBluetoothDeviceListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HighNoon extends Activity {

	private SensorManager sensorManager;
	private Sensor accelerometer;
	private TextView anzeige;
	private Sound melody;
	private Sound vulture;
	private Sound bang;
	private LocalBluetoothDevice localBT;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		anzeige = (TextView) findViewById(R.id.anzeige);
		melody = new Sound(HighNoon.this, R.raw.melody);
		vulture = new Sound(HighNoon.this, R.raw.vulture);
		bang = new Sound(HighNoon.this, R.raw.bang);
		anzeige.append("now: " + new Date() + "\n");
		
		try {			
			localBT = LocalBluetoothDevice.initLocalDevice(this);
			localBT.setListener(new LocalBluetoothDeviceListener() {
				
				public void scanStarted() {
//					Toast.makeText(HighNoon.this, "Scan started", Toast.LENGTH_LONG).show();
				}
				
				public void scanCompleted(ArrayList<String> addrs) {
					display("Scan completed");
					
					for (String addr : addrs) {
						RemoteBluetoothDevice remoteDevice = localBT.getRemoteBluetoothDevice(addr);
						remoteDevice.setListener(new RemoteListener(remoteDevice, HighNoon.this));
						remoteDevice.pair("0000");							
					}

					display("Paring started");
				}
				
				public void deviceFound(String deviceAddr) {
				}
				
				public void bluetoothEnabled() {
//					Toast.makeText(HighNoon.this, "Enabled", Toast.LENGTH_SHORT).show();					
				}
				
				public void bluetoothDisabled() {
//					Toast.makeText(HighNoon.this, "Disabled", Toast.LENGTH_SHORT).show();
				}
			});
			localBT.setEnabled(true);
			localBT.scan();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    
		Button startBtn = (Button) findViewById(R.id.start);
		startBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				melody.start(true);
			}
		});

		Button stopBtn = (Button) findViewById(R.id.stop);
		stopBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				stopOrDestroy();
				melody.stop();
				HighNoon.this.finish();
			}
		});

		Button pengBtn = (Button) findViewById(R.id.peng);
		pengBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				bang.start(false);
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
	
	public void display(String s) {
		anzeige.append("\n");
		anzeige.append(s);
	}
}