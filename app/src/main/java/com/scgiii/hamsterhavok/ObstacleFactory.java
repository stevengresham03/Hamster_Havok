package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class ObstacleFactory {
    private Context context;
    private Random random;
    private int width, height;
    Bitmap bitmap;

    public static int numOfAllObstacles = 7; // Set this directly if it's constant

    private Bitmap roomba, baby, cat, clothes, books, freakbob;

    public ObstacleFactory(Context context) {
        this.context = context;
        this.random = new Random();
        createAllObstacles();
    }

    private void createAllObstacles() {
        roomba = scaleImage(R.drawable.roomba, 5);
        clothes = scaleImage(R.drawable.clothes, 74);
        cat = scaleImage(R.drawable.cat, 3);
        baby = scaleImage(R.drawable.baby, 10);
        books = scaleImage(R.drawable.books, 8);
        freakbob = scaleImage(R.drawable.freakbob, 5);
    }

    private Bitmap scaleImage(int resourceId, int scaleFactor) {
        Bitmap original = BitmapFactory.decodeResource(context.getResources(), resourceId);
        int newWidth = original.getWidth() / scaleFactor;
        int newHeight = original.getHeight() / scaleFactor;
        return Bitmap.createScaledBitmap(original, newWidth, newHeight, true);
    }

    public void resetObstacle(Obstacle obstacle, float rightEdgeOfScreen) {
        obstacle.reset(rightEdgeOfScreen);
    }

    public Obstacle createRandomObstacle(float rightEdgeOfScreen) {
        int obstacleType = random.nextInt(7); // 0-6 inclusive
        float speed = Background.scrollSpeed;
        float spawnX = rightEdgeOfScreen + 50;
        float spawnY = Player.screenHeight - height;

        switch (obstacleType) {
            case 0:
                return new SmallObstacle(roomba, spawnX, spawnY, speed);
            case 1:
                return new SmallObstacle(baby, spawnX, spawnY, speed);
            case 2:
                return new SmallObstacle(clothes, spawnX, spawnY, speed);
            case 3:
                return new SmallObstacle(cat, spawnX, spawnY, speed);
            case 4:
                return new SmallObstacle(books, spawnX, spawnY, speed);
            case 5:
                return new LargeObstacle(freakbob, spawnX, spawnY, speed);
            case 6:
                return new LargeObstacle(roomba, spawnX, spawnY, speed);
            default:
                throw new IllegalStateException("Unexpected obstacle type: " + obstacleType);
        }
    }
}