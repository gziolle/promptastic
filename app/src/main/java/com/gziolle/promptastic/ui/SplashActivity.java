/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.gziolle.promptastic.firebase.FirebaseAuthManager;
import com.gziolle.promptastic.util.Constants;

/**
 * Displays an intro image to the user
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(() -> {
            routeToActivity();
            finish();
        }, 500);
    }

    private void routeToActivity() {
        Intent intent;
        if (FirebaseAuthManager.getInstance().isUserLoggedIn()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            Boolean isFirstUse = preferences.getBoolean(Constants.FIRST_USE_KEY, true);

            if(isFirstUse){
               intent = new Intent(this, IntroActivity.class);
               SharedPreferences.Editor editor = preferences.edit();
               editor.putBoolean(Constants.FIRST_USE_KEY, false);
               editor.apply();
            } else{
                intent = new Intent(this, LoginActivity.class);
            }
        }
        startActivity(intent);
    }
}
