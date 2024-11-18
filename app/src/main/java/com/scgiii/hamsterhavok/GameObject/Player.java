package com.scgiii.hamsterhavok.GameObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Player extends PlayerObject {
    private float velocityX, velocityY;
    private final float gravity;
    private boolean isJumping;
    public static int screenHeight;
    public int screenWidth;

    private float hitboxPaddingX;
    private float hitboxPaddingY;

    public Player(Context context, Bitmap bitmap, float x, float y) {
        super(bitmap, x, y);
        this.velocityX = 0;
        this.velocityY = 0;
        this.gravity = 1500f; // Pixels per second squared, adjust as needed
        this.isJumping = false;
        this.screenHeight = context.getResources().getDisplayMetrics().heightPixels - 50; // Offset so it's not in the ground
        this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        // Calculate the hitbox padding dynamically with the image dimensions
        this.hitboxPaddingX = bitmap.getWidth() * 0.05f;
        this.hitboxPaddingY = bitmap.getHeight() * 0.05f;

        // Changing the initial position
        //this.y = screenHeight - bitmap.getHeight() - 100;
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
            velocityY = -1450; // Pixels per second, adjust as needed
            isJumping = true;
        }
    }

    public void stopMoving() {
        velocityX = 0;
    }

    /*
    * This is the OG isColliding method. I updated it to be obstacleY instead of the roomba.
    *
    * the bitmap class includes data on the pixels, like size and color (w/h of pixels and color/transparency).
    * In the context of collision detection, the width and height of the `Bitmap` are used to calculate the bounding box (the rectangular area that surrounds the visible image).
    *
    * The draw method above already implements the canvas, which draws the image in x, y positions
    * The original isColliding method checks for overlaps using simple rectangle math.
    * However, this method includes the entire image dimensions (transparent and visible parts), leading to inaccurate collision detection.
    *
    * The RectF class defines a smaller hitbox on a floating pt lvl
    public boolean isColliding(float obstacleX, float obstacleY, float obstacleWidth, float obstacleHeight) {
        //does this even work??? haven't tested...
        return x < obstacleX + obstacleWidth &&
                x + bitmap.getWidth() > obstacleX &&
                y < obstacleY + obstacleHeight &&
                y + bitmap.getHeight() > obstacleY;
    } */

    /*
    * Updated isColliding method.
    * Calls RectF.intersects instead which bc it checks if two rectangular areas overlap.
    * */
    public boolean isColliding(RectF otherHitbox) {
        return RectF.intersects(this.getHitbox(), otherHitbox);
    }

    /*
    * Defines a custom hitbox for the player by creating a `RectF` (rectangle defined with floating-point vals).
    * The padding variable reduces the size of the hitbox by shrinking it inward.
    * The smaller hitbox improves collision detection so that it's based on the visible portion of the image.
    */
    public RectF getHitbox(){
        return new RectF(
                x + hitboxPaddingX,
                y+ hitboxPaddingY,
                x + bitmap.getWidth() - hitboxPaddingX,
                 y + bitmap.getHeight() - hitboxPaddingY
        );
    }

}
