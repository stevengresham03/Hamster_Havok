package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Background {
    private Bitmap bitmap;
    private float offsetX1, offsetX2;
    private float scrollSpeed;
    static int SCREEN_WIDTH, SCREEN_HEIGHT;

    public Background(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        offsetX1 = 0;
        offsetX2 = bitmap.getWidth();
        scrollSpeed = 5;
    }

    public void update() {
        //background moves left or right depending on action of hamster
        scrollSpeed = 5;
        offsetX1 -= scrollSpeed;
        offsetX2 -= scrollSpeed;

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

    //return background width
    public int getBackgroundWidth(){
        return bitmap.getWidth();
    }

    //return background height
    public int getBackgroundHeight(){
        return bitmap.getHeight();
    }

}