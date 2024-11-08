package com.scgiii.hamsterhavok;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Canvas;

public class GameButton {
    private final Rect bounds;
    private final Bitmap bitmap;

    public GameButton(int left, int top, int right, int bottom, Bitmap bitmap) {
        //sets the area for rectangles (so we can determine if user tap is in the rectangle bounds)
        this.bounds = new Rect(left, top, right, bottom);
        this.bitmap = bitmap;
    }
    public boolean contains(float x, float y) {
        return bounds.contains((int)x, (int)y);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, bounds, null);
    }

    public boolean isPressed(int x, int y) {
        return bounds.contains(x, y);
    }
}

