package com.scgiii.hamsterhavok.GameObject;

import android.graphics.Bitmap;

public class MovingObstacle extends Obstacle {
    private final boolean isFalling;

    public MovingObstacle(Bitmap bitmap, float x, float y, float speed, boolean isFalling) {
        super(bitmap, x, y, speed);
        this.isFalling = isFalling;
    }

    @Override
    public void update(float dt) {
        if (isFalling) {
            y += speed * dt; // Move downward
        } else {
            x -= speed * dt; // Move left
        }
    }
}
