package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Background {
    private Bitmap bitmap;
    private float offsetX1;
    private float offsetX2;

    public Background(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        offsetX1 = 0;
        offsetX2 = bitmap.getWidth();
    }

    public void update(boolean isMovingForward, boolean isMovingBackward) {
        if (isMovingBackward) {
            offsetX1 -= 20;
            offsetX2 -= 20;
        }
        if (isMovingForward) {
            offsetX1 += 20;
            offsetX2 += 20;
        }

        // Reset positions if out of bounds
        if (offsetX1 <= -bitmap.getWidth()) {
            offsetX1 = offsetX2 + bitmap.getWidth();
        } else if (offsetX1 >= bitmap.getWidth()) {
            offsetX1 = offsetX2 - bitmap.getWidth();
        }

        if (offsetX2 <= -bitmap.getWidth()) {
            offsetX2 = offsetX1 + bitmap.getWidth();
        } else if (offsetX2 >= bitmap.getWidth()) {
            offsetX2 = offsetX1 - bitmap.getWidth();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, offsetX1, -600, null);
        canvas.drawBitmap(bitmap, offsetX2, -600, null);
    }

    public void recycle() {
        bitmap.recycle();
    }
}