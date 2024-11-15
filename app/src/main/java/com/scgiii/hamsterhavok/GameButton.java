package com.scgiii.hamsterhavok;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Canvas;

public class GameButton {
    private final Rect bounds;
    private final Bitmap bitmap;
    private Paint paint;

    public GameButton(int left, int top, int right, int bottom, Bitmap bitmap) {
        //sets the area for rectangles (so we can determine if user tap is in the rectangle bounds)
        this.bounds = new Rect(left, top, right, bottom);
        this.bitmap = bitmap;
        paint = new Paint();
        paint.setAlpha(128); // 50% transparent
    }
    public boolean contains(float x, float y) {
        return bounds.contains((int)x, (int)y);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null, bounds, paint);
    }

    public boolean isPressed(int x, int y) {
        return bounds.contains(x, y);
    }
}

