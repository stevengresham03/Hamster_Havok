package com.scgiii.hamsterhavok;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
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

    private static final float GLOBAL_SPEED_MULTIPLIER = 2.5f; // Adjust this to change overall game speed


    private final ObstacleFactory obstacleFactory;
    private final List<Obstacle> activeObstacles;
    private float timeSinceLastSpawn;
    private float minSpawnInterval = 4.0f; // Minimum time between spawns in seconds
    private float maxSpawnInterval = 10.0f; // Maximum time between spawns in seconds
    private float nextSpawnTime;
    private Random random;

    private boolean isGameRunning;

    public GameViews(Context context) {
        super(context);

        //getting surface holder and adding callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        background = new Background(getContext());
        hamster = new Player(getContext(), BitmapFactory.decodeResource(context.getResources(), R.drawable.player), getWidth(), getHeight());

        //button stuff
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        leftButton = new GameButton(100, screenHeight - 250, 300, screenHeight - 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.left_button));
        rightButton = new GameButton(350, screenHeight - 250, 550, screenHeight - 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.right_button));
        jumpButton = new GameButton(screenWidth - 250, screenHeight - 250, screenWidth - 50, screenHeight - 50, BitmapFactory.decodeResource(context.getResources(), R.drawable.jump_button));

        //all obstacle stuff
        //factory is a class that rand generates the obstacles. the activeObstacles lists
        obstacleFactory = new ObstacleFactory(context, getWidth());
        activeObstacles = new ArrayList<>();
        random = new Random();
        nextSpawnTime = getRandomSpawnInterval();

        //used to control whether a view can gain focus, which means it can receive input events like key presses/touch events.
        setFocusable(true);
    }


    //next 3 methods starts and ends the game loop
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

    //draws everything
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        background.draw(canvas);

        // Draw obstacles
        for (Obstacle obstacle : activeObstacles) {
            obstacle.draw(canvas);
        }
        hamster.draw(canvas);
        leftButton.draw(canvas);
        rightButton.draw(canvas);
        jumpButton.draw(canvas);
    }

    //updates and synchonizes with time.deltaTime passed from gameLoop
    public void update(float dt) {
        // Apply the global speed multiplier to dt
        dt = dt * GLOBAL_SPEED_MULTIPLIER;
        // Update player with delta time
        hamster.update(dt);
        // Update background (if it needs delta time)
        background.update(dt);
        // Update and spawn obstacles
        updateObstacles(dt);
    }


//calculates spawn rate based on intervals set before
    private float getRandomSpawnInterval() {
        return minSpawnInterval + random.nextFloat() * (maxSpawnInterval - minSpawnInterval);
    }

    private void spawnObstacle() {
        float rightEdgeOfScreen = getWidth(); // Assuming getWidth() returns the screen width
        Obstacle newObstacle = obstacleFactory.createRandomObstacle(rightEdgeOfScreen);
        activeObstacles.add(newObstacle);
    }

    //goes thru list of obstacles, calls their update methods, removes them when off screen, and spawns new ones
    private void updateObstacles(float dt) {
        // Update existing obstacles
        Iterator<Obstacle> iterator = activeObstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            obstacle.update(dt);
            if (obstacle.isOffScreen()) {
                iterator.remove();
                // If we implement object pooling:
                // obstacleFactory.recycleObstacle(obstacle);
            }
        }

        // Spawn new obstacles
        timeSinceLastSpawn += dt;
        if (timeSinceLastSpawn >= nextSpawnTime) {
            spawnObstacle();
            timeSinceLastSpawn = 0;
            nextSpawnTime = getRandomSpawnInterval();
        }
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