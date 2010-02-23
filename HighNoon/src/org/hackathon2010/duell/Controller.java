package org.hackathon2010.duell;

import android.util.Log;
import android.widget.Toast;

public class Controller {
	private static enum STATE {SYNC, READY, STEADY, GO, SHOT1, SHOT2}

	private HighNoon activity;
	private Transmitter transmitter;
	private GunDirection localGun = GunDirection.UNKNOWN;
	private GunDirection remoteGun = GunDirection.UNKNOWN;
	private boolean localGunShot;
	private boolean remoteGunShot;
	private boolean shootingAlowed;
	private STATE state;
	
	public Controller(HighNoon activity) {
		this.activity = activity;
		reset();
	}

	public void reset() {
		state = STATE.SYNC;
		activity.shutUp();
	}
	
	public void setTransmitter(Transmitter transmitter) {
		this.transmitter = transmitter;
	}
	
	public void close() {
		transmitter.close();
	}

	public void localGunDown() {
		transmitter.localGunDown();
		setLocalGunDirection(GunDirection.DOWN);
	}
	
	public void remoteGunDown() {
		setRemoteGunDirection(GunDirection.DOWN);
	}
	
	public void localGunUp() {
		transmitter.localGunUp();
		setLocalGunDirection(GunDirection.UP);
	}

	public void remoteGunUp() {
		setRemoteGunDirection(GunDirection.UP);
	}

	public void localGunIntermediate() {
		transmitter.localGunIntermediate();
		setLocalGunDirection(GunDirection.INTERMEDIATE);
	}
	
	public void remoteGunIntermediate() {
		setRemoteGunDirection(GunDirection.INTERMEDIATE);
	}

	public void localGunShot()  {
		transmitter.localGunShot();
		localGunShot = true;
		
		// nextState();
		// TODO: if not allowed, you were dead by sherif
		//       if remoteGunShot==false, you win
		
	}

	public void remoteGunShot() {
		remoteGunShot = true;
		// nextState();
		// TODO: if not allowed, he is dead by sherif, you win
		//       if localGunShot==false, you are dead, he wins
	}

	private void setLocalGunDirection(GunDirection newLocalGun) {
		localGun = newLocalGun;
		handleState();
	}

	private void setRemoteGunDirection(GunDirection newRemoteGun) {
		remoteGun = newRemoteGun;

		if (state == STATE.SYNC) {	// this is first life sign from opponent
			state = STATE.READY;
			activity.flute.start(true);
		}

		handleState();
	}
	
	private void handleState() {
		switch(state) {
		case SYNC:
			break;
		case READY:
			if (localGun == GunDirection.DOWN && remoteGun == GunDirection.DOWN) {
				long delayTilCry = 3000L;
				state = STATE.STEADY;
				activity.flute.stop();
				activity.intro.stop();
				activity.background.start(true);
				
				activity.handler.postDelayed(
					new Runnable() {
						public void run() {
							state = STATE.GO;
							activity.howl.start(false);
							activity.background.stop();
						}
					}, delayTilCry);
			}
			break;
		case STEADY:
			// TODO: what should we do if players shrug during STEADY?
			break;
		case GO:
			break;
		case SHOT1:
		case SHOT2:
		}
	}
	
//	public void display(String s) {
//		activity.display(s);
//	}

	public HighNoon getActivity() {
		return activity;
	}

	public void connect() throws Exception {
		transmitter.connect();
	}
}

/*
local \ remote	|
----------------+----
				|
				
 */
