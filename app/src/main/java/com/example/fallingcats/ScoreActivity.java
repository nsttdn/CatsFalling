package com.example.fallingcats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Set<String> scoreSet = prefs.getStringSet("highScores", new HashSet<>());

        List<Integer> highScores = new ArrayList<>();
        for (String score : scoreSet) {
            highScores.add(Integer.parseInt(score));
        }

        TextView highScoresTextView = findViewById(R.id.textView3);
        StringBuilder highScoresText = new StringBuilder("High Scores:\n");
        for (int i = 0; i < highScores.size(); i++) {
            highScoresText.append(i + 1).append(". ").append(highScores.get(i)).append("\n");
        }

        highScoresTextView.setText(highScoresText.toString());

    }
    private void displayHighScores() {

    }
}