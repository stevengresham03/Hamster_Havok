package com.scgiii.hamsterhavok;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ImageButton startButton = findViewById(R.id.start_button);
        ImageButton settingsButton = findViewById(R.id.settings_button);
        ImageButton howToPlayButton = findViewById(R.id.how_to_play_button);

        //listeners
        startButton.setOnClickListener(v -> startGame());
        settingsButton.setOnClickListener(v -> openSettings());
        howToPlayButton.setOnClickListener(view -> openHowToPlay());
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