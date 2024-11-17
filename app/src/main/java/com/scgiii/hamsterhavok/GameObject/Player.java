package com.scgiii.hamsterhavok.GameObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends PlayerObject {
    private float velocityX, velocityY;
    private final float gravity;
    private boolean isJumping;
    public static int screenHeight;
    public int screenWidth;

    public Player(Context context, Bitmap bitmap, float x, float y) {
        super(bitmap, x, y);
        this.velocityX = 0;
        this.velocityY = 0;
        this.gravity = 1500f; // Pixels per second squared, adjust as needed
        this.isJumping = false;
        this.screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void update(float dt) {
        // Apply gravity
        velocityY += gravity * dt;

        // Update position
        x += velocityX * dt;
        y += velocityY * dt;

        // Ground collision
        if (y > screenHeight - bitmap.getHeight()) {
            y = screenHeight - bitmap.getHeight();
            velocityY = 0;
            isJumping = false;
        }

        // Screen boundaries
        if (x < 0) x = 0;
        if (x > screenWidth - bitmap.getWidth()) {
            x = screenWidth - bitmap.getWidth();
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void moveLeft() {
        velocityX = -500; // Pixels per second, adjust as needed
    }

    public void moveRight() {
        velocityX = 500; // Pixels per second, adjust as needed
    }

    public void jump() {
        if (!isJumping) {
            velocityY = -1500; // Pixels per second, adjust as needed
            isJumping = true;
        }
    }

    public void stopMoving() {
        velocityX = 0;
    }

    public boolean isColliding(float obstacleX, float roombaY, float obstacleWidth, float obstacleHeight) {
        //does this even work??? haven't tested...
        return x < obstacleX + obstacleWidth &&
                x + width > obstacleX &&
                y < roombaY + obstacleHeight &&
                y + height > roombaY;
    }

}
