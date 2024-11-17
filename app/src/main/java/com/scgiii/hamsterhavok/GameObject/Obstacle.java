package com.scgiii.hamsterhavok.GameObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

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

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public abstract void update(float dt);

    public boolean isOffScreen(float screenHeight) {
        return y > screenHeight || x + bitmap.getWidth() < 0;
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
        float padding = 5;
        return new RectF(
                x + padding,
                y + padding,
                x + getWidth() - padding,
                y + getHeight() - padding
        );
    }
}
