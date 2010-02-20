/**
 * 
 */
package net.pongjour.model;

import java.net.SocketAddress;

import android.graphics.PointF;

/**
 * @author benedict
 * 
 */
class GameEngineImpl implements GameEngine {

	GameEngineImpl(final SocketAddress addr) {
	}

	public PointF getBallPosition() {
		throw new RuntimeException("Not implemented yet!");
	}

	public float getMyRacketPosition() {
        throw new RuntimeException("Not implemented yet!");
	}

	public int getMyScore() {
        throw new RuntimeException("Not implemented yet!");
	}

	public float getOpponentRacketPosition() {
        throw new RuntimeException("Not implemented yet!");
	}

	public int getOpponentScore() {
        throw new RuntimeException("Not implemented yet!");
	}

	public float getRacketWidth() {
        throw new RuntimeException("Not implemented yet!");
	}

	public void setMyRacketPosition(final float p) {
        throw new RuntimeException("Not implemented yet!");
	}

	public void startGame() {
        throw new RuntimeException("Not implemented yet!");
	}
}
