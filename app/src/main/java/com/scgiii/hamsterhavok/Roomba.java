package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

//import com.scgiii.hamsterhavok.PlayerObject;

public class Roomba extends PlayerObject {

    public Roomba(Context context, Bitmap bitmap, float x, float y) {
        super(bitmap, x, y);

        // x = 0; // Initialize position
        //y = 0; // Initialize position
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

}
