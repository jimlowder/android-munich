package org.hackathon2010.duell;

import it.gerdavax.android.bluetooth.BluetoothException;
import it.gerdavax.android.bluetooth.RemoteBluetoothDevice;
import it.gerdavax.android.bluetooth.RemoteBluetoothDeviceListener;
import android.widget.Toast;

public class RemoteListener implements RemoteBluetoothDeviceListener {
	private RemoteBluetoothDevice remoteDevice;
	private HighNoon activity;
	
	public RemoteListener(RemoteBluetoothDevice remoteDevice, HighNoon activity) {
		this.remoteDevice = remoteDevice;
		this.activity = activity;
	}

	public void gotServiceChannel(int arg0, int arg1) {
		alert("gotServiceChannel/" + arg0 + "/" + arg1);
	}

	public void paired() {
		alert("paired");
	}

	public void pinRequested() {
		alert("pin requested");
	}

	public void serviceChannelNotAvailable(int arg0) {
		alert("serviceChannelNotAvailable/" + arg0);
	}

	private void alert(String txt) {
//		activity.display(txt);
		
		String deviceName;
		try {
			deviceName = remoteDevice.getName();
		} catch (BluetoothException e) {
			deviceName = remoteDevice.getAddress();
		}
		Toast.makeText(activity, txt + ": " + deviceName, Toast.LENGTH_LONG).show();
	}
}
