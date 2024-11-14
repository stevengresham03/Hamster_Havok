package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
        float spawnY = isFalling ? -50 : screenHeight - 100; // Off-screen for falling, ground level otherwise

        int obstacleType = random.nextInt(3); // Select obstacle type
        switch (obstacleType) {
            case 0:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.roomba);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.books);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat);
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 8, bitmap.getHeight() / 8, true);

        return new MovingObstacle(bitmap, spawnX, spawnY, isFalling ? 300 : Background.scrollSpeed, isFalling);
    }
}
