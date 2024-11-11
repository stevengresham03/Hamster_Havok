package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Obstacle extends ObstacleObject{

    public Obstacle(Context context, Bitmap bitmap, float x, float y) {
        super(context, bitmap, x, y);
    }



    public void update() {
    }

    public boolean isOffScreen(int width) {
        return false;
    }
}
