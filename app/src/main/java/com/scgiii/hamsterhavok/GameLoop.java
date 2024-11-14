package com.scgiii.hamsterhavok;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameViews gameViews;

    private boolean isRunning = false;
    private static final double MAX_UPS = 70.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;

    // New variables for delta time
    private long lastUpdateTime;
    private static final float MAX_DT = 0.05f; // 50 milliseconds

    public GameLoop(GameViews gameViews, SurfaceHolder surfaceHolder){
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
        try{
            join();
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    private boolean isPaused = false;

    public void pauseLoop() {
        isPaused = true;
    }

    public void resumeLoop() {
        isPaused = false;
        synchronized (this) {
            this.notify();
        }
    }

    //this is the actual gameloop
    @Override
    public void run(){
        super.run();

        //declaring time and cycle count variables
        int updateCount = 0;
        int frameCount = 0;
        long startTime;
        long elapsedTime;
        long sleepTime;

        startTime = System.currentTimeMillis();
        //this while loop is the GameLoop (constantly repeats while isRunning=true)
        while(isRunning){
            //this try/catch is what Steven added 11/8 11:30pm
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

            //is it better to initialize canvas here or before while loop????????????
            Canvas canvas = null;
            try {
                //locking canvas to draw bitmap
                canvas = surfaceHolder.lockCanvas();
                //stops multiple threads from calling update and draw methods of this surfaceHolder at same time as this thread
                synchronized (surfaceHolder) {
                    gameViews.update(dt); // Pass dt to update method
                    gameViews.draw(canvas);
                    updateCount++;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }finally{
                if(canvas != null){
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //Pause game loop to not exceed target UPS
            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long)(updateCount * UPS_PERIOD - elapsedTime);
            if(sleepTime > 0){
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //skipping frames to keep up with target UPS (not sure why)
            while(sleepTime < 0 && updateCount < MAX_UPS - 1){
                gameViews.update(dt); // Pass dt here as well
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long)(updateCount * UPS_PERIOD - elapsedTime);
            }
        }
    }
}