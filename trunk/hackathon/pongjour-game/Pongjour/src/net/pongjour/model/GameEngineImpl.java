/**
 * 
 */
package net.pongjour.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

import android.graphics.PointF;

/**
 * @author benedict
 * 
 */
class GameEngineImpl implements GameEngine {

    static class Networking {
        private static final int _200 = 200;
        private static final String UTF_8 = "UTF-8";
        DatagramSocket sock;
        PointF ballPos, ballSpeed;
        float racket;
        long time;

        Networking(final SocketAddress addr) throws IOException {
            sock = new DatagramSocket(addr);
            // make a dummy call to ensure the encoding is ok:
            try {
                "".getBytes(UTF_8);
            }
            catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            receiveLoop();
        }

        synchronized void gotMessage(final DatagramPacket pack) {
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

        void setBallTrajectory(final long t, final PointF pos, final PointF speed) throws IOException {
            final char sep = ':';
            final String message = "trajectory" + ":" + t + sep + pos.x + sep + pos.y + sep + speed.x + sep + speed.y
                    + sep;
            try {
                final byte[] bytes = message.getBytes(UTF_8);
                final DatagramPacket pack = new DatagramPacket(bytes, bytes.length);
                sock.send(pack);
            }
            catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        void setMyRacketPosition(final long t, final float p) {

        }

        float getOppRacketPosition(final long t) {
            throw new RuntimeException("Not implemented yet.");
        }
    }

    private final Networking opponent;
    private float myRacketPosition;
    PointF ballPosition;
    PointF ballSpeed;
    long t0, t_coll;

    private long getCurrentTime() {
        return System.currentTimeMillis();
    }

    GameEngineImpl(final SocketAddress addr) throws IOException {
        opponent = new Networking(addr);
    }

    public PointF getBallPosition() {
        // @TODO Reflexion oben und unten!!!
        final PointF p = new PointF();
        final long t = getCurrentTime();
        p.x = ballPosition.x + (t - t0) * ballSpeed.x;
        p.y = ballPosition.y + (t - t0) * ballSpeed.y;
        return p;
    }

    void setBallTrajectory(final long t, final PointF pos, final PointF speed) throws IOException {
        // final long t = getCurrentTime();
        // @TODO Gegner benachrichtigen!
        // - pos + speed merken
        ballPosition.x = pos.x;
        ballPosition.y = pos.y;
        ballSpeed.x = speed.x;
        ballSpeed.y = speed.y;
        // - Kollisionszeitpunkt (Zukunft) berechnen
        t_coll = (long) ((1 - ballPosition.x + t0 * ballSpeed.x) / ballSpeed.x);
        opponent.setBallTrajectory(t, ballPosition, ballSpeed);
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
