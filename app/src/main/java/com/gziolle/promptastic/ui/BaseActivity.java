package com.gziolle.promptastic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void showProgressBar(ProgressBar progressBar){
        if(progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    void hideProgressBar(ProgressBar progressBar){
        if(progressBar != null){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}