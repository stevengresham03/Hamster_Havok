package com.scgiii.hamsterhavok;

import static android.opengl.ETC1.getHeight;
import static android.opengl.ETC1.getWidth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Player extends PlayerObject{
    private float velocityX, velocityY;
    private float gravity;
    private boolean isJumping;
    private int screenHeight;

    public Player(Context context, Bitmap bitmap, float x, float y) {
        super(bitmap, x, y);
        this.velocityX = 0;
        this.velocityY = 0;
        this.gravity = 0.9f;
        this.isJumping = false;
        this.screenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void update() {
        // Applies gravity to game
        velocityY += gravity;

        // Updates position
        y += velocityY;

        // ground collision
        if (y > screenHeight - bitmap.getHeight()) {
            y = screenHeight - bitmap.getHeight();
            velocityY = 0;
            isJumping = false;
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