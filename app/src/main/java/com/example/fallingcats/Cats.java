package com.example.fallingcats;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import android.os.Handler;

public class Cats {
    private Point positionOfCats = new Point();
    private Bitmap neko1Bitmap;
    private Bitmap neko2Bitmap;
    private Bitmap neko3Bitmap;
    private Bitmap neko4Bitmap;
    private Bitmap neko5Bitmap;
    private int BLOCK_SIZE;
    private int NUM_BLOCK_WIDE;
    private int NUM_BLOCK_HIGH;
    private int[] catImages = {
            R.drawable.neko1,
            R.drawable.neko2,
            R.drawable.neko3,
            R.drawable.neko4,
            R.drawable.neko5
    };
    private Bitmap selectedCatBitmap;


    Cats(Context context, int NUM_BLOCK_HIGH, int NUM_BLOCK_WIDE, int BLOCK_SIZE){
        this.BLOCK_SIZE = BLOCK_SIZE;
        this.NUM_BLOCK_HIGH = NUM_BLOCK_HIGH;
        this.NUM_BLOCK_WIDE = NUM_BLOCK_WIDE;
        positionOfCats.x = 0;
        positionOfCats.y = 0;

        neko1Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.neko1);
        neko1Bitmap = Bitmap.createScaledBitmap(neko1Bitmap,BLOCK_SIZE, BLOCK_SIZE,true);

        neko2Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.neko2);
        neko2Bitmap = Bitmap.createScaledBitmap(neko2Bitmap,BLOCK_SIZE, BLOCK_SIZE,true);

        neko3Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.neko3);
        neko3Bitmap = Bitmap.createScaledBitmap(neko3Bitmap,BLOCK_SIZE, BLOCK_SIZE,true);

        neko4Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.neko4);
        neko4Bitmap = Bitmap.createScaledBitmap(neko4Bitmap,BLOCK_SIZE, BLOCK_SIZE,true);

        neko5Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.neko5);
        neko5Bitmap = Bitmap.createScaledBitmap(neko5Bitmap,BLOCK_SIZE, BLOCK_SIZE,true);

    }

    void setCatsPosition(){
        Random random = new Random();

        positionOfCats.x = random.nextInt(NUM_BLOCK_WIDE);
        positionOfCats.y = NUM_BLOCK_HIGH;

        positionOfCats.x *= BLOCK_SIZE;
    }
    public Point getPositionOfCats(){
        return positionOfCats;
    }
    void setRandomCats(){
        int randomIndex = new Random().nextInt(catImages.length);
        switch (randomIndex) {
            case 0:
                selectedCatBitmap = neko1Bitmap;
                break;
            case 1:
                selectedCatBitmap = neko2Bitmap;
                break;
            case 2:
                selectedCatBitmap = neko3Bitmap;
                break;
            case 3:
                selectedCatBitmap = neko4Bitmap;
                break;
            case 4:
                selectedCatBitmap = neko5Bitmap;
                break;
            default:
                // Handle unexpected index, or provide a default cat image
                selectedCatBitmap = neko1Bitmap;
                break;
        }
    }

    public Bitmap getSelectedCatBitmap() {
        return selectedCatBitmap;
    }
    void moveCats(){
        positionOfCats.y -= 1;
    }
    void drawSnake(Canvas canvas, Paint paint){
        canvas.drawBitmap(selectedCatBitmap, positionOfCats.x, positionOfCats.y, paint);
    }
    void reset(){
        setCatsPosition();
        setRandomCats();
    }
    boolean checkCollisionWithBed(Bed bed) {
        Point catPosition = getPositionOfCats();
        if (catPosition.y >= (bed.getY() - neko1Bitmap.getHeight()) &&
                catPosition.y <= bed.getY() &&
                catPosition.x >= bed.getX() &&
                catPosition.x <= (bed.getX() + bed.getBedBitmap().getWidth())) {
            // Cat touched the bed, increase the score
            reset(); // Reset the cat's position
            return true;
        }
        return false;
    }
    boolean checkCollisionWithBottom(int screenHeight) {
        Point catPosition = getPositionOfCats();
        return catPosition.y >= screenHeight;
    }
}






