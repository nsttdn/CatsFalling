package com.example.fallingcats;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class PlayActivity extends AppCompatActivity {
    FallingCatsGame fallingCatsGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        fallingCatsGame = new FallingCatsGame(this, size);
        setContentView(fallingCatsGame);
    }
    @Override
    protected void onPause(){
        super.onPause();
        fallingCatsGame.onPause();
    }
    @Override
    protected void onResume(){
        super.onResume();
        fallingCatsGame.onResume();
    }
}