package com.gziolle.promptastic.ui;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.widget.EditText;

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
    }

    @OnClick(R.id.bt_login)
    public void loginUser() {
        String username = mUserEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        FirebaseAuthManager.getInstance().loginUser(this, username, password);
    }
}
