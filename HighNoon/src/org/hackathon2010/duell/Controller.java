package org.hackathon2010.duell;

import android.view.View;

public class Controller {
	private static enum STATE {SYNC, READY, STEADY, GO, SHOT}

	private HighNoon activity;
	private Transmitter transmitter;
	private GunDirection localGun = GunDirection.UNKNOWN;
	private GunDirection remoteGun = GunDirection.UNKNOWN;
	private STATE state;
	
	public Controller(HighNoon activity) {
		this.activity = activity;
	}

	public void reset() {
		state = STATE.SYNC;
		activity.shutUp();
		activity.background.start(true);
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
		if (state == STATE.SHOT) {
			return;
		} else if (state != STATE.GO) {
			shotBySherif();
		} else if (localGun == GunDirection.UP) {
			transmitter.localGunShot();
			state = STATE.SHOT;
			activity.handler.post(
					new Runnable() {					
						public void run() {
							activity.setBackgroundResource(R.drawable.winner);
							activity.bangBtn.setVisibility(View.INVISIBLE);
						}
					});
		}		
	}

	public void remoteGunShot() {
		if (state != STATE.SHOT) {
			state = STATE.SHOT;
			activity.handler.post(
					new Runnable() {					
						public void run() {
							activity.setBackgroundResource(R.drawable.looser);
							activity.bangBtn.setVisibility(View.INVISIBLE);
						}
					});
		}
	}

	private void setLocalGunDirection(GunDirection newLocalGun) {
		localGun = newLocalGun;
		handleState();
	}

	private void setRemoteGunDirection(GunDirection newRemoteGun) {
		remoteGun = newRemoteGun;

		if (state == STATE.SYNC) {	// this is first life sign from opponent
			state = STATE.READY;
			activity.background.stop();
			activity.flute.start(true);
			activity.handler.post(
				new Runnable() {					
					public void run() {
						activity.setBackgroundResource(R.drawable.holddown);
					}
				});
		}

		handleState();
	}
	
	private void handleState() {
		switch(state) {
		case SYNC:
			break;
		case READY:
			if (localGun == GunDirection.DOWN && remoteGun == GunDirection.DOWN) {
				state = STATE.STEADY;
				activity.flute.stop();
				activity.handler.post(
					new Runnable() {					
						public void run() {
							activity.setBackgroundResource(R.drawable.shoot_when_vulture);
							activity.bangBtn.setVisibility(View.VISIBLE);
						}
					});
				activity.handler.postDelayed(
					new Runnable() {
						public void run() {
							state = STATE.GO;
							activity.howl.start(false);
							// activity.creaky.stop();
						}
					}, randomDelay());
			}
			break;
		case STEADY:
			if (localGun != GunDirection.DOWN) {
				shotBySherif();
			}
			break;
		case GO:
			break;
		case SHOT:
		}
	}

	private void shotBySherif() {
		state = STATE.SHOT;
		activity.handler.post(
			new Runnable() {					
				public void run() {
					activity.setBackgroundResource(R.drawable.dead_by_sherif);
					activity.bangBtn.setVisibility(View.INVISIBLE);
				}
			});
	}
	
	public void remoteShotBySherif() {
		state = STATE.SHOT;
		activity.handler.post(
			new Runnable() {					
				public void run() {
					activity.setBackgroundResource(R.drawable.opponent_cheated);
					activity.bangBtn.setVisibility(View.INVISIBLE);
				}
			});
	}

	public HighNoon getActivity() {
		return activity;
	}

	public void connect() throws Exception {
		transmitter.connect();
	}
	
	private long randomDelay() {
		return 5000L;
	}

}
