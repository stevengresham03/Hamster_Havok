package com.scgiii.hamsterhavok;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class MovingObstacle extends Obstacle {
    private float amplitude = 50;
    private float frequency = 0.05f;
    private float initialY;

    public MovingObstacle(Bitmap bitmap, float x, float y, float speed) {
        super(bitmap, x, y, speed);
        this.initialY = y;
    }



    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }
}