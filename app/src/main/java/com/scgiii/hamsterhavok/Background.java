package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class Background {
    private final Bitmap bitmap;
    private float offsetX1, offsetX2;
    private float scrollSpeed;
    static int SCREEN_WIDTH, SCREEN_HEIGHT;
    private final Context context;
    private int screenHeight;



    public Background(Context context) {
        this.context = context;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        offsetX1 = 0;
        offsetX2 = bitmap.getWidth();
        scrollSpeed = 5;
    }
    public float getOffsetX1(){
        return offsetX1;
    }

    public float getOffsetX2(){
        return offsetX2;
    }



    public void getScreenDimensions() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;


        // Use screenHeight and screenWidth as needed
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
        getScreenDimensions();
       // Log.d("background","screen height: " + screenHeight );
        double tempHeight = screenHeight * .5;
        screenHeight = (int) Math.floor(tempHeight);
        Log.d("background","offset2: " + offsetX2);

        canvas.drawBitmap(bitmap, offsetX1, -screenHeight, null);
        canvas.drawBitmap(bitmap, offsetX2, -screenHeight, null);
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