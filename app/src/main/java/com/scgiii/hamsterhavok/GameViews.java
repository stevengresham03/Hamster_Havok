package com.scgiii.hamsterhavok;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

//Manages all objects in game and is responsible for updating all states and rendering objects to screen.
public class GameViews extends SurfaceView implements SurfaceHolder.Callback {
    private final GameLoop gameLoop;
    public Background background;
    private final Player hamster;
    private final Roomba roomba;
    private final GameButton leftButton;
    private final GameButton rightButton;
    private final GameButton jumpButton;

    public GameViews(Context context) {
        super(context);

        //getting surface holder and adding callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        background = new Background(getContext());
        hamster = new Player(getContext(), BitmapFactory.decodeResource(context.getResources(), R.drawable.player), 900, 724);
        roomba = new Roomba(getContext(), BitmapFactory.decodeResource(context.getResources(), R.drawable.roomba), 0,900);

        int buttonWidth = 200;
        int buttonHeight = 100;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        leftButton = new GameButton(50, screenHeight - 150, 250, screenHeight - 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.left_button));
        rightButton = new GameButton(300, screenHeight - 150, 500, screenHeight - 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.right_button));
        jumpButton = new GameButton(screenWidth - 250, screenHeight - 150, screenWidth - 50, screenHeight - 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.jump_button));
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
        hamster.update();
        background.update();
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
        leftButton.draw(canvas);
        rightButton.draw(canvas);
        jumpButton.draw(canvas);
    }


    //two dummy varaibles so game doesn't break before user touches screen
    private boolean isButtonPressed = false;
    private int activePointerId = -1;

//the line directlhy below just gets the IDE to stop bothering me lols. Doesn't cause any errors
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //gets the action, and where the user has touched
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);


        //switch case for where the user has touched (if they've touched the buttons)
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (leftButton.contains(event.getX(pointerIndex), event.getY(pointerIndex))) {
                    isButtonPressed = true;
                    activePointerId = pointerId;
                    hamster.moveLeft();
                } else if (rightButton.contains(event.getX(pointerIndex), event.getY(pointerIndex))) {
                    isButtonPressed = true;
                    activePointerId = pointerId;
                    hamster.moveRight();
                } else if (jumpButton.contains(event.getX(pointerIndex), event.getY(pointerIndex))) {

                    isButtonPressed = true;
                    //i took this next line out bc it makes the hamster stop moving when jumping
                   // activePointerId = pointerId;
                    hamster.jump();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isButtonPressed) {
                    int index = event.findPointerIndex(activePointerId);
                    if (index != -1) {
                        float x = event.getX(index);
                        float y = event.getY(index);
                        if (!leftButton.contains(x, y) && !rightButton.contains(x, y)) {
                            // Finger moved outside the move forward or backward buttons
                            releaseLeftRightButtons();
                        }

                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (pointerId == activePointerId) {
                    releaseLeftRightButtons();
                }
                break;
        }
        return true;
    }

    private void releaseLeftRightButtons() {
        isButtonPressed = false;
        activePointerId = -1;
        hamster.stopMoving();

    }
    /* saving this in case we need it later for whatever reason, but it can be safely deleted prolly
private boolean isInButton(MotionEvent event, Rect buttonRect) {
    return buttonRect.contains((int)event.getX(), (int)event.getY());
}*/
/*
    //onTouchEvent for controls
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //gets position of tap
                int x = (int) event.getX();
                int y = (int) event.getY();
                //checks to see if tap is in each button's rectangle
                if (leftButton.isPressed(x,y)) {
                    hamster.moveLeft();
                } else if (rightButton.isPressed(x,y)) {
                    hamster.moveRight();
                } else if(jumpButton.isPressed(x,y)) {
                    hamster.jump();
                }
                break;
            case MotionEvent.ACTION_UP:
                hamster.stopMoving();
                break;
        }
        return true;
    }


    public boolean isMovingForward() {
        return isMovingForward;
    }

    public boolean isMovingBackward() {
        return isMovingBackward;
    }*/
}