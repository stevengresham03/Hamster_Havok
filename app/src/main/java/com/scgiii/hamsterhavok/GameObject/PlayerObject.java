package com.scgiii.hamsterhavok.GameObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class PlayerObject {
    protected Bitmap bitmap;
    protected float x;
    protected float y;
    int width, height;

    public PlayerObject(Bitmap bitmap, float x, float y) {
        if (bitmap == null) {
            throw new IllegalArgumentException("Bitmap cannot be null");
        }
        width = bitmap.getWidth() / 5;
        height = bitmap.getHeight() / 5;
        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        this.x = x;
        this.y = y;
    }

    protected abstract void draw(Canvas canvas);

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public int getHeight() { return height; }

    public int getWidth() { return width; }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void recycle() {
        bitmap.recycle();
    }
}