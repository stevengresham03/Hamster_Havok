package com.scgiii.hamsterhavok;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GameLoop gameLoop;
    private GameViews gameViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Initialize game components
        gameViews = new GameViews(this);

        //I think restoring the state seems to be useless. onPause/onResume gets the job done. But keeping it just in case
        if (savedInstanceState != null) {
            gameViews.restoreState(savedInstanceState);
        }

        setContentView(gameViews);

        gameLoop = new GameLoop(gameViews, gameViews.getHolder());
        gameLoop.startLoop(); // Start the game loop here
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gameLoop != null) {
            gameLoop.pauseLoop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameLoop != null) {
            gameLoop.resumeLoop();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (gameViews != null) {
            gameViews.saveState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameLoop != null) {
            gameLoop.stopLoop();
        }
    }
}