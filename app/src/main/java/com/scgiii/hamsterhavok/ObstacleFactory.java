package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



import java.util.Random;

public class ObstacleFactory {
    private Context context;

    private Random random;
    private int width, height;

    public ObstacleFactory(Context context, float screenWidth) {
        this.context = context;
        this.random = new Random();
    }

    public Obstacle createRandomObstacle(float rightEdgeOfScreen) {
        int obstacleType = random.nextInt(7); // 0-6 inclusive
        float speed = Background.scrollSpeed;  //5 + random.nextFloat() * 3; // Random speed between 5 and 8
        Bitmap bitmap;

        float spawnX = rightEdgeOfScreen + 50; // Spawn 50 pixels off-screen
        float spawnY;


        switch (obstacleType) {
            //Roomba
            case 0:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.roomba);
                width = bitmap.getWidth() / 5;
                height = bitmap.getHeight() / 5;
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                //spawnY = groundY - bitmap.getHeight();
                spawnY = Player.screenHeight - height;
                return new SmallObstacle(bitmap, spawnX, spawnY, speed);
            //Clothes
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.clothes);
                width = bitmap.getWidth() / 7;
                height = bitmap.getHeight() / 7;
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                spawnY = Player.screenHeight - height;
                return new SmallObstacle(bitmap, spawnX, spawnY, speed);
            //Cat
            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat);
                width = bitmap.getWidth() / 3;
                height = bitmap.getHeight() / 3;
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                spawnY = Player.screenHeight - height;
                return new SmallObstacle(bitmap, spawnX, spawnY, speed);
            //Baby
            case 3:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.baby);
                width = bitmap.getWidth() / 10;
                height = bitmap.getHeight() / 10;
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                spawnY = Player.screenHeight - height;
                return new SmallObstacle(bitmap, spawnX, spawnY, speed);
            //Books
            case 4:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.books);
                width = bitmap.getWidth() / 8;
                height = bitmap.getHeight() / 8;
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                spawnY = Player.screenHeight - height;
                return new SmallObstacle(bitmap, spawnX, spawnY, speed);
            //FreakBob
            case 5:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.freakbob);

                width = bitmap.getWidth() / 5;
                height = bitmap.getHeight() / 5;

                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                spawnY = Player.screenHeight - height;
                return new LargeObstacle(bitmap, spawnX, spawnY, speed);
            //player
            case 6:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);

                width = bitmap.getWidth() / 5;
                height = bitmap.getHeight() / 5;
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                spawnY = Player.screenHeight - height;
                return new LargeObstacle(bitmap, spawnX, spawnY, speed);
            default:
                throw new IllegalStateException("Unexpected obstacle type: " + obstacleType);
        }
    }
}