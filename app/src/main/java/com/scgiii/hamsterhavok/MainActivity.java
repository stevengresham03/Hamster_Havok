package com.scgiii.hamsterhavok;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MainActivity extends AppCompatActivity {
    //The MediaaPlayer instance is for the background music
    private MediaPlayer mediaPlayer;
    //Broadcast receiver so that changes to the music setting are noted
    private BroadcastReceiver musicSettingReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(new GameViews(this));

        /*
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         */
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

    @Override
    protected void onResume(){
        super.onResume();
        //resumes music if it's not playing
        SharedPreferences preferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        boolean musicOn = preferences.getBoolean("music_on", true);
        if (musicOn && !mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        //pauses music when app paused
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mediaPlayer != null){ //yarg hrow
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}