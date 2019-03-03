/*
 * Copyright (C) 2019 Guilherme Ziolle
 */

package com.gziolle.promptastic.ui;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.gziolle.promptastic.R;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;
import com.gziolle.promptastic.interfaces.FirebaseResultInterface;
import com.gziolle.promptastic.util.Constants;

/*
 * Handles User sign up to Firebase Authentication servers
 */

public class SignupActivity extends BaseActivity implements FirebaseResultInterface {

    @BindView(R.id.et_signup_display_name)
    EditText mNameEditText;

    @BindView(R.id.et_signup_user)
    EditText mUserEditText;

    @BindView(R.id.et_signup_password)
    EditText mPasswordEditText;

    @BindView(R.id.signup_progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.signup_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        mNameEditText.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(Constants.NAME_MAX_LENGTH) });
    }

    @OnClick(R.id.bt_signup)
    public void signupUser() {
        String displayName = mNameEditText.getText().toString();
        String email = mUserEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            showProgressBar(mProgressBar);
            FirebaseAuthManager.getInstance().createUser(this, email, password, displayName, this);
        } else {
            if(TextUtils.isEmpty(email)){
                mUserEditText.setError(getString(R.string.login_username_error));
            }
            if(TextUtils.isEmpty(password)){
                mPasswordEditText.setError(getString(R.string.login_password_error));
            }
        }
    }

    @Override
    public void onFirebaseResult(boolean isSuccessful, Exception exception) {
        hideProgressBar(mProgressBar);
        if(isSuccessful){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.signup_dialog_title))
                    .setMessage(getString(R.string.signup_dialog_message))
                    .setPositiveButton(getString(R.string.signup_dialog_ok), (dialog, which) -> finish());
            AlertDialog dialog = builder.create();
            dialog.show();
        } else{
            Snackbar snackbar = Snackbar.make(mCoordinatorLayout, exception.getMessage(), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
}
