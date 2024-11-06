package com.scgiii.hamsterhavok;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
    private SurfaceHolder surfaceHolder;
    private GameViews gameViews;

    private boolean isRunning = false;
    private static final double MAX_UPS = 70.0;
    private static final double UPS_PERIOD = 1E+3/MAX_UPS;


    public GameLoop(GameViews gameViews, SurfaceHolder surfaceHolder){
        this.surfaceHolder = surfaceHolder;
        this.gameViews = gameViews;
    }

    public void startLoop() {
        isRunning = true;
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
            //is it better to initialize canvas here or before while loop????????????
            Canvas canvas = null;
            try {
                //locking canvas to draw bitmap
                canvas = surfaceHolder.lockCanvas();
                //stops multiple threads from calling update and draw methods of this surfaceHolder at same time as this thread
                synchronized (surfaceHolder) {
                    gameViews.update();
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
                    throw new RuntimeException(e);
                }
            }

            //skipping frames to keep up with target UPS (not sure why)
            if(sleepTime > 0 && updateCount < MAX_UPS - 1){
                gameViews.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long)(updateCount * UPS_PERIOD - elapsedTime);

            }

        }
    }


}