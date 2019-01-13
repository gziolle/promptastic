package com.gziolle.promptastic.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gziolle.promptastic.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(
                new ScriptListFragment(), "test"
        ).commit();
    }
}
