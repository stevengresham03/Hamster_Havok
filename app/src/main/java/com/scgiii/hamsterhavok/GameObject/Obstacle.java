package com.scgiii.hamsterhavok.GameObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public abstract class Obstacle {
    protected Bitmap bitmap;
    protected float x, y;
    protected float speed;
    protected boolean isFalling;

    private float hitboxPaddingX;
    private float hitboxPaddingY;


    public Obstacle(Bitmap bitmap, float x, float y, float speed, boolean isFalling) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.isFalling = isFalling;

        // Hitbox padding
        this.hitboxPaddingX = bitmap.getWidth() * 0.005f;
        this.hitboxPaddingY = bitmap.getHeight() * 0.005f;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public abstract void update(float dt);

    public void setSpeed(float speed)
    {
        this.speed = speed;
    }

    public boolean isOffScreen(float screenHeight) {
        return y > screenHeight || x + bitmap.getWidth() < 0;
    }

    public boolean isFalling(){
        return this.isFalling;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public RectF getHitbox(){
        return new RectF(
                x + hitboxPaddingX,
                y + hitboxPaddingY,
                x + bitmap.getWidth() - hitboxPaddingX,
                y + bitmap.getHeight() - hitboxPaddingY
        );
    }
}
