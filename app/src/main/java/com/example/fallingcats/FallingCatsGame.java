package com.example.fallingcats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class FallingCatsGame extends SurfaceView implements Runnable {
    private Thread thread;
    private Boolean isThreadRunning;
    private Bitmap backgroundImage;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint = new Paint();
    private Boolean paused = false;
    private int score = 0;
    private final int NUM_BLOCK_HIGHT = 10;
    private int blockSize;
    private int NUM_BLOCK_WIDE;
    private Cats cats;
    private Bed bed;
    private long lastUpdateTime;
    private int fallingSpeed = 320;
    GestureDetector gestureDetector;

    private boolean touchLeft;
    private boolean touchRight;
    private Context context;
    FallingCatsGame(Context context, Point screenSize){
        super(context);
        this.context = context;
        blockSize = screenSize.y/NUM_BLOCK_HIGHT;
        NUM_BLOCK_WIDE = screenSize.x/blockSize;

        backgroundImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        backgroundImage = Bitmap.createScaledBitmap(backgroundImage, screenSize.x, screenSize.y, false);

        cats = new Cats(context, NUM_BLOCK_HIGHT, NUM_BLOCK_WIDE, blockSize);
        cats.reset();
        bed = new Bed(context, NUM_BLOCK_HIGHT, NUM_BLOCK_WIDE, blockSize,  getHeight());

        lastUpdateTime = System.currentTimeMillis();
        surfaceHolder = getHolder();

        gestureDetector = new GestureDetector(context, getGestureLiner());
        setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                touchLeft = event.getX() < getWidth() / 2;
                touchRight = event.getX() >= getWidth() / 2;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                touchLeft = false;
                touchRight = false;
            }
            return true;
        });
    }

    @Override
    public void run(){
        while(isThreadRunning) {
            update();
            draw();
        }
    }

    private void draw(){
        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(backgroundImage, 0, 0, paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(120);
            canvas.drawText(""+ score, 998, 120,paint);
            if(!paused){
                cats.drawSnake(canvas, paint);
                //Log.d("Bed", "x: " + bed.getX() + ", y: " + bed.getY());
                bed.drawBed(canvas, paint);
            }
            else{
                paint.setTextSize(250);
                paint.setColor(Color.BLACK);
                canvas.drawText("tap to play", 250,250, paint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    public void update() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastUpdateTime;
        int distanceToFall = (int) (fallingSpeed * (elapsedTime / 1000.0));

        cats.getPositionOfCats().y += distanceToFall;

        if (cats.getPositionOfCats().y >= (NUM_BLOCK_HIGHT * blockSize)) {
            cats.reset();
        }

        if(cats.checkCollisionWithBed(bed)){
            score ++;
            fallingSpeed+=20;
            Log.d("score", "score:" + score);
            Log.d("fallingSpeed", "fallingSpeed:" + fallingSpeed);
        }

        if(cats.checkCollisionWithBottom(getHeight()) && score>1){
            Intent intent = new Intent(context, LossActivity.class);
            context.startActivity(intent);
            paused = true;
        }
        lastUpdateTime = currentTime;
        if (touchLeft) {
            bed.moveBedLeft();
        } else if (touchRight) {
            bed.moveBedRight();
        }

        bed.updateBedPosition(getWidth());
    }
    private GestureDetector.OnGestureListener getGestureLiner(){
        return new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent e) {
                return false;
            }
            @Override
            public void onShowPress(@NonNull MotionEvent e) {

            }
            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent e) {
                if(paused){
                    paused = false;
                    score = 0;
                    cats.reset();
                }
                return false;
            }
            @Override
            public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }
            @Override
            public void onLongPress(@NonNull MotionEvent e) {

            }
            @Override
            public boolean onFling(@NonNull MotionEvent downEvent, @NonNull MotionEvent upEvent, float velocityX, float velocityY) {
                return false;
            }
        };
    }
    public void onResume(){
        isThreadRunning = true;
        thread = new Thread(this);
        thread.start();
    }
    public void onPause(){
        isThreadRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
