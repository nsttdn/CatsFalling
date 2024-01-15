package com.example.fallingcats;

import android.app.Activity;
import android.content.*;
import android.graphics.*;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import androidx.annotation.NonNull;
import java.util.*;
import java.util.stream.Collectors;

public class FallingCatsGame extends SurfaceView implements Runnable {
    private Thread thread;
    private Boolean isThreadRunning;
    private Bitmap backgroundImage;
    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint = new Paint();
    private Boolean paused = false;
    private int score = 0;
    private final int NUM_BLOCK_HEIGHT = 10;
    private int blockSize;
    private int NUM_BLOCK_WIDTH;
    private Cats cats;
    private Bed bed;
    private long lastUpdateTime;
    private int fallingSpeed = 320;
    private GestureDetector gestureDetector;
    private boolean touchLeft;
    private boolean touchRight;
    private Context context;

    private Button pauseButton;
    private Button restartButton;
    private Button continueButton;

    FallingCatsGame(Context context, Point screenSize) {
        super(context);
        this.context = context;
        initializeGame(screenSize);
    }

    private void initializeGame(Point screenSize) {
        blockSize = screenSize.y / NUM_BLOCK_HEIGHT;
        NUM_BLOCK_WIDTH = screenSize.x / blockSize;

        backgroundImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        backgroundImage = Bitmap.createScaledBitmap(backgroundImage, screenSize.x, screenSize.y, false);

        cats = new Cats(context, NUM_BLOCK_HEIGHT, NUM_BLOCK_WIDTH, blockSize);
        cats.reset();
        bed = new Bed(context, NUM_BLOCK_HEIGHT, NUM_BLOCK_WIDTH, blockSize, getHeight());

        lastUpdateTime = System.currentTimeMillis();
        surfaceHolder = getHolder();

        gestureDetector = new GestureDetector(context, getGestureListener());
        setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            handleTouchEvents(event);
            return true;
        });

        pauseButton = ((Activity) context).findViewById(R.id.pauseButton);
        restartButton = ((Activity) context).findViewById(R.id.restartButton);
        continueButton = ((Activity) context).findViewById(R.id.resumeButton);
        hideButtonsOnResume();

        setButtonListeners();
    }

    private GestureDetector.OnGestureListener getGestureListener() {
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
                handleSingleTap();
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

    private void handleTouchEvents(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            touchLeft = event.getX() < getWidth() / 2;
            touchRight = event.getX() >= getWidth() / 2;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            touchLeft = false;
            touchRight = false;
        }
    }

    private void setButtonListeners() {
        if (pauseButton != null) {
            pauseButton.setOnClickListener(v -> {
                paused = true;
                showButtonsOnPause();
            });
        }

        if (restartButton != null) {
            restartButton.setOnClickListener(v -> restartGame());
        }

        if (continueButton != null) {
            continueButton.setOnClickListener(v -> {
                paused = false;
                hideButtonsOnResume();
            });
        }
    }

    private void handleSingleTap() {
        if (paused) {
            paused = false;
            score = 0;
            cats.reset();
        }
    }

    @Override
    public void run() {
        while (isThreadRunning) {
            update();
            draw();
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawBitmap(backgroundImage, 0, 0, paint);
            if (!paused) {
                cats.drawSnake(canvas, paint);
                //Log.d("Bed", "x: " + bed.getX() + ", y: " + bed.getY());
                bed.drawBed(canvas, paint);
            } else {
                paint.setTextSize(250);
                paint.setColor(Color.BLACK);
                canvas.drawText("tap to play", 250, 250, paint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastUpdateTime;
        int distanceToFall = (int) (fallingSpeed * (elapsedTime / 1000.0));

        cats.getPositionOfCats().y += distanceToFall;

        if (cats.getPositionOfCats().y >= (NUM_BLOCK_HEIGHT * blockSize)) {
            cats.reset();
        }

        if (cats.checkCollisionWithBed(bed)) {
            score++;
            fallingSpeed += 20;
            Log.d("score", "score:" + score);
            Log.d("fallingSpeed", "fallingSpeed:" + fallingSpeed);
        }

        if (cats.checkCollisionWithBottom(getHeight()) && score > 1) {
            updateHighScore(score);
            Intent intent = new Intent(context, LossActivity.class);
            intent.putExtra("LAST_SCORE", score);
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

    public void onResume() {
        isThreadRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void onPause() {
        isThreadRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateHighScore(int score) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Set<String> scoreSet = prefs.getStringSet("highScores", new HashSet<>());
        List<Integer> highScores = new ArrayList<>();

        for (String scoreStr : scoreSet) {
            highScores.add(Integer.parseInt(scoreStr));
        }

        highScores.add(score);
        Collections.sort(highScores, Collections.reverseOrder());

        if (highScores.size() > 5) {
            highScores = highScores.subList(0, 5);
        }

        Set<String> updatedScoreSet = new HashSet<>(highScores.stream().map(String::valueOf).collect(Collectors.toSet()));
        editor.putStringSet("highScores", updatedScoreSet);
        editor.apply();
    }

    private void showButtonsOnPause() {
        restartButton.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.VISIBLE);
        pauseButton.setVisibility(View.GONE);
    }

    private void hideButtonsOnResume() {
        restartButton.setVisibility(View.GONE);
        continueButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    private void restartGame() {
        paused = false;
        hideButtonsOnResume();
    }
}
