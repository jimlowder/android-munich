package org.hackathon2010.duell;

import it.gerdavax.android.bluetooth.LocalBluetoothDevice;
import it.gerdavax.android.bluetooth.LocalBluetoothDeviceListener;
import it.gerdavax.android.bluetooth.RemoteBluetoothDevice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class LocalListener implements LocalBluetoothDeviceListener, Transmitter {
	private Controller controller;
	private LocalBluetoothDevice localBT;
	private List<Connection> connections = new ArrayList<Connection>();
	
	public LocalListener(Controller controller) throws Exception {
		this.controller = controller;
		localBT = LocalBluetoothDevice.initLocalDevice(controller.getActivity());
		localBT.setListener(this);
		localBT.setScanMode(LocalBluetoothDevice.SCAN_MODE_CONNECTABLE_DISCOVERABLE);
		localBT.setEnabled(true);
	}


	public void scanStarted() {
		try {
			Log.e(HighNoon.TAG, "scan mode: " + localBT.getScanMode());
		} catch (Exception e) {
			Log.e(HighNoon.TAG, "can not determine scan mode");
		}
	}
	
	public void scanCompleted(ArrayList<String> addrs) {
		Log.e(HighNoon.TAG, "Scan completed: " + addrs.size() + " devices");
		
		for (String addr : addrs) {
			RemoteBluetoothDevice remoteDevice = localBT.getRemoteBluetoothDevice(addr);
			if (remoteDevice.isPaired()) {
				Log.e(HighNoon.TAG, "hurray, it is paired: " + addr);
				try {
					Connection con = new Connection(controller, remoteDevice);
					connections.add(con);
				} catch (Exception e) {
					Log.e(HighNoon.TAG, "ERR-Connect: " + e);
				}
			} else {
				remoteDevice.setListener(new RemoteListener(remoteDevice, controller));
				remoteDevice.pair("0000");
				Log.e(HighNoon.TAG, "Paring initiated for: " + addr);
			}
		}
	}
	
	public void deviceFound(String deviceAddr) {
		Log.e(HighNoon.TAG, "device found: " + deviceAddr);
	}
	
	public void bluetoothEnabled() {
		Log.e(HighNoon.TAG, "bluetoothEnabled");
	}
	
	public void bluetoothDisabled() {
		Log.e(HighNoon.TAG, "bluetoothDisabled");
	}

	public void close() {
		for (Connection con : connections) {
			con.close();
		}
		if (localBT != null) {
			localBT.close();
			localBT = null;
		}
	}

	public void localGunDown() {
		for (Connection con : connections) {
			try {
				con.gunDown();
			} catch (IOException e) {
				Log.e(HighNoon.TAG, "sending gun down: " + e);
			}
		}
	}
		
	public void localGunUp() {
		for (Connection con : connections) {
			try {
				con.gunUp();
			} catch (IOException e) {
				Log.e(HighNoon.TAG, "sending gun up: " + e);
			}
		}
	}

	public void localGunIntermediate() {
		for (Connection con : connections) {
			try {
				con.gunIntermediate();
			} catch (IOException e) {
				Log.e(HighNoon.TAG, "sending gun intermediate: " + e);
			}
		}
	}
	
	public void localGunShot()  {
		for (Connection con : connections) {
			try {
				con.gunShot();
			} catch (IOException e) {
				Log.e(HighNoon.TAG, "sending gun shot: " + e);
			}
		}
	}


	public void connect() throws Exception {
		if (! localBT.isScanning()) {
			localBT.scan();
		}
	}

}
