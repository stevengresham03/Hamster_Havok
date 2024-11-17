package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Background {
    private Bitmap bitmap;
    private float offsetX1, offsetX2;
    public static float scrollSpeed = 300; // Initial scroll speed in pixels per second
    private static final float BASE_SPEED = 300; // Starting speed
    private static final float INCREMENT_FACTOR = 2f; // Speed increment per score point
    private static final float MAX_SPEED = 800; // Maximum scroll speed
    private final int screenWidth;
    private final int screenHeight;

    public Background(Context context) {
        // Get screen dimensions
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        // Load and scale the bitmap
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        bitmap = Bitmap.createScaledBitmap(originalBitmap, screenWidth, screenHeight, true);

        offsetX1 = 0;
        offsetX2 = bitmap.getWidth();
    }

    public void increaseScrollSpeed(float increment) {
        scrollSpeed += increment;
    }

    public void resetScrollSpeed() {
        scrollSpeed = 300; // Reset to initial speed
    }


    public void update(float dt, int score) {
        // Gradually increase speed based on score, capped at MAX_SPEED
        scrollSpeed = Math.min(BASE_SPEED + (score * INCREMENT_FACTOR), MAX_SPEED);

        // Update background positions
        offsetX1 -= scrollSpeed * dt;
        offsetX2 -= scrollSpeed * dt;

        // Loop background when it scrolls out of view
        if (offsetX1 <= -bitmap.getWidth()) {
            offsetX1 = offsetX2 + bitmap.getWidth();
        }
        if (offsetX2 <= -bitmap.getWidth()) {
            offsetX2 = offsetX1 + bitmap.getWidth();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, offsetX1, 0, null);
        canvas.drawBitmap(bitmap, offsetX2, 0, null);
    }

}