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

    private final GameButton leftButton;
    private final GameButton rightButton;
    private final GameButton jumpButton;

    //obstacle code
    private List<Obstacle> obstacles;
    private Random random;
    private long lastObstacleSpawnTime;
    private static final long OBSTACLE_SPAWN_INTERVAL = 3000; // 3 seconds
    private ObstacleFactory obstacleFactory;
    private ObstaclePool obstaclePool;

    private boolean isPlaying;
    private Thread gameThread;

    private long lastUpdateTime;
    private static final float MAX_DT = 0.05f; // 50 milliseconds

    private static final float OBSTACLE_SPAWN_INTERVAL_SECONDS = 3.0f; // 3 seconds

    // Add this field to track time since last obstacle spawn
    private float timeSinceLastObstacleSpawn = 0;

    private static final float GLOBAL_SPEED_MULTIPLIER =2.5f; // Adjust this to change overall game speed




    private boolean isGameRunning;

    public GameViews(Context context) {
        super(context);

        //getting surface holder and adding callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        background = new Background(getContext());
        hamster = new Player(getContext(), BitmapFactory.decodeResource(context.getResources(), R.drawable.player), 900, 724);

        int buttonWidth = 200;
        int buttonHeight = 100;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        leftButton = new GameButton(50, screenHeight - 150, 250, screenHeight - 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.left_button));
        rightButton = new GameButton(300, screenHeight - 150, 500, screenHeight - 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.right_button));
        jumpButton = new GameButton(screenWidth - 250, screenHeight - 150, screenWidth - 50, screenHeight - 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.jump_button));
        //used to control whether a view can gain focus, which means it can receive input events like key presses/touch events.
        setFocusable(true);

        //obstacle code for later
        //obstacles = new ArrayList<>();
        obstacleFactory = new ObstacleFactory(context); // Adjust ground height
        obstaclePool = new ObstaclePool(obstacleFactory);
        lastObstacleSpawnTime = System.currentTimeMillis();


        lastUpdateTime = System.nanoTime();


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

    public void update(float dt) {

        // Apply the global speed multiplier to dt
        dt = dt * GLOBAL_SPEED_MULTIPLIER;
        // Update player with delta time


        hamster.update(dt);

        // Update background (if it needs delta time)
        background.update(dt);

        // Update obstacle spawning
        timeSinceLastObstacleSpawn += dt;
        if (timeSinceLastObstacleSpawn >= OBSTACLE_SPAWN_INTERVAL_SECONDS) {
            spawnObstacle();
            timeSinceLastObstacleSpawn = 0;
        }

        // Update obstacles with delta time
        obstaclePool.updateObstacles(dt);

        // checkCollisions(); // Uncomment if you implement this
    }




    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        background.draw(canvas);
        hamster.draw(canvas);
        leftButton.draw(canvas);
        rightButton.draw(canvas);
        jumpButton.draw(canvas);

        Log.d("GameViews", "Drawing obstacles");
        obstaclePool.drawObstacles(canvas);
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
/*
    private void spawnObstacle() {
        Obstacle obstacle = obstacleFactory.createRandomObstacle(getRightEdgeOfScreen());
        obstacles.add(obstacle);
    }*/
private void spawnObstacle() {
    Obstacle obstacle = obstaclePool.obtainObstacle(getWidth());
    Log.d("GameViews", "Spawned obstacle at x: " + obstacle.getX() + ", y: " + obstacle.getY());
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
    private float getRightEdgeOfScreen() {
        return getWidth(); // Assuming 0 is the left edge
    }
}