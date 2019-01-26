package com.gziolle.promptastic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gziolle.promptastic.firebase.FirebaseAuthManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(() -> {
            routeToActivity();
            finish();
        }, 3000);
    }

    private void routeToActivity(){
        Intent intent;
        if (FirebaseAuthManager.getInstance().isUserLoggedIn()){
            intent = new Intent(this, MainActivity.class);
        } else{
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
    }
}
