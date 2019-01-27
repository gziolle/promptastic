package com.gziolle.promptastic.ui;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.gziolle.promptastic.R;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_login_password)
    EditText mPasswordEditText;

    @BindView(R.id.et_login_user)
    EditText mUserEditText;

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
            RelativeLayout layout = new RelativeLayout(this);
            ProgressBar progressBar = new ProgressBar(this,null,android.R.attr.progressBarStyleLarge);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            layout.addView(progressBar,params);

            setContentView(layout);
            FirebaseAuthManager.getInstance().loginUser(this, username, password);
        }

    }

    @OnClick(R.id.tv_signup)
    public void goToSignUpActivity(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
