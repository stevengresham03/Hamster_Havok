package com.scgiii.hamsterhavok;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SettingsFragment extends DialogFragment {
    private SwitchCompat musicToggle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        musicToggle = view.findViewById(R.id.music_toggle);

        //this gets the current setting from SharedPreferences and then sets it to that stored val
        SharedPreferences preferences = requireActivity().getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        musicToggle.setChecked(preferences.getBoolean("music_on", true));

        //listener for the toggle
        musicToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //updates the preference wth the new value of isChecked
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("music_on", isChecked);
            editor.apply();

            Intent intent = new Intent("music_setting_changed");
            //new state is extra data
            intent.putExtra("music_on", isChecked);
            //sends out that local broadcast
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
        });

        return view;
    }

}
