package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public abstract class ObstacleObject {
    private float obstacleX,obstacleY;
    private int obstacleWidth, obstacleHeight;
    private Bitmap bitmap;
    public int screenHeight, screenWidth;

    public ObstacleObject(Context context, Bitmap bitmap, float x, float y){
        //possibly width and height later
        this.obstacleX = x;
        this.obstacleY = y;
        this.obstacleWidth = bitmap.getWidth() / 5;
        this.obstacleHeight = bitmap.getHeight() / 5;
        this.bitmap = Bitmap.createScaledBitmap(bitmap, obstacleWidth, obstacleHeight, true);
        this.screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        this.screenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    private boolean isColliding(float hamsterX, float hamsterY, float hamsterWidth, float hamsterHeight) {
        //does this even work??? haven't tested... (yes)
        return hamsterX < obstacleX + obstacleWidth &&
                hamsterX + hamsterWidth > obstacleX &&
                hamsterY < obstacleY + obstacleHeight &&
                hamsterY + hamsterHeight > obstacleY;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, obstacleX, obstacleY, null);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}