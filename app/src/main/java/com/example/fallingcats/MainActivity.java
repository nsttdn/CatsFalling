package com.example.fallingcats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button shareButton = findViewById(R.id.shareButton);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnShare();
            }
        });
        Button rateButton = findViewById(R.id.rateButton);

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAppRating();
            }
        });
    }

    public void btnPlay(View v){
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }
    public void btnScore(View v){
        Intent intent = new Intent(this, ScoreActivity.class);
        startActivity(intent);
    }
        private void btnShare() {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This game cool\n*Link to PlayMarket*" );
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
    private void openAppRating() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        goToMarket.addFlags(flags);

        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Uri webUri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
            Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
            startActivity(webIntent);
        }
    }
    public void exitGame(View view) {
        finish();
    }

}