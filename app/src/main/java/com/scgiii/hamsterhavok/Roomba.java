package com.scgiii.hamsterhavok;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Roomba extends PlayerObject {

    public Roomba(Context context, Bitmap bitmap, float x, float y) {
        super(bitmap, x, y);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

}
