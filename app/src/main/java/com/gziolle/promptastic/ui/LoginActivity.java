package com.gziolle.promptastic.ui;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.gziolle.promptastic.R;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;
import com.gziolle.promptastic.interfaces.FirebaseResultInterface;

public class LoginActivity extends BaseActivity implements FirebaseResultInterface {

    @BindView(R.id.et_login_password)
    EditText mPasswordEditText;

    @BindView(R.id.et_login_user)
    EditText mUserEditText;

    @BindView(R.id.login_progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.login_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    @OnClick(R.id.bt_login)
    public void loginUser() {
        String username = mUserEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            if(TextUtils.isEmpty(username)){
                mUserEditText.setError(getString(R.string.login_username_error));
            }
            if(TextUtils.isEmpty(password)){
                mPasswordEditText.setError(getString(R.string.login_password_error));
            }
        } else{
            showProgressBar(mProgressBar);
            FirebaseAuthManager.getInstance().loginUser(this, username, password, this);
        }

    }

    @OnClick(R.id.tv_signup)
    public void goToSignUpActivity(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFirebaseResult(boolean isSuccessful, Exception exception) {
        hideProgressBar(mProgressBar);
        if (!isSuccessful) {
            Snackbar.make(mCoordinatorLayout, exception.getMessage(), Snackbar.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
