package org.hackathon2010.duell;

import it.gerdavax.android.bluetooth.BluetoothSocket;
import it.gerdavax.android.bluetooth.RemoteBluetoothDevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.util.Log;

public class Connection {
	final Controller controller;
	BluetoothSocket socket;
	InputStream input;
	OutputStream output;
	BufferedReader br;
	Thread readerThread;
	
	public Connection(final Controller controller, RemoteBluetoothDevice remoteDevice) throws Exception  {
		this.controller = controller;
		socket = remoteDevice.openSocket(1);
		input = socket.getInputStream();
		output = socket.getOutputStream();
		br = new BufferedReader(new InputStreamReader(input));
		
		readerThread = new Thread(new Runnable() {
			public void run() {
				while (! Thread.interrupted()) {
					try {
						String line = br.readLine();
						
						if (line.equals(GUN_DOWN))
							controller.remoteGunDown();
						else if (line.equals(GUN_UP))
							controller.remoteGunUp();
						else if (line.equals(GUN_X))
							controller.remoteGunIntermediate();
						else if (line.equals(GUN_SHOT))
							controller.remoteGunShot();
						else if (line.equals(SHOT_BY_SHERIF))
							controller.remoteShotBySherif();
						
						// localListener.display("READ: " + line);
					} catch (IOException e) {
						Log.e(HighNoon.TAG, "ERROR reading from connection: " + e);
					}
				}
			}
		});
		readerThread.start();
	}
	
	public void close() {
		readerThread.interrupt();
		socket.closeSocket();
	}
	
	private static final String GUN_DOWN = "GUN_DOWN";
	private static final byte[] GUN_DOWN_BUFFER = (GUN_DOWN + "\n").getBytes();
	public void gunDown() throws IOException {
		output.write(GUN_DOWN_BUFFER);
	}

	private static final String GUN_UP = "GUN_UP";
	private static final byte[] GUN_UP_BUFFER = (GUN_UP + "\n").getBytes();
	public void gunUp() throws IOException {
		output.write(GUN_UP_BUFFER);
	}

	private static final String GUN_X = "GUN_X";
	private static final byte[] GUN_X_BUFFER = (GUN_X + "\n").getBytes();
	public void gunIntermediate() throws IOException {
		output.write(GUN_X_BUFFER);
	}
	
	private static final String GUN_SHOT = "GUN_SHOT";
	private static final byte[] GUN_SHOT_BUFFER = (GUN_SHOT + "\n").getBytes();
	public void gunShot() throws IOException {
		output.write(GUN_SHOT_BUFFER);
	}

	private static final String SHOT_BY_SHERIF = "SHERIF";
	private static final byte[] SHOT_BY_SHERIF_BUFFER = (SHOT_BY_SHERIF + "\n").getBytes();
	public void shotBySherif() throws IOException {
		output.write(SHOT_BY_SHERIF_BUFFER);
	}
}
