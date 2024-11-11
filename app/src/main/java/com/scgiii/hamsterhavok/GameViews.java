package com.scgiii.hamsterhavok;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

//Manages all objects in game and is responsible for updating all states and rendering objects to screen.
public class GameViews extends SurfaceView implements SurfaceHolder.Callback {
    private final GameLoop gameLoop;
    public Background background;
    private final Player hamster;
    private final Roomba roomba;
    private final GameButton leftButton;
    private final GameButton rightButton;
    private final GameButton jumpButton;

    //obstacle code once we have a class
    private List<Obstacle> obstacles;
    private Random random;
    private long lastObstacleSpawnTime;
    private static final long OBSTACLE_SPAWN_INTERVAL = 3000; // 3 seconds


    private boolean isGameRunning;

    public GameViews(Context context) {
        super(context);

        //getting surface holder and adding callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        background = new Background(getContext());
        hamster = new Player(getContext(), BitmapFactory.decodeResource(context.getResources(), R.drawable.player), 900, 724);
        roomba = new Roomba(getContext(), BitmapFactory.decodeResource(context.getResources(), R.drawable.roomba), 0, 900);

        int buttonWidth = 200;
        int buttonHeight = 100;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        leftButton = new GameButton(50, screenHeight - 150, 250, screenHeight - 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.left_button));
        rightButton = new GameButton(300, screenHeight - 150, 500, screenHeight - 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.right_button));
        jumpButton = new GameButton(screenWidth - 250, screenHeight - 150, screenWidth - 50, screenHeight - 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.jump_button));
        //used to control whether a view can gain focus, which means it can receive input events like key presses/touch events.
        setFocusable(true);

        //enemies code for later
        obstacles = new ArrayList<>();
        random = new Random();
        lastObstacleSpawnTime = System.currentTimeMillis();


    }


    private void spawnObstacle() {
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        float obstacleY = random.nextFloat() * (screenHeight - 100); // Adjust 100 based on obstacle height
        float speed = 5 + random.nextFloat() * 5; // Random speed between 5 and 10
        int direction = random.nextBoolean() ? 1 : -1; // Randomly choose direction
        float obstacleX = (direction == 1) ? screenWidth : -100; // Start off-screen

        Bitmap obstacleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.roomba);
        Obstacle obstacle = new Obstacle(getContext(), obstacleBitmap, obstacleX, obstacleY);
        obstacles.add(obstacle);
    }

    private void updateObstacles() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastObstacleSpawnTime > OBSTACLE_SPAWN_INTERVAL) {
            spawnObstacle();
            lastObstacleSpawnTime = currentTime;
        }

        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            obstacle.update();
            if (obstacle.isOffScreen(getWidth())) {
                iterator.remove(); // Remove obstacle if off-screen
            }
        }
    }

    private void drawObstacles(Canvas canvas) {
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(canvas);
        }
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
        updateObstacles();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        background.draw(canvas);
        hamster.draw(canvas);
        roomba.draw(canvas);
        leftButton.draw(canvas);
        rightButton.draw(canvas);
        jumpButton.draw(canvas);
        drawObstacles(canvas);
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
        //Log.d("GameViews", "PlayerX,Y" + hamster.getPositionX() + " " + hamster.getPositionY());

    }


    /*I'm gonna be so real: the game seems to function without saveState or restoreState
    but I'm keeping them here anyways bc they don't seem to mess anything up and we might need them later?
    But the mainActivity seems to handle the pause/resume by itself with the onResume method
 */
    public void saveState(Bundle outState) {
        //outState saves these values with the key (first parameter)
        outState.putFloat("playerX", hamster.getPositionX());
        outState.putFloat("playerY", hamster.getPositionY());
        //outState.putInt("score", score);
        outState.putFloat("backgroundOffsetX1", background.getOffsetX1());
        outState.putFloat("backgroundOffsetX2", background.getOffsetX2());


        outState.putBoolean("isGameRunning", isGameRunning);
        // Save any other necessary game state
    }

    public void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //savedInstanceState pulls the values with the key provided in first parameter, and if not found it will use the 2nd parameter as default value
            //these variables are for saving the state for resume/pause and opening and closing app
            float playerX = savedInstanceState.getFloat("playerX", 1240);
            float playerY = savedInstanceState.getFloat("playerY", 792);
            float backgroundOffsetX1 = savedInstanceState.getFloat("backgroundOffsetX1", 0);
            float backgroundOffsetX2 = savedInstanceState.getFloat("backgroundOffsetX2", -1500);
            hamster.setPosition(playerX, playerY);

            //  score = savedInstanceState.getInt("score", 0);
            isGameRunning = savedInstanceState.getBoolean("isGameRunning", false);
            // Restore any other saved game state

        }
    }
}