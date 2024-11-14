package com.scgiii.hamsterhavok;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class LargeObstacle extends Obstacle {
    private boolean isFalling;

    public LargeObstacle(Bitmap bitmap, float x, float y, float speed, boolean isFalling) {
        super(bitmap, x, y, speed);
        this.isFalling = isFalling;
    }

    @Override
    public void update(float dt) {
        if (isFalling) {
            y += speed * dt; // Fall vertically if isFalling is true
        } else {
            x -= speed * dt; // Move horizontally otherwise
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }
}
