/**
 * 
 */
package net.pongjour.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.PointF;

/**
 * @author benedict
 * 
 */
class GameEngineImpl implements GameEngine {

	static class Networking {
		private static final int _200 = 200;
		private static final String UTF_8 = "UTF-8";
		private final String sep = ":";
		private final DatagramSocket sock;
		private final GameEngineImpl ge;

		Networking(final SocketAddress addr, final GameEngineImpl ge)
				throws IOException {
			sock = new DatagramSocket(addr);
			this.ge = ge;
			// make a dummy call to ensure the encoding is ok:
			try {
				"".getBytes(UTF_8);
			} catch (final UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			new Thread() {
				@Override
				public void run() {
					for (;;)
						try {
							Networking.this.receiveLoop();
						} catch (final IOException e) {
							throw new RuntimeException(e);
						}
				}
			}.start();
		}

		float getOppRacketPosition(final long t) {
			throw new RuntimeException("Not implemented yet.");
		}

		synchronized void gotMessage(final DatagramPacket pack) {
			final Pattern trajPat = Pattern
					.compile("^trajectory:([^:)+:([^:)+:([^:)+:([^:)+:([^:)+:$");
			final Pattern pointPat = Pattern.compile("^givepoint$");

			final String s;
			try {
				s = new String(pack.getData(), UTF_8);
			} catch (final UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			Matcher m = trajPat.matcher(s);
			if (m.matches()) {
				ge.setTrajectoryLocal(new PointF(Float.parseFloat(m.group(2)),
						Float.parseFloat(m.group(3))), new PointF(Float
						.parseFloat(m.group(4)), Float.parseFloat(m.group(5))));
				// time = Long.parseLong(m.group(1));
				return;
			}

			m = pointPat.matcher(s);
			if (m.matches())
				// Hurra, Gegener hat uns einen Punkt gegeben.
				return;
		}

		/**
		 * Endless blocking receive loop. Holzhammer!
		 * 
		 * @throws IOException
		 */
		void receiveLoop() throws IOException {
			final byte[] bytes = new byte[_200];
			final DatagramPacket pack = new DatagramPacket(bytes, bytes.length);
			sock.receive(pack);
			gotMessage(pack);
		}

		void sendBallTrajectory(final long t, final PointF pos,
				final PointF speed) throws IOException {
			sendMessage("trajectory" + sep + t + sep + pos.x + sep + pos.y
					+ sep + speed.x + sep + speed.y + sep);
		}

		void givePoint() throws IOException {
			sendMessage("givepoint");
		}

		private void sendMessage(final String message) throws IOException {
			try {
				final byte[] bytes = message.getBytes(UTF_8);
				final DatagramPacket pack = new DatagramPacket(bytes,
						bytes.length);
				sock.send(pack);
			} catch (final UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}

		void setMyRacketPosition(final long t, final float p) {

		}
	}

	private PointF ballPosition;
	private PointF ballSpeed;
	private float myRacketPosition;
	private final Networking opponent;
	private boolean pause = false;

	private long t;
	private long t0;

	GameEngineImpl(final SocketAddress addr) throws IOException {
		opponent = new Networking(addr, this);
	}

	PointF computePosition(final long t) {
		final PointF p = new PointF();
		p.x = ballPosition.x + (t - t0) * ballSpeed.x;
		p.y = ballPosition.y + (t - t0) * ballSpeed.y;
		return p;
	}

	public PointF getBallPosition() {
		// @TODO Reflexion oben und unten!!!
		return computePosition(t);
	}

	private long getCurrentTime() {
		return t;
	}

	public float getMyRacketPosition() {
		return myRacketPosition;
	}

	public int getMyScore() {
		throw new RuntimeException("Not implemented yet!");
	}

	public float getOpponentRacketPosition() {
		return opponent.getOppRacketPosition(getCurrentTime());
	}

	public int getOpponentScore() {
		throw new RuntimeException("Not implemented yet!");
	}

	public float getRacketWidth() {
		return 0.25f;
	}

	public void nextLoop() throws IOException {
		if (pause)
			return;
		t = System.currentTimeMillis();
		final PointF p = computePosition(t);

		if (p.x >= 1) {
			// Abprall bei Gegner
			// Wir tuen gar nichts: Wir warten, bis der Gegner uns den Punkt
			// gibt.
		} else if (p.x < 0) {
			// Abprall bei uns
			if (Math.abs(p.y - getMyRacketPosition()) <= getRacketWidth() / 2) {
				// Schläger getroffen
				// - Neue Bahnkurve berechnen
				ballPosition.x = -ballPosition.x;
				ballSpeed.x = ballSpeed.x;
				// - Gegner Bescheid sagen
				opponent.sendBallTrajectory(t, ballPosition, ballSpeed);
				// @TODO Gegner benachrichtigen!
			} else {
				// Gegner bekommt Punkt
				opponent.givePoint();
				// Wir warten solange: Stillstands-Flag
				pause = true;
			}
		} else if (p.y > 1) {
			// Abprall oben
			ballPosition.y = 2 - ballPosition.y;
			ballSpeed.y = -ballSpeed.y;
		} else if (p.y < 0) {
			// Abprall unten
			ballPosition.y = -ballPosition.y;
			ballSpeed.y = -ballSpeed.y;
		}

	}

	/**
	 * Don't forget to {@link #sendTrajectory(long)}
	 * 
	 * @param t
	 * @param pos
	 * @param speed
	 * @throws IOException
	 */
	void setBallTrajectory(final long t, final PointF pos, final PointF speed)
			throws IOException {
		setTrajectoryLocal(pos, speed);
	}

	private void setTrajectoryLocal(final PointF pos, final PointF speed) {
		// final long t = getCurrentTime();

		// - pos + speed merken
		ballPosition.x = pos.x;
		ballPosition.y = pos.y;
		ballSpeed.x = speed.x;
		ballSpeed.y = speed.y;
		// - Kollisionszeitpunkt (Zukunft) berechnen
		// t_coll = (long) ((1 - ballPosition.x + t0 * ballSpeed.x) /
		// ballSpeed.x);
	}

	public void setMyRacketPosition(final float p) throws IOException {
		setMyRacketPosition(getCurrentTime(), p);
	}

	void setMyRacketPosition(final long t, final float p) {
		myRacketPosition = p;
		// Gegner benachrichtigen!
		opponent.setMyRacketPosition(t, p);
	}

	public void startGame() throws IOException {
		throw new RuntimeException("Not implemented yet!");
	}
}
