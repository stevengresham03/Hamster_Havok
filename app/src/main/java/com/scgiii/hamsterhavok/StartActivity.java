package com.scgiii.hamsterhavok;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class StartActivity extends AppCompatActivity {
    private TextView highScoreValue;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ImageButton startButton = findViewById(R.id.start_button);
        ImageButton settingsButton = findViewById(R.id.settings_button);
        ImageButton howToPlayButton = findViewById(R.id.how_to_play_button);

        // Initialize the high score display
        highScoreValue = findViewById(R.id.highScoreValue);

        //listeners
        startButton.setOnClickListener(v -> startGame());
        settingsButton.setOnClickListener(v -> openSettings());
        howToPlayButton.setOnClickListener(view -> openHowToPlay());
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Getting high score from sharedpref
        SharedPreferences preferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        int highScore = preferences.getInt("high_score", 0);

        //Setting the val
        highScoreValue.setText(String.valueOf(highScore));

        // Updating the high score font
        Typeface customFont = ResourcesCompat.getFont(this, R.font.americancapt);
        if (customFont != null){
            highScoreValue.setTypeface(customFont);
        }
    }

    private void startGame(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    private void openSettings(){
        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.show(getSupportFragmentManager(), "SettingsFragment");
    }
    private void openHowToPlay(){
        HowToPlayFragment howToPlayFragment = new HowToPlayFragment();
        howToPlayFragment.show(getSupportFragmentManager(), "HowToPlayFragment");
    }

}
