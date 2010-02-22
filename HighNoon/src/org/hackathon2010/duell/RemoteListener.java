package org.hackathon2010.duell;

import android.util.Log;
import it.gerdavax.android.bluetooth.BluetoothException;
import it.gerdavax.android.bluetooth.RemoteBluetoothDevice;
import it.gerdavax.android.bluetooth.RemoteBluetoothDeviceListener;

public class RemoteListener implements RemoteBluetoothDeviceListener {
	private RemoteBluetoothDevice remoteDevice;
	 private Controller controller;
	
	public RemoteListener(RemoteBluetoothDevice remoteDevice, Controller controller) {
		this.remoteDevice = remoteDevice;
		this.controller = controller;
	}

	public void gotServiceChannel(int arg0, int arg1) {
		alert("RL:gotServiceChannel/" + arg0 + "/" + arg1);
	}

	public void paired() {
		alert("RL:paired");
	}

	public void pinRequested() {
		alert("RL:pin requested");
	}

	public void serviceChannelNotAvailable(int arg0) {
		alert("RL:serviceChannelNotAvailable/" + arg0);
	}

	private void alert(String txt) {		
		String deviceName;
		try {
			deviceName = remoteDevice.getName();
		} catch (BluetoothException e) {
			deviceName = "???-" + remoteDevice.getAddress();
		}
		final String msg = txt + ": " + deviceName;
		
		Log.i(HighNoon.TAG, msg);
	}
}
