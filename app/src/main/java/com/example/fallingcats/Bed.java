package com.example.fallingcats;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Bed {
    private Bitmap bedBitmap;
    private float x;  // x-coordinate of the bed's top-left corner
    private float y;  // y-coordinate of the bed's top-left corner
    private float bedSpeed = 15;  // Adjust the speed as needed

    Bed(Context context, int NUM_BLOCK_HIGH, int NUM_BLOCK_WIDE, int BLOCK_SIZE, int screenHeight) {
        bedBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bed);
        bedBitmap = Bitmap.createScaledBitmap(bedBitmap, 2 * BLOCK_SIZE, BLOCK_SIZE, true);

        // Initial position of the bed (centered at the bottom of the screen)
        x = (NUM_BLOCK_WIDE * BLOCK_SIZE - bedBitmap.getWidth()) / 2;
        y = screenHeight + 8*BLOCK_SIZE;

    }

    public float getY() {
        return y;
    }

    public Bitmap getBedBitmap() {
        return bedBitmap;
    }

    public float getX() {
        return x;
    }

    public void updateBedPosition(float screenWidth) {
        // Ensure the bed stays within the screen bounds
        if (x < 0) {
            x = 0;
        } else if (x + bedBitmap.getWidth() > screenWidth) {
            x = screenWidth - bedBitmap.getWidth();
        }
    }

    public void moveBedRight() {
        x += bedSpeed;
    }

    public void moveBedLeft() {
        x -= bedSpeed;
    }

    public void drawBed(Canvas canvas, Paint paint) {
        canvas.drawBitmap(bedBitmap, x, y, paint);
    }
}

