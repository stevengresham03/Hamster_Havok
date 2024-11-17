package com.scgiii.hamsterhavok;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;


public class MainActivity extends AppCompatActivity {
    //The MediaaPlayer instance is for the background music
    private MediaPlayer mediaPlayer;
    //Broadcast receiver so that changes to the music setting are noted
    private BroadcastReceiver musicSettingReceiver;

    private GameViews gameViews;
    private View pauseOverlay;
    private View deathOverlay;
    private boolean isPauseOverlayAdded = false;

    private static final String PREFS_NAME = "GamePrefs";
    private static final String HIGH_SCORE_KEY = "high_score";
    //private FrameLayout gameContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the main layout
        setContentView(R.layout.activity_main);

        // Initialize GameViews and add it to the main container
        gameViews = new GameViews( this,this);
        FrameLayout gameContainer = findViewById(R.id.mainContainer);
        gameContainer.addView(gameViews, 0); // Add GameViews behind all UI elements

        // Initialize pause & death overlay
        initializePauseOverlay();
        initializeDeathOverlay();

        // Initialize button logic
        setupButtons();
        //Initializing the mediaplayer
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
        mediaPlayer.setLooping(true);

        //SharedPreferences stores and saves detailed system data
        SharedPreferences preferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        boolean musicOn = preferences.getBoolean("music_on", true);
        //If the preference is set for music then it starts it
        if (musicOn) {
            mediaPlayer.start();
        }

        //broadcastreceiver for changes in the music settings yessiree
        musicSettingReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //checking
                boolean musicOn = intent.getBooleanExtra("music_on", true);
                if (musicOn) {
                    if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                } else {
                    if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(musicSettingReceiver, new IntentFilter("music_setting_changed"));
    }

    private void setupButtons() {
        // Pause button logic
        ImageButton pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(v -> {
            showPauseScreen(); // Call the method to show the pause overlay
        });

        // Left button logic
        ImageButton leftButton = findViewById(R.id.leftButton);
        leftButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                gameViews.onLeftButtonPressed();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                gameViews.stopMovement();
            }
            return true;
        });

        // Right button logic
        ImageButton rightButton = findViewById(R.id.rightButton);
        rightButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                gameViews.onRightButtonPressed();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                gameViews.stopMovement();
            }
            return true;
        });

        // Jump button logic
        ImageButton jumpButton = findViewById(R.id.jumpButton);
        jumpButton.setOnClickListener(v -> gameViews.onJumpButtonPressed());
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mediaPlayer != null) { //yarg hrow
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void initializePauseOverlay() {
        if (pauseOverlay == null) {
            // Inflate the pause screen layout
            pauseOverlay = getLayoutInflater().inflate(R.layout.pause_screen, null);

            // Set up the resume button logic
            View resumeButton = pauseOverlay.findViewById(R.id.resumeButton);
            resumeButton.setOnClickListener(v -> hidePauseScreen());

            // Add the pause overlay to the main container
            FrameLayout gameContainer = findViewById(R.id.mainContainer);
            gameContainer.addView(pauseOverlay);

            // Initially hide the pause overlay
            pauseOverlay.setVisibility(View.GONE);
        }
    }


    private void showPauseScreen() {
        if (pauseOverlay != null) {
            pauseOverlay.setVisibility(View.VISIBLE);
        }
        gameViews.pauseGame();
    }

    private void hidePauseScreen() {
        if (pauseOverlay != null) {
            pauseOverlay.setVisibility(View.GONE);
        }
        gameViews.resumeGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameViews != null) {
            gameViews.resumeGame();
            }
           //resumes music if it's not playing
        SharedPreferences preferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        boolean musicOn = preferences.getBoolean("music_on", true);
        if (musicOn && !mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameViews != null) {
            gameViews.pauseGame();
        }
      //pauses music when app paused
       if (mediaPlayer.isPlaying()){
         
            mediaPlayer.pause();
        }
    }




    public void playerDeath(int finalScore) {
        Log.d("MainActivity", "playerDeath() called.");
        runOnUiThread(() -> {
            showDeathScreen(finalScore);
        });
    }

    private void initializeDeathOverlay() {
        if (deathOverlay == null) {
            deathOverlay = getLayoutInflater().inflate(R.layout.death_screen, null);

            // Set up the restart button logic
            View restartButton = deathOverlay.findViewById(R.id.restartButton);
            restartButton.setOnClickListener(v -> restartGame());

            // Set up the exit button logic
            View exitButton = deathOverlay.findViewById(R.id.exitButton);
            exitButton.setOnClickListener(v -> finish());

            // Add the pause overlay to the main container
            FrameLayout gameContainer = findViewById(R.id.mainContainer);
            gameContainer.addView(deathOverlay);

            // Initially hide the death overlay
            deathOverlay.setVisibility(View.GONE);
        }
    }


    private void showDeathScreen(int finalScore) {
        Log.d("MainActivity", "Showing death screen.");
        if (deathOverlay == null) {
            initializeDeathOverlay();
        }

        // Get the current high score
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int highScore = prefs.getInt(HIGH_SCORE_KEY, 0);

        // Update high score if necessary
        if (finalScore > highScore) {
            highScore = finalScore;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(HIGH_SCORE_KEY, highScore);
            editor.apply();

            // Show congratulations popup
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New High Score!")
                    .setMessage("Congratulations! You've set a new high score of " + highScore + "!")
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();

        }

        TextView finalScoreText = deathOverlay.findViewById(R.id.finalScoreText);
        TextView highScoreText = deathOverlay.findViewById(R.id.highScoreText);

        finalScoreText.setText("Final Score: " + finalScore);
        highScoreText.setText("High Score: " + highScore);

        deathOverlay.setVisibility(View.VISIBLE);
    }

    private void restartGame() {
        if (deathOverlay != null) {
            deathOverlay.setVisibility(View.GONE);
        }

        // Remove the old GameViews
        FrameLayout gameContainer = findViewById(R.id.mainContainer);
        gameContainer.removeView(gameViews);

        // Create and add a new GameViews
        gameViews = new GameViews(this, this);
        gameContainer.addView(gameViews, 0);

        // Reset background scroll speed
        gameViews.getGameBackground().resetScrollSpeed();

        // Reset the player's position
        gameViews.getPlayer().setPosition(
                gameViews.getWidth() / 2f,
                gameViews.getHeight() - gameViews.getPlayer().getHeight()
        );

        // Resume the game
        gameViews.resumeGame();
    }



}
