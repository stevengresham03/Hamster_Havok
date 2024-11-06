package com.scgiii.hamsterhavok;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class Background {
    private Bitmap bitmap;
    private float offsetX1, offsetX2;
    private float scrollSpeed;
    static int SCREEN_WIDTH, SCREEN_HEIGHT;

    public Background(Context context) {
        //setScreenSize(context);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        // bitmap = Bitmap.createScaledBitmap(bitmap, screenX, screenY, false );
        offsetX1 = 0;
        offsetX2 = bitmap.getWidth();
        scrollSpeed = 5;
    }

    public void update() {
        //background moves left or right depending on action of hamster
        float scrollSpeed = 5;
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

    //setting screen size so image takes up all the screen
    /*
    private static void setScreenSize(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        SCREEN_WIDTH = width;
        SCREEN_HEIGHT = height;
    }

    public Bitmap scaleImage(Bitmap bitmap){
        float widthHeightRatio = (float) getBackgroundWidth() / getBackgroundHeight();
        //We're multiplying widthHeightRatio with screenHeight to get scaled width of the bitmap
        //then call createScaledBitmap() to create a new bitmap, scaled from an existing bitmap when possible
        int backgroundScaledWidth = (int) widthHeightRatio * SCREEN_HEIGHT;

        return Bitmap.createScaledBitmap(bitmap, backgroundScaledWidth,SCREEN_HEIGHT, false);
    }
    */
}