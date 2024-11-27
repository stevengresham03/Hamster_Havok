package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.res.ResourcesCompat;

import com.scgiii.hamsterhavok.GameObject.Obstacle;
import com.scgiii.hamsterhavok.GameObject.ObstacleFactory;
import com.scgiii.hamsterhavok.GameObject.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameViews extends SurfaceView implements SurfaceHolder.Callback {
    private final MainActivity mainActivity;
    private final GameLoop gameLoop;
    private final Background background;
    private final Player hamster;
    private final ObstacleFactory obstacleFactory;
    private final List<Obstacle> activeObstacles;

    private Paint scorePaint; // Paint for score text
    private Paint scoreBackgroundPaint; // Paint for score background

    private static final float GLOBAL_SPEED_MULTIPLIER = 2.5f;

    private boolean isPaused = false;

    private int score;
    private float timeElapsed;
    private float timeSinceLastSpawn;
    private final float spawnInterval = 4.0f; // Spawn every 2 seconds

    public GameViews(MainActivity activity, Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.mainActivity = activity;
        gameLoop = new GameLoop(this, surfaceHolder);
        background = new Background(getContext());
        hamster = new Player(
                getContext(),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.player),
                getWidth(),
                getHeight()
        );

        obstacleFactory = new ObstacleFactory(context);
        activeObstacles = new ArrayList<>();

        initializeScoreEffects();
    }

    private void initializeScoreEffects() {
        // Score logic
        score = 0;
        timeElapsed = 0;

        // Paint for the text
        scorePaint = new Paint();
        scorePaint.setColor(0xFFFFFFFF); // White text color
        scorePaint.setTextSize(80); // Larger text size
        scorePaint.setFakeBoldText(true); // Bold text
        scorePaint.setShadowLayer(8.0f, 4.0f, 4.0f, 0xFF000000); // Black shadow for depth
        scorePaint.setTextAlign(Paint.Align.CENTER); // Center-align the text
        Typeface customFont = ResourcesCompat.getFont(getContext(), R.font.americancapt); // Replace with your font resource
        if (customFont != null) {
            scorePaint.setTypeface(customFont);
        }

        // Paint for the background box
        scoreBackgroundPaint = new Paint();
        scoreBackgroundPaint.setColor(0x99000000); // Semi-transparent black
        scoreBackgroundPaint.setStyle(Paint.Style.FILL);
        scoreBackgroundPaint.setAntiAlias(true); // Smooth edges
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameLoop.startLoop();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (gameLoop != null) {
            gameLoop.stopLoop();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        background.draw(canvas);
        hamster.draw(canvas);

        // Draw all active obstacles
        for (Obstacle obstacle : activeObstacles) {
            obstacle.draw(canvas);
        }

        drawScore(canvas); // Draw the enhanced score
    }

    private void drawScore(Canvas canvas) {
        int screenWidth = getWidth();
        int padding = 20; // Padding around the score box
        int boxWidth = 300; // Width of the score box
        int boxHeight = 120; // Height of the score box
        int boxX = screenWidth - boxWidth - padding; // X-coordinate of the box
        int boxY = padding; // Y-coordinate of the box

        // Draw the background box
        canvas.drawRoundRect(
                boxX, // Left
                boxY, // Top
                boxX + boxWidth, // Right
                boxY + boxHeight, // Bottom
                30, // Corner radius X
                30, // Corner radius Y
                scoreBackgroundPaint
        );

        // Draw the score text inside the box
        String scoreText = "Score: " + score;
        canvas.drawText(scoreText, boxX + (boxWidth / 2.0f), boxY + (boxHeight / 1.5f), scorePaint);
    }

    public void update(float dt) {
        if (isPaused) return;

        dt *= GLOBAL_SPEED_MULTIPLIER;

        hamster.update(dt);
        background.update(dt, score); // Pass score to control speed

        updateScore(dt);
        updateObstacles(dt);
    }



    // New method to increase difficulty
    private void increaseDifficultyBasedOnScore() {
        // Example: Increase speed every 10 points
        if (score % 10 == 0 && score > 0) {
            background.increaseScrollSpeed(20); // Adjust increment as needed
        }

    }

    private void updateScore(float dt) {
        timeElapsed += dt;
        if (timeElapsed >= 1.0f) {
            score++;
            timeElapsed = 0;

            // Rapidly increase the scroll speed
            if (score % 5 == 0) { // Every 5 points
                background.increaseScrollSpeed(50); // Adjust increment as needed
            }
        }
    }


    private void updateObstacles(float dt) {
        timeSinceLastSpawn += dt;

        // Spawn a new obstacle if the interval has passed
        if (timeSinceLastSpawn >= spawnInterval) {
            Obstacle newObstacle = obstacleFactory.createRandomObstacle(getWidth(), getHeight());
            activeObstacles.add(newObstacle);
            timeSinceLastSpawn = 0;
        }

        // Update and remove off-screen obstacles
        Iterator<Obstacle> iterator = activeObstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();

            if (!obstacle.isFalling()){
                obstacle.setSpeed(Background.scrollSpeed);
            }

            obstacle.update(dt);

            if (obstacle.isOffScreen(getHeight())) {
                iterator.remove();
            }

            /* Updated to compare hitboxes.
            * The getHitBox method excludes the transparent parts.
            * */
            if (RectF.intersects(hamster.getHitbox(), obstacle.getHitbox())) {
                // Handle player death. whatever that may entail
                handlePlayerDeath();
            }
        }
    }


    public void handlePlayerDeath() {
        mainActivity.playerDeath(score);
        pauseGame();
        //gameLoop.pauseLoop(); ISSUE HERE


    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
    }

    public Background getGameBackground() {
        return background;
    }

    public Player getPlayer() {
        return hamster;
    }



    public void onLeftButtonPressed() {
        hamster.moveLeft();
    }

    public void stopMovement() {
        hamster.stopMoving();
    }

    public void onRightButtonPressed() {
        hamster.moveRight();
    }

    public void onJumpButtonPressed() {
        hamster.jump();
    }
/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState();
        outState.putInt("score", score);
        // Save other important game state
    }



    // Method to save state
    public void saveState(Bundle outState) {
        outState.putInt("score", score);
        outState.putBoolean("isGameRunning", is);
        // Save other game state
    }

    // Method to restore state
    public void restoreState(Bundle savedState) {
        if (savedState != null) {
            currentScore = savedState.getInt("score", 0);
            currentLevel = savedState.getInt("level", 1);
            isGameRunning = savedState.getBoolean("isGameRunning", false);
            // Restore other game state
            updateGameState(); // Method to update game based on restored state
        } else {
            initializeNewGame(); // Method to start a new game
        }
    }
*/


}
