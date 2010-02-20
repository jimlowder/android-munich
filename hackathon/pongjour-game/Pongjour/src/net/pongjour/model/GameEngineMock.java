/**
 * 
 */
package net.pongjour.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketAddress;

import android.graphics.PointF;

/**
 * @author benedict
 * 
 */
class GameEngineMock implements GameEngine {

    private float mOppRacketPosition = 0.5f;
    private float mOppRacketNextDelta = 0.01f;

    static class Networking {
        PointF ballPos, ballSpeed;
        float racket;
        long time;

        Networking(final SocketAddress addr) throws IOException {
        }

        synchronized void gotMessage(final DatagramPacket pack) {
        }

        /**
         * Endless blocking receive loop. Holzhammer!
         * 
         * @throws IOException
         */
        void receiveLoop() throws IOException {
        }

        void setBallTrajectory(final long t, final PointF pos, final PointF speed) throws IOException {
            final char sep = ':';
            final String message = "trajectory" + ":" + t + sep + pos.x + sep + pos.y + sep + speed.x + sep + speed.y
                    + sep;
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

    GameEngineMock(final SocketAddress addr) throws IOException {
        opponent = new Networking(addr);
    }

    public PointF getBallPosition() {
        // @TODO Reflexion oben und unten!!!
        final PointF p = new PointF();
        p.x = 0.5f;
        p.y = 0.5f;
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
        if (mOppRacketPosition >= 1.0 || mOppRacketPosition <= 0)
            mOppRacketNextDelta = -mOppRacketNextDelta;
        mOppRacketPosition += mOppRacketNextDelta;
        return mOppRacketPosition;
        // return opponent.getOppRacketPosition(getCurrentTime());
    }

    public int getOpponentScore() {
        throw new RuntimeException("Not implemented yet!");
    }

    public float getRacketWidth() {
        return 0.25f;
    }

    public void setMyRacketPosition(final float p) throws IOException {
        setMyRacketPosition(getCurrentTime(), p);
        System.out.println(p);
    }

    void setMyRacketPosition(final long t, final float p) {
        myRacketPosition = p;
        // Gegner benachrichtigen!
        opponent.setMyRacketPosition(t, p);
    }

    public void startGame() throws IOException {
        throw new RuntimeException("Not implemented yet!");
    }

    public void nextLoop() throws IOException {
        // TODO Auto-generated method stub
        
    }
}
