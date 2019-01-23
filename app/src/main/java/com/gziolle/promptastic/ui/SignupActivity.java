package com.gziolle.promptastic.ui;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.gziolle.promptastic.R;
import com.gziolle.promptastic.firebase.FirebaseAuthManager;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.et_signup_user)
    EditText mUserEditText;

    @BindView(R.id.et_signup_password)
    EditText mPasswordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_signup)
    public void signupUser() {
        String email = mUserEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            FirebaseAuthManager.getInstance().createUser(this, email, password);
        } else {
            Toast.makeText(this, "Fill in the blanks", Toast.LENGTH_SHORT).show();
        }
    }
}
