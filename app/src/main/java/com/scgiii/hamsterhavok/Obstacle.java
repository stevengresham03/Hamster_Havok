package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class Obstacle {
    protected Bitmap bitmap;
    protected float x, y;
    protected float speed;

    public Obstacle(Bitmap bitmap, float x, float y, float speed) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.speed = speed;
    }


    public abstract void draw(Canvas canvas);

    public void update() {
       // x -= speed; // Move to the left
    }

    public boolean isOffScreen() {
        return x + bitmap.getWidth() < 0; // Off-screen when completely to the left of the screen
    }

    // Getters for collision detection
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return bitmap.getWidth(); }
    public int getHeight() { return bitmap.getHeight(); }
}