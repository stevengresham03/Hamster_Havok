package com.scgiii.hamsterhavok;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
    private final SurfaceHolder surfaceHolder;
    private final GameViews gameViews;

    private boolean isRunning = false;
    private static final double MAX_UPS = 70.0;
    private static final double UPS_PERIOD = 1E+3 / MAX_UPS;

    // Delta time variables
    private long lastUpdateTime;
    private static final float MAX_DT = 0.05f; // 50 milliseconds

    private boolean isPaused = false;

    public GameLoop(GameViews gameViews, SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        this.gameViews = gameViews;
    }

    public void startLoop() {
        isRunning = true;
        lastUpdateTime = System.nanoTime(); // Initialize lastUpdateTime
        start();
    }

    public void stopLoop() {
        isRunning = false;
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pauseLoop() {
        isPaused = true;
    }

    public void resumeLoop() {
        isPaused = false;
        synchronized (this) {
            this.notify();
        }
    }

    @Override
    public void run() {
        super.run();

        long startTime = System.currentTimeMillis();
        while (isRunning) {
            if (isPaused) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Calculate delta time
            long now = System.nanoTime();
            float dt = Math.min((now - lastUpdateTime) / 1_000_000_000f, MAX_DT);
            lastUpdateTime = now;

            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    gameViews.update(dt); // Pass dt to update method
                    gameViews.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // Control the frame rate to stay within MAX_UPS
            long elapsedTime = System.currentTimeMillis() - startTime;
            long sleepTime = (long) (UPS_PERIOD - elapsedTime);

            if (sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            startTime = System.currentTimeMillis();
        }
    }
}
