package net.pongjour.model;

import android.graphics.PointF;

public interface GameEngine {
	PointF getBallPosition();

	/**
	 * Prozentuale Position des eigenen Schlägers
	 * 
	 * @return 0-1
	 */
	float getMyRacketPosition();

	int getMyScore();

	/**
	 * Prozentuale Position des gegnerischen Schlägers
	 * 
	 * @return 0-1
	 */
	float getOpponentRacketPosition();

	int getOpponentScore();

	float getRacketWidth();

	void setMyRacketPosition(float p);

	/**
	 * Signalisiert dem Gegner, wir sind bereit, von uns aus kann es los gehen.
	 */
	void startGame();

}
