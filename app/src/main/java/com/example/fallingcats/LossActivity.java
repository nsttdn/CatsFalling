package com.example.fallingcats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LossActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss);
    }
    public void btnRestart(View v){
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }
    public void btnMenu(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}