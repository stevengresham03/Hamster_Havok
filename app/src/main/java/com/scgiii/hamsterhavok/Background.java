package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Background {
    private final Bitmap bitmap;
    private float offsetX1, offsetX2;
    public static float scrollSpeed = 300; // Pixels per second

    public Background(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        offsetX1 = 0;
        offsetX2 = bitmap.getWidth();
    }

    public void update(float dt) {
        offsetX1 -= scrollSpeed * dt;
        offsetX2 -= scrollSpeed * dt;

        if (offsetX1 <= -bitmap.getWidth()) {
            offsetX1 = offsetX2 + bitmap.getWidth();
        }
        if (offsetX2 <= -bitmap.getWidth()) {
            offsetX2 = offsetX1 + bitmap.getWidth();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, offsetX1, 0, null);
        canvas.drawBitmap(bitmap, offsetX2, 0, null);
    }
}
