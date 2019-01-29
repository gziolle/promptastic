package com.gziolle.promptastic.ui;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gziolle.promptastic.R;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;
import com.gziolle.promptastic.interfaces.FirebaseResultInterface;

public class SignupActivity extends BaseActivity implements FirebaseResultInterface {

    @BindView(R.id.et_signup_user)
    EditText mUserEditText;

    @BindView(R.id.et_signup_password)
    EditText mPasswordEditText;

    @BindView(R.id.signup_progress_bar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
    }

    @OnClick(R.id.bt_signup)
    public void signupUser() {
        String email = mUserEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            showProgressBar(mProgressBar);
            FirebaseAuthManager.getInstance().createUser(this, email, password, this);
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
    public void onFirebaseResult(boolean isSuccessful) {
        hideProgressBar(mProgressBar);
        if(isSuccessful){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage("Your account has been created. Please, login to continue")
                    .setPositiveButton("OK", (dialog, which) -> {
                        finish();
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else{
            Toast.makeText(this, "Please, try again", Toast.LENGTH_SHORT).show();
        }
    }
}
