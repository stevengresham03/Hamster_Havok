package com.scgiii.hamsterhavok.GameObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.scgiii.hamsterhavok.Background;
import com.scgiii.hamsterhavok.R;

import java.util.Random;

public class ObstacleFactory {
    private final Context context;
    private final Random random;

    public ObstacleFactory(Context context) {
        this.context = context;
        this.random = new Random();
    }

    public Obstacle createRandomObstacle(float screenWidth, float screenHeight) {
        boolean isFalling = random.nextBoolean(); // Randomly decide if the obstacle falls
        Bitmap bitmap;
        float spawnX = isFalling ? random.nextFloat() * screenWidth : screenWidth + 50; // Spawn above screen for falling
        //float spawnY = isFalling ? -50 : screenHeight - 100; // Off-screen for falling, ground level otherwise
        //float spawnY = isFalling ? -bitmap.getHeight() : screenHeight - bitmap.getHeight();

        int obstacleType = random.nextInt(4); // Select obstacle type
        switch (obstacleType) {
            case 0:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.roomba);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.books);
                break;
            case 2:
                int case2Index = random.nextInt(3);
                switch (case2Index){
                    case 0:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.baby);
                        break;
                    case 1:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bowling_ball);
                        break;
                    default:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bomb);
                        break;
                }
                break;
            case 3:
                int case3Index = random.nextInt(3);
                switch (case3Index){
                    case 0:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.clothes);
                        break;
                    case 1:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pillow);
                        break;
                    default:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.basketball);
                        break;
                }
                break;
            default:
                int defaultIndex = random.nextInt(4);
                switch (defaultIndex){
                    case 0:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat);
                        break;
                    case 1:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.microwave);
                        break;
                    case 2:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chair);
                        break;
                    default:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.brick);
                        break;

                }
                break;
        }

        // This is so that the scaling for the obstacles can be done dynamically.
        // The standard is 15% of the og size and then the switch statement changes specifics
        float scaleFactor = 0.15f;

        switch (obstacleType){
            case 1:
                scaleFactor = 0.45f;
                break;
            case 2:
                scaleFactor = 0.35f;
                break;
            case 3:
                scaleFactor = 0.25f;
                break;
        }

        // This gets the new size based on its scaleFactor
        int newWidth = (int) (bitmap.getWidth() * scaleFactor);
        int newHeight= (int) (bitmap.getHeight() * scaleFactor);


        bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

        float spawnY = isFalling ? -bitmap.getHeight() : screenHeight - bitmap.getHeight();

        float initialSpeed = isFalling ? 300 : Background.scrollSpeed;

        return new MovingObstacle(bitmap, spawnX, spawnY, initialSpeed, isFalling);
    }
}
