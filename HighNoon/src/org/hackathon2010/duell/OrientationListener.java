package org.hackathon2010.duell;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class OrientationListener implements SensorEventListener {
	private Controller controller;
	private GunDirection gunDirection = GunDirection.UNKNOWN;
	
	public OrientationListener(Controller controller) {
		this.controller = controller;
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// currently no use
	}

	public void onSensorChanged(SensorEvent event) {
		float pitch = event.values[1];
		float roll  = event.values[2];
		
		GunDirection newGunDirection = calculateDirection(pitch, roll);
		if (newGunDirection != gunDirection) {
			gunDirection = newGunDirection;
			switch (gunDirection) {
			case DOWN: 
				controller.localGunDown();
				break;
			case UP: 
				controller.localGunUp();
				break;
			case INTERMEDIATE: 
				controller.localGunIntermediate();
				break;
			}
		}
	}

	private GunDirection calculateDirection(float pitch, float roll) {
		// schuss-stellung: pitch == 0, roll == 0
		// duell-stellung: pitch = 90, roll == 0

		if (-10 < roll && roll < 10) {
			if (80 < pitch && pitch < 100) {
				return GunDirection.DOWN;
			} else if (-10 < pitch && pitch < 10) {
				return GunDirection.UP;
			}
		} 
		
		return GunDirection.INTERMEDIATE;
	}

}
