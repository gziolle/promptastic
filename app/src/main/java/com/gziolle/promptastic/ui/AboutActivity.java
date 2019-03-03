/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.ui;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.gziolle.promptastic.R;

/*
 * Handles App and Open Source Licenses information
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.tv_about_open_source)
    void goToOpenSourceLicenses(){
        startActivity(new Intent(this, OssLicensesMenuActivity.class));
    }
}
