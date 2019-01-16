package com.gziolle.promptastic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.gziolle.promptastic.R;
import com.gziolle.promptastic.firebase.FirebaseManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        routeToActivity();
        finish();
    }

    private void routeToActivity(){
        Intent intent;
        if (FirebaseManager.getInstance().isUserLoggedIn()){
            intent = new Intent(this, MainActivity.class);
        } else{
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
    }
}
