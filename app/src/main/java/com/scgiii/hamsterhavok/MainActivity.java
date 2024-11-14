package com.scgiii.hamsterhavok;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GameViews gameViews;
    private View pauseOverlay;
    private boolean isPauseOverlayAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the main layout
        setContentView(R.layout.activity_main);

        // Initialize GameViews and add it to the main container
        gameViews = new GameViews(this);
        FrameLayout gameContainer = findViewById(R.id.mainContainer);
        gameContainer.addView(gameViews, 0); // Add GameViews behind all UI elements

        // Initialize pause overlay
        initializePauseOverlay();

        // Initialize button logic
        setupButtons();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameViews != null) {
            gameViews.pauseGame();
        }
    }
}
