/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.pongjour.view;

import java.io.IOException;

import net.pongjour.R;
import net.pongjour.model.GameEngine;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;


/**
 * View that draws, takes keystrokes, etc. for a simple LunarLander game.
 * 
 * Has a mode which RUNNING, PAUSED, etc. Has a x, y, dx, dy, ... capturing the
 * current ship physics. All x/y etc. are measured with (0,0) at the lower left.
 * updatePhysics() advances the physics based on realtime. draw() renders the
 * ship, and does an invalidate() to prompt another draw() as soon as possible
 * by the system.
 */
public class PongView extends SurfaceView implements SurfaceHolder.Callback {
	public enum Direction {
		UP, DOWN, LEFT, RIGHT, NONE;
	}
	
    public class PongThread extends Thread {
        /*
         * Physics constants
         */
        public int PHYS_DOWN_ACCEL_SEC = 35;
        public static final int PHYS_FIRE_ACCEL_SEC = 80;
        public static final int PHYS_FUEL_INIT = 60;
        public static final int PHYS_FUEL_MAX = 100;
        public static final int PHYS_FUEL_SEC = 10;
        public static final int PHYS_SLEW_SEC = 120; // degrees/second rotate
        public static final int PHYS_SPEED_HYPERSPACE = 180;
        public static final int PHYS_SPEED_INIT = 30;
        public static final int PHYS_SPEED_MAX = 120;
        /*
         * State-tracking constants
         */
        public static final int STATE_LOSE = 1;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_READY = 3;
        public static final int STATE_RUNNING = 4;
        public static final int STATE_WIN = 5;

        /*
         * Goal condition constants
         */
        public static final int TARGET_ANGLE = 18; // > this angle means crash
        public static final int TARGET_BOTTOM_PADDING = 17; // px below gear
        public static final int TARGET_PAD_HEIGHT = 8; // how high above ground
        public static final int TARGET_SPEED = 28; // > this speed means crash
        public static final double TARGET_WIDTH = 1.6; // width of target
        /*
         * UI constants (i.e. the speed & fuel bars)
         */
        public static final int UI_BAR = 100; // width of the bar(s)
        public static final int UI_BAR_HEIGHT = 10; // height of the bar(s)
        private static final String KEY_DIFFICULTY = "mDifficulty";
        private static final String KEY_DX = "mDX";

        private static final String KEY_DY = "mDY";
        private static final String KEY_FUEL = "mFuel";
        private static final String KEY_GOAL_ANGLE = "mGoalAngle";
        private static final String KEY_GOAL_SPEED = "mGoalSpeed";
        private static final String KEY_HEADING = "mHeading";
        private static final String KEY_LANDER_HEIGHT = "mLanderHeight";
        private static final String KEY_LANDER_WIDTH = "mLanderWidth";
        private static final String KEY_WINS = "mWinsInARow";

        private static final String KEY_X = "mX";
        private static final String KEY_Y = "mY";

        /*
         * Member (state) fields
         */
        /** The drawable to use as the background of the animation canvas */
        private Bitmap mBackgroundImage;

        /**
         * Current height of the surface/canvas.
         * 
         * @see #setSurfaceSize
         */
        private int mCanvasHeight = 1;

        /**
         * Current width of the surface/canvas.
         * 
         * @see #setSurfaceSize
         */
        private int mCanvasWidth = 1;

        /** What to draw for the Lander when it has crashed */
        private Drawable mCrashedImage;

        /** Velocity dx. */
        private double mDX;

        /** Velocity dy. */
        private double mDY;

        /** Is the engine burning? */
        private boolean mEngineFiring;

        /** What to draw for the Lander when the engine is firing */
        private Drawable mFiringImage;

        /** Fuel remaining */
        private double mFuel;

        /** Allowed angle. */
        private int mGoalAngle;

        /** Allowed speed. */
        private int mGoalSpeed;

        /** Message handler used by thread to interact with TextView */
        private Handler mHandler;

        /**
         * Lander heading in degrees, with 0 up, 90 right. Kept in the range
         * 0..360.
         */
        private double mHeading;

        /** Pixel height of Ball image. */
        private int mBallHeight;

        /** What to draw for the Ball */
        private Drawable mBallImage;

        /** Pixel width of lander image. */
        private int mBallWidth;

        /** Used to figure out elapsed time between frames */
        private long mLastTime;

        /** Paint to draw the lines on screen. */
        private Paint mLinePaint;

        /** The state of the game. One of READY, RUNNING, PAUSE, LOSE, or WIN */
        private int mMode;

        /** Currently rotating, -1 left, 0 none, 1 right. */
        private int mRotating;

        /** Indicate whether the surface has been created & is ready to draw */
        private boolean mRun = false;

        /** Handle to the surface manager object we interact with */
        private SurfaceHolder mSurfaceHolder;

        /** Number of wins in a row. */
        private int mWinsInARow;

        /** X of lander center. */
        private double mX;

        /** Y of lander center. */
        private double mY;
        
        /** Left lever object. */
        private RectF mLeftLever;
        
        /** Right lever object. */
        private RectF mRightLever;
        
        private float mLeverHeight = 64;
        
        private float mLeverWidth = 4;
        
        private float mLeftLeverPos = 0.5f;
        
        private float mRightLeverPos = 0.5f;
        
        private PointF mBallPos = new PointF(0.5f, 0.5f);
        
        private float mOffset = 2;
        
        private float mDelta = 0.7f;
        
        private Direction mDirection;
        
        private GameEngine mGameEngine;
        

        public PongThread(SurfaceHolder surfaceHolder, Context context,
                Handler handler) {
        	try {
				mGameEngine = GameEngine.Factory.getInstance(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
            // get handles to some important objects
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            mContext = context;

            Resources res = context.getResources();
            // cache handles to our key sprites & other drawables
            mBallImage = context.getResources().getDrawable(
                    R.drawable.ball);

            // load background image as a Bitmap instead of a Drawable b/c
            // we don't need to transform it and it's faster to draw this way
            mBackgroundImage = BitmapFactory.decodeResource(res,
                    R.drawable.bg);

            // Use the regular lander image as the model size for all sprites
            mBallWidth = mBallImage.getIntrinsicWidth();
            mBallHeight = mBallImage.getIntrinsicHeight();

            // Initialize paints for speedometer
            mLinePaint = new Paint();
            mLinePaint.setAntiAlias(true);
            mLinePaint.setARGB(255, 0, 255, 0);
            
            mLeftLever = new RectF(0, 0, 0, 0);
            
            mRightLever = new RectF(0, 0, 0, 0);

            mWinsInARow = 0;

            // initial show-up of lander (not yet playing)
            mX = mBallWidth;
            mY = mBallHeight * 2;
            mFuel = PHYS_FUEL_INIT;
            mDX = 0;
            mDY = 0;
            mHeading = 0;
            mEngineFiring = true;
        }

        /**
         * Starts the game, setting parameters for the current difficulty.
         */
        public void doStart() {
            synchronized (mSurfaceHolder) {
                // First set the game for Medium difficulty
                mFuel = PHYS_FUEL_INIT;
                mEngineFiring = false;
                mGoalSpeed = TARGET_SPEED;
                mGoalAngle = TARGET_ANGLE;
                int speedInit = PHYS_SPEED_INIT;

                // pick a convenient initial location for the lander sprite
                mX = mCanvasWidth / 2;
                mY = mCanvasHeight - mBallHeight / 2;

                // start with a little random motion
                mDY = Math.random() * -speedInit;
                mDX = Math.random() * 2 * speedInit - speedInit;
                mHeading = 0;

                mLastTime = System.currentTimeMillis() + 100;
                setState(STATE_RUNNING);
            }
        }

        /**
         * Pauses the physics update & animation.
         */
        public void pause() {
            synchronized (mSurfaceHolder) {
                if (mMode == STATE_RUNNING) setState(STATE_PAUSE);
            }
        }

        /**
         * Restores game state from the indicated Bundle. Typically called when
         * the Activity is being restored after having been previously
         * destroyed.
         * 
         * @param savedState Bundle containing the game state
         */
        public synchronized void restoreState(Bundle savedState) {
            synchronized (mSurfaceHolder) {
                setState(STATE_PAUSE);
                mRotating = 0;
                mEngineFiring = false;

                mX = savedState.getDouble(KEY_X);
                mY = savedState.getDouble(KEY_Y);
                mDX = savedState.getDouble(KEY_DX);
                mDY = savedState.getDouble(KEY_DY);
                mHeading = savedState.getDouble(KEY_HEADING);

                mBallWidth = savedState.getInt(KEY_LANDER_WIDTH);
                mBallHeight = savedState.getInt(KEY_LANDER_HEIGHT);
                mGoalSpeed = savedState.getInt(KEY_GOAL_SPEED);
                mGoalAngle = savedState.getInt(KEY_GOAL_ANGLE);
                mWinsInARow = savedState.getInt(KEY_WINS);
                mFuel = savedState.getDouble(KEY_FUEL);
            }
        }

        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        if (mMode == STATE_RUNNING) updatePhysics();
                        doDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        /**
         * Dump game state to the provided Bundle. Typically called when the
         * Activity is being suspended.
         * 
         * @return Bundle with this view's state
         */
        public Bundle saveState(Bundle map) {
            synchronized (mSurfaceHolder) {
                if (map != null) {
                    map.putDouble(KEY_X, Double.valueOf(mX));
                    map.putDouble(KEY_Y, Double.valueOf(mY));
                    map.putDouble(KEY_DX, Double.valueOf(mDX));
                    map.putDouble(KEY_DY, Double.valueOf(mDY));
                    map.putDouble(KEY_HEADING, Double.valueOf(mHeading));
                    map.putInt(KEY_LANDER_WIDTH, Integer.valueOf(mBallWidth));
                    map.putInt(KEY_LANDER_HEIGHT, Integer
                            .valueOf(mBallHeight));
                    map.putInt(KEY_GOAL_SPEED, Integer.valueOf(mGoalSpeed));
                    map.putInt(KEY_GOAL_ANGLE, Integer.valueOf(mGoalAngle));
                    map.putInt(KEY_WINS, Integer.valueOf(mWinsInARow));
                    map.putDouble(KEY_FUEL, Double.valueOf(mFuel));
                }
            }
            return map;
        }

        /**
         * Sets if the engine is currently firing.
         */
        public void setFiring(boolean firing) {
            synchronized (mSurfaceHolder) {
                mEngineFiring = firing;
            }
        }

        /**
         * Used to signal the thread whether it should be running or not.
         * Passing true allows the thread to run; passing false will shut it
         * down if it's already running. Calling start() after this was most
         * recently called with false will result in an immediate shutdown.
         * 
         * @param b true to run, false to shut down
         */
        public void setRunning(boolean b) {
            mRun = b;
        }

        /**
         * Sets the game mode. That is, whether we are running, paused, in the
         * failure state, in the victory state, etc.
         * 
         * @see #setState(int, CharSequence)
         * @param mode one of the STATE_* constants
         */
        public void setState(int mode) {
            synchronized (mSurfaceHolder) {
                setState(mode, null);
            }
        }

        /**
         * Sets the game mode. That is, whether we are running, paused, in the
         * failure state, in the victory state, etc.
         * 
         * @param mode one of the STATE_* constants
         * @param message string to add to screen or null
         */
        public void setState(int mode, CharSequence message) {
            /*
             * This method optionally can cause a text message to be displayed
             * to the user when the mode changes. Since the View that actually
             * renders that text is part of the main View hierarchy and not
             * owned by this thread, we can't touch the state of that View.
             * Instead we use a Message + Handler to relay commands to the main
             * thread, which updates the user-text View.
             */
            synchronized (mSurfaceHolder) {
                mMode = mode;

                if (mMode == STATE_RUNNING) {
                    Message msg = mHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("text", "");
                    b.putInt("viz", View.INVISIBLE);
                    msg.setData(b);
                    mHandler.sendMessage(msg);
                } else {
                    mRotating = 0;
                    mEngineFiring = false;
                    Resources res = mContext.getResources();
                    CharSequence str = "";
                    if (mMode == STATE_READY)
                        str = res.getText(R.string.mode_ready);
                    else if (mMode == STATE_PAUSE)
                        str = res.getText(R.string.mode_pause);
                    else if (mMode == STATE_LOSE)
                        str = res.getText(R.string.mode_lose);
                    else if (mMode == STATE_WIN)
                        str = res.getString(R.string.mode_win_prefix)
                                + mWinsInARow + " "
                                + res.getString(R.string.mode_win_suffix);

                    if (message != null) {
                        str = message + "\n" + str;
                    }

                    if (mMode == STATE_LOSE) mWinsInARow = 0;

                    Message msg = mHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("text", str.toString());
                    b.putInt("viz", View.VISIBLE);
                    msg.setData(b);
                    mHandler.sendMessage(msg);
                }
            }
        }

        /* Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(int width, int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;
                
                mLeverHeight = height/5;

                // don't forget to resize the background image
                mBackgroundImage = mBackgroundImage.createScaledBitmap(
                        mBackgroundImage, width, height, true);
            }
        }

        /**
         * Resumes from a pause.
         */
        public void unpause() {
            // Move the real time clock up to now
            synchronized (mSurfaceHolder) {
                mLastTime = System.currentTimeMillis() + 100;
            }
            setState(STATE_RUNNING);
        }

        /**
         * Handles a key-down event.
         * 
         * @param keyCode the key that was pressed
         * @param msg the original event object
         * @return true
         */
        boolean doKeyDown(int keyCode, KeyEvent msg) {
            synchronized (mSurfaceHolder) {
                boolean okStart = false;
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) okStart = true;
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) okStart = true;
                if (keyCode == KeyEvent.KEYCODE_S) okStart = true;

                boolean center = (keyCode == KeyEvent.KEYCODE_DPAD_UP);

                if (okStart
                        && (mMode == STATE_READY || mMode == STATE_LOSE || mMode == STATE_WIN)) {
                    // ready-to-start -> start
                    doStart();
                    return true;
                } else if (mMode == STATE_PAUSE && okStart) {
                    // paused -> running
                    unpause();
                    return true;
                } else if (mMode == STATE_RUNNING) {
                    // center/space -> fire
                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                            || keyCode == KeyEvent.KEYCODE_SPACE) {
                        setFiring(true);
                        return true;
                        // left/q -> left
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                            || keyCode == KeyEvent.KEYCODE_Q) {
                    	mDirection = Direction.LEFT;
                        mRotating = -1;
                        return true;
                        // right/w -> right
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                            || keyCode == KeyEvent.KEYCODE_W) {
                    	mDirection = Direction.RIGHT;
                        mRotating = 1;
                        return true;
                        // up -> pause
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        //pause();
                    	mDirection = Direction.UP;
                        return true;
                    }
                    else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    	mDirection = Direction.DOWN;
                    }
                    else {
                    	mDirection = Direction.NONE;
                    }
                }

                return false;
            }
        }

        /**
         * Handles a key-up event.
         * 
         * @param keyCode the key that was pressed
         * @param msg the original event object
         * @return true if the key was handled and consumed, or else false
         */
        boolean doKeyUp(int keyCode, KeyEvent msg) {
            boolean handled = false;

            synchronized (mSurfaceHolder) {
                if (mMode == STATE_RUNNING) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                            || keyCode == KeyEvent.KEYCODE_SPACE) {
                        setFiring(false);
                        handled = true;
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                            || keyCode == KeyEvent.KEYCODE_Q
                            || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                            || keyCode == KeyEvent.KEYCODE_W) {
                        mRotating = 0;
                        handled = true;
                    }
                }
            }

            return handled;
        }

        /**
         * Draws the ship, fuel/speed bars, and background to the provided
         * Canvas.
         */
        private void doDraw(Canvas canvas) {
            // Draw the background image. Operations on the Canvas accumulate
            // so this is like clearing the screen.
            canvas.drawBitmap(mBackgroundImage, 0, 0, null);
            
            // Daw the both levers
			mLeftLever.set(mOffset, (mLeftLeverPos * mCanvasHeight)
					- mLeverHeight / 2, mOffset + mLeverWidth,
					(mLeftLeverPos * mCanvasHeight) + mLeverHeight / 2);
            canvas.drawRect(mLeftLever, mLinePaint);
            
			mRightLever.set(mCanvasWidth - mOffset - mLeverWidth,
					(mRightLeverPos * mCanvasHeight)-mLeverHeight/2, mCanvasWidth - mOffset,
					(mRightLeverPos * mCanvasHeight)+mLeverHeight/2);
            canvas.drawRect(mRightLever, mLinePaint);


            canvas.save();           
            
            // draw the ball
            float x = mBallPos.x*mCanvasWidth;
            float y = mBallPos.y*mCanvasHeight;
            
            float halfBallWidth = mBallWidth/2;
			mBallImage.setBounds((int) (x - halfBallWidth),
					(int) (y - halfBallWidth), (int) (x + halfBallWidth),
					(int) (y + halfBallWidth));
            mBallImage.draw(canvas);
            
            canvas.restore();
        }

        /**
         * Figures the lander state (x, y, fuel, ...) based on the passage of
         * realtime. Does not invalidate(). Called at the start of draw().
         * Detects the end-of-game and sets the UI to the next state.
         */
        private void updatePhysics() {
            long now = System.currentTimeMillis();

            // Do nothing if mLastTime is in the future.
            // This allows the game-start to delay the start of the physics
            // by 100ms or whatever.
            if (mLastTime > now) return;

            double elapsed = (now - mLastTime) / 1000.0;
            
			if (mDirection != null ) {
				switch (mDirection) {
				case UP:
					mRightLeverPos = Math.max(0f, mRightLeverPos -= elapsed * mDelta);
					break;

				case DOWN:
					mRightLeverPos = Math.min(1f, mRightLeverPos += elapsed * mDelta);
					break;

				default:
					break;
				}
			}
			
			
			
			try {
				mGameEngine.setMyRacketPosition(mRightLeverPos);
				
				mLeftLeverPos = mGameEngine.getOpponentRacketPosition();
				mBallPos = mGameEngine.getBallPosition();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            // mRotating -- update heading
            if (mRotating != 0) {
                mHeading += mRotating * (PHYS_SLEW_SEC * elapsed);

                // Bring things back into the range 0..360
                if (mHeading < 0)
                    mHeading += 360;
                else if (mHeading >= 360) mHeading -= 360;
            }

            // Base accelerations -- 0 for x, gravity for y
            double ddx = 0.0;
            double ddy = -PHYS_DOWN_ACCEL_SEC * elapsed;

            double dxOld = mDX;
            double dyOld = mDY;

            // figure speeds for the end of the period
            mDX += ddx;
            mDY += ddy;

            // figure position based on average speed during the period
            mX += elapsed * (mDX + dxOld) / 2;
            mY += elapsed * (mDY + dyOld) / 2;

            mLastTime = now;

            // Evaluate if we have landed ... stop the game
            double yLowerBound = TARGET_PAD_HEIGHT + mBallHeight / 2
                    - TARGET_BOTTOM_PADDING;
            if (mY <= yLowerBound) {
                mY = yLowerBound;
            }
        }
    }

    /** Handle to the application context, used to e.g. fetch Drawables. */
    private Context mContext;

    /** Pointer to the text view to display "Paused.." etc. */
    private TextView mStatusText;

    /** The thread that actually draws the animation */
    private PongThread thread;

    public PongView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        // create thread only; it's started in surfaceCreated()
        thread = new PongThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {
                mStatusText.setVisibility(m.getData().getInt("viz"));
                mStatusText.setText(m.getData().getString("text"));
            }
        });

        setFocusable(true); // make sure we get key events
    }

    /**
     * Fetches the animation thread corresponding to this LunarView.
     * 
     * @return the animation thread
     */
    public PongThread getThread() {
        return thread;
    }

    /**
     * Standard override to get key-press events.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        return thread.doKeyDown(keyCode, msg);
    }

    /**
     * Standard override for key-up. We actually care about these, so we can
     * turn off the engine or stop rotating.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        return thread.doKeyUp(keyCode, msg);
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) thread.pause();
    }

    /**
     * Installs a pointer to the text view used for messages.
     */
    public void setTextView(TextView textView) {
        mStatusText = textView;
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        thread.setSurfaceSize(width, height);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        thread.setRunning(true);
        thread.start();
    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}
