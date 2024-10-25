package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

//Manages all objects in game and is responsible for updating all states and rendering objects to screen.
public class GameViews extends SurfaceView implements SurfaceHolder.Callback {
    private boolean isMovingForward = false;
    private boolean isMovingBackward = false;

    private GameLoop gameLoop;
    public Background background;
    private Player hamster;
    private Roomba roomba;

    public GameViews(Context context) {
        super(context);

        //getting surface holder and adding callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        background = new Background(context);
        hamster = new Player(getContext(), BitmapFactory.decodeResource(context.getResources(), R.drawable.player), 900, 724);
        roomba = new Roomba(getContext(), BitmapFactory.decodeResource(context.getResources(), R.drawable.roomba), 0,900);
        //used to control whether a view can gain focus, which means it can receive input events like key presses/touch events.
        setFocusable(true);

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.stopLoop();
    }

    public void update() {

    }

    public void resume(){
        gameLoop.startLoop();
    }

    public void pause(){
        gameLoop.stopLoop();
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        background.draw(canvas);
        hamster.draw(canvas);
        roomba.draw(canvas);
        drawFPS(canvas);
        drawUPS(canvas);
    }

    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS" + averageUPS, 150, 120, paint);
    }

    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.white);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS" + averageFPS, 150, 220, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > getWidth() / 2) { // Check if touch is on the left side
                    isMovingBackward = true;
                    isMovingForward = false; // Stop right movement
                } else { // Touch is on the right side
                    isMovingForward = true;
                    isMovingBackward = false; // Stop left movement
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getX() > getWidth() / 2) {
                    isMovingBackward = true; // Continue moving left
                    isMovingForward = false; // Stop right movement
                } else {
                    isMovingForward = true; // Continue moving right
                    isMovingBackward = false; // Stop left movement
                }
                break;
            case MotionEvent.ACTION_UP:
                isMovingBackward = false; // Stop moving left when touch is released
                isMovingForward = false; // Stop moving right when touch is released
                break;
        }

        return true;
    }


    public boolean isMovingForward() {
        return isMovingForward;
    }

    public boolean isMovingBackward() {
        return isMovingBackward;
    }
}