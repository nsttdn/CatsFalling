package com.example.fallingcats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LossActivity extends AppCompatActivity {
    private int lastScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss);

        Intent intent = getIntent();
        if (intent != null) {
            lastScore = intent.getIntExtra("LAST_SCORE", 0);
        }

        TextView scoreTextView = findViewById(R.id.textView);
        scoreTextView.setText("Your Score: " + lastScore);

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