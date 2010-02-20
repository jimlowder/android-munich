package net.pongjour.model;

import java.io.IOException;
import java.net.SocketAddress;

import android.graphics.PointF;

public interface GameEngine {

    static class Factory {
        public static GameEngine getInstance(final SocketAddress addr) throws IOException {
            return new GameEngineMock(addr);
        }
    }

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

    void setMyRacketPosition(float p) throws IOException;

    /**
     * Signalisiert dem Gegner, wir sind bereit, von uns aus kann es los gehen.
     */
    void startGame() throws IOException;
    
    void nextLoop() throws IOException;

}
