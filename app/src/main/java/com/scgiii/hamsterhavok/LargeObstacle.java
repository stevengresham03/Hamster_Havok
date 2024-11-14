package com.scgiii.hamsterhavok;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class LargeObstacle extends Obstacle {
    public LargeObstacle(Bitmap bitmap, float x, float y, float speed) {
        super(bitmap, x, y, speed);
    }



    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }
}