package com.scgiii.hamsterhavok;

import static android.opengl.ETC1.getHeight;
import static android.opengl.ETC1.getWidth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Player extends PlayerObject{

    public Player(Context context, Bitmap bitmap, float x, float y) {
        super(bitmap, x, y);

        //MIGUEL this is what i was talking about. these two lines don't work here, but did in first github upload.
        //for now, the x an Y are currently hardcoded. We can worry about this later though. I want to get joystick first
        //x = getWidth() / 2 - bitmap.getWidth() / 2;
        //y = getHeight() - bitmap.getHeight(); // Initialize position
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

}