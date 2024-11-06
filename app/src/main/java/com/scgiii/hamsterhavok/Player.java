package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends PlayerObject{
    private float velocityX, velocityY;
    private float gravity;
    private boolean isJumping;
    private int screenHeight, screenWidth;

    public Player(Context context, Bitmap bitmap, float x, float y) {
        super(bitmap, x, y);
        this.velocityX = 0;
        this.velocityY = 0;
        this.gravity = 0.9f;
        this.isJumping = false;
        this.screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void update() {
        // Applies gravity to game
        velocityY += gravity;

        // Updates position
        x += velocityX;
        y += velocityY;

        // ground collision
        if (y > screenHeight - bitmap.getHeight()) {
            y = screenHeight - bitmap.getHeight();
            velocityY = 0;
            isJumping = false;
        }

        //screen boundaries
        if(x < 0) x = 0;
        if(x > screenWidth - bitmap.getWidth()){
            x = screenWidth - bitmap.getWidth();
        }
    }

    public void moveLeft() {
        velocityX = -10;
    }

    public void moveRight() {
        velocityX = 10;
    }

    public void jump() {
        if (!isJumping) {
            velocityY = -40; // Adjust jump strength as needed
            isJumping = true;
        }
    }

    public void stopMoving() {
        velocityX = 0;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

}